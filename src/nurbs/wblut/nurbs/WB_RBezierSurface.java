/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.nurbs;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.math.WB_Bernstein;

/**
 *
 */
public class WB_RBezierSurface extends WB_BezierSurface {
	/**
	 *
	 */
	private final double[][]			weights;
	/**
	 *
	 */
	protected WB_PointHomogeneous[][]	wpoints;

	/**
	 *
	 *
	 * @param controlPoints
	 */
	public WB_RBezierSurface(final WB_Coord[][] controlPoints) {
		super(controlPoints);
		weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				weights[i][j] = 1.0;
			}
		}
		wpoints = new WB_PointHomogeneous[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(points[i][j],
						weights[i][j]);
			}
		}
	}

	/**
	 *
	 *
	 * @param controlPoints
	 * @param weights
	 */
	public WB_RBezierSurface(final WB_Coord[][] controlPoints,
			final double[][] weights) {
		super(controlPoints);
		this.weights = weights;
		wpoints = new WB_PointHomogeneous[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(points[i][j],
						weights[i][j]);
			}
		}
	}

	/**
	 *
	 *
	 * @param controlPoints
	 */
	public WB_RBezierSurface(final WB_PointHomogeneous[][] controlPoints) {
		super(controlPoints);
		weights = new double[n + 1][m + 1];
		wpoints = new WB_PointHomogeneous[n + 1][m + 1];
		for (int i = 0; i < n + 1; i++) {
			for (int j = 0; j < m + 1; j++) {
				wpoints[i][j] = new WB_PointHomogeneous(controlPoints[i][j]);
				weights[i][j] = controlPoints[i][j].wd();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Surface#surfacePoint(double, double)
	 */
	@Override
	public WB_Point surfacePoint(final double u, final double v) {
		final WB_PointHomogeneous S = new WB_PointHomogeneous(0, 0, 0, 0);
		if (n <= m) {
			final WB_PointHomogeneous[] Q = new WB_PointHomogeneous[m + 1];
			double[] B;
			for (int j = 0; j <= m; j++) {
				B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n);
				Q[j] = new WB_PointHomogeneous(0, 0, 0, 0);
				for (int k = 0; k <= n; k++) {
					Q[j].addMulSelf(B[k], wpoints[k][j]);
				}
			}
			B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(v, m);
			for (int k = 0; k <= m; k++) {
				S.addMulSelf(B[k], Q[k]);
			}
		} else {
			final WB_PointHomogeneous[] Q = new WB_PointHomogeneous[n + 1];
			double[] B;
			for (int i = 0; i <= n; i++) {
				B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(v, m);
				Q[i] = new WB_PointHomogeneous(0, 0, 0, 0);
				for (int k = 0; k <= m; k++) {
					Q[i].addMulSelf(B[k], wpoints[i][k]);
				}
			}
			B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n);
			for (int k = 0; k <= n; k++) {
				S.addMulSelf(B[k], Q[k]);
			}
		}
		return new WB_Point(S.project());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_BezierSurface#points()
	 */
	@Override
	public WB_Coord[][] points() {
		return points;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_BezierSurface#n()
	 */
	@Override
	public int n() {
		return n;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_BezierSurface#m()
	 */
	@Override
	public int m() {
		return m;
	}

	@Override
	public double getLowerU() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Curve#upperu()
	 */
	@Override
	public double getUpperU() {
		return 1;
	}

	@Override
	public double getLowerV() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Curve#upperu()
	 */
	@Override
	public double getUpperV() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_BezierSurface#elevateUDegree()
	 */
	@Override
	public WB_RBezierSurface elevateUDegree() {
		final WB_PointHomogeneous[][] npoints = new WB_PointHomogeneous[n + 2][m
				+ 1];
		for (int j = 0; j <= m; j++) {
			npoints[0][j] = wpoints[0][j];
			npoints[n + 1][j] = wpoints[n][j];
			final double inp = 1.0 / (n + 1);
			for (int i = 1; i <= n; i++) {
				npoints[i][j] = WB_PointHomogeneous.interpolate(wpoints[i][j],
						wpoints[i - 1][j], i * inp);
			}
		}
		return new WB_RBezierSurface(npoints);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_BezierSurface#elevateVDegree()
	 */
	@Override
	public WB_RBezierSurface elevateVDegree() {
		final WB_PointHomogeneous[][] npoints = new WB_PointHomogeneous[n + 1][m
				+ 2];
		for (int i = 0; i <= n; i++) {
			npoints[i][0] = wpoints[i][0];
			npoints[i][m + 1] = wpoints[i][m];
			final double inp = 1.0 / (n + 1);
			for (int j = 1; j <= m; j++) {
				npoints[i][j] = WB_PointHomogeneous.interpolate(wpoints[i][j],
						wpoints[i][j - 1], j * inp);
			}
		}
		return new WB_RBezierSurface(npoints);
	}
}