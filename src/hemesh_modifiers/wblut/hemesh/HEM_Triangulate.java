/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.List;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEM_Triangulate extends HEM_Modifier {
	/**
	 *
	 */
	public HE_Selection triangles;

	/**
	 *
	 */
	public HEM_Triangulate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		triangles = HE_Selection.getSelection(mesh);
		tracker.setStartStatus(this, "Starting HEM_Triangulate.");
		final HE_Face[] f = mesh.getFacesAsArray();
		final int n = mesh.getNumberOfFaces();
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Triangulating faces.", counter);
		for (int i = 0; i < n; i++) {
			if (!WB_Epsilon.isZero(
					WB_Vector.getLength3D(HE_MeshOp.getFaceNormal(f[i])))) {
				triangulateNoPairing(f[i], mesh);
			} else {
				final HE_Halfedge he = f[i].getHalfedge();
				do {
					mesh.clearPair(he);
					mesh.setHalfedge(he.getVertex(), he);
				} while (he != f[i].getHalfedge());
			}
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		tracker.setStopStatus(this, "Exiting HEM_Triangulate.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		triangles = HE_Selection.getSelection(selection.getParent());
		tracker.setStartStatus(this, "Starting HEM_Triangulate.");
		final HE_Face[] f = selection.getFacesAsArray();
		final int n = selection.getNumberOfFaces();
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Triangulating faces.", counter);
		for (int i = 0; i < n; i++) {
			if (!WB_Epsilon.isZero(
					WB_Vector.getLength3D(HE_MeshOp.getFaceNormal(f[i])))) {
				triangulateNoPairing(f[i], selection.getParent());
			} else {
				final HE_Halfedge he = f[i].getHalfedge();
				do {
					selection.getParent().clearPair(he);
					selection.getParent().setHalfedge(he.getVertex(), he);
				} while (he != f[i].getHalfedge());
			}
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(selection.getParent());
		HE_MeshOp.capHalfedges(selection.getParent());
		selection.clearFaces();
		selection.add(triangles);
		tracker.setStopStatus(this, "Exiting HEM_Triangulate.");
		return selection.getParent();
	}

	/**
	 *
	 *
	 * @param face
	 * @param mesh
	 */
	private void triangulateNoPairing(final HE_Face face, final HE_Mesh mesh) {
		if (face.getFaceDegree() == 3) {
			triangles.add(face);
		} else if (face.getFaceDegree() > 3) {
			final int[] tris = face.getTriangles(false);
			final List<HE_Vertex> vertices = face.getFaceVertices();
			final List<HE_TextureCoordinate> UVWs = face.getFaceUVWs();
			HE_Halfedge he = face.getHalfedge();
			do {
				mesh.clearPair(he);
				mesh.remove(he);
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			for (int i = 0; i < tris.length; i += 3) {
				final HE_Face f = new HE_Face();
				mesh.add(f);
				triangles.add(f);
				f.copyProperties(face);
				final HE_Halfedge he1 = new HE_Halfedge();
				final HE_Halfedge he2 = new HE_Halfedge();
				final HE_Halfedge he3 = new HE_Halfedge();
				he1.setUVW(UVWs.get(tris[i]));
				he2.setUVW(UVWs.get(tris[i + 1]));
				he3.setUVW(UVWs.get(tris[i + 2]));
				mesh.setVertex(he1, vertices.get(tris[i]));
				mesh.setVertex(he2, vertices.get(tris[i + 1]));
				mesh.setVertex(he3, vertices.get(tris[i + 2]));
				mesh.setHalfedge(he1.getVertex(), he1);
				mesh.setHalfedge(he2.getVertex(), he2);
				mesh.setHalfedge(he3.getVertex(), he3);
				mesh.setFace(he1, f);
				mesh.setFace(he2, f);
				mesh.setFace(he3, f);
				mesh.setNext(he1, he2);
				mesh.setNext(he2, he3);
				mesh.setNext(he3, he1);
				mesh.setHalfedge(f, he1);
				mesh.add(he1);
				mesh.add(he2);
				mesh.add(he3);
			}
			mesh.remove(face);
		}
	}
}
