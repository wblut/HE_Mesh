/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Vector;

/**
 * The Class HET_Diagnosis.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class HET_Diagnosis {
	/**
	 * Check consistency of datastructure of closed mesh.
	 *
	 * @param mesh
	 *            the mesh
	 * @return true or false
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
		System.out.println("Faces: " + mesh.getNumberOfFaces());
		System.out.println("Vertices: " + mesh.getNumberOfVertices());
		System.out.println("Edges: " + mesh.getNumberOfEdges());
		System.out.println("Halfedges: " + mesh.getNumberOfHalfedges());
	}

	/**
	 * Check consistency of datastructure of surface.
	 *
	 * @param mesh
	 *            the mesh
	 * @return true or false
	 */
	public static boolean isValidSurface(final HE_Mesh mesh) {
		return validate(mesh, false, false, true);
	}

	/**
	 * Check consistency of datastructure.
	 *
	 * @param mesh
	 *            the mesh
	 * @return true or false
	 */
	public static boolean validate(final HE_Mesh mesh) {
		return validate(mesh, true, true, false);
	}

	/**
	 * Validate surface.
	 *
	 * @param mesh
	 *            the mesh
	 * @return true, if successful
	 */
	public static boolean validateSurface(final HE_Mesh mesh) {
		return validate(mesh, true, true, true);
	}

	/**
	 * Check consistency of datastructure.
	 *
	 * @param mesh
	 *            the mesh
	 * @param verbose
	 *            true: print to console, HE.SILENT: no output
	 * @param force
	 *            true: full scan, HE.BREAK: stop on first error
	 * @param allowSurface
	 *            the allow surface
	 * @return true or false
	 */
	public static boolean validate(final HE_Mesh mesh, final boolean verbose,
			final boolean force, final boolean allowSurface) {
		boolean result = true;
		if (verbose == true) {
			System.out.println("Checking face (" + mesh.getNumberOfFaces()
					+ ") properties");
		}
		HE_FaceIterator fItr = mesh.fItr();
		HE_Face face;
		while (fItr.hasNext()) {
			face = fItr.next();
			if (face.getHalfedge() == null) {
				if (verbose == true) {
					System.out.println(
							"Null reference in face " + face.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(face.getHalfedge())) {
					if (verbose == true) {
						System.out.println("External reference in face "
								+ face.getKey() + ".");
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
								System.out.println("Wrong reference in face "
										+ face.getKey() + ".");
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
			System.out.println("Checking vertex (" + mesh.getNumberOfVertices()
					+ ") properties");
		}
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if(Double.isNaN(v.xd())||Double.isNaN(v.yd())||Double.isNaN(v.zd()))  {
				if (verbose == true) {
					System.out.println(
							"NaN position in vertex  " + v.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			}
			if (v.getHalfedge() == null) {
				if (verbose == true) {
					System.out.println(
							"Null reference in vertex  " + v.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(v.getHalfedge())) {
					if (verbose == true) {
						System.out.println("External reference in vertex  "
								+ v.getKey() + ".");
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
							System.out.println("Wrong reference in vertex  "
									+ v.getKey() + ".");
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
			System.out.println("Checking half edge ("
					+ mesh.getNumberOfHalfedges() + ") properties");
		}
		HE_Halfedge he;
		HE_HalfedgeIterator heItr = mesh.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getNextInFace() == null) {
				if (verbose == true) {
					System.out.println("Null reference (next) in half edge  "
							+ he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getNextInFace())) {
					if (verbose == true) {
						System.out.println(
								"External reference (next) in half edge  "
										+ he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (he.getFace() != null
						&& he.getNextInFace().getFace() != null) {
					if (he.getFace() != he.getNextInFace().getFace()) {
						if (verbose == true) {
							System.out.println(
									"Inconsistent reference (face) in half edge  "
											+ he.getKey() + ".");
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
					System.out.println("Null reference (prev) in half edge  "
							+ he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getPrevInFace())) {
					if (verbose == true) {
						System.out.println(
								"External reference (prev) in half edge  "
										+ he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (he.getFace() != null
						&& he.getPrevInFace().getFace() != null) {
					if (he.getFace() != he.getPrevInFace().getFace()) {
						if (verbose == true) {
							System.out.println(
									"Inconsistent reference (face) in half edge  "
											+ he.getKey() + ".");
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
						System.out.println(
								"Unmatched (next)/(prev) in half edge  "
										+ he.getKey() + ".");
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
					System.out.println("Null reference (pair) in half edge  "
							+ he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getPair())) {
					if (verbose == true) {
						System.out.println(
								"External reference (pair) in half edge  "
										+ he.getKey() + ".");
					}
					if (force == true) {
						result = false;
					} else {
						return false;
					}
				}
				if (he.getPair().getPair() == null) {
					if (verbose == true) {
						System.out
								.println("No pair reference back to half edge  "
										+ he.getKey() + ".");
					}
				} else {
					if (he.getPair().getPair() != he) {
						if (verbose == true) {
							System.out.println(
									"Wrong pair reference back to half edge  "
											+ he.getKey() + ".");
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
				if (he.getNextInFace().getVertex() != null
						&& he.getPair().getVertex() != null) {
					if (he.getNextInFace().getVertex() != he.getPair()
							.getVertex()) {
						if (verbose == true) {
							System.out.println(
									"Inconsistent reference (pair)/(next) in half edge  "
											+ he.getKey() + ".");
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
						System.out
								.println("Null reference (face) in half edge  "
										+ he.getKey() + ".");
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
						System.out.println(
								"External reference (face) in half edge  "
										+ he.getKey() + ".");
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
					System.out.println("Null reference (vert) in half edge  "
							+ he.getKey() + ".");
				}
				if (force == true) {
					result = false;
				} else {
					return false;
				}
			} else {
				if (!mesh.contains(he.getVertex())) {
					if (verbose == true) {
						System.out.println(
								"External reference (vert) in half edge  "
										+ he.getKey() + ".");
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
				System.out.println(
						"   Inconsistent reference (halfedge) in vertex!");
			}
			try {
				v.getFaceStar();
			} catch (final Exception e) {
				System.out.println("   Can't retrieve star of vertex!");
			}
			try {
				final WB_Coord n = HE_MeshOp.getVertexNormal(v);
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
				System.out.println("   Umbrella angle: "
						+ HE_MeshOp.getUmbrellaAngle(v) / (2 * Math.PI));
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
				System.out.println(
						"   Inconsistent reference (halfedge) in face!");
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
				final WB_Coord n = HE_MeshOp.getFaceNormal(f);
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

	public static void checkHalfedges(final HE_HalfedgeStructure mesh) {
		int i = 0;
		for (HE_Halfedge he : mesh.getHalfedges()) {
			if (!mesh.contains(he.getVertex())) {
				i++;
			}
		}
		System.out.println("Halfedges with external reference (vert): " + i);
	}
}
