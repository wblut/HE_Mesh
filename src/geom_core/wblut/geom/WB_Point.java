package wblut.geom;

import lombok.ToString;
import wblut.core.WB_HashCode;
import wblut.math.WB_Ease;
import wblut.math.WB_Epsilon;
import wblut.math.WB_M33;

/**
 *
 */
/**
 *
 *
 * @return
 */
@ToString(callSuper = true, includeFieldNames = true)
public class WB_Point extends WB_Vector implements WB_Transformable3D {
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
	public WB_Vector subToVector3D(final double x, final double y, final double z) {
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
	public WB_Vector subToVector2D(final double x, final double y, final double z) {
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

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector subToVector(final WB_Coord p, final WB_Coord q) {
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
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static WB_Vector subToVector(final WB_Coord p, final double x, final double y, final double z) {
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
	public static WB_Point addMul(final WB_Coord p, final double f, final WB_Coord q) {
		return new WB_Point(p.xd() + f * q.xd(), p.yd() + f * q.yd(), p.zd() + f * q.zd());
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
	public static WB_Point mulAddMul(final double f, final WB_Coord p, final double g, final WB_Coord q) {
		return new WB_Point(f * p.xd() + g * q.xd(), f * p.yd() + g * q.yd(), f * p.zd() + g * q.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Point cross(final WB_Coord p, final WB_Coord q) {
		return new WB_Point(p.yd() * q.zd() - p.zd() * q.yd(), p.zd() * q.xd() - p.xd() * q.zd(),
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

	/**
	 *
	 *
	 * @param v
	 * @param w
	 * @param f
	 * @return
	 */
	public static WB_Point interpolate(final WB_Coord v, final WB_Coord w, final double f) {
		return new WB_Point(WB_GeometryOp.interpolate(v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd(), f));
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
	public static WB_Point interpolateEase(final WB_Coord v, final WB_Coord w, final double f, final WB_Ease ease,
			final WB_Ease.EaseType type) {
		return new WB_Point(
				WB_GeometryOp.interpolateEase(v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd(), f, ease, type));
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Point add(final double... x) {
		if (x.length == 3) {
			return new WB_Point(this.xd() + x[0], this.yd() + x[1], this.zd() + x[2]);
		} else if (x.length == 2) {
			return new WB_Point(this.xd() + x[0], this.yd() + x[1], this.zd());
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
	public WB_Point add(final WB_Coord p) {
		return new WB_Point(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Point sub(final double... x) {
		if (x.length == 3) {
			return new WB_Point(this.xd() - x[0], this.yd() - x[1], this.zd() - x[2]);
		} else if (x.length == 2) {
			return new WB_Point(this.xd() - x[0], this.yd() - x[1], this.zd());
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
	public WB_Point sub(final WB_Coord p) {
		return new WB_Point(this.xd() - p.xd(), this.yd() - p.yd(), this.zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point mul(final double f) {
		return new WB_Point(xd() * f, yd() * f, zd() * f);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point div(final double f) {
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
	public WB_Point addMul(final double f, final double... x) {
		if (x.length == 3) {
			return new WB_Point(this.xd() + f * x[0], this.yd() + f * x[1], this.zd() + f * x[2]);
		} else if (x.length == 2) {
			return new WB_Point(this.xd() + f * x[0], this.yd() + f * x[1], this.zd());
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
	public WB_Point addMul(final double f, final WB_Coord p) {
		return new WB_Point(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd());
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
	public WB_Point mulAddMul(final double f, final double g, final WB_Coord p) {
		return new WB_Point(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd());
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
	public WB_Point mulAddMul(final double f, final double g, final double... x) {
		if (x.length == 3) {
			return new WB_Point(f * this.xd() + g * x[0], f * this.yd() + g * x[1], f * this.zd() + g * x[2]);
		} else if (x.length == 2) {
			return new WB_Point(f * this.xd() + g * x[0], f * this.yd() + g * x[1], this.zd());
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
	public WB_Point cross(final WB_Coord p) {
		return new WB_Point(yd() * p.zd() - zd() * p.yd(), zd() * p.xd() - xd() * p.zd(),
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
	public WB_Point addSelf(final WB_Coord p) {
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

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Point subSelf(final WB_Coord v) {
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
	public WB_Point mulSelf(final double f) {
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
	public WB_Point divSelf(final double f) {
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

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point addMulSelf(final double f, final WB_Coord p) {
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

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Point addSelf(final double x, final double y, final double z) {
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
	public WB_Point mulAddMulSelf(final double f, final double g, final double... x) {
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
	public WB_Point mulAddMulSelf(final double f, final double g, final WB_Coord p) {
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
	public WB_Point crossSelf(final WB_Coord p) {
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
	public WB_Point trimSelf(final double d) {
		if (getSqLength3D() > d * d) {
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
	public WB_Point apply2D(final WB_Transform2D T) {
		return T.applyAsPoint2D(this);
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
	public WB_Point applyAsVector2D(final WB_Transform2D T) {
		return new WB_Point(T.applyAsVector2D(this));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point applyAsNormal2D(final WB_Transform2D T) {
		return new WB_Point(T.applyAsNormal2D(this));
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	public WB_Point translate2D(final double px, final double py) {
		return new WB_Point(this.xd() + px, this.yd() + py);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point translate2D(final WB_Coord p) {
		return new WB_Point(this.xd() + p.xd(), this.yd() + p.yd());
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
	public WB_Point rotateAboutPoint2D(final double angle, final double px, final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		final WB_Point result = new WB_Point(this);
		raa.applyAsPoint2DSelf(result);
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
	public WB_Point rotateAboutPoint2D(final double angle, final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		final WB_Point result = new WB_Point(this);
		raa.applyAsPoint2DSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	@Override
	public WB_Point rotateAboutOrigin2D(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		final WB_Point result = new WB_Point(this);
		raa.applyAsPoint2DSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point scale2D(final double f) {
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
	public WB_Point scale2D(final double fx, final double fy) {
		return new WB_Point(xd() * fx, yd() * fy);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point apply2DSelf(final WB_Transform2D T) {
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
	public WB_Point applyAsPoint2DSelf(final WB_Transform2D T) {
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
	public WB_Point applyAsVector2DSelf(final WB_Transform2D T) {
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
	public WB_Point applyAsNormal2DSelf(final WB_Transform2D T) {
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
	public WB_Point translate2DSelf(final double px, final double py) {
		set(xd() + px, yd() + py);
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point translate2DSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd());
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
	public WB_Point rotateAboutPoint2DSelf(final double angle, final double px, final double py) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, new WB_Point(px, py));
		raa.applyAsPoint2DSelf(this);
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
	public WB_Point rotateAboutPoint2DSelf(final double angle, final WB_Coord p) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutPoint(angle, p);
		raa.applyAsPoint2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	@Override
	public WB_Point rotateAboutOrigin2DSelf(final double angle) {
		final WB_Transform2D raa = new WB_Transform2D();
		raa.addRotateAboutOrigin(angle);
		raa.applyAsPoint2DSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point scale2DSelf(final double f) {
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
	public WB_Point scale2DSelf(final double fx, final double fy) {
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
	public WB_Point apply(final WB_Transform3D T) {
		return T.applyAsPoint(this);
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
	public WB_Point applyAsNormal(final WB_Transform3D T) {
		return new WB_Point(T.applyAsNormal(this));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point applyAsVector(final WB_Transform3D T) {
		return new WB_Point(T.applyAsVector(this));
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
	public WB_Point translate(final double px, final double py, final double pz) {
		return new WB_Point(this.xd() + px, this.yd() + py, this.zd() + pz);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point translate(final WB_Coord p) {
		return new WB_Point(xd() + p.xd(), yd() + p.yd(), zd() + p.zd());
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
	public WB_Point rotateAboutAxis2P(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(p1x, p1y, p1z), new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		raa.applyAsPointSelf(result);
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
	public WB_Point rotateAboutAxis2P(final double angle, final WB_Coord p1, final WB_Coord p2) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		raa.applyAsPointSelf(result);
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
	public WB_Point rotateAboutAxis(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(px, py, pz), new WB_Vector(ax, ay, az));
		raa.applyAsPointSelf(result);
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
	public WB_Point rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord a) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		raa.applyAsPointSelf(result);
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
	public WB_Point rotateAboutOrigin(final double angle, final double x, final double y, final double z) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(x, y, z));
		raa.applyAsPointSelf(result);
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
	public WB_Point rotateAboutOrigin(final double angle, final WB_Coord a) {
		final WB_Point result = new WB_Point(this);
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		raa.applyAsPointSelf(result);
		return result;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point scale(final double f) {
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
	public WB_Point scale(final double fx, final double fy, final double fz) {
		return new WB_Point(xd() * fx, yd() * fy, zd() * fz);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point applySelf(final WB_Transform3D T) {
		return applyAsPointSelf(T);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Point applyAsPointSelf(final WB_Transform3D T) {
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
	public WB_Point translateSelf(final double px, final double py, final double pz) {
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
	public WB_Point translateSelf(final WB_Coord p) {
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
	public WB_Point rotateAboutAxis2PSelf(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(p1x, p1y, p1z), new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		raa.applyAsPointSelf(this);
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
	public WB_Point rotateAboutAxis2PSelf(final double angle, final WB_Coord p1, final WB_Coord p2) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		raa.applyAsPointSelf(this);
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
	public WB_Point rotateAboutAxisSelf(final double angle, final WB_Coord p, final WB_Coord a) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		raa.applyAsPointSelf(this);
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
	public WB_Point rotateAboutAxisSelf(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Vector(px, py, pz), new WB_Vector(ax, ay, az));
		raa.applyAsPointSelf(this);
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
	public WB_Point rotateAboutOriginSelf(final double angle, final double x, final double y, final double z) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(x, y, z));
		raa.applyAsPointSelf(this);
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
	public WB_Point rotateAboutOriginSelf(final double angle, final WB_Coord a) {
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		raa.applyAsPointSelf(this);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point scaleSelf(final double f) {
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
	public WB_Point scaleSelf(final double fx, final double fy, final double fz) {
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

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double wd() {
		return 1;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float wf() {
		return 1;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
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

	/**
	 *
	 *
	 * @param i
	 * @return
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
