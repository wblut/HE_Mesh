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
	private WB_ScalarParameter	factor;

	public HEC_Interpolate() {
		super();
		override = true;
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
		factor = new WB_ConstantScalarParameter(f);
		return this;
	}

	public HEC_Interpolate setFactor(final WB_ScalarParameter f) {
		factor = f;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (meshA.getNumberOfVertices() != meshB.getNumberOfVertices()) {
			return new HE_Mesh();
		}
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
