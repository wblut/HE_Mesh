package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

public class WB_M33 {
	public double m11, m12, m13;
	public double m21, m22, m23;
	public double m31, m32, m33;

	public WB_M33() {
	}

	public WB_M33(final double[][] matrix33) {
		m11 = matrix33[0][0];
		m12 = matrix33[0][1];
		m13 = matrix33[0][2];
		m21 = matrix33[1][0];
		m22 = matrix33[1][1];
		m23 = matrix33[1][2];
		m31 = matrix33[2][0];
		m32 = matrix33[2][1];
		m33 = matrix33[2][2];
	}

	public WB_M33(final double m11, final double m12, final double m13, final double m21, final double m22,
			final double m23, final double m31, final double m32, final double m33) {
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public void set(final double[][] matrix33) {
		m11 = matrix33[0][0];
		m12 = matrix33[0][1];
		m13 = matrix33[0][2];
		m21 = matrix33[1][0];
		m22 = matrix33[1][1];
		m23 = matrix33[1][2];
		m31 = matrix33[2][0];
		m32 = matrix33[2][1];
		m33 = matrix33[2][2];
	}

	public void set(final float[][] matrix33) {
		m11 = matrix33[0][0];
		m12 = matrix33[0][1];
		m13 = matrix33[0][2];
		m21 = matrix33[1][0];
		m22 = matrix33[1][1];
		m23 = matrix33[1][2];
		m31 = matrix33[2][0];
		m32 = matrix33[2][1];
		m33 = matrix33[2][2];
	}

	public void set(final int[][] matrix33) {
		m11 = matrix33[0][0];
		m12 = matrix33[0][1];
		m13 = matrix33[0][2];
		m21 = matrix33[1][0];
		m22 = matrix33[1][1];
		m23 = matrix33[1][2];
		m31 = matrix33[2][0];
		m32 = matrix33[2][1];
		m33 = matrix33[2][2];
	}

	public void set(final double m11, final double m12, final double m13, final double m21, final double m22,
			final double m23, final double m31, final double m32, final double m33) {
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public void set(final WB_M33 m) {
		m11 = m.m11;
		m12 = m.m12;
		m13 = m.m13;
		m21 = m.m21;
		m22 = m.m22;
		m23 = m.m23;
		m31 = m.m31;
		m32 = m.m32;
		m33 = m.m33;
	}

	public WB_M33 get() {
		return new WB_M33(m11, m12, m13, m21, m22, m23, m31, m32, m33);
	}

	public WB_Vector row(final int i) {
		if (i == 0) {
			return new WB_Vector(m11, m12, m13);
		}
		if (i == 1) {
			return new WB_Vector(m21, m22, m23);
		}
		if (i == 2) {
			return new WB_Vector(m31, m32, m33);
		}
		return null;
	}

	public void rowInto(final int i, final WB_MutableCoord result) {
		if (i == 0) {
			result.set(m11, m12, m13);
		}
		if (i == 1) {
			result.set(m21, m22, m23);
		}
		if (i == 2) {
			result.set(m31, m32, m33);
		}
		result.set(0, 0, 0);
	}

	public WB_Vector col(final int i) {
		if (i == 0) {
			return new WB_Vector(m11, m21, m31);
		}
		if (i == 1) {
			return new WB_Vector(m12, m22, m32);
		}
		if (i == 2) {
			return new WB_Vector(m13, m23, m33);
		}
		return null;
	}

	public void colInto(final int i, final WB_MutableCoord result) {
		if (i == 0) {
			result.set(m11, m21, m31);
		}
		if (i == 1) {
			result.set(m12, m22, m32);
		}
		if (i == 2) {
			result.set(m13, m23, m33);
		}
		result.set(0, 0, 0);
	}

	public void add(final WB_M33 m) {
		m11 += m.m11;
		m12 += m.m12;
		m13 += m.m13;
		m21 += m.m21;
		m22 += m.m22;
		m23 += m.m23;
		m31 += m.m31;
		m32 += m.m32;
		m33 += m.m33;
	}

	public void sub(final WB_M33 m) {
		m11 -= m.m11;
		m12 -= m.m12;
		m13 -= m.m13;
		m21 -= m.m21;
		m22 -= m.m22;
		m23 -= m.m23;
		m31 -= m.m31;
		m32 -= m.m32;
		m33 -= m.m33;
	}

	public void mul(final double f) {
		m11 *= f;
		m12 *= f;
		m13 *= f;
		m21 *= f;
		m22 *= f;
		m23 *= f;
		m31 *= f;
		m32 *= f;
		m33 *= f;
	}

	public void div(final double f) {
		final double invf = WB_Epsilon.isZero(f) ? Double.NaN : 1.0 / f;
		m11 *= invf;
		m12 *= invf;
		m13 *= invf;
		m21 *= invf;
		m22 *= invf;
		m23 *= invf;
		m31 *= invf;
		m32 *= invf;
		m33 *= invf;
	}

	public void addInto(final WB_M33 m, final WB_M33 result) {
		result.m11 = m11 + m.m11;
		result.m12 = m12 + m.m12;
		result.m13 = m13 + m.m13;
		result.m21 = m21 + m.m21;
		result.m22 = m22 + m.m22;
		result.m23 = m23 + m.m23;
		result.m31 = m31 + m.m31;
		result.m32 = m32 + m.m32;
		result.m33 = m33 + m.m33;
	}

	public void subInto(final WB_M33 m, final WB_M33 result) {
		result.m11 = m11 - m.m11;
		result.m12 = m12 - m.m12;
		result.m13 = m13 - m.m13;
		result.m21 = m21 - m.m21;
		result.m22 = m22 - m.m22;
		result.m23 = m23 - m.m23;
		result.m31 = m31 - m.m31;
		result.m32 = m32 - m.m32;
		result.m33 = m33 - m.m33;
	}

	public void multInto(final double f, final WB_M33 result) {
		result.m11 = f * m11;
		result.m12 = f * m12;
		result.m13 = f * m13;
		result.m21 = f * m21;
		result.m22 = f * m22;
		result.m23 = f * m23;
		result.m31 = f * m31;
		result.m32 = f * m32;
		result.m33 = f * m33;
	}

	public void divInto(final double f, final WB_M33 result) {
		final double invf = WB_Epsilon.isZero(f) ? 0 : 1.0 / f;
		result.m11 = invf * m11;
		result.m12 = invf * m12;
		result.m13 = invf * m13;
		result.m21 = invf * m21;
		result.m22 = invf * m22;
		result.m23 = invf * m23;
		result.m31 = invf * m31;
		result.m32 = invf * m32;
		result.m33 = invf * m33;
	}

	public static WB_M33 mul(final WB_M33 m, final WB_M33 n) {
		return new WB_M33(m.m11 * n.m11 + m.m12 * n.m21 + m.m13 * n.m31, m.m11 * n.m12 + m.m12 * n.m22 + m.m13 * n.m32,
				m.m11 * n.m13 + m.m12 * n.m23 + m.m13 * n.m33, m.m21 * n.m11 + m.m22 * n.m21 + m.m23 * n.m31,
				m.m21 * n.m12 + m.m22 * n.m22 + m.m23 * n.m32, m.m21 * n.m13 + m.m22 * n.m23 + m.m23 * n.m33,
				m.m31 * n.m11 + m.m32 * n.m21 + m.m33 * n.m31, m.m31 * n.m12 + m.m32 * n.m22 + m.m33 * n.m32,
				m.m31 * n.m13 + m.m32 * n.m23 + m.m33 * n.m33);
	}

	public static void mulInto(final WB_M33 m, final WB_M33 n, final WB_M33 result) {
		result.set(m.m11 * n.m11 + m.m12 * n.m21 + m.m13 * n.m31, m.m11 * n.m12 + m.m12 * n.m22 + m.m13 * n.m32,
				m.m11 * n.m13 + m.m12 * n.m23 + m.m13 * n.m33, m.m21 * n.m11 + m.m22 * n.m21 + m.m23 * n.m31,
				m.m21 * n.m12 + m.m22 * n.m22 + m.m23 * n.m32, m.m21 * n.m13 + m.m22 * n.m23 + m.m23 * n.m33,
				m.m31 * n.m11 + m.m32 * n.m21 + m.m33 * n.m31, m.m31 * n.m12 + m.m32 * n.m22 + m.m33 * n.m32,
				m.m31 * n.m13 + m.m32 * n.m23 + m.m33 * n.m33);
	}

	public WB_M33 mul(final WB_M33 n) {
		return new WB_M33(m11 * n.m11 + m12 * n.m21 + m13 * n.m31, m11 * n.m12 + m12 * n.m22 + m13 * n.m32,
				m11 * n.m13 + m12 * n.m23 + m13 * n.m33, m21 * n.m11 + m22 * n.m21 + m23 * n.m31,
				m21 * n.m12 + m22 * n.m22 + m23 * n.m32, m21 * n.m13 + m22 * n.m23 + m23 * n.m33,
				m31 * n.m11 + m32 * n.m21 + m33 * n.m31, m31 * n.m12 + m32 * n.m22 + m33 * n.m32,
				m31 * n.m13 + m32 * n.m23 + m33 * n.m33);
	}

	public void multInto(final WB_M33 n, final WB_M33 result) {
		result.set(m11 * n.m11 + m12 * n.m21 + m13 * n.m31, m11 * n.m12 + m12 * n.m22 + m13 * n.m32,
				m11 * n.m13 + m12 * n.m23 + m13 * n.m33, m21 * n.m11 + m22 * n.m21 + m23 * n.m31,
				m21 * n.m12 + m22 * n.m22 + m23 * n.m32, m21 * n.m13 + m22 * n.m23 + m23 * n.m33,
				m31 * n.m11 + m32 * n.m21 + m33 * n.m31, m31 * n.m12 + m32 * n.m22 + m33 * n.m32,
				m31 * n.m13 + m32 * n.m23 + m33 * n.m33);
	}

	public static void mulInto(final WB_M33 m, final WB_Coord v, final WB_MutableCoord result) {
		result.set(v.xd() * m.m11 + v.yd() * m.m12 + v.zd() * m.m13, v.xd() * m.m21 + v.yd() * m.m22 + v.zd() * m.m23,
				v.xd() * m.m31 + v.yd() * m.m32 + v.zd() * m.m33);
	}

	public static void mulInto(final WB_Coord v, final WB_M33 m, final WB_MutableCoord result) {
		result.set(v.xd() * m.m11 + v.yd() * m.m21 + v.zd() * m.m31, v.xd() * m.m12 + v.yd() * m.m22 + v.zd() * m.m32,
				v.xd() * m.m13 + v.yd() * m.m23 + v.zd() * m.m33);
	}

	public static WB_Point mulToPoint(final WB_M33 m, final WB_Coord v) {
		return new WB_Point(v.xd() * m.m11 + v.yd() * m.m12 + v.zd() * m.m13,
				v.xd() * m.m21 + v.yd() * m.m22 + v.zd() * m.m23, v.xd() * m.m31 + v.yd() * m.m32 + v.zd() * m.m33);
	}

	public static WB_Point mulToPoint(final WB_Coord v, final WB_M33 m) {
		return new WB_Point(v.xd() * m.m11 + v.yd() * m.m21 + v.zd() * m.m31,
				v.xd() * m.m12 + v.yd() * m.m22 + v.zd() * m.m32, v.xd() * m.m13 + v.yd() * m.m23 + v.zd() * m.m33);
	}

	public static WB_Vector mulToVector(final WB_M33 m, final WB_Coord v) {
		return new WB_Vector(v.xd() * m.m11 + v.yd() * m.m12 + v.zd() * m.m13,
				v.xd() * m.m21 + v.yd() * m.m22 + v.zd() * m.m23, v.xd() * m.m31 + v.yd() * m.m32 + v.zd() * m.m33);
	}

	public static WB_Vector mulToVector(final WB_Coord v, final WB_M33 m) {
		return new WB_Vector(v.xd() * m.m11 + v.yd() * m.m21 + v.zd() * m.m31,
				v.xd() * m.m12 + v.yd() * m.m22 + v.zd() * m.m32, v.xd() * m.m13 + v.yd() * m.m23 + v.zd() * m.m33);
	}

	public double det() {
		return m11 * (m22 * m33 - m23 * m32) + m12 * (m23 * m31 - m21 * m33) + m13 * (m21 * m32 - m22 * m31);
	}

	public void transpose() {
		double tmp = m12;
		m12 = m21;
		m21 = tmp;
		tmp = m13;
		m13 = m31;
		m31 = tmp;
		tmp = m23;
		m23 = m32;
		m32 = tmp;
	}

	public WB_M33 getTranspose() {
		return new WB_M33(m11, m21, m31, m12, m22, m32, m13, m23, m33);
	}

	public void transposeInto(final WB_M33 result) {
		result.set(m11, m21, m31, m12, m22, m32, m13, m23, m33);
	}

	public WB_M33 inverse() {
		final double d = det();
		if (WB_Epsilon.isZero(d)) {
			return null;
		}
		final WB_M33 I = new WB_M33(m22 * m33 - m23 * m32, m13 * m32 - m12 * m33, m12 * m23 - m13 * m22,
				m23 * m31 - m21 * m33, m11 * m33 - m13 * m31, m13 * m21 - m11 * m23, m21 * m32 - m22 * m31,
				m12 * m31 - m11 * m32, m11 * m22 - m12 * m21);
		I.div(d);
		return I;
	}

	public static WB_Vector Cramer3(final double a1, final double b1, final double c1, final double d1, final double a2,
			final double b2, final double c2, final double d2, final double a3, final double b3, final double c3,
			final double d3) {
		final WB_M33 m = new WB_M33(a1, b1, c1, a2, b2, c2, a3, b3, c3);
		final double d = m.det();
		if (WB_Epsilon.isZero(d)) {
			return null;
		}
		m.set(d1, b1, c1, d2, b2, c2, d3, b3, c3);
		final double x = m.det();
		m.set(a1, d1, c1, a2, d2, c2, a3, d3, c3);
		final double y = m.det();
		m.set(a1, b1, d1, a2, b2, d2, a3, b3, d3);
		return new WB_Vector(x, y, m.det());
	}

	private double[] symSchur2(final int p, final int q, final double[][] m) {
		final double[] result = new double[2];
		if (!WB_Epsilon.isZero(WB_Math.fastAbs(m[p][q]))) {
			final double r = (m[q][q] - m[p][p]) / (2 * m[p][q]);
			double t;
			if (r >= 0) {
				t = 1 / (r + Math.sqrt(1 + r * r));
			} else {
				t = -1 / (-r + Math.sqrt(1 + r * r));
			}
			result[0] = 1 / Math.sqrt(1 + t * t);
			result[1] = t * result[0];
		} else {
			result[0] = 1;
			result[1] = 0;
		}
		return result;
	}

	public WB_M33 Jacobi() {
		int i, j, n, p, q;
		double prevoff = 0;
		double[] cs = new double[2];
		final double[][] Jm = new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
		final WB_M33 a = get();
		double[][] am = a.toArray();
		final WB_M33 J = new WB_M33(1, 0, 0, 0, 1, 0, 0, 0, 1);
		WB_M33 JT = new WB_M33();
		final WB_M33 v = new WB_M33(1, 0, 0, 0, 1, 0, 0, 0, 1);
		final int MAX_ITERATIONS = 50;
		for (n = 0; n < MAX_ITERATIONS; n++) {
			p = 0;
			q = 1;
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					if (i == j) {
						continue;
					}
					if (WB_Math.fastAbs(am[i][j]) > WB_Math.fastAbs(am[p][q])) {
						p = i;
						q = j;
					}
				}
			}
			cs = symSchur2(p, q, am);
			for (i = 0; i < 3; i++) {
				Jm[i][0] = Jm[i][i] = Jm[i][2];
				Jm[i][i] = 1;
			}
			Jm[p][p] = cs[0];
			Jm[p][q] = cs[1];
			Jm[q][p] = -cs[1];
			Jm[q][q] = cs[0];
			J.set(Jm);
			v.mul(J);
			JT = J.getTranspose();
			JT.mul(a);
			JT.mul(J);
			a.set(JT);
			double off = 0;
			am = a.toArray();
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					if (i == j) {
						continue;
					}
					off += am[i][j] * am[i][j];
				}
			}
			if (n > 2 && off >= prevoff) {
				return a;
			}
			prevoff = off;
		}
		return a;
	}

	public double[][] toArray() {
		return new double[][] { { m11, m12, m13 }, { m21, m22, m23 }, { m31, m32, m33 } };
	}

	@Override
	public String toString() {
		return "M33: " + m11 + ", " + m12 + ", " + m13 + ", " + m21 + ", " + m22 + ", " + m23 + ", " + m31 + ", " + m32
				+ ", " + m33;
	}

	public static WB_M33 covarianceMatrix(final WB_Coord[] points) {
		final int n = points.length;
		final double oon = 1 / (double) n;
		final WB_Point c = new WB_Point();
		WB_Point p = new WB_Point();
		double e00, e11, e22, e01, e02, e12;
		for (int i = 0; i < n; i++) {
			c.addSelf(points[i]);
		}
		c.mulSelf(oon);
		e00 = e11 = e22 = e01 = e02 = e12 = 0;
		for (int i = 0; i < n; i++) {
			p = WB_Point.sub(points[i], c);
			e00 += p.xd() * p.xd();
			e11 += p.yd() * p.yd();
			e22 += p.zd() * p.zd();
			e01 += p.xd() * p.yd();
			e02 += p.xd() * p.zd();
			e12 += p.yd() * p.zd();
		}
		final WB_M33 cov = new WB_M33(e00, e01, e02, e01, e11, e12, e02, e12, e22);
		cov.mul(oon);
		return cov;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_M33)) {
			return false;
		}
		final WB_M33 p = (WB_M33) o;
		if (!WB_Epsilon.isEqualAbs(m11, p.m11)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m12, p.m12)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m13, p.m13)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m21, p.m21)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m22, p.m22)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m23, p.m23)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m31, p.m31)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m32, p.m32)) {
			return false;
		}
		if (!WB_Epsilon.isEqualAbs(m33, p.m33)) {
			return false;
		}
		return true;
	}
}
