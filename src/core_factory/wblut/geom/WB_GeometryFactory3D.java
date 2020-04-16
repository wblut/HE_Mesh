package wblut.geom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3d;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.twak.camp.Corner;
import org.twak.camp.Edge;
import org.twak.camp.Machine;
import org.twak.camp.Output.Face;
import org.twak.camp.Skeleton;
import org.twak.utils.collections.Loop;
import org.twak.utils.collections.LoopL;

import wblut.data.WB_JohnsonPolyhedraData01;
import wblut.data.WB_JohnsonPolyhedraData02;
import wblut.data.WB_JohnsonPolyhedraData03;
import wblut.data.WB_JohnsonPolyhedraData04;
import wblut.data.WB_PolyhedraData;
import wblut.external.QuickHull3D.WB_QuickHull3D;

import wblut.math.WB_Epsilon;

public class WB_GeometryFactory3D extends WB_GeometryFactory2D {
	final WB_Point origin;
	final WB_Vector X;
	final WB_Vector Y;
	final WB_Vector Z;
	final WB_Vector mX;
	final WB_Vector mY;
	final WB_Vector mZ;
	WB_Plane XY;
	WB_Plane YZ;
	WB_Plane ZX;
	WB_Plane YX;
	WB_Plane ZY;
	WB_Plane XZ;

	public WB_GeometryFactory3D() {
		super();
		origin = createPoint(0, 0, 0);
		X = createVector(1, 0, 0);
		Y = createVector(0, 1, 0);
		Z = createVector(0, 0, 1);
		mX = createVector(-1, 0, 0);
		mY = createVector(0, -1, 0);
		mZ = createVector(0, 0, -1);
	}

	public static WB_GeometryFactory3D instance() {
		return new WB_GeometryFactory3D();
	}

	public WB_Point origin() {
		return origin;
	}

	public WB_Vector X() {
		return X;
	}

	public WB_Vector Y() {
		return Y;
	}

	public WB_Vector Z() {
		return Z;
	}

	public WB_Vector minX() {
		return mX;
	}

	public WB_Vector minY() {
		return mY;
	}

	public WB_Vector minZ() {
		return mZ;
	}

	public WB_Plane XY() {
		if (XY == null) {
			XY = createPlane(origin(), Z());
		}
		return XY;
	}

	public WB_Plane YZ() {
		if (YZ == null) {
			YZ = createPlane(origin(), X());
		}
		return YZ;
	}

	public WB_Plane ZX() {
		if (ZX == null) {
			ZX = createPlane(origin(), Y());
		}
		return ZX;
	}

	public WB_Plane YX() {
		if (YX == null) {
			YX = createPlane(origin(), minZ());
		}
		return XY;
	}

	public WB_Plane ZY() {
		if (ZY == null) {
			ZY = createPlane(origin(), minX());
		}
		return ZY;
	}

	public WB_Plane XZ() {
		if (XZ == null) {
			XZ = createPlane(origin(), minY());
		}
		return XZ;
	}

	public WB_CoordinateSystem createCSFromOriginAndX(final WB_Coord origin, final WB_Coord X) {
		final WB_Point lOrigin = createPoint(origin.xd(), origin.yd(), 0);
		final WB_Vector lX = createNormalizedVector(X.xd(), X.yd(), 0);
		final WB_Vector lY = createVector(-lX.yd(), lX.xd());
		return createCSFromOriginAndXY(lOrigin, lX, lY);
	}

	public WB_CoordinateSystem createCSFromOriginAndX(final WB_Coord origin, final WB_Coord X,
			final WB_CoordinateSystem parent) {
		final WB_Point lOrigin = createPoint(origin.xd(), origin.yd());
		final WB_Vector lX = createNormalizedVector(X.xd(), X.yd(), 0);
		final WB_Vector lY = createVector(-lX.yd(), lX.xd());
		return createCSFromOriginAndXY(lOrigin, lX, lY, parent);
	}

	public WB_CoordinateSystem createCSFromOriginAndXY(final WB_Coord origin, final WB_Coord X, final WB_Coord Y) {
		final WB_Vector lX = createNormalizedVector(X);
		WB_Vector lY = createNormalizedVector(Y);
		final WB_Vector lZ = lX.cross(lY);
		if (WB_Epsilon.isZeroSq(lZ.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		lZ.normalizeSelf();
		lY = createNormalizedVector(lZ.cross(lX));
		return new WB_CoordinateSystem(origin, lX, lY, lZ, WORLD());
	}

	public WB_CoordinateSystem createCSFromOriginAndXY(final WB_Coord origin, final WB_Coord X, final WB_Coord Y,
			final WB_CoordinateSystem parent) {
		final WB_Vector lX = createNormalizedVector(X);
		WB_Vector lY = createNormalizedVector(Y);
		final WB_Vector lZ = lX.cross(lY);
		if (WB_Epsilon.isZeroSq(lZ.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		lZ.normalizeSelf();
		lY = createNormalizedVector(lZ.cross(lX));
		return new WB_CoordinateSystem(origin, lX, lY, lZ, parent);
	}

	public WB_CoordinateSystem createCSFromOriginAndXYZ(final WB_Coord origin, final WB_Coord X, final WB_Coord Y,
			final WB_Coord Z, final WB_CoordinateSystem parent) {
		return new WB_CoordinateSystem(origin, X, Y, Z, parent);
	}

	public WB_CoordinateSystem createCSFromOriginAndXYZ(final WB_Coord origin, final WB_Coord X, final WB_Coord Y,
			final WB_Coord Z) {
		return new WB_CoordinateSystem(origin, X, Y, Z, WORLD());
	}

	public WB_Map2D createEmbeddedPlane() {
		return new WB_PlanarMap();
	}

	public WB_Map2D createEmbeddedPlane(final int mode, final double offset) {
		return new WB_PlanarMap(mode, offset);
	}

	public WB_Map2D createEmbeddedPlane(final int mode) {
		return new WB_PlanarMap(mode);
	}

	public WB_Map2D createEmbeddedPlane(final WB_Plane P, final double offset) {
		return new WB_PlanarMap(P, offset);
	}

	public WB_Map2D createEmbeddedPlane(final WB_Plane P) {
		return new WB_PlanarMap(P);
	}

	public WB_Point createPoint() {
		return new WB_Point(0, 0, 0);
	}

	public WB_Point createPoint(final WB_Coord p) {
		return new WB_Point(p);
	}

	public WB_Point createPoint(final double[] p) {
		return new WB_Point(p);
	}

	public WB_Point createPoint(final double _x, final double _y) {
		return createPoint(_x, _y, 0);
	}

	public WB_Point createPoint(final double _x, final double _y, final double _z) {
		return new WB_Point(_x, _y, _z);
	}

	public WB_Point createInterpolatedPoint(final WB_Coord p, final WB_Coord q, final double f) {
		return new WB_Point((1.0 - f) * p.xd() + f * q.xd(), (1.0 - f) * p.yd() + f * q.yd(),
				(1.0 - f) * p.zd() + f * q.zd());
	}

	public WB_Point createIncenter(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(1, 1, 1, tri);
	}

	public WB_Point createOrthocenter(final WB_Triangle tri) {
		final double a2 = tri.a() * tri.a();
		final double b2 = tri.b() * tri.b();
		final double c2 = tri.c() * tri.c();
		return createPointFromBarycentricCoordinates((a2 + b2 - c2) * (a2 - b2 + c2), (a2 + b2 - c2) * (-a2 + b2 + c2),
				(a2 - b2 + c2) * (-a2 + b2 + c2), tri);
	}

	public WB_Point createPointFromBarycentricCoordinates(final double u, final double v, final double w,
			final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(u / tri.a(), v / tri.b(), w / tri.c(), tri);
	}

	public WB_Point createPointFromCylindrical(final double r, final double phi, final double z) {
		return createPoint(r * Math.cos(phi), r * Math.sin(phi), z);
	}

	public WB_Point createPointFromSpherical(final double r, final double theta, final double phi) {
		return createPoint(r * Math.cos(phi) * Math.sin(theta), r * Math.sin(phi) * Math.sin(theta),
				r * Math.cos(theta));
	}

	public WB_Point createPointFromParaboloidal(final double sigma, final double tau, final double phi) {
		return createPoint(sigma * tau * Math.cos(phi), sigma * tau * Math.sin(phi), 0.5 * (tau * tau - sigma * sigma));
	}

	public WB_Point createPointFromParabolic(final double sigma, final double tau, final double z) {
		return createPoint(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
	}

	public WB_Point createPointFromOblateSpheroidal(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createPoint(common * Math.cos(phi), common * Math.sin(phi), a * Math.sinh(mu) * Math.sin(nu));
	}

	public WB_Point createPointFromProlateSpheroidal(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createPoint(common * Math.cos(phi), common * Math.sin(phi), a * Math.cosh(mu) * Math.cos(nu));
	}

	public WB_Point createPointFromEllipsoidal(final double a, final double b, final double c, final double lambda,
			final double mu, final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createPoint(Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2) / (a2 - c2)),
				Math.sqrt((b2 - lambda) * (b2 - mu) * (b2 - nu) / (b2 - a2) / (b2 - c2)),
				Math.sqrt((c2 - lambda) * (c2 - mu) * (c2 - nu) / (c2 - a2) / (c2 - b2)));
	}

	public WB_Point createPointFromElliptic(final double a, final double mu, final double nu, final double z) {
		return createPoint(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu), z);
	}

	public WB_Point createPointFromToroidal(final double a, final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint(Math.sinh(tau) * invdenom * Math.cos(phi), Math.sinh(tau) * invdenom * Math.sin(phi),
				Math.sin(sigma) * invdenom);
	}

	public WB_Point createPointFromBispherical(final double a, final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint(Math.sin(sigma) * invdenom * Math.cos(phi), Math.sin(sigma) * invdenom * Math.sin(phi),
				Math.sinh(tau) * invdenom);
	}

	public WB_Point createPointFromBipolarCylindrical(final double a, final double sigma, final double tau,
			final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createPoint(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom, z);
	}

	public WB_Point createPointFromConical(final double b, final double c, final double r, final double mu,
			final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createPoint(r * mu * nu / b / c, r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}

	public WB_Point createCentroid(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(tri.b() * tri.c(), tri.c() * tri.a(), tri.a() * tri.b(), tri);
	}

	public WB_Point createCentroid(final WB_Coord... points) {
		final WB_Point c = new WB_Point();
		for (final WB_Coord point : points) {
			c.addSelf(point);
		}
		c.divSelf(points.length);
		return c;
	}

	public WB_Point createCircumcenter(final WB_Triangle tri) {
		return createPointFromTrilinearCoordinates(tri.cosA(), tri.cosB(), tri.cosC(), tri);
	}

	public WB_Point createPointFromTrilinearCoordinates(final double u, final double v, final double w,
			final WB_Triangle tri) {
		final double invabc = 1.0 / (tri.a() * u + tri.b() * v + tri.c() * w);
		final double bv = tri.b() * v;
		final double au = tri.a() * u;
		final double eax = ((tri.p2().xd() - tri.p3().xd()) * bv + (tri.p1().xd() - tri.p3().xd()) * au) * invabc
				+ tri.p3().xd();
		final double eay = ((tri.p2().yd() - tri.p3().yd()) * bv + (tri.p1().yd() - tri.p3().yd()) * au) * invabc
				+ tri.p3().yd();
		final double eaz = ((tri.p2().zd() - tri.p3().zd()) * bv + (tri.p1().zd() - tri.p3().zd()) * au) * invabc
				+ tri.p3().zd();
		return createPoint(eax, eay, eaz);
	}

	public WB_Point createMidpoint(final WB_Coord p, final WB_Coord q) {
		return createPoint((p.xd() + q.xd()) * 0.5, (p.yd() + q.yd()) * 0.5, (p.zd() + q.zd()) * 0.5);
	}

	public WB_Point createMidpoint(final WB_Coord... p) {
		final WB_Point m = createPoint();
		for (final WB_Coord point : p) {
			m.addSelf(point);
		}
		m.divSelf(p.length);
		return m;
	}

	private List<WB_Point> cleanPointlist(final List<WB_Point> points) {
		final List<WB_Point> result = new WB_PointList();
		final int n = points.size();
		for (int i = 0; i < n; i++) {
			if (!points.get(i).equals(points.get((i + 1) % n))) {
				result.add(points.get(i));
			}
		}
		return result;
	}

	public WB_Vector createVector() {
		return createVector(0, 0, 0);
	}

	public WB_Vector createVectorFromTo(final WB_Coord p, final WB_Coord q) {
		return createVector(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
	}

	public final WB_Vector createVector(final WB_Coord p) {
		return new WB_Vector(p);
	}

	public WB_Vector createVector(final double _x, final double _y) {
		return createVector(_x, _y, 0);
	}

	public WB_Vector createVector(final double _x, final double _y, final double _z) {
		return new WB_Vector(_x, _y, _z);
	}

	public WB_Vector createNormalizedVector(final WB_Coord p) {
		final WB_Vector vec = createVector(p);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedVectorFromTo(final WB_Coord p, final WB_Coord q) {
		final WB_Vector vec = createVector(q.xd() - p.xd(), q.yd() - p.yd(), q.zd() - p.zd());
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedVector(final double _x, final double _y, final double _z) {
		final WB_Vector vec = createVector(_x, _y, _z);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedVector(final double _x, final double _y, final double _z, final double _w) {
		final WB_Vector vec = createVector(_x, _y, _z);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedVector(final double _x, final double _y) {
		final WB_Vector vec = createVector(_x, _y);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedPerpendicularVector(final double _x, final double _y) {
		final WB_Vector vec = createVector(-_y, _x, 0);
		vec.normalizeSelf();
		return vec;
	}

	public WB_Vector createNormalizedPerpendicularVector(final double _x, final double _y, final double _z) {
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

	public WB_Vector createVectorFromCylindrical(final double r, final double phi, final double z) {
		return createVector(r * Math.cos(phi), r * Math.sin(phi), z);
	}

	public WB_Vector createVectorFromSpherical(final double r, final double theta, final double phi) {
		return createVector(r * Math.cos(phi) * Math.sin(theta), r * Math.sin(phi) * Math.sin(theta),
				r * Math.cos(theta));
	}

	public WB_Vector createVectorFromParaboloidal(final double sigma, final double tau, final double phi) {
		return createVector(sigma * tau * Math.cos(phi), sigma * tau * Math.sin(phi),
				0.5 * (tau * tau - sigma * sigma));
	}

	public WB_Vector createVectorFromParabolic(final double sigma, final double tau, final double z) {
		return createVector(sigma * tau, 0.5 * (tau * tau - sigma * sigma), z);
	}

	public WB_Vector createVectorFromOblateSpheroidal(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.cosh(mu) * Math.cos(nu);
		return createVector(common * Math.cos(phi), common * Math.sin(phi), a * Math.sinh(mu) * Math.sin(nu));
	}

	public WB_Vector createVectorFromProlateSpheroidal(final double a, final double mu, final double nu,
			final double phi) {
		final double common = a * Math.sinh(mu) * Math.sin(nu);
		return createVector(common * Math.cos(phi), common * Math.sin(phi), a * Math.cosh(mu) * Math.cos(nu));
	}

	public WB_Vector createVectorFromEllipsoidal(final double a, final double b, final double c, final double lambda,
			final double mu, final double nu) {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return createVector(Math.sqrt((a2 - lambda) * (a2 - mu) * (a2 - nu) / (a2 - b2) / (a2 - c2)),
				Math.sqrt((b2 - lambda) * (b2 - mu) * (b2 - nu) / (b2 - a2) / (b2 - c2)),
				Math.sqrt((c2 - lambda) * (c2 - mu) * (c2 - nu) / (c2 - a2) / (c2 - b2)));
	}

	public WB_Vector createVectorFromElliptic(final double a, final double mu, final double nu, final double z) {
		return createVector(a * Math.cosh(mu) * Math.cos(nu), a * Math.sinh(mu) * Math.cos(nu), z);
	}

	public WB_Vector createVectorFromToroidal(final double a, final double sigma, final double tau, final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector(Math.sinh(tau) * invdenom * Math.cos(phi), Math.sinh(tau) * invdenom * Math.sin(phi),
				Math.sin(sigma) * invdenom);
	}

	public WB_Vector createVectorFromBispherical(final double a, final double sigma, final double tau,
			final double phi) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector(Math.sin(sigma) * invdenom * Math.cos(phi), Math.sin(sigma) * invdenom * Math.sin(phi),
				Math.sinh(tau) * invdenom);
	}

	public WB_Vector createVectorFromBipolarCylindrical(final double a, final double sigma, final double tau,
			final double z) {
		double invdenom = Math.cosh(tau) - Math.cos(sigma);
		invdenom = WB_Epsilon.isZero(invdenom) ? 0.0 : a / invdenom;
		return createVector(Math.sinh(tau) * invdenom, Math.sin(sigma) * invdenom, z);
	}

	public WB_Vector createVectorFromConical(final double b, final double c, final double r, final double mu,
			final double nu) {
		final double b2 = b * b;
		final double c2 = c * c;
		final double mu2 = mu * mu;
		final double nu2 = nu * nu;
		return createVector(r * mu * nu / b / c, r / b * Math.sqrt((mu2 - b2) * (nu2 - b2) / (b2 - c2)),
				r / c * Math.sqrt((mu2 - c2) * (nu2 - c2) / (c2 - b2)));
	}

	public WB_Line createLineThroughPoints(final WB_Coord p1, final WB_Coord p2) {
		return createLineWithDirection(p1, createVectorFromTo(p1, p2));
	}

	public WB_Line createLineThroughPoints(final double x1, final double y1, final double x2, final double y2) {
		return createLineWithDirection(createPoint(x1, y1), createVector(x2 - x1, y2 - y1));
	}

	// 3D
	public WB_Line createLineThroughPoints(final double x1, final double y1, final double z1, final double x2,
			final double y2, final double z2) {
		return createLineWithDirection(createPoint(x1, y1, z1), createVector(x2 - x1, y2 - y1, z2 - z1));
	}

	public WB_Line createLineWithDirection(final WB_Coord origin, final WB_Coord direction) {
		return new WB_Line(origin, direction);
	}

	public WB_Line createLineWithDirection(final double ox, final double oy, final double oz, final double dx,
			final double dy, final double dz) {
		return createLineWithDirection(createPoint(ox, oy, oz), createVector(dx, dy, dz));
	}

	public WB_Line createParallelLineThroughPoint(final WB_Line L, final WB_Coord p) {
		return createLineWithDirection(p, L.getDirection());
	}

	public WB_Plane createBisector(final WB_Coord p, final WB_Coord q) {
		return createPlane(createMidpoint(p, q), createVectorFromTo(p, q));
	}

	public WB_Ray createRayThroughPoints(final WB_Coord p1, final WB_Coord p2) {
		return createRayWithDirection(p1, createVector(p2).subSelf(p1));
	}

	public WB_Ray createRayThroughPoints(final double x1, final double y1, final double x2, final double y2) {
		return createRayWithDirection(createPoint(x1, y1), createVector(x2 - x1, y2 - y1));
	}

	// 3D
	public WB_Ray createRayThroughPoints(final double x1, final double y1, final double z1, final double x2,
			final double y2, final double z2) {
		return createRayWithDirection(createPoint(x1, y1, z1), createVector(x2 - x1, y2 - y1, z2 - z1));
	}

	public WB_Ray createRayWithDirection(final WB_Coord origin, final WB_Coord direction) {
		return new WB_Ray(origin, direction);
	}

	public WB_Ray createRayWithDirection(final double ox, final double oy, final double oz, final double dx,
			final double dy, final double dz) {
		return createRayWithDirection(createPoint(ox, oy, oz), createVector(dx, dy, dz));
	}

	public WB_Ray createParallelRayThroughPoint(final WB_Line L, final WB_Coord p) {
		return createRayWithDirection(p, L.getDirection());
	}

	public WB_Segment createSegment(final WB_Coord p1, final WB_Coord p2) {
		return new WB_Segment(p1, p2);
	}

	public WB_Segment createSegmentWithLength(final WB_Coord origin, final WB_Coord direction, final double length) {
		return createSegment(origin, createPoint(origin).addMulSelf(length, createNormalizedVector(direction)));
	}

	public WB_Segment createSegment(final double x1, final double y1, final double x2, final double y2) {
		return createSegment(createPoint(x1, y1), createVector(x2, y2));
	}

	public WB_Segment createSegmentWithLength(final double ox, final double oy, final double dx, final double dy,
			final double length) {
		return createSegment(createPoint(ox, oy),
				createPoint(ox, oy).addMul(length, createNormalizedVector(dx, dy, 0)));
	}

	// 3D
	public WB_Segment createSegment(final double x1, final double y1, final double z1, final double x2, final double y2,
			final double z2) {
		return createSegment(createPoint(x1, y1, z1), createVector(x2, y2, z2));
	}

	public WB_Segment createSegmentWithLength(final double ox, final double oy, final double oz, final double dx,
			final double dy, final double dz, final double length) {
		return createSegment(createPoint(ox, oy, oz),
				createPoint(ox, oy, oz).addMul(length, createNormalizedVector(dx, dy, dz)));
	}

	public WB_Polygon[] splitSimplePolygon(final WB_Polygon poly, final WB_Plane P) {
		List<WB_Point> frontVerts = new WB_PointList();
		List<WB_Point> backVerts = new WB_PointList();
		final int numVerts = poly.getNumberOfPoints();
		final WB_Polygon[] polys = new WB_Polygon[2];
		if (numVerts > 0) {
			WB_Point a = new WB_Point(poly.getPoint(numVerts - 1));
			WB_Classification aSide = WB_GeometryOp3D.classifyPointToPlane3D(a, P);
			WB_Point b;
			WB_Classification bSide;
			for (int n = 0; n < numVerts; n++) {
				WB_Point intersection;
				b = new WB_Point(poly.getPoint(n));
				bSide = WB_GeometryOp3D.classifyPointToPlane3D(b, P);
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

	public WB_Triangle createTriangle(final double p1x, final double p1y, final double p1z, final double p2x,
			final double p2y, final double p2z, final double p3x, final double p3y, final double p3z) {
		return createTriangle(createPoint(p1x, p1y, p1z), createPoint(p2x, p2y, p2z), createPoint(p3x, p3y, p3z));
	}

	public WB_Triangle createTriangle(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		return new WB_Triangle(p1, p2, p3);
	}

	public WB_Point createInversionPoint(final WB_Coord p, final WB_Sphere inversionSphere) {
		final double r2 = inversionSphere.getRadius() * inversionSphere.getRadius();
		final double OP = WB_GeometryOp3D.getDistance3D(inversionSphere.getCenter(), p);
		if (WB_Epsilon.isZero(OP)) {
			return null;
		}
		final double OPp = r2 / OP;
		final WB_Vector v = createNormalizedVectorFromTo(inversionSphere.getCenter(), p);
		return createPoint(WB_Point.addMul(inversionSphere.getCenter(), OPp, v));
	}

	public WB_Circle createInversionCircle(final WB_Circle C, final WB_Circle inversionCircle) {
		if (WB_GeometryOp2D.classifyPointToCircle2D(inversionCircle.getCenter(), C) == WB_Classification.ON) {
			return null;
		}
		final double x0 = inversionCircle.getCenter().xd();
		final double y0 = inversionCircle.getCenter().yd();
		final double k = inversionCircle.getRadius();
		final double k2 = k * k;
		final double s = k2 / (WB_GeometryOp3D.getSqDistance3D(C.getCenter(), inversionCircle.getCenter())
				- C.getRadius() * C.getRadius());
		return createCircleWithRadius(x0 + s * (C.getCenter().xd() - x0), y0 + s * (C.getCenter().yd() - y0),
				Math.abs(s) * C.getRadius());
	}

	public WB_Circle createCircumcircle3D(final WB_Triangle tri) {
		final WB_Plane P = tri.getPlane();
		if (P == null) {
			return createCircleWithRadius(createCentroid(tri), new WB_Vector(0, 0, 1), 0.0);
		}
		final double a = tri.a();
		final double b = tri.b();
		final double c = tri.c();
		final double radius = a * b * c / Math.sqrt(2 * a * a * b * b + 2 * b * b * c * c + 2 * a * a * c * c
				- a * a * a * a - b * b * b * b - c * c * c * c);
		return createCircleWithRadius(createCircumcenter(tri), tri.getPlane().getNormal(), radius);
	}

	public WB_Circle createIncircle(final WB_Triangle tri) {
		final WB_Plane P = tri.getPlane();
		if (P == null) {
			return createCircleWithRadius(createCentroid(tri), new WB_Vector(0, 0, 1), 0.0);
		}
		final double a = tri.a();
		final double b = tri.b();
		final double c = tri.c();
		final double invabc = 1.0 / (a + b + c);
		final double radius = 0.5 * Math.sqrt((b + c - a) * (c + a - b) * (a + b - c) * invabc);
		final double x = (tri.p1().xd() * a + tri.p2().xd() * b + tri.p3().xd() * c) * invabc;
		final double y = (tri.p1().yd() * a + tri.p2().yd() * b + tri.p3().yd() * c) * invabc;
		final double z = (tri.p1().zd() * a + tri.p2().zd() * b + tri.p3().zd() * c) * invabc;
		return createCircleWithRadius(createPoint(x, y, z), tri.getPlane().getNormal(), radius);
	}

	public WB_Plane createPlane(final WB_Coord origin, final WB_Coord normal) {
		return new WB_Plane(origin, normal);
	}

	public WB_Plane createPlane(final double ox, final double oy, final double oz, final double nx, final double ny,
			final double nz) {
		return new WB_Plane(createPoint(ox, oy, oz), createVector(nx, ny, nz));
	}

	public WB_Plane createPlane(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		final WB_Vector v21 = createVectorFromTo(p1, p2);
		final WB_Vector v31 = createVectorFromTo(p1, p3);
		return new WB_Plane(p1, v21.crossSelf(v31));
	}

	public WB_Plane createPlane(final WB_Triangle T) {
		return new WB_Plane(T.p1(), T.p2(), T.p3());
	}

	public WB_Plane createFlippedPlane(final WB_Plane P) {
		return new WB_Plane(P.getOrigin(), P.getNormal().mul(-1));
	}

	public WB_Plane createOffsetPlane(final WB_Coord origin, final WB_Coord normal, final double offset) {
		return new WB_Plane(createPoint(origin).addMulSelf(offset, normal), normal);
	}

	public WB_Plane createOffsetPlane(final double ox, final double oy, final double oz, final double nx,
			final double ny, final double nz, final double offset) {
		return new WB_Plane(createPoint(ox + offset * nx, oy + offset * ny, oz + offset * nz),
				createVector(nx, ny, nz));
	}

	public WB_Plane createOffsetPlane(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final double offset) {
		final WB_Vector v21 = createVectorFromTo(p1, p2);
		final WB_Vector v31 = createVectorFromTo(p1, p3);
		final WB_Vector n = v21.crossSelf(v31);
		n.normalizeSelf();
		return new WB_Plane(createPoint(p1).addMulSelf(offset, n), n);
	}

	public WB_SimpleMesh createMesh(final WB_Coord[] points, final int[][] faces) {
		return new WB_SimpleMesh(points, faces);
	}

	public WB_SimpleMesh createMesh(final Collection<? extends WB_Coord> points, final int[][] faces) {
		return new WB_SimpleMesh(points, faces);
	}

	public WB_SimpleMesh createMesh(final WB_AABB aabb) {
		return createMesh(aabb.getCorners(), aabb.getFaces());
	}

	public WB_SimpleMesh createUniqueMesh(final WB_SimpleMesh mesh) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final int[] oldnew = new int[mesh.getNumberOfVertices()];
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = mesh.getVertex(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		oldnew[0] = 0;
		int nuv = 1;
		for (int i = 1; i < mesh.getNumberOfVertices(); i++) {
			v = mesh.getVertex(i);
			neighbor = kdtree.getNearestNeighbor(v);
			if (neighbor.d2 < WB_Epsilon.SQEPSILON) {
				oldnew[i] = neighbor.value;
			} else {
				kdtree.add(v, nuv);
				uniqueVertices.add(v);
				oldnew[i] = nuv++;
			}
		}
		final int[][] newfaces = new int[mesh.getNumberOfFaces()][];
		for (int i = 0; i < mesh.getNumberOfFaces(); i++) {
			final int[] face = mesh.getFace(i);
			newfaces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				newfaces[i][j] = oldnew[face[j]];
			}
		}
		return createMesh(uniqueVertices, newfaces);
	}

	public WB_SimpleMesh createUniqueMesh(final WB_SimpleMesh mesh, final double threshold) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final int[] oldnew = new int[mesh.getNumberOfVertices()];
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
		final double t2 = threshold * threshold;
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = mesh.getVertex(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		oldnew[0] = 0;
		int nuv = 1;
		for (int i = 1; i < mesh.getNumberOfVertices(); i++) {
			v = mesh.getVertex(i);
			neighbor = kdtree.getNearestNeighbor(v);
			if (neighbor.d2 < t2) {
				oldnew[i] = neighbor.value;
			} else {
				kdtree.add(v, nuv);
				uniqueVertices.add(v);
				oldnew[i] = nuv++;
			}
		}
		final int[][] newfaces = new int[mesh.getNumberOfFaces()][];
		for (int i = 0; i < mesh.getNumberOfFaces(); i++) {
			final int[] face = mesh.getFace(i);
			newfaces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				newfaces[i][j] = oldnew[face[j]];
			}
		}
		return createMesh(uniqueVertices, newfaces);
	}

	public WB_SimpleMesh createRegularPrism(final int n, final double radius, final double h) {
		final List<WB_Point> lpoints = new WB_PointList();
		for (int i = 0; i < n; i++) {
			lpoints.add(
					createPoint(radius * Math.cos(Math.PI * 2.0 / n * i), radius * Math.sin(Math.PI * 2.0 / n * i), 0));
			lpoints.add(
					createPoint(radius * Math.cos(Math.PI * 2.0 / n * i), radius * Math.sin(Math.PI * 2.0 / n * i), h));
		}
		return createMesh(lpoints, createPrismFaces(n));
	}

	public WB_SimpleMesh createPrism(final Collection<? extends WB_Coord> points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createPrismFaces(points.size()));
	}

	public WB_SimpleMesh createPrismOpen(final Collection<? extends WB_Coord> points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createPrismFacesOpen(points.size()));
	}

	public WB_SimpleMesh createPrism(final WB_Coord[] points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createPrismFaces(points.length));
	}

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

	public WB_SimpleMesh createPrism(final WB_Polygon poly, final double h, final double offset) {
		if (h == 0) {
			return createMesh(poly, offset);
		}
		final WB_Vector N = poly.getPlane().getNormal();
		final WB_Vector offset1 = N.mul(offset);
		final WB_Vector offset2 = N.mul(offset + h);
		final List<WB_Point> lpoints = new WB_PointList();
		WB_Coord point;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			point = poly.getPoint(i);
			lpoints.add(createPoint(point).addSelf(offset1));
			lpoints.add(createPoint(point).addSelf(offset2));
		}
		final int numfaces = poly.getNumberOfPoints();
		final int[] triangles = poly.getTriangles();
		final int[][] prismfaces = new int[2 * triangles.length / 3 + numfaces][];
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
		final WB_Vector N = poly.getPlane().getNormal();
		final WB_Vector offset1 = N.mul(offset);
		final List<WB_Point> lpoints = new WB_PointList();
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

	public WB_SimpleMesh createPrismOpen(final WB_Polygon poly, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
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

	public WB_SimpleMesh createRegularAntiPrism(final int n, final double radius, final double h) {
		final List<WB_Point> points = new WB_PointList();
		for (int i = 0; i < n; i++) {
			points.add(
					createPoint(radius * Math.cos(Math.PI * 2.0 / n * i), radius * Math.sin(Math.PI * 2.0 / n * i), 0));
			points.add(createPoint(radius * Math.cos(Math.PI * 2.0 / n * (i + 0.5)),
					radius * Math.sin(Math.PI * 2.0 / n * (i + 0.5)), h));
		}
		return createMesh(points, createAntiprismFaces(n));
	}

	public WB_SimpleMesh createAntiPrism(final Collection<? extends WB_Coord> points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createAntiprismFaces(points.size()));
	}

	public WB_SimpleMesh createAntiPrism(final WB_Coord[] points, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
		for (final WB_Coord point : points) {
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		return createMesh(lpoints, createAntiprismFaces(points.length));
	}

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

	public WB_SimpleMesh createAntiPrism(final WB_Polygon poly, final double h) {
		final WB_Vector offset = createVector(0, 0, h);
		final List<WB_Point> lpoints = new WB_PointList();
		WB_Coord point;
		for (int i = 0; i < poly.getNumberOfPoints(); i++) {
			point = poly.getPoint(i);
			lpoints.add(createPoint(point));
			lpoints.add(createPoint(point).addSelf(offset));
		}
		final int numfaces = poly.getNumberOfPoints();
		final int[] triangles = poly.getTriangles();
		final int[][] prismfaces = new int[2 * triangles.length / 3 + 2 * numfaces][];
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

	private List<WB_Point> createVerticesFromArray(final double[][] vertices) {
		final List<WB_Point> points = new WB_PointList();
		for (final double[] vertice : vertices) {
			points.add(createPoint(vertice[0], vertice[1], vertice[2]));
		}
		return points;
	}

	public WB_SimpleMesh createJohnson(final int type, final double edgeLength) {
		final List<WB_Point> vertices;
		final int[][] faces;
		if (type < 23) {
			vertices = createVerticesFromArray(WB_JohnsonPolyhedraData01.vertices[type]);
			faces = WB_JohnsonPolyhedraData01.faces[type];
		} else if (type < 46) {
			vertices = createVerticesFromArray(WB_JohnsonPolyhedraData02.vertices[type - 23]);
			faces = WB_JohnsonPolyhedraData02.faces[type - 23];
		} else if (type < 70) {
			vertices = createVerticesFromArray(WB_JohnsonPolyhedraData03.vertices[type - 46]);
			faces = WB_JohnsonPolyhedraData03.faces[type - 46];
		} else {
			vertices = createVerticesFromArray(WB_JohnsonPolyhedraData04.vertices[type - 70]);
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

	public WB_SimpleMesh createOtherPolyhedron(final int type, final double edgeLength) {
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

	public WB_SimpleMesh createPolyhedronFromWRL(String name, final double radius) {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream("resources/" + name + ".wrl")));
		final List<WB_Point> points = new WB_PointList();
		final List<int[]> faces = new FastList<>();
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
						points.add(createPoint(Double.parseDouble(words[0].trim()), Double.parseDouble(words[1].trim()),
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
			d2 = Math.max(d2, p.getSqLength3D());
		}
		d2 = radius / Math.sqrt(d2);
		for (final WB_Point p : points) {
			p.mulSelf(d2);
		}
		return createMesh(points, ifaces);
	}

	public WB_SimpleMesh createZonohedron(final WB_Coord[] vectors, final double scale) {
		final int n = vectors.length;
		if (n < 3) {
			return null;
		}
		final int nop = (int) Math.pow(2, n);
		final List<WB_Point> points = new WB_PointList();
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

	public WB_SimpleMesh createStellatedIcosahedron(final int type, final double radius) {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream("resources/stellated_icosahedron1-59.txt")));
		final List<WB_Point> points = new ArrayList<>();
		final List<int[]> faces = new ArrayList<>();
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
			while ((thisline = br.readLine()) != null && currentline <= endface) {
				if (currentline >= startpoint && currentline <= endpoint) {
					coordinates = thisline.split(",");
					points.add(createPoint(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]),
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
			d2 = Math.max(d2, p.getSqLength3D());
		}
		d2 = radius / Math.sqrt(d2);
		for (final WB_Point p : points) {
			p.mulSelf(d2);
		}
		return createMesh(points, ifaces);
	}

	public WB_SimpleMesh createConvexHull(final List<? extends WB_Coord> points) {
		return createConvexHull(points, true);
	}

	public WB_SimpleMesh createConvexHull(final WB_Coord[] points) {
		return createConvexHull(points, true);
	}

	public WB_SimpleMesh createConvexHull(final WB_Coord[] points, final boolean triangulate) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
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
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices, triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	public WB_SimpleMesh createConvexHull(final List<? extends WB_Coord> points, final boolean triangulate) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
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
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices, triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	public WB_SimpleMesh createConvexHullWithThreshold(final WB_Coord[] points, final boolean triangulate,
			final double threshold) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
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
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices, triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	public WB_SimpleMesh createConvexHullWithThreshold(final List<? extends WB_Coord> points, final boolean triangulate,
			final double threshold) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
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
			final WB_QuickHull3D hull = new WB_QuickHull3D(uniqueVertices, triangulate);
			final int[][] faces = hull.getFaces();
			final List<WB_Coord> hullpoints = hull.getVertices();
			return createMesh(hullpoints, faces);
		} catch (final Exception e) {
			return null;
		}
	}

	public WB_Point createClosestPointOnTriangle(final WB_Coord p, final WB_Coord a, final WB_Coord b,
			final WB_Coord c) {
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

	public WB_Point createClosestPointOnPolygon(final WB_Coord p, final WB_Polygon poly) {
		final int[] triangles = poly.getTriangles();
		final int n = triangles.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = null;
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = createClosestPointOnTriangle(p, poly.getPoint(triangles[i]), poly.getPoint(triangles[i + 1]),
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

	public WB_Sphere createSphereWithRadius(final WB_Coord center, final double radius) {
		return new WB_Sphere(center, radius);
	}

	public WB_Sphere createSphereWithDiameter(final WB_Coord center, final double diameter) {
		return createSphereWithRadius(center, .5 * diameter);
	}

	public WB_Sphere createSphereWithRadius(final double x, final double y, final double z, final double radius) {
		return createSphereWithRadius(createPoint(x, y, z), radius);
	}

	public WB_Sphere createSphereWithDiameter(final double x, final double y, final double z, final double diameter) {
		return createSphereWithRadius(createPoint(x, y, z), .5 * diameter);
	}

	public WB_Tetrahedron createTetrahedron(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3,
			final WB_Coord p4) {
		return new WB_Tetrahedron(p1, p2, p3, p4);
	}

	public WB_Point getIntersection(final WB_Coord a, final WB_Coord b, final WB_Plane P) {
		final Object o = WB_GeometryOp3D.getIntersection3D(a, b, P).getObject();
		return o == null ? null : (WB_Point) o;
	}

	public WB_Polygon createPolygonConvexHull(final WB_Polygon poly) {
		return WB_JTS.createPolygonConvexHull(poly);
	}

	public List<WB_Polygon> createBufferedPolygons(final WB_Polygon poly, final double d) {
		return WB_JTS.createBufferedPolygons(poly, d);
	}

	public List<WB_Polygon> createBufferedPolygons(final WB_Polygon poly, final double d, final int n) {
		return WB_JTS.createBufferedPolygons(poly, d, n);
	}

	public List<WB_Polygon> createBufferedPolygonsStraight(final WB_Polygon poly, final double d) {
		return WB_JTS.createBufferedPolygonsStraight(poly, d);
	}

	public List<WB_Polygon> createBufferedPolygons(final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createBufferedPolygons(poly, d);
	}

	public List<WB_Polygon> createBufferedPolygons(final Collection<? extends WB_Polygon> poly, final double d,
			final int n) {
		return WB_JTS.createBufferedPolygons(poly, d, n);
	}

	public List<WB_Polygon> createBufferedPolygonsStraight(final Collection<? extends WB_Polygon> poly,
			final double d) {
		return WB_JTS.createBufferedPolygonsStraight(poly, d);
	}

	public List<WB_Polygon> createBoundaryPolygons(final WB_Polygon poly) {
		return WB_JTS.createBoundaryPolygons(poly);
	}

	public List<WB_Polygon> createRibbonPolygons(final WB_Polygon poly, final double d) {
		return WB_JTS.createRibbonPolygons(poly, d);
	}

	public List<WB_Polygon> createRibbonPolygons(final Collection<? extends WB_Polygon> poly, final double d) {
		return WB_JTS.createRibbonPolygons(poly, d);
	}

	public List<WB_Polygon> createRibbonPolygons(final WB_Polygon poly, final double o, final double i) {
		return WB_JTS.createRibbonPolygons(poly, o, i);
	}

	public List<WB_Polygon> createRibbonPolygons(final Collection<? extends WB_Polygon> poly, final double o,
			final double i) {
		return WB_JTS.createRibbonPolygons(poly, o, i);
	}

	public List<WB_Polygon> createSimplifiedPolygon(final WB_Polygon poly, final double tol) {
		return WB_JTS.createSimplifiedPolygon(poly, tol);
	}

	public List<WB_Polygon> createDensifiedPolygon(final WB_Polygon poly, final double max) {
		return WB_JTS.createDensifiedPolygon(poly, max);
	}

	public List<WB_Polygon> constrainPolygons(final WB_Polygon poly, final WB_Polygon container) {
		return WB_JTS.constrainPolygons(poly, container);
	}

	public List<WB_Polygon> constrainPolygons(final WB_Polygon[] polygons, final WB_Polygon container) {
		return WB_JTS.constrainPolygons(polygons, container);
	}

	public List<WB_Polygon> constrainPolygons(final List<WB_Polygon> polygons, final WB_Polygon container) {
		return WB_JTS.constrainPolygons(polygons, container);
	}

	public List<WB_Coord> createUniquePoints(final List<WB_Coord> points, final double threshold) {
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = points.get(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		final int nuv = 1;
		final double threshold2 = threshold * threshold;
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
		final List<WB_Coord> uniqueVertices = new WB_CoordList();
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
		WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Coord> neighbor;
		WB_Coord v = points.get(0);
		kdtree.add(v, 0);
		uniqueVertices.add(v);
		final int nuv = 1;
		final double threshold2 = WB_Epsilon.SQEPSILON;
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
		final List<WB_Plane> uniquePlanes = new FastList<>();
		boolean unique = true;
		WB_Plane Pi, Pj;
		uniquePlanes.add(planes.get(0));
		for (int i = 1; i < planes.size(); i++) {
			Pi = planes.get(i);
			unique = true;
			for (int j = 0; j < i; j++) {
				Pj = planes.get(j);
				if (WB_GeometryOp3D.isEqual(Pi, Pj)) {
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
