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
public abstract class HEMC_MultiCreator extends HE_Machine {

	/**
	 *
	 */
	protected int _numberOfMeshes;

	/**
	 *
	 */
	public HEMC_MultiCreator() {
		super();
		_numberOfMeshes = 0;
	}

	/**
	 *
	 *
	 * @return
	 */
	public final HE_MeshCollection create() {
		HE_MeshCollection result = new HE_MeshCollection();
		create(result);
		return result;

	}

	abstract void create(HE_MeshCollection result);

	/**
	 *
	 *
	 * @return
	 */
	public int numberOfMeshes() {
		return _numberOfMeshes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Machine#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		mesh.setNoCopy(create().getMesh(0));
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Machine#apply(wblut.hemesh.HE_Selection)
	 */
	@Override
	public HE_Mesh apply(final HE_Selection sel) {
		return create().getMesh(0);
	}
}
