package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoord;
import wblut.math.WB_Epsilon;

public class HE_TextureCoordinate implements WB_MutableCoord {
	public static final HE_TextureCoordinate ZERO = new HE_TextureCoordinate();
	private double u, v, w;

	public HE_TextureCoordinate() {
		u = v = w = 0.0;
	}

	public HE_TextureCoordinate(final WB_Coord uvw) {
		u = uvw.xd();
		v = uvw.yd();
		w = uvw.zd();
	}

	public HE_TextureCoordinate(final double f, final HE_TextureCoordinate uvw1, final HE_TextureCoordinate uvw2) {
		final double omf = 1.0 - f;
		u = f * uvw1.ud() + omf * uvw2.ud();
		v = f * uvw1.vd() + omf * uvw2.vd();
		w = f * uvw1.wd() + omf * uvw2.wd();
	}

	public HE_TextureCoordinate(final double u, final double v) {
		this.u = u;
		this.v = v;
		w = 0;
	}

	public HE_TextureCoordinate(final double u, final double v, final double w) {
		this.u = u;
		this.v = v;
		this.w = w;
	}

	public void clear() {
		u = v = w = 0;
	}

	public double ud() {
		return u;
	}

	public double vd() {
		return v;
	}

	@Override
	public double wd() {
		return w;
	}

	public float uf() {
		return (float) u;
	}

	public float vf() {
		return (float) v;
	}

	@Override
	public float wf() {
		return (float) w;
	}

	public void setUVW(final double u, final double v, final double w) {
		this.u = u;
		this.v = v;
		this.w = w;
	}

	public void setUVW(final WB_Coord u) {
		this.u = u.xd();
		this.v = u.yd();
		this.w = u.zd();
	}

	@Override
	public String toString() {
		return "Texture Coordinate: [u=" + ud() + ", v=" + vd() + ", w=" + wd() + "]";
	}

	@Override
	public double xd() {
		return u;
	}

	@Override
	public double yd() {
		return v;
	}

	@Override
	public double zd() {
		return w;
	}

	@Override
	public double getd(final int i) {
		if (i == 0) {
			return u;
		}
		if (i == 1) {
			return v;
		}
		if (i == 2) {
			return w;
		}
		return Double.NaN;
	}

	@Override
	public float xf() {
		return (float) u;
	}

	@Override
	public float yf() {
		return (float) v;
	}

	@Override
	public float zf() {
		return (float) w;
	}

	@Override
	public float getf(final int i) {
		if (i == 0) {
			return (float) u;
		}
		if (i == 1) {
			return (float) v;
		}
		if (i == 2) {
			return (float) w;
		}
		return Float.NaN;
	}

	@Override
	public void setX(final double x) {
		u = x;
	}

	@Override
	public void setY(final double y) {
		v = y;
	}

	@Override
	public void setZ(final double z) {
		w = z;
	}

	@Override
	public void setW(final double w) {
		throw new UnsupportedOperationException("4D coordinates not available for texture coordinates.");
	}

	@Override
	public void setCoord(final int i, final double v) {
		if (i == 0) {
			this.u = v;
		}
		if (i == 1) {
			this.v = v;
		}
		if (i == 2) {
			this.w = v;
		}
	}

	@Override
	public void set(final WB_Coord p) {
		set(p.xd(), p.yd(), p.zd());
	}

	@Override
	public void set(final double x, final double y) {
		u = x;
		v = y;
	}

	@Override
	public void set(final double x, final double y, final double z) {
		u = x;
		v = y;
		w = z;
	}

	@Override
	public void set(final double x, final double y, final double z, final double w) {
		throw new UnsupportedOperationException("4D coordinates not available for texture coordinates.");
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
		if (!WB_Epsilon.isEqual(xd(), p.xd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(yd(), p.yd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(zd(), p.zd())) {
			return false;
		}
		return true;
	}
}
