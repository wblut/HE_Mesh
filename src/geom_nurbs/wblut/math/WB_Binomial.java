package wblut.math;

/**
 *
 */
public class WB_Binomial {
	/**  */
	private final long[][] binomial;

	/**
	 *
	 *
	 * @param N
	 * @param K
	 */
	public WB_Binomial(final int N, final int K) {
		binomial = new long[N + 1][K + 1];
		// base cases
		for (int k = 1; k <= K; k++) {
			binomial[0][k] = 0;
		}
		for (int n = 0; n <= N; n++) {
			binomial[n][0] = 1;
		}
		for (int n = 1; n <= N; n++) {
			for (int k = 1; k <= K; k++) {
				binomial[n][k] = binomial[n - 1][k - 1] + binomial[n - 1][k];
			}
		}
	}

	/**
	 *
	 *
	 * @param n
	 * @param k
	 * @return
	 */
	public long binPrecalc(final int n, final int k) {
		return binomial[n][k];
	}

	/**
	 *
	 *
	 * @param N
	 * @param K
	 * @return
	 */
	public static long bin(final int N, final int K) {
		final long[][] binomial = new long[N + 1][K + 1];
		for (int k = 1; k <= K; k++) {
			binomial[0][k] = 0;
		}
		for (int n = 0; n <= N; n++) {
			binomial[n][0] = 1;
		}
		for (int n = 1; n <= N; n++) {
			for (int k = 1; k <= K; k++) {
				binomial[n][k] = binomial[n - 1][k - 1] + binomial[n - 1][k];
			}
		}
		return binomial[N][K];
	}
}
