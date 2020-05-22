package wblut.math;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

/**
 *
 */
public class WB_ExpressionScalarParameter implements WB_ScalarParameter {
	/**  */
	String expression;
	/**  */
	StaticVariableSet<Double> variables;
	/**  */
	String[] vars;
	/**  */
	DoubleEvaluator eval;

	/**
	 *
	 *
	 * @param equation
	 * @param vars
	 */
	public WB_ExpressionScalarParameter(final String equation, final String... vars) {
		expression = equation;
		eval = new DoubleEvaluator();
		variables = new StaticVariableSet<>();
		for (int i = 0; i < vars.length; i++) {
			this.vars[i] = vars[i];
		}
	}

	/**
	 *
	 *
	 * @param value
	 * @return
	 */
	@Override
	public double evaluate(final double... value) {
		for (int i = 0; i < vars.length; i++) {
			variables.set(vars[i], value[i]);
		}
		return eval.evaluate(expression, variables);
	}
}
