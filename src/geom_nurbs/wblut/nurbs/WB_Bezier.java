package wblut.nurbs;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Curve;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.geom.WB_Vector;
import wblut.math.WB_Bernstein;

/**
 *
 */
public class WB_Bezier implements WB_Curve {
	/**  */
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	/**  */
	protected WB_CoordCollection points;
	/**  */
	protected int n;

	/**
	 *
	 *
	 * @param controlPoints
	 */
	public WB_Bezier(final WB_CoordCollection controlPoints) {
		points = controlPoints;
		n = points.size() - 1;
	}

	/**
	 *
	 *
	 * @param controlPoints
	 */
	public WB_Bezier(final WB_PointHomogeneous[] controlPoints) {
		n = controlPoints.length - 1;
		final WB_Point[] tmpPoints = new WB_Point[n + 1];
		for (int i = 0; i < n + 1; i++) {
			tmpPoints[i] = new WB_Point(controlPoints[i].project());
		}
		points = WB_CoordCollection.getCollection(tmpPoints);
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	@Override
	public WB_Point getPointOnCurve(final double u) {
		final WB_Point C = new WB_Point();
		if (n <= 0) {
			return C;
		}
		final double[] B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n);
		for (int k = 0; k <= n; k++) {
			C.addMulSelf(B[k], points.get(k));
		}
		return C;
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_Vector firstDerivative(final double u) {
		final WB_Vector Cp = new WB_Vector();
		if (n <= 0) {
			return Cp;
		}
		final double[] B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n - 1);
		for (int k = 0; k <= n - 1; k++) {
			Cp.addMulSelf(B[k], WB_Point.sub(points.get(k + 1), points.get(k)));
		}
		Cp.mulSelf(n);
		return Cp;
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	@Override
	public WB_Vector getDirectionOnCurve(final double u) {
		final WB_Vector v = firstDerivative(u);
		v.normalizeSelf();
		return v;
	}

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	@Override
	public WB_Vector getDerivative(final double u) {
		return firstDerivative(u);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double n() {
		return n;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLowerU() {
		return 0;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getUpperU() {
		return 1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Bezier elevateDegree() {
		if (n <= 0) {
			return null;
		}
		final WB_Coord[] npoints = new WB_Coord[n + 2];
		npoints[0] = points.get(0);
		npoints[n + 1] = points.get(n);
		final double inp = 1.0 / (n + 1);
		for (int i = 1; i <= n; i++) {
			npoints[i] = gf.createInterpolatedPoint(points.get(i), points.get(i - 1), i * inp);
		}
		return new WB_Bezier(WB_CoordCollection.getCollection(npoints));
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Bezier derivative() {
		if (n <= 0) {
			return null;
		}
		final WB_Coord[] npoints = new WB_Coord[n - 1];
		for (int i = 0; i < n; i++) {
			npoints[i] = WB_Point.sub(points.get(i + 1), points.get(i)).mulSelf(n);
		}
		return new WB_Bezier(WB_CoordCollection.getCollection(npoints));
	}
}
