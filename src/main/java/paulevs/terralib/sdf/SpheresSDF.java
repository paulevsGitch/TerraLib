package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bhcore.storage.vector.Vec3I;

import java.util.Random;

public class SpheresSDF extends TerrainSDF {
	private static final Random RANDOM = new Random(0);
	private int distance;
	private float radius;
	
	public SpheresSDF setDistance(int distance) {
		this.distance = distance;
		return this;
	}
	
	public SpheresSDF setRadius(float radius) {
		this.radius = radius;
		return this;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		int ix = pos.x / distance;
		int iy = pos.y / distance;
		int iz = pos.z / distance;
		
		float dx = (float) pos.x / distance - ix;
		float dy = (float) pos.y / distance - iy;
		float dz = (float) pos.z / distance - iz;
		
		float density = -100F;
		for (byte x = -1; x < 2; x++) {
			for (byte y = -1; y < 2; y++) {
				for (byte z = -1; z < 2; z++) {
					RANDOM.setSeed(MathHelper.hashCode(ix + x, iy + y, iz + z));
					float px = RANDOM.nextFloat() + x - dx;
					float py = RANDOM.nextFloat() + y - dy;
					float pz = RANDOM.nextFloat() + z - dz;
					float d = radius - MathHelper.sqrt(px * px + py * py + pz * pz);
					if (d > density) density = d;
				}
			}
		}
		
		return density;
	}
}
