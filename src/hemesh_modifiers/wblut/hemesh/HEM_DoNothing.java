package wblut.hemesh;

public class HEM_DoNothing extends HEM_Modifier {
	public HEM_DoNothing() {
		super();
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return selection.getParent();
	}
}
