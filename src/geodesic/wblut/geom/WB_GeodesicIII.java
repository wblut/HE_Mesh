/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.security.InvalidParameterException;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Geodesic.Type;
import wblut.math.WB_Epsilon;

class WB_GeodesicIII {

	public static final int TETRAHEDRO = 0;

	public static final int OCTAHEDRO = 1;

	public static final int ICOSAHEDRO = 2;

	private final double[][] centralanglesabc;

	private static double PI = Math.PI;

	private static double[][] surfaceanglesABC = new double[][] { { PI / 3.0, PI / 3.0, PI / 2.0 },
			{ PI / 3.0, PI / 4.0, PI / 2.0 }, { PI / 3.0, PI / 5.0, PI / 2.0 } };

	private final double TORADIANS = Math.PI / 180.0;

	private final int b, c, v;

	private final double radius;

	private final Type type;

	private WB_SimpleMesh mesh;

	private static WB_GeometryFactory gf = new WB_GeometryFactory();

	public List<WB_Point> points;

	public List<WB_Point> PPT;

	public List<WB_Point> zeropoints;

	/**
	 *
	 *
	 * @param radius
	 * @param b
	 * @param c
	 * @param type
	 */
	public WB_GeodesicIII(final double radius, final int b, final int c, final Type type) {
		if (b <= 0 || c <= 0 || b == c) {
			throw new InvalidParameterException("Invalid values for b and c.");
		}
		if (type != Type.TETRAHEDRON && type != Type.OCTAHEDRON && type != Type.ICOSAHEDRON) {
			throw new InvalidParameterException("Type should be one of TETRAHEDRON , OCTAHEDRON or ICOSAHEDRON.");
		}

		this.type = type;
		this.radius = radius;
		this.b = b;
		this.c = c;
		this.v = b + c;
		centralanglesabc = new double[3][3];
		for (int i = 0; i < 3; i++) {
			centralanglesabc[i][0] = Math.acos(Math.cos(surfaceanglesABC[i][0]) / Math.sin(surfaceanglesABC[i][1]));// cos
			// a
			// =
			// cos
			// A
			// /
			// sin
			// B
			centralanglesabc[i][1] = Math.acos(Math.cos(surfaceanglesABC[i][1]) / Math.sin(surfaceanglesABC[i][0]));// cos
			// b
			// =
			// cos
			// B
			// /
			// sin
			// A
			centralanglesabc[i][2] = Math.acos(Math.cos(centralanglesabc[i][0]) * Math.cos(centralanglesabc[i][1]));// cos
			// c
			// =
			// cos
			// a
			// x
			// cos
			// b
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_SimpleMesh getMesh() {
		createMesh();
		return mesh;
	}

	/**
	 *
	 */
	private void createMesh() {
		
		final WB_Point p0 = getPoint(0, 0);
		final WB_Point p1 = getPoint(b, c);
		final WB_Point p2 = getPoint(b + c, c - (b + c));
		WB_Point p;
		WB_Point cp;
		double scalefactor = 1.0;
		WB_Vector zshift = new WB_Vector(0, 0, 1);
		switch (type) {
		case TETRAHEDRON:
			scalefactor = Math.sqrt(8.0 / 3.0) / p1.getLength();
			zshift = new WB_Vector(0, 0, 1.0 / 3.0);
			break;
		case OCTAHEDRON:
			scalefactor = Math.sqrt(2.0) / p1.getLength();
			zshift = new WB_Vector(0, 0, Math.sqrt(3.0) / 3.0);
			break;
		case ICOSAHEDRON:
		default:
			scalefactor = 1.0 / Math.sin(0.4 * Math.PI) / p1.getLength();
			zshift = new WB_Vector(0, 0, Math.sqrt(3) / 12.0 * (3 + Math.sqrt(5)) / Math.sin(0.4 * Math.PI));
		}
		p0.mulSelf(scalefactor);
		p1.mulSelf(scalefactor);
		p2.mulSelf(scalefactor);
		
		PPT = new FastList<WB_Point>();
		for (int i = -v; i <= v; i++) {
			for (int j = -v; j <= v; j++) {
				p = getPoint(i, j).mulSelf(scalefactor);
				cp = WB_GeometryOp.getClosestPointToTriangle3D(p, p0, p1, p2);
				if (WB_Epsilon.isZeroSq(cp.getSqDistance(p))) {
					PPT.add(p);
				}
			}
		}
		zeropoints = new FastList<WB_Point>();
		final double angle = Math.PI / 6.0 - p1.getHeading2D();
		final WB_Point center = gf.createMidpoint(p0, p1, p2).mulSelf(-1);
		WB_Transform3D T = new WB_Transform3D().addTranslate(center).addRotateZ(angle).addTranslate(zshift)
				.addRotateY(centralanglesabc[type == Type.TETRAHEDRON ? 0 : type == Type.OCTAHEDRON ? 1 : 2][2]);
		for (int i = 0; i < PPT.size(); i++) {
			p = T.applyAsPoint(PPT.get(i));
			p.normalizeSelf();
			p.mulSelf(radius);
			zeropoints.add(p);
			PPT.get(i).applyAsPointSelf(T);
			PPT.get(i).mulSelf(radius);
		}
		final double threshold = zeropoints.get(0).getDistance(zeropoints.get(1)) / (2 * v);

		points = new FastList<WB_Point>();
		points.addAll(zeropoints);
		switch (type) {
		case TETRAHEDRON:
			T = new WB_Transform3D().addRotateZ(TORADIANS * 120.0);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(TORADIANS * 240.0);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(Math.PI).addRotateY(TORADIANS * 250.5288);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			break;
		case OCTAHEDRON:
			final List<WB_Point> points4 = new FastList<WB_Point>();
			T = new WB_Transform3D().addRotateZ(Math.PI).addRotateY(Math.PI);
			for (final WB_Point point : zeropoints) {
				points4.add(T.applyAsPoint(point));
			}
			points.addAll(points4);
			T = new WB_Transform3D().addRotateZ(Math.PI / 2.0);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points4) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(Math.PI / 2.0);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points4) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(Math.PI);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points4) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(1.5 * Math.PI);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points4) {
				points.add(T.applyAsPoint(point));
			}
			break;
		case ICOSAHEDRON:
		default:
			T = new WB_Transform3D().addRotateZ(Math.PI).addRotateY(Math.PI / 180 * 116.5651);
			final List<WB_Point> points5 = new FastList<WB_Point>();
			for (final WB_Point point : zeropoints) {
				points5.add(T.applyAsPoint(point));
			}
			points.addAll(points5);
			T = new WB_Transform3D().addRotateY(Math.PI / 180 * 63.43495).addRotateZ(Math.PI / 180 * 36);
			final List<WB_Point> points6 = new FastList<WB_Point>();
			for (final WB_Point point : zeropoints) {
				points6.add(T.applyAsPoint(point));
			}
			points.addAll(points6);
			T = new WB_Transform3D().addRotateY(-Math.PI).addRotateZ(-Math.PI / 180 * 144);
			final List<WB_Point> points15 = new FastList<WB_Point>();
			for (final WB_Point point : zeropoints) {
				points15.add(T.applyAsPoint(point));
			}
			points.addAll(points15);
			T = new WB_Transform3D().addRotateZ(TORADIANS * 72.0);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points5) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points6) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points15) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(TORADIANS * 144);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points5) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points6) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points15) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(TORADIANS * 216);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points5) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points6) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points15) {
				points.add(T.applyAsPoint(point));
			}
			T = new WB_Transform3D().addRotateZ(TORADIANS * 288);
			for (final WB_Point point : zeropoints) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points5) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points6) {
				points.add(T.applyAsPoint(point));
			}
			for (final WB_Point point : points15) {
				points.add(T.applyAsPoint(point));
			}
		}
		mesh = gf.createConvexHullWithThreshold(points, false, threshold);
	}
	static final private double cos60 = Math.cos(Math.PI / 3.0);
	static final private double sin60 = Math.sin(Math.PI / 3.0);
	WB_Point getPoint(final int b, final int c) {
		return new WB_Point( (b+cos60 * c ), sin60 * c, 0);
	}
}
