package paulevs.terralib.sdf;

public class FlatSDF extends HeightSDF {
	private int height;
	
	public FlatSDF setHeight(int height) {
		this.height = height;
		return this;
	}
	
	@Override
	protected float getHeight(int x, int z) {
		return height;
	}
}
