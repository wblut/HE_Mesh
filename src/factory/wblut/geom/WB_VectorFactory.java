/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import wblut.math.WB_Epsilon;

/**
 * @author FVH
 *
 */
public interface WB_VectorFactory{
	public static WB_Vector X() {
		return new WB_Point(1,0,0);
	}
	public static WB_Vector Y() {
		return new WB_Point(0,1,0);
	}
	public static WB_Vector Z() {
		return new WB_Point(0,0,1);
	}
	public static WB_Vector ZERO() {
		return new WB_Vector();
	}
	public WB_Vector nextVector();
	
	
	public WB_VectorCollection getVectors(int N);
	/**
	 * New zero-length vector.
	 *
	 * @return zero-length vector
	 */
	public static WB_Vector createVector2D() {
		return new WB_Vector(0, 0);
	}
	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector createVectorFromTo2D(final WB_Coord p, final WB_Coord q) {
		return createVector2D(q.xd() - p.xd(), q.yd() - p.yd());
	}
	/**
	 * Copy of coordinate as vector, z-ordinate is ignored.
	 *
	 * @param p
	 *            vector
	 * @return vector
	 */
	public static WB_Vector createVector2D(final WB_Coord p) {
		return new WB_Vector(p.xd(), p.yd());
	}
	/**
	 * Vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @return 2D vector
	 */
	public static WB_Vector createVector2D(final double _x, final double _y) {
		return new WB_Vector(_x, _y);
	}
	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Vector createNormalizedVector2D(final WB_Coord p) {
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
	public static WB_Vector createNormalizedVectorFromTo2D(final WB_Coord p, final WB_Coord q) {
		final WB_Vector vec = createVector2D(q.xd() - p.xd(), q.yd() - p.yd());
		vec.normalizeSelf();
		return vec;
	}
	/**
	 * Normalized vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @return 2D vector
	 */
	public static WB_Vector createNormalizedVector2D(final double _x, final double _y) {
		final WB_Vector vec = createVector2D(_x, _y);
		vec.normalizeSelf();
		return vec;
	}
	/**
	 * Normalized vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @return 2D vector
	 */
	public static WB_Vector createNormalizedPerpendicularVector2D(final double _x, final double _y) {
		final WB_Vector vec = createVector2D(-_y, _x);
		vec.normalizeSelf();
		return vec;
	}
	public static WB_Vector createNormalizedPerpendicularVector2D(final WB_Coord v) {
		final WB_Vector vec = createVector2D(-v.yd(), v.xd());
		vec.normalizeSelf();
		return vec;
	}
	/**
	 * Vector from polar coordinates
	 * http://en.wikipedia.org/wiki/Polar_coordinate_system
	 *
	 * @param r
	 *            radius
	 * @param phi
	 *            angle
	 * @return 2D vector
	 */
	public static WB_Vector createVectorFromPolar2D(final double r, final double phi) {
		return createVector2D(r * Math.cos(phi), r * Math.sin(phi));
	}
	/**
	 * Vector from bipolar coordinates
	 * http://en.wikipedia.org/wiki/Bipolar_coordinates
	 *
	 * @param a
	 *            focus
	 * @param sigma
	 *            bipolar coordinate
	 * @param tau
	 *            bipolar coordinate
	 * @return 2D vector
	 */
	public static WB_Vector createVectorFromBipolar2D(final double a, final double sigma, final double tau) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector2D(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom);
	}
	/**
	 * Vector from parabolic coordinates
	 * http://en.wikipedia.org/wiki/Parabolic_coordinates
	 *
	 * @param sigma
	 *            parabolic coordinate
	 * @param tau
	 *            parabolic coordinate
	 * @return 2D vector
	 */
	public static WB_Vector createVectorFromParabolic2D(final double sigma, final double tau) {
		return createVector2D(sigma * tau, 0.5 * (tau * tau - sigma * sigma));
	}
	/**
	 * Vector from hyperbolic coordinates
	 * http://en.wikipedia.org/wiki/Hyperbolic_coordinates
	 *
	 * @param u
	 *            hyperbolic angle
	 * @param v
	 *            geometric mean >0
	 * @return 2D vector
	 */
	public static WB_Vector createVectorFromHyperbolic2D(final double u, final double v) {
		return createVector2D(v * Math.exp(u), v * Math.exp(-u));
	}
	/**
	 * Vector from elliptic coordinates
	 * http://en.wikipedia.org/wiki/Elliptic_coordinates
	 *
	 * @param a
	 *            focus
	 * @param mu
	 *            elliptic coordinate >=0
	 * @param nu
	 *            elliptic coordinate between -PI and PI
	 * @return 2D vector
	 */
	public static WB_Vector createVectorFromElliptic2D(final double a, final double mu, final double nu) {
		return createVector2D(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu));
	}
	/**
	 * New zero-length vector.
	 *
	 * @return zero-length vector
	 */
	public static WB_Vector createVector3D() {
		return createVector3D(0, 0, 0);
	}
	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public static WB_Vector createVectorFromTo3D(final WB_Coord p, final WB_Coord q) {
		return createVector3D(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
	}
	/**
	 * Copy of coordinate as vector.
	 *
	 * @param p
	 *            vector
	 * @return vector
	 */
	public static WB_Vector createVector3D(final WB_Coord p) {
		return new WB_Vector(p);
	}
	/**
	 * Vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @return 2D vector
	 */
	public static WB_Vector createVector3D(final double _x, final double _y) {
		return createVector3D(_x, _y, 0);
	}
	/**
	 * Vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Elliptic_coordinates
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @param _z
	 *            z
	 * @return 3D vector
	 */
	public static WB_Vector createVector3D(final double _x, final double _y, final double _z) {
		return new WB_Vector(_x, _y, _z);
	}
	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Vector createNormalizedVector3D(final WB_Coord p) {
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
	public static WB_Vector createNormalizedVectorFromTo3D(final WB_Coord p, final WB_Coord q) {
		final WB_Vector vec = createVector3D(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
		vec.normalizeSelf();
		return vec;
	}
	/**
	 * Normalized vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @param _z
	 *            z
	 *
	 * @return 3D vector
	 */
	public static WB_Vector createNormalizedVector3D(final double _x, final double _y, final double _z) {
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
	public static WB_Vector createNormalizedVector3D(final double _x, final double _y, final double _z, final double _w) {
		final WB_Vector vec = createVector3D(_x, _y, _z);
		vec.normalizeSelf();
		return vec;
	}
	public static WB_Vector createNormalizedVector3D(final double _x, final double _y) {
		final WB_Vector vec = createVector3D(_x, _y);
		vec.normalizeSelf();
		return vec;
	}
	/**
	 * Normalized vector from Cartesian coordinates
	 * http://en.wikipedia.org/wiki/Cartesian_coordinate_system
	 *
	 * @param _x
	 *            x
	 * @param _y
	 *            y
	 * @return 2D vector
	 */
	public static WB_Vector createNormalizedPerpendicularVector3D(final double _x, final double _y) {
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
	public static WB_Vector createNormalizedPerpendicularVector3D(final double _x, final double _y, final double _z) {
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
	public static WB_Vector createNormalizedPerpendicularVector3D(final WB_Coord p) {
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
	 * Vector from cylindrical coordinates
	 * http://en.wikipedia.org/wiki/Cylindrical_coordinate_system
	 *
	 * @param r
	 *            radius
	 * @param phi
	 *            angle
	 * @param z
	 *            height
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromCylindrical3D(final double r, final double phi, final double z) {
		return createVector3D(r * Math.cos(phi), r * Math.sin(phi), z);
	}
	/**
	 * Vector from spherical coordinates
	 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
	 *
	 * @param r
	 *            radius
	 * @param theta
	 *            inclination coordinate between -0.5*PI and 0.5*PI
	 * @param phi
	 *            azimuth coordinate between -PI and PI
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromSpherical3D(final double r, final double theta, final double phi) {
		return createVector3D(r * Math.cos(phi) * Math.sin(theta), r * Math.sin(phi) * Math.sin(theta),
				r * Math.cos(theta));
	}
	/**
	 * Vector from paraboloidal coordinates
	 * http://en.wikipedia.org/wiki/Paraboloidal_coordinates
	 *
	 * @param sigma
	 *            parabolic coordinate
	 * @param tau
	 *            parabolic coordinate
	 * @param phi
	 *            azimuth coordinate between -PI and PI
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromParaboloidal3D(final double sigma, final double tau, final double phi) {
		return createVector3D(sigma * tau * Math.cos(phi), sigma * tau * Math.sin(phi),
				0.5 * (tau * tau - sigma * sigma));
	}
	/**
	 * Vector from parabolic coordinates
	 * http://en.wikipedia.org/wiki/Parabolic_cylindrical_coordinates
	 *
	 * @param sigma
	 *            parabolic coordinate
	 * @param tau
	 *            parabolic coordinate
	 * @param z
	 *            height
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromParabolic3D(final double sigma, final double tau, final double z) {
		return createVector3D(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
	}
	/**
	 * Vector from oblate spheroidal coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromOblateSpheroidal3D(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createVector3D(common * Math.cos(phi), common * Math.sin(phi), a * Math.sinh(mu) * Math.sin(nu));
	}
	/**
	 * Vector from prolate spheroidal coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromProlateSpheroidal3D(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createVector3D(common * Math.cos(phi), common * Math.sin(phi), a * Math.cosh(mu) * Math.cos(nu));
	}
	/**
	 * Vector from ellipsoidal coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromEllipsoidal3D(final double a, final double b, final double c, final double lambda,
			final double mu, final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createVector3D(Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2) / (a2 - c2)),
				Math.sqrt((b2 - lambda) * (b2 - mu) * (b2 - nu) / (b2 - a2) / (b2 - c2)),
				Math.sqrt((c2 - lambda) * (c2 - mu) * (c2 - nu) / (c2 - a2) / (c2 - b2)));
	}
	/**
	 * Vector from elliptic coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromElliptic3D(final double a, final double mu, final double nu, final double z) {
		return createVector3D(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu), z);
	}
	/**
	 * Vector from toroidal coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromToroidal3D(final double a, final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector3D(Math.sinh(tau) * invdenom * Math.cos(phi), Math.sinh(tau) * invdenom * Math.sin(phi),
				Math.sin(sigma) * invdenom);
	}
	/**
	 * Vector from bispherical coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromBispherical3D(final double a, final double sigma, final double tau,
			final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector3D(Math.sin(sigma) * invdenom * Math.cos(phi), Math.sin(sigma) * invdenom * Math.sin(phi),
				Math.sinh(tau) * invdenom);
	}
	/**
	 * Vector from bipolar cylindrical coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromBipolarCylindrical3D(final double a, final double sigma, final double tau,
			final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector3D(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom, z);
	}
	/**
	 * Vector from conical coordinates
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
	 * @return 3D vector
	 */
	public static WB_Vector createVectorFromConical3D(final double b, final double c, final double r, final double mu,
			final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createVector3D(r * mu * nu / b / c, r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}
}
