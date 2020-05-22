package wblut.hemesh;

import wblut.geom.WB_Point;
import wblut.math.WB_MTRandom;

/**
 *
 */
public class HES_Planar extends HES_Subdividor {
	/**  */
	private boolean random;
	/**  */
	private boolean keepTriangles;
	/**  */
	private double range;
	/**  */
	private final WB_MTRandom randomGen;

	/**
	 *
	 */
	public HES_Planar() {
		super();
		random = false;
		range = 1;
		keepTriangles = true;
		randomGen = new WB_MTRandom();
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HES_Planar setRandom(final boolean b) {
		random = b;
		return this;
	}

	/**
	 *
	 *
	 * @param seed
	 * @return
	 */
	public HES_Planar setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HES_Planar setKeepTriangles(final boolean b) {
		keepTriangles = b;
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HES_Planar setRange(final double r) {
		range = r;
		if (range > 1) {
			range = 1;
		}
		if (range < 0) {
			range = 0;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		randomGen.reset();
		final HE_ObjectMap<HE_Vertex> faceVertices = new HE_ObjectMap<>();
		final HE_ObjectMap<HE_TextureCoordinate> textureCoordinates = new HE_ObjectMap<>();
		HE_Face face;
		HE_FaceIterator fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			if (!random) {
				final HE_Vertex fv = new HE_Vertex(mesh.getFaceCenter(face));
				double u = 0;
				double v = 0;
				double w = 0;
				HE_Halfedge he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.hasUVW()) {
						hasTexture = false;
						break;
					}
					u += he.getUVW().ud();
					v += he.getUVW().vd();
					w += he.getUVW().wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				final double ifo = 1.0 / face.getFaceDegree();
				u *= ifo;
				v *= ifo;
				w *= ifo;
				faceVertices.put(face.getKey(), fv);
				if (hasTexture) {
					textureCoordinates.put(fv.getKey(), new HE_TextureCoordinate(u, v, w));
				}
			} else {
				HE_Halfedge he = face.getHalfedge();
				HE_Vertex fv = new HE_Vertex();
				int trial = 0;
				do {
					double c = 0;
					fv = new HE_Vertex();
					do {
						final WB_Point tmp = new WB_Point(he.getVertex());
						final double t = 0.5 + (randomGen.nextDouble() - 0.5) * range;
						tmp.mulSelf(t);
						fv.getPosition().addSelf(tmp);
						c += t;
						he = he.getNextInFace();
					} while (he != face.getHalfedge());
					fv.getPosition().divSelf(c);
					trial++;
				} while (!HE_MeshOp.pointIsStrictlyInFace(fv, face) && trial < 10);
				if (trial == 10) {
					fv.set(mesh.getFaceCenter(face));
				}
				double u = 0;
				double v = 0;
				double w = 0;
				he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.hasUVW()) {
						hasTexture = false;
						break;
					}
					u += he.getUVW().ud();
					v += he.getUVW().vd();
					w += he.getUVW().wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				final double ifo = 1.0 / face.getFaceDegree();
				u *= ifo;
				v *= ifo;
				w *= ifo;
				faceVertices.put(face.getKey(), fv);
				if (hasTexture) {
					textureCoordinates.put(fv.getKey(), new HE_TextureCoordinate(u, v, w));
				}
			}
		}
		final int n = mesh.getNumberOfEdges();
		final HE_Selection orig = HE_Selection.getSelection(mesh);
		orig.addVertices(mesh);
		if (random) {
			final HE_Halfedge[] origE = mesh.getEdgesAsArray();
			for (int i = 0; i < n; i++) {
				final double f = 0.5 + (randomGen.nextDouble() - 0.5) * range;
				HE_MeshOp.splitEdge(mesh, origE[i], f);
			}
		} else {
			HE_MeshOp.splitEdges(mesh);
		}
		final HE_FaceList newFaces = new HE_FaceList();
		fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			// loop
			HE_Halfedge startHE = face.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			if (face.getFaceDegree() == 6 && keepTriangles) {
				HE_Halfedge origHE1 = startHE;
				final HE_Face centerFace = new HE_Face();
				newFaces.add(centerFace);
				mesh.addDerivedElement(centerFace, face);
				centerFace.copyProperties(face);
				final HE_HalfedgeList faceHalfedges = new HE_HalfedgeList();
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					mesh.addDerivedElement(newFace, face);
					newFace.copyProperties(face);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE = new HE_Halfedge();
					final HE_Halfedge newHEp = new HE_Halfedge();
					faceHalfedges.add(newHEp);
					if (origHE3.hasUVW()) {
						newHE.setUVW(origHE3.getUVW());
					}
					mesh.setHalfedge(newFace, origHE1);
					mesh.setNext(origHE2, newHE);
					mesh.setNext(newHE, origHE1);
					mesh.setVertex(newHE, origHE3.getVertex());
					mesh.setFace(newHE, newFace);
					mesh.setFace(origHE1, newFace);
					mesh.setFace(origHE2, newFace);
					mesh.setVertex(newHEp, origHE1.getVertex());
					mesh.setPair(newHE, newHEp);
					mesh.setFace(newHEp, centerFace);
					mesh.setHalfedge(centerFace, newHEp);
					mesh.add(newHE);
					mesh.add(newHEp);
					origHE1 = origHE3;
				} while (origHE1 != startHE);
				HE_MeshOp.cycleHalfedges(mesh, faceHalfedges);
				HE_Halfedge cfhe = centerFace.getHalfedge();
				do {
					if (cfhe.getPair().getNextInFace().hasUVW()) {
						cfhe.setUVW(cfhe.getPair().getNextInFace().getUVW());
					}
					cfhe = cfhe.getNextInFace();
				} while (cfhe != centerFace.getHalfedge());
			} else {
				HE_Halfedge origHE1 = startHE;
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					mesh.addDerivedElement(newFace, face);
					newFace.copyProperties(face);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE1 = new HE_Halfedge();
					final HE_Halfedge newHE2 = new HE_Halfedge();
					if (origHE3.hasUVW()) {
						newHE1.setUVW(origHE3.getUVW());
					}
					mesh.setHalfedge(newFace, origHE1);
					mesh.setNext(origHE2, newHE1);
					mesh.setNext(newHE1, newHE2);
					mesh.setNext(newHE2, origHE1);
					mesh.setVertex(newHE1, origHE3.getVertex());
					final HE_Vertex fv = faceVertices.get(origHE1.getFace().getKey());
					mesh.setVertex(newHE2, fv);
					if (fv.getHalfedge() == null) {
						mesh.setHalfedge(fv, newHE2);
					}
					final HE_TextureCoordinate uvw = textureCoordinates.get(fv.getKey());
					if (uvw != null) {
						newHE2.setUVW(uvw);
					}
					if (!mesh.contains(fv)) {
						mesh.add(fv);
					}
					mesh.setFace(newHE1, newFace);
					mesh.setFace(newHE2, newFace);
					mesh.setFace(origHE1, newFace);
					mesh.setFace(origHE2, newFace);
					origHE1 = origHE3;
					mesh.add(newHE1);
					mesh.add(newHE2);
				} while (origHE1 != startHE);
				HE_MeshOp.pairHalfedges(mesh);
			}
			face.setInternalLabel(0);
		} // end of face loop
		final HE_FaceList faces = mesh.getFaces();
		for (final HE_Face f : faces) {
			if (!newFaces.contains(f)) {
				mesh.remove(f);
			}
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HE_Mesh mesh = selection.getParent();
		randomGen.reset();
		selection.cleanSelection();
		if (selection.getNumberOfFaces() == 0) {
			return mesh;
		}
		final HE_ObjectMap<HE_Vertex> faceVertices = new HE_ObjectMap<>();
		final HE_ObjectMap<HE_TextureCoordinate> textureCoordinates = new HE_ObjectMap<>();
		HE_Face face;
		HE_FaceIterator fItr = selection.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			if (!random) {
				final HE_Vertex fv = new HE_Vertex(mesh.getFaceCenter(face));
				double u = 0;
				double v = 0;
				double w = 0;
				HE_Halfedge he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.hasUVW()) {
						hasTexture = false;
						break;
					}
					u += he.getUVW().ud();
					v += he.getUVW().vd();
					w += he.getUVW().wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				final double ifo = 1.0 / face.getFaceDegree();
				u *= ifo;
				v *= ifo;
				w *= ifo;
				faceVertices.put(face.getKey(), fv);
				if (hasTexture) {
					textureCoordinates.put(fv.getKey(), new HE_TextureCoordinate(u, v, w));
				}
			} else {
				HE_Halfedge he = face.getHalfedge();
				HE_Vertex fv = new HE_Vertex();
				int trial = 0;
				do {
					double c = 0;
					fv = new HE_Vertex();
					do {
						final WB_Point tmp = new WB_Point(he.getVertex());
						final double t = 0.5 + (randomGen.nextDouble() - 0.5) * range;
						tmp.mulSelf(t);
						fv.getPosition().addSelf(tmp);
						c += t;
						he = he.getNextInFace();
					} while (he != face.getHalfedge());
					fv.getPosition().divSelf(c);
					trial++;
				} while (!HE_MeshOp.pointIsStrictlyInFace(fv, face) && trial < 10);
				if (trial == 10) {
					fv.set(mesh.getFaceCenter(face));
				}
				double u = 0;
				double v = 0;
				double w = 0;
				he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.hasUVW()) {
						hasTexture = false;
						break;
					}
					u += he.getUVW().ud();
					v += he.getUVW().vd();
					w += he.getUVW().wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				final double ifo = 1.0 / face.getFaceDegree();
				u *= ifo;
				v *= ifo;
				w *= ifo;
				faceVertices.put(face.getKey(), fv);
				if (hasTexture) {
					textureCoordinates.put(fv.getKey(), new HE_TextureCoordinate(u, v, w));
				}
			}
		}
		selection.collectEdgesByFace();
		final HE_Selection newVertices = HE_Selection.getSelection(mesh);
		if (random) {
			final HE_Halfedge[] edges = selection.getEdgesAsArray();
			final int ne = selection.getNumberOfEdges();
			for (int i = 0; i < ne; i++) {
				HE_Vertex v;
				final double f = 0.5 + (randomGen.nextDouble() - 0.5) * range;
				v = HE_MeshOp.splitEdge(mesh, edges[i], f).vItr().next();
				if (v != null) {
					newVertices.add(v);
				}
			}
		} else {
			newVertices.add(HE_MeshOp.splitEdges(selection));
		}
		final HE_FaceList newFaces = new HE_FaceList();
		fItr = selection.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			HE_Halfedge startHE = face.getHalfedge();
			while (!newVertices.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			if (face.getFaceDegree() == 6 && keepTriangles) {
				HE_Halfedge origHE1 = startHE;
				final HE_Face centerFace = new HE_Face();
				centerFace.copyProperties(face);
				mesh.addDerivedElement(centerFace, face);
				newFaces.add(centerFace);
				final HE_HalfedgeList faceHalfedges = new HE_HalfedgeList();
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					mesh.addDerivedElement(newFace, face);
					newFace.copyProperties(face);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE = new HE_Halfedge();
					final HE_Halfedge newHEp = new HE_Halfedge();
					faceHalfedges.add(newHEp);
					if (origHE3.hasUVW()) {
						newHE.setUVW(origHE3.getUVW());
					}
					mesh.setHalfedge(newFace, origHE1);
					mesh.setNext(origHE2, newHE);
					mesh.setNext(newHE, origHE1);
					mesh.setVertex(newHE, origHE3.getVertex());
					mesh.setFace(newHE, newFace);
					mesh.setFace(origHE1, newFace);
					mesh.setFace(origHE2, newFace);
					mesh.setVertex(newHEp, origHE1.getVertex());
					mesh.setPair(newHE, newHEp);
					mesh.setFace(newHEp, centerFace);
					mesh.setHalfedge(centerFace, newHEp);
					mesh.add(newHE);
					mesh.add(newHEp);
					origHE1 = origHE3;
				} while (origHE1 != startHE);
				HE_MeshOp.cycleHalfedges(mesh, faceHalfedges);
				HE_Halfedge cfhe = centerFace.getHalfedge();
				do {
					if (cfhe.getPair().getNextInFace().hasUVW()) {
						cfhe.setUVW(cfhe.getPair().getNextInFace().getUVW());
					}
					cfhe = cfhe.getNextInFace();
				} while (cfhe != centerFace.getHalfedge());
			} else {
				HE_Halfedge origHE1 = startHE;
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					mesh.addDerivedElement(newFace, face);
					newFace.copyProperties(face);
					mesh.setHalfedge(newFace, origHE1);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE1 = new HE_Halfedge();
					final HE_Halfedge newHE2 = new HE_Halfedge();
					if (origHE3.hasUVW()) {
						newHE1.setUVW(origHE3.getUVW());
					}
					mesh.setNext(origHE2, newHE1);
					mesh.setNext(newHE1, newHE2);
					mesh.setNext(newHE2, origHE1);
					mesh.setVertex(newHE1, origHE3.getVertex());
					final HE_Vertex fv = faceVertices.get(origHE1.getFace().getKey());
					mesh.setVertex(newHE2, fv);
					if (fv.getHalfedge() == null) {
						mesh.setHalfedge(fv, newHE2);
					}
					final HE_TextureCoordinate uvw = textureCoordinates.get(fv.getKey());
					if (uvw != null) {
						newHE2.setUVW(uvw);
					}
					if (!mesh.contains(fv)) {
						mesh.add(fv);
					}
					mesh.setFace(newHE1, newFace);
					mesh.setFace(newHE2, newFace);
					mesh.setFace(origHE1, newFace);
					mesh.setFace(origHE2, newFace);
					mesh.add(newHE1);
					mesh.add(newHE2);
					origHE1 = origHE3;
				} while (origHE1 != startHE);
				HE_MeshOp.pairHalfedges(mesh);
			}
		} // end of face loop
		HE_MeshOp.pairHalfedges(mesh);
		for (final HE_Face f : selection.getFaces()) {
			if (!newFaces.contains(f)) {
				mesh.remove(f);
			}
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final HEC_Cube creator = new HEC_Cube(400, 1, 1, 1);
		final HE_Mesh mesh = new HE_Mesh(creator);
		mesh.modify(new HEM_ChamferCorners().setDistance(70));
		final HES_Planar subdividor = new HES_Planar();
		subdividor.setRandom(true);// Randomize center edge and center face
									// points
		subdividor.setRange(0.3);// Random range of center offset, from 0 (no
									// random) to 1(fully random)
		subdividor.setSeed(1234L);// Seed of random point generator
		subdividor.setKeepTriangles(true);// Subdivide triangles into 4
											// triangles instead of 3 quads
		mesh.subdivide(subdividor);
		mesh.validate();
		mesh.selectRandomFaces(0.5).subdivide(subdividor);
	}
}
