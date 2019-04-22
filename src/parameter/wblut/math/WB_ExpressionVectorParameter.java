/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.math;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoordinate;

/**
 * The Class WB_ConstantParameter.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 *
 */
public class WB_ExpressionVectorParameter implements WB_VectorParameter {
	String						expressionX;
	String						expressionY;
	String						expressionZ;
	StaticVariableSet<Double>	variables;
	String[]					vars;
	DoubleEvaluator				eval;

	/**
	 * 
	 *
	 * @param equationX
	 * @param equationY
	 * @param equationZ
	 * @param vars
	 */
	public WB_ExpressionVectorParameter(final String equationX,
			final String equationY, final String equationZ,
			final String... vars) {
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		eval = new DoubleEvaluator();
		variables = new StaticVariableSet<Double>();
		for (int i = 0; i < vars.length; i++) {
			this.vars[i] = vars[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.math.WB_VectorParameter#evaluate(double[])
	 */
	@Override
	public WB_Coord evaluate(final double... value) {
		for (int i = 0; i < vars.length; i++) {
			variables.set(vars[i], value[i]);
		}
		double x = eval.evaluate(expressionX, variables);
		double y = eval.evaluate(expressionY, variables);
		double z = eval.evaluate(expressionZ, variables);
		return new WB_MutableCoordinate(x, y, z);
	}
}
