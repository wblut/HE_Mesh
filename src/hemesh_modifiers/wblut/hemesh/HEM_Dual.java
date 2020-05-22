package wblut.hemesh;

/**
 *
 */
public class HEM_Dual extends HEM_Modifier {
	/**  */
	private boolean fixNonPlanarFaces;
	/**  */
	private boolean keepBoundary;

	/**
	 *
	 */
	public HEM_Dual() {
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Dual setKeepBoundary(final boolean b) {
		keepBoundary = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Dual setFixNonPlanarFaces(final boolean b) {
		fixNonPlanarFaces = b;
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
		final HE_Mesh result = new HE_Mesh(
				new HEC_Dual(mesh).setFixNonPlanarFaces(fixNonPlanarFaces).setPreserveBoundaries(keepBoundary));
		mesh.set(result);
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
		return applySelf(selection.getParent());
	}
}
