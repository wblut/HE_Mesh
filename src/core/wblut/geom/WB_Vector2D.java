/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import wblut.core.WB_HashCode;
import wblut.math.WB_Ease;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_Vector2D extends WB_MutableCoordinate2D
		implements WB_MutableCoord, WB_MutableCoordMath2D, WB_Geometry2D {
	private static final WB_Coord	X		= new WB_MutableCoordinate2D(1, 0);
	private static final WB_Coord	Y		= new WB_MutableCoordinate2D(0, 1);
	private static final WB_Coord	ORIGIN	= new WB_MutableCoordinate2D(0, 0);
	private static final WB_Coord	ZERO	= new WB_MutableCoordinate2D(0, 0);

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
	public WB_Vector2D() {
		super();
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public WB_Vector2D(final double x, final double y) {
		super(x, y);
	}

	/**
	 *
	 *
	 * @param x
	 */
	public WB_Vector2D(final double[] x) {
		super(x);
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_Vector2D(final double[] fromPoint, final double[] toPoint) {
		super(fromPoint, toPoint);
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_Vector2D(final WB_Coord v) {
		super(v);
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_Vector2D(final WB_Coord fromPoint, final WB_Coord toPoint) {
		super(fromPoint, toPoint);
	}

	public static WB_Vector2D add(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector2D(q.xd() + p.xd(), q.yd() + p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector2D sub(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector2D(p.xd() - q.xd(), p.yd() - q.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector2D subToVector2D(final WB_Coord p,
			final WB_Coord q) {
		return new WB_Vector2D(p.xd() - q.xd(), p.yd() - q.yd());
	}

	public static WB_Vector2D subToVector2D(final WB_Coord p, final double x,
			final double y) {
		return new WB_Vector2D(p.xd() - x, p.yd() - y);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Vector2D mul(final WB_Coord p, final double f) {
		return new WB_Vector2D(p.xd() * f, p.yd() * f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Vector2D div(final WB_Coord p, final double f) {
		return WB_Vector2D.mul(p, 1.0 / f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @param q
	 * @return
	 */
	public static WB_Vector2D addMul(final WB_Coord p, final double f,
			final WB_Coord q) {
		return new WB_Vector2D(p.xd() + f * q.xd(), p.yd() + f * q.yd());
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
	public static WB_Vector2D mulAddMul(final double f, final WB_Coord p,
			final double g, final WB_Coord q) {
		return new WB_Vector2D(f * p.xd() + g * q.xd(),
				f * p.yd() + g * q.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double dot2D(final WB_Coord p, final WB_Coord q) {
		return WB_CoordOp2D.dot2D(p.xd(), p.yd(), q.xd(), q.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static double absDot2D(final WB_Coord p, final WB_Coord q) {
		return WB_Math
				.fastAbs(WB_CoordOp2D.dot2D(p.xd(), p.yd(), q.xd(), q.yd()));
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getAngle(final WB_Coord q, final WB_Coord p) {
		return WB_CoordOp2D.getAngleBetween2D(q.xd(), q.yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getAngleNorm(final WB_Coord q, final WB_Coord p) {
		return WB_CoordOp2D.getAngleBetweenNorm2D(q.xd(), q.yd(), p.xd(),
				p.yd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getDistance2D(final WB_Coord q, final WB_Coord p) {
		return WB_CoordOp2D.getDistance2D(q.xd(), q.yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static double getLength2D(final WB_Coord p) {
		return WB_CoordOp2D.getLength2D(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param q
	 * @param p
	 * @return
	 */
	public static double getSqDistance2D(final WB_Coord q, final WB_Coord p) {
		return WB_CoordOp2D.getSqDistance2D(q.xd(), q.yd(), p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static double getSqLength2D(final WB_Coord v) {
		return WB_CoordOp2D.getSqLength2D(v.xd(), v.yd());
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
	public static boolean isCollinear2D(final WB_Coord o, final WB_Coord p,
			final WB_Coord q) {
		return WB_CoordOp2D.isCollinear2D(o, p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isParallel2D(final WB_Coord p, final WB_Coord q) {
		return WB_CoordOp2D.isParallel2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isParallel2D(final WB_Coord p, final WB_Coord q,
			final double t) {
		return WB_CoordOp2D.isParallel2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isParallelNorm2D(final WB_Coord p, final WB_Coord q) {
		return WB_CoordOp2D.isParallelNorm2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isParallelNorm2D(final WB_Coord p, final WB_Coord q,
			final double t) {
		return WB_CoordOp2D.isParallelNorm2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isOrthogonal2D(final WB_Coord p, final WB_Coord q) {
		return WB_CoordOp2D.isOrthogonal2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isOrthogonal2D(final WB_Coord p, final WB_Coord q,
			final double t) {
		return WB_CoordOp2D.isOrthogonal2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static boolean isOrthogonalNorm2D(final WB_Coord p,
			final WB_Coord q) {
		return WB_CoordOp2D.isOrthogonalNorm2D(p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param t
	 * @return
	 */
	public static boolean isOrthogonalNorm2D(final WB_Coord p, final WB_Coord q,
			final double t) {
		return WB_CoordOp2D.isOrthogonalNorm2D(p, q, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Vector2D getOrthoNormal2D(final WB_Coord p) {
		final WB_Vector2D a = new WB_Vector2D(-p.yd(), p.xd());
		a.normalizeSelf();
		return a;
	}

	public static WB_Vector2D interpolate(final WB_Coord v, final WB_Coord w,
			final double f) {
		return new WB_Vector2D(
				WB_CoordOp2D.interpolate2D(v.xd(), v.yd(), w.xd(), w.yd(), f));
	}

	public static WB_Vector2D interpolateEase(final WB_Coord v,
			final WB_Coord w, final double f, final WB_Ease ease,
			final WB_Ease.EaseType type) {
		return new WB_Vector2D(WB_CoordOp2D.interpolateEase2D(v.xd(), v.yd(),
				w.xd(), w.yd(), f, ease, type));
	}

	@Override
	public WB_Vector2D add(final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		return new WB_Vector2D(this.xd() + x[0], this.yd() + x[1]);
	}

	@Override
	public WB_Vector2D add(final WB_Coord p) {
		return new WB_Vector2D(xd() + p.xd(), yd() + p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#sub(double[])
	 */
	@Override
	public WB_Vector2D sub(final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		return new WB_Vector2D(this.xd() - x[0], this.yd() - x[1]);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#sub(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Vector2D sub(final WB_Coord p) {
		return new WB_Vector2D(this.xd() - p.xd(), this.yd() - p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#mul(double)
	 */
	@Override
	public WB_Vector2D mul(final double f) {
		return new WB_Vector2D(xd() * f, yd() * f);
	}

	@Override
	public WB_Vector2D div(final double f) {
		return mul(1.0 / f);
	}

	@Override
	public WB_Vector2D addMul(final double f, final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		return new WB_Vector2D(this.xd() + f * x[0], this.yd() + f * x[1]);
	}

	@Override
	public WB_Vector2D addMul(final double f, final WB_Coord p) {
		return new WB_Vector2D(xd() + f * p.xd(), yd() + f * p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#mulAddMul(double, double, double[])
	 */
	@Override
	public WB_Vector2D mulAddMul(final double f, final double g,
			final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		return new WB_Vector2D(f * this.xd() + g * x[0],
				f * this.yd() + g * x[1]);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#mulAddMul(double, double,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Vector2D mulAddMul(final double f, final double g,
			final WB_Coord p) {
		return new WB_Vector2D(f * xd() + g * p.xd(), f * yd() + g * p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#dot2D(wblut.geom.WB_Coordinate)
	 */
	@Override
	public double dot2D(final WB_Coord p) {
		return WB_CoordOp2D.dot2D(xd(), yd(), p.xd(), p.yd());
	}

	@Override
	public double absDot2D(final WB_Coord p) {
		return WB_Math.fastAbs(WB_CoordOp2D.dot2D(xd(), yd(), p.xd(), p.yd()));
	}

	@Override
	public WB_Vector2D addSelf(final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		set(xd() + x[0], yd() + x[1], zd());
		return this;
	}

	@Override
	public WB_Vector2D addSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd());
		return this;
	}

	@Override
	public WB_Vector2D subSelf(final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		set(xd() - x[0], yd() - x[1]);
		return this;
	}

	@Override
	public WB_Vector2D subSelf(final WB_Coord v) {
		set(xd() - v.xd(), yd() - v.yd());
		return this;
	}

	@Override
	public WB_Vector2D mulSelf(final double f) {
		set(f * xd(), f * yd(), f * zd());
		return this;
	}

	@Override
	public WB_Vector2D divSelf(final double f) {
		return mulSelf(1.0 / f);
	}

	@Override
	public WB_Vector2D addMulSelf(final double f, final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		set(xd() + f * x[0], yd() + f * x[1], zd());
		return this;
	}

	@Override
	public WB_Vector2D addMulSelf(final double f, final WB_Coord p) {
		set(xd() + f * p.xd(), yd() + f * p.yd());
		return this;
	}

	@Override
	public WB_Vector2D mulAddMulSelf(final double f, final double g,
			final double... x) {
		if (x.length < 2) {
			throw new IllegalArgumentException(
					"Array needs to be at least of length 2.");
		}
		set(f * this.xd() + g * x[0], f * this.yd() + g * x[1]);
		return this;
	}

	@Override
	public WB_Vector2D mulAddMulSelf(final double f, final double g,
			final WB_Coord p) {
		set(f * xd() + g * p.xd(), f * yd() + g * p.yd());
		return this;
	}

	@Override
	public double normalizeSelf() {
		final double d = getLength2D();
		if (WB_Epsilon.isZero(d)) {
			set(0, 0);
		} else {
			set(xd() / d, yd() / d);
		}
		return d;
	}

	@Override
	public WB_Vector2D trimSelf(final double d) {
		if (getSqLength2D() > d * d) {
			normalizeSelf();
			mulSelf(d);
		}
		return this;
	}

	@Override
	public WB_Vector2D apply2D(final WB_Transform2D T) {
		final WB_Vector2D v = new WB_Vector2D(this);
		return v.apply2DSelf(T);
	}

	@Override
	public WB_Point applyAsPoint2D(final WB_Transform2D T) {
		final WB_Point result = new WB_Point();
		T.applyAsPoint2DInto(this, result);
		return result;
	}

	@Override
	public WB_Vector2D applyAsVector2D(final WB_Transform2D T) {
		final WB_Vector2D result = new WB_Vector2D();
		T.applyAsVector2DInto(this, result);
		return result;
	}

	@Override
	public WB_Vector2D applyAsNormal2D(final WB_Transform2D T) {
		final WB_Vector2D result = new WB_Vector2D();
		T.applyAsNormal2DInto(this, result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform2D#translate2D(double, double)
	 */
	@Override
	public WB_Vector2D translate2D(final double px, final double py) {
		return new WB_Vector2D(this.xd() + px, this.yd() + py);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform2D#translate2D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Vector2D translate2D(final WB_Coord p) {
		return new WB_Vector2D(this.xd() + p.xd(), this.yd() + p.yd());
	}

	@Override
	public WB_Vector2D rotateAboutPoint2D(final double angle, final double px,
			final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		WB_Vector2D result = new WB_Vector2D(this);
		raa.applyAsVector2DSelf(result);
		return result;
	}

	@Override
	public WB_Vector2D rotateAboutPoint2D(final double angle,
			final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		WB_Vector2D result = new WB_Vector2D(this);
		raa.applyAsVector2DSelf(result);
		return result;
	}

	@Override
	public WB_Vector2D rotateAboutOrigin2D(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		WB_Vector2D result = new WB_Vector2D(this);
		raa.applyAsVector2DSelf(result);
		return result;
	}

	@Override
	public WB_Vector2D scale2D(final double f) {
		return mul(f);
	}

	@Override
	public WB_Vector2D scale2D(final double fx, final double fy) {
		return new WB_Vector2D(xd() * fx, yd() * fy);
	}

	@Override
	public WB_Vector2D apply2DSelf(final WB_Transform2D T) {
		return applyAsVector2DSelf(T);
	}

	@Override
	public WB_Vector2D applyAsPoint2DSelf(final WB_Transform2D T) {
		T.applyAsPoint2DSelf(this);
		return this;
	}

	@Override
	public WB_Vector2D applyAsVector2DSelf(final WB_Transform2D T) {
		T.applyAsVector2DSelf(this);
		return this;
	}

	@Override
	public WB_Vector2D applyAsNormal2DSelf(final WB_Transform2D T) {
		T.applyAsNormal2DSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform2D#translate2DSelf(double,
	 * double)
	 */
	@Override
	public WB_Vector2D translate2DSelf(final double px, final double py) {
		this.x += px;
		this.y += py;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform2D#translate2DSelf(wblut.geom.
	 * WB_Coord)
	 */
	@Override
	public WB_Vector2D translate2DSelf(final WB_Coord p) {
		this.x += p.xd();
		this.y += p.yd();
		return this;
	}

	@Override
	public WB_Vector2D rotateAboutPoint2DSelf(final double angle,
			final double px, final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		raa.applyAsVector2DSelf(this);
		return this;
	}

	@Override
	/**
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	public WB_Vector2D rotateAboutPoint2DSelf(final double angle,
			final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		raa.applyAsVector2DSelf(this);
		return this;
	}

	@Override
	public WB_Vector2D rotateAboutOrigin2DSelf(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		raa.applyAsVector2DSelf(this);
		return this;
	}

	@Override
	public WB_Vector2D scale2DSelf(final double f) {
		mulSelf(f);
		return this;
	}

	@Override
	public WB_Vector2D scale2DSelf(final double fx, final double fy) {
		set(xd() * fx, yd() * fy);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_CoordinateMetric#getDistance2D(wblut.geom.WB_Coordinate)
	 */
	@Override
	public double getDistance2D(final WB_Coord p) {
		return WB_CoordOp2D.getDistance2D(xd(), yd(), p.xd(), p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_CoordinateMetric#getSqDistance2D(wblut.geom.WB_Coordinate)
	 */
	@Override
	public double getSqDistance2D(final WB_Coord p) {
		return WB_CoordOp2D.getSqDistance2D(xd(), yd(), p.xd(), p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMetric#getLength2D()
	 */
	@Override
	public double getLength2D() {
		return WB_CoordOp2D.getLength2D(xd(), yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMetric#getSqLength2D()
	 */
	@Override
	public double getSqLength2D() {
		return WB_CoordOp2D.getSqLength2D(xd(), yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMetric#getAngle(wblut.geom.WB_Coordinate)
	 */
	@Override
	public double getAngle(final WB_Coord p) {
		return WB_CoordOp2D.getAngleBetween2D(xd(), yd(), p.xd(), p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_CoordinateMetric#getAngleNorm(wblut.geom.WB_Coordinate)
	 */
	@Override
	public double getAngleNorm(final WB_Coord p) {
		return WB_CoordOp2D.getAngleBetweenNorm2D(xd(), yd(), p.xd(), p.yd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMetric#heading2D()
	 */
	@Override
	public double getHeading2D() {
		return Math.atan2(yd(), xd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMetric#getOrthoNormal2D()
	 */
	@Override
	public WB_Vector2D getOrthoNormal2D() {
		final WB_Vector2D a = new WB_Vector2D(-yd(), xd());
		a.normalizeSelf();
		return a;
	}

	@Override
	public boolean isZero() {
		return WB_CoordOp2D.isZero2D(xd(), yd());
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
		return new double[] { xd(), yd() };
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector2D copy() {
		return new WB_Vector2D(xd(), yd());
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
		return WB_CoordOp2D.isCollinear2D(this, p, q);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isParallel2D(final WB_Coord p) {
		return WB_CoordOp2D.isParallel2D(this, p);
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
		return WB_CoordOp2D.isParallel2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isParallelNorm2D(final WB_Coord p) {
		return WB_CoordOp2D.isParallelNorm2D(this, p);
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
		return WB_CoordOp2D.isParallelNorm2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isOrthogonal2D(final WB_Coord p) {
		return WB_CoordOp2D.isOrthogonal2D(this, p);
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
		return WB_CoordOp2D.isOrthogonal2D(this, p, t);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public boolean isOrthogonalNorm2D(final WB_Coord p) {
		return WB_CoordOp2D.isOrthogonalNorm2D(this, p);
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
		return WB_CoordOp2D.isOrthogonalNorm2D(this, p, t);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return WB_HashCode.calculateHashCode(xd(), yd());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WB_Vector2D [x=" + xd() + ", y=" + yd() + "]";
	}
}
