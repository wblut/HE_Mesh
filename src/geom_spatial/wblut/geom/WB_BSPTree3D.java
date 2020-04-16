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

public class WB_BSPTree3D {
	private WB_BSPNode3D root;

	public WB_BSPTree3D() {
		root = null;
	}

	private void build(final WB_BSPNode3D tree, final List<WB_Polygon> polygons) {
		if (polygons.size() > 0) {
			WB_Polygon cpol = null;
			final Iterator<WB_Polygon> PItr = polygons.iterator();
			do {
				if (PItr.hasNext()) {
					cpol = PItr.next();
				}
				tree.partition = cpol.getPlane();
			} while (tree.partition == null && PItr.hasNext());
			if (tree.partition == null) {
				return;
			}
			final FastList<WB_Polygon> _pols = new FastList<>();
			_pols.add(cpol);
			final FastList<WB_Polygon> pos_list = new FastList<>();
			final FastList<WB_Polygon> neg_list = new FastList<>();
			WB_Polygon pol = null;
			while (PItr.hasNext()) {
				pol = PItr.next();
				final WB_Classification result = WB_GeometryOp3D.classifyPolygonToPlane3D(pol, tree.partition);
				if (result == WB_Classification.FRONT) {
					pos_list.add(pol);
				} else if (result == WB_Classification.BACK) {
					neg_list.add(pol);
				} else if (result == WB_Classification.CROSSING) {
					final WB_Polygon[] polys = splitPolygon(pol, tree.partition);
					if (polys[0] != null) {
						pos_list.add(polys[0]);
					}
					if (polys[1] != null) {
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

	private void build(final WB_BSPNode3D tree, final WB_Polygon[] polygons) {
		if (polygons.length > 0) {
			final WB_Polygon cpol = polygons[0];
			tree.partition = cpol.getPlane();
			final FastList<WB_Polygon> _pols = new FastList<>();
			_pols.add(cpol);
			final FastList<WB_Polygon> pos_list = new FastList<>();
			final FastList<WB_Polygon> neg_list = new FastList<>();
			WB_Polygon pol = null;
			for (int i = 1; i < polygons.length; i++) {
				pol = polygons[i];
				final WB_Classification result = WB_GeometryOp3D.classifyPolygonToPlane3D(pol, tree.partition);
				if (result == WB_Classification.FRONT) {
					pos_list.add(pol);
				} else if (result == WB_Classification.BACK) {
					neg_list.add(pol);
				} else if (result == WB_Classification.CROSSING) {
					final WB_Polygon[] polys = splitPolygon(pol, tree.partition);
					if (polys[0] != null) {
						pos_list.add(polys[0]);
					}
					if (polys[1] != null) {
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

	public void build(final List<WB_Polygon> polygons) {
		if (root == null) {
			root = new WB_BSPNode3D();
		}
		build(root, polygons);
	}

	public void build(final WB_Polygon[] polygons) {
		if (root == null) {
			root = new WB_BSPNode3D();
		}
		build(root, polygons);
	}

	public void build(final HE_Mesh mesh) {
		if (root == null) {
			root = new WB_BSPNode3D();
		}
		build(root, mesh.getPolygons());
	}

	public int pointLocation(final WB_Coord p) {
		return pointLocation(root, p);
	}

	public int pointLocation(final double x, final double y, final double z) {
		return pointLocation(root, new WB_Point(x, y, z));
	}

	private int pointLocation(final WB_BSPNode3D node, final WB_Coord p) {
		final WB_Classification type = WB_GeometryOp3D.classifyPointToPlane3D(p, node.partition);
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
				if (WB_Epsilon.isZeroSq(WB_GeometryOp3D.getSqDistance3D(p, node.polygons.get(i)))) {
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

	public void partitionPolygon(final WB_Polygon P, final List<WB_Polygon> pos, final List<WB_Polygon> neg,
			final List<WB_Polygon> coSame, final List<WB_Polygon> coDiff) {
		partitionPolygon(root, P, pos, neg, coSame, coDiff);
	}

	private void partitionPolygon(final WB_BSPNode3D node, final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg, final List<WB_Polygon> coSame, final List<WB_Polygon> coDiff) {
		final WB_Classification type = WB_GeometryOp3D.classifyPolygonToPlane3D(P, node.partition);
		if (type == WB_Classification.CROSSING) {
			final WB_Polygon[] polygons = splitPolygon(P, node.partition);
			if (polygons[0] != null) {
				getPolygonPosPartition(node, polygons[0], pos, neg, coSame, coDiff);
			}
			if (polygons[1] != null) {
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

	private void partitionCoincidentPolygons(final WB_BSPNode3D node, final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg, final List<WB_Polygon> coSame, final List<WB_Polygon> coDiff) {
	}

	private void getPolygonPosPartition(final WB_BSPNode3D node, final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg, final List<WB_Polygon> coSame, final List<WB_Polygon> coDiff) {
		if (node.pos != null) {
			partitionPolygon(node.pos, P, pos, neg, coSame, coDiff);
		} else {
			pos.add(P);
		}
	}

	private void getPolygonNegPartition(final WB_BSPNode3D node, final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg, final List<WB_Polygon> coSame, final List<WB_Polygon> coDiff) {
		if (node.neg != null) {
			partitionPolygon(node.neg, P, pos, neg, coSame, coDiff);
		} else {
			neg.add(P);
		}
	}

	public void partitionMesh(final HE_Mesh mesh, final List<HE_Mesh> pos, final List<HE_Mesh> neg) {
		partitionMesh(root, mesh, pos, neg);
	}

	private void partitionMesh(final WB_BSPNode3D node, final HE_Mesh mesh, final List<HE_Mesh> pos,
			final List<HE_Mesh> neg) {
		final HEMC_SplitMesh sm = new HEMC_SplitMesh();
		sm.setMesh(mesh);
		sm.setPlane(node.partition);
		final HE_MeshCollection split = sm.create();
		if (split.getMesh(0).getNumberOfVertices() > 4) {
			getMeshPosPartition(node, split.getMesh(0), pos, neg);
		}
		if (split.getMesh(1).getNumberOfVertices() > 4) {
			getMeshNegPartition(node, split.getMesh(1), pos, neg);
		}
	}

	private void getMeshPosPartition(final WB_BSPNode3D node, final HE_Mesh mesh, final List<HE_Mesh> pos,
			final List<HE_Mesh> neg) {
		if (node.pos != null) {
			partitionMesh(node.pos, mesh, pos, neg);
		} else {
			pos.add(mesh);
		}
	}

	private void getMeshNegPartition(final WB_BSPNode3D node, final HE_Mesh mesh, final List<HE_Mesh> pos,
			final List<HE_Mesh> neg) {
		if (node.neg != null) {
			partitionMesh(node.neg, mesh, pos, neg);
		} else {
			neg.add(mesh);
		}
	}

	public ArrayList<WB_Polygon> toPolygons() {
		final ArrayList<WB_Polygon> polygons = new ArrayList<>();
		addPolygons(root, polygons);
		return polygons;
	}

	private void addPolygons(final WB_BSPNode3D node, final ArrayList<WB_Polygon> polygons) {
		polygons.addAll(node.polygons);
		if (node.pos != null) {
			addPolygons(node.pos, polygons);
		}
		if (node.neg != null) {
			addPolygons(node.neg, polygons);
		}
	}

	public WB_BSPTree3D negate() {
		final WB_BSPTree3D negTree = new WB_BSPTree3D();
		negTree.root = negate(root);
		return negTree;
	}

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

	static WB_Polygon[] splitPolygon(final WB_Polygon poly, final WB_Plane P) {
		int numFront = 0;
		int numBack = 0;
		final WB_Polygon[] polygons = new WB_Polygon[2];
		final WB_CoordList frontVerts = new WB_CoordList();
		final WB_CoordList backVerts = new WB_CoordList();
		final int numVerts = poly.getNumberOfPoints();
		if (numVerts > 0) {
			WB_Coord a = poly.getPoint(numVerts - 1);
			WB_Classification aSide = WB_GeometryOp3D.classifyPointToPlane3D(a, P);
			WB_Coord b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				final WB_IntersectionResult i;
				b = poly.getPoint(n);
				bSide = WB_GeometryOp3D.classifyPointToPlane3D(b, P);
				if (bSide == WB_Classification.FRONT) {
					if (aSide == WB_Classification.BACK) {
						i = WB_GeometryOp3D.getIntersection3D(b, a, P);
						frontVerts.add((WB_Coord) i.object);
						numFront++;
						backVerts.add((WB_Coord) i.object);
						numBack++;
					}
					frontVerts.add(b);
					numFront++;
				} else if (bSide == WB_Classification.BACK) {
					if (aSide == WB_Classification.FRONT) {
						i = WB_GeometryOp3D.getIntersection3D(a, b, P);
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
			if (numFront > 2) {
				polygons[0] = new WB_Polygon(frontVerts);
			}
			if (numBack > 2) {
				polygons[1] = new WB_Polygon(backVerts);
			}
		}
		return polygons;
	}

	public static void main(final String[] args) {
		final HEC_Dodecahedron creator = new HEC_Dodecahedron();
		creator.setRadius(150);
		final HE_Mesh mesh = new HE_Mesh(creator);
		mesh.modify(new HEM_Crocodile().setDistance(150));
		final WB_BSPTree3D tree = new WB_BSPTree3D();
		tree.build(mesh);
		creator.setRadius(250);
		final HE_Mesh mesh2 = new HE_Mesh(creator);
		final List<HE_Mesh> pos = new ArrayList<>();
		final List<HE_Mesh> neg = new ArrayList<>();
		tree.partitionMesh(mesh2, pos, neg);
	}
}
