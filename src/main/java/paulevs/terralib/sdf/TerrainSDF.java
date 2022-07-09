package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import paulevs.bhcore.storage.vector.Vec3I;

public abstract class TerrainSDF {
	public abstract float getDensity(Level level, Vec3I pos);
}
