package wblut.hemesh;

public class HEM_Dual extends HEM_Modifier {
	private boolean fixNonPlanarFaces;
	private boolean keepBoundary;

	public HEM_Dual() {
	}

	public HEM_Dual setKeepBoundary(final boolean b) {
		keepBoundary = b;
		return this;
	}

	public HEM_Dual setFixNonPlanarFaces(final boolean b) {
		fixNonPlanarFaces = b;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HE_Mesh result = new HE_Mesh(
				new HEC_Dual(mesh).setFixNonPlanarFaces(fixNonPlanarFaces).setPreserveBoundaries(keepBoundary));
		mesh.set(result);
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
