package wblut.geom;

/**
 *
 */
public class WB_RandomTube extends WB_RandomFactory {
	/**  */
	private double innerRadius;
	/**  */
	private double outerRadius;
	/**  */
	private double height;

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomTube(final long seed) {
		super(seed);
		innerRadius = 0.5;
		outerRadius = 1.0;
		height = 1.0;
	}

	/**
	 *
	 *
	 * @param ir
	 * @param or
	 * @return
	 */
	public WB_RandomTube setRadius(final double ir, final double or) {
		innerRadius = Math.min(Math.abs(ir), Math.abs(or));
		outerRadius = Math.max(Math.abs(ir), Math.abs(or));
		return this;
	}

	/**
	 *
	 *
	 * @param h
	 * @return
	 */
	public WB_RandomTube setHeight(final double h) {
		height = h;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point nextPointImp() {
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		}
		final double t = 2 * Math.PI * randomGen.nextDouble();
		final double h = height * randomGen.nextDouble();
		return new WB_Point(r * Math.cos(t), r * Math.sin(t), h);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		}
		final double t = 2 * Math.PI * randomGen.nextDouble();
		final double h = height * randomGen.nextDouble();
		return new WB_Vector(r * Math.cos(t), r * Math.sin(t), h);
	}
}