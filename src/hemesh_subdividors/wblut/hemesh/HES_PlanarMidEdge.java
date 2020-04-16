package wblut.hemesh;

import java.util.ArrayList;
import java.util.List;

public class HES_PlanarMidEdge extends HES_Subdividor {
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_MeshOp.splitEdges(mesh);
		final ArrayList<HE_Face> newFaces = new ArrayList<>();
		HE_Face face;
		final HE_FaceIterator fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			final HE_Halfedge startHE = face.getHalfedge().getNextInFace();
			HE_Halfedge origHE1 = startHE;
			final HE_Face centerFace = new HE_Face();
			newFaces.add(centerFace);
			mesh.addDerivedElement(centerFace, face);
			centerFace.copyProperties(face);
			final ArrayList<HE_Halfedge> faceHalfedges = new ArrayList<>();
			do {
				final HE_Face newFace = new HE_Face();
				newFace.copyProperties(face);
				newFaces.add(newFace);
				mesh.addDerivedElement(newFace, face);
				mesh.setHalfedge(newFace, origHE1);
				final HE_Halfedge origHE2 = origHE1.getNextInFace();
				final HE_Halfedge origHE3 = origHE2.getNextInFace();
				final HE_Halfedge newHE = new HE_Halfedge();
				final HE_Halfedge newHEp = new HE_Halfedge();
				faceHalfedges.add(newHEp);
				mesh.setNext(origHE2, newHE);
				mesh.setNext(newHE, origHE1);
				mesh.setVertex(newHE, origHE3.getVertex());
				if (origHE3.hasUVW()) {
					newHE.setUVW(origHE3.getUVW());
				}
				mesh.setFace(newHE, newFace);
				mesh.setFace(origHE1, newFace);
				mesh.setFace(origHE2, newFace);
				mesh.setVertex(newHEp, origHE1.getVertex());
				if (origHE1.hasUVW()) {
					newHEp.setUVW(origHE1.getUVW());
				}
				mesh.setPair(newHE, newHEp);
				mesh.setFace(newHEp, centerFace);
				mesh.setHalfedge(centerFace, newHEp);
				mesh.add(newHE);
				mesh.add(newHEp);
				origHE1 = origHE3;
			} while (origHE1 != startHE);
			HE_MeshOp.cycleHalfedges(mesh, faceHalfedges);
		}
		HE_MeshOp.pairHalfedges(mesh);
		final List<HE_Face> faces = mesh.getFaces();
		for (final HE_Face f : faces) {
			if (!newFaces.contains(f)) {
				mesh.remove(f);
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HE_Mesh mesh = selection.getParent();
		selection.collectEdgesByFace();
		HE_MeshOp.splitEdges(selection);
		final ArrayList<HE_Face> newFaces = new ArrayList<>();
		HE_Face face;
		final HE_FaceIterator fItr = selection.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			final HE_Halfedge startHE = face.getHalfedge().getNextInFace();
			HE_Halfedge origHE1 = startHE;
			final HE_Face centerFace = new HE_Face();
			newFaces.add(centerFace);
			mesh.addDerivedElement(centerFace, face);
			centerFace.copyProperties(face);
			final ArrayList<HE_Halfedge> faceHalfedges = new ArrayList<>();
			do {
				final HE_Face newFace = new HE_Face();
				newFaces.add(newFace);
				mesh.addDerivedElement(newFace, face);
				newFace.copyProperties(face);
				mesh.setHalfedge(newFace, origHE1);
				final HE_Halfedge origHE2 = origHE1.getNextInFace();
				final HE_Halfedge origHE3 = origHE2.getNextInFace();
				final HE_Halfedge newHE = new HE_Halfedge();
				final HE_Halfedge newHEp = new HE_Halfedge();
				faceHalfedges.add(newHEp);
				mesh.setNext(origHE2, newHE);
				mesh.setNext(newHE, origHE1);
				mesh.setVertex(newHE, origHE3.getVertex());
				if (origHE3.hasUVW()) {
					newHE.setUVW(origHE3.getUVW());
				}
				mesh.setFace(newHE, newFace);
				mesh.setFace(origHE1, newFace);
				mesh.setFace(origHE2, newFace);
				mesh.setVertex(newHEp, origHE1.getVertex());
				if (origHE1.hasUVW()) {
					newHEp.setUVW(origHE1.getUVW());
				}
				mesh.setPair(newHE, newHEp);
				mesh.setFace(newHEp, centerFace);
				mesh.setHalfedge(centerFace, newHEp);
				mesh.add(newHE);
				mesh.add(newHEp);
				origHE1 = origHE3;
			} while (origHE1 != startHE);
			HE_MeshOp.cycleHalfedges(mesh, faceHalfedges);
		}
		HE_MeshOp.pairHalfedges(mesh);
		for (final HE_Face f : selection.getFaces()) {
			if (!newFaces.contains(f)) {
				mesh.remove(f);
			}
		}
		return mesh;
	}
}
