package wblut.geom;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class WB_VoronoiFactory2D {
	/**  */
	private static WB_GeometryFactory3D geometryfactory = new WB_GeometryFactory3D();
	/**  */
	final static WB_Map2D XY = geometryfactory.createEmbeddedPlane();

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final WB_Map2D context) {
		return getVoronoi2D(points, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final WB_Map2D context) {
		return getVoronoi2D(points, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c) {
		return WB_JTS.getVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d) {
		return getVoronoi2D(points, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points) {
		return WB_JTS.getVoronoi2D(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final int c) {
		return WB_JTS.getVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d) {
		return getVoronoi2D(points, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points) {
		return WB_JTS.getVoronoi2D(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final int c) {
		return WB_JTS.getClippedVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points) {
		return WB_JTS.getClippedVoronoi2D(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points) {
		return WB_JTS.getClippedVoronoi2D(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary,
			final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary,
			final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(final WB_Coord[] points) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points);
		return tri.getNeighbors();
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(final List<? extends WB_Coord> points) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points);
		return tri.getNeighbors();
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(final WB_Coord[] points, final WB_Map2D context) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points, context);
		return tri.getNeighbors();
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(final List<? extends WB_Coord> points, final WB_Map2D context) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points, context);
		return tri.getNeighbors();
	}
}
