package wblut.geom;

import wblut.math.WB_Epsilon;

/**
 *
 */
public interface WB_VectorFactory {
	/**
	 *
	 *
	 * @return
	 */
	static WB_Vector X() {
		return new WB_Point(1, 0, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	static WB_Vector Y() {
		return new WB_Point(0, 1, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	static WB_Vector Z() {
		return new WB_Point(0, 0, 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	static WB_Vector ZERO() {
		return new WB_Vector();
	}

	/**
	 *
	 *
	 * @return
	 */
	WB_Vector nextVector();

	/**
	 *
	 *
	 * @param N
	 * @return
	 */
	WB_VectorCollection getVectors(int N);

	/**
	 *
	 *
	 * @param N
	 * @param vectors
	 * @return
	 */
	WB_VectorCollection getVectors(int N, WB_Coord... vectors);

	/**
	 *
	 *
	 * @return
	 */
	static WB_Vector createVector2D() {
		return new WB_Vector(0, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	static WB_Vector createVectorFromTo2D(final WB_Coord p, final WB_Coord q) {
		return createVector2D(q.xd() - p.xd(), q.yd() - p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Vector createVector2D(final WB_Coord p) {
		return new WB_Vector(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Vector createVector2D(final double _x, final double _y) {
		return new WB_Vector(_x, _y);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Vector createNormalizedVector2D(final WB_Coord p) {
		final WB_Vector vec = createVector2D(p);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	static WB_Vector createNormalizedVectorFromTo2D(final WB_Coord p, final WB_Coord q) {
		final WB_Vector vec = createVector2D(q.xd() - p.xd(), q.yd() - p.yd());
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Vector createNormalizedVector2D(final double _x, final double _y) {
		final WB_Vector vec = createVector2D(_x, _y);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Vector createNormalizedPerpendicularVector2D(final double _x, final double _y) {
		final WB_Vector vec = createVector2D(-_y, _x);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	static WB_Vector createNormalizedPerpendicularVector2D(final WB_Coord v) {
		final WB_Vector vec = createVector2D(-v.yd(), v.xd());
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param r
	 * @param phi
	 * @return
	 */
	static WB_Vector createVectorFromPolar2D(final double r, final double phi) {
		return createVector2D(r * Math.cos(phi), r * Math.sin(phi));
	}

	/**
	 *
	 *
	 * @param a
	 * @param sigma
	 * @param tau
	 * @return
	 */
	static WB_Vector createVectorFromBipolar2D(final double a, final double sigma, final double tau) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector2D(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom);
	}

	/**
	 *
	 *
	 * @param sigma
	 * @param tau
	 * @return
	 */
	static WB_Vector createVectorFromParabolic2D(final double sigma, final double tau) {
		return createVector2D(sigma * tau, 0.5 * (tau * tau - sigma * sigma));
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	static WB_Vector createVectorFromHyperbolic2D(final double u, final double v) {
		return createVector2D(v * Math.exp(u), v * Math.exp(-u));
	}

	/**
	 *
	 *
	 * @param a
	 * @param mu
	 * @param nu
	 * @return
	 */
	static WB_Vector createVectorFromElliptic2D(final double a, final double mu, final double nu) {
		return createVector2D(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu));
	}

	/**
	 *
	 *
	 * @return
	 */
	static WB_Vector createVector3D() {
		return createVector3D(0, 0, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	static WB_Vector createVectorFromTo3D(final WB_Coord p, final WB_Coord q) {
		return createVector3D(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Vector createVector3D(final WB_Coord p) {
		return new WB_Vector(p);
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Vector createVector3D(final double _x, final double _y) {
		return createVector3D(_x, _y, 0);
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return
	 */
	static WB_Vector createVector3D(final double _x, final double _y, final double _z) {
		return new WB_Vector(_x, _y, _z);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Vector createNormalizedVector3D(final WB_Coord p) {
		final WB_Vector vec = createVector3D(p);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	static WB_Vector createNormalizedVectorFromTo3D(final WB_Coord p, final WB_Coord q) {
		final WB_Vector vec = createVector3D(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return
	 */
	static WB_Vector createNormalizedVector3D(final double _x, final double _y, final double _z) {
		final WB_Vector vec = createVector3D(_x, _y, _z);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @param _z
	 * @param _w
	 * @return
	 */
	static WB_Vector createNormalizedVector3D(final double _x, final double _y, final double _z, final double _w) {
		final WB_Vector vec = createVector3D(_x, _y, _z);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Vector createNormalizedVector3D(final double _x, final double _y) {
		final WB_Vector vec = createVector3D(_x, _y);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @return
	 */
	static WB_Vector createNormalizedPerpendicularVector3D(final double _x, final double _y) {
		final WB_Vector vec = createVector3D(-_y, _x, 0);
		vec.normalizeSelf();
		return vec;
	}

	/**
	 *
	 *
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return
	 */
	static WB_Vector createNormalizedPerpendicularVector3D(final double _x, final double _y, final double _z) {
		if (_x > _y) {
			if (_y > _z) {
				return createNormalizedVector3D(-_y, _x, 0);
			} else {
				return createNormalizedVector3D(-_z, 0, _x);
			}
		} else {
			if (_x > _z) {
				return createNormalizedVector3D(-_y, _x, 0);
			} else {
				return createNormalizedVector3D(0, -_z, _x);
			}
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	static WB_Vector createNormalizedPerpendicularVector3D(final WB_Coord p) {
		if (p.xd() > p.yd()) {
			if (p.yd() > p.zd()) {
				return createNormalizedVector3D(-p.yd(), p.xd(), 0);
			} else {
				return createNormalizedVector3D(-p.zd(), 0, p.xd());
			}
		} else {
			if (p.xd() > p.zd()) {
				return createNormalizedVector3D(-p.yd(), p.xd(), 0);
			} else {
				return createNormalizedVector3D(0, -p.zd(), p.xd());
			}
		}
	}

	/**
	 *
	 *
	 * @param r
	 * @param phi
	 * @param z
	 * @return
	 */
	static WB_Vector createVectorFromCylindrical3D(final double r, final double phi, final double z) {
		return createVector3D(r * Math.cos(phi), r * Math.sin(phi), z);
	}

	/**
	 *
	 *
	 * @param r
	 * @param theta
	 * @param phi
	 * @return
	 */
	static WB_Vector createVectorFromSpherical3D(final double r, final double theta, final double phi) {
		return createVector3D(r * Math.cos(phi) * Math.sin(theta), r * Math.sin(phi) * Math.sin(theta),
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
	static WB_Vector createVectorFromParaboloidal3D(final double sigma, final double tau, final double phi) {
		return createVector3D(sigma * tau * Math.cos(phi), sigma * tau * Math.sin(phi),
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
	static WB_Vector createVectorFromParabolic3D(final double sigma, final double tau, final double z) {
		return createVector3D(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
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
	static WB_Vector createVectorFromOblateSpheroidal3D(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createVector3D(common * Math.cos(phi), common * Math.sin(phi), a * Math.sinh(mu) * Math.sin(nu));
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
	static WB_Vector createVectorFromProlateSpheroidal3D(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createVector3D(common * Math.cos(phi), common * Math.sin(phi), a * Math.cosh(mu) * Math.cos(nu));
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
	static WB_Vector createVectorFromEllipsoidal3D(final double a, final double b, final double c, final double lambda,
			final double mu, final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createVector3D(Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2) / (a2 - c2)),
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
	static WB_Vector createVectorFromElliptic3D(final double a, final double mu, final double nu, final double z) {
		return createVector3D(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu), z);
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
	static WB_Vector createVectorFromToroidal3D(final double a, final double sigma, final double tau,
			final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector3D(Math.sinh(tau) * invdenom * Math.cos(phi), Math.sinh(tau) * invdenom * Math.sin(phi),
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
	static WB_Vector createVectorFromBispherical3D(final double a, final double sigma, final double tau,
			final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector3D(Math.sin(sigma) * invdenom * Math.cos(phi), Math.sin(sigma) * invdenom * Math.sin(phi),
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
	static WB_Vector createVectorFromBipolarCylindrical3D(final double a, final double sigma, final double tau,
			final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector3D(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom, z);
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
	static WB_Vector createVectorFromConical3D(final double b, final double c, final double r, final double mu,
			final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createVector3D(r * mu * nu / b / c, r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}
}
