package paulevs.terralib.biome;

import paulevs.bhcore.storage.WeightedList;
import paulevs.terralib.TerraLib;
import paulevs.terralib.sdf.FlatSDF;
import paulevs.terralib.sdf.MaxSDF;
import paulevs.terralib.sdf.SinSDF;
import paulevs.terralib.sdf.TerrainSDF;
import paulevs.terralib.sdf.VanillaTerrainSDF;

public class OverworldBiomes {
	public static final WeightedList<TerraBiome> OCEAN_BIOMES = new WeightedList();
	public static final WeightedList<TerraBiome> LAND_BIOMES = new WeightedList();
	public static final TerrainSDF OVERWORLD_GEN;
	
	public static final TerraBiome OCEAN = new TerraBiome(TerraLib.id("ocean")).setTemperature(0.5F).setGrassColor(0xFF00FF).setTerrainSDF(new SinSDF()
		.setScale(0.1F)
		.setAmplitude(5)
		.setHeight(32)
	);
	
	public static void addLandBiome(TerraBiome biome, float weight) {
		LAND_BIOMES.add(biome, weight);
	}
	
	public static void addOceanBiome(TerraBiome biome, float weight) {
		OCEAN_BIOMES.add(biome, weight);
	}
	
	static {
		OVERWORLD_GEN = new MaxSDF().setA(new VanillaTerrainSDF()).setB(new FlatSDF().setHeight(62));
		
		VanillaBiomes.OVERWORLD_BIOMES.forEach(biome -> {
			biome.setTerrainSDF(OVERWORLD_GEN);
			addLandBiome(biome, 1.0F);
		});
		
		addOceanBiome(OCEAN, 1.0F);
	}
}
