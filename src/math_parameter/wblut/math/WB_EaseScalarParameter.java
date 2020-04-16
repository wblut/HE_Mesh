package wblut.math;

public class WB_EaseScalarParameter implements WB_ScalarParameter {
	double lowT, highT;
	double lowValue, highValue;
	double rangeT, rangeValue;
	boolean clamp;
	WB_Ease ease;
	WB_Ease.EaseType type;

	public WB_EaseScalarParameter(final double lowT, final double highT, final double lowValue, final double highValue,
			final WB_Ease ease, final WB_Ease.EaseType type) {
		if (lowT <= highT) {
			this.lowT = lowT;
			this.highT = highT;
			this.lowValue = lowValue;
			this.highValue = highValue;
		} else {
			this.lowT = highT;
			this.highT = lowT;
			this.lowValue = highValue;
			this.highValue = lowValue;
		}
		rangeT = this.highT - this.lowT;
		rangeValue = this.highValue - this.lowValue;
		clamp = false;
		this.ease = ease;
		this.type = type;
	}

	public WB_EaseScalarParameter(final double lowT, final double highT, final double lowValue, final double highValue,
			final boolean clamp, final WB_Ease ease, final WB_Ease.EaseType type) {
		if (lowT <= highT) {
			this.lowT = lowT;
			this.highT = highT;
			this.lowValue = lowValue;
			this.highValue = highValue;
		} else {
			this.lowT = highT;
			this.highT = lowT;
			this.lowValue = highValue;
			this.highValue = lowValue;
		}
		rangeT = this.highT - this.lowT;
		rangeValue = this.highValue - this.lowValue;
		this.clamp = clamp;
		this.ease = ease;
		this.type = type;
	}

	public WB_EaseScalarParameter(final double lowT, final double highT, final double lowValue, final double highValue,
			final WB_Ease ease) {
		if (lowT <= highT) {
			this.lowT = lowT;
			this.highT = highT;
			this.lowValue = lowValue;
			this.highValue = highValue;
		} else {
			this.lowT = highT;
			this.highT = lowT;
			this.lowValue = highValue;
			this.highValue = lowValue;
		}
		rangeT = this.highT - this.lowT;
		rangeValue = this.highValue - this.lowValue;
		clamp = false;
		this.ease = ease;
		this.type = WB_Ease.EaseType.INOUT;
	}

	public WB_EaseScalarParameter(final double lowT, final double highT, final double lowValue, final double highValue,
			final boolean clamp, final WB_Ease ease) {
		if (lowT <= highT) {
			this.lowT = lowT;
			this.highT = highT;
			this.lowValue = lowValue;
			this.highValue = highValue;
		} else {
			this.lowT = highT;
			this.highT = lowT;
			this.lowValue = highValue;
			this.highValue = lowValue;
		}
		rangeT = this.highT - this.lowT;
		rangeValue = this.highValue - this.lowValue;
		this.clamp = clamp;
		this.ease = ease;
		this.type = WB_Ease.EaseType.INOUT;
	}

	@Override
	public double evaluate(final double... x) {
		if (rangeT == 0) {
			return lowValue;
		}
		if (clamp) {
			if (x[0] <= lowT) {
				return lowValue;
			}
			if (x[0] >= highT) {
				return highValue;
			}
		}
		final double t = (x[0] - lowT) / rangeT;
		return ease.ease(type, t) * rangeValue + lowValue;
	}
}
