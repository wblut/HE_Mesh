package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Vector;

/**
 *
 */
public class WB_Math {
	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public static double fastAbs(final double x) {
		return x > 0 ? x : -x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static double max(final double x, final double y) {
		return y > x ? y : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static double min(final double x, final double y) {
		return y < x ? y : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static float max(final float x, final float y) {
		return y > x ? y : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static float min(final float x, final float y) {
		return y < x ? y : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static int max(final int x, final int y) {
		return y > x ? y : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static int min(final int x, final int y) {
		return y < x ? y : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static double max(final double x, final double y, final double z) {
		return y > x ? z > y ? z : y : z > x ? z : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static double min(final double x, final double y, final double z) {
		return y < x ? z < y ? z : y : z < x ? z : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static float max(final float x, final float y, final float z) {
		return y > x ? z > y ? z : y : z > x ? z : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static float min(final float x, final float y, final float z) {
		return y < x ? z < y ? z : y : z < x ? z : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int max(final int x, final int y, final int z) {
		return y > x ? z > y ? z : y : z > x ? z : x;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int min(final int x, final int y, final int z) {
		return y < x ? z < y ? z : y : z < x ? z : x;
	}

	/**
	 *
	 *
	 * @param numbers
	 * @return
	 */
	public static final int max(final int... numbers) {
		int maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	/**
	 *
	 *
	 * @param numbers
	 * @return
	 */
	public static final int min(final int... numbers) {
		int minValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < minValue) {
				minValue = numbers[i];
			}
		}
		return minValue;
	}

	/**
	 *
	 *
	 * @param numbers
	 * @return
	 */
	public static final float max(final float... numbers) {
		float maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	/**
	 *
	 *
	 * @param numbers
	 * @return
	 */
	public static final float min(final float... numbers) {
		float minValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < minValue) {
				minValue = numbers[i];
			}
		}
		return minValue;
	}

	/**
	 *
	 *
	 * @param numbers
	 * @return
	 */
	public static final double max(final double... numbers) {
		double maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	/**
	 *
	 *
	 * @param numbers
	 * @return
	 */
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
	 *
	 *
	 * @param x
	 * @return
	 */
	public static final int floor(final float x) {
		return x >= 0 ? (int) x : (int) x - 1;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
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
	 *
	 *
	 * @param i
	 * @return
	 */
	public static final float fastPow2(final float i) {
		float x = i - floor(i);
		x = (x - x * x) * 0.33971f;
		return Float.intBitsToFloat((int) ((i + 127 - x) * (1 << 23)));
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static final float fastPow(final float a, final float b) {
		return fastPow2(b * fastLog2(a));
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public static final float fastInvSqrt(float x) {
		final float half = 0.5F * x;
		int i = Float.floatToIntBits(x);
		i = 0x5f375a86 - (i >> 1);
		x = Float.intBitsToFloat(i);
		return x * (1.5F - half * x * x);
	}

	/**
	 *
	 *
	 * @param x
	 * @return
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
	 *
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
	 *
	 *
	 * @param value
	 * @return
	 */
	public static double logBase2(final double value) {
		return Math.log(value) / Math.log(2d);
	}

	/**
	 *
	 *
	 * @param value
	 * @return
	 */
	public static boolean isPowerOfTwo(final int value) {
		return value == powerOfTwoCeiling(value);
	}

	/**
	 *
	 *
	 * @param reference
	 * @return
	 */
	public static int powerOfTwoCeiling(final int reference) {
		final int power = (int) Math.ceil(Math.log(reference) / Math.log(2d));
		return (int) Math.pow(2d, power);
	}

	/**
	 *
	 *
	 * @param reference
	 * @return
	 */
	public static int powerOfTwoFloor(final int reference) {
		final int power = (int) Math.floor(Math.log(reference) / Math.log(2d));
		return (int) Math.pow(2d, power);
	}

	/**
	 *
	 *
	 * @param v
	 * @param min
	 * @param max
	 * @return
	 */
	public static double clamp(final double v, final double min, final double max) {
		return v < min ? min : v > max ? max : v;
	}

	/**
	 *
	 *
	 * @param v
	 * @param min
	 * @param max
	 * @return
	 */
	public static int clamp(final int v, final int min, final int max) {
		return v < min ? min : v > max ? max : v;
	}

	/**  */
	final static double DEGTORAD = Math.PI / 180.0;

	/**
	 *
	 *
	 * @param degrees
	 * @return
	 */
	public static double radians(final double degrees) {
		return degrees * DEGTORAD;
	}

	/**  */
	final static double RADTODEG = 180.0 / Math.PI;

	/**
	 *
	 *
	 * @param radians
	 * @return
	 */
	public static double degrees(final double radians) {
		return radians * RADTODEG;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static WB_Coord abs(final WB_Coord v) {
		return new WB_Vector(Math.abs(v.xd()), Math.abs(v.yd()), Math.abs(v.zd()));
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static WB_Coord sign(final WB_Coord v) {
		return new WB_Vector(Math.signum(v.xd()), Math.signum(v.yd()), Math.signum(v.zd()));
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static WB_Coord floor(final WB_Coord v) {
		return new WB_Vector(Math.floor(v.xd()), Math.floor(v.yd()), Math.floor(v.zd()));
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static WB_Coord ceiling(final WB_Coord v) {
		return new WB_Vector(Math.ceil(v.xd()), Math.ceil(v.yd()), Math.ceil(v.zd()));
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public static double fract(final double x) {
		return x - Math.floor(x);
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public static WB_Coord fract(final WB_Coord v) {
		return new WB_Vector(fract(v.xd()), fract(v.yd()), fract(v.zd()));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_Coord mod(final WB_Coord u, final WB_Coord v) {
		return new WB_Vector(u.xd() % v.xd(), u.yd() % v.yd(), u.zd() % v.zd());
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_Coord mod(final WB_Coord u, final double v) {
		return new WB_Vector(u.xd() % v, u.yd() % v, u.zd() % v);
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_Coord min(final WB_Coord u, final WB_Coord v) {
		return new WB_Vector(min(u.xd(), v.xd()), min(u.yd(), v.yd()), min(u.zd(), v.zd()));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_Coord min(final WB_Coord u, final double v) {
		return new WB_Vector(min(u.xd(), v), min(u.yd(), v), min(u.zd(), v));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_Coord max(final WB_Coord u, final WB_Coord v) {
		return new WB_Vector(max(u.xd(), v.xd()), max(u.yd(), v.yd()), max(u.zd(), v.zd()));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	public static WB_Coord max(final WB_Coord u, final double v) {
		return new WB_Vector(max(u.xd(), v), max(u.yd(), v), max(u.zd(), v));
	}

	/**
	 *
	 *
	 * @param u
	 * @param min
	 * @param max
	 * @return
	 */
	public static WB_Coord clamp(final WB_Coord u, final WB_Coord min, final WB_Coord max) {
		return new WB_Vector(clamp(u.xd(), min.xd(), max.xd()), clamp(u.yd(), min.yd(), max.yd()),
				clamp(u.zd(), min.zd(), max.zd()));
	}

	/**
	 *
	 *
	 * @param u
	 * @param min
	 * @param max
	 * @return
	 */
	public static WB_Coord clamp(final WB_Coord u, final double min, final double max) {
		return new WB_Vector(clamp(u.xd(), min, max), clamp(u.yd(), min, max), clamp(u.zd(), min, max));
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param a
	 * @return
	 */
	public static double mix(final double x, final double y, final double a) {
		return (1.0 - a) * x + a * y;
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param a
	 * @return
	 */
	public static WB_Coord mix(final WB_Coord u, final WB_Coord v, final double a) {
		return new WB_Vector(mix(u.xd(), v.xd(), a), mix(u.yd(), v.yd(), a), mix(u.zd(), v.zd(), a));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param a
	 * @return
	 */
	public static WB_Coord mix(final WB_Coord u, final WB_Coord v, final WB_Coord a) {
		return new WB_Vector(mix(u.xd(), v.xd(), a.xd()), mix(u.yd(), v.yd(), a.yd()), mix(u.zd(), v.zd(), a.zd()));
	}

	/**
	 *
	 *
	 * @param edge
	 * @param x
	 * @return
	 */
	public static double step(final double edge, final double x) {
		return x < edge ? 0.0 : 1.0;
	}

	/**
	 *
	 *
	 * @param edge
	 * @param v
	 * @return
	 */
	public static WB_Vector step(final double edge, final WB_Coord v) {
		return new WB_Vector(step(v.xd(), edge), step(v.yd(), edge), step(v.zd(), edge));
	}

	/**
	 *
	 *
	 * @param edge
	 * @param v
	 * @return
	 */
	public static WB_Vector step(final WB_Coord edge, final WB_Coord v) {
		return new WB_Vector(step(v.xd(), edge.xd()), step(v.yd(), edge.yd()), step(v.zd(), edge.zd()));
	}

	/**
	 *
	 *
	 * @param edge0
	 * @param edge1
	 * @param x
	 * @return
	 */
	public static double smoothstep(final double edge0, final double edge1, final double x) {
		final double y = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
		return y * y * (3.0 - 2.0 * y);
	}

	/**
	 *
	 *
	 * @param edge0
	 * @param edge1
	 * @param v
	 * @return
	 */
	public static WB_Vector smoothstep(final double edge0, final double edge1, final WB_Coord v) {
		return new WB_Vector(smoothstep(edge0, edge1, v.xd()), smoothstep(edge0, edge1, v.yd()),
				smoothstep(edge0, edge1, v.zd()));
	}

	/**
	 *
	 *
	 * @param edge0
	 * @param edge1
	 * @param v
	 * @return
	 */
	public static WB_Vector smoothstep(final WB_Coord edge0, final WB_Coord edge1, final WB_Coord v) {
		return new WB_Vector(smoothstep(edge0.xd(), edge1.xd(), v.xd()), smoothstep(edge0.yd(), edge1.yd(), v.yd()),
				smoothstep(edge0.zd(), edge1.zd(), v.zd()));
	}
}
