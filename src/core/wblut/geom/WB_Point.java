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
import wblut.math.WB_M33;

/**
 *
 */
public class WB_Point extends WB_Vector implements WB_Geometry {
	private static final WB_Coord	X		= new WB_MutableCoordinate(1, 0, 0);
	private static final WB_Coord	Y		= new WB_MutableCoordinate(0, 1, 0);
	private static final WB_Coord	Z		= new WB_MutableCoordinate(0, 0, 1);
	private static final WB_Coord	ORIGIN	= new WB_MutableCoordinate(0, 0, 0);
	private static final WB_Coord	ZERO	= new WB_MutableCoordinate(0, 0, 0);

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
	public WB_Point() {
		super();
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public WB_Point(final double x, final double y) {
		super(x, y);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public WB_Point(final double x, final double y, final double z) {
		super(x, y, z);
	}

	/**
	 *
	 *
	 * @param x
	 */
	public WB_Point(final double[] x) {
		super(x);
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_Point(final double[] fromPoint, final double[] toPoint) {
		super(fromPoint, toPoint);
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_Point(final WB_Coord v) {
		super(v);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Vector subToVector3D(final double x, final double y,
			final double z) {
		return new WB_Vector(this.xd() - x, this.yd() - y, this.zd() - z);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Vector subToVector3D(final WB_Coord p) {
		return new WB_Vector(xd() - p.xd(), yd() - p.yd(), zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Vector subToVector2D(final double x, final double y,
			final double z) {
		return new WB_Vector(this.xd() - x, this.yd() - y, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Vector subToVector2D(final WB_Coord p) {
		return new WB_Vector(xd() - p.xd(), yd() - p.yd(), 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Point add(final WB_Coord p, final WB_Coord q) {
		return new WB_Point(q.xd() + p.xd(), q.yd() + p.yd(), q.zd() + p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Point sub(final WB_Coord p, final WB_Coord q) {
		return new WB_Point(p.xd() - q.xd(), p.yd() - q.yd(), p.zd() - q.zd());
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

	public static WB_Vector subToVector(final WB_Coord p, final WB_Coord q) {
		return new WB_Vector(p.xd() - q.xd(), p.yd() - q.yd(), p.zd() - q.zd());
	}

	public static WB_Vector subToVector2D(final WB_Coord p, final double x,
			final double y) {
		return new WB_Vector(p.xd() - x, p.yd() - y);
	}

	public static WB_Vector subToVector3D(final WB_Coord p, final double x,
			final double y, final double z) {
		return new WB_Vector(p.xd() - x, p.yd() - y, p.zd() - z);
	}

	public static WB_Vector subToVector(final WB_Coord p, final double x,
			final double y, final double z) {
		return new WB_Vector(p.xd() - x, p.yd() - y, p.zd() - z);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Point mul(final WB_Coord p, final double f) {
		return new WB_Point(p.xd() * f, p.yd() * f, p.zd() * f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Point div(final WB_Coord p, final double f) {
		return WB_Point.mul(p, 1.0 / f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @param q
	 * @return
	 */
	public static WB_Point addMul(final WB_Coord p, final double f,
			final WB_Coord q) {
		return new WB_Point(p.xd() + f * q.xd(), p.yd() + f * q.yd(),
				p.zd() + f * q.zd());
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
	public static WB_Point mulAddMul(final double f, final WB_Coord p,
			final double g, final WB_Coord q) {
		return new WB_Point(f * p.xd() + g * q.xd(), f * p.yd() + g * q.yd(),
				f * p.zd() + g * q.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Point cross(final WB_Coord p, final WB_Coord q) {
		return new WB_Point(p.yd() * q.zd() - p.zd() * q.yd(),
				p.zd() * q.xd() - p.xd() * q.zd(),
				p.xd() * q.yd() - p.yd() * q.xd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Point getOrthoNormal2D(final WB_Coord p) {
		final WB_Point a = new WB_Point(-p.yd(), p.xd(), 0);
		a.normalizeSelf();
		return a;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Point getOrthoNormal3D(final WB_Coord p) {
		if (Math.abs(p.zd()) > WB_Epsilon.EPSILON) {
			final WB_Point a = new WB_Point(1, 0, -p.xd() / p.zd());
			a.normalizeSelf();
			return a;
		} else {
			return new WB_Point(0, 0, 1);
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Point getOrthoNormal(final WB_Coord p) {
		if (Math.abs(p.zd()) > WB_Epsilon.EPSILON) {
			final WB_Point a = new WB_Point(1, 0, -p.xd() / p.zd());
			a.normalizeSelf();
			return a;
		} else {
			return new WB_Point(0, 0, 1);
		}
	}

	public static WB_Point interpolate(final WB_Coord v, final WB_Coord w,
			final double f) {
		return new WB_Point(WB_CoordOp.interpolate(v.xd(), v.yd(), v.zd(),
				w.xd(), w.yd(), w.zd(), f));
	}

	public static WB_Point interpolateEase(final WB_Coord v, final WB_Coord w,
			final double f, final WB_Ease ease, final WB_Ease.EaseType type) {
		return new WB_Point(WB_CoordOp.interpolateEase(v.xd(), v.yd(), v.zd(),
				w.xd(), w.yd(), w.zd(), f, ease, type));
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#add(double, double, double)
	 */
	@Override
	public WB_Point add(final double... x) {
		if (x.length == 3) {
			return new WB_Point(this.xd() + x[0], this.yd() + x[1],
					this.zd() + x[2]);
		} else if (x.length == 2) {
			return new WB_Point(this.xd() + x[0], this.yd() + x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#add(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point add(final WB_Coord p) {
		return new WB_Point(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#sub(double[])
	 */
	@Override
	public WB_Point sub(final double... x) {
		if (x.length == 3) {
			return new WB_Point(this.xd() - x[0], this.yd() - x[1],
					this.zd() - x[2]);
		} else if (x.length == 2) {
			return new WB_Point(this.xd() - x[0], this.yd() - x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#sub(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point sub(final WB_Coord p) {
		return new WB_Point(this.xd() - p.xd(), this.yd() - p.yd(),
				this.zd() - p.zd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#mul(double)
	 */
	@Override
	public WB_Point mul(final double f) {
		return new WB_Point(xd() * f, yd() * f, zd() * f);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#div(double)
	 */
	@Override
	public WB_Point div(final double f) {
		return mul(1.0 / f);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#addMul(double, double, double, double)
	 */
	@Override
	public WB_Point addMul(final double f, final double... x) {
		if (x.length == 3) {
			return new WB_Point(this.xd() + f * x[0], this.yd() + f * x[1],
					this.zd() + f * x[2]);
		} else if (x.length == 2) {
			return new WB_Point(this.xd() + f * x[0], this.yd() + f * x[1],
					this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#addMul(double,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point addMul(final double f, final WB_Coord p) {
		return new WB_Point(xd() + f * p.xd(), yd() + f * p.yd(),
				zd() + f * p.zd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#mulAddMul(double, double,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point mulAddMul(final double f, final double g,
			final WB_Coord p) {
		return new WB_Point(f * xd() + g * p.xd(), f * yd() + g * p.yd(),
				f * zd() + g * p.zd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#mulAddMul(double, double, double[])
	 */
	@Override
	public WB_Point mulAddMul(final double f, final double g,
			final double... x) {
		if (x.length == 3) {
			return new WB_Point(f * this.xd() + g * x[0],
					f * this.yd() + g * x[1], f * this.zd() + g * x[2]);
		} else if (x.length == 2) {
			return new WB_Point(f * this.xd() + g * x[0],
					f * this.yd() + g * x[1], this.zd());
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#cross(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point cross(final WB_Coord p) {
		return new WB_Point(yd() * p.zd() - zd() * p.yd(),
				zd() * p.xd() - xd() * p.zd(), xd() * p.yd() - yd() * p.xd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#tensor(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_M33 tensor(final WB_Coord v) {
		return new WB_M33(WB_CoordOp.tensor3D(xd(), yd(), zd(), v.xd(),
				v.yd(), v.zd()));
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateMath#scalarTriple(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public double scalarTriple(final WB_Coord v, final WB_Coord w) {
		return WB_CoordOp.scalarTriple(xd(), yd(), zd(), v.xd(), v.yd(),
				v.zd(), w.xd(), w.yd(), w.zd());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateMath#addSelf(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point addSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#subSelf(double, double, double)
	 */
	@Override
	public WB_Point subSelf(final double... x) {
		if (x.length == 3) {
			set(xd() - x[0], yd() - x[1], zd() - x[2]);
			return this;
		} else if (x.length == 2) {
			set(xd() - x[0], yd() - x[1], zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateMath#subSelf(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point subSelf(final WB_Coord v) {
		set(xd() - v.xd(), yd() - v.yd(), zd() - v.zd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#mulSelf(double)
	 */
	@Override
	public WB_Point mulSelf(final double f) {
		set(f * xd(), f * yd(), f * zd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#divSelf(double)
	 */
	@Override
	public WB_Point divSelf(final double f) {
		return mulSelf(1.0 / f);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#addMulSelf(double, double,
	 * double, double)
	 */
	@Override
	public WB_Point addMulSelf(final double f, final double... x) {
		if (x.length == 3) {
			set(xd() + f * x[0], yd() + f * x[1], zd() + f * x[2]);
			return this;
		} else if (x.length == 2) {
			set(xd() + f * x[0], yd() + f * x[1], zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#addMulSelf(double,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point addMulSelf(final double f, final WB_Coord p) {
		set(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#addSelf(double, double, double)
	 */
	@Override
	public WB_Point addSelf(final double... x) {
		if (x.length == 3) {
			set(xd() + x[0], yd() + x[1], zd() + x[2]);
			return this;
		} else if (x.length == 2) {
			set(xd() + x[0], yd() + x[1], zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	@Override
	public WB_Point addSelf(final double x, final double y, final double z) {
		set(xd() + x, yd() + y, zd() + z);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#mulAddMulSelf(double, double,
	 * double[])
	 */
	@Override
	public WB_Point mulAddMulSelf(final double f, final double g,
			final double... x) {
		if (x.length == 3) {
			set(f * this.xd() + g * x[0], f * this.yd() + g * x[1],
					f * this.zd() + g * x[2]);
			return this;
		} else if (x.length == 2) {
			set(f * this.xd() + g * x[0], f * this.yd() + g * x[1], this.zd());
			return this;
		}
		throw new IllegalArgumentException("Array should be length 2 or 3.");
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#mulAddMulSelf(double, double,
	 * wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point mulAddMulSelf(final double f, final double g,
			final WB_Coord p) {
		set(f * xd() + g * p.xd(), f * yd() + g * p.yd(),
				f * zd() + g * p.zd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateMath#crossSelf(wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point crossSelf(final WB_Coord p) {
		set(yd() * p.zd() - this.zd() * p.yd(),
				this.zd() * p.xd() - this.xd() * p.zd(),
				this.xd() * p.yd() - yd() * p.xd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#normalizeSelf()
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

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#trimSelf(double)
	 */
	@Override
	public WB_Point trimSelf(final double d) {
		if (getSqLength() > d * d) {
			normalizeSelf();
			mulSelf(d);
		}
		return this;
	}

	@Override
	public WB_Point apply2D(final WB_Transform2D T) {
		return T.applyAsPoint2D(this);
	}

	@Override
	public WB_Point applyAsPoint2D(final WB_Transform2D T) {
		return T.applyAsPoint2D(this);
	}

	@Override
	public WB_Point applyAsVector2D(final WB_Transform2D T) {
		return new WB_Point(T.applyAsVector2D(this));
	}

	@Override
	public WB_Point applyAsNormal2D(final WB_Transform2D T) {
		return new WB_Point(T.applyAsNormal2D(this));
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform2D#translate2D(double, double)
	 */
	@Override
	public WB_Point translate2D(final double px, final double py) {
		return new WB_Point(this.xd() + px, this.yd() + py);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform2D#translate2D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point translate2D(final WB_Coord p) {
		return new WB_Point(this.xd() + p.xd(), this.yd() + p.yd());
	}

	@Override
	public WB_Point rotateAboutPoint2D(final double angle, final double px,
			final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		WB_Point result = new WB_Point(this);
		raa.applyAsPoint2DSelf(result);
		return result;
	}

	@Override
	public WB_Point rotateAboutPoint2D(final double angle, final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		WB_Point result = new WB_Point(this);
		raa.applyAsPoint2DSelf(result);
		return result;
	}

	@Override
	public WB_Point rotateAboutOrigin2D(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		WB_Point result = new WB_Point(this);
		raa.applyAsPoint2DSelf(result);
		return result;
	}

	@Override
	public WB_Point scale2D(final double f) {
		return mul(f);
	}

	@Override
	public WB_Point scale2D(final double fx, final double fy) {
		return new WB_Point(xd() * fx, yd() * fy);
	}

	@Override
	public WB_Point apply2DSelf(final WB_Transform2D T) {
		T.applyAsPoint2DSelf(this);
		return this;
	}

	@Override
	public WB_Point applyAsPoint2DSelf(final WB_Transform2D T) {
		T.applyAsPoint2DSelf(this);
		return this;
	}

	@Override
	public WB_Point applyAsVector2DSelf(final WB_Transform2D T) {
		T.applyAsVector2DSelf(this);
		return this;
	}

	@Override
	public WB_Point applyAsNormal2DSelf(final WB_Transform2D T) {
		T.applyAsNormal2DSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform3D#translate2DSelf(double,
	 * double)
	 */
	@Override
	public WB_Point translate2DSelf(final double px, final double py) {
		set(xd() + px, yd() + py);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform3D#translate2DSelf(wblut.geom.
	 * WB_Coord)
	 */
	@Override
	public WB_Point translate2DSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd());
		return this;
	}

	@Override
	public WB_Point rotateAboutPoint2DSelf(final double angle, final double px,
			final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		raa.applyAsPoint2DSelf(this);
		return this;
	}

	@Override
	/**
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	public WB_Point rotateAboutPoint2DSelf(final double angle,
			final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		raa.applyAsPoint2DSelf(this);
		return this;
	}

	@Override
	public WB_Point rotateAboutOrigin2DSelf(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		raa.applyAsPoint2DSelf(this);
		return this;
	}

	@Override
	public WB_Point scale2DSelf(final double f) {
		mulSelf(f);
		return this;
	}

	@Override
	public WB_Point scale2DSelf(final double fx, final double fy) {
		set(xd() * fx, yd() * fy);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#apply(wblut.geom.WB_Transform)
	 */
	@Override
	public WB_Point apply(final WB_Transform3D T) {
		return T.applyAsPoint(this);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_CoordinateTransform#applyAsPoint(wblut.geom.WB_Transform)
	 */
	@Override
	public WB_Point applyAsPoint(final WB_Transform3D T) {
		return T.applyAsPoint(this);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_CoordinateTransform#applyAsNormal(wblut.geom.WB_Transform)
	 */
	@Override
	public WB_Point applyAsNormal(final WB_Transform3D T) {
		return new WB_Point(T.applyAsNormal(this));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_CoordinateTransform#applyAsVector(wblut.geom.WB_Transform)
	 */
	@Override
	public WB_Point applyAsVector(final WB_Transform3D T) {
		return new WB_Point(T.applyAsVector(this));
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform3D#translate(double, double)
	 */
	@Override
	public WB_Point translate(final double px, final double py,
			final double pz) {
		return new WB_Point(this.xd() + px, this.yd() + py, this.zd() + pz);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform3D#translate(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point translate(final WB_Coord p) {
		return new WB_Point(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#rotateAbout2PointAxis(double,
	 * double, double, double, double, double, double)
	 */
	@Override
	public WB_Point rotateAboutAxis2P(final double angle, final double p1x,
			final double p1y, final double p1z, final double p2x,
			final double p2y, final double p2z) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(p1x, p1y, p1z),
				new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		raa.applyAsPointSelf(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#rotateAbout2PointAxis(double,
	 * wblut.geom.WB_Coordinate, wblut.geom.WB_Coordinate)
	 */
	@Override
	/**
	 *
	 * @param angle
	 * @param p1
	 * @param p2
	 * @return
	 */
	public WB_Point rotateAboutAxis2P(final double angle, final WB_Coord p1,
			final WB_Coord p2) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		raa.applyAsPointSelf(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform#rotateAbout2PointAxisSelf(double
	 * , double, double, double, double, double, double)
	 */
	@Override
	public WB_Point rotateAboutAxis(final double angle, final double px,
			final double py, final double pz, final double ax, final double ay,
			final double az) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(px, py, pz),
				new WB_Vector(ax, ay, az));
		raa.applyAsPointSelf(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#rotateAboutAxis(double,
	 * wblut.geom.WB_Coordinate, wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point rotateAboutAxis(final double angle, final WB_Coord p,
			final WB_Coord a) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		raa.applyAsPointSelf(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform#rotateAbout2PointAxisSelf(double
	 * , double, double, double, double, double, double)
	 */
	@Override
	public WB_Point rotateAboutOrigin(final double angle, final double x,
			final double y, final double z) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(x, y, z));
		raa.applyAsPointSelf(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#rotateAboutAxis(double,
	 * wblut.geom.WB_Coordinate, wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point rotateAboutOrigin(final double angle, final WB_Coord a) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		raa.applyAsPointSelf(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#scale(double)
	 */
	@Override
	/**
	 *
	 * @param f
	 * @return
	 */
	public WB_Point scale(final double f) {
		return mul(f);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#scale(double, double, double)
	 */
	@Override
	/**
	 *
	 * @param fx
	 * @param fy
	 * @param fz
	 * @return
	 */
	public WB_Point scale(final double fx, final double fy, final double fz) {
		return new WB_Point(xd() * fx, yd() * fy, zd() * fz);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateMath#applySelf(wblut.geom.WB_Transform)
	 */
	@Override
	public WB_Point applySelf(final WB_Transform3D T) {
		return applyAsPointSelf(T);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#applyAsPointSelf(wblut.geom.
	 * WB_Transform )
	 */
	@Override
	public WB_Point applyAsPointSelf(final WB_Transform3D T) {
		T.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#applyAsVectorSelf(wblut.geom.
	 * WB_Transform )
	 */
	@Override
	public WB_Vector applyAsVectorSelf(final WB_Transform3D T) {
		T.applyAsVectorSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateMath#applyAsNormalSelf(wblut.geom.
	 * WB_Transform )
	 */
	@Override
	public WB_Vector applyAsNormalSelf(final WB_Transform3D T) {
		T.applyAsNormalSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform3D#translateSelf(double,
	 * double, double)
	 */
	@Override
	public WB_Point translateSelf(final double px, final double py,
			final double pz) {
		set(xd() + px, yd() + py, zd() + pz);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform3D#translateSelf(wblut.geom.
	 * WB_Coord)
	 */
	@Override
	public WB_Point translateSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform#rotateAbout2PointAxisSelf(double
	 * , double, double, double, double, double, double)
	 */
	@Override
	public WB_Point rotateAboutAxis2PSelf(final double angle, final double p1x,
			final double p1y, final double p1z, final double p2x,
			final double p2y, final double p2z) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(p1x, p1y, p1z),
				new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		raa.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform#rotateAbout2PointAxisSelf(double
	 * , wblut.geom.WB_Coordinate, wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point rotateAboutAxis2PSelf(final double angle, final WB_Coord p1,
			final WB_Coord p2) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		raa.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform#rotateAboutAxisSelf(double,
	 * wblut.geom.WB_Coordinate, wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point rotateAboutAxisSelf(final double angle, final WB_Coord p,
			final WB_Coord a) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		raa.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform#rotateAbout2PointAxisSelf(double
	 * , double, double, double, double, double, double)
	 */
	@Override
	public WB_Point rotateAboutAxisSelf(final double angle, final double px,
			final double py, final double pz, final double ax, final double ay,
			final double az) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(px, py, pz),
				new WB_Vector(ax, ay, az));
		raa.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.geom.WB_MutableCoordinateTransform#rotateAbout2PointAxisSelf(double
	 * , double, double, double, double, double, double)
	 */
	@Override
	public WB_Point rotateAboutOriginSelf(final double angle, final double x,
			final double y, final double z) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(x, y, z));
		raa.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_CoordinateTransform#rotateAboutAxis(double,
	 * wblut.geom.WB_Coordinate, wblut.geom.WB_Coordinate)
	 */
	@Override
	public WB_Point rotateAboutOriginSelf(final double angle,
			final WB_Coord a) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		raa.applyAsPointSelf(this);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform#scaleSelf(double)
	 */
	@Override
	public WB_Point scaleSelf(final double f) {
		mulSelf(f);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MutableCoordinateTransform#scaleSelf(double, double,
	 * double)
	 */
	@Override
	public WB_Point scaleSelf(final double fx, final double fy,
			final double fz) {
		set(xd() * fx, yd() * fy, zd() * fz);
		return this;
	}

	/**
	 *
	 */
	@Override
	public void invert() {
		mulSelf(-1);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double[] coords() {
		return new double[] { xd(), yd(), zd() };
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point copy() {
		return new WB_Point(xd(), yd(), zd());
	}

	/**
	 *
	 *
	 * @param otherXYZ
	 * @return
	 */
	@Override
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
		return "WB_Point3D [x=" + xd() + ", y=" + yd() + ", z=" + zd() + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Coord#wd()
	 */
	@Override
	public double wd() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Coord#wd()
	 */
	@Override
	public float wf() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Coord#getd(int)
	 */
	@Override
	public double getd(final int i) {
		if (i == 0) {
			return x;
		}
		if (i == 1) {
			return y;
		}
		if (i == 2) {
			return z;
		}
		if (i == 3) {
			return 1;
		}
		return Double.NaN;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Coord#getf(int)
	 */
	@Override
	public float getf(final int i) {
		if (i == 0) {
			return (float) x;
		}
		if (i == 1) {
			return (float) y;
		}
		if (i == 2) {
			return (float) z;
		}
		if (i == 3) {
			return 1;
		}
		return Float.NaN;
	}
}
