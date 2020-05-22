package wblut.math;

/**
 *
 */
public class WB_ConstantScalarParameter implements WB_ScalarParameter {
	/**  */
	double value;

	/**
	 *
	 *
	 * @param value
	 */
	public WB_ConstantScalarParameter(final double value) {
		this.value = value;
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public double evaluate(final double... x) {
		return value;
	}
}
