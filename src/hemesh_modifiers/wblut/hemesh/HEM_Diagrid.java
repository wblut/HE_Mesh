package wblut.hemesh;

/**
 *
 */
public class HEM_Diagrid extends HEM_Modifier {
	/**  */
	private double limitAngle;

	/**
	 *
	 */
	public HEM_Diagrid() {
		limitAngle = 1.001 * 0.5 * Math.PI;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_Diagrid setLimitAngle(final double a) {
		limitAngle = a;
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
		final HE_Selection sel = mesh.selectAllEdges();
		HE_MeshOp.splitFacesTri(mesh);
		final HE_EdgeIterator eitr = sel.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (!e.isInnerBoundary() && HE_MeshOp.getEdgeDihedralAngle(e) > limitAngle) {
				mesh.deleteEdge(e);
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
		selection.collectEdgesByFace();
		HE_MeshOp.splitFacesTri(selection);
		final HE_RAS<HE_Halfedge> border = new HE_RAS<>();
		border.addAll(selection.getOuterEdges());
		final HE_EdgeIterator eitr = selection.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (!border.contains(e) && HE_MeshOp.getEdgeDihedralAngle(e) > limitAngle) {
				selection.getParent().deleteEdge(e);
			}
		}
		return selection.getParent();
	}
}
