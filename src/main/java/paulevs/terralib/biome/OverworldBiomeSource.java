package paulevs.terralib.biome;

import paulevs.bhcore.noise.OpenSimplexNoise;
import paulevs.terralib.map.BiomeMap;

import java.util.Random;

public class OverworldBiomeSource extends TerraBiomeSource {
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
	
	@Override
	public TerraBiome getTerraBiome(double x, double z) {
		return landNoise.eval(x * 0.1, z * 0.1) > 0 ? landMap.getBiome(x, z) : oceanMap.getBiome(x, z);
	}
}
