package wblut.processing;

import processing.core.PVector;
import wblut.core.WB_HashCode;
import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoord;
import wblut.math.WB_Epsilon;

public class WB_PVector extends PVector implements WB_MutableCoord {
	private static final long serialVersionUID = 3211029434911447698L;

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
		return z;
	}

	@Override
	public double wd() {
		return 0;
	}

	@Override
	public double getd(final int i) {
		if (i == 0) {
			return x;
		} else if (i == 1) {
			return y;
		} else if (i == 2) {
			return z;
		}
		return 0;
	}

	@Override
	public float xf() {
		return x;
	}

	@Override
	public float yf() {
		return y;
	}

	@Override
	public float zf() {
		return z;
	}

	@Override
	public float wf() {
		return 0;
	}

	@Override
	public float getf(final int i) {
		if (i == 0) {
			return x;
		} else if (i == 1) {
			return y;
		} else if (i == 2) {
			return z;
		}
		return 0;
	}

	@Override
	public void setX(final double x) {
		this.x = (float) x;
	}

	@Override
	public void setY(final double y) {
		this.y = (float) y;
	}

	@Override
	public void setZ(final double z) {
		this.z = (float) z;
	}

	@Override
	public void setW(final double w) {
	}

	@Override
	public void setCoord(final int i, final double v) {
		if (i == 0) {
			this.x = (float) v;
		} else if (i == 1) {
			this.y = (float) v;
		} else if (i == 2) {
			this.z = (float) v;
		}
	}

	@Override
	public void set(final WB_Coord p) {
		x = p.xf();
		y = p.yf();
		z = p.zf();
	}

	@Override
	public void set(final double x, final double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	@Override
	public void set(final double x, final double y, final double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	@Override
	public void set(final double x, final double y, final double z, final double w) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
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
		if (!WB_Epsilon.isEqualAbs(xd(), p.xd())) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(yd(), p.yd())) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(zd(), p.zd())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return WB_HashCode.calculateHashCode(xd(), yd(), zd());
	}
}
