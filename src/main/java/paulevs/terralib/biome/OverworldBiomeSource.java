package paulevs.terralib.biome;

import paulevs.bhcore.noise.OpenSimplexNoise;
import paulevs.terralib.map.BiomeMap;
import paulevs.terralib.map.picker.TwoAreaBiomePicker;

import java.util.Random;

public class OverworldBiomeSource extends TerraBiomeSource {
	private final OpenSimplexNoise landNoise;
	//private final BiomeMap oceanMap;
	//private final BiomeMap landMap;
	private final BiomeMap map;
	
	public OverworldBiomeSource(long seed, int biomeSize) {
		Random random = new Random(seed);
		this.landNoise = new OpenSimplexNoise(random.nextLong());
		//this.oceanMap = new BiomeMap(random.nextLong(), biomeSize, new RandomBiomePicker(OverworldBiomes.OCEAN_BIOMES));
		//this.landMap = new BiomeMap(random.nextLong(), biomeSize, new RandomBiomePicker(OverworldBiomes.LAND_BIOMES));
		this.map = new BiomeMap(random.nextLong(), biomeSize, new TwoAreaBiomePicker(random.nextLong(), OverworldBiomes.LAND_BIOMES, OverworldBiomes.OCEAN_BIOMES));
		
		
	}
	
	@Override
	public TerraBiome getTerraBiome(double x, double z) {
		return map.getBiome(x, z);//landNoise.eval(x * 0.01, z * 0.01) > 0 ? landMap.getBiome(x, z) : oceanMap.getBiome(x, z);
	}
}
