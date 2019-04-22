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
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_RemoveParts extends HEM_Modifier {

	/**
	 *
	 */
	private int n;

	/**
	 *
	 */
	public HEM_RemoveParts() {
		super();
		n = 4;
	}

	/**
	 *
	 */
	public HEM_RemoveParts(final int min) {
		super();
		n = min;
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	public HEM_RemoveParts setMinimumFaces(final int n) {
		this.n = n;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HEMC_Explode explode = new HEMC_Explode().setMesh(mesh);
		final HE_MeshCollection fragments = explode.create();
		mesh.clear();

		for (final HE_Mesh frag : fragments.meshes) {
			if (frag.getNumberOfFaces() > n) {
				mesh.add(frag);
			}
		}

		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}

}
