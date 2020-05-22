package wblut.math;

/**
 *
 */
public class WB_FactorScalarParameter implements WB_ScalarParameter {
	/**  */
	private final double factor;
	/**  */
	private final WB_ScalarParameter parameter;

	/**
	 *
	 *
	 * @param factor
	 * @param parameter
	 */
	public WB_FactorScalarParameter(final double factor, final WB_ScalarParameter parameter) {
		this.factor = factor;
		this.parameter = parameter;
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public double evaluate(final double... x) {
		return factor * parameter.evaluate(x);
	}
}
