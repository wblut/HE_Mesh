/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

public class HEM_Dual extends HEM_Modifier {
	private boolean fixNonPlanarFaces;
	
	private boolean keepBoundary;
	/**
	 *
	 */
	public HEM_Dual() {

	}
	
	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Dual setKeepBoundary(final boolean b) {
		keepBoundary = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Dual setFixNonPlanarFaces(final boolean b) {
		fixNonPlanarFaces = b;
		return this;
	}

	/**
	 * /* (non-Javadoc).
	 *
	 * @param mesh
	 * @return
	 * @see wblut.hemesh.HEM_Modifier#applySelf(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_Mesh result = new HE_Mesh(new HEC_Dual(mesh).setFixNonPlanarFaces(fixNonPlanarFaces).setPreserveBoundaries(keepBoundary));
		mesh.set(result);
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Selection)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {

		return applySelf(selection.getParent());
	}
}
