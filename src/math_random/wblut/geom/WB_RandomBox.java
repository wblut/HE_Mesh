package wblut.geom;

public class WB_RandomBox extends WB_RandomFactory {
	private double X, Y, Z;

	public WB_RandomBox() {
		super();
		X = Y = Z = 1.0;
	}

	public WB_RandomBox(final long seed) {
		super();
		X = Y = Z = 1.0;
	}

	public WB_RandomBox setSize(final double X, final double Y, final double Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		return this;
	}

	public WB_RandomBox set(final WB_AABB AABB) {
		this.X = AABB.getWidth();
		this.Y = AABB.getHeight();
		this.Z = AABB.getDepth();
		setOffset(AABB.getCenterX(), AABB.getCenterY(), AABB.getCenterZ());
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		return new WB_Point(X * randomGen.nextCenteredDouble(), Y * randomGen.nextCenteredDouble(),
				Z * randomGen.nextCenteredDouble());
	}

	@Override
	public WB_Vector nextVectorImp() {
		return new WB_Vector(X * randomGen.nextCenteredDouble(), Y * randomGen.nextCenteredDouble(),
				Z * randomGen.nextCenteredDouble());
	}
}
