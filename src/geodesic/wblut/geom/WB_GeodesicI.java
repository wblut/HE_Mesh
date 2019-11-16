/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.security.InvalidParameterException;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Geodesic.Type;
import wblut.geom.WB_Geodesic.WB_GreatCircleIntersection;

class WB_GeodesicI {
	public static final int				EQUALCHORD	= 0;
	public static final int				EQUALARC	= 1;
	public static final int				EQUALARC2GC	= 2;
	public static final int				MIDARC		= 3;
	private static double[][]			deltahedron	= new double[][] {
			{ 0.471405, 0.816497, -0.333333 }, { 0.707107, 0.707107, 0 },
			{ 0.723607, 0.525731, 0.447214 } };
	public WB_Point[]					apices;
	public WB_Point[]					refpoints;
	public WB_Point[]					PPTpoints;
	private WB_Plane					P;
	private final int					v;
	private WB_SimpleMesh						mesh;
	private static WB_GeometryFactory	gf			= new WB_GeometryFactory();
	private final double				radius;
	private final WB_Geodesic.Type		type;
	private final int					div;

	/**
	 *
	 *
	 * @param radius
	 * @param v
	 */
	public WB_GeodesicI(final double radius, final int v) {
		this(radius, v, WB_Geodesic.Type.ICOSAHEDRON, EQUALARC);
	}

	/**
	 *
	 *
	 * @param radius
	 * @param v
	 * @param type
	 * @param div
	 */
	public WB_GeodesicI(final double radius, final int v, final Type type,
			final int div) {
		if (v <= 0) {
			throw new InvalidParameterException("v should be 1 or larger.");
		}
		if (type != Type.TETRAHEDRON && type != Type.OCTAHEDRON
				&& type != Type.ICOSAHEDRON) {
			throw new InvalidParameterException(
					"Type should be one of TETRAHEDRON , OCTAHEDRON or ICOSAHEDRON.");
		}
		this.type = type;
		this.radius = radius;
		this.div = div;
		final int lv = (int) Math.round(Math.log(v) / Math.log(2.0));
		final int cv = (int) Math.pow(2, lv);
		this.v = div == MIDARC ? cv : v;
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
		apices = new WB_Point[3];
		apices[0] = gf.createPoint(0, 0, 1);
		switch (type) {
			case TETRAHEDRON:
				apices[2] = gf.createPoint(deltahedron[0]);
				apices[1] = gf.createPoint(deltahedron[0][0],
						-deltahedron[0][1], deltahedron[0][2]);
				break;
			case OCTAHEDRON:
				apices[2] = gf.createPoint(deltahedron[1]);
				apices[1] = gf.createPoint(deltahedron[1][0],
						-deltahedron[1][1], deltahedron[1][2]);
				break;
			case ICOSAHEDRON:
			default:
				apices[2] = gf.createPoint(deltahedron[2]);
				apices[1] = gf.createPoint(deltahedron[2][0],
						-deltahedron[2][1], deltahedron[2][2]);
		}
		P = new WB_Plane(apices[0], apices[1], apices[2]);
		final double iv = 1.0 / v;
		if (div == EQUALCHORD) {
			refpoints = new WB_Point[3 * (v - 1)];
			for (int i = 1; i < v; i++) {
				refpoints[i - 1 + 2 * (v - 1)] = apices[0]
						.mulAddMul(1.0 - i * iv, i * iv, apices[1]);
				refpoints[i - 1 + 2 * (v - 1)].normalizeSelf();
				refpoints[i - 1 + v - 1] = apices[0].mulAddMul(1.0 - i * iv,
						i * iv, apices[2]);
				refpoints[i - 1 + v - 1].normalizeSelf();
				refpoints[i - 1] = apices[1].mulAddMul(1.0 - i * iv, i * iv,
						apices[2]);
				refpoints[i - 1].normalizeSelf();
			}
		} else if (div == MIDARC) {
		} else {
			refpoints = new WB_Point[3 * (v - 1)];
			for (int i = 1; i < v; i++) {
				refpoints[i - 1 + 2 * (v - 1)] = gf.createPoint(WB_Geodesic
						.getPointOnGreatCircleArc(apices[0], apices[1], i * iv));
				refpoints[i - 1 + v - 1] = gf.createPoint(WB_Geodesic
						.getPointOnGreatCircleArc(apices[0], apices[2], i * iv));
				refpoints[i - 1] = gf.createPoint(WB_Geodesic
						.getPointOnGreatCircleArc(apices[1], apices[2], i * iv));
			}
		}
		if (div == EQUALCHORD || div == EQUALARC2GC) {
			PPTpoints = new WB_Point[(v - 1) * (v - 2) / 2];
			int id = 0;
			WB_Point p5, p6, p3, p4;
			WB_GreatCircleIntersection gci;
			for (int i = 1; i < v - 1; i++) {
				for (int j = 0; j < i; j++) {
					p3 = refpoints[j];
					p6 = refpoints[v - 1 - i + j];
					p5 = refpoints[i - j - 1 + 2 * (v - 1)];
					p4 = refpoints[j + v - 1];
					gci = WB_Geodesic.getGreatCircleIntersection(p5, p6, p3,
							p4);
					PPTpoints[id] = selectPoint(gci, apices, type);
					PPTpoints[id].normalizeSelf();
					id++;
				}
			}
		} else if (div == MIDARC) {
			final int lv = (int) Math.round(Math.log(v) / Math.log(2.0));
			final WB_Point[][] pts = midpoint(lv);
			refpoints = new WB_Point[3 * (v - 1)];
			for (int i = 0; i < v - 1; i++) {
				refpoints[i] = pts[v][i + 1];
				refpoints[i + v - 1] = pts[i + 1][i + 1];
				refpoints[i + 2 * v - 2] = pts[i + 1][0];
			}
			PPTpoints = new WB_Point[(v - 1) * (v - 2) / 2];
			int id = 0;
			for (int i = 2; i < v; i++) {
				for (int j = 0; j < i - 1; j++) {
					PPTpoints[id++] = pts[i][j + 1];
				}
			}
		} else {
			PPTpoints = new WB_Point[(v - 1) * (v - 2) / 2];
			int id = 0;
			WB_Point p1, p2, p3, p4, p5, p6, i0, i1, i2;
			WB_GreatCircleIntersection gci;
			for (int i = 1; i < v - 1; i++) {
				p1 = refpoints[i + 2 * (v - 1)];
				p2 = refpoints[i + v - 1];
				for (int j = 0; j < i; j++) {
					p3 = refpoints[j];
					p6 = refpoints[v - 1 - i + j];
					p5 = refpoints[i - j - 1 + 2 * (v - 1)];
					p4 = refpoints[j + v - 1];
					gci = WB_Geodesic.getGreatCircleIntersection(p1, p2, p3,
							p4);
					i0 = selectPoint(gci, apices, type);
					gci = WB_Geodesic.getGreatCircleIntersection(p1, p2, p5,
							p6);
					i1 = selectPoint(gci, apices, type);
					gci = WB_Geodesic.getGreatCircleIntersection(p3, p4, p5,
							p6);
					i2 = selectPoint(gci, apices, type);
					PPTpoints[id] = i0.add(i1).addSelf(i2);
					PPTpoints[id].normalizeSelf();
					id++;
				}
			}
		}
		int pid = 0;
		final List<WB_Point> points = new FastList<WB_Point>();
		final List<WB_Point> zeropoints = new FastList<WB_Point>();
		points.add(apices[0]);
		for (int i = 0; i < v - 1; i++) {
			points.add(refpoints[i + 2 * (v - 1)]);
			for (int j = 0; j < i; j++) {
				points.add(PPTpoints[pid++]);
			}
			points.add(refpoints[i + v - 1]);
		}
		points.add(apices[1]);
		for (int i = 0; i < v - 1; i++) {
			points.add(refpoints[i]);
		}
		points.add(apices[2]);
		for (final WB_Point p : points) {
			p.normalizeSelf();
			p.mulSelf(radius);
		}
		final double threshold = apices[0].getDistance(apices[1]) / (10 * v);
		zeropoints.addAll(points);
		WB_Transform3D T = new WB_Transform3D();
		switch (type) {
			case TETRAHEDRON:
				T = new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 120.0);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 240.0);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI)
						.addRotateY(Math.PI / 180.0 * 250.5288);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				break;
			case OCTAHEDRON:
				final List<WB_Point> points4 = new FastList<WB_Point>();
				T = new WB_Transform3D().addRotateZ(Math.PI).addRotateY(Math.PI);
				for (final WB_Point p : zeropoints) {
					points4.add(T.applyAsPoint(p));
				}
				points.addAll(points4);
				T = new WB_Transform3D().addRotateZ(Math.PI / 2.0);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points4) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI / 2.0);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points4) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points4) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(1.5 * Math.PI);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points4) {
					points.add(T.applyAsPoint(p));
				}
				break;
			case ICOSAHEDRON:
			default:
				T = new WB_Transform3D().addRotateZ(Math.PI)
						.addRotateY(Math.PI / 180 * 116.5651);
				final List<WB_Point> points5 = new FastList<WB_Point>();
				for (final WB_Point p : zeropoints) {
					points5.add(T.applyAsPoint(p));
				}
				points.addAll(points5);
				T = new WB_Transform3D().addRotateY(Math.PI / 180 * 63.43495)
						.addRotateZ(Math.PI / 180 * 36);
				final List<WB_Point> points6 = new FastList<WB_Point>();
				for (final WB_Point p : zeropoints) {
					points6.add(T.applyAsPoint(p));
				}
				points.addAll(points6);
				T = new WB_Transform3D().addRotateY(-Math.PI)
						.addRotateZ(-Math.PI / 180 * 144);
				final List<WB_Point> points15 = new FastList<WB_Point>();
				for (final WB_Point p : zeropoints) {
					points15.add(T.applyAsPoint(p));
				}
				points.addAll(points15);
				T = new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 72.0);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points5) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points6) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points15) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 144);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points5) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points6) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points15) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 216);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points5) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points6) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points15) {
					points.add(T.applyAsPoint(p));
				}
				T = new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 288);
				for (final WB_Point p : zeropoints) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points5) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points6) {
					points.add(T.applyAsPoint(p));
				}
				for (final WB_Point p : points15) {
					points.add(T.applyAsPoint(p));
				}
		}
		mesh = gf.createConvexHullWithThreshold(points, false, threshold);
	}

	/**
	 *
	 *
	 * @param gci
	 * @param apices
	 * @param type
	 * @return
	 */
	private WB_Point selectPoint(final WB_GreatCircleIntersection gci,
			final WB_Point[] apices, final Type type) {
		if (type == Type.TETRAHEDRON) {
			return Math.abs(WB_GeometryOp.getDistance3D(gci.p0, P)) < Math
					.abs(WB_GeometryOp.getDistance3D(gci.p1, P))
							? gf.createPoint(gci.p0)
							: gf.createPoint(gci.p1);
		}
		return gci.p0[2] > 0 ? gf.createPoint(gci.p0) : gf.createPoint(gci.p1);
	}

	/**
	 *
	 *
	 * @param log2v
	 * @return
	 */
	private WB_Point[][] midpoint(final int log2v) {
		final WB_Point[][] points = new WB_Point[v + 1][v + 1];
		points[0][0] = apices[0];
		points[v][0] = apices[1];
		points[v][v] = apices[2];
		recdivtriangle(points, 0, 0, v, 0, v, v, 0, log2v);
		return points;
	}

	/**
	 *
	 *
	 * @param points
	 * @param i0
	 * @param j0
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 * @param r
	 * @param n
	 */
	private void recdivtriangle(final WB_Point[][] points, final int i0,
			final int j0, final int i1, final int j1, final int i2,
			final int j2, final int r, final int n) {
		final int i01 = (i0 + i1) / 2;
		final int j01 = (j0 + j1) / 2;
		points[i01][j01] = points[i0][j0].mulAddMul(0.5, 0.5, points[i1][j1]);
		points[i01][j01].normalizeSelf();
		final int i02 = (i0 + i2) / 2;
		final int j02 = (j0 + j2) / 2;
		points[i02][j02] = points[i0][j0].mulAddMul(0.5, 0.5, points[i2][j2]);
		points[i02][j02].normalizeSelf();
		final int i12 = (i1 + i2) / 2;
		final int j12 = (j1 + j2) / 2;
		points[i12][j12] = points[i1][j1].mulAddMul(0.5, 0.5, points[i2][j2]);
		points[i12][j12].normalizeSelf();
		if (r < n - 1) {
			recdivtriangle(points, i0, j0, i01, j01, i02, j02, r + 1, n);
			recdivtriangle(points, i01, j01, i1, j1, i12, j12, r + 1, n);
			recdivtriangle(points, i02, j02, i12, j12, i2, j2, r + 1, n);
			recdivtriangle(points, i01, j01, i02, j02, i12, j12, r + 1, n);
		}
	}
}
