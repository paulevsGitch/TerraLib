package paulevs.terralib.map;

import com.google.common.collect.Maps;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bhcore.noise.OpenSimplexNoise;
import paulevs.terralib.biome.TerraBiome;
import paulevs.terralib.map.picker.BiomePicker;

import java.util.Map;
import java.util.Random;

public class BiomeMap {
	private static final float RAD_INNER = (float) Math.sqrt(3.0) * 0.5F;
	private static final float COEF = 0.25F * (float) Math.sqrt(3.0);
	private static final float COEF_HALF = COEF * 0.5F;
	private static final float SIN = (float) Math.sin(0.4);
	private static final float COS = (float) Math.cos(0.4);
	private static final float[] EDGE_CIRCLE_X;
	private static final float[] EDGE_CIRCLE_Z;
	
	private final Map<ChunkPos, BiomeChunk> chunks = Maps.newConcurrentMap();
	private final BiomePicker picker;
	
	private final OpenSimplexNoise[] noises = new OpenSimplexNoise[2];
	private final byte noiseIterations;
	private final float scale;
	private final int seed;
	private int cacheTicks;
	
	public BiomeMap(long seed, int size, BiomePicker picker) {
		this.picker = picker;
		this.scale = BiomeChunk.scaleMap(size);
		Random random = new Random(seed);
		noises[0] = new OpenSimplexNoise(random.nextInt());
		noises[1] = new OpenSimplexNoise(random.nextInt());
		noiseIterations = (byte) Math.min(Math.ceil(Math.log(scale) / Math.log(2)), 5);
		this.seed = random.nextInt();
	}
	
	public TerraBiome getBiome(double x, double z) {
		if (cacheTicks++ == 65536) {
			cacheTicks = 0;
			if (chunks.size() > 127) {
				chunks.clear();
			}
		}
		
		TerraBiome biome = getRawBiome(x, z);
		TerraBiome edge = biome.getEdgeBiome();
		int size = biome.getEdgeSize();
		
		if (edge == null && biome.getParentBiome() != null) {
			edge = biome.getParentBiome().getEdgeBiome();
			size = biome.getParentBiome().getEdgeSize();
		}
		
		if (edge == null) {
			return biome;
		}
		
		for (byte i = 0; i < 8; i++) {
			if (!getRawBiome(x + size * EDGE_CIRCLE_X[i], z + size * EDGE_CIRCLE_Z[i]).equals(biome)) {
				return edge;
			}
		}
		
		return biome;
	}
	
	public BiomeChunk getChunk(final int cx, final int cz) {
		final ChunkPos pos = new ChunkPos(cx, cz);
		BiomeChunk chunk = chunks.get(pos);
		if (chunk == null) {
			Random random = new Random(MathHelper.hashCode(cx, seed, cz));
			chunk = new BiomeChunk(random, picker, cx, cz);
			chunks.put(pos, chunk);
		}
		return chunk;
	}
	
	private TerraBiome getRawBiome(double x, double z) {
		double px = x / scale * RAD_INNER;
		double pz = z / scale;
		double dx = rotateX(px, pz);
		double dz = rotateZ(px, pz);
		px = dx;
		pz = dz;
		
		dx = getNoise(px, pz, (byte) 0) * 0.2F;
		dz = getNoise(pz, px, (byte) 1) * 0.2F;
		px += dx;
		pz += dz;
		
		int cellZ = (int) Math.floor(pz);
		boolean offset = (cellZ & 1) == 1;
		
		if (offset) {
			px += 0.5;
		}
		
		int cellX = (int) Math.floor(px);
		
		float pointX = (float) (px - cellX - 0.5);
		float pointZ = (float) (pz - cellZ - 0.5);
		
		if (Math.abs(pointZ) < 0.3333F) {
			return getChunkBiome(cellX, cellZ);
		}
		
		if (insideHexagon(0, 0, 1.1555F, pointZ * RAD_INNER, pointX)) {
			return getChunkBiome(cellX, cellZ);
		}
		
		cellX = pointX < 0 ? (offset ? cellX - 1 : cellX) : (offset ? cellX : cellX + 1);
		cellZ = pointZ < 0 ? cellZ - 1 : cellZ + 1;
		
		return getChunkBiome(cellX, cellZ);
	}
	
	private TerraBiome getChunkBiome(int x, int z) {
		int cx = BiomeChunk.scaleCoordinate(x);
		int cz = BiomeChunk.scaleCoordinate(z);
		
		if (((z >> 2) & 1) == 0 && BiomeChunk.isBorder(x)) {
			x = 0;
			cx += 1;
		}
		else if (((x >> 2) & 1) == 0 && BiomeChunk.isBorder(z)) {
			z = 0;
			cz += 1;
		}
		
		return getChunk(cx, cz).getBiome(x, z);
	}
	
	private boolean insideHexagon(float centerX, float centerZ, float radius, float x, float z) {
		double dx = Math.abs(x - centerX) / radius;
		double dy = Math.abs(z - centerZ) / radius;
		return (dy <= COEF) && (COEF * dx + 0.25F * dy <= COEF_HALF);
	}
	
	private double getNoise(double x, double z, byte state) {
		double result = 0;
		for (byte i = 1; i <= noiseIterations; i++) {
			OpenSimplexNoise noise = noises[state];
			state = (byte) ((state + 1) & 1);
			result += noise.eval(x * i, z * i) / i;
		}
		return result;
	}
	
	private double rotateX(double x, double z) {
		return x * COS - z * SIN;
	}
	
	private double rotateZ(double x, double z) {
		return x * SIN + z * COS;
	}
	
	static {
		EDGE_CIRCLE_X = new float[8];
		EDGE_CIRCLE_Z = new float[8];
		
		for (byte i = 0; i < 8; i++) {
			float angle = i / 4F * (float) Math.PI;
			EDGE_CIRCLE_X[i] = (float) Math.sin(angle);
			EDGE_CIRCLE_Z[i] = (float) Math.cos(angle);
		}
	}
}
