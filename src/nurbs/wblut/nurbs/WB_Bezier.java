/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.nurbs;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Curve;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.geom.WB_Vector;
import wblut.math.WB_Bernstein;

/**
 *
 */
public class WB_Bezier implements WB_Curve {
	/**
	 *
	 */
	private static WB_GeometryFactory	gf	= new WB_GeometryFactory();
	/**
	 *
	 */
	protected WB_Coord[]				points;
	/**
	 * Degree of the Bezier curve
	 */
	protected int						n;

	/**
	 * n+1 controlpoint.
	 *
	 * @param controlPoints
	 */
	public WB_Bezier(final WB_Coord[] controlPoints) {
		points = controlPoints;
		n = points.length - 1;
	}

	/**
	 *
	 *
	 * @param controlPoints
	 */
	public WB_Bezier(final WB_PointHomogeneous[] controlPoints) {
		n = controlPoints.length - 1;
		points = new WB_Point[n + 1];
		for (int i = 0; i < n + 1; i++) {
			points[i] = new WB_Point(controlPoints[i].project());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Curve#curvePoint(double)
	 */
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
		final double[] B = WB_Bernstein.getBernsteinCoefficientsOfOrderN(u,
				n - 1);
		for (int k = 0; k <= n - 1; k++) {
			Cp.addMulSelf(B[k], WB_Point.sub(points[k + 1], points[k]));
		}
		Cp.mulSelf(n);
		return Cp;
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
	 * Get degree.
	 *
	 * @return
	 */
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
		npoints[0] = points[0];
		npoints[n + 1] = points[n];
		final double inp = 1.0 / (n + 1);
		for (int i = 1; i <= n; i++) {
			npoints[i] = gf.createInterpolatedPoint(points[i], points[i - 1],
					i * inp);
		}
		return new WB_Bezier(npoints);
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
			npoints[i] = WB_Point.sub(points[i + 1], points[i]).mulSelf(n);
		}
		return new WB_Bezier(npoints);
	}
}
