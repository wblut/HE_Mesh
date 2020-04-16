package wblut.hemesh;

public abstract class HEMC_MultiCreator extends HE_Machine {
	protected int _numberOfMeshes;

	public HEMC_MultiCreator() {
		super();
		_numberOfMeshes = 0;
	}

	public final HE_MeshCollection create() {
		final HE_MeshCollection result = new HE_MeshCollection();
		create(result);
		return result;
	}

	abstract void create(HE_MeshCollection result);

	public int numberOfMeshes() {
		return _numberOfMeshes;
	}

	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		mesh.setNoCopy(create().getMesh(0));
		return mesh;
	}

	@Override
	public HE_Mesh apply(final HE_Selection sel) {
		return create().getMesh(0);
	}
}
