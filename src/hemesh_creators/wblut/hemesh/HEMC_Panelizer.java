/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Planar cut of a mesh. Both parts are returned as separate meshes.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_Panelizer extends HEMC_MultiCreator {
	/** Source mesh. */
	private HE_Mesh				mesh;
	private WB_ScalarParameter	thickness;
	private WB_ScalarParameter	offset;

	public HEMC_Panelizer() {
		super();
		thickness = new WB_ConstantScalarParameter(0);
		offset = new WB_ConstantScalarParameter(0);
	}

	public HEMC_Panelizer setThickness(final double d) {
		thickness = new WB_ConstantScalarParameter(d);
		return this;
	}

	public HEMC_Panelizer setOffset(final double d) {
		offset = new WB_ConstantScalarParameter(d);
		return this;
	}

	public HEMC_Panelizer setThickness(final WB_ScalarParameter d) {
		thickness = d;
		return this;
	}

	public HEMC_Panelizer setOffset(final WB_ScalarParameter d) {
		offset = d;
		return this;
	}

	/**
	 * Set source mesh.
	 *
	 * @param mesh
	 *            mesh to panelize
	 * @return self
	 */
	public HEMC_Panelizer setMesh(final HE_Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {
		if (mesh == null) {
			_numberOfMeshes = 0;
			return;
		}
		int id = 0;
		final HEC_Polygon pc = new HEC_Polygon();
		WB_Coord fc;
		for (final HE_Face f : mesh.getFaces()) {
			fc = HE_MeshOp.getFaceCenter(f);
			pc.setThickness(-thickness.evaluate(fc.xd(), fc.yd(), fc.zd()));
			pc.setOffset(-offset.evaluate(fc.xd(), fc.yd(), fc.zd()));
			pc.setPolygon(HE_MeshOp.getPolygon(f));
			result.add(new HE_Mesh(pc));
			id++;
		}
		_numberOfMeshes = id;
	}
}
