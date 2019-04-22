/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 * This work is published from Belgium.
 * (http://creativecommons.org/publicdomain/zero/1.0/)
 */
package wblut.geom;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

public class WB_ExpressionCurve implements WB_Curve {
	String						expressionX;
	String						expressionY;
	String						expressionZ;
	StaticVariableSet<Double>	variables;
	String						var;
	boolean						is3D;
	double						lowerU, upperU;
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
	public WB_ExpressionCurve(final String equationX, final String equationY,
			final String equationZ, final String var) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.var = var;
		variables = new StaticVariableSet<Double>();
		is3D = true;
		lowerU = Double.NEGATIVE_INFINITY;
		upperU = Double.POSITIVE_INFINITY;
		h = 0.00001;
	}

	/**
	 *
	 *
	 * @param equationX
	 * @param equationY
	 * @param var
	 */
	public WB_ExpressionCurve(final String equationX, final String equationY,
			final String var) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.var = var;
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
	public WB_ExpressionCurve(final String equationX, final String equationY,
			final String equationZ, final String var, final double minU,
			final double maxU) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		expressionZ = equationZ;
		this.var = var;
		variables = new StaticVariableSet<Double>();
		is3D = true;
		lowerU = minU;
		upperU = maxU;
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
	public WB_ExpressionCurve(final String equationX, final String equationY,
			final String var, final double minU, final double maxU) {
		eval = new DoubleEvaluator();
		expressionX = equationX;
		expressionY = equationY;
		this.var = var;
		variables = new StaticVariableSet<Double>();
		expressionZ = null;
		is3D = false;
		lowerU = minU;
		upperU = maxU;
		h = 0.00001;
	}

	/**
	 *
	 *
	 * @param value
	 * @return
	 */
	private WB_Point evaluate2D(final double value) {
		variables.set(var, value);
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
	private WB_Point evaluate3D(final double value) {
		variables.set(var, value);
		double x = eval.evaluate(expressionX, variables);
		double y = eval.evaluate(expressionY, variables);
		double z = eval.evaluate(expressionZ, variables);
		return new WB_Point(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curvePoint(double)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#getLowerU()
	 */
	@Override
	public double getLowerU() {
		return lowerU;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#getUpperU()
	 */
	@Override
	public double getUpperU() {
		return upperU;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curveDirection(double)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Curve#curveDerivative(double)
	 */
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
