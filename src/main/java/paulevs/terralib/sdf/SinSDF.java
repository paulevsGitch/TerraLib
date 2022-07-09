package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bhcore.storage.vector.Vec3I;

public class SinSDF extends TerrainSDF {
	private float scale;
	
	public SinSDF setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		return MathHelper.sin((pos.x + pos.z) * scale);
	}
}
