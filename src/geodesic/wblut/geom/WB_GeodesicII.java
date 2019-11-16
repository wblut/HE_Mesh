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

import wblut.geom.WB_Geodesic.Type;

class WB_GeodesicII {

	private final double[][] centralanglesabc;

	private static double PI = Math.PI;

	private static double[][] surfaceanglesABC = new double[][] { { PI / 3.0, PI / 3.0, PI / 2.0 },
			{ PI / 3.0, PI / 4.0, PI / 2.0 }, { PI / 4.0, PI / 3.0, PI / 2.0 }, { PI / 5.0, PI / 3.0, PI / 2.0 },
			{ PI / 3.0, PI / 5.0, PI / 2.0 } };

	private WB_Point[][] LCDPoints;

	private WB_Point[] triacon;

	private WB_Point[] points;

	private int[][] faces;

	private final int v, hv;

	private WB_SimpleMesh mesh;

	private static WB_GeometryFactory gf = new WB_GeometryFactory();

	private final double radius;

	private final Type type;

	private int vertexoffset;

	private int faceoffset;

	/**
	 *
	 *
	 * @param radius
	 * @param v
	 */
	public WB_GeodesicII(final double radius, final int v) {
		this(radius, v, Type.ICOSAHEDRON);
	}

	/**
	 *
	 *
	 * @param radius
	 * @param v
	 * @param type
	 */
	public WB_GeodesicII(final double radius, final int v, final Type type) {
		if (v <= 0) {
			throw new InvalidParameterException("v should be 1 or larger.");
		}

		this.type = type;
		this.radius = radius;
		this.v = v / 2 * 2;
		hv = this.v / 2;
		centralanglesabc = new double[5][3];
		for (int i = 0; i < 5; i++) {
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
		LCDPoints = new WB_Point[hv + 1][hv + 1];
		final double[][] subtrianglesabc = new double[hv][3];
		final double[][] subtrianglesABC = new double[hv][3];
		subtrianglesabc[0] = centralanglesabc[type.getIndex()];
		subtrianglesABC[0] = surfaceanglesABC[type.getIndex()];
		final double[] a = new double[hv + 1];
		final double[] b = new double[hv + 1];
		a[0] = 0;
		b[0] = 0;
		a[hv] = centralanglesabc[type.getIndex()][0];
		b[hv] = centralanglesabc[type.getIndex()][1];
		for (int i = 1; i < hv; i++) {
			a[i] = i * centralanglesabc[type.getIndex()][0] / hv;
			subtrianglesABC[i][1] = surfaceanglesABC[type.getIndex()][1];
			subtrianglesABC[i][2] = surfaceanglesABC[type.getIndex()][2];
			subtrianglesABC[i][0] = Math.acos(Math.cos(a[i]) * Math.sin(subtrianglesABC[i][1])); // cos
																									// A
																									// =
																									// cos
																									// a
																									// x
																									// sin
																									// B
			b[i] = Math.acos(Math.cos(subtrianglesABC[i][1]) / Math.sin(subtrianglesABC[i][0])); // cos
																									// b
																									// =
																									// cos
																									// B
																									// /
																									// sin
																									// A
		}
		for (int i = 0; i <= hv; i++) {
			for (int j = 0; j <= hv - i; j++) {
				LCDPoints[i][j] = new WB_Point(radius * Math.sin(a[i]) * Math.cos(b[j]), radius * Math.sin(b[j]),
						radius * Math.cos(a[i]) * Math.cos(b[j]));
			}
		}
		triacon = new WB_Point[(hv + 1) * (hv + 1)];
		final int oddeven = hv % 2;
		final double qv = hv / 2.0;
		for (int i = 0; i <= hv; i++) {
			for (int j = 0; j <= hv - i; j++) {
				if ((i + j) % 2 == oddeven) {
					triacon[index(qv + (i - j) / 2.0, qv + (i + j) / 2.0, hv)] = LCDPoints[i][j];
					if (j != 0) {
						triacon[index(qv + (i + j) / 2.0, qv + (i - j) / 2.0, hv)] = new WB_Point(LCDPoints[i][j].xd(),
								-LCDPoints[i][j].yd(), LCDPoints[i][j].zd());
					}
					if (i != 0) {
						triacon[index(qv - (i + j) / 2.0, qv - (i - j) / 2.0, hv)] = new WB_Point(-LCDPoints[i][j].xd(),
								LCDPoints[i][j].yd(), LCDPoints[i][j].zd());
					}
					if (i != 0 && j != 0) {
						triacon[index(qv - (i - j) / 2.0, qv - (i + j) / 2.0, hv)] = new WB_Point(-LCDPoints[i][j].xd(),
								-LCDPoints[i][j].yd(), LCDPoints[i][j].zd());
					}
				}
			}
		}
		int numberoftriacons = 0;
		switch (type) {
		case TETRAHEDRON:
			numberoftriacons = 6;
			break;
		case OCTAHEDRON:
			numberoftriacons = 12;
			break;
		case CUBE:
			numberoftriacons = 12;
			break;
		case DODECAHEDRON:
			numberoftriacons = 30;
			break;
		case ICOSAHEDRON:
		default:
			numberoftriacons = 30;
		}
		points = new WB_Point[(hv + 1) * (hv + 1) * numberoftriacons];
		for (int i = 0; i < (hv + 1) * (hv + 1); i++) {
			points[i] = triacon[i];
		}
		faces = new int[2 * hv * hv * numberoftriacons][];
		int index = 0;
		for (int i = 0; i < hv; i++) {
			for (int j = 0; j < hv; j++) {
				faces[index++] = new int[] { index(i, j, hv), index(i + 1, j, hv), index(i, j + 1, hv) };
				faces[index++] = new int[] { index(i, j + 1, hv), index(i + 1, j, hv), index(i + 1, j + 1, hv) };
			}
		}
		vertexoffset = (hv + 1) * (hv + 1);
		faceoffset = 2 * hv * hv;
		switch (type) {
		case TETRAHEDRON:
			addTransformedFaces(new WB_Transform3D().addRotateZ(0.5 * Math.PI).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-0.5 * Math.PI).addRotateY(-0.25 * Math.PI)
					.addRotateZ(-0.25 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-0.5 * Math.PI).addRotateY(0.25 * Math.PI)
					.addRotateZ(0.25 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(0.5 * Math.PI).addRotateY(0.25 * Math.PI)
					.addRotateZ(-0.25 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(0.5 * Math.PI).addRotateY(-0.25 * Math.PI)
					.addRotateZ(0.25 * Math.PI));
			break;
		case OCTAHEDRON:
			addTransformedFaces(new WB_Transform3D().addRotateY(0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(1.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(0.25 * Math.PI).addRotateZ(0.5 * Math.PI)
					.addRotateY(-0.25 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-0.25 * Math.PI).addRotateZ(-0.5 * Math.PI)
					.addRotateY(0.25 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(0.25 * Math.PI).addRotateZ(0.5 * Math.PI)
					.addRotateY(-0.25 * Math.PI).addRotateZ(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-0.25 * Math.PI).addRotateZ(-0.5 * Math.PI)
					.addRotateY(0.25 * Math.PI).addRotateZ(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(0.25 * Math.PI).addRotateZ(0.5 * Math.PI)
					.addRotateY(-0.25 * Math.PI).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-0.25 * Math.PI).addRotateZ(-0.5 * Math.PI)
					.addRotateY(0.25 * Math.PI).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(0.25 * Math.PI).addRotateZ(0.5 * Math.PI)
					.addRotateY(-0.25 * Math.PI).addRotateZ(Math.PI).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-0.25 * Math.PI).addRotateZ(-0.5 * Math.PI)
					.addRotateY(0.25 * Math.PI).addRotateZ(Math.PI).addRotateX(Math.PI));
			break;
		case CUBE:
			addTransformedFaces(new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 35).addRotateY(Math.PI / 3.0)
					.addRotateZ(Math.PI / 180.0 * 35));
			addTransformedFaces(new WB_Transform3D().addRotateX(-0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(-Math.PI / 180.0 * 35).addRotateY(-Math.PI / 3.0)
					.addRotateZ(-Math.PI / 180.0 * 35));
			addTransformedFaces(new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 35).addRotateY(-Math.PI / 3.0)
					.addRotateZ(Math.PI / 180.0 * 35));
			addTransformedFaces(new WB_Transform3D().addRotateX(0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(-Math.PI / 180.0 * 35).addRotateY(Math.PI / 3.0)
					.addRotateZ(-Math.PI / 180.0 * 35));
			addTransformedFaces(new WB_Transform3D().addRotateZ(-Math.PI / 180.0 * 35).addRotateY(Math.PI / 3.0)
					.addRotateZ(-Math.PI / 180.0 * 35).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 35).addRotateY(-Math.PI / 3.0)
					.addRotateZ(Math.PI / 180.0 * 35).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(-Math.PI / 180.0 * 35).addRotateY(-Math.PI / 3.0)
					.addRotateZ(-Math.PI / 180.0 * 35).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(Math.PI / 180.0 * 35).addRotateY(Math.PI / 3.0)
					.addRotateZ(Math.PI / 180.0 * 35).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI));
			break;
		case DODECAHEDRON:
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 2.5)
					.addRotateX(-Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 5.0)
					.addRotateX(-Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 5.0)
					.addRotateX(-Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 2.5)
					.addRotateX(-Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 2.5)
					.addRotateX(Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 5.0)
					.addRotateX(Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 5.0)
					.addRotateX(Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 2.5)
					.addRotateX(Math.PI / 180.0 * 31.7170));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 10.0)
					.addRotateY(Math.PI / 180.0 * 58.2350));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 10.0)
					.addRotateY(-Math.PI / 180.0 * 58.2350));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 10.0)
					.addRotateY(-Math.PI / 180.0 * 58.2350));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 10.0)
					.addRotateY(Math.PI / 180.0 * 58.2350));
			addTransformedFaces(new WB_Transform3D().addRotateZ(0.5 * Math.PI).addRotateY(0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(0.5 * Math.PI).addRotateX(-0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(0.5 * Math.PI).addRotateY(-0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(0.5 * Math.PI).addRotateX(0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 2.5)
					.addRotateX(-Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 5.0)
					.addRotateX(-Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 5.0)
					.addRotateX(-Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 2.5)
					.addRotateX(-Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 2.5)
					.addRotateX(Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 5.0)
					.addRotateX(Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 5.0)
					.addRotateX(Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 2.5)
					.addRotateX(Math.PI / 180.0 * 31.7170).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 10.0)
					.addRotateY(Math.PI / 180.0 * 58.2350).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(-Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 10.0)
					.addRotateY(-Math.PI / 180.0 * 58.2350).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(-Math.PI / 10.0)
					.addRotateY(-Math.PI / 180.0 * 58.2350).addRotateX(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateX(Math.PI / 180.0 * 31.7170).addRotateZ(Math.PI / 10.0)
					.addRotateY(Math.PI / 180.0 * 58.2350).addRotateX(Math.PI));
			break;
		case ICOSAHEDRON:
		default:
			addTransformedFaces(new WB_Transform3D().addRotateZ(-0.5 * Math.PI).addRotateY(0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(-0.5 * Math.PI).addRotateX(-0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(-0.5 * Math.PI).addRotateY(-0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateZ(0.5 * Math.PI).addRotateX(0.5 * Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(Math.PI / 180.0 * 108.0).addRotateY(Math.PI / 180.0 * 31.7175));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(-Math.PI / 180.0 * 108.0).addRotateY(-Math.PI / 180.0 * 31.71749));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(Math.PI / 180.0 * 108.0).addRotateY(-Math.PI / 180.0 * 31.71749));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(-Math.PI / 180.0 * 108.0).addRotateY(Math.PI / 180.0 * 31.7175));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(Math.PI / 180.0 * 198.0).addRotateX(-Math.PI / 180.0 * 58.28255));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(-Math.PI / 180.0 * 144.0).addRotateY(Math.PI / 180.0 * 31.7175));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(Math.PI / 180.0 * 144.0).addRotateY(Math.PI / 180.0 * 31.7175));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(-Math.PI / 180.0 * 198.0).addRotateX(Math.PI / 180.0 * 58.2826));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(Math.PI / 180.0 * 198.0).addRotateX(Math.PI / 180.0 * 58.2826));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(-Math.PI / 180.0 * 144.0).addRotateY(-Math.PI / 180.0 * 31.71749));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(Math.PI / 180.0 * 144.0).addRotateY(-Math.PI / 180.0 * 31.71749));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(-Math.PI / 180.0 * 198.0).addRotateX(-Math.PI / 180.0 * 58.28255));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(Math.PI / 180.0 * 108.0).addRotateY(Math.PI / 180.0 * 31.7175).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(-Math.PI / 180.0 * 108.0).addRotateY(-Math.PI / 180.0 * 31.71749).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(Math.PI / 180.0 * 108.0).addRotateY(-Math.PI / 180.0 * 31.71749).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(-Math.PI / 180.0 * 108.0).addRotateY(Math.PI / 180.0 * 31.7175).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(Math.PI / 180.0 * 198.0).addRotateX(-Math.PI / 180.0 * 58.28255).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(-Math.PI / 180.0 * 144.0).addRotateY(Math.PI / 180.0 * 31.7175).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(Math.PI / 180.0 * 144.0).addRotateY(Math.PI / 180.0 * 31.7175).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(-Math.PI / 180.0 * 31.71749)
					.addRotateZ(-Math.PI / 180.0 * 198.0).addRotateX(Math.PI / 180.0 * 58.2826).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(Math.PI / 180.0 * 198.0).addRotateX(Math.PI / 180.0 * 58.2826).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(-Math.PI / 180.0 * 144.0).addRotateY(-Math.PI / 180.0 * 31.71749).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(Math.PI / 180.0 * 144.0).addRotateY(-Math.PI / 180.0 * 31.71749).addRotateY(Math.PI));
			addTransformedFaces(new WB_Transform3D().addRotateY(Math.PI / 180.0 * 31.7175)
					.addRotateZ(-Math.PI / 180.0 * 198.0).addRotateX(-Math.PI / 180.0 * 58.28255).addRotateY(Math.PI));
		}
		final WB_Transform3D T = new WB_Transform3D().addRotateY(-centralanglesabc[type.getIndex()][0]);
		if (type == Type.OCTAHEDRON || type == Type.DODECAHEDRON) {
			T.addRotateZ(surfaceanglesABC[type.getIndex()][1]);
		}
		for (final WB_Point p : points) {
			p.applyAsPointSelf(T);
		}
		final double threshold = LCDPoints[0][0].getDistance(LCDPoints[0][hv]) / (2 * v);
		mesh = gf.createUniqueMesh(gf.createMesh(points, faces), threshold);
	}

	/**
	 *
	 *
	 * @param T
	 */
	private void addTransformedFaces(final WB_Transform3D T) {
		int index = faceoffset;
		for (int i = 0; i < hv; i++) {
			for (int j = 0; j < hv; j++) {
				faces[index++] = new int[] { vertexoffset + index(i, j, hv), vertexoffset + index(i + 1, j, hv),
						vertexoffset + index(i, j + 1, hv) };
				faces[index++] = new int[] { vertexoffset + index(i, j + 1, hv), vertexoffset + index(i + 1, j, hv),
						vertexoffset + index(i + 1, j + 1, hv) };
			}
		}
		index = vertexoffset;
		for (int i = 0; i < (hv + 1) * (hv + 1); i++) {
			points[index++] = T.applyAsPoint(points[i]);
		}
		vertexoffset += (hv + 1) * (hv + 1);
		faceoffset += 2 * hv * hv;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param hv
	 * @return
	 */
	private int index(final double i, final double j, final int hv) {
		return (int) i + (int) j * (hv + 1);
	}
}
