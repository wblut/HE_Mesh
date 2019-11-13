/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 *
 */
class WB_TriangulationFactory2D {
	/**
	 *
	 */
	WB_TriangulationFactory2D() {
	}

	/**
	 * Planar Delaunay triangulation.
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2D triangulate2D(
			final WB_CoordCollection points) {
		return WB_JTS.triangulate2D(points);
	}

	/**
	 * Planar Delaunay triangulation.
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2D triangulate2D(final WB_Coord[] points) {
		return triangulate2D(WB_CoordCollection.getCollection(points));
	}

	/**
	 * Planar Delaunay triangulation.
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2D triangulate2D(
			final Collection<? extends WB_Coord> points) {
		return triangulate2D(WB_CoordCollection.getCollection(points));
	}

	/**
	 * Planar Delaunay triangulation.
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2D triangulate2D(
			final WB_CoordCollection points, final WB_Map2D context) {
		return triangulate2D(points.map(context));
	}

	/**
	 * Planar Delaunay triangulation.
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2D triangulate2D(final WB_Coord[] points,
			final WB_Map2D context) {
		return triangulate2D(WB_CoordCollection.getCollection(points), context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2D triangulate2D(
			final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		return triangulate2D(WB_CoordCollection.getCollection(points), context);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_CoordCollection points) {
		return WB_JTS.triangulateConforming2D(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 * @param points
	 * @param tolerance
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_CoordCollection points, final double tolerance) {
		return WB_JTS.triangulateConforming2D(points, tolerance);
	}

	/**
	 *
	 *
	 * @param points
	 * @param tol
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points),
				tol);
	}

	/**
	 *
	 *
	 * @param points
	 * @param tol
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points, final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points),
				tol);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_CoordCollection points, final int[] constraints) {
		return WB_JTS.triangulateConforming2D(points, constraints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final int[] constraints) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points),
				constraints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points,
			final int[] constraints) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points),
				constraints);
	}

	/**
	 *
	 * @param points
	 * @param constraints
	 * @param tolerance
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_CoordCollection points, final int[] constraints,
			final double tolerance) {
		return WB_JTS.triangulateConforming2D(points, constraints, tolerance);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @param tol
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final int[] constraints,
			final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points),
				constraints, tol);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @param tol
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points,
			final int[] constraints, final double tol) {
		return triangulateConforming2D(WB_CoordCollection.getCollection(points),
				constraints, tol);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final WB_Map2D context) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points).map(context));
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		return WB_JTS.triangulateConforming2D(points, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param tol
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final double tol, final WB_Map2D context) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points).map(context), tol);
	}

	/**
	 *
	 *
	 * @param points
	 * @param tol
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points, final double tol,
			final WB_Map2D context) {
		return WB_JTS.triangulateConforming2D(points, tol, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final int[] constraints,
			final WB_Map2D context) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points).map(context),
				constraints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points,
			final int[] constraints, final WB_Map2D context) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points).map(context),
				constraints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @param tol
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final WB_Coord[] points, final int[] constraints, final double tol,
			final WB_Map2D context) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points).map(context),
				constraints, tol);
	}

	/**
	 *
	 *
	 * @param points
	 * @param constraints
	 * @param tol
	 * @param context
	 * @return
	 */
	public static WB_Triangulation2DWithPoints triangulateConforming2D(
			final Collection<? extends WB_Coord> points,
			final int[] constraints, final double tol, final WB_Map2D context) {
		return triangulateConforming2D(
				WB_CoordCollection.getCollection(points).map(context),
				constraints, tol);
	}



	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_AlphaTriangulation2D alphaTriangulate2D(
			final Collection<? extends WB_Coord> points) {
		final WB_Triangulation2D tri = triangulate2D(points);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}

	/**
	 *
	 * @param points
	 * @param jitter
	 * @return
	 */
	public static WB_AlphaTriangulation2D alphaTriangulate2D(
			final Collection<? extends WB_Coord> points, final double jitter) {
		FastList<WB_Point> jigPoints = new FastList<WB_Point>();
		WB_RandomOnSphere ros = new WB_RandomOnSphere();
		for (WB_Coord p : points) {
			jigPoints.add(WB_Point.addMul(p, jitter, ros.nextVector()));
		}
		final WB_Triangulation2D tri = triangulate2D(jigPoints);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_AlphaTriangulation2D alphaTriangulate2D(
			final WB_Coord[] points) {
		final WB_Triangulation2D tri = triangulate2D(points);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}

	/**
	 *
	 * @param points
	 * @param jitter
	 * @return
	 */
	public static WB_AlphaTriangulation2D alphaTriangulate2D(
			final WB_Coord[] points, final double jitter) {
		WB_Coord[] jigPoints = Arrays.copyOf(points, points.length);
		WB_RandomOnSphere ros = new WB_RandomOnSphere();
		int i = 0;
		for (WB_Coord p : points) {
			jigPoints[i++] = WB_Point.addMul(p, jitter, ros.nextVector());
		}
		final WB_Triangulation2D tri = triangulate2D(jigPoints);
		return new WB_AlphaTriangulation2D(tri.getTriangles(), points);
	}
}
