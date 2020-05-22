package wblut.geom;

import lombok.ToString;
import wblut.core.WB_HashCode;
import wblut.math.WB_Ease;
import wblut.math.WB_Epsilon;
import wblut.math.WB_M33;
import wblut.math.WB_Math;

@ToString(callSuper = true, includeFieldNames = true)
public class WB_Vector extends WB_MutableCoordinate
		implements WB_MutableCoord, WB_MutableCoordMath, WB_Transformable3D {
	/**  */
	private static final WB_Coord X = new WB_MutableCoordinate(1, 0, 0);
	/**  */
	private static final WB_Coord Y = new WB_MutableCoordinate(0, 1, 0);
	/**  */
	private static final WB_Coord Z = new WB_MutableCoordinate(0, 0, 1);
	/**  */
	private static final WB_Coord ORIGIN = new WB_MutableCoordinate(0, 0, 0);
	/**  */
	private static final WB_Coord ZERO = new WB_MutableCoordinate(0, 0, 0);

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Coord X() {
		return X;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Coord Y() {
		return Y;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Coord Z() {
		return Z;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Coord ZERO() {
		return ZERO;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Coord ORIGIN() {
		return ORIGIN;
	}

	/**
	 *
	 */
	public WB_Vector() {
		super();
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public WB_Vector(final double x, final double y) {
		super(x, y);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public WB_Vector(final double x, final double y, final double z) {
		super(x, y, z);
	}

	/**
	 *
	 *
	 * @param x
	 */
	public WB_Vector(final double[] x) {
		super(x);
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_Vector(final double[] fromPoint, final double[] toPoint) {
		super(fromPoint, toPoint);
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_Vector(final WB_Coord v) {
		super(v);
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_Vector(final WB_Coord fromPoint, final WB_Coord toPoint) {
		super(fromPoint, toPoint);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector add(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector(q.xd() + p.xd(), q.yd() + p.yd(), q.zd() + p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector sub(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector(p.xd() - q.xd(), p.yd() - q.yd(), p.zd() - q.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector subToVector2D(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector(p.xd() - q.xd(), p.yd() - q.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector subToVector3D(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector(p.xd() - q.xd(), p.yd() - q.yd(), p.zd() - q.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param x
	 * @param y
	 * @return
	 */
	public static WB_Vector subToVector2D(final WB_Coord p, final double x, final double y) {
		return new WB_Vector(p.xd() - x, p.yd() - y);
	}

	/**
	 *
	 *
	 * @param p
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static WB_Vector subToVector3D(final WB_Coord p, final double x, final double y, final double z) {
		return new WB_Vector(p.xd() - x, p.yd() - y, p.zd() - z);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Vector mul(final WB_Coord p, final double f) {
		return new WB_Vector(p.xd() * f, p.yd() * f, p.zd() * f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Vector div(final WB_Coord p, final double f) {
		return WB_Vector.mul(p, 1.0 / f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @param q
	 * @return
	 */
	public static WB_Vector addMul(final WB_Coord p, final double f, final WB_Coord q) {
		return new WB_Vector(p.xd() + f * q.xd(), p.yd() + f * q.yd(), p.zd() + f * q.zd());
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @param g
	 * @param q
	 * @return
	 */
	public static WB_Vector mulAddMul(final double f, final WB_Coord p, final double g, final WB_Coord q) {
		return new WB_Vector(f * p.xd() + g * q.xd(), f * p.yd() + g * q.yd(), f * p.zd() + g * q.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double dot2D(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.dot2D(p.xd(), p.yd(), q.xd(), q.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double absDot2D(final WB_Coord p, final WB_Coord q) {
		return WB_Math.fastAbs(WB_GeometryOp.dot2D(p.xd(), p.yd(), q.xd(), q.yd()));
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double dot(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.dot(p.xd(), p.yd(), p.zd(), q.xd(), q.yd(), q.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double absDot(final WB_Coord p, final WB_Coord q) {
		return WB_Math.fastAbs(WB_GeometryOp.dot(p.xd(), p.yd(), p.zd(), q.xd(), q.yd(), q.zd()));
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector cross(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector(p.yd() * q.zd() - p.zd() * q.yd(), p.zd() * q.xd() - p.xd() * q.zd(),
				p.xd() * q.yd() - p.yd() * q.xd());
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_M33 tensor(final WB_Coord u, final WB_Coord v) {
		return new WB_M33(WB_GeometryOp.tensor3D(u.xd(), u.yd(), u.zd(), v.xd(), v.yd(), v.zd()));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	public static double scalarTriple(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		return WB_GeometryOp.scalarTriple(u.xd(), u.yd(), u.zd(), v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getDistance2D(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getDistance2D(q.xd(), q.yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getDistance3D(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getDistance3D(q.xd(), q.yd(), q.zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getDistance(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getDistance3D(q.xd(), q.yd(), q.zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getSqDistance2D(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getSqDistance2D(q.xd(), q.yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getSqDistance3D(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getSqDistance3D(q.xd(), q.yd(), q.zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getSqDistance(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getSqDistance3D(q.xd(), q.yd(), q.zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getLength2D(final WB_Coord p) {
		return WB_GeometryOp.getLength2D(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getLength3D(final WB_Coord p) {
		return WB_GeometryOp.getLength3D(p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getLength(final WB_Coord p) {
		return WB_GeometryOp.getLength3D(p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static double getSqLength2D(final WB_Coord v) {
		return WB_GeometryOp.getSqLength2D(v.xd(), v.yd());
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static double getSqLength3D(final WB_Coord v) {
		return WB_GeometryOp.getSqLength3D(v.xd(), v.yd(), v.zd());
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static double getSqLength(final WB_Coord v) {
		return WB_GeometryOp.getSqLength3D(v.xd(), v.yd(), v.zd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getAngle(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getAngleBetween(q.xd(), q.yd(), q.zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getAngleNorm(final WB_Coord q, final WB_Coord p) {
		return WB_GeometryOp.getAngleBetweenNorm(q.xd(), q.yd(), q.zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getHeading2D(final WB_Coord p) {
		return Math.atan2(p.yd(), p.xd());
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
		return WB_GeometryOp.isCollinear(o, p, q);
	}

	/**
	 *
	 *
	 * @param o
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isCollinear2D(final WB_Coord o, final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isCollinear2D(o, p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isParallel(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isParallel(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isParallel(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isParallel(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isParallelNorm(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isParallelNorm(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isParallelNorm(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isParallelNorm(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isParallel2D(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isParallel2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isParallel2D(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isParallel2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isParallelNorm2D(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isParallelNorm2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isParallelNorm2D(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isParallelNorm2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isOrthogonal(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isOrthogonal(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isOrthogonal(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isOrthogonal(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isOrthogonalNorm(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isOrthogonalNorm(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isOrthogonalNorm(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isOrthogonalNorm(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isOrthogonal2D(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isOrthogonal2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isOrthogonal2D(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isOrthogonal2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isOrthogonalNorm2D(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isOrthogonalNorm2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isOrthogonalNorm2D(final WB_Coord p, final WB_Coord q, final double t) {
		return WB_GeometryOp.isOrthogonalNorm2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Vector getOrthoNormal2D(final WB_Coord p) {
		final WB_Vector a = new WB_Vector(-p.yd(), p.xd(), 0);
		a.normalizeSelf();
		return a;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Vector getOrthoNormal3D(final WB_Coord p) {
		if (Math.abs(p.zd()) > WB_Epsilon.EPSILON) {
			final WB_Vector a = new WB_Vector(1, 0, -p.xd() / p.zd());
			a.normalizeSelf();
			return a;
		} else {
			return new WB_Vector(0, 0, 1);
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Vector getOrthoNormal(final WB_Coord p) {
		if (Math.abs(p.zd()) > WB_Epsilon.EPSILON) {
			final WB_Vector a = new WB_Vector(1, 0, -p.xd() / p.zd());
			a.normalizeSelf();
			return a;
		} else {
			return new WB_Vector(0, 0, 1);
		}
	}

	/**
	 *
	 *
	 * @param v
	 * @param w
	 * @param f
	 * @return
	 */
	public static WB_Vector interpolate(final WB_Coord v, final WB_Coord w, final double f) {
		return new WB_Vector(WB_GeometryOp.interpolate(v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd(), f));
	}

	/**
	 *
	 *
	 * @param v
	 * @param w
	 * @param f
	 * @param ease
	 * @param type
	 * @return
	 */
	public static WB_Vector interpolateEase(final WB_Coord v, final WB_Coord w, final double f, final WB_Ease ease,
			final WB_Ease.EaseType type) {
		return new WB_Vector(
				WB_GeometryOp.interpolateEase(v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd(), f, ease, type));
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector add(final double... x) {
		if (x.length == 3) {
			return new WB_Vector(this.xd() + x[0], this.yd() + x[1], this.zd() + x[2]);
		} else if (x.length == 2) {
			return new WB_Vector(this.xd() + x[0], this.yd() + x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector add(final WB_Coord p) {
		return new WB_Vector(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector sub(final double... x) {
		if (x.length == 3) {
			return new WB_Vector(this.xd() - x[0], this.yd() - x[1], this.zd() - x[2]);
		} else if (x.length == 2) {
			return new WB_Vector(this.xd() - x[0], this.yd() - x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector sub(final WB_Coord p) {
		return new WB_Vector(this.xd() - p.xd(), this.yd() - p.yd(), this.zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector mul(final double f) {
		return new WB_Vector(xd() * f, yd() * f, zd() * f);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector div(final double f) {
		return mul(1.0 / f);
	}

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector addMul(final double f, final double... x) {
		if (x.length == 3) {
			return new WB_Vector(this.xd() + f * x[0], this.yd() + f * x[1], this.zd() + f * x[2]);
		} else if (x.length == 2) {
			return new WB_Vector(this.xd() + f * x[0], this.yd() + f * x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector addMul(final double f, final WB_Coord p) {
		return new WB_Vector(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd());
	}

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector mulAddMul(final double f, final double g, final WB_Coord p) {
		return new WB_Vector(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd());
	}

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector mulAddMul(final double f, final double g, final double... x) {
		if (x.length == 3) {
			return new WB_Vector(f * this.xd() + g * x[0], f * this.yd() + g * x[1], f * this.zd() + g * x[2]);
		} else if (x.length == 2) {
			return new WB_Vector(f * this.xd() + g * x[0], f * this.yd() + g * x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double dot2D(final WB_Coord p) {
		return WB_GeometryOp.dot2D(xd(), yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double absDot2D(final WB_Coord p) {
		return WB_Math.fastAbs(WB_GeometryOp.dot2D(xd(), yd(), p.xd(), p.yd()));
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double dot(final WB_Coord p) {
		return WB_GeometryOp.dot(xd(), yd(), zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double absDot(final WB_Coord p) {
		return WB_Math.fastAbs(WB_GeometryOp.dot(xd(), yd(), zd(), p.xd(), p.yd(), p.zd()));
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector cross(final WB_Coord p) {
		return new WB_Vector(yd() * p.zd() - zd() * p.yd(), zd() * p.xd() - xd() * p.zd(),
				xd() * p.yd() - yd() * p.xd());
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_M33 tensor(final WB_Coord v) {
		return new WB_M33(WB_GeometryOp.tensor3D(xd(), yd(), zd(), v.xd(), v.yd(), v.zd()));
	}

	/**
	 *
	 *
	 * @param v
	 * @param w
	 * @return
	 */
	@Override
	public double scalarTriple(final WB_Coord v, final WB_Coord w) {
		return WB_GeometryOp.scalarTriple(xd(), yd(), zd(), v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector addSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector subSelf(final double... x) {
		if (x.length == 3) {
			set(xd() - x[0], yd() - x[1], zd() - x[2]);
			return this;
		} else if (x.length == 2) {
			set(xd() - x[0], yd() - x[1], zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Vector subSelf(final WB_Coord v) {
		set(xd() - v.xd(), yd() - v.yd(), zd() - v.zd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector mulSelf(final double f) {
		set(f * xd(), f * yd(), f * zd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector divSelf(final double f) {
		return mulSelf(1.0 / f);
	}

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector addMulSelf(final double f, final double... x) {
		if (x.length == 3) {
			set(xd() + f * x[0], yd() + f * x[1], zd() + f * x[2]);
			return this;
		} else if (x.length == 2) {
			set(xd() + f * x[0], yd() + f * x[1], zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector addMulSelf(final double f, final WB_Coord p) {
		set(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd());
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector addSelf(final double... x) {
		if (x.length == 3) {
			set(xd() + x[0], yd() + x[1], zd() + x[2]);
			return this;
		} else if (x.length == 2) {
			set(xd() + x[0], yd() + x[1], zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Vector addSelf(final double x, final double y, final double z) {
		set(xd() + x, yd() + y, zd() + z);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return
	 */
	@Override
	public WB_Vector mulAddMulSelf(final double f, final double g, final double... x) {
		if (x.length == 3) {
			set(f * this.xd() + g * x[0], f * this.yd() + g * x[1], f * this.zd() + g * x[2]);
			return this;
		} else if (x.length == 2) {
			set(f * this.xd() + g * x[0], f * this.yd() + g * x[1], this.zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector mulAddMulSelf(final double f, final double g, final WB_Coord p) {
		set(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd());
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector crossSelf(final WB_Coord p) {
		set(yd() * p.zd() - this.zd() * p.yd(), this.zd() * p.xd() - this.xd() * p.zd(),
				this.xd() * p.yd() - yd() * p.xd());
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double normalizeSelf() {
		final double d = getLength();
		if (WB_Epsilon.isZero(d)) {
			set(0, 0, 0);
		} else {
			set(xd() / d, yd() / d, zd() / d);
		}
		return d;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	@Override
	public WB_Vector trimSelf(final double d) {
		if (getSqLength() > d * d) {
			normalizeSelf();
			mulSelf(d);
		}
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector apply2D(final WB_Transform2D T) {
		return T.applyAsVector2D(this);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point applyAsPoint2D(final WB_Transform2D T) {
		return T.applyAsPoint2D(this);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsVector2D(final WB_Transform2D T) {
		return T.applyAsVector2D(this);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsNormal2D(final WB_Transform2D T) {
		return T.applyAsNormal2D(this);
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	public WB_Vector translate2D(final double px, final double py) {
		return new WB_Vector(this.xd() + px, this.yd() + py);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector translate2D(final WB_Coord p) {
		return new WB_Vector(this.xd() + p.xd(), this.yd() + p.yd());
	}

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutPoint2D(final double angle, final double px, final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		final WB_Vector result = new WB_Vector(this);
		raa.applyAsVector2DSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutPoint2D(final double angle, final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		final WB_Vector result = new WB_Vector(this);
		raa.applyAsVector2DSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutOrigin2D(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		final WB_Vector result = new WB_Vector(this);
		raa.applyAsVector2DSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector scale2D(final double f) {
		return mul(f);
	}

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @return
	 */
	@Override
	public WB_Vector scale2D(final double fx, final double fy) {
		return new WB_Vector(xd() * fx, yd() * fy);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector apply2DSelf(final WB_Transform2D T) {
		T.applyAsVector2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsPoint2DSelf(final WB_Transform2D T) {
		T.applyAsPoint2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsVector2DSelf(final WB_Transform2D T) {
		T.applyAsVector2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsNormal2DSelf(final WB_Transform2D T) {
		T.applyAsNormal2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	public WB_Vector translate2DSelf(final double px, final double py) {
		set(xd() + px, yd() + py, 0);
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector translate2DSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), 0);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutPoint2DSelf(final double angle, final double px, final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		raa.applyAsVector2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutPoint2DSelf(final double angle, final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		raa.applyAsVector2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutOrigin2DSelf(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		raa.applyAsVector2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector scale2DSelf(final double f) {
		mulSelf(f);
		return this;
	}

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @return
	 */
	@Override
	public WB_Vector scale2DSelf(final double fx, final double fy) {
		set(xd() * fx, yd() * fy);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector apply(final WB_Transform3D T) {
		return T.applyAsVector(this);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point applyAsPoint(final WB_Transform3D T) {
		return T.applyAsPoint(this);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsNormal(final WB_Transform3D T) {
		return T.applyAsNormal(this);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsVector(final WB_Transform3D T) {
		return T.applyAsVector(this);
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @return
	 */
	@Override
	public WB_Vector translate(final double px, final double py, final double pz) {
		return new WB_Vector(this.xd() + px, this.yd() + py, this.zd() + pz);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector translate(final WB_Coord p) {
		return new WB_Vector(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @param p2x
	 * @param p2y
	 * @param p2z
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxis2P(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z) {
		final WB_Vector result = new WB_Vector(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(p1x, p1y, p1z), new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		raa.applyAsVectorSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p1
	 * @param p2
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxis2P(final double angle, final WB_Coord p1, final WB_Coord p2) {
		final WB_Vector result = new WB_Vector(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		raa.applyAsVectorSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @param pz
	 * @param ax
	 * @param ay
	 * @param az
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxis(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az) {
		final WB_Vector result = new WB_Vector(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(px, py, pz), new WB_Vector(ax, ay, az));
		raa.applyAsVectorSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @param a
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord a) {
		final WB_Vector result = new WB_Vector(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		raa.applyAsVectorSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutOrigin(final double angle, final double x, final double y, final double z) {
		final WB_Vector result = new WB_Vector(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(x, y, z));
		raa.applyAsVectorSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param a
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutOrigin(final double angle, final WB_Coord a) {
		final WB_Vector result = new WB_Vector(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		raa.applyAsVectorSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector scale(final double f) {
		return mul(f);
	}

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @param fz
	 * @return
	 */
	@Override
	public WB_Vector scale(final double fx, final double fy, final double fz) {
		return new WB_Vector(xd() * fx, yd() * fy, zd() * fz);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applySelf(final WB_Transform3D T) {
		return applyAsVectorSelf(T);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsPointSelf(final WB_Transform3D T) {
		T.applyAsPointSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsVectorSelf(final WB_Transform3D T) {
		T.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Vector applyAsNormalSelf(final WB_Transform3D T) {
		T.applyAsNormalSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @return
	 */
	@Override
	public WB_Vector translateSelf(final double px, final double py, final double pz) {
		set(xd() + px, yd() + py, zd() + pz);
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector translateSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @param p2x
	 * @param p2y
	 * @param p2z
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxis2PSelf(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(p1x, p1y, p1z), new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		raa.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p1
	 * @param p2
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxis2PSelf(final double angle, final WB_Coord p1, final WB_Coord p2) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		raa.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @param a
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxisSelf(final double angle, final WB_Coord p, final WB_Coord a) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		raa.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @param pz
	 * @param ax
	 * @param ay
	 * @param az
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutAxisSelf(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(px, py, pz), new WB_Vector(ax, ay, az));
		raa.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutOriginSelf(final double angle, final double x, final double y, final double z) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(x, y, z));
		raa.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @param a
	 * @return
	 */
	@Override
	public WB_Vector rotateAboutOriginSelf(final double angle, final WB_Coord a) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		raa.applyAsVectorSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Vector scaleSelf(final double f) {
		mulSelf(f);
		return this;
	}

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @param fz
	 * @return
	 */
	@Override
	public WB_Vector scaleSelf(final double fx, final double fy, final double fz) {
		set(xd() * fx, yd() * fy, zd() * fz);
		return this;
	}

	/**
	 *
	 */
	public void invert() {
		mulSelf(-1);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double[] coords() {
		return new double[] { xd(), yd(), zd() };
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector copy() {
		return new WB_Vector(xd(), yd(), zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getDistance2D(final WB_Coord p) {
		return WB_GeometryOp.getDistance2D(xd(), yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getSqDistance2D(final WB_Coord p) {
		return WB_GeometryOp.getSqDistance2D(xd(), yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLength2D() {
		return WB_GeometryOp.getLength2D(xd(), yd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getSqLength2D() {
		return WB_GeometryOp.getSqLength2D(xd(), yd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getHeading2D() {
		return Math.atan2(yd(), xd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector getOrthoNormal2D() {
		final WB_Vector a = new WB_Vector(-yd(), xd(), 0);
		a.normalizeSelf();
		return a;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	@Override
	public boolean isCollinear2D(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isCollinear2D(this, p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isParallel2D(final WB_Coord p) {
		return WB_GeometryOp.isParallel2D(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isParallel2D(final WB_Coord p, final double t) {
		return WB_GeometryOp.isParallel2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isParallelNorm2D(final WB_Coord p) {
		return WB_GeometryOp.isParallelNorm2D(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isParallelNorm2D(final WB_Coord p, final double t) {
		return WB_GeometryOp.isParallelNorm2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isOrthogonal2D(final WB_Coord p) {
		return WB_GeometryOp.isOrthogonal2D(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isOrthogonal2D(final WB_Coord p, final double t) {
		return WB_GeometryOp.isOrthogonal2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isOrthogonalNorm2D(final WB_Coord p) {
		return WB_GeometryOp.isOrthogonalNorm2D(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isOrthogonalNorm2D(final WB_Coord p, final double t) {
		return WB_GeometryOp.isOrthogonalNorm2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getDistance3D(final WB_Coord p) {
		return getDistance(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getDistance(final WB_Coord p) {
		return WB_GeometryOp.getDistance3D(xd(), yd(), zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getSqDistance3D(final WB_Coord p) {
		return getSqDistance(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getSqDistance(final WB_Coord p) {
		return WB_GeometryOp.getSqDistance3D(xd(), yd(), zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLength3D() {
		return getLength();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLength() {
		return WB_GeometryOp.getLength3D(xd(), yd(), zd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Deprecated
	@Override
	public double getSqLength() {
		return getSqLength();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getSqLength3D() {
		return WB_GeometryOp.getSqLength3D(xd(), yd(), zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getAngle(final WB_Coord p) {
		return WB_GeometryOp.getAngleBetween(xd(), yd(), zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public double getAngleNorm(final WB_Coord p) {
		return WB_GeometryOp.getAngleBetweenNorm(xd(), yd(), zd(), p.xd(), p.yd(), p.zd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector getOrthoNormal3D() {
		return getOrthoNormal();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector getOrthoNormal() {
		if (Math.abs(zd()) > WB_Epsilon.EPSILON) {
			final WB_Vector a = new WB_Vector(1, 0, -xd() / zd());
			a.normalizeSelf();
			return a;
		} else {
			return new WB_Vector(0, 0, 1);
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	@Override
	public boolean isCollinear(final WB_Coord p, final WB_Coord q) {
		return WB_GeometryOp.isCollinear(this, p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isParallel(final WB_Coord p) {
		return WB_GeometryOp.isParallel(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isParallel(final WB_Coord p, final double t) {
		return WB_GeometryOp.isParallel(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isParallelNorm(final WB_Coord p) {
		return WB_GeometryOp.isParallelNorm(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isParallelNorm(final WB_Coord p, final double t) {
		return WB_GeometryOp.isParallelNorm(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isOrthogonal(final WB_Coord p) {
		return WB_GeometryOp.isOrthogonal(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isOrthogonal(final WB_Coord p, final double t) {
		return WB_GeometryOp.isOrthogonal(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isOrthogonalNorm(final WB_Coord p) {
		return WB_GeometryOp.isOrthogonalNorm(this, p);
	}

	/**
	 *
	 *
	 * @param p
	 * @param t
	 * @return
	 */
	@Override
	public boolean isOrthogonalNorm(final WB_Coord p, final double t) {
		return WB_GeometryOp.isOrthogonalNorm(this, p, t);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public boolean isZero() {
		return WB_GeometryOp.isZero3D(xd(), yd(), zd());
	}

	/**
	 *
	 *
	 * @param otherXYZ
	 * @return
	 */
	public boolean smallerThan(final WB_Coord otherXYZ) {
		int _tmp = WB_Epsilon.compare(xd(), otherXYZ.xd());
		if (_tmp != 0) {
			return _tmp < 0;
		}
		_tmp = WB_Epsilon.compare(yd(), otherXYZ.yd());
		if (_tmp != 0) {
			return _tmp < 0;
		}
		_tmp = WB_Epsilon.compare(zd(), otherXYZ.zd());
		return _tmp < 0;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public int compareTo(final WB_Coord p) {
		int cmp = Double.compare(xd(), p.xd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(yd(), p.yd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(zd(), p.zd());
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(wd(), p.wd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public int compareToY1st(final WB_Coord p) {
		int cmp = Double.compare(yd(), p.yd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(xd(), p.xd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(zd(), p.zd());
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(wd(), p.wd());
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Coord)) {
			return false;
		}
		final WB_Coord p = (WB_Coord) o;
		if (!WB_Epsilon.isEqual(xd(), p.xd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(yd(), p.yd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(zd(), p.zd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(wd(), p.wd())) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		return WB_HashCode.calculateHashCode(xd(), yd());
	}
}
