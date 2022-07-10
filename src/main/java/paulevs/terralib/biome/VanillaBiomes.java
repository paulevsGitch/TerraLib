package paulevs.terralib.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.level.biome.BaseBiome;

import java.util.List;

public class VanillaBiomes {
	public static final TerraBiome RAINFOREST = new TerraBiome(BaseBiome.RAINFOREST).setTemperature(1.0F);
	public static final TerraBiome SWAMPLAND = new TerraBiome(BaseBiome.SWAMPLAND).setTemperature(0.7F);
	public static final TerraBiome SEASONAL_FOREST = new TerraBiome(BaseBiome.SEASONAL_FOREST).setTemperature(0.9F);
	public static final TerraBiome FOREST = new TerraBiome(BaseBiome.FOREST).setTemperature(0.97F);
	public static final TerraBiome SAVANNA = new TerraBiome(BaseBiome.SAVANNA).setTemperature(0.95F);
	public static final TerraBiome SHRUBLAND = new TerraBiome(BaseBiome.SHRUBLAND).setTemperature(0.97F);
	public static final TerraBiome TAIGA = new TerraBiome(BaseBiome.TAIGA).setTemperature(0.5F);
	public static final TerraBiome DESERT = new TerraBiome(BaseBiome.DESERT).setTemperature(1.0F);
	public static final TerraBiome PLAINS = new TerraBiome(BaseBiome.PLAINS).setTemperature(0.45F);
	public static final TerraBiome ICE_DESERT = new TerraBiome(BaseBiome.ICE_DESERT).setTemperature(0.2F);
	public static final TerraBiome TUNDRA = new TerraBiome(BaseBiome.TUNDRA).setTemperature(0.5F);
	public static final TerraBiome NETHER = new TerraBiome(BaseBiome.NETHER).setTemperature(2.0F);
	public static final TerraBiome SKY = new TerraBiome(BaseBiome.SKY).setTemperature(1.0F);
	
	public static final List<TerraBiome> OVERWORLD_BIOMES;
	
	static {
		Builder<TerraBiome> builder = ImmutableList.builder();
		builder.add(VanillaBiomes.RAINFOREST);
		builder.add(VanillaBiomes.SWAMPLAND);
		builder.add(VanillaBiomes.SEASONAL_FOREST);
		builder.add(VanillaBiomes.FOREST);
		builder.add(VanillaBiomes.SAVANNA);
		builder.add(VanillaBiomes.SHRUBLAND);
		builder.add(VanillaBiomes.TAIGA);
		builder.add(VanillaBiomes.DESERT);
		builder.add(VanillaBiomes.PLAINS);
		builder.add(VanillaBiomes.ICE_DESERT);
		builder.add(VanillaBiomes.TUNDRA);
		OVERWORLD_BIOMES = builder.build();
	}
}
