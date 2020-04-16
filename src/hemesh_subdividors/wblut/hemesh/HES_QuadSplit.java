package wblut.hemesh;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Point;

public class HES_QuadSplit extends HES_Subdividor {
	private HE_Selection selectionOut;
	private double d;

	public HES_QuadSplit() {
		super();
		d = 0;
	}

	public HES_QuadSplit setOffset(final double d) {
		this.d = d;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		selectionOut = HE_Selection.getSelection(mesh);
		final int n = mesh.getNumberOfFaces();
		final WB_Point[] faceCenters = new WB_Point[n];
		final int[] faceDegrees = new int[n];
		HE_Face f;
		int i = 0;
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Getting face centers.", counter);
		final HE_FaceIterator fItr = mesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			faceCenters[i] = WB_Point.addMul(HE_MeshOp.getFaceCenter(f), d, HE_MeshOp.getFaceNormal(f));
			faceDegrees[i] = f.getFaceDegree();
			i++;
			counter.increment();
		}
		final HE_Selection orig = mesh.selectAllFaces();
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(HE_MeshOp.splitEdges(mesh).getVertices());
		final HE_Face[] faces = mesh.getFacesAsArray();
		HE_Vertex vi = new HE_Vertex();
		counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Splitting faces into quads.", counter);
		double u, v, w;
		boolean hasTexture = true;
		for (i = 0; i < n; i++) {
			f = faces[i];
			vi = new HE_Vertex(faceCenters[i]);
			vi.setInternalLabel(2);
			u = 0;
			v = 0;
			w = 0;
			HE_Halfedge he = f.getHalfedge();
			do {
				if (!he.hasUVW()) {
					hasTexture = false;
					break;
				}
				u += he.getUVW().ud();
				v += he.getUVW().vd();
				w += he.getUVW().wd();
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
			final double ifo = 1.0 / f.getFaceDegree();
			u *= ifo;
			v *= ifo;
			w *= ifo;
			mesh.addDerivedElement(vi, f);
			selectionOut.add(vi);
			HE_Halfedge startHE = f.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			he = startHE;
			final HE_Halfedge[] he0 = new HE_Halfedge[faceDegrees[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceDegrees[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceDegrees[i]];
			final HE_Halfedge[] he3 = new HE_Halfedge[faceDegrees[i]];
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
				mesh.setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
				if (he.getNextInFace().getNextInFace().hasUVW()) {
					he2[c].setUVW(he.getNextInFace().getNextInFace().getUVW());
				}
				mesh.setVertex(he3[c], vi);
				if (hasTexture) {
					he3[c].setUVW(u, v, w);
				}
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

	@Override
	protected HE_Mesh applySelf(final HE_Selection sel) {
		final HE_Mesh mesh = sel.getParent();
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		selectionOut = HE_Selection.getSelection(mesh);
		final int n = sel.getNumberOfFaces();
		final WB_Point[] faceCenters = new WB_Point[n];
		final int[] faceDegrees = new int[n];
		HE_Face f;
		final HE_FaceIterator fItr = sel.fItr();
		int i = 0;
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Getting face centers.", counter);
		while (fItr.hasNext()) {
			f = fItr.next();
			faceCenters[i] = WB_Point.addMul(HE_MeshOp.getFaceCenter(f), d, HE_MeshOp.getFaceNormal(f));
			faceDegrees[i] = f.getFaceDegree();
			i++;
			counter.increment();
		}
		final HE_Selection orig = HE_Selection.getSelection(mesh);
		orig.addFaces(sel.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(HE_MeshOp.splitEdges(orig).getVertices());
		final HE_Face[] faces = sel.getFacesAsArray();
		counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Splitting faces into quads.", counter);
		double u, v, w;
		boolean hasTexture = true;
		for (i = 0; i < n; i++) {
			f = faces[i];
			final HE_Vertex vi = new HE_Vertex(faceCenters[i]);
			mesh.addDerivedElement(vi, f);
			vi.setInternalLabel(2);
			u = 0;
			v = 0;
			w = 0;
			HE_Halfedge he = f.getHalfedge();
			hasTexture = true;
			do {
				if (!he.hasUVW()) {
					hasTexture = false;
					break;
				}
				u += he.getUVW().ud();
				v += he.getUVW().vd();
				w += he.getUVW().wd();
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
			final double ifo = 1.0 / f.getFaceDegree();
			u *= ifo;
			v *= ifo;
			w *= ifo;
			selectionOut.add(vi);
			HE_Halfedge startHE = f.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			he = startHE;
			final HE_Halfedge[] he0 = new HE_Halfedge[faceDegrees[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceDegrees[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceDegrees[i]];
			final HE_Halfedge[] he3 = new HE_Halfedge[faceDegrees[i]];
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
				mesh.setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
				if (he.getNextInFace().getNextInFace().hasUVW()) {
					he2[c].setUVW(he.getNextInFace().getNextInFace().getUVW());
				}
				mesh.setVertex(he3[c], vi);
				if (hasTexture) {
					he3[c].setUVW(u, v, w);
				}
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

	public HE_Selection getSplitFaces() {
		return this.selectionOut;
	}

	public static void main(final String args[]) {
		final HE_Mesh mesh = new HEC_Grid().setSizeU(100.0).setsizeV(100.0).setU(10).setsizeV(10).create();
		mesh.validate();
		mesh.subdivide(new HES_QuadSplit());
		mesh.selectRandomFaces("test", 0.5);
		mesh.getSelection("test").subdivide(new HES_QuadSplit());
	}
}
