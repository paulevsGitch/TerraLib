package paulevs.terralib.map;

public class ChunkPos {
	private int x;
	private int z;
	
	public ChunkPos() {}
	
	public ChunkPos(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public ChunkPos set(int x, int z) {
		this.x = x;
		this.z = z;
		return this;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	protected ChunkPos clone() {
		return new ChunkPos(x, z);
	}
	
	@Override
	public int hashCode() {
		return (x & 65535) << 16 | (z & 65535);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof ChunkPos) {
			ChunkPos pos = (ChunkPos) obj;
			return pos.x == x && pos.z == z;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("[%d,%d]", x, z);
	}
}
