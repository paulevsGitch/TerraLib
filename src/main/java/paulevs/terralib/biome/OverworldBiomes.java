package paulevs.terralib.biome;

import paulevs.terralib.TerraLib;
import paulevs.terralib.map.BiomePicker;
import paulevs.terralib.sdf.FlatSDF;
import paulevs.terralib.sdf.MaxSDF;
import paulevs.terralib.sdf.SpheresSDF;

public class OverworldBiomes {
	public static final BiomePicker OCEAN_PICKER = new BiomePicker();
	public static final BiomePicker LAND_PICKER = new BiomePicker();
	
	public static final TerraBiome OCEAN = new TerraBiome(TerraLib.id("ocean")).setTemperature(0.5F).setGrassColor(0xFF00FF).setTerrainSDF(new FlatSDF().setHeight(32));
	
	public static void addLandBiome(TerraBiome biome, float weight) {
		LAND_PICKER.addBiome(biome, weight);
	}
	
	public static void addOceanBiome(TerraBiome biome, float weight) {
		OCEAN_PICKER.addBiome(biome, weight);
	}
	
	static {
		addLandBiome(VanillaBiomes.RAINFOREST, 1.0F);
		addLandBiome(VanillaBiomes.SWAMPLAND, 1.0F);
		addLandBiome(VanillaBiomes.SEASONAL_FOREST, 1.0F);
		addLandBiome(VanillaBiomes.FOREST, 1.0F);
		addLandBiome(VanillaBiomes.SAVANNA, 1.0F);
		addLandBiome(VanillaBiomes.SHRUBLAND, 1.0F);
		addLandBiome(VanillaBiomes.TAIGA, 1.0F);
		addLandBiome(VanillaBiomes.DESERT, 1.0F);
		addLandBiome(VanillaBiomes.PLAINS, 1.0F);
		addLandBiome(VanillaBiomes.ICE_DESERT, 1.0F);
		addLandBiome(VanillaBiomes.TUNDRA, 1.0F);
		
		MaxSDF sdf = new MaxSDF().setA(new FlatSDF().setHeight(70)).setB(new SpheresSDF().setRadius(0.3F).setDistance(32));
		VanillaBiomes.RAINFOREST.setTerrainSDF(sdf);
		VanillaBiomes.SWAMPLAND.setTerrainSDF(sdf);
		VanillaBiomes.SEASONAL_FOREST.setTerrainSDF(sdf);
		VanillaBiomes.FOREST.setTerrainSDF(sdf);
		VanillaBiomes.PLAINS.setTerrainSDF(sdf);
		VanillaBiomes.DESERT.setTerrainSDF(new FlatSDF().setHeight(80));
		
		addOceanBiome(OCEAN, 1.0F);
	}
}
