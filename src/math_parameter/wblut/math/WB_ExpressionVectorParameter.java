package wblut.math;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoordinate;

/**
 *
 */
public class WB_ExpressionVectorParameter implements WB_VectorParameter {
	/**  */
	String expressionX;
	/**  */
	String expressionY;
	/**  */
	String expressionZ;
	/**  */
	StaticVariableSet<Double> variables;
	/**  */
	String[] vars;
	/**  */
	DoubleEvaluator eval;

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param equationZ
	 * @param vars
	 */
	public WB_ExpressionVectorParameter(final String equationX, final String equationY, final String equationZ,
			final String... vars) {
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
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
	public WB_Coord evaluate(final double... value) {
		for (int i = 0; i < vars.length; i++) {
			variables.set(vars[i], value[i]);
		}
		final double x = eval.evaluate(expressionX, variables);
		final double y = eval.evaluate(expressionY, variables);
		final double z = eval.evaluate(expressionZ, variables);
		return new WB_MutableCoordinate(x, y, z);
	}
}
