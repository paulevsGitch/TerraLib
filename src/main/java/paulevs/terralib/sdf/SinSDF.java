package paulevs.terralib.sdf;

import net.minecraft.util.maths.MathHelper;

public class SinSDF extends FlatSDF {
	private float amplitude;
	private float scale;
	
	public SinSDF setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public SinSDF setAmplitude(float amplitude) {
		this.amplitude = amplitude;
		return this;
	}
	
	@Override
	protected float getHeight(int x, int z) {
		float s1 = MathHelper.sin(x * scale);
		float s2 = MathHelper.sin(z * scale);
		return (s1 + s2) * amplitude + height;
	}
}
