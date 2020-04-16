package wblut.geom;

public class WB_PointHomogeneous extends WB_Point4D {
	public WB_PointHomogeneous() {
		super(0, 0, 0, 0);
	}

	public WB_PointHomogeneous(final double x, final double y, final double z) {
		super(x, y, z, 1);
	}

	public WB_PointHomogeneous(final double x, final double y, final double z, final double w) {
		super(x, y, z, w);
	}

	public WB_PointHomogeneous(final WB_PointHomogeneous v) {
		super(v);
	}

	public WB_PointHomogeneous get() {
		return new WB_PointHomogeneous(this);
	}

	public WB_PointHomogeneous(final WB_Coord v) {
		setX(v.xd());
		setY(v.yd());
		setZ(v.zd());
		setW(1);
	}

	public WB_PointHomogeneous(final WB_Coord p, final double w) {
		setX(p.xd() * w);
		setY(p.yd() * w);
		setZ(p.zd() * w);
		setW(w);
	}

	@Override
	public void set(final double x, final double y, final double z, final double w) {
		setX(x);
		setY(y);
		setZ(z);
		setW(w);
	}

	public void set(final WB_PointHomogeneous p) {
		setX(p.xd());
		setY(p.yd());
		setZ(p.zd());
		setW(p.wd());
	}

	public WB_Coord project() {
		if (w == 0) {
			return new WB_Vector(x, y, z);
		}
		final double iw = 1.0 / wd();
		return new WB_Point(xd() * iw, yd() * iw, zd() * iw);
	}

	public void setWeight(final double w) {
		final WB_Coord p = project();
		set(p.xd(), p.yd(), p.zd(), w);
	}

	public static WB_PointHomogeneous interpolate(final WB_PointHomogeneous p0, final WB_PointHomogeneous p1,
			final double t) {
		return new WB_PointHomogeneous(p0.xd() + t * (p1.xd() - p0.xd()), p0.yd() + t * (p1.yd() - p0.yd()),
				p0.zd() + t * (p1.zd() - p0.zd()), p0.wd() + t * (p1.wd() - p0.wd()));
	}
}
