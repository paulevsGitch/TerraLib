package paulevs.terralib.map;

import net.minecraft.level.biome.BaseBiome;
import paulevs.bhcore.storage.WeighTree;
import paulevs.bhcore.storage.WeightedList;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomePicker {
	private final Map<BaseBiome, Float> biomes = new HashMap<>();
	private WeighTree<BaseBiome> tree;
	private boolean update;
	
	public void addBiome(BaseBiome biome, float weight) {
		if (biomes.containsKey(biome)) return;
		biomes.put(biome, weight);
		update = true;
	}
	
	public BaseBiome getBiome(Random random) {
		if (update) rebuild();
		return biomes.isEmpty() ? BaseBiome.PLAINS : tree.get(random);
	}
	
	public void rebuild() {
		if (biomes.isEmpty()) {
			return;
		}
		WeightedList<BaseBiome> list = new WeightedList<>();
		biomes.forEach((biome, weight) -> {
			list.add(biome, weight);
		});
		tree = new WeighTree<>(list);
		update = false;
	}
}
