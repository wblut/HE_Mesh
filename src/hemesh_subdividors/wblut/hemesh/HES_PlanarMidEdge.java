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

/**
 *
 */
public class HES_PlanarMidEdge extends HES_Subdividor {
	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Subdividor#subdivide(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_MeshOp.splitEdges(mesh);
		final ArrayList<HE_Face> newFaces = new ArrayList<HE_Face>();
		HE_Face face;
		final Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			final HE_Halfedge startHE = face.getHalfedge().getNextInFace();
			HE_Halfedge origHE1 = startHE;
			final HE_Face centerFace = new HE_Face();
			newFaces.add(centerFace);
			final ArrayList<HE_Halfedge> faceHalfedges = new ArrayList<HE_Halfedge>();
			do {
				final HE_Face newFace = new HE_Face();
				newFaces.add(newFace);
				mesh.setHalfedge(newFace, origHE1);
				final HE_Halfedge origHE2 = origHE1.getNextInFace();
				final HE_Halfedge origHE3 = origHE2.getNextInFace();
				final HE_Halfedge newHE = new HE_Halfedge();
				final HE_Halfedge newHEp = new HE_Halfedge();
				faceHalfedges.add(newHEp);
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
		}
		HE_MeshOp.pairHalfedges(mesh);
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
		HE_MeshOp.splitEdges(selection);
		final ArrayList<HE_Face> newFaces = new ArrayList<HE_Face>();
		HE_Face face;
		final Iterator<HE_Face> fItr = selection.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			final HE_Halfedge startHE = face.getHalfedge().getNextInFace();
			HE_Halfedge origHE1 = startHE;
			final HE_Face centerFace = new HE_Face();
			newFaces.add(centerFace);
			final ArrayList<HE_Halfedge> faceHalfedges = new ArrayList<HE_Halfedge>();
			do {
				final HE_Face newFace = new HE_Face();
				newFaces.add(newFace);
				selection.getParent().setHalfedge(newFace, origHE1);
				final HE_Halfedge origHE2 = origHE1.getNextInFace();
				final HE_Halfedge origHE3 = origHE2.getNextInFace();
				final HE_Halfedge newHE = new HE_Halfedge();
				final HE_Halfedge newHEp = new HE_Halfedge();
				faceHalfedges.add(newHEp);
				selection.getParent().setNext(origHE2, newHE);
				selection.getParent().setNext(newHE, origHE1);
				selection.getParent().setVertex(newHE, origHE3.getVertex());
				selection.getParent().setFace(newHE, newFace);
				selection.getParent().setFace(origHE1, newFace);
				selection.getParent().setFace(origHE2, newFace);
				selection.getParent().setVertex(newHEp, origHE1.getVertex());
				selection.getParent().setPair(newHE, newHEp);
				selection.getParent().setFace(newHEp, centerFace);
				selection.getParent().setHalfedge(centerFace, newHEp);
				selection.getParent().add(newHE);
				selection.getParent().add(newHEp);
				origHE1 = origHE3;
			} while (origHE1 != startHE);
			HE_MeshOp.cycleHalfedges(selection.getParent(), faceHalfedges);
		}
		HE_MeshOp.pairHalfedges(selection.getParent());
		selection.getParent().removeFaces(selection.getFacesAsArray());
		selection.getParent().addFaces(newFaces);
		return null;
	}
}
