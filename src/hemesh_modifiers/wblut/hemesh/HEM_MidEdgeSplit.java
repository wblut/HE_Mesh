package wblut.hemesh;

public class HEM_MidEdgeSplit extends HEM_Modifier {
	public HEM_MidEdgeSplit() {
		super();
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_MeshOp.splitFacesMidEdge(mesh);
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		HE_MeshOp.splitFacesMidEdge(selection);
		return selection.getParent();
	}
}
