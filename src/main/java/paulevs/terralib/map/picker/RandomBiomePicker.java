package paulevs.terralib.map.picker;

import paulevs.bhcore.storage.WeighTree;
import paulevs.bhcore.storage.WeightedList;
import paulevs.terralib.biome.TerraBiome;

import java.util.Random;

public class RandomBiomePicker implements BiomePicker {
	private WeighTree<TerraBiome> tree;
	
	public RandomBiomePicker(WeightedList<TerraBiome> biomes) {
		tree = new WeighTree<>(biomes);
	}
	
	@Override
	public TerraBiome getBiome(int x, int z, Random random) {
		return tree.get(random);
	}
}
