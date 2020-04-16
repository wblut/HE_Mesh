package wblut.math;

public class WB_Bernstein {
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
