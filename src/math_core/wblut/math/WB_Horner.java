package wblut.math;

public class WB_Horner {
	public static double Horner(final double[] a, final double u) {
		final int n = a.length - 1;
		double result = a[n];
		for (int i = n - 1; i >= 0; i--) {
			result = result * u + a[i];
		}
		return result;
	}
}
