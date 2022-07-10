package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bhcore.storage.vector.Vec3I;

import java.util.Random;

public class FlatScatterSDF extends TerrainSDF {
	private static final Random RANDOM = new Random(0);
	TerrainSDF scattered;
	int distance;
	
	public FlatScatterSDF setScattered(TerrainSDF scattered) {
		this.scattered = scattered;
		return this;
	}
	
	public FlatScatterSDF setDistance(int distance) {
		this.distance = distance;
		return this;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		int ix = pos.x / distance;
		int iz = pos.z / distance;
		
		float dx = (float) pos.x / distance - ix;
		float dz = (float) pos.z / distance - iz;
		
		float density = -100F;
		Vec3I pos2 = new Vec3I();
		pos2.y = pos.y;
		for (byte x = -1; x < 2; x++) {
			for (byte z = -1; z < 2; z++) {
				RANDOM.setSeed(MathHelper.hashCode(ix + x, 0, iz + z));
				float px = RANDOM.nextFloat() + x - dx;
				float pz = RANDOM.nextFloat() + z - dz;
				pos2.x = net.minecraft.util.maths.MathHelper.floor(px * distance);
				pos2.z = net.minecraft.util.maths.MathHelper.floor(pz * distance);
				float d = scattered.getDensity(level, pos2);
				if (d > density) density = d;
			}
		}
		
		return density;
	}
}
