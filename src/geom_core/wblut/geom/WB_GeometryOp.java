package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wblut.geom.WB_AABBTree3D.WB_AABBNode3D;
import wblut.math.WB_Ease;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_GeometryOp {
	/**  */
	private static final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();

	private static final WB_Intersection NOINTERSECTION() {
		final WB_Intersection i = new WB_Intersection();
		i.intersection = false;
		i.sqDist = Float.POSITIVE_INFINITY;
		i.t1 = Double.NaN;
		i.t2 = Double.NaN;
		return i;
	}

	/**
	 *
	 *
	 * @param S
	 * @param P
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Segment S, final WB_Plane P) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(), S.getOrigin());
		final double denom = P.getNormal().dot(ab);
		if (!WB_Epsilon.isZero(denom)) {
			double t = (-P.d() - P.getNormal().dot(S.getOrigin())) / P.getNormal().dot(ab);
			if (t >= -WB_Epsilon.EPSILON && t <= 1.0 + WB_Epsilon.EPSILON) {
				t = WB_Epsilon.clampEpsilon(t, 0, 1);
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				i.t1 = t;
				i.t2 = t;
				i.object = S.getParametricPoint(t);
				i.dimension = 0;
				i.sqDist = 0;
				return i;
			}
			return NOINTERSECTION(t, t);
		}
		return NOINTERSECTION();
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param P
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Coord a, final WB_Coord b, final WB_Plane P) {
		final WB_Vector ab = new WB_Vector(a, b);
		double t = (-P.d() - P.getNormal().dot(a)) / P.getNormal().dot(ab);
		if (t >= -WB_Epsilon.EPSILON && t <= 1.0 + WB_Epsilon.EPSILON) {
			t = WB_Epsilon.clampEpsilon(t, 0, 1);
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = t;
			i.t2 = t;
			i.object = new WB_Point(a.xd() + t * (b.xd() - a.xd()), a.yd() + t * (b.yd() - a.yd()),
					a.zd() + t * (b.zd() - a.zd()));
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		}
		return NOINTERSECTION(t, t);
	}

	/**
	 *
	 *
	 * @param R
	 * @param P
	 * @return
	 */
	// RAY-PLANE
	public static WB_Intersection getIntersection3D(final WB_Ray R, final WB_Plane P) {
		final WB_Coord ab = R.getDirection();
		final double denom = P.getNormal().dot(ab);
		if (!WB_Epsilon.isZero(denom)) {
			double t = (-P.d() - P.getNormal().dot(R.getOrigin())) / denom;
			if (t >= -WB_Epsilon.EPSILON) {
				t = WB_Epsilon.clampEpsilon(t, 0, Double.POSITIVE_INFINITY);
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				i.t1 = t;
				i.t2 = t;
				i.object = R.getPoint(t);
				i.dimension = 0;
				i.sqDist = 0;
				return i;
			}
			return NOINTERSECTION(t, t);
		}
		return NOINTERSECTION();
	}

	/**
	 *
	 *
	 * @param R
	 * @param aabb
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Ray R, final WB_AABB aabb) {
		final WB_Coord d = R.getDirection();
		final WB_Coord p = R.getOrigin();
		double tmin = 0.0;
		double tmax = Double.POSITIVE_INFINITY;
		if (WB_Epsilon.isZero(d.xd())) {
			if (p.xd() < aabb.getMinX() || p.xd() > aabb.getMaxX()) {
				return NOINTERSECTION();
			}
		} else {
			final double ood = 1.0 / d.xd();
			double t1 = (aabb.getMinX() - p.xd()) * ood;
			double t2 = (aabb.getMaxX() - p.xd()) * ood;
			if (t1 > t2) {
				final double tmp = t1;
				t1 = t2;
				t2 = tmp;
			}
			tmin = Math.max(tmin, t1);
			tmax = Math.min(tmax, t2);
			if (tmin > tmax) {
				return NOINTERSECTION();
			}
		}
		if (WB_Epsilon.isZero(d.yd())) {
			if (p.yd() < aabb.getMinY() || p.yd() > aabb.getMaxY()) {
				return NOINTERSECTION();
			}
		} else {
			final double ood = 1.0 / d.yd();
			double t1 = (aabb.getMinY() - p.yd()) * ood;
			double t2 = (aabb.getMaxY() - p.yd()) * ood;
			if (t1 > t2) {
				final double tmp = t1;
				t1 = t2;
				t2 = tmp;
			}
			tmin = Math.max(tmin, t1);
			tmax = Math.min(tmax, t2);
			if (tmin > tmax) {
				return NOINTERSECTION();
			}
		}
		if (WB_Epsilon.isZero(d.zd())) {
			if (p.zd() < aabb.getMinZ() || p.zd() > aabb.getMaxZ()) {
				return NOINTERSECTION();
			}
		} else {
			final double ood = 1.0 / d.zd();
			double t1 = (aabb.getMinZ() - p.zd()) * ood;
			double t2 = (aabb.getMaxZ() - p.zd()) * ood;
			if (t1 > t2) {
				final double tmp = t1;
				t1 = t2;
				t2 = tmp;
			}
			tmin = Math.max(tmin, t1);
			tmax = Math.min(tmax, t2);
			if (tmin > tmax) {
				return NOINTERSECTION();
			}
		}
		final WB_Intersection i = new WB_Intersection();
		i.intersection = true;
		i.t1 = tmin;
		i.t2 = 0;
		i.object = R.getPoint(tmin);
		i.dimension = 0;
		i.sqDist = getSqDistance3D(p, (WB_Point) i.object);
		return i;
	}

	/**
	 *
	 *
	 * @param L
	 * @param P
	 * @return
	 */
	// LINE-PLANE
	public static WB_Intersection getIntersection3D(final WB_Line L, final WB_Plane P) {
		final WB_Coord ab = L.getDirection();
		final double denom = P.getNormal().dot(ab);
		if (!WB_Epsilon.isZero(denom)) {
			final double t = (-P.d() - P.getNormal().dot(L.getOrigin())) / denom;
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = t;
			i.t2 = t;
			i.object = L.getPoint(t);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			return NOINTERSECTION();
		}
	}

	/**
	 *
	 *
	 * @param P1
	 * @param P2
	 * @return
	 */
	// PLANE-PLANE
	public static WB_Intersection getIntersection3D(final WB_Plane P1, final WB_Plane P2) {
		final WB_Vector N1 = P1.getNormal();
		final WB_Vector N2 = P2.getNormal();
		final WB_Vector N1xN2 = new WB_Vector(N1.cross(N2));
		if (WB_Epsilon.isZeroSq(N1xN2.getSqLength3D())) {
			return NOINTERSECTION();
		} else {
			final double d1 = P1.d();
			final double d2 = P2.d();
			final double N1N2 = N1.dot(N2);
			final double det = 1 - N1N2 * N1N2;
			final double c1 = (d1 - d2 * N1N2) / det;
			final double c2 = (d2 - d1 * N1N2) / det;
			final WB_Point O = new WB_Point(N1.mul(c1).addSelf(N2.mul(c2)));
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = 0;
			i.t2 = 0;
			i.object = new WB_Line(O, N1xN2);
			i.dimension = 1;
			i.sqDist = 0;
			return i;
		}
	}

	/**
	 *
	 *
	 * @param P1
	 * @param P2
	 * @param P3
	 * @return
	 */
	// PLANE-PLANE-PLANE
	public static WB_Intersection getIntersection3D(final WB_Plane P1, final WB_Plane P2, final WB_Plane P3) {
		final WB_Vector N1 = P1.getNormal().copy();
		final WB_Vector N2 = P2.getNormal().copy();
		final WB_Vector N3 = P3.getNormal().copy();
		final double denom = N1.dot(N2.cross(N3));
		if (WB_Epsilon.isZero(denom)) {
			return NOINTERSECTION();
		} else {
			final WB_Vector N1xN2 = N1.cross(N2);
			final WB_Vector N2xN3 = N2.cross(N3);
			final WB_Vector N3xN1 = N3.cross(N1);
			final double d1 = -P1.d();
			final double d2 = -P2.d();
			final double d3 = -P3.d();
			final WB_Point p = new WB_Point(N2xN3).mulSelf(d1);
			p.addSelf(N3xN1.mul(d2));
			p.addSelf(N1xN2.mul(d3));
			p.divSelf(denom);
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = 0;
			i.t2 = 0;
			i.object = p;
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		}
	}

	/**
	 *
	 *
	 * @param one
	 * @param other
	 * @return
	 */
	// AABB-AABB
	public static boolean checkIntersection3D(final WB_AABB one, final WB_AABB other) {
		if (one.getMaxX() < other.getMinX() || one.getMinX() > other.getMaxX()) {
			return false;
		}
		if (one.getMaxY() < other.getMinY() || one.getMinY() > other.getMaxY()) {
			return false;
		}
		if (one.getMaxZ() < other.getMinZ() || one.getMinZ() > other.getMaxZ()) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @param AABB
	 * @param P
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_AABB AABB, final WB_Plane P) {
		final WB_Point c = AABB.getMax().add(AABB.getMin()).mulSelf(0.5);
		final WB_Point e = AABB.getMax().sub(c);
		final double r = e.xd() * WB_Math.fastAbs(P.getNormal().xd()) + e.yd() * WB_Math.fastAbs(P.getNormal().yd())
				+ e.zd() * WB_Math.fastAbs(P.getNormal().zd());
		final double s = P.getNormal().dot(c) + P.d();
		return WB_Math.fastAbs(s) <= r;
	}

	/**
	 *
	 *
	 * @param AABB
	 * @param S
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_AABB AABB, final WB_Sphere S) {
		final double d2 = getSqDistance3D(S.getCenter(), AABB);
		return d2 <= S.getRadius() * S.getRadius();
	}

	/**
	 *
	 *
	 * @param T
	 * @param S
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Triangle T, final WB_Sphere S) {
		final WB_Point p = getClosestPoint3D(S.getCenter(), T);
		return p.subToVector3D(S.getCenter()).getSqLength3D() <= S.getRadius() * S.getRadius();
	}

	/**
	 *
	 *
	 * @param T
	 * @param AABB
	 * @return
	 */
	// TRIANGLE-AABB
	public static boolean checkIntersection3D(final WB_Triangle T, final WB_AABB AABB) {
		double p0, p1, p2, r;
		final WB_Point c = AABB.getMax().add(AABB.getMin()).mulSelf(0.5);
		final double e0 = (AABB.getMaxX() - AABB.getMinX()) * 0.5;
		final double e1 = (AABB.getMaxY() - AABB.getMinY()) * 0.5;
		final double e2 = (AABB.getMaxZ() - AABB.getMinZ()) * 0.5;
		final WB_Point v0 = new WB_Point(T.p1());
		final WB_Point v1 = new WB_Point(T.p2());
		final WB_Point v2 = new WB_Point(T.p3());
		v0.subSelf(c);
		v1.subSelf(c);
		v2.subSelf(c);
		final WB_Vector f0 = v1.subToVector3D(v0);
		final WB_Vector f1 = v2.subToVector3D(v1);
		final WB_Vector f2 = v0.subToVector3D(v2);
		// a00
		final WB_Vector a = new WB_Vector(0, -f0.zd(), f0.yd());// u0xf0
		if (a.isZero()) {
			a.set(0, v0.yd(), v0.zd());
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a01
		a.set(0, -f1.zd(), f1.yd());// u0xf1
		if (a.isZero()) {
			a.set(0, v1.yd(), v1.zd());
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a02
		a.set(0, -f2.zd(), f2.yd());// u0xf2
		if (a.isZero()) {
			a.set(0, v2.yd(), v2.zd());
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a10
		a.set(f0.zd(), 0, -f0.xd());// u1xf0
		if (a.isZero()) {
			a.set(v0.xd(), 0, v0.zd());
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a11
		a.set(f1.zd(), 0, -f1.xd());// u1xf1
		if (a.isZero()) {
			a.set(v1.xd(), 0, v1.zd());
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a12
		a.set(f2.zd(), 0, -f2.xd());// u1xf2
		if (a.isZero()) {
			a.set(v2.xd(), 0, v2.zd());
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a20
		a.set(-f0.yd(), f0.xd(), 0);// u2xf0
		if (a.isZero()) {
			a.set(v0.xd(), v0.yd(), 0);
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a21
		a.set(-f1.yd(), f1.xd(), 0);// u2xf1
		if (a.isZero()) {
			a.set(v1.xd(), v1.yd(), 0);
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		// a22
		a.set(-f2.yd(), f2.xd(), 0);// u2xf2
		if (a.isZero()) {
			a.set(v2.xd(), v2.yd(), 0);
		}
		if (!a.isZero()) {
			p0 = v0.dot(a);
			p1 = v1.dot(a);
			p2 = v2.dot(a);
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd()) + e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2), -WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		if (WB_Math.max(v0.xd(), v1.xd(), v2.xd()) < -e0 || WB_Math.max(v0.xd(), v1.xd(), v2.xd()) > e0) {
			return false;
		}
		if (WB_Math.max(v0.yd(), v1.yd(), v2.yd()) < -e1 || WB_Math.max(v0.yd(), v1.yd(), v2.yd()) > e1) {
			return false;
		}
		if (WB_Math.max(v0.zd(), v1.zd(), v2.zd()) < -e2 || WB_Math.max(v0.zd(), v1.zd(), v2.zd()) > e2) {
			return false;
		}
		WB_Vector n = f0.cross(f1);
		WB_Plane P;
		if (!n.isZero()) {
			P = new WB_Plane(n, n.dot(v0));
		} else {
			n = f0.cross(f2);
			n = f0.cross(n);
			if (!n.isZero()) {
				P = new WB_Plane(n, n.dot(v0));
			} else {
				final WB_Vector t = new WB_Point(T.p3()).subToVector3D(T.p1());
				final double a1 = WB_Vector.dot(T.p1(), t);
				final double a2 = WB_Vector.dot(T.p2(), t);
				final double a3 = WB_Vector.dot(T.p3(), t);
				if (a1 < WB_Math.min(a2, a3)) {
					if (a2 < a3) {
						return checkIntersection3D(new WB_Segment(T.p1(), T.p3()), AABB);
					} else {
						return checkIntersection3D(new WB_Segment(T.p1(), T.p2()), AABB);
					}
				} else if (a2 < WB_Math.min(a1, a3)) {
					if (a1 < a3) {
						return checkIntersection3D(new WB_Segment(T.p2(), T.p3()), AABB);
					} else {
						return checkIntersection3D(new WB_Segment(T.p2(), T.p1()), AABB);
					}
				} else {
					if (a1 < a2) {
						return checkIntersection3D(new WB_Segment(T.p3(), T.p2()), AABB);
					} else {
						return checkIntersection3D(new WB_Segment(T.p3(), T.p1()), AABB);
					}
				}
			}
		}
		return checkIntersection3D(AABB, P);
	}

	/**
	 *
	 *
	 * @param S
	 * @param AABB
	 * @return
	 */
	// SEGMENT-AABB
	public static boolean checkIntersection3D(final WB_Segment S, final WB_AABB AABB) {
		final WB_Vector e = AABB.getMax().subToVector3D(AABB.getMin());
		final WB_Vector d = WB_Vector.subToVector3D(S.getEndpoint(), S.getOrigin());
		final WB_Point m = new WB_Point(S.getEndpoint().xd() + S.getOrigin().xd() - AABB.getMinX() - AABB.getMaxX(),
				S.getEndpoint().yd() + S.getOrigin().yd() - AABB.getMinY() - AABB.getMaxY(),
				S.getEndpoint().zd() + S.getOrigin().zd() - AABB.getMinZ() - AABB.getMaxZ());
		double adx = WB_Math.fastAbs(d.xd());
		if (WB_Math.fastAbs(m.xd()) > e.xd() + adx) {
			return false;
		}
		double ady = WB_Math.fastAbs(d.yd());
		if (WB_Math.fastAbs(m.yd()) > e.yd() + ady) {
			return false;
		}
		double adz = WB_Math.fastAbs(d.zd());
		if (WB_Math.fastAbs(m.zd()) > e.zd() + adz) {
			return false;
		}
		adx += WB_Epsilon.EPSILON;
		ady += WB_Epsilon.EPSILON;
		adz += WB_Epsilon.EPSILON;
		if (WB_Math.fastAbs(m.yd() * d.zd() - m.zd() * d.yd()) > e.yd() * adz + e.zd() * ady) {
			return false;
		}
		if (WB_Math.fastAbs(m.zd() * d.xd() - m.xd() * d.zd()) > e.xd() * adz + e.zd() * adx) {
			return false;
		}
		if (WB_Math.fastAbs(m.xd() * d.yd() - m.yd() * d.xd()) > e.xd() * ady + e.yd() * adx) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	// SPHERE-SPHERE
	public static boolean checkIntersection3D(final WB_Sphere S1, final WB_Sphere S2) {
		final WB_Vector d = WB_Vector.sub(S1.getCenter(), S2.getCenter());
		final double d2 = d.getSqLength3D();
		final double radiusSum = S1.getRadius() + S2.getRadius();
		return d2 <= radiusSum * radiusSum;
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Sphere S1, final WB_Sphere S2) {
		final WB_Vector d = WB_Vector.sub(S2.getCenter(), S1.getCenter());
		final double dist = d.normalizeSelf();
		final double R = S1.getRadius();
		final double r = S2.getRadius();
		double disc = dist * dist - r * r + R * R;
		disc *= disc;
		disc = 4 * dist * dist * R * R - disc;
		if (disc < 0) {
			return NOINTERSECTION();
		}
		final double x = (dist * dist - r * r + R * R) / (2.0 * dist);
		if (WB_Epsilon.isZero(disc)) {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Point(S1.getCenter()).addMulSelf(x, d);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			final double a = Math.sqrt(disc) / (2.0 * dist);
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Circle(new WB_Point(S1.getCenter()).addMulSelf(x, d), d, a);
			i.dimension = 2;
			i.sqDist = 0;
			return i;
		}
	}

	/**
	 *
	 *
	 * @param S
	 * @param P
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Sphere S, final WB_Plane P) {
		final double d = WB_GeometryOp.getDistance3D(S.getCenter(), P);
		final double radius = S.getRadius();
		if (d > radius) {
			return NOINTERSECTION();
		}
		if (WB_Epsilon.isZero(d - radius)) {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = radius;
			i.t2 = 0;
			i.object = WB_GeometryOp.getClosestPoint3D(S.getCenter(), P);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			final double a = Math.sqrt(radius * radius - d * d);
			final WB_Intersection i = new WB_Intersection();
			final WB_Point p = WB_GeometryOp.getClosestPoint3D(S.getCenter(), P);
			i.intersection = true;
			i.t1 = d;
			i.t2 = 0;
			i.object = new WB_Circle(p, P.getNormal(), a);
			i.dimension = 2;
			i.sqDist = 0;
			return i;
		}
	}

	/**
	 *
	 *
	 * @param C1
	 * @param C2
	 * @return
	 */
	public static WB_Intersection getIntersection3DPlanar(final WB_Circle C1, final WB_Circle C2) {
		final WB_Vector d = WB_Vector.sub(C2.getCenter(), C1.getCenter());
		final double dist = d.normalizeSelf();
		final double R = C1.getRadius();
		final double r = C2.getRadius();
		double disc = dist * dist - r * r + R * R;
		disc *= disc;
		disc = 4 * dist * dist * R * R - disc;
		if (disc < 0) {
			return NOINTERSECTION();
		}
		final double x = (dist * dist - r * r + R * R) / (2.0 * dist);
		if (WB_Epsilon.isZero(disc)) {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Point(C1.getCenter()).addMulSelf(x, d);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			final double a = Math.sqrt(disc) / (2.0 * dist);
			final WB_Point p = new WB_Point(C1.getCenter()).addMulSelf(x, d);
			final WB_Vector v = d.cross(C1.getNormal());
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Segment(p.addMul(a, v), p.addMul(-a, v));
			i.dimension = 1;
			i.sqDist = 0;
			return i;
		}
	}

	/**
	 *
	 *
	 * @param S
	 * @param C
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Sphere S, final WB_Circle C) {
		final WB_Plane P = C.getPlane();
		final WB_Intersection is = getIntersection3D(S, P);
		if (!is.intersection) {
			return NOINTERSECTION();
		}
		if (is.dimension == 0) {
			final WB_Point p = (WB_Point) is.object;
			if (WB_Epsilon.isZero(S.getRadius() - p.getDistance3D(S.getCenter()))) {
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				i.t1 = S.getRadius();
				i.t2 = 0;
				i.object = p;
				i.dimension = 0;
				i.sqDist = 0;
				return i;
			} else {
				return NOINTERSECTION();
			}
		} else {
			final WB_Circle C2 = (WB_Circle) is.object;
			return getIntersection3DPlanar(C, C2);
		}
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @param S3
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Sphere S1, final WB_Sphere S2, final WB_Sphere S3) {
		final WB_Intersection is = getIntersection3D(S1, S2);
		if (!is.intersection) {
			return NOINTERSECTION();
		}
		if (is.dimension == 0) {
			final WB_Point p = (WB_Point) is.object;
			if (WB_Epsilon.isZero(S3.getRadius() - p.getDistance3D(S3.getCenter()))) {
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				i.t1 = S3.getRadius();
				i.t2 = 0;
				i.object = p;
				i.dimension = 0;
				i.sqDist = 0;
				return i;
			} else {
				return NOINTERSECTION();
			}
		} else {
			final WB_Circle C = (WB_Circle) is.object;
			return getIntersection3D(S3, C);
		}
	}

	/**
	 *
	 *
	 * @param R
	 * @param S
	 * @return
	 */
	// RAY-SPHERE
	public static boolean checkIntersection3D(final WB_Ray R, final WB_Sphere S) {
		final WB_Vector m = WB_Vector.subToVector3D(R.getOrigin(), S.getCenter());
		final double c = m.dot(m) - S.getRadius() * S.getRadius();
		if (c <= 0) {
			return true;
		}
		final double b = m.dot(R.getDirection());
		if (b >= 0) {
			return false;
		}
		final double disc = b * b - c;
		if (disc < 0) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @param R
	 * @param AABB
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Ray R, final WB_AABB AABB) {
		double t0 = 0;
		double t1 = Double.POSITIVE_INFINITY;
		final double irx = 1.0 / R.direction.xd();
		double tnear = (AABB.getMinX() - R.origin.xd()) * irx;
		double tfar = (AABB.getMaxX() - R.origin.xd()) * irx;
		double tmp = tnear;
		if (tnear > tfar) {
			tnear = tfar;
			tfar = tmp;
		}
		t0 = tnear > t0 ? tnear : t0;
		t1 = tfar < t1 ? tfar : t1;
		if (t0 > t1) {
			return false;
		}
		final double iry = 1.0 / R.direction.yd();
		tnear = (AABB.getMinY() - R.origin.yd()) * iry;
		tfar = (AABB.getMaxY() - R.origin.yd()) * iry;
		tmp = tnear;
		if (tnear > tfar) {
			tnear = tfar;
			tfar = tmp;
		}
		t0 = tnear > t0 ? tnear : t0;
		t1 = tfar < t1 ? tfar : t1;
		if (t0 > t1) {
			return false;
		}
		final double irz = 1.0 / R.direction.zd();
		tnear = (AABB.getMinZ() - R.origin.zd()) * irz;
		tfar = (AABB.getMaxZ() - R.origin.zd()) * irz;
		tmp = tnear;
		if (tnear > tfar) {
			tnear = tfar;
			tfar = tmp;
		}
		t0 = tnear > t0 ? tnear : t0;
		t1 = tfar < t1 ? tfar : t1;
		if (t0 > t1) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @param R
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Ray R, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(R, current.getAABB())) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param aabb
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_AABB aabb, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(aabb, current.getAABB())) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param tree1
	 * @param tree2
	 * @return
	 */
	public static List<WB_AABBNode3D[]> getIntersection3D(final WB_AABBTree3D tree1, final WB_AABBTree3D tree2) {
		final List<WB_AABBNode3D[]> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree1.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(current.getAABB(), tree2)) {
				if (current.isLeaf()) {
					for (final WB_AABBNode3D node : getIntersection3D(current.getAABB(), tree2)) {
						result.add(new WB_AABBNode3D[] { current, node });
					}
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param aabb
	 * @param tree
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_AABB aabb, final WB_AABBTree3D tree) {
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(aabb, current.getAABB())) {
				if (current.isLeaf()) {
					return true;
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	 *
	 * @param p
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Coord p, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (contains(p, current.getAABB())) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param L
	 * @param AABB
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Line L, final WB_AABB AABB) {
		double t0 = Double.NEGATIVE_INFINITY;
		double t1 = Double.POSITIVE_INFINITY;
		final double irx = 1.0 / L.direction.xd();
		double tnear = (AABB.getMinX() - L.origin.xd()) * irx;
		double tfar = (AABB.getMaxX() - L.origin.xd()) * irx;
		double tmp = tnear;
		if (tnear > tfar) {
			tnear = tfar;
			tfar = tmp;
		}
		t0 = tnear > t0 ? tnear : t0;
		t1 = tfar < t1 ? tfar : t1;
		if (t0 > t1) {
			return false;
		}
		final double iry = 1.0 / L.direction.yd();
		tnear = (AABB.getMinY() - L.origin.yd()) * iry;
		tfar = (AABB.getMaxY() - L.origin.yd()) * iry;
		tmp = tnear;
		if (tnear > tfar) {
			tnear = tfar;
			tfar = tmp;
		}
		t0 = tnear > t0 ? tnear : t0;
		t1 = tfar < t1 ? tfar : t1;
		if (t0 > t1) {
			return false;
		}
		final double irz = 1.0 / L.direction.zd();
		tnear = (AABB.getMinZ() - L.origin.zd()) * irz;
		tfar = (AABB.getMaxZ() - L.origin.zd()) * irz;
		tmp = tnear;
		if (tnear > tfar) {
			tnear = tfar;
			tfar = tmp;
		}
		t0 = tnear > t0 ? tnear : t0;
		t1 = tfar < t1 ? tfar : t1;
		if (t0 > t1) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @param L
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Line L, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(L, current.getAABB())) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param S
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Segment S, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(S, current.getAABB())) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param P
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Plane P, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(current.getAABB(), P)) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param T
	 * @param tree
	 * @return
	 */
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Triangle T, final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<>();
		queue.add(tree.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(T, current.getAABB())) {
				if (current.isLeaf()) {
					result.add(current);
				} else {
					if (current.getChildA() != null) {
						queue.add(current.getChildA());
					}
					if (current.getChildB() != null) {
						queue.add(current.getChildB());
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param P
	 * @return
	 */
	public static List<WB_Segment> getIntersection3D(final WB_Polygon poly, final WB_Plane P) {
		final List<WB_Segment> result = new ArrayList<>();
		final List<WB_Coord> splitVerts = new ArrayList<>();
		final int numVerts = poly.getNumberOfShellPoints();
		if (numVerts > 0) {
			WB_Coord a = poly.getPoint(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp.classifyPointToPlane3D(a, P);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				WB_Intersection i;
				b = poly.getPoint(n);
				bSide = WB_GeometryOp.classifyPointToPlane3D(b, P);
				if (bSide == WB_Classification.FRONT) {
					if (aSide == WB_Classification.BACK) {
						i = WB_GeometryOp.getIntersection3D(b, a, P);
						splitVerts.add((WB_Point) i.object);
					}
				} else if (bSide == WB_Classification.BACK) {
					if (aSide == WB_Classification.FRONT) {
						i = WB_GeometryOp.getIntersection3D(a, b, P);
						splitVerts.add((WB_Point) i.object);
					}
				}
				if (aSide == WB_Classification.ON) {
					splitVerts.add(a);
				}
				a = b;
				aSide = bSide;
			}
		}
		for (int i = 0; i < splitVerts.size(); i += 2) {
			if (i + 1 < splitVerts.size() && splitVerts.get(i + 1) != null) {
				result.add(new WB_Segment(splitVerts.get(i), splitVerts.get(i + 1)));
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Segment S1, final WB_Segment S2) {
		final WB_Vector d1 = new WB_Vector(S1.getEndpoint());
		d1.subSelf(S1.getOrigin());
		final WB_Vector d2 = new WB_Vector(S2.getEndpoint());
		d2.subSelf(S2.getOrigin());
		final WB_Vector r = new WB_Vector(S1.getOrigin());
		r.subSelf(S2.getOrigin());
		final double a = d1.dot(d1);
		final double e = d2.dot(d2);
		final double f = d2.dot(r);
		if (WB_Epsilon.isZero(a) && WB_Epsilon.isZero(e)) {
			// Both segments are degenerate
			final WB_Intersection i = new WB_Intersection();
			i.sqDist = r.getSqLength3D();
			i.intersection = WB_Epsilon.isZeroSq(i.sqDist);
			if (i.intersection) {
				i.dimension = 0;
				i.object = S1.getOrigin();
			} else {
				i.dimension = 1;
				i.object = new WB_Segment(S1.getOrigin(), S2.getOrigin());
			}
			return i;
		}
		if (WB_Epsilon.isZero(a)) {
			// First segment is degenerate
			final WB_Intersection i = new WB_Intersection();
			i.sqDist = r.getSqLength3D();
			i.intersection = WB_Epsilon.isZeroSq(i.sqDist);
			if (i.intersection) {
				i.dimension = 0;
				i.object = S1.getOrigin();
			} else {
				i.dimension = 1;
				i.object = new WB_Segment(S1.getOrigin(), getClosestPoint3D(S1.getOrigin(), S2));
			}
			return i;
		}
		if (WB_Epsilon.isZero(e)) {
			// Second segment is degenerate
			final WB_Intersection i = new WB_Intersection();
			i.sqDist = r.getSqLength3D();
			i.intersection = WB_Epsilon.isZeroSq(i.sqDist);
			if (i.intersection) {
				i.dimension = 0;
				i.object = S2.getOrigin();
			} else {
				i.dimension = 1;
				i.object = new WB_Segment(S2.getOrigin(), getClosestPoint3D(S2.getOrigin(), S1));
			}
			return i;
		}
		double t1 = 0;
		double t2 = 0;
		final double c = d1.dot(r);
		final double b = d1.dot(d2);
		final double denom = a * e - b * b;
		if (!WB_Epsilon.isZero(denom)) {
			// Non-parallel segments
			t1 = WB_Math.clamp((b * f - c * e) / denom, 0, 1);
		} else {
			// Parallel segments, non-parallel code handles case where
			// projections of segments are disjoint.
			final WB_Line L1 = new WB_Line(S1.getOrigin(), S1.getDirection());
			double s1 = 0;
			double e1 = getParameterOfPointOnLine3D(S1.getEndpoint(), L1);
			double s2 = getParameterOfPointOnLine3D(S2.getOrigin(), L1);
			double e2 = getParameterOfPointOnLine3D(S2.getEndpoint(), L1);
			double tmp;
			if (e2 < s2) {
				tmp = s2;
				s2 = e2;
				e2 = tmp;
			}
			if (s2 < s1) {
				tmp = s2;
				s2 = s1;
				s1 = tmp;
				tmp = e2;
				e2 = e1;
				e1 = tmp;
			}
			if (s2 < e1) {
				// Projections are overlapping
				final WB_Point start = L1.getPoint(s2);
				WB_Point end = L1.getPoint(Math.min(e1, e2));
				if (WB_Epsilon.isZeroSq(getSqDistance3D(S2.getOrigin(), L1))) {
					// Segments are overlapping
					final WB_Intersection i = new WB_Intersection();
					i.sqDist = getSqDistance3D(start, end);
					i.intersection = true;
					if (WB_Epsilon.isZeroSq(i.sqDist)) {
						i.dimension = 0;
						i.object = start;
					} else {
						i.dimension = 1;
						i.object = new WB_Segment(start, end);
					}
					return i;
				} else {
					final WB_Intersection i = new WB_Intersection();
					i.sqDist = getSqDistance3D(start, end);
					i.intersection = false;
					i.dimension = 1;
					start.addSelf(end);
					start.scaleSelf(0.5);
					end = new WB_Point(getClosestPoint3D(start, S2));
					i.object = new WB_Segment(start, end);
					return i;
				}
			}
			t1 = 0;
		}
		final double tnom = b * t1 + f;
		if (tnom < 0) {
			t1 = WB_Math.clamp(-c / a, 0, 1);
		} else if (tnom > e) {
			t2 = 1;
			t1 = WB_Math.clamp((b - c) / a, 0, 1);
		} else {
			t2 = tnom / e;
		}
		final WB_Intersection i = new WB_Intersection();
		final WB_Point p1 = S1.getParametricPoint(t1);
		final WB_Point p2 = S2.getParametricPoint(t2);
		i.sqDist = getSqDistance3D(p1, p2);
		i.intersection = WB_Epsilon.isZeroSq(i.sqDist);
		if (i.intersection) {
			i.dimension = 0;
			i.object = p1;
		} else {
			i.dimension = 1;
			i.object = new WB_Segment(p1, p2);
		}
		return i;
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Plane P) {
		final WB_Vector n = P.getNormal();
		final double t = n.dot(p) + P.d();
		return new WB_Point(p.xd() - t * n.xd(), p.yd() - t * n.yd(), p.zd() - t * n.zd());
	}

	/**
	 *
	 *
	 * @param P
	 * @param p
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Plane P, final WB_Coord p) {
		return getClosestPoint3D(P, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Segment S) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(), S.getOrigin());
		final WB_Vector ac = new WB_Vector(S.getOrigin(), p);
		double t = ac.dot(ab);
		if (t <= 0) {
			t = 0;
			return new WB_Point(S.getOrigin());
		} else {
			final double denom = S.getLength() * S.getLength();
			if (t >= denom) {
				t = 1;
				return new WB_Point(S.getEndpoint());
			} else {
				t = t / denom;
				return new WB_Point(S.getParametricPoint(t));
			}
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param PL
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_PolyLine PL) {
		double d2, d2min = Double.POSITIVE_INFINITY;
		WB_Point q = null, test = null;
		WB_Segment S;
		for (int s = 0; s < PL.getNumberSegments(); s++) {
			S = PL.getSegment(s);
			final WB_Vector ab = new WB_Vector(S.getOrigin(), S.getEndpoint());
			final WB_Vector ac = new WB_Vector(S.getOrigin(), p);
			double t = ac.dot(ab);
			if (t <= 0) {
				t = 0;
				test = new WB_Point(S.getOrigin());
			} else {
				final double denom = S.getLength() * S.getLength();
				if (t >= denom) {
					t = 1;
					test = new WB_Point(S.getEndpoint());
				} else {
					t = t / denom;
					test = new WB_Point(S.getParametricPoint(t));
				}
			}
			d2 = getSqDistance3D(p, test);
			if (d2 < d2min) {
				d2min = d2;
				q = test;
			}
		}
		return q;
	}

	/**
	 *
	 *
	 * @param S
	 * @param p
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Segment S, final WB_Coord p) {
		return getClosestPoint3D(p, S);
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static double getClosestPointParametric3D(final WB_Coord p, final WB_Segment S) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(), S.getOrigin());
		final WB_Vector ac = new WB_Vector(S.getOrigin(), p);
		double t = ac.dot(ab);
		if (t <= WB_Epsilon.EPSILON) {
			return 0;
		} else {
			final double denom = S.getLength() * S.getLength();
			if (t >= denom - WB_Epsilon.EPSILON) {
				t = 1;
				return 1;
			} else {
				t = t / denom;
				return t;
			}
		}
	}

	/**
	 *
	 *
	 * @param S
	 * @param p
	 * @return
	 */
	public static double getClosestPointParametric3D(final WB_Segment S, final WB_Coord p) {
		return getClosestPointParametric3D(p, S);
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static WB_Point getClosestPointToSegment3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		double t = ac.dot(ab);
		if (t <= 0) {
			t = 0;
			return new WB_Point(a);
		} else {
			final double denom = ab.dot(ab);
			if (t >= denom) {
				t = 1;
				return new WB_Point(b);
			} else {
				t = t / denom;
				return new WB_Point(a.xd() + t * ab.xd(), a.yd() + t * ab.yd(), a.zd() + t * ab.zd());
			}
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Line L) {
		final WB_Vector ca = new WB_Vector(p.xd() - L.getOrigin().xd(), p.yd() - L.getOrigin().yd(),
				p.zd() - L.getOrigin().zd());
		return L.getPoint(ca.dot(L.getDirection()));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static WB_Point getClosestPointToLine3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return getClosestPoint3D(p, new WB_Line(a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Ray R) {
		final WB_Vector ac = new WB_Vector(R.getOrigin(), p);
		double t = ac.dot(R.getDirection());
		if (t <= 0) {
			t = 0;
			return new WB_Point(R.getOrigin());
		} else {
			return new WB_Point(R.getPoint(t));
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static WB_Point getClosestPointToRay3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return getClosestPoint3D(p, new WB_Ray(a, new WB_Vector(a, b)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_AABB AABB) {
		final WB_Point result = new WB_Point();
		double v = p.xd();
		if (v < AABB.getMinX()) {
			v = AABB.getMinX();
		}
		if (v > AABB.getMaxX()) {
			v = AABB.getMaxX();
		}
		result.setX(v);
		v = p.yd();
		if (v < AABB.getMinY()) {
			v = AABB.getMinY();
		}
		if (v > AABB.getMaxY()) {
			v = AABB.getMaxY();
		}
		result.setY(v);
		v = p.zd();
		if (v < AABB.getMinZ()) {
			v = AABB.getMinZ();
		}
		if (v > AABB.getMaxZ()) {
			v = AABB.getMaxZ();
		}
		result.setZ(v);
		return result;
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @param result
	 */
	public static void getClosestPoint3D(final WB_Coord p, final WB_AABB AABB, final WB_MutableCoord result) {
		double v = p.xd();
		if (v < AABB.getMinX()) {
			v = AABB.getMinX();
		}
		if (v > AABB.getMaxX()) {
			v = AABB.getMaxX();
		}
		result.setX(v);
		v = p.yd();
		if (v < AABB.getMinY()) {
			v = AABB.getMinY();
		}
		if (v > AABB.getMaxY()) {
			v = AABB.getMaxY();
		}
		result.setY(v);
		v = p.zd();
		if (v < AABB.getMinZ()) {
			v = AABB.getMinZ();
		}
		if (v > AABB.getMaxZ()) {
			v = AABB.getMaxZ();
		}
		result.setZ(v);
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	// POINT-TRIANGLE
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Triangle T) {
		final WB_Vector ab = new WB_Point(T.p2()).subToVector3D(T.p1());
		final WB_Vector ac = new WB_Point(T.p3()).subToVector3D(T.p1());
		final WB_Vector ap = new WB_Vector(T.p1(), p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Point(T.p1());
		}
		final WB_Vector bp = new WB_Vector(T.p2(), p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Point(T.p2());
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Point(T.p1()).addSelf(ab.mulSelf(v));
		}
		final WB_Vector cp = new WB_Vector(T.p3(), p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Point(T.p3());
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Point(T.p1()).addSelf(ac.mulSelf(w));
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Point(T.p2()).addSelf(new WB_Point(T.p3()).subToVector3D(T.p2()).mulSelf(w));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return new WB_Point(T.p1()).addSelf(ab.mulSelf(v).addSelf(ac.mulSelf(w)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static WB_Point getClosestPointToTriangle3D(final WB_Coord p, final WB_Coord a, final WB_Coord b,
			final WB_Coord c) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, c);
		final WB_Vector ap = new WB_Vector(a, p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Point(a);
		}
		final WB_Vector bp = new WB_Vector(b, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Point(b);
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Point(a).addSelf(ab.mulSelf(v));
		}
		final WB_Vector cp = new WB_Vector(c, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Point(c);
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Point(a).addSelf(ac.mulSelf(w));
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Point(b).addSelf(new WB_Vector(b, c).mulSelf(w));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return new WB_Point(a).addSelf(ab.mulSelf(v).addSelf(ac.mulSelf(w)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static WB_Point getClosestPointOnPeriphery3D(final WB_Coord p, final WB_Triangle T) {
		final WB_Vector ab = new WB_Point(T.p2()).subToVector3D(T.p1());
		final WB_Vector ac = new WB_Point(T.p3()).subToVector3D(T.p1());
		final WB_Vector ap = new WB_Vector(T.p1(), p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Point(T.p1());
		}
		final WB_Vector bp = new WB_Vector(T.p2(), p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Point(T.p2());
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Point(T.p1()).addSelf(ab.mulSelf(v));
		}
		final WB_Vector cp = new WB_Vector(T.p3(), p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Point(T.p3());
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Point(T.p1()).addSelf(ac.mulSelf(w));
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Point(T.p2()).addSelf(new WB_Point(T.p3()).subToVector3D(T.p2()).mulSelf(w));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		final double u = 1 - v - w;
		// WB_Vector bc = new WB_Point(T.p3()).subToVector3D(T.p2());
		if (WB_Epsilon.isZero(u - 1)) {
			return new WB_Point(T.p1());
		}
		if (WB_Epsilon.isZero(v - 1)) {
			return new WB_Point(T.p2());
		}
		if (WB_Epsilon.isZero(w - 1)) {
			return new WB_Point(T.p3());
		}
		final WB_Point A = getClosestPointToSegment3D(p, T.p2(), T.p3());
		final double dA2 = getSqDistance3D(p, A);
		final WB_Point B = getClosestPointToSegment3D(p, T.p1(), T.p3());
		final double dB2 = getSqDistance3D(p, B);
		final WB_Point C = getClosestPointToSegment3D(p, T.p1(), T.p2());
		final double dC2 = getSqDistance3D(p, C);
		if (dA2 < dB2 && dA2 < dC2) {
			return A;
		} else if (dB2 < dA2 && dB2 < dC2) {
			return B;
		} else {
			return C;
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param tris
	 * @return
	 */
	// POINT-POLYGON
	public static WB_Point getClosestPoint3D(final WB_Coord p, final List<? extends WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint3D(p, T);
			final double d2 = getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param L1
	 * @param L2
	 * @return
	 */
	// LINE-LINE
	public static WB_Intersection getClosestPoint3D(final WB_Line L1, final WB_Line L2) {
		final double a = WB_Vector.dot(L1.getDirection(), L1.getDirection());
		final double b = WB_Vector.dot(L1.getDirection(), L2.getDirection());
		final WB_Vector r = WB_Vector.subToVector3D(L1.getOrigin(), L2.getOrigin());
		final double c = WB_Vector.dot(L1.getDirection(), r);
		final double e = WB_Vector.dot(L2.getDirection(), L2.getDirection());
		final double f = WB_Vector.dot(L2.getDirection(), r);
		double denom = a * e - b * b;
		if (WB_Epsilon.isZero(denom)) {
			final double t2 = r.dot(L1.getDirection());
			final WB_Point p2 = new WB_Point(L2.getPoint(t2));
			final double d2 = getSqDistance3D(L1.getOrigin(), p2);
			final WB_Intersection i = new WB_Intersection();
			i.intersection = false;
			i.t1 = 0;
			i.t2 = t2;
			i.object = new WB_Segment(L1.getOrigin(), p2);
			i.dimension = 1;
			i.sqDist = d2;
			return i;
		}
		denom = 1.0 / denom;
		final double t1 = (b * f - c * e) * denom;
		final double t2 = (a * f - b * c) * denom;
		final WB_Point p1 = new WB_Point(L1.getPoint(t1));
		final WB_Point p2 = new WB_Point(L2.getPoint(t2));
		final double d2 = getSqDistance3D(p1, p2);
		if (WB_Epsilon.isZeroSq(d2)) {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = t1;
			i.t2 = t2;
			i.dimension = 0;
			i.object = p1;
			i.sqDist = d2;
			return i;
		} else {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = true;
			i.t1 = t1;
			i.t2 = t2;
			i.dimension = 1;
			i.object = new WB_Segment(p1, p2);
			i.sqDist = d2;
			return i;
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	// POINT-TETRAHEDRON
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Tetrahedron T) {
		WB_Point closestPt = new WB_Point(p);
		double bestSqDist = Double.POSITIVE_INFINITY;
		if (pointOtherSideOfPlane(p, T.p4, T.p1, T.p2, T.p3)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p1, T.p2, T.p3);
			final double sqDist = q.subToVector3D(p).getSqLength3D();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		if (pointOtherSideOfPlane(p, T.p2, T.p1, T.p3, T.p4)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p1, T.p3, T.p4);
			final double sqDist = q.subToVector3D(p).getSqLength3D();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		if (pointOtherSideOfPlane(p, T.p3, T.p1, T.p4, T.p2)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p1, T.p4, T.p2);
			final double sqDist = q.subToVector3D(p).getSqLength3D();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		if (pointOtherSideOfPlane(p, T.p1, T.p2, T.p4, T.p3)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p2, T.p4, T.p3);
			final double sqDist = q.subToVector3D(p).getSqLength3D();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		return new WB_Point(closestPt);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static boolean pointOtherSideOfPlane(final WB_Coord p, final WB_Coord q, final WB_Coord a, final WB_Coord b,
			final WB_Coord c) {
		final double signp = new WB_Vector(a, p).dot(new WB_Vector(a, b).crossSelf(new WB_Vector(a, c)));
		final double signq = new WB_Vector(a, q).dot(new WB_Vector(a, b).crossSelf(new WB_Vector(a, c)));
		return signp * signq <= 0;
	}

	/**
	 *
	 */
	protected static class TriangleIntersection {
		/**  */
		public WB_Point p0; // the first point of the line
		/**  */
		public WB_Point p1; // the second point of the line
		/**  */
		public double s0; // the distance along the line to the first
		/**  */
		// intersection with the triangle
		public double s1; // the distance along the line to the second
		// intersection with the triangle
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param q1
	 * @param q2
	 * @param q3
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3,
			final WB_Coord q1, final WB_Coord q2, final WB_Coord q3) {
		// Taken from
		// http://jgt.akpeters.com/papers/Moller97/tritri.html#ISECTLINE
		// Compute plane equation of first triangle: n1 * x + d1 = 0.
		final WB_Plane P1 = gf.createPlane(p1, p2, p3);
		final WB_Vector n1 = P1.getNormal();
		final double d1 = P1.d();
		// Evaluate second triangle with plane equation 1 to determine signed
		// distances to the plane.
		double du0 = n1.dot(q1) + d1;
		double du1 = n1.dot(q2) + d1;
		double du2 = n1.dot(q3) + d1;
		// Coplanarity robustness check.
		if (Math.abs(du0) < WB_Epsilon.EPSILON) {
			du0 = 0;
		}
		if (Math.abs(du1) < WB_Epsilon.EPSILON) {
			du1 = 0;
		}
		if (Math.abs(du2) < WB_Epsilon.EPSILON) {
			du2 = 0;
		}
		final double du0du1 = du0 * du1;
		final double du0du2 = du0 * du2;
		if (du0du1 > 0 && du0du2 > 0) {
			return NOINTERSECTION();
			// same sign on all of them + != 0 ==> no
		}
		// intersection
		final WB_Plane P2 = gf.createPlane(q1, q2, q3);
		final WB_Vector n2 = P2.getNormal();
		final double d2 = -P2.d();
		// Compute plane equation of second triangle: n2 * x + d2 = 0
		// Evaluate first triangle with plane equation 2 to determine signed
		// distances to the plane.
		double dv0 = n2.dot(p1) + d2;
		double dv1 = n2.dot(p2) + d2;
		double dv2 = n2.dot(p3) + d2;
		// Coplanarity robustness check.
		if (Math.abs(dv0) < WB_Epsilon.EPSILON) {
			dv0 = 0;
		}
		if (Math.abs(dv1) < WB_Epsilon.EPSILON) {
			dv1 = 0;
		}
		if (Math.abs(dv2) < WB_Epsilon.EPSILON) {
			dv2 = 0;
		}
		final double dv0dv1 = dv0 * dv1;
		final double dv0dv2 = dv0 * dv2;
		if (dv0dv1 > 0 && dv0dv2 > 0) {
			return NOINTERSECTION();
			// same sign on all of them + != 0 ==> no
		}
		// Compute direction of intersection line.
		final WB_Vector ld = n1.cross(n2);
		// Compute an index to the largest component of line direction.
		double max = Math.abs(ld.xd());
		int index = 0;
		final double b = Math.abs(ld.yd());
		final double c = Math.abs(ld.zd());
		if (b > max) {
			max = b;
			index = 1;
		}
		if (c > max) {
			index = 2;
		}
		// This is the simplified projection onto the line of intersection.
		double vp0 = p1.xd();
		double vp1 = p2.xd();
		double vp2 = p3.xd();
		double up0 = q1.xd();
		double up1 = q2.xd();
		double up2 = q3.xd();
		if (index == 1) {
			vp0 = p1.yd();
			vp1 = p2.yd();
			vp2 = p3.yd();
			up0 = q1.yd();
			up1 = q2.yd();
			up2 = q3.yd();
		} else if (index == 2) {
			vp0 = p1.zd();
			vp1 = p2.zd();
			vp2 = p3.zd();
			up0 = q1.zd();
			up1 = q2.zd();
			up2 = q3.zd();
		}
		// Compute interval for triangle 1.
		final TriangleIntersection isectA = compute_intervals_isectline(p1, p2, p3, vp0, vp1, vp2, dv0, dv1, dv2,
				dv0dv1, dv0dv2);
		if (isectA == null) {
			if (coplanarTriangles(n1, p1, p2, p3, q1, q2, q3)) {
				return NOINTERSECTION();
			} else {
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				return i;
			}
		}
		int smallest1 = 0;
		if (isectA.s0 > isectA.s1) {
			final double cc = isectA.s0;
			isectA.s0 = isectA.s1;
			isectA.s1 = cc;
			smallest1 = 1;
		}
		// Compute interval for triangle 2.
		final TriangleIntersection isectB = compute_intervals_isectline(q1, q2, q3, up0, up1, up2, du0, du1, du2,
				du0du1, du0du2);
		int smallest2 = 0;
		if (isectB == null) {
			System.out.println("coplanar triangles on compute_intervals_isectline");
			return NOINTERSECTION();
		}
		if (isectB.s0 > isectB.s1) {
			final double cc = isectB.s0;
			isectB.s0 = isectB.s1;
			isectB.s1 = cc;
			smallest2 = 1;
		}
		if (isectA.s1 < isectB.s0 || isectB.s1 < isectA.s0) {
			return NOINTERSECTION();
		}
		// At this point we know that the triangles intersect: there's an
		// intersection line, the triangles are not
		// coplanar, and they overlap.
		final WB_Point[] intersectionVertices = new WB_Point[2];
		if (isectB.s0 < isectA.s0) {
			if (smallest1 == 0) {
				intersectionVertices[0] = isectA.p0;
			} else {
				intersectionVertices[0] = isectA.p1;
			}
			if (isectB.s1 < isectA.s1) {
				if (smallest2 == 0) {
					intersectionVertices[1] = isectB.p1;
				} else {
					intersectionVertices[1] = isectB.p0;
				}
			} else {
				if (smallest1 == 0) {
					intersectionVertices[1] = isectA.p1;
				} else {
					intersectionVertices[1] = isectA.p0;
				}
			}
		} else {
			if (smallest2 == 0) {
				intersectionVertices[0] = isectB.p0;
			} else {
				intersectionVertices[0] = isectB.p1;
			}
			if (isectB.s1 > isectA.s1) {
				if (smallest1 == 0) {
					intersectionVertices[1] = isectA.p1;
				} else {
					intersectionVertices[1] = isectA.p0;
				}
			} else {
				if (smallest2 == 0) {
					intersectionVertices[1] = isectB.p1;
				} else {
					intersectionVertices[1] = isectB.p0;
				}
			}
		}
		final WB_Intersection ir = new WB_Intersection();
		ir.intersection = true;
		ir.object = gf.createSegment(intersectionVertices[0], intersectionVertices[1]);
		return ir;
	}

	/**
	 *
	 *
	 * @param v
	 * @param u
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Triangle v, final WB_Triangle u) {
		// Taken from
		// http://jgt.akpeters.com/papers/Moller97/tritri.html#ISECTLINE
		// Compute plane equation of first triangle: n1 * x + d1 = 0.
		final WB_Plane P1 = gf.createPlane(v);
		final WB_Vector n1 = P1.getNormal();
		final double d1 = P1.d();
		// Evaluate second triangle with plane equation 1 to determine signed
		// distances to the plane.
		double du0 = n1.dot(u.p1()) + d1;
		double du1 = n1.dot(u.p2()) + d1;
		double du2 = n1.dot(u.p3()) + d1;
		// Coplanarity robustness check.
		if (Math.abs(du0) < WB_Epsilon.EPSILON) {
			du0 = 0;
		}
		if (Math.abs(du1) < WB_Epsilon.EPSILON) {
			du1 = 0;
		}
		if (Math.abs(du2) < WB_Epsilon.EPSILON) {
			du2 = 0;
		}
		final double du0du1 = du0 * du1;
		final double du0du2 = du0 * du2;
		if (du0du1 > 0 && du0du2 > 0) {
			return NOINTERSECTION();
			// same sign on all of them + != 0 ==> no
		}
		// intersection
		final WB_Plane P2 = gf.createPlane(u);
		final WB_Vector n2 = P2.getNormal();
		final double d2 = P2.d();
		// Compute plane equation of second triangle: n2 * x + d2 = 0
		// Evaluate first triangle with plane equation 2 to determine signed
		// distances to the plane.
		double dv0 = n2.dot(v.p1()) + d2;
		double dv1 = n2.dot(v.p2()) + d2;
		double dv2 = n2.dot(v.p3()) + d2;
		// Coplanarity robustness check.
		if (Math.abs(dv0) < WB_Epsilon.EPSILON) {
			dv0 = 0;
		}
		if (Math.abs(dv1) < WB_Epsilon.EPSILON) {
			dv1 = 0;
		}
		if (Math.abs(dv2) < WB_Epsilon.EPSILON) {
			dv2 = 0;
		}
		final double dv0dv1 = dv0 * dv1;
		final double dv0dv2 = dv0 * dv2;
		if (dv0dv1 > 0 && dv0dv2 > 0) {
			return NOINTERSECTION();
			// same sign on all of them + != 0 ==> no
		}
		// Compute direction of intersection line.
		final WB_Vector ld = n1.cross(n2);
		// Compute an index to the largest component of line direction.
		double max = Math.abs(ld.xd());
		int index = 0;
		final double b = Math.abs(ld.yd());
		final double c = Math.abs(ld.zd());
		if (b > max) {
			max = b;
			index = 1;
		}
		if (c > max) {
			index = 2;
		}
		// This is the simplified projection onto the line of intersection.
		double vp0 = v.p1().xd();
		double vp1 = v.p2().xd();
		double vp2 = v.p3().xd();
		double up0 = u.p1().xd();
		double up1 = u.p2().xd();
		double up2 = u.p3().xd();
		if (index == 1) {
			vp0 = v.p1().yd();
			vp1 = v.p2().yd();
			vp2 = v.p3().yd();
			up0 = u.p1().yd();
			up1 = u.p2().yd();
			up2 = u.p3().yd();
		} else if (index == 2) {
			vp0 = v.p1().zd();
			vp1 = v.p2().zd();
			vp2 = v.p3().zd();
			up0 = u.p1().zd();
			up1 = u.p2().zd();
			up2 = u.p3().zd();
		}
		// Compute interval for triangle 1.
		final TriangleIntersection isectA = compute_intervals_isectline(v, vp0, vp1, vp2, dv0, dv1, dv2, dv0dv1,
				dv0dv2);
		if (isectA == null) {
			if (coplanarTriangles(n1, v, u)) {
				return NOINTERSECTION();
			} else {
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				return i;
			}
		}
		int smallest1 = 0;
		if (isectA.s0 > isectA.s1) {
			final double cc = isectA.s0;
			isectA.s0 = isectA.s1;
			isectA.s1 = cc;
			smallest1 = 1;
		}
		// Compute interval for triangle 2.
		final TriangleIntersection isectB = compute_intervals_isectline(u, up0, up1, up2, du0, du1, du2, du0du1,
				du0du2);
		int smallest2 = 0;
		if (isectB.s0 > isectB.s1) {
			final double cc = isectB.s0;
			isectB.s0 = isectB.s1;
			isectB.s1 = cc;
			smallest2 = 1;
		}
		if (isectA.s1 < isectB.s0 || isectB.s1 < isectA.s0) {
			return NOINTERSECTION();
		}
		// At this point we know that the triangles intersect: there's an
		// intersection line, the triangles are not
		// coplanar, and they overlap.
		final WB_Point[] intersectionVertices = new WB_Point[2];
		if (isectB.s0 < isectA.s0) {
			if (smallest1 == 0) {
				intersectionVertices[0] = isectA.p0;
			} else {
				intersectionVertices[0] = isectA.p1;
			}
			if (isectB.s1 < isectA.s1) {
				if (smallest2 == 0) {
					intersectionVertices[1] = isectB.p1;
				} else {
					intersectionVertices[1] = isectB.p0;
				}
			} else {
				if (smallest1 == 0) {
					intersectionVertices[1] = isectA.p1;
				} else {
					intersectionVertices[1] = isectA.p0;
				}
			}
		} else {
			if (smallest2 == 0) {
				intersectionVertices[0] = isectB.p0;
			} else {
				intersectionVertices[0] = isectB.p1;
			}
			if (isectB.s1 > isectA.s1) {
				if (smallest1 == 0) {
					intersectionVertices[1] = isectA.p1;
				} else {
					intersectionVertices[1] = isectA.p0;
				}
			} else {
				if (smallest2 == 0) {
					intersectionVertices[1] = isectB.p1;
				} else {
					intersectionVertices[1] = isectB.p0;
				}
			}
		}
		final WB_Intersection ir = new WB_Intersection();
		ir.intersection = true;
		ir.object = gf.createSegment(intersectionVertices[0], intersectionVertices[1]);
		return ir;
	}

	/**
	 *
	 *
	 * @param v
	 * @param vv0
	 * @param vv1
	 * @param vv2
	 * @param d0
	 * @param d1
	 * @param d2
	 * @param d0d1
	 * @param d0d2
	 * @return
	 */
	protected static TriangleIntersection compute_intervals_isectline(final WB_Triangle v, final double vv0,
			final double vv1, final double vv2, final double d0, final double d1, final double d2, final double d0d1,
			final double d0d2) {
		if (d0d1 > 0) {
			// plane
			return intersect(v.p3(), v.p1(), v.p2(), vv2, vv0, vv1, d2, d0, d1);
		} else if (d0d2 > 0) {
			return intersect(v.p2(), v.p1(), v.p3(), vv1, vv0, vv2, d1, d0, d2);
		} else if (d1 * d2 > 0 || d0 != 0) {
			return intersect(v.p1(), v.p2(), v.p3(), vv0, vv1, vv2, d0, d1, d2);
		} else if (d1 != 0) {
			return intersect(v.p2(), v.p1(), v.p3(), vv1, vv0, vv2, d1, d0, d2);
		} else if (d2 != 0) {
			return intersect(v.p3(), v.p1(), v.p2(), vv2, vv0, vv1, d2, d0, d1);
		} else {
			return null; // triangles are coplanar
		}
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param vv0
	 * @param vv1
	 * @param vv2
	 * @param d0
	 * @param d1
	 * @param d2
	 * @param d0d1
	 * @param d0d2
	 * @return
	 */
	protected static TriangleIntersection compute_intervals_isectline(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3, final double vv0, final double vv1, final double vv2, final double d0, final double d1,
			final double d2, final double d0d1, final double d0d2) {
		if (d0d1 > 0) {
			// plane
			return intersect(p3, p1, p2, vv2, vv0, vv1, d2, d0, d1);
		} else if (d0d2 > 0) {
			return intersect(p2, p1, p3, vv1, vv0, vv2, d1, d0, d2);
		} else if (d1 * d2 > 0 || d0 != 0) {
			return intersect(p1, p2, p3, vv0, vv1, vv2, d0, d1, d2);
		} else if (d1 != 0) {
			return intersect(p2, p1, p3, vv1, vv0, vv2, d1, d0, d2);
		} else if (d2 != 0) {
			return intersect(p3, p1, p2, vv2, vv0, vv1, d2, d0, d1);
		} else {
			return null; // triangles are coplanar
		}
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param v2
	 * @param vv0
	 * @param vv1
	 * @param vv2
	 * @param d0
	 * @param d1
	 * @param d2
	 * @return
	 */
	protected static TriangleIntersection intersect(final WB_Coord v0, final WB_Coord v1, final WB_Coord v2,
			final double vv0, final double vv1, final double vv2, final double d0, final double d1, final double d2) {
		final TriangleIntersection intersection = new TriangleIntersection();
		double tmp = d0 / (d0 - d1);
		intersection.s0 = vv0 + (vv1 - vv0) * tmp;
		WB_Vector diff = new WB_Vector(v0, v1);
		diff.mulSelf(tmp);
		intersection.p0 = WB_Point.add(v0, diff);
		tmp = d0 / (d0 - d2);
		intersection.s1 = vv0 + (vv2 - vv0) * tmp;
		diff = new WB_Vector(v0, v2);
		diff.mulSelf(tmp);
		intersection.p1 = WB_Point.add(v0, diff);
		return intersection;
	}

	/**
	 *
	 *
	 * @param n
	 * @param v
	 * @param u
	 * @return
	 */
	protected static boolean coplanarTriangles(final WB_Vector n, final WB_Triangle v, final WB_Triangle u) {
		// First project onto an axis-aligned plane that maximizes the area of
		// the triangles.
		int i0;
		int i1;
		final double[] a = new double[] { Math.abs(n.xd()), Math.abs(n.yd()), Math.abs(n.zd()) };
		if (a[0] > a[1]) // X > Y
		{
			if (a[0] > a[2]) { // X is greatest
				i0 = 1;
				i1 = 2;
			} else { // Z is greatest
				i0 = 0;
				i1 = 1;
			}
		} else // X < Y
		{
			if (a[2] > a[1]) { // Z is greatest
				i0 = 0;
				i1 = 1;
			} else { // Y is greatest
				i0 = 0;
				i1 = 2;
			}
		}
		// Test all edges of triangle 1 against the edges of triangle 2.
		final double[] v0 = new double[] { v.p1().xd(), v.p1().yd(), v.p1().zd() };
		final double[] v1 = new double[] { v.p2().xd(), v.p2().yd(), v.p2().zd() };
		final double[] v2 = new double[] { v.p3().xd(), v.p3().yd(), v.p3().zd() };
		final double[] u0 = new double[] { u.p1().xd(), u.p1().yd(), u.p1().zd() };
		final double[] u1 = new double[] { u.p2().xd(), u.p2().yd(), u.p2().zd() };
		final double[] u2 = new double[] { u.p3().xd(), u.p3().yd(), u.p3().zd() };
		boolean tf = triangleEdgeTest(v0, v1, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		tf = triangleEdgeTest(v1, v2, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		tf = triangleEdgeTest(v2, v0, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		// Finally, test whether one triangle is contained in the other one.
		tf = pointInTri(v0, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		return pointInTri(u0, v0, v1, v2, i0, i1);
	}

	/**
	 *
	 *
	 * @param n
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param q1
	 * @param q2
	 * @param q3
	 * @return
	 */
	protected static boolean coplanarTriangles(final WB_Vector n, final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3, final WB_Coord q1, final WB_Coord q2, final WB_Coord q3) {
		// First project onto an axis-aligned plane that maximizes the are of
		// the triangles.
		int i0;
		int i1;
		final double[] a = new double[] { Math.abs(n.xd()), Math.abs(n.yd()), Math.abs(n.zd()) };
		if (a[0] > a[1]) // X > Y
		{
			if (a[0] > a[2]) { // X is greatest
				i0 = 1;
				i1 = 2;
			} else { // Z is greatest
				i0 = 0;
				i1 = 1;
			}
		} else // X < Y
		{
			if (a[2] > a[1]) { // Z is greatest
				i0 = 0;
				i1 = 1;
			} else { // Y is greatest
				i0 = 0;
				i1 = 2;
			}
		}
		// Test all edges of triangle 1 against the edges of triangle 2.
		final double[] v0 = new double[] { p1.xd(), p1.yd(), p1.zd() };
		final double[] v1 = new double[] { p2.xd(), p2.yd(), p2.zd() };
		final double[] v2 = new double[] { p3.xd(), p3.yd(), p3.zd() };
		final double[] u0 = new double[] { q1.xd(), q1.yd(), q1.zd() };
		final double[] u1 = new double[] { q2.xd(), q2.yd(), q2.zd() };
		final double[] u2 = new double[] { q3.xd(), q3.yd(), q3.zd() };
		boolean tf = triangleEdgeTest(v0, v1, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		tf = triangleEdgeTest(v1, v2, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		tf = triangleEdgeTest(v2, v0, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		// Finally, test whether one triangle is contained in the other one.
		tf = pointInTri(v0, u0, u1, u2, i0, i1);
		if (tf) {
			return true;
		}
		return pointInTri(u0, v0, v1, v2, i0, i1);
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param u0
	 * @param u1
	 * @param u2
	 * @param i0
	 * @param i1
	 * @return
	 */
	protected static boolean triangleEdgeTest(final double[] v0, final double[] v1, final double[] u0,
			final double[] u1, final double[] u2, final int i0, final int i1) {
		final double ax = v1[i0] - v0[i0];
		final double ay = v1[i1] - v0[i1];
		// Test edge u0:u1 against v0:v1
		boolean tf = edgeEdgeTest(v0, u0, u1, i0, i1, ax, ay);
		if (tf) {
			return true;
		}
		// Test edge u1:u2 against v0:v1
		tf = edgeEdgeTest(v0, u1, u2, i0, i1, ax, ay);
		if (tf) {
			return true;
		}
		// Test edge u2:u0 against v0:v1
		return edgeEdgeTest(v0, u2, u0, i0, i1, ax, ay);
	}

	/**
	 *
	 *
	 * @param v0
	 * @param u0
	 * @param u1
	 * @param i0
	 * @param i1
	 * @param ax
	 * @param ay
	 * @return
	 */
	protected static boolean edgeEdgeTest(final double[] v0, final double[] u0, final double[] u1, final int i0,
			final int i1, final double ax, final double ay) {
		final double bx = u0[i0] - u1[i0];
		final double by = u0[i1] - u1[i1];
		final double cx = v0[i0] - u0[i0];
		final double cy = v0[i1] - u0[i1];
		final double f = ay * bx - ax * by;
		final double d = by * cx - bx * cy;
		if (f > 0 && d >= 0 && d <= f || f < 0 && d <= 0 && d >= f) {
			final double e = ax * cy - ay * cx;
			if (f > 0) {
				if (e >= 0 && e <= f) {
					return true;
				}
			} else {
				if (e <= 0 && e >= f) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param u0
	 * @param u1
	 * @param u2
	 * @param i0
	 * @param i1
	 * @return
	 */
	protected static boolean pointInTri(final double[] v0, final double[] u0, final double[] u1, final double[] u2,
			final int i0, final int i1) {
		double a = u1[i1] - u0[i1];
		double b = -(u1[i0] - u0[i0]);
		double c = -a * u0[i0] - b * u0[i1];
		final double d0 = a * v0[i0] + b * v0[i1] + c;
		a = u2[i1] - u1[i1];
		b = -(u2[i0] - u1[i0]);
		c = -a * u1[i0] - b * u1[i1];
		final double d1 = a * v0[i0] + b * v0[i1] + c;
		a = u0[i1] - u2[i1];
		b = -(u0[i0] - u2[i0]);
		c = -a * u2[i0] - b * u2[i1];
		final double d2 = a * v0[i0] + b * v0[i1] + c;
		return d0 * d1 > 0 && d0 * d2 > 0;
	}

	/**
	 *
	 *
	 * @param t1
	 * @param t2
	 * @return
	 */
	private static WB_Intersection NOINTERSECTION(final double t1, final double t2) {
		final WB_Intersection i = new WB_Intersection();
		i.intersection = false;
		i.sqDist = Float.POSITIVE_INFINITY;
		i.t1 = t1;
		i.t2 = t2;
		return i;
	}

	/**
	 *
	 *
	 * @param L
	 * @param S
	 * @return
	 */
	public static WB_Intersection getClosestPoint3D(final WB_Line L, final WB_Segment S) {
		final WB_Intersection i = getClosestPoint3D(L, new WB_Line(S.getOrigin(), S.getDirection()));
		if (i.dimension == 0) {
			return i;
		}
		if (i.t2 <= WB_Epsilon.EPSILON) {
			i.t2 = 0;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(), S.getOrigin());
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		if (i.t2 >= S.getLength() - WB_Epsilon.EPSILON) {
			i.t2 = 1;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(), S.getEndpoint());
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		return i;
	}
	// POINT-TRIANGLE

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	// POINT-POLYGON
	public static WB_Intersection getClosestPoint3D(final WB_Segment S1, final WB_Segment S2) {
		final WB_Point d1 = WB_Point.sub(S1.getEndpoint(), S1.getOrigin());
		final WB_Point d2 = WB_Point.sub(S2.getEndpoint(), S2.getOrigin());
		final WB_Point r = WB_Point.sub(S1.getOrigin(), S2.getOrigin());
		final double a = d1.dot(d1);
		final double e = d2.dot(d2);
		final double f = d2.dot(r);
		if (WB_Epsilon.isZero(a) || WB_Epsilon.isZero(e)) {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = false;
			i.t1 = 0;
			i.t2 = 0;
			i.object = new WB_Segment(S1.getOrigin(), S2.getOrigin());
			i.dimension = 1;
			i.sqDist = r.getSqLength3D();
			return i;
		}
		double t1 = 0;
		double t2 = 0;
		if (WB_Epsilon.isZero(a)) {
			t2 = WB_Math.clamp(f / e, 0, 1);
		} else {
			final double c = d1.dot(r);
			if (WB_Epsilon.isZero(e)) {
				t1 = WB_Math.clamp(-c / a, 0, 1);
			} else {
				final double b = d1.dot(d2);
				final double denom = a * e - b * b;
				if (!WB_Epsilon.isZero(denom)) {
					t1 = WB_Math.clamp((b * f - c * e) / denom, 0, 1);
				} else {
					t1 = 0;
				}
				final double tnom = b * t1 + f;
				if (tnom < 0) {
					t1 = WB_Math.clamp(-c / a, 0, 1);
				} else if (tnom > e) {
					t2 = 1;
					t1 = WB_Math.clamp((b - c) / a, 0, 1);
				} else {
					t2 = tnom / e;
				}
			}
		}
		final WB_Intersection i = new WB_Intersection();
		i.intersection = t1 > 0 && t1 < 1 && t2 > 0 && t2 < 1;
		i.t1 = t1;
		i.t2 = t2;
		final WB_Point p1 = S1.getParametricPoint(t1);
		final WB_Point p2 = S2.getParametricPoint(t2);
		i.sqDist = getSqDistance3D(p1, p2);
		if (WB_Epsilon.isZeroSq(i.sqDist)) {
			i.dimension = 0;
			i.object = p1;
		} else {
			i.dimension = 1;
			i.object = new WB_Segment(p1, p2);
		}
		return i;
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	// POINT-POLYGON
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmin2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			WB_Point q = new WB_Point(p);
			if (n > 1) {
				q = projectOnPlane(p,
						new WB_Plane(poly.getPoint(T[i]), poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2])));
			}
			tmp = getClosestPointToTriangle3D(q, poly.getPoint(T[i]), poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2]));
			final double d2 = getSqDistance3D(tmp, q);
			if (d2 < dmin2) {
				closest = tmp;
				dmin2 = d2;
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static double getDistanceToClosestPoint3D(final WB_Coord p, final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmin2 = Double.POSITIVE_INFINITY;
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			WB_Point q = new WB_Point(p);
			if (n > 1) {
				q = projectOnPlane(p,
						new WB_Plane(poly.getPoint(T[i]), poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2])));
			}
			tmp = getClosestPointToTriangle3D(q, poly.getPoint(T[i]), poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2]));
			final double d2 = getSqDistance3D(tmp, q);
			if (d2 < dmin2) {
				dmin2 = d2;
			}
		}
		return Math.sqrt(dmin2);
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static WB_Point getClosestPointOnPeriphery3D(final WB_Coord p, final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = getClosestPointToTriangle3D(p, poly.getPoint(T[i]), poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2]));
			final double d2 = getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfPoints() - 1; i < poly.getNumberOfPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint3D(p, S);
				final double d2 = getSqDistance3D(tmp, p);
				if (d2 < dmax2) {
					closest = tmp;
					dmax2 = d2;
				}
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @param tris
	 * @return
	 */
	public static WB_Point getClosestPointOnPeriphery3D(final WB_Coord p, final WB_Polygon poly,
			final List<? extends WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint3D(p, T);
			final double d2 = getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfPoints() - 1; i < poly.getNumberOfPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint3D(p, S);
				final double d2 = getSqDistance3D(tmp, p);
				if (d2 < dmax2) {
					closest = tmp;
					dmax2 = d2;
				}
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param ray
	 * @param poly
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Ray ray, final WB_Polygon poly) {
		final WB_Intersection ir = getIntersection3D(ray, poly.getPlane());
		if (ir.intersection == false) {
			return ir;
		}
		final WB_Point p = (WB_Point) ir.object;
		if (WB_Epsilon.isZero(getDistanceToClosestPoint3D(p, poly))) {
			return ir;
		}
		ir.intersection = false;
		return ir;
	}

	/**
	 *
	 *
	 * @param line
	 * @param poly
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Line line, final WB_Polygon poly) {
		final WB_Intersection ir = getIntersection3D(line, poly.getPlane());
		if (ir.intersection == false) {
			return ir;
		}
		final WB_Point p = (WB_Point) ir.object;
		if (WB_Epsilon.isZero(getDistanceToClosestPoint3D(p, poly))) {
			return ir;
		}
		ir.intersection = false;
		return ir;
	}

	/**
	 *
	 *
	 * @param segment
	 * @param poly
	 * @return
	 */
	public static WB_Intersection getIntersection3D(final WB_Segment segment, final WB_Polygon poly) {
		final WB_Intersection ir = getIntersection3D(segment, poly.getPlane());
		if (ir.intersection == false) {
			return ir;
		}
		final WB_Point p = (WB_Point) ir.object;
		if (WB_Epsilon.isZero(getDistanceToClosestPoint3D(p, poly))) {
			return ir;
		}
		ir.intersection = false;
		return ir;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static double getParameterOfPointOnLine3D(final WB_Coord a, final WB_Coord b, final WB_Coord p) {
		double x1, x2, y1, y2, z1, z2;
		x1 = b.xd() - a.xd();
		x2 = p.xd() - a.xd();
		y1 = b.yd() - a.yd();
		y2 = p.yd() - a.yd();
		z1 = b.zd() - a.zd();
		z2 = p.zd() - a.zd();
		return (x1 * x2 + y1 * y2 + z1 * z2) / (x1 * x1 + y1 * y1 + z1 * z1);
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static double getParameterOfPointOnLine3D(final WB_Coord p, final WB_Line L) {
		final WB_Coord ab = L.getDirection();
		final WB_Vector ac = new WB_Vector(p);
		ac.subSelf(L.getOrigin());
		return ac.dot(ab);
	}

	/**
	 *
	 *
	 * @param p1
	 * @param d1
	 * @param p2
	 * @param d2
	 * @param p3
	 * @param d3
	 * @return
	 */
	public static WB_Coord[] getFourthPoint(final WB_Coord p1, final double d1, final WB_Coord p2, final double d2,
			final WB_Coord p3, final double d3) {
		final WB_Sphere S1 = new WB_Sphere(p1, d1);
		final WB_Sphere S2 = new WB_Sphere(p2, d2);
		final WB_Sphere S3 = new WB_Sphere(p3, d3);
		final WB_Intersection is = getIntersection3D(S1, S2, S3);
		if (!is.intersection) {
			return new WB_Point[0];
		} else if (is.dimension == 0) {
			return new WB_Point[] { (WB_Point) is.object };
		} else {
			return new WB_Coord[] { ((WB_Segment) is.object).getOrigin(), ((WB_Segment) is.object).getEndpoint() };
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static boolean contains(final WB_Coord p, final WB_AABB AABB) {
		return p.xd() >= AABB.getMinX() && p.yd() >= AABB.getMinY() && p.zd() >= AABB.getMinZ()
				&& p.xd() < AABB.getMaxX() && p.yd() < AABB.getMaxY() && p.zd() < AABB.getMaxZ();
	}

	/**
	 *
	 *
	 * @param S
	 * @param T
	 * @return
	 */
	public static double getDistance3D(final WB_Segment S, final WB_Segment T) {
		return Math.sqrt(WB_GeometryOp.getIntersection3D(S, T).sqDist);
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_Segment S) {
		return Math.sqrt(getSqDistance3D(p, S));
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_Polygon poly) {
		return Math.sqrt(getSqDistance3D(p, poly));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_Line L) {
		return Math.sqrt(getSqDistance3D(p, L));
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_Plane P) {
		return P.getNormal().dot(p) + P.d();
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double getDistance3D(final double p[], final WB_Plane P) {
		final WB_Vector n = P.getNormal();
		return n.xd() * p[0] + n.yd() * p[1] + n.zd() * p[2] + P.d();
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_Ray R) {
		return Math.sqrt(getSqDistance3D(p, R));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getDistanceToLine3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToLine3D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static double getDistanceToLine3D(final WB_Coord p, final WB_Line L) {
		return Math.sqrt(getSqDistanceToLine3D(p, L));
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double getDistanceToPlane3D(final WB_Coord p, final WB_Plane P) {
		final double d = P.getNormal().dot(p) + P.d();
		return d < 0 ? -d : d;
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double getDistanceToPlane3D(final double[] p, final WB_Plane P) {
		final WB_Vector v = P.getNormal();
		final double d = v.xd() * p[0] + v.yd() * p[1] + v.zd() * p[2] + P.d();
		return d < 0 ? -d : d;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double getDistanceToPoint3D(final WB_Coord p, final WB_Coord q) {
		return Math.sqrt(getSqDistanceToPoint3D(p, q));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getDistanceToRay3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToRay3D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static double getDistanceToRay3D(final WB_Coord p, final WB_Ray R) {
		return Math.sqrt(getSqDistanceToRay3D(p, R));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getDistanceToSegment3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToSegment3D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static WB_Point projectOnPlane(final WB_Coord p, final WB_Plane P) {
		final WB_Point projection = new WB_Point(p);
		final WB_Vector po = new WB_Vector(P.getOrigin(), p);
		final WB_Vector n = P.getNormal();
		return projection.subSelf(n.mulSelf(n.dot(po)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static double getDistanceToSegment3D(final WB_Coord p, final WB_Segment S) {
		return Math.sqrt(getSqDistanceToSegment3D(p, S));
	}

	/**
	 *
	 *
	 * @param S
	 * @param T
	 * @return
	 */
	public static double getSqDistance3D(final WB_Segment S, final WB_Segment T) {
		return WB_GeometryOp.getIntersection3D(S, T).sqDist;
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_Segment S) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(), S.getOrigin());
		final WB_Vector ac = new WB_Vector(S.getOrigin(), p);
		final WB_Vector bc = new WB_Vector(S.getEndpoint(), p);
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = ab.dot(ab);
		if (e >= f) {
			return bc.dot(bc);
		}
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param PL
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_PolyLine PL) {
		double d2min = Double.POSITIVE_INFINITY;
		double d2;
		for (int s = 0; s < PL.getNumberSegments(); s++) {
			d2 = WB_GeometryOp.getSqDistance3D(p, PL.getSegment(s));
			if (d2 < d2min) {
				d2min = d2;
			}
		}
		return d2min;
	}

	/**
	 *
	 *
	 * @param p
	 * @param PL
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_PolyLine PL) {
		return Math.sqrt(getSqDistance3D(p, PL));
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Coord tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = WB_GeometryOp.getClosestPointToTriangle3D(p, poly.getPoint(T[i]), poly.getPoint(T[i + 1]),
					poly.getPoint(T[i + 2]));
			final double d2 = getDistance3D(tmp, p);
			if (d2 < dmax2) {
				dmax2 = d2;
				if (WB_Epsilon.isZeroSq(dmax2)) {
					break;
				}
			}
		}
		return dmax2;
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	// POINT-POINT
	public static double getSqDistance3D(final WB_Coord p, final WB_Line L) {
		final WB_Coord ab = L.getDirection();
		final WB_Vector ac = new WB_Vector(L.getOrigin(), p);
		final double e = ac.dot(ab);
		final double f = WB_Vector.dot(ab, ab);
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	// POINT-PLANE
	public static double getSqDistance3D(final WB_Coord p, final WB_Plane P) {
		final double d = P.getNormal().dot(p) + P.d();
		return d * d;
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_Ray R) {
		final WB_Coord ab = R.getDirection();
		final WB_Vector ac = new WB_Vector(R.getOrigin(), p);
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = WB_Vector.dot(ab, ab);
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	// POINT-SEGMENT
	public static double getSqDistanceToLine3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		final double e = ac.dot(ab);
		final double f = ab.dot(ab);
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static double getSqDistanceToLine3D(final WB_Coord p, final WB_Line L) {
		final WB_Coord ab = L.getDirection();
		final WB_Vector ac = gf.createVectorFromTo(L.getOrigin(), p);
		final double e = ac.dot(ab);
		final double f = WB_Vector.dot(ab, ab);
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double getSqDistanceToPlane3D(final WB_Coord p, final WB_Plane P) {
		final double d = P.getNormal().dot(p) + P.d();
		return d * d;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double getSqDistanceToPoint3D(final WB_Coord p, final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd()) + (q.yd() - p.yd()) * (q.yd() - p.yd())
				+ (q.zd() - p.zd()) * (q.zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getSqDistanceToRay3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = gf.createVectorFromTo(a, b);
		final WB_Vector ac = gf.createVectorFromTo(a, p);
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = ab.dot(ab);
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	// POINT-AABB
	public static double getSqDistanceToRay3D(final WB_Coord p, final WB_Ray R) {
		final WB_Coord ab = R.getDirection();
		final WB_Vector ac = gf.createVectorFromTo(R.getOrigin(), p);
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = WB_Vector.dot(ab, ab);
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getSqDistanceToSegment3D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = gf.createVectorFromTo(a, b);
		final WB_Vector ac = gf.createVectorFromTo(a, p);
		final WB_Vector bc = gf.createVectorFromTo(b, p);
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = ab.dot(ab);
		if (e >= f) {
			return bc.dot(bc);
		}
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static double getSqDistanceToSegment3D(final WB_Coord p, final WB_Segment S) {
		final WB_Point ab = gf.createPoint(S.getEndpoint()).sub(S.getOrigin());
		final WB_Point ac = gf.createPoint(p).sub(S.getOrigin());
		final WB_Point bc = gf.createPoint(p).sub(S.getEndpoint());
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = ab.dot(ab);
		if (e >= f) {
			return bc.dot(bc);
		}
		return ac.dot(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double signedDistanceToPlane3D(final WB_Coord p, final WB_Plane P) {
		final double d = P.getNormal().dot(p) + P.d();
		return d;
	}

	/**
	 *
	 *
	 * @param points
	 * @param dir
	 * @return
	 */
	public static int[] getExtremePointsAlongDirection(final WB_Coord[] points, final WB_Coord dir) {
		final int[] result = new int[] { -1, -1 };
		double minproj = Double.POSITIVE_INFINITY;
		double maxproj = Double.NEGATIVE_INFINITY;
		double proj;
		for (int i = 0; i < points.length; i++) {
			proj = WB_Vector.dot(points[i], dir);
			if (proj < minproj) {
				minproj = proj;
				result[0] = i;
			}
			if (proj > maxproj) {
				maxproj = proj;
				result[1] = i;
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param points
	 * @param dir
	 * @return
	 */
	public static int[] getExtremePointsAlongDirection(final Collection<? extends WB_Coord> points,
			final WB_Coord dir) {
		final int[] result = new int[] { -1, -1 };
		double minproj = Double.POSITIVE_INFINITY;
		double maxproj = Double.NEGATIVE_INFINITY;
		double proj;
		int i = 0;
		for (final WB_Coord point : points) {
			proj = WB_Vector.dot(point, dir);
			if (proj < minproj) {
				minproj = proj;
				result[0] = i;
			}
			if (proj > maxproj) {
				maxproj = proj;
				result[1] = i;
			}
			i++;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static WB_Classification classifyPointToPlaneFast3D(final WB_Coord p, final WB_Plane P) {
		return classifyPointToPlaneFast3D(P, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static WB_Classification classifyPointToPlane3D(final WB_Coord p, final WB_Plane P) {
		return classifyPointToPlane3D(P, p);
	}

	/**
	 *
	 *
	 * @param P
	 * @param p
	 * @return
	 */
	public static WB_Classification classifyPointToPlaneFast3D(final WB_Plane P, final WB_Coord p) {
		final double signp = WB_GeometryOp.signedDistanceToPlane3D(p, P);
		if (WB_Epsilon.isZero(signp)) {
			return WB_Classification.ON;
		}
		if (signp > 0) {
			return WB_Classification.FRONT;
		}
		return WB_Classification.BACK;
	}

	/**
	 *
	 *
	 * @param P
	 * @param p
	 * @return
	 */
	public static WB_Classification classifyPointToPlane3D(final WB_Plane P, final WB_Coord p) {
		if (WB_Epsilon.isZero(WB_GeometryOp.getDistanceToPlane3D(p, P))) {
			return WB_Classification.ON;
		}
		final double signp = WB_Predicates.orient3D(P.getOrigin(), P.getOrigin().addMul(100, P.getU()),
				P.getOrigin().addMul(100, P.getV()), p);
		if (signp == 0) {
			return WB_Classification.ON;
		}
		if (signp < 0) {
			return WB_Classification.FRONT;
		}
		return WB_Classification.BACK;
	}

	/**
	 *
	 *
	 * @param T
	 * @param p
	 * @return
	 */
	public static WB_Classification classifyPointToTetrahedron3D(final WB_Tetrahedron T, final WB_Coord p) {
		final WB_Plane pl012 = gf.createPlane(T.p1(), T.p2(), T.p3());
		final WB_Plane pl013 = gf.createPlane(T.p1(), T.p2(), T.p4());
		final WB_Plane pl023 = gf.createPlane(T.p1(), T.p3(), T.p4());
		final WB_Plane pl123 = gf.createPlane(T.p2(), T.p3(), T.p4());
		int on = 0;
		int front = 0;
		int back = 0;
		final WB_Classification c012 = classifyPointToPlane3D(pl012, p);
		if (c012 == WB_Classification.ON) {
			on++;
		} else if (c012 == WB_Classification.FRONT) {
			front++;
		} else {
			back++;
		}
		final WB_Classification c013 = classifyPointToPlane3D(pl013, p);
		if (c013 == WB_Classification.ON) {
			on++;
		} else if (c013 == WB_Classification.FRONT) {
			front++;
		} else {
			back++;
		}
		final WB_Classification c023 = classifyPointToPlane3D(pl023, p);
		if (c023 == WB_Classification.ON) {
			on++;
		} else if (c023 == WB_Classification.FRONT) {
			front++;
		} else {
			back++;
		}
		final WB_Classification c123 = classifyPointToPlane3D(pl123, p);
		if (c123 == WB_Classification.ON) {
			on++;
		} else if (c123 == WB_Classification.FRONT) {
			front++;
		} else {
			back++;
		}
		if (front == 4 || back == 4) {
			return WB_Classification.INSIDE;
		}
		if (front + on == 4 || back + on == 4) {
			return WB_Classification.ON;
		}
		return WB_Classification.OUTSIDE;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param P
	 * @return
	 */
	public static WB_Classification classifyPolygonToPlane3D(final WB_Polygon poly, final WB_Plane P) {
		int numInFront = 0;
		int numBehind = 0;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			switch (classifyPointToPlane3D(P, poly.getPoint(i))) {
			case FRONT:
				numInFront++;
				break;
			case BACK:
				numBehind++;
				break;
			default:
			}
			if (numBehind != 0 && numInFront != 0) {
				return WB_Classification.CROSSING;
			}
		}
		if (numInFront != 0) {
			return WB_Classification.FRONT;
		}
		if (numBehind != 0) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	/**
	 *
	 *
	 * @param segment
	 * @param P
	 * @return
	 */
	public static WB_Classification classifySegmentToPlane3D(final WB_Segment segment, final WB_Plane P) {
		int numInFront = 0;
		int numBehind = 0;
		switch (classifyPointToPlane3D(segment.getOrigin(), P)) {
		case FRONT:
			numInFront++;
			break;
		case BACK:
			numBehind++;
			break;
		default:
		}
		switch (classifyPointToPlane3D(segment.getEndpoint(), P)) {
		case FRONT:
			numInFront++;
			break;
		case BACK:
			numBehind++;
			break;
		default:
		}
		if (numBehind != 0 && numInFront != 0) {
			return WB_Classification.CROSSING;
		}
		if (numInFront != 0) {
			return WB_Classification.FRONT;
		}
		if (numBehind != 0) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param P
	 * @return
	 */
	public static WB_Classification classifySegmentToPlane3D(final WB_Coord p, final WB_Coord q, final WB_Plane P) {
		int numInFront = 0;
		int numBehind = 0;
		switch (classifyPointToPlane3D(p, P)) {
		case FRONT:
			numInFront++;
			break;
		case BACK:
			numBehind++;
			break;
		default:
		}
		switch (classifyPointToPlane3D(q, P)) {
		case FRONT:
			numInFront++;
			break;
		case BACK:
			numBehind++;
			break;
		default:
		}
		if (numBehind != 0 && numInFront != 0) {
			return WB_Classification.CROSSING;
		}
		if (numInFront != 0) {
			return WB_Classification.FRONT;
		}
		if (numBehind != 0) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param P
	 * @return
	 */
	public static WB_Classification classifyPolygonToPlaneFast3D(final WB_Polygon poly, final WB_Plane P) {
		int numInFront = 0;
		int numBehind = 0;
		double d;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			d = WB_GeometryOp.signedDistanceToPlane3D(poly.getPoint(i), P);
			if (d > WB_Epsilon.EPSILON) {
				numInFront++;
			} else if (d < -WB_Epsilon.EPSILON) {
				numBehind++;
			}
			if (numBehind != 0 && numInFront != 0) {
				return WB_Classification.CROSSING;
			}
		}
		if (numInFront != 0) {
			return WB_Classification.FRONT;
		}
		if (numBehind != 0) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	/**
	 *
	 *
	 * @param L
	 * @param p
	 * @return
	 */
	// POINT-POLYGON
	public static WB_Line getParallelLineThroughPoint(final WB_Line L, final WB_Coord p) {
		return new WB_Line(p, L.getDirection());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Plane getBisector(final WB_Coord p, final WB_Coord q) {
		return new WB_Plane(gf.createInterpolatedPoint(p, q, 0.5), gf.createVectorFromTo(p, q));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Sphere getBoundingSphere(final WB_Coord[] points) {
		WB_Point center = new WB_Point(points[0]);
		double radius = WB_Epsilon.EPSILON;
		double radius2 = radius * radius;
		double dist, dist2, alpha, ialpha2;
		for (int i = 0; i < 3; i++) {
			for (final WB_Coord point : points) {
				dist2 = WB_Vector.getSqDistance3D(point, center);
				if (dist2 > radius2) {
					dist = Math.sqrt(dist2);
					if (i < 2) {
						alpha = dist / radius;
						ialpha2 = 1.0 / (alpha * alpha);
						radius = 0.5 * (alpha + 1 / alpha) * radius;
						center = gf.createMidpoint(center.mulSelf(1.0 + ialpha2), WB_Point.mul(point, 1.0 - ialpha2));
					} else {
						radius = (radius + dist) * 0.5;
						center.mulAddMulSelf(radius / dist, (dist - radius) / dist, point);
					}
					radius2 = radius * radius;
				}
			}
		}
		return new WB_Sphere(center, radius);
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static double getArea(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		return WB_Math.fastAbs(getSignedArea(p1, p2, p3));
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static double getSignedArea(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		final WB_Plane P = new WB_Plane(p1, p2, p3);
		if (P.getNormal().getSqLength3D() < WB_Epsilon.SQEPSILON) {
			return 0.0;
		}
		final WB_Vector n = P.getNormal();
		final double x = WB_Math.fastAbs(n.xd());
		final double y = WB_Math.fastAbs(n.yd());
		final double z = WB_Math.fastAbs(n.zd());
		double area = 0;
		int coord = 3;
		if (x >= y && x >= z) {
			coord = 1;
		} else if (y >= x && y >= z) {
			coord = 2;
		}
		switch (coord) {
		case 1:
			area = p1.yd() * (p2.zd() - p3.zd()) + p2.yd() * (p3.zd() - p1.zd()) + p3.yd() * (p1.zd() - p2.zd());
			break;
		case 2:
			area = p1.xd() * (p2.zd() - p3.zd()) + p2.xd() * (p3.zd() - p1.zd()) + p3.xd() * (p1.zd() - p2.zd());
			break;
		case 3:
			area = p1.xd() * (p2.yd() - p3.yd()) + p2.xd() * (p3.yd() - p1.yd()) + p3.xd() * (p1.yd() - p2.yd());
			break;
		}
		switch (coord) {
		case 1:
			area *= 0.5 / x;
			break;
		case 2:
			area *= 0.5 / y;
			break;
		case 3:
			area *= 0.5 / z;
		}
		return area;
	}

	/**
	 *
	 *
	 * @param p
	 * @param A
	 * @param B
	 * @param C
	 * @return
	 */
	public static boolean pointInTriangleBary3D(final WB_Coord p, final WB_Coord A, final WB_Coord B,
			final WB_Coord C) {
		if (p == A) {
			return false;
		}
		if (p == B) {
			return false;
		}
		if (p == C) {
			return false;
		}
		if (WB_Epsilon.isZeroSq(getSqDistanceToLine2D(A, B, C))) {
			return false;
		}
		// Compute vectors
		final WB_Vector v0 = new WB_Vector(C).subSelf(A);
		final WB_Vector v1 = new WB_Vector(B).subSelf(A);
		final WB_Vector v2 = new WB_Vector(p).subSelf(A);
		// Compute dot products
		final double dot00 = v0.dot(v0);
		final double dot01 = v0.dot(v1);
		final double dot02 = v0.dot(v2);
		final double dot11 = v1.dot(v1);
		final double dot12 = v1.dot(v2);
		// Compute barycentric coordinates
		final double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
		final double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		final double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
		// Check if point is in triangle
		return u > WB_Epsilon.EPSILON && v > WB_Epsilon.EPSILON && u + v < 1 - WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static boolean pointInTriangleBary3D(final WB_Coord p, final WB_Triangle T) {
		return pointInTriangleBary3D(p, T.p1, T.p2, T.p3);
	}

	/**
	 *
	 *
	 * @param P
	 * @param Q
	 * @return
	 */
	public static boolean isEqual(final WB_Plane P, final WB_Plane Q) {
		if (!WB_Epsilon.isZero(WB_GeometryOp.getDistance3D(P.getOrigin(), Q))) {
			return false;
		}
		if (!WB_Epsilon.isZero(WB_GeometryOp.getDistance3D(Q.getOrigin(), P))) {
			return false;
		}
		if (!P.getNormal().isParallelNorm(Q.getNormal())) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param P
	 * @return
	 */
	public static WB_Polygon[] splitPolygon(final WB_Polygon poly, final WB_Plane P) {
		if (!poly.isSimple()) {
			throw new UnsupportedOperationException("Only simple polygons are supported at this time!");
		}
		final List<WB_Coord> frontVerts = new WB_CoordList();
		final List<WB_Coord> backVerts = new WB_CoordList();
		final int numVerts = poly.numberOfShellPoints;
		if (numVerts > 0) {
			WB_Coord a = poly.points.get(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp.classifyPointToPlane3D(a, P);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				final WB_Intersection i;
				b = poly.points.get(n);
				bSide = WB_GeometryOp.classifyPointToPlane3D(b, P);
				if (bSide == WB_Classification.FRONT) {
					if (aSide == WB_Classification.BACK) {
						i = WB_GeometryOp.getIntersection3D(b, a, P);
						frontVerts.add((WB_Point) i.object);
						backVerts.add((WB_Point) i.object);
					}
					frontVerts.add(b);
				} else if (bSide == WB_Classification.BACK) {
					if (aSide == WB_Classification.FRONT) {
						i = WB_GeometryOp.getIntersection3D(a, b, P);
						frontVerts.add((WB_Point) i.object);
						backVerts.add((WB_Point) i.object);
					} else if (aSide == WB_Classification.ON) {
						backVerts.add(a);
					}
					backVerts.add(b);
				} else {
					frontVerts.add(b);
					if (aSide == WB_Classification.BACK) {
						backVerts.add(b);
					}
				}
				a = b;
				aSide = bSide;
			}
		}
		final WB_Polygon[] result = new WB_Polygon[2];
		result[0] = new WB_Polygon(frontVerts);
		result[1] = new WB_Polygon(backVerts);
		return result;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public static WB_Polygon trimConvexPolygon(WB_Polygon poly, final double d) {
		final WB_Polygon cpoly = new WB_Polygon(poly.points);
		final int n = cpoly.numberOfShellPoints; // get number of vertices
		final WB_Plane P = cpoly.getPlane(); // get plane of poly
		WB_Coord p1, p2;
		WB_Point origin;
		WB_Vector v, normal;
		for (int i = 0, j = n - 1; i < n; j = i, i++) {
			p1 = cpoly.getPoint(i);// startpoint of edge
			p2 = cpoly.getPoint(j);// endpoint of edge
			// vector along edge
			v = gf.createNormalizedVectorFromTo(p1, p2);
			// edge normal is perpendicular to edge and plane normal
			normal = v.cross(P.getNormal());
			// center of edge
			origin = new WB_Point(p1).addSelf(p2).mulSelf(0.5);
			// offset cutting plane origin by the desired distance d
			origin.addMulSelf(d, normal);
			final WB_Polygon[] split = splitPolygon(poly, new WB_Plane(origin, normal));
			poly = split[0];
		}
		return poly;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public static WB_Polygon trimConvexPolygon(WB_Polygon poly, final double[] d) {
		final WB_Polygon cpoly = new WB_Polygon(poly.points);
		final int n = cpoly.numberOfShellPoints; // get number of vertices
		final WB_Plane P = cpoly.getPlane(); // get plane of poly
		WB_Coord p1, p2;
		WB_Point origin;
		WB_Vector v, normal;
		for (int i = 0, j = n - 1; i < n; j = i, i++) {
			p1 = cpoly.getPoint(i);// startpoint of edge
			p2 = cpoly.getPoint(j);// endpoint of edge
			// vector along edge
			v = gf.createNormalizedVectorFromTo(p1, p2);
			// edge normal is perpendicular to edge and plane normal
			normal = v.cross(P.getNormal());
			// center of edge
			origin = new WB_Point(p1).addSelf(p2).mulSelf(0.5);
			// offset cutting plane origin by the desired distance d
			origin.addMulSelf(d[j], normal);
			final WB_Polygon[] split = splitPolygon(poly, new WB_Plane(origin, normal));
			poly = split[0];
		}
		return poly;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static double getCosDihedralAngle(final WB_Coord a, final WB_Coord b, final WB_Coord c, final WB_Coord d) {
		return getCosDihedralAngle(gf.createVectorFromTo(b, a), gf.createVectorFromTo(c, b),
				gf.createVectorFromTo(d, c));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	public static double getCosDihedralAngle(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		final WB_Vector uxv = gf.createVector(u).crossSelf(v);
		final WB_Vector vxw = gf.createVector(v).crossSelf(w);
		return WB_Math.clamp(-uxv.dot(vxw) / (uxv.getLength() * vxw.getLength()), -1, 1);
	}

	/**
	 *
	 *
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static double getCosDihedralAngleNorm(final WB_Coord n1, final WB_Coord n2) {
		return -WB_Math.clamp(WB_Vector.dot(n1, n2), -1, 1);
	}

	/**
	 *
	 *
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static double getCosDihedralAngle(final WB_Coord n1, final WB_Coord n2) {
		final WB_Vector nn1 = new WB_Vector(n1);
		nn1.normalizeSelf();
		final WB_Vector nn2 = new WB_Vector(n2);
		nn2.normalizeSelf();
		return -WB_Math.clamp(WB_Vector.dot(nn1, nn2), -1, 1);
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static double getDihedralAngleNorm(final WB_Coord a, final WB_Coord b, final WB_Coord c, final WB_Coord d) {
		return Math.acos(getCosDihedralAngle(a, b, c, d));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	public static double getDihedralAngle(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		return Math.acos(getCosDihedralAngle(u, v, w));
	}

	/**
	 *
	 *
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static double getDihedralAngleNorm(final WB_Coord n1, final WB_Coord n2) {
		return Math.acos(getCosDihedralAngleNorm(n1, n2));
	}

	/**
	 *
	 *
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static double getDihedralAngle(final WB_Coord n1, final WB_Coord n2) {
		return Math.acos(getCosDihedralAngle(n1, n2));
	}

	/**
	 *
	 *
	 * @param p
	 * @param circle
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p, final WB_Circle circle) {
		final WB_Point pmc = WB_Point.sub(p, circle.getCenter());
		final WB_Point qmc = pmc.sub(WB_Point.mul(circle.getNormal(), pmc.dot(circle.getNormal())));
		if (qmc.isZero()) {
			qmc.set(perpendicular(circle.getNormal()));
		}
		final WB_Point rmc = qmc.mulSelf(circle.getRadius() / qmc.getLength());
		return rmc.addSelf(circle.getCenter());
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	static WB_Vector perpendicular(final WB_Coord n) {
		if (Math.abs(n.yd()) < Math.abs(n.zd())) {
			return WB_Vector.cross(n, WB_Vector.X());
		} else {
			return WB_Vector.cross(n, WB_Vector.Y());
		}
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return
	 */
	public static double getSignedAngleBetween(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz, final double nx, final double ny, final double nz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		final WB_Vector vn = new WB_Vector(nx, ny, nz);
		return Math.atan2(v1.cross(v0).dot(vn), v0.dot(v1));
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return
	 */
	public static double getSignedAngleBetween(final double cx, final double cy, final double cz, final double px,
			final double py, final double pz, final double qx, final double qy, final double qz, final double nx,
			final double ny, final double nz) {
		final WB_Vector v0 = new WB_Vector(px - cx, py - cy, pz - cz);
		final WB_Vector v1 = new WB_Vector(qx - cx, qy - cy, qz - cz);
		final WB_Vector vn = new WB_Vector(nx, ny, nz);
		return Math.atan2(v1.cross(v0).dot(vn), v0.dot(v1));
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return
	 */
	public static double getSignedAngleBetweenNorm(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz, final double nx, final double ny, final double nz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		final WB_Vector cross = v0.cross(v1);
		final double sign = cross.dot(new WB_Vector(nx, ny, nz));
		return sign < 0 ? -Math.acos(d) : Math.acos(d);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Sphere getBoundingSphereInCenter(final Collection<? extends WB_Coord> points) {
		double r = 0;
		final WB_Point center = new WB_Point();
		for (final WB_Coord p : points) {
			center.addSelf(p);
		}
		center.divSelf(points.size());
		for (final WB_Coord p : points) {
			final WB_Vector diff = WB_Vector.sub(p, center);
			final double radiusSqr = diff.dot(diff);
			if (radiusSqr > r) {
				r = radiusSqr;
			}
		}
		return new WB_Sphere(center, Math.sqrt(r));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Sphere getBoundingSphereInCenter(final WB_Coord[] points) {
		double r = 0;
		final WB_Point center = new WB_Point();
		for (final WB_Coord p : points) {
			center.addSelf(p);
		}
		center.divSelf(points.length);
		for (final WB_Coord p : points) {
			final WB_Vector diff = WB_Vector.sub(p, center);
			final double radiusSqr = diff.dot(diff);
			if (radiusSqr > r) {
				r = radiusSqr;
			}
		}
		return new WB_Sphere(center, Math.sqrt(r));
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static WB_Sphere mergeSpheres(final WB_Sphere S1, final WB_Sphere S2) {
		final WB_Vector cenDiff = WB_Vector.sub(S2.getCenter(), S1.getCenter());
		final double lenSqr = cenDiff.dot(cenDiff);
		final double rDiff = S2.getRadius() - S1.getRadius();
		final double rDiffSqr = rDiff * rDiff;
		if (rDiffSqr >= lenSqr) {
			return rDiff >= 0.0 ? S2 : S1;
		} else {
			final double length = Math.sqrt(lenSqr);
			WB_Point center;
			if (length > 0) {
				final double coeff = (length + rDiff) / (2.0 * length);
				center = WB_Point.addMul(S1.getCenter(), coeff, cenDiff);
			} else {
				center = new WB_Point(S1.getCenter());
			}
			final double radius = 0.5 * (length + S1.getRadius() + S2.getRadius());
			return new WB_Sphere(center, radius);
		}
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static double cotan(final WB_Coord v0, final WB_Coord v1) {
		return WB_Vector.dot(v0, v1) / WB_Vector.cross(v0, v1).getLength();
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double cotan(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2) {
		final WB_Vector v0 = WB_Vector.subToVector3D(p1, p0);
		final WB_Vector v1 = WB_Vector.subToVector3D(p2, p0);
		return WB_Vector.dot(v0, v1) / WB_Vector.cross(v0, v1).getLength();
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	public static double[] cross(final double ux, final double uy, final double uz, final double vx, final double vy,
			final double vz) {
		return new double[] { uy * vz - uz * vy, uz * vx - ux * vz, ux * vy - uy * vx };
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @return
	 */
	public static double[] cross(final double cx, final double cy, final double cz, final double px, final double py,
			final double pz, final double qx, final double qy, final double qz) {
		return cross(px - cx, py - cy, pz - cz, qx - cx, qy - cy, qz - cz);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	public static double dot(final double ux, final double uy, final double uz, final double vx, final double vy,
			final double vz) {
		final double k0 = ux * vx;
		final double k1 = uy * vy;
		final double k2 = uz * vz;
		final double exp0 = WB_Math.getExp(k0);
		final double exp1 = WB_Math.getExp(k1);
		final double exp2 = WB_Math.getExp(k2);
		if (exp0 < exp1) {
			if (exp0 < exp2) {
				return k1 + k2 + k0;
			} else {
				return k0 + k1 + k2;
			}
		} else {
			if (exp1 < exp2) {
				return k0 + k2 + k1;
			} else {
				return k0 + k1 + k2;
			}
		}
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	public static double getAngleBetween(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @return
	 */
	public static double getAngleBetween(final double cx, final double cy, final double cz, final double px,
			final double py, final double pz, final double qx, final double qy, final double qz) {
		final WB_Vector v0 = new WB_Vector(px - cx, py - cy, pz - cz);
		final WB_Vector v1 = new WB_Vector(qx - cx, qy - cy, qz - cz);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	public static double getAngleBetweenNorm(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	// POINT-POINT
	public static double getCosAngleBetween(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return d;
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @return
	 */
	public static double getCosAngleBetween(final double cx, final double cy, final double cz, final double px,
			final double py, final double pz, final double qx, final double qy, final double qz) {
		final WB_Vector v0 = new WB_Vector(px - cx, py - cy, pz - cz);
		final WB_Vector v1 = new WB_Vector(qx - cx, qy - cy, qz - cz);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return d;
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	public static double getCosAngleBetweenNorm(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return d;
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @return
	 */
	public static double getDistance3D(final double px, final double py, final double pz, final double qx,
			final double qy, final double qz) {
		return Math.sqrt((qx - px) * (qx - px) + (qy - py) * (qy - py) + (qz - pz) * (qz - pz));
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_AABB AABB) {
		return Math.sqrt(getSqDistance3D(p, AABB));
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double getDistance3D(final WB_Coord p, final WB_Coord q) {
		return Math.sqrt(getSqDistance3D(p, q));
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return
	 */
	public static double getLength3D(final double ux, final double uy, final double uz) {
		return Math.sqrt(ux * ux + uy * uy + uz * uz);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getLength3D(final WB_Coord p) {
		return Math.sqrt(p.xd() * p.xd() + p.yd() * p.yd() + p.zd() * p.zd());
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @return
	 */
	public static double getSqDistance3D(final double px, final double py, final double pz, final double qx,
			final double qy, final double qz) {
		return (qx - px) * (qx - px) + (qy - py) * (qy - py) + (qz - pz) * (qz - pz);
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_AABB AABB) {
		double sqDist = 0;
		double v = p.xd();
		if (v < AABB.getMinX()) {
			sqDist += (AABB.getMinX() - v) * (AABB.getMinX() - v);
		}
		if (v > AABB.getMaxX()) {
			sqDist += (v - AABB.getMaxX()) * (v - AABB.getMaxX());
		}
		v = p.yd();
		if (v < AABB.getMinY()) {
			sqDist += (AABB.getMinY() - v) * (AABB.getMinY() - v);
		}
		if (v > AABB.getMaxY()) {
			sqDist += (v - AABB.getMaxY()) * (v - AABB.getMaxY());
		}
		v = p.zd();
		if (v < AABB.getMinZ()) {
			sqDist += (AABB.getMinZ() - v) * (AABB.getMinZ() - v);
		}
		if (v > AABB.getMaxZ()) {
			sqDist += (v - AABB.getMaxZ()) * (v - AABB.getMaxZ());
		}
		return sqDist;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	// POINT-POINT
	public static double getSqDistance3D(final WB_Coord p, final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd()) + (q.yd() - p.yd()) * (q.yd() - p.yd())
				+ (q.zd() - p.zd()) * (q.zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return
	 */
	public static double getSqLength3D(final double ux, final double uy, final double uz) {
		return ux * ux + uy * uy + uz * uz;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getSqLength3D(final WB_Coord p) {
		return p.xd() * p.xd() + p.yd() * p.yd() + p.zd() * p.zd();
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @param t
	 * @return
	 */
	public static double[] interpolate(final double px, final double py, final double qx, final double qy,
			final double t) {
		return new double[] { px + t * (qx - px), py + t * (qy - py) };
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @param t
	 * @return
	 */
	public static double[] interpolate(final double px, final double py, final double pz, final double qx,
			final double qy, final double qz, final double t) {
		return new double[] { px + t * (qx - px), py + t * (qy - py), pz + t * (qz - pz) };
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @param qx
	 * @param qy
	 * @param qz
	 * @param t
	 * @param ease
	 * @param type
	 * @return
	 */
	public static double[] interpolateEase(final double px, final double py, final double pz, final double qx,
			final double qy, final double qz, final double t, final WB_Ease ease, final WB_Ease.EaseType type) {
		double et;
		switch (type) {
		case IN:
			et = ease.easeIn(t);
			break;
		case INOUT:
			et = ease.easeInOut(t);
			break;
		case OUT:
			et = ease.easeOut(t);
			break;
		default:
			et = ease.easeIn(t);
			break;
		}
		return new double[] { px + et * (qx - px), py + et * (qy - py), pz + et * (qz - pz) };
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @param t
	 * @param ease
	 * @param type
	 * @return
	 */
	public static double[] interpolateEase(final double px, final double py, final double qx, final double qy,
			final double t, final WB_Ease ease, final WB_Ease.EaseType type) {
		double et;
		switch (type) {
		case IN:
			et = ease.easeIn(t);
			break;
		case INOUT:
			et = ease.easeInOut(t);
			break;
		case OUT:
			et = ease.easeOut(t);
			break;
		default:
			et = ease.easeIn(t);
			break;
		}
		return new double[] { px + et * (qx - px), py + et * (qy - py) };
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isCollinear(final WB_Coord o, final WB_Coord p, final WB_Coord q) {
		if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToPoint3D(p, q))) {
			return true;
		}
		if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToPoint3D(o, q))) {
			return true;
		}
		if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToPoint3D(o, p))) {
			return true;
		}
		return WB_Epsilon.isZeroSq(WB_Vector.sub(o, p).crossSelf(WB_Vector.sub(o, q)).getSqLength3D());
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static boolean isOrthogonal(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(
				dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd()) / (getLength3D(v0) * getLength3D(v1)));
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static boolean isOrthogonal(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())
				/ (getLength3D(v0) * getLength3D(v1))) < epsilon;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static boolean isOrthogonalNorm(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd()));
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static boolean isOrthogonalNorm(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())) < epsilon;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static boolean isParallel(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())
				/ (getLength3D(v0) * getLength3D(v1))) < WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static boolean isParallel(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())
				/ (getLength3D(v0) * getLength3D(v1))) < epsilon;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static boolean isParallelNorm(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())) < WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static boolean isParallelNorm(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())) < epsilon;
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @return
	 */
	public static boolean isParallelNormX(final WB_Coord o, final WB_Coord p) {
		return WB_Vector.cross(o, p).getLength() < WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @param t
	 * @return
	 */
	public static boolean isParallelNormX(final WB_Coord o, final WB_Coord p, final double t) {
		return WB_Vector.cross(o, p).getLength() < t + WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @return
	 */
	public static boolean isParallelX(final WB_Coord o, final WB_Coord p) {
		final double pm2 = p.xd() * p.xd() + p.yd() * p.yd() + p.zd() * p.zd();
		return WB_Vector.cross(o, p).getSqLength3D() / (pm2 * WB_Vector.getSqLength3D(o)) < WB_Epsilon.SQEPSILON;
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @param t
	 * @return
	 */
	public static boolean isParallelX(final WB_Coord o, final WB_Coord p, final double t) {
		final double pm2 = p.xd() * p.xd() + p.yd() * p.yd() + p.zd() * p.zd();
		return WB_Vector.cross(o, p).getSqLength3D() / (pm2 * WB_Vector.getSqLength3D(o)) < t + WB_Epsilon.SQEPSILON;
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return
	 */
	public static boolean isZero3D(final double ux, final double uy, final double uz) {
		return getSqLength3D(ux, uy, uz) < WB_Epsilon.SQEPSILON;
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @param wx
	 * @param wy
	 * @param wz
	 * @return
	 */
	public static double scalarTriple(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz, final double wx, final double wy, final double wz) {
		final double[] c = cross(vx, vy, vz, wx, wy, wz);
		return dot(ux, uy, uz, c[0], c[1], c[2]);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param vx
	 * @param vy
	 * @param vz
	 * @return
	 */
	public static double[][] tensor3D(final double ux, final double uy, final double uz, final double vx,
			final double vy, final double vz) {
		return new double[][] { { ux * vx, ux * vy, ux * vz }, { uy * vx, uy * vy, uy * vz },
				{ uz * vx, uz * vy, uz * vz } };
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static final WB_Intersection getIntersection2D(final WB_Segment S1, final WB_Segment S2) {
		final double a1 = twiceSignedTriArea2D(S1.getOrigin(), S1.getEndpoint(), S2.getEndpoint());
		final double a2 = twiceSignedTriArea2D(S1.getOrigin(), S1.getEndpoint(), S2.getOrigin());
		if (!WB_Epsilon.isZero(a1) && !WB_Epsilon.isZero(a2) && a1 * a2 < 0) {
			final double a3 = twiceSignedTriArea2D(S2.getOrigin(), S2.getEndpoint(), S1.getOrigin());
			final double a4 = a3 + a2 - a1;
			if (a3 * a4 < 0) {
				final double t1 = a3 / (a3 - a4);
				final double t2 = a1 / (a1 - a2);
				final WB_Intersection i = new WB_Intersection();
				i.intersection = true;
				i.t1 = t1;
				i.t2 = t2;
				i.object = S1.getParametricPoint(t1);
				i.dimension = 0;
				i.sqDist = 0;
				return i;
			}
		}
		return NOINTERSECTION();
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @param i
	 */
	public static final void getIntersection2DInto(final WB_Segment S1, final WB_Segment S2, final WB_Intersection i) {
		final double a1 = twiceSignedTriArea2D(S1.getOrigin(), S1.getEndpoint(), S2.getEndpoint());
		final double a2 = twiceSignedTriArea2D(S1.getOrigin(), S1.getEndpoint(), S2.getOrigin());
		if (!WB_Epsilon.isZero(a1) && !WB_Epsilon.isZero(a2) && a1 * a2 < 0) {
			final double a3 = twiceSignedTriArea2D(S2.getOrigin(), S2.getEndpoint(), S1.getOrigin());
			final double a4 = a3 + a2 - a1;
			if (a3 * a4 < 0) {
				final double t1 = a3 / (a3 - a4);
				final double t2 = a1 / (a1 - a2);
				i.intersection = true;
				i.t1 = t1;
				i.t2 = t2;
				i.object = S1.getParametricPoint(t1);
				i.dimension = 0;
				i.sqDist = 0;
			}
		} else {
			i.intersection = false;
			i.t1 = 0;
			i.t2 = 0;
			i.sqDist = Float.POSITIVE_INFINITY;
		}
	}

	/**
	 *
	 *
	 * @param S
	 * @param L
	 * @return
	 */
	public static final WB_Segment[] splitSegment2D(final WB_Segment S, final WB_Line L) {
		WB_Segment[] result = new WB_Segment[2];
		final WB_Intersection ir2D = getClosestPoint2D(S, L);
		if (!ir2D.intersection) {
			result = new WB_Segment[1];
			result[0] = S;
		}
		if (ir2D.dimension == 0) {
			result = new WB_Segment[2];
			if (classifyPointToLine2D(S.getOrigin(), L) == WB_Classification.FRONT) {
				result[0] = new WB_Segment(S.getOrigin(), (WB_Point) ir2D.object);
				result[1] = new WB_Segment((WB_Point) ir2D.object, S.getEndpoint());
			} else if (classifyPointToLine2D(S.getOrigin(), L) == WB_Classification.BACK) {
				result[1] = new WB_Segment(S.getOrigin(), (WB_Point) ir2D.object);
				result[0] = new WB_Segment((WB_Point) ir2D.object, S.getEndpoint());
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param L
	 * @return
	 */
	public static final WB_Polygon[] splitPolygon2D(final WB_Polygon poly, final WB_Line L) {
		final ArrayList<WB_Coord> frontVerts = new ArrayList<>(20);
		final ArrayList<WB_Coord> backVerts = new ArrayList<>(20);
		final int numVerts = poly.numberOfShellPoints;
		if (numVerts > 0) {
			WB_Coord a = poly.getPoint(numVerts - 1);
			WB_Classification aSide = classifyPointToLine2D(a, L);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				WB_Intersection i = new WB_Intersection();
				b = poly.getPoint(n);
				bSide = classifyPointToLine2D(b, L);
				if (bSide == WB_Classification.FRONT) {
					if (aSide == WB_Classification.BACK) {
						i = getClosestPoint2D(L, new WB_Segment(a, b));
						WB_Coord p1 = null;
						if (i.dimension == 0) {
							p1 = (WB_Point) i.object;
						} else if (i.dimension == 1) {
							p1 = ((WB_Segment) i.object).getOrigin();
						}
						frontVerts.add(p1);
						backVerts.add(p1);
					}
					frontVerts.add(b);
				} else if (bSide == WB_Classification.BACK) {
					if (aSide == WB_Classification.FRONT) {
						i = getClosestPoint2D(L, new WB_Segment(a, b));
						final WB_Point p1 = (WB_Point) i.object;
						frontVerts.add(p1);
						backVerts.add(p1);
					} else if (aSide == WB_Classification.ON) {
						backVerts.add(a);
					}
					backVerts.add(b);
				} else {
					frontVerts.add(b);
					if (aSide == WB_Classification.BACK) {
						backVerts.add(b);
					}
				}
				a = b;
				aSide = bSide;
			}
		}
		final WB_Polygon[] result = new WB_Polygon[2];
		result[0] = gf.createSimplePolygon(frontVerts);
		result[1] = gf.createSimplePolygon(backVerts);
		return result;
	}

	/**
	 *
	 *
	 * @param C0
	 * @param C1
	 * @return
	 */
	public static final ArrayList<WB_Point> getIntersection2D(final WB_Circle C0, final WB_Circle C1) {
		final ArrayList<WB_Point> result = new ArrayList<>();
		final WB_Point u = WB_Point.sub(C1.getCenter(), C0.getCenter());
		final double d2 = u.getSqLength2D();
		final double d = Math.sqrt(d2);
		if (WB_Epsilon.isEqualAbs(d, C0.getRadius() + C1.getRadius())) {
			result.add(gf.createInterpolatedPoint(C0.getCenter(), C1.getCenter(),
					C0.getRadius() / (C0.getRadius() + C1.getRadius())));
			return result;
		}
		if (d > C0.getRadius() + C1.getRadius() || d < WB_Math.fastAbs(C0.getRadius() - C1.getRadius())) {
			return result;
		}
		final double r02 = C0.getRadius() * C0.getRadius();
		final double r12 = C1.getRadius() * C1.getRadius();
		final double a = (r02 - r12 + d2) / (2 * d);
		final double h = Math.sqrt(r02 - a * a);
		final WB_Point c = u.mul(a / d).addSelf(C0.getCenter());
		final double p0x = c.xd() + h * (C1.getCenter().yd() - C0.getCenter().yd()) / d;
		final double p0y = c.yd() - h * (C1.getCenter().xd() - C0.getCenter().xd()) / d;
		final double p1x = c.xd() - h * (C1.getCenter().yd() - C0.getCenter().yd()) / d;
		final double p1y = c.yd() + h * (C1.getCenter().xd() - C0.getCenter().xd()) / d;
		final WB_Point p0 = new WB_Point(p0x, p0y);
		result.add(p0);
		final WB_Point p1 = new WB_Point(p1x, p1y);
		if (!WB_Epsilon.isZeroSq(getSqDistance2D(p0, p1))) {
			result.add(new WB_Point(p1x, p1y));
		}
		return result;
	}

	/**
	 *
	 *
	 * @param L
	 * @param C
	 * @return
	 */
	public static final ArrayList<WB_Point> getIntersection2D(final WB_Line L, final WB_Circle C) {
		final ArrayList<WB_Point> result = new ArrayList<>();
		final double b = 2 * (L.getDirection().xd() * (L.getOrigin().xd() - C.getCenter().xd())
				+ L.getDirection().yd() * (L.getOrigin().yd() - C.getCenter().yd()));
		final double c = getSqLength2D(C.getCenter()) + WB_Vector.getSqLength2D(L.getOrigin())
				- 2 * (C.getCenter().xd() * L.getOrigin().xd() + C.getCenter().yd() * L.getOrigin().yd())
				- C.getRadius() * C.getRadius();
		double disc = b * b - 4 * c;
		if (disc < -WB_Epsilon.EPSILON) {
			return result;
		}
		if (WB_Epsilon.isZero(disc)) {
			result.add(L.getPoint(-0.5 * b));
			return result;
		}
		disc = Math.sqrt(disc);
		result.add(L.getPoint(0.5 * (-b + disc)));
		result.add(L.getPoint(0.5 * (-b - disc)));
		return result;
	}

	/**
	 *
	 *
	 * @param R
	 * @param C
	 * @return
	 */
	public static final ArrayList<WB_Point> getIntersection2D(final WB_Ray R, final WB_Circle C) {
		final ArrayList<WB_Point> result = new ArrayList<>();
		final double b = 2 * (R.getDirection().xd() * (R.getOrigin().xd() - C.getCenter().xd())
				+ R.getDirection().yd() * (R.getOrigin().yd() - C.getCenter().yd()));
		final double c = getSqLength2D(C.getCenter()) + WB_Vector.getSqLength2D(R.getOrigin())
				- 2 * (C.getCenter().xd() * R.getOrigin().xd() + C.getCenter().yd() * R.getOrigin().yd())
				- C.getRadius() * C.getRadius();
		double disc = b * b - 4 * c;
		if (disc < -WB_Epsilon.EPSILON) {
			return result;
		}
		if (WB_Epsilon.isZero(disc)) {
			if (-0.5 * b >= 0) {
				result.add(R.getPoint(-0.5 * b));
			}
			return result;
		}
		disc = Math.sqrt(disc);
		if (-b + disc >= 0) {
			result.add(R.getPoint(0.5 * (-b + disc)));
		}
		if (-b - disc >= 0) {
			result.add(R.getPoint(0.5 * (-b - disc)));
		}
		return result;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static final boolean checkIntersection2DProper(final WB_Coord a, final WB_Coord b, final WB_Coord c,
			final WB_Coord d) {
		if (WB_Predicates.orient2D(a, b, c) == 0 || WB_Predicates.orient2D(a, b, d) == 0
				|| WB_Predicates.orient2D(c, d, a) == 0 || WB_Predicates.orient2D(c, d, b) == 0) {
			return false;
		} else if (WB_Predicates.orient2D(a, b, c) * WB_Predicates.orient2D(a, b, d) > 0
				|| WB_Predicates.orient2D(c, d, a) * WB_Predicates.orient2D(c, d, b) > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final WB_Segment S) {
		final WB_Vector ab = new WB_Vector(S.getOrigin(), S.getEndpoint());
		final WB_Vector ac = new WB_Vector(S.getOrigin(), p);
		double t = ac.dot(ab);
		if (t <= 0) {
			t = 0;
			return new WB_Point(S.getOrigin());
		} else {
			final double denom = S.getLength() * S.getLength();
			if (t >= denom) {
				t = 1;
				return new WB_Point(S.getEndpoint());
			} else {
				t = t / denom;
				return new WB_Point(S.getParametricPoint(t));
			}
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param PL
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final WB_PolyLine PL) {
		double d2, d2min = Double.POSITIVE_INFINITY;
		WB_Point q = null, test = null;
		WB_Segment S;
		for (int s = 0; s < PL.getNumberSegments(); s++) {
			S = PL.getSegment(s);
			final WB_Vector ab = new WB_Vector(S.getOrigin(), S.getEndpoint());
			final WB_Vector ac = new WB_Vector(S.getOrigin(), p);
			double t = ac.dot(ab);
			if (t <= 0) {
				t = 0;
				test = new WB_Point(S.getOrigin());
			} else {
				final double denom = S.getLength() * S.getLength();
				if (t >= denom) {
					t = 1;
					test = new WB_Point(S.getEndpoint());
				} else {
					t = t / denom;
					test = new WB_Point(S.getParametricPoint(t));
				}
			}
			d2 = getSqDistance2D(p, test);
			if (d2 < d2min) {
				d2min = d2;
				q = test;
			}
		}
		return q;
	}

	/**
	 *
	 *
	 * @param S
	 * @param p
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Segment S, final WB_Coord p) {
		return getClosestPoint2D(p, S);
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final WB_Point getClosestPointToSegment2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		double t = ac.dot(ab);
		if (t <= 0) {
			t = 0;
			return new WB_Point(a);
		} else {
			final double denom = ab.dot(ab);
			if (t >= denom) {
				t = 1;
				return new WB_Point(b);
			} else {
				t = t / denom;
				return new WB_Point(a.xd() + t * ab.xd(), a.yd() + t * ab.yd());
			}
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final WB_Line L) {
		if (WB_Epsilon.isZero(L.getDirection().xd())) {
			return new WB_Point(L.getOrigin().xd(), p.yd());
		}
		if (WB_Epsilon.isZero(L.getDirection().yd())) {
			return new WB_Point(p.xd(), L.getOrigin().yd());
		}
		final double m = L.getDirection().yd() / L.getDirection().xd();
		final double b = L.getOrigin().yd() - m * L.getOrigin().xd();
		final double x = (m * p.yd() + p.xd() - m * b) / (m * m + 1);
		final double y = (m * m * p.yd() + m * p.xd() + b) / (m * m + 1);
		return new WB_Point(x, y);
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final WB_Point getClosestPointToLine2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Line L = new WB_Line();
		L.setFromPoints(a, b);
		return getClosestPoint2D(p, L);
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final WB_Ray R) {
		final WB_Vector ac = new WB_Vector(R.getOrigin(), p);
		double t = ac.dot(R.getDirection());
		if (t <= 0) {
			t = 0;
			return new WB_Point(R.getOrigin());
		} else {
			return R.getPoint(t);
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final WB_Point getClosestPointToRay2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Ray R = new WB_Ray();
		R.setFromPoints(a, b);
		return getClosestPoint2D(p, R);
	}

	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static final WB_Intersection getClosestPoint2D(final WB_Segment S1, final WB_Segment S2) {
		final WB_Point d1 = WB_Point.sub(S1.getEndpoint(), S1.getOrigin());
		final WB_Point d2 = WB_Point.sub(S2.getEndpoint(), S2.getOrigin());
		final WB_Point r = WB_Point.sub(S1.getOrigin(), S2.getOrigin());
		final double a = d1.dot(d1);
		final double e = d2.dot(d2);
		final double f = d2.dot(r);
		if (WB_Epsilon.isZero(a) || WB_Epsilon.isZero(e)) {
			final WB_Intersection i = new WB_Intersection();
			i.intersection = false;
			i.t1 = 0;
			i.t2 = 0;
			i.object = new WB_Segment(S1.getOrigin(), S2.getOrigin());
			i.dimension = 1;
			i.sqDist = r.getSqLength2D();
			return i;
		}
		double t1 = 0;
		double t2 = 0;
		if (WB_Epsilon.isZero(a)) {
			t2 = WB_Math.clamp(f / e, 0, 1);
		} else {
			final double c = d1.dot(r);
			if (WB_Epsilon.isZero(e)) {
				t1 = WB_Math.clamp(-c / a, 0, 1);
			} else {
				final double b = d1.dot(d2);
				final double denom = a * e - b * b;
				if (!WB_Epsilon.isZero(denom)) {
					t1 = WB_Math.clamp((b * f - c * e) / denom, 0, 1);
				} else {
					t1 = 0;
				}
				final double tnom = b * t1 + f;
				if (tnom < 0) {
					t1 = WB_Math.clamp(-c / a, 0, 1);
				} else if (tnom > e) {
					t2 = 1;
					t1 = WB_Math.clamp((b - c) / a, 0, 1);
				} else {
					t2 = tnom / e;
				}
			}
		}
		final WB_Intersection i = new WB_Intersection();
		i.intersection = t1 > 0 && t1 < 1 && t2 > 0 && t2 < 1;
		i.t1 = t1;
		i.t2 = t2;
		final WB_Point p1 = S1.getParametricPoint(t1);
		final WB_Point p2 = S2.getParametricPoint(t2);
		i.sqDist = getSqDistance2D(p1, p2);
		if (WB_Epsilon.isZeroSq(i.sqDist)) {
			i.dimension = 0;
			i.object = p1;
		} else {
			i.dimension = 1;
			i.object = new WB_Segment(p1, p2);
		}
		return i;
	}

	/**
	 *
	 *
	 * @param L1
	 * @param L2
	 * @return
	 */
	public static final WB_Intersection getClosestPoint2D(final WB_Line L1, final WB_Line L2) {
		final double a = WB_Vector.dot(L1.getDirection(), L1.getDirection());
		final double b = WB_Vector.dot(L1.getDirection(), L2.getDirection());
		final WB_Point r = WB_Point.sub(L1.getOrigin(), L2.getOrigin());
		final double c = WB_Vector.dot(L1.getDirection(), r);
		final double e = WB_Vector.dot(L2.getDirection(), L2.getDirection());
		final double f = WB_Vector.dot(L2.getDirection(), r);
		double denom = a * e - b * b;
		if (WB_Epsilon.isZero(denom)) {
			final double t2 = r.dot(L1.getDirection());
			final WB_Point p2 = new WB_Point(L2.getPoint(t2));
			final double d2 = getSqDistance2D(L1.getOrigin(), p2);
			final WB_Intersection i = new WB_Intersection();
			i.intersection = false;
			i.t1 = 0;
			i.t2 = t2;
			i.dimension = 1;
			i.object = new WB_Segment(L1.getOrigin(), p2);
			i.sqDist = d2;
			return i;
		}
		denom = 1.0 / denom;
		final double t1 = (b * f - c * e) * denom;
		final double t2 = (a * f - b * c) * denom;
		final WB_Point p1 = new WB_Point(L1.getPoint(t1));
		final WB_Point p2 = new WB_Point(L2.getPoint(t2));
		final double d2 = getSqDistance2D(p1, p2);
		final WB_Intersection i = new WB_Intersection();
		i.intersection = true;
		i.t1 = t1;
		i.t2 = t2;
		i.dimension = 0;
		i.object = p1;
		i.sqDist = d2;
		return i;
	}

	/**
	 *
	 *
	 * @param L
	 * @param S
	 * @return
	 */
	public static final WB_Intersection getClosestPoint2D(final WB_Line L, final WB_Segment S) {
		final WB_Intersection i = getClosestPoint2D(L, new WB_Line(S.getOrigin(), S.getDirection()));
		if (i.dimension == 0) {
			return i;
		}
		if (i.t2 <= WB_Epsilon.EPSILON) {
			i.t2 = 0;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(), S.getOrigin());
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		if (i.t2 >= S.getLength() - WB_Epsilon.EPSILON) {
			i.t2 = 1;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(), S.getEndpoint());
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		return i;
	}

	/**
	 *
	 *
	 * @param S
	 * @param L
	 * @return
	 */
	public static final WB_Intersection getClosestPoint2D(final WB_Segment S, final WB_Line L) {
		return getClosestPoint2D(L, S);
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	// POINT-TRIANGLE
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final WB_Triangle T) {
		final WB_Vector ab = WB_Vector.subToVector3D(T.p2, T.p1);
		final WB_Vector ac = WB_Vector.subToVector3D(T.p3, T.p1);
		final WB_Vector ap = new WB_Vector(T.p1, p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Point(T.p1);
		}
		final WB_Vector bp = new WB_Vector(T.p2, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Point(T.p2);
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Point(T.p1).addMulSelf(v, ab);
		}
		final WB_Vector cp = new WB_Vector(T.p3, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Point(T.p3);
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Point(T.p1).addMulSelf(w, ac);
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Point(T.p2).addMulSelf(w, WB_Point.sub(T.p3, T.p2));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return new WB_Point(T.p1).addSelf(ab.mulSelf(v).addSelf(ac.mulSelf(w)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static final WB_Point getClosestPointToTriangle2D(final WB_Coord p, final WB_Coord a, final WB_Coord b,
			final WB_Coord c) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, c);
		final WB_Vector ap = new WB_Vector(a, p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Point(a);
		}
		final WB_Vector bp = new WB_Vector(b, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Point(b);
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Point(a).addMulSelf(v, ab);
		}
		final WB_Vector cp = new WB_Vector(c, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Point(c);
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Point(a).addMulSelf(w, ac);
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Point(b).addMulSelf(w, new WB_Vector(b, c));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return new WB_Point(a).addMulSelf(w, ac).addMulSelf(v, ab);
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static final WB_Point getClosestPointOnPeriphery2D(final WB_Coord p, final WB_Triangle T) {
		final WB_Vector ab = WB_Vector.subToVector3D(T.p2, T.p1);
		final WB_Vector ac = WB_Vector.subToVector3D(T.p3, T.p1);
		final WB_Vector ap = new WB_Vector(T.p1, p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Point(T.p1);
		}
		final WB_Vector bp = new WB_Vector(T.p2, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Point(T.p2);
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Point(T.p1).addMulSelf(v, ab);
		}
		final WB_Vector cp = new WB_Vector(T.p3, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Point(T.p3);
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Point(T.p1).addMulSelf(w, ac);
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Point(T.p2).addMulSelf(w, WB_Point.sub(T.p3, T.p2));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		final double u = 1 - v - w;
		if (WB_Epsilon.isZero(u - 1)) {
			return new WB_Point(T.p1);
		}
		if (WB_Epsilon.isZero(v - 1)) {
			return new WB_Point(T.p2);
		}
		if (WB_Epsilon.isZero(w - 1)) {
			return new WB_Point(T.p3);
		}
		final WB_Point A = getClosestPointToSegment2D(p, T.p2, T.p3);
		final double dA2 = getSqDistance2D(p, A);
		final WB_Point B = getClosestPointToSegment2D(p, T.p1, T.p3);
		final double dB2 = getSqDistance2D(p, B);
		final WB_Point C = getClosestPointToSegment2D(p, T.p1, T.p2);
		final double dC2 = getSqDistance2D(p, C);
		if (dA2 < dB2 && dA2 < dC2) {
			return A;
		} else if (dB2 < dA2 && dB2 < dC2) {
			return B;
		} else {
			return C;
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	// POINT-POLYGON
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final WB_Polygon poly) {
		final int[] tris = poly.getTriangles();
		final int n = tris.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = getClosestPointToTriangle2D(p, poly.getPoint(tris[i]), poly.getPoint(tris[i + 1]),
					poly.getPoint(tris[i + 2]));
			final double d2 = getDistance2D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param p
	 * @param tris
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p, final ArrayList<? extends WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint2D(p, T);
			final double d2 = getDistance2D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static final WB_Point getClosestPointOnPeriphery2D(final WB_Coord p, final WB_Polygon poly) {
		final int[] tris = poly.getTriangles();
		final int n = tris.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = getClosestPointToTriangle2D(p, poly.getPoint(tris[i]), poly.getPoint(tris[i + 1]),
					poly.getPoint(tris[i + 2]));
			final double d2 = getSqDistance2D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfShellPoints() - 1; i < poly.getNumberOfShellPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint2D(p, S);
				final double d2 = getSqDistance2D(tmp, p);
				if (d2 < dmax2) {
					closest = tmp;
					dmax2 = d2;
				}
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @param tris
	 * @return
	 */
	public static final WB_Point getClosestPointOnPeriphery2D(final WB_Coord p, final WB_Polygon poly,
			final ArrayList<WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint2D(p, T);
			final double d2 = getSqDistance2D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfShellPoints() - 1; i < poly.getNumberOfShellPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint2D(p, S);
				final double d2 = getSqDistance2D(tmp, p);
				if (d2 < dmax2) {
					closest = tmp;
					dmax2 = d2;
				}
			}
		}
		return closest;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static final boolean between2D(final WB_Coord a, final WB_Coord b, final WB_Coord c) {
		if (isCoincident2D(a, c)) {
			return true;
		} else if (isCoincident2D(b, c)) {
			return true;
		} else {
			if (getSqDistanceToLine2D(c, a, b) < WB_Epsilon.SQEPSILON) {
				final double d = getParameterOfPointOnLine2D(c, a, b);
				if (0 <= d && d <= 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static final boolean betweenStrict2D(final WB_Coord a, final WB_Coord b, final WB_Coord c) {
		if (isCoincident2D(a, c)) {
			return false;
		} else if (isCoincident2D(b, c)) {
			return false;
		} else {
			if (getSqDistanceToLine2D(c, a, b) < WB_Epsilon.SQEPSILON) {
				final double d = getParameterOfPointOnLine2D(c, a, b);
				if (0 < d && d < 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static final double getParameterOfPointOnLine2D(final WB_Coord a, final WB_Coord b, final WB_Coord p) {
		double x1, x2, y1, y2;
		x1 = b.xd() - a.xd();
		x2 = p.xd() - a.xd();
		y1 = b.yd() - a.yd();
		y2 = p.yd() - a.yd();
		return (x1 * x2 + y1 * y2) / (x1 * x1 + y1 * y1);
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getParameterOfPointOnLine2D(final WB_Coord p, final WB_Line L) {
		final WB_Vector ab = new WB_Vector(L.direction.xd(), L.direction.yd());
		final WB_Vector ac = new WB_Vector(p.xd() - L.origin.xd(), p.yd() - L.origin.yd());
		return ac.dot2D(ab);
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static final boolean contains2D(final WB_Coord p, final WB_AABB2D AABB) {
		return p.xd() >= AABB.getMinX() && p.yd() >= AABB.getMinY() && p.xd() < AABB.getMaxX()
				&& p.yd() < AABB.getMaxY();
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getDistanceToLine2D(final WB_Coord p, final WB_Line L) {
		return Math.sqrt(getSqDistanceToLine2D(p, L));
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p, final WB_Coord q) {
		return Math.sqrt(getSqDistance2D(p, q));
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p, final WB_Segment S) {
		return Math.sqrt(getSqDistance2D(p, S));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p, final WB_Line L) {
		return Math.sqrt(getSqDistance2D(p, L));
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p, final WB_Ray R) {
		return Math.sqrt(getSqDistance2D(p, R));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final double getDistanceToLine2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToLine2D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getDistanceToPoint2D(final WB_Coord p, final WB_Coord q) {
		return Math.sqrt(getSqDistanceToPoint2D(p, q));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final double getDistanceToRay2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToRay2D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final double getDistanceToSegment2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToSegment2D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p, final WB_Segment S) {
		final WB_Vector ab = new WB_Vector(S.getOrigin(), S.getEndpoint());
		final WB_Vector ac = new WB_Vector(p).sub(S.getOrigin());
		final WB_Vector bc = new WB_Vector(p).sub(S.getEndpoint());
		final double e = ac.dot2D(ab);
		if (e <= 0) {
			return ac.dot2D(ac);
		}
		final double f = ab.dot2D(ab);
		if (e >= f) {
			return bc.dot2D(bc);
		}
		return ac.dot2D(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param PL
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p, final WB_PolyLine PL) {
		double d2min = Double.POSITIVE_INFINITY;
		double d2;
		for (int s = 0; s < PL.getNumberSegments(); s++) {
			d2 = getSqDistance2D(p, PL.getSegment(s));
			if (d2 < d2min) {
				d2min = d2;
			}
		}
		return d2min;
	}

	/**
	 *
	 *
	 * @param p
	 * @param PL
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p, final WB_PolyLine PL) {
		return Math.sqrt(getSqDistance2D(p, PL));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p, final WB_Line L) {
		final WB_Coord ab = L.getDirection();
		final WB_Vector ac = new WB_Vector(L.getOrigin(), p);
		final double e = ac.dot2D(ab);
		final double f = WB_Vector.dot2D(ab, ab);
		return ac.dot2D(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p, final WB_Ray R) {
		final WB_Coord ab = R.getDirection();
		final WB_Vector ac = new WB_Vector(R.getOrigin(), p);
		final double e = ac.dot2D(ab);
		if (e <= 0) {
			return ac.dot2D(ac);
		}
		final double f = WB_Vector.dot2D(ab, ab);
		return ac.dot2D(ac) - e * e / f;
	}
	// POINT-POINT
	// POINT-PLANE

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	// POINT-SEGMENT
	public static final double getSqDistanceToLine2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		final double e = ac.dot2D(ab);
		final double f = ab.dot2D(ab);
		return ac.dot2D(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getSqDistanceToLine2D(final WB_Coord p, final WB_Line L) {
		final WB_Point ab = gf.createPoint(L.getDirection().xd(), L.getDirection().yd());
		final WB_Point ac = gf.createPoint(p.xd() - L.getOrigin().xd(), p.yd() - L.getOrigin().yd());
		final double e = ac.dot2D(ab);
		final double f = ab.dot2D(ab);
		return ac.dot2D(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getSqDistanceToPoint2D(final WB_Coord p, final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd()) + (q.yd() - p.yd()) * (q.yd() - p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p, final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd()) + (q.yd() - p.yd()) * (q.yd() - p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	// POINT-RAY
	public static final double getSqDistanceToRay2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		final double e = ac.dot2D(ab);
		if (e <= 0) {
			return ac.dot2D(ac);
		}
		final double f = ab.dot2D(ab);
		return ac.dot2D(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	// POINT-AABB
	public static final double getSqDistanceToSegment2D(final WB_Coord p, final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		final WB_Vector bc = new WB_Vector(b, p);
		final double e = ac.dot2D(ab);
		if (e <= 0) {
			return ac.dot2D(ac);
		}
		final double f = ab.dot2D(ab);
		if (e >= f) {
			return bc.dot2D(bc);
		}
		return ac.dot2D(ac) - e * e / f;
	}

	/**
	 *
	 *
	 * @param points
	 * @param dir
	 * @return
	 */
	public static final int[] getExtremePointsAlongDirection2D(final WB_Coord[] points, final WB_Coord dir) {
		final int[] result = new int[] { -1, -1 };
		double minproj = Double.POSITIVE_INFINITY;
		double maxproj = Double.NEGATIVE_INFINITY;
		double proj;
		for (int i = 0; i < points.length; i++) {
			proj = WB_Vector.dot2D(points[i], dir);
			if (proj < minproj) {
				minproj = proj;
				result[0] = i;
			}
			if (proj > maxproj) {
				maxproj = proj;
				result[1] = i;
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param points
	 * @param dir
	 * @return
	 */
	public static final int[] getExtremePointsAlongDirection2D(final Collection<? extends WB_Coord> points,
			final WB_Coord dir) {
		final int[] result = new int[] { -1, -1 };
		double minproj = Double.POSITIVE_INFINITY;
		double maxproj = Double.NEGATIVE_INFINITY;
		double proj;
		int i = 0;
		for (final WB_Coord point : points) {
			proj = WB_Vector.dot2D(point, dir);
			if (proj < minproj) {
				minproj = proj;
				result[0] = i;
			}
			if (proj > maxproj) {
				maxproj = proj;
				result[1] = i;
			}
			i++;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final WB_Classification classifyPointToLine2D(final WB_Coord p, final WB_Line L) {
		final double dist = -L.getDirection().yd() * p.xd() + L.getDirection().xd() * p.yd()
				+ L.getOrigin().xd() * L.getDirection().yd() - L.getOrigin().yd() * L.getDirection().xd();
		if (dist > WB_Epsilon.EPSILON) {
			return WB_Classification.FRONT;
		}
		if (dist < -WB_Epsilon.EPSILON) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	/**
	 *
	 *
	 * @param p
	 * @param C
	 * @return
	 */
	public static final WB_Classification classifyPointToCircle2D(final WB_Coord p, final WB_Circle C) {
		final double dist = getDistanceToPoint2D(p, C.getCenter());
		if (WB_Epsilon.isZero(dist - C.getRadius())) {
			return WB_Classification.ON;
		} else if (dist < C.getRadius()) {
			return WB_Classification.INSIDE;
		} else {
			return WB_Classification.OUTSIDE;
		}
	}

	/**
	 *
	 *
	 * @param C1
	 * @param C2
	 * @return
	 */
	public static final WB_Classification classifyCircleToCircle2D(final WB_Circle C1, final WB_Circle C2) {
		if (C1.equals(C2)) {
			return WB_Classification.ON;
		}
		final double dist = getDistanceToPoint2D(C1.getCenter(), C2.getCenter());
		final double rsum = C1.getRadius() + C2.getRadius();
		final double rdiff = Math.abs(C1.getRadius() - C2.getRadius());
		if (dist >= rsum) {
			return WB_Classification.OUTSIDE;
		} else if (dist <= rdiff) {
			if (C1.getRadius() < C2.getRadius()) {
				return WB_Classification.INSIDE;
			} else {
				return WB_Classification.CONTAINING;
			}
		}
		return WB_Classification.CROSSING;
	}

	/**
	 *
	 *
	 * @param C
	 * @param L
	 * @return
	 */
	public static final WB_Classification classifyCircleToLine2D(final WB_Circle C, final WB_Line L) {
		final double d = getDistanceToLine2D(C.getCenter(), L);
		if (WB_Epsilon.isZero(d - C.getRadius())) {
			return WB_Classification.TANGENT;
		} else if (d < C.getRadius()) {
			return WB_Classification.CROSSING;
		}
		return WB_Classification.OUTSIDE;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param L
	 * @return
	 */
	public static final boolean sameSideOfLine2D(final WB_Coord p, final WB_Coord q, final WB_Line L) {
		final WB_Point pL = L.getPoint(1.0);
		final double pside = Math.signum(WB_Predicates.orient2D(L.getOrigin(), pL, p));
		final double qside = Math.signum(WB_Predicates.orient2D(L.getOrigin(), pL, q));
		if (pside == 0 || qside == 0 || pside == qside) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param seg
	 * @param L
	 * @return
	 */
	public static final WB_Classification classifySegmentToLine2D(final WB_Segment seg, final WB_Line L) {
		final WB_Classification a = classifyPointToLine2D(seg.getOrigin(), L);
		final WB_Classification b = classifyPointToLine2D(seg.getEndpoint(), L);
		if (a == WB_Classification.ON) {
			if (b == WB_Classification.ON) {
				return WB_Classification.ON;
			} else if (b == WB_Classification.FRONT) {
				return WB_Classification.FRONT;
			} else {
				return WB_Classification.BACK;
			}
		}
		if (b == WB_Classification.ON) {
			if (a == WB_Classification.FRONT) {
				return WB_Classification.FRONT;
			} else {
				return WB_Classification.BACK;
			}
		}
		if (a == WB_Classification.FRONT && b == WB_Classification.BACK) {
			return WB_Classification.CROSSING;
		}
		if (a == WB_Classification.BACK && b == WB_Classification.FRONT) {
			return WB_Classification.CROSSING;
		}
		if (a == WB_Classification.FRONT) {
			return WB_Classification.FRONT;
		}
		return WB_Classification.BACK;
	}

	/**
	 *
	 *
	 * @param P
	 * @param L
	 * @return
	 */
	public static final WB_Classification classifyPolygonToLine2D(final WB_Polygon P, final WB_Line L) {
		int numFront = 0;
		int numBack = 0;
		for (int i = 0; i < P.getNumberOfPoints(); i++) {
			if (classifyPointToLine2D(P.getPoint(i), L) == WB_Classification.FRONT) {
				numFront++;
			} else if (classifyPointToLine2D(P.getPoint(i), L) == WB_Classification.BACK) {
				numBack++;
			}
			if (numFront > 0 && numBack > 0) {
				return WB_Classification.CROSSING;
			}
		}
		if (numFront > 0) {
			return WB_Classification.FRONT;
		}
		if (numBack > 0) {
			return WB_Classification.BACK;
		}
		return null;
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static final boolean contains2D(final WB_Coord p, final WB_Polygon poly) {
		return WB_Epsilon.isZeroSq(WB_Vector.getSqDistance2D(p, getClosestPoint2D(p, poly)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param tris
	 * @return
	 */
	public static final boolean contains2D(final WB_Coord p, final ArrayList<? extends WB_Triangle> tris) {
		return WB_Epsilon.isZeroSq(WB_Vector.getSqDistance2D(p, getClosestPoint2D(p, tris)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param tris
	 * @return
	 */
	public static final boolean contains2D(final WB_Coord p, final WB_Triangle tris) {
		return WB_Epsilon.isZeroSq(WB_Vector.getSqDistance2D(p, getClosestPoint2D(p, tris)));
	}

	/**
	 *
	 *
	 * @param u0
	 * @param u1
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static final double[] getIntervalIntersection2D(final double u0, final double u1, final double v0,
			final double v1) {
		double lu0, lu1;
		if (u1 < u0) {
			lu0 = u1;
			lu1 = u0;
		} else {
			lu0 = u0;
			lu1 = u1;
		}
		double lv0, lv1;
		if (v1 < v0) {
			lv0 = v1;
			lv1 = v0;
		} else {
			lv0 = v0;
			lv1 = v1;
		}
		if (lu1 < lv0 || lu0 > lv1) {
			return new double[] { 0 };
		}
		if (lu1 > lv0) {
			if (lu0 < lv1) {
				double w0, w1;
				if (lu0 < lv0) {
					w0 = lv0;
				} else {
					w0 = lu0;
				}
				if (lu1 > lv1) {
					w1 = lv1;
				} else {
					w1 = lu1;
				}
				return new double[] { 2, w0, w1 };
			} else {
				return new double[] { 1, lu0 };
			}
		} else {
			return new double[] { 1, lu1 };
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static final WB_Circle getBoundingCircle2D(final WB_Coord[] points) {
		WB_Point center = new WB_Point(points[0]);
		double radius = WB_Epsilon.EPSILON;
		double radius2 = radius * radius;
		double dist, dist2, alpha, ialpha2;
		for (int i = 0; i < 3; i++) {
			for (final WB_Coord point : points) {
				dist2 = WB_Vector.getSqDistance2D(point, center);
				if (dist2 > radius2) {
					dist = Math.sqrt(dist2);
					if (i < 2) {
						alpha = dist / radius;
						ialpha2 = 1.0 / (alpha * alpha);
						radius = 0.5 * (alpha + 1 / alpha) * radius;
						center = gf.createMidpoint(center.mulSelf(1.0 + ialpha2), WB_Point.mul(point, 1.0 - ialpha2));
					} else {
						radius = (radius + dist) * 0.5;
						center.mulAddMulSelf(radius / dist, (dist - radius) / dist, point);
					}
					radius2 = radius * radius;
				}
			}
		}
		return new WB_Circle(center, radius);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static final WB_Circle getBoundingCircle2D(final Collection<? extends WB_Coord> points) {
		WB_Point center = new WB_Point(points.iterator().next());
		double radius = WB_Epsilon.EPSILON;
		double radius2 = radius * radius;
		double dist, dist2, alpha, ialpha2;
		for (int i = 0; i < 3; i++) {
			for (final WB_Coord point : points) {
				dist2 = WB_Vector.getSqDistance2D(point, center);
				if (dist2 > radius2) {
					dist = Math.sqrt(dist2);
					if (i < 2) {
						alpha = dist / radius;
						ialpha2 = 1.0 / (alpha * alpha);
						radius = 0.5 * (alpha + 1 / alpha) * radius;
						center = gf.createMidpoint(center.mulSelf(1.0 + ialpha2), WB_Point.mul(point, 1.0 - ialpha2));
					} else {
						radius = (radius + dist) * 0.5;
						center.mulAddMulSelf(radius / dist, (dist - radius) / dist, point);
					}
					radius2 = radius * radius;
				}
			}
		}
		return new WB_Circle(center, radius);
	}

	/**
	 *
	 *
	 * @param C
	 * @param p
	 * @return
	 */
	public static final WB_Line getLineTangentToCircleAtPoint2D(final WB_Circle C, final WB_Coord p) {
		final WB_Vector v = new WB_Vector(C.getCenter(), p);
		return new WB_Line(p, new WB_Point(-v.yd(), v.xd()));
	}

	/**
	 *
	 *
	 * @param C
	 * @param p
	 * @return
	 */
	public static final ArrayList<WB_Line> getLinesTangentToCircleThroughPoint(final WB_Circle C, final WB_Coord p) {
		final ArrayList<WB_Line> result = new ArrayList<>(2);
		final double dcp = getDistance2D(C.getCenter(), p);
		if (WB_Epsilon.isZero(dcp - C.getRadius())) {
			final WB_Vector u = new WB_Vector(C.getCenter(), p);
			result.add(new WB_Line(p, new WB_Point(-u.yd(), u.xd())));
		} else if (dcp < C.getRadius()) {
			return result;
		} else {
			final WB_Vector u = new WB_Vector(C.getCenter(), p);
			final double ux2 = u.xd() * u.xd();
			final double ux4 = ux2 * ux2;
			final double uy2 = u.yd() * u.yd();
			final double r2 = C.getRadius() * C.getRadius();
			final double r4 = r2 * r2;
			final double num = r2 * uy2;
			final double denom = ux2 + uy2;
			final double rad = Math.sqrt(-r4 * ux2 + r2 * ux4 + r2 * ux2 * uy2);
			result.add(new WB_Line(p,
					new WB_Point(-(r2 * u.yd() + rad) / denom, (r2 - (num + u.yd() * rad) / denom) / u.xd())));
			result.add(new WB_Line(p,
					new WB_Point(-(r2 * u.yd() - rad) / denom, (r2 - (num - u.yd() * rad) / denom) / u.xd())));
		}
		return result;
	}

	/**
	 *
	 *
	 * @param C0
	 * @param C1
	 * @return
	 */
	public static final ArrayList<WB_Line> getLinesTangentTo2Circles(final WB_Circle C0, final WB_Circle C1) {
		final ArrayList<WB_Line> result = new ArrayList<>(4);
		final WB_Point w = WB_Point.sub(C1.getCenter(), C0.getCenter());
		final double wlensqr = w.getSqLength2D();
		final double rsum = C0.getRadius() + C1.getRadius();
		if (wlensqr <= rsum * rsum + WB_Epsilon.SQEPSILON) {
			return result;
		}
		final double rdiff = C1.getRadius() - C0.getRadius();
		if (!WB_Epsilon.isZero(rdiff)) {
			final double r0sqr = C0.getRadius() * C0.getRadius();
			final double r1sqr = C1.getRadius() * C1.getRadius();
			final double c0 = -r0sqr;
			final double c1 = 2 * r0sqr;
			final double c2 = C1.getRadius() * C1.getRadius() - r0sqr;
			final double invc2 = 1.0 / c2;
			final double discr = Math.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			double s, oms, a;
			s = -0.5 * (c1 + discr) * invc2;
			if (s >= 0.5) {
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r0sqr / (s * s)));
			} else {
				oms = 1.0 - s;
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r1sqr / (oms * oms)));
			}
			WB_Point[] dir = getDirectionsOfLinesTangentToCircle2D(w, a);
			WB_Point org = new WB_Point(C0.getCenter().xd() + s * w.xd(), C0.getCenter().yd() + s * w.yd());
			result.add(new WB_Line(org, dir[0]));
			result.add(new WB_Line(org, dir[1]));
			s = -0.5 * (c1 - discr) * invc2;
			if (s >= 0.5) {
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r0sqr / (s * s)));
			} else {
				oms = 1.0 - s;
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r1sqr / (oms * oms)));
			}
			dir = getDirectionsOfLinesTangentToCircle2D(w, a);
			org = new WB_Point(C0.getCenter().xd() + s * w.xd(), C0.getCenter().yd() + s * w.yd());
			result.add(new WB_Line(org, dir[0]));
			result.add(new WB_Line(org, dir[1]));
		} else {
			final WB_Point mid = WB_Point.add(C0.getCenter(), C1.getCenter()).mulSelf(0.5);
			final double a = Math.sqrt(WB_Math.fastAbs(wlensqr - 4 * C0.getRadius() * C0.getRadius()));
			final WB_Point[] dir = getDirectionsOfLinesTangentToCircle2D(w, a);
			result.add(new WB_Line(mid, dir[0]));
			result.add(new WB_Line(mid, dir[1]));
			final double invwlen = 1.0 / Math.sqrt(wlensqr);
			w.mulSelf(invwlen);
			result.add(new WB_Line(new WB_Point(mid.xd() + C0.getRadius() * w.yd(), mid.yd() - C0.getRadius() * w.xd()),
					w));
			result.add(new WB_Line(new WB_Point(mid.xd() - C0.getRadius() * w.yd(), mid.yd() + C0.getRadius() * w.xd()),
					w));
		}
		return result;
	}

	/**
	 *
	 *
	 * @param w
	 * @param a
	 * @return
	 */
	private static final WB_Point[] getDirectionsOfLinesTangentToCircle2D(final WB_Coord w, final double a) {
		final WB_Point[] dir = new WB_Point[2];
		final double asqr = a * a;
		final double wxsqr = w.xd() * w.xd();
		final double wysqr = w.yd() * w.yd();
		final double c2 = wxsqr + wysqr;
		final double invc2 = 1.0 / c2;
		double c0, c1, discr, invwx;
		final double invwy;
		if (WB_Math.fastAbs(w.xd()) >= WB_Math.fastAbs(w.yd())) {
			c0 = asqr - wxsqr;
			c1 = -2 * a * w.yd();
			discr = Math.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			invwx = 1.0 / w.xd();
			final double dir0y = -0.5 * (c1 + discr) * invc2;
			dir[0] = new WB_Point((a - w.yd() * dir0y) * invwx, dir0y);
			final double dir1y = -0.5 * (c1 - discr) * invc2;
			dir[1] = new WB_Point((a - w.yd() * dir1y) * invwx, dir1y);
		} else {
			c0 = asqr - wysqr;
			c1 = -2 * a * w.xd();
			discr = Math.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			invwy = 1.0 / w.yd();
			final double dir0x = -0.5 * (c1 + discr) * invc2;
			dir[0] = new WB_Point(dir0x, (a - w.xd() * dir0x) * invwy);
			final double dir1x = -0.5 * (c1 - discr) * invc2;
			dir[1] = new WB_Point(dir1x, (a - w.xd() * dir1x) * invwy);
		}
		return dir;
	}

	/**
	 *
	 *
	 * @param L
	 * @param p
	 * @return
	 */
	public static final WB_Line getPerpendicularLineThroughPoint2D(final WB_Line L, final WB_Coord p) {
		return new WB_Line(p, new WB_Point(-L.getDirection().yd(), L.getDirection().xd()));
	}

	/**
	 *
	 *
	 * @param L
	 * @param p
	 * @return
	 */
	public static final WB_Line getParallelLineThroughPoint2D(final WB_Line L, final WB_Coord p) {
		return new WB_Line(p, L.getDirection());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final WB_Line getBisector2D(final WB_Coord p, final WB_Coord q) {
		return new WB_Line(gf.createInterpolatedPoint(p, q, 0.5), new WB_Point(p.yd() - q.yd(), q.xd() - p.xd()));
	}

	/**
	 *
	 *
	 * @param L
	 * @param d
	 * @return
	 */
	public static final WB_Line[] getParallelLines2D(final WB_Line L, final double d) {
		final WB_Line[] result = new WB_Line[2];
		result[0] = new WB_Line(new WB_Point(L.getOrigin().xd() - d * L.getDirection().yd(),
				L.getOrigin().yd() + d * L.getDirection().xd()), L.getDirection());
		result[1] = new WB_Line(new WB_Point(L.getOrigin().xd() + d * L.getDirection().yd(),
				L.getOrigin().yd() - d * L.getDirection().xd()), L.getDirection());
		return result;
	}

	/**
	 *
	 *
	 * @param L
	 * @param C
	 * @return
	 */
	public static final WB_Line[] getPerpendicularLinesTangentToCircle2D(final WB_Line L, final WB_Circle C) {
		final WB_Line[] result = new WB_Line[2];
		result[0] = new WB_Line(
				new WB_Point(C.getCenter().xd() + C.getRadius() * L.getDirection().xd(),
						C.getCenter().yd() + C.getRadius() * L.getDirection().yd()),
				new WB_Point(-L.getDirection().yd(), L.getDirection().xd()));
		result[1] = new WB_Line(
				new WB_Point(C.getCenter().xd() - C.getRadius() * L.getDirection().xd(),
						C.getCenter().yd() - C.getRadius() * L.getDirection().yd()),
				new WB_Point(-L.getDirection().yd(), L.getDirection().xd()));
		return result;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static final WB_Sphere getBoundingSphere(final Collection<? extends WB_Coord> points) {
		WB_Point center = new WB_Point(points.iterator().next());
		double radius = WB_Epsilon.EPSILON;
		double radius2 = radius * radius;
		double dist, dist2, alpha, ialpha2;
		for (int i = 0; i < 3; i++) {
			for (final WB_Coord point : points) {
				dist2 = WB_Vector.getSqDistance3D(point, center);
				if (dist2 > radius2) {
					dist = Math.sqrt(dist2);
					if (i < 2) {
						alpha = dist / radius;
						ialpha2 = 1.0 / (alpha * alpha);
						radius = 0.5 * (alpha + 1 / alpha) * radius;
						center = gf.createMidpoint(center.mulSelf(1.0 + ialpha2), WB_Point.mul(point, 1.0 - ialpha2));
					} else {
						radius = (radius + dist) * 0.5;
						center.mulAddMulSelf(radius / dist, (dist - radius) / dist, point);
					}
					radius2 = radius * radius;
				}
			}
		}
		return new WB_Sphere(center, radius);
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static final double getArea2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		return WB_Math.fastAbs(getSignedArea2D(p1, p2, p3));
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public static final double getSimpleArea2D(final WB_Polygon poly) {
		final int n = poly.getNumberOfShellPoints();
		WB_Coord p, q;
		double area = 0;
		for (int i = 0; i < n; i++) {
			p = poly.getPoint(i);
			q = poly.getPoint((i + 1) % n);
			area += p.xd() * q.yd() - p.yd() * q.xd();
		}
		return 0.5 * area;
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param A
	 * @param B
	 * @return
	 */
	public static final boolean sameSide2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord A, final WB_Coord B) {
		final double pside = Math.signum(WB_Predicates.orient2D(A, B, p1));
		final double qside = Math.signum(WB_Predicates.orient2D(A, B, p2));
		if (pside == 0 || qside == 0 || pside == qside) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param p
	 * @param A
	 * @param B
	 * @param C
	 * @return
	 */
	public static final boolean pointInTriangle2D(final WB_Coord p, final WB_Coord A, final WB_Coord B,
			final WB_Coord C) {
		if (WB_Epsilon.isZeroSq(getSqDistanceToLine2D(A, B, C))) {
			return false;
		}
		if (sameSide2D(p, A, B, C) && sameSide2D(p, B, A, C) && sameSide2D(p, C, A, B)) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static final boolean pointInTriangle2D(final WB_Coord p, final WB_Triangle T) {
		return pointInTriangle2D(p, T.p1, T.p2, T.p3);
	}

	/**
	 *
	 *
	 * @param p
	 * @param A
	 * @param B
	 * @param C
	 * @return
	 */
	public static final boolean pointInTriangleBary2D(final WB_Coord p, final WB_Coord A, final WB_Coord B,
			final WB_Coord C) {
		if (p == A) {
			return false;
		}
		if (p == B) {
			return false;
		}
		if (p == C) {
			return false;
		}
		if (WB_Epsilon.isZeroSq(getSqDistanceToLine2D(A, B, C))) {
			return false;
		}
		// Compute vectors
		final WB_Point v0 = new WB_Point(C).subSelf(A);
		final WB_Point v1 = new WB_Point(B).subSelf(A);
		final WB_Point v2 = new WB_Point(p).subSelf(A);
		// Compute dot products
		final double dot00 = v0.dot2D(v0);
		final double dot01 = v0.dot2D(v1);
		final double dot02 = v0.dot2D(v2);
		final double dot11 = v1.dot2D(v1);
		final double dot12 = v1.dot2D(v2);
		// Compute barycentric coordinates
		final double invDenom = 1.0 / (dot00 * dot11 - dot01 * dot01);
		final double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		final double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
		// Check if point is in triangle
		return u > WB_Epsilon.EPSILON && v > WB_Epsilon.EPSILON && u + v < 1 - WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static final boolean pointInTriangleBary2D(final WB_Coord p, final WB_Triangle T) {
		return pointInTriangleBary2D(p, T.p1, T.p2, T.p3);
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static final double twiceSignedTriArea2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		return (p1.xd() - p3.xd()) * (p2.yd() - p3.yd()) - (p1.yd() - p3.yd()) * (p2.xd() - p3.xd());
	}

	/**
	 *
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	public static final double twiceSignedTriArea2D(final double x1, final double y1, final double x2, final double y2,
			final double x3, final double y3) {
		return (x1 - x2) * (y2 - y3) - (x2 - x3) * (y1 - y2);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param start
	 * @param end
	 * @return
	 */
	public static final double getSignedArea2D(final List<? extends WB_Coord> coords, final int start, final int end) {
		double sum = 0;
		for (int i = start, j = end - 1; i < end; j = i, i++) {
			sum += (coords.get(j).xd() - coords.get(i).xd()) * (coords.get(i).yd() + coords.get(j).yd());
		}
		return sum;
	}

	/**
	 *
	 *
	 * @param coords
	 * @param start
	 * @param end
	 * @return
	 */
	public static final double getSignedArea2D(final WB_Coord[] coords, final int start, final int end) {
		double sum = 0;
		for (int i = start, j = end - 1; i < end; j = i, i++) {
			sum += (coords[j].xd() - coords[i].xd()) * (coords[i].yd() + coords[j].yd());
		}
		return sum;
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static final double getSignedArea2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		double sum = (p3.xd() - p1.xd()) * (p1.yd() + p3.yd());
		sum += (p1.xd() - p2.xd()) * (p2.yd() + p1.yd());
		sum += (p2.xd() - p3.xd()) * (p3.yd() + p2.yd());
		return sum;
	}

	/**
	 *
	 *
	 * @param C
	 * @param v
	 * @return
	 */
	public static final WB_Coord projectToCircle2D(final WB_Circle C, final WB_Coord v) {
		final WB_Point vc = new WB_Point(v).sub(C.getCenter());
		final double er = vc.normalizeSelf();
		if (WB_Epsilon.isZero(er)) {
			return null;
		}
		return WB_Point.addMul(C.getCenter(), C.getRadius(), vc);
	}

	/**
	 *
	 *
	 * @param C
	 * @param p
	 * @return
	 */
	public static final WB_Circle growCircleByPoint2D(final WB_Circle C, final WB_Coord p) {
		final WB_Vector d = WB_Point.subToVector2D(p, C.getCenter());
		final double dist2 = d.getSqLength2D();
		double radius = C.getRadius();
		final WB_Coord center = C.getCenter();
		if (dist2 > radius * radius) {
			final double dist = Math.sqrt(dist2);
			final double newRadius = (radius + dist) * 0.5;
			final double k = (newRadius - radius) / dist;
			radius = newRadius;
			return new WB_Circle(center.xd() + k * d.xd(), center.yd() + k * d.yd(), newRadius);
		}
		return new WB_Circle(center, radius);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public static final WB_Polygon trimConvexPolygon2D(WB_Polygon poly, final double d) {
		final WB_Polygon cpoly = new WB_Polygon(poly.points);
		final int n = cpoly.numberOfShellPoints; // get number of vertices
		WB_Coord p1, p2;
		WB_Point origin;
		WB_Vector v, normal;
		for (int i = 0, j = n - 1; i < n; j = i, i++) {
			p1 = cpoly.getPoint(i);// startpoint of edge
			p2 = cpoly.getPoint(j);// endpoint of edge
			// vector along edge
			v = gf.createNormalizedVectorFromTo(p1, p2);
			// edge normal is perpendicular to edge and plane normal
			normal = v.cross(WB_Vector.Z());
			// center of edge
			origin = new WB_Point(p1).addSelf(p2).mulSelf(0.5);
			// offset cutting plane origin by the desired distance d
			origin.addMulSelf(d, normal);
			final WB_Polygon[] split = splitPolygon2D(poly, new WB_Line(origin, v));
			poly = split[0];
		}
		return poly;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public static final WB_Polygon trimConvexPolygon2D(WB_Polygon poly, final double[] d) {
		final WB_Polygon cpoly = new WB_Polygon(poly.points);
		final int n = cpoly.numberOfShellPoints; // get number of vertices
		WB_Coord p1, p2;
		WB_Point origin;
		WB_Vector v, normal;
		for (int i = 0, j = n - 1; i < n; j = i, i++) {
			p1 = cpoly.getPoint(i);// startpoint of edge
			p2 = cpoly.getPoint(j);// endpoint of edge
			// vector along edge
			v = gf.createNormalizedVectorFromTo(p1, p2);
			// edge normal is perpendicular to edge and plane normal
			normal = v.cross(WB_Vector.Z());
			// center of edge
			origin = new WB_Point(p1).addSelf(p2).mulSelf(0.5);
			// offset cutting plane origin by the desired distance d
			origin.addMulSelf(d[j], normal);
			final WB_Polygon[] split = splitPolygon2D(poly, new WB_Line(origin, v));
			poly = split[0];
		}
		return poly;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static final boolean isLeftStrict2D(final WB_Coord a, final WB_Coord b, final WB_Coord p) {
		return WB_Predicates.orient2D(p, a, b) > 0;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static final boolean isLeft2D(final WB_Coord a, final WB_Coord b, final WB_Coord p) {
		return WB_Predicates.orient2D(p, a, b) >= 0;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static final boolean isRightStrict2D(final WB_Coord a, final WB_Coord b, final WB_Coord p) {
		return WB_Predicates.orient2D(p, a, b) < 0;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param p
	 * @return
	 */
	public static final boolean isRight2D(final WB_Coord a, final WB_Coord b, final WB_Coord p) {
		return WB_Predicates.orient2D(p, a, b) <= 0;
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p
	 * @param p1
	 * @return
	 */
	public static final boolean isReflex2D(final WB_Coord p0, final WB_Coord p, final WB_Coord p1) {
		return isRightStrict2D(p1, p0, p);
	}

	/**
	 *
	 *
	 * @param ap1
	 * @param ap2
	 * @param bp1
	 * @param bp2
	 * @return
	 */
	public static final WB_Coord getSegmentIntersection2D(final WB_Coord ap1, final WB_Coord ap2, final WB_Coord bp1,
			final WB_Coord bp2) {
		final WB_Coord A = WB_Point.sub(ap2, ap1);
		final WB_Coord B = WB_Point.sub(bp2, bp1);
		final double BxA = cross2D(B, A);
		if (Math.abs(BxA) <= WB_Epsilon.EPSILON) {
			return null;
		}
		final double ambxA = cross2D(WB_Point.sub(ap1, bp1), A);
		if (Math.abs(ambxA) <= WB_Epsilon.EPSILON) {
			return null;
		}
		final double tb = ambxA / BxA;
		if (tb < 0.0 || tb > 1.0) {
			return null;
		}
		final WB_Point ip = WB_Point.mul(B, tb).addSelf(bp1);
		final double ta = WB_Point.sub(ip, ap1).dot(A) / WB_Vector.dot(A, A);
		if (ta < 0.0 || ta > 1.0) {
			return null;
		}
		return ip;
	}

	/**
	 *
	 *
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @param p
	 * @return
	 */
	public static final boolean getLineIntersectionInto2D(final WB_Coord a1, final WB_Coord a2, final WB_Coord b1,
			final WB_Coord b2, final WB_MutableCoord p) {
		final WB_Vector s1 = gf.createVectorFromTo2D(a2, a1);
		final WB_Vector s2 = gf.createVectorFromTo2D(b2, b1);
		double det = cross2D(s1, s2);
		if (Math.abs(det) <= WB_Epsilon.EPSILON) {
			return false;
		} else {
			det = 1.0 / det;
			final double t2 = det * (cross2D(a1, s1) - cross2D(b1, s1));
			p.set(b1.xd() * (1.0 - t2) + b2.xd() * t2, b1.yd() * (1.0 - t2) + b2.yd() * t2);
			return true;
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static final WB_Circle getBoundingCircleInCenter2D(final Collection<? extends WB_Coord> points) {
		double r = 0;
		final WB_Point center = new WB_Point();
		for (final WB_Coord p : points) {
			center.addSelf(p);
		}
		center.divSelf(points.size());
		for (final WB_Coord p : points) {
			final WB_Vector diff = WB_Vector.sub(p, center);
			final double radiusSqr = diff.dot(diff);
			if (radiusSqr > r) {
				r = radiusSqr;
			}
		}
		return new WB_Circle(center, Math.sqrt(r));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static final WB_Circle getBoundingCircleInCenter2D(final WB_Coord[] points) {
		double r = 0;
		final WB_Point center = new WB_Point();
		for (final WB_Coord p : points) {
			center.addSelf(p);
		}
		center.divSelf(points.length);
		for (final WB_Coord p : points) {
			final WB_Vector diff = WB_Vector.sub(p, center);
			final double radiusSqr = diff.dot(diff);
			if (radiusSqr > r) {
				r = radiusSqr;
			}
		}
		return new WB_Circle(center, Math.sqrt(r));
	}

	/**
	 *
	 *
	 * @param C1
	 * @param C2
	 * @return
	 */
	public static final WB_Circle mergeCircles2D(final WB_Circle C1, final WB_Circle C2) {
		final WB_Vector cenDiff = WB_Vector.sub(C2.getCenter(), C1.getCenter());
		final double lenSqr = cenDiff.dot(cenDiff);
		final double rDiff = C2.getRadius() - C1.getRadius();
		final double rDiffSqr = rDiff * rDiff;
		if (rDiffSqr >= lenSqr) {
			return rDiff >= 0.0 ? C2 : C1;
		} else {
			final double length = Math.sqrt(lenSqr);
			WB_Point center;
			if (length > 0) {
				final double coeff = (length + rDiff) / (2.0 * length);
				center = WB_Point.addMul(C1.getCenter(), coeff, cenDiff);
			} else {
				center = new WB_Point(C1.getCenter());
			}
			final double radius = 0.5 * (length + C1.getRadius() + C2.getRadius());
			return new WB_Circle(center, radius);
		}
	}

	/**
	 *
	 *
	 * @param C1
	 * @param C2
	 * @return
	 */
	public static final boolean isTangent2D(final WB_Circle C1, final WB_Circle C2) {
		final double d = WB_Vector.getDistance2D(C1.getCenter(), C2.getCenter());
		return WB_Epsilon.isZero(d - WB_Math.fastAbs(C2.getRadius() - C1.getRadius()))
				|| WB_Epsilon.isZero(d - WB_Math.fastAbs(C2.getRadius() + C1.getRadius()));
	}

	/**
	 *
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static final double cross2D(final WB_Coord v1, final WB_Coord v2) {
		return v1.xd() * v2.yd() - v1.yd() * v2.xd();
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @return
	 */
	public static final double dot2D(final double ux, final double uy, final double vx, final double vy) {
		return ux * vx + uy * vy;
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static final double dot2D(final WB_Coord u, final WB_Coord v) {
		return u.xd() * v.xd() + u.yd() * v.yd();
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @return
	 */
	public static final double getAngleBetween2D(final double ux, final double uy, final double vx, final double vy) {
		final WB_Vector v0 = new WB_Vector(ux, uy);
		final WB_Vector v1 = new WB_Vector(vx, vy);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @return
	 */
	public static final double getAngleBetween2D(final double cx, final double cy, final double px, final double py,
			final double qx, final double qy) {
		final WB_Vector v0 = new WB_Vector(px - cx, py - cy);
		final WB_Vector v1 = new WB_Vector(qx - cx, qy - cy);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static final double getAngleBetween2D(final WB_Coord u, final WB_Coord v) {
		final WB_Vector v0 = new WB_Vector(u);
		final WB_Vector v1 = new WB_Vector(v);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param c
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getAngleBetween2D(final WB_Coord c, final WB_Coord p, final WB_Coord q) {
		final WB_Vector v0 = WB_Vector.subToVector2D(p, c);
		final WB_Vector v1 = WB_Vector.subToVector2D(q, c);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @return
	 */
	public static final double getAngleBetweenNorm2D(final double ux, final double uy, final double vx,
			final double vy) {
		final WB_Vector v0 = new WB_Vector(ux, uy);
		final WB_Vector v1 = new WB_Vector(vx, vy);
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static final double getAngleBetweenNorm2D(final WB_Coord u, final WB_Coord v) {
		double d = WB_Vector.dot2D(u, v);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return Math.acos(d);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @return
	 */
	public static final double getCosAngleBetween2D(final double ux, final double uy, final double vx,
			final double vy) {
		final WB_Vector v0 = new WB_Vector(ux, uy);
		final WB_Vector v1 = new WB_Vector(vx, vy);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return d;
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @return
	 */
	public static final double getCosAngleBetween2D(final double cx, final double cy, final double px, final double py,
			final double qx, final double qy) {
		final WB_Vector v0 = new WB_Vector(px - cx, py - cy);
		final WB_Vector v1 = new WB_Vector(qx - cx, qy - cy);
		v0.normalizeSelf();
		v1.normalizeSelf();
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return d;
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @return
	 */
	public static final double getCosAngleBetweenNorm2D(final double ux, final double uy, final double vx,
			final double vy) {
		final WB_Vector v0 = new WB_Vector(ux, uy);
		final WB_Vector v1 = new WB_Vector(vx, vy);
		double d = v0.dot2D(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		return d;
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @return
	 */
	public static final double getDistance2D(final double px, final double py, final double qx, final double qy) {
		return Math.sqrt((qx - px) * (qx - px) + (qy - py) * (qy - py));
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @return
	 */
	public static final double getLength2D(final double ux, final double uy) {
		return Math.sqrt(ux * ux + uy * uy);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static final double getLength2D(final WB_Coord p) {
		return Math.sqrt(p.xd() * p.xd() + p.yd() * p.yd());
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @param nx
	 * @param ny
	 * @return
	 */
	public static final double getSignedAngleBetween2D(final double ux, final double uy, final double vx,
			final double vy, final double nx, final double ny) {
		final WB_Vector v0 = new WB_Vector(ux, uy);
		final WB_Vector v1 = new WB_Vector(vx, vy);
		final WB_Vector vn = new WB_Vector(nx, ny);
		return Math.atan2(v1.cross(v0).dot(vn), v0.dot(v1));
	}

	/**
	 *
	 *
	 * @param cx
	 * @param cy
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @param nx
	 * @param ny
	 * @return
	 */
	public static final double getSignedAngleBetween2D(final double cx, final double cy, final double px,
			final double py, final double qx, final double qy, final double nx, final double ny) {
		final WB_Vector v0 = new WB_Vector(px - cx, py - cy);
		final WB_Vector v1 = new WB_Vector(qx - cx, qy - cy);
		final WB_Vector vn = new WB_Vector(nx, ny);
		return Math.atan2(v1.cross(v0).dot(vn), v0.dot(v1));
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param vx
	 * @param vy
	 * @param nx
	 * @param ny
	 * @return
	 */
	public static final double getSignedAngleBetweenNorm2D(final double ux, final double uy, final double vx,
			final double vy, final double nx, final double ny) {
		final WB_Vector v0 = new WB_Vector(ux, uy);
		final WB_Vector v1 = new WB_Vector(vx, vy);
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		final WB_Vector cross = v0.cross(v1);
		final double sign = cross.dot(new WB_Vector(nx, ny));
		return sign < 0 ? -Math.acos(d) : Math.acos(d);
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @return
	 */
	public static final double getSqDistance2D(final double px, final double py, final double qx, final double qy) {
		return (qx - px) * (qx - px) + (qy - py) * (qy - py);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @return
	 */
	public static final double getSqLength2D(final double ux, final double uy) {
		return ux * ux + uy * uy;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static final double getSqLength2D(final WB_Coord p) {
		return p.xd() * p.xd() + p.yd() * p.yd();
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @param t
	 * @return
	 */
	public static final double[] interpolate2D(final double px, final double py, final double qx, final double qy,
			final double t) {
		return new double[] { px + t * (qx - px), py + t * (qy - py) };
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static final double[] interpolate2D(final WB_Coord p, final WB_Coord q, final double t) {
		return new double[] { p.xd() + t * (q.xd() - p.xd()), p.yd() + t * (q.yd() - p.yd()) };
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param qx
	 * @param qy
	 * @param t
	 * @param ease
	 * @param type
	 * @return
	 */
	public static final double[] interpolateEase2D(final double px, final double py, final double qx, final double qy,
			final double t, final WB_Ease ease, final WB_Ease.EaseType type) {
		double et;
		switch (type) {
		case IN:
			et = ease.easeIn(t);
			break;
		case INOUT:
			et = ease.easeInOut(t);
			break;
		case OUT:
			et = ease.easeOut(t);
			break;
		default:
			et = ease.easeIn(t);
			break;
		}
		return new double[] { px + et * (qx - px), py + et * (qy - py) };
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @param ease
	 * @param type
	 * @return
	 */
	public static final double[] interpolateEase2D(final WB_Coord p, final WB_Coord q, final double t,
			final WB_Ease ease, final WB_Ease.EaseType type) {
		double et;
		switch (type) {
		case IN:
			et = ease.easeIn(t);
			break;
		case INOUT:
			et = ease.easeInOut(t);
			break;
		case OUT:
			et = ease.easeOut(t);
			break;
		default:
			et = ease.easeIn(t);
			break;
		}
		return new double[] { p.xd() + et * (q.xd() - p.xd()), p.yd() + et * (q.yd() - p.yd()) };
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static final boolean isCoincident2D(final WB_Coord a, final WB_Coord b) {
		if (getSqDistance2D(a, b) < WB_Epsilon.SQEPSILON) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @param q
	 * @return
	 */
	public static final boolean isCollinear2D(final WB_Coord o, final WB_Coord p, final WB_Coord q) {
		if (WB_Epsilon.isZeroSq(getSqDistanceToPoint2D(p, q))) {
			return true;
		}
		if (WB_Epsilon.isZeroSq(getSqDistanceToPoint2D(o, q))) {
			return true;
		}
		if (WB_Epsilon.isZeroSq(getSqDistanceToPoint2D(o, p))) {
			return true;
		}
		return WB_Epsilon.isZeroSq(getSqDistanceToLine2D(o, p, q));
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static final boolean isOrthogonal2D(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1)));
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static final boolean isOrthogonal2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1))) < epsilon;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static final boolean isOrthogonalNorm2D(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()));
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static final boolean isOrthogonalNorm2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd())) < epsilon;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static final boolean isParallel2D(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0
				- dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1))) < WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static final boolean isParallel2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math
				.abs(1.0 - dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1))) < epsilon;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static final boolean isParallelNorm2D(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0 - dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd())) < WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param epsilon
	 * @return
	 */
	public static final boolean isParallelNorm2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(1.0 - dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd())) < epsilon;
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @return
	 */
	public static final boolean isZero2D(final double ux, final double uy) {
		return getSqLength2D(ux, uy) < WB_Epsilon.SQEPSILON;
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static double getDistance2D(final WB_Coord p, final WB_AABB2D AABB) {
		return Math.sqrt(getSqDistance2D(p, AABB));
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static double getSqDistance2D(final WB_Coord p, final WB_AABB2D AABB) {
		double sqDist = 0;
		double v = p.xd();
		if (v < AABB.getMinX()) {
			sqDist += (AABB.getMinX() - v) * (AABB.getMinX() - v);
		}
		if (v > AABB.getMaxX()) {
			sqDist += (v - AABB.getMaxX()) * (v - AABB.getMaxX());
		}
		v = p.yd();
		if (v < AABB.getMinY()) {
			sqDist += (AABB.getMinY() - v) * (AABB.getMinY() - v);
		}
		if (v > AABB.getMaxY()) {
			sqDist += (v - AABB.getMaxY()) * (v - AABB.getMaxY());
		}
		return sqDist;
	}
}
