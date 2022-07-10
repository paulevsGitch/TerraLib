package paulevs.terralib.map.picker;

import paulevs.bhcore.noise.Noise;
import paulevs.bhcore.storage.WeighTree;
import paulevs.bhcore.storage.WeightedList;
import paulevs.terralib.biome.TerraBiome;

import java.util.Random;

public class TwoAreaBiomePicker implements BiomePicker {
	private final WeighTree<TerraBiome> posBiomes;
	private final WeighTree<TerraBiome> negBiomes;
	private final Noise landNoise;
	private final float criteria;
	private final double scale;
	
	public TwoAreaBiomePicker(Noise noise, double scale, float criteria, WeightedList<TerraBiome> posAreaBiomes, WeightedList<TerraBiome> negAreaBiomes) {
		posBiomes = new WeighTree<>(posAreaBiomes);
		negBiomes = new WeighTree<>(negAreaBiomes);
		landNoise = noise;
		this.criteria = criteria;
		this.scale = scale;
	}
	
	@Override
	public TerraBiome getBiome(int x, int z, Random random) {
		return landNoise.eval(x * scale, z * scale) > criteria ? posBiomes.get(random) : negBiomes.get(random);
	}
}
