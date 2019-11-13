package wblut.geom;

import wblut.math.WB_Ease;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_CoordOp2D {



	/**
	 *
	 * @param C1
	 * @param C2
	 * @return
	 */
	public static final boolean isTangent2D(final WB_Circle C1, final WB_Circle C2) {
		final double d = WB_Point.getDistance2D(C1.getCenter(), C2.getCenter());
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
		WB_Vector cross = v0.cross(v1);
		double sign = cross.dot(new WB_Vector(nx, ny));
	
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

	public static final double[] interpolate2D(final WB_Coord p, final WB_Coord q, final double t) {
		return new double[] { p.xd() + t * (q.xd() - p.xd()), p.yd() + t * (q.yd() - p.yd()) };
	}

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
	 * @param o
	 * @param p
	 * @param q
	 * @return
	 */
	public static final boolean isCollinear2D(final WB_Coord o, final WB_Coord p, final WB_Coord q) {
		if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToPoint2D(p, q))) {
			return true;
		}
		if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToPoint2D(o, q))) {
			return true;
		}
		if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToPoint2D(o, p))) {
			return true;
		}
		return WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistanceToLine2D(o, p, q));
	}

	public static final boolean isOrthogonal2D(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1)));
	}

	public static final boolean isOrthogonal2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1))) < epsilon;
	}

	public static final boolean isOrthogonalNorm2D(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()));
	}

	public static final boolean isOrthogonalNorm2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd())) < epsilon;
	}

	public static final boolean isParallel2D(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0
				- dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1))) < WB_Epsilon.EPSILON;
	}

	public static final boolean isParallel2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math
				.abs(1.0 - dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd()) / (getLength2D(v0) * getLength2D(v1))) < epsilon;
	}

	public static final boolean isParallelNorm2D(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0 - dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd())) < WB_Epsilon.EPSILON;
	}

	public static final boolean isParallelNorm2D(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(1.0 - dot2D(v0.xd(), v0.yd(), v1.xd(), v1.yd())) < epsilon;
	}

	/**
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
