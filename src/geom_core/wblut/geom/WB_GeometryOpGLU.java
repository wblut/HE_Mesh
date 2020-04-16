package wblut.geom;

import java.util.List;

public class WB_GeometryOpGLU {
	public static boolean GLUvertEq(final WB_Coord u, final WB_Coord v) {
		return u.xd() == v.xd() && u.yd() == v.yd();
	}

	public static boolean GLUvertLeq(final WB_Coord u, final WB_Coord v) {
		return u.xd() < v.xd() || u.xd() == v.xd() && u.yd() < v.yd();
	}

	public static boolean GLUtransLeq(final WB_Coord u, final WB_Coord v) {
		return u.yd() < v.yd() || u.yd() == v.yd() && u.xd() < v.xd();
	}

	public static boolean GLUedgeGoesLeft(final WB_Segment e) {
		return GLUvertLeq(e.getEndpoint(), e.getOrigin());
	}

	public static boolean GLUedgeGoesRight(final WB_Segment e) {
		return GLUvertLeq(e.getOrigin(), e.getEndpoint());
	}

	public static double GLUedgeEval(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		double gapL, gapR;
		assert GLUvertLeq(u, v) && GLUvertLeq(v, w);
		gapL = v.xd() - u.xd();
		gapR = w.xd() - v.xd();
		if (gapL + gapR > 0) {
			if (gapL < gapR) {
				return v.yd() - u.yd() + (u.yd() - w.yd()) * (gapL / (gapL + gapR));
			} else {
				return v.yd() - w.yd() + (w.yd() - u.yd()) * (gapR / (gapL + gapR));
			}
		}
		return 0;
	}

	public static double GLUedgeSign(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		double gapL, gapR;
		assert GLUvertLeq(u, v) && GLUvertLeq(v, w);
		gapL = v.xd() - u.xd();
		gapR = w.xd() - v.xd();
		if (gapL + gapR > 0) {
			return (v.yd() - w.yd()) * gapL + (v.yd() - u.yd()) * gapR;
		}
		return 0;
	}

	public static double GLUtransEval(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		double gapL, gapR;
		assert GLUtransLeq(u, v) && GLUtransLeq(v, w);
		gapL = v.yd() - u.yd();
		gapR = w.yd() - v.yd();
		if (gapL + gapR > 0) {
			if (gapL < gapR) {
				return v.xd() - u.xd() + (u.xd() - w.xd()) * (gapL / (gapL + gapR));
			} else {
				return v.xd() - w.xd() + (w.xd() - u.xd()) * (gapR / (gapL + gapR));
			}
		}
		return 0;
	}

	public static double GLUtransSign(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		double gapL, gapR;
		assert GLUtransLeq(u, v) && GLUtransLeq(v, w);
		gapL = v.yd() - u.yd();
		gapR = w.yd() - v.yd();
		if (gapL + gapR > 0) {
			return (v.xd() - w.xd()) * gapL + (v.xd() - u.xd()) * gapR;
		}
		return 0;
	}

	public static boolean GLUvertCCW(final WB_Coord u, final WB_Coord v, final WB_Coord w) {
		return u.xd() * (v.yd() - w.yd()) + v.xd() * (w.yd() - u.yd()) + w.xd() * (u.yd() - v.yd()) >= 0;
	}

	public static double GLUrealInterpolate(double a, final double x, double b, final double y) {
		a = a < 0 ? 0 : a;
		b = b < 0 ? 0 : b;
		return a <= b ? b == 0 ? (x + y) / 2 : x + (y - x) * (a / (a + b)) : y + (x - y) * (b / (a + b));
	}

	public static double GLUinterpolate(final double a, final double x, final double b, final double y) {
		return GLUrealInterpolate(a, x, b, y);
	}

	public static WB_Coord GLUedgeIntersect(WB_Coord o1, WB_Coord d1, WB_Coord o2, WB_Coord d2) {
		double z1, z2;
		if (!GLUvertLeq(o1, d1)) {
			final WB_Coord t = o1;
			o1 = d1;
			d1 = t;
		}
		if (!GLUvertLeq(o2, d2)) {
			final WB_Coord t = o2;
			o2 = d2;
			d2 = t;
		}
		if (!GLUvertLeq(o1, o2)) {
			WB_Coord t = o1;
			o1 = o2;
			o2 = t;
			t = d1;
			d1 = d2;
			d2 = t;
		}
		final WB_Point v = new WB_Point();
		if (!GLUvertLeq(o2, d1)) {
			v.setX((o2.xd() + d1.xd()) / 2);
		} else if (GLUvertLeq(d1, d2)) {
			z1 = GLUedgeEval(o1, o2, d1);
			z2 = GLUedgeEval(o2, d1, d2);
			if (z1 + z2 < 0) {
				z1 = -z1;
				z2 = -z2;
			}
			v.setX(GLUinterpolate(z1, o2.xd(), z2, d1.xd()));
		} else {
			z1 = GLUedgeSign(o1, o2, d1);
			z2 = -GLUedgeSign(o1, d2, d1);
			if (z1 + z2 < 0) {
				z1 = -z1;
				z2 = -z2;
			}
			v.setX(GLUinterpolate(z1, o2.xd(), z2, d2.yd()));
		}
		if (!GLUtransLeq(o1, d1)) {
			final WB_Coord t = o1;
			o1 = d1;
			d1 = t;
		}
		if (!GLUtransLeq(o2, d2)) {
			final WB_Coord t = o2;
			o2 = d2;
			d2 = t;
		}
		if (!GLUtransLeq(o1, o2)) {
			WB_Coord t = o1;
			o1 = o2;
			o2 = t;
			t = d1;
			d1 = d2;
			d2 = t;
		}
		if (!GLUtransLeq(o2, d1)) {
			v.setY((o2.yd() + d1.yd()) / 2);
		} else if (GLUtransLeq(d1, d2)) {
			z1 = GLUtransEval(o1, o2, d1);
			z2 = GLUtransEval(o2, d1, d2);
			if (z1 + z2 < 0) {
				z1 = -z1;
				z2 = -z2;
			}
			v.setY(GLUinterpolate(z1, o2.yd(), z2, d1.yd()));
		} else {
			z1 = GLUtransSign(o1, o2, d1);
			z2 = -GLUtransSign(o1, d2, d1);
			if (z1 + z2 < 0) {
				z1 = -z1;
				z2 = -z2;
			}
			v.setY(GLUinterpolate(z1, o2.yd(), z2, d2.yd()));
		}
		return v;
	}

	static double ABS(final double v) {
		return v < 0 ? -v : v;
	}

	public static int GLUlongAxis(final WB_Coord v) {
		int i = 0;
		if (ABS(v.yd()) > ABS(v.xd())) {
			i = 1;
		}
		if (ABS(v.zd()) > ABS(v.getd(i))) {
			i = 2;
		}
		return i;
	}

	public static int GLUlongAxis(final double[] v) {
		int i = 0;
		if (ABS(v[1]) > ABS(v[0])) {
			i = 1;
		}
		if (ABS(v[2]) > ABS(v[i])) {
			i = 2;
		}
		return i;
	}

	public static WB_Coord GLUcomputeNormal(final List<? extends WB_Coord> vertices) {
		WB_Coord v1, v2;
		double c, tLen2, maxLen2;
		double[] maxVal, minVal, d1, d2, norm, tNorm;
		WB_Coord[] maxVert, minVert;
		int i;
		maxVal = new double[3];
		maxVal[0] = maxVal[1] = maxVal[2] = Double.NEGATIVE_INFINITY;
		minVal = new double[3];
		minVal[0] = minVal[1] = minVal[2] = Double.POSITIVE_INFINITY;
		maxVert = new WB_Coord[3];
		minVert = new WB_Coord[3];
		for (final WB_Coord v : vertices) {
			for (i = 0; i < 3; ++i) {
				c = v.getd(i);
				if (c < minVal[i]) {
					minVal[i] = c;
					minVert[i] = v;
				}
				if (c > maxVal[i]) {
					maxVal[i] = c;
					maxVert[i] = v;
				}
			}
		}
		i = 0;
		if (maxVal[1] - minVal[1] > maxVal[0] - minVal[0]) {
			i = 1;
		}
		if (maxVal[2] - minVal[2] > maxVal[i] - minVal[i]) {
			i = 2;
		}
		if (minVal[i] >= maxVal[i]) {
			return new WB_Vector(0, 0, 1);
		}
		maxLen2 = 0;
		v1 = minVert[i];
		v2 = maxVert[i];
		d1 = new double[3];
		d2 = new double[3];
		tNorm = new double[3];
		norm = new double[3];
		d1[0] = v1.xd() - v2.xd();
		d1[1] = v1.yd() - v2.yd();
		d1[2] = v1.zd() - v2.zd();
		for (final WB_Coord v : vertices) {
			d2[0] = v.xd() - v2.xd();
			d2[1] = v.yd() - v2.yd();
			d2[2] = v.zd() - v2.zd();
			tNorm[0] = d1[1] * d2[2] - d1[2] * d2[1];
			tNorm[1] = d1[2] * d2[0] - d1[0] * d2[2];
			tNorm[2] = d1[0] * d2[1] - d1[1] * d2[0];
			tLen2 = tNorm[0] * tNorm[0] + tNorm[1] * tNorm[1] + tNorm[2] * tNorm[2];
			if (tLen2 > maxLen2) {
				maxLen2 = tLen2;
				norm[0] = tNorm[0];
				norm[1] = tNorm[1];
				norm[2] = tNorm[2];
			}
		}
		if (maxLen2 <= 0) {
			norm[0] = norm[1] = norm[2] = 0;
			norm[GLUlongAxis(d1)] = 1;
		}
		return new WB_Vector(norm);
	}
}
