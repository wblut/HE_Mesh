/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.nurbs;

import java.util.ArrayList;
import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Curve;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.geom.WB_Vector;
import wblut.math.WB_Binomial;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_BSpline implements WB_Curve {
	/**
	 *
	 */
	private static WB_GeometryFactory	gf	= new WB_GeometryFactory();
	/**
	 *
	 */
	protected WB_NurbsKnot				knot;
	/**
	 *
	 */
	protected WB_Coord[]				points;
	/**
	 *
	 */
	protected int						p;
	/**
	 *
	 */
	protected int						n;

	/**
	 *
	 */
	public WB_BSpline() {
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param knot
	 */
	public WB_BSpline(final WB_Coord[] controlPoints, final WB_NurbsKnot knot) {
		if (knot.n != controlPoints.length - 1) {
			throw new IllegalArgumentException(
					"Knot size and/or order doesn't match number of control points.");
		}
		p = knot.p();
		n = knot.n();
		this.knot = knot;
		points = controlPoints;
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param knot
	 */
	public WB_BSpline(final WB_PointHomogeneous[] controlPoints,
			final WB_NurbsKnot knot) {
		if (knot.n != controlPoints.length - 1) {
			throw new IllegalArgumentException(
					"Knot size and/or order doesn't match number of control points.");
		}
		p = knot.p();
		n = knot.n();
		this.knot = knot;
		points = new WB_Point[n + 1];
		for (int i = 0; i < n + 1; i++) {
			points[i] = new WB_Point(controlPoints[i].project());
		}
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param order
	 */
	public WB_BSpline(final WB_Coord[] controlPoints, final int order) {
		knot = new WB_NurbsKnot(controlPoints.length, order);
		p = knot.p();
		n = knot.n();
		points = controlPoints;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Curve#curvePoint(double)
	 */
	@Override
	public WB_Point getPointOnCurve(final double u) {
		final int span = knot.span(u);
		final double[] N = knot.basisFunctions(span, u);
		final WB_Point C = new WB_Point();
		final int p = knot.p();
		for (int i = 0; i <= p; i++) {
			final WB_Coord tmp = points[span - p + i];
			C.addSelf(N[i] * tmp.xd(), N[i] * tmp.yd(), N[i] * tmp.zd());
		}
		return C;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curveDirection(double)
	 */
	@Override
	public WB_Vector getDirectionOnCurve(final double u) {
		WB_Vector v = firstDerivative(u);
		v.normalizeSelf();
		return v;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curveDerivative(double)
	 */
	@Override
	public WB_Vector getDerivative(final double u) {
		return firstDerivative(u);
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_BSpline insertKnot(final double u) {
		return insertKnot(u, 1);
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_BSpline insertKnotMax(final double u) {
		final int k = knot.multiplicity(u);
		return insertKnot(u, p - k);
	}

	/**
	 *
	 *
	 * @param u
	 * @param r
	 * @return
	 */
	public WB_BSpline insertKnot(final double u, final int r) {
		final int mp = n + p + 1;
		final int nq = n + r;
		final int k = knot.span(u);
		final int s = knot.multiplicity(u, k);
		if (r + s > p) {
			throw new IllegalArgumentException(
					"Attempting to increase knot multiplicity above curve order.");
		}
		final WB_NurbsKnot UQ = new WB_NurbsKnot(n + 1 + r, p);
		final WB_Coord[] Q = new WB_Point[nq + 1];
		for (int i = 0; i <= k; i++) {
			UQ.setValue(i, knot.value(i));
		}
		for (int i = 1; i <= r; i++) {
			UQ.setValue(k + i, u);
		}
		for (int i = k + 1; i <= mp; i++) {
			UQ.setValue(i + r, knot.value(i));
		}
		for (int i = 0; i <= k - p; i++) {
			Q[i] = points[i];
		}
		for (int i = k - s; i <= n; i++) {
			Q[i + r] = points[i];
		}
		final WB_Point[] RW = new WB_Point[p + 1];
		for (int i = 0; i <= p - s; i++) {
			RW[i] = new WB_Point(points[k - p + i]);
		}
		int L = 0;
		for (int j = 1; j <= r; j++) {
			L = k - p + j;
			for (int i = 0; i <= p - j - s; i++) {
				final double alpha = (u - knot.value(L + i))
						/ (knot.value(i + k + 1) - knot.value(L + i));
				RW[i] = gf.createInterpolatedPoint(RW[i], RW[i + 1], alpha);
			}
			Q[L] = RW[0];
			Q[k + r - j - s] = RW[p - j - s];
		}
		for (int i = L + 1; i < k - s; i++) {
			Q[i] = RW[i - L];
		}
		return new WB_BSpline(Q, UQ);
	}

	/**
	 *
	 *
	 * @param K
	 * @return
	 */
	public WB_BSpline refineKnot(final WB_NurbsKnot K) {
		double[][] multVal = K.multVal();
		List<Double> newVal = new ArrayList<Double>();
		for (int i = 0; i < multVal.length; i++) {
			int orig = knot.multiplicity(multVal[i][0]);
			int target = (int) multVal[i][1];
			int rep = Math.max(0, target - orig);
			for (int j = 0; j < rep; j++) {
				newVal.add(multVal[i][0]);
			}
		}
		if (newVal.size() == 0) {
			return this;
		}
		double[] fX = new double[newVal.size()];
		for (int j = 0; j < newVal.size(); j++) {
			fX[j] = newVal.get(j);
		}
		return refineKnotRestricted(fX);
	}

	/**
	 *
	 *
	 * @param X
	 * @return
	 */
	private WB_BSpline refineKnotRestricted(final double[] X) {
		final int r = X.length - 1;
		final int a = knot.span(X[0]);
		final int b = knot.span(X[r]) + 1;
		final WB_Point[] Q = new WB_Point[n + r + 2];
		final WB_NurbsKnot Ubar = new WB_NurbsKnot(n + r + 2, p);
		for (int j = 0; j <= a - p; j++) {
			Q[j] = new WB_Point(points[j]);
		}
		for (int j = b - 1; j <= n; j++) {
			Q[j + r + 1] = new WB_Point(points[j]);
		}
		for (int j = 0; j <= a; j++) {
			Ubar.setValue(j, knot.value(j));
		}
		for (int j = b + p; j <= knot.m; j++) {
			Ubar.setValue(j + r + 1, knot.value(j));
		}
		int i = b + p - 1;
		int k = b + p + r;
		for (int j = r; j >= 0; j--) {
			while (X[j] <= knot.value(i) && i > a) {
				Q[k - p - 1] = new WB_Point(points[i - p - 1]);
				Ubar.setValue(k, knot.value(i));
				k = k - 1;
				i = i - 1;
			}
			Q[k - p - 1] = new WB_Point(Q[k - p]);
			for (int el = 1; el <= p; el++) {
				final int ind = k - p + el;
				double alpha = Ubar.value(k + el) - X[j];
				if (WB_Epsilon.isZero(alpha)) {
					Q[ind - 1] = new WB_Point(Q[ind]);
				} else {
					alpha /= Ubar.value(k + el) - knot.value(i - p + el);
					Q[ind - 1] = gf.createInterpolatedPoint(Q[ind], Q[ind - 1],
							alpha);
				}
			}
			Ubar.setValue(k, X[j]);
			k--;
		}
		return new WB_BSpline(Q, Ubar);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord[] points() {
		return points;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int p() {
		return p;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int n() {
		return n;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_NurbsKnot knot() {
		return knot;
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_BSpline[] split(final double u) {
		final WB_BSpline newBSpline = insertKnotMax(u);
		final int k = newBSpline.knot().span(u);
		final int m = newBSpline.knot().m;
		final WB_NurbsKnot knot1 = new WB_NurbsKnot(k + 1 - p, p);
		for (int i = 0; i < knot1.m; i++) {
			knot1.setValue(i, newBSpline.knot().value(i));
		}
		knot1.setValue(knot1.m, u);
		knot1.normalize();
		final WB_Coord[] points1 = new WB_Point[k + 1 - p];
		for (int i = 0; i < k + 1 - p; i++) {
			points1[i] = newBSpline.points[i];
		}
		final WB_NurbsKnot knot2 = new WB_NurbsKnot(m - k, p);
		for (int i = 0; i <= p; i++) {
			knot2.setValue(i, u);
		}
		for (int i = k + 1; i <= m; i++) {
			knot2.setValue(i - k + p, newBSpline.knot().value(i));
		}
		knot2.normalize();
		final WB_Coord[] points2 = new WB_Point[m - k];
		for (int i = 0; i < m - k; i++) {
			points2[i] = newBSpline.points[k - p + i];
		}
		final WB_BSpline[] splitCurves = new WB_BSpline[2];
		splitCurves[0] = new WB_BSpline(points1, knot1);
		splitCurves[1] = new WB_BSpline(points2, knot2);
		return splitCurves;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public WB_BSpline elevateDegree(final int t) {
		final int m = n + p + 1;
		final int ph = p + t;
		final int ph2 = ph / 2;
		final double[][] bezalfs = new double[p + t + 1][p + 1];
		final WB_Point[] bpts = new WB_Point[p + 1];
		final WB_Point[] ebpts = new WB_Point[p + t + 1];
		final WB_Point[] nextbpts = new WB_Point[p - 1];
		final double[] alfs = new double[p - 1];
		bezalfs[0][0] = bezalfs[ph][p] = 1.0;
		int mpi;
		for (int i = 1; i <= ph2; i++) {
			final double inv = 1.0 / WB_Binomial.bin(ph, i);
			mpi = Math.min(p, i);
			for (int j = Math.max(0, i - t); j <= mpi; j++) {
				bezalfs[i][j] = inv * WB_Binomial.bin(p, j)
						* WB_Binomial.bin(t, i - j);
			}
		}
		for (int i = ph2 + 1; i <= ph - 1; i++) {
			mpi = Math.min(p, i);
			for (int j = Math.max(0, i - t); j <= mpi; j++) {
				bezalfs[i][j] = bezalfs[ph - i][p - j];
			}
		}
		int kind = ph + 1;
		int r = -1;
		int a = p;
		int b = p + 1;
		int cind = 1;
		int i = 0;
		int j = 0;
		int k = 0;
		int lbz, rbz, oldr;
		double ua = knot.value(0);
		final int s = knot.s();
		final WB_Point[] Q = new WB_Point[n + t * (s + 1) + 1];
		Q[0] = new WB_Point(points[0]);
		final WB_NurbsKnot Uh = new WB_NurbsKnot(n + t * (s + 1) + 1, ph);
		for (i = 0; i <= ph; i++) {
			Uh.setValue(i, ua);
		}
		for (i = 0; i <= p; i++) {
			bpts[i] = new WB_Point(points[i]);
		}
		while (b < m) {
			i = b;
			while (b < m && knot.value(b) == knot.value(b + 1)) {
				b++;
			}
			final int mul = b - i + 1;
			final double ub = knot.value(b);
			oldr = r;
			r = p - mul;
			if (oldr > 0) {
				lbz = (oldr + 2) / 2;
			} else {
				lbz = 1;
			}
			if (r > 0) {
				rbz = ph - (r + 1) / 2;
			} else {
				rbz = ph;
			}
			if (r > 0) {
				final double numer = ub - ua;
				for (k = p; k > mul; k--) {
					alfs[k - mul - 1] = numer / (knot.value(a + k) - ua);
				}
				for (j = 1; j <= r; j++) {
					final int save = r - j;
					final int sj = mul + j;
					for (k = p; k >= sj; k--) {
						bpts[k] = gf.createInterpolatedPoint(bpts[k - 1],
								bpts[k], alfs[k - sj]);
					}
					nextbpts[save] = new WB_Point(bpts[p]);
				}
			}
			for (i = lbz; i <= ph; i++) {
				ebpts[i] = new WB_Point();
				mpi = Math.min(p, i);
				for (j = Math.max(0, i - t); j <= mpi; j++) {
					ebpts[i].addMulSelf(bezalfs[i][j], bpts[j]);
				}
			}
			if (oldr > 1) {
				int first = kind - 2;
				int last = kind;
				final double den = ub - ua;
				final double bet = (ub - Uh.value(kind - 1)) / den;
				for (int tr = 1; tr < oldr; tr++) {
					i = first;
					j = last;
					int kj = j - kind + 1;
					while (j - i > tr) {
						if (i < cind) {
							final double alf = (ub - Uh.value(i))
									/ (ua - Uh.value(i));
							Q[i] = gf.createInterpolatedPoint(Q[i - 1], Q[i],
									alf);
						}
						if (j >= lbz) {
							if (j - tr <= kind - ph + oldr) {
								final double gam = (ub - Uh.value(j - tr))
										/ den;
								ebpts[kj] = gf.createInterpolatedPoint(
										ebpts[kj + 1], ebpts[kj], gam);
							} else {
								ebpts[kj] = gf.createInterpolatedPoint(
										ebpts[kj + 1], ebpts[kj], bet);
							}
						}
						i++;
						j--;
						kj--;
					}
					first--;
					last++;
				}
			}
			if (a != p) {
				for (i = 0; i < ph - oldr; i++) {
					Uh.setValue(kind, ua);
					kind++;
				}
			}
			for (j = lbz; j <= rbz; j++) {
				Q[cind] = new WB_Point(ebpts[j]);
				cind++;
			}
			if (b < m) {
				for (j = 0; j < r; j++) {
					bpts[j] = nextbpts[j];
				}
				for (j = r; j <= p; j++) {
					bpts[j] = new WB_Point(points[b - p + j]);
				}
				a = b;
				b++;
				ua = ub;
			} else {
				for (i = 0; i <= ph; i++) {
					Uh.setValue(kind + i, ub);
				}
			}
		}
		return new WB_BSpline(Q, Uh);
	}

	/**
	 *
	 *
	 * @param d
	 * @param r1
	 * @param r2
	 * @return
	 */
	public WB_Coord[][] curveDerivCPoints(final int d, final int r1,
			final int r2) {
		final WB_Coord[][] PK = new WB_Point[d + 1][r2 - r1 + 1];
		final int r = r2 - r1;
		for (int i = 0; i <= r; i++) {
			PK[0][i] = points[r1 + i];
		}
		for (int k = 1; k <= d; k++) {
			final int tmp = p - k + 1;
			for (int i = 0; i <= r - k; i++) {
				PK[k][i] = WB_Point.sub(PK[k - 1][i + 1], PK[k - 1][i])
						.mulSelf(tmp / (knot.value(r1 + i + p + 1)
								- knot.value(r1 + i + k)));
			}
		}
		return PK;
	}

	/**
	 *
	 *
	 * @param u
	 * @param d
	 * @return
	 */
	public WB_Coord[] curveDerivs(final double u, final int d) {
		final WB_Point[] CK = new WB_Point[d + 1];
		final int du = Math.min(d, p);
		for (int k = p + 1; k <= d; k++) {
			CK[k] = new WB_Point();
		}
		final int span = knot.span(u);
		final double[][] N = knot.allBasisFunctions(span, u, p);
		final WB_Coord[][] PK = curveDerivCPoints(du, span - p, span);
		for (int k = 0; k <= du; k++) {
			CK[k] = new WB_Point();
			for (int j = 0; j <= p - k; j++) {
				CK[k].addSelf(WB_Point.mul(PK[k][j], N[j][p - k]));
			}
		}
		return CK;
	}

	/**
	 *
	 *
	 * @param u
	 * @param d
	 * @return
	 */
	public WB_Point[] curveDerivsNorm(final double u, final int d) {
		final WB_Point[] CK = new WB_Point[d + 1];
		final int du = Math.min(d, p);
		for (int k = p + 1; k <= d; k++) {
			CK[k] = new WB_Point();
		}
		final int span = knot.span(u);
		final double[][] N = knot.allBasisFunctions(span, u, p);
		final WB_Coord[][] PK = curveDerivCPoints(du, span - p, span);
		for (int k = 0; k <= du; k++) {
			CK[k] = new WB_Point();
			for (int j = 0; j <= p - k; j++) {
				CK[k].addSelf(WB_Point.mul(PK[k][j], N[j][p - k]));
			}
			CK[k].normalizeSelf();
		}
		return CK;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Curve#curveFirstDeriv(double)
	 */
	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_Vector firstDerivative(final double u) {
		final WB_Vector[] CK = new WB_Vector[2];
		if (p == 0) {
			return null;
		}
		final int span = knot.span(u);
		final double[][] N = knot.allBasisFunctions(span, u, p);
		final WB_Coord[][] PK = curveDerivCPoints(1, span - p, span);
		for (int k = 0; k <= 1; k++) {
			CK[k] = new WB_Vector();
			for (int j = 0; j <= p - k; j++) {
				CK[k].addSelf(WB_Point.mul(PK[k][j], N[j][p - k]));
			}
			CK[k].normalizeSelf();
		}
		return CK[1];
	}

	@Override
	public double getLowerU() {
		return knot.value(0);
	}

	@Override
	public double getUpperU() {
		return knot.value(knot.m);
	}
}
