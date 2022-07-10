package paulevs.terralib.biome;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.Vec2i;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.terralib.map.Vec2I;
import paulevs.terralib.sdf.MixSDF;
import paulevs.terralib.sdf.TerrainSDF;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TerraBiomeSource  extends BiomeSource {
	private static final Map<TerraBiome, Integer> BIOME_CACHE = new HashMap<>();
	private static final Map<Byte, List<Vec2I>> CIRCLES = new HashMap<>();
	private static final MixSDF SDF = new MixSDF();
	private static final Vec3F RGB = new Vec3F();
	
	public TerraBiomeSource() {
		this.temperatureNoises = new double[256];
		this.rainfallNoises = new double[256];
		this.detailNoises = new double[256];
	}
	
	public abstract TerraBiome getTerraBiome(double x, double z);
	
	protected int fillCache(int x, int z, byte radius) {
		BIOME_CACHE.clear();
		List<Vec2I> circle = CIRCLES.computeIfAbsent(radius, key -> {
			List<Vec2I> positions = new ArrayList<>();
			for (short i = (short) -radius; i <= radius; i++) {
				for (short j = (short) -radius; j <= radius; j++) {
					positions.add(new Vec2I(i, j));
				}
			}
			return positions;
		});
		circle.forEach(offset -> {
			TerraBiome biome = getTerraBiome(x + offset.getX(), z + offset.getZ());
			BIOME_CACHE.put(biome, BIOME_CACHE.getOrDefault(biome, 0) + 1);
		});
		return circle.size();
		/*for (short i = (short) -radius; i <= radius; i++) {
			int px = x + i;
			for (short j = (short) -radius; j <= radius; j++) {
				int pz = z + j;
				TerraBiome biome = getTerraBiome(px, pz);
				BIOME_CACHE.put(biome, BIOME_CACHE.getOrDefault(biome, 0) + 1);
			}
		}*/
	}
	
	public int getGrassColor(int x, int z) {
		int globalCount = fillCache(x, z, (byte) 3);
		RGB.set(0, 0, 0);
		BIOME_CACHE.forEach((biome, count) -> {
			int rgb = biome.getGrassColor();
			float r = ((rgb >> 16) & 255) / 255.0F;
			float g = ((rgb >> 8) & 255) / 255.0F;
			float b = (rgb & 255) / 255.0F;
			float delta = (float) count / globalCount;
			RGB.add(r * delta, g * delta, b * delta);
		});
		int r = (int) (RGB.x * 255);
		int g = (int) (RGB.y * 255);
		int b = (int) (RGB.z * 255);
		return r << 16 | g << 8 | b;
	}
	
	public TerrainSDF getSDF(int x, int z) {
		int globalCount = fillCache(x, z, (byte) 16);
		if (BIOME_CACHE.size() == 1) {
			return BIOME_CACHE.keySet().iterator().next().getTerrainSDF();
		}
		SDF.clear();
		BIOME_CACHE.forEach((biome, count) -> SDF.add(biome.getTerrainSDF(), (float) count / globalCount));
		return SDF;
	}
	
	@Override
	public BaseBiome getBiomeInChunk(Vec2i pos) {
		return getTerraBiome(pos.x << 4 | 8, pos.z << 4 | 8);
	}
	
	@Override
	public BaseBiome getBiome(int x, int z) {
		return getTerraBiome(x, z);
	}
	
	@Override
	public BaseBiome[] getBiomes(BaseBiome[] biomes, int x, int z, int sideX, int sideZ) {
		int capacity = sideX * sideZ;
		if (biomes == null || biomes.length < capacity) {
			biomes = new BaseBiome[capacity];
		}
		for (int index = 0; index < capacity; index++) {
			int px = index / sideZ + x;
			int pz = index % sideZ + z;
			biomes[index] = getTerraBiome(px, pz);
		}
		return biomes;
	}
	
	@Environment(value= EnvType.CLIENT)
	public double getTemperature(int x, int z) {
		return getTerraBiome(x, z).getTemperature();
	}
	
	@Override
	public double[] getTemperatures(double[] temperatures, int x, int z, int sideX, int sideZ) {
		int capacity = sideX * sideZ;
		if (temperatures == null || temperatures.length < capacity) {
			temperatures = new double[capacity];
		}
		for (int index = 0; index < capacity; index++) {
			int px = index / sideZ + x;
			int pz = index % sideZ + z;
			temperatures[index] = getTerraBiome(px, pz).getTemperature();
		}
		return temperatures;
	}
	
	protected void buildDebugMap(int scale) {
		BufferedImage img = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		int index = 0;
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				data[index++] = getBiome((x - 256) * scale, (z - 256) * scale).grassColor | 255 << 24;
			}
		}
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel(new ImageIcon(img)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
