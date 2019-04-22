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
 * Spherical harmonic functions and associated Legendre polynomials. Calculated
 * using recurrence relations:
 * https://en.wikipedia.org/wiki/Associated_Legendre_polynomials#
 * Recurrence_formula
 *
 */
public class WB_SphericalHarmonics {

	final static double[][] norms = initNormalization();
	final static double SQRT2 = Math.sqrt(2.0);

	private static double[][] initNormalization() {
		double[][] norms = new double[41][42];
		for (int L = 0; L < 40; L++) {
			for (int M = 0; M <= L; M++) {
				norms[L][M] = getNormalization(L, M);
			}
		}
		return norms;
	}

	private static double getNormalizationLUT(final int L, final int M) {
		if (L <= 40) {
			return norms[L][M];
		}
		return getNormalization(L, M);

	}

	private static double getNormalization(final int L, final int M) {

		// sqrt((2L+1)/4PI.(L-M)!/(L+M)!)
		double factor = 4.0 * Math.PI;
		// (L-M)!/(L+M)! = 1.0/(L-M+1)*(L-M+2)*...*(L+M)
		for (int i = L - M + 1; i <= L + M; i++) {
			factor *= i;
		}
		factor = (2 * L + 1.0) / factor;
		return Math.sqrt(factor);
	}

	/**
	 * Associated Legendre polynomial at x. Recurrence property:
	 *
	 *
	 * @param L
	 *            Spherical harmonic order
	 * @param M
	 *            Spherical harmonic index
	 * @param x
	 * @return
	 */
	public static double Plm(final int L, final int M, final double x) {
		if (M < 0 || M > L) {
			throw new IllegalArgumentException(
					"Bad argument in associated Legendre polynomial: index m outside of range for order l: 0<=m<=l");
		}
		if (Math.abs(x) > 1.0) {
			throw new IllegalArgumentException("Bad argument in associated Legendre polynomial: -1.0<=x<=+1.0");
		}

		// start recursion: (0,0)->(M,M) up to PMM
		// P(M+1,M+1)=-2M.P(M,M)-sqrt(1.0-x²)P(M,M), P(0,0)=1.0;
		double PMM = 1.0;
		if (M > 0) {
			double somx2 = Math.sqrt((1.0 - x) * (1.0 + x));// robust way of
															// calculating
															// sqrt(1-x²)
			double fact = 1.0;
			for (int i = 1; i <= M; i++) {
				PMM *= -fact * somx2;
				fact += 2.0;
			}
		}
		if (L == M) {
			return PMM;// =PLM
		} else {
			// Next recursion (M,M)->(M+1,M)
			// P(M+1,M)=x(2M+1)P(M,M)
			double PMMP = x * (2 * M + 1) * PMM;
			if (L == M + 1) {// =PLM
				return PMMP;
			} else {
				// Final recursion (M+1,M)->(L,M)
				// P(M,L+1)=((2L+1)xP(M,L)-(L+M)P(M,L-1))/(L-M+1)
				double Pfinal = 0.0;
				for (int currentL = M + 2; currentL <= L; currentL++) {
					Pfinal = (x * (2 * currentL - 1) * PMMP - (currentL + M - 1) * PMM) / (currentL - M);
					PMM = PMMP;
					PMMP = Pfinal;
				}
				return Pfinal;
			}
		}
	}

	public static WB_Coord rotateSpherical(final double angle, final double ax, final double ay, final double az,
			final double theta, final double phi) {
		WB_Vector v = new WB_Vector(Math.cos(phi) * Math.sin(theta), Math.sin(phi) * Math.sin(theta), Math.cos(theta));
		v.rotateAboutOriginSelf(angle, ax, ay, az);
		return new WB_Vector(Math.acos(Math.min(Math.max(v.zd(), -1.0), 1.0)), Math.atan2(v.yd(), v.xd()));
	}

	/**
	 * Complex spherical harmonic of order l, index m
	 *
	 * @param L
	 *            spherical harmonic order.
	 * @param M
	 *            spherical harmonic index. -l<=m<=l
	 * @param theta
	 * @param phi
	 * @return Y(theta, phi)
	 */
	public static WB_Complex Y(final int L, final int M, final double theta, final double phi) {
		if (Math.abs(M) > L) {
			throw new IllegalArgumentException(
					"Bad argument in spherical harmonic: index m outside of range for order l: -l<=m<=l");
		}
		int absM = Math.abs(M);
		double norm = getNormalizationLUT(L, absM);
		double legendre = Plm(L, absM, Math.cos(theta));
		WB_Complex exponential = new WB_Complex(Math.cos(absM * phi), Math.sin(absM * phi));
		WB_Complex result = exponential.mul(norm * legendre);
		if (M < 0) {
			result = result.conjugate();
			if (absM % 2 == 1) {
				result = result.negate();
			}
		}
		return result;
	}

	public static WB_Complex Y(final int L, final int M, final double theta, final double phi, final double angle,
			final double ax, final double ay, final double az) {
		WB_Coord rotatedThetaPhi = rotateSpherical(angle, ax, ay, az, theta, phi);
		return Y(L, M, rotatedThetaPhi.xd(), rotatedThetaPhi.yd());
	}

	public static double Ylm(final int L, final int M, final double theta, final double phi) {
		if (Math.abs(M) > L) {
			throw new IllegalArgumentException(
					"Bad argument in spherical harmonic: index m outside of range for order l: -l<=m<=l");
		}
		int absM = Math.abs(M);
		double norm = getNormalizationLUT(L, absM);
		double legendre = Plm(L, absM, Math.cos(theta));

		if (M == 0) {
			return norm * legendre * Math.cos(absM * phi);

		}
		if (M < 0) {
			double Ylm = -norm * legendre * Math.sin(absM * phi) * SQRT2;
			return absM % 2 == 1 ? -Ylm : Ylm;
		}

		return norm * legendre * Math.cos(absM * phi) * SQRT2;
	}

	public static double Ylm(final int L, final int M, final double theta, final double phi, final double angle,
			final double ax, final double ay, final double az) {
		WB_Coord rotatedThetaPhi = rotateSpherical(angle, ax, ay, az, theta, phi);
		return Ylm(L, M, rotatedThetaPhi.xd(), rotatedThetaPhi.yd());
	}

	public static double Ylm2(final int L, final int M, final double theta, final double phi) {
		double result = Ylm(L, M, theta, phi);
		return result * result;
	}

	public static double Ylm2(final int L, final int M, final double theta, final double phi, final double angle,
			final double ax, final double ay, final double az) {
		WB_Coord rotatedThetaPhi = rotateSpherical(angle, ax, ay, az, theta, phi);
		return Ylm2(L, M, rotatedThetaPhi.xd(), rotatedThetaPhi.yd());
	}

}