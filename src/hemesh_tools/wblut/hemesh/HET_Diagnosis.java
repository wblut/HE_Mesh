package wblut.hemesh;

import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HET_Diagnosis {
	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static boolean isValidMesh(final HE_Mesh mesh) {
		return validate(mesh, false, false, false);
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void stats(final HE_Mesh mesh) {
		System.out.println("   Faces: " + mesh.getNumberOfFaces());
		System.out.println("   Vertices: " + mesh.getNumberOfVertices());
		System.out.println("   Edges: " + mesh.getNumberOfEdges());
		System.out.println("   Halfedges: " + mesh.getNumberOfHalfedges());
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static boolean isValidSurface(final HE_Mesh mesh) {
		return validate(mesh, false, false, true);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static boolean validate(final HE_Mesh mesh) {
		System.out.println("Checking mesh, key=" + mesh.getKey());
		stats(mesh);
		return validate(mesh, true, true, false);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static boolean validateSurface(final HE_Mesh mesh) {
		System.out.println("Checking mesh, key=" + mesh.getKey());
		stats(mesh);
		return validate(mesh, true, true, true);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param verbose
	 * @param force
	 * @param allowSurface
	 * @return
	 */
	public static boolean validate(final HE_Mesh mesh, final boolean verbose, final boolean force,
			final boolean allowSurface) {
		boolean result = true;
		if (verbose == true) {
			System.out.println("Checking face (" + mesh.getNumberOfFaces() + ") properties");
		}
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face face;
		while (fItr.hasNext()) {
			face = fItr.next();
			if (face.getHalfedge() == null) {
				if (verbose == true) {
					System.out.println("Null reference in face " + face.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(face.getHalfedge())) {
					if (verbose == true) {
						System.out.println("External reference in face " + face.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				} else {
					if (face.getHalfedge().getFace() != null) {
						if (face.getHalfedge().getFace() != face) {
							if (verbose == true) {
								System.out.println("Wrong reference in face " + face.getKey() + ".");
							}
							if (force == true) {
								result = false;
							} else {
								return false;
							}
						}
					}
				}
			}
		}
		if (verbose == true) {
			System.out.println("Checking vertex (" + mesh.getNumberOfVertices() + ") properties");
		}
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (Double.isNaN(v.xd()) || Double.isNaN(v.yd()) || Double.isNaN(v.zd())) {
				if (verbose == true) {
					System.out.println("NaN position in vertex  " + v.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			}
			if (v.getHalfedge() == null) {
				if (verbose == true) {
					System.out.println("Null reference in vertex  " + v.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(v.getHalfedge())) {
					if (verbose == true) {
						System.out.println("External reference in vertex  " + v.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (v.getHalfedge().getVertex() != null) {
					if (v.getHalfedge().getVertex() != v) {
						if (verbose == true) {
							System.out.println("Wrong reference in vertex  " + v.getKey() + ".");
						}
						if (force == true) {
							result = false;
						} else {
							return false;
						}
					}
				}
			}
		}
		if (verbose == true) {
			System.out.println("Checking half edge (" + mesh.getNumberOfHalfedges() + ") properties");
		}
		HE_Halfedge he;
		final HE_HalfedgeIterator heItr = mesh.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getNextInFace() == null) {
				if (verbose == true) {
					System.out.println("Null reference (next) in half edge  " + he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getNextInFace())) {
					if (verbose == true) {
						System.out.println("External reference (next) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (he.getFace() != null && he.getNextInFace().getFace() != null) {
					if (he.getFace() != he.getNextInFace().getFace()) {
						if (verbose == true) {
							System.out.println("Inconsistent reference (face) in half edge  " + he.getKey() + ".");
						}
						if (force == true) {
							result = false;
						} else {
							return false;
						}
					}
				}
			}
			if (he.getPrevInFace() == null) {
				if (verbose == true) {
					System.out.println("Null reference (prev) in half edge  " + he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getPrevInFace())) {
					if (verbose == true) {
						System.out.println("External reference (prev) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (he.getFace() != null && he.getPrevInFace().getFace() != null) {
					if (he.getFace() != he.getPrevInFace().getFace()) {
						if (verbose == true) {
							System.out.println("Inconsistent reference (face) in half edge  " + he.getKey() + ".");
						}
						if (force == true) {
							result = false;
						} else {
							return false;
						}
					}
				}
				if (he.getPrevInFace().getNextInFace() != he) {
					if (verbose == true) {
						System.out.println("Unmatched (next)/(prev) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
			}
			if (he.getPair() == null) {
				if (verbose == true) {
					System.out.println("Null reference (pair) in half edge  " + he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getPair())) {
					if (verbose == true) {
						System.out.println("External reference (pair) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (he.getPair().getPair() == null) {
					if (verbose == true) {
						System.out.println("No pair reference back to half edge  " + he.getKey() + ".");
					}
				} else {
					if (he.getPair().getPair() != he) {
						if (verbose == true) {
							System.out.println("Wrong pair reference back to half edge  " + he.getKey() + ".");
						}
						if (force == true) {
							result = false;
						} else {
							return false;
						}
					}
				}
			}
			if (he.getNextInFace() != null && he.getPair() != null) {
				if (he.getNextInFace().getVertex() != null && he.getPair().getVertex() != null) {
					if (he.getNextInFace().getVertex() != he.getPair().getVertex()) {
						if (verbose == true) {
							System.out
									.println("Inconsistent reference (pair)/(next) in half edge  " + he.getKey() + ".");
						}
						if (force == true) {
							result = false;
						} else {
							return false;
						}
					}
				}
			}
			if (he.getFace() == null) {
				if (!allowSurface) {
					if (verbose == true) {
						System.out.println("Null reference (face) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
			} else {
				if (!mesh.contains(he.getFace())) {
					if (verbose == true) {
						System.out.println("External reference (face) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
			}
			if (he.getVertex() == null) {
				if (verbose == true) {
					System.out.println("Null reference (vert) in half edge  " + he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getVertex())) {
					if (verbose == true) {
						System.out.println("External reference (vert) in half edge  " + he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
			}
		}
		if (verbose == true) {
			System.out.println("Validation complete!");
		}
		return result;
	}

	/**
	 *
	 *
	 * @param v
	 * @param mesh
	 */
	public static void checkVertex(final HE_Vertex v, final HE_Mesh mesh) {
		System.out.println("Checking vertex " + v.getKey());
		if (!mesh.contains(v)) {
			System.out.println("   Vertex not in mesh!");
		}
		if (v.getHalfedge() == null) {
			System.out.println("   Null reference (halfedge) in vertex!");
		} else {
			System.out.println("   HE_Halfedge " + v.getHalfedge().getKey());
			if (v.getHalfedge().getVertex() != v) {
				System.out.println("   Inconsistent reference (halfedge) in vertex!");
			}
			try {
				v.getFaceStar();
			} catch (final Exception e) {
				System.out.println("   Can't retrieve star of vertex!");
			}
			try {
				final WB_Coord n = mesh.getVertexNormal(v);
				if (WB_Vector.getLength3D(n) < 0.5) {
					System.out.println("   Degenerate normal vector!");
				} else {
					System.out.println("   Normal: " + n);
				}
			} catch (final Exception e) {
				System.out.println("   Can't retrieve normal of vertex!");
			}
			try {
				System.out.println("   Order: " + v.getVertexDegree());
				System.out.println("   Area: " + HE_MeshOp.getVertexArea(v));
				System.out.println("   Umbrella angle: " + HE_MeshOp.getUmbrellaAngle(v) / (2 * Math.PI));
			} catch (final Exception e) {
				System.out.println("   Can't calculate properties of vertex!");
			}
		}
		System.out.println();
	}

	/**
	 *
	 *
	 * @param f
	 * @param mesh
	 */
	public static void checkFace(final HE_Face f, final HE_Mesh mesh) {
		System.out.println("Checking face " + f.getKey());
		if (!mesh.contains(f)) {
			System.out.println("   Face not in mesh!");
		}
		if (f.getHalfedge() == null) {
			System.out.println("   Null reference (halfedge) in face!");
		} else {
			System.out.println("   HE_Halfedge " + f.getHalfedge().getKey());
			if (f.getHalfedge().getFace() != f) {
				System.out.println("   Inconsistent reference (halfedge) in face!");
			}
			try {
				f.getFaceVertices();
			} catch (final Exception e) {
				System.out.println("   Can't retrieve vertices of face!");
			}
			try {
				f.getNeighborFaces();
			} catch (final Exception e) {
				System.out.println("   Can't retrieve neighbors of face!");
			}
			try {
				f.getTriangles();
			} catch (final Exception e) {
				System.out.println("   Can't triangulate face!");
			}
			try {
				final WB_Coord n = mesh.getFaceNormal(f);
				if (WB_Vector.getLength3D(n) < 0.5) {
					System.out.println("   Degenerate face vector!");
				} else {
					System.out.println("   Normal: " + n);
				}
			} catch (final Exception e) {
				System.out.println("   Can't retrieve normal of face!");
			}
			try {
				System.out.println("   Order: " + f.getFaceDegree());
				System.out.println("   Area: " + HE_MeshOp.getFaceArea(f));
			} catch (final Exception e) {
				System.out.println("   Can't calculate properties of face!");
			}
		}
		System.out.println();
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public static void checkHalfedges(final HE_HalfedgeStructure mesh) {
		int i = 0;
		for (final HE_Halfedge he : mesh.getHalfedges()) {
			if (!mesh.contains(he.getVertex())) {
				i++;
			}
		}
		System.out.println("Halfedges with external reference (vert): " + i);
	}

	// detect edges with 0 or more than 2 faces
	public static void checkNonManifoldEdges(final HE_Mesh mesh) {
		final HE_IntMap edges = new HE_IntMap();
		final HE_FaceIterator fItr = mesh.fItr();
		HE_FaceHalfedgeInnerCirculator fheiCrc;
		HE_Halfedge he;
		long key;
		int count;
		while (fItr.hasNext()) {
			fheiCrc = fItr.next().fheiCrc();
			while (fheiCrc.hasNext()) {
				he = fheiCrc.next();
				key = key(he.getStartVertex(), he.getEndVertex());
				count = edges.getIfAbsent(key, -1);
				if (count == -1) {
					edges.put(key, 1);
				} else {
					edges.put(key, count + 1);
				}
			}
		}
		final HE_Selection sel = mesh.getNewSelection("NonManifoldEdges");
		final HE_HalfedgeIterator heItr = mesh.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			key = key(he.getStartVertex(), he.getEndVertex());
			count = edges.getIfAbsent(key, -1);
			if (count < 1 || count > 2) {
				sel.add(he);
			}
			System.out.println("Faces associated with halfedge " + he.getKey() + ": " + count + ".");
		}
	}

	private static long key(final HE_Vertex v1, final HE_Vertex v2) {
		final long a = Math.max(v1.getKey(), v2.getKey());
		final long b = Math.min(v1.getKey(), v2.getKey());
		return a * a + a + b;
	}

	// detect isolated vertices or vertices shared by more than 1 face-fan
	public static void checkNonManifoldVertices(final HE_Mesh mesh) {
		final HE_Selection sel = mesh.getNewSelection("NonManifoldVertices");
		HE_Vertex v;
		final HE_ObjectMap<HE_FaceSet> facesPerVertex = new HE_ObjectMap<>();
		HE_FaceSet faces;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_FaceHalfedgeInnerCirculator fheiCrc;
		HE_Halfedge he;
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			fheiCrc = f.fheiCrc();
			while (fheiCrc.hasNext()) {
				he = fheiCrc.next();
				v = he.getVertex();
				faces = facesPerVertex.get(v);
				if (faces == null) {
					faces = new HE_FaceSet();
					faces.add(f);
					facesPerVertex.put(v, faces);
				} else {
					faces.add(f);
				}
			}
		}
		final HE_VertexIterator vItr = mesh.vItr();
		int fans = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			faces = facesPerVertex.get(v);
			if (faces == null) {
				sel.add(v);
			} else {
				fans = countFaceFans(faces, mesh.getSelection("NonManifoldEdges"));
				if (fans > 1) {
					sel.add(v);
				}
			}
			System.out.println("Fans associated with vertex " + v.getKey() + ": " + (faces == null ? 0 : fans) + ".");
		}
	}

	static int countFaceFans(final HE_FaceSet faces, final HE_Selection nonManifoldEdges) {
		final List<HE_Face> fs = faces.toList();
		int fanCount = 0;
		HE_FaceList fan = new HE_FaceList();
		do {
			fan = getFirstFan(fs, nonManifoldEdges);
			fanCount++;
		} while (fs.size() > 0);
		return fanCount;
	}

	static HE_FaceList getFirstFan(final List<HE_Face> faces, final HE_Selection nonManifoldEdges) {
		final HE_FaceList fan = new HE_FaceList();
		HE_Face f = faces.get(0);
		fan.add(f);
		faces.remove(0);
		int facesInFan = 1;
		int prevFacesInFan;
		do {
			prevFacesInFan = facesInFan;
			for (int i = 0; i < faces.size(); i++) {
				if (isNeighbor(f, faces.get(i), nonManifoldEdges)) {
					f = faces.get(i);
					fan.add(f);
					faces.remove(i);
					break;
				}
			}
			facesInFan = fan.size();
		} while (facesInFan != prevFacesInFan);
		return fan;
	}

	static boolean isNeighbor(final HE_Face f1, final HE_Face f2, final HE_Selection nonManifoldEdges) {
		final HE_FaceHalfedgeInnerCirculator fheiCrc1 = f1.fheiCrc();
		HE_FaceHalfedgeInnerCirculator fheiCrc2;
		HE_Halfedge he1, he2;
		while (fheiCrc1.hasNext()) {
			he1 = fheiCrc1.next();
			if (nonManifoldEdges.contains(he1)) {
				continue;
			}
			fheiCrc2 = f2.fheiCrc();
			while (fheiCrc2.hasNext()) {
				he2 = fheiCrc2.next();
				if (nonManifoldEdges.contains(he2)) {
					continue;
				}
				if ((he1.getStartVertex() == he2.getEndVertex() && he2.getStartVertex() == he1.getEndVertex())
						|| (he1.getStartVertex() == he2.getStartVertex() && he2.getEndVertex() == he1.getEndVertex())) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(final String[] args) {
		final HE_Mesh mesh = new HEC_StellatedIcosahedron(15, 200.0).create();
		HET_Diagnosis.checkNonManifoldEdges(mesh);
		HET_Diagnosis.checkNonManifoldVertices(mesh);
		HET_Diagnosis.findSubmeshes(mesh);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_MeshCollection findSubmeshes(final HE_Mesh mesh) {
		mesh.clearVisitedElements();
		final HE_Selection sel = mesh.getSelection("NonManifoldEdges");
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
			submesh = mesh.getNewSelection("submesh" + (id++));
			submesh.add(start);
			// find all unvisited faces connected to face
			HE_RAS<HE_Face> facesToProcess = new HE_RAS<>();
			HE_RAS<HE_Face> newFacesToProcess;
			facesToProcess.add(start);
			final HE_FaceList facesToCheck = mesh.getFaces();
			do {
				newFacesToProcess = new HE_RAS<>();
				for (final HE_Face f : facesToProcess) {
					for (final HE_Face check : facesToCheck) {
						if (isNeighbor(f, check, sel) && !check.isVisited()) {
							check.setVisited();// visited
							submesh.add(check);
							newFacesToProcess.add(check);
						}
					}
				}
				facesToProcess = newFacesToProcess;
			} while (facesToProcess.size() > 0);
		} while (true);
		final HE_MeshCollection submeshes = new HE_MeshCollection();
		for (int i = 0; i < id; i++) {
			final HE_Mesh sm = new HEC_FromFaces().setFaces(mesh.getSelection("submesh" + i)).create();
			HET_Fixer.fixNonManifoldVertices(sm);
			submeshes.add(sm);
		}
		return submeshes;
	}
}
