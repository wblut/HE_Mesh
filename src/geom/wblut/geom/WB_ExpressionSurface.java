package wblut.geom;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class WB_ExpressionSurface implements WB_Surface {
	String						expressionX;
	String						expressionY;
	String						expressionZ;
	StaticVariableSet<Double>	variables;
	String						varU, varV;
	boolean						is3D;
	double						lowerU, upperU;
	double						lowerV, upperV;
	double						h;
	DoubleEvaluator				eval;

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param equationZ
	 * @param var
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY,
			final String equationZ, final String u, String v) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<Double>();
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
	 * @param var
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY,
			final String u, String v) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<Double>();
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
	 * @param var
	 * @param minU
	 * @param maxU
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY,
			final String equationZ, final String u, final double minU,
			final double maxU, final String v, final double minV,
			final double maxV) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<Double>();
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
	 * @param var
	 * @param minU
	 * @param maxU
	 */
	public WB_ExpressionSurface(final String equationX, final String equationY,
			final String u, final double minU, final double maxU,
			final String v, final double minV, final double maxV) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.varU = u;
		this.varV = v;
		variables = new StaticVariableSet<Double>();
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
	 * @param value
	 * @return
	 */
	private WB_Point evaluate2D(final double valU, double valV) {
		variables.set(varU, valU);
		variables.set(varV, valV);
		double x = eval.evaluate(expressionX, variables);
		double y = eval.evaluate(expressionY, variables);
		return new WB_Point(x, y, 0);
	}

	/**
	 *
	 *
	 * @param value
	 * @return
	 */
	private WB_Point evaluate3D(final double valU, double valV) {
		variables.set(varU, valU);
		variables.set(varV, valV);
		double x = eval.evaluate(expressionX, variables);
		double y = eval.evaluate(expressionY, variables);
		double z = eval.evaluate(expressionZ, variables);
		return new WB_Point(x, y, z);
	}

	@Override
	public WB_Point surfacePoint(final double u, double v) {
		if ((u < lowerU) || (u > upperU) || (v < lowerV) || (v > upperV)) {
			return null;
		}
		if (is3D) {
			return evaluate3D(u, v);
		}
		return evaluate2D(u, v);
	}

	@Override
	public double getLowerU() {
		return lowerU;
	}

	@Override
	public double getUpperU() {
		return upperU;
	}

	@Override
	public double getLowerV() {
		return lowerV;
	}

	@Override
	public double getUpperV() {
		return upperV;
	}
}
