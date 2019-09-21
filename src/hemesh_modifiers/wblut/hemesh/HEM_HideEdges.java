/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.math.WB_Epsilon;

/**
 * Flip face normals.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_HideEdges extends HEM_Modifier {
	private double threshold;

	/**
	 * Instantiates a new HEM_FlipFaces.
	 */
	public HEM_HideEdges() {
		super();
		threshold = 0.0;
	}

	public HEM_HideEdges setThreshold(final double t) {
		threshold = t;
		return this;
	}
	
	public HEM_HideEdges setThresholdAngle(final double a) {
		threshold = 1.0-Math.cos(a);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_EdgeIterator eItr = mesh.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (WB_Epsilon.isEqualAbs(HE_MeshOp.getEdgeCosDihedralAngle(e),
					-1.0, threshold)) {
				e.setVisible(false);
			}
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		HE_EdgeIterator eItr = selection.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (WB_Epsilon.isEqualAbs(HE_MeshOp.getEdgeCosDihedralAngle(e),
					-1.0, threshold)) {
				e.setVisible(false);
			}
		}
		return selection.getParent();
	}
}
