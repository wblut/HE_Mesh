/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import wblut.geom.WB_Point;
import wblut.math.WB_MTRandom;

/**
 * Planar subdivision of a mesh. Divides all edges in half. Non-triangular faces
 * are divided in new faces connecting each vertex with the two adjacent mid
 * edge vertices and the face center. Triangular faces are divided in four new
 * triangular faces by connecting the mid edge points. Faces are tris or quads.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HES_Planar extends HES_Subdividor {
	/** Random subdivision. */
	private boolean				random;
	/** Triangular division of triangles?. */
	private boolean				keepTriangles;
	/** Random range. */
	private double				range;
	/** The random gen. */
	private final WB_MTRandom	randomGen;

	/**
	 * Instantiates a new HES_Planar.
	 */
	public HES_Planar() {
		super();
		random = false;
		range = 1;
		keepTriangles = true;
		randomGen = new WB_MTRandom();
	}

	/**
	 * Set random mode.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HES_Planar setRandom(final boolean b) {
		random = b;
		return this;
	}

	/**
	 * Set random seed.
	 *
	 * @param seed
	 *            seed
	 * @return self
	 */
	public HES_Planar setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 * Set preservation of triangular faces.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HES_Planar setKeepTriangles(final boolean b) {
		keepTriangles = b;
		return this;
	}

	/**
	 * Set range of random variation.
	 *
	 * @param r
	 *            range (0..1)
	 * @return self
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

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Subdividor#subdivide(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		randomGen.reset();
		final LongObjectHashMap<HE_Vertex> faceVertices = new LongObjectHashMap<HE_Vertex>();
		HE_Face face;
		Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			if (!random) {
				final HE_Vertex fv = new HE_Vertex(
						HE_MeshOp.getFaceCenter(face));
				double u = 0;
				double v = 0;
				double w = 0;
				HE_Halfedge he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.getVertex().hasUVW(face)) {
						hasTexture = false;
						break;
					}
					u += he.getVertex().getUVW(face).ud();
					v += he.getVertex().getUVW(face).vd();
					w += he.getVertex().getUVW(face).wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				if (hasTexture) {
					final double ifo = 1.0 / face.getFaceDegree();
					fv.setUVW(u * ifo, v * ifo, w * ifo);
				}
				faceVertices.put(face.getKey(), fv);
			} else {
				HE_Halfedge he = face.getHalfedge();
				HE_Vertex fv = new HE_Vertex();
				int trial = 0;
				do {
					double c = 0;
					fv = new HE_Vertex();
					do {
						final WB_Point tmp = new WB_Point(he.getVertex());
						final double t = 0.5
								+ (randomGen.nextDouble() - 0.5) * range;
						tmp.mulSelf(t);
						fv.getPosition().addSelf(tmp);
						c += t;
						he = he.getNextInFace();
					} while (he != face.getHalfedge());
					fv.getPosition().divSelf(c);
					trial++;
				} while (!HE_MeshOp.pointIsStrictlyInFace(fv, face)
						&& trial < 10);
				if (trial == 10) {
					fv.set(HE_MeshOp.getFaceCenter(face));
				}
				double u = 0;
				double v = 0;
				double w = 0;
				he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.getVertex().hasUVW(face)) {
						hasTexture = false;
						break;
					}
					u += he.getVertex().getUVW(face).ud();
					v += he.getVertex().getUVW(face).vd();
					w += he.getVertex().getUVW(face).wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				if (hasTexture) {
					final double ifo = 1.0 / face.getFaceDegree();
					fv.setUVW(u * ifo, v * ifo, w * ifo);
				}
				faceVertices.put(face.getKey(), fv);
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
		final ArrayList<HE_Face> newFaces = new ArrayList<HE_Face>();
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
				centerFace.copyProperties(face);
				final ArrayList<HE_Halfedge> faceHalfedges = new ArrayList<HE_Halfedge>();
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					newFace.copyProperties(face);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE = new HE_Halfedge();
					final HE_Halfedge newHEp = new HE_Halfedge();
					faceHalfedges.add(newHEp);
					if (origHE3.getVertex().hasHalfedgeUVW(face)) {
						newHE.setUVW(origHE3.getVertex().getHalfedgeUVW(face));
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
					if (cfhe.getPair().getNextInFace().hasHalfedgeUVW()) {
						cfhe.setUVW(cfhe.getPair().getNextInFace().getUVW());
					}
					cfhe = cfhe.getNextInFace();
				} while (cfhe != centerFace.getHalfedge());
			} else {
				HE_Halfedge origHE1 = startHE;
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					newFace.copyProperties(face);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE1 = new HE_Halfedge();
					final HE_Halfedge newHE2 = new HE_Halfedge();
					if (origHE3.getVertex().hasHalfedgeUVW(face)) {
						newHE1.setUVW(origHE3.getVertex().getHalfedgeUVW(face));
					}
					mesh.setHalfedge(newFace, origHE1);
					mesh.setNext(origHE2, newHE1);
					mesh.setNext(newHE1, newHE2);
					mesh.setNext(newHE2, origHE1);
					mesh.setVertex(newHE1, origHE3.getVertex());
					final HE_Vertex fv = faceVertices
							.get(origHE1.getFace().getKey());
					mesh.setVertex(newHE2, fv);
					if (fv.getHalfedge() == null) {
						mesh.setHalfedge(fv, newHE2);
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
		List<HE_Face> faces = mesh.getFaces();
		mesh.addFaces(newFaces);
		for (HE_Face f : faces) {
			if (!newFaces.contains(f)) {
				mesh.remove(f);
			}
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.subdividors.HEB_Subdividor#subdivideSelected(wblut.hemesh
	 * .HE_Mesh, wblut.hemesh.HE_Selection)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		randomGen.reset();
		selection.cleanSelection();
		if (selection.getNumberOfFaces() == 0) {
			return selection.getParent();
		}
		final LongObjectHashMap<HE_Vertex> faceVertices = new LongObjectHashMap<HE_Vertex>();
		HE_Face face;
		Iterator<HE_Face> fItr = selection.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			if (!random) {
				final HE_Vertex fv = new HE_Vertex(
						HE_MeshOp.getFaceCenter(face));
				double u = 0;
				double v = 0;
				double w = 0;
				HE_Halfedge he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.getVertex().hasUVW(face)) {
						hasTexture = false;
						break;
					}
					u += he.getVertex().getUVW(face).ud();
					v += he.getVertex().getUVW(face).vd();
					w += he.getVertex().getUVW(face).wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				if (hasTexture) {
					final double ifo = 1.0 / face.getFaceDegree();
					fv.setUVW(u * ifo, v * ifo, w * ifo);
				}
				faceVertices.put(face.getKey(), fv);
			} else {
				HE_Halfedge he = face.getHalfedge();
				HE_Vertex fv = new HE_Vertex();
				int trial = 0;
				do {
					double c = 0;
					fv = new HE_Vertex();
					do {
						final WB_Point tmp = new WB_Point(he.getVertex());
						final double t = 0.5
								+ (randomGen.nextDouble() - 0.5) * range;
						tmp.mulSelf(t);
						fv.getPosition().addSelf(tmp);
						c += t;
						he = he.getNextInFace();
					} while (he != face.getHalfedge());
					fv.getPosition().divSelf(c);
					trial++;
				} while (!HE_MeshOp.pointIsStrictlyInFace(fv, face)
						&& trial < 10);
				if (trial == 10) {
					fv.set(HE_MeshOp.getFaceCenter(face));
				}
				double u = 0;
				double v = 0;
				double w = 0;
				he = face.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.getVertex().hasUVW(face)) {
						hasTexture = false;
						break;
					}
					u += he.getVertex().getUVW(face).ud();
					v += he.getVertex().getUVW(face).vd();
					w += he.getVertex().getUVW(face).wd();
					he = he.getNextInFace();
				} while (he != face.getHalfedge());
				if (hasTexture) {
					final double ifo = 1.0 / face.getFaceDegree();
					fv.setUVW(u * ifo, v * ifo, w * ifo);
				}
				faceVertices.put(face.getKey(), fv);
			}
		}
		selection.collectEdgesByFace();
		final HE_Selection newVertices = HE_Selection
				.getSelection(selection.getParent());
		if (random) {
			final HE_Halfedge[] edges = selection.getEdgesAsArray();
			final int ne = selection.getNumberOfEdges();
			for (int i = 0; i < ne; i++) {
				HE_Vertex v;
				final double f = 0.5 + (randomGen.nextDouble() - 0.5) * range;
				v = HE_MeshOp.splitEdge(selection.getParent(), edges[i], f)
						.vItr().next();
				if (v != null) {
					newVertices.add(v);
				}
			}
		} else {
			newVertices.add(HE_MeshOp.splitEdges(selection));
		}
		final ArrayList<HE_Face> newFaces = new ArrayList<HE_Face>();
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
				newFaces.add(centerFace);
				final ArrayList<HE_Halfedge> faceHalfedges = new ArrayList<HE_Halfedge>();
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					newFace.copyProperties(face);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE = new HE_Halfedge();
					final HE_Halfedge newHEp = new HE_Halfedge();
					faceHalfedges.add(newHEp);
					if (origHE3.getVertex().hasHalfedgeUVW(face)) {
						newHE.setUVW(origHE3.getVertex().getHalfedgeUVW(face));
					}
					selection.getParent().setHalfedge(newFace, origHE1);
					selection.getParent().setNext(origHE2, newHE);
					selection.getParent().setNext(newHE, origHE1);
					selection.getParent().setVertex(newHE, origHE3.getVertex());
					selection.getParent().setFace(newHE, newFace);
					selection.getParent().setFace(origHE1, newFace);
					selection.getParent().setFace(origHE2, newFace);
					selection.getParent().setVertex(newHEp,
							origHE1.getVertex());
					selection.getParent().setPair(newHE, newHEp);
					selection.getParent().setFace(newHEp, centerFace);
					selection.getParent().setHalfedge(centerFace, newHEp);
					selection.getParent().add(newHE);
					selection.getParent().add(newHEp);
					origHE1 = origHE3;
				} while (origHE1 != startHE);
				HE_MeshOp.cycleHalfedges(selection.getParent(), faceHalfedges);
				HE_Halfedge cfhe = centerFace.getHalfedge();
				do {
					if (cfhe.getPair().getNextInFace().hasHalfedgeUVW()) {
						cfhe.setUVW(cfhe.getPair().getNextInFace().getUVW());
					}
					cfhe = cfhe.getNextInFace();
				} while (cfhe != centerFace.getHalfedge());
			} else {
				HE_Halfedge origHE1 = startHE;
				do {
					final HE_Face newFace = new HE_Face();
					newFaces.add(newFace);
					newFace.copyProperties(face);
					selection.getParent().setHalfedge(newFace, origHE1);
					final HE_Halfedge origHE2 = origHE1.getNextInFace();
					final HE_Halfedge origHE3 = origHE2.getNextInFace();
					final HE_Halfedge newHE1 = new HE_Halfedge();
					final HE_Halfedge newHE2 = new HE_Halfedge();
					if (origHE3.getVertex().hasHalfedgeUVW(face)) {
						newHE1.setUVW(origHE3.getVertex().getHalfedgeUVW(face));
					}
					selection.getParent().setNext(origHE2, newHE1);
					selection.getParent().setNext(newHE1, newHE2);
					selection.getParent().setNext(newHE2, origHE1);
					selection.getParent().setVertex(newHE1,
							origHE3.getVertex());
					final HE_Vertex fv = faceVertices
							.get(origHE1.getFace().getKey());
					selection.getParent().setVertex(newHE2, fv);
					if (fv.getHalfedge() == null) {
						selection.getParent().setHalfedge(fv, newHE2);
					}
					if (!selection.getParent().contains(fv)) {
						selection.getParent().add(fv);
					}
					selection.getParent().setFace(newHE1, newFace);
					selection.getParent().setFace(newHE2, newFace);
					selection.getParent().setFace(origHE1, newFace);
					selection.getParent().setFace(origHE2, newFace);
					selection.getParent().add(newHE1);
					selection.getParent().add(newHE2);
					origHE1 = origHE3;
				} while (origHE1 != startHE);
				HE_MeshOp.pairHalfedges(selection.getParent());
			}
		} // end of face loop
		HE_MeshOp.pairHalfedges(selection.getParent());
		selection.getParent().removeFaces(selection.getFacesAsArray());
		selection.getParent().addFaces(newFaces);
		return selection.getParent();
	}

	public static void main(final String[] args) {
		HEC_Cube creator = new HEC_Cube(400, 1, 1, 1);
		HE_Mesh mesh = new HE_Mesh(creator);
		mesh.modify(new HEM_ChamferCorners().setDistance(70));
		HES_Planar subdividor = new HES_Planar();
		subdividor.setRandom(true);// Randomize center edge and center face
									// points
		subdividor.setRange(0.3);// Random range of center offset, from 0 (no
									// random) to 1(fully random)
		subdividor.setSeed(1234L);// Seed of random point generator
		subdividor.setKeepTriangles(true);// Subdivide triangles into 4
											// triangles instead of 3 quads
		mesh.subdivide(subdividor);
		mesh.validate();
	}
}
