package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import paulevs.bhcore.storage.vector.Vec3I;

public class FlatSDF extends TerrainSDF {
	private int height;
	
	public FlatSDF setHeight(int height) {
		this.height = height;
		return this;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		return (height - pos.y) * 0.0625F;
	}
}
