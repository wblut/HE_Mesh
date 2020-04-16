package wblut.geom;

public class WB_RandomDisk extends WB_RandomFactory {
	private double radius;

	public WB_RandomDisk() {
		super();
		radius = 1.0;
	}

	public WB_RandomDisk(final long seed) {
		super(seed);
		radius = 1.0;
	}

	public WB_RandomDisk setRadius(final double r) {
		radius = r;
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		final double r = radius * Math.sqrt(randomGen.nextDouble());
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(r * Math.cos(t), r * Math.sin(t), 0);
	}

	@Override
	public WB_Vector nextVectorImp() {
		final double r = radius * Math.sqrt(randomGen.nextDouble());
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(r * Math.cos(t), r * Math.sin(t), 0);
	}
}