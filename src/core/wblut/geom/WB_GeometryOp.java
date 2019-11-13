/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_AABBTree3D.WB_AABBNode3D;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_GeometryOp extends WB_GeometryOp2D {
	private static final WB_GeometryFactory gf = new WB_GeometryFactory();

	/**
	 *
	 *
	 * @param S
	 * @param P
	 * @return
	 */
	public static WB_IntersectionResult getIntersection3D(final WB_Segment S,
			final WB_Plane P) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(),
				S.getOrigin());
		final double denom = P.getNormal().dot(ab);
		if (!WB_Epsilon.isZero(denom)) {
			double t = (-P.d() - P.getNormal().dot(S.getOrigin()))
					/ P.getNormal().dot(ab);
			if (t >= -WB_Epsilon.EPSILON && t <= 1.0 + WB_Epsilon.EPSILON) {
				t = WB_Epsilon.clampEpsilon(t, 0, 1);
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
	public static WB_IntersectionResult getIntersection3D(final WB_Coord a,
			final WB_Coord b, final WB_Plane P) {
		final WB_Vector ab = new WB_Vector(a, b);
		double t = (-P.d() - P.getNormal().dot(a)) / P.getNormal().dot(ab);
		if (t >= -WB_Epsilon.EPSILON && t <= 1.0 + WB_Epsilon.EPSILON) {
			t = WB_Epsilon.clampEpsilon(t, 0, 1);
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = t;
			i.t2 = t;
			i.object = new WB_Point(a.xd() + t * (b.xd() - a.xd()),
					a.yd() + t * (b.yd() - a.yd()),
					a.zd() + t * (b.zd() - a.zd()));
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		}
		return NOINTERSECTION(t, t);
	}

	// RAY-PLANE
	/**
	 *
	 *
	 * @param R
	 * @param P
	 * @return
	 */
	public static WB_IntersectionResult getIntersection3D(final WB_Ray R,
			final WB_Plane P) {
		final WB_Coord ab = R.getDirection();
		final double denom = P.getNormal().dot(ab);
		if (!WB_Epsilon.isZero(denom)) {
			double t = (-P.d() - P.getNormal().dot(R.getOrigin())) / denom;
			if (t >= -WB_Epsilon.EPSILON) {
				t = WB_Epsilon.clampEpsilon(t, 0, Double.POSITIVE_INFINITY);
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
	public static WB_IntersectionResult getIntersection3D(final WB_Ray R,
			final WB_AABB aabb) {
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
		final WB_IntersectionResult i = new WB_IntersectionResult();
		i.intersection = true;
		i.t1 = tmin;
		i.t2 = 0;
		i.object = R.getPoint(tmin);
		i.dimension = 0;
		i.sqDist = WB_CoordOp.getSqDistance3D(p, (WB_Point) i.object);
		return i;
	}

	// LINE-PLANE
	/**
	 *
	 *
	 * @param L
	 * @param P
	 * @return
	 */
	public static WB_IntersectionResult getIntersection3D(final WB_Line L,
			final WB_Plane P) {
		final WB_Coord ab = L.getDirection();
		final double denom = P.getNormal().dot(ab);
		if (!WB_Epsilon.isZero(denom)) {
			final double t = (-P.d() - P.getNormal().dot(L.getOrigin()))
					/ denom;
			final WB_IntersectionResult i = new WB_IntersectionResult();
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

	// PLANE-PLANE
	/**
	 *
	 *
	 * @param P1
	 * @param P2
	 * @return
	 */
	public static WB_IntersectionResult getIntersection3D(final WB_Plane P1,
			final WB_Plane P2) {
		final WB_Vector N1 = P1.getNormal();
		final WB_Vector N2 = P2.getNormal();
		final WB_Vector N1xN2 = new WB_Vector(N1.cross(N2));
		if (WB_Epsilon.isZeroSq(N1xN2.getSqLength())) {
			return NOINTERSECTION();
		} else {
			final double d1 = P1.d();
			final double d2 = P2.d();
			final double N1N2 = N1.dot(N2);
			final double det = 1 - N1N2 * N1N2;
			final double c1 = (d1 - d2 * N1N2) / det;
			final double c2 = (d2 - d1 * N1N2) / det;
			final WB_Point O = new WB_Point(N1.mul(c1).addSelf(N2.mul(c2)));
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = 0;
			i.t2 = 0;
			i.object = new WB_Line(O, N1xN2);
			i.dimension = 1;
			i.sqDist = 0;
			return i;
		}
	}

	// PLANE-PLANE-PLANE
	/**
	 *
	 *
	 * @param P1
	 * @param P2
	 * @param P3
	 * @return
	 */
	public static WB_IntersectionResult getIntersection3D(final WB_Plane P1,
			final WB_Plane P2, final WB_Plane P3) {
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
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = 0;
			i.t2 = 0;
			i.object = p;
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		}
	}

	// AABB-AABB
	/**
	 *
	 *
	 * @param one
	 * @param other
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_AABB one,
			final WB_AABB other) {
		if (one.getMaxX() < other.getMinX()
				|| one.getMinX() > other.getMaxX()) {
			return false;
		}
		if (one.getMaxY() < other.getMinY()
				|| one.getMinY() > other.getMaxY()) {
			return false;
		}
		if (one.getMaxZ() < other.getMinZ()
				|| one.getMinZ() > other.getMaxZ()) {
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
	public static boolean checkIntersection3D(final WB_AABB AABB,
			final WB_Plane P) {
		final WB_Point c = AABB.getMax().add(AABB.getMin()).mulSelf(0.5);
		final WB_Point e = AABB.getMax().sub(c);
		final double r = e.xd() * WB_Math.fastAbs(P.getNormal().xd())
				+ e.yd() * WB_Math.fastAbs(P.getNormal().yd())
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
	public static boolean checkIntersection3D(final WB_AABB AABB,
			final WB_Sphere S) {
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
	public static boolean checkIntersection3D(final WB_Triangle T,
			final WB_Sphere S) {
		final WB_Point p = getClosestPoint3D(S.getCenter(), T);
		return p.subToVector3D(S.getCenter()).getSqLength() <= S.getRadius()
				* S.getRadius();
	}

	// TRIANGLE-AABB
	/**
	 *
	 *
	 * @param T
	 * @param AABB
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Triangle T,
			final WB_AABB AABB) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
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
			r = e0 * WB_Math.fastAbs(a.xd()) + e1 * WB_Math.fastAbs(a.yd())
					+ e2 * WB_Math.fastAbs(a.zd());
			if (WB_Math.max(WB_Math.min(p0, p1, p2),
					-WB_Math.max(p0, p1, p2)) > r) {
				return false;
			}
		}
		if (WB_Math.max(v0.xd(), v1.xd(), v2.xd()) < -e0
				|| WB_Math.max(v0.xd(), v1.xd(), v2.xd()) > e0) {
			return false;
		}
		if (WB_Math.max(v0.yd(), v1.yd(), v2.yd()) < -e1
				|| WB_Math.max(v0.yd(), v1.yd(), v2.yd()) > e1) {
			return false;
		}
		if (WB_Math.max(v0.zd(), v1.zd(), v2.zd()) < -e2
				|| WB_Math.max(v0.zd(), v1.zd(), v2.zd()) > e2) {
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
						return checkIntersection3D(
								new WB_Segment(T.p1(), T.p3()), AABB);
					} else {
						return checkIntersection3D(
								new WB_Segment(T.p1(), T.p2()), AABB);
					}
				} else if (a2 < WB_Math.min(a1, a3)) {
					if (a1 < a3) {
						return checkIntersection3D(
								new WB_Segment(T.p2(), T.p3()), AABB);
					} else {
						return checkIntersection3D(
								new WB_Segment(T.p2(), T.p1()), AABB);
					}
				} else {
					if (a1 < a2) {
						return checkIntersection3D(
								new WB_Segment(T.p3(), T.p2()), AABB);
					} else {
						return checkIntersection3D(
								new WB_Segment(T.p3(), T.p1()), AABB);
					}
				}
			}
		}
		return checkIntersection3D(AABB, P);
	}

	// SEGMENT-AABB
	/**
	 *
	 *
	 * @param S
	 * @param AABB
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Segment S,
			final WB_AABB AABB) {
		final WB_Vector e = AABB.getMax().subToVector3D(AABB.getMin());
		final WB_Vector d = WB_Vector.subToVector3D(S.getEndpoint(),
				S.getOrigin());
		final WB_Point m = new WB_Point(
				S.getEndpoint().xd() + S.getOrigin().xd() - AABB.getMinX()
						- AABB.getMaxX(),
				S.getEndpoint().yd() + S.getOrigin().yd() - AABB.getMinY()
						- AABB.getMaxY(),
				S.getEndpoint().zd() + S.getOrigin().zd() - AABB.getMinZ()
						- AABB.getMaxZ());
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
		if (WB_Math.fastAbs(m.yd() * d.zd() - m.zd() * d.yd()) > e.yd() * adz
				+ e.zd() * ady) {
			return false;
		}
		if (WB_Math.fastAbs(m.zd() * d.xd() - m.xd() * d.zd()) > e.xd() * adz
				+ e.zd() * adx) {
			return false;
		}
		if (WB_Math.fastAbs(m.xd() * d.yd() - m.yd() * d.xd()) > e.xd() * ady
				+ e.yd() * adx) {
			return false;
		}
		return true;
	}

	// SPHERE-SPHERE
	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Sphere S1,
			final WB_Sphere S2) {
		final WB_Vector d = WB_Vector.sub(S1.getCenter(), S2.getCenter());
		final double d2 = d.getSqLength();
		final double radiusSum = S1.getRadius() + S2.getRadius();
		return d2 <= radiusSum * radiusSum;
	}

	public static WB_IntersectionResult getIntersection3D(final WB_Sphere S1,
			final WB_Sphere S2) {
		final WB_Vector d = WB_Vector.sub(S2.getCenter(), S1.getCenter());
		final double dist = d.normalizeSelf();
		double R = S1.getRadius();
		double r = S2.getRadius();
		double disc = dist * dist - r * r + R * R;
		disc *= disc;
		disc = 4 * dist * dist * R * R - disc;
		if (disc < 0) {
			return NOINTERSECTION();
		}
		double x = (dist * dist - r * r + R * R) / (2.0 * dist);
		if (WB_Epsilon.isZero(disc)) {
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Point(S1.getCenter()).addMulSelf(x, d);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			double a = Math.sqrt(disc) / (2.0 * dist);
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Circle(
					new WB_Point(S1.getCenter()).addMulSelf(x, d), d, a);
			i.dimension = 2;
			i.sqDist = 0;
			return i;
		}
	}

	public static WB_IntersectionResult getIntersection3D(final WB_Sphere S,
			final WB_Plane P) {
		double d = WB_GeometryOp.getDistance3D(S.getCenter(), P);
		double radius = S.getRadius();
		if (d > radius) {
			return NOINTERSECTION();
		}
		if (WB_Epsilon.isZero(d - radius)) {
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = radius;
			i.t2 = 0;
			i.object = WB_GeometryOp.getClosestPoint3D(S.getCenter(), P);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			double a = Math.sqrt(radius * radius - d * d);
			final WB_IntersectionResult i = new WB_IntersectionResult();
			WB_Point p = WB_GeometryOp.getClosestPoint3D(S.getCenter(), P);
			i.intersection = true;
			i.t1 = d;
			i.t2 = 0;
			i.object = new WB_Circle(p, P.getNormal(), a);
			i.dimension = 2;
			i.sqDist = 0;
			return i;
		}
	}

	public static WB_IntersectionResult getIntersection3DPlanar(
			final WB_Circle C1, final WB_Circle C2) {
		final WB_Vector d = WB_Vector.sub(C2.getCenter(), C1.getCenter());
		final double dist = d.normalizeSelf();
		double R = C1.getRadius();
		double r = C2.getRadius();
		double disc = dist * dist - r * r + R * R;
		disc *= disc;
		disc = 4 * dist * dist * R * R - disc;
		if (disc < 0) {
			return NOINTERSECTION();
		}
		double x = (dist * dist - r * r + R * R) / (2.0 * dist);
		if (WB_Epsilon.isZero(disc)) {
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Point(C1.getCenter()).addMulSelf(x, d);
			i.dimension = 0;
			i.sqDist = 0;
			return i;
		} else {
			double a = Math.sqrt(disc) / (2.0 * dist);
			WB_Point p = new WB_Point(C1.getCenter()).addMulSelf(x, d);
			WB_Vector v = d.cross(C1.getNormal());
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = x;
			i.t2 = dist - x;
			i.object = new WB_Segment(p.addMul(a, v), p.addMul(-a, v));
			i.dimension = 1;
			i.sqDist = 0;
			return i;
		}
	}

	public static WB_IntersectionResult getIntersection3D(final WB_Sphere S,
			final WB_Circle C) {
		WB_Plane P = C.getPlane();
		WB_IntersectionResult is = getIntersection3D(S, P);
		if (!is.intersection) {
			return NOINTERSECTION();
		}
		if (is.dimension == 0) {
			WB_Point p = (WB_Point) is.object;
			if (WB_Epsilon
					.isZero(S.getRadius() - p.getDistance3D(S.getCenter()))) {
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
			WB_Circle C2 = (WB_Circle) is.object;
			return getIntersection3DPlanar(C, C2);
		}
	}

	public static WB_IntersectionResult getIntersection3D(final WB_Sphere S1,
			final WB_Sphere S2, final WB_Sphere S3) {
		WB_IntersectionResult is = getIntersection3D(S1, S2);
		if (!is.intersection) {
			return NOINTERSECTION();
		}
		if (is.dimension == 0) {
			WB_Point p = (WB_Point) is.object;
			if (WB_Epsilon
					.isZero(S3.getRadius() - p.getDistance3D(S3.getCenter()))) {
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
			WB_Circle C = (WB_Circle) is.object;
			return getIntersection3D(S3, C);
		}
	}

	// RAY-SPHERE
	/**
	 *
	 *
	 * @param R
	 * @param S
	 * @return
	 */
	public static boolean checkIntersection3D(final WB_Ray R,
			final WB_Sphere S) {
		final WB_Vector m = WB_Vector.subToVector3D(R.getOrigin(),
				S.getCenter());
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
	public static boolean checkIntersection3D(final WB_Ray R,
			final WB_AABB AABB) {
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
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Ray R,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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
	public static List<WB_AABBNode3D> getIntersection3D(final WB_AABB aabb,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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

	public static List<WB_AABBNode3D[]> getIntersection3D(final WB_AABBTree3D tree1,
			final WB_AABBTree3D tree2) {
		final List<WB_AABBNode3D[]> result = new ArrayList<WB_AABBNode3D[]>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
		queue.add(tree1.getRoot());
		WB_AABBNode3D current;
		while (!queue.isEmpty()) {
			current = queue.pop();
			if (checkIntersection3D(current.getAABB(), tree2)) {
				if (current.isLeaf()) {
					for (WB_AABBNode3D node : getIntersection3D(current.getAABB(),
							tree2)) {
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
	public static boolean checkIntersection3D(final WB_AABB aabb,
			final WB_AABBTree3D tree) {
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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

	public static List<WB_AABBNode3D> getIntersection3D(final WB_Coord p,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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
	public static boolean checkIntersection3D(final WB_Line L,
			final WB_AABB AABB) {
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
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Line L,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Segment S,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Plane P,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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
	public static List<WB_AABBNode3D> getIntersection3D(final WB_Triangle T,
			final WB_AABBTree3D tree) {
		final List<WB_AABBNode3D> result = new ArrayList<WB_AABBNode3D>();
		final LinkedList<WB_AABBNode3D> queue = new LinkedList<WB_AABBNode3D>();
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
	public static List<WB_Segment> getIntersection3D(final WB_Polygon poly,
			final WB_Plane P) {
		final List<WB_Segment> result = new ArrayList<WB_Segment>();
		final List<WB_Coord> splitVerts = new ArrayList<WB_Coord>();
		final int numVerts = poly.getNumberOfShellPoints();
		if (numVerts > 0) {
			WB_Coord a = poly.getPoint(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp.classifyPointToPlane3D(a,
					P);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				WB_IntersectionResult i;
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
				result.add(new WB_Segment(splitVerts.get(i),
						splitVerts.get(i + 1)));
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
	public static WB_IntersectionResult getIntersection3D(final WB_Segment S1,
			final WB_Segment S2) {
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
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.sqDist = r.getSqLength();
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
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.sqDist = r.getSqLength();
			i.intersection = WB_Epsilon.isZeroSq(i.sqDist);
			if (i.intersection) {
				i.dimension = 0;
				i.object = S1.getOrigin();
			} else {
				i.dimension = 1;
				i.object = new WB_Segment(S1.getOrigin(),
						getClosestPoint3D(S1.getOrigin(), S2));
			}
			return i;
		}
		if (WB_Epsilon.isZero(e)) {
			// Second segment is degenerate
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.sqDist = r.getSqLength();
			i.intersection = WB_Epsilon.isZeroSq(i.sqDist);
			if (i.intersection) {
				i.dimension = 0;
				i.object = S2.getOrigin();
			} else {
				i.dimension = 1;
				i.object = new WB_Segment(S2.getOrigin(),
						getClosestPoint3D(S2.getOrigin(), S1));
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
					final WB_IntersectionResult i = new WB_IntersectionResult();
					i.sqDist = WB_CoordOp.getSqDistance3D(start, end);
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
					final WB_IntersectionResult i = new WB_IntersectionResult();
					i.sqDist = WB_CoordOp.getSqDistance3D(start, end);
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
		final WB_IntersectionResult i = new WB_IntersectionResult();
		final WB_Point p1 = S1.getParametricPoint(t1);
		final WB_Point p2 = S2.getParametricPoint(t2);
		i.sqDist = WB_CoordOp.getSqDistance3D(p1, p2);
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
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Plane P) {
		final WB_Vector n = P.getNormal();
		final double t = n.dot(p) + P.d();
		return new WB_Point(p.xd() - t * n.xd(), p.yd() - t * n.yd(),
				p.zd() - t * n.zd());
	}

	/**
	 *
	 *
	 * @param P
	 * @param p
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Plane P,
			final WB_Coord p) {
		return getClosestPoint3D(P, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Segment S) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(),
				S.getOrigin());
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

	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_PolyLine PL) {
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
			d2 = WB_CoordOp.getSqDistance3D(p, test);
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
	public static WB_Point getClosestPoint3D(final WB_Segment S,
			final WB_Coord p) {
		return getClosestPoint3D(p, S);
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static double getClosestPointParametric3D(final WB_Coord p,
			final WB_Segment S) {
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(),
				S.getOrigin());
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
	public static double getClosestPointParametric3D(final WB_Segment S,
			final WB_Coord p) {
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
	public static WB_Point getClosestPointToSegment3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
				return new WB_Point(a.xd() + t * ab.xd(), a.yd() + t * ab.yd(),
						a.zd() + t * ab.zd());
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
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Line L) {
		final WB_Vector ca = new WB_Vector(p.xd() - L.getOrigin().yd(),
				p.yd() - L.getOrigin().xd(), p.zd() - L.getOrigin().zd());
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
	public static WB_Point getClosestPointToLine3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static WB_Point getClosestPointToRay3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
		return getClosestPoint3D(p, new WB_Ray(a, new WB_Vector(a, b)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_AABB AABB) {
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
	public static void getClosestPoint3D(final WB_Coord p, final WB_AABB AABB,
			final WB_MutableCoord result) {
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

	// POINT-TRIANGLE
	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Triangle T) {
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
			return new WB_Point(T.p2()).addSelf(
					new WB_Point(T.p3()).subToVector3D(T.p2()).mulSelf(w));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return new WB_Point(T.p1())
				.addSelf(ab.mulSelf(v).addSelf(ac.mulSelf(w)));
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
	public static WB_Point getClosestPointToTriangle3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b, final WB_Coord c) {
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
	public static WB_Point getClosestPointOnPeriphery3D(final WB_Coord p,
			final WB_Triangle T) {
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
			return new WB_Point(T.p2()).addSelf(
					new WB_Point(T.p3()).subToVector3D(T.p2()).mulSelf(w));
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
		final double dA2 = WB_CoordOp.getSqDistance3D(p, A);
		final WB_Point B = getClosestPointToSegment3D(p, T.p1(), T.p3());
		final double dB2 = WB_CoordOp.getSqDistance3D(p, B);
		final WB_Point C = getClosestPointToSegment3D(p, T.p1(), T.p2());
		final double dC2 = WB_CoordOp.getSqDistance3D(p, C);
		if (dA2 < dB2 && dA2 < dC2) {
			return A;
		} else if (dB2 < dA2 && dB2 < dC2) {
			return B;
		} else {
			return C;
		}
	}

	// POINT-POLYGON
	/**
	 *
	 *
	 * @param p
	 * @param tris
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final List<? extends WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint3D(p, T);
			final double d2 = WB_CoordOp.getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		return closest;
	}

	// LINE-LINE
	/**
	 *
	 *
	 * @param L1
	 * @param L2
	 * @return
	 */
	public static WB_IntersectionResult getClosestPoint3D(final WB_Line L1,
			final WB_Line L2) {
		final double a = WB_Vector.dot(L1.getDirection(), L1.getDirection());
		final double b = WB_Vector.dot(L1.getDirection(), L2.getDirection());
		final WB_Vector r = WB_Vector.subToVector3D(L1.getOrigin(),
				L2.getOrigin());
		final double c = WB_Vector.dot(L1.getDirection(), r);
		final double e = WB_Vector.dot(L2.getDirection(), L2.getDirection());
		final double f = WB_Vector.dot(L2.getDirection(), r);
		double denom = a * e - b * b;
		if (WB_Epsilon.isZero(denom)) {
			final double t2 = r.dot(L1.getDirection());
			final WB_Point p2 = new WB_Point(L2.getPoint(t2));
			final double d2 = WB_CoordOp.getSqDistance3D(L1.getOrigin(), p2);
			final WB_IntersectionResult i = new WB_IntersectionResult();
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
		final double d2 = WB_CoordOp.getSqDistance3D(p1, p2);
		if (WB_Epsilon.isZeroSq(d2)) {
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = t1;
			i.t2 = t2;
			i.dimension = 0;
			i.object = p1;
			i.sqDist = d2;
			return i;
		} else {
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = true;
			i.t1 = t1;
			i.t2 = t2;
			i.dimension = 1;
			i.object = new WB_Segment(p1, p2);
			i.sqDist = d2;
			return i;
		}
	}

	// POINT-TETRAHEDRON
	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Tetrahedron T) {
		WB_Point closestPt = new WB_Point(p);
		double bestSqDist = Double.POSITIVE_INFINITY;
		if (pointOtherSideOfPlane(p, T.p4, T.p1, T.p2, T.p3)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p1, T.p2, T.p3);
			final double sqDist = q.subToVector3D(p).getSqLength();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		if (pointOtherSideOfPlane(p, T.p2, T.p1, T.p3, T.p4)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p1, T.p3, T.p4);
			final double sqDist = q.subToVector3D(p).getSqLength();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		if (pointOtherSideOfPlane(p, T.p3, T.p1, T.p4, T.p2)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p1, T.p4, T.p2);
			final double sqDist = q.subToVector3D(p).getSqLength();
			if (sqDist < bestSqDist) {
				bestSqDist = sqDist;
				closestPt = q;
			}
		}
		if (pointOtherSideOfPlane(p, T.p1, T.p2, T.p4, T.p3)) {
			final WB_Point q = getClosestPointToTriangle3D(p, T.p2, T.p4, T.p3);
			final double sqDist = q.subToVector3D(p).getSqLength();
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
	public static boolean pointOtherSideOfPlane(final WB_Coord p,
			final WB_Coord q, final WB_Coord a, final WB_Coord b,
			final WB_Coord c) {
		final double signp = new WB_Vector(a, p)
				.dot(new WB_Vector(a, b).crossSelf(new WB_Vector(a, c)));
		final double signq = new WB_Vector(a, q)
				.dot(new WB_Vector(a, b).crossSelf(new WB_Vector(a, c)));
		return signp * signq <= 0;
	}

	protected static class TriangleIntersection {
		public WB_Point	p0;	// the first point of the line
		public WB_Point	p1;	// the second point of the line
		public double	s0;	// the distance along the line to the first
		// intersection with the triangle
		public double	s1;	// the distance along the line to the second
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
	public static WB_IntersectionResult getIntersection3D(final WB_Coord p1,
			final WB_Coord p2, final WB_Coord p3, final WB_Coord q1,
			final WB_Coord q2, final WB_Coord q3) {
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
		final TriangleIntersection isectA = compute_intervals_isectline(p1, p2,
				p3, vp0, vp1, vp2, dv0, dv1, dv2, dv0dv1, dv0dv2);
		if (isectA == null) {
			if (coplanarTriangles(n1, p1, p2, p3, q1, q2, q3)) {
				return NOINTERSECTION();
			} else {
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
		final TriangleIntersection isectB = compute_intervals_isectline(q1, q2,
				q3, up0, up1, up2, du0, du1, du2, du0du1, du0du2);
		int smallest2 = 0;
		if (isectB == null) {
			System.out.println(
					"coplanar triangles on compute_intervals_isectline");
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
		final WB_IntersectionResult ir = new WB_IntersectionResult();
		ir.intersection = true;
		ir.object = gf.createSegment(intersectionVertices[0],
				intersectionVertices[1]);
		return ir;
	}

	/**
	 *
	 *
	 * @param v
	 * @param u
	 * @return
	 */
	public static WB_IntersectionResult getIntersection3D(final WB_Triangle v,
			final WB_Triangle u) {
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
		final TriangleIntersection isectA = compute_intervals_isectline(v, vp0,
				vp1, vp2, dv0, dv1, dv2, dv0dv1, dv0dv2);
		if (isectA == null) {
			if (coplanarTriangles(n1, v, u)) {
				return NOINTERSECTION();
			} else {
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
		final TriangleIntersection isectB = compute_intervals_isectline(u, up0,
				up1, up2, du0, du1, du2, du0du1, du0du2);
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
		final WB_IntersectionResult ir = new WB_IntersectionResult();
		ir.intersection = true;
		ir.object = gf.createSegment(intersectionVertices[0],
				intersectionVertices[1]);
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
	protected static TriangleIntersection compute_intervals_isectline(
			final WB_Triangle v, final double vv0, final double vv1,
			final double vv2, final double d0, final double d1, final double d2,
			final double d0d1, final double d0d2) {
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
	protected static TriangleIntersection compute_intervals_isectline(
			final WB_Coord p1, final WB_Coord p2, final WB_Coord p3,
			final double vv0, final double vv1, final double vv2,
			final double d0, final double d1, final double d2,
			final double d0d1, final double d0d2) {
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
	protected static TriangleIntersection intersect(final WB_Coord v0,
			final WB_Coord v1, final WB_Coord v2, final double vv0,
			final double vv1, final double vv2, final double d0,
			final double d1, final double d2) {
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
	protected static boolean coplanarTriangles(final WB_Vector n,
			final WB_Triangle v, final WB_Triangle u) {
		// First project onto an axis-aligned plane that maximizes the area of
		// the triangles.
		int i0;
		int i1;
		final double[] a = new double[] { Math.abs(n.xd()), Math.abs(n.yd()),
				Math.abs(n.zd()) };
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
		final double[] v0 = new double[] { v.p1().xd(), v.p1().yd(),
				v.p1().zd() };
		final double[] v1 = new double[] { v.p2().xd(), v.p2().yd(),
				v.p2().zd() };
		final double[] v2 = new double[] { v.p3().xd(), v.p3().yd(),
				v.p3().zd() };
		final double[] u0 = new double[] { u.p1().xd(), u.p1().yd(),
				u.p1().zd() };
		final double[] u1 = new double[] { u.p2().xd(), u.p2().yd(),
				u.p2().zd() };
		final double[] u2 = new double[] { u.p3().xd(), u.p3().yd(),
				u.p3().zd() };
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
	protected static boolean coplanarTriangles(final WB_Vector n,
			final WB_Coord p1, final WB_Coord p2, final WB_Coord p3,
			final WB_Coord q1, final WB_Coord q2, final WB_Coord q3) {
		// First project onto an axis-aligned plane that maximizes the are of
		// the triangles.
		int i0;
		int i1;
		final double[] a = new double[] { Math.abs(n.xd()), Math.abs(n.yd()),
				Math.abs(n.zd()) };
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
	protected static boolean triangleEdgeTest(final double[] v0,
			final double[] v1, final double[] u0, final double[] u1,
			final double[] u2, final int i0, final int i1) {
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
	protected static boolean edgeEdgeTest(final double[] v0, final double[] u0,
			final double[] u1, final int i0, final int i1, final double ax,
			final double ay) {
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
	protected static boolean pointInTri(final double[] v0, final double[] u0,
			final double[] u1, final double[] u2, final int i0, final int i1) {
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
	private static WB_IntersectionResult NOINTERSECTION(final double t1,
			final double t2) {
		final WB_IntersectionResult i = new WB_IntersectionResult();
		i.intersection = false;
		i.sqDist = Float.POSITIVE_INFINITY;
		i.t1 = t1;
		i.t2 = t2;
		return i;
	}

	/**
	 *
	 *
	 * @return
	 */
	private static WB_IntersectionResult NOINTERSECTION() {
		final WB_IntersectionResult i = new WB_IntersectionResult();
		i.intersection = false;
		i.sqDist = Float.POSITIVE_INFINITY;
		i.t1 = Double.NaN;
		i.t2 = Double.NaN;
		return i;
	}

	/**
	 *
	 *
	 * @param L
	 * @param S
	 * @return
	 */
	public static WB_IntersectionResult getClosestPoint3D(final WB_Line L,
			final WB_Segment S) {
		final WB_IntersectionResult i = getClosestPoint3D(L,
				new WB_Line(S.getOrigin(), S.getDirection()));
		if (i.dimension == 0) {
			return i;
		}
		if (i.t2 <= WB_Epsilon.EPSILON) {
			i.t2 = 0;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(),
					S.getOrigin());
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		if (i.t2 >= S.getLength() - WB_Epsilon.EPSILON) {
			i.t2 = 1;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(),
					S.getEndpoint());
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		return i;
	}
	// POINT-TRIANGLE

	// POINT-POLYGON
	/**
	 *
	 *
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static WB_IntersectionResult getClosestPoint3D(final WB_Segment S1,
			final WB_Segment S2) {
		final WB_Point d1 = WB_Point.sub(S1.getEndpoint(), S1.getOrigin());
		final WB_Point d2 = WB_Point.sub(S2.getEndpoint(), S2.getOrigin());
		final WB_Point r = WB_Point.sub(S1.getOrigin(), S2.getOrigin());
		final double a = d1.dot(d1);
		final double e = d2.dot(d2);
		final double f = d2.dot(r);
		if (WB_Epsilon.isZero(a) || WB_Epsilon.isZero(e)) {
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = false;
			i.t1 = 0;
			i.t2 = 0;
			i.object = new WB_Segment(S1.getOrigin(), S2.getOrigin());
			i.dimension = 1;
			i.sqDist = r.getSqLength();
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
		final WB_IntersectionResult i = new WB_IntersectionResult();
		i.intersection = t1 > 0 && t1 < 1 && t2 > 0 && t2 < 1;
		i.t1 = t1;
		i.t2 = t2;
		final WB_Point p1 = S1.getParametricPoint(t1);
		final WB_Point p2 = S2.getParametricPoint(t2);
		i.sqDist = WB_CoordOp.getSqDistance3D(p1, p2);
		if (WB_Epsilon.isZeroSq(i.sqDist)) {
			i.dimension = 0;
			i.object = p1;
		} else {
			i.dimension = 1;
			i.object = new WB_Segment(p1, p2);
		}
		return i;
	}

	// POINT-POLYGON
	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmin2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			WB_Point q = new WB_Point(p);
			if (n > 1) {
				q = projectOnPlane(p, new WB_Plane(poly.getPoint(T[i]),
						poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2])));
			}
			tmp = getClosestPointToTriangle3D(q, poly.getPoint(T[i]),
					poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2]));
			final double d2 = WB_CoordOp.getSqDistance3D(tmp, q);
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
	public static double getDistanceToClosestPoint3D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmin2 = Double.POSITIVE_INFINITY;
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			WB_Point q = new WB_Point(p);
			if (n > 1) {
				q = projectOnPlane(p, new WB_Plane(poly.getPoint(T[i]),
						poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2])));
			}
			tmp = getClosestPointToTriangle3D(q, poly.getPoint(T[i]),
					poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2]));
			final double d2 = WB_CoordOp.getSqDistance3D(tmp, q);
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
	public static WB_Point getClosestPointOnPeriphery3D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = getClosestPointToTriangle3D(p, poly.getPoint(T[i]),
					poly.getPoint(T[i + 1]), poly.getPoint(T[i + 2]));
			final double d2 = WB_CoordOp.getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfPoints() - 1; i < poly
					.getNumberOfPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint3D(p, S);
				final double d2 = WB_CoordOp.getSqDistance3D(tmp, p);
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
	public static WB_Point getClosestPointOnPeriphery3D(final WB_Coord p,
			final WB_Polygon poly, final List<? extends WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint3D(p, T);
			final double d2 = WB_CoordOp.getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfPoints() - 1; i < poly
					.getNumberOfPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint3D(p, S);
				final double d2 = WB_CoordOp.getSqDistance3D(tmp, p);
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
	public static WB_IntersectionResult getIntersection3D(final WB_Ray ray,
			final WB_Polygon poly) {
		final WB_IntersectionResult ir = getIntersection3D(ray,
				poly.getPlane());
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
	public static WB_IntersectionResult getIntersection3D(final WB_Line line,
			final WB_Polygon poly) {
		final WB_IntersectionResult ir = getIntersection3D(line,
				poly.getPlane());
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
	public static WB_IntersectionResult getIntersection3D(
			final WB_Segment segment, final WB_Polygon poly) {
		final WB_IntersectionResult ir = getIntersection3D(segment,
				poly.getPlane());
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
	public static double getParameterOfPointOnLine3D(final WB_Coord a,
			final WB_Coord b, final WB_Coord p) {
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
	public static double getParameterOfPointOnLine3D(final WB_Coord p,
			final WB_Line L) {
		final WB_Coord ab = L.getDirection();
		final WB_Vector ac = new WB_Vector(p);
		ac.subSelf(L.getOrigin());
		return ac.dot(ab);
	}

	/**
	 * Given three vertices and three distances, return the possible fourth
	 * points of a tetrahedron
	 *
	 * @param p1
	 * @param d1
	 * @param p2
	 * @param d2
	 * @param p3
	 * @param d3
	 * @return
	 */
	public static WB_Coord[] getFourthPoint(final WB_Coord p1, final double d1,
			final WB_Coord p2, final double d2, final WB_Coord p3,
			final double d3) {
		WB_Sphere S1 = new WB_Sphere(p1, d1);
		WB_Sphere S2 = new WB_Sphere(p2, d2);
		WB_Sphere S3 = new WB_Sphere(p3, d3);
		WB_IntersectionResult is = getIntersection3D(S1, S2, S3);
		if (!is.intersection) {
			return new WB_Point[0];
		} else if (is.dimension == 0) {
			return new WB_Point[] { (WB_Point) is.object };
		} else {
			return new WB_Coord[] { ((WB_Segment) is.object).getOrigin(),
					((WB_Segment) is.object).getEndpoint() };
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
		return p.xd() >= AABB.getMinX() && p.yd() >= AABB.getMinY()
				&& p.zd() >= AABB.getMinZ() && p.xd() < AABB.getMaxX()
				&& p.yd() < AABB.getMaxY() && p.zd() < AABB.getMaxZ();
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
	public static double getDistance3D(final WB_Coord p,
			final WB_Polygon poly) {
		return Math.sqrt(getSqDistance3D(p, poly));
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
	public static double getDistanceToLine3D(final WB_Coord p, final WB_Coord a,
			final WB_Coord b) {
		return Math.sqrt(getSqDistanceToLine3D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static double getDistanceToLine3D(final WB_Coord p,
			final WB_Line L) {
		return Math.sqrt(getSqDistanceToLine3D(p, L));
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static double getDistanceToPlane3D(final WB_Coord p,
			final WB_Plane P) {
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
	public static double getDistanceToPlane3D(final double[] p,
			final WB_Plane P) {
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
	public static double getDistanceToPoint3D(final WB_Coord p,
			final WB_Coord q) {
		return Math.sqrt(getSqDistanceToPoint3D(p, q));
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
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getDistanceToRay3D(final WB_Coord p, final WB_Coord a,
			final WB_Coord b) {
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
	public static double getDistanceToSegment3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static double getDistanceToSegment3D(final WB_Coord p,
			final WB_Segment S) {
		return Math.sqrt(getSqDistanceToSegment3D(p, S));
	}

	/**
	 *
	 *
	 * @param S
	 * @param T
	 * @return
	 */
	public static double getSqDistance3D(final WB_Segment S,
			final WB_Segment T) {
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
		final WB_Vector ab = WB_Vector.subToVector3D(S.getEndpoint(),
				S.getOrigin());
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

	public static double getSqDistance3D(final WB_Coord p,
			final WB_PolyLine PL) {
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
	public static double getSqDistance3D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] T = poly.getTriangles();
		final int n = T.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Coord tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = WB_GeometryOp.getClosestPointToTriangle3D(p,
					poly.getPoint(T[i]), poly.getPoint(T[i + 1]),
					poly.getPoint(T[i + 2]));
			final double d2 = WB_CoordOp.getDistance3D(tmp, p);
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

	// POINT-POINT
	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_Line L) {
		final WB_Coord ab = L.getDirection();
		final WB_Vector ac = new WB_Vector(L.getOrigin(), p);
		final double e = ac.dot(ab);
		final double f = WB_Vector.dot(ab, ab);
		return ac.dot(ac) - e * e / f;
	}

	// POINT-PLANE
	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
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

	// POINT-SEGMENT
	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getSqDistanceToLine3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static double getSqDistanceToLine3D(final WB_Coord p,
			final WB_Line L) {
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
	public static double getSqDistanceToPlane3D(final WB_Coord p,
			final WB_Plane P) {
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
	public static double getSqDistanceToPoint3D(final WB_Coord p,
			final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd())
				+ (q.yd() - p.yd()) * (q.yd() - p.yd())
				+ (q.zd() - p.zd()) * (q.zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord p, final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd())
				+ (q.yd() - p.yd()) * (q.yd() - p.yd())
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
	public static double getSqDistanceToRay3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = gf.createVectorFromTo(a, b);
		final WB_Vector ac = gf.createVectorFromTo(a, p);
		final double e = ac.dot(ab);
		if (e <= 0) {
			return ac.dot(ac);
		}
		final double f = ab.dot(ab);
		return ac.dot(ac) - e * e / f;
	}

	// POINT-AABB
	/**
	 *
	 *
	 * @param p
	 * @param R
	 * @return
	 */
	public static double getSqDistanceToRay3D(final WB_Coord p,
			final WB_Ray R) {
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
	public static double getSqDistanceToSegment3D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static double getSqDistanceToSegment3D(final WB_Coord p,
			final WB_Segment S) {
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
	public static double signedDistanceToPlane3D(final WB_Coord p,
			final WB_Plane P) {
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
	public static int[] getExtremePointsAlongDirection(final WB_Coord[] points,
			final WB_Coord dir) {
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
	public static int[] getExtremePointsAlongDirection(
			final Collection<? extends WB_Coord> points, final WB_Coord dir) {
		final int[] result = new int[] { -1, -1 };
		double minproj = Double.POSITIVE_INFINITY;
		double maxproj = Double.NEGATIVE_INFINITY;
		double proj;
		int i = 0;
		for (WB_Coord point : points) {
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
	public static WB_Classification classifyPointToPlaneFast3D(final WB_Coord p,
			final WB_Plane P) {
		return classifyPointToPlaneFast3D(P, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param P
	 * @return
	 */
	public static WB_Classification classifyPointToPlane3D(final WB_Coord p,
			final WB_Plane P) {
		return classifyPointToPlane3D(P, p);
	}

	/**
	 *
	 *
	 * @param P
	 * @param p
	 * @return
	 */
	public static WB_Classification classifyPointToPlaneFast3D(final WB_Plane P,
			final WB_Coord p) {
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
	 * @return WB_Classification.ON,WB_Classification.FRONT or
	 *         WB_Classification.BACK
	 */
	public static WB_Classification classifyPointToPlane3D(final WB_Plane P,
			final WB_Coord p) {
		if (WB_Epsilon.isZero(WB_GeometryOp.getDistanceToPlane3D(p, P))) {
			return WB_Classification.ON;
		}
		final double signp = WB_Predicates.orient3D(P.getOrigin(),
				P.getOrigin().addMul(100, P.getU()),
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
	public static WB_Classification classifyPointToTetrahedron3D(
			final WB_Tetrahedron T, final WB_Coord p) {
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
	public static WB_Classification classifyPolygonToPlane3D(
			final WB_Polygon poly, final WB_Plane P) {
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
	public static WB_Classification classifySegmentToPlane3D(
			final WB_Segment segment, final WB_Plane P) {
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

	public static WB_Classification classifySegmentToPlane3D(final WB_Coord p,
			final WB_Coord q, final WB_Plane P) {
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
	public static WB_Classification classifyPolygonToPlaneFast3D(
			final WB_Polygon poly, final WB_Plane P) {
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

	// POINT-POLYGON
	/**
	 *
	 *
	 * @param L
	 * @param p
	 * @return
	 */
	public static WB_Line getParallelLineThroughPoint(final WB_Line L,
			final WB_Coord p) {
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
		return new WB_Plane(gf.createInterpolatedPoint(p, q, 0.5),
				gf.createVectorFromTo(p, q));
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
			for (WB_Coord point : points) {
				dist2 = WB_Point.getSqDistance3D(point, center);
				if (dist2 > radius2) {
					dist = Math.sqrt(dist2);
					if (i < 2) {
						alpha = dist / radius;
						ialpha2 = 1.0 / (alpha * alpha);
						radius = 0.5 * (alpha + 1 / alpha) * radius;
						center = gf.createMidpoint(
								center.mulSelf(1.0 + ialpha2),
								WB_Point.mul(point, 1.0 - ialpha2));
					} else {
						radius = (radius + dist) * 0.5;
						center.mulAddMulSelf(radius / dist,
								(dist - radius) / dist, point);
					}
					radius2 = radius * radius;
				}
			}
		}
		return new WB_Sphere(center, radius);
	}

	/**
	 * Gets the area.
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return the area
	 */
	public static double getArea(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3) {
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
	public static double getSignedArea(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3) {
		final WB_Plane P = new WB_Plane(p1, p2, p3);
		if (P.getNormal().getSqLength() < WB_Epsilon.SQEPSILON) {
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
				area = p1.yd() * (p2.zd() - p3.zd())
						+ p2.yd() * (p3.zd() - p1.zd())
						+ p3.yd() * (p1.zd() - p2.zd());
				break;
			case 2:
				area = p1.xd() * (p2.zd() - p3.zd())
						+ p2.xd() * (p3.zd() - p1.zd())
						+ p3.xd() * (p1.zd() - p2.zd());
				break;
			case 3:
				area = p1.xd() * (p2.yd() - p3.yd())
						+ p2.xd() * (p3.yd() - p1.yd())
						+ p3.xd() * (p1.yd() - p2.yd());
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
	public static boolean pointInTriangleBary3D(final WB_Coord p,
			final WB_Coord A, final WB_Coord B, final WB_Coord C) {
		if (p == A) {
			return false;
		}
		if (p == B) {
			return false;
		}
		if (p == C) {
			return false;
		}
		if (WB_Epsilon
				.isZeroSq(WB_GeometryOp.getSqDistanceToLine2D(A, B, C))) {
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
		return u > WB_Epsilon.EPSILON && v > WB_Epsilon.EPSILON
				&& u + v < 1 - WB_Epsilon.EPSILON;
	}

	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static boolean pointInTriangleBary3D(final WB_Coord p,
			final WB_Triangle T) {
		return pointInTriangleBary3D(p, T.p1, T.p2, T.p3);
	}

	/**
	 * Are the planes equal?.
	 *
	 * @param P
	 *            the p
	 * @param Q
	 *            the q
	 * @return true/false
	 */
	public static boolean isEqual(final WB_Plane P, final WB_Plane Q) {
		if (!WB_Epsilon
				.isZero(WB_GeometryOp.getDistance3D(P.getOrigin(), Q))) {
			return false;
		}
		if (!WB_Epsilon
				.isZero(WB_GeometryOp.getDistance3D(Q.getOrigin(), P))) {
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
	public static WB_Polygon[] splitPolygon(final WB_Polygon poly,
			final WB_Plane P) {
		if (!poly.isSimple()) {
			throw new UnsupportedOperationException(
					"Only simple polygons are supported at this time!");
		}
		final List<WB_Coord> frontVerts = new FastList<WB_Coord>();
		final List<WB_Coord> backVerts = new FastList<WB_Coord>();
		final int numVerts = poly.numberOfShellPoints;
		if (numVerts > 0) {
			WB_Coord a = poly.points.get(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp.classifyPointToPlane3D(a,
					P);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				final WB_IntersectionResult i;
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
	public static WB_Polygon trimConvexPolygon(WB_Polygon poly,
			final double d) {
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
			final WB_Polygon[] split = splitPolygon(poly,
					new WB_Plane(origin, normal));
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
	public static WB_Polygon trimConvexPolygon(WB_Polygon poly,
			final double[] d) {
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
			final WB_Polygon[] split = splitPolygon(poly,
					new WB_Plane(origin, normal));
			poly = split[0];
		}
		return poly;
	}

	/**
	 * Get cosine of dihedral angle between two triangles abc and bcd defined by
	 * 4 points, bc is the common edge.
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return cosine of dihedral angle
	 */
	public static double getCosDihedralAngle(final WB_Coord a, final WB_Coord b,
			final WB_Coord c, final WB_Coord d) {
		return getCosDihedralAngle(gf.createVectorFromTo(b, a),
				gf.createVectorFromTo(c, b), gf.createVectorFromTo(d, c));
	}

	/**
	 * Get cosine of dihedral angle between two planes uv and vw defined by
	 * three vectors.
	 *
	 * @param u
	 *            WB_Coordinate
	 * @param v
	 *            WB_Coordinate
	 * @param w
	 *            WB_Coordinate
	 * @return cosine of dihedral angle
	 */
	public static double getCosDihedralAngle(final WB_Coord u, final WB_Coord v,
			final WB_Coord w) {
		final WB_Vector uxv = gf.createVector(u).crossSelf(v);
		final WB_Vector vxw = gf.createVector(v).crossSelf(w);
		return WB_Math.clamp(
				-uxv.dot(vxw) / (uxv.getLength() * vxw.getLength()), -1, 1);
	}

	/**
	 * Get cosine of dihedral angle between two planes with unit length normals
	 * n1 and n2.
	 *
	 * @param n1
	 *            WB_Coordinate
	 * @param n2
	 *            WB_Coordinate
	 * @return cosine of dihedral angle
	 */
	public static double getCosDihedralAngleNorm(final WB_Coord n1,
			final WB_Coord n2) {
		return -WB_Math.clamp(WB_Vector.dot(n1, n2), -1, 1);
	}

	/**
	 * Get cosine of dihedral angle between two planes with arbitrary normals n1
	 * and n2.
	 *
	 * @param n1
	 *            WB_Coordinate
	 * @param n2
	 *            WB_Coordinate
	 * @return cosine of dihedral angle
	 */
	public static double getCosDihedralAngle(final WB_Coord n1,
			final WB_Coord n2) {
		WB_Vector nn1 = new WB_Vector(n1);
		nn1.normalizeSelf();
		WB_Vector nn2 = new WB_Vector(n2);
		nn2.normalizeSelf();
		return -WB_Math.clamp(WB_Vector.dot(nn1, nn2), -1, 1);
	}

	/**
	 * Get dihedral angle between two triangles abc and bcd defined by 4 points,
	 * bc is the common edge.
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return dihedral angle
	 */
	public static double getDihedralAngleNorm(final WB_Coord a,
			final WB_Coord b, final WB_Coord c, final WB_Coord d) {
		return Math.acos(getCosDihedralAngle(a, b, c, d));
	}

	/**
	 * Get dihedral angle between two planes uv and vw defined by three vectors.
	 *
	 * @param u
	 *            WB_Coordinate
	 * @param v
	 *            WB_Coordinate
	 * @param w
	 *            WB_Coordinate
	 * @return dihedral angle
	 */
	public static double getDihedralAngle(final WB_Coord u, final WB_Coord v,
			final WB_Coord w) {
		return Math.acos(getCosDihedralAngle(u, v, w));
	}

	/**
	 * Get dihedral angle between two planes with unit length normals n1 and n2.
	 *
	 * @param n1
	 *            WB_Coordinate
	 * @param n2
	 *            WB_Coordinate
	 * @return dihedral angle
	 */
	public static double getDihedralAngleNorm(final WB_Coord n1,
			final WB_Coord n2) {
		return Math.acos(getCosDihedralAngleNorm(n1, n2));
	}

	/**
	 * Get dihedral angle between two planes with arbitrary normals n1 and n2.
	 *
	 * @param n1
	 *            WB_Coordinate
	 * @param n2
	 *            WB_Coordinate
	 * @return dihedral angle
	 */
	public static double getDihedralAngle(final WB_Coord n1,
			final WB_Coord n2) {
		return Math.acos(getCosDihedralAngle(n1, n2));
	}

	public static WB_Point getClosestPoint3D(final WB_Coord p,
			final WB_Circle circle) {
		WB_Point pmc = WB_Point.sub(p, circle.getCenter());
		WB_Point qmc = pmc.sub(
				WB_Point.mul(circle.getNormal(), pmc.dot(circle.getNormal())));
		if (qmc.isZero()) {
			qmc.set(perpendicular(circle.getNormal()));
		}
		WB_Point rmc = qmc.mulSelf(circle.getRadius() / qmc.getLength());
		return rmc.addSelf(circle.getCenter());
	}

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
	public static double getSignedAngleBetween(final double ux, final double uy,
			final double uz, final double vx, final double vy, final double vz,
			final double nx, final double ny, final double nz) {
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
	public static double getSignedAngleBetween(final double cx, final double cy,
			final double cz, final double px, final double py, final double pz,
			final double qx, final double qy, final double qz, final double nx,
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
	public static double getSignedAngleBetweenNorm(final double ux,
			final double uy, final double uz, final double vx, final double vy,
			final double vz, final double nx, final double ny,
			final double nz) {
		final WB_Vector v0 = new WB_Vector(ux, uy, uz);
		final WB_Vector v1 = new WB_Vector(vx, vy, vz);
		double d = v0.dot(v1);
		if (d < -1.0) {
			d = -1.0;
		}
		if (d > 1.0) {
			d = 1.0;
		}
		WB_Vector cross = v0.cross(v1);
		double sign = cross.dot(new WB_Vector(nx, ny, nz));
		return sign < 0 ? -Math.acos(d) : Math.acos(d);
	}

	public static WB_Sphere getBoundingSphereInCenter(
			final Collection<? extends WB_Coord> points) {
		double r = 0;
		WB_Point center = new WB_Point();
		for (WB_Coord p : points) {
			center.addSelf(p);
		}
		center.divSelf(points.size());
		for (WB_Coord p : points) {
			WB_Vector diff = WB_Vector.sub(p, center);
			double radiusSqr = diff.dot(diff);
			if (radiusSqr > r) {
				r = radiusSqr;
			}
		}
		return new WB_Sphere(center, Math.sqrt(r));
	}

	public static WB_Sphere getBoundingSphereInCenter(final WB_Coord[] points) {
		double r = 0;
		WB_Point center = new WB_Point();
		for (WB_Coord p : points) {
			center.addSelf(p);
		}
		center.divSelf(points.length);
		for (WB_Coord p : points) {
			WB_Vector diff = WB_Vector.sub(p, center);
			double radiusSqr = diff.dot(diff);
			if (radiusSqr > r) {
				r = radiusSqr;
			}
		}
		return new WB_Sphere(center, Math.sqrt(r));
	}

	public static WB_Sphere mergeSpheres(final WB_Sphere S1,
			final WB_Sphere S2) {
		WB_Vector cenDiff = WB_Vector.sub(S2.getCenter(), S1.getCenter());
		double lenSqr = cenDiff.dot(cenDiff);
		double rDiff = S2.getRadius() - S1.getRadius();
		double rDiffSqr = rDiff * rDiff;
		if (rDiffSqr >= lenSqr) {
			return rDiff >= 0.0 ? S2 : S1;
		} else {
			double length = Math.sqrt(lenSqr);
			WB_Point center;
			if (length > 0) {
				double coeff = (length + rDiff) / (2.0 * length);
				center = WB_Point.addMul(S1.getCenter(), coeff, cenDiff);
			} else {
				center = new WB_Point(S1.getCenter());
			}
			double radius = 0.5 * (length + S1.getRadius() + S2.getRadius());
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
	public static double cotan(final WB_Coord p0, final WB_Coord p1,
			final WB_Coord p2) {
		WB_Vector v0 = WB_Vector.subToVector3D(p1, p0);
		WB_Vector v1 = WB_Vector.subToVector3D(p2, p0);
		return WB_Vector.dot(v0, v1) / WB_Vector.cross(v0, v1).getLength();
	}
}
