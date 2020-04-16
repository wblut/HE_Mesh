package wblut.math;

public class WB_ConstantScalarParameter implements WB_ScalarParameter {
	double value;

	public WB_ConstantScalarParameter(final double value) {
		this.value = value;
	}

	@Override
	public double evaluate(final double... x) {
		return value;
	}
}
