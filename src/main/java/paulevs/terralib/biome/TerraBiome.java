package paulevs.terralib.biome;

import net.minecraft.level.biome.BaseBiome;
import paulevs.terralib.mixin.BaseBiomeAccessor;
import paulevs.terralib.sdf.FlatSDF;
import paulevs.terralib.sdf.TerrainSDF;

public class TerraBiome extends BaseBiome {
	public static final TerrainSDF DEFAULT_TERRAIN = new FlatSDF().setHeight(64);
	private TerrainSDF terrainSDF = DEFAULT_TERRAIN;
	private int fogColor;
	private int skyColor;
	
	public int getFogColor() {
		return fogColor;
	}
	
	public TerraBiome setFogColor(int fogColor) {
		this.fogColor = fogColor;
		return this;
	}
	
	public int getSkyColor() {
		return skyColor;
	}
	
	public TerraBiome setSkyColor(int skyColor) {
		this.skyColor = skyColor;
		return this;
	}
	
	public BaseBiome setRain(boolean rain) {
		access(this).setPrecipitation(rain);
		return this;
	}
	
	public TerrainSDF getTerrainSDF() {
		return terrainSDF;
	}
	
	public TerraBiome setTerrainSDF(TerrainSDF terrainSDF) {
		this.terrainSDF = terrainSDF;
		return this;
	}
	
	public static BaseBiomeAccessor access(BaseBiome biome) {
		return (BaseBiomeAccessor) biome;
	}
}
