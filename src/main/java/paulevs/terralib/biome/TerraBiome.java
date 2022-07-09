package paulevs.terralib.biome;

import net.minecraft.level.biome.BaseBiome;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.Nullable;
import paulevs.bhcore.storage.WeightedList;
import paulevs.terralib.mixin.BaseBiomeAccessor;
import paulevs.terralib.sdf.FlatSDF;
import paulevs.terralib.sdf.TerrainSDF;

import java.util.Random;

public class TerraBiome extends BaseBiome {
	public static final TerrainSDF DEFAULT_TERRAIN = new FlatSDF().setHeight(64);
	private final WeightedList<TerraBiome> subBiomes = new WeightedList<>();
	private final Identifier id;
	
	private TerrainSDF terrainSDF = DEFAULT_TERRAIN;
	private TerraBiome parentBiome;
	private TerraBiome edgeBiome;
	private int edgeSize = 16;
	private float temperature;
	private int fogColor;
	private int skyColor;
	
	public TerraBiome(Identifier id) {
		subBiomes.add(this, 1F);
		setName(id.id);
		this.id = id;
	}
	
	public TerraBiome(BaseBiome source) {
		this(Identifier.of(source.name), source);
	}
	
	public TerraBiome(Identifier id, BaseBiome source) {
		this(id);
		setName(source.name);
		setGrassColor(source.grassColor);
		setFoliageColor(source.foliageColor);
		BaseBiomeAccessor accessor = access(source);
		setPrecipitation(accessor.getPrecipitation());
		setSnow(accessor.getSnow());
	}
	
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
	
	public TerraBiome setPrecipitation(boolean precipitation) {
		access().setPrecipitation(precipitation);
		return this;
	}
	
	public TerraBiome setSnow(boolean snow) {
		access().setSnow(snow);
		return this;
	}
	
	public float getTemperature() {
		return temperature;
	}
	
	public TerraBiome setTemperature(float temperature) {
		this.temperature = temperature;
		return this;
	}
	
	public TerrainSDF getTerrainSDF() {
		return terrainSDF;
	}
	
	public TerraBiome setTerrainSDF(TerrainSDF terrainSDF) {
		this.terrainSDF = terrainSDF;
		return this;
	}
	
	public TerraBiome addSubBiome(TerraBiome biome, float weight) {
		if (biome.parentBiome != null) {
			throw new RuntimeException("Biome " + biome + " already have a parent biome " + biome.parentBiome);
		}
		subBiomes.add(biome, weight);
		biome.parentBiome = parentBiome;
		return this;
	}
	
	public TerraBiome getSubBiome(Random random) {
		return subBiomes.get(random);
	}
	
	public TerraBiome setEdgeBiome(TerraBiome edgeBiome) {
		this.edgeBiome = edgeBiome;
		return this;
	}
	
	public int getEdgeSize() {
		return edgeSize;
	}
	
	public TerraBiome setEdgeSize(int edgeSize) {
		this.edgeSize = edgeSize;
		return this;
	}
	
	@Nullable
	public TerraBiome getEdgeBiome() {
		return this.edgeBiome;
	}
	
	public TerraBiome getParentBiome() {
		return parentBiome;
	}
	
	public BaseBiomeAccessor access() {
		return access(this);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof TerraBiome) {
			return cast(obj).id.equals(id);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	public static TerraBiome cast(Object biome) {
		return (TerraBiome) biome;
	}
	
	public static BaseBiomeAccessor access(BaseBiome biome) {
		return (BaseBiomeAccessor) biome;
	}
}
