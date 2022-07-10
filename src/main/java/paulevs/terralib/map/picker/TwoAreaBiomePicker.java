package paulevs.terralib.map.picker;

import paulevs.bhcore.noise.OpenSimplexNoise;
import paulevs.bhcore.storage.WeighTree;
import paulevs.bhcore.storage.WeightedList;
import paulevs.terralib.biome.TerraBiome;

import java.util.Random;

public class TwoAreaBiomePicker implements BiomePicker {
	private final OpenSimplexNoise landNoise;
	private WeighTree<TerraBiome> posBiomes;
	private WeighTree<TerraBiome> negBiomes;
	
	public TwoAreaBiomePicker(long seed, WeightedList<TerraBiome> posAreaBiomes, WeightedList<TerraBiome> negAreaBiomes) {
		landNoise = new OpenSimplexNoise(seed);
		posBiomes = new WeighTree<>(posAreaBiomes);
		negBiomes = new WeighTree<>(negAreaBiomes);
	}
	
	@Override
	public TerraBiome getBiome(int x, int z, Random random) {
		return landNoise.eval(x, z) > 0 ? posBiomes.get(random) : negBiomes.get(random);
	}
}
