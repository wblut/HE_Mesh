/**
 *
 */
package wblut.hemesh;

import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * @author FVH
 *
 */
public class HEC_Interpolate extends HEC_Creator {
	private HE_Mesh				meshA;
	private HE_Mesh				meshB;
	

	public HEC_Interpolate() {
		super();
		setOverride(true);
	}
	
	protected WB_ScalarParameter getFactor() {
		return (WB_ScalarParameter)parameters.get("factor", new WB_ConstantScalarParameter(0.0));
	}

	public HEC_Interpolate setMeshA(final HE_Mesh mesh) {
		meshA = mesh;
		return this;
	}

	public HEC_Interpolate setMeshB(final HE_Mesh mesh) {
		meshB = mesh;
		return this;
	}

	public HEC_Interpolate setFactor(final double f) {
		parameters.get("factor", new WB_ConstantScalarParameter(f));
		return this;
	}

	public HEC_Interpolate setFactor(final WB_ScalarParameter f) {
		parameters.get("factor", f);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (meshA==null||meshB==null||meshA.getNumberOfVertices() != meshB.getNumberOfVertices()) {
			return new HE_Mesh();
		}
		WB_ScalarParameter factor=getFactor();
		HE_Mesh result = meshA.get();
		HE_Vertex vA;
		HE_Vertex vB;
		double f;
		for (int i = 0; i < meshA.getNumberOfVertices(); i++) {
			vA = result.getVertexWithIndex(i);
			vB = meshB.getVertexWithIndex(i);
			f = factor.evaluate(vA.xd(), vA.yd(), vA.zd());
			vA.getPosition().mulAddMulSelf(1.0 - f, f, vB);
		}
		return result;
	}
}
