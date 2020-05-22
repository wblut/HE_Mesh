package wblut.geom;

import wblut.math.WB_Epsilon;

/**
 *
 */
public interface WB_PointFactory {
	/**
	 *
	 *
	 * @return
	 */
	static WB_Point ORIGIN() {
		return new WB_Point();
	}

	/**
	 *
	 *
	 * @return
	 */
	WB_Point nextPoint();

	/**
	 *
	 *
	 * @param N
	 * @return
	 */
	WB_PointCollection getPoints(int N);

	/**
	 *
	 *
	 * @param N
	 * @param points
	 * @return
	 */
	WB_PointCollection getPoints(int N, WB_Coord... points);

	/**
	 *
	 *
	 * @return
	 */
	static WB_Point createPoint2D() {
		return new WB_Point(0, 0, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Point createPoint2D(final WB_Coord p) {
		return new WB_Point(p.xd(), p.yd(), 0);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	static WB_Point createPoint2D(final double x, final double y) {
		return new WB_Point(x, y, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param f
	 * @return
	 */
	static WB_Point createInterpolatedPoint2D(final WB_Coord p, final WB_Coord q, final double f) {
		return new WB_Point((1.0 - f) * p.xd() + f * q.xd(), (1.0 - f) * p.yd() + f * q.yd());
	}

	/**
	 *
	 *
	 * @param r
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromPolar2D(final double r, final double phi) {
		return createPoint2D(r * Math.cos(phi), r * Math.sin(phi));
	}

	/**
	 *
	 *
	 * @param a
	 * @param sigma
	 * @param tau
	 * @return
	 */
	static WB_Point createPointFromBipolar2D(final double a, final double sigma, final double tau) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint2D(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom);
	}

	/**
	 *
	 *
	 * @param sigma
	 * @param tau
	 * @return
	 */
	static WB_Point createPointFromParabolic2D(final double sigma, final double tau) {
		return createPoint2D(sigma * tau, 0.5 * (tau * tau - sigma * sigma));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	static WB_Point createPointFromHyperbolic2D(final double u, final double v) {
		return createPoint2D(v * Math.exp(u), v * Math.exp(-u));
	}

	/**
	 *
	 *
	 * @param a
	 * @param sigma
	 * @param tau
	 * @return
	 */
	static WB_Point createPointFromElliptic2D(final double a, final double sigma, final double tau) {
		return createPoint2D(a * sigma * tau, Math.sqrt(a * a * (sigma * sigma - 1) * (1 - tau * tau)));
	}

	/**
	 *
	 *
	 * @return
	 */
	static WB_Point createPoint3D() {
		return new WB_Point(0, 0, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Point createPoint3D(final WB_Coord p) {
		return new WB_Point(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Point createPoint3D(final double[] p) {
		return new WB_Point(p);
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Point createPoint3D(final double _x, final double _y) {
		return createPoint3D(_x, _y, 0);
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return
	 */
	static WB_Point createPoint3D(final double _x, final double _y, final double _z) {
		return new WB_Point(_x, _y, _z);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @param f
	 * @return
	 */
	static WB_Point createInterpolatedPoint3D(final WB_Coord p, final WB_Coord q, final double f) {
		return new WB_Point((1.0 - f) * p.xd() + f * q.xd(), (1.0 - f) * p.yd() + f * q.yd(),
				(1.0 - f) * p.zd() + f * q.zd());
	}

	/**
	 *
	 *
	 * @param r
	 * @param phi
	 * @param z
	 * @return
	 */
	static WB_Point createPointFromCylindrical3D(final double r, final double phi, final double z) {
		return createPoint3D(r * Math.cos(phi), r * Math.sin(phi), z);
	}

	/**
	 *
	 *
	 * @param r
	 * @param theta
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromSpherical3D(final double r, final double theta, final double phi) {
		return createPoint3D(r * Math.cos(phi) * Math.sin(theta), r * Math.sin(phi) * Math.sin(theta),
				r * Math.cos(theta));
	}

	/**
	 *
	 *
	 * @param sigma
	 * @param tau
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromParaboloidal3D(final double sigma, final double tau, final double phi) {
		return createPoint3D(sigma * tau * Math.cos(phi), sigma * tau * Math.sin(phi),
				0.5 * (tau * tau - sigma * sigma));
	}

	/**
	 *
	 *
	 * @param sigma
	 * @param tau
	 * @param z
	 * @return
	 */
	static WB_Point createPointFromParabolic3D(final double sigma, final double tau, final double z) {
		return createPoint3D(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
	}

	/**
	 *
	 *
	 * @param a
	 * @param mu
	 * @param nu
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromOblateSpheroidal3D(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createPoint3D(common * Math.cos(phi), common * Math.sin(phi), a * Math.sinh(mu) * Math.sin(nu));
	}

	/**
	 *
	 *
	 * @param a
	 * @param mu
	 * @param nu
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromProlateSpheroidal3D(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createPoint3D(common * Math.cos(phi), common * Math.sin(phi), a * Math.cosh(mu) * Math.cos(nu));
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param lambda
	 * @param mu
	 * @param nu
	 * @return
	 */
	static WB_Point createPointFromEllipsoidal3D(final double a, final double b, final double c, final double lambda,
			final double mu, final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createPoint3D(Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2) / (a2 - c2)),
				Math.sqrt((b2 - lambda) * (b2 - mu) * (b2 - nu) / (b2 - a2) / (b2 - c2)),
				Math.sqrt((c2 - lambda) * (c2 - mu) * (c2 - nu) / (c2 - a2) / (c2 - b2)));
	}

	/**
	 *
	 *
	 * @param a
	 * @param mu
	 * @param nu
	 * @param z
	 * @return
	 */
	static WB_Point createPointFromElliptic3D(final double a, final double mu, final double nu, final double z) {
		return createPoint3D(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu), z);
	}

	/**
	 *
	 *
	 * @param a
	 * @param sigma
	 * @param tau
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromToroidal3D(final double a, final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint3D(Math.sinh(tau) * invdenom * Math.cos(phi), Math.sinh(tau) * invdenom * Math.sin(phi),
				Math.sin(sigma) * invdenom);
	}

	/**
	 *
	 *
	 * @param a
	 * @param sigma
	 * @param tau
	 * @param phi
	 * @return
	 */
	static WB_Point createPointFromBispherical3D(final double a, final double sigma, final double tau,
			final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint3D(Math.sin(sigma) * invdenom * Math.cos(phi), Math.sin(sigma) * invdenom * Math.sin(phi),
				Math.sinh(tau) * invdenom);
	}

	/**
	 *
	 *
	 * @param a
	 * @param sigma
	 * @param tau
	 * @param z
	 * @return
	 */
	static WB_Point createPointFromBipolarCylindrical3D(final double a, final double sigma, final double tau,
			final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint3D(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom, z);
	}

	/**
	 *
	 *
	 * @param b
	 * @param c
	 * @param r
	 * @param mu
	 * @param nu
	 * @return
	 */
	static WB_Point createPointFromConical3D(final double b, final double c, final double r, final double mu,
			final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createPoint3D(r * mu * nu / b / c, r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}
}
