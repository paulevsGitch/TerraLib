package paulevs.terralib.mixin;

import net.minecraft.level.biome.BaseBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BaseBiome.class)
public interface BaseBiomeAccessor {
	@Accessor("precipitates")
	boolean getPrecipitation();
	
	@Accessor("precipitates")
	void setPrecipitation(boolean precipitates);
}
