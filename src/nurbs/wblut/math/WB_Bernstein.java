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
 *
 */
public class WB_Bernstein {
	/**
	 * Calculate the (n+1) coefficients of the Bernstein polynomial of order n,
	 * for the value u (0..1)
	 *
	 * @param u
	 *            value at which to evaluate the Bernstein coefficients
	 * @param order
	 *            order of the Bernstein polynomial
	 * @return n+1 coefficients at value u
	 */
	public static double[] getBernsteinCoefficientsOfOrderN(final double u, final int order) {
		final double[] B = new double[order + 1];
		B[0] = 1.0;
		final double u1 = 1.0 - u;
		double saved, temp;
		for (int j = 1; j <= order; j++) {
			saved = 0.0;
			for (int k = 0; k < j; k++) {
				temp = B[k];
				B[k] = saved + u1 * temp;
				saved = u * temp;
			}
			B[j] = saved;
		}
		return B;
	}

	/**
	 * Calculate the (n+1) coefficients of the Bernstein polynomial of order n,
	 * for the value u ranging from (0..1) in the number of steps requested.
	 *
	 * @param steps
	 *            values at which to evaluate the Bernstein coefficients,
	 *            minimal 2, first and last value are calculated at 0 and 1, the
	 *            other are calculated equidistant.
	 * @param order
	 *            order of the Bernstein polynomial
	 * @return steps * n+1 coefficients, first index is the step, second index
	 *         is the coefficient
	 */
	public static double[][] getBernsteinCoefficientsOfOrderNForRange(final int steps, final int order) {
		final int clampedsteps = Math.max(2, steps);
		final double[][] B = new double[clampedsteps][order + 1];
		double u, u1, saved, temp;
		for (int i = 0; i < clampedsteps; i++) {
			B[i][0] = 1.0;
			u = i == clampedsteps - 1 ? 1.0 : i * (1.0 / (clampedsteps - 1.0));
			u1 = 1.0 - u;
			for (int j = 1; j <= order; j++) {
				saved = 0.0;
				for (int k = 0; k < j; k++) {
					temp = B[i][k];
					B[i][k] = saved + u1 * temp;
					saved = u * temp;
				}
				B[i][j] = saved;
			}
		}
		return B;
	}
}
