/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 * This work is published from Belgium.
 * (http://creativecommons.org/publicdomain/zero/1.0/)
 */
package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_Line implements WB_Curve, WB_Geometry {
	protected WB_Point	origin;
	protected WB_Vector	direction;

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
	public WB_Line(final double ox, final double oy, final double oz,
			final double dx, final double dy, final double dz) {
		this(new WB_Point(ox, oy, oz), new WB_Vector(dx, dy, dz));
	}

	/**
	 * 
	 * @param ox
	 * @param oy
	 * @param dx
	 * @param dy
	 */
	public WB_Line(final double ox, final double oy, final double dx,
			final double dy) {
		this(new WB_Point(ox, oy), new WB_Vector(dx, dy));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
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

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curvePoint(double)
	 */
	@Override
	public WB_Point getPointOnCurve(final double u) {
		return this.getPoint(u);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curveDirection(double)
	 */
	@Override
	public WB_Vector getDirectionOnCurve(final double u) {
		return new WB_Vector(direction);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curveDerivative(double)
	 */
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

	/**
	 * a.x+b.y+c=0
	 *
	 * @return a for a 2D line
	 */
	public double a() {
		return -direction.yd();
	}

	/**
	 * a.x+b.y+c=0
	 *
	 * @return b for a 2D line
	 */
	public double b() {
		return direction.xd();
	}

	/**
	 * a.x+b.y+c=0
	 *
	 * @return c for a 2D line
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
	 * Get vector perpendicular to the line
	 *
	 * @return
	 */
	public WB_Vector getNormal() {
		WB_Vector n = new WB_Vector(0, 0, 1);
		n = n.cross(direction);
		final double d = n.normalizeSelf();
		if (WB_Epsilon.isZero(d)) {
			n = new WB_Vector(1, 0, 0);
		}
		return n;
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
	public void getParametricPointInto(final double t,
			final WB_MutableCoord result) {
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
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Line)) {
			return false;
		}
		return origin.equals(((WB_Line) o).getOrigin())
				&& direction.equals(((WB_Line) o).getDirection());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * origin.hashCode() + direction.hashCode();
	}

	@Override
	public WB_Line apply2D(WB_Transform2D T) {
		return new WB_Line(origin.apply2D(T), direction.apply2D(T));
	}

	@Override
	public WB_Line apply2DSelf(WB_Transform2D T) {
		origin.apply2DSelf(T);
		direction.apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Line apply(WB_Transform3D T) {
		return new WB_Line(origin.apply(T), direction.apply(T));
	}

	@Override
	public WB_Line applySelf(WB_Transform3D T) {
		origin.applySelf(T);
		direction.applySelf(T);
		return this;
	}
}
