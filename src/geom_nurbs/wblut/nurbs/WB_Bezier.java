package wblut.nurbs;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Curve;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.geom.WB_Vector;
import wblut.math.WB_Bernstein;

public class WB_Bezier implements WB_Curve {
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	protected WB_Coord[] points;
	protected int n;

	public WB_Bezier(final WB_Coord[] controlPoints) {
		points = controlPoints;
		n = points.length - 1;
	}

	public WB_Bezier(final WB_PointHomogeneous[] controlPoints) {
		n = controlPoints.length - 1;
		points = new WB_Point[n + 1];
		for (int i = 0; i < n + 1; i++) {
			points[i] = new WB_Point(controlPoints[i].project());
		}
	}

	@Override
	public WB_Point getPointOnCurve(final double u) {
		final WB_Point C = new WB_Point();
		if (n <= 0) {
			return C;
		}
		final double[] B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n);
		for (int k = 0; k <= n; k++) {
			C.addMulSelf(B[k], points[k]);
		}
		return C;
	}

	public WB_Vector firstDerivative(final double u) {
		final WB_Vector Cp = new WB_Vector();
		if (n <= 0) {
			return Cp;
		}
		final double[] B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u, n - 1);
		for (int k = 0; k <= n - 1; k++) {
			Cp.addMulSelf(B[k], WB_Point.sub(points[k + 1], points[k]));
		}
		Cp.mulSelf(n);
		return Cp;
	}

	@Override
	public WB_Vector getDirectionOnCurve(final double u) {
		final WB_Vector v = firstDerivative(u);
		v.normalizeSelf();
		return v;
	}

	@Override
	public WB_Vector getDerivative(final double u) {
		return firstDerivative(u);
	}

	public double n() {
		return n;
	}

	@Override
	public double getLowerU() {
		return 0;
	}

	@Override
	public double getUpperU() {
		return 1;
	}

	public WB_Bezier elevateDegree() {
		if (n <= 0) {
			return null;
		}
		final WB_Coord[] npoints = new WB_Coord[n + 2];
		npoints[0] = points[0];
		npoints[n + 1] = points[n];
		final double inp = 1.0 / (n + 1);
		for (int i = 1; i <= n; i++) {
			npoints[i] = gf.createInterpolatedPoint(points[i], points[i - 1], i * inp);
		}
		return new WB_Bezier(npoints);
	}

	public WB_Bezier derivative() {
		if (n <= 0) {
			return null;
		}
		final WB_Coord[] npoints = new WB_Coord[n - 1];
		for (int i = 0; i < n; i++) {
			npoints[i] = WB_Point.sub(points[i + 1], points[i]).mulSelf(n);
		}
		return new WB_Bezier(npoints);
	}
}
