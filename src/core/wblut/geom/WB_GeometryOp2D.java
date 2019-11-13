/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_GeometryOp2D extends WB_GeometryOpGLU {
	private static final WB_GeometryFactory gf = new WB_GeometryFactory();

	/**
	 *
	 *
	 * @return
	 */
	private static final WB_IntersectionResult NOINTERSECTION() {
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
	 * @param S1
	 * @param S2
	 * @return
	 */
	public static final WB_IntersectionResult getIntersection2D(
			final WB_Segment S1, final WB_Segment S2) {
		final double a1 = WB_GeometryOp.twiceSignedTriArea2D(S1.getOrigin(),
				S1.getEndpoint(), S2.getEndpoint());
		final double a2 = WB_GeometryOp.twiceSignedTriArea2D(S1.getOrigin(),
				S1.getEndpoint(), S2.getOrigin());
		if (!WB_Epsilon.isZero(a1) && !WB_Epsilon.isZero(a2) && a1 * a2 < 0) {
			final double a3 = WB_GeometryOp.twiceSignedTriArea2D(
					S2.getOrigin(), S2.getEndpoint(), S1.getOrigin());
			final double a4 = a3 + a2 - a1;
			if (a3 * a4 < 0) {
				final double t1 = a3 / (a3 - a4);
				final double t2 = a1 / (a1 - a2);
				final WB_IntersectionResult i = new WB_IntersectionResult();
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
	public static final void getIntersection2DInto(final WB_Segment S1,
			final WB_Segment S2, final WB_IntersectionResult i) {
		final double a1 = WB_GeometryOp.twiceSignedTriArea2D(S1.getOrigin(),
				S1.getEndpoint(), S2.getEndpoint());
		final double a2 = WB_GeometryOp.twiceSignedTriArea2D(S1.getOrigin(),
				S1.getEndpoint(), S2.getOrigin());
		if (!WB_Epsilon.isZero(a1) && !WB_Epsilon.isZero(a2) && a1 * a2 < 0) {
			final double a3 = WB_GeometryOp.twiceSignedTriArea2D(
					S2.getOrigin(), S2.getEndpoint(), S1.getOrigin());
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
	public static final WB_Segment[] splitSegment2D(final WB_Segment S,
			final WB_Line L) {
		WB_Segment[] result = new WB_Segment[2];
		final WB_IntersectionResult ir2D = getClosestPoint2D(S, L);
		if (!ir2D.intersection) {
			result = new WB_Segment[1];
			result[0] = S;
		}
		if (ir2D.dimension == 0) {
			result = new WB_Segment[2];
			if (WB_GeometryOp.classifyPointToLine2D(S.getOrigin(),
					L) == WB_Classification.FRONT) {
				result[0] = new WB_Segment(S.getOrigin(),
						(WB_Point) ir2D.object);
				result[1] = new WB_Segment((WB_Point) ir2D.object,
						S.getEndpoint());
			} else if (WB_GeometryOp.classifyPointToLine2D(S.getOrigin(),
					L) == WB_Classification.BACK) {
				result[1] = new WB_Segment(S.getOrigin(),
						(WB_Point) ir2D.object);
				result[0] = new WB_Segment((WB_Point) ir2D.object,
						S.getEndpoint());
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
	public static final WB_Polygon[] splitPolygon2D(final WB_Polygon poly,
			final WB_Line L) {
		final ArrayList<WB_Coord> frontVerts = new ArrayList<WB_Coord>(20);
		final ArrayList<WB_Coord> backVerts = new ArrayList<WB_Coord>(20);
		final int numVerts = poly.numberOfShellPoints;
		if (numVerts > 0) {
			WB_Coord a = poly.getPoint(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp.classifyPointToLine2D(a,
					L);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				WB_IntersectionResult i = new WB_IntersectionResult();
				b = poly.getPoint(n);
				bSide = WB_GeometryOp.classifyPointToLine2D(b, L);
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
	public static final ArrayList<WB_Point> getIntersection2D(
			final WB_Circle C0, final WB_Circle C1) {
		final ArrayList<WB_Point> result = new ArrayList<WB_Point>();
		final WB_Point u = WB_Point.sub(C1.getCenter(), C0.getCenter());
		final double d2 = u.getSqLength2D();
		final double d = Math.sqrt(d2);
		if (WB_Epsilon.isEqualAbs(d, C0.getRadius() + C1.getRadius())) {
			result.add(gf.createInterpolatedPoint(C0.getCenter(),
					C1.getCenter(),
					C0.getRadius() / (C0.getRadius() + C1.getRadius())));
			return result;
		}
		if (d > C0.getRadius() + C1.getRadius()
				|| d < WB_Math.fastAbs(C0.getRadius() - C1.getRadius())) {
			return result;
		}
		final double r02 = C0.getRadius() * C0.getRadius();
		final double r12 = C1.getRadius() * C1.getRadius();
		final double a = (r02 - r12 + d2) / (2 * d);
		final double h = Math.sqrt(r02 - a * a);
		final WB_Point c = u.mul(a / d).addSelf(C0.getCenter());
		final double p0x = c.xd()
				+ h * (C1.getCenter().yd() - C0.getCenter().yd()) / d;
		final double p0y = c.yd()
				- h * (C1.getCenter().xd() - C0.getCenter().xd()) / d;
		final double p1x = c.xd()
				- h * (C1.getCenter().yd() - C0.getCenter().yd()) / d;
		final double p1y = c.yd()
				+ h * (C1.getCenter().xd() - C0.getCenter().xd()) / d;
		final WB_Point p0 = new WB_Point(p0x, p0y);
		result.add(p0);
		final WB_Point p1 = new WB_Point(p1x, p1y);
		if (!WB_Epsilon.isZeroSq(WB_CoordOp2D.getSqDistance2D(p0, p1))) {
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
	public static final ArrayList<WB_Point> getIntersection2D(final WB_Line L,
			final WB_Circle C) {
		final ArrayList<WB_Point> result = new ArrayList<WB_Point>();
		final double b = 2 * (L.getDirection().xd()
				* (L.getOrigin().xd() - C.getCenter().xd())
				+ L.getDirection().yd()
						* (L.getOrigin().yd() - C.getCenter().yd()));
		final double c = WB_CoordOp2D.getSqLength2D(C.getCenter())
				+ WB_Vector.getSqLength2D(L.getOrigin())
				- 2 * (C.getCenter().xd() * L.getOrigin().xd()
						+ C.getCenter().yd() * L.getOrigin().yd())
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

	public static final ArrayList<WB_Point> getIntersection2D(final WB_Ray R,
			final WB_Circle C) {
		final ArrayList<WB_Point> result = new ArrayList<WB_Point>();
		final double b = 2 * (R.getDirection().xd()
				* (R.getOrigin().xd() - C.getCenter().xd())
				+ R.getDirection().yd()
						* (R.getOrigin().yd() - C.getCenter().yd()));
		final double c = WB_CoordOp2D.getSqLength2D(C.getCenter())
				+ WB_Vector.getSqLength2D(R.getOrigin())
				- 2 * (C.getCenter().xd() * R.getOrigin().xd()
						+ C.getCenter().yd() * R.getOrigin().yd())
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
	public static final boolean checkIntersection2DProper(final WB_Coord a,
			final WB_Coord b, final WB_Coord c, final WB_Coord d) {
		if (WB_Predicates.orient2D(a, b, c) == 0
				|| WB_Predicates.orient2D(a, b, d) == 0
				|| WB_Predicates.orient2D(c, d, a) == 0
				|| WB_Predicates.orient2D(c, d, b) == 0) {
			return false;
		} else if (WB_Predicates.orient2D(a, b, c)
				* WB_Predicates.orient2D(a, b, d) > 0
				|| WB_Predicates.orient2D(c, d, a)
						* WB_Predicates.orient2D(c, d, b) > 0) {
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
	public static final WB_Point getClosestPoint2D(final WB_Coord p,
			final WB_Segment S) {
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

	public static final WB_Point getClosestPoint2D(final WB_Coord p,
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
			d2 = WB_CoordOp2D.getSqDistance2D(p, test);
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
	public static final WB_Point getClosestPoint2D(final WB_Segment S,
			final WB_Coord p) {
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
	public static final WB_Point getClosestPointToSegment2D(final WB_Coord p,
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
	public static final WB_Point getClosestPoint2D(final WB_Coord p,
			final WB_Line L) {
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
	public static final WB_Point getClosestPointToLine2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static final WB_Point getClosestPoint2D(final WB_Coord p,
			final WB_Ray R) {
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
	public static final WB_Point getClosestPointToRay2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static final WB_IntersectionResult getClosestPoint2D(
			final WB_Segment S1, final WB_Segment S2) {
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
		i.sqDist = WB_CoordOp2D.getSqDistance2D(p1, p2);
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
	public static final WB_IntersectionResult getClosestPoint2D(
			final WB_Line L1, final WB_Line L2) {
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
			final double d2 = WB_CoordOp2D.getSqDistance2D(L1.getOrigin(), p2);
			final WB_IntersectionResult i = new WB_IntersectionResult();
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
		final double d2 = WB_CoordOp2D.getSqDistance2D(p1, p2);
		final WB_IntersectionResult i = new WB_IntersectionResult();
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
	public static final WB_IntersectionResult getClosestPoint2D(final WB_Line L,
			final WB_Segment S) {
		final WB_IntersectionResult i = getClosestPoint2D(L,
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

	/**
	 *
	 *
	 * @param S
	 * @param L
	 * @return
	 */
	public static final WB_IntersectionResult getClosestPoint2D(
			final WB_Segment S, final WB_Line L) {
		return getClosestPoint2D(L, S);
	}

	// POINT-TRIANGLE
	/**
	 *
	 *
	 * @param p
	 * @param T
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p,
			final WB_Triangle T) {
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
	public static final WB_Point getClosestPointToTriangle2D(final WB_Coord p,
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
	public static final WB_Point getClosestPointOnPeriphery2D(final WB_Coord p,
			final WB_Triangle T) {
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
		final double dA2 = WB_CoordOp2D.getSqDistance2D(p, A);
		final WB_Point B = getClosestPointToSegment2D(p, T.p1, T.p3);
		final double dB2 = WB_CoordOp2D.getSqDistance2D(p, B);
		final WB_Point C = getClosestPointToSegment2D(p, T.p1, T.p2);
		final double dC2 = WB_CoordOp2D.getSqDistance2D(p, C);
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
	 * @param poly
	 * @return
	 */
	public static final WB_Point getClosestPoint2D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] tris = poly.getTriangles();
		final int n = tris.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = getClosestPointToTriangle2D(p, poly.getPoint(tris[i]),
					poly.getPoint(tris[i + 1]), poly.getPoint(tris[i + 2]));
			final double d2 = WB_CoordOp2D.getDistance2D(tmp, p);
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
	public static final WB_Point getClosestPoint2D(final WB_Coord p,
			final ArrayList<? extends WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint2D(p, T);
			final double d2 = WB_CoordOp2D.getDistance2D(tmp, p);
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
	public static final WB_Point getClosestPointOnPeriphery2D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] tris = poly.getTriangles();
		final int n = tris.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = getClosestPointToTriangle2D(p, poly.getPoint(tris[i]),
					poly.getPoint(tris[i + 1]), poly.getPoint(tris[i + 2]));
			final double d2 = WB_CoordOp2D.getSqDistance2D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfShellPoints() - 1; i < poly
					.getNumberOfShellPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint2D(p, S);
				final double d2 = WB_CoordOp2D.getSqDistance2D(tmp, p);
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
	public static final WB_Point getClosestPointOnPeriphery2D(final WB_Coord p,
			final WB_Polygon poly, final ArrayList<WB_Triangle> tris) {
		final int n = tris.size();
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		WB_Triangle T;
		for (int i = 0; i < n; i++) {
			T = tris.get(i);
			tmp = getClosestPoint2D(p, T);
			final double d2 = WB_CoordOp2D.getSqDistance2D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		if (WB_Epsilon.isZeroSq(dmax2)) {
			dmax2 = Double.POSITIVE_INFINITY;
			WB_Segment S;
			for (int i = 0, j = poly.getNumberOfShellPoints() - 1; i < poly
					.getNumberOfShellPoints(); j = i, i++) {
				S = new WB_Segment(poly.getPoint(j), poly.getPoint(i));
				tmp = getClosestPoint2D(p, S);
				final double d2 = WB_CoordOp2D.getSqDistance2D(tmp, p);
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
	public static final boolean between2D(final WB_Coord a, final WB_Coord b,
			final WB_Coord c) {
		if (WB_CoordOp2D.isCoincident2D(a, c)) {
			return true;
		} else if (WB_CoordOp2D.isCoincident2D(b, c)) {
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
	public static final boolean betweenStrict2D(final WB_Coord a,
			final WB_Coord b, final WB_Coord c) {
		if (WB_CoordOp2D.isCoincident2D(a, c)) {
			return false;
		} else if (WB_CoordOp2D.isCoincident2D(b, c)) {
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
	public static final double getParameterOfPointOnLine2D(final WB_Coord a,
			final WB_Coord b, final WB_Coord p) {
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
	public static final double getParameterOfPointOnLine2D(final WB_Coord p,
			final WB_Line L) {
		final WB_Vector ab = new WB_Vector(L.direction.xd(), L.direction.yd());
		final WB_Vector ac = new WB_Vector(p.xd() - L.origin.xd(),
				p.yd() - L.origin.yd());
		return ac.dot2D(ab);
	}

	/**
	 *
	 *
	 * @param p
	 * @param AABB
	 * @return
	 */
	public static final boolean contains2D(final WB_Coord p,
			final WB_AABB2D AABB) {
		return p.xd() >= AABB.getMinX() && p.yd() >= AABB.getMinY()
				&& p.xd() < AABB.getMaxX() && p.yd() < AABB.getMaxY();
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getDistanceToLine2D(final WB_Coord p,
			final WB_Line L) {
		return Math.sqrt(getSqDistanceToLine2D(p, L));
	}
	
	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p,
			final WB_Coord q) {
		return Math.sqrt(getSqDistance2D(p, q));
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p,
			final WB_Segment S) {
		return Math.sqrt(getSqDistance2D(p, S));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getDistance2D(final WB_Coord p,
			final WB_Line L) {
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
	public static final double getDistanceToLine2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToLine2D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getDistanceToPoint2D(final WB_Coord p,
			final WB_Coord q) {
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
	public static final double getDistanceToRay2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static final double getDistanceToSegment2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
		return Math.sqrt(getSqDistanceToSegment2D(p, a, b));
	}

	/**
	 *
	 *
	 * @param p
	 * @param S
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p,
			final WB_Segment S) {
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

	public static final double getSqDistance2D(final WB_Coord p,
			final WB_PolyLine PL) {
		double d2min = Double.POSITIVE_INFINITY;
		double d2;
		for (int s = 0; s < PL.getNumberSegments(); s++) {
			d2 = WB_GeometryOp.getSqDistance2D(p, PL.getSegment(s));
			if (d2 < d2min) {
				d2min = d2;
			}
		}
		return d2min;
	}

	public static final double getDistance2D(final WB_Coord p,
			final WB_PolyLine PL) {
		return Math.sqrt(getSqDistance2D(p, PL));
	}

	/**
	 *
	 *
	 * @param p
	 * @param L
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p,
			final WB_Line L) {
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
	public static final double getSqDistance2D(final WB_Coord p,
			final WB_Ray R) {
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

	// POINT-SEGMENT
	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final double getSqDistanceToLine2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static final double getSqDistanceToLine2D(final WB_Coord p,
			final WB_Line L) {
		final WB_Point ab = gf.createPoint(L.getDirection().xd(),
				L.getDirection().yd());
		final WB_Point ac = gf.createPoint(p.xd() - L.getOrigin().xd(),
				p.yd() - L.getOrigin().yd());
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
	public static final double getSqDistanceToPoint2D(final WB_Coord p,
			final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd())
				+ (q.yd() - p.yd()) * (q.yd() - p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static final double getSqDistance2D(final WB_Coord p,
			final WB_Coord q) {
		return (q.xd() - p.xd()) * (q.xd() - p.xd())
				+ (q.yd() - p.yd()) * (q.yd() - p.yd());
	}

	// POINT-RAY
	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final double getSqDistanceToRay2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
		final WB_Vector ab = new WB_Vector(a, b);
		final WB_Vector ac = new WB_Vector(a, p);
		final double e = ac.dot2D(ab);
		if (e <= 0) {
			return ac.dot2D(ac);
		}
		final double f = ab.dot2D(ab);
		return ac.dot2D(ac) - e * e / f;
	}

	// POINT-AABB
	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	public static final double getSqDistanceToSegment2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b) {
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
	public static final int[] getExtremePointsAlongDirection2D(
			final WB_Coord[] points, final WB_Coord dir) {
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
	public static final int[] getExtremePointsAlongDirection2D(
			final Collection<? extends WB_Coord> points, final WB_Coord dir) {
		final int[] result = new int[] { -1, -1 };
		double minproj = Double.POSITIVE_INFINITY;
		double maxproj = Double.NEGATIVE_INFINITY;
		double proj;
		int i = 0;
		for (WB_Coord point : points) {
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
	public static final WB_Classification classifyPointToLine2D(
			final WB_Coord p, final WB_Line L) {
		final double dist = -L.getDirection().yd() * p.xd()
				+ L.getDirection().xd() * p.yd()
				+ L.getOrigin().xd() * L.getDirection().yd()
				- L.getOrigin().yd() * L.getDirection().xd();
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
	public static final WB_Classification classifyPointToCircle2D(
			final WB_Coord p, final WB_Circle C) {
		final double dist = WB_GeometryOp.getDistanceToPoint2D(p,
				C.getCenter());
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
	public static final WB_Classification classifyCircleToCircle2D(
			final WB_Circle C1, final WB_Circle C2) {
		if (C1.equals(C2)) {
			return WB_Classification.ON;
		}
		final double dist = WB_GeometryOp.getDistanceToPoint2D(C1.getCenter(),
				C2.getCenter());
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
	public static final WB_Classification classifyCircleToLine2D(
			final WB_Circle C, final WB_Line L) {
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
	public static final boolean sameSideOfLine2D(final WB_Coord p,
			final WB_Coord q, final WB_Line L) {
		final WB_Point pL = L.getPoint(1.0);
		final double pside = Math
				.signum(WB_Predicates.orient2D(L.getOrigin(), pL, p));
		final double qside = Math
				.signum(WB_Predicates.orient2D(L.getOrigin(), pL, q));
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
	public static final WB_Classification classifySegmentToLine2D(
			final WB_Segment seg, final WB_Line L) {
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
	public static final WB_Classification classifyPolygonToLine2D(
			final WB_Polygon P, final WB_Line L) {
		int numFront = 0;
		int numBack = 0;
		for (int i = 0; i < P.getNumberOfPoints(); i++) {
			if (classifyPointToLine2D(P.getPoint(i),
					L) == WB_Classification.FRONT) {
				numFront++;
			} else if (classifyPointToLine2D(P.getPoint(i),
					L) == WB_Classification.BACK) {
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
	public static final boolean contains2D(final WB_Coord p,
			final WB_Polygon poly) {
		return WB_Epsilon.isZeroSq(
				WB_Point.getSqDistance2D(p, getClosestPoint2D(p, poly)));
	}

	/**
	 *
	 *
	 * @param p
	 * @param tris
	 * @return
	 */
	public static final boolean contains2D(final WB_Coord p,
			final ArrayList<? extends WB_Triangle> tris) {
		return WB_Epsilon.isZeroSq(
				WB_Point.getSqDistance2D(p, getClosestPoint2D(p, tris)));
	}

	public static final boolean contains2D(final WB_Coord p,
			final WB_Triangle tris) {
		return WB_Epsilon.isZeroSq(
				WB_Point.getSqDistance2D(p, getClosestPoint2D(p, tris)));
	}

	/**
	 * Check the intersection of two intervals [u0,u1] and [v0,v1]. The result
	 * is an array of double, the first value gives the number of values needed
	 * to define the intersection interval. If the intervals do not
	 * intersect,the array contains no additional values. If the intervals
	 * intersect in a single value, the array contains this value. Otherwise the
	 * endvalues of the intersection interval are given The function sorts the
	 * passed intervals if necessary.
	 *
	 * For example
	 *
	 * [0,2] and [3,4]: result [0] [0,2] and [2,4]: result [1,2] [0,2] and
	 * [1,4]: result [2,1,2]
	 *
	 * @param u0
	 * @param u1
	 * @param v0
	 * @param v1
	 * @return
	 */
	public static final double[] getIntervalIntersection2D(final double u0,
			final double u1, final double v0, final double v1) {
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
			for (WB_Coord point : points) {
				dist2 = WB_Point.getSqDistance2D(point, center);
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
		return new WB_Circle(center, radius);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static final WB_Circle getBoundingCircle2D(
			final Collection<? extends WB_Coord> points) {
		WB_Point center = new WB_Point(points.iterator().next());
		double radius = WB_Epsilon.EPSILON;
		double radius2 = radius * radius;
		double dist, dist2, alpha, ialpha2;
		for (int i = 0; i < 3; i++) {
			for (WB_Coord point : points) {
				dist2 = WB_Point.getSqDistance2D(point, center);
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
		return new WB_Circle(center, radius);
	}

	/**
	 *
	 *
	 * @param C
	 * @param p
	 * @return
	 */
	public static final WB_Line getLineTangentToCircleAtPoint2D(
			final WB_Circle C, final WB_Coord p) {
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
	public static final ArrayList<WB_Line> getLinesTangentToCircleThroughPoint(
			final WB_Circle C, final WB_Coord p) {
		final ArrayList<WB_Line> result = new ArrayList<WB_Line>(2);
		final double dcp = WB_CoordOp2D.getDistance2D(C.getCenter(), p);
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
			result.add(new WB_Line(p, new WB_Point(-(r2 * u.yd() + rad) / denom,
					(r2 - (num + u.yd() * rad) / denom) / u.xd())));
			result.add(new WB_Line(p, new WB_Point(-(r2 * u.yd() - rad) / denom,
					(r2 - (num - u.yd() * rad) / denom) / u.xd())));
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
	public static final ArrayList<WB_Line> getLinesTangentTo2Circles(
			final WB_Circle C0, final WB_Circle C1) {
		final ArrayList<WB_Line> result = new ArrayList<WB_Line>(4);
		final WB_Point w = WB_Point.sub(C1.getCenter(), C0.getCenter());
		final double wlensqr = w.getSqLength();
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
			final double discr = Math
					.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			double s, oms, a;
			s = -0.5 * (c1 + discr) * invc2;
			if (s >= 0.5) {
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r0sqr / (s * s)));
			} else {
				oms = 1.0 - s;
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r1sqr / (oms * oms)));
			}
			WB_Point[] dir = getDirectionsOfLinesTangentToCircle2D(w, a);
			WB_Point org = new WB_Point(C0.getCenter().xd() + s * w.xd(),
					C0.getCenter().yd() + s * w.yd());
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
			org = new WB_Point(C0.getCenter().xd() + s * w.xd(),
					C0.getCenter().yd() + s * w.yd());
			result.add(new WB_Line(org, dir[0]));
			result.add(new WB_Line(org, dir[1]));
		} else {
			final WB_Point mid = WB_Point.add(C0.getCenter(), C1.getCenter())
					.mulSelf(0.5);
			final double a = Math.sqrt(WB_Math
					.fastAbs(wlensqr - 4 * C0.getRadius() * C0.getRadius()));
			final WB_Point[] dir = getDirectionsOfLinesTangentToCircle2D(w, a);
			result.add(new WB_Line(mid, dir[0]));
			result.add(new WB_Line(mid, dir[1]));
			final double invwlen = 1.0 / Math.sqrt(wlensqr);
			w.mulSelf(invwlen);
			result.add(
					new WB_Line(new WB_Point(mid.xd() + C0.getRadius() * w.yd(),
							mid.yd() - C0.getRadius() * w.xd()), w));
			result.add(
					new WB_Line(new WB_Point(mid.xd() - C0.getRadius() * w.yd(),
							mid.yd() + C0.getRadius() * w.xd()), w));
		}
		return result;
	}

	private static final WB_Point[] getDirectionsOfLinesTangentToCircle2D(
			final WB_Coord w, final double a) {
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
	public static final WB_Line getPerpendicularLineThroughPoint2D(
			final WB_Line L, final WB_Coord p) {
		return new WB_Line(p,
				new WB_Point(-L.getDirection().yd(), L.getDirection().xd()));
	}

	/**
	 *
	 *
	 * @param L
	 * @param p
	 * @return
	 */
	public static final WB_Line getParallelLineThroughPoint2D(final WB_Line L,
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
	public static final WB_Line getBisector2D(final WB_Coord p,
			final WB_Coord q) {
		return new WB_Line(gf.createInterpolatedPoint(p, q, 0.5),
				new WB_Point(p.yd() - q.yd(), q.xd() - p.xd()));
	}

	/**
	 *
	 *
	 * @param L
	 * @param d
	 * @return
	 */
	public static final WB_Line[] getParallelLines2D(final WB_Line L,
			final double d) {
		final WB_Line[] result = new WB_Line[2];
		result[0] = new WB_Line(
				new WB_Point(L.getOrigin().xd() - d * L.getDirection().yd(),
						L.getOrigin().yd() + d * L.getDirection().xd()),
				L.getDirection());
		result[1] = new WB_Line(
				new WB_Point(L.getOrigin().xd() + d * L.getDirection().yd(),
						L.getOrigin().yd() - d * L.getDirection().xd()),
				L.getDirection());
		return result;
	}

	/**
	 *
	 *
	 * @param L
	 * @param C
	 * @return
	 */
	public static final WB_Line[] getPerpendicularLinesTangentToCircle2D(
			final WB_Line L, final WB_Circle C) {
		final WB_Line[] result = new WB_Line[2];
		result[0] = new WB_Line(new WB_Point(
				C.getCenter().xd() + C.getRadius() * L.getDirection().xd(),
				C.getCenter().yd() + C.getRadius() * L.getDirection().yd()),
				new WB_Point(-L.getDirection().yd(), L.getDirection().xd()));
		result[1] = new WB_Line(new WB_Point(
				C.getCenter().xd() - C.getRadius() * L.getDirection().xd(),
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
	public static final WB_Sphere getBoundingSphere(
			final Collection<? extends WB_Coord> points) {
		WB_Point center = new WB_Point(points.iterator().next());
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
	public static final double getArea2D(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3) {
		return WB_Math.fastAbs(getSignedArea2D(p1, p2, p3));
	}

	public static final double getSimpleArea2D(final WB_Polygon poly) {
		int n = poly.getNumberOfShellPoints();
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
	 * Check if points p1 and p2 lie on same side of line A-B.
	 *
	 * @param p1
	 * @param p2
	 * @param A
	 * @param B
	 * @return true, false
	 */
	public static final boolean sameSide2D(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord A, final WB_Coord B) {
		final double pside = Math.signum(WB_Predicates.orient2D(A, B, p1));
		final double qside = Math.signum(WB_Predicates.orient2D(A, B, p2));
		if (pside == 0 || qside == 0 || pside == qside) {
			return true;
		}
		return false;
	}

	public static final boolean pointInTriangle2D(final WB_Coord p,
			final WB_Coord A, final WB_Coord B, final WB_Coord C) {
		if (WB_Epsilon
				.isZeroSq(WB_GeometryOp.getSqDistanceToLine2D(A, B, C))) {
			return false;
		}
		if (sameSide2D(p, A, B, C) && sameSide2D(p, B, A, C)
				&& sameSide2D(p, C, A, B)) {
			return true;
		}
		return false;
	}

	public static final boolean pointInTriangle2D(final WB_Coord p,
			final WB_Triangle T) {
		return pointInTriangle2D(p, T.p1, T.p2, T.p3);
	}

	/**
	 * Check if point p lies in triangle A-B-C using barycentric coordinates.
	 *
	 * @param p
	 *            the p
	 * @param A
	 *            the a
	 * @param B
	 *            the b
	 * @param C
	 *            the c
	 * @return true, false
	 */
	public static final boolean pointInTriangleBary2D(final WB_Coord p,
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
		return u > WB_Epsilon.EPSILON && v > WB_Epsilon.EPSILON
				&& u + v < 1 - WB_Epsilon.EPSILON;
	}

	public static final boolean pointInTriangleBary2D(final WB_Coord p,
			final WB_Triangle T) {
		return pointInTriangleBary2D(p, T.p1, T.p2, T.p3);
	}

	/**
	 * Twice signed tri area2 d.
	 *
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @param p3
	 *            the p3
	 * @return the double
	 */
	public static final double twiceSignedTriArea2D(final WB_Coord p1,
			final WB_Coord p2, final WB_Coord p3) {
		return (p1.xd() - p3.xd()) * (p2.yd() - p3.yd())
				- (p1.yd() - p3.yd()) * (p2.xd() - p3.xd());
	}

	/**
	 * Twice signed tri area2 d.
	 *
	 * @param x1
	 *            the x1
	 * @param y1
	 *            the y1
	 * @param x2
	 *            the x2
	 * @param y2
	 *            the y2
	 * @param x3
	 *            the x3
	 * @param y3
	 *            the y3
	 * @return the double
	 */
	public static final double twiceSignedTriArea2D(final double x1,
			final double y1, final double x2, final double y2, final double x3,
			final double y3) {
		return (x1 - x2) * (y2 - y3) - (x2 - x3) * (y1 - y2);
	}

	public static final double getSignedArea2D(
			final List<? extends WB_Coord> coords, final int start,
			final int end) {
		double sum = 0;
		for (int i = start, j = end - 1; i < end; j = i, i++) {
			sum += (coords.get(j).xd() - coords.get(i).xd())
					* (coords.get(i).yd() + coords.get(j).yd());
		}
		return sum;
	}

	public static final double getSignedArea2D(final WB_Coord[] coords,
			final int start, final int end) {
		double sum = 0;
		for (int i = start, j = end - 1; i < end; j = i, i++) {
			sum += (coords[j].xd() - coords[i].xd())
					* (coords[i].yd() + coords[j].yd());
		}
		return sum;
	}

	public static final double getSignedArea2D(final WB_Coord p1,
			final WB_Coord p2, final WB_Coord p3) {
		double sum = (p3.xd() - p1.xd()) * (p1.yd() + p3.yd());
		sum += (p1.xd() - p2.xd()) * (p2.yd() + p1.yd());
		sum += (p2.xd() - p3.xd()) * (p3.yd() + p2.yd());
		return sum;
	}

	/**
	 * Project point to circle
	 *
	 * @param C
	 * @param v
	 * @return point projected to circle
	 */
	public static final WB_Coord projectToCircle2D(final WB_Circle C,
			final WB_Coord v) {
		final WB_Point vc = new WB_Point(v).sub(C.getCenter());
		final double er = vc.normalizeSelf();
		if (WB_Epsilon.isZero(er)) {
			return null;
		}
		return WB_Point.addMul(C.getCenter(), C.getRadius(), vc);
	}

	public static final WB_Circle growCircleByPoint2D(final WB_Circle C,
			final WB_Coord p) {
		final WB_Vector d = WB_Point.subToVector2D(p, C.getCenter());
		final double dist2 = d.getSqLength2D();
		double radius = C.getRadius();
		WB_Coord center = C.getCenter();
		if (dist2 > radius * radius) {
			final double dist = Math.sqrt(dist2);
			final double newRadius = (radius + dist) * 0.5;
			final double k = (newRadius - radius) / dist;
			radius = newRadius;
			return new WB_Circle(center.xd() + k * d.xd(),
					center.yd() + k * d.yd(), newRadius);
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
	public static final WB_Polygon trimConvexPolygon2D(WB_Polygon poly,
			final double d) {
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
			final WB_Polygon[] split = splitPolygon2D(poly,
					new WB_Line(origin, v));
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
	public static final WB_Polygon trimConvexPolygon2D(WB_Polygon poly,
			final double[] d) {
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
			final WB_Polygon[] split = splitPolygon2D(poly,
					new WB_Line(origin, v));
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
	public static final boolean isLeftStrict2D(final WB_Coord a,
			final WB_Coord b, final WB_Coord p) {
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
	public static final boolean isLeft2D(final WB_Coord a, final WB_Coord b,
			final WB_Coord p) {
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
	public static final boolean isRightStrict2D(final WB_Coord a,
			final WB_Coord b, final WB_Coord p) {
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
	public static final boolean isRight2D(final WB_Coord a, final WB_Coord b,
			final WB_Coord p) {
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
	public static final boolean isReflex2D(final WB_Coord p0, final WB_Coord p,
			final WB_Coord p1) {
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
	public static final WB_Coord getSegmentIntersection2D(final WB_Coord ap1,
			final WB_Coord ap2, final WB_Coord bp1, final WB_Coord bp2) {
		WB_Coord A = WB_Point.sub(ap2, ap1);
		WB_Coord B = WB_Point.sub(bp2, bp1);
		double BxA = WB_CoordOp2D.cross2D(B, A);
		if (Math.abs(BxA) <= WB_Epsilon.EPSILON) {
			return null;
		}
		double ambxA = WB_CoordOp2D.cross2D(WB_Point.sub(ap1, bp1), A);
		if (Math.abs(ambxA) <= WB_Epsilon.EPSILON) {
			return null;
		}
		double tb = ambxA / BxA;
		if (tb < 0.0 || tb > 1.0) {
			return null;
		}
		WB_Point ip = WB_Point.mul(B, tb).addSelf(bp1);
		double ta = WB_Point.sub(ip, ap1).dot(A) / WB_Point.dot(A, A);
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
	public static final boolean getLineIntersectionInto2D(final WB_Coord a1,
			final WB_Coord a2, final WB_Coord b1, final WB_Coord b2,
			final WB_MutableCoord p) {
		WB_Vector s1 = gf.createVectorFromTo2D(a2, a1);
		WB_Vector s2 = gf.createVectorFromTo2D(b2, b1);
		double det = WB_CoordOp2D.cross2D(s1, s2);
		if (Math.abs(det) <= WB_Epsilon.EPSILON) {
			return false;
		} else {
			det = 1.0 / det;
			double t2 = det * (WB_CoordOp2D.cross2D(a1, s1)
					- WB_CoordOp2D.cross2D(b1, s1));
			p.set(b1.xd() * (1.0 - t2) + b2.xd() * t2,
					b1.yd() * (1.0 - t2) + b2.yd() * t2);
			return true;
		}
	}

	public static final WB_Circle getBoundingCircleInCenter2D(
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
		return new WB_Circle(center, Math.sqrt(r));
	}

	public static final WB_Circle getBoundingCircleInCenter2D(
			final WB_Coord[] points) {
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
		return new WB_Circle(center, Math.sqrt(r));
	}

	public static final WB_Circle mergeCircles2D(final WB_Circle C1,
			final WB_Circle C2) {
		WB_Vector cenDiff = WB_Vector.sub(C2.getCenter(), C1.getCenter());
		double lenSqr = cenDiff.dot(cenDiff);
		double rDiff = C2.getRadius() - C1.getRadius();
		double rDiffSqr = rDiff * rDiff;
		if (rDiffSqr >= lenSqr) {
			return rDiff >= 0.0 ? C2 : C1;
		} else {
			double length = Math.sqrt(lenSqr);
			WB_Point center;
			if (length > 0) {
				double coeff = (length + rDiff) / (2.0 * length);
				center = WB_Point.addMul(C1.getCenter(), coeff, cenDiff);
			} else {
				center = new WB_Point(C1.getCenter());
			}
			double radius = 0.5 * (length + C1.getRadius() + C2.getRadius());
			return new WB_Circle(center, radius);
		}
	}
}
