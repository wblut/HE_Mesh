/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.core.WB_HashCode;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_MutableCoordinate4D extends WB_MutableCoordinate {
	/** Coordinates. */
	double w;

	private static final WB_Coord X = new WB_MutableCoordinate4D(1, 0, 0, 0);
	private static final WB_Coord Y = new WB_MutableCoordinate4D(0, 1, 0, 0);
	private static final WB_Coord Z = new WB_MutableCoordinate4D(0, 0, 1, 0);
	private static final WB_Coord W = new WB_MutableCoordinate4D(0, 0, 0, 1);
	private static final WB_Coord ORIGIN = new WB_MutableCoordinate4D(0, 0, 0, 0);
	private static final WB_Coord ZERO = new WB_MutableCoordinate4D(0, 0, 0, 0);

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
	public static WB_Coord W() {
		return W;
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
	public WB_MutableCoordinate4D() {
		x = y = z = w = 0;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public WB_MutableCoordinate4D(final double x, final double y) {
		this.x = x;
		this.y = y;
		z = 0;
		w = 0;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public WB_MutableCoordinate4D(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		w = 0;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public WB_MutableCoordinate4D(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 *
	 *
	 * @param x
	 */
	public WB_MutableCoordinate4D(final double[] x) {
		if (x.length != 4) {
			throw new IllegalArgumentException("Array needs to be of length 4.");
		}
		this.x = x[0];
		this.y = x[1];
		this.z = x[2];
		this.w = x[3];
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_MutableCoordinate4D(final WB_Coord v) {
		x = v.xd();
		y = v.yd();
		z = v.zd();
		w = v.wd();
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_MutableCoordinate4D(final double[] fromPoint, final double[] toPoint) {
		if (fromPoint.length != 4 || toPoint.length != 4) {
			throw new IllegalArgumentException("Array needs to be of length 4.");
		}
		this.x = toPoint[0] - fromPoint[0];
		this.y = toPoint[1] - fromPoint[1];
		this.z = toPoint[2] - fromPoint[2];
		this.w = toPoint[3] - fromPoint[3];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#getd(int)
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
			return w;
		}
		return Double.NaN;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#getf(int)
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
			return (float) w;
		}
		return Float.NaN;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#xd()
	 */
	@Override
	public double xd() {
		return x;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#yd()
	 */
	@Override
	public double yd() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#zd()
	 */
	@Override
	public double zd() {
		return z;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#wd()
	 */
	@Override
	public double wd() {
		return w;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#xf()
	 */
	@Override
	public float xf() {
		return (float) x;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#yf()
	 */
	@Override
	public float yf() {
		return (float) y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#zf()
	 */
	@Override
	public float zf() {
		return (float) z;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Coordinate#wf()
	 */
	@Override
	public float wf() {
		return (float) w;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#setX(double)
	 */
	@Override
	public void setX(final double x) {
		this.x = x;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#setY(double)
	 */
	@Override
	public void setY(final double y) {
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#setZ(double)
	 */
	@Override
	public void setZ(final double z) {
		this.z = z;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#setW(double)
	 */
	@Override
	public void setW(final double w) {
		this.w = w;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#setCoord(int, double)
	 */
	@Override
	public void setCoord(final int i, final double v) {
		if (i == 0) {
			this.x = v;
		}
		if (i == 1) {
			this.y = v;
		}
		if (i == 2) {
			this.z = v;
		}
		if (i == 3) {
			this.w = v;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#set(double, double)
	 */
	@Override
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
		z = 0;
		w = 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#set(double, double, double)
	 */
	@Override
	public void set(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		w = 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#set(double, double, double, double)
	 */
	@Override
	public void set(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_MutableCoordinate#set(wblut.geom.WB_Coordinate)
	 */
	@Override
	public void set(final WB_Coord v) {
		set(v.xd(), v.yd(), v.zd(), v.wd());
	}

	/*
	 * (non-Javadoc)
	 *
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
	 *
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
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return WB_HashCode.calculateHashCode(xd(), yd(), zd(), wd());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WB_SimpleVector4D [x=" + xd() + ", y=" + yd() + ", z=" + zd() + ", w=" + wd() + "]";
	}
}
