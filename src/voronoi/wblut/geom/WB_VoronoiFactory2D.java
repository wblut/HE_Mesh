/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

/**
 *
 */
class WB_VoronoiFactory2D {
	/**
	 *
	 */
	private static WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();
	final static WB_Map2D				XY				= geometryfactory
			.createEmbeddedPlane();

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points,
			final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
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
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points,
			final double d, final WB_Map2D context) {
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
	public static WB_Voronoi2D getVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d,
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
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points,
			final double d, final int c, final WB_Map2D context) {
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
	public static WB_Voronoi2D getVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d,
			final int c, final WB_Map2D context) {
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
	public static WB_Voronoi2D getVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d,
			final int c) {
		return WB_JTS.getVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d) {
		return getVoronoi2D(points, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(
			final Collection<? extends WB_Coord> points) {
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
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points,
			final double d, final int c) {
		return WB_JTS.getVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points,
			final double d) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Coord[] boundary, final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Polygon boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final double d, final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Coord[] boundary, final double d, final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d,
			final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d,
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final double d, final int c, final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Coord[] boundary, final double d, final int c,
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Polygon boundary, final double d, final int c,
			final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d,
			final int c, final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c,
			final WB_Map2D context) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d,
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, d, c);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final double d) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Coord[] boundary, final double d, final int c) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Coord[] boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Coord[] boundary) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Polygon boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final List<WB_Polygon> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Polygon boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final List<WB_Polygon> boundary, final double d) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final WB_Polygon boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points,
			final List<WB_Polygon> boundary, final double d, final int c) {
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
	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(
			final Collection<? extends WB_Coord> points,
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
		WB_Triangulation2D tri = WB_TriangulationFactory.triangulate2D(points);
		return tri.getNeighbors();
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(
			final List<? extends WB_Coord> points) {
		WB_Triangulation2D tri = WB_TriangulationFactory.triangulate2D(points);
		return tri.getNeighbors();
	}

	/**
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(final WB_Coord[] points,
			final WB_Map2D context) {
		WB_Triangulation2D tri = WB_TriangulationFactory.triangulate2D(points, context);
		return tri.getNeighbors();
	}

	/**
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	public static int[][] getVoronoi2DNeighbors(
			final List<? extends WB_Coord> points, final WB_Map2D context) {
		WB_Triangulation2D tri = WB_TriangulationFactory.triangulate2D(points, context);
		return tri.getNeighbors();
	}

	public static WB_Voronoi2D getPowerDiagram2D(WB_CoordCollection points,
			double[] weights, WB_Polygon clip) {
		if (weights.length != points.size())
			throw new IllegalArgumentException(
					"Weights needs to be the same length as number of points.");
		PowerDiagram diagram = new PowerDiagram();
		PolygonSimple root = new PolygonSimple();
		WB_Coord p;
		for (int i = 0; i < clip.getNumberOfShellPoints(); i++) {
			p = clip.getPoint(i);
			root.add(p.xd(), p.yd());
		}
		OpenList sites = new OpenList();
		for (int i = 0; i < points.size(); i++) {
			p = points.get(i);
			Site site = new Site(p.xd(), p.yd());
			site.setWeight(weights[i]);
			System.out.println(site.getWeight());
			sites.add(site);
		}
		diagram.setSites(sites);
		diagram.setClipPoly(root);
		diagram.computeDiagram();
		List<WB_VoronoiCell2D> cells = new FastList<WB_VoronoiCell2D>();
		Site site;
		PolygonSimple polygon;
		for (int i = 0; i < sites.size; i++) {
			site = sites.array[i];
			polygon = site.getPolygon();
			if (polygon != null) {
				List<WB_Point> cps = new FastList<WB_Point>();
				double[] xs = polygon.getXPoints();
				double[] ys = polygon.getYPoints();
				for (int j = 0; j < polygon.length; j++) {
					cps.add(new WB_Point(xs[j], ys[j]));
				}
				WB_VoronoiCell2D cell = new WB_VoronoiCell2D(cps, i,
						new WB_Point(site.getX(), site.getY()), 0.0,
						new WB_Point());
				cells.add(cell);
			}
		}
		return new WB_Voronoi2D(cells);
	}
}
