/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

public class WB_Horner {

	/**
	 * 
	 *
	 * @param a
	 * @param u
	 * @return
	 */
	public static double Horner(final double[] a, final double u) {
		final int n = a.length - 1;
		double result = a[n];
		for (int i = n - 1; i >= 0; i--) {
			result = result * u + a[i];
		}
		return result;
	}
}
