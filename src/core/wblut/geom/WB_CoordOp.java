package wblut.geom;

import wblut.math.WB_Ease;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_CoordOp {

	public WB_CoordOp() {
		// TODO Auto-generated constructor stub
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

	// POINT-POINT
	
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

	// POINT-POINT
	
	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
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
		return WB_Epsilon.isZeroSq(WB_Vector.sub(o, p).crossSelf(WB_Vector.sub(o, q)).getSqLength());
	}

	public static boolean isOrthogonal(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(
				dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd()) / (getLength3D(v0) * getLength3D(v1)));
	}

	public static boolean isOrthogonal(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())
				/ (getLength3D(v0) * getLength3D(v1))) < epsilon;
	}

	public static boolean isOrthogonalNorm(final WB_Coord v0, final WB_Coord v1) {
		return WB_Epsilon.isZero(dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd()));
	}

	public static boolean isOrthogonalNorm(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())) < epsilon;
	}

	public static boolean isParallel(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())
				/ (getLength3D(v0) * getLength3D(v1))) < WB_Epsilon.EPSILON;
	}

	public static boolean isParallel(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())
				/ (getLength3D(v0) * getLength3D(v1))) < epsilon;
	}

	public static boolean isParallelNorm(final WB_Coord v0, final WB_Coord v1) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())) < WB_Epsilon.EPSILON;
	}

	public static boolean isParallelNorm(final WB_Coord v0, final WB_Coord v1, final double epsilon) {
		return Math.abs(1.0 - dot(v0.xd(), v0.yd(), v0.zd(), v1.xd(), v1.yd(), v1.zd())) < epsilon;
	}

	/**
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
	 * @param o
	 * @param p
	 * @return
	 */
	public static boolean isParallelX(final WB_Coord o, final WB_Coord p) {
		final double pm2 = p.xd() * p.xd() + p.yd() * p.yd() + p.zd() * p.zd();
		return WB_Vector.cross(o, p).getSqLength() / (pm2 * WB_Vector.getSqLength3D(o)) < WB_Epsilon.SQEPSILON;
	}

	/**
	 *
	 * @param o
	 * @param p
	 * @param t
	 * @return
	 */
	public static boolean isParallelX(final WB_Coord o, final WB_Coord p, final double t) {
		final double pm2 = p.xd() * p.xd() + p.yd() * p.yd() + p.zd() * p.zd();
		return WB_Vector.cross(o, p).getSqLength() / (pm2 * WB_Vector.getSqLength3D(o)) < t + WB_Epsilon.SQEPSILON;
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

}
