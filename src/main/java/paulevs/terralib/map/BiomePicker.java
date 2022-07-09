package paulevs.terralib.map;

import org.jetbrains.annotations.Nullable;
import paulevs.bhcore.storage.WeighTree;
import paulevs.bhcore.storage.WeightedList;
import paulevs.terralib.biome.TerraBiome;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomePicker {
	private final Map<TerraBiome, Float> biomes = new HashMap<>();
	private WeighTree<TerraBiome> tree;
	private boolean update;
	
	public void addBiome(TerraBiome biome, float weight) {
		if (biomes.containsKey(biome)) return;
		biomes.put(biome, weight);
		update = true;
	}
	
	@Nullable
	public TerraBiome getBiome(Random random) {
		if (update) rebuild();
		return biomes.isEmpty() ? null : tree.get(random);
	}
	
	public void rebuild() {
		if (biomes.isEmpty()) {
			return;
		}
		WeightedList<TerraBiome> list = new WeightedList<>();
		biomes.forEach((biome, weight) -> {
			list.add(biome, weight);
		});
		tree = new WeighTree<>(list);
		update = false;
	}
}
