package wblut.hemesh;

/**
 *
 */
public class HEM_HybridSplit extends HEM_Modifier {
	/**
	 *
	 */
	public HEM_HybridSplit() {
		super();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_MeshOp.splitFacesHybrid(mesh);
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
		HE_MeshOp.splitFacesHybrid(selection);
		return selection.getParent();
	}
}
