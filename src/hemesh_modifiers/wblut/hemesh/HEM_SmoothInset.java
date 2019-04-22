/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Iterator;

/**
 *
 */
public class HEM_SmoothInset extends HEM_Modifier {

	/**
	 *
	 */
	private int rep;

	/**
	 *
	 */
	private double offset;

	/**
	 *
	 */
	public HEM_SmoothInset() {
		rep = 1;
		offset = 0.1;
	}

	/**
	 *
	 *
	 * @param level
	 * @return
	 */
	public HEM_SmoothInset setLevel(final int level) {
		rep = level;
		return this;
	}

	/**
	 *
	 *
	 * @param offset
	 * @return
	 */
	public HEM_SmoothInset setOffset(final double offset) {
		this.offset = offset;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HEM_Extrude ext = new HEM_Extrude().setChamfer(offset).setRelative(false);
		mesh.modify(ext);
		for (int i = 0; i < rep; i++) {
			mesh.getSelection("extruded").collectEdgesByFace();
			final Iterator<HE_Halfedge> eItr = mesh.getSelection("extruded").eItr();
			while (eItr.hasNext()) {
				HE_MeshOp.divideEdge(mesh, eItr.next(), 2);
			}
			mesh.getSelection("extruded").collectVertices();
			mesh.getSelection("extruded").modify(new HEM_Smooth());
		}
		mesh.renameSelection("extruded", "inset");

		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HEM_Extrude ext = new HEM_Extrude().setChamfer(offset).setRelative(false);
		selection.modify(ext);
		for (int i = 0; i < rep; i++) {
			selection.getParent().getSelection("extruded").collectEdgesByFace();
			final Iterator<HE_Halfedge> eItr = selection.getParent().getSelection("extruded").eItr();
			while (eItr.hasNext()) {
				HE_MeshOp.divideEdge(selection.getParent(), eItr.next(), 2);
			}
			selection.getParent().getSelection("extruded").collectVertices();
			selection.getParent().getSelection("extruded").modify(new HEM_Smooth());
		}
		selection.getParent().renameSelection("extruded", "inset");
		return selection.getParent();
	}
}