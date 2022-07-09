package paulevs.terralib.biome;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.Vec2i;
import paulevs.bhcore.storage.vector.Vec3F;

import java.util.HashMap;
import java.util.Map;

public abstract class TerraBiomeSource  extends BiomeSource {
	private static final Map<TerraBiome, Integer> BIOME_CACHE = new HashMap<>();
	private static final Vec3F RGB = new Vec3F();
	
	public abstract TerraBiome getTerraBiome(double x, double z);
	
	protected void fillCache(int x, int z, byte radius) {
		BIOME_CACHE.clear();
		for (short i = (short) -radius; i <= radius; i++) {
			int px = x + i;
			for (short j = (short) -radius; j <= radius; j++) {
				int pz = z + j;
				TerraBiome biome = getTerraBiome(px, pz);
				BIOME_CACHE.put(biome, BIOME_CACHE.getOrDefault(biome, 0) + 1);
			}
		}
	}
	
	public int getGrassColor(int x, int z) {
		fillCache(x, z, (byte) 3);
		RGB.set(0, 0, 0);
		BIOME_CACHE.forEach((biome, count) -> {
			int rgb = biome.grassColor;
			float r = ((rgb >> 16) & 255) / 255.0F;
			float g = ((rgb >> 8) & 255) / 255.0F;
			float b = (rgb & 255) / 255.0F;
			float delta = count / 49.0F;
			RGB.add(r * delta, g * delta, b * delta);
		});
		int r = (int) (RGB.x * 255);
		int g = (int) (RGB.y * 255);
		int b = (int) (RGB.z * 255);
		return r << 16 | g << 8 | b;
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
}
