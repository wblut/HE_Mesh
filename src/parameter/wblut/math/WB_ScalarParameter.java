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
 *
 *
 *
 */
public interface WB_ScalarParameter {

	public static final WB_ScalarParameter ZERO = new WB_ConstantScalarParameter(0.0);
	public static final WB_ScalarParameter ONE = new WB_ConstantScalarParameter(1.0);

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public double evaluate(double... x);
}
