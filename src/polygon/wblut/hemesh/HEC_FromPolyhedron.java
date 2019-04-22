/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Polyhedron;

/**
 * Creates a new mesh from a WB_Polyhedron.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_FromPolyhedron extends HEC_Creator {
	/**
	 * Polyhedron.
	 */
	private final WB_Polyhedron source;

	/**
	 *
	 *
	 * @param source
	 */
	public HEC_FromPolyhedron(final WB_Polyhedron source) {
		super();
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final HE_Mesh mesh = new HE_Mesh();
		if (source == null) {
			return mesh;
		}
		if (source.getNumberOfVertices() == 0) {
			return mesh;
		}
		final int[][] faces = source.getFaces();
		final List<HE_Vertex> vertices = new FastList<HE_Vertex>();
		HE_Vertex v;
		for (int i = 0; i < source.getNumberOfVertices(); i++) {
			v = new HE_Vertex(source.getVertex(i));
			v.setInternalLabel(i);
			vertices.add(v);
			mesh.add(v);
		}
		int id = 0;
		HE_Halfedge he;
		for (final int[] face : faces) {
			final ArrayList<HE_Halfedge> faceEdges = new ArrayList<HE_Halfedge>();
			final HE_Face hef = new HE_Face();
			hef.setInternalLabel(id);
			id++;
			final int fl = face.length;
			final int[] locface = new int[fl];
			int li = 0;
			locface[li++] = face[0];
			for (int i = 1; i < fl - 1; i++) {
				if (vertices.get(face[i]) != vertices.get(face[i - 1])) {
					locface[li++] = face[i];
				}
			}
			if (vertices.get(face[fl - 1]) != vertices.get(face[fl - 2])
					&& vertices.get(face[fl - 1]) != vertices.get(face[0])) {
				locface[li++] = face[fl - 1];
			}
			if (li > 2) {
				for (int i = 0; i < li; i++) {
					he = new HE_Halfedge();
					faceEdges.add(he);
					mesh.setFace(he, hef);
					if (hef.getHalfedge() == null) {
						mesh.setHalfedge(hef, he);
					}
					mesh.setVertex(he, vertices.get(locface[i]));
					mesh.setHalfedge(he.getVertex(), he);
				}
				mesh.add(hef);
				HE_MeshOp.cycleHalfedges(mesh, faceEdges);
				mesh.addHalfedges(faceEdges);
			}
		}
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		mesh.scaleSelf(source.getScale());
		return mesh;
	}
}
