package paulevs.terralib.biome;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.Vec2i;
import paulevs.bhcore.noise.OpenSimplexNoise;
import paulevs.terralib.map.BiomeMap;

import java.util.Random;

public class OverworldBiomeSource extends BiomeSource {
	private final OpenSimplexNoise landNoise;
	private final BiomeMap oceanMap;
	private final BiomeMap landMap;
	
	public OverworldBiomeSource(long seed, int biomeSize) {
		Random random = new Random(seed);
		this.landNoise = new OpenSimplexNoise(random.nextLong());
		this.oceanMap = new BiomeMap(random.nextLong(), biomeSize, OverworldBiomes.OCEAN_PICKER);
		this.landMap = new BiomeMap(random.nextLong(), biomeSize, OverworldBiomes.LAND_PICKER);
		this.temperatureNoises = new double[256];
		this.rainfallNoises = new double[256];
		this.detailNoises = new double[256];
	}
	
	@Environment(value= EnvType.CLIENT)
	public double getTemperature(int x, int z) {
		return getTerraBiome(x, z).getTemperature();
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
	
	public TerraBiome getTerraBiome(double x, double z) {
		return landNoise.eval(x * 0.1, z * 0.1) > 0 ? landMap.getBiome(x, z) : oceanMap.getBiome(x, z);
	}
}
