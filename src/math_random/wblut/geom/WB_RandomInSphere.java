package wblut.geom;

/**
 *
 */
public class WB_RandomInSphere extends WB_RandomFactory {
	/**  */
	private double radius;

	/**
	 *
	 */
	public WB_RandomInSphere() {
		super();
		radius = 1.0;
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomInSphere(final long seed) {
		super(seed);
		radius = 1.0;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public WB_RandomInSphere setRadius(final double r) {
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
		final double elevation = Math.asin(2.0 * randomGen.nextDouble() - 1);
		final double azimuth = 2 * Math.PI * randomGen.nextDouble();
		final double r = radius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		return new WB_Point(r * Math.cos(elevation) * Math.cos(azimuth), r * Math.cos(elevation) * Math.sin(azimuth),
				r * Math.sin(elevation));
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		final double elevation = Math.asin(2.0 * randomGen.nextDouble() - 1);
		final double azimuth = 2 * Math.PI * randomGen.nextDouble();
		final double r = radius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		return new WB_Vector(r * Math.cos(elevation) * Math.cos(azimuth), r * Math.cos(elevation) * Math.sin(azimuth),
				r * Math.sin(elevation));
	}
}
