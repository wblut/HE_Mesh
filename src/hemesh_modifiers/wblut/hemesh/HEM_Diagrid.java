/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

/**
 * Divides all faces in mesh or selection in triangles connecting the face
 * center with each edge and deletes all original non-boundary edges with a
 * dihedral angle larger that parameter limitAngle.
 *
 *
 * @author frederikvanhoutte
 *
 */
public class HEM_Diagrid extends HEM_Modifier {
	/**
	 *
	 */
	private double limitAngle;

	/**
	 *
	 */
	public HEM_Diagrid() {
		limitAngle = 1.001 * 0.5 * Math.PI;
	}

	/**
	 * Set the lower limit dihedral angle.
	 *
	 * @param a
	 *            : limit angle in radius, edges with dihedral angle lower than
	 *            this angle are not removed. Default value is PI/2
	 * @return
	 */
	public HEM_Diagrid setLimitAngle(final double a) {
		limitAngle = a;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HE_Selection sel = mesh.selectAllEdges();
		HE_MeshOp.splitFacesTri(mesh);
		final HE_EdgeIterator eitr = sel.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (!e.isInnerBoundary()
					&& HE_MeshOp.getEdgeDihedralAngle(e) > limitAngle) {
				mesh.deleteEdge(e);
			}
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Selection)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		selection.collectEdgesByFace();
		HE_MeshOp.splitFacesTri(selection);
		final HE_RAS<HE_Halfedge> border = new HE_RAS<HE_Halfedge>();
		border.addAll(selection.getOuterEdges());
		final HE_EdgeIterator eitr = selection.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (!border.contains(e)
					&& HE_MeshOp.getEdgeDihedralAngle(e) > limitAngle) {
				selection.getParent().deleteEdge(e);
			}
		}
		return selection.getParent();
	}
}
