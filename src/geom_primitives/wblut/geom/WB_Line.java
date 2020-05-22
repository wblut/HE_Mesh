package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_Line implements WB_Curve, WB_Transformable3D {
	/**  */
	protected WB_Point origin;
	/**  */
	protected WB_Vector direction;
	private WB_Vector u, v;

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Line X() {
		return new WB_Line(0, 0, 0, 1, 0, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Line Y() {
		return new WB_Line(0, 0, 0, 0, 1, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Line Z() {
		return new WB_Line(0, 0, 0, 0, 0, 1);
	}

	/**
	 *
	 */
	public WB_Line() {
		origin = new WB_Point();
		final WB_Vector dn = new WB_Vector(0, 0, 1);
		direction = dn;
		setAxes();
	}

	/**
	 *
	 *
	 * @param o
	 * @param d
	 */
	public WB_Line(final WB_Coord o, final WB_Coord d) {
		origin = new WB_Point(o);
		final WB_Vector dn = new WB_Vector(d);
		dn.normalizeSelf();
		direction = dn;
		setAxes();
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public WB_Line(final double ox, final double oy, final double oz, final double dx, final double dy,
			final double dz) {
		this(new WB_Point(ox, oy, oz), new WB_Vector(dx, dy, dz));
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param dx
	 * @param dy
	 */
	public WB_Line(final double ox, final double oy, final double dx, final double dy) {
		this(new WB_Point(ox, oy), new WB_Vector(dx, dy));
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "Line: " + origin.toString() + " " + direction.toString();
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 */
	public void setFromPoints(final WB_Coord p1, final WB_Coord p2) {
		set(p1, p2);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public double getT(final WB_Coord p) {
		double t = Double.NaN;
		final WB_Coord proj = WB_GeometryOp.getClosestPoint2D(p, this);
		final double x = WB_Math.fastAbs(direction.xd());
		final double y = WB_Math.fastAbs(direction.yd());
		if (x >= y) {
			t = (proj.xd() - origin.xd()) / direction.xd();
		} else {
			t = (proj.yd() - origin.yd()) / direction.yd();
		}
		return t;
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	@Override
	public WB_Point getPointOnCurve(final double u) {
		return this.getPoint(u);
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	@Override
	public WB_Vector getDirectionOnCurve(final double u) {
		return new WB_Vector(direction);
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	@Override
	public WB_Vector getDerivative(final double u) {
		return new WB_Vector(direction);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLowerU() {
		return Double.NEGATIVE_INFINITY;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getUpperU() {
		return Double.POSITIVE_INFINITY;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double a() {
		return -direction.yd();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double b() {
		return direction.xd();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double c() {
		return origin.xd() * direction.yd() - origin.yd() * direction.xd();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getDirection() {
		return direction;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector getNormal() {
		return getU();
	}

	public WB_Vector getTangent() {
		return getW();
	}

	public WB_Vector getBinormal() {
		return getV();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getOrigin() {
		return origin;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public WB_Point getParametricPoint(final double t) {
		final WB_Point result = new WB_Point(direction);
		result.scaleSelf(t);
		result.addSelf(origin);
		return result;
	}

	/**
	 *
	 *
	 * @param t
	 * @param result
	 */
	public void getParametricPointInto(final double t, final WB_MutableCoord result) {
		result.set(new WB_Vector(direction).mulSelf(t).addSelf(origin));
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public WB_Point getPoint(final double t) {
		final WB_Point result = new WB_Point(direction);
		result.scaleSelf(t);
		result.addSelf(origin);
		return result;
	}

	/**
	 *
	 *
	 * @param t
	 * @param result
	 */
	public void getPointInto(final double t, final WB_MutableCoord result) {
		result.set(new WB_Vector(direction).mulSelf(t).addSelf(origin));
	}

	/**
	 *
	 *
	 * @param o
	 * @param d
	 */
	protected void set(final WB_Coord o, final WB_Coord d) {
		origin = new WB_Point(o);
		final WB_Vector dn = new WB_Vector(d);
		dn.normalizeSelf();
		direction = dn;
		setAxes();
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		return 31 * origin.hashCode() + direction.hashCode();
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Line apply2D(final WB_Transform2D T) {
		return new WB_Line(origin.apply2D(T), direction.apply2D(T));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Line apply2DSelf(final WB_Transform2D T) {
		origin.apply2DSelf(T);
		direction.apply2DSelf(T);
		setAxes();
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Line apply(final WB_Transform3D T) {
		return new WB_Line(origin.apply(T), direction.apply(T));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Line applySelf(final WB_Transform3D T) {
		origin.applySelf(T);
		direction.applySelf(T);
		setAxes();
		return this;
	}

	/**
	 *
	 */
	private void setAxes() {
		u = WB_Vector.cross(WB_Vector.Z(), direction);
		if (WB_Epsilon.isZeroSq(u.dot(u))) {
			u = WB_Vector.cross(WB_Vector.Y(), direction);
		}
		u.normalizeSelf();
		v = direction.cross(u);
	}

	public void overrideUV(final WB_Coord u, final WB_Coord v) {
		this.u = new WB_Vector(u);
		this.v = new WB_Vector(v);
	}

	/**
	 *
	 *
	 * @return
	 */
	// Return copy of u coordinate axis in world coordinates
	public WB_Vector getU() {
		return u.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	// Return copy of v coordinate axis in world coordinates
	public WB_Vector getV() {
		return v.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	// Return copy of w coordinate axis in world coordinates
	public WB_Vector getW() {
		return new WB_Vector(getDirection());
	}
}
