package wblut.geom;

public class WB_RandomSquare extends WB_RandomFactory {
	private double S;

	public WB_RandomSquare() {
		super();
		S = 1.0;
	}

	public WB_RandomSquare(final long seed) {
		super(seed);
		S = 1.0;
	}

	public WB_RandomSquare setSize(final double S) {
		this.S = S;
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		return new WB_Point(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(), 0);
	}

	@Override
	public WB_Vector nextVectorImp() {
		return new WB_Vector(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(), 0);
	}
}
