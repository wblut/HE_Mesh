package wblut.math;

/**
 *
 */
public interface WB_ScalarParameter {
	/**  */
	WB_ScalarParameter ZERO = new WB_ConstantScalarParameter(0.0);
	/**  */
	WB_ScalarParameter ONE = new WB_ConstantScalarParameter(1.0);

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	double evaluate(double... x);
}
