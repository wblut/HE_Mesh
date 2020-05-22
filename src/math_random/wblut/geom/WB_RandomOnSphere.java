package wblut.geom;

/**
 *
 */
public class WB_RandomOnSphere extends WB_RandomFactory {
	/**  */
	private double radius;

	/**
	 *
	 */
	public WB_RandomOnSphere() {
		super();
		radius = 1.0;
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomOnSphere(final long seed) {
		super(seed);
		radius = 1.0;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public WB_RandomOnSphere setRadius(final double r) {
		radius = r;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point nextPointImp() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		final double r = radius * Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(r * Math.cos(t), r * Math.sin(t), radius * z);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		final double r = radius * Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(r * Math.cos(t), r * Math.sin(t), radius * z);
	}
}
