/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Vector;

/**
 * A collection of fast and approximated math functions. Not as robust as the
 * JAVA implementations when dealing with special values (NaN etc). Some
 * functions only give an approximation.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_Math {
	/**
	 * Fast absolute value.
	 *
	 * @param x
	 *            the x
	 * @return abs(x)
	 */
	public static double fastAbs(final double x) {
		return x > 0 ? x : -x;
	}

	/**
	 * Fast max.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return max(x,y)
	 */
	public static double max(final double x, final double y) {
		return y > x ? y : x;
	}

	/**
	 * Fast min.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return min(x,y)
	 */
	public static double min(final double x, final double y) {
		return y < x ? y : x;
	}

	/**
	 * Fast max.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return max(x,y)
	 */
	public static float max(final float x, final float y) {
		return y > x ? y : x;
	}

	/**
	 * Fast min.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return min(x,y)
	 */
	public static float min(final float x, final float y) {
		return y < x ? y : x;
	}

	/**
	 * Fast max.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return max(x,y)
	 */
	public static int max(final int x, final int y) {
		return y > x ? y : x;
	}

	/**
	 * Fast min.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return min(x,y)
	 */
	public static int min(final int x, final int y) {
		return y < x ? y : x;
	}

	/**
	 * Maximum of three values.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @return max(x,y,z)
	 */
	public static double max(final double x, final double y, final double z) {
		return y > x ? z > y ? z : y : z > x ? z : x;
	}

	/**
	 * Minimum of three values.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @return min(x,y,z)
	 */
	public static double min(final double x, final double y, final double z) {
		return y < x ? z < y ? z : y : z < x ? z : x;
	}

	public static float max(final float x, final float y, final float z) {
		return y > x ? z > y ? z : y : z > x ? z : x;
	}

	public static float min(final float x, final float y, final float z) {
		return y < x ? z < y ? z : y : z < x ? z : x;
	}

	public static int max(final int x, final int y, final int z) {
		return y > x ? z > y ? z : y : z > x ? z : x;
	}

	public static int min(final int x, final int y, final int z) {
		return y < x ? z < y ? z : y : z < x ? z : x;
	}

	public static final int max(final int... numbers) {
		int maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	public static final int min(final int... numbers) {
		int minValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < minValue) {
				minValue = numbers[i];
			}
		}
		return minValue;
	}

	public static final float max(final float... numbers) {
		float maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	public static final float min(final float... numbers) {
		float minValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < minValue) {
				minValue = numbers[i];
			}
		}
		return minValue;
	}

	public static final double max(final double... numbers) {
		double maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	public static final double min(final double... numbers) {
		double minValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < minValue) {
				minValue = numbers[i];
			}
		}
		return minValue;
	}

	/**
	 * Largest integer smaller than value.
	 *
	 * @param x
	 *            the x
	 * @return result
	 */
	public static final int floor(final float x) {
		return x >= 0 ? (int) x : (int) x - 1;
	}

	/**
	 * Fast log2 approximation for floats.
	 *
	 * @param i
	 *            the i
	 * @return result
	 */
	public static final float fastLog2(final float i) {
		float x = Float.floatToRawIntBits(i);
		x *= 1.0f / (1 << 23);
		x -= 127;
		float y = x - floor(x);
		y = (y - y * y) * 0.346607f;
		return x + y;
	}

	/**
	 * Fast square power approximation for floats.
	 *
	 * @param i
	 *            the i
	 * @return result
	 */
	public static final float fastPow2(final float i) {
		float x = i - floor(i);
		x = (x - x * x) * 0.33971f;
		return Float.intBitsToFloat((int) ((i + 127 - x) * (1 << 23)));
	}

	/**
	 * Fast power approximation for floats.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            exponent
	 * @return result
	 */
	public static final float fastPow(final float a, final float b) {
		return fastPow2(b * fastLog2(a));
	}

	/**
	 * Fast inverse sqrt approximation for floats.
	 *
	 * @param x
	 *            the x
	 * @return result
	 */
	public static final float fastInvSqrt(float x) {
		final float half = 0.5F * x;
		int i = Float.floatToIntBits(x);
		i = 0x5f375a86 - (i >> 1);
		x = Float.intBitsToFloat(i);
		return x * (1.5F - half * x * x);
	}

	/**
	 * Fast sqrt approximation for floats.
	 *
	 * @param x
	 *            the x
	 * @return result
	 */
	public static final float fastSqrt(final float x) {
		return 1f / fastInvSqrt(x);
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static int getExp(final double v) {
		if (v == 0) {
			return 0;
		}
		return (int) ((0x7ff0000000000000L & Double.doubleToLongBits(v)) >> 52) - 1022;
	}

	/**
	 * sqrt(a^2 + b^2) without under/overflow. *
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static double hypot(final double a, final double b) {
		double r;
		if (Math.abs(a) > Math.abs(b)) {
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
		} else if (b != 0) {
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
		} else {
			r = 0.0;
		}
		return r;
	}

	/**
	 * Convenience method to compute the log base 2 of a value.
	 *
	 * @param value
	 *            the value to take the log of.
	 *
	 * @return the log base 2 of the specified value.
	 */
	public static double logBase2(final double value) {
		return Math.log(value) / Math.log(2d);
	}

	/**
	 * Convenience method for testing whether a value is a power of two.
	 *
	 * @param value
	 *            the value to test for power of 2
	 *
	 * @return true if power of 2, else false
	 */
	public static boolean isPowerOfTwo(final int value) {
		return value == powerOfTwoCeiling(value);
	}

	/**
	 * Returns the value that is the nearest power of 2 greater than or equal to
	 * the given value.
	 *
	 * @param reference
	 *            the reference value. The power of 2 returned is greater than
	 *            or equal to this value.
	 *
	 * @return the value that is the nearest power of 2 greater than or equal to
	 *         the reference value
	 */
	public static int powerOfTwoCeiling(final int reference) {
		final int power = (int) Math.ceil(Math.log(reference) / Math.log(2d));
		return (int) Math.pow(2d, power);
	}

	/**
	 * Returns the value that is the nearest power of 2 less than or equal to
	 * the given value.
	 *
	 * @param reference
	 *            the reference value. The power of 2 returned is less than or
	 *            equal to this value.
	 *
	 * @return the value that is the nearest power of 2 less than or equal to
	 *         the reference value
	 */
	public static int powerOfTwoFloor(final int reference) {
		final int power = (int) Math.floor(Math.log(reference) / Math.log(2d));
		return (int) Math.pow(2d, power);
	}

	/**
	 * Clamps a value to a given range.
	 *
	 * @param v
	 *            the value to clamp.
	 * @param min
	 *            the floor.
	 * @param max
	 *            the ceiling
	 *
	 * @return the nearest value such that min <= v <= max.
	 */
	public static double clamp(final double v, final double min, final double max) {
		return v < min ? min : v > max ? max : v;
	}

	/**
	 * Clamps an integer value to a given range.
	 *
	 * @param v
	 *            the value to clamp.
	 * @param min
	 *            the floor.
	 * @param max
	 *            the ceiling
	 *
	 * @return the nearest value such that min <= v <= max.
	 */
	public static int clamp(final int v, final int min, final int max) {
		return v < min ? min : v > max ? max : v;
	}

	final static double DEGTORAD = Math.PI / 180.0;

	public static double radians(final double degrees) {
		return degrees * DEGTORAD;
	}

	final static double RADTODEG = 180.0 / Math.PI;

	public static double degrees(final double radians) {
		return radians * RADTODEG;
	}

	public static WB_Coord abs(final WB_Coord v) {
		return new WB_Vector(Math.abs(v.xd()), Math.abs(v.yd()), Math.abs(v.zd()));
	}

	public static WB_Coord sign(final WB_Coord v) {
		return new WB_Vector(Math.signum(v.xd()), Math.signum(v.yd()), Math.signum(v.zd()));
	}

	public static WB_Coord floor(final WB_Coord v) {
		return new WB_Vector(Math.floor(v.xd()), Math.floor(v.yd()), Math.floor(v.zd()));
	}

	public static WB_Coord ceiling(final WB_Coord v) {
		return new WB_Vector(Math.ceil(v.xd()), Math.ceil(v.yd()), Math.ceil(v.zd()));
	}

	public static double fract(final double x) {
		return x - Math.floor(x);
	}

	public static WB_Coord fract(final WB_Coord v) {
		return new WB_Vector(fract(v.xd()), fract(v.yd()), fract(v.zd()));
	}

	public static WB_Coord mod(final WB_Coord u, final WB_Coord v) {
		return new WB_Vector(u.xd() % v.xd(), u.yd() % v.yd(), u.zd() % v.zd());
	}

	public static WB_Coord mod(final WB_Coord u, final double v) {
		return new WB_Vector(u.xd() % v, u.yd() % v, u.zd() % v);
	}

	public static WB_Coord min(final WB_Coord u, final WB_Coord v) {
		return new WB_Vector(min(u.xd(), v.xd()), min(u.yd(), v.yd()), min(u.zd(), v.zd()));
	}

	public static WB_Coord min(final WB_Coord u, final double v) {
		return new WB_Vector(min(u.xd(), v), min(u.yd(), v), min(u.zd(), v));
	}

	public static WB_Coord max(final WB_Coord u, final WB_Coord v) {
		return new WB_Vector(max(u.xd(), v.xd()), max(u.yd(), v.yd()), max(u.zd(), v.zd()));
	}

	public static WB_Coord max(final WB_Coord u, final double v) {
		return new WB_Vector(max(u.xd(), v), max(u.yd(), v), max(u.zd(), v));
	}

	public static WB_Coord clamp(final WB_Coord u, final WB_Coord min, final WB_Coord max) {
		return new WB_Vector(clamp(u.xd(), min.xd(), max.xd()), clamp(u.yd(), min.yd(), max.yd()),
				clamp(u.zd(), min.zd(), max.zd()));
	}

	public static WB_Coord clamp(final WB_Coord u, final double min, final double max) {
		return new WB_Vector(clamp(u.xd(), min, max), clamp(u.yd(), min, max), clamp(u.zd(), min, max));
	}

	public static double mix(final double x, final double y, final double a) {
		return (1.0 - a) * x + a * y;
	}

	public static WB_Coord mix(final WB_Coord u, final WB_Coord v, final double a) {
		return new WB_Vector(mix(u.xd(), v.xd(), a), mix(u.yd(), v.yd(), a), mix(u.zd(), v.zd(), a));
	}

	public static WB_Coord mix(final WB_Coord u, final WB_Coord v, final WB_Coord a) {
		return new WB_Vector(mix(u.xd(), v.xd(), a.xd()), mix(u.yd(), v.yd(), a.yd()), mix(u.zd(), v.zd(), a.zd()));
	}

	public static double step(final double edge, final double x) {
		return x < edge ? 0.0 : 1.0;
	}

	public static WB_Vector step(final double edge, final WB_Coord v) {
		return new WB_Vector(step(v.xd(), edge), step(v.yd(), edge), step(v.zd(), edge));
	}

	public static WB_Vector step(final WB_Coord edge, final WB_Coord v) {
		return new WB_Vector(step(v.xd(), edge.xd()), step(v.yd(), edge.yd()), step(v.zd(), edge.zd()));
	}

	public static double smoothstep(final double edge0, final double edge1, final double x) {
		double y = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
		return y * y * (3.0 - 2.0 * y);
	}

	public static WB_Vector smoothstep(final double edge0, final double edge1, final WB_Coord v) {
		return new WB_Vector(smoothstep(edge0, edge1, v.xd()), smoothstep(edge0, edge1, v.yd()),
				smoothstep(edge0, edge1, v.zd()));
	}

	public static WB_Vector smoothstep(final WB_Coord edge0, final WB_Coord edge1, final WB_Coord v) {
		return new WB_Vector(smoothstep(edge0.xd(), edge1.xd(), v.xd()), smoothstep(edge0.yd(), edge1.yd(), v.yd()),
				smoothstep(edge0.zd(), edge1.zd(), v.zd()));
	}

}
