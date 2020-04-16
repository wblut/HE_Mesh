package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_Line implements WB_Curve, WB_Transformable3D {
	protected WB_Point origin;
	protected WB_Vector direction;

	public static WB_Line X() {
		return new WB_Line(0, 0, 0, 1, 0, 0);
	}

	public static WB_Line Y() {
		return new WB_Line(0, 0, 0, 0, 1, 0);
	}

	public static WB_Line Z() {
		return new WB_Line(0, 0, 0, 0, 0, 1);
	}

	public WB_Line() {
		origin = new WB_Point();
		final WB_Vector dn = new WB_Vector(0, 0, 1);
		direction = dn;
	}

	public WB_Line(final WB_Coord o, final WB_Coord d) {
		origin = new WB_Point(o);
		final WB_Vector dn = new WB_Vector(d);
		dn.normalizeSelf();
		direction = dn;
	}

	public WB_Line(final double ox, final double oy, final double oz, final double dx, final double dy,
			final double dz) {
		this(new WB_Point(ox, oy, oz), new WB_Vector(dx, dy, dz));
	}

	public WB_Line(final double ox, final double oy, final double dx, final double dy) {
		this(new WB_Point(ox, oy), new WB_Vector(dx, dy));
	}

	@Override
	public String toString() {
		return "Line: " + origin.toString() + " " + direction.toString();
	}

	public void setFromPoints(final WB_Coord p1, final WB_Coord p2) {
		set(p1, p2);
	}

	public double getT(final WB_Coord p) {
		double t = Double.NaN;
		final WB_Coord proj = WB_GeometryOp2D.getClosestPoint2D(p, this);
		final double x = WB_Math.fastAbs(direction.xd());
		final double y = WB_Math.fastAbs(direction.yd());
		if (x >= y) {
			t = (proj.xd() - origin.xd()) / direction.xd();
		} else {
			t = (proj.yd() - origin.yd()) / direction.yd();
		}
		return t;
	}

	@Override
	public WB_Point getPointOnCurve(final double u) {
		return this.getPoint(u);
	}

	@Override
	public WB_Vector getDirectionOnCurve(final double u) {
		return new WB_Vector(direction);
	}

	@Override
	public WB_Vector getDerivative(final double u) {
		return new WB_Vector(direction);
	}

	@Override
	public double getLowerU() {
		return Double.NEGATIVE_INFINITY;
	}

	@Override
	public double getUpperU() {
		return Double.POSITIVE_INFINITY;
	}

	public double a() {
		return -direction.yd();
	}

	public double b() {
		return direction.xd();
	}

	public double c() {
		return origin.xd() * direction.yd() - origin.yd() * direction.xd();
	}

	public WB_Coord getDirection() {
		return direction;
	}

	public WB_Vector getNormal() {
		WB_Vector n = new WB_Vector(0, 0, 1);
		n = n.cross(direction);
		final double d = n.normalizeSelf();
		if (WB_Epsilon.isZero(d)) {
			n = new WB_Vector(1, 0, 0);
		}
		return n;
	}

	public WB_Coord getOrigin() {
		return origin;
	}

	public WB_Point getParametricPoint(final double t) {
		final WB_Point result = new WB_Point(direction);
		result.scaleSelf(t);
		result.addSelf(origin);
		return result;
	}

	public void getParametricPointInto(final double t, final WB_MutableCoord result) {
		result.set(new WB_Vector(direction).mulSelf(t).addSelf(origin));
	}

	public WB_Point getPoint(final double t) {
		final WB_Point result = new WB_Point(direction);
		result.scaleSelf(t);
		result.addSelf(origin);
		return result;
	}

	public void getPointInto(final double t, final WB_MutableCoord result) {
		result.set(new WB_Vector(direction).mulSelf(t).addSelf(origin));
	}

	protected void set(final WB_Coord o, final WB_Coord d) {
		origin = new WB_Point(o);
		final WB_Vector dn = new WB_Vector(d);
		dn.normalizeSelf();
		direction = dn;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Line)) {
			return false;
		}
		return origin.equals(((WB_Line) o).getOrigin()) && direction.equals(((WB_Line) o).getDirection());
	}

	@Override
	public int hashCode() {
		return 31 * origin.hashCode() + direction.hashCode();
	}

	@Override
	public WB_Line apply2D(final WB_Transform2D T) {
		return new WB_Line(origin.apply2D(T), direction.apply2D(T));
	}

	@Override
	public WB_Line apply2DSelf(final WB_Transform2D T) {
		origin.apply2DSelf(T);
		direction.apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Line apply(final WB_Transform3D T) {
		return new WB_Line(origin.apply(T), direction.apply(T));
	}

	@Override
	public WB_Line applySelf(final WB_Transform3D T) {
		origin.applySelf(T);
		direction.applySelf(T);
		return this;
	}
}
