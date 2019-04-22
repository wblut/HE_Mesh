/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.nurbs;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;

/**
 *
 */
public class WB_RBSplineSurface extends WB_BSplineSurface {
	/**
	 *
	 */
	private final double[][] weights;
	/**
	 *
	 */
	protected WB_PointHomogeneous[][] wpoints;

	/**
	 *
	 *
	 * @param controlPoints
	 * @param uknot
	 * @param vknot
	 * @param weights
	 */
	public WB_RBSplineSurface(final WB_Coord[][] controlPoints, final WB_NurbsKnot uknot, final WB_NurbsKnot vknot,
			final double[][] weights) {
		super(controlPoints, uknot, vknot);
		if (weights.length != controlPoints.length || weights[0].length != controlPoints[0].length) {
			throw new IllegalArgumentException("Number of weights doesn't match number of control points.");
		}
		this.weights = weights;
		wpoints = new WB_PointHomogeneous[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(points[i][j], weights[i][j]);

			}
		}
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param udegree
	 * @param vdegree
	 * @param weights
	 */
	public WB_RBSplineSurface(final WB_Point[][] controlPoints, final int udegree, final int vdegree,
			final double[][] weights) {
		super(controlPoints, udegree, vdegree);
		if (weights.length != controlPoints.length || weights[0].length != controlPoints[0].length) {
			throw new IllegalArgumentException("Number of weights doesn't match number of control points.");
		}
		this.weights = weights;
		wpoints = new WB_PointHomogeneous[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(points[i][j], weights[i][j]);
			}
		}
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param udegree
	 * @param vdegree
	 */
	public WB_RBSplineSurface(final WB_Point[][] controlPoints, final int udegree, final int vdegree) {
		super(controlPoints, udegree, vdegree);
		weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				weights[i][j] = 1.0;
			}
		}
		wpoints = new WB_PointHomogeneous[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(points[i][j], weights[i][j]);
			}
		}
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param uknot
	 * @param vknot
	 */
	public WB_RBSplineSurface(final WB_PointHomogeneous[][] controlPoints, final WB_NurbsKnot uknot,
			final WB_NurbsKnot vknot) {
		super();
		if (uknot.n != controlPoints.length - 1) {
			throw new IllegalArgumentException("U knot size and/or degree doesn't match number of control points.");
		}
		if (vknot.n != controlPoints[0].length - 1) {
			throw new IllegalArgumentException("V knot size and/or degree doesn't match number of control points.");
		}
		p = uknot.p();
		n = uknot.n();
		q = vknot.p();
		m = vknot.n();
		this.uknot = uknot;
		this.vknot = vknot;
		wpoints = controlPoints;
		points = new WB_Point[n + 1][m + 1];
		weights = new double[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				points[i][j] = controlPoints[i][j].project();
				weights[i][j] = controlPoints[i][j].wd();
			}
		}
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param w
	 */
	public void setWeight(final int i, final int j, final double w) {
		if (i < 0 || i > n || j < 0 || j > m) {
			throw new IllegalArgumentException("Index outside of weights range.");
		}
		weights[i][j] = w;
		wpoints[i][j] = new WB_PointHomogeneous(points[i][j], weights[i][j]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.nurbs.WB_Surface#surfacePoint(double, double)
	 */
	@Override
	public WB_Point surfacePoint(final double u, final double v) {
		final int uspan = uknot.span(u);
		final double[] Nu = uknot.basisFunctions(uspan, u);
		final int vspan = vknot.span(v);
		final double[] Nv = vknot.basisFunctions(vspan, v);
		WB_PointHomogeneous ttmp = new WB_PointHomogeneous();
		final WB_PointHomogeneous[] tmp = new WB_PointHomogeneous[q + 1];
		for (int el = 0; el <= q; el++) {
			tmp[el] = new WB_PointHomogeneous();
			for (int k = 0; k <= p; k++) {
				ttmp = new WB_PointHomogeneous(wpoints[uspan - p + k][vspan - q + el]);
				tmp[el].addSelf(ttmp.mulSelf(Nu[k]));
			}
		}
		final WB_PointHomogeneous SH = new WB_PointHomogeneous();
		for (int el = 0; el <= q; el++) {
			SH.addSelf(tmp[el].mulSelf(Nv[el]));
		}

		return new WB_Point(SH.project());
	}

	/**
	 *
	 */
	public void updateWeights() {
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(points[i][j], weights[i][j]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#insertUKnot(double)
	 */
	@Override
	public WB_RBSplineSurface insertUKnot(final double u) {
		return insertUKnot(u, 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#insertUKnotMax(double)
	 */
	@Override
	public WB_RBSplineSurface insertUKnotMax(final double u) {
		final int k = uknot.multiplicity(u);
		return insertUKnot(u, p - k);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#insertUKnot(double, int)
	 */
	@Override
	public WB_RBSplineSurface insertUKnot(final double u, final int r) {
		final int nq = n + r;
		final int k = uknot.span(u);
		final int s = uknot.multiplicity(u, k);
		if (r + s > p) {
			throw new IllegalArgumentException("Attempting to increase knot multiplicity above curve degree.");
		}
		final WB_NurbsKnot UQ = new WB_NurbsKnot(n + 1 + r, p);
		for (int i = 0; i <= k; i++) {
			UQ.setValue(i, uknot.value(i));
		}
		for (int i = 1; i <= r; i++) {
			UQ.setValue(k + i, u);
		}
		for (int i = k + 1; i <= n + p + 1; i++) {
			UQ.setValue(i + r, uknot.value(i));
		}
		int L = 0;
		final double[][] alpha = new double[p - s + 1][r + 1];
		for (int j = 1; j <= r; j++) {
			L = k - p + j;
			for (int i = 0; i <= p - j - s; i++) {
				alpha[i][j] = (u - uknot.value(L + i)) / (uknot.value(i + k + 1) - uknot.value(L + i));
			}
		}
		final WB_PointHomogeneous[][] Q = new WB_PointHomogeneous[nq + 1][m + 1];
		final WB_PointHomogeneous[] RW = new WB_PointHomogeneous[p - s + 1];
		for (int row = 0; row <= m; row++) {
			for (int i = 0; i <= k - p; i++) {
				Q[i][row] = new WB_PointHomogeneous(wpoints[i][row]);
			}
			for (int i = k - s; i <= n; i++) {
				Q[i + r][row] = new WB_PointHomogeneous(wpoints[i][row]);
			}
			for (int i = 0; i <= p - s; i++) {
				RW[i] = new WB_PointHomogeneous(wpoints[k - p + i][row]);
			}
			for (int j = 1; j <= r; j++) {
				L = k - p + j;
				for (int i = 0; i <= p - j - s; i++) {
					RW[i] = WB_PointHomogeneous.interpolate(RW[i], RW[i + 1], alpha[i][j]);
				}
				Q[L][row] = RW[0];
				Q[k + r - j - s][row] = RW[p - j - s];
			}
			for (int i = L + 1; i < k - s; i++) {
				Q[i][row] = RW[i - L];
			}
		}
		return new WB_RBSplineSurface(Q, UQ, vknot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#insertVKnot(double)
	 */
	@Override
	public WB_RBSplineSurface insertVKnot(final double v) {
		return insertVKnot(v, 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#insertVKnotMax(double)
	 */
	@Override
	public WB_RBSplineSurface insertVKnotMax(final double v) {
		final int k = vknot.multiplicity(v);
		return insertVKnot(v, q - k);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#insertVKnot(double, int)
	 */
	@Override
	public WB_RBSplineSurface insertVKnot(final double v, final int r) {
		final int mq = m + r;
		final int k = vknot.span(v);
		final int s = vknot.multiplicity(v, k);
		if (r + s > q) {
			throw new IllegalArgumentException("Attempting to increase knot multiplicity above curve degree.");
		}
		final WB_NurbsKnot VQ = new WB_NurbsKnot(m + 1 + r, q);
		for (int i = 0; i <= k; i++) {
			VQ.setValue(i, vknot.value(i));
		}
		for (int i = 1; i <= r; i++) {
			VQ.setValue(k + i, v);
		}
		for (int i = k + 1; i <= m + q + 1; i++) {
			VQ.setValue(i + r, vknot.value(i));
		}
		int L = 0;
		final double[][] alpha = new double[q - s + 1][r + 1];
		for (int j = 1; j <= r; j++) {
			L = k - q + j;
			for (int i = 0; i <= q - j - s; i++) {
				alpha[i][j] = (v - vknot.value(L + i)) / (vknot.value(i + k + 1) - vknot.value(L + i));
			}
		}
		final WB_PointHomogeneous[][] Q = new WB_PointHomogeneous[n + 1][mq + 1];
		final WB_PointHomogeneous[] RW = new WB_PointHomogeneous[q - s + 1];
		for (int col = 0; col <= n; col++) {
			for (int i = 0; i <= k - q; i++) {
				Q[col][i] = new WB_PointHomogeneous(wpoints[col][i]);
			}
			for (int i = k - s; i <= m; i++) {
				Q[col][i + r] = new WB_PointHomogeneous(wpoints[col][i]);
			}
			for (int i = 0; i <= q - s; i++) {
				RW[i] = new WB_PointHomogeneous(wpoints[col][k - q + i]);
			}
			for (int j = 1; j <= r; j++) {
				L = k - q + j;
				for (int i = 0; i <= q - j - s; i++) {
					RW[i] = WB_PointHomogeneous.interpolate(RW[i], RW[i + 1], alpha[i][j]);
				}
				Q[col][L] = RW[0];
				Q[col][k + r - j - s] = RW[q - j - s];
			}
			for (int i = L + 1; i < k - s; i++) {
				Q[col][i] = RW[i - L];
			}
		}
		return new WB_RBSplineSurface(Q, uknot, VQ);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#isoCurveU(double)
	 */
	@Override
	public WB_RBSpline isoCurveU(final double u) {
		final WB_PointHomogeneous[] cpoints = new WB_PointHomogeneous[m + 1];
		final int span = uknot.span(u);
		double[] N;
		for (int j = 0; j <= m; j++) {
			N = uknot.basisFunctions(span, u);
			cpoints[j] = new WB_PointHomogeneous();
			for (int i = 0; i <= p; i++) {
				final WB_PointHomogeneous tmp = wpoints[span - p + i][j];
				cpoints[j].add(N[i] * tmp.xd(), N[i] * tmp.yd(), N[i] * tmp.zd(), N[i] * tmp.wd());
			}
		}
		return new WB_RBSpline(cpoints, vknot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#isoCurveV(double)
	 */
	@Override
	public WB_RBSpline isoCurveV(final double v) {
		final WB_PointHomogeneous[] cpoints = new WB_PointHomogeneous[n + 1];
		final int span = vknot.span(v);
		double[] N;
		for (int i = 0; i <= n; i++) {
			N = vknot.basisFunctions(span, v);
			cpoints[i] = new WB_PointHomogeneous();
			for (int j = 0; j <= q; j++) {
				final WB_PointHomogeneous tmp = wpoints[i][span - q + j];
				cpoints[i].add(N[j] * tmp.xd(), N[j] * tmp.yd(), N[j] * tmp.zd(), N[j] * tmp.wd());
			}
		}
		return new WB_RBSpline(cpoints, uknot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#splitU(double)
	 */
	@Override
	public WB_RBSplineSurface[] splitU(final double u) {
		final WB_RBSplineSurface newRBSplineSurface = insertUKnotMax(u);
		final int k = newRBSplineSurface.uknot().span(u);
		final int km = newRBSplineSurface.uknot().m;
		final WB_NurbsKnot knot1 = new WB_NurbsKnot(k + 1 - p, p);
		for (int i = 0; i < knot1.m; i++) {
			knot1.setValue(i, newRBSplineSurface.uknot().value(i));
		}
		knot1.setValue(knot1.m, u);
		knot1.normalize();
		final WB_PointHomogeneous[][] wpoints1 = new WB_PointHomogeneous[k + 1 - p][m + 1];
		for (int j = 0; j <= m; j++) {
			for (int i = 0; i < k + 1 - p; i++) {
				wpoints1[i][j] = newRBSplineSurface.wpoints[i][j];
			}
		}
		final WB_NurbsKnot knot2 = new WB_NurbsKnot(km - k, p);
		for (int i = 0; i <= p; i++) {
			knot2.setValue(i, u);
		}
		for (int i = k + 1; i <= km; i++) {
			knot2.setValue(i - k + p, newRBSplineSurface.uknot().value(i));
		}
		knot2.normalize();
		final WB_PointHomogeneous[][] wpoints2 = new WB_PointHomogeneous[km - k][m + 1];
		for (int j = 0; j <= m; j++) {
			for (int i = 0; i < km - k; i++) {
				wpoints2[i][j] = newRBSplineSurface.wpoints[k - p + i][j];
			}
		}
		final WB_RBSplineSurface[] splitSurfaces = new WB_RBSplineSurface[2];
		splitSurfaces[0] = new WB_RBSplineSurface(wpoints1, knot1, vknot);
		splitSurfaces[1] = new WB_RBSplineSurface(wpoints2, knot2, vknot);
		return splitSurfaces;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#splitV(double)
	 */
	@Override
	public WB_RBSplineSurface[] splitV(final double v) {
		final WB_RBSplineSurface newRBSplineSurface = insertVKnotMax(v);
		final int k = newRBSplineSurface.vknot().span(v);
		final int km = newRBSplineSurface.vknot().m;
		final WB_NurbsKnot knot1 = new WB_NurbsKnot(k + 1 - q, q);
		for (int i = 0; i < knot1.m; i++) {
			knot1.setValue(i, newRBSplineSurface.vknot().value(i));
		}
		knot1.setValue(knot1.m, v);
		knot1.normalize();
		final WB_PointHomogeneous[][] wpoints1 = new WB_PointHomogeneous[n + 1][k + 1 - q];
		for (int j = 0; j <= n; j++) {
			for (int i = 0; i < k + 1 - q; i++) {
				wpoints1[j][i] = newRBSplineSurface.wpoints[j][i];
			}
		}
		final WB_NurbsKnot knot2 = new WB_NurbsKnot(km - k, q);
		for (int i = 0; i <= q; i++) {
			knot2.setValue(i, v);
		}
		for (int i = k + 1; i <= km; i++) {
			knot2.setValue(i - k + q, newRBSplineSurface.vknot().value(i));
		}
		knot2.normalize();
		final WB_PointHomogeneous[][] wpoints2 = new WB_PointHomogeneous[n + 1][km - k];
		for (int j = 0; j <= n; j++) {
			for (int i = 0; i < km - k; i++) {
				wpoints2[j][i] = newRBSplineSurface.wpoints[j][k - q + i];
			}
		}
		final WB_RBSplineSurface[] splitSurfaces = new WB_RBSplineSurface[2];
		splitSurfaces[0] = new WB_RBSplineSurface(wpoints1, uknot, knot1);
		splitSurfaces[1] = new WB_RBSplineSurface(wpoints2, uknot, knot2);
		return splitSurfaces;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_BSplineSurface#split(double, double)
	 */
	@Override
	public WB_RBSplineSurface[] split(final double u, final double v) {
		final WB_RBSplineSurface[] splitSurfaces = new WB_RBSplineSurface[4];
		WB_RBSplineSurface[] tmp = new WB_RBSplineSurface[2];
		tmp = splitU(u);
		splitSurfaces[0] = tmp[0];
		splitSurfaces[2] = tmp[1];
		tmp = splitSurfaces[0].splitV(v);
		splitSurfaces[0] = tmp[0];
		splitSurfaces[1] = tmp[1];
		tmp = splitSurfaces[2].splitV(v);
		splitSurfaces[2] = tmp[0];
		splitSurfaces[3] = tmp[1];
		return splitSurfaces;
	}
}
