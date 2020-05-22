package wblut.geom;

/**
 *
 */
public class WB_RandomSquare extends WB_RandomFactory {
	/**  */
	private double S;

	/**
	 *
	 */
	public WB_RandomSquare() {
		super();
		S = 1.0;
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomSquare(final long seed) {
		super(seed);
		S = 1.0;
	}

	/**
	 *
	 *
	 * @param S
	 * @return
	 */
	public WB_RandomSquare setSize(final double S) {
		this.S = S;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point nextPointImp() {
		return new WB_Point(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(), 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		return new WB_Vector(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(), 0);
	}
}
