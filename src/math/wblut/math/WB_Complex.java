/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

/**
 * @author FVH
 *
 */
public class WB_Complex {

	private double re;
	private double im;

	public WB_Complex(final double r, final double i) {
		re = r;
		im = i;
	}

	public WB_Complex() {
		re = 0.0;
		im = 0.0;
	}

	private void set(final double r, final double i) {
		re = r;
		im = i;
	}

	public WB_Complex get() {
		return new WB_Complex(re, im);
	}

	public double re() {
		return re;
	}

	public double im() {
		return im;
	}

	public double theta() {
		double r = mod();
		double theta = 0.0;
		if (r != 0.0) {
			theta = Math.atan(im / re);
		}

		if (re < 0.0) {
			theta += Math.PI;
		}
		return theta;
	}

	public double mod() {
		return Math.sqrt(re * re + im * im);
	}

	public double r() {
		return mod();
	}

	public double mag() {
		return re * re + im * im;
	}

	public WB_Complex conjugate() {
		return new WB_Complex(re, -im);
	}

	public WB_Complex conjugateSelf() {
		set(re, -im);
		return this;
	}

	public WB_Complex add(final WB_Complex c) {
		double newr = re + c.re();
		double newi = im + c.im();
		return new WB_Complex(newr, newi);
	}

	public WB_Complex addSelf(final WB_Complex c) {
		re += c.re();
		im += c.im();
		return this;
	}

	public WB_Complex sub(final WB_Complex c) {
		double newr = re - c.re();
		double newi = im - c.im();
		return new WB_Complex(newr, newi);
	}

	public WB_Complex subSelf(final WB_Complex c) {
		re -= c.re();
		im -= c.im();
		return this;
	}

	public WB_Complex mul(final WB_Complex c) {
		double newr = re * c.re() - im * c.im();
		double newi = re * c.im() + im * c.re();
		return new WB_Complex(newr, newi);
	}

	public WB_Complex mulSelf(final WB_Complex c) {
		set(re * c.re() - im * c.im(), re * c.im() + im * c.re());
		return this;
	}

	public WB_Complex mul(final double d) {
		double newr = re * d;
		double newi = im * d;
		return new WB_Complex(newr, newi);
	}

	public WB_Complex mulSelf(final double d) {
		re *= d;
		im *= d;
		return this;
	}

	public WB_Complex inverse() {
		double denominator = re * re + im * im;

		if (denominator == 0) {
			return null;
		}

		double newr = re / denominator;
		double newi = -im / denominator;

		return new WB_Complex(newr, newi);
	}

	public WB_Complex inverseSelf() {
		double denominator = re * re + im * im;

		if (denominator == 0) {
			return null;
		}

		set(re / denominator, -im / denominator);

		return this;
	}

	public WB_Complex div(final WB_Complex c) {
		return mul(c.inverse());
	}

	public WB_Complex divSelf(final WB_Complex c) {
		return mulSelf(c.inverse());
	}

	public WB_Complex negate() {
		double newr = -re;
		double newi = -im;
		return new WB_Complex(newr, newi);
	}

	public WB_Complex negateSelf() {
		re *= -1;
		im *= -1;
		return this;
	}

	public WB_Complex positive() {
		double newr = Math.abs(re);
		double newi = Math.abs(im);
		return new WB_Complex(newr, newi);
	}

	public WB_Complex positiveSelf() {
		set(Math.abs(re), Math.abs(im));
		return this;
	}

	public WB_Complex sqrt() {
		double newTheta = theta() / 2.0;
		double newMod = Math.sqrt(r());
		double newr = newMod * Math.cos(newTheta);
		double newi = newMod * Math.sin(newTheta);
		return new WB_Complex(newr, newi);
	}

	public WB_Complex sin() {
		double newr = Math.sin(re) * cosh(im);
		double newi = sinh(im) * Math.cos(re);
		return new WB_Complex(newr, newi);
	}

	public WB_Complex cos() {
		double newr = Math.cos(re) * cosh(im);
		double newi = -Math.sin(re) * sinh(im);
		return new WB_Complex(newr, newi);
	}

	public WB_Complex tan() {
		return sin().div(cos());
	}

	private static double sinh(final double a) {
		return 0.5 * (Math.exp(a) - Math.exp(-a));
	}

	private static double cosh(final double a) {
		return 0.5 * (Math.exp(a) + Math.exp(-a));
	}

	public WB_Complex sinh() {
		return new WB_Complex(sinh(re) * Math.cos(im), cosh(re) * Math.sin(im));
	}

	public WB_Complex cosh() {
		return new WB_Complex(cosh(re) * Math.cos(im), sinh(re) * Math.sin(im));
	}

	public WB_Complex tanh() {
		double denominator = cosh(2.0 * re) + Math.cos(2.0 * im);
		return new WB_Complex(sinh(2.0 * re) / denominator, Math.sin(2.0 * im) / denominator);
	}

	public boolean equals(final WB_Complex c) {
		return re == c.re() && im == c.im();
	}

	public boolean isZero() {
		return re == 0.0 && im == 0.0;
	}

	@Override
	public String toString() {
		return "Complex: " + String.valueOf(re) + ") + (" + String.valueOf(im) + ")i";
	}

}
