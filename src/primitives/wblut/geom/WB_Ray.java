/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import wblut.math.WB_Math;

/**
 *
 */
public class WB_Ray extends WB_Line {
	/**
	 *
	 *
	 * @return
	 */
	public static WB_Ray X() {
		return new WB_Ray(0, 0, 0, 1, 0, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Ray Y() {
		return new WB_Ray(0, 0, 0, 0, 1, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Ray Z() {
		return new WB_Ray(0, 0, 0, 0, 0, 1);
	}

	/**
	 *
	 */
	public WB_Ray() {
		origin = new WB_Point();
		direction = new WB_Vector(1, 0, 0);
	}

	/**
	 *
	 *
	 * @param o
	 * @param d
	 */
	public WB_Ray(final WB_Coord o, final WB_Coord d) {
		origin = new WB_Point(o);
		direction = new WB_Vector(d);
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
	public WB_Ray(final double ox, final double oy, final double oz,
			final double dx, final double dy, final double dz) {
		origin = new WB_Point(ox, oy, oz);
		final WB_Vector dn = new WB_Vector(dx, dy, dz);
		dn.normalizeSelf();
		direction = dn;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ray: " + origin.toString() + " " + direction.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Linear#set(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public void set(final WB_Coord o, final WB_Coord d) {
		origin = new WB_Point(o);
		final WB_Vector dn = new WB_Vector(d);
		dn.normalizeSelf();
		direction = dn;
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 */
	@Override
	public void setFromPoints(final WB_Coord p1, final WB_Coord p2) {
		origin = new WB_Point(p1);
		final WB_Vector dn = new WB_Vector(p1, p2);
		dn.normalizeSelf();
		direction = dn;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Linear#getPointOnLine(double)
	 */
	@Override
	public WB_Point getPoint(final double t) {
		final WB_Point result = new WB_Point(direction);
		result.scaleSelf(WB_Math.max(0, t));
		result.addSelf(origin);
		return result;
	}

	@Override
	public void getPointInto(final double t, final WB_MutableCoord p) {
		if (t > 0) {
			p.set(direction.mul(t).addSelf(origin));
		} else {
			p.set(origin);
		}
	}

	@Override
	public WB_Point getParametricPoint(final double t) {
		final WB_Point result = new WB_Point(direction);
		result.scaleSelf(WB_Math.max(0, t));
		result.addSelf(origin);
		return result;
	}

	@Override
	public void getParametricPointInto(final double t,
			final WB_MutableCoord p) {
		if (t > 0) {
			p.set(direction.mul(t).addSelf(origin));
		} else {
			p.set(origin);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Linear#getOrigin()
	 */
	@Override
	public WB_Coord getOrigin() {
		return origin;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Linear#getDirection()
	 */
	@Override
	public WB_Coord getDirection() {
		return direction;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curvePoint(double)
	 */
	@Override
	public WB_Point getPointOnCurve(final double u) {
		if (u < 0) {
			return null;
		}
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
		return 0;
	}

	@Override
	public double getUpperU() {
		return Double.POSITIVE_INFINITY;
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
		if (!(o instanceof WB_Ray)) {
			return false;
		}
		return origin.equals(((WB_Ray) o).getOrigin())
				&& direction.equals(((WB_Ray) o).getDirection());
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
	public WB_Ray apply2D(WB_Transform2D T) {
		return new WB_Ray(origin.apply2D(T), direction.apply2D(T));
	}

	@Override
	public WB_Ray apply2DSelf(WB_Transform2D T) {
		origin.apply2DSelf(T);
		direction.apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Ray apply(WB_Transform3D T) {
		return new WB_Ray(origin.apply(T), direction.apply(T));
	}

	@Override
	public WB_Ray applySelf(WB_Transform3D T) {
		origin.applySelf(T);
		direction.applySelf(T);
		return this;
	}
}