/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_Sphere {
	/** Center. */
	WB_Point center;
	/** Radius. */
	double radius, r2;

	/**
	 *
	 */
	private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	/**
	 *
	 */
	public WB_Sphere() {
		this.center = geometryfactory.createPoint();
		this.radius = 0;
		r2 = radius * radius;
	}

	/**
	 * Instantiates a new WB_Circle.
	 *
	 * @param center
	 * @param radius
	 */
	public WB_Sphere(final WB_Coord center, final double radius) {
		this.center = geometryfactory.createPoint(center);
		this.radius = WB_Math.fastAbs(radius);
		r2 = radius * radius;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Sphere)) {
			return false;
		}
		return WB_Epsilon.isEqualAbs(radius, ((WB_Sphere) o).getRadius()) && center.equals(((WB_Sphere) o).getCenter());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * center.hashCode() + hashCode(radius);
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

	public WB_Sphere apply(final WB_Transform3D T) {
		return geometryfactory.createSphereWithRadius(center.applyAsPoint(T), radius);
	}

	/**
	 * Get copy.
	 *
	 * @return copy
	 */
	public WB_Sphere get() {
		return new WB_Sphere(center, radius);
	}

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public WB_Coord getCenter() {
		return center;
	}

	/**
	 * Sets the center.
	 *
	 * @param c
	 *            the new center
	 */
	public void setCenter(final WB_Coord c) {
		this.center = new WB_Point(c);
	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius.
	 *
	 * @param r
	 *            the new radius
	 */
	public void setRadius(final double r) {
		this.radius = r;
		r2 = this.radius * this.radius;
	}

	/**
	 * Grow sphere to include point.
	 *
	 * @param p
	 *            point to include
	 */
	public void growSpherebyPoint(final WB_Coord p) {
		final WB_Vector d = WB_Point.subToVector3D(p, center);
		final double dist2 = d.getSqLength();
		if (dist2 > radius * radius) {
			final double dist = Math.sqrt(dist2);
			final double newRadius = (radius + dist) * 0.5;
			final double k = (newRadius - radius) / dist;
			radius = newRadius;
			center.addSelf(k * d.xd(), k * d.yd(), k * d.zd());
		}
	}

	/**
	 * Project point to sphere.
	 *
	 * @param v
	 *            the v
	 * @return point projected to sphere
	 */
	public WB_Coord projectToSphere(final WB_Coord v) {
		final WB_Point vc = new WB_Point(v).sub(center);
		final double er = vc.normalizeSelf();
		if (WB_Epsilon.isZero(er)) {
			return null;
		}
		return center.addMul(radius, vc);
	}

	public boolean contains(final WB_Coord p) {
		return center.getSqDistance(p) <= r2;

	}
}
