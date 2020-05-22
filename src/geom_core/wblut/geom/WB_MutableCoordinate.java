package wblut.geom;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(includeFieldNames = true)
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class WB_MutableCoordinate implements WB_MutableCoord {
	@Getter
	@Setter
	double x, y, z;
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
	 *
	 * @param x
	 * @param y
	 */
	public WB_MutableCoordinate(final double x, final double y) {
		this.x = x;
		this.y = y;
		z = 0;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public WB_MutableCoordinate(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 *
	 *
	 * @param x
	 */
	public WB_MutableCoordinate(final double[] x) {
		if (x.length != 3 && x.length != 2) {
			throw new IllegalArgumentException("Array needs to be of length 2 or 3.");
		}
		this.x = x[0];
		this.y = x[1];
		this.z = x.length > 2 ? x[2] : 0.0;
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_MutableCoordinate(final double[] fromPoint, final double[] toPoint) {
		if (fromPoint.length != 3 && fromPoint.length != 2 || toPoint.length != 3 && toPoint.length != 2) {
			throw new IllegalArgumentException("Array needs to be of length 2 or 3.");
		}
		this.x = toPoint[0] - fromPoint[0];
		this.y = toPoint[1] - fromPoint[1];
		this.z = (toPoint.length > 2 ? toPoint[2] : 0.0) - (fromPoint.length > 2 ? fromPoint[2] : 0.0);
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_MutableCoordinate(final WB_Coord v) {
		x = v.xd();
		y = v.yd();
		z = v.zd();
	}

	/**
	 *
	 *
	 * @param fromPoint
	 * @param toPoint
	 */
	public WB_MutableCoordinate(final WB_Coord fromPoint, final WB_Coord toPoint) {
		x = toPoint.xd() - fromPoint.xd();
		y = toPoint.yd() - fromPoint.yd();
		z = toPoint.zd() - fromPoint.zd();
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
		return Float.NaN;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double xd() {
		return x;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double yd() {
		return y;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double zd() {
		return z;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double wd() {
		return 0;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float xf() {
		return (float) x;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float yf() {
		return (float) y;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float zf() {
		return (float) z;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float wf() {
		return 0;
	}

	/**
	 *
	 *
	 * @param w
	 */
	@Override
	public void setW(final double w) {
	}

	/**
	 *
	 *
	 * @param i
	 * @param v
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
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	@Override
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
		z = 0;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void set(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	@Override
	public void set(final double x, final double y, final double z, final double w) {
		set(x, y, z);
	}

	/**
	 *
	 *
	 * @param v
	 */
	@Override
	public void set(final WB_Coord v) {
		set(v.xd(), v.yd(), v.zd());
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

	@Override
	public void getd(final double[] result) {
		result[0] = xd();
		result[1] = yd();
		result[2] = zd();
	}

	@Override
	public void getd(final int i, final double[] result) {
		result[i] = xd();
		result[i + 1] = yd();
		result[i + 2] = zd();
	}

	@Override
	public void getf(final float[] result) {
		result[0] = xf();
		result[1] = yf();
		result[2] = zf();
	}

	@Override
	public void getf(final int i, final float[] result) {
		result[i] = xf();
		result[i + 1] = yf();
		result[i + 2] = zf();
	}
}
