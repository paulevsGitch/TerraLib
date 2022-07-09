package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import paulevs.bhcore.storage.vector.Vec3I;
import paulevs.bhcore.util.MathUtil;

public class MaxSDF extends TerrainSDF {
	private TerrainSDF a;
	private TerrainSDF b;
	
	public MaxSDF setA(TerrainSDF a) {
		this.a = a;
		return this;
	}
	
	public MaxSDF setB(TerrainSDF b) {
		this.b = b;
		return this;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		return MathUtil.max(a.getDensity(level, pos), b.getDensity(level, pos));
	}
}
