package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_Triangle implements WB_Transformable3D {
	WB_Coord p1;
	WB_Coord p2;
	WB_Coord p3;
	private double a;
	private double b;
	private double c;
	private double cosA;
	private double cosB;
	private double cosC;
	int index;

	protected WB_Triangle() {
	}

	private final WB_GeometryFactory3D geometryfactory = new WB_GeometryFactory3D();

	public WB_Triangle(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		update();
	}

	public void cycle() {
		final WB_Coord tmp = p1;
		p1 = p2;
		p2 = p3;
		p3 = tmp;
		update();
	}

	public void cycle(int n) {
		while (n >= 3) {
			n -= 3;
		}
		while (n < 0) {
			n += 3;
		}
		for (int i = 0; i < n; i++) {
			cycle();
		}
	}

	protected void update() {
		a = WB_Vector.getDistance3D(p2, p3);
		b = WB_Vector.getDistance3D(p1, p3);
		c = WB_Vector.getDistance3D(p1, p2);
		final WB_Plane P = this.getPlane();
		if (P == null) {
			cosA = cosB = cosC = Double.NaN;
		} else {
			final WB_Map2D plane = geometryfactory.createEmbeddedPlane(P);
			final WB_Point pp1 = new WB_Point();
			plane.mapPoint3D(p1, pp1);
			final WB_Point pp2 = new WB_Point();
			plane.mapPoint3D(p2, pp2);
			final WB_Point pp3 = new WB_Point();
			plane.mapPoint3D(p3, pp3);
			cosA = WB_Epsilon.isZero(b * c) ? Double.NaN
					: ((pp2.xd() - pp1.xd()) * (pp3.xd() - pp1.xd()) + (pp2.yd() - pp1.yd()) * (pp3.yd() - pp1.yd()))
							/ (b * c);
			cosB = WB_Epsilon.isZero(a * c) ? Double.NaN
					: ((pp1.xd() - pp2.xd()) * (pp3.xd() - pp2.xd()) + (pp1.yd() - pp2.yd()) * (pp3.yd() - pp2.yd()))
							/ (a * c);
			cosC = WB_Epsilon.isZero(a * b) ? Double.NaN
					: ((pp2.xd() - pp3.xd()) * (pp1.xd() - pp3.xd()) + (pp2.yd() - pp3.yd()) * (pp1.yd() - pp3.yd()))
							/ (a * b);
		}
	}

	public WB_Coord p1() {
		return p1;
	}

	public WB_Coord p2() {
		return p2;
	}

	public WB_Coord p3() {
		return p3;
	}

	public double a() {
		return a;
	}

	public double b() {
		return b;
	}

	public double c() {
		return c;
	}

	public double cosA() {
		return cosA;
	}

	public double cosB() {
		return cosB;
	}

	public double cosC() {
		return cosC;
	}

	public WB_Coord getPoint(final int i) {
		if (i == 0) {
			return p1;
		} else if (i == 1) {
			return p2;
		} else if (i == 2) {
			return p3;
		}
		return null;
	}

	public WB_Point getCenter() {
		return geometryfactory.createMidpoint(p1, p2, p3);
	}

	public WB_Plane getPlane() {
		final WB_Plane P = new WB_Plane(p1, p2, p3);
		if (P.getNormal().getSqLength3D() < WB_Epsilon.SQEPSILON) {
			return null;
		}
		return new WB_Plane(getCenter(), P.getNormal());
	}

	public WB_Map2D getEmbeddedPlane() {
		final WB_Plane P = getPlane();
		return P == null ? null : geometryfactory.createEmbeddedPlane(getPlane());
	}

	public WB_Point getCentroid() {
		return getPointFromTrilinear(b * c, c * a, a * b);
	}

	public WB_Point getCircumcenter() {
		return getPointFromTrilinear(cosA, cosB, cosC);
	}

	public WB_Point getOrthocenter() {
		final double a2 = a * a;
		final double b2 = b * b;
		final double c2 = c * c;
		return getPointFromBarycentric((a2 + b2 - c2) * (a2 - b2 + c2), (a2 + b2 - c2) * (-a2 + b2 + c2),
				(a2 - b2 + c2) * (-a2 + b2 + c2));
	}

	public WB_Point getPointFromTrilinear(final double x, final double y, final double z) {
		final double abc = a * x + b * y + c * z;
		final WB_Point ea = WB_Point.sub(p2, p3);
		final WB_Point eb = WB_Point.sub(p1, p3);
		ea.mulSelf(b * y);
		eb.mulSelf(a * x);
		ea.addSelf(eb);
		ea.divSelf(abc);
		ea.addSelf(p3);
		return ea;
	}

	public WB_Point getPointFromBarycentric(final double x, final double y, final double z) {
		return getPointFromTrilinear(x / a, y / b, z / c);
	}

	public double[] getBarycentricCoordinates(final WB_Coord p) {
		final double m = (p3.xd() - p1.xd()) * (p2.yd() - p1.yd()) - (p3.yd() - p1.yd()) * (p2.xd() - p1.xd());
		double nu, nv, ood;
		nu = WB_GeometryOp2D.twiceSignedTriArea2D(p.xd(), p.yd(), p2.xd(), p2.yd(), p3.xd(), p3.yd());
		nv = WB_GeometryOp2D.twiceSignedTriArea2D(p.xd(), p.yd(), p3.xd(), p3.yd(), p1.xd(), p1.yd());
		ood = -1.0 / m;
		nu *= ood;
		nv *= ood;
		return new double[] { nu, nv, 1 - nu - nv };
	}

	public WB_Point getBarycentric(final WB_Coord p) {
		final WB_Vector m = WB_Vector.subToVector3D(p3, p1).cross(WB_Vector.subToVector3D(p2, p1));
		double nu, nv, ood;
		final double x = WB_Math.fastAbs(m.xd());
		final double y = WB_Math.fastAbs(m.yd());
		final double z = WB_Math.fastAbs(m.zd());
		if (x >= y && x >= z) {
			nu = WB_GeometryOp2D.twiceSignedTriArea2D(p.yd(), p.zd(), p2.yd(), p2.zd(), p3.yd(), p3.zd());
			nv = WB_GeometryOp2D.twiceSignedTriArea2D(p.yd(), p.zd(), p3.yd(), p3.zd(), p1.yd(), p1.zd());
			ood = 1.0 / m.xd();
		} else if (y >= x && y >= z) {
			nu = WB_GeometryOp2D.twiceSignedTriArea2D(p.xd(), p.zd(), p2.xd(), p2.zd(), p3.xd(), p3.zd());
			nv = WB_GeometryOp2D.twiceSignedTriArea2D(p.xd(), p.zd(), p3.xd(), p3.zd(), p1.xd(), p1.zd());
			ood = -1.0 / m.yd();
		} else {
			nu = WB_GeometryOp2D.twiceSignedTriArea2D(p.xd(), p.yd(), p2.xd(), p2.yd(), p3.xd(), p3.yd());
			nv = WB_GeometryOp2D.twiceSignedTriArea2D(p.xd(), p.yd(), p3.xd(), p3.yd(), p1.xd(), p1.yd());
			ood = -1.0 / m.zd();
		}
		nu *= ood;
		nv *= ood;
		return new WB_Point(nu, nv, 1 - nu - nv);
	}

	public double getArea() {
		return WB_Math.fastAbs(getSignedArea());
	}

	public WB_AABB getAABB() {
		return new WB_AABB(minX(), minY(), minZ(), maxX(), maxY(), maxZ());
	}

	public WB_AABB2D getAABB2D() {
		return new WB_AABB2D(minX(), minY(), maxX(), maxY());
	}

	public double getSignedArea() {
		final WB_Plane P = getPlane();
		if (P == null) {
			return 0.0;
		}
		final WB_Vector n = P.getNormal();
		final double x = WB_Math.fastAbs(n.xd());
		final double y = WB_Math.fastAbs(n.yd());
		final double z = WB_Math.fastAbs(n.zd());
		double area = 0;
		int coord = 3;
		if (x >= y && x >= z) {
			coord = 1;
		} else if (y >= x && y >= z) {
			coord = 2;
		}
		switch (coord) {
		case 1:
			area = p1.yd() * (p2.zd() - p3.zd()) + p2.yd() * (p3.zd() - p1.zd()) + p3.yd() * (p1.zd() - p2.zd());
			break;
		case 2:
			area = p1.xd() * (p2.zd() - p3.zd()) + p2.xd() * (p3.zd() - p1.zd()) + p3.xd() * (p1.zd() - p2.zd());
			break;
		case 3:
			area = p1.xd() * (p2.yd() - p3.yd()) + p2.xd() * (p3.yd() - p1.yd()) + p3.xd() * (p1.yd() - p2.yd());
			break;
		}
		switch (coord) {
		case 1:
			area *= 0.5 / x;
			break;
		case 2:
			area *= 0.5 / y;
			break;
		case 3:
			area *= 0.5 / z;
		}
		return area;
	}

	public WB_Circle getCircumcircle() {
		final WB_Circle result = new WB_Circle();
		result.setRadius(a * b * c / Math.sqrt(2 * a * a * b * b + 2 * b * b * c * c + 2 * a * a * c * c - a * a * a * a
				- b * b * b * b - c * c * c * c));
		final double bx = p2.xd() - p1.xd();
		final double by = p2.yd() - p1.yd();
		final double cx = p3.xd() - p1.xd();
		final double cy = p3.yd() - p1.yd();
		double d = 2 * (bx * cy - by * cx);
		if (WB_Epsilon.isZero(d)) {
			return null;
		}
		d = 1.0 / d;
		final double b2 = bx * bx + by * by;
		final double c2 = cx * cx + cy * cy;
		final double x = (cy * b2 - by * c2) * d;
		final double y = (bx * c2 - cx * b2) * d;
		result.setCenter(x + p1.xd(), y + p1.yd());
		return result;
	}

	public WB_Circle getIncircle() {
		final WB_Circle result = new WB_Circle();
		final double abc = a + b + c;
		result.setRadius(0.5 * Math.sqrt((b + c - a) * (c + a - b) * (a + b - c) / abc));
		final WB_Point ta = WB_Point.mul(p1, a);
		final WB_Point tb = WB_Point.mul(p2, b);
		final WB_Point tc = WB_Point.mul(p3, c);
		tc.addSelf(ta).addSelf(tb).divSelf(abc);
		result.setCenter(tc);
		return result;
	}

	public WB_Point getIncenter() {
		return getPointFromTrilinear(1, 1, 1);
	}

	public double minX() {
		return Math.min(Math.min(p1.xd(), p2.xd()), p3.xd());
	}

	public double minY() {
		return Math.min(Math.min(p1.yd(), p2.yd()), p3.yd());
	}

	public double minZ() {
		return Math.min(Math.min(p1.zd(), p2.zd()), p3.zd());
	}

	public double maxX() {
		return Math.max(Math.max(p1.xd(), p2.xd()), p3.xd());
	}

	public double maxY() {
		return Math.max(Math.max(p1.yd(), p2.yd()), p3.yd());
	}

	public double maxZ() {
		return Math.max(Math.max(p1.zd(), p2.zd()), p3.zd());
	}

	@Override
	public WB_Triangle apply2D(final WB_Transform2D T) {
		return new WB_Triangle(new WB_Point(p1).apply2D(T), new WB_Point(p2).apply2D(T), new WB_Point(p3).apply2D(T));
	}

	@Override
	public WB_Triangle apply2DSelf(final WB_Transform2D T) {
		p1 = new WB_Point(p1).apply2DSelf(T);
		p2 = new WB_Point(p2).apply2DSelf(T);
		p3 = new WB_Point(p3).apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Triangle apply(final WB_Transform3D T) {
		return new WB_Triangle(new WB_Point(p1).apply(T), new WB_Point(p2).apply(T), new WB_Point(p3).apply(T));
	}

	@Override
	public WB_Triangle applySelf(final WB_Transform3D T) {
		p1 = new WB_Point(p1).applySelf(T);
		p2 = new WB_Point(p2).applySelf(T);
		p3 = new WB_Point(p3).applySelf(T);
		return this;
	}
}
