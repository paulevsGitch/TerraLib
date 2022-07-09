package paulevs.terralib.mixin;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.Level;
import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.OverworldLevelSource;
import net.minecraft.util.noise.PerlinOctaveNoise;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSectionsAccessor;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.MathUtil;
import paulevs.terralib.biome.TerraBiomeSource;
import paulevs.terralib.sdf.TerrainSDF;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

@Mixin(OverworldLevelSource.class)
public class OverworldLevelSourceMixin {
	@Shadow	private PerlinOctaveNoise interpolationNoise;
	@Shadow private Level level;
	
	@Unique private ForkJoinPool customPool = new ForkJoinPool(8);
	@Unique float[][] dataColumns = new float[25][33];
	
	@Inject(method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/source/OverworldLevelSource;shapeChunk(II[B[Lnet/minecraft/level/biome/BaseBiome;[D)V"
	), locals = LocalCapture.CAPTURE_FAILHARD)
	private void onGetChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> info, byte[] blocks, Chunk chunk, double[] var5) {
		if (!(level.getBiomeSource() instanceof TerraBiomeSource)) return;
		TerraBiomeSource source = (TerraBiomeSource) level.getBiomeSource();
		
		short minY = (short) ((HeightLimitView) level).getBottomY();
		short height = (short) ((HeightLimitView) level).getHeight();
		short capacity = (short) ((height >> 2) + 1);
		int posX = chunkX << 4;
		int posZ = chunkZ << 4;
		
		Vec3I pos = new Vec3I();
		for (byte i = 0; i < 25; i++) {
			if (dataColumns[i].length != capacity) dataColumns[i] = new float[capacity];
			float[] column = dataColumns[i];
			pos.x = posX + ((i % 5) << 2);
			pos.z = posZ + ((i / 5) << 2);
			TerrainSDF sdf = source.getSDF(pos.x, pos.z);
			for (int j = 0; j < capacity; j++) {
				pos.y = (j << 2) + minY;
				column[j] = sdf.getDensity(level, pos);
			}
		};
		
		ChunkSectionsAccessor accessor = (ChunkSectionsAccessor) chunk;
		ChunkSection[] sections = accessor.getSections();
		short minSection = (short) ChunkSectionPos.getSectionCoord(minY);
		short maxSection = (short) ChunkSectionPos.getSectionCoord(minY + height);
		
		BlockState stone = ((BlockStateHolder) BaseBlock.STONE).getDefaultState();
		
		customPool.submit(() -> IntStream.range(minSection, maxSection).parallel().forEach(n -> {
			ChunkSection section = sections[n];
			if (section == null) {
				section = new ChunkSection(n);
				sections[n] = section;
			}
			
			int offset = section.getYOffset() - minY;
			for (byte x = 0; x < 16; x++) {
				byte ix = (byte) (x >> 2);
				float dx = (x / 4.0F - ix);
				for (byte z = 0; z < 16; z++) {
					byte iz = (byte) (z >> 2);
					float dz = (z / 4.0F - iz);
					
					byte ixz = (byte) (iz * 5 + ix);
					float[] column1 = dataColumns[ixz];
					float[] column2 = dataColumns[ixz + 1];
					float[] column3 = dataColumns[ixz + 5];
					float[] column4 = dataColumns[ixz + 6];
					
					for (byte y = 0; y < 16; y++) {
						short py = (short) (offset + y);
						short iy = (short) (py >> 2);
						float dy = (py / 4.0F - iy);
						
						float a = column1[iy];
						float b = column2[iy];
						float c = column3[iy];
						float d = column4[iy];
						iy++;
						float e = column1[iy];
						float f = column2[iy];
						float g = column3[iy];
						float h = column4[iy];
						
						a = MathUtil.lerp(a, b, dx);
						b = MathUtil.lerp(c, d, dx);
						c = MathUtil.lerp(e, f, dx);
						d = MathUtil.lerp(g, h, dx);
						
						a = MathUtil.lerp(a, b, dz);
						b = MathUtil.lerp(c, d, dz);
						
						a = MathUtil.lerp(a, b, dy);
						
						if (a > 0) section.setBlockState(x, y, z, stone);
					}
				}
			}
		}));
	}
	
	@Inject(method = "shapeChunk(II[B[Lnet/minecraft/level/biome/BaseBiome;[D)V", at = @At("HEAD"), cancellable = true)
	private void disableShapeChunk(int chunkX, int chunkZ, byte[] blocks, BaseBiome[] biomes, double[] temperatures, CallbackInfo info) {
		info.cancel();
	}
	
	@Inject(method = "buildSurface", at = @At("HEAD"), cancellable = true)
	private void disableBuildSurface(int chunkX, int chunkZ, byte[] tiles, BaseBiome[] biomes, CallbackInfo info) {
		info.cancel();
	}
}
