package wblut.hemesh;

import wblut.math.WB_Epsilon;

public class HEM_HideEdges extends HEM_Modifier {
	private double threshold;

	public HEM_HideEdges() {
		super();
		threshold = 0.0;
	}

	public HEM_HideEdges setThreshold(final double t) {
		threshold = t;
		return this;
	}

	public HEM_HideEdges setThresholdAngle(final double a) {
		threshold = 1.0 - Math.cos(a);
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HE_EdgeIterator eItr = mesh.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (WB_Epsilon.isEqualAbs(HE_MeshOp.getEdgeCosDihedralAngle(e), -1.0, threshold)) {
				e.setVisible(false);
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HE_EdgeIterator eItr = selection.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (WB_Epsilon.isEqualAbs(HE_MeshOp.getEdgeCosDihedralAngle(e), -1.0, threshold)) {
				e.setVisible(false);
			}
		}
		return selection.getParent();
	}
}
