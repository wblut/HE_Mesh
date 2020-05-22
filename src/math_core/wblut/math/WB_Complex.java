package wblut.math;

/**
 *
 */
public class WB_Complex {
	/**  */
	private double re;
	/**  */
	private double im;

	/**
	 *
	 *
	 * @param r
	 * @param i
	 */
	public WB_Complex(final double r, final double i) {
		re = r;
		im = i;
	}

	/**
	 *
	 */
	public WB_Complex() {
		re = 0.0;
		im = 0.0;
	}

	/**
	 *
	 *
	 * @param r
	 * @param i
	 */
	private void set(final double r, final double i) {
		re = r;
		im = i;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex get() {
		return new WB_Complex(re, im);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double re() {
		return re;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double im() {
		return im;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double theta() {
		final double r = mod();
		double theta = 0.0;
		if (r != 0.0) {
			theta = Math.atan(im / re);
		}
		if (re < 0.0) {
			theta += Math.PI;
		}
		return theta;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double mod() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double r() {
		return mod();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double mag() {
		return re * re + im * im;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex conjugate() {
		return new WB_Complex(re, -im);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex conjugateSelf() {
		set(re, -im);
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex add(final WB_Complex c) {
		final double newr = re + c.re();
		final double newi = im + c.im();
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex addSelf(final WB_Complex c) {
		re += c.re();
		im += c.im();
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex sub(final WB_Complex c) {
		final double newr = re - c.re();
		final double newi = im - c.im();
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex subSelf(final WB_Complex c) {
		re -= c.re();
		im -= c.im();
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex mul(final WB_Complex c) {
		final double newr = re * c.re() - im * c.im();
		final double newi = re * c.im() + im * c.re();
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex mulSelf(final WB_Complex c) {
		set(re * c.re() - im * c.im(), re * c.im() + im * c.re());
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public WB_Complex mul(final double d) {
		final double newr = re * d;
		final double newi = im * d;
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public WB_Complex mulSelf(final double d) {
		re *= d;
		im *= d;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex inverse() {
		final double denominator = re * re + im * im;
		if (denominator == 0) {
			return null;
		}
		final double newr = re / denominator;
		final double newi = -im / denominator;
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex inverseSelf() {
		final double denominator = re * re + im * im;
		if (denominator == 0) {
			return null;
		}
		set(re / denominator, -im / denominator);
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex div(final WB_Complex c) {
		return mul(c.inverse());
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public WB_Complex divSelf(final WB_Complex c) {
		return mulSelf(c.inverse());
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex negate() {
		final double newr = -re;
		final double newi = -im;
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex negateSelf() {
		re *= -1;
		im *= -1;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex positive() {
		final double newr = Math.abs(re);
		final double newi = Math.abs(im);
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex positiveSelf() {
		set(Math.abs(re), Math.abs(im));
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex sqrt() {
		final double newTheta = theta() / 2.0;
		final double newMod = Math.sqrt(r());
		final double newr = newMod * Math.cos(newTheta);
		final double newi = newMod * Math.sin(newTheta);
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex sin() {
		final double newr = Math.sin(re) * cosh(im);
		final double newi = sinh(im) * Math.cos(re);
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex cos() {
		final double newr = Math.cos(re) * cosh(im);
		final double newi = -Math.sin(re) * sinh(im);
		return new WB_Complex(newr, newi);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex tan() {
		return sin().div(cos());
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	private static double sinh(final double a) {
		return 0.5 * (Math.exp(a) - Math.exp(-a));
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	private static double cosh(final double a) {
		return 0.5 * (Math.exp(a) + Math.exp(-a));
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex sinh() {
		return new WB_Complex(sinh(re) * Math.cos(im), cosh(re) * Math.sin(im));
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex cosh() {
		return new WB_Complex(cosh(re) * Math.cos(im), sinh(re) * Math.sin(im));
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Complex tanh() {
		final double denominator = cosh(2.0 * re) + Math.cos(2.0 * im);
		return new WB_Complex(sinh(2.0 * re) / denominator, Math.sin(2.0 * im) / denominator);
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public boolean equals(final WB_Complex c) {
		return re == c.re() && im == c.im();
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isZero() {
		return re == 0.0 && im == 0.0;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "Complex: " + String.valueOf(re) + ") + (" + String.valueOf(im) + ")i";
	}
}
