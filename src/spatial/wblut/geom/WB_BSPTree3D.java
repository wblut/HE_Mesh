/**
 * 
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.hemesh.HEC_Dodecahedron;
import wblut.hemesh.HEMC_SplitMesh;
import wblut.hemesh.HEM_Crocodile;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_MeshCollection;
import wblut.math.WB_Epsilon;
/**
 * WB_BSPTree.
 * 
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_BSPTree3D {

	private WB_BSPNode3D root;

	public WB_BSPTree3D() {
		root = null;
	}

	private void build(final WB_BSPNode3D tree,
			final List<WB_Polygon> polygons) {
		if (polygons.size() > 0) {
			WB_Polygon cpol = null;
			final Iterator<WB_Polygon> PItr = polygons.iterator();
			do{
			if (PItr.hasNext()) {
				cpol = PItr.next();
			}
			tree.partition = cpol.getPlane();
			}
			while(tree.partition==null && PItr.hasNext());
			if(tree.partition==null) return;
			final FastList<WB_Polygon> _pols = new FastList<WB_Polygon>();

			_pols.add(cpol);
			final FastList<WB_Polygon> pos_list = new FastList<WB_Polygon>();
			final FastList<WB_Polygon> neg_list = new FastList<WB_Polygon>();
			WB_Polygon pol = null;
			
			while (PItr.hasNext()) {
				pol = PItr.next();
				final WB_Classification result = WB_GeometryOp.classifyPolygonToPlane3D(pol,tree.partition);

				if (result == WB_Classification.FRONT) {
					pos_list.add(pol);
				} else if (result == WB_Classification.BACK) {
					neg_list.add(pol);
				} else if (result == WB_Classification.CROSSING) { /* spanning */

					WB_Polygon[] polys=splitPolygon(pol,tree.partition);
					if (polys[0] !=null) {
						pos_list.add(polys[0]);
					}
					if (polys[1] !=null) {
						neg_list.add(polys[1]);
					}
				} else if (result == WB_Classification.ON) {
					_pols.add(pol);
				}
			}
			if (!pos_list.isEmpty()) {
				tree.pos = new WB_BSPNode3D();
				build(tree.pos, pos_list);
			}
			if (!neg_list.isEmpty()) {
				tree.neg = new WB_BSPNode3D();
				build(tree.neg, neg_list);
			}
			if (tree.polygons != null) {
				tree.polygons.clear();
			}
			tree.polygons.addAll(_pols);
		}
	}

	/**
	 * Builds the.
	 * 
	 * @param tree
	 *            the tree
	 * @param polygons
	 *            the polygons
	 */
	private void build(final WB_BSPNode3D tree,
			final WB_Polygon[] polygons) {
		if (polygons.length > 0) {
			final WB_Polygon cpol = polygons[0];

			tree.partition = cpol.getPlane();
			final FastList<WB_Polygon> _pols = new FastList<WB_Polygon>();

			_pols.add(cpol);
			final FastList<WB_Polygon> pos_list = new FastList<WB_Polygon>();
			final FastList<WB_Polygon> neg_list = new FastList<WB_Polygon>();
			WB_Polygon pol = null;
			for (int i = 1; i < polygons.length; i++) {
				pol = polygons[i];
				final WB_Classification result = WB_GeometryOp.classifyPolygonToPlane3D(pol,tree.partition);

				if (result == WB_Classification.FRONT) {
					pos_list.add(pol);
				} else if (result == WB_Classification.BACK) {
					neg_list.add(pol);
				} else if (result == WB_Classification.CROSSING) { /* spanning */

					
					WB_Polygon[] polys=splitPolygon(pol,tree.partition);
					if (polys[0] !=null) {
						pos_list.add(polys[0]);
					}
					if (polys[1] !=null) {
						neg_list.add(polys[1]);
					}
				} else if (result == WB_Classification.ON) {
					_pols.add(pol);
				}
			}
			if (!pos_list.isEmpty()) {
				tree.pos = new WB_BSPNode3D();
				build(tree.pos, pos_list);
			}
			if (!neg_list.isEmpty()) {
				tree.neg = new WB_BSPNode3D();
				build(tree.neg, neg_list);
			}
			if (tree.polygons != null) {
				tree.polygons.clear();
			}
			tree.polygons.addAll(_pols);
		}
	}

	/**
	 * Builds the.
	 * 
	 * @param polygons
	 *            the polygons
	 */
	public void build(final List<WB_Polygon> polygons) {
		if (root == null) {
			root = new WB_BSPNode3D();
		}
		build(root, polygons);
	}

	/**
	 * Builds the.
	 * 
	 * @param polygons
	 *            the polygons
	 */
	public void build(final WB_Polygon[] polygons) {
		if (root == null) {
			root = new WB_BSPNode3D();
		}
		build(root, polygons);
	}

	/**
	 * Builds the.
	 * 
	 * @param mesh
	 *            the mesh
	 */
	public void build(final HE_Mesh mesh) {
		if (root == null) {
			root = new WB_BSPNode3D();
		}

		build(root, mesh.getPolygons());
	}

	/**
	 * Point location.
	 * 
	 * @param p
	 *            the p
	 * @return the int
	 */
	public int pointLocation(final WB_Coord p) {
		return pointLocation(root, p);

	}

	/**
	 * Point location.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @return the int
	 */
	public int pointLocation(final double x, final double y, final double z) {
		return pointLocation(root, new WB_Point(x, y, z));

	}

	/**
	 * Point location.
	 * 
	 * @param node
	 *            the node
	 * @param p
	 *            the p
	 * @return the int
	 */
	private int pointLocation(final WB_BSPNode3D node, final WB_Coord p) {
		final WB_Classification type = WB_GeometryOp.
				classifyPointToPlane3D(p,node.partition);
		if (type == WB_Classification.FRONT) {
			if (node.pos != null) {
				return pointLocation(node.pos, p);
			} else {
				return 1;
			}
		} else if (type == WB_Classification.BACK) {
			if (node.neg != null) {
				return pointLocation(node.neg, p);
			} else {
				return -1;
			}
		} else {
			for (int i = 0; i < node.polygons.size(); i++) {
				if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistance3D(p,
						node.polygons.get(i)))) {
					return 0;
				}
			}
			if (node.pos != null) {
				return pointLocation(node.pos, p);
			} else if (node.neg != null) {
				return pointLocation(node.neg, p);
			} else {
				return 0;

			}
		}
	}

	public void partitionPolygon(final WB_Polygon P,
			final List<WB_Polygon> pos,
			final List<WB_Polygon> neg,
			final List<WB_Polygon> coSame,
			final List<WB_Polygon> coDiff) {

		partitionPolygon(root, P, pos, neg, coSame, coDiff);

	}

	private void partitionPolygon(final WB_BSPNode3D node,
			final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg,
			final List<WB_Polygon> coSame,
			final List<WB_Polygon> coDiff) {

		final WB_Classification type = 
				WB_GeometryOp.classifyPolygonToPlane3D(P,node.partition);

		if (type == WB_Classification.CROSSING) {


			WB_Polygon[] polygons=splitPolygon(P,node.partition);
			if (polygons[0] !=null) {
				getPolygonPosPartition(node, polygons[0], pos, neg, coSame,
						coDiff);
			}
			if (polygons[1] !=null) {
				getPolygonNegPartition(node, polygons[1], pos, neg, coSame, coDiff);
			}

		} else if (type == WB_Classification.FRONT) {
			getPolygonPosPartition(node, P, pos, neg, coSame, coDiff);

		} else if (type == WB_Classification.BACK) {
			getPolygonNegPartition(node, P, pos, neg, coSame, coDiff);

		} else if (type == WB_Classification.ON) {
			partitionCoincidentPolygons(node, P, pos, neg, coSame, coDiff);
		}

	}

	/**
	 * Partition coincident polygons.
	 * 
	 * @param node
	 *            the node
	 * @param P
	 *            the p
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 * @param coSame
	 *            the co same
	 * @param coDiff
	 *            the co diff
	 */
	private void partitionCoincidentPolygons(final WB_BSPNode3D node,
			final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg,
			final List<WB_Polygon> coSame,
			final List<WB_Polygon> coDiff) {

		/*
		 * FastList<> partSegments = new FastList<>();
		 * partSegments.add(S);  thisS, otherS; final WB_Line2D L =
		 * node.partition; for (int i = 0; i < node.segments.size(); i++) {
		 * final FastList<> newpartSegments = new
		 * FastList<>(); otherS = node.segments.get(i); final double
		 * v0 = L.getT(otherS.origin()); final double v1 = L.getT(otherS.end());
		 * for (int j = 0; j < partSegments.size(); j++) { thisS =
		 * partSegments.get(j); final double u0 = L.getT(thisS.origin()); final
		 * double u1 = L.getT(thisS.end()); double[] intersection; if (u0 <= u1)
		 * { intersection = WB_Intersection2D.intervalIntersection(u0, u1, v0,
		 * v1); if (intersection[0] == 2) { final WB_XY pi =
		 * L.getPoint(intersection[1]); final WB_XY pj =
		 * L.getPoint(intersection[2]); if (u0 < intersection[1]) {
		 * newpartSegments.add(new (thisS.origin(), pi)); }
		 * coSame.add(new (pi, pj)); if (u1 > intersection[2]) {
		 * newpartSegments .add(new (pj, thisS.end())); } } else {//
		 * this segment doesn't coincide with an edge
		 * newpartSegments.add(thisS); } } else { intersection =
		 * WB_Intersection2D.intervalIntersection(u1, u0, v0, v1); if
		 * (intersection[0] == 2) { final WB_XY pi =
		 * L.getPoint(intersection[1]); final WB_XY pj =
		 * L.getPoint(intersection[2]); if (u1 < intersection[1]) {
		 * newpartSegments .add(new (pi, thisS.end())); }
		 * coDiff.add(new (pj, pi)); if (u0 > intersection[2]) {
		 * newpartSegments.add(new (thisS.origin(), pj)); } } else {
		 * newpartSegments.add(thisS); } } } partSegments = newpartSegments; }
		 * for (int i = 0; i < partSegments.size(); i++) {
		 * getSegmentPosPartition(node, partSegments.get(i), pos, neg, coSame,
		 * coDiff); getSegmentNegPartition(node, partSegments.get(i), pos, neg,
		 * coSame, coDiff); }
		 */
	}

	/**
	 * Gets the polygon pos partition.
	 * 
	 * @param node
	 *            the node
	 * @param P
	 *            the p
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 * @param coSame
	 *            the co same
	 * @param coDiff
	 *            the co diff
	 * @return the polygon pos partition
	 */
	private void getPolygonPosPartition(final WB_BSPNode3D node,
			final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg,
			final List<WB_Polygon> coSame,
			final List<WB_Polygon> coDiff) {
		if (node.pos != null) {
			partitionPolygon(node.pos, P, pos, neg, coSame, coDiff);
		} else {
			pos.add(P);
		}

	}

	/**
	 * Gets the polygon neg partition.
	 * 
	 * @param node
	 *            the node
	 * @param P
	 *            the p
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 * @param coSame
	 *            the co same
	 * @param coDiff
	 *            the co diff
	 * @return the polygon neg partition
	 */
	private void getPolygonNegPartition(final WB_BSPNode3D node,
			final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg,
			final List<WB_Polygon> coSame,
			final List<WB_Polygon> coDiff) {
		if (node.neg != null) {
			partitionPolygon(node.neg, P, pos, neg, coSame, coDiff);
		} else {
			neg.add(P);
		}
	}

	/**
	 * Partition mesh.
	 * 
	 * @param mesh
	 *            the mesh
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 */
	public void partitionMesh(final HE_Mesh mesh, final List<HE_Mesh> pos,
			final List<HE_Mesh> neg) {

		partitionMesh(root, mesh, pos, neg);

	}

	/**
	 * Partition mesh.
	 * 
	 * @param node
	 *            the node
	 * @param mesh
	 *            the mesh
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 */
	private void partitionMesh(final WB_BSPNode3D node, final HE_Mesh mesh,
			final List<HE_Mesh> pos, final List<HE_Mesh> neg) {

		final HEMC_SplitMesh sm = new HEMC_SplitMesh();
		sm.setMesh(mesh);
		sm.setPlane(node.partition);
		final HE_MeshCollection split = sm.create();

		if (split.getMesh(0).getNumberOfVertices() > 4) {
			getMeshPosPartition(node, split.getMesh(0), pos, neg);
		}
		if (split.getMesh(1).getNumberOfVertices() > 4) {
			getMeshNegPartition(node,split.getMesh(1), pos, neg);
		}

	}

	/**
	 * Gets the mesh pos partition.
	 * 
	 * @param node
	 *            the node
	 * @param mesh
	 *            the mesh
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 * @return the mesh pos partition
	 */
	private void getMeshPosPartition(final WB_BSPNode3D node, final HE_Mesh mesh,
			final List<HE_Mesh> pos, final List<HE_Mesh> neg) {
		if (node.pos != null) {
			partitionMesh(node.pos, mesh, pos, neg);
		} else {
			pos.add(mesh);
		}

	}

	/**
	 * Gets the mesh neg partition.
	 * 
	 * @param node
	 *            the node
	 * @param mesh
	 *            the mesh
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 * @return the mesh neg partition
	 */
	private void getMeshNegPartition(final WB_BSPNode3D node, final HE_Mesh mesh,
			final List<HE_Mesh> pos, final List<HE_Mesh> neg) {
		if (node.neg != null) {
			partitionMesh(node.neg, mesh, pos, neg);
		} else {
			neg.add(mesh);
		}
	}

	/**
	 * To polygons.
	 * 
	 * @return the array list
	 */
	public ArrayList<WB_Polygon> toPolygons() {
		final ArrayList<WB_Polygon> polygons = new ArrayList<WB_Polygon>();
		addPolygons(root, polygons);
		return polygons;

	}

	/**
	 * Adds the polygons.
	 * 
	 * @param node
	 *            the node
	 * @param polygons
	 *            the polygons
	 */
	private void addPolygons(final WB_BSPNode3D node,
			final ArrayList<WB_Polygon> polygons) {
		polygons.addAll(node.polygons);
		if (node.pos != null) {
			addPolygons(node.pos, polygons);
		}
		if (node.neg != null) {
			addPolygons(node.neg, polygons);
		}

	}

	/**
	 * Negate.
	 * 
	 * @return the w b_ bsp tree
	 */
	public WB_BSPTree3D negate() {
		final WB_BSPTree3D negTree = new WB_BSPTree3D();
		negTree.root = negate(root);
		return negTree;
	}

	/**
	 * Negate.
	 * 
	 * @param node
	 *            the node
	 * @return the w b_ bsp node
	 */
	private WB_BSPNode3D negate(final WB_BSPNode3D node) {
		final WB_BSPNode3D negNode = new WB_BSPNode3D();
		negNode.partition = node.partition.get();
		negNode.partition.flipNormal();
		for (int i = 0; i < node.polygons.size(); i++) {
			final WB_Polygon pol = node.polygons.get(i);
			negNode.polygons.add(pol.negate());
		}
		if (node.pos != null) {
			negNode.neg = negate(node.pos);
		}
		if (node.neg != null) {
			negNode.pos = negate(node.neg);
		}
		return negNode;
	}
	
	static WB_Polygon[] splitPolygon(final WB_Polygon poly,
			final WB_Plane P) {
		int numFront = 0;
		int numBack = 0;
WB_Polygon[] polygons =new WB_Polygon[2];
		final FastList<WB_Coord> frontVerts = new FastList<WB_Coord>(20);
		final FastList<WB_Coord> backVerts = new FastList<WB_Coord>(20);

		final int numVerts = poly.getNumberOfPoints();
		if (numVerts > 0) {
			WB_Coord a = poly.getPoint(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp.classifyPointToPlane3D(a,P);
			WB_Coord b;
			WB_Classification bSide;

			for (int n = 0; n < numVerts; n++) {
				final WB_IntersectionResult i;
				b = poly.getPoint(n);
				bSide = WB_GeometryOp.classifyPointToPlane3D(b,P);
				if (bSide == WB_Classification.FRONT) {
					if (aSide == WB_Classification.BACK) {
						i = WB_GeometryOp.getIntersection3D(b, a, P);

						/*
						 * if (classifyPointToPlane(i.p1, P) !=
						 * ClassifyPointToPlane.POINT_ON_PLANE) { System.out
						 * .println("Inconsistency: intersection not on plane");
						 * }
						 */

						frontVerts.add((WB_Coord) i.object);
						numFront++;
						backVerts.add((WB_Coord) i.object);
						numBack++;
					}
					frontVerts.add(b);
					numFront++;
				} else if (bSide == WB_Classification.BACK) {
					if (aSide == WB_Classification.FRONT) {
						i = WB_GeometryOp.getIntersection3D(a, b, P);

						/*
						 * if (classifyPointToPlane(i.p1, P) !=
						 * ClassifyPointToPlane.POINT_ON_PLANE) { System.out
						 * .println("Inconsistency: intersection not on plane");
						 * }
						 */

						frontVerts.add((WB_Coord) i.object);
						numFront++;
						backVerts.add((WB_Coord) i.object);
						numBack++;
					} else if (aSide == WB_Classification.ON) {
						backVerts.add(a);
						numBack++;
					}
					backVerts.add(b);
					numBack++;
				} else {
					frontVerts.add(b);
					numFront++;
					if (aSide == WB_Classification.BACK) {
						backVerts.add(b);
						numBack++;
					}
				}
				a = b;
				aSide = bSide;

			}
			if(numFront>2)polygons[0]=new WB_Polygon(frontVerts);
			if(numBack>2)polygons[1]=new WB_Polygon(backVerts);
		}
		return polygons;
	}
	


	public static void main(String [] args)
	{
		 HEC_Dodecahedron creator=new HEC_Dodecahedron();
		  creator.setRadius(150); 
		 HE_Mesh  mesh=new HE_Mesh(creator); 
		  mesh.modify(new HEM_Crocodile().setDistance(150));
		  WB_BSPTree3D tree=new WB_BSPTree3D();
		  tree.build(mesh);
		  creator.setRadius(250); 
		  HE_Mesh mesh2=new HE_Mesh(creator); 
		  List<HE_Mesh> pos=new ArrayList<HE_Mesh>();
		  List<HE_Mesh> neg=new ArrayList<HE_Mesh>();
		   tree.partitionMesh(mesh2,pos,neg);
	}

	
	
}
