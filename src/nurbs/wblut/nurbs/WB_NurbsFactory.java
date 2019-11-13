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
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_IntersectionResult;
import wblut.geom.WB_Line;
import wblut.geom.WB_Point;
import wblut.geom.WB_PointHomogeneous;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

/**
 * @author FVH
 *
 */
public class WB_NurbsFactory {

	/**
	 *
	 *
	 * @param C
	 * @param p
	 * @param axis
	 * @param theta
	 * @return
	 */
	public static WB_RBSplineSurface getSurfaceOfRevolution(final WB_BSpline C, final WB_Coord p, final WB_Coord axis,
			double theta) {
		boolean full=(theta==2*Math.PI);
		final WB_Vector v = new WB_Vector(axis);
		v.normalizeSelf();
		if (theta < 0) {
			theta *= -1;
			v.mulSelf(-1);
		}
		while (theta > 2 * Math.PI) {
			theta -= 2 * Math.PI;
		}
		int narcs;
		final WB_Line L = new WB_Line(p, v);
		final double[] U;
		if (theta <= 0.5 * Math.PI) {
			narcs = 1;
			U = new double[6];
		} else if (theta <= Math.PI) {
			narcs = 2;
			U = new double[8];
			U[3] = 0.5;
			U[4] = 0.5;
		} else if (theta <= 1.5 * Math.PI) {
			U = new double[10];
			narcs = 3;
			U[3] = 1.0 / 3;
			U[4] = U[3];
			U[5] = 2.0 / 3;
			U[6] = U[5];
		} else {
			U = new double[12];
			narcs = 4;
			U[3] = 0.25;
			U[4] = U[3];
			U[5] = 0.5;
			U[6] = U[5];
			U[7] = 0.75;
			U[8] = U[7];
		}
		final WB_Coord[][] points = new WB_Coord[1 + 2 * narcs][C.n() + 1];
		final double[][] weights = new double[1 + 2 * narcs][C.n() + 1];
		final double dtheta = theta / narcs;
		int i = 0;
		int j = 3 + 2 * (narcs - 1);
		for (i = 0; i < 3; j++, i++) {
			U[i] = 0;
			U[j] = 1;
		}
		final double wm = Math.cos(dtheta * 0.5);
		double angle = 0;
		final double[] cosines = new double[narcs + 1];
		final double[] sines = new double[narcs + 1];
		for (i = 1; i <= narcs; i++) {
			angle = (i==narcs&& full)?0:angle + dtheta;
			cosines[i] = Math.cos(angle);
			sines[i] = Math.sin(angle);
		}
		for (j = 0; j <= C.n(); j++) {
			final WB_Point O = WB_GeometryOp.getClosestPoint3D(C.points()[j], L);
			final WB_Vector X = WB_Vector.subToVector3D(C.points()[j], O);
			final double r = X.normalizeSelf();
			final WB_Vector Y = new WB_Vector(v).crossSelf(X);
			final WB_Point P0 = new WB_Point(C.points()[j]);
			points[0][j] = new WB_Point(P0);
			weights[0][j] = 1;
			final WB_Vector T0 = new WB_Vector(Y);
			int index = 0;
			angle = 0.0;
			for (i = 1; i <= narcs; i++) {
				final WB_Point P2 = new WB_Point(O);
				P2.addMulSelf(r * cosines[i], X);
				P2.addMulSelf(r * sines[i], Y);
				points[index + 2][j] = new WB_Point(P2);
				weights[index + 2][j] = 1;
				final WB_Vector T2 = Y.mul(cosines[i]);
				T2.addMulSelf(-sines[i], X);
				final WB_Line L1 = new WB_Line(P0, T0);
				final WB_Line L2 = new WB_Line(P2, T2);
				final WB_IntersectionResult is = WB_GeometryOp.getClosestPoint3D(L1, L2);
				final WB_Coord p1 = is.dimension == 0 ? (WB_Point) is.object : ((WB_Segment) is.object).getOrigin();
				points[index + 1][j] = p1;
				weights[index + 1][j] = wm;
				index = index + 2;
				if (i < narcs) {
					P0.set(P2);
					T0.set(T2);
				}
			}
			if(full) {
				points[narcs][j] = points[0][j];		
			}
		}
		final WB_NurbsKnot UKnot = new WB_NurbsKnot(2, U);
		return new WB_RBSplineSurface(points, UKnot, C.knot(), weights);
	}

	public static WB_RBSplineSurface getFullSurfaceOfRevolution(final WB_BSpline C, final WB_Coord p,
			final WB_Coord axis) {
		return getSurfaceOfRevolution(C, p, axis, 2 * Math.PI);
	}

	/**
	 *
	 *
	 * @param C
	 * @param p
	 * @param axis
	 * @param theta
	 * @return
	 */
	public static WB_RBSplineSurface getSurfaceOfRevolution(final WB_RBSpline C, final WB_Coord p, final WB_Coord axis,
			double theta) {
		final WB_Vector v = new WB_Vector(axis);
		v.normalizeSelf();
		if (theta < 0) {
			theta *= -1;
			v.mulSelf(-1);
		}
		while (theta > 2 * Math.PI) {
			theta -= 2 * Math.PI;
		}
		int narcs;
		final WB_Line L = new WB_Line(p, v);
		final double[] U;
		if (theta <= 0.5 * Math.PI) {
			narcs = 1;
			U = new double[6];
		} else if (theta <= Math.PI) {
			narcs = 2;
			U = new double[8];
			U[3] = 0.5;
			U[4] = 0.5;
		} else if (theta <= 1.5 * Math.PI) {
			U = new double[10];
			narcs = 3;
			U[3] = 1.0 / 3;
			U[4] = U[3];
			U[5] = 2.0 / 3;
			U[6] = U[5];
		} else {
			U = new double[12];
			narcs = 4;
			U[3] = 0.25;
			U[4] = U[3];
			U[5] = 0.5;
			U[6] = U[5];
			U[7] = 0.75;
			U[8] = U[7];
		}
		final WB_Coord[][] points = new WB_Coord[1 + 2 * narcs][C.n() + 1];
		final double[][] weights = new double[1 + 2 * narcs][C.n() + 1];
		final double dtheta = theta / narcs;
		int i = 0;
		int j = 3 + 2 * (narcs - 1);
		for (i = 0; i < 3; j++, i++) {
			U[i] = 0;
			U[j] = 1;
		}
		final double wm = Math.cos(dtheta * 0.5);
		double angle = 0;
		final double[] cosines = new double[narcs + 1];
		final double[] sines = new double[narcs + 1];
		for (i = 1; i <= narcs; i++) {
			angle = angle + dtheta;
			cosines[i] = Math.cos(angle);
			sines[i] = Math.sin(angle);
		}
		for (j = 0; j <= C.n(); j++) {
			final WB_Point O = WB_GeometryOp.getClosestPoint3D(C.points()[j], L);
			final WB_Vector X = WB_Vector.subToVector3D(C.points()[j], O);
			final double r = X.normalizeSelf();
			final WB_Vector Y = new WB_Vector(v).crossSelf(X);
			final WB_Point P0 = new WB_Point(C.points()[j]);
			points[0][j] = new WB_Point(P0);
			weights[0][j] = C.wpoints()[j].wd();
			final WB_Vector T0 = new WB_Vector(Y);
			int index = 0;
			angle = 0.0;
			for (i = 1; i <= narcs; i++) {
				final WB_Point P2 = new WB_Point(O);
				P2.addMulSelf(r * cosines[i], X);
				P2.addMulSelf(r * sines[i], Y);
				points[index + 2][j] = new WB_Point(P2);
				weights[index + 2][j] = C.wpoints()[j].wd();
				final WB_Vector T2 = Y.mul(cosines[i]);
				T2.addMulSelf(-sines[i], X);
				final WB_Line L1 = new WB_Line(P0, T0);
				final WB_Line L2 = new WB_Line(P2, T2);
				final WB_IntersectionResult is = WB_GeometryOp.getClosestPoint3D(L1, L2);
				final WB_Coord p1 = is.dimension == 0 ? (WB_Point) is.object : ((WB_Segment) is.object).getOrigin();
				points[index + 1][j] = p1;
				weights[index + 1][j] = wm * C.wpoints()[j].wd();
				index = index + 2;
				if (i < narcs) {
					P0.set(P2);
					T0.set(T2);
				}
			}
		}
		final WB_NurbsKnot UKnot = new WB_NurbsKnot(2, U);
		return new WB_RBSplineSurface(points, UKnot, C.knot(), weights);
	}

	public static WB_RBSplineSurface getFullSurfaceOfRevolution(final WB_RBSpline C, final WB_Coord p,
			final WB_Coord axis) {
		return getSurfaceOfRevolution(C, p, axis, 2 * Math.PI);
	}

	/**
	 *
	 *
	 * @param CA
	 * @param CB
	 * @return
	 */
	public static WB_BSplineSurface getRuledSurface(WB_BSpline CA, WB_BSpline CB) {
		if (CA.getLowerU() != CB.getLowerU() || CA.getUpperU() != CB.getUpperU()) {
			throw new IllegalArgumentException("Curves not defined on same parameter range.");
		}

		final int degreeA = CA.p();
		final int degreeB = CB.p();
		if (degreeA < degreeB) {
			CA = CA.elevateDegree(degreeB - degreeA);
		} else if (degreeB < degreeA) {
			CB = CB.elevateDegree(degreeA - degreeB);
		}

		final WB_NurbsKnot mergedKnot = WB_NurbsKnot.merge(CA.knot(), CB.knot());
		CA = CA.refineKnot(mergedKnot);
		CB = CB.refineKnot(mergedKnot);
		final WB_NurbsKnot VKnot = new WB_NurbsKnot(2, 1);
		final int nocp = mergedKnot.n() + 1;
		final WB_Coord[][] controlPoints = new WB_Point[nocp][2];
		for (int i = 0; i < nocp; i++) {
			// System.out.println(i + " " + CA.points()[i]);
			// System.out.println(i + " " + CB.points()[i]);

			controlPoints[i][0] = new WB_Point(CA.points()[i]);
			controlPoints[i][1] = new WB_Point(CB.points()[i]);
		}
		return new WB_BSplineSurface(controlPoints, mergedKnot, VKnot);
	}

	/**
	 *
	 *
	 * @param CA
	 * @param CB
	 * @return
	 */
	public static WB_RBSplineSurface getRuledSurface(WB_RBSpline CA, WB_RBSpline CB) {
		if (CA.getLowerU() != CB.getLowerU() || CA.getUpperU() != CB.getUpperU()) {
			throw new IllegalArgumentException("Curves not defined on same parameter range.");
		}
		final int degreeA = CA.p();
		final int degreeB = CB.p();
		if (degreeA < degreeB) {
			CA = CA.elevateDegree(degreeB - degreeA);
		} else if (degreeB < degreeA) {
			CB = CB.elevateDegree(degreeA - degreeB);
		} else {
		}
		final WB_NurbsKnot mergedKnot = WB_NurbsKnot.merge(CA.knot(), CB.knot());
		CA = CA.refineKnot(mergedKnot);
		CB = CB.refineKnot(mergedKnot);
		final WB_NurbsKnot VKnot = new WB_NurbsKnot(2, 1);
		final int nocp = mergedKnot.n() + 1;
		final WB_PointHomogeneous[][] controlPoints = new WB_PointHomogeneous[nocp][2];
		for (int i = 0; i < nocp; i++) {
			controlPoints[i][0] = CA.wpoints[i];
			controlPoints[i][1] = CB.wpoints[i];
		}
		return new WB_RBSplineSurface(controlPoints, mergedKnot, VKnot);
	}

	/**
	 *
	 *
	 * @param xzprofile
	 * @param xytrajectory
	 * @param alpha
	 * @return
	 */
	public static WB_BSplineSurface getSwungSurface(final WB_BSpline xzprofile, final WB_BSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point[][] points = new WB_Point[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point(alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].xd(),
						alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].yd(), xzprofile.points()[i].zd());
			}
		}
		return new WB_BSplineSurface(points, xzprofile.knot(), xytrajectory.knot());
	}

	/**
	 *
	 *
	 * @param xzprofile
	 * @param xytrajectory
	 * @param alpha
	 * @return
	 */
	public static WB_RBSplineSurface getSwungSurface(final WB_BSpline xzprofile, final WB_RBSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point[][] points = new WB_Point[n + 1][m + 1];
		final double[][] weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point(alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].xd(),
						alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].yd(), xzprofile.points()[i].zd());
				weights[i][j] = xytrajectory.weights()[j];
			}
		}
		return new WB_RBSplineSurface(points, xzprofile.knot(), xytrajectory.knot(), weights);
	}

	/**
	 *
	 *
	 * @param xzprofile
	 * @param xytrajectory
	 * @param alpha
	 * @return
	 */
	public static WB_RBSplineSurface getSwungSurface(final WB_RBSpline xzprofile, final WB_BSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point[][] points = new WB_Point[n + 1][m + 1];
		final double[][] weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point(alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].xd(),
						alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].yd(), xzprofile.points()[i].zd());
				weights[i][j] = xzprofile.weights()[i];

			}
		}
		return new WB_RBSplineSurface(points, xzprofile.knot(), xytrajectory.knot(), weights);
	}

	/**
	 *
	 *
	 * @param xzprofile
	 * @param xytrajectory
	 * @param alpha
	 * @return
	 */
	public static WB_RBSplineSurface getSwungSurface(final WB_RBSpline xzprofile, final WB_RBSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point[][] points = new WB_Point[n + 1][m + 1];
		final double[][] weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point(alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].xd(),
						alpha * xzprofile.points()[i].xd() * xytrajectory.points()[j].yd(), xzprofile.points()[i].zd());
				weights[i][j] = xzprofile.weights()[i] * xytrajectory.weights()[j];

			}
		}
		return new WB_RBSplineSurface(points, xzprofile.knot(), xytrajectory.knot(), weights);
	}

	/**
	 *
	 *
	 * @param C
	 * @param v
	 * @param f
	 * @return
	 */
	public static WB_BSplineSurface getLineSweep(final WB_BSpline C, final WB_Coord v, final double f) {
		final WB_NurbsKnot VKnot = new WB_NurbsKnot(2, 1);
		final WB_Point[][] points = new WB_Point[C.n() + 1][2];
		for (int i = 0; i <= C.n(); i++) {
			points[i][0] = new WB_Point(C.points()[i]);
			points[i][1] = points[i][0].addMul(f, v);
		}
		return new WB_BSplineSurface(points, C.knot(), VKnot);
	}

	/**
	 *
	 *
	 * @param C
	 * @param v
	 * @param f
	 * @return
	 */
	public static WB_RBSplineSurface getLineSweep(final WB_RBSpline C, final WB_Coord v, final double f) {
		final WB_NurbsKnot VKnot = new WB_NurbsKnot(2, 1);
		final WB_Point[][] points = new WB_Point[C.n() + 1][2];
		final double[][] weights = new double[C.n() + 1][2];
		for (int i = 0; i <= C.n(); i++) {
			points[i][0] = new WB_Point(C.points()[i]);
			points[i][1] = WB_Point.addMul(C.points()[i], f, v);
			weights[i][0] = C.weights()[i];
			weights[i][1] = weights[i][0];
		}
		return new WB_RBSplineSurface(points, C.knot(), VKnot, weights);
	}

}
