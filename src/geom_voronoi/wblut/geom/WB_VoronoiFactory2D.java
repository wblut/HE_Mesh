package wblut.geom;

import java.util.Collection;
import java.util.List;

import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;

public class WB_VoronoiFactory2D {
	private static WB_GeometryFactory3D geometryfactory = new WB_GeometryFactory3D();
	final static WB_Map2D XY = geometryfactory.createEmbeddedPlane();

	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, context);
	}

	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, context);
	}

	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final WB_Map2D context) {
		return getVoronoi2D(points, d, 2, context);
	}

	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final WB_Map2D context) {
		return getVoronoi2D(points, d, 2, context);
	}

	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, d, c, context);
	}

	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getVoronoi2D(points, d, c, context);
	}

	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c) {
		return WB_JTS.getVoronoi2D(points, d, c);
	}

	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d) {
		return getVoronoi2D(points, d, 2, XY);
	}

	public static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points) {
		return WB_JTS.getVoronoi2D(points);
	}

	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final int c) {
		return WB_JTS.getVoronoi2D(points, d, c);
	}

	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d) {
		return getVoronoi2D(points, d, 2, XY);
	}

	public static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points) {
		return WB_JTS.getVoronoi2D(points);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final int c,
			final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c, final WB_Map2D context) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c, context);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final int c) {
		return WB_JTS.getClippedVoronoi2D(points, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points) {
		return WB_JTS.getClippedVoronoi2D(points);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points) {
		return WB_JTS.getClippedVoronoi2D(points);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary) {
		return WB_JTS.getClippedVoronoi2D(points, boundary);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary,
			final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary,
			final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final WB_Polygon boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c) {
		return WB_JTS.getClippedVoronoi2D(points, boundary, d, c);
	}

	public static int[][] getVoronoi2DNeighbors(final WB_Coord[] points) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points);
		return tri.getNeighbors();
	}

	public static int[][] getVoronoi2DNeighbors(final List<? extends WB_Coord> points) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points);
		return tri.getNeighbors();
	}

	public static int[][] getVoronoi2DNeighbors(final WB_Coord[] points, final WB_Map2D context) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points, context);
		return tri.getNeighbors();
	}

	public static int[][] getVoronoi2DNeighbors(final List<? extends WB_Coord> points, final WB_Map2D context) {
		final WB_Triangulation2D tri = WB_TriangulationFactory2D.triangulate2D(points, context);
		return tri.getNeighbors();
	}

	public static WB_Voronoi2D getPowerDiagram2D(final WB_CoordCollection points, final double[] weights,
			final WB_Polygon clip) {
		if (weights.length != points.size()) {
			throw new IllegalArgumentException("Weights needs to be the same length as number of points.");
		}
		final PowerDiagram diagram = new PowerDiagram();
		final PolygonSimple root = new PolygonSimple();
		WB_Coord p;
		for (int i = 0; i < clip.getNumberOfShellPoints(); i++) {
			p = clip.getPoint(i);
			root.add(p.xd(), p.yd());
		}
		final OpenList sites = new OpenList();
		for (int i = 0; i < points.size(); i++) {
			p = points.get(i);
			final Site site = new Site(p.xd(), p.yd());
			site.setWeight(weights[i]);
			System.out.println(site.getWeight());
			sites.add(site);
		}
		diagram.setSites(sites);
		diagram.setClipPoly(root);
		diagram.computeDiagram();
		final List<WB_VoronoiCell2D> cells = new WB_List<>();
		Site site;
		PolygonSimple polygon;
		for (int i = 0; i < sites.size; i++) {
			site = sites.array[i];
			polygon = site.getPolygon();
			if (polygon != null) {
				final List<WB_Point> cps = new WB_PointList();
				final double[] xs = polygon.getXPoints();
				final double[] ys = polygon.getYPoints();
				for (int j = 0; j < polygon.length; j++) {
					cps.add(new WB_Point(xs[j], ys[j]));
				}
				final WB_VoronoiCell2D cell = new WB_VoronoiCell2D(cps, i, new WB_Point(site.getX(), site.getY()), 0.0,
						new WB_Point());
				cells.add(cell);
			}
		}
		return new WB_Voronoi2D(cells);
	}
}
