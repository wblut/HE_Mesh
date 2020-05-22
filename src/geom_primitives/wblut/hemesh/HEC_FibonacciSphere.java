// http://extremelearning.com.au/evenly-distributing-points-on-a-sphere/
package wblut.hemesh;

import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEC_FibonacciSphere extends HEC_Creator {
	/**  */
	private double R;
	/**  */
	private int N;
	// private static final double PHI = 0.5 * (1.0 + Math.sqrt(5.0));

	/**
	 *
	 */
	public HEC_FibonacciSphere() {
		super();
		R = 1.0;
		N = 100;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_FibonacciSphere setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param N
	 * @return
	 */
	public HEC_FibonacciSphere setN(final int N) {
		this.N = N;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh createBase() {
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		if (N < 4) {
			return new HE_Mesh();
		}
		if (WB_Epsilon.isZero(R)) {
			return new HE_Mesh();
		}
		final WB_Point[] points = new WB_Point[N];
		for (int i = 0; i < N; i++) {
			points[i] = new WB_Point(Math.acos(1 - 2 * (i + 0.5) / N), Math.PI * (1.0 + Math.sqrt(5.0)) * (i + 0.5));
		}
		for (int i = 0; i < N; i++) {
			points[i] = gf.createPointFromSpherical(R, points[i].xd(), points[i].yd());
		}
		final HEC_Creator creator = new HEC_ConvexHull().setPoints(points);
		return creator.createBase();
	}
}
