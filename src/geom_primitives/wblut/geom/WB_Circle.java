package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_Circle implements WB_Transformable3D {
	private final WB_Point center;
	private final WB_Vector normal;
	private double radius, r2;
	private final WB_GeometryFactory3D geometryfactory = new WB_GeometryFactory3D();

	public WB_Circle() {
		center = geometryfactory.createPoint();
		normal = geometryfactory.createVector(0, 0, 1);
		radius = 1;
		r2 = 1;
	}

	public WB_Circle(final WB_Coord center, final double radius) {
		this.center = geometryfactory.createPoint(center);
		this.radius = WB_Math.fastAbs(radius);
		r2 = this.radius * this.radius;
		normal = geometryfactory.createVector(0, 0, 1);
	}

	public WB_Circle(final WB_Coord center, final WB_Coord normal, final double radius) {
		this.center = geometryfactory.createPoint(center);
		this.radius = WB_Math.fastAbs(radius);
		r2 = this.radius * this.radius;
		this.normal = geometryfactory.createNormalizedVector(normal);
	}

	public WB_Circle(final double x, final double y, final double r) {
		center = geometryfactory.createPoint(x, y);
		radius = WB_Math.fastAbs(r);
		normal = geometryfactory.createVector(0, 0, 1);
	}

	public double getRadius() {
		return radius;
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_Coord getNormal() {
		return normal;
	}

	@Override
	public WB_Circle apply(final WB_Transform3D T) {
		final WB_Point p = geometryfactory.createPoint(center).applyAsPointSelf(T);
		final WB_Point q = geometryfactory.createPoint(center).addSelf(radius, 0, 0).applyAsPointSelf(T);
		final double newradius = p.getDistance2D(q);
		return geometryfactory.createCircleWithRadius(p, geometryfactory.createVector(normal).applyAsNormalSelf(T),
				newradius);
	}

	@Override
	public WB_Circle applySelf(final WB_Transform3D T) {
		final WB_Point p = geometryfactory.createPoint(center).applyAsPointSelf(T);
		final WB_Point q = geometryfactory.createPoint(center).addSelf(radius, 0, 0).applyAsPointSelf(T);
		final double newradius = p.getDistance2D(q);
		center.set(p);
		normal.applyAsNormalSelf(T);
		radius = newradius;
		return this;
	}

	public void set(final WB_Circle c) {
		center.set(c.getCenter());
		normal.set(c.getNormal());
		radius = c.getRadius();
	}

	public void setCenter(final double x, final double y) {
		center.set(x, y);
	}

	public void setCenter(final double x, final double y, final double z) {
		center.set(x, y, z);
	}

	public void setCenter(final WB_Coord c) {
		center.set(c);
	}

	public void setNormal(final double x, final double y, final double z) {
		normal.set(x, y, z);
	}

	public void setNormal(final WB_Coord c) {
		normal.set(c);
	}

	public void setRadius(final double radius) {
		this.radius = radius;
		r2 = this.radius * this.radius;
	}

	public void setDiameter(final double diameter) {
		this.radius = diameter * 0.5;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Circle)) {
			return false;
		}
		return WB_Epsilon.isEqualAbs(radius, ((WB_Circle) o).getRadius()) && center.equals(((WB_Circle) o).getCenter())
				&& normal.equals(((WB_Circle) o).getNormal());
	}

	@Override
	public int hashCode() {
		return 31 * (31 * center.hashCode() + hashCode(radius)) + normal.hashCode();
	}

	private int hashCode(final double v) {
		final long tmp = Double.doubleToLongBits(v);
		return (int) (tmp ^ tmp >>> 32);
	}

	public boolean contains(final WB_Coord p) {
		return center.getSqDistance2D(p) <= r2;
	}

	public WB_Point[] getPoints(final int n, final double phase) {
		final WB_Plane P = new WB_Plane(center, normal);
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		final double da = 2.0 * Math.PI / n;
		final WB_Point[] result = new WB_Point[n];
		WB_Point p;
		for (int i = 0; i < n; i++) {
			p = new WB_Point(radius * Math.cos(i * da + phase), radius * Math.sin(i * da + phase));
			result[i] = p.applySelf(T);
		}
		return result;
	}

	public WB_Point[] getPoints(final int n) {
		final WB_Plane P = new WB_Plane(center, normal);
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		final double da = 2.0 * Math.PI / n;
		final WB_Point[] result = new WB_Point[n];
		WB_Point p;
		for (int i = 0; i < n; i++) {
			p = new WB_Point(radius * Math.cos(i * da), radius * Math.sin(i * da));
			result[i] = p.applySelf(T);
		}
		return result;
	}

	public double[] getPointsAsArray(final int n) {
		final WB_Plane P = new WB_Plane(center, normal);
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(new WB_CoordinateSystem(P));
		final double da = 2.0 * Math.PI / n;
		final double[] result = new double[3 * n];
		WB_Point p;
		int id = 0;
		for (int i = 0; i < n; i++) {
			p = new WB_Point(radius * Math.cos(i * da), radius * Math.sin(i * da));
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

	@Override
	public WB_Circle apply2D(final WB_Transform2D T) {
		final WB_Point p = geometryfactory.createPoint(center).applyAsPoint2DSelf(T);
		final WB_Point q = geometryfactory.createPoint(center).addSelf(radius, 0, 0).applyAsPoint2DSelf(T);
		final double newradius = p.getDistance2D(q);
		return geometryfactory.createCircleWithRadius(p, geometryfactory.createVector(normal).applyAsNormal2DSelf(T),
				newradius);
	}

	@Override
	public WB_Circle apply2DSelf(final WB_Transform2D T) {
		final WB_Point p = geometryfactory.createPoint(center).applyAsPoint2DSelf(T);
		final WB_Point q = geometryfactory.createPoint(center).addSelf(radius, 0, 0).applyAsPoint2DSelf(T);
		final double newradius = p.getDistance2D(q);
		center.set(p);
		normal.applyAsNormal2DSelf(T);
		radius = newradius;
		return this;
	}
}
