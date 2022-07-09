package paulevs.terralib.sdf;

import net.minecraft.level.Level;
import paulevs.bhcore.storage.vector.Vec3I;

import java.util.ArrayList;
import java.util.List;

public class MixSDF extends TerrainSDF {
	private List<Entry> entries = new ArrayList<>();
	private int index;
	
	public void clear() {
		index = 0;
	}
	
	public void add(TerrainSDF sdf, float power) {
		Entry entry;
		if (index < entries.size()) {
			entry = entries.get(index);
		}
		else {
			entry = new Entry();
			entries.add(entry);
		}
		entry.sdf = sdf;
		entry.power = power;
		index++;
	}
	
	@Override
	public float getDensity(Level level, Vec3I pos) {
		float density = 0;
		for (int i = 0; i < index; i++) {
			Entry entry = entries.get(i);
			density += entry.sdf.getDensity(level, pos) * entry.power;
		}
		return density;
	}
	
	private class Entry {
		TerrainSDF sdf;
		float power;
	}
}
