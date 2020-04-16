package wblut.geom;

public class WB_IndexedSegment extends WB_Segment {
	private int i1;
	private int i2;
	// private final WB_Coordinate[] points;

	public WB_IndexedSegment(final int i1, final int i2, final WB_Coord[] points) {
		super(points[i1], points[i2]);
		this.i1 = i1;
		this.i2 = i2;
	}

	protected WB_IndexedSegment(final int i1, final int i2, final WB_Coord p1, final WB_Coord p2) {
		super(p1, p2);
		this.i1 = i1;
		this.i2 = i2;
	}

	public int i1() {
		return i1;
	}

	public int i2() {
		return i2;
	}

	@Override
	public WB_IndexedSegment negate() {
		return new WB_IndexedSegment(i2, i1, endpoint, origin);
	}

	@Override
	public void reverse() {
		direction = new WB_Vector(direction).mulSelf(-1);
		final WB_Point tmpp = origin;
		origin = endpoint;
		endpoint = tmpp;
		final int tmp = i2;
		i2 = i1;
		i1 = tmp;
	}
}
