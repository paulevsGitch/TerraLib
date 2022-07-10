package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import net.minecraft.util.noise.PerlinOctaveNoise;
import net.minecraft.util.noise.SimplexOctaveNoise;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.MathUtil;

import java.util.Random;

public class VanillaTerrainSDF extends TerrainSDF {
	private PerlinOctaveNoise upperInterpolationNoise;
	private PerlinOctaveNoise lowerInterpolationNoise;
	private PerlinOctaveNoise interpolationNoise;
	private SimplexOctaveNoise temperatureNoise;
	private SimplexOctaveNoise rainfallNoise;
	private PerlinOctaveNoise biomeNoise;
	private PerlinOctaveNoise depthNoise;
	private Random rand;
	private long seed;
	private int lastX;
	private int lastZ;
	
	private final double[] upperInterpolationNoises = new double[17];
	private final double[] lowerInterpolationNoises = new double[17];
	private final double[] interpolationNoises = new double[17];
	private final double[] temperatureNoises = new double[1];
	private final double[] rainfallNoises = new double[1];
	private final double[] biomeNoises = new double[1];
	private final double[] depthNoises = new double[1];
	private final float[] column = new float[17];
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		int bx = pos.x >> 2;
		int bz = pos.z >> 2;
		if (column != null && lastX == bx && lastZ == bz) {
			return interpolate(pos.y);
		}
		lastX = bx;
		lastZ = bz;
		setSeed(level.getSeed());
		calculateNoise(bx, 0, bz);
		return interpolate(pos.y);
	}
	
	private float interpolate(int y) {
		y = MathUtil.clamp(y, 0, 127);
		int iy = y >> 3;
		float dy = y / 8.0F - iy;
		return MathUtil.lerp(column[iy], column[iy + 1], dy);
	}
	
	private void setSeed(long seed) {
		if (this.seed != seed || rand == null) {
			this.seed = seed;
			this.rand = new Random(seed);
			this.upperInterpolationNoise = new PerlinOctaveNoise(this.rand, 16);
			this.lowerInterpolationNoise = new PerlinOctaveNoise(this.rand, 16);
			this.interpolationNoise = new PerlinOctaveNoise(this.rand, 8);
			new PerlinOctaveNoise(this.rand, 4);
			new PerlinOctaveNoise(this.rand, 4);
			this.biomeNoise = new PerlinOctaveNoise(this.rand, 10);
			this.depthNoise = new PerlinOctaveNoise(this.rand, 16);
			this.temperatureNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
			this.rainfallNoise = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
		}
	}
	
	private void calculateNoise(int posX, int posY, int posZ) {
		int blockX = posX << 2;
		int blockZ = posZ << 2;
		
		upperInterpolationNoise.sample(upperInterpolationNoises, posX, posY, posZ, 1, column.length, 1, 684.412F, 684.412F, 684.412F);
		lowerInterpolationNoise.sample(lowerInterpolationNoises, posX, posY, posZ, 1, column.length, 1, 684.412F, 684.412F, 684.412F);
		interpolationNoise.sample(interpolationNoises, posX, posY, posZ, 1, column.length, 1, 8.55515F, 4.277575F, 8.55515F);
		rainfallNoise.sample(rainfallNoises, blockX, blockZ, 1, 1, 0.05F, 0.05F, 0.3333333333333333);
		temperatureNoise.sample(temperatureNoises, blockX, blockZ, 1, 1, 0.025F, 0.025F, 0.25F);
		biomeNoise.sample(biomeNoises, posX, posZ, 1, 1, 1.121F, 1.121F, 0.5F);
		depthNoise.sample(depthNoises, posX, posZ, 1, 1, 200.0F, 200.0F, 0.5F);
		
		float wetCurve = 1.0F - (float) rainfallNoises[0] * (float) temperatureNoises[0];
		wetCurve *= wetCurve;
		wetCurve *= wetCurve;
		wetCurve = 1.0F - wetCurve;
		float bio = ((float) biomeNoises[0] + 256.0F) / 512.0F;
		
		if ((bio *= wetCurve) > 1.0F) {
			bio = 1.0F;
		}
		
		float depth;
		if ((depth = (float) depthNoises[0] / 8000.0F) < 0.0F) {
			depth = -depth * 0.3F;
		}
		
		if ((depth = depth * 3.0F - 2.0F) < 0.0F) {
			if ((depth /= 2.0F) < -1.0F) {
				depth = -1.0F;
			}
			depth /= 1.4F;
			depth /= 2.0F;
			bio = 0.0F;
		}
		else {
			if (depth > 1.0F) {
				depth = 1.0F;
			}
			depth /= 8.0F;
		}
		
		if (bio < 0.0F) {
			bio = 0.0F;
		}
		
		bio += 0.5F;
		depth = depth * column.length / 16.0F;
		float d8 = column.length / 2.0F + depth * 4.0F;
		
		for (byte y = 0; y < column.length; y++) {
			double py = (y - d8) * 12.0F / bio;
			if (py < 0.0F) py *= 4.0F;
			float upper = (float) upperInterpolationNoises[y] / 512.0F;
			float lower = (float) lowerInterpolationNoises[y] / 512.0F;
			float inter = ((float) interpolationNoises[y] / 10.0F + 1.0F) / 2.0F;
			float density = inter < 0.0F ? upper : (inter > 1.0F ? lower : upper + (lower - upper) * inter);
			density -= py;
			if (y > column.length - 4) {
				float d14 = (float) (y - (column.length - 4)) / 3.0F;
				density = density * (1.0F - d14) + -10.0F * d14;
			}
			column[y] = density;
		}
	}
}
