package wblut.hemesh;

/**
 *
 */
public class HEM_DoNothing extends HEM_Modifier {
	/**
	 *
	 */
	public HEM_DoNothing() {
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
		return selection.getParent();
	}
}
