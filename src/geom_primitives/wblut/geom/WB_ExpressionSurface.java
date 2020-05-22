package wblut.geom;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

/**
 *
 */
public class WB_ExpressionSurface implements WB_Surface {
	/**  */
	String expressionX;
	/**  */
	String expressionY;
	/**  */
	String expressionZ;
	/**  */
	StaticVariableSet<Double> variables;
	/**  */
	String varU, varV;
	/**  */
	boolean is3D;
	/**  */
	double lowerU, upperU;
	/**  */
	double lowerV, upperV;
	/**  */
	double h;
	/**  */
	DoubleEvaluator eval;

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param equationZ
	 * @param u
	 * @param v
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY, final String equationZ, final String u,
			final String v) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<>();
		is3D = true;
		lowerU = Double.NEGATIVE_INFINITY;
		upperU = Double.POSITIVE_INFINITY;
		lowerV = Double.NEGATIVE_INFINITY;
		upperV = Double.POSITIVE_INFINITY;
		h = 0.00001;
	}

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param u
	 * @param v
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY, final String u, final String v) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<>();
		expressionZ = null;
		is3D = false;
		lowerU = Double.NEGATIVE_INFINITY;
		upperU = Double.POSITIVE_INFINITY;
		h = 0.00001;
	}

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param equationZ
	 * @param u
	 * @param minU
	 * @param maxU
	 * @param v
	 * @param minV
	 * @param maxV
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY, final String equationZ, final String u,
			final double minU, final double maxU, final String v, final double minV, final double maxV) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<>();
		is3D = true;
		lowerU = minU;
		upperU = maxU;
		lowerV = minV;
		upperV = maxV;
		h = 0.00001;
	}

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param u
	 * @param minU
	 * @param maxU
	 * @param v
	 * @param minV
	 * @param maxV
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY, final String u, final double minU,
			final double maxU, final String v, final double minV, final double maxV) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<>();
		expressionZ = null;
		is3D = false;
		lowerU = minU;
		upperU = maxU;
		lowerV = minV;
		upperV = maxV;
		h = 0.00001;
	}

	/**
	 *
	 *
	 * @param valU
	 * @param valV
	 * @return
	 */
	private WB_Point evaluate2D(final double valU, final double valV) {
		variables.set(varU, valU);
		variables.set(varV, valV);
		final double x = eval.evaluate(expressionX, variables);
		final double y = eval.evaluate(expressionY, variables);
		return new WB_Point(x, y, 0);
	}

	/**
	 *
	 *
	 * @param valU
	 * @param valV
	 * @return
	 */
	private WB_Point evaluate3D(final double valU, final double valV) {
		variables.set(varU, valU);
		variables.set(varV, valV);
		final double x = eval.evaluate(expressionX, variables);
		final double y = eval.evaluate(expressionY, variables);
		final double z = eval.evaluate(expressionZ, variables);
		return new WB_Point(x, y, z);
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	@Override
	public WB_Point surfacePoint(final double u, final double v) {
		if ((u < lowerU) || (u > upperU) || (v < lowerV) || (v > upperV)) {
			return null;
		}
		if (is3D) {
			return evaluate3D(u, v);
		}
		return evaluate2D(u, v);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLowerU() {
		return lowerU;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getUpperU() {
		return upperU;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getLowerV() {
		return lowerV;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double getUpperV() {
		return upperV;
	}
}
