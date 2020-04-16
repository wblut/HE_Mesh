package wblut.geom;

public class WB_RandomSegment extends WB_RandomFactory {
	WB_Point start;
	WB_Point end;

	public WB_RandomSegment(final WB_Coord start, final WB_Coord end) {
		super();
		this.start = new WB_Point(start);
		this.end = new WB_Point(end);
	}

	public WB_RandomSegment(final WB_Coord start, final WB_Coord end, final long seed) {
		super(seed);
		this.start = new WB_Point(start);
		this.end = new WB_Point(end);
	}

	@Override
	public WB_Point nextPointImp() {
		final double d = randomGen.nextDouble();
		return new WB_Point(start.xd() + d * (end.xd() - start.xd()), start.yd() + d * (end.yd() - start.yd()),
				start.zd() + d * (end.zd() - start.zd()));
	}

	@Override
	public WB_Vector nextVectorImp() {
		final double d = randomGen.nextDouble();
		return new WB_Vector(start.xd() + d * (end.xd() - start.xd()), start.yd() + d * (end.yd() - start.yd()),
				start.zd() + d * (end.zd() - start.zd()));
	}
}
