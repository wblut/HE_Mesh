package wblut.geom;

public class WB_RandomCircle extends WB_RandomFactory {
	private double radius;

	public WB_RandomCircle() {
		radius = 1.0;
	}

	public WB_RandomCircle(final long seed) {
		radius = 1.0;
	}

	public WB_RandomCircle setRadius(final double r) {
		this.radius = r;
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(radius * Math.cos(t), radius * Math.sin(t), 0);
	}

	@Override
	public WB_Vector nextVectorImp() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(radius * Math.cos(t), radius * Math.sin(t), 0);
	}
}