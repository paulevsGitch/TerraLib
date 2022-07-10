package paulevs.terralib.biome;

import paulevs.bhcore.noise.OpenSimplexNoise;
import paulevs.terralib.map.BiomeMap;
import paulevs.terralib.map.picker.TwoAreaBiomePicker;
import paulevs.terralib.noise.F1F2Noise;

import java.util.Random;

public class OverworldBiomeSource extends TerraBiomeSource {
	private final OpenSimplexNoise landNoise;
	private final BiomeMap map;
	
	public OverworldBiomeSource(long seed, int biomeSize) {
		Random random = new Random(seed);
		this.landNoise = new OpenSimplexNoise(random.nextLong());
		this.map = new BiomeMap(random.nextLong(), biomeSize, new TwoAreaBiomePicker(
			new F1F2Noise(random.nextInt()),
			0.02, 0.7F,
			OverworldBiomes.OCEAN_BIOMES,
			OverworldBiomes.LAND_BIOMES
		));
	}
	
	@Override
	public TerraBiome getTerraBiome(double x, double z) {
		return map.getBiome(x, z);
	}
}
