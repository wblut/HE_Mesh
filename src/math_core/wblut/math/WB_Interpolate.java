package wblut.math;

public class WB_Interpolate {
	private WB_Interpolate() {
	}

	public static double linearInterpolate(final double v1, final double v2, final double alpha) {
		return v1 + alpha * (v2 - v1);
	}

	public static double cosineInterpolate(final double v1, final double v2, final double alpha) {
		final double alpha2 = (1 - Math.cos(alpha * Math.PI)) / 2;
		return v1 + alpha2 * (v2 - v1);
	}

	public static double cubicInterpolate(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		final double a0 = v3 - v2 - v0 + v1;
		final double a1 = v0 - v1 - a0;
		final double a2 = v2 - v0;
		final double a3 = v1;
		return a0 * alpha * alpha2 + a1 * alpha2 + a2 * alpha + a3;
	}

	public static double hermiteInterpolate(final double v0, final double v1, final double v2, final double v3,
			final double alpha, final double tension, final double bias) {
		final double alpha2 = alpha * alpha;
		final double alpha3 = alpha2 * alpha;
		double m0 = (v1 - v0) * (1 - tension) * (1 + bias) / 2.0;
		m0 += (v2 - v1) * (1 - tension) * (1 - bias) / 2.0;
		double m1 = (v2 - v1) * (1 - tension) * (1 + bias) / 2.0;
		m1 += (v3 - v2) * (1 - tension) * (1 - bias) / 2.0;
		final double a0 = 2 * alpha3 - 3 * alpha2 + 1;
		final double a1 = alpha3 - 2 * alpha2 + alpha;
		final double a2 = alpha3 - alpha2;
		final double a3 = -2 * alpha3 + 3 * alpha2;
		return a0 * v1 + a1 * m0 + a2 * m1 + a3 * v2;
	}

	public static double kochanekBartelsInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha, final double tension, final double continuity, final double bias) {
		final double alpha2 = alpha * alpha;
		final double alpha3 = alpha2 * alpha;
		double m0 = (v1 - v0) * (1 - tension) * (1 + continuity) * (1 + bias) / 2.0;
		m0 += (v2 - v1) * (1 - tension) * (1 - continuity) * (1 - bias) / 2.0;
		double m1 = (v2 - v1) * (1 - tension) * (1 - continuity) * (1 + bias) / 2.0;
		m1 += (v3 - v2) * (1 - tension) * (1 + continuity) * (1 - bias) / 2.0;
		final double a0 = 2 * alpha3 - 3 * alpha2 + 1;
		final double a1 = alpha3 - 2 * alpha2 + alpha;
		final double a2 = alpha3 - alpha2;
		final double a3 = -2 * alpha3 + 3 * alpha2;
		return a0 * v1 + a1 * m0 + a2 * m1 + a3 * v2;
	}

	public static double quadraticBezierInterpolator(final double v0, final double v1, final double v2,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		return (v2 - 2 * v1 + v0) * alpha2 + (v1 - v0) * 2 * alpha + v0;
	}

	public static double cubicBezierInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		final double alpha3 = alpha2 * alpha;
		return (v3 - 3 * v2 + 3 * v1 - v0) * alpha3 + (3 * v2 - 6 * v1 + 3 * v0) * alpha2 + (3 * v1 - 3 * v0) * alpha
				+ v0;
	}

	public static double quadraticBSplineInterpolator(final double v0, final double v1, final double v2,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		return ((v2 - 2 * v1 + v0) * alpha2 + (v1 - v0) * 2 * alpha + v0 + v1) / 2.0;
	}

	public static double cubicBSplineInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		final double alpha3 = alpha2 * alpha;
		return ((v3 - 3 * v2 + 3 * v1 - v0) * alpha3 + (3 * v2 - 6 * v1 + 3 * v0) * alpha2 + (3 * v2 - 3 * v0) * alpha
				+ v0 + 4 * v1 + v2) / 6.0;
	}

	public static double cubicCatmullRomInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		final double alpha3 = alpha2 * alpha;
		return ((v3 - 3 * v2 + 3 * v1 - v0) * alpha3 + (-v3 + 4 * v2 - 5 * v1 + 2 * v0) * alpha2 + (v2 - v0) * alpha
				+ 2 * v1 + v0) / 2.0;
	}

	public static double cubicHermiteInterpolator(final double v0, final double v1, final double v2, final double v3,
			final double alpha) {
		final double alpha2 = alpha * alpha;
		final double alpha3 = alpha2 * alpha;
		return (v3 - 2 * v2 + v1 + 2 * v0) * alpha3;
	}
}
