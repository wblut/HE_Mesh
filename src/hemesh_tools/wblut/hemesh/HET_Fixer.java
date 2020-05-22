package wblut.hemesh;

import java.util.Iterator;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.core.WB_ProgressReporter.WB_ProgressTracker;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HET_Fixer {
	/**  */
	public static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

	/**
	 *
	 *
	 * @param mesh
	 */
	public void fixHalfedgeFaceAssignment(final HE_Mesh mesh) {
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() != null) {
				mesh.setHalfedge(he.getFace(), he);
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public void fixHalfedgeVertexAssignment(final HE_Mesh mesh) {
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			mesh.setHalfedge(he.getVertex(), he);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param f
	 */
	public static void deleteTwoEdgeFace(final HE_Mesh mesh, final HE_Face f) {
		if (mesh.contains(f)) {
			final HE_Halfedge he = f.getHalfedge();
			final HE_Halfedge hen = he.getNextInFace();
			if (he == hen.getNextInFace()) {
				final HE_Halfedge hePair = he.getPair();
				final HE_Halfedge henPair = hen.getPair();
				mesh.remove(f);
				mesh.remove(he);
				mesh.setHalfedge(he.getVertex(), he.getNextInVertex());
				mesh.remove(hen);
				mesh.setHalfedge(hen.getVertex(), hen.getNextInVertex());
				mesh.setPair(hePair, henPair);
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void deleteTwoEdgeFaces(final HE_Mesh mesh) {
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			final HE_Halfedge he = f.getHalfedge();
			final HE_Halfedge hen = he.getNextInFace();
			if (he == hen.getNextInFace()) {
				final HE_Halfedge hePair = he.getPair();
				final HE_Halfedge henPair = hen.getPair();
				mesh.remove(f);
				mesh.remove(he);
				mesh.setHalfedge(he.getVertex(), he.getNextInVertex());
				mesh.remove(hen);
				mesh.setHalfedge(hen.getVertex(), hen.getNextInVertex());
				mesh.setPair(hePair, henPair);
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param v
	 */
	public static void deleteTwoEdgeVertex(final HE_Mesh mesh, final HE_Vertex v) {
		if (mesh.contains(v) && v.getVertexDegree() == 2) {
			final HE_Halfedge he0 = v.getHalfedge();
			final HE_Halfedge he1 = he0.getNextInVertex();
			final HE_Halfedge he0n = he0.getNextInFace();
			final HE_Halfedge he1n = he1.getNextInFace();
			final HE_Halfedge he0p = he0.getPair();
			final HE_Halfedge he1p = he1.getPair();
			mesh.setNext(he0p, he1n);
			mesh.setNext(he1p, he0n);
			if (he0.getFace() != null) {
				mesh.setHalfedge(he0.getFace(), he1p);
			}
			if (he1.getFace() != null) {
				mesh.setHalfedge(he1.getFace(), he0p);
			}
			mesh.setHalfedge(he0n.getVertex(), he0n);
			mesh.setHalfedge(he1n.getVertex(), he1n);
			mesh.setPair(he0p, he1p);
			mesh.remove(he0);
			mesh.remove(he1);
			mesh.remove(v);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void deleteTwoEdgeVertices(final HE_Mesh mesh) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		final HE_VertexList toremove = new HE_VertexList();
		while (vitr.hasNext()) {
			v = vitr.next();
			if (v.getVertexDegree() == 2) {
				toremove.add(v);
			}
		}
		for (final HE_Vertex vtr : toremove) {
			deleteTwoEdgeVertex(mesh, vtr);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void collapseDegenerateEdges(final HE_Mesh mesh) {
		final HE_HalfedgeList edgesToRemove = new HE_HalfedgeList();
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (WB_Epsilon.isZeroSq(WB_GeometryOp.getSqDistance3D(e.getVertex(), e.getEndVertex()))) {
				edgesToRemove.add(e);
			}
		}
		for (final HE_Halfedge element : edgesToRemove) {
			HE_MeshOp.collapseEdge(mesh, element);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 */
	public static void collapseDegenerateEdges(final HE_Mesh mesh, final double d) {
		final HE_HalfedgeList edgesToRemove = new HE_HalfedgeList();
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		final double d2 = d * d;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (WB_GeometryOp.getSqDistance3D(e.getVertex(), e.getEndVertex()) < d2) {
				edgesToRemove.add(e);
			}
		}
		for (final HE_Halfedge element : edgesToRemove) {
			HE_MeshOp.collapseEdge(mesh, element);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static boolean fixNonManifoldVerticesOnePass(final HE_Mesh mesh) {
		class VertexInfo {
			HE_HalfedgeList out;

			VertexInfo() {
				out = new HE_HalfedgeList();
			}
		}
		final HE_ObjectMap<VertexInfo> vertexLists = new HE_ObjectMap<>();
		HE_Vertex v;
		VertexInfo vi;
		WB_ProgressCounter counter = new WB_ProgressCounter(mesh.getNumberOfHalfedges(), 10);
		tracker.setCounterStatus("HET_Fixer", "Classifying halfedges per vertex.", counter);
		final HE_HalfedgeIterator heItr = mesh.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			v = he.getVertex();
			vi = vertexLists.get(v.getKey());
			if (vi == null) {
				vi = new VertexInfo();
				vertexLists.put(v.getKey(), vi);
			}
			vi.out.add(he);
			counter.increment();
		}
		final HE_VertexList toUnweld = new HE_VertexList();
		counter = new WB_ProgressCounter(mesh.getNumberOfVertices(), 10);
		tracker.setCounterStatus("HET_Fixer", "Checking vertex umbrellas.", counter);
		Iterator<HE_Vertex> vItr = mesh.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			final HE_HalfedgeList outgoing = vertexLists.get(v.getKey()).out;
			final HE_HalfedgeList vStar = v.getHalfedgeStar();
			if (outgoing.size() != vStar.size()) {
				toUnweld.add(v);
			}
		}
		vItr = toUnweld.iterator();
		counter = new WB_ProgressCounter(toUnweld.size(), 10);
		tracker.setCounterStatus("HET_Fixer", "Splitting vertex umbrellas. ", counter);
		while (vItr.hasNext()) {
			v = vItr.next();
			final HE_HalfedgeList vHalfedges = vertexLists.get(v.getKey()).out;
			final HE_HalfedgeList vStar = v.getHalfedgeStar();
			final HE_Vertex vc = new HE_Vertex(v);
			mesh.add(vc);
			for (final HE_Halfedge element : vStar) {
				mesh.setVertex(element, vc);
			}
			mesh.setHalfedge(vc, vStar.get(0));
			for (final HE_Halfedge vHalfedge : vHalfedges) {
				he = vHalfedge;
				if (he.getVertex() == v) {
					mesh.setHalfedge(v, he);
					break;
				}
			}
			counter.increment();
		}
		return toUnweld.size() > 0;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void fixDegenerateTriangles(final HE_Mesh mesh) {
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.isDegenerate() && f.getFaceDegree() == 3 && mesh.contains(f)) {
				double d = HE_MeshOp.getLength(f.getHalfedge());
				double dmax = d;
				HE_Halfedge he = f.getHalfedge();
				HE_Halfedge longesthe = he;
				if (d > WB_Epsilon.EPSILON) {
					do {
						he = he.getNextInFace();
						d = HE_MeshOp.getLength(he);
						if (WB_Epsilon.isZero(d)) {
							longesthe = he;
							break;
						}
						if (d > dmax) {
							longesthe = he;
							dmax = d;
						}
					} while (he != f.getHalfedge());
				}
				mesh.deleteEdge(longesthe);
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void fixNonManifoldVertices(final HE_Mesh mesh) {
		int counter = 0;
		do {
			counter++;
		} while (fixNonManifoldVerticesOnePass(mesh) || counter < 100);// Normally
		// this should
		// run at most
		// 3 or 4
		// times
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void deleteCollinearVertices(final HE_Mesh mesh) {
		final HE_VertexIterator vItr = mesh.vItr();
		HE_Vertex v;
		HE_Halfedge hef1, hef2, hepf1, hepf2, henf1, henf2;
		final HE_VertexList vertices = new HE_VertexList();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getVertexDegree() == 2) {
				hef1 = v.getHalfedge();
				hef2 = hef1.getPair();
				henf2 = hef2.getNextInFace();
				if (WB_Vector.dot(HE_MeshOp.getHalfedgeTangent(hef1), HE_MeshOp.getHalfedgeTangent(henf2)) < 1.0
						- WB_Epsilon.EPSILON) {
					vertices.add(v);
				}
			}
		}
		for (final HE_Vertex vv : vertices) {
			hef1 = vv.getHalfedge();
			hef2 = hef1.getPair();
			hepf1 = hef1.getPrevInFace();
			hepf2 = hef2.getPrevInFace();
			henf1 = hef1.getNextInFace();
			henf2 = hef2.getNextInFace();
			mesh.setNext(hepf1, henf1);
			mesh.setNext(hepf2, henf2);
			mesh.setVertex(henf2, hef2.getVertex());
			mesh.setHalfedge(hef2.getVertex(), henf2);
			if (hef1.getFace() != null) {
				if (hef1.getFace().getHalfedge() == hef1) {
					mesh.setHalfedge(hef1.getFace(), henf1);
				}
				hef1.getFace().clearPrecomputed();
			}
			if (hef2.getFace() != null) {
				if (hef2.getFace().getHalfedge() == hef2) {
					mesh.setHalfedge(hef2.getFace(), henf2);
				}
				hef2.getFace().clearPrecomputed();
			}
			mesh.remove(vv);
			mesh.remove(hef1);
			mesh.remove(hef2);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectCollinearVertices(final HE_Mesh mesh) {
		final HE_VertexIterator vItr = mesh.vItr();
		HE_Vertex v;
		HE_Halfedge hef1, hef2, henf2;
		final HE_VertexList vertices = new HE_VertexList();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getVertexDegree() == 2) {
				hef1 = v.getHalfedge();
				hef2 = hef1.getPair();
				henf2 = hef2.getNextInFace();
				if (WB_Vector.dot(HE_MeshOp.getHalfedgeTangent(hef1), HE_MeshOp.getHalfedgeTangent(henf2)) < 1.0
						- WB_Epsilon.EPSILON) {
					vertices.add(v);
				}
			}
		}
		final HE_Selection sel = new HE_Selection(mesh);
		sel.addVertices(vertices);
		return sel;
	}

	/**
	 *
	 *
	 * @param degree
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectVerticesWithDegree(final int degree, final HE_Mesh mesh) {
		final HE_VertexIterator vItr = mesh.vItr();
		HE_Vertex v;
		final HE_VertexList vertices = new HE_VertexList();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getVertexDegree() == degree) {
				vertices.add(v);
			}
		}
		final HE_Selection sel = new HE_Selection(mesh);
		sel.addVertices(vertices);
		return sel;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void deleteDegenerateTriangles(final HE_Mesh mesh) {
		final HE_FaceList faces = mesh.getFaces();
		HE_Halfedge he;
		for (final HE_Face face : faces) {
			if (!mesh.contains(face)) {
				continue; // face already removed by a previous change
			}
			if (face.isDegenerate()) {
				final int fo = face.getFaceDegree();
				if (fo == 3) {
					HE_Halfedge degeneratehe = null;
					he = face.getHalfedge();
					do {
						if (WB_Epsilon.isZero(HE_MeshOp.getLength(he))) {
							degeneratehe = he;
							break;
						}
						he = he.getNextInFace();
					} while (he != face.getHalfedge());
					if (degeneratehe != null) {
						// System.out.println("Zero length change!");
						HE_MeshOp.collapseHalfedge(mesh, he);
						continue;
					}
					he = face.getHalfedge();
					double d;
					double dmax = 0;
					do {
						d = HE_MeshOp.getLength(he);
						if (d > dmax) {
							degeneratehe = he;
							dmax = d;
						}
						he = he.getNextInFace();
					} while (he != face.getHalfedge());
					// System.out.println("Deleting longest edge: " + he);
					mesh.deleteEdge(degeneratehe);
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void clean(final HE_Mesh mesh) {
		mesh.modify(new HEM_Clean());
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void fixLoops(final HE_Mesh mesh) {
		for (final HE_Halfedge he : mesh.getHalfedges()) {
			if (he.getPrevInFace() == null) {
				HE_Halfedge hen = he.getNextInFace();
				while (hen.getNextInFace() != he) {
					hen = hen.getNextInFace();
				}
				mesh.setNext(hen, he);
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void findTJunctions(final HE_Mesh mesh) {
		final HE_HalfedgeList bhes = mesh.getAllBoundaryHalfedges();
		HE_Vertex v;
		HE_Halfedge heprev;
		final HE_Selection sel = new HE_Selection(mesh);
		double ds2, de2, dse2;
		for (final HE_Halfedge current : bhes) {
			heprev = current.getPrevInFace();
			v = current.getStartVertex();
			for (final HE_Halfedge check : bhes) {
				if (check != current && check != heprev) {
					dse2 = WB_GeometryOp.getDistanceToSegment3D(v, check.getStartVertex(), check.getEndVertex());
					if (WB_Epsilon.isZeroSq(dse2)) {
						ds2 = WB_GeometryOp.getSqDistance3D(v, check.getStartVertex());
						de2 = WB_GeometryOp.getSqDistance3D(v, check.getEndVertex());
						if (!WB_Epsilon.isZeroSq(ds2) && !WB_Epsilon.isZeroSq(de2)) {
							sel.add(v);
							//
							break;
						}
					}
				}
			}
		}
		mesh.addSelection("tjunctions", sel);
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void fixTJunctions(final HE_Mesh mesh) {
		final HE_HalfedgeList bhes = mesh.getAllBoundaryHalfedges();
		HE_Vertex v;
		HE_Halfedge heprev;
		final HE_Selection sel = new HE_Selection(mesh);
		double ds2, de2, dse2;
		for (final HE_Halfedge current : bhes) {
			heprev = current.getPrevInFace();
			v = current.getStartVertex();
			for (final HE_Halfedge check : bhes) {
				if (check != current && check != heprev) {
					dse2 = WB_GeometryOp.getDistanceToSegment3D(v, check.getStartVertex(), check.getEndVertex());
					if (WB_Epsilon.isZeroSq(dse2)) {
						ds2 = WB_GeometryOp.getSqDistance3D(v, check.getStartVertex());
						de2 = WB_GeometryOp.getSqDistance3D(v, check.getEndVertex());
						if (!WB_Epsilon.isZeroSq(ds2) && !WB_Epsilon.isZeroSq(de2)) {
							sel.add(v);
							HE_MeshOp.splitEdge(mesh, check, v);
							break;
						}
					}
				}
			}
		}
		mesh.addSelection("tjunctions", sel);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	private static boolean deleteDanglingEdgesOnePass(final HE_Mesh mesh) {
		HE_HalfedgeIterator heItr = mesh.heItr();
		final HE_Selection sel = mesh.getNewSelection("dangling");
		HE_Halfedge he;
		boolean found = false;
		while (heItr.hasNext()) {
			he = heItr.next();
			mesh.setHalfedge(he.getVertex(), he);
			if (he.getFace() != null && he.getPair() != null && he.getPair().getFace() != null
					&& he.getFace() == he.getPair().getFace() && he.getVertex() == he.getNextInFace().getEndVertex()) {
				sel.add(he);
				found = true;
			}
		}
		heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			mesh.setNext(he.getPrevInFace(), he.getNextInFace().getNextInFace());
			mesh.setHalfedge(he.getFace(), he.getPrevInFace());
			mesh.remove(he.getNextInFace().getVertex());
			mesh.remove(he);
			mesh.remove(he.getNextInFace());
		}
		return found;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void deleteDanglingEdges(final HE_Mesh mesh) {
		boolean found = false;
		do {
			found = deleteDanglingEdgesOnePass(mesh);
		} while (found);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static int findSubmeshes(final HE_Mesh mesh) {
		mesh.clearVisitedElements();
		HE_Face start = mesh.getFaceWithIndex(0);
		int lastfound = 0;
		HE_Selection submesh;
		int id = 0;
		do {
			// find next unvisited face
			for (int i = lastfound; i < mesh.getNumberOfFaces(); i++) {
				start = mesh.getFaceWithIndex(i);
				lastfound = i;
				if (!start.isVisited()) {// found
					break;
				}
			}
			// reached last face, was already visited
			if (start.isVisited()) {
				break;
			}
			start.setVisited();// visited
			submesh = HE_Selection.getSelection(mesh);
			submesh.add(start);
			// find all unvisited faces connected to face
			HE_RAS<HE_Face> facesToProcess = new HE_RAS<>();
			HE_RAS<HE_Face> newFacesToProcess;
			facesToProcess.add(start);
			HE_FaceList neighbors;
			do {
				newFacesToProcess = new HE_RAS<>();
				for (final HE_Face f : facesToProcess) {
					neighbors = f.getNeighborFaces();
					for (final HE_Face neighbor : neighbors) {
						if (!neighbor.isVisited()) {
							neighbor.setVisited();// visited
							submesh.add(neighbor);
							newFacesToProcess.add(neighbor);
						}
					}
				}
				facesToProcess = newFacesToProcess;
			} while (facesToProcess.size() > 0);
			mesh.addSelection("submesh" + (id++), submesh);
		} while (true);
		return id;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void unifyNormals(final HE_Mesh mesh) {
		final int num = findSubmeshes(mesh);
		for (int i = 0; i < num; i++) {
			final HE_Selection sel = num == 1 ? mesh.selectAllFaces() : mesh.getSelection("submesh" + i);
			HE_FaceIterator fItr = sel.fItr();
			final WB_Point c = new WB_Point();
			while (fItr.hasNext()) {
				c.addSelf(mesh.getFaceCenter(fItr.next()));
			}
			c.divSelf(sel.getNumberOfFaces());
			fItr = sel.fItr();
			HE_Face f;
			WB_Coord fn, fo;
			int plus = 0, minus = 0;
			while (fItr.hasNext()) {
				f = fItr.next();
				fn = mesh.getFaceNormal(f);
				fo = new WB_Vector(c, mesh.getFaceCenter(f));
				if (WB_Vector.dot(fn, fo) >= 0.0) {
					plus++;
				} else {
					minus++;
				}
			}
			if (plus < minus) {
				HE_MeshOp.flipFaces(sel);
			}
		}
		mesh.clearPrecomputed();
	}
}