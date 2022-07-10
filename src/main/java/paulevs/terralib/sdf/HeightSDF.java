package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import paulevs.bhcore.storage.vector.Vec3I;

public abstract class HeightSDF extends TerrainSDF {
	private float intensity = 1;
	
	public HeightSDF setIntensity(float intensity) {
		this.intensity = intensity;
		return this;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		return (getHeight(pos.x, pos.z) - pos.y) * intensity;
	}
	
	protected abstract float getHeight(int x, int z);
}
