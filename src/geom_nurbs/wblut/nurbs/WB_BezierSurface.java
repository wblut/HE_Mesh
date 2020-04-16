package wblut.nurbs;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.geom.WB_Surface;
import wblut.hemesh.HEC_FromFacelist;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Bernstein;

public class WB_BezierSurface implements WB_Surface {
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	protected WB_Coord[][] points;
	protected int n;
	protected int m;

	public WB_BezierSurface() {
	}

	public WB_BezierSurface(final WB_Coord[][] controlPoints) {
		n = controlPoints.length - 1;
		m = controlPoints[0].length - 1;
		points = controlPoints;
	}

	public WB_BezierSurface(final WB_PointHomogeneous[][] controlPoints) {
		n = controlPoints.length;
		m = controlPoints[0].length;
		points = new WB_Point[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = controlPoints[i][j].project();
			}
		}
	}

	@Override
	public WB_Point surfacePoint(final double u, final double v) {
		final WB_Point S = new WB_Point();
		if (n <= m) {
			final WB_Point[] Q = new WB_Point[m + 1];
			double[] B;
			for (int j = 0; j <= m; j++) {
				B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n);
				Q[j] = new WB_Point();
				for (int k = 0; k <= n; k++) {
					Q[j].addMulSelf(B[k], points[k][j]);
				}
			}
			B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(v, m);
			for (int k = 0; k <= m; k++) {
				S.addMulSelf(B[k], Q[k]);
			}
		} else {
			final WB_Point[] Q = new WB_Point[n + 1];
			double[] B;
			for (int i = 0; i <= n; i++) {
				B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(v, m);
				Q[i] = new WB_Point();
				for (int k = 0; k <= m; k++) {
					Q[i].addMulSelf(B[k], points[i][k]);
				}
			}
			B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n);
			for (int k = 0; k <= n; k++) {
				S.addMulSelf(B[k], Q[k]);
			}
		}
		return S;
	}

	public WB_Coord[][] points() {
		return points;
	}

	public int n() {
		return n;
	}

	public int m() {
		return m;
	}

	@Override
	public double getLowerU() {
		return 0;
	}

	@Override
	public double getUpperU() {
		return 1;
	}

	@Override
	public double getLowerV() {
		return 0;
	}

	@Override
	public double getUpperV() {
		return 1;
	}

	public WB_BezierSurface elevateUDegree() {
		final WB_Coord[][] npoints = new WB_Point[n + 2][m + 1];
		for (int j = 0; j <= m; j++) {
			npoints[0][j] = points[0][j];
			npoints[n + 1][j] = points[n][j];
			final double inp = 1.0 / (n + 1);
			for (int i = 1; i <= n; i++) {
				npoints[i][j] = gf.createInterpolatedPoint(points[i][j], points[i - 1][j], i * inp);
			}
		}
		return new WB_BezierSurface(npoints);
	}

	public WB_BezierSurface elevateVDegree() {
		final WB_Coord[][] npoints = new WB_Point[n + 1][m + 2];
		for (int i = 0; i <= n; i++) {
			npoints[i][0] = points[i][0];
			npoints[i][m + 1] = points[i][m];
			final double inp = 1.0 / (n + 1);
			for (int j = 1; j <= m; j++) {
				npoints[i][j] = gf.createInterpolatedPoint(points[i][j], points[i][j - 1], j * inp);
			}
		}
		return new WB_BezierSurface(npoints);
	}

	public HE_Mesh toControlHemesh() {
		final WB_Coord[] cpoints = new WB_Point[(n + 1) * (m + 1)];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				cpoints[i + (n + 1) * j] = points[i][j];
			}
		}
		final int[][] faces = new int[n * m][4];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				faces[i + n * j][0] = i + (n + 1) * j;
				faces[i + n * j][1] = i + 1 + (n + 1) * j;
				faces[i + n * j][2] = i + 1 + (n + 1) * (j + 1);
				faces[i + n * j][3] = i + (n + 1) * (j + 1);
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setFaces(faces).setVertices(cpoints);
		return new HE_Mesh(fl);
	}
}