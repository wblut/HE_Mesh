package wblut.geom;

import java.util.Arrays;
import java.util.Collection;

class WB_TriangulationFactory2D {
	WB_TriangulationFactory2D() {
	}

	public static WB_Triangulation2D triangulate2D(final WB_CoordCollection points) {
		return WB_JTS.triangulate2D(points);
	}

	public static WB_Triangulation2D triangulate2D(final WB_Coord[] points) {
		return triangulate2D(WB_CoordCollection.getCollection(points));
	}

	public static WB_Triangulation2D triangulate2D(final Collection<? extends WB_Coord> points) {
		return triangulate2D(WB_CoordCollection.getCollection(points));
	}

	public static WB_Triangulation2D triangulate2D(final WB_CoordCollection points, final WB_Map2D context) {
		return triangulate2D(points.map(context));
	}

	public static WB_Triangulation2D triangulate2D(final WB_Coord[] points, final WB_Map2D context) {
		return triangulate2D(WB_CoordCollection.getCollection(points), context);
	}

	public static WB_Triangulation2D triangulate2D(final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		return triangulate2D(WB_CoordCollection.getCollection(points), context);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points) {
		return WB_JTS.triangulateConforming2D(points);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points));
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points));
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points,
			final double tolerance) {
		return WB_JTS.triangulateConforming2D(points, tolerance);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points, final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points), tol);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points), tol);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points,
			final int[] constraints) {
		return WB_JTS.triangulateConforming2D(points, constraints);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points,
			final int[] constraints) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points), constraints);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final int[] constraints) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points), constraints);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points,
			final int[] constraints, final double tolerance) {
		return WB_JTS.triangulateConforming2D(points, constraints, tolerance);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points, final int[] constraints,
			final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points), constraints, tol);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final int[] constraints, final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points), constraints, tol);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points,
			final WB_Map2D context) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points).map(context));
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		return WB_JTS.triangulateConforming2D(points, context);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points, final double tol,
			final WB_Map2D context) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points).map(context), tol);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final double tol, final WB_Map2D context) {
		return WB_JTS.triangulateConforming2D(points, tol, context);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points, final int[] constraints,
			final WB_Map2D context) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points).map(context), constraints);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final int[] constraints, final WB_Map2D context) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points).map(context), constraints);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Coord[] points, final int[] constraints,
			final double tol, final WB_Map2D context) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points).map(context), constraints, tol);
	}

	public static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final int[] constraints, final double tol, final WB_Map2D context) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points).map(context), constraints, tol);
	}

	public static WB_AlphaTriangulation2D alphaTriangulate2D(final Collection<? extends WB_Coord> points) {
		final WB_Triangulation2D tri = triangulate2D(points);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}

	public static WB_AlphaTriangulation2D alphaTriangulate2D(final Collection<? extends WB_Coord> points,
			final double jitter) {
		final WB_PointList jigPoints = new WB_PointList();
		final WB_RandomOnSphere ros = new WB_RandomOnSphere();
		for (final WB_Coord p : points) {
			jigPoints.add(WB_Point.addMul(p, jitter, ros.nextVector()));
		}
		final WB_Triangulation2D tri = triangulate2D(jigPoints);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}

	public static WB_AlphaTriangulation2D alphaTriangulate2D(final WB_Coord[] points) {
		final WB_Triangulation2D tri = triangulate2D(points);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}

	public static WB_AlphaTriangulation2D alphaTriangulate2D(final WB_Coord[] points, final double jitter) {
		final WB_Coord[] jigPoints = Arrays.copyOf(points, points.length);
		final WB_RandomOnSphere ros = new WB_RandomOnSphere();
		int i = 0;
		for (final WB_Coord p : points) {
			jigPoints[i++] = WB_Point.addMul(p, jitter, ros.nextVector());
		}
		final WB_Triangulation2D tri = triangulate2D(jigPoints);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}
}
