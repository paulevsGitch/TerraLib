package paulevs.terralib.biome;

import paulevs.terralib.TerraLib;
import paulevs.terralib.map.BiomePicker;
import paulevs.terralib.sdf.FlatSDF;
import paulevs.terralib.sdf.MaxSDF;
import paulevs.terralib.sdf.TerrainSDF;
import paulevs.terralib.sdf.VanillaTerrainSDF;

public class OverworldBiomes {
	public static final BiomePicker OCEAN_PICKER = new BiomePicker();
	public static final BiomePicker LAND_PICKER = new BiomePicker();
	public static final TerrainSDF OVERWORLD_GEN;
	
	public static final TerraBiome OCEAN = new TerraBiome(TerraLib.id("ocean")).setTemperature(0.5F).setGrassColor(0xFF00FF).setTerrainSDF(new FlatSDF().setHeight(32));
	
	public static void addLandBiome(TerraBiome biome, float weight) {
		LAND_PICKER.addBiome(biome, weight);
	}
	
	public static void addOceanBiome(TerraBiome biome, float weight) {
		OCEAN_PICKER.addBiome(biome, weight);
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
