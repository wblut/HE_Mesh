/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_GeometryFactory2D {
	
	private WB_JTS.ShapeReader	shapereader;

	/**
	 *
	 */
	public WB_GeometryFactory2D() {

	}


	/**
	 *
	 * @return default origin
	 */
	public WB_CoordinateSystem WORLD() {
		return WB_CoordinateSystem.WORLD();
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
	public WB_CoordinateSystem createCSFromOriginAndX2D(final WB_Coord origin,
			final WB_Coord X) {
		final WB_Point lOrigin = createPoint2D(origin.xd(), origin.yd());
		final WB_Vector lX = createNormalizedVector2D(X.xd(), X.yd());
		final WB_Vector lY = createVector2D(-lX.yd(), lX.xd());
		return createCSFromOriginAndXY2D(lOrigin, lX, lY);
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
	public WB_CoordinateSystem createCSFromOriginAndX2D(final WB_Coord origin,
			final WB_Coord X, final WB_CoordinateSystem parent) {
		final WB_Point lOrigin = createPoint2D(origin.xd(), origin.yd());
		final WB_Vector lX = createNormalizedVector2D(X.xd(), X.yd());
		final WB_Vector lY = createVector2D(-lX.yd(), lX.xd());
		return createCSFromOriginAndXY2D(lOrigin, lX, lY, parent);
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
	public WB_CoordinateSystem createCSFromOriginAndXY2D(final WB_Coord origin,
			final WB_Coord X, final WB_Coord Y) {
		final WB_Vector lX = createNormalizedVector2D(X);
		WB_Vector lY = createNormalizedVector2D(Y);
		final WB_Vector lZ = new WB_Vector(0, 0, 1);
		if (WB_Epsilon.isZeroSq(lX.cross(lY).getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		lY = createNormalizedVector2D(lZ.cross(lX));
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
	public WB_CoordinateSystem createCSFromOriginAndXY2D(final WB_Coord origin,
			final WB_Coord X, final WB_Coord Y,
			final WB_CoordinateSystem parent) {
		final WB_Vector lX = createNormalizedVector2D(X);
		WB_Vector lY = createNormalizedVector2D(Y);
		final WB_Vector lZ = new WB_Vector(0, 0, 1);
		if (WB_Epsilon.isZeroSq(lX.cross(lY).getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		lY = createNormalizedVector2D(lZ.cross(lX));
		return new WB_CoordinateSystem(origin, lX, lY, lZ, parent);
	}

	/**
	 *
	 *
	 * @param CS
	 * @param T
	 * @param parent
	 * @return
	 */
	public WB_CoordinateSystem createTransformedCS(
			final WB_CoordinateSystem CS, final WB_Transform3D T,
			final WB_CoordinateSystem parent) {
		return CS.apply(T, parent);
	}

	/**
	 *
	 *
	 * @param CS
	 * @param T
	 * @return
	 */
	public WB_CoordinateSystem createTransformedCS(
			final WB_CoordinateSystem CS, final WB_Transform3D T) {
		return CS.apply(T);
	}

	/**
	 * New point at origin.
	 *
	 * @return new point at origin
	 */
	public WB_Point createPoint2D() {
		return new WB_Point(0, 0, 0);
	}

	/**
	 * Copy of coordinate as point, z-ordinate is ignored.
	 *
	 * @param p
	 *            point
	 * @return copy of point
	 */
	public WB_Point createPoint2D(final WB_Coord p) {
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
	public WB_Point createPoint2D(final double x, final double y) {
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
	public WB_Point createInterpolatedPoint2D(final WB_Coord p,
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
	public WB_Point createPointFromPolar(final double r, final double phi) {
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
	public WB_Point createPointFromBipolar(final double a, final double sigma,
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
	public WB_Point createPointFromParabolic(final double sigma,
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
	public WB_Point createPointFromHyperbolic(final double u, final double v) {
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
	public WB_Point createPointFromElliptic(final double a, final double sigma,
			final double tau) {
		return createPoint2D(a * sigma * tau,
				Math.sqrt(a * a * (sigma * sigma - 1) * (1 - tau * tau)));
	}

	/**
	 * Incenter of triangle, z-ordinate is ignored.
	 *
	 * @param tri
	 *            triangle
	 * @return incenter
	 */
	public WB_Point createIncenter2D(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates2D(1, 1, 1, tri);
	}

	/**
	 * Closest point to 2D line, z-ordinate is ignored.
	 *
	 * @param p
	 *            WB_Coordinate
	 * @param L
	 *            WB_Line
	 * @return closest point on line
	 */
	public WB_Point createClosestPointOnLine2D(final WB_Coord p,
			final WB_Line L) {
		if (WB_Epsilon.isZero(L.getDirection().xd())) {
			return createPoint2D(L.getOrigin().xd(), p.yd());
		}
		if (WB_Epsilon.isZero(L.getDirection().yd())) {
			return createPoint2D(p.xd(), L.getOrigin().yd());
		}
		final double m = L.getDirection().yd() / L.getDirection().xd();
		final double b = L.getOrigin().yd() - m * L.getOrigin().xd();
		final double x = (m * p.yd() + p.xd() - m * b) / (m * m + 1);
		final double y = (m * m * p.yd() + m * p.xd() + b) / (m * m + 1);
		return createPoint2D(x, y);
	}

	/**
	 * Closest points between two 2D lines, z-ordinate is ignored.
	 *
	 * @param L1
	 *            2D line
	 * @param L2
	 *            2D line
	 * @return if crossing: intersection, if parallel: origin of L1 + point on
	 *         L2 closest to origin of L1
	 */
	public List<WB_Point> createClosestPoint(final WB_Line L1,
			final WB_Line L2) {
		final List<WB_Point> result = new ArrayList<WB_Point>();
		final double a = WB_Vector.dot2D(L1.getDirection(), L1.getDirection());
		final double b = WB_Vector.dot2D(L1.getDirection(), L2.getDirection());
		final WB_Point r = createPoint2D(L1.getOrigin()).sub(L2.getOrigin());
		final double c = WB_Vector.dot2D(L1.getDirection(), r);
		final double e = WB_Vector.dot2D(L2.getDirection(), L2.getDirection());
		final double f = WB_Vector.dot2D(L2.getDirection(), r);
		double denom = a * e - b * b;
		if (WB_Epsilon.isZero(denom)) {
			final double t2 = r.dot2D(L1.getDirection());
			result.add(createPoint2D(L1.getOrigin()));
			result.add(createPoint2D(L2.getPoint(t2)));
			return result;
		}
		denom = 1.0 / denom;
		final double t1 = (b * f - c * e) * denom;
		result.add(createPoint2D(L1.getPoint(t1)));
		return result;
	}

	/**
	 * Gets intersection points of two circles, z-ordinate is ignored.
	 *
	 * @param C0
	 * @param C1
	 * @return intersection points of two circles
	 */
	public List<WB_Point> createIntersectionPoints2D(final WB_Circle C0,
			final WB_Circle C1) {
		final List<WB_Point> result = new ArrayList<WB_Point>();
		final WB_Vector u = createVector2D(C1.getCenter()).sub(C0.getCenter());
		final double d2 = u.getSqLength();
		final double d = Math.sqrt(d2);
		if (WB_Epsilon.isEqualAbs(d, C0.getRadius() + C1.getRadius())) {
			result.add(createInterpolatedPoint2D(C0.getCenter(), C1.getCenter(),
					C0.getRadius() / (C0.getRadius() + C1.getRadius())));
			return result;
		}
		if (d > C0.getRadius() + C1.getRadius()
				|| d < WB_Math.fastAbs(C0.getRadius() - C1.getRadius())) {
			return result;
		}
		final double r02 = C0.getRadius() * C0.getRadius();
		final double r12 = C1.getRadius() * C1.getRadius();
		final double a = (r02 - r12 + d2) / (2 * d);
		final double h = Math.sqrt(r02 - a * a);
		final WB_Point c = createPoint2D(C0.getCenter()).addMulSelf(a / d, u);
		final double p0x = c.xd()
				+ h * (C1.getCenter().yd() - C0.getCenter().yd()) / d;
		final double p0y = c.yd()
				- h * (C1.getCenter().xd() - C0.getCenter().xd()) / d;
		final double p1x = c.xd()
				- h * (C1.getCenter().yd() - C0.getCenter().yd()) / d;
		final double p1y = c.yd()
				+ h * (C1.getCenter().xd() - C0.getCenter().xd()) / d;
		final WB_Point p0 = createPoint2D(p0x, p0y);
		result.add(p0);
		final WB_Point p1 = createPoint2D(p1x, p1y);
		if (!WB_Epsilon.isZeroSq(p0.getSqDistance(p1))) {
			result.add(p1);
		}
		return result;
	}

	/**
	 * Gets intersection points of 2D line and circle, z-ordinate is ignored.
	 *
	 * @param L
	 * @param C
	 * @return intersection points of line and circle
	 */
	public List<WB_Point> createIntersectionPoints2D(final WB_Line L,
			final WB_Circle C) {
		final List<WB_Point> result = new ArrayList<WB_Point>();
		final double b = 2 * (L.getDirection().xd()
				* (L.getOrigin().xd() - C.getCenter().xd())
				+ L.getDirection().yd()
						* (L.getOrigin().yd() - C.getCenter().yd()));
		final double c = WB_Point.getSqLength3D(C.getCenter())
				+ WB_Vector.getSqLength3D(L.getOrigin())
				- 2 * (C.getCenter().xd() * L.getOrigin().xd()
						+ C.getCenter().yd() * L.getOrigin().yd())
				- C.getRadius() * C.getRadius();
		double disc = b * b - 4 * c;
		if (disc < -WB_Epsilon.EPSILON) {
			return result;
		}
		if (WB_Epsilon.isZero(disc)) {
			result.add(createPoint2D(L.getPoint(-0.5 * b)));
			return result;
		}
		disc = Math.sqrt(disc);
		result.add(createPoint2D(L.getPoint(0.5 * (-b + disc))));
		result.add(createPoint2D(L.getPoint(0.5 * (-b - disc))));
		return result;
	}

	/**
	 * Gets intersection points of two 2D lines, z-ordinate is ignored.
	 *
	 * @param L1
	 * @param L2
	 * @return intersection point
	 */
	public WB_Point createIntersectionPoint2D(final WB_Line L1,
			final WB_Line L2) {
		final double a = WB_Vector.dot(L1.getDirection(), L1.getDirection());
		final double b = WB_Vector.dot(L1.getDirection(), L2.getDirection());
		final WB_Vector r = createVector2D(
				WB_Vector.sub(L1.getOrigin(), L2.getOrigin()));
		final double c = WB_Vector.dot(L1.getDirection(), r);
		final double e = WB_Vector.dot(L2.getDirection(), L2.getDirection());
		final double f = WB_Vector.dot(L2.getDirection(), r);
		double denom = a * e - b * b;
		if (WB_Epsilon.isZero(denom)) {
			return null;
		}
		denom = 1.0 / denom;
		final double t1 = (b * f - c * e) * denom;
		final double t2 = (a * f - b * c) * denom;
		final WB_Point p1 = L1.getPoint(t1);
		final WB_Point p2 = L2.getPoint(t2);
		return p1.mulAddMul(0.5, 0.5, p2);
	}

	/**
	 * Mirror 2D point about 2D line.
	 *
	 * @param p
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return mirrored point
	 */
	public WB_Point createMirrorPoint2D(final WB_Coord p, final double x0,
			final double y0, final double x1, final double y1) {
		double dx, dy, a, b;
		double x2, y2;
		dx = x1 - x0;
		dy = y1 - y0;
		a = (dx * dx - dy * dy) / (dx * dx + dy * dy);
		b = 2 * dx * dy / (dx * dx + dy * dy);
		x2 = a * (p.xd() - x0) + b * (p.yd() - y0) + x0;
		y2 = b * (p.xd() - x0) - a * (p.yd() - y0) + y0;
		return createPoint2D(x2, y2);
	}

	/**
	 * Mirror 2D point about 2D line.
	 *
	 * @param p
	 *            WB_Coordinate
	 * @param L
	 *            WB_Linear
	 * @return mirrored point
	 */
	public WB_Point createMirrorPoint2D(final WB_Coord p, final WB_Line L) {
		double dx, dy, a, b;
		double x2, y2;
		dx = L.getDirection().xd();
		dy = L.getDirection().yd();
		a = dx * dx - dy * dy;
		b = 2 * dx * dy;
		x2 = a * (p.xd() - L.getOrigin().xd())
				+ b * (p.yd() - L.getOrigin().yd()) + L.getOrigin().xd();
		y2 = b * (p.xd() - L.getOrigin().xd())
				- a * (p.yd() - L.getOrigin().yd()) + L.getOrigin().yd();
		return createPoint2D(x2, y2);
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
	public WB_Point createPointFromBarycentricCoordinates2D(final double u,
			final double v, final double w, final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates2D(u / tri.a(), v / tri.b(),
				w / tri.c(), tri);
	}

	/**
	 * Inversion of 2D point p over circle C
	 * http://mathworld.wolfram.com/Inversion.html
	 *
	 * @param p
	 *            2D point
	 * @param inversionCircle
	 *            inversion circle
	 *
	 * @return Inversion of 2D point p over circle C, null if p coincides with
	 *         inversion circle center
	 */
	public WB_Point createInversionPoint2D(final WB_Coord p,
			final WB_Circle inversionCircle) {
		final double r2 = inversionCircle.getRadius()
				* inversionCircle.getRadius();
		final double OP = WB_CoordOp2D
				.getDistance2D(inversionCircle.getCenter(), p);
		if (WB_Epsilon.isZero(OP)) {
			return null;
		}
		final double OPp = r2 / OP;
		final WB_Vector v = createNormalizedVectorFromTo2D(
				inversionCircle.getCenter(), p);
		return createPoint2D(
				WB_Point.addMul(inversionCircle.getCenter(), OPp, v));
	}

	/**
	 * Centroid of triangle.
	 *
	 * @param tri
	 *            triangle
	 * @return centroid
	 */
	public WB_Point createCentroid2D(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates2D(tri.b() * tri.c(),
				tri.c() * tri.a(), tri.a() * tri.b(), tri);
	}

	public WB_Point createCentroid2D(final WB_Coord... points) {
		WB_Point c = new WB_Point();
		for (int i = 0; i < points.length; i++) {
			c.addSelf(points[i].xd(), points[i].yd());
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
	public WB_Point createCircumcenter2D(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates2D(tri.cosA(), tri.cosB(),
				tri.cosC(), tri);
	}

	/**
	 * Orthocenter of triangle.
	 *
	 * @param tri
	 *            triangle
	 * @return orthocenter
	 */
	public WB_Point createOrthocenter2D(final WB_Triangle tri) {
		final double a2 = tri.a() * tri.a();
		final double b2 = tri.b() * tri.b();
		final double c2 = tri.c() * tri.c();
		return createPointFromBarycentricCoordinates2D(
				(a2 + b2 - c2) * (a2 - b2 + c2),
				(a2 + b2 - c2) * (-a2 + b2 + c2),
				(a2 - b2 + c2) * (-a2 + b2 + c2), tri);
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
	public WB_Point createPointFromTrilinearCoordinates2D(final double u,
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
		return createPoint2D(eax, eay);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public WB_Point createMidpoint2D(final WB_Coord p, final WB_Coord q) {
		return createPoint2D((p.xd() + q.xd()) * 0.5, (p.yd() + q.yd()) * 0.5);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Point createMidpoint2D(final WB_Coord... p) {
		final WB_Point m = createPoint2D();
		for (final WB_Coord point : p) {
			m.addSelf(point.xd(), point.yd());
		}
		m.divSelf(p.length);
		return m;
	}

	/**
	 * New zero-length vector.
	 *
	 * @return zero-length vector
	 */
	public WB_Vector createVector2D() {
		return new WB_Vector(0, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public WB_Vector createVectorFromTo2D(final WB_Coord p, final WB_Coord q) {
		return createVector2D(q.xd() - p.xd(), q.yd() - p.yd());
	}

	/**
	 * Copy of coordinate as vector, z-ordinate is ignored.
	 *
	 * @param p
	 *            vector
	 * @return vector
	 */
	public WB_Vector createVector2D(final WB_Coord p) {
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
	public WB_Vector createVector2D(final double _x, final double _y) {
		return new WB_Vector(_x, _y);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Vector createNormalizedVector2D(final WB_Coord p) {
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
	public WB_Vector createNormalizedVectorFromTo2D(final WB_Coord p,
			final WB_Coord q) {
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
	public WB_Vector createNormalizedVector2D(final double _x,
			final double _y) {
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
	public WB_Vector createNormalizedPerpendicularVector2D(final double _x,
			final double _y) {
		final WB_Vector vec = createVector2D(-_y, _x);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedPerpendicularVector2D(final WB_Coord v) {
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
	public WB_Vector createVectorFromPolar(final double r, final double phi) {
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
	public WB_Vector createVectorFromBipolar(final double a, final double sigma,
			final double tau) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector2D(Math.sinh(tau) * invdenom,
				Math.sin(sigma) * invdenom);
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
	public WB_Vector createVectorFromParabolic(final double sigma,
			final double tau) {
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
	public WB_Vector createVectorFromHyperbolic(final double u,
			final double v) {
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
	public WB_Vector createVectorFromElliptic(final double a, final double mu,
			final double nu) {
		return createVector2D(a * Math.cosh(mu) * Math.cos(nu),
				a * Math.sinh(mu) * Math.cos(nu));
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
	public WB_Line createLineThroughPoints2D(final WB_Coord p1,
			final WB_Coord p2) {
		return createLineWithDirection2D(p1, createVectorFromTo2D(p1, p2));
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
	public WB_Line createLineThroughPoints2D(final double x1, final double y1,
			final double x2, final double y2) {
		return createLineWithDirection2D(createPoint2D(x1, y1),
				createVector2D(x2 - x1, y2 - y1));
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
	public WB_Line createLineWithDirection2D(final WB_Coord origin,
			final WB_Coord direction) {
		return new WB_Line(createPoint2D(origin), createVector2D(direction));
	}

	/**
	 * Get 2D line through point with given direction.
	 *
	 * @param ox
	 *            x-ordinate of origin
	 * @param oy
	 *            y-ordinate of origin
	 * @param dx
	 *            x-ordinate of direction
	 * @param dy
	 *            y-ordinate of direction
	 * @return 2D line through point with given direction
	 */
	public WB_Line createLineWithDirection2D(final double ox, final double oy,
			final double dx, final double dy) {
		return createLineWithDirection2D(createPoint2D(ox, oy),
				createVector2D(dx, dy));
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
	public WB_Line createParallelLineThroughPoint2D(final WB_Line L,
			final WB_Coord p) {
		return createLineWithDirection2D(p, L.getDirection());
	}

	/**
	 * Get a 2D line perpendicular to 2D line and through 2D point.
	 *
	 * @param L
	 *            2D line
	 * @param p
	 *            2D point
	 * @return perpendicular 2D line through point
	 */
	public WB_Line createPerpendicularLineThroughPoint2D(final WB_Line L,
			final WB_Coord p) {
		return createLineWithDirection2D(p,
				createVector2D(-L.getDirection().yd(), L.getDirection().xd()));
	}

	/**
	 * Get the two 2D lines parallel to a 2D line and separated by a distance d.
	 *
	 * @param L
	 *            2D line
	 * @param d
	 *            distance
	 * @return two parallel 2D lines
	 */
	public List<WB_Line> createParallelLines2D(final WB_Line L,
			final double d) {
		final List<WB_Line> result = new ArrayList<WB_Line>(2);
		result.add(createLineWithDirection2D(
				createPoint2D(L.getOrigin().xd() - d * L.getDirection().yd(),
						L.getOrigin().yd() + d * L.getDirection().xd()),
				L.getDirection()));
		result.add(createLineWithDirection2D(
				createPoint2D(L.getOrigin().xd() + d * L.getDirection().yd(),
						L.getOrigin().yd() - d * L.getDirection().xd()),
				L.getDirection()));
		return result;
	}

	/**
	 * Get the 2D bisector of two 2D points. The points should be distinct.
	 *
	 * @param p
	 *            2D point
	 * @param q
	 *            2D point
	 * @return 2D bisector
	 */
	public WB_Line createBisector2D(final WB_Coord p, final WB_Coord q) {
		return createLineWithDirection2D(
				createPoint2D(p).mulAddMulSelf(0.5, 0.5, q),
				createVector2D(p.yd() - q.yd(), q.xd() - p.xd()));
	}

	/**
	 * Get the 2D angle bisectors of two 2D lines.
	 *
	 * @param L1
	 *            2D line
	 * @param L2
	 *            2D line
	 * @return 2D angle bisector
	 */
	public List<WB_Line> createAngleBisector2D(final WB_Line L1,
			final WB_Line L2) {
		final WB_Point intersection = createIntersectionPoint2D(L1, L2);
		final List<WB_Line> result = new ArrayList<WB_Line>(2);
		if (intersection == null) {
			final WB_Point L1onL2 = createClosestPointOnLine2D(L1.getOrigin(),
					L2);
			result.add(createLineWithDirection2D(
					L1onL2.mulAddMul(0.5, 0.5, L1.getOrigin()),
					L1.getDirection()));
			return result;
		} else {
			if (WB_Vector.dot(L1.getDirection(), L2.getDirection()) > 0) {
				final WB_Point p1 = intersection.addMul(100, L1.getDirection());
				final WB_Point p2 = intersection.addMul(100, L2.getDirection());
				final WB_Vector dir = createVector2D(
						p1.mulAddMul(0.5, 0.5, p2).sub(intersection));
				result.add(createLineWithDirection2D(intersection, dir));
				result.add(createLineWithDirection2D(intersection.xd(),
						intersection.yd(), -dir.yd(), dir.xd()));
				return result;
			} else {
				final WB_Point p1 = intersection.addMul(100, L1.getDirection());
				final WB_Point p2 = intersection.addMul(-100,
						L2.getDirection());
				final WB_Vector dir = createVector2D(
						p1.mulAddMul(0.5, 0.5, p2).sub(intersection));
				result.add(createLineWithDirection2D(intersection, dir));
				result.add(createLineWithDirection2D(intersection.xd(),
						intersection.yd(), -dir.yd(), dir.xd()));
				return result;
			}
		}
	}

	/**
	 * Get the 2D line tangent to a circle at a 2D point.
	 *
	 * @param C
	 *            circle
	 * @param p
	 *            point
	 * @return 2D line tangent to circle at point
	 */
	public WB_Line createLineTangentToCircleInPoint(final WB_Circle C,
			final WB_Coord p) {
		final WB_Vector v = createVector2D(p).sub(C.getCenter());
		return createLineWithDirection2D(p, createVector2D(-v.yd(), v.xd()));
	}

	/**
	 * Gets the 2D lines tangent to a circle through 2D point.
	 *
	 * @param C
	 *            circle
	 * @param p
	 *            point
	 * @return 2D lines tangent to circle through point
	 */
	public List<WB_Line> createLinesTangentToCircleThroughPoint(
			final WB_Circle C, final WB_Coord p) {
		final List<WB_Line> result = new ArrayList<WB_Line>(2);
		final double dcp = WB_CoordOp2D.getDistance2D(C.getCenter(), p);
		final WB_Vector u = createVector2D(p).sub(C.getCenter());
		if (WB_Epsilon.isZero(dcp - C.getRadius())) {
			result.add(createLineWithDirection2D(p,
					createVector2D(-u.yd(), u.xd())));
		} else if (dcp < C.getRadius()) {
			return result;
		} else if (!WB_Epsilon.isZero(u.xd())) {
			final double ux2 = u.xd() * u.xd();
			final double ux4 = ux2 * ux2;
			final double uy2 = u.yd() * u.yd();
			final double r2 = C.getRadius() * C.getRadius();
			final double r4 = r2 * r2;
			final double num = r2 * uy2;
			final double denom = ux2 + uy2;
			final double rad = Math.sqrt(-r4 * ux2 + r2 * ux4 + r2 * ux2 * uy2);
			result.add(createLineWithDirection2D(p,
					createVector2D(-(r2 * u.yd() + rad) / denom,
							(r2 - (num + u.yd() * rad) / denom) / u.xd())));
			result.add(createLineWithDirection2D(p,
					createVector2D(-(r2 * u.yd() - rad) / denom,
							(r2 - (num - u.yd() * rad) / denom) / u.xd())));
		} else {
			final double ux2 = u.yd() * u.yd();
			final double ux4 = ux2 * ux2;
			final double uy2 = u.xd() * u.xd();
			final double r2 = C.getRadius() * C.getRadius();
			final double r4 = r2 * r2;
			final double num = r2 * uy2;
			final double denom = ux2 + uy2;
			final double rad = Math.sqrt(-r4 * ux2 + r2 * ux4 + r2 * ux2 * uy2);
			result.add(createLineWithDirection2D(p,
					createVector2D((r2 - (num + u.xd() * rad) / denom) / u.yd(),
							-(r2 * u.xd() + rad) / denom)));
			result.add(createLineWithDirection2D(p,
					createVector2D((r2 - (num - u.xd() * rad) / denom) / u.yd(),
							-(r2 * u.xd() - rad) / denom)));
		}
		return result;
	}

	/**
	 * Gets the 2D lines tangent to 2 circles.
	 *
	 * @param C0
	 *            circle
	 * @param C1
	 *            circle
	 * @return the 2D lines tangent to the 2 circles
	 */
	public List<WB_Line> createLinesTangentTo2Circles(final WB_Circle C0,
			final WB_Circle C1) {
		final List<WB_Line> result = new ArrayList<WB_Line>(4);
		final WB_Vector w = createVector2D(C1.getCenter()).sub(C0.getCenter());
		final double wlensqr = w.getSqLength();
		final double rsum = C0.getRadius() + C1.getRadius();
		final double rdiff = C1.getRadius() - C0.getRadius();
		if (wlensqr < rdiff * rdiff) {
			return result;
		}
		boolean inside = false;
		if (wlensqr <= rsum * rsum) {
			inside = true;
		}
		if (!WB_Epsilon.isZero(rdiff)) {
			final double r0sqr = C0.getRadius() * C0.getRadius();
			final double r1sqr = C1.getRadius() * C1.getRadius();
			final double c0 = -r0sqr;
			final double c1 = 2 * r0sqr;
			final double c2 = C1.getRadius() * C1.getRadius() - r0sqr;
			final double invc2 = 1.0 / c2;
			final double discr = Math
					.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			double s, oms, a;
			s = -0.5 * (c1 + discr) * invc2;
			if (s >= 0.5) {
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r0sqr / (s * s)));
			} else {
				oms = 1.0 - s;
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r1sqr / (oms * oms)));
			}
			List<WB_Vector> dir = getDirections2D(w, a);
			WB_Point org = createPoint2D(C0.getCenter().xd() + s * w.xd(),
					C0.getCenter().yd() + s * w.yd());
			result.add(createLineWithDirection2D(org, dir.get(0)));
			result.add(createLineWithDirection2D(org, dir.get(1)));
			s = -0.5 * (c1 - discr) * invc2;
			if (s >= 0.5) {
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r0sqr / (s * s)));
			} else {
				oms = 1.0 - s;
				a = Math.sqrt(WB_Math.fastAbs(wlensqr - r1sqr / (oms * oms)));
			}
			dir = getDirections2D(w, a);
			org = createPoint2D(C0.getCenter().xd() + s * w.xd(),
					C0.getCenter().yd() + s * w.yd());
			if (!inside) {
				result.add(createLineWithDirection2D(org, dir.get(0)));
			}
			if (!inside) {
				result.add(createLineWithDirection2D(org, dir.get(1)));
			}
		} else {
			final WB_Point mid = createPoint2D(C0.getCenter()).mulAddMul(0.5,
					0.5, C1.getCenter());
			final double a = Math.sqrt(WB_Math
					.fastAbs(wlensqr - 4 * C0.getRadius() * C0.getRadius()));
			final double invwlen = 1.0 / Math.sqrt(wlensqr);
			result.add(createLineWithDirection2D(
					createPoint2D(mid.xd() + C0.getRadius() * w.yd() * invwlen,
							mid.yd() - C0.getRadius() * w.xd() * invwlen),
					w));
			result.add(createLineWithDirection2D(
					createPoint2D(mid.xd() - C0.getRadius() * w.yd() * invwlen,
							mid.yd() + C0.getRadius() * w.xd() * invwlen),
					w));
			final List<WB_Vector> dir = getDirections2D(w, a);
			if (!inside) {
				result.add(createLineWithDirection2D(mid, dir.get(0)));
			}
			if (!inside) {
				result.add(createLineWithDirection2D(mid, dir.get(1)));
			}
		}
		if (WB_Epsilon.isZeroSq(wlensqr - rsum * rsum)) {
			final WB_Point org = createInterpolatedPoint2D(C0.getCenter(),
					C1.getCenter(),
					C0.getRadius() / (C0.getRadius() + C1.getRadius()));
			final WB_Vector dir = createNormalizedVector2D(
					C1.getCenter().xd() - C0.getCenter().xd(),
					C1.getCenter().yd() - C0.getCenter().yd());
			result.add(createLineWithDirection2D(org,
					createVector2D(-dir.yd(), dir.xd())));
		}
		if (WB_Epsilon.isZeroSq(wlensqr - rdiff * rdiff)) {
			final WB_Point org = createInterpolatedPoint2D(C0.getCenter(),
					C1.getCenter(),
					C0.getRadius() / (C0.getRadius() - C1.getRadius()));
			final WB_Vector dir = createNormalizedVector2D(
					C1.getCenter().xd() - C0.getCenter().xd(),
					C1.getCenter().yd() - C0.getCenter().yd());
			result.add(createLineWithDirection2D(org,
					createVector2D(-dir.yd(), dir.xd())));
		}
		return result;
	}

	/**
	 *
	 *
	 * @param w
	 * @param a
	 * @return
	 */
	private List<WB_Vector> getDirections2D(final WB_Coord w, final double a) {
		final List<WB_Vector> dir = new ArrayList<WB_Vector>(2);
		final double asqr = a * a;
		final double wxsqr = w.xd() * w.xd();
		final double wysqr = w.yd() * w.yd();
		final double c2 = wxsqr + wysqr;
		final double invc2 = 1.0 / c2;
		double c0, c1, discr, invwx;
		final double invwy;
		if (WB_Math.fastAbs(w.xd()) >= WB_Math.fastAbs(w.yd())) {
			c0 = asqr - wxsqr;
			c1 = -2 * a * w.yd();
			discr = Math.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			invwx = 1.0 / w.xd();
			final double dir0y = -0.5 * (c1 + discr) * invc2;
			dir.add(createVector2D((a - w.yd() * dir0y) * invwx, dir0y));
			final double dir1y = -0.5 * (c1 - discr) * invc2;
			dir.add(createVector2D((a - w.yd() * dir1y) * invwx, dir1y));
		} else {
			c0 = asqr - wysqr;
			c1 = -2 * a * w.xd();
			discr = Math.sqrt(WB_Math.fastAbs(c1 * c1 - 4 * c0 * c2));
			invwy = 1.0 / w.yd();
			final double dir0x = -0.5 * (c1 + discr) * invc2;
			dir.add(createVector2D(dir0x, (a - w.xd() * dir0x) * invwy));
			final double dir1x = -0.5 * (c1 - discr) * invc2;
			dir.add(createVector2D(dir1x, (a - w.xd() * dir1x) * invwy));
		}
		return dir;
	}

	/**
	 * Gets the two 2D lines perpendicular to a 2D line and tangent to a circle.
	 *
	 * @param L
	 *            2D line
	 * @param C
	 *            circle
	 * @return 2D lines perpendicular to line and tangent to circle
	 */
	public List<WB_Line> createPerpendicularLinesTangentToCircle(
			final WB_Line L, final WB_Circle C) {
		final List<WB_Line> result = new ArrayList<WB_Line>(2);
		result.add(createLineWithDirection2D(createPoint2D(
				C.getCenter().xd() + C.getRadius() * L.getDirection().xd(),
				C.getCenter().yd() + C.getRadius() * L.getDirection().yd()),
				createVector2D(-L.getDirection().yd(), L.getDirection().xd())));
		result.add(createLineWithDirection2D(createPoint2D(
				C.getCenter().xd() - C.getRadius() * L.getDirection().xd(),
				C.getCenter().yd() - C.getRadius() * L.getDirection().yd()),
				createVector2D(-L.getDirection().yd(), L.getDirection().xd())));
		return result;
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
	public WB_Ray createRayThroughPoints2D(final WB_Coord p1,
			final WB_Coord p2) {
		return createRayWithDirection2D(createPoint2D(p1),
				createVectorFromTo2D(p1, p2));
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
	public WB_Ray createRayThroughPoints2D(final double x1, final double y1,
			final double x2, final double y2) {
		return createRayWithDirection2D(createPoint2D(x1, y1),
				createVector2D(x2 - x1, y2 - y1));
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
	public WB_Ray createRayWithDirection2D(final WB_Coord origin,
			final WB_Coord direction) {
		return new WB_Ray(createPoint2D(origin), createVector2D(direction));
	}

	/**
	 * Get 2D ray through point with given direction.
	 *
	 * @param ox
	 *            x-ordinate of origin
	 * @param oy
	 *            y-ordinate of origin
	 * @param dx
	 *            x-ordinate of direction
	 * @param dy
	 *            y-ordinate of direction
	 * @return 2D ray through point with given direction
	 */
	public WB_Ray createRayWithDirection2D(final double ox, final double oy,
			final double dx, final double dy) {
		return createRayWithDirection2D(createPoint2D(ox, oy),
				createVector2D(dx, dy));
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
	public WB_Ray createParallelRayThroughPoint2D(final WB_Line L,
			final WB_Coord p) {
		return createRayWithDirection2D(p, L.getDirection());
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
	public WB_Segment createSegment2D(final WB_Coord p1, final WB_Coord p2) {
		return new WB_Segment(createPoint2D(p1), createPoint2D(p2));
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
	public WB_Segment createSegmentWithLength2D(final WB_Coord origin,
			final WB_Coord direction, final double length) {
		return createSegment2D(createPoint2D(origin), createPoint2D(origin)
				.addMulSelf(length, createNormalizedVector2D(direction)));
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
	public WB_Segment createSegment2D(final double x1, final double y1,
			final double x2, final double y2) {
		return createSegment2D(createPoint2D(x1, y1), createVector2D(x2, y2));
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
	public WB_Segment createSegmentWithLength2D(final double ox,
			final double oy, final double dx, final double dy,
			final double length) {
		return createSegment2D(createPoint2D(ox, oy), createPoint2D(ox, oy)
				.addMul(length, createNormalizedVector2D(dx, dy)));
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_PolyLine createPolyLine(final WB_Coord[] points) {
		return new WB_PolyLine(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_PolyLine createPolyLine(
			final Collection<? extends WB_Coord> points) {
		return new WB_PolyLine(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_Ring createRing(final WB_Coord[] points) {
		return new WB_Ring(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_Ring createRing(final List<? extends WB_Coord> points) {
		return new WB_Ring(points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_Polygon createSimplePolygon(final WB_Coord... points) {
		return new WB_Polygon(points);
	}

	public WB_Polygon createSimplePolygon(final WB_CoordCollection points) {
		return new WB_Polygon(points);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public WB_Polygon createSimplePolygon(final WB_Polygon poly) {
		return new WB_JTS.PolygonTriangulatorJTS().makeSimplePolygon(poly);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_Polygon createSimplePolygon(
			final Collection<? extends WB_Coord> points) {
		return new WB_Polygon(points);
	}

	public WB_Polygon createSimplePolygon(final WB_Triangle triangle) {
		return new WB_Polygon(triangle.p1, triangle.p2, triangle.p3);
	}

	public WB_Polygon createSimplePolygon(final WB_Quad quad) {
		return new WB_Polygon(quad.getP1(), quad.getP2(), quad.getP3(), quad.getP4());
	}

	public WB_Polygon createSimplePolygon(final WB_Pentagon pentagon) {
		return new WB_Polygon(pentagon.getP1(), pentagon.getP2(), pentagon.getP3(),
				pentagon.getP4(), pentagon.getP5());
	}

	public WB_Polygon createSimplePolygon(final WB_Hexagon hexagon) {
		return new WB_Polygon(hexagon.getP1(), hexagon.getP2(), hexagon.getP3(), hexagon.getP4(),
				hexagon.getP5(), hexagon.getP6());
	}

	public WB_Polygon createSimplePolygon(final WB_Octagon octagon) {
		return new WB_Polygon(octagon.getP1(), octagon.getP2(), octagon.getP3(), octagon.getP4(),
				octagon.getP5(), octagon.getP6(), octagon.getP7(), octagon.getP8());
	}

	/**
	 *
	 *
	 * @param tuples
	 * @param indices
	 * @return
	 */
	public WB_Polygon createSimplePolygon(final List<? extends WB_Coord> tuples,
			final int[] indices) {
		final List<WB_Coord> coords = new FastList<WB_Coord>();
		for (final int indice : indices) {
			coords.add(tuples.get(indice));
		}
		return createSimplePolygon(coords);
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 * @return
	 */
	public WB_Polygon createPolygonWithHole(final WB_Coord[] points,
			final WB_Coord[] innerpoints) {
		return new WB_Polygon(points, innerpoints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 * @return
	 */
	public WB_Polygon createPolygonWithHole(
			final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> innerpoints) {
		return new WB_Polygon(points, innerpoints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 * @return
	 */
	public WB_Polygon createPolygonWithHoles(final WB_Coord[] points,
			final WB_Coord[][] innerpoints) {
		return new WB_Polygon(points, innerpoints);
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 * @return
	 */
	public WB_Polygon createPolygonWithHoles(
			final Collection<? extends WB_Coord> points,
			final List<? extends WB_Coord>[] innerpoints) {
		return new WB_Polygon(points, innerpoints);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public WB_Polygon createPolygonConvexHull2D(final WB_Polygon poly) {
		return WB_JTS.createPolygonConvexHull2D(poly);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public List<WB_Polygon> createConvexPolygonDecomposition2D(
			final WB_Polygon poly) {
		return WB_PolygonDecomposer.decomposePolygon2D(poly);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons2D(final WB_Polygon poly,
			final double d) {
		return WB_JTS.createBufferedPolygons2D(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons2D(
			final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createBufferedPolygons2D(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons2D(final WB_Polygon poly,
			final double d, final int n) {
		return WB_JTS.createBufferedPolygons2D(poly, d, n);
	}

	public List<WB_Polygon> createBufferedPolygonsStraight2D(
			final WB_Polygon poly, final double d) {
		return WB_JTS.createBufferedPolygonsStraight2D(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	public List<WB_Polygon> createBufferedPolygons2D(
			final Collection<? extends WB_Polygon> poly, final double d,
			final int n) {
		return WB_JTS.createBufferedPolygons2D(poly, d, n);
	}

	public List<WB_Polygon> createBufferedPolygonsStraight2D(
			final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createBufferedPolygonsStraight2D(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public List<WB_Polygon> createBoundaryPolygons2D(final WB_Polygon poly) {
		return WB_JTS.createBoundaryPolygons2D(poly);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public List<WB_Polygon> createBoundaryPolygons2D(
			final Collection<? extends WB_Polygon> poly) {
		List<WB_Polygon> result = new ArrayList<WB_Polygon>();
		if (poly.size() < 1) {
			return result;
		} else if (poly.size() == 1) {
			return createBoundaryPolygons2D(poly.iterator().next());
		} else {
			Iterator<? extends WB_Polygon> pItr = poly.iterator();
			result = createBoundaryPolygons2D(pItr.next());
			while (pItr.hasNext()) {
				result = unionPolygons2D(result,
						createBoundaryPolygons2D(pItr.next()));
			}
			return result;
		}
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons2D(final WB_Polygon poly,
			final double d) {
		return WB_JTS.createRibbonPolygons2D(poly, d);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons2D(
			final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createRibbonPolygons2D(poly, d);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons2D(final WB_Polygon poly,
			final double o, final double i) {
		return WB_JTS.createRibbonPolygons2D(poly, o, i);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	public List<WB_Polygon> createRibbonPolygons2D(
			final Collection<? extends WB_Polygon> poly, final double o,
			final double i) {
		return WB_JTS.createRibbonPolygons2D(poly, o, i);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param tol
	 * @return
	 */
	public List<WB_Polygon> createSimplifiedPolygon2D(final WB_Polygon poly,
			final double tol) {
		return WB_JTS.createSimplifiedPolygon2D(poly, tol);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param max
	 * @return
	 */
	public List<WB_Polygon> createDensifiedPolygon2D(final WB_Polygon poly,
			final double max) {
		return WB_JTS.createDensifiedPolygon2D(poly, max);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> unionPolygons2D(final WB_Polygon poly1,
			final WB_Polygon poly2) {
		return WB_JTS.unionPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> unionPolygons2D(final WB_Polygon poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.unionPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> unionPolygons2D(
			final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.unionPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> subtractPolygons2D(final WB_Polygon poly1,
			final WB_Polygon poly2) {
		return WB_JTS.subtractPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> subtractPolygons2D(final WB_Polygon poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.subtractPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> subtractPolygons2D(
			final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.subtractPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> subtractPolygons2D(
			final Collection<? extends WB_Polygon> poly1,
			final WB_Polygon poly2) {
		return WB_JTS.subtractPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> intersectPolygons2D(final WB_Polygon poly1,
			final WB_Polygon poly2) {
		return WB_JTS.intersectPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> intersectPolygons2D(final WB_Polygon poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.intersectPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> intersectPolygons2D(
			final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.intersectPolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> symDifferencePolygons2D(final WB_Polygon poly1,
			final WB_Polygon poly2) {
		return WB_JTS.symDifferencePolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> symDifferencePolygons2D(final WB_Polygon poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.symDifferencePolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	public List<WB_Polygon> symDifferencePolygons2D(
			final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		return WB_JTS.symDifferencePolygons2D(poly1, poly2);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param container
	 * @return
	 */
	public List<WB_Polygon> constrainPolygons2D(final WB_Polygon poly,
			final WB_Polygon container) {
		return WB_JTS.constrainPolygons2D(poly, container);
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	public List<WB_Polygon> constrainPolygons2D(final WB_Polygon[] polygons,
			final WB_Polygon container) {
		return WB_JTS.constrainPolygons2D(polygons, container);
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	public List<WB_Polygon> constrainPolygons2D(final List<WB_Polygon> polygons,
			final WB_Polygon container) {
		return WB_JTS.constrainPolygons2D(polygons, container);
	}

	public List<WB_Polygon> createTextWithTrueTypeFont(final String text,
			final String fontName, final float pointSize) {
		return createTextWithTrueTypeFont(text, fontName, 0, pointSize, 400.0);
	}

	public List<WB_Polygon> createTextWithTrueTypeFont(final String text,
			final String fontName, final float pointSize,
			final double flatness) {
		return createTextWithTrueTypeFont(text, fontName, 0, pointSize,
				flatness);
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createTextWithTrueTypeFont(final String text,
			final String fontName, final int style, final float pointSize,
			final double flatness) {
		try {
			final InputStream is = new FileInputStream(fontName);
			final Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			return createText(text, font.deriveFont(style, pointSize),
					flatness);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new FastList<WB_Polygon>();
	}

	public List<WB_Polygon> createTextWithFont(final String text,
			final String fontName, final float pointSize) {
		return createTextWithFont(text, fontName, 0, pointSize, 400.0);
	}

	public List<WB_Polygon> createTextWithFont(final String text,
			final String fontName, final float pointSize,
			final double flatness) {
		return createTextWithFont(text, fontName, 0, pointSize, flatness);
	}

	public List<WB_Polygon> createTextWithFont(final String text,
			final String fontName, final int style, final float pointSize,
			final double flatness) {
		InputStream is = null;
		try {
			is = new FileInputStream(fontName);
		} catch (final Exception e) {
			e.printStackTrace();
			return new FastList<WB_Polygon>();
		}
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			return createText(text, font.deriveFont(style, pointSize),
					flatness);
		} catch (final Exception e) {
		}
		try {
			font = Font.createFont(Font.TYPE1_FONT, is);
			return createText(text, font.deriveFont(style, pointSize),
					flatness);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new FastList<WB_Polygon>();
	}

	public List<WB_Polygon> createTextWithOpenTypeFont(final String text,
			final String fontName, final float pointSize) {
		return createTextWithOpenTypeFont(text, fontName, 0, pointSize, 400.0);
	}

	public List<WB_Polygon> createTextWithOpenTypeFont(final String text,
			final String fontName, final float pointSize,
			final double flatness) {
		return createTextWithOpenTypeFont(text, fontName, 0, pointSize,
				flatness);
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createTextWithOpenTypeFont(final String text,
			final String fontName, final int style, final float pointSize,
			final double flatness) {
		try {
			final InputStream is = new FileInputStream(fontName);
			final Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			return createText(text, font.deriveFont(style, pointSize),
					flatness);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new FastList<WB_Polygon>();
	}

	public List<WB_Polygon> createTextWithType1Font(final String text,
			final String fontName, final float pointSize) {
		return createTextWithType1Font(text, fontName, 0, pointSize, 400.0);
	}

	public List<WB_Polygon> createTextWithType1Font(final String text,
			final String fontName, final float pointSize,
			final double flatness) {
		return createTextWithType1Font(text, fontName, 0, pointSize, flatness);
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createTextWithType1Font(final String text,
			final String fontName, final int style, final float pointSize,
			final double flatness) {
		try {
			final InputStream is = new FileInputStream(fontName);
			final Font font = Font.createFont(Font.TYPE1_FONT, is);
			return createText(text, font.deriveFont(style, pointSize),
					flatness);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new FastList<WB_Polygon>();
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @return
	 */
	public List<WB_Polygon> createText(final String text, final String fontName,
			final float pointSize) {
		final Font font = new Font(fontName, 0, (int) pointSize);
		return createText(text, font, 400.0);
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createText(final String text, final String fontName,
			final float pointSize, final double flatness) {
		final Font font = new Font(fontName, 0, (int) pointSize);
		return createText(text, font, flatness);
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createText(final String text, final String fontName,
			final int style, final float pointSize, final double flatness) {
		final Font font = new Font(fontName, style, (int) pointSize);
		return createText(text, font, flatness);
	}

	/**
	 *
	 *
	 * @param text
	 * @param font
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createText(final String text, final Font font,
			final int style, final float pointSize, final double flatness) {
		final Font nfont = font.deriveFont(style, pointSize);
		return createText(text, nfont, flatness);
	}

	/**
	 *
	 *
	 * @param text
	 * @param font
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createText(final String text, final Font font,
			final double flatness) {
		try {
			if (shapereader == null) {
				shapereader = new WB_JTS.ShapeReader();
			}
			final char[] chs = text.toCharArray();
			final FontRenderContext fontContext = new FontRenderContext(null,
					false, true);
			final GlyphVector gv = font.createGlyphVector(fontContext, chs);
			final List<WB_Polygon> geometries = new FastList<WB_Polygon>();
			for (int i = 0; i < gv.getNumGlyphs(); i++) {
				geometries.addAll(
						shapereader.read(gv.getGlyphOutline(i), flatness));
			}
			return geometries;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new FastList<WB_Polygon>();
	}

	/**
	 *
	 *
	 * @param shape
	 * @param flatness
	 * @return
	 */
	public List<WB_Polygon> createShape(final Shape shape,
			final double flatness) {
		if (shapereader == null) {
			shapereader = new WB_JTS.ShapeReader();
		}
		return shapereader.read(shape, flatness);
	}

	/**
	 * Get triangle from 3 points.
	 *
	 * @param p1x
	 *            x-ordinate of first point of triangle
	 * @param p1y
	 *            y-ordinate of first point of triangle
	 * @param p2x
	 *            x-ordinate of second point of triangle
	 * @param p2y
	 *            y-ordinate of second point of triangle
	 * @param p3x
	 *            x-ordinate of third point of triangle
	 * @param p3y
	 *            y-ordinate of third point of triangle
	 * @return triangle
	 */
	public WB_Triangle createTriangle2D(final double p1x, final double p1y,
			final double p2x, final double p2y, final double p3x,
			final double p3y) {
		return createTriangle2D(createPoint2D(p1x, p1y),
				createPoint2D(p2x, p2y), createPoint2D(p3x, p3y));
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
	public WB_Triangle createTriangle2D(final WB_Coord p1, final WB_Coord p2,
			final WB_Coord p3) {
		return new WB_Triangle(createPoint2D(p1), createPoint2D(p2),
				createPoint2D(p3));
	}

	/**
	 * Circle with center and radius.
	 *
	 * @param center
	 * @param normal
	 * @param diameter
	 * @return circle
	 */
	public WB_Circle createCircleWithDiameter(final WB_Coord center,
			final WB_Coord normal, final double diameter) {
		return new WB_Circle(center, normal, 0.5 * diameter);
	}

	/**
	 * Circle with center and diameter.
	 *
	 * @param center
	 * @param normal
	 * @param radius
	 * @return circle
	 */
	public WB_Circle createCircleWithRadius(final WB_Coord center,
			final WB_Coord normal, final double radius) {
		return new WB_Circle(center, normal, radius);
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public WB_Circle createCircleWithRadius(final WB_Coord center,
			final double radius) {
		return new WB_Circle(center, radius);
	}

	/**
	 * Circle with center and diameter.
	 *
	 * @param center
	 * @param diameter
	 * @return circle
	 */
	public WB_Circle createCircleWithDiameter(final WB_Coord center,
			final double diameter) {
		return createCircleWithRadius(center, .5 * diameter);
	}

	/**
	 * Circle with center and radius.
	 *
	 * @param x
	 * @param y
	 * @param radius
	 * @return circle
	 */
	public WB_Circle createCircleWithRadius(final double x, final double y,
			final double radius) {
		return createCircleWithRadius(createPoint2D(x, y), radius);
	}

	/**
	 * Circle with diameter and radius.
	 *
	 * @param x
	 * @param y
	 * @param diameter
	 * @return circle
	 */
	public WB_Circle createCircleWithDiameter(final double x, final double y,
			final double diameter) {
		return createCircleWithRadius(createPoint2D(x, y), .5 * diameter);
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
	public WB_Circle createInversionCircle2D(final WB_Circle C,
			final WB_Circle inversionCircle) {
		if (WB_GeometryOp2D.classifyPointToCircle2D(inversionCircle.getCenter(),
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

	// 3D
	/**
	 * Get circumcircle of 2D triangle, z-ordinate is ignored.
	 *
	 * @param tri
	 *            triangle
	 * @return circumcircle
	 */
	public WB_Circle createCircumcircle2D(final WB_Triangle tri) {
		final double a = tri.a();
		final double b = tri.b();
		final double c = tri.c();
		final double radius = a * b * c
				/ Math.sqrt(2 * a * a * b * b + 2 * b * b * c * c
						+ 2 * a * a * c * c - a * a * a * a - b * b * b * b
						- c * c * c * c);
		final double bx = tri.p2().xd() - tri.p1().xd();
		final double by = tri.p2().yd() - tri.p1().yd();
		final double cx = tri.p3().xd() - tri.p1().xd();
		final double cy = tri.p3().yd() - tri.p1().yd();
		double d = 2 * (bx * cy - by * cx);
		if (WB_Epsilon.isZero(d)) {
			return null;
		}
		d = 1.0 / d;
		final double b2 = bx * bx + by * by;
		final double c2 = cx * cx + cy * cy;
		final double x = (cy * b2 - by * c2) * d;
		final double y = (bx * c2 - cx * b2) * d;
		return createCircleWithRadius(
				createPoint2D(x + tri.p1().xd(), y + tri.p1().yd()), radius);
	}

	/**
	 * Get incircle of triangle, z-ordinate is ignored.
	 *
	 * @param tri
	 *            triangle
	 * @return incircle
	 */
	public WB_Circle createIncircle2D(final WB_Triangle tri) {
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
		return createCircleWithRadius(createPoint2D(x, y), radius);
	}

	/**
	 * Gets the circle through 3 2D points, z-ordinate is ignored.
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return circle through 3 points
	 */
	public WB_Circle createCirclePPP(final WB_Coord p0, final WB_Coord p1,
			final WB_Coord p2) {
		final double[] circumcenter = WB_Predicates.circumcenter2D(toDouble(p0),
				toDouble(p1), toDouble(p2));
		final WB_Point center = createPoint2D(circumcenter[0], circumcenter[1]);
		return createCircleWithRadius(center, center.getDistance2D(p0));
	}

	/**
	 * http://www.cut-the-knot.org/Curriculum/Geometry/GeoGebra/PPL.shtml
	 *
	 * @param p
	 * @param q
	 * @param L
	 * @return circles through 2 points and tangent to line
	 */
	public List<WB_Circle> createCirclePPL(final WB_Coord p, final WB_Coord q,
			final WB_Line L) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		if (!WB_GeometryOp2D.sameSideOfLine2D(p, q, L)) {
			return result;
		}
		if (WB_GeometryOp2D.classifyPointToLine2D(p, L) == WB_Classification.ON
				&& WB_GeometryOp2D.classifyPointToLine2D(q,
						L) == WB_Classification.ON) {
			return result;
		}
		final WB_Line PQ = createLineThroughPoints2D(p, q);
		if (WB_GeometryOp2D.classifyPointToLine2D(p,
				L) == WB_Classification.ON) {
			if (WB_Epsilon.isZeroSq(
					createClosestPointOnLine2D(q, L).getSqDistance(p))) {
				result.add(createCircleWithRadius(
						createPoint2D(p).mulAddMul(0.5, 0.5, q),
						0.5 * WB_GeometryOp2D.getDistanceToPoint2D(p, q)));
			} else {
				final WB_Line perp = createPerpendicularLineThroughPoint2D(L,
						p);
				final WB_Line PQbis = createBisector2D(p, q);
				final WB_Point intersect = createIntersectionPoint2D(perp,
						PQbis);
				result.add(createCircleWithRadius(intersect,
						WB_GeometryOp2D.getDistanceToPoint2D(p, intersect)));
			}
			return result;
		}
		if (WB_GeometryOp2D.classifyPointToLine2D(q,
				L) == WB_Classification.ON) {
			if (WB_Epsilon.isZeroSq(
					createClosestPointOnLine2D(p, L).getSqDistance(q))) {
				result.add(createCircleWithRadius(
						createPoint2D(p).mulAddMulSelf(0.5, 0.5, q),
						0.5 * WB_GeometryOp2D.getDistanceToPoint2D(p, q)));
			} else {
				final WB_Line perp = createPerpendicularLineThroughPoint2D(L,
						q);
				final WB_Line PQbis = createBisector2D(p, q);
				final WB_Point intersect = createIntersectionPoint2D(perp,
						PQbis);
				result.add(createCircleWithRadius(intersect,
						WB_GeometryOp2D.getDistanceToPoint2D(p, intersect)));
			}
			return result;
		}
		final WB_Point F = createIntersectionPoint2D(L, PQ);
		if (F == null) {
			final WB_Point r = createClosestPointOnLine2D(
					createPoint2D(0.5 * p.xd() + 0.5 * q.xd(),
							0.5 * p.yd() + 0.5 * q.yd()),
					L);
			result.add(createCirclePPP(p, q, r));
		} else {
			double d = WB_GeometryOp2D.getDistanceToPoint2D(p, q);
			final WB_Circle OPQ = createCircleThrough2Points(p, q, d).get(0);
			final WB_Point center = F.mulAddMul(0.5, 0.5, OPQ.getCenter());
			final WB_Circle STF = createCircleWithRadius(center,
					center.getDistance2D(F));
			final List<WB_Point> intersections = createIntersectionPoints2D(STF,
					OPQ);
			d = F.getDistance2D(intersections.get(0));
			final WB_Point K = F.addMul(d, L.getDirection());
			final WB_Point J = F.addMul(-d, L.getDirection());
			result.add(createCirclePPP(p, q, K));
			result.add(createCirclePPP(p, q, J));
		}
		return uniqueOnly(result);
	}

	/**
	 * Gets circles tangent to 2 2D lines through point.
	 * http://www.cut-the-knot.org/Curriculum/Geometry/GeoGebra/PLL.shtml
	 *
	 * @param p
	 *            point
	 * @param L1
	 *            line
	 * @param L2
	 *            line
	 * @return circles tangent to 2 2D lines through point
	 */
	public List<WB_Circle> createCirclePLL(final WB_Coord p, final WB_Line L1,
			final WB_Line L2) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>(2);
		final WB_Point A = createIntersectionPoint2D(L1, L2);
		final List<WB_Line> bis = createAngleBisector2D(L1, L2);
		if (A == null) {
			final double r = WB_GeometryOp2D.getDistanceToLine2D(L1.getOrigin(),
					bis.get(0));
			final WB_Circle C = createCircleWithRadius(p, r);
			final List<WB_Point> intersections = createIntersectionPoints2D(
					bis.get(0), C);
			for (final WB_Point point : intersections) {
				result.add(createCircleWithRadius(point, r));
			}
		} else {
			final List<WB_Circle> circles = createCircleTangentTo2Lines(L1, L2,
					100.0);
			final List<WB_Circle> selcircles = new ArrayList<WB_Circle>();
			for (final WB_Circle C : circles) {
				if (WB_GeometryOp2D.sameSideOfLine2D(p, C.getCenter(), L1)
						&& WB_GeometryOp2D.sameSideOfLine2D(p, C.getCenter(),
								L2)) {
					selcircles.add(C);
				}
			}
			for (final WB_Circle C : selcircles) {
				final WB_Point E = new WB_Point(C.getCenter());
				final WB_Line Ap = createLineThroughPoints2D(A, p);
				final List<WB_Point> intersections = createIntersectionPoints2D(
						Ap, C);
				if (intersections.size() == 1) {
					final WB_Point G = intersections.get(0);
					final double AG = A.getDistance2D(G);
					final double AD = A.getDistance2D(p);
					final double AE = A.getDistance2D(E);
					final double AK = AD / AG * AE;
					final WB_Vector v = createNormalizedVector2D(E.sub(A));
					final WB_Point K = createPoint2D(A.addMul(AK, v));
					result.add(createCircleWithRadius(K,
							WB_GeometryOp2D.getDistanceToLine2D(K, L1)));
				} else if (intersections.size() == 2) {
					final WB_Point G = intersections.get(0);
					final WB_Point H = intersections.get(1);
					final double AH = A.getDistance2D(H);
					final double AG = A.getDistance2D(G);
					final double AD = A.getDistance2D(p);
					final double AE = A.getDistance2D(E);
					final double AI = AD / AH * AE;
					final double AK = AD / AG * AE;
					final WB_Vector v = createNormalizedVector2D(E.sub(A));
					final WB_Point I = createPoint2D(A.addMul(AI, v));
					final WB_Point K = createPoint2D(A.addMul(AK, v));
					result.add(createCircleWithRadius(I,
							WB_GeometryOp2D.getDistanceToLine2D(I, L1)));
					result.add(createCircleWithRadius(K,
							WB_GeometryOp2D.getDistanceToLine2D(K, L1)));
				}
			}
		}
		return uniqueOnly(result);
	}

	/**
	 * Gets circles through two 2d points tangent to circle.
	 * http://mathafou.free.fr/pbg_en/sol136.html
	 *
	 * @param p
	 *            2D point
	 * @param q
	 *            2D point
	 * @param C
	 *            circle
	 * @return circles through two 2d points tangent to circle
	 */
	public List<WB_Circle> createCirclePPC(final WB_Coord p, final WB_Coord q,
			final WB_Circle C) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		final WB_Classification pType = WB_GeometryOp2D
				.classifyPointToCircle2D(p, C);
		final WB_Classification qType = WB_GeometryOp2D
				.classifyPointToCircle2D(q, C);
		if (WB_Epsilon.isZero(WB_GeometryOp2D.getDistanceToPoint2D(p, q))) {
			return result;
		}
		// Both points on circle: only solution is circle itself
		if (pType == WB_Classification.ON && qType == WB_Classification.ON) {
			return result;
			// Point p on circle, arbitrary point q.
		} else if (pType == WB_Classification.ON) {
			final WB_Line ABbis = createBisector2D(p, q);
			final WB_Line ATbis = createLineThroughPoints2D(p, C.getCenter());
			final WB_Point center = createIntersectionPoint2D(ABbis, ATbis);
			if (center != null) {
				result.add(createCircleWithRadius(center,
						center.getDistance2D(p)));
			}
			return result;
			// Point q on circle, arbitrary point p.
		} else if (qType == WB_Classification.ON) {
			final WB_Line ABbis = createBisector2D(p, q);
			final WB_Line ATbis = createLineThroughPoints2D(q, C.getCenter());
			final WB_Point center = createIntersectionPoint2D(ABbis, ATbis);
			if (center != null) {
				result.add(createCircleWithRadius(center,
						center.getDistance2D(p)));
			}
			return result;
		}
		// One point inside, one point outside. (All cases with points on circle
		// are already handled). No solution.
		if (pType != qType) {
			return result;
		}
		// Both points outside
		else if (pType == WB_Classification.OUTSIDE) {
			final WB_Line AB = createLineThroughPoints2D(p, q);
			final WB_Line ABbis = createBisector2D(p, q);
			if (WB_Point.isCollinear(C.getCenter(), ABbis.getOrigin(),
					ABbis.getPoint(100.0))) {
				final List<WB_Point> points = createIntersectionPoints2D(ABbis,
						C);
				for (final WB_Point pt : points) {
					result.add(createCirclePPP(pt, p, q));
				}
			} else {
				WB_Vector v = createVector2D(-AB.getDirection().yd(),
						AB.getDirection().xd());
				WB_Point E = WB_Point.addMul(C.getCenter(), 0.5 * C.getRadius(),
						v);
				if (E.isCollinear(p, q)) {
					v = createVector2D(AB.getDirection().yd(),
							-AB.getDirection().xd());
					E = WB_Point.addMul(C.getCenter(), 0.5 * C.getRadius(), v);
				}
				final WB_Circle circle = createCirclePPP(p, q, E);
				final List<WB_Point> intersections = createIntersectionPoints2D(
						circle, C);
				final WB_Line MN = createLineThroughPoints2D(
						intersections.get(0), intersections.get(1));
				final WB_Point point = createIntersectionPoint2D(AB, MN);
				if (point == null) {
					return result;
				}
				final List<WB_Line> tangents = createLinesTangentToCircleThroughPoint(
						C, point);
				for (final WB_Line L : tangents) {
					final WB_Point T = createClosestPointOnLine2D(C.getCenter(),
							L);
					final WB_Line ATbis;
					ATbis = createBisector2D(p, T);
					final WB_Point center = createIntersectionPoint2D(ABbis,
							ATbis);
					if (center != null) {
						result.add(createCircleWithRadius(center,
								center.getDistance2D(p)));
					}
				}
			}
			// Both points inside, solve with inversion
		} else {
			final List<WB_Circle> iresult = new ArrayList<WB_Circle>();
			final WB_Circle iC;
			final WB_Circle iC2;
			final double k2;
			final boolean dp = WB_Epsilon
					.isZero(WB_CoordOp2D.getDistance2D(C.getCenter(), p));
			final boolean dq = WB_Epsilon
					.isZero(WB_CoordOp2D.getDistance2D(C.getCenter(), q));
			if (dp || dq) {
				final WB_Vector v = createNormalizedVector2D(-p.yd() - q.yd(),
						p.xd() + q.xd());
				iC = createCircleWithRadius(
						WB_Point.addMul(C.getCenter(), 0.5 * C.getRadius(), v),
						C.getRadius() * 3);
				k2 = iC.getRadius() * iC.getRadius();
				final double s = k2 / (WB_CoordOp2D.getDistance2D(C.getCenter(),
						iC.getCenter()) - C.getRadius() * C.getRadius());
				iC2 = createCircleWithRadius(
						iC.getCenter().xd() + s
								* (C.getCenter().xd() - iC.getCenter().xd()),
						iC.getCenter().yd() + s
								* (C.getCenter().yd() - iC.getCenter().yd()),
						Math.abs(s) * C.getRadius());
			} else {
				iC = C;
				k2 = iC.getRadius() * iC.getRadius();
				iC2 = C;
			}
			final WB_Point ip = createInversionPoint2D(p, iC);
			final WB_Point iq = createInversionPoint2D(q, iC);
			final WB_Line ABbis = createBisector2D(ip, iq);
			if (WB_Point.isCollinear(iC2.getCenter(), ABbis.getOrigin(),
					ABbis.getPoint(100.0))) {
				final List<WB_Point> points = createIntersectionPoints2D(ABbis,
						iC2);
				for (final WB_Point pt : points) {
					iresult.add(createCirclePPP(pt, ip, iq));
				}
			} else {
				final WB_Line AB = createLineThroughPoints2D(ip, iq);
				WB_Vector v = createVector2D(-AB.getDirection().yd(),
						AB.getDirection().xd());
				WB_Point E = WB_Point.addMul(iC2.getCenter(),
						0.5 * iC2.getRadius(), v);
				if (E.isCollinear(ip, iq)) {
					v = createVector2D(AB.getDirection().yd(),
							-AB.getDirection().xd());
					E = WB_Point.addMul(iC2.getCenter(), 0.5 * iC2.getRadius(),
							v);
				}
				final WB_Circle circle = createCirclePPP(ip, iq, E);
				final List<WB_Point> intersections = createIntersectionPoints2D(
						circle, iC2);
				final WB_Line MN = createLineThroughPoints2D(
						intersections.get(0), intersections.get(1));
				final WB_Point point = createIntersectionPoint2D(AB, MN);
				if (point == null) {
					return result;
				}
				final List<WB_Line> tangents = createLinesTangentToCircleThroughPoint(
						iC2, point);
				for (final WB_Line L : tangents) {
					final WB_Point T = createClosestPointOnLine2D(
							iC2.getCenter(), L);
					final WB_Line ATbis;
					ATbis = createBisector2D(ip, T);
					final WB_Point center = createIntersectionPoint2D(ABbis,
							ATbis);
					if (center != null) {
						iresult.add(createCircleWithRadius(center,
								center.getDistance2D(ip)));
					}
				}
			}
			for (final WB_Circle circle : iresult) {
				final double s = k2 / (WB_CoordOp
						.getSqDistance3D(circle.getCenter(), iC.getCenter())
						- circle.getRadius() * circle.getRadius());
				result.add(createCircleWithRadius(iC.getCenter().xd()
						+ s * (circle.getCenter().xd() - iC.getCenter().xd()),
						iC.getCenter().yd() + s * (circle.getCenter().yd()
								- iC.getCenter().yd()),
						Math.abs(s) * circle.getRadius()));
			}
		}
		return uniqueOnly(result);
	}

	/**
	 * http://www.cut-the-knot.org/Curriculum/Geometry/GeoGebra/PCC.shtml#
	 * solution
	 *
	 * @param p
	 * @param C1
	 * @param C2
	 * @return circles through point and tangent to two circles
	 */
	public List<WB_Circle> createCirclePCC(final WB_Coord p, final WB_Circle C1,
			final WB_Circle C2) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		if (C1.equals(C2)) {
			return result;
		}
		// p on C1
		if (WB_GeometryOp2D.classifyPointToCircle2D(p,
				C1) == WB_Classification.ON) {
			final WB_Line tangent = createLineTangentToCircleInPoint(C1, p);
			return createCirclePLC(p, tangent, C2);
		}
		// p on C2
		if (WB_GeometryOp2D.classifyPointToCircle2D(p,
				C2) == WB_Classification.ON) {
			final WB_Line tangent = createLineTangentToCircleInPoint(C2, p);
			return createCirclePLC(p, tangent, C1);
		}
		final WB_Classification C1toC2 = WB_GeometryOp2D
				.classifyCircleToCircle2D(C1, C2);
		// C1 tangent to C2
		if (WB_CoordOp2D.isTangent2D(C1, C2)) {
			final WB_Point q = createIntersectionPoints2D(C1, C2).get(0);
			result.addAll(createCirclePPC(p, q, C1));
			// C1 inside C2, transform to outside case
		} else if (C1toC2 == WB_Classification.INSIDE) {
			if (WB_GeometryOp2D.classifyPointToCircle2D(p,
					C1) == WB_Classification.INSIDE) {
				return result;
			}
			final WB_Vector v = !WB_Epsilon.isZero(
					WB_CoordOp2D.getDistance2D(C1.getCenter(), C2.getCenter()))
							? createNormalizedVectorFromTo2D(C1.getCenter(),
									C2.getCenter())
							: WB_VectorGenerator.X();
			WB_Point invcenter = WB_Point.addMul(C1.getCenter(),
					0.5 * (C1.getRadius() + C2.getRadius()), v);
			if (WB_Epsilon.isZero(invcenter.getDistance2D(p))) {
				invcenter = WB_Point.addMul(C1.getCenter(), C1.getRadius()
						+ 0.4 * (C2.getRadius() - C1.getRadius()), v);
			}
			final WB_Circle invC = createCircleWithRadius(invcenter,
					2 * (C1.getRadius() + C2.getRadius()));
			final WB_Point q = createInversionPoint2D(p, invC);
			final WB_Circle invC1 = createInversionCircle2D(C1, invC);
			final WB_Circle invC2 = createInversionCircle2D(C2, invC);
			if (invC1 != null) {
				final List<WB_Circle> invResult = createCirclePCC(q, invC1,
						invC2);
				for (final WB_Circle inv : invResult) {
					result.add(createInversionCircle2D(inv, invC));
				}
			}
			// C2 inside C1, transfrom to outside case
		} else if (C1toC2 == WB_Classification.CONTAINING) {
			if (WB_GeometryOp2D.classifyPointToCircle2D(p,
					C2) == WB_Classification.INSIDE) {
				return result;
			}
			final WB_Vector v = !WB_Epsilon.isZero(
					WB_CoordOp2D.getDistance2D(C1.getCenter(), C2.getCenter()))
							? createNormalizedVectorFromTo2D(C2.getCenter(),
									C1.getCenter())
							: WB_VectorGenerator.X();
			WB_Point invcenter = WB_Point.addMul(C2.getCenter(),
					0.5 * (C1.getRadius() + C2.getRadius()), v);
			if (WB_Epsilon.isZero(invcenter.getDistance2D(p))) {
				invcenter = WB_Point.addMul(C2.getCenter(), C2.getRadius()
						+ 0.4 * (C1.getRadius() - C2.getRadius()), v);
			}
			final WB_Circle invC = createCircleWithRadius(invcenter,
					2 * (C1.getRadius() + C2.getRadius()));
			final WB_Point q = createInversionPoint2D(p, invC);
			final WB_Circle invC1 = createInversionCircle2D(C1, invC);
			final WB_Circle invC2 = createInversionCircle2D(C2, invC);
			if (invC1 != null) {
				final List<WB_Circle> invResult = createCirclePCC(q, invC1,
						invC2);
				for (final WB_Circle inv : invResult) {
					result.add(createInversionCircle2D(inv, invC));
				}
			}
		}
		// C1 and C2 outside or C1 and C2 crossing with p in intersection or
		// completely outside
		else if (C1toC2 == WB_Classification.OUTSIDE
				|| C1toC2 == WB_Classification.CROSSING
						&& !(WB_GeometryOp2D.classifyPointToCircle2D(p,
								C1) == WB_Classification.OUTSIDE)
								^ WB_GeometryOp2D.classifyPointToCircle2D(p,
										C2) == WB_Classification.OUTSIDE) {
			final List<WB_Line> tangents = createLinesTangentTo2Circles(C1, C2);
			// if ((WB_Classify.classifyPointToCircle2D(p, C1) ==
			// WB_Classification.INSIDE)
			// || (WB_Classify.classifyPointToCircle2D(p, C2) ==
			// WB_Classification.INSIDE)) {
			// return result;
			// }
			if (WB_GeometryOp2D.classifyPointToCircle2D(p,
					C1) == WB_Classification.ON) {
				final WB_Line L = createLineTangentToCircleInPoint(C1, p);
				return createCirclePLC(p, L, C2);
			}
			if (WB_GeometryOp2D.classifyPointToCircle2D(p,
					C2) == WB_Classification.ON) {
				final WB_Line L = createLineTangentToCircleInPoint(C2, p);
				return createCirclePLC(p, L, C1);
			}
			WB_Line T1;
			WB_Line T2;
			WB_Point point;
			if (tangents.size() > 1) {
				T1 = tangents.get(0);
				T2 = tangents.get(1);
				point = createIntersectionPoint2D(T1, T2);
				if (point != null) {
					final WB_Point G = createClosestPointOnLine2D(
							C1.getCenter(), T1);
					final WB_Point H = createClosestPointOnLine2D(
							C2.getCenter(), T1);
					final WB_Circle circle = createCirclePPP(G, H, p);
					final WB_Line Pp = createLineThroughPoints2D(p, point);
					final List<WB_Point> intersections = createIntersectionPoints2D(
							Pp, circle);
					WB_Point Ep = null;
					if (!WB_Epsilon
							.isZero(intersections.get(0).getDistance2D(p))) {
						Ep = intersections.get(0);
					} else if (!WB_Epsilon
							.isZero(intersections.get(1).getDistance2D(p))) {
						Ep = intersections.get(1);
					}
					if (Ep != null) {
						result.addAll(createCirclePPC(p, Ep, C1));
					}
				} else {// tangents T1 and T2 are parallel
					final WB_Point G = createClosestPointOnLine2D(
							C1.getCenter(), T1);
					final WB_Point H = createClosestPointOnLine2D(
							C2.getCenter(), T1);
					final WB_Circle circle = createCirclePPP(G, H, p);
					final WB_Line Pp = createParallelLineThroughPoint2D(T1, p);
					final List<WB_Point> intersections = createIntersectionPoints2D(
							Pp, circle);
					if (intersections.size() == 2) {
						WB_Point Ep = null;
						if (!WB_Epsilon.isZero(
								intersections.get(0).getDistance2D(p))) {
							Ep = intersections.get(0);
						} else if (!WB_Epsilon.isZero(
								intersections.get(1).getDistance2D(p))) {
							Ep = intersections.get(1);
						}
						if (Ep != null) {
							result.addAll(createCirclePPC(p, Ep, C1));
						}
					} else if (intersections.size() == 1) {
						final WB_Line L = createLineThroughPoints2D(
								C1.getCenter(), C2.getCenter());
						result.addAll(createCircleLCC(L, C1, C2));
						final List<WB_Circle> filter = new ArrayList<WB_Circle>();
						for (int i = 0; i < result.size(); i++) {
							final WB_Circle C = result.get(i);
							if (WB_CoordOp2D.isTangent2D(C, C1)
									&& WB_CoordOp2D.isTangent2D(C, C2)
									&& WB_GeometryOp2D.classifyPointToCircle2D(
											p, C) == WB_Classification.ON) {
								filter.add(C);
							}
						}
						return uniqueOnly(filter);
					}
				}
			}
			if (tangents.size() == 4) {
				T1 = tangents.get(2);
				T2 = tangents.get(3);
				point = createIntersectionPoint2D(T1, T2);
				if (point != null) {
					final WB_Point G = createClosestPointOnLine2D(
							C1.getCenter(), T1);
					final WB_Point H = createClosestPointOnLine2D(
							C2.getCenter(), T1);
					final WB_Circle circle = createCirclePPP(G, H, p);
					final WB_Line Pp = createLineThroughPoints2D(p, point);
					final List<WB_Point> intersections = createIntersectionPoints2D(
							Pp, circle);
					WB_Point Ep = null;
					if (!WB_Epsilon
							.isZero(intersections.get(0).getDistance2D(p))) {
						Ep = intersections.get(0);
					} else if (!WB_Epsilon
							.isZero(intersections.get(1).getDistance2D(p))) {
						Ep = intersections.get(1);
					}
					if (Ep != null) {
						result.addAll(createCirclePPC(p, Ep, C1));
					}
				}
			}
		}
		// C1 and C2 crossing and p in only one of the two circles
		else {
			if (WB_GeometryOp2D.classifyPointToCircle2D(p,
					C1) == WB_Classification.INSIDE) {
				final double r1 = C1.getRadius()
						- WB_CoordOp2D.getDistance2D(C1.getCenter(), p);
				final double r2 = WB_CoordOp2D.getDistance2D(C2.getCenter(), p)
						- C2.getRadius();
				WB_Circle invC;
				if (r1 <= r2) {
					final WB_Coord v = createLineThroughPoints2D(C1.getCenter(),
							p).getDirection();
					final WB_Point center = createPoint2D(p)
							.addMulSelf(0.45 * r1, v);
					invC = createCircleWithRadius(center, 0.45 * r1);
				} else {
					final WB_Coord v = createLineThroughPoints2D(C2.getCenter(),
							p).getDirection();
					final WB_Point center = createPoint2D(p)
							.addMulSelf(0.45 * r2, v);
					invC = createCircleWithRadius(center, 0.45 * r2);
				}
				final WB_Circle invC1 = createInversionCircle2D(C1, invC);
				final WB_Circle invC2 = createInversionCircle2D(C2, invC);
				if (invC1 != null) {
					final List<WB_Circle> invResult = createCirclePCC(p, invC1,
							invC2);
					for (final WB_Circle inv : invResult) {
						result.add(createInversionCircle2D(inv, invC));
					}
				}
			} else {
				final double r1 = -C1.getRadius()
						+ WB_CoordOp2D.getDistance2D(C1.getCenter(), p);
				final double r2 = -WB_CoordOp2D.getDistance2D(C2.getCenter(), p)
						+ C2.getRadius();
				WB_Circle invC;
				if (r1 <= r2) {
					final WB_Coord v = createLineThroughPoints2D(C1.getCenter(),
							p).getDirection();
					final WB_Point center = createPoint2D(p)
							.addMulSelf(0.45 * r1, v);
					invC = createCircleWithRadius(center, 0.45 * r1);
				} else {
					final WB_Coord v = createLineThroughPoints2D(C2.getCenter(),
							p).getDirection();
					final WB_Point center = createPoint2D(p)
							.addMulSelf(0.45 * r2, v);
					invC = createCircleWithRadius(center, 0.45 * r2);
				}
				final WB_Circle invC1 = createInversionCircle2D(C1, invC);
				final WB_Circle invC2 = createInversionCircle2D(C2, invC);
				if (invC1 != null) {
					final List<WB_Circle> invResult = createCirclePCC(p, invC1,
							invC2);
					for (final WB_Circle inv : invResult) {
						result.add(createInversionCircle2D(inv, invC));
					}
				}
			}
		}
		return uniqueOnly(result);
	}

	/**
	 * Gets circles through a 2D point tangent to a circle and a 2D line.
	 * http://www.epab.bme.hu/geoc2/GC2_Lecture_notes_11_Spring.pdf
	 *
	 * @param p
	 *            2D point
	 * @param L
	 *            2D line
	 * @param C
	 *            circle
	 * @return circles through a 2D point tangent to circle and 2D line
	 */
	public List<WB_Circle> createCirclePLC(final WB_Coord p, final WB_Line L,
			final WB_Circle C) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		createIntersectionPoints2D(L, C).size();
		WB_Line Lperp = createPerpendicularLineThroughPoint2D(L, C.getCenter());
		if (WB_GeometryOp2D.classifyPointToCircle2D(p,
				C) == WB_Classification.ON) {
			final WB_Line tangent = createLineTangentToCircleInPoint(C, p);
			result.addAll(createCirclePLL(p, tangent, L));
		} else if (WB_GeometryOp2D.classifyPointToLine2D(p,
				L) == WB_Classification.ON) {
			List<WB_Point> points = createIntersectionPoints2D(Lperp, C);
			final WB_Point A1 = points.get(0);
			final WB_Point A2 = points.get(1);
			if (WB_GeometryOp2D.classifyPointToLine2D(p,
					Lperp) != WB_Classification.ON) {
				final WB_Point B = createIntersectionPoint2D(L, Lperp);
				if (WB_Epsilon.isZero(A2.getDistance2D(B))) {
					final WB_Line A1P = createLineThroughPoints2D(A1, p);
					points = createIntersectionPoints2D(A1P, C);
					WB_Point Q1 = null;
					if (!WB_Epsilon.isZeroSq(points.get(0).getSqDistance(A1))) {
						Q1 = points.get(0);
					} else if (!WB_Epsilon
							.isZeroSq(points.get(1).getSqDistance(A1))) {
						Q1 = points.get(1);
					}
					if (Q1 != null) {
						Lperp = createPerpendicularLineThroughPoint2D(L, p);
						final WB_Line Q1P = createBisector2D(Q1, p);
						final WB_Point intersection = createIntersectionPoint2D(
								Lperp, Q1P);
						result.add(createCircleWithRadius(intersection,
								intersection.getDistance2D(Q1)));
					}
				} else if (!A2.isCollinear(B, p)) {
					final WB_Line A1P = createLineThroughPoints2D(A1, p);
					final WB_Circle BA2P = createCirclePPP(B, A2, p);
					points = createIntersectionPoints2D(A1P, BA2P);
					WB_Point Q1 = null;
					if (!WB_Epsilon.isZeroSq(points.get(0).getSqDistance(p))) {
						Q1 = points.get(0);
					} else if (!WB_Epsilon
							.isZeroSq(points.get(1).getSqDistance(p))) {
						Q1 = points.get(1);
					}
					if (Q1 != null) {
						result.addAll(createCirclePPL(p, Q1, L));
					}
				}
				if (WB_Epsilon.isZero(A1.getDistance2D(B))) {
					final WB_Line A2P = createLineThroughPoints2D(A2, p);
					points = createIntersectionPoints2D(A2P, C);
					WB_Point Q1 = null;
					if (!WB_Epsilon.isZeroSq(points.get(0).getSqDistance(A2))) {
						Q1 = points.get(0);
					} else if (!WB_Epsilon
							.isZeroSq(points.get(1).getSqDistance(A1))) {
						Q1 = points.get(1);
					}
					if (Q1 != null) {
						Lperp = createPerpendicularLineThroughPoint2D(L, p);
						final WB_Line Q1P = createBisector2D(Q1, p);
						final WB_Point intersection = createIntersectionPoint2D(
								Lperp, Q1P);
						result.add(createCircleWithRadius(intersection,
								intersection.getDistance2D(Q1)));
					}
				} else if (!A1.isCollinear(B, p)) {
					final WB_Line A2P = createLineThroughPoints2D(A2, p);
					final WB_Circle BA1P = createCirclePPP(B, A1, p);
					points = createIntersectionPoints2D(A2P, BA1P);
					WB_Point Q2 = null;
					if (!WB_Epsilon.isZeroSq(points.get(0).getSqDistance(p))) {
						Q2 = points.get(0);
					} else if (!WB_Epsilon
							.isZeroSq(points.get(1).getSqDistance(p))) {
						Q2 = points.get(1);
					}
					if (Q2 != null) {
						result.addAll(createCirclePPL(p, Q2, L));
					}
				}
			} else {
				double d = WB_GeometryOp2D.getDistanceToLine2D(A1, L);
				if (!WB_Epsilon.isZero(d)) {
					result.add(createCircleWithRadius(
							createInterpolatedPoint2D(p, A1, 0.5), 0.5 * d));
				}
				d = WB_GeometryOp2D.getDistanceToLine2D(A2, L);
				if (!WB_Epsilon.isZero(d)) {
					result.add(createCircleWithRadius(
							createInterpolatedPoint2D(p, A2, 0.5), 0.5 * d));
				}
			}
		} else {
			final boolean tangentcircle = WB_GeometryOp2D
					.classifyCircleToLine2D(C, L) == WB_Classification.TANGENT;
			if (tangentcircle) {
				result.addAll(createCirclePPL(p,
						createClosestPointOnLine2D(C.getCenter(), L), L));
				if (WB_GeometryOp2D.classifyPointToLine2D(p,
						L) == WB_GeometryOp2D
								.classifyPointToLine2D(C.getCenter(), L)
						&& WB_GeometryOp2D.classifyPointToCircle2D(p,
								C) != WB_Classification.INSIDE) {
					final WB_Circle inversion = createCircleWithRadius(p,
							100.0);
					WB_Point p1 = createInversionPoint2D(L.getOrigin(),
							inversion);
					WB_Point p2 = createInversionPoint2D(L.getPoint(100.0),
							inversion);
					final WB_Circle invL = createCirclePPP(p, p1, p2);
					p1 = createInversionPoint2D(WB_Point.addMul(C.getCenter(),
							C.getRadius(), WB_VectorGenerator.X()), inversion);
					p2 = createInversionPoint2D(WB_Point.addMul(C.getCenter(),
							-C.getRadius(),WB_VectorGenerator.X()), inversion);
					final WB_Point p3 = createInversionPoint2D(WB_Point.addMul(
							C.getCenter(), C.getRadius(), WB_VectorGenerator.Y()), inversion);
					final WB_Circle invC = createCirclePPP(p1, p2, p3);
					final List<WB_Line> invResult = createLinesTangentTo2Circles(
							invL, invC);
					for (int i = 0; i < Math.min(2, invResult.size()); i++) {
						final WB_Line inv = invResult.get(i);
						p1 = createInversionPoint2D(inv.getOrigin(), inversion);
						p2 = createInversionPoint2D(inv.getPoint(100.0),
								inversion);
						result.add(createCirclePPP(p, p1, p2));
					}
				}
			} else {
				final WB_Circle inversion = createCircleWithRadius(p, 100.0);
				WB_Point p1 = createInversionPoint2D(L.getOrigin(), inversion);
				WB_Point p2 = createInversionPoint2D(L.getPoint(100.0),
						inversion);
				final WB_Circle invL = createCirclePPP(p, p1, p2);
				p1 = createInversionPoint2D(
						WB_Point.addMul(C.getCenter(), C.getRadius(),WB_VectorGenerator.X()),
						inversion);
				p2 = createInversionPoint2D(
						WB_Point.addMul(C.getCenter(), -C.getRadius(), WB_VectorGenerator.X()),
						inversion);
				final WB_Point p3 = createInversionPoint2D(
						WB_Point.addMul(C.getCenter(), C.getRadius(), WB_VectorGenerator.Y()),
						inversion);
				final WB_Circle invC = createCirclePPP(p1, p2, p3);
				final List<WB_Line> invResult = createLinesTangentTo2Circles(
						invL, invC);
				for (int i = 0; i < invResult.size(); i++) {
					final WB_Line inv = invResult.get(i);
					p1 = createInversionPoint2D(inv.getOrigin(), inversion);
					p2 = createInversionPoint2D(inv.getPoint(100.0), inversion);
					result.add(createCirclePPP(p, p1, p2));
				}
			}
		}
		final List<WB_Circle> filter = new ArrayList<WB_Circle>();
		for (int i = 0; i < result.size(); i++) {
			if (!C.equals(result.get(i))) {
				filter.add(result.get(i));
			}
		}
		return uniqueOnly(filter);
	}

	/**
	 * Gets the circle tangent to 3 2D lines.
	 *
	 * @param L1
	 *
	 * @param L2
	 *
	 * @param L3
	 *
	 * @return circle tangent to 3 lines
	 */
	public List<WB_Circle> createCircleLLL(final WB_Line L1, final WB_Line L2,
			final WB_Line L3) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		final List<WB_Line> lines12 = createAngleBisector2D(L1, L2);
		final List<WB_Line> lines23 = createAngleBisector2D(L2, L3);
		final List<WB_Line> lines31 = createAngleBisector2D(L3, L1);
		final List<WB_Point> intersections = new ArrayList<WB_Point>();
		WB_Point point;
		for (int i = 0; i < lines12.size(); i++) {
			for (int j = 0; j < lines23.size(); j++) {
				point = createIntersectionPoint2D(lines12.get(i),
						lines23.get(j));
				if (point != null) {
					intersections.add(point);
				}
			}
			for (int j = 0; j < lines31.size(); j++) {
				point = createIntersectionPoint2D(lines12.get(i),
						lines31.get(j));
				if (point != null) {
					intersections.add(point);
				}
			}
		}
		for (int i = 0; i < lines23.size(); i++) {
			for (int j = 0; j < lines31.size(); j++) {
				point = createIntersectionPoint2D(lines23.get(i),
						lines31.get(j));
				if (point != null) {
					intersections.add(point);
				}
			}
		}
		for (final WB_Point p : intersections) {
			final WB_Point p2 = createClosestPointOnLine2D(p, L1);
			result.add(createCircleWithRadius(p, p.getDistance2D(p2)));
		}
		return uniqueOnly(result);
	}

	/**
	 * Gets circles tangent to 2 2D lines and a circle.
	 *
	 * @param L1
	 *            line
	 * @param L2
	 *            line
	 * @param C
	 *            circle
	 * @return circles tangent to 2 2D lines through point
	 */
	public List<WB_Circle> createCircleLLC(final WB_Line L1, final WB_Line L2,
			final WB_Circle C) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		final WB_Point p = createPoint2D(C.getCenter());
		final WB_Point A = createIntersectionPoint2D(L1, L2);
		final List<WB_Line> bis = createAngleBisector2D(L1, L2);
		if (A == null) {
			final WB_Line bisec = bis.get(0);
			final double d = 0.5
					* WB_GeometryOp2D.getDistanceToLine2D(L1.getOrigin(), L2);
			final WB_Circle C1 = createCircleWithRadius(C.getCenter(),
					d + C.getRadius());
			final WB_Circle C2 = createCircleWithRadius(C.getCenter(),
					d - C.getRadius());
			final List<WB_Point> points = createIntersectionPoints2D(bisec, C1);
			points.addAll(createIntersectionPoints2D(bisec, C2));
			for (final WB_Point point : points) {
				result.add(createCircleWithRadius(point, d));
			}
		} else if (WB_Epsilon.isZeroSq(A.getSqDistance(p))) {
			final List<WB_Point> points = createIntersectionPoints2D(bis.get(0),
					C);
			points.addAll(createIntersectionPoints2D(bis.get(1), C));
			for (final WB_Point point : points) {
				result.addAll(createCirclePLL(point, L1, L2));
			}
		} else {
			final WB_Vector v1 = createVector2D(-L1.getDirection().yd(),
					L1.getDirection().xd());
			final WB_Vector v2 = createVector2D(-L2.getDirection().yd(),
					L2.getDirection().xd());
			WB_Line L1s = createLineWithDirection2D(
					new WB_Point(L1.getOrigin()).addMulSelf(-C.getRadius(), v1),
					L1.getDirection());
			WB_Line L2s = createLineWithDirection2D(
					new WB_Point(L2.getOrigin()).addMulSelf(-C.getRadius(), v2),
					L2.getDirection());
			List<WB_Circle> tmp = createCirclePLL(p, L1s, L2s);
			for (final WB_Circle circle : tmp) {
				result.add(createCircleWithRadius(circle.getCenter(),
						WB_GeometryOp2D.getDistanceToLine2D(circle.getCenter(),
								L1)));
			}
			L1s = createLineWithDirection2D(
					new WB_Point(L1.getOrigin()).addMulSelf(C.getRadius(), v1),
					L1.getDirection());
			L2s = createLineWithDirection2D(
					new WB_Point(L2.getOrigin()).addMulSelf(C.getRadius(), v2),
					L2.getDirection());
			tmp = createCirclePLL(p, L1s, L2s);
			for (final WB_Circle circle : tmp) {
				result.add(createCircleWithRadius(circle.getCenter(),
						WB_GeometryOp2D.getDistanceToLine2D(circle.getCenter(),
								L1)));
			}
			L1s = createLineWithDirection2D(
					new WB_Point(L1.getOrigin()).addMulSelf(-C.getRadius(), v1),
					L1.getDirection());
			L2s = createLineWithDirection2D(
					new WB_Point(L2.getOrigin()).addMulSelf(C.getRadius(), v2),
					L2.getDirection());
			tmp = createCirclePLL(p, L1s, L2s);
			for (final WB_Circle circle : tmp) {
				result.add(createCircleWithRadius(circle.getCenter(),
						WB_GeometryOp2D.getDistanceToLine2D(circle.getCenter(),
								L1)));
			}
			L1s = createLineWithDirection2D(
					new WB_Point(L1.getOrigin()).addMulSelf(C.getRadius(), v1),
					L1.getDirection());
			L2s = createLineWithDirection2D(
					new WB_Point(L2.getOrigin()).addMulSelf(-C.getRadius(), v2),
					L2.getDirection());
			tmp = createCirclePLL(p, L1s, L2s);
			for (final WB_Circle circle : tmp) {
				result.add(createCircleWithRadius(circle.getCenter(),
						WB_GeometryOp2D.getDistanceToLine2D(circle.getCenter(),
								L1)));
			}
		}
		final List<WB_Circle> filter = new ArrayList<WB_Circle>();
		for (int i = 0; i < result.size(); i++) {
			if (!result.get(i).equals(C)
					&& WB_Epsilon.isEqualAbs(result.get(i).getRadius(),
							WB_GeometryOp2D.getDistanceToLine2D(
									result.get(i).getCenter(), L1))
					&& WB_Epsilon.isEqualAbs(result.get(i).getRadius(),
							WB_GeometryOp2D.getDistanceToLine2D(
									result.get(i).getCenter(), L2))) {
				filter.add(result.get(i));
			}
		}
		return uniqueOnly(filter);
	}

	/**
	 * Gets circles tangent to 2D line and two circles.
	 *
	 * @param L
	 *            line
	 * @param C1
	 *            circle
	 * @param C2
	 *            circle
	 * @return circles tangent to 2D line and two circles
	 */
	public List<WB_Circle> createCircleLCC(final WB_Line L, final WB_Circle C1,
			final WB_Circle C2) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		final List<WB_Circle> tmp;
		if (C1.getRadius() == C2.getRadius()) {
			final WB_Vector v = createVector2D(-L.getDirection().yd(),
					L.getDirection().xd());
			final WB_Line L1s = createLineWithDirection2D(
					WB_Vector.addMul(L.getOrigin(), -C1.getRadius(), v),
					L.getDirection());
			final WB_Line L2s = createLineWithDirection2D(
					WB_Vector.addMul(L.getOrigin(), C1.getRadius(), v),
					L.getDirection());
			tmp = createCirclePPL(C1.getCenter(), C2.getCenter(), L1s);
			tmp.addAll(createCirclePPL(C1.getCenter(), C2.getCenter(), L2s));
			createCircleWithRadius(C1.getCenter(),
					C1.getRadius() + C2.getRadius());
			tmp.addAll(
					createCirclePLC(C1.getCenter(), L1s, createCircleWithRadius(
							C2.getCenter(), C1.getRadius() + C2.getRadius())));
			tmp.addAll(
					createCirclePLC(C1.getCenter(), L2s, createCircleWithRadius(
							C2.getCenter(), C1.getRadius() + C2.getRadius())));
			tmp.addAll(
					createCirclePLC(C2.getCenter(), L1s, createCircleWithRadius(
							C1.getCenter(), C1.getRadius() + C2.getRadius())));
			tmp.addAll(
					createCirclePLC(C2.getCenter(), L2s, createCircleWithRadius(
							C1.getCenter(), C1.getRadius() + C2.getRadius())));
		} else {
			WB_Circle Cm;
			WB_Circle CM;
			if (C1.getRadius() < C2.getRadius()) {
				Cm = C1;
				CM = C2;
			} else {
				Cm = C2;
				CM = C1;
			}
			WB_Circle C = createCircleWithRadius(CM.getCenter(),
					CM.getRadius() - Cm.getRadius());
			final WB_Vector v = createVector2D(-L.getDirection().yd(),
					L.getDirection().xd());
			final WB_Line L1s = createLineWithDirection2D(
					WB_Vector.addMul(L.getOrigin(), -Cm.getRadius(), v),
					L.getDirection());
			final WB_Line L2s = createLineWithDirection2D(
					WB_Vector.addMul(L.getOrigin(), Cm.getRadius(), v),
					L.getDirection());
			tmp = createCirclePLC(Cm.getCenter(), L1s, C);
			tmp.addAll(createCirclePLC(Cm.getCenter(), L2s, C));
			C = createCircleWithRadius(CM.getCenter(),
					CM.getRadius() + Cm.getRadius());
			tmp.addAll(createCirclePLC(Cm.getCenter(), L1s, C));
			tmp.addAll(createCirclePLC(Cm.getCenter(), L2s, C));
		}
		for (final WB_Circle circle : tmp) {
			final WB_Circle newC = createCircleWithRadius(circle.getCenter(),
					WB_GeometryOp2D.getDistanceToLine2D(circle.getCenter(), L));
			if (WB_CoordOp2D.isTangent2D(newC, C1)
					&& WB_CoordOp2D.isTangent2D(newC, C2)) {
				result.add(newC);
			}
		}
		final List<WB_Circle> filter = new ArrayList<WB_Circle>();
		for (int i = 0; i < result.size(); i++) {
			if (!result.get(i).equals(C1) && !result.get(i).equals(C2)) {
				filter.add(result.get(i));
			}
		}
		return uniqueOnly(filter);
	}

	/**
	 *
	 *
	 * @param C1
	 * @param C2
	 * @param C3
	 * @return
	 */
	public List<WB_Circle> createCircleCCC(WB_Circle C1, WB_Circle C2,
			WB_Circle C3) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		if (C1.equals(C2) || C2.equals(C3) || C1.equals(C3)) {
			return result;
		}
		WB_Circle C;
		if (C1.getRadius() > C2.getRadius()) {
			C = C2;
			C2 = C1;
			C1 = C;
		}
		if (C2.getRadius() > C3.getRadius()) {
			C = C3;
			C3 = C2;
			C2 = C;
		}
		if (C1.getRadius() > C2.getRadius()) {
			C = C2;
			C2 = C1;
			C1 = C;
		}
		// if (C1.getCenter().isCollinear(C2.getCenter(), C3.getCenter())) {
		final List<WB_Circle> circles = new ArrayList<WB_Circle>();
		if (C1.getRadius() == C2.getRadius()
				&& C2.getRadius() == C3.getRadius()) {
			final double R = C1.getRadius();
			circles.add(createCirclePPP(C1.getCenter(), C2.getCenter(),
					C3.getCenter()));
			circles.addAll(createCirclePPC(C1.getCenter(), C2.getCenter(),
					createCircleWithRadius(C3.getCenter(), 2 * R)));
			circles.addAll(createCirclePPC(C1.getCenter(), C3.getCenter(),
					createCircleWithRadius(C2.getCenter(), 2 * R)));
			circles.addAll(createCirclePPC(C3.getCenter(), C2.getCenter(),
					createCircleWithRadius(C1.getCenter(), 2 * R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
		} else if (C1.getRadius() == C2.getRadius()) {
			final double R = C1.getRadius();
			circles.addAll(createCirclePPC(C1.getCenter(), C2.getCenter(),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePPC(C1.getCenter(), C2.getCenter(),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), 2 * R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), 2 * R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
		} else if (C2.getRadius() == C3.getRadius()) {
			double R = C1.getRadius();
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() - R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() - R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
			R = C2.getRadius();
			circles.addAll(createCirclePCC(C2.getCenter(),
					createCircleWithRadius(C1.getCenter(), C1.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C3.getCenter(),
					createCircleWithRadius(C1.getCenter(), C1.getRadius() + R),
					createCircleWithRadius(C2.getCenter(),
							C2.getRadius() + R)));
			circles.addAll(createCirclePPC(C2.getCenter(), C3.getCenter(),
					createCircleWithRadius(C1.getCenter(),
							C1.getRadius() + R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
		} else {
			double R = C1.getRadius();
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() - R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C1.getCenter(),
					createCircleWithRadius(C2.getCenter(), C2.getRadius() - R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
			R = C2.getRadius();
			circles.addAll(createCirclePCC(C2.getCenter(),
					createCircleWithRadius(C1.getCenter(), C1.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() + R)));
			circles.addAll(createCirclePCC(C2.getCenter(),
					createCircleWithRadius(C1.getCenter(), C1.getRadius() + R),
					createCircleWithRadius(C3.getCenter(),
							C3.getRadius() - R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
			R = C3.getRadius();
			circles.addAll(createCirclePCC(C3.getCenter(),
					createCircleWithRadius(C1.getCenter(), C1.getRadius() + R),
					createCircleWithRadius(C2.getCenter(),
							C2.getRadius() + R)));
			for (final WB_Circle circle : circles) {
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() + R));
				result.add(createCircleWithRadius(circle.getCenter(),
						circle.getRadius() - R));
			}
		}
		final List<WB_Circle> filter = new ArrayList<WB_Circle>();
		for (int i = 0; i < result.size(); i++) {
			C = result.get(i);
			if (!C.equals(C1) && !C.equals(C2) && !C.equals(C3)) {
				if (WB_CoordOp2D.isTangent2D(C, C1)
						&& WB_CoordOp2D.isTangent2D(C, C2)
						&& WB_CoordOp2D.isTangent2D(C, C3)) {
					filter.add(result.get(i));
				}
			}
		}
		return uniqueOnly(filter);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	private double[] toDouble(final WB_Coord p) {
		return new double[] { p.xd(), p.yd(), 0 };
	}

	/**
	 *
	 *
	 * @param circles
	 * @return
	 */
	private List<WB_Circle> uniqueOnly(final List<WB_Circle> circles) {
		final List<WB_Circle> uniqcircles = new ArrayList<WB_Circle>();
		for (int i = 0; i < circles.size(); i++) {
			boolean uniq = true;
			for (int j = 0; j < uniqcircles.size(); j++) {
				if (circles.get(i).equals(uniqcircles.get(j))) {
					uniq = false;
					break;
				}
			}
			if (uniq) {
				uniqcircles.add(circles.get(i));
			}
		}
		return uniqcircles;
	}

	/**
	 * Gets the circles with given radius through 2 points.
	 *
	 * @param p0
	 *
	 * @param p1
	 *
	 * @param r
	 *            radius
	 * @return circles with given radius through 2 points
	 */
	public List<WB_Circle> createCircleThrough2Points(final WB_Coord p0,
			final WB_Coord p1, final double r) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		final WB_Circle C0 = createCircleWithRadius(p0, r);
		final WB_Circle C1 = createCircleWithRadius(p1, r);
		final List<WB_Point> intersection = createIntersectionPoints2D(C0, C1);
		for (int i = 0; i < intersection.size(); i++) {
			result.add(createCircleWithRadius(intersection.get(i), r));
		}
		return result;
	}

	/**
	 * Gets circles with given radius tangent to 2D line through 2D point.
	 *
	 * @param L
	 *            line
	 * @param p
	 *            point
	 * @param r
	 *            radius
	 * @return circles with given radius tangent to line through point
	 */
	public List<WB_Circle> createCircleTangentToLineThroughPoint(
			final WB_Line L, final WB_Coord p, final double r) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>();
		double cPrime = L.c() + L.a() * p.xd() + L.b() * p.yd();
		if (WB_Epsilon.isZero(cPrime)) {
			result.add(createCircleWithRadius(
					createPoint2D(p.xd() + r * L.a(), p.yd() + r * L.b()), r));
			result.add(createCircleWithRadius(
					createPoint2D(p.xd() - r * L.a(), p.yd() - r * L.b()), r));
			return result;
		}
		double a, b;
		if (cPrime < 0) {
			a = -L.a();
			b = -L.b();
			cPrime *= -1;
		} else {
			a = L.a();
			b = L.b();
		}
		final double tmp1 = cPrime - r;
		double tmp2 = r * r - tmp1 * tmp1;
		if (WB_Epsilon.isZero(tmp2)) {
			result.add(createCircleWithRadius(
					createPoint2D(p.xd() - tmp1 * a, p.yd() - tmp1 * b), r));
			return result;
		} else if (tmp2 < 0) {
			return result;
		} else {
			tmp2 = Math.sqrt(tmp2);
			final WB_Point tmpp = createPoint2D(p.xd() - a * tmp1,
					p.yd() - b * tmp1);
			result.add(createCircleWithRadius(
					createPoint2D(tmpp.xd() + tmp2 * b, tmpp.yd() - tmp2 * a),
					r));
			result.add(createCircleWithRadius(
					createPoint2D(tmpp.xd() - tmp2 * b, tmpp.yd() + tmp2 * a),
					r));
			return result;
		}
	}

	/**
	 * Gets circles with given radius tangent to 2 2D lines.
	 *
	 * @param L0
	 *            line
	 * @param L1
	 *            line
	 * @param r
	 *            radius
	 * @return circles with radius tangent to 2 2D lines
	 */
	public List<WB_Circle> createCircleTangentTo2Lines(final WB_Line L0,
			final WB_Line L1, final double r) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>(4);
		final double discrm0 = r;
		final double discrm1 = r;
		final double invDenom = 1.0 / (-L1.a() * L0.b() + L0.a() * L1.b());
		double cx = -(L1.b() * (L0.c() + discrm0) - L0.b() * (L1.c() + discrm1))
				* invDenom;
		double cy = +(L1.a() * (L0.c() + discrm0) - L0.a() * (L1.c() + discrm1))
				* invDenom;
		result.add(createCircleWithRadius(createPoint2D(cx, cy), r));
		cx = -(L1.b() * (L0.c() + discrm0) - L0.b() * (L1.c() - discrm1))
				* invDenom;
		cy = +(L1.a() * (L0.c() + discrm0) - L0.a() * (L1.c() - discrm1))
				* invDenom;
		result.add(createCircleWithRadius(createPoint2D(cx, cy), r));
		cx = -(L1.b() * (L0.c() - discrm0) - L0.b() * (L1.c() + discrm1))
				* invDenom;
		cy = +(L1.a() * (L0.c() - discrm0) - L0.a() * (L1.c() + discrm1))
				* invDenom;
		result.add(createCircleWithRadius(createPoint2D(cx, cy), r));
		cx = -(L1.b() * (L0.c() - discrm0) - L0.b() * (L1.c() - discrm1))
				* invDenom;
		cy = +(L1.a() * (L0.c() - discrm0) - L0.a() * (L1.c() - discrm1))
				* invDenom;
		result.add(createCircleWithRadius(createPoint2D(cx, cy), r));
		return result;
	}

	/**
	 * Gets circles with given radius through 2D point and tangent to circle.
	 *
	 * @param C
	 *            circle
	 * @param p
	 *            point
	 * @param r
	 *            radius
	 *
	 * @return circles with given radius through point and tangent to circle
	 */
	public List<WB_Circle> createCircleTangentToCircleThroughPoint(
			final WB_Circle C, final WB_Coord p, final double r) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>(4);
		final double dcp = createPoint2D(p).getDistance2D(C.getCenter());
		if (dcp > C.getRadius() + 2 * r) {
			return result;
		} else if (dcp < C.getRadius() - 2 * r) {
			return result;
		} else {
			final WB_Circle ctmp1 = createCircleWithRadius(p, r);
			WB_Circle ctmp2 = createCircleWithRadius(C.getCenter(),
					r + C.getRadius());
			List<WB_Point> intersection = createIntersectionPoints2D(ctmp1,
					ctmp2);
			for (int i = 0; i < intersection.size(); i++) {
				result.add(createCircleWithRadius(intersection.get(i), r));
			}
			ctmp2 = createCircleWithRadius(C.getCenter(),
					WB_Math.fastAbs(r - C.getRadius()));
			intersection = createIntersectionPoints2D(ctmp1, ctmp2);
			for (int i = 0; i < intersection.size(); i++) {
				result.add(createCircleWithRadius(intersection.get(i), r));
			}
		}
		return result;
	}

	/**
	 * Gets the circle tangent to line and circle.
	 *
	 * @param L
	 *            the l
	 * @param C
	 *            the c
	 * @param r
	 *            the r
	 * @return the circle tangent to line and circle
	 */
	public List<WB_Circle> createCircleTangentToLineAndCircle(final WB_Line L,
			final WB_Circle C, final double r) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>(8);
		final double d = WB_GeometryOp2D.getDistanceToLine2D(C.getCenter(), L);
		if (d > 2 * r + C.getRadius()) {
			return result;
		}
		final WB_Line L1 = createLineWithDirection2D(
				WB_Vector
						.addMul(L.getOrigin(), r,
								createVector2D(L.getDirection().yd(),
										-L.getDirection().xd())),
				L.getDirection());
		final WB_Line L2 = createLineWithDirection2D(
				WB_Vector
						.addMul(L.getOrigin(), r,
								createVector2D(-L.getDirection().yd(),
										+L.getDirection().xd())),
				L.getDirection());
		final WB_Circle C1 = createCircleWithRadius(C.getCenter(),
				C.getRadius() + r);
		final WB_Circle C2 = createCircleWithRadius(C.getCenter(),
				WB_Math.fastAbs(C.getRadius() - r));
		final List<WB_Coord> intersections = new ArrayList<WB_Coord>();
		intersections.addAll(createIntersectionPoints2D(L1, C1));
		intersections.addAll(createIntersectionPoints2D(L1, C2));
		intersections.addAll(createIntersectionPoints2D(L2, C1));
		intersections.addAll(createIntersectionPoints2D(L2, C2));
		for (int i = 0; i < intersections.size(); i++) {
			result.add(createCircleWithRadius(intersections.get(i), r));
		}
		return result;
	}

	/**
	 * Gets circles with given radius tangent to two circles. This will return
	 * all tangent circles with a certaun radius whose center are non-collinear
	 * with the the two centers.
	 *
	 * @param C0
	 *
	 * @param C1
	 *
	 * @param r
	 *            radius
	 * @return non-collinear circles with given radius tangent to two circles
	 */
	public List<WB_Circle> createCircleTangentTo2CirclesNonCollinear(
			final WB_Circle C0, final WB_Circle C1, final double r) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>(2);
		final double d = WB_CoordOp2D.getDistance2D(C0.getCenter(),
				C1.getCenter());
		if (WB_Epsilon.isZero(d)) {
			return result;
		}
		final WB_Circle C0r = createCircleWithRadius(C0.getCenter(),
				C0.getRadius() + r);
		final WB_Circle C1r = createCircleWithRadius(C1.getCenter(),
				C1.getRadius() + r);
		final List<WB_Point> intersections = createIntersectionPoints2D(C0r,
				C1r);
		if (intersections.size() < 2) {
			return result;
		}
		for (int i = 0; i < intersections.size(); i++) {
			result.add(createCircleWithRadius(intersections.get(i), r));
		}
		final Iterator<WB_Circle> itr = result.iterator();
		WB_Circle C;
		while (itr.hasNext()) {
			C = itr.next();
			if (C.equals(C0) || C.equals(C1)) {
				itr.remove();
			}
		}
		return result;
	}

	/**
	 * Gets circles tangent to two circles. This will return all tangent circles
	 * whose center are collinear with the the two centers.
	 *
	 * @param C0
	 *
	 * @param C1
	 *
	 * @return collinear circles tangent to two circles
	 */
	public List<WB_Circle> createCircleTangentTo2CirclesCollinear(
			final WB_Circle C0, final WB_Circle C1) {
		final List<WB_Circle> result = new ArrayList<WB_Circle>(2);
		final double d = WB_CoordOp2D.getDistance2D(C0.getCenter(),
				C1.getCenter());
		if (WB_Epsilon.isZero(d)) {
			return result;
		}
		double r = 0.5 * (d + C0.getRadius() + C1.getRadius());
		double f = (r - C0.getRadius())
				/ (2 * r - C0.getRadius() - C1.getRadius());
		if (!WB_Epsilon.isZero(r)) {
			result.add(createCircleWithRadius(createInterpolatedPoint2D(
					C0.getCenter(), C1.getCenter(), f), WB_Math.fastAbs(r)));
		}
		r = 0.5 * (d + C0.getRadius() - C1.getRadius());
		f = (r - C0.getRadius()) / d;
		if (!WB_Epsilon.isZero(r)) {
			result.add(createCircleWithRadius(createInterpolatedPoint2D(
					C0.getCenter(), C1.getCenter(), f), WB_Math.fastAbs(r)));
		}
		r = 0.5 * (d + C1.getRadius() - C0.getRadius());
		f = (r - C1.getRadius()) / d;
		if (!WB_Epsilon.isZero(r)) {
			result.add(createCircleWithRadius(createInterpolatedPoint2D(
					C1.getCenter(), C0.getCenter(), f), WB_Math.fastAbs(r)));
		}
		r = 0.5 * (d - C1.getRadius() - C0.getRadius());
		f = (r + C0.getRadius()) / d;
		if (!WB_Epsilon.isZero(r)) {
			result.add(createCircleWithRadius(createInterpolatedPoint2D(
					C0.getCenter(), C1.getCenter(), f), WB_Math.fastAbs(r)));
		}
		final Iterator<WB_Circle> itr = result.iterator();
		WB_Circle C;
		while (itr.hasNext()) {
			C = itr.next();
			if (C.equals(C0) || C.equals(C1)) {
				itr.remove();
			}
		}
		return result;
	}

	/**
	 * Return all circles tangential to two given circles. This function returns
	 * all circles with a collinear center regardless of radius and the circles
	 * with a non-collinear center with the given radius.
	 *
	 * @param C0
	 * @param C1
	 * @param r
	 * @return
	 */
	public List<WB_Circle> createCircleTangentTo2Circles(final WB_Circle C0,
			final WB_Circle C1, final double r) {
		final List<WB_Circle> result = createCircleTangentTo2CirclesNonCollinear(
				C0, C1, r);
		result.addAll(createCircleTangentTo2CirclesCollinear(C0, C1));
		return result;
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
	public WB_Point createClosestPointOnTriangle2D(final WB_Coord p,
			final WB_Coord a, final WB_Coord b, final WB_Coord c) {
		final WB_Vector ab = createVectorFromTo2D(a, b);
		final WB_Vector ac = createVectorFromTo2D(a, c);
		final WB_Vector ap = createVectorFromTo2D(a, b);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return createPoint2D(a);
		}
		final WB_Vector bp = createVectorFromTo2D(b, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return createPoint2D(b);
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return createPoint2D(a).addSelf(ab.mul(v));
		}
		final WB_Vector cp = createVectorFromTo2D(c, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return createPoint2D(c);
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return createPoint2D(a).addSelf(ac.mul(w));
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return createPoint2D(b).addSelf(createVectorFromTo2D(c, b).mul(w));
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return createPoint2D(a).addSelf(ab.mul(v)).addSelf(ac.mul(w));
	}

	/**
	 *
	 *
	 * @param p
	 * @param poly
	 * @return
	 */
	public WB_Point createClosestPointOnPolygon2D(final WB_Coord p,
			final WB_Polygon poly) {
		final int[] triangles = poly.getTriangles();
		final int n = triangles.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = null;
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = createClosestPointOnTriangle2D(p, poly.getPoint(triangles[i]),
					poly.getPoint(triangles[i + 1]),
					poly.getPoint(triangles[i + 2]));
			final double d2 = tmp.getSqDistance2D(p);
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

	public List<WB_Coord> createUniquePoints2D(final List<WB_Coord> points,
			final double threshold) {
		final List<WB_Coord> uniqueVertices = new FastList<WB_Coord>();
		final WB_KDTreeInteger2D<WB_Coord> kdtree = new WB_KDTreeInteger2D<WB_Coord>();
		WB_KDTreeInteger2D.WB_KDEntryInteger<WB_Coord> neighbor;
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
}
