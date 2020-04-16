package wblut.hemesh;

import wblut.geom.WB_Transform3D;

public class HEM_Transform3D extends HEM_Modifier {
	private WB_Transform3D T;

	public HEM_Transform3D() {
		super();
		T = null;
	}

	public HEM_Transform3D(final WB_Transform3D T) {
		super();
		this.T = T;
	}

	public HEM_Transform3D setTransform(final WB_Transform3D T) {
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
			T.applyAsPointSelf(vItr.next());
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
			T.applyAsPointSelf(vItr.next());
		}
		return selection.getParent();
	}
}
