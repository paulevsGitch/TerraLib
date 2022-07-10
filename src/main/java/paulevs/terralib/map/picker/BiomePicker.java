package paulevs.terralib.map.picker;

import paulevs.terralib.biome.TerraBiome;

import java.util.Random;

public interface BiomePicker {
	TerraBiome getBiome(int x, int z, Random random);
}
