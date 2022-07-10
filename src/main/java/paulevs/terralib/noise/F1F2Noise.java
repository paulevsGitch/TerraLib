package paulevs.terralib.noise;

import paulevs.bhcore.noise.VoronoiNoise;

public class F1F2Noise extends VoronoiNoise {
	private final float[] buffer = new float[9];
	
	public F1F2Noise(int seed) {
		super(seed);
	}
	
	@Override
	public double eval(double x, double z) {
		getDistances(x, z, buffer);
		return buffer[0] / buffer[1];
	}
}
