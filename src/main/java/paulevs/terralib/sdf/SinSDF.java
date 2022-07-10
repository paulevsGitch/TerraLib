package paulevs.terralib.sdf;

import net.minecraft.util.maths.MathHelper;

public class SinSDF extends HeightSDF {
	private float scale;
	
	public SinSDF setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	@Override
	protected float getHeight(int x, int z) {
		return MathHelper.sin((x + z) * scale);
	}
}
