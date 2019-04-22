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
 * WB_Binomial.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 *         Calculates binomial coefficients.
 */
public class WB_Binomial {
	/** The binomial. */
	private final long[][] binomial;

	/**
	 * An instance of WB_Binomial stores a N by K matrix of binomial
	 * coefficients.
	 *
	 * n! / (n-k)! k!
	 *
	 * This is efficient if the same coefficients are need over and over. The
	 * precalculated coefficient are retrieved with binPreCalc(). If only a few
	 * coefficients are needed, the static function bin() can be more efficient.
	 * It can be directly called as WB_Binomial.bin(n,k)
	 *
	 * @param N
	 *            n-value
	 * @param K
	 *            k-value
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
	 * Retrieve the precalculated binomial coefficient from an instance of
	 * WB_Binomial.
	 *
	 * @param n
	 *            n-value, <=N of WB_Binomial instance
	 * @param k
	 *            k-value, <=K of WB_Binomial instance
	 * @return binomial coefficient as long
	 */
	public long binPrecalc(final int n, final int k) {
		return binomial[n][k];
	}

	/**
	 * Retrieve arbitrary binomial coefficient.
	 *
	 * @param N
	 *            n-value
	 * @param K
	 *            k-value
	 * @return binomial coefficient as long
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
