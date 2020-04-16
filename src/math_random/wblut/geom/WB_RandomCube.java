package wblut.geom;

public class WB_RandomCube extends WB_RandomFactory {
	private double S;

	public WB_RandomCube() {
		super();
		S = 1.0;
	}

	public WB_RandomCube(final long seed) {
		super(seed);
		S = 1.0;
	}

	public WB_RandomCube setSize(final double S) {
		this.S = S;
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		return new WB_Point(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(),
				S * randomGen.nextCenteredDouble());
	}

	@Override
	public WB_Vector nextVectorImp() {
		return new WB_Vector(S * randomGen.nextCenteredDouble(), S * randomGen.nextCenteredDouble(),
				S * randomGen.nextCenteredDouble());
	}
}
