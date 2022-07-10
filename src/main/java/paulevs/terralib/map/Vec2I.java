package paulevs.terralib.map;

public class Vec2I {
	private int x;
	private int z;
	
	public Vec2I() {}
	
	public Vec2I(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public Vec2I set(int x, int z) {
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
	protected Vec2I clone() {
		return new Vec2I(x, z);
	}
	
	@Override
	public int hashCode() {
		return (x & 65535) << 16 | (z & 65535);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof Vec2I) {
			Vec2I pos = (Vec2I) obj;
			return pos.x == x && pos.z == z;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("[%d,%d]", x, z);
	}
}
