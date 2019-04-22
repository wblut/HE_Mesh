/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.math;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

/**
 * The Class WB_ConstantParameter.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 *
 */
public class WB_ExpressionScalarParameter implements WB_ScalarParameter {
	String						expression;
	StaticVariableSet<Double>	variables;
	String[]					vars;
	DoubleEvaluator				eval;

	/**
	 * 
	 *
	 * @param equation
	 * @param vars
	 */
	public WB_ExpressionScalarParameter(final String equation,
			final String... vars) {
		expression = equation;
		eval = new DoubleEvaluator();
		variables = new StaticVariableSet<Double>();
		for (int i = 0; i < vars.length; i++) {
			this.vars[i] = vars[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.math.WB_ScalarParameter#evaluate(double[])
	 */
	@Override
	public double evaluate(final double... value) {
		for (int i = 0; i < vars.length; i++) {
			variables.set(vars[i], value[i]);
		}
		return eval.evaluate(expression, variables);
	}
}
