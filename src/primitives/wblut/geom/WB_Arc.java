/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import wblut.math.WB_Epsilon;

public class WB_Arc implements WB_Curve {
	private WB_Point			start;
	private WB_Point			end;
	private WB_Point			center;
	private WB_Vector			normal;
	private double				radius;

	/**
	 *
	 */
	public WB_Arc(WB_Coord start, WB_Coord end, WB_Coord center) {
		this.start = new WB_Point(start);
		this.center = new WB_Point(center);
		radius = this.start.getDistance3D(center);
		this.end = WB_Point.sub(end, center);
		this.end.normalizeSelf();
		this.end.mulSelf(radius).addSelf(center);
		final WB_Vector r0 = new WB_Vector(center, start);
		final WB_Vector r1 = new WB_Vector(center, end);
		normal = r1.cross(r0);
		normal.normalizeSelf();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getCenter() {
		return center;
	}

	public WB_Coord getStart() {
		return start;
	}

	public WB_Coord getEnd() {
		return end;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getNormal() {
		return normal;
	}

	/**
	 *
	 *
	 * @param c
	 */
	public void set(final WB_Arc c) {
		center.set(c.getCenter());
		normal.set(c.getNormal());
		radius = c.getRadius();
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public void setCenter(final double x, final double y) {
		center.set(x, y);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setCenter(final double x, final double y, final double z) {
		center.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param c
	 */
	public void setCenter(final WB_Coord c) {
		center.set(c);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setNormal(final double x, final double y, final double z) {
		normal.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param c
	 */
	public void setNormal(final WB_Coord c) {
		normal.set(c);
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
		if (!(o instanceof WB_Arc)) {
			return false;
		}
		return WB_Epsilon.isEqual(radius, ((WB_Arc) o).getRadius())
				&& center.equals(((WB_Arc) o).getCenter())
				&& start.equals(((WB_Arc) o).getStart())
				&& end.equals(((WB_Arc) o).getEnd());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * (31 * (31 * center.hashCode() + hashCode(radius))
				+ start.hashCode()) + end.hashCode();
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	private int hashCode(final double v) {
		final long tmp = Double.doubleToLongBits(v);
		return (int) (tmp ^ tmp >>> 32);
	}

	public WB_Point[] getPoints(final int n, final double phase) {
		WB_Plane P = new WB_Plane(center, normal);
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		double da = 2.0 * Math.PI / n;
		WB_Point[] result = new WB_Point[n];
		WB_Point p;
		for (int i = 0; i < n; i++) {
			p = new WB_Point(radius * Math.cos(i * da + phase),
					radius * Math.sin(i * da + phase));
			result[i] = p.applySelf(T);
		}
		return result;
	}

	public WB_Point[] getPoints(final int n) {
		WB_Plane P = new WB_Plane(center, normal);
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		double da = 2.0 * Math.PI / n;
		WB_Point[] result = new WB_Point[n];
		WB_Point p;
		for (int i = 0; i < n; i++) {
			p = new WB_Point(radius * Math.cos(i * da),
					radius * Math.sin(i * da));
			result[i] = p.applySelf(T);
		}
		return result;
	}

	public double[] getPointsAsArray(final int n) {
		WB_Plane P = new WB_Plane(center, normal);
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		double da = 2.0 * Math.PI / n;
		double[] result = new double[3 * n];
		WB_Point p;
		int id = 0;
		for (int i = 0; i < n; i++) {
			p = new WB_Point(radius * Math.cos(i * da),
					radius * Math.sin(i * da));
			p.applySelf(T);
			result[id++] = p.xd();
			result[id++] = p.yd();
			result[id++] = p.zd();
		}
		return result;
	}

	public WB_Plane getPlane() {
		return new WB_Plane(center, normal);
	}

	public WB_Plane getPlane(final double d) {
		return new WB_Plane(center, normal, d);
	}

	public WB_Point getPointOnCurve(double u) {
		WB_Plane P = new WB_Plane(center, normal);
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		WB_Point p = new WB_Point(radius * Math.cos(u), radius * Math.sin(u));
		p.applySelf(T);
		return p;
	}

	public WB_Vector getDirectionOnCurve(double u) {
		WB_Plane P = new WB_Plane(center, normal);
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		WB_Point p = new WB_Point(-Math.sin(u), Math.cos(u));
		p.applySelf(T);
		return p;
	}

	public WB_Vector getDerivative(double u) {
		WB_Plane P = new WB_Plane(center, normal);
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		WB_Point p = new WB_Point(-radius * Math.sin(u), radius * Math.cos(u));
		p.applySelf(T);
		return p;
	}

	@Override
	public double getLowerU() {
		return 0;
	}

	@Override
	public double getUpperU() {
		return 1.0;
	}
}
