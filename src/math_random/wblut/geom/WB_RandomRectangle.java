package wblut.geom;

public class WB_RandomRectangle extends WB_RandomFactory {
	private double X, Y;

	public WB_RandomRectangle() {
		super();
		X = 1.0;
		Y = 1.0;
	}

	public WB_RandomRectangle(final long seed) {
		super(seed);
		X = 1.0;
		Y = 1.0;
	}

	public WB_RandomRectangle setSize(final double X, final double Y) {
		this.X = X;
		this.Y = Y;
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		return new WB_Point(X * randomGen.nextCenteredDouble(), Y * randomGen.nextCenteredDouble(), 0);
	}

	@Override
	public WB_Vector nextVectorImp() {
		return new WB_Vector(X * randomGen.nextCenteredDouble(), Y * randomGen.nextCenteredDouble(), 0);
	}
}