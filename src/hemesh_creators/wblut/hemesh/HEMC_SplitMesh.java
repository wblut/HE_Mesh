/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Plane;

/**
 * Planar cut of a mesh. Both parts are returned as separate meshes.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_SplitMesh extends HEMC_MultiCreator {
	/** Cutting plane. */
	private WB_Plane P;
	/** Source mesh. */
	private HE_Mesh mesh;
	/** Cap holes?. */
	private boolean cap = true;;
	/** The offset. */
	private double offset;

	/**
	 * Set offset.
	 *
	 * @param d
	 *            offset
	 * @return self
	 */
	public HEMC_SplitMesh setOffset(final double d) {
		offset = d;
		return this;
	}

	/**
	 * Instantiates a new HEMC_SplitMesh.
	 *
	 */
	public HEMC_SplitMesh() {
		super();
	}

	/**
	 * Set split plane.
	 *
	 * @param P
	 *            plane
	 * @return self
	 */
	public HEMC_SplitMesh setPlane(final WB_Plane P) {
		this.P = P;
		return this;
	}

	/**
	 * Set source mesh.
	 *
	 * @param mesh
	 *            mesh to split
	 * @return self
	 */
	public HEMC_SplitMesh setMesh(final HE_Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	/**
	 * Set option to cap holes.
	 *
	 * @param b
	 *            true, false;
	 * @return self
	 */
	public HEMC_SplitMesh setCap(final Boolean b) {
		cap = b;
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {

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
		sm.setPlane(P).setReverse(false).setCap(cap).setOffset(offset);
		sm.applySelf(tmp);
		tmp.resetFaceInternalLabels();
		HE_FaceIterator fItr = tmp.getSelection("caps").fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(1);
		}
		result.add(tmp);
		P.flipNormal();
		sm.setPlane(P).setReverse(false).setCap(cap).setOffset(offset);
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
