package wblut.hemesh;

/**
 *
 */
public class HEM_QuadSplit extends HEM_Modifier {
	/**  */
	private double d;
	/**  */
	private HE_Selection selectionOut;

	/**
	 *
	 */
	public HEM_QuadSplit() {
		super();
		d = 0;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_QuadSplit setOffset(final double d) {
		this.d = d;
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
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		HE_MeshOp.splitFacesQuad(mesh.selectAllFaces(), d);
		tracker.setStopStatus(this, "Exiting HEM_QuadSplit.");
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
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		HE_MeshOp.splitFacesQuad(selection, d);
		tracker.setStopStatus(this, "Exiting HEM_QuadSplit.");
		return selection.getParent();
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
