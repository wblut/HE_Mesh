package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEMC_Panelizer extends HEMC_MultiCreator {
	/**  */
	private HE_Mesh mesh;

	/**
	 *
	 */
	public HEMC_Panelizer() {
		super();
		setThickness(0);
		setOffset(0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected WB_ScalarParameter getThickness() {
		return (WB_ScalarParameter) parameters.get("thickness", new WB_ConstantScalarParameter(0.0));
	}

	/**
	 *
	 *
	 * @return
	 */
	protected WB_ScalarParameter getOffset() {
		return (WB_ScalarParameter) parameters.get("offset", new WB_ConstantScalarParameter(0.0));
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEMC_Panelizer setThickness(final double d) {
		parameters.set("thickness", new WB_ConstantScalarParameter(d));
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEMC_Panelizer setOffset(final double o) {
		parameters.get("offset", new WB_ConstantScalarParameter(o));
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEMC_Panelizer setThickness(final WB_ScalarParameter d) {
		parameters.set("thickness", d);
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEMC_Panelizer setOffset(final WB_ScalarParameter o) {
		parameters.get("offset", o);
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public HEMC_Panelizer setMesh(final HE_Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	/**
	 *
	 *
	 * @param result
	 */
	@Override
	void create(final HE_MeshCollection result) {
		if (mesh == null) {
			_numberOfMeshes = 0;
			return;
		}
		int id = 0;
		final HEC_Polygon pc = new HEC_Polygon();
		WB_Coord fc;
		final WB_ScalarParameter thickness = getThickness();
		final WB_ScalarParameter offset = getOffset();
		for (final HE_Face f : mesh.getFaces()) {
			fc = mesh.getFaceCenter(f);
			pc.setThickness(-thickness.evaluate(fc.xd(), fc.yd(), fc.zd()));
			pc.setOffset(-offset.evaluate(fc.xd(), fc.yd(), fc.zd()));
			pc.setPolygon(HE_MeshOp.getPolygon(f));
			result.add(new HE_Mesh(pc));
			id++;
		}
		_numberOfMeshes = id;
	}
}
