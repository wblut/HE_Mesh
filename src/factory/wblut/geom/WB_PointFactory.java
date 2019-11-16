/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.math.WB_Epsilon;

/**
 * @author FVH
 *
 */
public interface WB_PointFactory{

	public static WB_Point ORIGIN() {
		return new WB_Point();
	}
	
	public WB_Point nextPoint();
	
	public WB_PointCollection getPoints(int N);

	/**
	 * New point at origin.
	 *
	 * @return new point at origin
	 */
	public static WB_Point createPoint2D() {
		return new WB_Point(0, 0, 0);
	}

	/**
	 * Copy of coordinate as point, z-ordinate is ignored.
	 *
	 * @param p
	 *            point
	 * @return copy of point
	 */
	public static WB_Point createPoint2D(final WB_Coord p) {
		return new WB_Point(p.xd(), p.yd(), 0);
	}

	/**
	 * Point from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @return 2D point
	 */
	public static WB_Point createPoint2D(final double x, final double y) {
		return new WB_Point(x, y, 0);
	}

	/**
	 * Interpolated point.
	 *
	 * @param p
	 *            point
	 * @param q
	 *            point
	 * @param f
	 *            interpolation value, p=0,q=1
	 * @return copy of point
	 */
	public static WB_Point createInterpolatedPoint2D(final WB_Coord p,
			final WB_Coord q, final double f) {
		return new WB_Point((1.0 - f) * p.xd() + f * q.xd(),
				(1.0 - f) * p.yd() + f * q.yd());
	}

	/**
	 * Point from polar coordinates
	 * http://en.wikipedia.org/wiki/Polar_coordinate_system
	 *
	 * @param r
	 *            radius
	 * @param phi
	 *            angle
	 * @return 2D point
	 */
	public static WB_Point createPointFromPolar2D(final double r, final double phi) {
		return createPoint2D(r * Math.cos(phi), r * Math.sin(phi));
	}

	/**
	 * Point from bipolar coordinates
	 * http://en.wikipedia.org/wiki/Bipolar_coordinates
	 *
	 * @param a
	 *            focus
	 * @param sigma
	 *            bipolar coordinate
	 * @param tau
	 *            bipolar coordinate
	 * @return 2D point
	 */
	public static WB_Point createPointFromBipolar2D(final double a, final double sigma,
			final double tau) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint2D(Math.sinh(tau) * invdenom,
				Math.sin(sigma) * invdenom);
	}

	/**
	 * Point from parabolic coordinates
	 * http://en.wikipedia.org/wiki/Parabolic_coordinates
	 *
	 * @param sigma
	 *            parabolic coordinate
	 * @param tau
	 *            parabolic coordinate
	 * @return 2D point
	 */
	public static WB_Point createPointFromParabolic2D(final double sigma,
			final double tau) {
		return createPoint2D(sigma * tau, 0.5 * (tau * tau - sigma * sigma));
	}

	/**
	 * Point from hyperbolic coordinates
	 * http://en.wikipedia.org/wiki/Hyperbolic_coordinates
	 *
	 * @param u
	 *            hyperbolic angle
	 * @param v
	 *            geometric mean >0
	 * @return 2D point
	 */
	public static WB_Point createPointFromHyperbolic2D(final double u,
			final double v) {
		return createPoint2D(v * Math.exp(u), v * Math.exp(-u));
	}

	/**
	 * Point from elliptic coordinates
	 * http://en.wikipedia.org/wiki/Elliptic_coordinates
	 *
	 * @param a
	 *            focus
	 * @param sigma
	 *            elliptic coordinate >=0
	 * @param tau
	 *            elliptic coordinate between -1 and 1
	 * @return 2D point
	 */
	public static WB_Point createPointFromElliptic2D(final double a,
			final double sigma, final double tau) {
		return createPoint2D(a * sigma * tau,
				Math.sqrt(a * a * (sigma * sigma - 1) * (1 - tau * tau)));
	}

	/**
	 * New point at origin.
	 *
	 * @return new point at origin
	 */
	public static WB_Point createPoint3D() {
		return new WB_Point(0, 0, 0);
	}

	/**
	 * Create new point.
	 *
	 * @param p
	 *            point
	 * @return copy of point
	 */
	public static WB_Point createPoint3D(final WB_Coord p) {
		return new WB_Point(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Point createPoint3D(final double[] p) {
		return new WB_Point(p);
	}

	/**
	 * Point from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @return 2D point
	 */
	public static WB_Point createPoint3D(final double _x, final double _y) {
		return createPoint3D(_x, _y, 0);
	}

	/**
	 * Point from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Elliptic_coordinates
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @param _z
	 *            z
	 * @return 3D point
	 */
	public static WB_Point createPoint3D(final double _x, final double _y,
			final double _z) {
		return new WB_Point(_x, _y, _z);
	}

	/**
	 * Interpolated point.
	 *
	 * @param p
	 *            point
	 * @param q
	 *            point
	 * @param f
	 *            interpolation value, p=0,q=1
	 * @return copy of point
	 */
	public static WB_Point createInterpolatedPoint3D(final WB_Coord p,
			final WB_Coord q, final double f) {
		return new WB_Point((1.0 - f) * p.xd() + f * q.xd(),
				(1.0 - f) * p.yd() + f * q.yd(),
				(1.0 - f) * p.zd() + f * q.zd());
	}

	/**
	 * Point from cylindrical coordinates
	 * http://en.wikipedia.org/wiki/Cylindrical_coordinate_system
	 *
	 * @param r
	 *            radius
	 * @param phi
	 *            angle
	 * @param z
	 *            height
	 * @return 3D point
	 */
	public static WB_Point createPointFromCylindrical3D(final double r,
			final double phi, final double z) {
		return createPoint3D(r * Math.cos(phi), r * Math.sin(phi), z);
	}

	/**
	 * Point from spherical coordinates
	 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
	 *
	 * @param r
	 *            radius
	 * @param theta
	 *            inclination coordinate between -0.5*PI and 0.5*PI
	 * @param phi
	 *            azimuth coordinate between -PI and PI
	 * @return 3D point
	 */
	public static WB_Point createPointFromSpherical3D(final double r,
			final double theta, final double phi) {
		return createPoint3D(r * Math.cos(phi) * Math.sin(theta),
				r * Math.sin(phi) * Math.sin(theta), r * Math.cos(theta));
	}

	/**
	 * Point from paraboloidal coordinates
	 * http://en.wikipedia.org/wiki/Paraboloidal_coordinates
	 *
	 * @param sigma
	 *            parabolic coordinate
	 * @param tau
	 *            parabolic coordinate
	 * @param phi
	 *            azimuth coordinate between -PI and PI
	 * @return 3D point
	 */
	public static WB_Point createPointFromParaboloidal3D(final double sigma,
			final double tau, final double phi) {
		return createPoint3D(sigma * tau * Math.cos(phi),
				sigma * tau * Math.sin(phi), 0.5 * (tau * tau - sigma * sigma));
	}

	/**
	 * Point from parabolic coordinates
	 * http://en.wikipedia.org/wiki/Parabolic_cylindrical_coordinates
	 *
	 * @param sigma
	 *            parabolic coordinate
	 * @param tau
	 *            parabolic coordinate
	 * @param z
	 *            height
	 * @return 3D point
	 */
	public static WB_Point createPointFromParabolic3D(final double sigma,
			final double tau, final double z) {
		return createPoint3D(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
	}

	/**
	 * Point from oblate spheroidal coordinates
	 * http://en.wikipedia.org/wiki/Oblate_spheroidal_coordinates
	 *
	 * @param a
	 *            focus
	 * @param mu
	 *            spheroidal coordinate >=0
	 * @param nu
	 *            spheroidal coordinate between -0.5*PI and 0.5*PI
	 * @param phi
	 *            azimuth coordinate between -PI and PI
	 *
	 * @return 3D point
	 */
	public static WB_Point createPointFromOblateSpheroidal3D(final double a,
			final double mu, final double nu, final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createPoint3D(common * Math.cos(phi), common * Math.sin(phi),
				a * Math.sinh(mu) * Math.sin(nu));
	}

	/**
	 * Point from prolate spheroidal coordinates
	 * http://en.wikipedia.org/wiki/Prolate_spheroidal_coordinates
	 *
	 * @param a
	 *            focus
	 * @param mu
	 *            spheroidal coordinate >=0
	 * @param nu
	 *            spheroidal coordinate between -0.5*PI and 0.5*PI
	 * @param phi
	 *            azimuth coordinate between -PI and PI
	 *
	 * @return 3D point
	 */
	public static WB_Point createPointFromProlateSpheroidal3D(final double a,
			final double mu, final double nu, final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createPoint3D(common * Math.cos(phi), common * Math.sin(phi),
				a * Math.cosh(mu) * Math.cos(nu));
	}

	/**
	 * Point from ellipsoidal coordinates
	 * http://en.wikipedia.org/wiki/Ellipsoidal_coordinates
	 *
	 * -lambda<c2<-mu<b2<-nu<a2
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param lambda
	 *            ellipsoidal coordinate
	 * @param mu
	 *            ellipsoidal coordinate
	 * @param nu
	 *            ellipsoidal coordinate
	 * @return 3D point
	 */
	public static WB_Point createPointFromEllipsoidal3D(final double a, final double b,
			final double c, final double lambda, final double mu,
			final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createPoint3D(
				Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2)
						/ (a2 - c2)),
				Math.sqrt((b2 - lambda) * (b2 - mu) * (b2 - nu) / (b2 - a2)
						/ (b2 - c2)),
				Math.sqrt((c2 - lambda) * (c2 - mu) * (c2 - nu) / (c2 - a2)
						/ (c2 - b2)));
	}

	/**
	 * Point from elliptic coordinates
	 * http://en.wikipedia.org/wiki/Elliptic_cylindrical_coordinates
	 *
	 * @param a
	 *            focus
	 * @param mu
	 *            elliptic coordinate >=0
	 * @param nu
	 *            elliptic coordinate between -PI and PI
	 * @param z
	 *            height
	 *
	 * @return 3D point
	 */
	public static WB_Point createPointFromElliptic3D(final double a, final double mu,
			final double nu, final double z) {
		return createPoint3D(a * Math.cosh(mu) * Math.cos(nu),
				a * Math.sinh(mu) * Math.cos(nu), z);
	}

	/**
	 * Point from toroidal coordinates
	 * http://en.wikipedia.org/wiki/Toroidal_coordinates
	 *
	 * @param a
	 *            focus
	 * @param sigma
	 *            toroidal coordinate
	 * @param tau
	 *            toroidal coordinate
	 * @param phi
	 *            toroidal coordinate
	 *
	 * @return 3D point
	 */
	public static WB_Point createPointFromToroidal3D(final double a,
			final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint3D(Math.sinh(tau) * invdenom * Math.cos(phi),
				Math.sinh(tau) * invdenom * Math.sin(phi),
				Math.sin(sigma) * invdenom);
	}

	/**
	 * Point from bispherical coordinates
	 * http://en.wikipedia.org/wiki/Bispherical_coordinates
	 *
	 * @param a
	 *            focus
	 * @param sigma
	 *            toroidal coordinate
	 * @param tau
	 *            toroidal coordinate
	 * @param phi
	 *            toroidal coordinate
	 *
	 * @return 3D point
	 */
	public static WB_Point createPointFromBispherical3D(final double a,
			final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint3D(Math.sin(sigma) * invdenom * Math.cos(phi),
				Math.sin(sigma) * invdenom * Math.sin(phi),
				Math.sinh(tau) * invdenom);
	}

	/**
	 * Point from bipolar cylindrical coordinates
	 * http://en.wikipedia.org/wiki/Bipolar_cylindrical_coordinates
	 *
	 * @param a
	 *            focus
	 * @param sigma
	 *            toroidal coordinate
	 * @param tau
	 *            toroidal coordinate
	 * @param z
	 *            height
	 *
	 * @return 3D point
	 */
	public static WB_Point createPointFromBipolarCylindrical3D(final double a,
			final double sigma, final double tau, final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint3D(Math.sinh(tau) * invdenom,
				Math.sin(sigma) * invdenom, z);
	}

	/**
	 * Point from conical coordinates
	 * http://en.wikipedia.org/wiki/Conical_coordinates
	 *
	 * nu2<c2<mu2<b2
	 *
	 * @param b
	 * @param c
	 * @param r
	 *            radius
	 * @param mu
	 *            conical coordinate
	 * @param nu
	 *            conical coordinate
	 * @return 3D point
	 */
	public static WB_Point createPointFromConical3D(final double b, final double c,
			final double r, final double mu, final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createPoint3D(r * mu * nu / b / c,
				r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}
	
	
}
