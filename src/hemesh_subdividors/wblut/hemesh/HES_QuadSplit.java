/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Point;

/**
 *
 */
public class HES_QuadSplit extends HES_Subdividor {
	/**
	 *
	 */
	private HE_Selection	selectionOut;
	/**
	 *
	 */
	private double			d;

	/**
	 *
	 */
	public HES_QuadSplit() {
		super();
		d = 0;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HES_QuadSplit setOffset(final double d) {
		this.d = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		selectionOut = HE_Selection.getSelection(mesh);
		final int n = mesh.getNumberOfFaces();
		final WB_Point[] faceCenters = new WB_Point[n];
		final int[] faceOrders = new int[n];
		HE_Face f;
		int i = 0;
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Getting face centers.", counter);
		final Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			faceCenters[i] = WB_Point.addMul(HE_MeshOp.getFaceCenter(f), d,
					HE_MeshOp.getFaceNormal(f));
			faceOrders[i] = f.getFaceDegree();
			i++;
			counter.increment();
		}
		final HE_Selection orig = mesh.selectAllFaces();
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut
				.addVertices(HE_MeshOp.splitEdges(mesh).getVerticesAsArray());
		final HE_Face[] faces = mesh.getFacesAsArray();
		HE_Vertex vi = new HE_Vertex();
		counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Splitting faces into quads.", counter);
		for (i = 0; i < n; i++) {
			f = faces[i];
			vi = new HE_Vertex(faceCenters[i]);
			vi.setInternalLabel(2);
			double u = 0;
			double v = 0;
			double w = 0;
			HE_Halfedge he = f.getHalfedge();
			boolean hasTexture = true;
			do {
				if (!he.getVertex().hasUVW(f)) {
					hasTexture = false;
					break;
				}
				u += he.getVertex().getUVW(f).ud();
				v += he.getVertex().getUVW(f).vd();
				w += he.getVertex().getUVW(f).wd();
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
			if (hasTexture) {
				final double ifo = 1.0 / f.getFaceDegree();
				vi.setUVW(u * ifo, v * ifo, w * ifo);
			}
			mesh.addDerivedElement(vi, f);
			selectionOut.add(vi);
			HE_Halfedge startHE = f.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			he = startHE;
			final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he3 = new HE_Halfedge[faceOrders[i]];
			int c = 0;
			do {
				HE_Face fc;
				if (c == 0) {
					fc = f;
				} else {
					fc = new HE_Face();
					fc.copyProperties(f);
					mesh.addDerivedElement(fc, f);
				}
				he0[c] = he;
				mesh.setFace(he, fc);
				mesh.setHalfedge(fc, he);
				he1[c] = he.getNextInFace();
				he2[c] = new HE_Halfedge();
				he3[c] = new HE_Halfedge();
				mesh.setVertex(he2[c],
						he.getNextInFace().getNextInFace().getVertex());
				if (he2[c].getVertex().hasHalfedgeUVW(f)) {
					he2[c].setUVW(he2[c].getVertex().getHalfedgeUVW(f));
				}
				mesh.setVertex(he3[c], vi);
				mesh.setNext(he2[c], he3[c]);
				mesh.setNext(he3[c], he);
				mesh.setFace(he1[c], fc);
				mesh.setFace(he2[c], fc);
				mesh.setFace(he3[c], fc);
				mesh.add(he2[c]);
				mesh.add(he3[c]);
				c++;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			mesh.setHalfedge(vi, he3[0]);
			for (int j = 0; j < c; j++) {
				mesh.setNext(he1[j], he2[j]);
			}
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(mesh);
		tracker.setStopStatus(this, "Exiting HEM_QuadSplit.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection sel) {
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		selectionOut = HE_Selection.getSelection(sel.getParent());
		final int n = sel.getNumberOfFaces();
		final WB_Point[] faceCenters = new WB_Point[n];
		final int[] faceOrders = new int[n];
		HE_Face face;
		final Iterator<HE_Face> fItr = sel.fItr();
		int i = 0;
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Getting face centers.", counter);
		while (fItr.hasNext()) {
			face = fItr.next();
			faceCenters[i] = WB_Point.addMul(HE_MeshOp.getFaceCenter(face), d,
					HE_MeshOp.getFaceNormal(face));
			faceOrders[i] = face.getFaceDegree();
			i++;
			counter.increment();
		}
		final HE_Selection orig = HE_Selection.getSelection(sel.getParent());
		orig.addFaces(sel.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(HE_MeshOp.splitEdges(orig).getVertices());
		final HE_Face[] faces = sel.getFacesAsArray();
		counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Splitting faces into quads.", counter);
		for (i = 0; i < n; i++) {
			face = faces[i];
			final HE_Vertex vi = new HE_Vertex(faceCenters[i]);
			sel.getParent().addDerivedElement(vi, face);
			vi.setInternalLabel(2);
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
				vi.setUVW(u * ifo, v * ifo, w * ifo);
			}
			selectionOut.add(vi);
			HE_Halfedge startHE = face.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			he = startHE;
			final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he3 = new HE_Halfedge[faceOrders[i]];
			int c = 0;
			do {
				HE_Face f;
				if (c == 0) {
					f = face;
				} else {
					f = new HE_Face();
					sel.getParent().addDerivedElement(f, face);
					f.copyProperties(face);
					sel.add(f);
				}
				he0[c] = he;
				sel.getParent().setFace(he, f);
				sel.getParent().setHalfedge(f, he);
				he1[c] = he.getNextInFace();
				he2[c] = new HE_Halfedge();
				he3[c] = new HE_Halfedge();
				sel.getParent().setVertex(he2[c],
						he.getNextInFace().getNextInFace().getVertex());
				if (he2[c].getVertex().hasHalfedgeUVW(face)) {
					he2[c].setUVW(he2[c].getVertex().getHalfedgeUVW(face));
				}
				sel.getParent().setVertex(he3[c], vi);
				sel.getParent().setNext(he2[c], he3[c]);
				sel.getParent().setNext(he3[c], he);
				sel.getParent().setFace(he1[c], f);
				sel.getParent().setFace(he2[c], f);
				sel.getParent().setFace(he3[c], f);
				sel.getParent().add(he2[c]);
				sel.getParent().add(he3[c]);
				c++;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			sel.getParent().setHalfedge(vi, he3[0]);
			for (int j = 0; j < c; j++) {
				sel.getParent().setNext(he1[j], he2[j]);
			}
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(sel.getParent());
		tracker.setStopStatus(this, "Exiting HEM_QuadSplit.");
		return sel.getParent();
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection getSplitFaces() {
		return this.selectionOut;
	}
}
