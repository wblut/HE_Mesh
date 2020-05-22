package wblut.hemesh;

import wblut.geom.WB_Plane;

/**
 *
 */
public class HEMC_SplitMesh extends HEMC_MultiCreator {
	/**  */
	private HE_Mesh mesh;

	/**
	 *
	 */
	public HEMC_SplitMesh() {
		super();
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getOffset() {
		return parameters.get("offset", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected WB_Plane getPlane() {
		return (WB_Plane) parameters.get("plane", null);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected boolean getCap() {
		return parameters.get("cap", true);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEMC_SplitMesh setOffset(final double d) {
		parameters.set("offset", d);
		return this;
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public HEMC_SplitMesh setPlane(final WB_Plane P) {
		parameters.set("plane", P.get());
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public HEMC_SplitMesh setMesh(final HE_Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEMC_SplitMesh setCap(final Boolean b) {
		parameters.set("cap", b);
		return this;
	}

	/**
	 *
	 *
	 * @param result
	 */
	@Override
	void create(final HE_MeshCollection result) {
		final WB_Plane P = getPlane();
		if (mesh == null) {
			_numberOfMeshes = 0;
			return;
		}
		if (P == null) {
			result.add(mesh.copy());
			result.add(new HE_Mesh());
			_numberOfMeshes = 2;
			return;
		}
		final HEM_Slice sm = new HEM_Slice();
		HE_Mesh tmp = mesh.copy();
		sm.setPlane(P).setReverse(false).setCap(getCap()).setOffset(getOffset());
		sm.applySelf(tmp);
		tmp.resetFaceInternalLabels();
		HE_FaceIterator fItr = tmp.getSelection("caps").fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(1);
		}
		result.add(tmp);
		P.flipNormal();
		sm.setPlane(P).setReverse(false).setCap(getCap()).setOffset(getOffset());
		tmp = mesh.copy();
		sm.applySelf(tmp);
		tmp.resetFaceInternalLabels();
		fItr = tmp.getSelection("caps").fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(1);
		}
		result.add(tmp);
		_numberOfMeshes = 2;
	}
}
