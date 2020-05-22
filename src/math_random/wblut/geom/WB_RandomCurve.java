package wblut.geom;

/**
 *
 */
public class WB_RandomCurve extends WB_RandomFactory {
	/**  */
	private final WB_Curve curve;
	/**  */
	private final double start, end;

	/**
	 *
	 *
	 * @param curve
	 * @param start
	 * @param end
	 */
	public WB_RandomCurve(final WB_Curve curve, final double start, final double end) {
		super();
		this.start = start;
		this.end = end;
		this.curve = curve;
	}

	/**
	 *
	 *
	 * @param curve
	 * @param start
	 * @param end
	 * @param seed
	 */
	public WB_RandomCurve(final WB_Curve curve, final double start, final double end, final long seed) {
		super(seed);
		this.start = start;
		this.end = end;
		this.curve = curve;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point nextPointImp() {
		final double d = start + (end - start) * randomGen.nextDouble();
		return curve.getPointOnCurve(d);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		final double d = start + (end - start) * randomGen.nextDouble();
		return curve.getPointOnCurve(d);
	}
}
