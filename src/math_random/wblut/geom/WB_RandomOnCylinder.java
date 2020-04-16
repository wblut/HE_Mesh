package wblut.geom;

public class WB_RandomOnCylinder extends WB_RandomFactory {
	private double radius, height;

	public WB_RandomOnCylinder() {
		super();
		radius = 1.0;
		height = 1.0;
	}

	public WB_RandomOnCylinder(final long seed) {
		super(seed);
		radius = 1.0;
		height = 1.0;
	}

	public WB_RandomOnCylinder setRadius(final double r) {
		radius = r;
		return this;
	}

	public WB_RandomOnCylinder setHeight(final double h) {
		height = h;
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(radius * Math.cos(t), radius * Math.sin(t), height * randomGen.nextCenteredDouble());
	}

	@Override
	public WB_Vector nextVectorImp() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(radius * Math.cos(t), radius * Math.sin(t), height * randomGen.nextCenteredDouble());
	}
}