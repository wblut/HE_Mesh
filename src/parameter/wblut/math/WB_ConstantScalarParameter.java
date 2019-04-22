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
 * The Class WB_ConstantParameter.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 *         A parameter which is constant, i.e. a single unchanging value.
 */
public class WB_ConstantScalarParameter implements WB_ScalarParameter {
	/** The value. */
	double value;

	/**
	 * 
	 *
	 * @param value
	 */
	public WB_ConstantScalarParameter(final double value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.math.WB_ScalarParameter#evaluate(double[])
	 */
	@Override
	public double evaluate(final double... x) {
		return value;
	}
}
