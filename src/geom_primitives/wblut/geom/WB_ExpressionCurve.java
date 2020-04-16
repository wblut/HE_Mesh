package wblut.geom;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class WB_ExpressionCurve implements WB_Curve {
	String expressionX;
	String expressionY;
	String expressionZ;
	StaticVariableSet<Double> variables;
	String var;
	boolean is3D;
	double lowerU, upperU;
	double h;
	DoubleEvaluator eval;

	public WB_ExpressionCurve(final String equationX, final String equationY, final String equationZ,
			final String var) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.var = var;
		variables = new StaticVariableSet<>();
		is3D = true;
		lowerU = Double.NEGATIVE_INFINITY;
		upperU = Double.POSITIVE_INFINITY;
		h = 0.00001;
	}

	public WB_ExpressionCurve(final String equationX, final String equationY, final String var) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.var = var;
		variables = new StaticVariableSet<>();
		expressionZ = null;
		is3D = false;
		lowerU = Double.NEGATIVE_INFINITY;
		upperU = Double.POSITIVE_INFINITY;
		h = 0.00001;
	}

	public WB_ExpressionCurve(final String equationX, final String equationY, final String equationZ, final String var,
			final double minU, final double maxU) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.var = var;
		variables = new StaticVariableSet<>();
		is3D = true;
		lowerU = minU;
		upperU = maxU;
		h = 0.00001;
	}

	public WB_ExpressionCurve(final String equationX, final String equationY, final String var, final double minU,
			final double maxU) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.var = var;
		variables = new StaticVariableSet<>();
		expressionZ = null;
		is3D = false;
		lowerU = minU;
		upperU = maxU;
		h = 0.00001;
	}

	private WB_Point evaluate2D(final double value) {
		variables.set(var, value);
		final double x = eval.evaluate(expressionX, variables);
		final double y = eval.evaluate(expressionY, variables);
		return new WB_Point(x, y, 0);
	}

	private WB_Point evaluate3D(final double value) {
		variables.set(var, value);
		final double x = eval.evaluate(expressionX, variables);
		final double y = eval.evaluate(expressionY, variables);
		final double z = eval.evaluate(expressionZ, variables);
		return new WB_Point(x, y, z);
	}

	@Override
	public WB_Point getPointOnCurve(final double u) {
		if ((u < lowerU) || (u > upperU)) {
			return null;
		}
		if (is3D) {
			return evaluate3D(u);
		}
		return evaluate2D(u);
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
	public WB_Vector getDirectionOnCurve(double u) {
		if ((u < lowerU) || (u > upperU)) {
			return null;
		}
		if (u > (upperU - h)) {
			u = upperU - h;
		}
		WB_Vector v;
		if (u > (upperU - h)) {
			v = new WB_Vector(getPointOnCurve(u - h), getPointOnCurve(u));
			v.normalizeSelf();
		} else if (u < (lowerU + h)) {
			v = new WB_Vector(getPointOnCurve(u), getPointOnCurve(u + h));
			v.normalizeSelf();
		} else {
			v = new WB_Vector(getPointOnCurve(u - h), getPointOnCurve(u + h));
			v.normalizeSelf();
		}
		return v;
	}

	@Override
	public WB_Vector getDerivative(final double u) {
		if ((u < lowerU) || (u > upperU)) {
			return null;
		}
		WB_Vector v;
		if (u > (upperU - h)) {
			v = new WB_Vector(getPointOnCurve(u - h), getPointOnCurve(u));
			v.div(h);
		} else if (u < (lowerU + h)) {
			v = new WB_Vector(getPointOnCurve(u), getPointOnCurve(u + h));
			v.div(h);
		} else {
			v = new WB_Vector(getPointOnCurve(u - h), getPointOnCurve(u + h));
			v.div(2 * h);
		}
		return v;
	}
}
