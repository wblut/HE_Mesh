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
public class WB_Interpolate {
	private WB_Interpolate() {
	};

	/*
	 * Good interpolator when you have two values to interpolate between, but
	 * doesn't give fluid animation when more points are involved since it
	 * follows straight lines between the points.
	 */
	public static double linearInterpolate(final double v1, final double v2, final double alpha) {
		return v1 + alpha * (v2 - v1);
	}

	/*
	 * When you have more than 2 points two interpolate (for example following a
	 * path), this is a better choice than a linear interpolator.
	 */
	public static double cosineInterpolate(final double v1, final double v2, final double alpha) {
		double alpha2 = (1 - Math.cos(alpha * Math.PI)) / 2;
		return v1 + alpha2 * (v2 - v1);
	}

	/*
	 * Cubic interpolator. Gives better continuity along the spline than the
	 * cosine interpolator, however needs 4 points to interpolate.
	 */
	public static double cubicInterpolate(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		double alpha2 = alpha * alpha;
		double a0 = v3 - v2 - v0 + v1;
		double a1 = v0 - v1 - a0;
		double a2 = v2 - v0;
		double a3 = v1;
		return a0 * alpha * alpha2 + a1 * alpha2 + a2 * alpha + a3;
	}

	/*
	 * Hermite interpolator. Allows better control of the bends in the spline by
	 * providing two parameters to adjust them:
	 *
	 * tension: 1 for high tension, 0 for normal tension and -1 for low tension.
	 * bias: 1 for bias towards the next segment, 0 for even bias, -1 for bias
	 * towards the previous segment.
	 *
	 * Using 0 bias gives a cardinal spline with just tension, using both 0
	 * tension and 0 bias gives a Catmul-Rom spline.
	 */
	public static double hermiteInterpolate(final double v0, final double v1, final double v2, final double v3,
			final double alpha, final double tension, final double bias) {
		double alpha2 = alpha * alpha;
		double alpha3 = alpha2 * alpha;
		double m0 = (v1 - v0) * (1 - tension) * (1 + bias) / 2.0;
		m0 += (v2 - v1) * (1 - tension) * (1 - bias) / 2.0;
		double m1 = (v2 - v1) * (1 - tension) * (1 + bias) / 2.0;
		m1 += (v3 - v2) * (1 - tension) * (1 - bias) / 2.0;
		double a0 = 2 * alpha3 - 3 * alpha2 + 1;
		double a1 = alpha3 - 2 * alpha2 + alpha;
		double a2 = alpha3 - alpha2;
		double a3 = -2 * alpha3 + 3 * alpha2;
		return a0 * v1 + a1 * m0 + a2 * m1 + a3 * v2;
	}

	/*
	 * Kochanek-Bartels interpolator. Allows even better control of the bends in
	 * the spline by providing three parameters to adjust them:
	 *
	 * tension: 1 for high tension, 0 for normal tension and -1 for low tension.
	 * continuity: 1 for inverted corners, 0 for normal corners, -1 for box
	 * corners. bias: 1 for bias towards the next segment, 0 for even bias, -1
	 * for bias towards the previous segment.
	 *
	 * Using 0 continuity gives a hermite spline.
	 */
	public static double kochanekBartelsInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha, final double tension, final double continuity, final double bias) {
		double alpha2 = alpha * alpha;
		double alpha3 = alpha2 * alpha;
		double m0 = (v1 - v0) * (1 - tension) * (1 + continuity) * (1 + bias) / 2.0;
		m0 += (v2 - v1) * (1 - tension) * (1 - continuity) * (1 - bias) / 2.0;
		double m1 = (v2 - v1) * (1 - tension) * (1 - continuity) * (1 + bias) / 2.0;
		m1 += (v3 - v2) * (1 - tension) * (1 + continuity) * (1 - bias) / 2.0;
		double a0 = 2 * alpha3 - 3 * alpha2 + 1;
		double a1 = alpha3 - 2 * alpha2 + alpha;
		double a2 = alpha3 - alpha2;
		double a3 = -2 * alpha3 + 3 * alpha2;
		return a0 * v1 + a1 * m0 + a2 * m1 + a3 * v2;
	}

	/*
	 * Quadratic Bezier interpolator. v0 and v2 are begin and end point
	 * respectively, v1 is a control point.
	 */
	public static double quadraticBezierInterpolator(final double v0, final double v1, final double v2,
			final double alpha) {
		double alpha2 = alpha * alpha;
		return (v2 - 2 * v1 + v0) * alpha2 + (v1 - v0) * 2 * alpha + v0;
	}

	/*
	 * Cubic Bezier interpolator. v0 and v3 are begin and end point
	 * respectively, v1 and v2 are control points.
	 */
	public static double cubicBezierInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		double alpha2 = alpha * alpha;
		double alpha3 = alpha2 * alpha;
		return (v3 - 3 * v2 + 3 * v1 - v0) * alpha3 + (3 * v2 - 6 * v1 + 3 * v0) * alpha2 + (3 * v1 - 3 * v0) * alpha
				+ v0;
	}

	/*
	 * Quadratic b-spline interpolator. v0 and v2 are begin and end point
	 * respectively, v1 is a control point.
	 */
	public static double quadraticBSplineInterpolator(final double v0, final double v1, final double v2,
			final double alpha) {
		double alpha2 = alpha * alpha;
		return ((v2 - 2 * v1 + v0) * alpha2 + (v1 - v0) * 2 * alpha + v0 + v1) / 2.0;
	}

	/*
	 * Cubic b-spline interpolator. v0 and v3 are begin and end point
	 * respectively, v1 and v2 are control points.
	 */

	public static double cubicBSplineInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		double alpha2 = alpha * alpha;
		double alpha3 = alpha2 * alpha;
		return ((v3 - 3 * v2 + 3 * v1 - v0) * alpha3 + (3 * v2 - 6 * v1 + 3 * v0) * alpha2 + (3 * v2 - 3 * v0) * alpha
				+ v0 + 4 * v1 + v2) / 6.0;
	}

	/*
	 * Cubic Catmull Rom interpolator. v0 and v3 are begin and end point
	 * respectively, v1 and v2 are control points.
	 */
	public static double cubicCatmullRomInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		double alpha2 = alpha * alpha;
		double alpha3 = alpha2 * alpha;
		return ((v3 - 3 * v2 + 3 * v1 - v0) * alpha3 + (-v3 + 4 * v2 - 5 * v1 + 2 * v0) * alpha2 + (v2 - v0) * alpha
				+ 2 * v1 + v0) / 2.0;
	}

	/*
	 * Cubic hermite interpolator. v0 and v3 are begin and end point
	 * respectively, v1 and v2 are control points.
	 */
	public static double cubicHermiteInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		double alpha2 = alpha * alpha;
		double alpha3 = alpha2 * alpha;
		return (v3 - 2 * v2 + v1 + 2 * v0) * alpha3;

	}
}
