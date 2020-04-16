package wblut.hemesh;

import wblut.geom.WB_Transform2D;

public class HEM_Transform2D extends HEM_Modifier {
	private WB_Transform2D T;

	public HEM_Transform2D() {
		super();
		T = null;
	}

	public HEM_Transform2D(final WB_Transform2D T) {
		super();
		this.T = T;
	}

	public HEM_Transform2D setTransform(final WB_Transform2D T) {
		this.T = T;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (T == null) {
			return mesh;
		}
		final HE_VertexIterator vItr = mesh.vItr();
		while (vItr.hasNext()) {
			T.applyAsPoint2DSelf(vItr.next());
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (T == null) {
			return selection.getParent();
		}
		final HE_VertexIterator vItr = selection.vItr();
		while (vItr.hasNext()) {
			T.applyAsPoint2DSelf(vItr.next());
		}
		return selection.getParent();
	}
}
