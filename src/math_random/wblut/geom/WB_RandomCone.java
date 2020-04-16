package wblut.geom;

public class WB_RandomCone extends WB_RandomFactory {
	private double radius;
	private double ca;
	private WB_Transform3D T;

	public WB_RandomCone() {
		super();
		radius = 1.0;
		final double angle = Math.PI / 6.0;
		ca = Math.cos(0.5 * angle);
		T = new WB_Transform3D();
	}

	public WB_RandomCone(final long seed) {
		super(seed);
		radius = 1.0;
		final double angle = Math.PI / 6.0;
		ca = Math.cos(0.5 * angle);
		T = new WB_Transform3D();
	}

	public WB_RandomCone setRadius(final double r) {
		radius = r;
		return this;
	}

	public WB_RandomCone setAngle(final double a) {
		ca = Math.cos(0.5 * a);
		return this;
	}

	public WB_RandomCone setDirection(final WB_Coord dir) {
		final WB_Plane P = new WB_Plane(new WB_Point(), dir);
		final WB_CoordinateSystem CS = new WB_CoordinateSystem(P);
		T = new WB_Transform3D();
		T.addFromCSToWorld(CS);
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		final double eps = randomGen.nextDouble();
		final double z = eps * (1.0 - ca) + ca;
		final double r = radius * Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return T.applyAsPoint(new WB_Point(r * Math.cos(t), r * Math.sin(t), radius * z));
	}

	@Override
	public WB_Vector nextVectorImp() {
		final double eps = randomGen.nextDouble();
		final double z = eps * (1.0 - ca) + ca;
		final double r = radius * Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return T.applyAsVector(new WB_Vector(r * Math.cos(t), r * Math.sin(t), radius * z));
	}
}
