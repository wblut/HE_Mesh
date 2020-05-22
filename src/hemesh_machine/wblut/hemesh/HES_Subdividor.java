package wblut.hemesh;

/**
 *
 */
abstract public class HES_Subdividor extends HE_Machine {
	/**
	 *
	 */
	public HES_Subdividor() {
		super();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting subvidision.");
		if (mesh == null || mesh.getNumberOfVertices() == 0) {
			tracker.setStopStatus(this, "Nothing to subdivide.");
			return new HE_Mesh();
		}
		HE_Mesh copy = mesh.get();
		try {
			final HE_Mesh result = applySelf(mesh);
			tracker.setStopStatus(this, "Mesh subdivided.");
			return result;
		} catch (final Exception e) {
			e.printStackTrace();
			mesh.setNoCopy(copy);
			tracker.setStopStatus(this, "Subdivision failed. Resetting mesh.");
			return mesh;
		} finally {
			copy = null;
		}
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	public HE_Mesh apply(final HE_Selection selection) {
		if (selection == null) {
			tracker.setStopStatus(this, "Nothing to subdivide.");
			return new HE_Mesh();
		}
		HE_Mesh copy = selection.getParent().get();
		try {
			final String sel = "selection" + (int) (Math.random() * 1000000);
			selection.getParent().replaceSelection(sel, selection);
			final HE_Mesh result = applySelf(selection);
			result.removeSelection(sel);
			tracker.setStopStatus(this, "Mesh subdivided.");
			return result;
		} catch (final Exception e) {
			e.printStackTrace();
			selection.getParent().setNoCopy(copy);
			tracker.setStopStatus(this, "Subdivision failed. Resetting mesh.");
			return selection.getParent();
		} finally {
			copy = null;
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	protected abstract HE_Mesh applySelf(final HE_Mesh mesh);

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	protected abstract HE_Mesh applySelf(final HE_Selection selection);
}
