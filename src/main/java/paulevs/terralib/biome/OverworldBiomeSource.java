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
	}
	
	@Override
	public TerraBiome getTerraBiome(double x, double z) {
		return landNoise.eval(x * 0.01, z * 0.01) > 0 ? landMap.getBiome(x, z) : oceanMap.getBiome(x, z);
	}
}
