package wblut.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(includeFieldNames = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class WB_MutableCoordinate2D implements WB_MutableCoord {
	@Setter
	@Getter
	double x, y;
	private static final WB_Coord X = new WB_MutableCoordinate2D(1, 0);
	private static final WB_Coord Y = new WB_MutableCoordinate2D(0, 1);
	private static final WB_Coord ORIGIN = new WB_MutableCoordinate2D(0, 0);
	private static final WB_Coord ZERO = new WB_MutableCoordinate2D(0, 0);

	public static WB_Coord X() {
		return X;
	}

	public static WB_Coord Y() {
		return Y;
	}

	public static WB_Coord ZERO() {
		return ZERO;
	}

	public static WB_Coord ORIGIN() {
		return ORIGIN;
	}

	public WB_MutableCoordinate2D(final double[] x) {
		if (x.length != 2) {
			throw new IllegalArgumentException("Array needs to be of length 2.");
		}
		this.x = x[0];
		this.y = x[1];
	}

	public WB_MutableCoordinate2D(final double[] fromPoint, final double[] toPoint) {
		if (fromPoint.length != 2 || toPoint.length != 2) {
			throw new IllegalArgumentException("Array needs to be of length 2.");
		}
		this.x = toPoint[0] - fromPoint[0];
		this.y = toPoint[1] - fromPoint[1];
	}

	public WB_MutableCoordinate2D(final WB_Coord v) {
		x = v.xd();
		y = v.yd();
	}

	public WB_MutableCoordinate2D(final WB_Coord fromPoint, final WB_Coord toPoint) {
		x = toPoint.xd() - fromPoint.xd();
		y = toPoint.yd() - fromPoint.yd();
	}

	@Override
	public double getd(final int i) {
		if (i == 0) {
			return x;
		}
		if (i == 1) {
			return y;
		}
		return Double.NaN;
	}

	@Override
	public float getf(final int i) {
		if (i == 0) {
			return (float) x;
		}
		if (i == 1) {
			return (float) y;
		}
		return Float.NaN;
	}

	@Override
	public double xd() {
		return x;
	}

	@Override
	public double yd() {
		return y;
	}

	@Override
	public double zd() {
		return 0;
	}

	@Override
	public double wd() {
		return 0;
	}

	@Override
	public float xf() {
		return (float) x;
	}

	@Override
	public float yf() {
		return (float) y;
	}

	@Override
	public float zf() {
		return 0;
	}

	@Override
	public float wf() {
		return 0;
	}

	@Override
	public void setZ(final double z) {
	}

	@Override
	public void setW(final double w) {
	}

	@Override
	public void setCoord(final int i, final double v) {
		if (i == 0) {
			this.x = v;
		}
		if (i == 1) {
			this.y = v;
		}
	}

	@Override
	public void set(final WB_Coord v) {
		set(v.xd(), v.yd());
	}

	@Override
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(final double x, final double y, final double z, final double w) {
		set(x, y);
	}

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
}