/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;

import wblut.data.WB_JohnsonPolyhedraData01;
import wblut.data.WB_JohnsonPolyhedraData02;
import wblut.data.WB_JohnsonPolyhedraData03;
import wblut.data.WB_JohnsonPolyhedraData04;
import wblut.data.WB_PolyhedraData;
import wblut.external.QuickHull3D.WB_QuickHull3D;
import wblut.external.straightskeleton.Corner;
import wblut.external.straightskeleton.Edge;
import wblut.external.straightskeleton.Loop;
import wblut.external.straightskeleton.LoopL;
import wblut.external.straightskeleton.Machine;
import wblut.external.straightskeleton.Output.Face;
import wblut.external.straightskeleton.Point3d;
import wblut.external.straightskeleton.Skeleton;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_GeometryFactory extends WB_GeometryFactory2D {
	/**
	 *
	 */
	final WB_Point	origin;
	/**
	 *
	 */
	final WB_Vector	X;
	/**
	 *
	 */
	final WB_Vector	Y;
	/**
	 *
	 */
	final WB_Vector	Z;
	/**
	 *
	 */
	final WB_Vector	mX;
	/**
	 *
	 */
	final WB_Vector	mY;
	/**
	 *
	 */
	final WB_Vector	mZ;
	/**
	 *
	 */
	WB_Plane		XY;
	/**
	 *
	 */
	WB_Plane		YZ;
	/**
	 *
	 */
	WB_Plane		ZX;
	/**
	 *
	 */
	WB_Plane		YX;
	/**
	 *
	 */
	WB_Plane		ZY;
	/**
	 *
	 */
	WB_Plane		XZ;

	/**
	 *
	 */
	public WB_GeometryFactory() {
		super();
		origin = createPoint(0, 0, 0);
		X = createVector(1, 0, 0);
		Y = createVector(0, 1, 0);
		Z = createVector(0, 0, 1);
		mX = createVector(-1, 0, 0);
		mY = createVector(0, -1, 0);
		mZ = createVector(0, 0, -1);
	}

	/**
	 * Legacy code, WB_GeometryFactory used to be a singleton but this limited
	 * its use in multithreaded code.
	 *
	 * @return
	 */
	public static WB_GeometryFactory instance() {
		return new WB_GeometryFactory();
	}

	/**
	 *
	 * @return default origin
	 */
	public WB_Point origin() {
		return origin;
	}

	/**
	 *
	 * @return default X-axis direction
	 */
	public WB_Vector X() {
		return X;
	}

	/**
	 *
	 * @return default Y-axis direction
	 */
	public WB_Vector Y() {
		return Y;
	}

	/**
	 *
	 * @return default Z-axis direction
	 */
	public WB_Vector Z() {
		return Z;
	}

	/**
	 *
	 * @return negative X-axis direction
	 */
	public WB_Vector minX() {
		return mX;
	}

	/**
	 *
	 * @return negative Y-axis direction
	 */
	public WB_Vector minY() {
		return mY;
	}

	/**
	 *
	 * @return default Z-axis direction
	 */
	public WB_Vector minZ() {
		return mZ;
	}

	/**
	 *
	 * @return default XY-plane
	 */
	public WB_Plane XY() {
		if (XY == null) {
			XY = createPlane(origin(), Z());
		}
		return XY;
	}

	/**
	 *
	 * @return default YZ-plane
	 */
	public WB_Plane YZ() {
		if (YZ == null) {
			YZ = createPlane(origin(), X());
		}
		return YZ;
	}

	/**
	 *
	 * @return default ZX-plane
	 */
	public WB_Plane ZX() {
		if (ZX == null) {
			ZX = createPlane(origin(), Y());
		}
		return ZX;
	}

	/**
	 *
	 * @return default YX-plane
	 */
	public WB_Plane YX() {
		if (YX == null) {
			YX = createPlane(origin(), minZ());
		}
		return XY;
	}

	/**
	 *
	 * @return default ZY-plane
	 */
	public WB_Plane ZY() {
		if (ZY == null) {
			ZY = createPlane(origin(), minX());
		}
		return ZY;
	}

	/**
	 *
	 * @return default XZ-plane
	 */
	public WB_Plane XZ() {
		if (XZ == null) {
			XZ = createPlane(origin(), minY());
		}
		return XZ;
	}

	/**
	 * Create a new right-handed coordinate system. The WORLD CS is the default
	 * parent; the z-coordinate of X is ignored and X is normalized, Z is
	 * (0,0,1) and Y is created from X and Z
	 *
	 * @param origin
	 * @param X
	 *
	 * @return coordinate
	 */
	public WB_CoordinateSystem createCSFromOriginAndX(final WB_Coord origin,
			final WB_Coord X) {
		final WB_Point lOrigin = createPoint(origin.xd(), origin.yd(), 0);
		final WB_Vector lX = createNormalizedVector(X.xd(), X.yd(), 0);
		final WB_Vector lY = createVector(-lX.yd(), lX.xd());
		return createCSFromOriginAndXY(lOrigin, lX, lY);
	}

	/**
	 * Create a new right-handed coordinate ; the z-coordinate of X is ignored
	 * and X is normalized, Z is (0,0,1) and Y is created from X and Z.
	 *
	 * @param origin
	 * @param X
	 * @param parent
	 *            parent coordinate system
	 * @return coordinate system
	 */
	public WB_CoordinateSystem createCSFromOriginAndX(final WB_Coord origin,
			final WB_Coord X, final WB_CoordinateSystem parent) {
		final WB_Point lOrigin = createPoint(origin.xd(), origin.yd());
		final WB_Vector lX = createNormalizedVector(X.xd(), X.yd(), 0);
		final WB_Vector lY = createVector(-lX.yd(), lX.xd());
		return createCSFromOriginAndXY(lOrigin, lX, lY, parent);
	}

	/**
	 * Create a new right-handed coordinate system. The WORLD CS is the default
	 * parent; X is normalized, Y is normalized and orthogonalized and Z is
	 * created from X and Y
	 *
	 * @param origin
	 * @param X
	 * @param Y
	 *
	 * @return coordinate system
	 */
	public WB_CoordinateSystem createCSFromOriginAndXY(final WB_Coord origin,
			final WB_Coord X, final WB_Coord Y) {
		final WB_Vector lX = createNormalizedVector(X);
		WB_Vector lY = createNormalizedVector(Y);
		final WB_Vector lZ = lX.cross(lY);
		if (WB_Epsilon.isZeroSq(lZ.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		lZ.normalizeSelf();
		lY = createNormalizedVector(lZ.cross(lX));
		return new WB_CoordinateSystem(origin, lX, lY, lZ, WORLD());
	}

	/**
	 * Create a new right-handed coordinate with a defined parent. X is
	 * normalized, Y is normalized and orthogonalized and Z is created from X
	 * and Y
	 *
	 * @param origin
	 * @param X
	 * @param Y
	 * @param parent
	 *            parent coordinate system
	 *
	 * @return coordinate system
	 */
	public WB_CoordinateSystem createCSFromOriginAndXY(final WB_Coord origin,
			final WB_Coord X, final WB_Coord Y,
			final WB_CoordinateSystem parent) {
		final WB_Vector lX = createNormalizedVector(X);
		WB_Vector lY = createNormalizedVector(Y);
		final WB_Vector lZ = lX.cross(lY);
		if (WB_Epsilon.isZeroSq(lZ.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		lZ.normalizeSelf();
		lY = createNormalizedVector(lZ.cross(lX));
		return new WB_CoordinateSystem(origin, lX, lY, lZ, parent);
	}

	/**
	 *
	 *
	 * @param origin
	 * @param X
	 * @param Y
	 * @param Z
	 * @param parent
	 * @return
	 */
	public WB_CoordinateSystem createCSFromOriginAndXYZ(final WB_Coord origin,
			final WB_Coord X, final WB_Coord Y, final WB_Coord Z,
			final WB_CoordinateSystem parent) {
		return new WB_CoordinateSystem(origin, X, Y, Z, parent);
	}

	/**
	 *
	 *
	 * @param origin
	 * @param X
	 * @param Y
	 * @param Z
	 * @return
	 */
	public WB_CoordinateSystem createCSFromOriginAndXYZ(final WB_Coord origin,
			final WB_Coord X, final WB_Coord Y, final WB_Coord Z) {
		return new WB_CoordinateSystem(origin, X, Y, Z, WORLD());
	}

	/**
	 *
	 * @return default 2D context: XY plane
	 */
	public WB_Map2D createEmbeddedPlane() {
		return new WB_PlanarMap();
	}

	/**
	 * Create a 2D context from an offset coordinate plane.
	 *
	 * @param mode
	 *            0=YZ, 1=ZX, 2=XY, 3=ZY, 4=XZ, 5=YX
	 * @param offset
	 *            offset of the 2D context origin along plane normal
	 * @return 2D context
	 */
	public WB_Map2D createEmbeddedPlane(final int mode, final double offset) {
		return new WB_PlanarMap(mode, offset);
	}

	/**
	 * Create a 2D context from a coordinate plane.
	 *
	 * @param mode
	 *            0=YZ, 1=ZX, 2=XY, 3=ZY, 4=XZ, 5=YX
	 * @return 2D context
	 */
	public WB_Map2D createEmbeddedPlane(final int mode) {
		return new WB_PlanarMap(mode);
	}

	/**
	 * Create a 2D context from an offset plane.
	 *
	 * @param P
	 *            plane
	 * @param offset
	 *            offset of the 2D context origin along plane normal
	 * @return 2D context
	 */
	public WB_Map2D createEmbeddedPlane(final WB_Plane P, final double offset) {
		return new WB_PlanarMap(P, offset);
	}

	/**
	 * Create a 2D context from a plane.
	 *
	 * @param P
	 *            plane
	 * @return 2D context
	 */
	public WB_Map2D createEmbeddedPlane(final WB_Plane P) {
		return new WB_PlanarMap(P);
	}

	/**
	 * New point at origin.
	 *
	 * @return new point at origin
	 */
	public WB_Point createPoint() {
		return new WB_Point(0, 0, 0);
	}

	/**
	 * Create new point.If parameter p is same class as caller then the original
	 * point is returned. Unsafe if the point is aftwerwards modified with
	 * unsafe operators (_setSelf,_addSelf,_subSelf,..)
	 *
	 * @param p
	 *            point
	 * @return copy of point
	 */
	public WB_Point createPoint(final WB_Coord p) {
		return new WB_Point(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Point createPoint(final double[] p) {
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
	public WB_Point createPoint(final double _x, final double _y) {
		return createPoint(_x, _y, 0);
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
	public WB_Point createPoint(final double _x, final double _y,
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
	public WB_Point createInterpolatedPoint(final WB_Coord p, final WB_Coord q,
			final double f) {
		return new WB_Point((1.0 - f) * p.xd() + f * q.xd(),
				(1.0 - f) * p.yd() + f * q.yd(),
				(1.0 - f) * p.zd() + f * q.zd());
	}

	/**
	 * Incenter of triangle, z-ordinate is ignored.
	 *
	 * @param tri
	 *            triangle
	 * @return incenter
	 */
	public WB_Point createIncenter(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(1, 1, 1, tri);
	}

	/**
	 * Orthocenter of triangle, z-ordinate is ignored.
	 *
	 * @param tri
	 *            triangle
	 * @return orthocenter
	 */
	public WB_Point createOrthocenter(final WB_Triangle tri) {
		final double a2 = tri.a() * tri.a();
		final double b2 = tri.b() * tri.b();
		final double c2 = tri.c() * tri.c();
		return createPointFromBarycentricCoordinates(
				(a2 + b2 - c2) * (a2 - b2 + c2),
				(a2 + b2 - c2) * (-a2 + b2 + c2),
				(a2 - b2 + c2) * (-a2 + b2 + c2), tri);
	}

	/**
	 * Get point with triangle barycentric coordinates.
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param tri
	 *            triangle
	 * @return point wit barycentric coordinates (u,v,w)
	 */
	public WB_Point createPointFromBarycentricCoordinates(final double u,
			final double v, final double w, final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(u / tri.a(), v / tri.b(),
				w / tri.c(), tri);
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
	public WB_Point createPointFromCylindrical(final double r, final double phi,
			final double z) {
		return createPoint(r * Math.cos(phi), r * Math.sin(phi), z);
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
	public WB_Point createPointFromSpherical(final double r, final double theta,
			final double phi) {
		return createPoint(r * Math.cos(phi) * Math.sin(theta),
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
	public WB_Point createPointFromParaboloidal(final double sigma,
			final double tau, final double phi) {
		return createPoint(sigma * tau * Math.cos(phi),
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
	public WB_Point createPointFromParabolic(final double sigma,
			final double tau, final double z) {
		return createPoint(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
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
	public WB_Point createPointFromOblateSpheroidal(final double a,
			final double mu, final double nu, final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createPoint(common * Math.cos(phi), common * Math.sin(phi),
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
	public WB_Point createPointFromProlateSpheroidal(final double a,
			final double mu, final double nu, final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createPoint(common * Math.cos(phi), common * Math.sin(phi),
				a * Math.cosh(mu) * Math.cos(nu));
	}

	/**
	 * Point from ellipsoidal coordinates
	 * http://en.wikipedia.org/wiki/Ellipsoidal_coordinates
	 *
	 * lambda<c???<mu<b???<nu<a???
	 *
	 * @param a
	 *            ,b,c focus
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
	public WB_Point createPointFromEllipsoidal(final double a, final double b,
			final double c, final double lambda, final double mu,
			final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createPoint(
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
	public WB_Point createPointFromElliptic(final double a, final double mu,
			final double nu, final double z) {
		return createPoint(a * Math.cosh(mu) * Math.cos(nu),
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
	public WB_Point createPointFromToroidal(final double a, final double sigma,
			final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint(Math.sinh(tau) * invdenom * Math.cos(phi),
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
	public WB_Point createPointFromBispherical(final double a,
			final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint(Math.sin(sigma) * invdenom * Math.cos(phi),
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
	public WB_Point createPointFromBipolarCylindrical(final double a,
			final double sigma, final double tau, final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint(Math.sinh(tau) * invdenom,
				Math.sin(sigma) * invdenom, z);
	}

	/**
	 * Point from conical coordinates
	 * http://en.wikipedia.org/wiki/Conical_coordinates
	 *
	 * nu???<c???<mu???<b???
	 *
	 * @param b
	 *            ,c conical constants
	 * @param c
	 * @param r
	 *            radius
	 * @param mu
	 *            conical coordinate
	 * @param nu
	 *            conical coordinate
	 * @return 3D point
	 */
	public WB_Point createPointFromConical(final double b, final double c,
			final double r, final double mu, final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createPoint(r * mu * nu / b / c,
				r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}

	/**
	 * Centroid of triangle.
	 *
	 * @param tri
	 *            triangle
	 * @return centroid
	 */
	public WB_Point createCentroid(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(tri.b() * tri.c(),
				tri.c() * tri.a(), tri.a() * tri.b(), tri);
	}

	public WB_Point createCentroid(final WB_Coord... points) {
		WB_Point c = new WB_Point();
		for (int i = 0; i < points.length; i++) {
			c.addSelf(points[i]);
		}
		c.divSelf(points.length);
		return c;
	}

	/**
	 * Circumcenter of triangle.
	 *
	 * @param tri
	 *            triangle
	 * @return circumcenter
	 */
	public WB_Point createCircumcenter(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(tri.cosA(), tri.cosB(),
				tri.cosC(), tri);
	}

	/**
	 * Get point with triangle trilinear coordinates.
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param tri
	 *            triangle
	 * @return point wit trilinear coordinates (u,v,w)
	 */
	public WB_Point createPointFromTrilinearCoordinates(final double u,
			final double v, final double w, final WB_Triangle tri) {
		final double invabc = 1.0 / (tri.a() * u + tri.b() * v + tri.c() * w);
		final double bv = tri.b() * v;
		final double au = tri.a() * u;
		final double eax = ((tri.p2().xd() - tri.p3().xd()) * bv
				+ (tri.p1().xd() - tri.p3().xd()) * au) * invabc
				+ tri.p3().xd();
		final double eay = ((tri.p2().yd() - tri.p3().yd()) * bv
				+ (tri.p1().yd() - tri.p3().yd()) * au) * invabc
				+ tri.p3().yd();
		final double eaz = ((tri.p2().zd() - tri.p3().zd()) * bv
				+ (tri.p1().zd() - tri.p3().zd()) * au) * invabc
				+ tri.p3().zd();
		return createPoint(eax, eay, eaz);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public WB_Point createMidpoint(final WB_Coord p, final WB_Coord q) {
		return createPoint((p.xd() + q.xd()) * 0.5, (p.yd() + q.yd()) * 0.5,
				(p.zd() + q.zd()) * 0.5);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Point createMidpoint(final WB_Coord... p) {
		final WB_Point m = createPoint();
		for (final WB_Coord point : p) {
			m.addSelf(point);
		}
		m.divSelf(p.length);
		return m;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	private List<WB_Point> cleanPointlist(final List<WB_Point> points) {
		final List<WB_Point> result = new FastList<WB_Point>();
		final int n = points.size();
		for (int i = 0; i < n; i++) {
			if (!points.get(i).equals(points.get((i + 1) % n))) {
				result.add(points.get(i));
			}
		}
		return result;
	}

	/**
	 * New zero-length vector.
	 *
	 * @return zero-length vector
	 */
	public WB_Vector createVector() {
		return createVector(0, 0, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public WB_Vector createVectorFromTo(final WB_Coord p, final WB_Coord q) {
		return createVector(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
	}

	/**
	 * Copy of coordinate as vector.
	 *
	 * @param p
	 *            vector
	 * @return vector
	 */
	public final WB_Vector createVector(final WB_Coord p) {
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
	public WB_Vector createVector(final double _x, final double _y) {
		return createVector(_x, _y, 0);
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
	public WB_Vector createVector(final double _x, final double _y,
			final double _z) {
		return new WB_Vector(_x, _y, _z);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Vector createNormalizedVector(final WB_Coord p) {
		final WB_Vector vec = createVector(p);
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
	public WB_Vector createNormalizedVectorFromTo(final WB_Coord p,
			final WB_Coord q) {
		final WB_Vector vec = createVector(q.xd() - p.xd(), q.yd() - p.yd(),
				q.zd() - p.zd());
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
	public WB_Vector createNormalizedVector(final double _x, final double _y,
			final double _z) {
		final WB_Vector vec = createVector(_x, _y, _z);
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
	public WB_Vector createNormalizedVector(final double _x, final double _y,
			final double _z, final double _w) {
		final WB_Vector vec = createVector(_x, _y, _z);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedVector(final double _x, final double _y) {
		final WB_Vector vec = createVector(_x, _y);
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
	public WB_Vector createNormalizedPerpendicularVector(final double _x,
			final double _y) {
		final WB_Vector vec = createVector(-_y, _x, 0);
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
	public WB_Vector createNormalizedPerpendicularVector(final double _x,
			final double _y, final double _z) {
		if (_x > _y) {
			if (_y > _z) {
				return createNormalizedVector(-_y, _x, 0);
			} else {
				return createNormalizedVector(-_z, 0, _x);
			}
		} else {
			if (_x > _z) {
				return createNormalizedVector(-_y, _x, 0);
			} else {
				return createNormalizedVector(0, -_z, _x);
			}
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Vector createNormalizedPerpendicularVector(final WB_Coord p) {
		if (p.xd() > p.yd()) {
			if (p.yd() > p.zd()) {
				return createNormalizedVector(-p.yd(), p.xd(), 0);
			} else {
				return createNormalizedVector(-p.zd(), 0, p.xd());
			}
		} else {
			if (p.xd() > p.zd()) {
				return createNormalizedVector(-p.yd(), p.xd(), 0);
			} else {
				return createNormalizedVector(0, -p.zd(), p.xd());
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
	public WB_Vector createVectorFromCylindrical(final double r,
			final double phi, final double z) {
		return createVector(r * Math.cos(phi), r * Math.sin(phi), z);
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
	public WB_Vector createVectorFromSpherical(final double r,
			final double theta, final double phi) {
		return createVector(r * Math.cos(phi) * Math.sin(theta),
				r * Math.sin(phi) * Math.sin(theta), r * Math.cos(theta));
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
	public WB_Vector createVectorFromParaboloidal(final double sigma,
			final double tau, final double phi) {
		return createVector(sigma * tau * Math.cos(phi),
				sigma * tau * Math.sin(phi), 0.5 * (tau * tau - sigma * sigma));
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
	public WB_Vector createVectorFromParabolic(final double sigma,
			final double tau, final double z) {
		return createVector(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
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
	public WB_Vector createVectorFromOblateSpheroidal(final double a,
			final double mu, final double nu, final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createVector(common * Math.cos(phi), common * Math.sin(phi),
				a * Math.sinh(mu) * Math.sin(nu));
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
	public WB_Vector createVectorFromProlateSpheroidal(final double a,
			final double mu, final double nu, final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createVector(common * Math.cos(phi), common * Math.sin(phi),
				a * Math.cosh(mu) * Math.cos(nu));
	}

	/**
	 * Vector from ellipsoidal coordinates
	 * http://en.wikipedia.org/wiki/Ellipsoidal_coordinates
	 *
	 * lambda<c???<mu<b???<nu<a???
	 *
	 * @param a
	 *            ,b,c focus
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
	public WB_Vector createVectorFromEllipsoidal(final double a, final double b,
			final double c, final double lambda, final double mu,
			final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createVector(
				Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2)
						/ (a2 - c2)),
				Math.sqrt((b2 - lambda) * (b2 - mu) * (b2 - nu) / (b2 - a2)
						/ (b2 - c2)),
				Math.sqrt((c2 - lambda) * (c2 - mu) * (c2 - nu) / (c2 - a2)
						/ (c2 - b2)));
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
	public WB_Vector createVectorFromElliptic(final double a, final double mu,
			final double nu, final double z) {
		return createVector(a * Math.cosh(mu) * Math.cos(nu),
				a * Math.sinh(mu) * Math.cos(nu), z);
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
	public WB_Vector createVectorFromToroidal(final double a,
			final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector(Math.sinh(tau) * invdenom * Math.cos(phi),
				Math.sinh(tau) * invdenom * Math.sin(phi),
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
	public WB_Vector createVectorFromBispherical(final double a,
			final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector(Math.sin(sigma) * invdenom * Math.cos(phi),
				Math.sin(sigma) * invdenom * Math.sin(phi),
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
	public WB_Vector createVectorFromBipolarCylindrical(final double a,
			final double sigma, final double tau, final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector(Math.sinh(tau) * invdenom,
				Math.sin(sigma) * invdenom, z);
	}

	/**
	 * Vector from conical coordinates
	 * http://en.wikipedia.org/wiki/Conical_coordinates
	 *
	 * nu???<c???<mu???<b???
	 *
	 * @param b
	 *            ,c conical constants
	 * @param c
	 * @param r
	 *            radius
	 * @param mu
	 *            conical coordinate
	 * @param nu
	 *            conical coordinate
	 * @return 3D vector
	 */
	public WB_Vector createVectorFromConical(final double b, final double c,
			final double r, final double mu, final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createVector(r * mu * nu / b / c,
				r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}

	/**
	 * Get line through two points. The first point will become the origin
	 *
	 * @param p1
	 *            point 1
	 * @param p2
	 *            point 2
	 * @return line through points
	 */
	public WB_Line createLineThroughPoints(final WB_Coord p1,
			final WB_Coord p2) {
		return createLineWithDirection(p1, createVectorFromTo(p1, p2));
	}

	/**
	 * Get line through two points. The first point will become the origin
	 *
	 * @param x1
	 *            x-ordinate of point 1
	 * @param y1
	 *            y-ordinate of point 1
	 * @param x2
	 *            x-ordinate of point 2
	 * @param y2
	 *            y-ordinate of point 2
	 * @return line through points
	 */
	public WB_Line createLineThroughPoints(final double x1, final double y1,
			final double x2, final double y2) {
		return createLineWithDirection(createPoint(x1, y1),
				createVector(x2 - x1, y2 - y1));
	}

	// 3D
	/**
	 * Get line through two points. The first point will become the origin
	 *
	 * @param x1
	 *            x-ordinate of point 1
	 * @param y1
	 *            y-ordinate of point 1 * @param z1 z-ordinate of point 1
	 * @param z1
	 * @param x2
	 *            x-ordinate of point 2
	 * @param y2
	 *            y-ordinate of point 2
	 * @param z2
	 *            z-ordinate of point 2
	 * @return line through points
	 */
	public WB_Line createLineThroughPoints(final double x1, final double y1,
			final double z1, final double x2, final double y2,
			final double z2) {
		return createLineWithDirection(createPoint(x1, y1, z1),
				createVector(x2 - x1, y2 - y1, z2 - z1));
	}

	/**
	 * Get line through point with given direction.
	 *
	 * @param origin
	 *            point on line
	 * @param direction
	 *            direction
	 * @return line through point with direction
	 */
	public WB_Line createLineWithDirection(final WB_Coord origin,
			final WB_Coord direction) {
		return new WB_Line(origin, direction);
	}

	/**
	 * Get 3D line through point with given direction.
	 *
	 * @param ox
	 *            x-ordinate of origin
	 * @param oy
	 *            y-ordinate of origin
	 * @param oz
	 *            z-ordinate of origin
	 * @param dx
	 *            x-ordinate of direction
	 * @param dy
	 *            y-ordinate of direction
	 * @param dz
	 *            z-ordinate of direction
	 * @return 3D line through point with given direction
	 */
	public WB_Line createLineWithDirection(final double ox, final double oy,
			final double oz, final double dx, final double dy,
			final double dz) {
		return createLineWithDirection(createPoint(ox, oy, oz),
				createVector(dx, dy, dz));
	}

	/**
	 * Get a line parallel to a line and through point.
	 *
	 * @param L
	 *            line
	 * @param p
	 *            point
	 * @return parallel line through point
	 */
	public WB_Line createParallelLineThroughPoint(final WB_Line L,
			final WB_Coord p) {
		return createLineWithDirection(p, L.getDirection());
	}

	public WB_Plane createBisector(final WB_Coord p, final WB_Coord q) {
		return createPlane(createMidpoint(p, q), createVectorFromTo(p, q));
	}

	/**
	 * Get ray through two points. The first point will become the origin
	 *
	 * @param p1
	 *            point 1
	 * @param p2
	 *            point 2
	 * @return ray through points
	 */
	public WB_Ray createRayThroughPoints(final WB_Coord p1, final WB_Coord p2) {
		return createRayWithDirection(p1, createVector(p2).subSelf(p1));
	}

	/**
	 * Get ray through two points. The first point will become the origin
	 *
	 * @param x1
	 *            x-ordinate of point 1
	 * @param y1
	 *            y-ordinate of point 1
	 * @param x2
	 *            x-ordinate of point 2
	 * @param y2
	 *            y-ordinate of point 2
	 * @return ray through points
	 */
	public WB_Ray createRayThroughPoints(final double x1, final double y1,
			final double x2, final double y2) {
		return createRayWithDirection(createPoint(x1, y1),
				createVector(x2 - x1, y2 - y1));
	}

	// 3D
	/**
	 * Get ray through two points. The first point will become the origin
	 *
	 * @param x1
	 *            x-ordinate of point 1
	 * @param y1
	 *            y-ordinate of point 1 * @param z1 z-ordinate of point 1
	 * @param z1
	 * @param x2
	 *            x-ordinate of point 2
	 * @param y2
	 *            y-ordinate of point 2
	 * @param z2
	 *            z-ordinate of point 2
	 * @return ray through points
	 */
	public WB_Ray createRayThroughPoints(final double x1, final double y1,
			final double z1, final double x2, final double y2,
			final double z2) {
		return createRayWithDirection(createPoint(x1, y1, z1),
				createVector(x2 - x1, y2 - y1, z2 - z1));
	}

	/**
	 * Get ray through point with given direction.
	 *
	 * @param origin
	 *            point on line
	 * @param direction
	 *            direction
	 * @return ray through point with direction
	 */
	public WB_Ray createRayWithDirection(final WB_Coord origin,
			final WB_Coord direction) {
		return new WB_Ray(origin, direction);
	}

	/**
	 * Get 3D ray through point with given direction.
	 *
	 * @param ox
	 *            x-ordinate of origin
	 * @param oy
	 *            y-ordinate of origin
	 * @param oz
	 *            z-ordinate of origin
	 * @param dx
	 *            x-ordinate of direction
	 * @param dy
	 *            y-ordinate of direction
	 * @param dz
	 *            z-ordinate of direction
	 * @return 3D ray through point with given direction
	 */
	public WB_Ray createRayWithDirection(final double ox, final double oy,
			final double oz, final double dx, final double dy,
			final double dz) {
		return createRayWithDirection(createPoint(ox, oy, oz),
				createVector(dx, dy, dz));
	}

	/**
	 * Get a ray parallel to a line and through point.
	 *
	 * @param L
	 *            line
	 * @param p
	 *            point
	 * @return parallel line through point
	 */
	public WB_Ray createParallelRayThroughPoint(final WB_Line L,
			final WB_Coord p) {
		return createRayWithDirection(p, L.getDirection());
	}

	/**
	 * Get segment between two points. The first point will become the origin
	 *
	 * @param p1
	 *            point 1
	 * @param p2
	 *            point 2
	 * @return segment
	 */
	public WB_Segment createSegment(final WB_Coord p1, final WB_Coord p2) {
		return new WB_Segment(p1, p2);
	}

	/**
	 * Get segment between two points. The first point will become the origin
	 *
	 * @param origin
	 *            origin
	 * @param direction
	 *            direction
	 * @param length
	 *            length
	 * @return segment
	 */
	public WB_Segment createSegmentWithLength(final WB_Coord origin,
			final WB_Coord direction, final double length) {
		return createSegment(origin, createPoint(origin).addMulSelf(length,
				createNormalizedVector(direction)));
	}

	/**
	 * Get segment. The first point will become the origin
	 *
	 * @param x1
	 *            x-ordinate of point 1
	 * @param y1
	 *            y-ordinate of point 1
	 * @param x2
	 *            x-ordinate of point 2
	 * @param y2
	 *            y-ordinate of point 2
	 * @return line through points
	 */
	public WB_Segment createSegment(final double x1, final double y1,
			final double x2, final double y2) {
		return createSegment(createPoint(x1, y1), createVector(x2, y2));
	}

	/**
	 * Get segment from point, direction and length.
	 *
	 * @param ox
	 *            x-ordinate of origin
	 * @param oy
	 *            y-ordinate of origin
	 * @param dx
	 *            x-ordinate of direction
	 * @param dy
	 *            y-ordinate of direction
	 * @param length
	 *            length
	 * @return segment
	 */
	public WB_Segment createSegmentWithLength(final double ox, final double oy,
			final double dx, final double dy, final double length) {
		return createSegment(createPoint(ox, oy), createPoint(ox, oy)
				.addMul(length, createNormalizedVector(dx, dy, 0)));
	}

	// 3D
	/**
	 * Get segment. The first point will become the origin
	 *
	 * @param x1
	 *            x-ordinate of point 1
	 * @param y1
	 *            y-ordinate of point 1 * @param z1 z-ordinate of point 1
	 * @param z1
	 * @param x2
	 *            x-ordinate of point 2
	 * @param y2
	 *            y-ordinate of point 2
	 * @param z2
	 *            z-ordinate of point 2
	 * @return line through points
	 */
	public WB_Segment createSegment(final double x1, final double y1,
			final double z1, final double x2, final double y2,
			final double z2) {
		return createSegment(createPoint(x1, y1, z1), createVector(x2, y2, z2));
	}

	/**
	 * Get segment from point, direction and length.
	 *
	 * @param ox
	 *            x-ordinate of origin
	 * @param oy
	 *            y-ordinate of origin
	 * @param oz
	 *            z-ordinate of origin
	 * @param dx
	 *            x-ordinate of direction
	 * @param dy
	 *            y-ordinate of direction
	 * @param dz
	 *            z-ordinate of direction
	 * @param length
	 *            length
	 * @return segment
	 */
	public WB_Segment createSegmentWithLength(final double ox, final double oy,
			final double oz, final double dx, final double dy, final double dz,
			final double length) {
		return createSegment(createPoint(ox, oy, oz), createPoint(ox, oy, oz)
				.addMul(length, createNormalizedVector(dx, dy, dz)));
	}

	/**
	 *
	 *
	 * @param poly
	 * @param P
	 * @return
	 */
	public WB_Polygon[] splitSimplePolygon(final WB_Polygon poly,
			final WB_Plane P) {
		List<WB_Point> frontVerts = new FastList<WB_Point>();
		List<WB_Point> backVerts = new FastList<WB_Point>();
		final int numVerts = poly.getNumberOfPoints();
		final WB_Polygon[] polys = new WB_Polygon[2];
		if (numVerts > 0) {
			WB_Point a = new WB_Point(poly.getPoint(numVerts - 1));
			WB_Classification aSide = WB_GeometryOp.classifyPointToPlane3D(a,
					P);
			WB_Point b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				WB_Point intersection;
				b = new WB_Point(poly.getPoint(n));
				bSide = WB_GeometryOp.classifyPointToPlane3D(b, P);
				if (bSide == WB_Classification.FRONT) {
					if (aSide == WB_Classification.BACK) {
						intersection = getIntersection(b, a, P);
						frontVerts.add(intersection);
						backVerts.add(intersection);
					}
					frontVerts.add(b);
				} else if (bSide == WB_Classification.BACK) {
					if (aSide == WB_Classification.FRONT) {
						intersection = getIntersection(a, b, P);
						/*
						 * if (classifyPointToPlane(i.p1, WB_Point) !=
						 * ClassifyPointToPlane.POINT_ON_PLANE) { System.out
						 * .println("Inconsistency: intersection not on plane");
						 * }
						 */
						frontVerts.add(intersection);
						backVerts.add(intersection);
					} else if (aSide == WB_Classification.ON) {
						backVerts.add(a);
					}
					backVerts.add(b);
				} else {
					frontVerts.add(b);
					if (aSide == WB_Classification.BACK) {
						backVerts.add(b);
					}
				}
				a = b;
				aSide = bSide;
			}
			frontVerts = cleanPointlist(frontVerts);
			backVerts = cleanPointlist(backVerts);
			if (frontVerts.size() > 2) {
				polys[0] = createSimplePolygon(frontVerts);
			}
			if (backVerts.size() > 2) {
				polys[1] = createSimplePolygon(backVerts);
			}
		}
		return polys;
	}

	/**
	 * Get triangle from 3 points.
	 *
	 * @param p1x
	 *            x-ordinate of first point of triangle
	 * @param p1y
	 *            y-ordinate of first point of triangle
	 * @param p1z
	 *            z-ordinate of first point of triangle
	 * @param p2x
	 *            x-ordinate of second point of triangle
	 * @param p2y
	 *            y-ordinate of second point of triangle
	 * @param p2z
	 *            z-ordinate of second point of triangle
	 * @param p3x
	 *            x-ordinate of third point of triangle
	 * @param p3y
	 *            y-ordinate of third point of triangle
	 * @param p3z
	 *            z-ordinate of third point of triangle
	 * @return triangle
	 */
	public WB_Triangle createTriangle(final double p1x, final double p1y,
			final double p1z, final double p2x, final double p2y,
			final double p2z, final double p3x, final double p3y,
			final double p3z) {
		return createTriangle(createPoint(p1x, p1y, p1z),
				createPoint(p2x, p2y, p2z), createPoint(p3x, p3y, p3z));
	}

	/**
	 * Get triangle from 3 points.
	 *
	 * @param p1
	 *            first point of triangle
	 * @param p2
	 *            second point of triangle
	 * @param p3
	 *            third point of triangle
	 * @return triangle
	 */
	public WB_Triangle createTriangle(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3) {
		return new WB_Triangle(p1, p2, p3);
	}

	public WB_Point createInversionPoint(final WB_Coord p,
			final WB_Sphere inversionSphere) {
		final double r2 = inversionSphere.getRadius()
				* inversionSphere.getRadius();
		final double OP = WB_CoordOp
				.getDistance3D(inversionSphere.getCenter(), p);
		if (WB_Epsilon.isZero(OP)) {
			return null;
		}
		final double OPp = r2 / OP;
		final WB_Vector v = createNormalizedVectorFromTo(
				inversionSphere.getCenter(), p);
		return createPoint(
				WB_Point.addMul(inversionSphere.getCenter(), OPp, v));
	}

	/**
	 * Inversion of circle C over circle inversionCircle
	 * http://mathworld.wolfram.com/Inversion.html
	 *
	 * @param C
	 *            circle
	 * @param inversionCircle
	 *            inversion circle
	 *
	 * @return of circle C over circle inversionCircle, null if C is tangent to
	 *         inversionCircle
	 */
	public WB_Circle createInversionCircle(final WB_Circle C,
			final WB_Circle inversionCircle) {
		if (WB_GeometryOp.classifyPointToCircle2D(inversionCircle.getCenter(),
				C) == WB_Classification.ON) {
			return null;
		}
		final double x0 = inversionCircle.getCenter().xd();
		final double y0 = inversionCircle.getCenter().yd();
		final double k = inversionCircle.getRadius();
		final double k2 = k * k;
		final double s = k2 / (WB_CoordOp.getSqDistance3D(C.getCenter(),
				inversionCircle.getCenter()) - C.getRadius() * C.getRadius());
		return createCircleWithRadius(x0 + s * (C.getCenter().xd() - x0),
				y0 + s * (C.getCenter().yd() - y0),
				Math.abs(s) * C.getRadius());
	}

	/**
	 *
	 *
	 * @param tri
	 * @return
	 */
	public WB_Circle createCircumcircle3D(final WB_Triangle tri) {
		WB_Plane P = tri.getPlane();
		if (P == null) {
			return createCircleWithRadius(createCentroid(tri),
					new WB_Vector(0, 0, 1), 0.0);
		}
		final double a = tri.a();
		final double b = tri.b();
		final double c = tri.c();
		final double radius = a * b * c
				/ Math.sqrt(2 * a * a * b * b + 2 * b * b * c * c
						+ 2 * a * a * c * c - a * a * a * a - b * b * b * b
						- c * c * c * c);
		return createCircleWithRadius(createCircumcenter(tri),
				tri.getPlane().getNormal(), radius);
	}

	/**
	 *
	 *
	 * @param tri
	 * @return
	 */
	public WB_Circle createIncircle(final WB_Triangle tri) {
		WB_Plane P = tri.getPlane();
		if (P == null) {
			return createCircleWithRadius(createCentroid(tri),
					new WB_Vector(0, 0, 1), 0.0);
		}
		final double a = tri.a();
		final double b = tri.b();
		final double c = tri.c();
		final double invabc = 1.0 / (a + b + c);
		final double radius = 0.5
				* Math.sqrt((b + c - a) * (c + a - b) * (a + b - c) * invabc);
		final double x = (tri.p1().xd() * a + tri.p2().xd() * b
				+ tri.p3().xd() * c) * invabc;
		final double y = (tri.p1().yd() * a + tri.p2().yd() * b
				+ tri.p3().yd() * c) * invabc;
		final double z = (tri.p1().zd() * a + tri.p2().zd() * b
				+ tri.p3().zd() * c) * invabc;
		return createCircleWithRadius(createPoint(x, y, z),
				tri.getPlane().getNormal(), radius);
	}

	/**
	 * Get plane through point on plane with normal direction.
	 *
	 * @param origin
	 *            point on plane
	 * @param normal
	 * @return plane
	 */
	public WB_Plane createPlane(final WB_Coord origin, final WB_Coord normal) {
		return new WB_Plane(origin, normal);
	}

	/**
	 * Get plane through point on plane with normal direction.
	 *
	 * @param ox
	 *            x-ordinate of point on plane
	 * @param oy
	 *            y-ordinate of point on plane
	 * @param oz
	 *            z-ordinate of point on plane
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return plane
	 */
	public WB_Plane createPlane(final double ox, final double oy,
			final double oz, final double nx, final double ny,
			final double nz) {
		return new WB_Plane(createPoint(ox, oy, oz), createVector(nx, ny, nz));
	}

	/**
	 * Get plane through 3 points.
	 *
	 * @param p1
	 *            point on plane
	 * @param p2
	 *            point on plane
	 * @param p3
	 *            point on plane
	 * @return plane
	 */
	public WB_Plane createPlane(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3) {
		final WB_Vector v21 = createVectorFromTo(p1, p2);
		final WB_Vector v31 = createVectorFromTo(p1, p3);
		return new WB_Plane(p1, v21.crossSelf(v31));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	public WB_Plane createPlane(final WB_Triangle T) {
		return new WB_Plane(T.p1(), T.p2(), T.p3());
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public WB_Plane createFlippedPlane(final WB_Plane P) {
		return new WB_Plane(P.getOrigin(), P.getNormal().mul(-1));
	}

	/**
	 * Get plane through point on plane with normal direction.
	 *
	 * @param origin
	 *            point on plane
	 * @param normal
	 * @param offset
	 *            offset
	 * @return plane
	 */
	public WB_Plane createOffsetPlane(final WB_Coord origin,
			final WB_Coord normal, final double offset) {
		return new WB_Plane(createPoint(origin).addMulSelf(offset, normal),
				normal);
	}

	/**
	 * Get plane through point on plane with normal direction.
	 *
	 * @param ox
	 *            x-ordinate of point on plane
	 * @param oy
	 *            y-ordinate of point on plane
	 * @param oz
	 *            z-ordinate of point on plane
	 * @param nx
	 * @param ny
	 * @param nz
	 * @param offset
	 *            offset
	 * @return plane
	 */
	public WB_Plane createOffsetPlane(final double ox, final double oy,
			final double oz, final double nx, final double ny, final double nz,
			final double offset) {
		return new WB_Plane(createPoint(ox + offset * nx, oy + offset * ny,
				oz + offset * nz), createVector(nx, ny, nz));
	}

	/**
	 * Get offset plane through 3 points.
	 *
	 * @param p1
	 *            point on plane
	 * @param p2
	 *            point on plane
	 * @param p3
	 *            point on plane
	 * @param offset
	 *            offset
	 * @return plane
	 */
	public WB_Plane createOffsetPlane(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3, final double offset) {
		final WB_Vector v21 = createVectorFromTo(p1, p2);
		final WB_Vector v31 = createVectorFromTo(p1, p3);
		final WB_Vector n = v21.crossSelf(v31);
		n.normalizeSelf();
		return new WB_Plane(createPoint(p1).addMulSelf(offset, n), n);
	}

	/**
	 *
	 *
	 * @param points
	 * @param faces
	 * @return
	 */
	public WB_SimpleMesh createMesh(final WB_Coord[] points, final int[][] faces) {
		return new WB_SimpleMesh(points, faces);
	}

	/**
	 *
	 *
	 * @param points
	 * @param faces
	 * @return
	 */
	public WB_SimpleMesh createMesh(final Collection<? extends WB_Coord> points,
			final int[][] faces) {
		return new WB_SimpleMesh(points, faces);
	}

	/**
	 *
	 *
	 * @param aabb
	 * @return
	 */
	public WB_SimpleMesh createMesh(final WB_AABB aabb) {
		return createMesh(aabb.getCorners(), aabb.getFaces());
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public WB_SimpleMesh createUniqueMesh(final WB_SimpleMesh mesh) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final IntIntHashMap oldnew = new IntIntHashMap();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = mesh.getVertex(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		oldnew.put(0, 0);
		int nuv = 1;
		for (int i = 1; i < mesh.getNumberOfVertices(); i++) {
			v = mesh.getVertex(i);
			neighbor = kdtree.getNearestNeighbor(v);
			if (neighbor.d2 < WB_Epsilon.SQEPSILON) {
				oldnew.put(i, neighbor.value);
			} else {
				kdtree.add(v, nuv);
				uniqueVertices.add(v);
				oldnew.put(i, nuv++);
			}
		}
		final int[][] newfaces = new int[mesh.getNumberOfFaces()][];
		for (int i = 0; i < mesh.getNumberOfFaces(); i++) {
			final int[] face = mesh.getFace(i);
			newfaces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				newfaces[i][j] = oldnew.get(face[j]);
			}
		}
		return createMesh(uniqueVertices, newfaces);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param threshold
	 * @return
	 */
	public WB_SimpleMesh createUniqueMesh(final WB_SimpleMesh mesh,
			final double threshold) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final IntIntHashMap oldnew = new IntIntHashMap();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		final double t2 = threshold * threshold;
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = mesh.getVertex(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		oldnew.put(0, 0);
		int nuv = 1;
		for (int i = 1; i < mesh.getNumberOfVertices(); i++) {
			v = mesh.getVertex(i);
			neighbor = kdtree.getNearestNeighbor(v);
			if (neighbor.d2 < t2) {
				oldnew.put(i, neighbor.value);
			} else {
				kdtree.add(v, nuv);
				uniqueVertices.add(v);
				oldnew.put(i, nuv++);
			}
		}
		final int[][] newfaces = new int[mesh.getNumberOfFaces()][];
		for (int i = 0; i < mesh.getNumberOfFaces(); i++) {
			final int[] face = mesh.getFace(i);
			newfaces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				newfaces[i][j] = oldnew.get(face[j]);
			}
		}
		return createMesh(uniqueVertices, newfaces);
	}

	/**
	 *
	 *
	 * @param n
	 * @param radius
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createRegularPrism(final int n, final double radius,
			final double h) {
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		for (int i = 0; i < n; i++) {
			lpoints.add(createPoint(radius * Math.cos(Math.PI * 2.0 / n * i),
					radius * Math.sin(Math.PI * 2.0 / n * i), 0));
			lpoints.add(createPoint(radius * Math.cos(Math.PI * 2.0 / n * i),
					radius * Math.sin(Math.PI * 2.0 / n * i), h));
		}
		return createMesh(lpoints, createPrismFaces(n));
	}

	/**
	 *
	 *
	 * @param points
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createPrism(final Collection<? extends WB_Coord> points,
			final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createPrismFaces(points.size()));
	}

	/**
	 *
	 *
	 * @param points
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createPrismOpen(final Collection<? extends WB_Coord> points,
			final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createPrismFacesOpen(points.size()));
	}

	/**
	 *
	 *
	 * @param points
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createPrism(final WB_Coord[] points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createPrismFaces(points.length));
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	private int[][] createPrismFaces(final int n) {
		final int[][] faces = new int[2 + n][];
		faces[n] = new int[n];
		faces[n + 1] = new int[n];
		for (int i = 0; i < n; i++) {
			faces[n][n - i - 1] = 2 * i;
			faces[n + 1][i] = 2 * i + 1;
		}
		for (int i = 0; i < n; i++) {
			faces[i] = new int[4];
			faces[i][0] = 2 * i;
			faces[i][1] = 2 * ((i + 1) % n);
			faces[i][2] = 2 * ((i + 1) % n) + 1;
			faces[i][3] = 2 * i + 1;
		}
		return faces;
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	private int[][] createPrismFacesOpen(final int n) {
		final int[][] faces = new int[1 + n][];
		faces[n] = new int[n];
		for (int i = 0; i < n; i++) {
			faces[n][i] = 2 * i + 1;
		}
		for (int i = 0; i < n; i++) {
			faces[i] = new int[4];
			faces[i][0] = 2 * i;
			faces[i][1] = 2 * ((i + 1) % n);
			faces[i][2] = 2 * ((i + 1) % n) + 1;
			faces[i][3] = 2 * i + 1;
		}
		return faces;
	}

	public WB_SimpleMesh createPrism(final WB_Polygon poly, final double h) {
		return createPrism(poly, h, 0);
	}

	public WB_SimpleMesh createPrism(final WB_Polygon poly, final double h,
			final double offset) {
		if (h == 0) {
			return createMesh(poly, offset);
		}
		WB_Vector N = poly.getPlane().getNormal();
		final WB_Vector offset1 = N.mul(offset);
		final WB_Vector offset2 = N.mul(offset + h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Coord point;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			point = poly.getPoint(i);
			lpoints.add(createPoint(point).addSelf(offset1));
			lpoints.add(createPoint(point).addSelf(offset2));
		}
		final int numfaces = poly.getNumberOfPoints();
		final int[] triangles = poly.getTriangles();
		final int[][] prismfaces = new int[2 * triangles.length / 3
				+ numfaces][];
		int index = 0;
		for (int i = 0; i < triangles.length; i += 3) {
			prismfaces[index] = new int[3];
			prismfaces[index][0] = 2 * triangles[i];
			prismfaces[index][1] = 2 * triangles[i + 2];
			prismfaces[index][2] = 2 * triangles[i + 1];
			index++;
			prismfaces[index] = new int[3];
			prismfaces[index][0] = 2 * triangles[i] + 1;
			prismfaces[index][1] = 2 * triangles[i + 1] + 1;
			prismfaces[index][2] = 2 * triangles[i + 2] + 1;
			index++;
		}
		final int[] npc = poly.getNumberOfPointsPerContour();
		int start = 0;
		for (int j = 0; j < poly.getNumberOfContours(); j++) {
			final int n = npc[j];
			for (int i = 0; i < n; i++) {
				prismfaces[index] = new int[4];
				prismfaces[index][0] = 2 * (start + i);
				prismfaces[index][1] = 2 * (start + (i + 1) % n);
				prismfaces[index][2] = 2 * (start + (i + 1) % n) + 1;
				prismfaces[index][3] = 2 * (start + i) + 1;
				index++;
			}
			start += n;
		}
		return createMesh(lpoints, prismfaces);
	}

	public WB_SimpleMesh createMesh(final WB_Polygon poly) {
		return createMesh(poly, 0);
	}

	public WB_SimpleMesh createMesh(final WB_Polygon poly, final double offset) {
		WB_Vector N = poly.getPlane().getNormal();
		final WB_Vector offset1 = N.mul(offset);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Coord point;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			point = poly.getPoint(i);
			lpoints.add(createPoint(point).addSelf(offset1));
		}
		final int[] triangles = poly.getTriangles();
		final int[][] prismfaces = new int[triangles.length / 3][];
		int index = 0;
		for (int i = 0; i < triangles.length; i += 3) {
			prismfaces[index] = new int[3];
			prismfaces[index][0] = triangles[i];
			prismfaces[index][1] = triangles[i + 2];
			prismfaces[index][2] = triangles[i + 1];
			index++;
		}
		return createMesh(lpoints, prismfaces);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createPrismOpen(final WB_Polygon poly, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Coord point;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			point = poly.getPoint(i);
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		final int n = poly.getNumberOfPoints();
		final int[][] faces = new int[1 + n][];
		faces[n] = new int[n];
		for (int i = 0; i < n; i++) {
			faces[n][i] = 2 * i + 1;
		}
		for (int i = 0; i < n; i++) {
			faces[i] = new int[4];
			faces[i][0] = 2 * i;
			faces[i][1] = 2 * ((i + 1) % n);
			faces[i][2] = 2 * ((i + 1) % n) + 1;
			faces[i][3] = 2 * i + 1;
		}
		return createMesh(lpoints, faces);
	}

	/**
	 *
	 *
	 * @param n
	 * @param radius
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createRegularAntiPrism(final int n, final double radius,
			final double h) {
		final List<WB_Point> points = new FastList<WB_Point>();
		for (int i = 0; i < n; i++) {
			points.add(createPoint(radius * Math.cos(Math.PI * 2.0 / n * i),
					radius * Math.sin(Math.PI * 2.0 / n * i), 0));
			points.add(createPoint(
					radius * Math.cos(Math.PI * 2.0 / n * (i + 0.5)),
					radius * Math.sin(Math.PI * 2.0 / n * (i + 0.5)), h));
		}
		return createMesh(points, createAntiprismFaces(n));
	}

	/**
	 *
	 *
	 * @param points
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createAntiPrism(final Collection<? extends WB_Coord> points,
			final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createAntiprismFaces(points.size()));
	}

	/**
	 *
	 *
	 * @param points
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createAntiPrism(final WB_Coord[] points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createAntiprismFaces(points.length));
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	private int[][] createAntiprismFaces(final int n) {
		final int[][] faces = new int[2 + 2 * n][];
		faces[2 * n] = new int[n];
		faces[2 * n + 1] = new int[n];
		for (int i = 0; i < n; i++) {
			faces[2 * n][n - i - 1] = 2 * i;
			faces[2 * n + 1][i] = 2 * i + 1;
		}
		for (int i = 0; i < n; i++) {
			faces[2 * i] = new int[3];
			faces[2 * i][0] = 2 * i;
			faces[2 * i][1] = 2 * ((i + 1) % n);
			faces[2 * i][2] = 2 * i + 1;
			faces[2 * i + 1] = new int[3];
			faces[2 * i + 1][0] = 2 * i + 1;
			faces[2 * i + 1][1] = 2 * ((i + 1) % n);
			faces[2 * i + 1][2] = 2 * ((i + 1) % n) + 1;
		}
		return faces;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param h
	 * @return
	 */
	public WB_SimpleMesh createAntiPrism(final WB_Polygon poly, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Coord point;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			point = poly.getPoint(i);
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		final int numfaces = poly.getNumberOfPoints();
		final int[] triangles = poly.getTriangles();
		final int[][] prismfaces = new int[2 * triangles.length / 3
				+ 2 * numfaces][];
		int index = 0;
		for (int i = 0; i < triangles.length; i += 3) {
			prismfaces[index] = new int[3];
			prismfaces[index][0] = 2 * triangles[i];
			prismfaces[index][1] = 2 * triangles[i + 2];
			prismfaces[index][2] = 2 * triangles[i + 1];
			index++;
			prismfaces[index] = new int[3];
			prismfaces[index][0] = 2 * triangles[i] + 1;
			prismfaces[index][1] = 2 * triangles[i + 1] + 1;
			prismfaces[index][2] = 2 * triangles[i + 2] + 1;
			index++;
		}
		final int[] npc = poly.getNumberOfPointsPerContour();
		int start = 0;
		for (int j = 0; j < poly.getNumberOfContours(); j++) {
			final int n = npc[j];
			for (int i = 0; i < n; i++) {
				prismfaces[index] = new int[3];
				prismfaces[index][0] = 2 * (start + i);
				prismfaces[index][1] = 2 * (start + (i + 1) % n);
				prismfaces[index][2] = 2 * (start + i) + 1;
				index++;
				prismfaces[index] = new int[3];
				prismfaces[index][0] = 2 * (start + i) + 1;
				prismfaces[index][1] = 2 * (start + (i + 1) % n);
				prismfaces[index][2] = 2 * (start + (i + 1) % n) + 1;
				index++;
			}
			start += n;
		}
		return createMesh(lpoints, prismfaces);
	}

	/**
	 *
	 *
	 * @param type
	 * @param edgeLength
	 * @return
	 */
	public WB_SimpleMesh createArchimedes(final int type, final double edgeLength) {
		final List<WB_Point> vertices;
		final int[][] faces;
		vertices = createVerticesFromArray(WB_PolyhedraData.Avertices[type]);
		faces = WB_PolyhedraData.Afaces[type];
		final WB_Point p0 = vertices.get(faces[0][0]);
		final WB_Point p1 = vertices.get(faces[0][1]);
		final double el = p0.getDistance(p1);
		final double scale = edgeLength / el;
		final WB_Point cog = createPoint();
		for (final WB_Point p : vertices) {
			p.mulSelf(scale);
			cog.addSelf(p);
		}
		cog.div(vertices.size());
		for (final WB_Point p : vertices) {
			p.subSelf(cog);
		}
		return createMesh(vertices, faces);
	}

	/**
	 *
	 *
	 * @param type
	 * @param edgeLength
	 * @return
	 */
	public WB_SimpleMesh createCatalan(final int type, final double edgeLength) {
		final List<WB_Point> vertices;
		final int[][] faces;
		vertices = createVerticesFromArray(WB_PolyhedraData.Cvertices[type]);
		faces = WB_PolyhedraData.Cfaces[type];
		final WB_Point p0 = vertices.get(faces[0][0]);
		final WB_Point p1 = vertices.get(faces[0][1]);
		final double el = p0.getDistance(p1);
		final double scale = edgeLength / el;
		final WB_Point cog = createPoint();
		for (final WB_Point p : vertices) {
			p.mulSelf(scale);
			cog.addSelf(p);
		}
		cog.div(vertices.size());
		for (final WB_Point p : vertices) {
			p.subSelf(cog);
		}
		return createMesh(vertices, faces);
	}

	/**
	 *
	 *
	 * @param vertices
	 * @return
	 */
	private List<WB_Point> createVerticesFromArray(final double[][] vertices) {
		final List<WB_Point> points = new FastList<WB_Point>();
		for (final double[] vertice : vertices) {
			points.add(createPoint(vertice[0], vertice[1], vertice[2]));
		}
		return points;
	}

	/**
	 * Johnson polyhedra.
	 *
	 * Implemented by Frederik Vanhoutte (W:Blut), painstakingly collected by
	 * David Marec. Many thanks, without David this wouldn't be here.
	 *
	 * @param type
	 * @param edgeLength
	 * @return
	 */
	public WB_SimpleMesh createJohnson(final int type, final double edgeLength) {
		final List<WB_Point> vertices;
		final int[][] faces;
		if (type < 23) {
			vertices = createVerticesFromArray(
					WB_JohnsonPolyhedraData01.vertices[type]);
			faces = WB_JohnsonPolyhedraData01.faces[type];
		} else if (type < 46) {
			vertices = createVerticesFromArray(
					WB_JohnsonPolyhedraData02.vertices[type - 23]);
			faces = WB_JohnsonPolyhedraData02.faces[type - 23];
		} else if (type < 70) {
			vertices = createVerticesFromArray(
					WB_JohnsonPolyhedraData03.vertices[type - 46]);
			faces = WB_JohnsonPolyhedraData03.faces[type - 46];
		} else {
			vertices = createVerticesFromArray(
					WB_JohnsonPolyhedraData04.vertices[type - 70]);
			faces = WB_JohnsonPolyhedraData04.faces[type - 70];
		}
		final WB_Point p0 = vertices.get(faces[0][0]);
		final WB_Point p1 = vertices.get(faces[0][1]);
		final double el = p0.getDistance(p1);
		final double scale = edgeLength / el;
		final WB_Point cog = createPoint();
		for (final WB_Point p : vertices) {
			p.mulSelf(scale);
			cog.addSelf(p);
		}
		cog.div(vertices.size());
		for (final WB_Point p : vertices) {
			p.subSelf(cog);
		}
		return createMesh(vertices, faces);
	}

	/**
	 *
	 *
	 * @param type
	 * @param edgeLength
	 * @return
	 */
	public WB_SimpleMesh createOtherPolyhedron(final int type,
			final double edgeLength) {
		final List<WB_Point> vertices;
		final int[][] faces;
		vertices = createVerticesFromArray(WB_PolyhedraData.Overtices[type]);
		faces = WB_PolyhedraData.Ofaces[type];
		final WB_Point p0 = vertices.get(faces[0][0]);
		final WB_Point p1 = vertices.get(faces[0][1]);
		final double el = p0.getDistance(p1);
		final double scale = edgeLength / el;
		final WB_Point cog = createPoint();
		for (final WB_Point p : vertices) {
			p.mulSelf(scale);
			cog.addSelf(p);
		}
		cog.div(vertices.size());
		for (final WB_Point p : vertices) {
			p.subSelf(cog);
		}
		return createMesh(vertices, faces);
	}

	/**
	 *
	 *
	 * @param type
	 * @param edgeLength
	 * @return
	 */
	public WB_SimpleMesh createPlato(final int type, final double edgeLength) {
		final List<WB_Point> vertices;
		final int[][] faces;
		vertices = createVerticesFromArray(WB_PolyhedraData.Pvertices[type]);
		faces = WB_PolyhedraData.Pfaces[type];
		final WB_Point p0 = vertices.get(faces[0][0]);
		final WB_Point p1 = vertices.get(faces[0][1]);
		final double el = p0.getDistance(p1);
		final double scale = edgeLength / el;
		final WB_Point cog = createPoint();
		for (final WB_Point p : vertices) {
			p.mulSelf(scale);
			cog.addSelf(p);
		}
		cog.div(vertices.size());
		for (final WB_Point p : vertices) {
			p.subSelf(cog);
		}
		return createMesh(vertices, faces);
	}

	/**
	 *
	 *
	 * @param name
	 * @param radius
	 * @return
	 */
	public WB_SimpleMesh createPolyhedronFromWRL(String name, final double radius) {
		final BufferedReader br = new BufferedReader(
				new InputStreamReader(this.getClass().getClassLoader()
						.getResourceAsStream("resources/" + name + ".wrl")));
		final List<WB_Point> points = new FastList<WB_Point>();
		final List<int[]> faces = new FastList<int[]>();
		String line;
		String[] words;
		try {
			while ((line = br.readLine()) != null) {
				if (line.contains("Title Info")) {
					line = br.readLine().trim();
					words = line.split("\"");
					name = words[1].trim();
				}
				if (line.contains("Coordinate3")) {
					line = br.readLine().trim();
					line = br.readLine().trim();
					while (!line.contains("]")) {
						words = line.split("\\s+");
						words[2] = words[2].substring(0, words[2].length() - 1);
						points.add(
								createPoint(Double.parseDouble(words[0].trim()),
										Double.parseDouble(words[1].trim()),
										Double.parseDouble(words[2].trim())));
						line = br.readLine().trim();
					}
				}
				if (line.contains("IndexedFaceSet")) {
					line = br.readLine().trim();
					line = br.readLine().trim();
					while (!line.contains("]")) {
						words = line.split(",");
						final int[] face = new int[words.length - 1];
						for (int i = 0; i < words.length - 1; i++) {
							face[i] = Integer.parseInt(words[i].trim());
						}
						faces.add(face);
						line = br.readLine().trim();
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		final int[][] ifaces = new int[faces.size()][];
		for (int i = 0; i < faces.size(); i++) {
			ifaces[i] = faces.get(i);
		}
		double d2 = 0;
		for (final WB_Point p : points) {
			d2 = Math.max(d2, p.getSqLength());
		}
		d2 = radius / Math.sqrt(d2);
		for (final WB_Point p : points) {
			p.mulSelf(d2);
		}
		return createMesh(points, ifaces);
	}

	/**
	 *
	 *
	 * @param vectors
	 * @param scale
	 * @return
	 */
	public WB_SimpleMesh createZonohedron(final WB_Coord[] vectors,
			final double scale) {
		final int n = vectors.length;
		if (n < 3) {
			return null;
		}
		final int nop = (int) Math.pow(2, n);
		final List<WB_Point> points = new FastList<WB_Point>();
		for (int i = 0; i < nop; i++) {
			final WB_Point point = createPoint();
			int div = i;
			for (int p = 0; p < n; p++) {
				if (div % 2 == 0) {
					point.subSelf(vectors[p]);
				} else {
					point.addSelf(vectors[p]);
				}
				div = div / 2;
			}
			point.mulSelf(scale);
			points.add(point);
		}
		return createConvexHull(points, false);
	}

	/**
	 *
	 *
	 * @param type
	 * @param radius
	 * @return
	 */
	public WB_SimpleMesh createStellatedIcosahedron(final int type,
			final double radius) {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream(
						"resources/stellated_icosahedron1-59.txt")));
		final List<WB_Point> points = new ArrayList<WB_Point>();
		final List<int[]> faces = new ArrayList<int[]>();
		String thisline;
		String[] pointindices;
		String[] faceindices;
		String[] coordinates;
		String[] facedata;
		try {
			thisline = br.readLine();
			pointindices = thisline.split("\\s+");
			final int startpoint = Integer.parseInt(pointindices[2 * type]);
			final int endpoint = Integer.parseInt(pointindices[2 * type + 1]);
			thisline = br.readLine();
			faceindices = thisline.split("\\s+");
			final int startface = Integer.parseInt(faceindices[2 * type]);
			final int endface = Integer.parseInt(faceindices[2 * type + 1]);
			int currentline = 2;
			while ((thisline = br.readLine()) != null
					&& currentline <= endface) {
				if (currentline >= startpoint && currentline <= endpoint) {
					coordinates = thisline.split(",");
					points.add(createPoint(Double.parseDouble(coordinates[0]),
							Double.parseDouble(coordinates[1]),
							Double.parseDouble(coordinates[2])));
				}
				if (currentline >= startface && currentline <= endface) {
					facedata = thisline.split(",");
					final int[] face = new int[facedata.length];
					for (int i = 0; i < facedata.length; i++) {
						face[i] = Integer.parseInt(facedata[i]);
					}
					faces.add(face);
				}
				currentline++;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		final int[][] ifaces = new int[faces.size()][];
		for (int i = 0; i < faces.size(); i++) {
			ifaces[i] = faces.get(i);
		}
		double d2 = 0;
		for (final WB_Point p : points) {
			d2 = Math.max(d2, p.getSqLength());
		}
		d2 = radius / Math.sqrt(d2);
		for (final WB_Point p : points) {
			p.mulSelf(d2);
		}
		return createMesh(points, ifaces);
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngleRange(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(
					Math.random() * (maxangle - minangle) + minangle);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, true);
		skel.skeleton();
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int flc = faceloop.count();
				final int[] tmp = new int[flc];
				final int[] tmp2 = new int[flc];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					tmp2[flc - 1 - i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z, point);
					lpoints.add(point);
					i++;
				}
				tmpfaces.add(tmp);
				tmpfaces.add(tmp2);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngleRange(final WB_Coord[] points,
			final double minangle, final double maxangle,
			final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(
					Math.random() * (maxangle - minangle) + minangle);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, true);
		skel.skeleton();
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int flc = faceloop.count();
				final int[] tmp = new int[flc];
				final int[] tmp2 = new int[flc];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					tmp2[flc - 1 - i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z, point);
					lpoints.add(point);
					i++;
				}
				tmpfaces.add(tmp);
				tmpfaces.add(tmp2);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngle(
			final Collection<? extends WB_Coord> points, final double angle,
			final WB_Map2D context) {
		return createDipyramidWithAngleRange(points, angle, angle, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngle(final WB_Coord[] points,
			final double angle, final WB_Map2D context) {
		return createDipyramidWithAngleRange(points, angle, angle, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngle(
			final Collection<? extends WB_Coord> points, final double angle) {
		return createDipyramidWithAngle(points, angle, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngle(final WB_Coord[] points,
			final double angle) {
		return createDipyramidWithAngle(points, angle, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeight(
			final Collection<? extends WB_Coord> points, final double height,
			final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(Math.PI * 0.25);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, true);
		skel.skeleton();
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int flc = faceloop.count();
				final int[] tmp = new int[flc];
				final int[] tmp2 = new int[flc];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z == 0 ? 0 : height,
							point);
					lpoints.add(point);
					tmp2[flc - 1 - i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z == 0 ? 0 : -height,
							point);
					lpoints.add(point);
					i++;
				}
				tmpfaces.add(tmp);
				tmpfaces.add(tmp2);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeight(final WB_Coord[] points,
			final double height) {
		return createDipyramidWithHeight(points, height, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeight(final WB_Coord[] points,
			final double height, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(Math.PI * 0.25);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, true);
		skel.skeleton();
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int flc = faceloop.count();
				final int[] tmp = new int[flc];
				final int[] tmp2 = new int[flc];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z == 0 ? 0 : height,
							point);
					lpoints.add(point);
					tmp2[flc - 1 - i] = counter++;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z == 0 ? 0 : -height,
							point);
					lpoints.add(point);
					i++;
				}
				tmpfaces.add(tmp);
				tmpfaces.add(tmp2);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeight(
			final Collection<? extends WB_Coord> points, final double height) {
		return createDipyramidWithHeight(points, height, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAnglesAndHeight(
			final Collection<? extends WB_Coord> points, final double[] angles,
			final double height, final boolean b, final boolean t,
			final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? 1 : 0) + (t ? top.size() : 0)][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (b) {
			faces[n] = new int[n];
			i = 0;
			for (final WB_Coord p : points) {
				faces[n][i++] = counter++;
				point = createPoint();
				context.unmapPoint2D(p.xd(), p.yd(), point);
				lpoints.add(point);
			}
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + (b ? 1 : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAnglesAndHeight(final WB_Coord[] points,
			final double angles[], final double height, final boolean b,
			final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? 1 : 0) + (t ? top.size() : 0)][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (b) {
			faces[n] = new int[n];
			i = 0;
			for (final WB_Coord p : points) {
				faces[n][i++] = counter++;
				point = createPoint();
				context.unmapPoint2D(p.xd(), p.yd(), point);
				lpoints.add(point);
			}
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + (b ? 1 : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height, final boolean b,
			final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(
					Math.random() * (maxangle - minangle) + minangle);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? 1 : 0) + (t ? top.size() : 0)][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (b) {
			faces[n] = new int[n];
			i = 0;
			for (final WB_Coord p : points) {
				faces[n][i++] = counter++;
				point = createPoint();
				context.unmapPoint2D(p.xd(), p.yd(), point);
				lpoints.add(point);
			}
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + (b ? 1 : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height,
			final boolean b, final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(
					Math.random() * (maxangle - minangle) + minangle);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? 1 : 0) + (t ? top.size() : 0)][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (b) {
			faces[n] = new int[n];
			i = 0;
			for (final WB_Coord p : points) {
				faces[n][i++] = counter++;
				point = createPoint();
				context.unmapPoint2D(p.xd(), p.yd(), point);
				lpoints.add(point);
			}
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + (b ? 1 : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height,
			final WB_Map2D context) {
		return createTaperWithAngleRangeAndHeight(points, minangle, maxangle,
				height, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height,
			final WB_Map2D context) {
		return createTaperWithAngleRangeAndHeight(points, minangle, maxangle,
				height, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleAndHeight(
			final Collection<? extends WB_Coord> points, final double angle,
			final double height, final WB_Map2D context) {
		return createTaperWithAngleRangeAndHeight(points, angle, angle, height,
				true, true, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleAndHeight(final WB_Coord[] points,
			final double angle, final double height, final WB_Map2D context) {
		return createTaperWithAngleRangeAndHeight(points, angle, angle, height,
				true, true, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleAndHeight(
			final Collection<? extends WB_Coord> points, final double angle,
			final double height) {
		return createTaperWithAngleAndHeight(points, angle, height,
				createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createTaperWithAngleAndHeight(final WB_Coord[] points,
			final double angle, final double height) {
		return createTaperWithAngleAndHeight(points, angle, height,
				createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAnglesAndHeight(
			final Collection<? extends WB_Coord> points, final double[] angles,
			final double height, final boolean b, final boolean t,
			final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? top.size() : 0)
				+ (t ? top.size() : 0)][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		if (b) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				tmp.reverse();
				final int index = n + (t ? top.size() : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, -c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAnglesAndHeight(final WB_Coord[] points,
			final double[] angles, final double height, final boolean b,
			final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? top.size() : 0)
				+ (t ? top.size() : 0)][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		if (b) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				tmp.reverse();
				final int index = n + (t ? top.size() : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, -c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height, final boolean b,
			final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(
					Math.random() * (maxangle - minangle) + minangle);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? top.size() : 0)
				+ (t ? top.size() : 0)][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		if (b) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				tmp.reverse();
				final int index = n + (t ? top.size() : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, -c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height,
			final boolean b, final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		for (final Edge e : poly) {
			e.machine = new Machine(
					Math.random() * (maxangle - minangle) + minangle);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, height);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				int i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? top.size() : 0)
				+ (t ? top.size() : 0)][];
		int i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z, point);
					lpoints.add(point);
				}
			}
		}
		if (b) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				tmp.reverse();
				final int index = n + (t ? top.size() : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, -c.z, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height,
			final WB_Map2D context) {
		return createBitaperWithAngleRangeAndHeight(points, minangle, maxangle,
				height, true, true, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height,
			final WB_Map2D context) {
		return createBitaperWithAngleRangeAndHeight(points, minangle, maxangle,
				height, true, true, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height) {
		return createBitaperWithAngleRangeAndHeight(points, minangle, maxangle,
				height, true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height) {
		return createBitaperWithAngleRangeAndHeight(points, minangle, maxangle,
				height, true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleAndHeight(
			final Collection<? extends WB_Coord> points, final double angle,
			final double height, final WB_Map2D context) {
		return createBitaperWithAngleRangeAndHeight(points, angle, angle,
				height, true, true, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleAndHeight(final WB_Coord[] points,
			final double angle, final double height, final WB_Map2D context) {
		return createBitaperWithAngleRangeAndHeight(points, angle, angle,
				height, true, true, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleAndHeight(
			final Collection<? extends WB_Coord> points, final double angle,
			final double height) {
		return createBitaperWithAngleAndHeight(points, angle, height,
				createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createBitaperWithAngleAndHeight(final WB_Coord[] points,
			final double angle, final double height) {
		return createBitaperWithAngleAndHeight(points, angle, height,
				createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param cap
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAnglesAndHeight(
			final Collection<? extends WB_Coord> points, final double[] angles,
			final double height, final double cap, final boolean b,
			final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, cap);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		i = 0;
		WB_Point point;
		for (final WB_Coord p : points) {
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), -height * 0.5, point);
			lpoints.add(point);
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), +height * 0.5, point);
			lpoints.add(point);
		}
		counter = lpoints.size();
		for (i = 0; i < points.size(); i++) {
			final int[] tmp = new int[4];
			tmp[0] = 2 * i;
			tmp[1] = 2 * ((i + 1) % points.size());
			tmp[2] = 2 * ((i + 1) % points.size()) + 1;
			tmp[3] = 2 * i + 1;
			tmpfaces.add(tmp);
		}
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z + height * 0.5, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z - height * 0.5, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? top.size() : 0)
				+ (t ? top.size() : 0)][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z + height * 0.5, point);
					lpoints.add(point);
				}
			}
		}
		if (b) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				tmp.reverse();
				final int index = n + (t ? top.size() : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, -c.z - height * 0.5, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param cap
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height, final double cap,
			final boolean b, final boolean t, final WB_Map2D context) {
		final double[] angles = new double[points.size()];
		for (int i = 0; i < points.size(); i++) {
			angles[i] = Math.random() * (maxangle - minangle) + minangle;
		}
		return createCapsuleWithAnglesAndHeight(points, angles, height, cap, b,
				t, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param cap
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleAndHeight(
			final Collection<? extends WB_Coord> points, final double angle,
			final double height, final double cap, final boolean b,
			final boolean t, final WB_Map2D context) {
		final double[] angles = new double[points.size()];
		for (int i = 0; i < points.size(); i++) {
			angles[i] = angle;
		}
		return createCapsuleWithAnglesAndHeight(points, angles, height, cap, b,
				t, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAnglesAndHeight(
			final Collection<? extends WB_Coord> points, final double[] angles,
			final double height, final double cap) {
		return createCapsuleWithAnglesAndHeight(points, angles, height, cap,
				true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleRangeAndHeight(
			final Collection<? extends WB_Coord> points, final double minangle,
			final double maxangle, final double height, final double cap) {
		return createCapsuleWithAngleRangeAndHeight(points, minangle, maxangle,
				height, cap, true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleAndHeight(
			final Collection<? extends WB_Coord> points, final double angle,
			final double height, final double cap) {
		return createCapsuleWithAngleRangeAndHeight(points, angle, angle,
				height, cap, true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param cap
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAnglesAndHeight(final WB_Coord[] points,
			final double[] angles, final double height, final double cap,
			final boolean b, final boolean t, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, cap);
		skel.skeleton();
		final LoopL<Corner> top = skel.flatTop;
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		WB_Point point;
		i = 0;
		for (final WB_Coord p : points) {
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), -height * 0.5, point);
			lpoints.add(point);
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), +height * 0.5, point);
			lpoints.add(point);
		}
		counter = lpoints.size();
		for (i = 0; i < points.length; i++) {
			final int[] tmp = new int[4];
			tmp[0] = 2 * i;
			tmp[1] = 2 * ((i + 1) % points.length);
			tmp[2] = 2 * ((i + 1) % points.length) + 1;
			tmp[3] = 2 * i + 1;
			tmpfaces.add(tmp);
		}
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, p.z + height * 0.5, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y, -p.z - height * 0.5, point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n + (b ? top.size() : 0)
				+ (t ? top.size() : 0)][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		if (t) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, c.z + height * 0.5, point);
					lpoints.add(point);
				}
			}
		}
		if (b) {
			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				tmp.reverse();
				final int index = n + (t ? top.size() : 0) + i;
				faces[index] = new int[tmp.count()];
				int j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					point = createPoint();
					context.unmapPoint3D(c.x, c.y, -c.z - height * 0.5, point);
					lpoints.add(point);
				}
			}
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param cap
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height,
			final double cap, final boolean b, final boolean t,
			final WB_Map2D context) {
		final double[] angles = new double[points.length];
		for (int i = 0; i < points.length; i++) {
			angles[i] = Math.random() * (maxangle - minangle) + minangle;
		}
		return createCapsuleWithAnglesAndHeight(points, angles, height, cap, b,
				t, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param cap
	 * @param b
	 * @param t
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleAndHeight(final WB_Coord[] points,
			final double angle, final double height, final double cap,
			final boolean b, final boolean t, final WB_Map2D context) {
		final double[] angles = new double[points.length];
		for (int i = 0; i < points.length; i++) {
			angles[i] = angle;
		}
		return createCapsuleWithAnglesAndHeight(points, angles, height, cap, b,
				t, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAnglesAndHeight(final WB_Coord[] points,
			final double[] angles, final double height, final double cap) {
		return createCapsuleWithAnglesAndHeight(points, angles, height, cap,
				true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param minangle
	 * @param maxangle
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleRangeAndHeight(final WB_Coord[] points,
			final double minangle, final double maxangle, final double height,
			final double cap) {
		return createCapsuleWithAngleRangeAndHeight(points, minangle, maxangle,
				height, cap, true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angle
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createCapsuleWithAngleAndHeight(final WB_Coord[] points,
			final double angle, final double height, final double cap) {
		return createCapsuleWithAngleRangeAndHeight(points, angle, angle,
				height, cap, true, true, createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param cap
	 * @param context
	 * @return
	 */
	WB_SimpleMesh createSpindleWithAnglesAndHeight(
			final Collection<? extends WB_Coord> points, final double[] angles,
			final double height, final double cap, final WB_Map2D context) {
		final Corner[] corners = new Corner[points.size()];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.size(); i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, true);
		skel.skeleton();
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		i = 0;
		WB_Point point;
		for (final WB_Coord p : points) {
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), -height * 0.5, point);
			lpoints.add(point);
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), +height * 0.5, point);
			lpoints.add(point);
		}
		counter = lpoints.size();
		for (i = 0; i < points.size(); i++) {
			final int[] tmp = new int[4];
			tmp[0] = 2 * i;
			tmp[1] = 2 * ((i + 1) % points.size());
			tmp[2] = 2 * ((i + 1) % points.size()) + 1;
			tmp[3] = 2 * i + 1;
			tmpfaces.add(tmp);
		}
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y,
							p.z == 0 ? p.z + height * 0.5 : +height * 0.5 + cap,
							point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y,
							p.z == 0 ? -p.z - height * 0.5
									: -height * 0.5 - cap,
							point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @param cap
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createSpindle(final Collection<? extends WB_Coord> points,
			final double height, final double cap, final WB_Map2D context) {
		final double[] angles = new double[points.size()];
		for (int i = 0; i < points.size(); i++) {
			angles[i] = 0.25 * Math.PI;
		}
		return createSpindleWithAnglesAndHeight(points, angles, height, cap,
				context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createSpindle(final Collection<? extends WB_Coord> points,
			final double height, final double cap) {
		final double[] angles = new double[points.size()];
		for (int i = 0; i < points.size(); i++) {
			angles[i] = 0.25 * Math.PI;
		}
		return createSpindleWithAnglesAndHeight(points, angles, height, cap,
				createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @param angles
	 * @param height
	 * @param cap
	 * @param context
	 * @return
	 */
	WB_SimpleMesh createSpindleWithAnglesAndHeight(final WB_Coord[] points,
			final double[] angles, final double height, final double cap,
			final WB_Map2D context) {
		final Corner[] corners = new Corner[points.length];
		// final WB_Point local = createPoint();
		int id = 0;
		for (final WB_Coord p : points) {
			// local.set(context.pointTo2D(p));
			corners[id++] = new Corner(p.xd(), p.yd());// new Corner(local.x(),
			// local.y());
		}
		final Loop<Edge> poly = new Loop<Edge>();
		for (int i = 0; i < points.length; i++) {
			poly.append(new Edge(corners[i], corners[(i + 1) % points.length]));
		}
		int i = 0;
		for (final Edge e : poly) {
			e.machine = new Machine(angles[i++]);
		}
		final LoopL<Edge> out = new LoopL<Edge>();
		out.add(poly);
		final Skeleton skel = new Skeleton(out, true);
		skel.skeleton();
		final Collection<Face> expfaces = skel.output.faces.values();
		int counter = 0;
		final List<int[]> tmpfaces = new FastList<int[]>();
		final List<WB_Point> lpoints = new FastList<WB_Point>();
		i = 0;
		WB_Point point;
		for (final WB_Coord p : points) {
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), -height * 0.5, point);
			lpoints.add(point);
			point = createPoint();
			context.unmapPoint3D(p.xd(), p.yd(), +height * 0.5, point);
			lpoints.add(point);
		}
		counter = lpoints.size();
		for (i = 0; i < points.length; i++) {
			final int[] tmp = new int[4];
			tmp[0] = 2 * i;
			tmp[1] = 2 * ((i + 1) % points.length);
			tmp[2] = 2 * ((i + 1) % points.length) + 1;
			tmp[3] = 2 * i + 1;
			tmpfaces.add(tmp);
		}
		for (final Face face : expfaces) {
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y,
							p.z == 0 ? p.z + height * 0.5 : height * 0.5 + cap,
							point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
			for (final Loop<Point3d> faceloop : face.points) {
				final int[] tmp = new int[faceloop.count()];
				i = 0;
				for (final Point3d p : faceloop) {
					tmp[faceloop.count() - 1 - i++] = counter;
					point = createPoint();
					context.unmapPoint3D(p.x, p.y,
							p.z == 0 ? -p.z - height * 0.5
									: -height * 0.5 - cap,
							point);
					lpoints.add(point);
					counter++;
				}
				tmpfaces.add(tmp);
			}
		}
		final int n = tmpfaces.size();
		final int[][] faces = new int[n][];
		i = 0;
		for (final int[] tmp : tmpfaces) {
			faces[i++] = tmp;
		}
		return createUniqueMesh(createMesh(lpoints, faces));
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @param cap
	 * @param context
	 * @return
	 */
	public WB_SimpleMesh createSpindle(final WB_Coord[] points, final double height,
			final double cap, final WB_Map2D context) {
		final double[] angles = new double[points.length];
		for (int i = 0; i < points.length; i++) {
			angles[i] = 0.25 * Math.PI;
		}
		return createSpindleWithAnglesAndHeight(points, angles, height, cap,
				context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param height
	 * @param cap
	 * @return
	 */
	public WB_SimpleMesh createSpindle(final WB_Coord[] points, final double height,
			final double cap) {
		final double[] angles = new double[points.length];
		for (int i = 0; i < points.length; i++) {
			angles[i] = 0.25 * Math.PI;
		}
		return createSpindleWithAnglesAndHeight(points, angles, height, cap,
				createEmbeddedPlane());
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_SimpleMesh createConvexHull(final List<? extends WB_Coord> points) {
		return createConvexHull(points, true);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_SimpleMesh createConvexHull(final WB_Coord[] points) {
		return createConvexHull(points, true);
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulate
	 * @return
	 */
	public WB_SimpleMesh createConvexHull(final WB_Coord[] points,
			final boolean triangulate) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		int n = 0;
		for (final WB_Coord p : points) {
			if (n == 0) {
				kdtree.add(p, n++);
				uniqueVertices.add(p);
			} else {
				neighbor = kdtree.getNearestNeighbor(p);
				if (neighbor.d2 > WB_Epsilon.SQEPSILON) {
					kdtree.add(p, n++);
					uniqueVertices.add(p);
				}
			}
		}
		if (n < 4) {
			return null;
		}
		try {
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices,
					triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulate
	 * @return
	 */
	public WB_SimpleMesh createConvexHull(final List<? extends WB_Coord> points,
			final boolean triangulate) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		int n = 0;
		for (final WB_Coord p : points) {
			if (n == 0) {
				kdtree.add(p, n++);
				uniqueVertices.add(p);
			} else {
				neighbor = kdtree.getNearestNeighbor(p);
				if (neighbor.d2 > WB_Epsilon.SQEPSILON) {
					kdtree.add(p, n++);
					uniqueVertices.add(p);
				}
			}
		}
		if (n < 4) {
			return null;
		}
		try {
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices,
					triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulate
	 * @param threshold
	 * @return
	 */
	public WB_SimpleMesh createConvexHullWithThreshold(final WB_Coord[] points,
			final boolean triangulate, final double threshold) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		final double t2 = threshold * threshold;
		int n = 0;
		for (final WB_Coord p : points) {
			if (n == 0) {
				kdtree.add(p, n++);
				uniqueVertices.add(p);
			} else {
				neighbor = kdtree.getNearestNeighbor(p);
				if (neighbor.d2 > t2) {
					kdtree.add(p, n++);
					uniqueVertices.add(p);
				}
			}
		}
		if (n < 4) {
			return null;
		}
		try {
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices,
					triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulate
	 * @param threshold
	 * @return
	 */
	public WB_SimpleMesh createConvexHullWithThreshold(
			final List<? extends WB_Coord> points, final boolean triangulate,
			final double threshold) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		final double t2 = threshold * threshold;
		int n = 0;
		for (final WB_Coord p : points) {
			if (n == 0) {
				kdtree.add(p, n++);
				uniqueVertices.add(p);
			} else {
				neighbor = kdtree.getNearestNeighbor(p);
				if (neighbor.d2 > t2) {
					kdtree.add(p, n++);
					uniqueVertices.add(p);
				}
			}
		}
		if (n < 4) {
			return null;
		}
		try {
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices,
					triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public WB_Point createClosestPointOnTriangle(final WB_Coord p,
			final WB_Coord a, final WB_Coord b, final WB_Coord c) {
		final WB_Vector ab = createVectorFromTo(a, b);
		final WB_Vector ac = createVectorFromTo(a, c);
		final WB_Vector ap = createVectorFromTo(a, b);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return createPoint(a);
		}
		final WB_Vector bp = createVectorFromTo(b, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return createPoint(b);
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return createPoint(a).addSelf(ab.mul(v));
		}
		final WB_Vector cp = createVectorFromTo(c, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return createPoint(c);
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return createPoint(a).addSelf(ac.mul(w));
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return createPoint(b).addSelf(createVectorFromTo(c, b).mul(w));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return createPoint(a).addSelf(ab.mul(v)).addSelf(ac.mul(w));
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public WB_Point createClosestPointOnPolygon(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] triangles = poly.getTriangles();
		final int n = triangles.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = null;
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = createClosestPointOnTriangle(p, poly.getPoint(triangles[i]),
					poly.getPoint(triangles[i + 1]),
					poly.getPoint(triangles[i + 2]));
			final double d2 = tmp.getSqDistance(p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
				if (WB_Epsilon.isZeroSq(d2)) {
					return closest;
				}
			}
		}
		return closest;
	}

	/**
	 * Sphere with center and radius.
	 *
	 * @param center
	 * @param radius
	 * @return sphere
	 */
	public WB_Sphere createSphereWithRadius(final WB_Coord center,
			final double radius) {
		return new WB_Sphere(center, radius);
	}

	/**
	 * Sphere with center and diameter.
	 *
	 * @param center
	 * @param diameter
	 * @return sphere
	 */
	public WB_Sphere createSphereWithDiameter(final WB_Coord center,
			final double diameter) {
		return createSphereWithRadius(center, .5 * diameter);
	}

	/**
	 * Sphere with center and radius.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return sphere
	 */
	public WB_Sphere createSphereWithRadius(final double x, final double y,
			final double z, final double radius) {
		return createSphereWithRadius(createPoint(x, y, z), radius);
	}

	/**
	 * Sphere with diameter and radius.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param diameter
	 * @return sphere
	 */
	public WB_Sphere createSphereWithDiameter(final double x, final double y,
			final double z, final double diameter) {
		return createSphereWithRadius(createPoint(x, y, z), .5 * diameter);
	}

	/**
	 * Get tetrahedron from 4 points.
	 *
	 * @param p1
	 *            first point of tetrahedron
	 * @param p2
	 *            second point of tetrahedron
	 * @param p3
	 *            third point of tetrahedron
	 * @param p4
	 *            fourth point of tetrahedron
	 * @return tetrahedron
	 */
	public WB_Tetrahedron createTetrahedron(final WB_Coord p1,
			final WB_Coord p2, final WB_Coord p3, final WB_Coord p4) {
		return new WB_Tetrahedron(p1, p2, p3, p4);
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param P
	 * @return
	 */
	public WB_Point getIntersection(final WB_Coord a, final WB_Coord b,
			final WB_Plane P) {
		Object o = WB_GeometryOp.getIntersection3D(a, b, P).getObject();
		return o == null ? null : (WB_Point) o;
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public WB_Polygon createPolygonConvexHull(final WB_Polygon poly) {
		return WB_JTS.createPolygonConvexHull(poly);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons(final WB_Polygon poly,
			final double d) {
		return WB_JTS.createBufferedPolygons(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons(final WB_Polygon poly,
			final double d, final int n) {
		return WB_JTS.createBufferedPolygons(poly, d, n);
	}

	public List<WB_Polygon> createBufferedPolygonsStraight(
			final WB_Polygon poly, final double d) {
		return WB_JTS.createBufferedPolygonsStraight(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons(
			final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createBufferedPolygons(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons(
			final Collection<? extends WB_Polygon> poly, final double d,
			final int n) {
		return WB_JTS.createBufferedPolygons(poly, d, n);
	}

	public List<WB_Polygon> createBufferedPolygonsStraight(
			final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createBufferedPolygonsStraight(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public List<WB_Polygon> createBoundaryPolygons(final WB_Polygon poly) {
		return WB_JTS.createBoundaryPolygons(poly);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons(final WB_Polygon poly,
			final double d) {
		return WB_JTS.createRibbonPolygons(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons(
			final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createRibbonPolygons(poly, d);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons(final WB_Polygon poly,
			final double o, final double i) {
		return WB_JTS.createRibbonPolygons(poly, o, i);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons(
			final Collection<? extends WB_Polygon> poly, final double o,
			final double i) {
		return WB_JTS.createRibbonPolygons(poly, o, i);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param tol
	 * @return
	 */
	public List<WB_Polygon> createSimplifiedPolygon(final WB_Polygon poly,
			final double tol) {
		return WB_JTS.createSimplifiedPolygon(poly, tol);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param max
	 * @return
	 */
	public List<WB_Polygon> createDensifiedPolygon(final WB_Polygon poly,
			final double max) {
		return WB_JTS.createDensifiedPolygon(poly, max);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param container
	 * @return
	 */
	public List<WB_Polygon> constrainPolygons(final WB_Polygon poly,
			final WB_Polygon container) {
		return WB_JTS.constrainPolygons(poly, container);
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	public List<WB_Polygon> constrainPolygons(final WB_Polygon[] polygons,
			final WB_Polygon container) {
		return WB_JTS.constrainPolygons(polygons, container);
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	public List<WB_Polygon> constrainPolygons(final List<WB_Polygon> polygons,
			final WB_Polygon container) {
		return WB_JTS.constrainPolygons(polygons, container);
	}

	public List<WB_Coord> createUniquePoints(final List<WB_Coord> points,
			final double threshold) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = points.get(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		int nuv = 1;
		double threshold2 = threshold * threshold;
		for (int i = 1; i < points.size(); i++) {
			v = points.get(i);
			neighbor = kdtree.getNearestNeighbor(v);
			if (neighbor.d2 > threshold2) {
				kdtree.add(v, nuv);
				uniqueVertices.add(v);
			}
		}
		return uniqueVertices;
	}

	public List<WB_Coord> createUniquePoints(final List<WB_Coord> points) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = points.get(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		int nuv = 1;
		double threshold2 = WB_Epsilon.SQEPSILON;
		for (int i = 1; i < points.size(); i++) {
			v = points.get(i);
			neighbor = kdtree.getNearestNeighbor(v);
			if (neighbor.d2 > threshold2) {
				kdtree.add(v, nuv);
				uniqueVertices.add(v);
			}
		}
		return uniqueVertices;
	}

	public List<WB_Plane> createUniquePlanes(final List<WB_Plane> planes) {
		final List<WB_Plane> uniquePlanes = new FastList<WB_Plane>();
		boolean unique = true;
		WB_Plane Pi, Pj;
		uniquePlanes.add(planes.get(0));
		for (int i = 1; i < planes.size(); i++) {
			Pi = planes.get(i);
			unique = true;
			for (int j = 0; j < i; j++) {
				Pj = planes.get(j);
				if (WB_GeometryOp.isEqual(Pi, Pj)) {
					unique = false;
					break;
				}
			}
			if (unique) {
				uniquePlanes.add(Pi);
			}
		}
		return uniquePlanes;
	}
}
