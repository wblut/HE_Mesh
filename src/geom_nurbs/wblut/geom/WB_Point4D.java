package wblut.geom;

import wblut.core.WB_HashCode;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_Point4D extends WB_Vector4D {
	/**
	 *
	 *
	 * @return
	 */
	public static final WB_Point4D ZERO() {
		return new WB_Point4D(0, 0, 0, 1);
	}

	/**
	 *
	 */
	public WB_Point4D() {
		x = y = z = 0;
		w = 1;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public WB_Point4D(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_Point4D(final WB_Coord v) {
		x = v.xd();
		y = v.yd();
		z = v.zd();
		w = v.wd();
	}

	/**
	 *
	 *
	 * @param v
	 * @param w
	 */
	public WB_Point4D(final WB_Coord v, final double w) {
		x = v.xd();
		y = v.yd();
		z = v.zd();
		this.w = w;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Point4D add(final WB_Coord p, final WB_Coord q) {
		return new WB_Point4D(q.xd() + p.xd(), q.yd() + p.yd(), q.zd() + p.zd(), q.wd() + p.wd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Point4D sub(final WB_Coord p, final WB_Coord q) {
		return new WB_Point4D(p.xd() - q.xd(), p.yd() - q.yd(), p.zd() - q.zd(), p.wd() - q.wd());
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Point4D mul(final WB_Coord p, final double f) {
		return new WB_Point4D(p.xd() * f, p.yd() * f, p.zd() * f, p.wd() * f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @return
	 */
	public static WB_Point4D div(final WB_Coord p, final double f) {
		return WB_Point4D.mul(p, 1.0 / f);
	}

	/**
	 *
	 *
	 * @param p
	 * @param f
	 * @param q
	 * @return
	 */
	public static WB_Point4D addMul(final WB_Coord p, final double f, final WB_Coord q) {
		return new WB_Point4D(p.xd() + f * q.xd(), p.yd() + f * q.yd(), p.zd() + f * q.zd(), p.wd() + f * q.wd());
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
	public static WB_Point4D mulAddMul(final double f, final WB_Coord p, final double g, final WB_Coord q) {
		return new WB_Point4D(f * p.xd() + g * q.xd(), f * p.yd() + g * q.yd(), f * p.zd() + g * q.zd(),
				f * p.wd() + g * q.wd());
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param t
	 * @return
	 */
	public static WB_Point4D interpolate(final WB_Coord p0, final WB_Coord p1, final double t) {
		return new WB_Point4D(p0.xd() + t * (p1.xd() - p0.xd()), p0.yd() + t * (p1.yd() - p0.yd()),
				p0.zd() + t * (p1.zd() - p0.zd()), p0.wd() + t * (p1.wd() - p0.wd()));
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Point4D add(final double... x) {
		return new WB_Point4D(this.xd() + x[0], this.yd() + x[1], this.zd() + x[2], this.wd() + x[3]);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D add(final WB_Coord p) {
		return new WB_Point4D(xd() + p.xd(), yd() + p.yd(), zd() + p.zd(), wd() + p.wd());
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Point4D sub(final double... x) {
		return new WB_Point4D(this.xd() - x[0], this.yd() - x[1], this.zd() - x[2], this.wd() - x[3]);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D sub(final WB_Coord p) {
		return new WB_Point4D(this.xd() - p.xd(), this.yd() - p.yd(), this.zd() - p.zd(), this.wd() - p.wd());
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D mul(final double f) {
		return new WB_Point4D(xd() * f, yd() * f, zd() * f, wd() * f);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D div(final double f) {
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
	public WB_Point4D addMul(final double f, final double... x) {
		return new WB_Point4D(this.xd() + f * x[0], this.yd() + f * x[1], this.zd() + f * x[2], this.wd() + f * x[3]);
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D addMul(final double f, final WB_Coord p) {
		return new WB_Point4D(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd(), wd() + f * p.wd());
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
	public WB_Point4D mulAddMul(final double f, final double g, final double... x) {
		return new WB_Point4D(f * xd() + g * x[0], f * yd() + g * x[1], f * zd() + g * x[2], f * wd() + g * x[3]);
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
	public WB_Point4D mulAddMul(final double f, final double g, final WB_Coord p) {
		return new WB_Point4D(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd(),
				f * wd() + g * p.wd());
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Point4D addSelf(final double... x) {
		set(xd() + x[0], yd() + x[1], zd() + x[2], wd() + x[3]);
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D addSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), zd() + p.zd(), wd() + p.wd());
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Point4D subSelf(final double... x) {
		set(xd() - x[0], yd() - x[1], zd() - x[2], wd() - x[3]);
		return this;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Point4D subSelf(final WB_Coord v) {
		set(xd() - v.xd(), yd() - v.yd(), zd() - v.zd(), wd() - v.wd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D mulSelf(final double f) {
		set(f * xd(), f * yd(), f * zd(), f * wd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D divSelf(final double f) {
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
	public WB_Point4D addMulSelf(final double f, final double... x) {
		set(xd() + f * x[0], yd() + f * x[1], zd() + f * x[2], wd() + f * x[3]);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D addMulSelf(final double f, final WB_Coord p) {
		set(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd(), wd() + f * p.wd());
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
	public WB_Point4D mulAddMulSelf(final double f, final double g, final double... x) {
		set(f * this.xd() + g * x[0], f * this.yd() + g * x[1], f * this.zd() + g * x[2], f * this.wd() + g * x[3]);
		return this;
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
	public WB_Point4D mulAddMulSelf(final double f, final double g, final WB_Coord p) {
		set(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd(), f * wd() + g * p.wd());
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	@Override
	public WB_Point4D trimSelf(final double d) {
		if (getSqLength4D() > d * d) {
			normalizeSelf();
			mulSelf(d);
		}
		return this;
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
	public WB_Point4D add3D(final double x, final double y, final double z) {
		return new WB_Point4D(this.xd() + x, this.yd() + y, this.zd() + z, this.wd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D add3D(final WB_Coord p) {
		return new WB_Point4D(xd() + p.xd(), yd() + p.yd(), zd() + p.zd(), wd());
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
	public WB_Point4D sub3D(final double x, final double y, final double z) {
		return new WB_Point4D(this.xd() - x, this.yd() - y, this.zd() - z, this.wd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D sub3D(final WB_Coord p) {
		return new WB_Point4D(this.xd() - p.xd(), this.yd() - p.yd(), this.zd() - p.zd(), this.wd());
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D mul3D(final double f) {
		return new WB_Point4D(xd() * f, yd() * f, zd() * f, wd());
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D div3D(final double f) {
		return mul3D(1.0 / f);
	}

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Point4D addMul3D(final double f, final double x, final double y, final double z) {
		return new WB_Point4D(this.xd() + f * x, this.yd() + f * y, this.zd() + f * z, this.wd());
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D addMul3D(final double f, final WB_Coord p) {
		return new WB_Point4D(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd(), wd());
	}

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Point4D mulAddMul3D(final double f, final double g, final double x, final double y, final double z) {
		return new WB_Point4D(f * this.xd() + g * x, f * this.yd() + g * y, f * this.zd() + g * z, this.wd());
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
	public WB_Point4D mulAddMul3D(final double f, final double g, final WB_Coord p) {
		return new WB_Point4D(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd(), wd());
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
	public WB_Point4D add3DSelf(final double x, final double y, final double z) {
		set(xd() + x, yd() + y, zd() + z, wd());
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D add3DSelf(final WB_Coord p) {
		set(xd() + p.xd(), yd() + p.yd(), zd() + p.zd(), wd());
		return this;
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
	public WB_Point4D sub3DSelf(final double x, final double y, final double z) {
		set(xd() - x, yd() - y, zd() - z, wd());
		return this;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Point4D sub3DSelf(final WB_Coord v) {
		set(xd() - v.xd(), yd() - v.yd(), zd() - v.zd(), wd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D mul3DSelf(final double f) {
		set(f * xd(), f * yd(), f * zd(), wd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public WB_Point4D div3DSelf(final double f) {
		return mul3DSelf(1.0 / f);
	}

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Point4D addMul3DSelf(final double f, final double x, final double y, final double z) {
		set(xd() + f * x, yd() + f * y, zd() + f * z, wd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point4D addMul3DSelf(final double f, final WB_Coord p) {
		set(xd() + f * p.xd(), yd() + f * p.yd(), zd() + f * p.zd(), wd());
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Point4D mulAddMul3DSelf(final double f, final double g, final double x, final double y, final double z) {
		set(f * this.xd() + g * x, f * this.yd() + g * y, f * this.zd() + g * z, this.wd());
		return this;
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
	public WB_Point4D mulAddMul3DSelf(final double f, final double g, final WB_Coord p) {
		set(f * xd() + g * p.xd(), f * yd() + g * p.yd(), f * zd() + g * p.zd(), wd());
		return this;
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	@Override
	public WB_Point4D rotateXWSelf(final double theta) {
		set(xd() * Math.cos(theta) + wd() * Math.sin(theta), yd(), zd(),
				xd() * -Math.sin(theta) + wd() * Math.cos(theta));
		return this;
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	@Override
	public WB_Point4D rotateXYSelf(final double theta) {
		set(xd() * Math.cos(theta) + yd() * -Math.sin(theta), xd() * Math.sin(theta) + yd() * Math.cos(theta), zd(),
				wd());
		return this;
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	@Override
	public WB_Point4D rotateXZSelf(final double theta) {
		set(xd() * Math.cos(theta) + zd() * -Math.sin(theta), yd(), xd() * Math.sin(theta) + zd() * Math.cos(theta),
				wd());
		return this;
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	@Override
	public WB_Point4D rotateYWSelf(final double theta) {
		set(xd(), yd() * Math.cos(theta) + wd() * -Math.sin(theta), zd(),
				yd() * Math.sin(theta) + wd() * Math.cos(theta));
		return this;
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	@Override
	public WB_Point4D rotateYZSelf(final double theta) {
		set(xd(), yd() * Math.cos(theta) + zd() * Math.sin(theta), yd() * -Math.sin(theta) + zd() * Math.cos(theta),
				wd());
		return this;
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	@Override
	public WB_Point4D rotateZWSelf(final double theta) {
		set(xd(), yd(), zd() * Math.cos(theta) + wd() * -Math.sin(theta),
				zd() * Math.sin(theta) + wd() * Math.cos(theta));
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point4D copy() {
		return new WB_Point4D(xd(), yd(), zd(), wd());
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
		return WB_HashCode.calculateHashCode(xd(), yd(), zd(), wd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "WB_Point4D [x=" + xd() + ", y=" + yd() + ", z=" + zd() + ", w=" + wd() + "]";
	}
}
