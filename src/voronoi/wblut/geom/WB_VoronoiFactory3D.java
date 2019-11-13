/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import wblut.external.ProGAL.CTetrahedron;
import wblut.external.ProGAL.CTriangle;
import wblut.external.ProGAL.CVertex;
import wblut.external.ProGAL.DelaunayComplex;
import wblut.external.ProGAL.Point;
import wblut.hemesh.HEC_Geodesic;
import wblut.hemesh.HEMC_VoronoiCells;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
class WB_VoronoiFactory3D extends WB_VoronoiFactory2D {
	/**
	 *
	 */
	private static WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();
	final static WB_Map2D				XY				= geometryfactory
			.createEmbeddedPlane();

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_Coord[] points, int nv,
			final WB_AABB aabb, final WB_ScalarParameter d) {
		return getVoronoi3D(WB_CoordCollection.getCollection(points),nv,aabb,d);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_Coord[] points,
			final int nv, final WB_AABB aabb, final double d) {
		return getVoronoi3D(points, nv, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_Coord[] points,
			final int nv, final WB_AABB aabb) {
		return getVoronoi3D(points, nv, aabb, 0);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_Coord[] points,
			final WB_AABB aabb, final WB_ScalarParameter d) {
		return getVoronoi3D(points, points.length, aabb, d);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_Coord[] points,
			final WB_AABB aabb, final double d) {
		return getVoronoi3D(points, points.length, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_Coord[] points,
			final WB_AABB aabb) {
		return getVoronoi3D(points, points.length, aabb, 0);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(
			final List<? extends WB_Coord> points, final WB_AABB aabb,
			final WB_ScalarParameter d) {
		return getVoronoi3D(points, points.size(), aabb, d);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(
			final List<? extends WB_Coord> points, final WB_AABB aabb,
			final double d) {
		return getVoronoi3D(points, points.size(), aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(
			final List<? extends WB_Coord> points, final WB_AABB aabb) {
		return getVoronoi3D(points, points.size(), aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(
			final List<? extends WB_Coord> points, int nv, final WB_AABB aabb,
			final WB_ScalarParameter d) {
		
		return getVoronoi3D(WB_CoordCollection.getCollection(points),nv,aabb,d);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(
			final List<? extends WB_Coord> points, final int nv,
			final WB_AABB aabb, final double d) {
		return getVoronoi3D(points, nv, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(
			final List<? extends WB_Coord> points, final int nv,
			final WB_AABB aabb) {
		return getVoronoi3D(points, nv, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_CoordCollection points,
			final WB_AABB aabb, final WB_ScalarParameter d) {
		return getVoronoi3D(points, points.size(), aabb, d);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_CoordCollection points,
			final WB_AABB aabb, final double d) {
		return getVoronoi3D(points, points.size(), aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_CoordCollection points,
			final WB_AABB aabb) {
		return getVoronoi3D(points, points.size(), aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_CoordCollection points,
			int nv, final WB_AABB aabb, final WB_ScalarParameter d) {
		nv = Math.min(nv, points.size());
		if (nv <= 4) {
			return getVoronoi3DBruteForce(points, nv, aabb, d);
		}
		final int n = points.size();
		final List<wblut.external.ProGAL.Point> tmppoints = new ArrayList<wblut.external.ProGAL.Point>(
				n);
		final WB_KDTreeInteger3D<WB_Coord> tree = new WB_KDTreeInteger3D<WB_Coord>();
		int i = 0;
		WB_Coord p;
		for (i = 0; i < points.size(); i++) {
			p = points.get(i);
			tmppoints.add(
					new wblut.external.ProGAL.Point(p.xd(), p.yd(), p.zd()));
			tree.add(p, i);
		}
		final DelaunayComplex dc = new DelaunayComplex(tmppoints);
		final List<CVertex> vertices = dc.getVertices();
		final List<WB_VoronoiCell3D> result = new FastList<WB_VoronoiCell3D>();
		for (i = 0; i < nv; i++) {
			final CVertex v = vertices.get(i);
			final Set<CTetrahedron> vertexhull = dc.getVertexHull(v);
			v.getAdjacentTriangles();
			final List<WB_Point> hullpoints = new ArrayList<WB_Point>();
			for (final CTetrahedron tetra : vertexhull) {
				// if (!tetra.containsBigPoint()) {
				hullpoints.add(toPoint(tetra.circumcenter()));
				
				// }
			}
			final List<WB_Point> finalpoints = new FastList<WB_Point>();
			for (int j = 0; j < hullpoints.size(); j++) {
				finalpoints.add(geometryfactory.createPoint(hullpoints.get(j)));
			}
			final int index = tree.getNearestNeighbor(toPoint(v)).value;
			final WB_VoronoiCell3D vor = new WB_VoronoiCell3D(finalpoints,
					geometryfactory.createPoint(points.get(index)), index);
			if (vor.cell != null) {
				vor.constrain(aabb);
			}
			if (vor.cell != null) {
				vor.trim(d.evaluate(vor.generator.xd(), vor.generator.yd(),
						vor.generator.zd()));
			}
			if (vor.cell != null && vor.cell.getNumberOfVertices() > 0) {
				result.add(vor);
			}
		}
		return new WB_Voronoi3D(result);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_CoordCollection points,
			final int nv, final WB_AABB aabb, final double d) {
		return getVoronoi3D(points, nv, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3D(final WB_CoordCollection points,
			final int nv, final WB_AABB aabb) {
		return getVoronoi3D(points, nv, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static int[][] getVoronoi3DNeighbors(final WB_Coord[] points) {
		return getVoronoi3DNeighbors(WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static int[][] getVoronoi3DNeighbors(
			final List<? extends WB_Coord> points) {
		return getVoronoi3DNeighbors(WB_CoordCollection.getCollection(points));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public static int[][] getVoronoi3DNeighbors(
			final WB_CoordCollection points) {
		final int nv = points.size();
		if (nv == 2) {
			return new int[][] { { 1 }, { 0 } };
		} else if (nv == 3) {
			return new int[][] { { 1, 2 }, { 0, 2 }, { 0, 1 } };
		} else if (nv == 4) {
			return new int[][] { { 1, 2, 3 }, { 0, 2, 3 }, { 0, 1, 3 },
					{ 0, 1, 2 } };
		}
		final List<wblut.external.ProGAL.Point> tmppoints = new ArrayList<wblut.external.ProGAL.Point>(
				nv);
		final WB_KDTreeInteger3D<WB_Coord> tree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_Coord p;
		for (int i = 0; i < nv; i++) {
			p = points.get(i);
			tmppoints.add(
					new wblut.external.ProGAL.Point(p.xd(), p.yd(), p.zd()));
			tree.add(p, i);
		}
		final DelaunayComplex dc = new DelaunayComplex(tmppoints);
		final List<CVertex> vertices = dc.getVertices();
		final int[][] ns = new int[nv][];
		for (int i = 0; i < nv; i++) {
			final CVertex v = vertices.get(i);
			final Set<CTetrahedron> vertexhull = dc.getVertexHull(v);
			final IntHashSet neighbors = new IntHashSet();
			for (final CTetrahedron tetra : vertexhull) {
				for (int j = 0; j < 4; j++) {
					if (!tetra.getPoint(j).isBigpoint()) {
						neighbors.add(tree.getNearestNeighbor(
								toPoint(tetra.getPoint(j))).value);
					}
				}
			}
			ns[i] = neighbors.toArray();
		}
		return ns;
	}

	public static int[][] getVoronoi3DNeighborPairs(
			final WB_CoordCollection points) {
		final int nv = points.size();
		if (nv == 2) {
			return new int[][] { {0,1 } };
		} else if (nv == 3) {
			return new int[][] { { 0, 1 }, { 1, 2 }, { 2, 0 } };
		} else if (nv == 4) {
			return new int[][] { { 0,1 }, { 0,2 }, { 0,3 },
					{ 1, 2 },{ 1, 3 },{ 2, 3 } };
		}
		final List<wblut.external.ProGAL.Point> tmppoints = new ArrayList<wblut.external.ProGAL.Point>(
				nv);
		final WB_KDTreeInteger3D<WB_Coord> tree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_Coord p;
		for (int i = 0; i < nv; i++) {
			p = points.get(i);
			tmppoints.add(
					new wblut.external.ProGAL.Point(p.xd(), p.yd(), p.zd()));
			tree.add(p, i);
		}
		final DelaunayComplex dc = new DelaunayComplex(tmppoints);
		final List<CVertex> vertices = dc.getVertices();
		final int[][] ns = new int[nv][];
		int numberOfPairs=0;
		for (int i = 0; i < nv; i++) {
			final CVertex v = vertices.get(i);
			final Set<CTetrahedron> vertexhull = dc.getVertexHull(v);
			final IntHashSet neighbors = new IntHashSet();
			for (final CTetrahedron tetra : vertexhull) {
				for (int j = 0; j < 4; j++) {
					if (!tetra.getPoint(j).isBigpoint()) {
						neighbors.add(tree.getNearestNeighbor(
								toPoint(tetra.getPoint(j))).value);
					}
				}
			}
			ns[i] = neighbors.toArray();
			 numberOfPairs+=ns[i].length;
		}
		int[][] pairs=new int[numberOfPairs/2][2];
		int index=0;
		for (int i = 0; i < nv; i++) {
			for(int j=0;j<ns[i].length;j++) {
				if(i<ns[i][j]) {
					pairs[index][0]=i;
					pairs[index++][1]=ns[i][j];
				}
			}
		}
		int[][] finalPairs=new int[index][2];
		for(int i=0;i<index;i++) {
			finalPairs[i]=pairs[i];
		}
		
		
		return finalPairs;
	}

	public static int[][] getVoronoi3DNeighborPairs(final WB_Coord[] points) {
		return getVoronoi3DNeighborPairs(WB_CoordCollection.getCollection(points));
	}

	public static int[][] getVoronoi3DNeighborPairs(
			final List<? extends WB_Coord> points) {
		return getVoronoi3DNeighborPairs(WB_CoordCollection.getCollection(points));
	}

	public static WB_Network getNetwork(final WB_Coord[] points) {
		return new WB_Network(points,getVoronoi3DNeighborPairs(points));
	}

	public static WB_Network getNetwork(final List<? extends WB_Coord> points) {
		return new WB_Network(points,getVoronoi3DNeighborPairs(points));
	}

	public static WB_Network getNetwork(final WB_CoordCollection points) {
		
		final int n = points.size();
		final List<wblut.external.ProGAL.Point> tmppoints = new ArrayList<wblut.external.ProGAL.Point>(
				n);
		
		int i = 0;
		WB_Coord p;
		for (i = 0; i < points.size(); i++) {
			p = points.get(i);
			tmppoints.add(
					new wblut.external.ProGAL.Point(p.xd(), p.yd(), p.zd()));
		}
		final DelaunayComplex dc = new DelaunayComplex(tmppoints);
		final List<CTetrahedron> tetrahedra = dc.getTetrahedra();
		
		final WB_KDTreeInteger3D<WB_Coord> tree = new WB_KDTreeInteger3D<WB_Coord>();
		List<WB_Coord> vorPoints=new FastList<WB_Coord>();
		int index=0;
		WB_Point vorPoint;
		for(CTetrahedron tetra:tetrahedra) {
			vorPoint=toPoint(tetra.circumcenter());
			vorPoints.add(vorPoint);
			tree.add(vorPoint,index++);
		}
		
		final List<CTriangle> triangles = dc.getTriangles();
		CTetrahedron tetra1,tetra2;
		int i1,i2;
		List<int[]> pairs=new FastList<int[]>();
		for(CTriangle triangle:triangles) {
			tetra1=triangle.getAdjacentTetrahedron(0);
			if((tetra1==null)||(tetra1.containsBigPoint())) continue;
			tetra2=triangle.getAdjacentTetrahedron(1);
			if((tetra2==null)||(tetra2.containsBigPoint())) continue;
			i1=tree.getNearestNeighbor(toPoint(tetra1.circumcenter())).value;
			i2=tree.getNearestNeighbor(toPoint(tetra2.circumcenter())).value;
			if(i1<i2) {
				pairs.add(new int[] {i1,i2});
				
			}
		}
		
		int[][] finalPairs=new int[pairs.size()][2];
		for(int j=0;j<pairs.size();j++) {
			finalPairs[j]=pairs.get(j);
		}
		
		
		return new WB_Network(vorPoints,finalPairs);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final List<? extends WB_Coord> points, int nv, final WB_AABB aabb,
			final WB_ScalarParameter d) {
		return getVoronoi3DBruteForce(WB_CoordCollection.getCollection(points),nv,aabb,d);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final List<? extends WB_Coord> points, final int nv,
			final WB_AABB aabb, final double d) {
		return getVoronoi3DBruteForce(points, nv, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final List<? extends WB_Coord> points, final int nv,
			final WB_AABB aabb) {
		return getVoronoi3DBruteForce(points, nv, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final List<? extends WB_Coord> points, final WB_AABB aabb,
			final WB_ScalarParameter d) {
		return getVoronoi3DBruteForce(points, points.size(), aabb, d);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final List<? extends WB_Coord> points, final WB_AABB aabb,
			final double d) {
		return getVoronoi3DBruteForce(points, points.size(), aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final List<? extends WB_Coord> points, final WB_AABB aabb) {
		return getVoronoi3DBruteForce(points, points.size(), aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(final WB_Coord[] points,
			int nv, final WB_AABB aabb, final WB_ScalarParameter d) {
		return getVoronoi3DBruteForce(WB_CoordCollection.getCollection(points),nv,aabb,d);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(final WB_Coord[] points,
			final int nv, final WB_AABB aabb, final double d) {
		return getVoronoi3DBruteForce(points, nv, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(final WB_Coord[] points,
			final int nv, final WB_AABB aabb) {
		return getVoronoi3DBruteForce(points, nv, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(final WB_Coord[] points,
			final WB_AABB aabb, final WB_ScalarParameter d) {
		return getVoronoi3DBruteForce(points, points.length, aabb, d);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(final WB_Coord[] points,
			final WB_AABB aabb, final double d) {
		return getVoronoi3DBruteForce(points, points.length, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 *
	 * @param points
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(final WB_Coord[] points,
			final WB_AABB aabb) {
		return getVoronoi3DBruteForce(points, points.length, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final WB_CoordCollection points, int nv, final WB_AABB aabb,
			final WB_ScalarParameter d) {
		nv = Math.min(nv, points.size());
		final int n = points.size();
		final List<WB_VoronoiCell3D> result = new FastList<WB_VoronoiCell3D>();
		for (int i = 0; i < nv; i++) {
			final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
			final WB_Point O = new WB_Point();
			WB_Plane P;
			final WB_SimpleMesh cell = geometryfactory.createMesh(aabb);
			for (int j = 0; j < n; j++) {
				if (j != i) {
					final WB_Vector N = new WB_Vector(points.get(i));
					N.subSelf(points.get(j));
					N.normalizeSelf();
					O.set(points.get(i)); // plane origin=point halfway
					// between point i and point j
					O.addSelf(points.get(j));
					O.mulSelf(0.5);
					P = new WB_Plane(O, N);
					cutPlanes.add(P);
				}
			}
			boolean unique;
			final ArrayList<WB_Plane> cleaned = new ArrayList<WB_Plane>();
			for (int j = 0; j < cutPlanes.size(); j++) {
				P = cutPlanes.get(j);
				unique = true;
				for (int k = 0; k < j; k++) {
					final WB_Plane Pj = cutPlanes.get(j);
					if (WB_GeometryOp.isEqual(P, Pj)) {
						unique = false;
						break;
					}
				}
				if (unique) {
					cleaned.add(P);
				}
			}
			final WB_VoronoiCell3D vor = new WB_VoronoiCell3D(cell,
					geometryfactory.createPoint(points.get(i)), i);
			if (vor.cell != null) {
				vor.constrain(cutPlanes);
			}
			if (vor.cell != null) {
				vor.trim(d.evaluate(vor.generator.xd(), vor.generator.yd(),
						vor.generator.zd()));
			}
			if (vor.cell != null && vor.cell.getNumberOfVertices() > 0) {
				result.add(vor);
			}
			result.add(vor);
		}
		return new WB_Voronoi3D(result);
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final WB_CoordCollection points, final int nv, final WB_AABB aabb,
			final double d) {
		return getVoronoi3DBruteForce(points, nv, aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param nv
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final WB_CoordCollection points, final int nv, final WB_AABB aabb) {
		return getVoronoi3DBruteForce(points, nv, aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final WB_CoordCollection points, final WB_AABB aabb,
			final WB_ScalarParameter d) {
		return getVoronoi3DBruteForce(points, points.size(), aabb, d);
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @param d
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final WB_CoordCollection points, final WB_AABB aabb,
			final double d) {
		return getVoronoi3DBruteForce(points, points.size(), aabb,
				new WB_ConstantScalarParameter(d));
	}

	/**
	 *
	 * @param points
	 * @param aabb
	 * @return
	 */
	public static WB_Voronoi3D getVoronoi3DBruteForce(
			final WB_CoordCollection points, final WB_AABB aabb) {
		return getVoronoi3DBruteForce(points, points.size(), aabb,
				new WB_ConstantScalarParameter(0));
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	private static WB_Point toPoint(final wblut.external.ProGAL.Point v) {
		return geometryfactory.createPoint(v.x(), v.y(), v.z());
	}

	public static void main(String[] args) {
		HEC_Geodesic creator = new HEC_Geodesic().setB(3).setC(3)
				.setRadius(400);
		HE_Mesh container = new HE_Mesh(creator);
		int numpoints = 11;
		WB_Point[] points = new WB_Point[numpoints];
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 1; j++) {
				points[i] = new WB_Point(-200 + i * 40, -200 + j * 40, 0);
			}
		}
		HEMC_VoronoiCells multiCreator = new HEMC_VoronoiCells();
		multiCreator.setPoints(points);
		multiCreator.setContainer(container);
		multiCreator.setOffset(5);
		List<HE_Mesh> cells = (multiCreator.create()).toList();
		cells.size();
	}
}
