package wblut.geom;

/**
 *
 */
public class WB_RandomCube extends WB_RandomFactory {
	/**  */
	private double S;

	/**
	 *
	 */
	public WB_RandomCube() {
		super();
		S = 1.0;
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomCube(final long seed) {
		super(seed);
		S = 1.0;
	}

	/**
	 *
	 *
	 * @param S
	 * @return
	 */
	public WB_RandomCube setSize(final double S) {
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
		return new WB_Point(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(),
				S * randomGen.nextCenteredDouble());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		return new WB_Vector(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(),
				S * randomGen.nextCenteredDouble());
	}
}
