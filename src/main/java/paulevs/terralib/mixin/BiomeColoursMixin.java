package paulevs.terralib.mixin;

import net.minecraft.level.BlockView;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.client.colour.world.BiomeColours;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.terralib.biome.TerraBiomeSource;

@Mixin(value = BiomeColours.class, remap = false)
public class BiomeColoursMixin {
	@Inject(method = "getGrassColour", at = @At("HEAD"), cancellable = true)
	private static void getGrassColour(BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		BiomeSource source = world.getBiomeSource();
		if (source instanceof TerraBiomeSource) {
			info.setReturnValue(((TerraBiomeSource) source).getGrassColor(pos.x, pos.z));
		}
		else {
			info.setReturnValue(world.getBiomeSource().getBiome(pos.x, pos.z).grassColor);
		}
	}
}
