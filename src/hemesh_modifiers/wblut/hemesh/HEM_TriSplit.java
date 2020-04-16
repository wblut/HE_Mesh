package wblut.hemesh;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

public class HEM_TriSplit extends HEM_Modifier {
	private double d;
	private HE_Selection selectionOut;

	public HEM_TriSplit() {
		super();
		d = 0;
	}

	public HEM_TriSplit setOffset(final double d) {
		this.d = d;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_TriSplit.");
		splitFacesTri(mesh.selectAllFaces(), d);
		tracker.setStopStatus(this, "Exiting HEM_TriSplit.");
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting HEM_TriSplit.");
		splitFacesTri(selection, d);
		tracker.setStopStatus(this, "Exiting HEM_TriSplit.");
		return selection.getParent();
	}

	private void splitFacesTri(final HE_Selection selection, final double d) {
		selectionOut = HE_Selection.getSelection(selection.getParent());
		final HE_Face[] faces = selection.getFacesAsArray();
		final int n = selection.getNumberOfFaces();
		final WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Splitting faces.", counter);
		for (int i = 0; i < n; i++) {
			final HE_Selection sft = splitFaceTri(faces[i], d, selection.getParent());
			if (sft != null) {
				selectionOut.add(sft);
			}
			counter.increment();
		}
		selection.add(selectionOut);
	}

	private HE_Selection splitFaceTri(final HE_Face face, final double d, final HE_Mesh mesh) {
		return splitFaceTri(mesh, face,
				WB_Point.addMul(HE_MeshOp.getFaceCenter(face), d, HE_MeshOp.getFaceNormal(face)));
	}

	public static HE_Selection splitFaceTri(final HE_Mesh mesh, final HE_Face face, final WB_Coord p) {
		HE_Halfedge he = face.getHalfedge();
		final HE_Vertex vi = new HE_Vertex(p);
		vi.setInternalLabel(2);
		double u = 0;
		double v = 0;
		double w = 0;
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
		he = face.getHalfedge();
		final HE_Selection out = HE_Selection.getSelection(mesh);
		int c = 0;
		boolean onEdge = false;
		do {
			c++;
			final WB_Plane P = new WB_Plane(HE_MeshOp.getHalfedgeCenter(he), HE_MeshOp.getHalfedgeNormal(he));
			final double d = WB_GeometryOp3D.getDistance3D(p, P);
			if (WB_Epsilon.isZero(d)) {
				onEdge = true;
				break;
			}
			he = he.getNextInFace();
		} while (he != face.getHalfedge());
		if (!onEdge) {
			mesh.addDerivedElement(vi, face);
			final HE_Halfedge[] he0 = new HE_Halfedge[c];
			final HE_Halfedge[] he1 = new HE_Halfedge[c];
			final HE_Halfedge[] he2 = new HE_Halfedge[c];
			c = 0;
			do {
				HE_Face f;
				if (c == 0) {
					f = face;
				} else {
					f = new HE_Face();
					f.copyProperties(face);
					mesh.addDerivedElement(f, face);
					out.add(f);
				}
				he0[c] = he;
				mesh.setFace(he, f);
				mesh.setHalfedge(f, he);
				he1[c] = new HE_Halfedge();
				he2[c] = new HE_Halfedge();
				if (he.getNextInFace().hasUVW()) {
					he1[c].setUVW(he.getNextInFace().getUVW());
				}
				mesh.setVertex(he1[c], he.getNextInFace().getVertex());
				mesh.setVertex(he2[c], vi);
				if (hasTexture) {
					final double ifo = 1.0 / face.getFaceDegree();
					he2[c].setUVW(u * ifo, v * ifo, w * ifo);
				}
				mesh.setNext(he1[c], he2[c]);
				mesh.setNext(he2[c], he);
				mesh.setFace(he1[c], f);
				mesh.setFace(he2[c], f);
				mesh.addDerivedElement(he1[c], face);
				mesh.addDerivedElement(he2[c], face);
				c++;
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			mesh.setHalfedge(vi, he2[0]);
			for (int i = 0; i < c; i++) {
				mesh.setNext(he0[i], he1[i]);
				mesh.setPair(he1[i], he2[i == c - 1 ? 0 : i + 1]);
			}
			out.add(vi);
			return out;
		}
		return null;
	}

	public HE_Selection getSplitFaces() {
		return this.selectionOut;
	}
}
