package wblut.hemesh;

public class HEM_RemoveParts extends HEM_Modifier {
	private int n;

	public HEM_RemoveParts() {
		super();
		n = 4;
	}

	public HEM_RemoveParts(final int min) {
		super();
		n = min;
	}

	public HEM_RemoveParts setMinimumFaces(final int n) {
		this.n = n;
		return this;
	}

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

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
