/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wblut.external.ProGAL.CEdge;
import wblut.external.ProGAL.CTetrahedron;
import wblut.external.ProGAL.CTriangle;
import wblut.external.ProGAL.CVertex;
import wblut.external.ProGAL.DelaunayComplex;
import wblut.external.ProGAL.Point;

/**
 *
 */
class WB_TriangulationFactory3D extends WB_TriangulationFactory2D {
	/**
	 *
	 */
	private static WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	/**
	 *
	 */
	WB_TriangulationFactory3D() {
		super();
	}

	/**
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation3D triangulate3D(final WB_CoordCollection points) {
		// WB_Predicates predicates = new WB_Predicates();
		final int n = points.size();
		final List<Point> tmppoints = new ArrayList<Point>(n);
		final WB_KDTree3D<WB_Coord, Integer> tree = new WB_KDTree3D<WB_Coord, Integer>();
		WB_Coord c;
		for (int i = 0; i < n; i++) {
			c = points.get(i);
			tmppoints.add(new Point(c.xd(), c.yd(), c.zd()));
			tree.add(c, i);
		}
		final DelaunayComplex dc = new DelaunayComplex(tmppoints);
		final List<CTetrahedron> tetras = dc.getTetrahedra();
		final List<CTriangle> tris = dc.getTriangles();
		final List<CEdge> edges = dc.getEdges();
		int nt = tetras.size();
		List<int[]> tmpresult = new ArrayList<int[]>();
		for (int i = 0; i < nt; i++) {
			final int[] tmp = new int[4];
			final CTetrahedron tetra = tetras.get(i);
			int index = tree.getNearestNeighbor(convert(tetra.getPoint(0))).value;
			tmp[0] = index;
			index = tree.getNearestNeighbor(convert(tetra.getPoint(1))).value;
			tmp[1] = index;
			index = tree.getNearestNeighbor(convert(tetra.getPoint(2))).value;
			tmp[2] = index;
			index = tree.getNearestNeighbor(convert(tetra.getPoint(3))).value;
			tmp[3] = index;
			/*
			 * double o = predicates.orientTetra(points[tmp[0]].coords(),
			 * points[tmp[1]].coords(), points[tmp[2]].coords(),
			 * points[tmp[3]].coords()); if (o != 0) {
			 */
			tmpresult.add(tmp);
			/*
			 * }
			 */
		}
		final int[] tetra = new int[4 * tmpresult.size()];
		for (int i = 0; i < tmpresult.size(); i++) {
			for (int j = 0; j < 4; j++) {
				tetra[i * 4 + j] = tmpresult.get(i)[j];
			}
		}
		nt = tris.size();
		tmpresult = new ArrayList<int[]>();
		for (int i = 0; i < nt; i++) {
			final int[] tmp = new int[3];
			final CTriangle tri = tris.get(i);
			int index = tree.getNearestNeighbor(convert(tri.getPoint(0))).value;
			tmp[0] = index;
			index = tree.getNearestNeighbor(convert(tri.getPoint(1))).value;
			tmp[1] = index;
			index = tree.getNearestNeighbor(convert(tri.getPoint(2))).value;
			tmp[2] = index;

			tmpresult.add(tmp);

		}
		final int[] tri = new int[3 * tmpresult.size()];
		for (int i = 0; i < tmpresult.size(); i++) {
			for (int j = 0; j < 3; j++) {
				tri[3 * i + j] = tmpresult.get(i)[j];
			}
		}
		nt = edges.size();
		tmpresult = new ArrayList<int[]>();
		for (int i = 0; i < nt; i++) {
			final int[] tmp = new int[3];
			final CEdge edge = edges.get(i);
			int index = tree.getNearestNeighbor(convert(edge.getPoint(0))).value;
			tmp[0] = index;
			index = tree.getNearestNeighbor(convert(edge.getPoint(1))).value;
			tmp[1] = index;
			tmpresult.add(tmp);

		}
		final int[] edge = new int[2 * tmpresult.size()];
		for (int i = 0; i < tmpresult.size(); i++) {
			for (int j = 0; j < 2; j++) {
				edge[2 * i + j] = tmpresult.get(i)[j];
			}
		}

		final WB_Triangulation3D result = new WB_Triangulation3D(points, tetra, tri, edge);
		return result;
	}

	/**
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation3D triangulate3D(final WB_Coord[] points) {
		return triangulate3D(WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_Triangulation3D triangulate3D(final Collection<? extends WB_Coord> points) {
		return triangulate3D(WB_CoordCollection.getCollection(points));
	}
	
	public static WB_Network getNetwork(final WB_CoordCollection points) {
		return triangulate3D(points).getNetwork();
	}
	
	public static WB_Network getNetwork(final WB_Coord[] points) {
		return triangulate3D(WB_CoordCollection.getCollection(points)).getNetwork();
	}
	
	public static WB_Network getNetwork(final Collection<? extends WB_Coord> points) {
		return triangulate3D(WB_CoordCollection.getCollection(points)).getNetwork();
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	private static WB_Point convert(final CVertex v) {
		return geometryfactory.createPoint(v.x(), v.y(), v.z());
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	private static WB_Point convert(final Point v) {
		return geometryfactory.createPoint(v.x(), v.y(), v.z());
	}

	/**
	 *
	 * @param points
	 * @return
	 */
	public static WB_AlphaTriangulation3D alphaTriangulate3D(final WB_CoordCollection points) {

		final WB_Triangulation3D tri = WB_TriangulationFactory3D.triangulate3D(points);
		return new WB_AlphaTriangulation3D(tri.getTetrahedra(), points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_AlphaTriangulation3D alphaTriangulate3D(final WB_Coord[] points) {

		return alphaTriangulate3D(WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static WB_AlphaTriangulation3D alphaTriangulate3D(final Collection<? extends WB_Coord> points) {

		return alphaTriangulate3D(WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 * @param points
	 * @return
	 */
	public static WB_AlphaTriangulation3D alphaTriangulate3D(final WB_CoordCollection points, final double jitter) {
		WB_RandomOnSphere ros = new WB_RandomOnSphere().setRadius(jitter);
		final WB_Triangulation3D tri = WB_TriangulationFactory3D.triangulate3D(points.noise(ros));
		return new WB_AlphaTriangulation3D(tri.getTetrahedra(), points);
	}

	/**
	 *
	 * @param points
	 * @param jitter
	 * @return
	 */
	public static WB_AlphaTriangulation3D alphaTriangulate3D(final WB_Coord[] points, final double jitter) {
		return alphaTriangulate3D(WB_CoordCollection.getCollection(points), jitter);
	}

	/**
	 *
	 * @param points
	 * @param jitter
	 * @return
	 */
	public static WB_AlphaTriangulation3D alphaTriangulate3D(final Collection<? extends WB_Coord> points,
			final double jitter) {
		return alphaTriangulate3D(WB_CoordCollection.getCollection(points), jitter);
	}
}
