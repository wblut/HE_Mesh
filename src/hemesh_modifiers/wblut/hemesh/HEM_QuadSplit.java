/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

/**
 *
 */
public class HEM_QuadSplit extends HEM_Modifier {
	/**
	 *
	 */
	private double			d;
	/**
	 *
	 */
	private HE_Selection	selectionOut;

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

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_QuadSplit.");
		HE_MeshOp.splitFacesQuad(mesh.selectAllFaces(), d);
		tracker.setStopStatus(this, "Exiting HEM_QuadSplit.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
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
