package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_Sphere {
	WB_Point center;
	double radius, r2;
	private final WB_GeometryFactory3D geometryfactory = new WB_GeometryFactory3D();

	public WB_Sphere() {
		this.center = geometryfactory.createPoint();
		this.radius = 0;
		r2 = radius * radius;
	}

	public WB_Sphere(final WB_Coord center, final double radius) {
		this.center = geometryfactory.createPoint(center);
		this.radius = WB_Math.fastAbs(radius);
		r2 = radius * radius;
	}

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

	@Override
	public int hashCode() {
		return 31 * center.hashCode() + hashCode(radius);
	}

	private int hashCode(final double v) {
		final long tmp = Double.doubleToLongBits(v);
		return (int) (tmp ^ tmp >>> 32);
	}

	public WB_Sphere apply(final WB_Transform3D T) {
		return geometryfactory.createSphereWithRadius(center.applyAsPoint(T), radius);
	}

	public WB_Sphere get() {
		return new WB_Sphere(center, radius);
	}

	public WB_Coord getCenter() {
		return center;
	}

	public void setCenter(final WB_Coord c) {
		this.center = new WB_Point(c);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(final double r) {
		this.radius = r;
		r2 = this.radius * this.radius;
	}

	public void growSpherebyPoint(final WB_Coord p) {
		final WB_Vector d = WB_Point.subToVector3D(p, center);
		final double dist2 = d.getSqLength3D();
		if (dist2 > radius * radius) {
			final double dist = Math.sqrt(dist2);
			final double newRadius = (radius + dist) * 0.5;
			final double k = (newRadius - radius) / dist;
			radius = newRadius;
			center.addSelf(k * d.xd(), k * d.yd(), k * d.zd());
		}
	}

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
