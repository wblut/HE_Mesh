package wblut.hemesh;

import java.util.Collections;
import java.util.Comparator;

/**
 *
 */
public class HEM_KeepLargestParts extends HEM_Modifier {
	/**  */
	private int n;

	/**
	 *
	 */
	public HEM_KeepLargestParts() {
		super();
		n = 1;
	}

	/**
	 *
	 *
	 * @param number
	 */
	public HEM_KeepLargestParts(final int number) {
		super();
		n = number;
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	public HEM_KeepLargestParts setNumberOfParts(final int n) {
		this.n = n;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HEMC_Explode explode = new HEMC_Explode().setMesh(mesh);
		final HE_MeshCollection fragments = explode.create();
		Collections.sort(fragments.meshes, new MeshSizeComparator());
		mesh.clear();
		for (int i = 0; i < Math.min(n, fragments.size()); i++) {
			mesh.add(fragments.getMesh(i));
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}

	/**
	 *
	 */
	static class MeshSizeComparator implements Comparator<HE_Mesh> {
		/**
		 *
		 *
		 * @param mesh1
		 * @param mesh2
		 * @return
		 */
		@Override
		public int compare(final HE_Mesh mesh1, final HE_Mesh mesh2) {
			return 0 - Integer.compare(mesh1.getNumberOfFaces(), mesh2.getNumberOfFaces());
		}
	}
}
