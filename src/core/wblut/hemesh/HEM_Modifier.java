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
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
abstract public class HEM_Modifier extends HE_Machine {
	/**
	 * Instantiates a new HEM_Modifier.
	 */
	public HEM_Modifier() {
		super();
	}

	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting modifier.");
		if (mesh == null || mesh.getNumberOfVertices() == 0) {
			return new HE_Mesh();
		}
		HE_Mesh copy = mesh.get();
		try {
			HE_Mesh result = applySelf(mesh);
			tracker.setStopStatus(this, "Mesh modified.");
			result.cleanSelections();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			mesh.setNoCopy(copy);
			tracker.setStopStatus(this, "Modifier failed. Resetting mesh.");
			return mesh;
		} finally {
			copy = null;
		}

	}

	@Override
	public HE_Mesh apply(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting modifier.");
		if (selection == null) {
			return new HE_Mesh();
		}
		HE_Mesh copy = selection.getParent().get();
		try {
			HE_Mesh result = applySelf(selection);
			result.cleanSelections();
			tracker.setStopStatus(this, "Mesh modified.");

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			selection.getParent().setNoCopy(copy);
			tracker.setStopStatus(this, "Modifier failed. Resetting mesh.");
			return selection.getParent();
		} finally {
			copy = null;
		}

	}

	protected abstract HE_Mesh applySelf(final HE_Mesh mesh);

	protected abstract HE_Mesh applySelf(final HE_Selection selection);

}
