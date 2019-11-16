/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Transform3D;

/**
 * @author FVH
 *
 */
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

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
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

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Selection)
	 */
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
