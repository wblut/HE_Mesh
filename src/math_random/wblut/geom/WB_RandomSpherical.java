package wblut.geom;

/**
 *
 */
public class WB_RandomSpherical extends WB_RandomFactory {
	/**
	 *
	 */
	public WB_RandomSpherical() {
		super();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomSpherical(final long seed) {
		super(seed);
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
		final double phi = Math.acos(z);
		final double theta = 2.0 * Math.PI * randomGen.nextDouble();
		return new WB_Point(phi, theta, 0.0);
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
		final double phi = Math.acos(z);
		final double theta = 2.0 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(phi, theta, 0.0);
	}
}
