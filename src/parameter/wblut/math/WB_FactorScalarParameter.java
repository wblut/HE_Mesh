/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

/**
 * @author FVH
 *
 */
public class WB_FactorScalarParameter implements WB_ScalarParameter {
	private double factor;
	private WB_ScalarParameter parameter;

	public WB_FactorScalarParameter(final double factor, final WB_ScalarParameter parameter) {
		this.factor = factor;
		this.parameter = parameter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.math.WB_ScalarParameter#evaluate(double[])
	 */
	@Override
	public double evaluate(final double... x) {

		return factor * parameter.evaluate(x);
	}

}
