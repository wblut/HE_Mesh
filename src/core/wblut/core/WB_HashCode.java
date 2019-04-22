/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.core;

/**
 * @author FVH
 *
 */
public class WB_HashCode {

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static int calculateHashCode(final double x, final double y) {
		int result = 17;
		final long a = Double.doubleToLongBits(x);
		result += 31 * result + (int) (a ^ a >>> 32);
		final long b = Double.doubleToLongBits(y);
		result += 31 * result + (int) (b ^ b >>> 32);
		return result;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int calculateHashCode(final double x, final double y, final double z) {
		int result = 17;
		final long a = Double.doubleToLongBits(x);
		result += 31 * result + (int) (a ^ a >>> 32);
		final long b = Double.doubleToLongBits(y);
		result += 31 * result + (int) (b ^ b >>> 32);
		final long c = Double.doubleToLongBits(z);
		result += 31 * result + (int) (c ^ c >>> 32);
		return result;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return
	 */
	public static int calculateHashCode(final double x, final double y, final double z, final double w) {
		int result = 17;
		final long a = Double.doubleToLongBits(x);
		result += 31 * result + (int) (a ^ a >>> 32);
		final long b = Double.doubleToLongBits(y);
		result += 31 * result + (int) (b ^ b >>> 32);
		final long c = Double.doubleToLongBits(z);
		result += 31 * result + (int) (c ^ c >>> 32);
		final long d = Double.doubleToLongBits(w);
		result += 31 * result + (int) (d ^ d >>> 32);
		return result;
	}

}
