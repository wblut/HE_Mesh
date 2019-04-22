/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

/**
 *
 */
public class HEM_CenterSplitHole extends HEM_Modifier {

	/**
	 *
	 */
	private double d;

	/**
	 *
	 */
	private double c;

	boolean relative;

	/**
	 *
	 */
	private HE_Selection selectionOut;

	/**
	 *
	 */
	public HEM_CenterSplitHole() {
		super();
		d = 0;
		c = 0.5;
		relative = false;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_CenterSplitHole setOffset(final double d) {
		this.d = d;
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEM_CenterSplitHole setChamfer(final double c) {
		this.c = c;
		return this;
	}

	public HEM_CenterSplitHole setRelative(final boolean b) {
		this.relative = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HEM_Extrude ext = new HEM_Extrude().setChamfer(c).setDistance(d).setRelative(relative);
		mesh.modify(ext);
		mesh.deleteFaces(mesh.getSelection("extruded"));
		mesh.removeSelection("extruded");

		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HEM_Extrude ext = new HEM_Extrude().setChamfer(c).setDistance(d).setRelative(relative);
		selection.modify(ext);
		selection.getParent().deleteFaces(selection.getParent().getSelection("extruded"));
		selection.getParent().removeSelection("extruded");
		return selection.getParent();
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection getWallFaces() {
		return this.selectionOut;
	}
}
