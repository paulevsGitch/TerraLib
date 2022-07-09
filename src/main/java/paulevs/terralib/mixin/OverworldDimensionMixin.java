package paulevs.terralib.mixin;

import net.minecraft.level.dimension.BaseDimension;
import net.minecraft.level.dimension.OverworldDimension;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.terralib.biome.OverworldBiomeSource;

@Mixin(OverworldDimension.class)
public class OverworldDimensionMixin extends BaseDimension {
	@Override
	protected void initBiomeSource() {
		biomeSource = new OverworldBiomeSource(level.getSeed(), 64);
	}
}
