/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * @author FVH
 *
 */
public class WB_Subdivision {
	private static WB_GeometryFactory gf = new WB_GeometryFactory();

	public static WB_SubdivisionResult subdivide(final WB_Triangle triangle, final WB_TriangleSubdivision rule) {
		return rule.apply(triangle);
	}

	public static WB_SubdivisionResult subdivide(final WB_Quad quad, final WB_QuadSubdivision rule) {
		return rule.apply(quad);
	}

	public static WB_SubdivisionResult subdivide(final WB_Pentagon pentagon, final WB_PentagonSubdivision rule) {
		return rule.apply(pentagon);
	}

	public static WB_SubdivisionResult subdivide(final WB_Hexagon hexagon, final WB_HexagonSubdivision rule) {
		return rule.apply(hexagon);
	}

	public static WB_SubdivisionResult subdivide(final WB_Octagon octagon, final WB_OctagonSubdivision rule) {
		return rule.apply(octagon);
	}

	public static interface WB_TriangleSubdivision {
		public WB_SubdivisionResult apply(WB_Triangle triangle);

	}

	public static class T00 implements WB_TriangleSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			result.addTriangle(p1, p2, p3);
			return result;
		}

	}

	public static class T01 implements WB_TriangleSubdivision {
		private int n = 2;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point[] q = new WB_Point[n - 1];
			double f = 1.0 / n;
			for (int i = 0; i < n - 1; i++) {
				q[i] = WB_Point.interpolate(p2, p3, f);
				f += 1.0 / n;
			}
			result.addTriangle(p1, q[0], p3);
			for (int i = 1; i < n - 2; i++) {
				result.addTriangle(p1, q[i], q[i - 1]);
			}
			result.addTriangle(p1, p2, q[n - 2]);
			return result;
		}

		public T01 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 2;
			}
			return this;
		}
	}

	public static class T02 implements WB_TriangleSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q = gf.createCentroid(triangle);
			result.addTriangle(q, p2, p3);
			result.addTriangle(q, p3, p1);
			result.addTriangle(q, p1, p2);
			return result;
		}

	}

	public static class T03 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, f);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q31 = WB_Point.interpolate(p1, p3, f);
			result.addTriangle(p1, q12, q31);
			result.addTriangle(p2, q23, q12);
			result.addTriangle(p3, q31, q23);
			result.addTriangle(q23, q31, q12);
			return result;
		}

		public T03 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}

	}

	public static class T04 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, f);
			WB_Point q31 = WB_Point.interpolate(p1, p3, f);
			result.addTriangle(p1, q12, q31);
			result.addQuad(q31, q12, p2, p3);
			return result;
		}

		public T04 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}

	}

	public static class T05 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, f);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q31 = WB_Point.interpolate(p1, p3, f);
			result.addQuad(p1, q12, q23, q31);
			result.addTriangle(q12, p2, q23);
			result.addTriangle(q31, q23, p3);
			return result;
		}

		public T05 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class T06 implements WB_TriangleSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q31 = WB_Point.interpolate(p3, p1, 0.5);
			WB_Point q = gf.createCentroid(triangle);
			result.addQuad(p1, q12, q, q31);
			result.addQuad(p2, q23, q, q12);
			result.addQuad(p3, q31, q, q23);
			return result;
		}
	}

	public static class T07 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q31 = WB_Point.interpolate(p3, p1, 0.5);
			WB_Point q = gf.createCentroid(triangle);
			q12 = WB_Point.interpolate(q, q12, f);
			q23 = WB_Point.interpolate(q, q23, f);
			q31 = WB_Point.interpolate(q, q31, f);
			result.addQuad(p1, q12, q, q31);
			result.addQuad(p2, q23, q, q12);
			result.addQuad(p3, q31, q, q23);
			result.addTriangle(q23, p2, p3);
			result.addTriangle(q31, p3, p1);
			result.addTriangle(q12, p1, p2);
			return result;
		}

		public T07 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class T08 implements WB_TriangleSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q31 = WB_Point.interpolate(p3, p1, 0.5);
			WB_Point q = gf.createCentroid(triangle);
			result.addTriangle(p1, q, q31);
			result.addTriangle(p1, q12, q);
			result.addTriangle(p2, q, q12);
			result.addTriangle(p2, q23, q);
			result.addTriangle(p3, q, q23);
			result.addTriangle(p3, q31, q);
			return result;
		}
	}

	public static class T09 implements WB_TriangleSubdivision {
		private double f = 1.0 / 3.0;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12a = WB_Point.interpolate(p1, p2, f);
			WB_Point q12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point q31a = WB_Point.interpolate(p3, p1, f);
			WB_Point q31b = WB_Point.interpolate(p3, p1, 1.0 - f);
			WB_Point q = gf.createCentroid(triangle);

			result.addQuad(p1, q12a, q, q31b);
			result.addQuad(p2, q23a, q, q12b);
			result.addQuad(p3, q31a, q, q23b);
			result.addTriangle(q, q23a, q23b);
			result.addTriangle(q, q31a, q31b);
			result.addTriangle(q, q12a, q12b);
			return result;
		}

		public T09 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}
	}

	public static class T10 implements WB_TriangleSubdivision {
		private double f = 1.0 / 3.0;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();

			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			result.addTriangle(p1, q23b, p3);
			result.addTriangle(p1, q23a, q23b);
			result.addTriangle(p1, p2, q23a);
			return result;
		}

		public T10 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}

	}

	public static class T11 implements WB_TriangleSubdivision {
		private double f = 1.0 / 3.0;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12a = WB_Point.interpolate(p1, p2, f);
			WB_Point q12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point q31a = WB_Point.interpolate(p3, p1, f);
			WB_Point q31b = WB_Point.interpolate(p3, p1, 1.0 - f);
			result.addTriangle(p1, q12a, q31b);
			result.addTriangle(p2, q23a, q12b);
			result.addTriangle(p3, q31a, q23b);
			result.addHexagon(q31b, q12a, q12b, q23a, q23b, q31a);

			return result;
		}

		public T11 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}
	}

	public static class T12 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point q12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q31 = WB_Point.interpolate(p3, p1, 0.5);
			WB_Point q = gf.createCentroid(triangle);
			q12 = WB_Point.interpolate(q, q12, f);
			q23 = WB_Point.interpolate(q, q23, f);
			q31 = WB_Point.interpolate(q, q31, f);
			result.addTriangle(p1, q12, q31);
			result.addTriangle(p2, q23, q12);
			result.addTriangle(p3, q31, q23);
			result.addTriangle(q23, p2, p3);
			result.addTriangle(q31, p3, p1);
			result.addTriangle(q12, p1, p2);
			result.addTriangle(q23, q31, q12);
			return result;
		}

		public T12 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class T13 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p31 = WB_Point.interpolate(p3, p1, 0.5);
			WB_Point q = gf.createCentroid(triangle);
			WB_Point q12 = WB_Point.interpolate(q, p12, f);
			WB_Point q23 = WB_Point.interpolate(q, p23, f);
			WB_Point q31 = WB_Point.interpolate(q, p31, f);
			result.addPentagon(p1, p12, q12, q31, p31);
			result.addPentagon(p2, p23, q23, q12, p12);
			result.addPentagon(p3, p31, q31, q23, p23);
			result.addTriangle(q23, q31, q12);
			return result;
		}

		public T13 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class T14 implements WB_TriangleSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Triangle triangle) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = triangle.p1();
			WB_Coord p2 = triangle.p2();
			WB_Coord p3 = triangle.p3();

			WB_Point q = gf.createCentroid(triangle);
			WB_Point q1 = WB_Point.interpolate(q, p1, f);
			WB_Point q2 = WB_Point.interpolate(q, p2, f);
			WB_Point q3 = WB_Point.interpolate(q, p3, f);
			result.addTriangle(q1, q2, q3);
			result.addQuad(p1, p2, q2, q1);
			result.addQuad(p2, p3, q3, q2);
			result.addQuad(p3, p1, q1, q3);
			return result;
		}

		public T14 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static WB_TriangleSubdivision[] TSubs = new WB_TriangleSubdivision[] { new T01(), new T02(), new T03(),
			new T04(), new T05(), new T06(), new T07(), new T08(), new T09(), new T10(), new T11(), new T12(),
			new T13(), new T14() };

	public static interface WB_QuadSubdivision {
		public WB_SubdivisionResult apply(WB_Quad quad);

	}

	public static class Q01 implements WB_QuadSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p41 = WB_Point.interpolate(p1, p4, 0.5);
			result.addQuad(p12, p23, p34, p41);
			result.addTriangle(p1, p12, p41);
			result.addTriangle(p2, p23, p12);
			result.addTriangle(p3, p34, p23);
			result.addTriangle(p4, p41, p34);
			return result;
		}

	}

	public static class Q02 implements WB_QuadSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			result.addQuad(q1, q2, q3, q4);
			result.addQuad(p1, p2, q2, q1);
			result.addQuad(p2, p3, q3, q2);
			result.addQuad(p3, p4, q4, q3);
			result.addQuad(p4, p1, q1, q4);
			return result;
		}

		public Q02 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}

	}

	public static class Q03 implements WB_QuadSubdivision {
		private int n = 3;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;

			WB_Point[] q12 = new WB_Point[n - 1];
			WB_Point[] q13 = new WB_Point[n - 1];
			WB_Point[] q14 = new WB_Point[n - 1];

			double f = 1.0 / n;
			for (int i = 0; i < n - 1; i++) {
				q12[i] = WB_Point.interpolate(p1, p2, f);
				q13[i] = WB_Point.interpolate(p1, p3, f);
				q14[i] = WB_Point.interpolate(p1, p4, f);
				f += 1.0 / n;
			}
			result.addTriangle(p1, q14[0], q13[0]);
			result.addTriangle(p1, q12[0], q13[0]);
			for (int i = 0; i < n - 2; i++) {
				result.addQuad(q13[i], q13[i + 1], q14[i + 1], q14[i]);
				result.addQuad(q13[i], q12[i], q12[i + 1], q13[i + 1]);
			}
			result.addQuad(q13[n - 2], p3, p4, q14[n - 2]);
			result.addQuad(q13[n - 2], q12[n - 2], p2, p3);
			return result;
		}

		public Q03 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 3;
			}
			return this;
		}

	}

	public static class Q04 implements WB_QuadSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point q41 = WB_Point.interpolate(p1, p4, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			result.addTriangle(p1, q1, q41);
			result.addTriangle(p1, q12, q1);
			result.addQuad(q, q41, q1, q12);
			result.addTriangle(p2, q2, q12);
			result.addTriangle(p2, q23, q2);
			result.addQuad(q, q12, q2, q23);
			result.addTriangle(p3, q3, q23);
			result.addTriangle(p3, q34, q3);
			result.addQuad(q, q23, q3, q34);
			result.addTriangle(p4, q4, q34);
			result.addTriangle(p4, q41, q4);
			result.addQuad(q, q34, q4, q41);
			return result;
		}

		public Q04 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class Q05 implements WB_QuadSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point q23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point q34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point q41 = WB_Point.interpolate(p1, p4, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);

			result.addTriangle(p1, q, q41);
			result.addTriangle(p1, q12, q);

			result.addTriangle(p2, q, q12);
			result.addTriangle(p2, q23, q);

			result.addTriangle(p3, q, q23);
			result.addTriangle(p3, q34, q);

			result.addTriangle(p4, q, q34);
			result.addTriangle(p4, q41, q);

			return result;
		}

	}

	public static class Q06 implements WB_QuadSubdivision {
		private double f = 1.0 / 3.0;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12a = WB_Point.interpolate(p1, p2, f);
			WB_Point q12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point q34a = WB_Point.interpolate(p3, p4, f);
			WB_Point q34b = WB_Point.interpolate(p3, p4, 1.0 - f);
			WB_Point q41a = WB_Point.interpolate(p1, p4, f);
			WB_Point q41b = WB_Point.interpolate(p1, p4, 1.0 - f);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			result.addQuad(p1, q12a, q, q41a);
			result.addQuad(p2, q23a, q, q12b);
			result.addQuad(p3, q34a, q, q23b);
			result.addQuad(p4, q41b, q, q34b);
			result.addTriangle(q, q41b, q41a);
			result.addTriangle(q, q12a, q12b);
			result.addTriangle(q, q23a, q23b);
			result.addTriangle(q, q34a, q34b);
			return result;
		}

		public Q06 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}
	}

	public static class Q07 implements WB_QuadSubdivision {
		private double f = 1.0 / 3.0;
		private double g = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12a = WB_Point.interpolate(p1, p2, f);
			WB_Point q12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point q34a = WB_Point.interpolate(p3, p4, f);
			WB_Point q34b = WB_Point.interpolate(p3, p4, 1.0 - f);
			WB_Point q41a = WB_Point.interpolate(p1, p4, f);
			WB_Point q41b = WB_Point.interpolate(p1, p4, 1.0 - f);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, g);
			WB_Point q2 = WB_Point.interpolate(p2, q, g);
			WB_Point q3 = WB_Point.interpolate(p3, q, g);
			WB_Point q4 = WB_Point.interpolate(p4, q, g);

			result.addQuad(p1, q12a, q1, q41a);
			result.addQuad(p2, q23a, q2, q12b);
			result.addQuad(p3, q34a, q3, q23b);
			result.addQuad(p4, q41b, q4, q34b);
			result.addPentagon(q4, q41b, q41a, q1, q);
			result.addPentagon(q1, q12a, q12b, q2, q);
			result.addPentagon(q2, q23a, q23b, q3, q);
			result.addPentagon(q3, q34a, q34b, q4, q);

			return result;
		}

		public Q07 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}

		public Q07 setG(final double gg) {
			if (gg > 0 && gg < 1.0) {
				g = gg;
			} else {
				g = 0.5;
			}
			return this;
		}
	}

	public static class Q08 implements WB_QuadSubdivision {
		private double f = 1.0 / 3.0;
		private double g = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12a = WB_Point.interpolate(p1, p2, f);
			WB_Point q12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point q34a = WB_Point.interpolate(p3, p4, f);
			WB_Point q34b = WB_Point.interpolate(p3, p4, 1.0 - f);
			WB_Point q41a = WB_Point.interpolate(p1, p4, f);
			WB_Point q41b = WB_Point.interpolate(p1, p4, 1.0 - f);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, g);
			WB_Point q2 = WB_Point.interpolate(p2, q, g);
			WB_Point q3 = WB_Point.interpolate(p3, q, g);
			WB_Point q4 = WB_Point.interpolate(p4, q, g);
			WB_Point q41 = WB_Point.interpolate(q12a, q34b, 0.5);
			WB_Point q23 = WB_Point.interpolate(q12b, q34a, 0.5);
			result.addQuad(p1, q12a, q1, q41a);
			result.addQuad(p2, q23a, q2, q12b);
			result.addQuad(p3, q34a, q3, q23b);
			result.addQuad(p4, q41b, q4, q34b);
			result.addPentagon(q41, q4, q41b, q41a, q1);
			result.addHexagon(q41, q1, q12a, q12b, q2, q23);
			result.addPentagon(q23, q2, q23a, q23b, q3);
			result.addHexagon(q23, q3, q34a, q34b, q4, q41);
			return result;
		}

		public Q08 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}

		public Q08 setG(final double gg) {
			if (gg > 0 && gg < 1.0) {
				g = gg;
			} else {
				g = 0.5;
			}
			return this;
		}
	}

	public static class Q09 implements WB_QuadSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12 = WB_Point.interpolate(p1, p2, f);
			WB_Point q23 = WB_Point.interpolate(p2, p3, f);
			WB_Point q34 = WB_Point.interpolate(p3, p4, f);
			WB_Point q41 = WB_Point.interpolate(p1, p4, f);

			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);

			result.addQuad(p1, q12, q1, q41);
			result.addQuad(p2, q23, q2, q12);
			result.addQuad(p3, q34, q3, q23);
			result.addQuad(p4, q41, q4, q34);
			result.addQuad(q1, q12, q2, q);
			result.addQuad(q2, q23, q3, q);
			result.addQuad(q3, q34, q4, q);
			result.addQuad(q4, q41, q1, q);

			return result;
		}

		public Q09 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class Q10 implements WB_QuadSubdivision {
		private int n = 3;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;

			WB_Point[][] q = new WB_Point[n + 1][n + 1];

			double f = 0;
			WB_Coord a, b;
			double g;
			for (int i = 0; i <= n; i++) {
				a = WB_Point.interpolate(p1, p2, f);
				b = WB_Point.interpolate(p4, p3, f);
				g = 0;
				for (int j = 0; j <= n; j++) {
					q[i][j] = WB_Point.interpolate(a, b, g);
					if (j == n - 1) {
						g = 1.0;
					} else {
						g += 1.0 / n;
					}

				}
				if (i == n - 1) {
					f = 1.0;
				} else {
					f += 1.0 / n;
				}
			}
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					result.addQuad(q[i][j], q[i + 1][j], q[i + 1][j + 1], q[i][j + 1]);
				}
			}
			return result;
		}

		public Q10 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 3;
			}
			return this;
		}

	}

	public static class Q11 implements WB_QuadSubdivision {
		private double f = 1.0 / 3.0;
		private double g = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point p12a = WB_Point.interpolate(p1, p2, f);
			WB_Point p12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point p23a = WB_Point.interpolate(p2, p3, f);
			WB_Point p23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point p34a = WB_Point.interpolate(p3, p4, f);
			WB_Point p34b = WB_Point.interpolate(p3, p4, 1.0 - f);
			WB_Point p41a = WB_Point.interpolate(p1, p4, f);
			WB_Point p41b = WB_Point.interpolate(p1, p4, 1.0 - f);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, g);
			WB_Point q2 = WB_Point.interpolate(p2, q, g);
			WB_Point q3 = WB_Point.interpolate(p3, q, g);
			WB_Point q4 = WB_Point.interpolate(p4, q, g);
			WB_Point q12a = WB_Point.interpolate(q1, q2, f);
			WB_Point q12b = WB_Point.interpolate(q1, q2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(q2, q3, f);
			WB_Point q23b = WB_Point.interpolate(q2, q3, 1.0 - f);
			WB_Point q34a = WB_Point.interpolate(q3, q4, f);
			WB_Point q34b = WB_Point.interpolate(q3, q4, 1.0 - f);
			WB_Point q41a = WB_Point.interpolate(q1, q4, f);
			WB_Point q41b = WB_Point.interpolate(q1, q4, 1.0 - f);

			result.addPentagon(p1, p12a, q12a, q41a, p41a);
			result.addPentagon(p2, p23a, q23a, q12b, p12b);
			result.addPentagon(p3, p34a, q34a, q23b, p23b);
			result.addPentagon(p4, p41b, q41b, q34b, p34b);

			result.addQuad(p41b, p41a, q41a, q41b);
			result.addQuad(p12a, p12b, q12b, q12a);
			result.addQuad(p23a, p23b, q23b, q23a);
			result.addQuad(p34a, p34b, q34b, q34a);

			result.addOctagon(q41a, q12a, q12b, q23a, q23b, q34a, q34b, q41b);

			return result;
		}

		public Q11 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}
	}

	public static class Q12 implements WB_QuadSubdivision {
		private double f = 1.0 / 3.0;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12a = WB_Point.interpolate(p1, p2, f);
			WB_Point q12b = WB_Point.interpolate(p1, p2, 1.0 - f);
			WB_Point q23a = WB_Point.interpolate(p2, p3, f);
			WB_Point q23b = WB_Point.interpolate(p2, p3, 1.0 - f);
			WB_Point q34a = WB_Point.interpolate(p3, p4, f);
			WB_Point q34b = WB_Point.interpolate(p3, p4, 1.0 - f);
			WB_Point q41a = WB_Point.interpolate(p1, p4, f);
			WB_Point q41b = WB_Point.interpolate(p1, p4, 1.0 - f);

			result.addOctagon(q41a, q12a, q12b, q23a, q23b, q34a, q34b, q41b);
			result.addTriangle(p1, q12a, q41a);
			result.addTriangle(p2, q23a, q12b);
			result.addTriangle(p3, q34a, q23b);
			result.addTriangle(p4, q41b, q34b);
			return result;
		}

		public Q12 setF(final double ff) {
			if (ff > 0 && ff < 0.5) {
				f = ff;
			} else {
				f = 1.0 / 3.0;
			}
			return this;
		}
	}

	public static class Q13 implements WB_QuadSubdivision {
		private int n = 3;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;

			WB_Point[][] q = new WB_Point[n + 1][2];

			double f = 0;
			WB_Coord a, b;
			double g;
			for (int i = 0; i <= n; i++) {
				a = WB_Point.interpolate(p1, p2, f);
				b = WB_Point.interpolate(p4, p3, f);
				g = 0;
				for (int j = 0; j < 2; j++) {
					q[i][j] = WB_Point.interpolate(a, b, g);
					g = 1.0;
				}
				if (i == n - 1) {
					f = 1.0;
				} else {
					f += 1.0 / n;
				}
			}
			for (int i = 0; i < n; i++) {
				result.addQuad(q[i][0], q[i + 1][0], q[i + 1][1], q[i][1]);
			}
			return result;
		}

		public Q13 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 3;
			}
			return this;
		}

	}

	public static class Q14 implements WB_QuadSubdivision {
		private int n = 3;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;

			WB_Point[] q12 = new WB_Point[n - 1];
			WB_Point[] q14 = new WB_Point[n - 1];

			double f = 1.0 / n;
			for (int i = 0; i < n - 1; i++) {
				q12[i] = WB_Point.interpolate(p1, p2, f);
				q14[i] = WB_Point.interpolate(p1, p4, f);
				f += 1.0 / n;
			}
			result.addTriangle(p1, q12[0], q14[0]);
			for (int i = 0; i < n - 2; i++) {
				result.addQuad(q12[i], q12[i + 1], q14[i + 1], q14[i]);
			}
			result.addQuad(q12[n - 2], p2, p4, q14[n - 2]);
			result.addTriangle(p4, p2, p3);
			return result;
		}

		public Q14 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 3;
			}
			return this;
		}

	}

	public static class Q15 implements WB_QuadSubdivision {
		private int n = 3;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;

			WB_Point[] q12 = new WB_Point[n - 1];
			WB_Point[] q14 = new WB_Point[n - 1];
			WB_Point[] q32 = new WB_Point[n - 1];
			WB_Point[] q34 = new WB_Point[n - 1];
			double f = 1.0 / n;
			for (int i = 0; i < n - 1; i++) {
				q12[i] = WB_Point.interpolate(p1, p2, f);
				q14[i] = WB_Point.interpolate(p1, p4, f);
				q32[i] = WB_Point.interpolate(p3, p2, f);
				q34[i] = WB_Point.interpolate(p3, p4, f);
				f += 1.0 / n;
			}
			result.addTriangle(p1, q12[0], q14[0]);
			result.addTriangle(p3, q32[0], q34[0]);
			for (int i = 0; i < n - 2; i++) {
				result.addQuad(q12[i], q12[i + 1], q14[i + 1], q14[i]);
				result.addQuad(q32[i + 1], q32[i], q34[i], q34[i + 1]);
			}
			result.addQuad(q12[n - 2], p2, p4, q14[n - 2]);
			result.addQuad(p2, q32[n - 2], q34[n - 2], p4);
			return result;
		}

		public Q15 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 3;
			}
			return this;
		}

	}

	public static class Q16 implements WB_QuadSubdivision {
		private int n = 3;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;

			WB_Point[] q32 = new WB_Point[n - 1];
			WB_Point[] q34 = new WB_Point[n - 1];
			double f = 1.0 / n;
			for (int i = 0; i < n - 1; i++) {
				q32[i] = WB_Point.interpolate(p3, p2, f);
				q34[i] = WB_Point.interpolate(p3, p4, f);
				f += 1.0 / n;
			}
			result.addQuad(p1, q32[0], p3, q34[0]);
			for (int i = 0; i < n - 2; i++) {
				result.addTriangle(p1, q32[i], q32[i + 1]);
				result.addTriangle(p1, q34[i], q34[i + 1]);
			}
			result.addTriangle(p1, q32[n - 2], p2);
			result.addTriangle(p1, q34[n - 2], p4);
			return result;
		}

		public Q16 setN(final int nn) {
			if (nn > 0) {
				n = nn;
			} else {
				n = 3;
			}
			return this;
		}

	}

	public static class Q17 implements WB_QuadSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Quad quad) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = quad.p1;
			WB_Coord p2 = quad.p2;
			WB_Coord p3 = quad.p3;
			WB_Coord p4 = quad.p4;
			WB_Point q12 = WB_Point.interpolate(p1, p2, f);
			WB_Point q23 = WB_Point.interpolate(p2, p3, f);
			WB_Point q34 = WB_Point.interpolate(p3, p4, f);
			WB_Point q41 = WB_Point.interpolate(p1, p4, f);

			WB_Coord q = gf.createCentroid(p1, p2, p3, p4);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);

			result.addTriangle(p1, q12, q1);
			result.addTriangle(p1, q1, q41);
			result.addTriangle(p2, q2, q12);
			result.addTriangle(p2, q23, q2);
			result.addTriangle(p3, q3, q23);
			result.addTriangle(p3, q34, q3);
			result.addTriangle(p4, q4, q34);
			result.addTriangle(p4, q41, q4);
			result.addQuad(q1, q12, q2, q);
			result.addQuad(q2, q23, q3, q);
			result.addQuad(q3, q34, q4, q);
			result.addQuad(q4, q41, q1, q);

			return result;
		}

		public Q17 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static WB_QuadSubdivision[] QSubs = new WB_QuadSubdivision[] { new Q01(), new Q02(), new Q03(), new Q04(),
			new Q05(), new Q06(), new Q07(), new Q08(), new Q09(), new Q10(), new Q11(), new Q12(), new Q13(),
			new Q14(), new Q15(), new Q16(), new Q17() };

	public static interface WB_PentagonSubdivision {
		public WB_SubdivisionResult apply(WB_Pentagon pentagon);

	}

	public static class P01 implements WB_PentagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p51 = WB_Point.interpolate(p5, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q51 = WB_Point.interpolate(p51, q, f);
			result.addPentagon(p1, p12, q12, q51, p51);
			result.addPentagon(p2, p23, q23, q12, p12);
			result.addPentagon(p3, p34, q34, q23, p23);
			result.addPentagon(p4, p45, q45, q34, p34);
			result.addPentagon(p5, p51, q51, q45, p45);
			result.addPentagon(q34, q23, q12, q51, q45);
			return result;
		}

		public P01 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class P02 implements WB_PentagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p51 = WB_Point.interpolate(p5, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			result.addQuad(p1, p12, q, p51);
			result.addQuad(p2, p23, q, p12);
			result.addQuad(p3, p34, q, p23);
			result.addQuad(p4, p45, q, p34);
			result.addQuad(p5, p51, q, p45);
			return result;
		}
	}

	public static class P03 implements WB_PentagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p51 = WB_Point.interpolate(p5, p1, 0.5);
			result.addTriangle(p1, p12, p51);
			result.addTriangle(p2, p23, p12);
			result.addTriangle(p3, p34, p23);
			result.addTriangle(p4, p45, p34);
			result.addTriangle(p5, p51, p45);
			result.addPentagon(p34, p45, p51, p12, p23);
			return result;
		}
	}

	public static class P04 implements WB_PentagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p51 = WB_Point.interpolate(p5, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			result.addQuad(p1, p12, q1, p51);
			result.addQuad(p2, p23, q2, p12);
			result.addQuad(p3, p34, q3, p23);
			result.addQuad(p4, p45, q4, p34);
			result.addQuad(p5, p51, q5, p45);
			result.addQuad(p34, q4, q, q3);
			result.addQuad(p45, q5, q, q4);
			result.addQuad(p51, q1, q, q5);
			result.addQuad(p12, q2, q, q1);
			result.addQuad(p23, q3, q, q2);
			return result;
		}

		public P04 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class P05 implements WB_PentagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			result.addQuad(p1, q, p45, p5);
			result.addQuad(p1, p2, p23, q);
			result.addPentagon(q, p23, p3, p4, p45);
			return result;
		}
	}

	public static class P06 implements WB_PentagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p51 = WB_Point.interpolate(p5, p1, 0.5);

			result.addTriangle(p34, p51, p1);
			result.addTriangle(p34, p1, p12);
			result.addTriangle(p34, p5, p51);
			result.addTriangle(p34, p12, p2);
			result.addTriangle(p34, p4, p5);
			result.addTriangle(p34, p2, p3);

			return result;
		}
	}

	public static class P07 implements WB_PentagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			result.addTriangle(q, p3, p4);
			result.addTriangle(q, p4, p5);
			result.addTriangle(q, p5, p1);
			result.addTriangle(q, p1, p2);
			result.addTriangle(q, p2, p3);

			return result;
		}
	}

	public static class P08 implements WB_PentagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			result.addQuad(p3, p4, q4, q3);
			result.addQuad(p4, p5, q5, q4);
			result.addQuad(p5, p1, q1, q5);
			result.addQuad(p1, p2, q2, q1);
			result.addQuad(p2, p3, q3, q2);
			result.addPentagon(q1, q2, q3, q4, q5);
			return result;
		}

		public P08 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class P09 implements WB_PentagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;

			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p51 = WB_Point.interpolate(p5, p1, 0.5);
			WB_Point.interpolate(p1, q, f);
			WB_Point.interpolate(p2, q, f);
			WB_Point.interpolate(p3, q, f);
			WB_Point.interpolate(p4, q, f);
			WB_Point.interpolate(p5, q, f);

			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q51 = WB_Point.interpolate(p51, q, f);

			result.addQuad(p1, q12, q, q51);
			result.addQuad(p2, q23, q, q12);
			result.addQuad(p3, q34, q, q23);
			result.addQuad(p4, q45, q, q34);
			result.addQuad(p5, q51, q, q45);

			result.addTriangle(q51, p5, p1);
			result.addTriangle(q12, p1, p2);
			result.addTriangle(q23, p2, p3);
			result.addTriangle(q34, p3, p4);
			result.addTriangle(q45, p4, p5);

			return result;
		}

		public P09 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class P10 implements WB_PentagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;

			result.addQuad(p2, p3, p4, p5);

			result.addTriangle(p1, p2, p5);

			return result;
		}

	}

	public static class P11 implements WB_PentagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Pentagon pentagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = pentagon.p1;
			WB_Coord p2 = pentagon.p2;
			WB_Coord p3 = pentagon.p3;
			WB_Coord p4 = pentagon.p4;
			WB_Coord p5 = pentagon.p5;

			result.addTriangle(p1, p2, p3);
			result.addTriangle(p1, p3, p4);
			result.addTriangle(p1, p4, p5);
			return result;
		}

	}

	public static WB_PentagonSubdivision[] PSubs = new WB_PentagonSubdivision[] { new P01(), new P02(), new P03(),
			new P04(), new P05(), new P06(), new P07(), new P08(), new P09(), new P10(), new P11() };

	public static interface WB_HexagonSubdivision {
		public WB_SubdivisionResult apply(WB_Hexagon hexagon);

	}

	public static class H01 implements WB_HexagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p61 = WB_Point.interpolate(p6, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q61 = WB_Point.interpolate(p61, q, f);
			result.addPentagon(p1, p12, q12, q61, p61);
			result.addPentagon(p2, p23, q23, q12, p12);
			result.addPentagon(p3, p34, q34, q23, p23);
			result.addPentagon(p4, p45, q45, q34, p34);
			result.addPentagon(p5, p56, q56, q45, p45);
			result.addPentagon(p6, p61, q61, q56, p56);
			result.addHexagon(q56, q61, q12, q23, q34, q45);
			return result;
		}

		public H01 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class H02 implements WB_HexagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p61 = WB_Point.interpolate(p6, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			result.addQuad(p1, p12, q, p61);
			result.addQuad(p2, p23, q, p12);
			result.addQuad(p3, p34, q, p23);
			result.addQuad(p4, p45, q, p34);
			result.addQuad(p5, p56, q, p45);
			result.addQuad(p6, p61, q, p56);
			return result;
		}
	}

	public static class H03 implements WB_HexagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p61 = WB_Point.interpolate(p6, p1, 0.5);
			result.addTriangle(p1, p12, p61);
			result.addTriangle(p2, p23, p12);
			result.addTriangle(p3, p34, p23);
			result.addTriangle(p4, p45, p34);
			result.addTriangle(p5, p56, p45);
			result.addTriangle(p6, p61, p56);
			result.addHexagon(p56, p61, p12, p23, p34, p45);
			return result;
		}
	}

	public static class H04 implements WB_HexagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p61 = WB_Point.interpolate(p6, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			WB_Point q6 = WB_Point.interpolate(p6, q, f);
			result.addQuad(p1, p12, q1, p61);
			result.addQuad(p2, p23, q2, p12);
			result.addQuad(p3, p34, q3, p23);
			result.addQuad(p4, p45, q4, p34);
			result.addQuad(p5, p56, q5, p45);
			result.addQuad(p6, p61, q6, p56);
			result.addQuad(p61, q1, q, q6);
			result.addQuad(p12, q2, q, q1);
			result.addQuad(p23, q3, q, q2);
			result.addQuad(p34, q4, q, q3);
			result.addQuad(p45, q5, q, q4);
			result.addQuad(p56, q6, q, q5);
			return result;
		}

		public H04 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class H05 implements WB_HexagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			result.addPentagon(q, p12, p2, p3, p34);
			result.addPentagon(q, p34, p4, p5, p56);
			result.addPentagon(q, p56, p6, p1, p12);
			return result;
		}
	}

	public static class H06 implements WB_HexagonSubdivision {
		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);

			result.addTriangle(q, p6, p1);
			result.addTriangle(q, p1, p2);
			result.addTriangle(q, p2, p3);
			result.addTriangle(q, p3, p4);
			result.addTriangle(q, p4, p5);
			result.addTriangle(q, p5, p6);
			return result;
		}
	}

	public static class H07 implements WB_HexagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p61 = WB_Point.interpolate(p6, p1, 0.5);
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			WB_Point q6 = WB_Point.interpolate(p6, q, f);
			result.addQuad(p1, p12, q1, p61);
			result.addQuad(p2, p23, q2, p12);
			result.addQuad(p3, p34, q3, p23);
			result.addQuad(p4, p45, q4, p34);
			result.addQuad(p5, p56, q5, p45);
			result.addQuad(p6, p61, q6, p56);
			result.addQuad(p61, q1, q, q6);
			result.addQuad(p12, q2, q, q1);
			result.addQuad(p23, q3, q, q2);
			result.addQuad(p34, q4, q, q3);
			result.addQuad(p45, q5, q, q4);
			result.addQuad(p56, q6, q, q5);
			return result;
		}

		public H07 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class H08 implements WB_HexagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			WB_Point q6 = WB_Point.interpolate(p6, q, f);
			result.addQuad(p6, p1, q1, q6);
			result.addQuad(p1, p2, q2, q1);
			result.addQuad(p2, p3, q3, q2);
			result.addQuad(p3, p4, q4, q3);
			result.addQuad(p4, p5, q5, q4);
			result.addQuad(p5, p6, q6, q5);
			result.addHexagon(q1, q2, q3, q4, q5, q6);
			return result;
		}

		public H08 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class H09 implements WB_HexagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;

			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p61 = WB_Point.interpolate(p6, p1, 0.5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q61 = WB_Point.interpolate(p61, q, f);

			result.addQuad(p1, q12, q, q61);
			result.addQuad(p2, q23, q, q12);
			result.addQuad(p3, q34, q, q23);
			result.addQuad(p4, q45, q, q34);
			result.addQuad(p5, q56, q, q45);
			result.addQuad(p6, q61, q, q56);

			result.addTriangle(q61, p6, p1);
			result.addTriangle(q12, p1, p2);
			result.addTriangle(q23, p2, p3);
			result.addTriangle(q34, p3, p4);
			result.addTriangle(q45, p4, p5);
			result.addTriangle(q56, p5, p6);

			return result;
		}

		public H09 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class H10 implements WB_HexagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;

			result.addQuad(p2, p3, p5, p6);

			result.addTriangle(p1, p2, p6);
			result.addTriangle(p3, p4, p5);

			return result;
		}

	}

	public static class H11 implements WB_HexagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Hexagon hexagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = hexagon.p1;
			WB_Coord p2 = hexagon.p2;
			WB_Coord p3 = hexagon.p3;
			WB_Coord p4 = hexagon.p4;
			WB_Coord p5 = hexagon.p5;
			WB_Coord p6 = hexagon.p6;

			result.addQuad(p1, p2, p3, p4);
			result.addQuad(p1, p4, p5, p6);

			return result;
		}

	}

	public static WB_HexagonSubdivision[] HSubs = new WB_HexagonSubdivision[] { new H01(), new H02(), new H03(),
			new H04(), new H05(), new H06(), new H07(), new H08(), new H09(), new H10(), new H11() };

	public static interface WB_OctagonSubdivision {
		public WB_SubdivisionResult apply(WB_Octagon octagon);

	}

	public static class O01 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q67 = WB_Point.interpolate(p67, q, f);
			WB_Point q81 = WB_Point.interpolate(p81, q, f);
			result.addQuad(p1, p2, q23, q81);
			result.addQuad(p3, p4, q45, q23);
			result.addQuad(p5, p6, q67, q45);
			result.addQuad(p7, p8, q81, q67);
			result.addTriangle(q81, p8, p1);
			result.addTriangle(q23, p2, p3);
			result.addTriangle(q45, p4, p5);
			result.addTriangle(q67, p6, p7);
			result.addQuad(q81, q23, q45, q67);
			return result;
		}

		public O01 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O02 implements WB_OctagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);

			result.addPentagon(q, p78, p8, p1, p12);
			result.addPentagon(q, p12, p2, p3, p34);
			result.addPentagon(q, p34, p4, p5, p56);
			result.addPentagon(q, p56, p6, p7, p78);

			return result;
		}

	}

	public static class O03 implements WB_OctagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			result.addTriangle(q, p8, p1);
			result.addTriangle(q, p1, p2);
			result.addTriangle(q, p2, p3);
			result.addTriangle(q, p3, p4);
			result.addTriangle(q, p4, p5);
			result.addTriangle(q, p5, p6);
			result.addTriangle(q, p6, p7);
			result.addTriangle(q, p7, p8);
			return result;
		}
	}

	public static class O04 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q67 = WB_Point.interpolate(p67, q, f);
			WB_Point q78 = WB_Point.interpolate(p78, q, f);
			WB_Point q81 = WB_Point.interpolate(p81, q, f);
			result.addQuad(p1, q12, q, q81);
			result.addQuad(p2, q23, q, q12);
			result.addQuad(p3, q34, q, q23);
			result.addQuad(p4, q45, q, q34);
			result.addQuad(p5, q56, q, q45);
			result.addQuad(p6, q67, q, q56);
			result.addQuad(p7, q78, q, q67);
			result.addQuad(p8, q81, q, q78);
			result.addTriangle(q81, p8, p1);
			result.addTriangle(q12, p1, p2);
			result.addTriangle(q23, p2, p3);
			result.addTriangle(q34, p3, p4);
			result.addTriangle(q45, p4, p5);
			result.addTriangle(q56, p5, p6);
			result.addTriangle(q67, p6, p7);
			result.addTriangle(q78, p7, p8);

			return result;
		}

		public O04 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O05 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			WB_Point q6 = WB_Point.interpolate(p6, q, f);
			WB_Point q7 = WB_Point.interpolate(p7, q, f);
			WB_Point q8 = WB_Point.interpolate(p8, q, f);

			result.addQuad(p1, p12, q1, p81);
			result.addQuad(p2, p23, q2, p12);
			result.addQuad(p3, p34, q3, p23);
			result.addQuad(p4, p45, q4, p34);
			result.addQuad(p5, p56, q5, p45);
			result.addQuad(p6, p67, q6, p56);
			result.addQuad(p7, p78, q7, p67);
			result.addQuad(p8, p81, q8, p78);
			result.addQuad(p81, q1, q, q8);
			result.addQuad(p12, q2, q, q1);
			result.addQuad(p23, q3, q, q2);
			result.addQuad(p34, q4, q, q3);
			result.addQuad(p45, q5, q, q4);
			result.addQuad(p56, q6, q, q5);
			result.addQuad(p67, q7, q, q6);
			result.addQuad(p78, q8, q, q7);

			return result;
		}

		public O05 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O06 implements WB_OctagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);

			result.addQuad(p1, p12, q, p81);
			result.addQuad(p2, p23, q, p12);
			result.addQuad(p3, p34, q, p23);
			result.addQuad(p4, p45, q, p34);
			result.addQuad(p5, p56, q, p45);
			result.addQuad(p6, p67, q, p56);
			result.addQuad(p7, p78, q, p67);
			result.addQuad(p8, p81, q, p78);

			return result;
		}
	}

	public static class O07 implements WB_OctagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);

			result.addTriangle(p1, p12, p81);
			result.addTriangle(p2, p23, p12);
			result.addTriangle(p3, p34, p23);
			result.addTriangle(p4, p45, p34);
			result.addTriangle(p5, p56, p45);
			result.addTriangle(p6, p67, p56);
			result.addTriangle(p7, p78, p67);
			result.addTriangle(p8, p81, p78);
			result.addTriangle(q, p12, p81);
			result.addTriangle(q, p23, p12);
			result.addTriangle(q, p34, p23);
			result.addTriangle(q, p45, p34);
			result.addTriangle(q, p56, p45);
			result.addTriangle(q, p67, p56);
			result.addTriangle(q, p78, p67);
			result.addTriangle(q, p81, p78);

			return result;
		}
	}

	public static class O08 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q78 = WB_Point.interpolate(p78, q, f);
			result.addTriangle(q, p8, p1);
			result.addTriangle(q, p2, p3);
			result.addTriangle(q, p4, p5);
			result.addTriangle(q, p6, p7);
			result.addTriangle(q12, p1, p2);
			result.addTriangle(q, p1, q12);
			result.addTriangle(q, q12, p2);
			result.addTriangle(q34, p3, p4);
			result.addTriangle(q, p3, q34);
			result.addTriangle(q, q34, p4);
			result.addTriangle(q56, p5, p6);
			result.addTriangle(q, p5, q56);
			result.addTriangle(q, q56, p6);
			result.addTriangle(q78, p7, p8);
			result.addTriangle(q, p7, q78);
			result.addTriangle(q, q78, p8);
			return result;
		}

		public O08 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O09 implements WB_OctagonSubdivision {

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);

			result.addTriangle(p1, p12, p81);
			result.addTriangle(p2, p23, p12);
			result.addTriangle(p3, p34, p23);
			result.addTriangle(p4, p45, p34);
			result.addTriangle(p5, p56, p45);
			result.addTriangle(p6, p67, p56);
			result.addTriangle(p7, p78, p67);
			result.addTriangle(p8, p81, p78);
			result.addQuad(q, p81, p12, p23);
			result.addQuad(q, p23, p34, p45);
			result.addQuad(q, p45, p56, p67);
			result.addQuad(q, p67, p78, p81);

			return result;
		}
	}

	public static class O10 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);
			WB_Point q1 = WB_Point.interpolate(p1, q, f);
			WB_Point q2 = WB_Point.interpolate(p2, q, f);
			WB_Point q3 = WB_Point.interpolate(p3, q, f);
			WB_Point q4 = WB_Point.interpolate(p4, q, f);
			WB_Point q5 = WB_Point.interpolate(p5, q, f);
			WB_Point q6 = WB_Point.interpolate(p6, q, f);
			WB_Point q7 = WB_Point.interpolate(p7, q, f);
			WB_Point q8 = WB_Point.interpolate(p8, q, f);
			WB_Point.interpolate(p12, q, f);
			WB_Point.interpolate(p23, q, f);
			WB_Point.interpolate(p34, q, f);
			WB_Point.interpolate(p45, q, f);
			WB_Point.interpolate(p56, q, f);
			WB_Point.interpolate(p67, q, f);
			WB_Point.interpolate(p78, q, f);
			WB_Point.interpolate(p81, q, f);

			result.addQuad(p8, p1, q1, q8);
			result.addQuad(p1, p2, q2, q1);
			result.addQuad(p2, p3, q3, q2);
			result.addQuad(p3, p4, q4, q3);
			result.addQuad(p4, p5, q5, q4);
			result.addQuad(p5, p6, q6, q5);
			result.addQuad(p6, p7, q7, q6);
			result.addQuad(p7, p8, q8, q7);
			result.addOctagon(q1, q2, q3, q4, q5, q6, q7, q8);

			return result;
		}

		public O10 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O11 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);

			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q23 = WB_Point.interpolate(p23, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q45 = WB_Point.interpolate(p45, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q67 = WB_Point.interpolate(p67, q, f);
			WB_Point q78 = WB_Point.interpolate(p78, q, f);
			WB_Point q81 = WB_Point.interpolate(p81, q, f);

			result.addPentagon(p1, p12, q12, q81, p81);
			result.addPentagon(p2, p23, q23, q12, p12);
			result.addPentagon(p3, p34, q34, q23, p23);
			result.addPentagon(p4, p45, q45, q34, p34);
			result.addPentagon(p5, p56, q56, q45, p45);
			result.addPentagon(p6, p67, q67, q56, p56);
			result.addPentagon(p7, p78, q78, q67, p67);
			result.addPentagon(p8, p81, q81, q78, p78);

			result.addOctagon(q81, q12, q23, q34, q45, q56, q67, q78);

			return result;
		}

		public O11 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O12 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p23 = WB_Point.interpolate(p2, p3, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p45 = WB_Point.interpolate(p4, p5, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p67 = WB_Point.interpolate(p6, p7, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point p81 = WB_Point.interpolate(p8, p1, 0.5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q78 = WB_Point.interpolate(p78, q, f);

			result.addPentagon(q12, p81, p1, p2, p23);
			result.addPentagon(q34, p23, p3, p4, p45);
			result.addPentagon(q56, p45, p5, p6, p67);
			result.addPentagon(q78, p67, p7, p8, p81);

			result.addQuad(q, q78, p81, q12);
			result.addQuad(q, q12, p23, q34);
			result.addQuad(q, q34, p45, q56);
			result.addQuad(q, q56, p67, q78);

			return result;
		}

		public O12 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O13 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q78 = WB_Point.interpolate(p78, q, f);
			result.addPentagon(q, q78, p8, p1, q12);
			result.addPentagon(q, q12, p2, p3, q34);
			result.addPentagon(q, q34, p4, p5, q56);
			result.addPentagon(q, q56, p6, p7, q78);

			result.addTriangle(q12, p1, p2);
			result.addTriangle(q34, p3, p4);
			result.addTriangle(q56, p5, p6);
			result.addTriangle(q78, p7, p8);

			return result;
		}

		public O13 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static class O14 implements WB_OctagonSubdivision {
		private double f = 0.5;

		@Override
		public WB_SubdivisionResult apply(final WB_Octagon octagon) {
			WB_SubdivisionResult result = new WB_SubdivisionResult();
			WB_Coord p1 = octagon.p1;
			WB_Coord p2 = octagon.p2;
			WB_Coord p3 = octagon.p3;
			WB_Coord p4 = octagon.p4;
			WB_Coord p5 = octagon.p5;
			WB_Coord p6 = octagon.p6;
			WB_Coord p7 = octagon.p7;
			WB_Coord p8 = octagon.p8;
			WB_Coord q = gf.createCentroid(p1, p2, p3, p4, p5, p6, p7, p8);
			WB_Point p12 = WB_Point.interpolate(p1, p2, 0.5);
			WB_Point p34 = WB_Point.interpolate(p3, p4, 0.5);
			WB_Point p56 = WB_Point.interpolate(p5, p6, 0.5);
			WB_Point p78 = WB_Point.interpolate(p7, p8, 0.5);
			WB_Point q12 = WB_Point.interpolate(p12, q, f);
			WB_Point q34 = WB_Point.interpolate(p34, q, f);
			WB_Point q56 = WB_Point.interpolate(p56, q, f);
			WB_Point q78 = WB_Point.interpolate(p78, q, f);
			result.addQuad(p8, p1, q12, q78);
			result.addQuad(p2, p3, q34, q12);
			result.addQuad(p4, p5, q56, q34);
			result.addQuad(p6, p7, q78, q56);

			result.addTriangle(q12, p1, p2);
			result.addTriangle(q34, p3, p4);
			result.addTriangle(q56, p5, p6);
			result.addTriangle(q78, p7, p8);

			result.addTriangle(q, q78, q12);
			result.addTriangle(q, q12, q34);
			result.addTriangle(q, q34, q56);
			result.addTriangle(q, q56, q78);
			return result;
		}

		public O14 setF(final double ff) {
			if (ff > 0 && ff < 1.0) {
				f = ff;
			} else {
				f = 0.5;
			}
			return this;
		}
	}

	public static WB_OctagonSubdivision[] OSubs = new WB_OctagonSubdivision[] { new O01(), new O02(), new O03(),
			new O04(), new O05(), new O06(), new O07(), new O08(), new O09(), new O10(), new O11(), new O12(),
			new O13(), new O14() };

	public static class WB_SubdivisionResult {
		FastList<WB_Triangle> triangles;
		FastList<WB_Quad> quads;
		FastList<WB_Pentagon> pentagons;
		FastList<WB_Hexagon> hexagons;
		FastList<WB_Octagon> octagons;

		public WB_SubdivisionResult() {
			triangles = new FastList<WB_Triangle>();
			quads = new FastList<WB_Quad>();
			pentagons = new FastList<WB_Pentagon>();
			hexagons = new FastList<WB_Hexagon>();
			octagons = new FastList<WB_Octagon>();
		}

		public void addTriangle(final WB_Triangle triangle) {
			triangles.add(triangle);
		}

		public void addQuad(final WB_Quad quad) {

			if (quad.isConvex()) {
				quads.add(quad);
			} else {
				final boolean p0inside = WB_GeometryOp3D.pointInTriangleBary3D(quad.p1, quad.p2, quad.p3, quad.p4);
				final boolean p2inside = WB_GeometryOp3D.pointInTriangleBary3D(quad.p3, quad.p1, quad.p2, quad.p4);
				if (p0inside || p2inside) {
					addTriangle(quad.p1, quad.p2, quad.p3);
					addTriangle(quad.p1, quad.p3, quad.p4);
				} else {
					addTriangle(quad.p1, quad.p2, quad.p4);
					addTriangle(quad.p2, quad.p3, quad.p4);

				}

			}
		}

		public void addPentagon(final WB_Pentagon pentagon) {
			pentagons.add(pentagon);
		}

		public void addHexagon(final WB_Hexagon hexagon) {
			hexagons.add(hexagon);
		}

		public void addOctagon(final WB_Octagon octagon) {
			octagons.add(octagon);
		}

		public void addTriangle(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
			triangles.add(new WB_Triangle(p1, p2, p3));
		}

		public void addQuad(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord p4) {
			WB_Quad quad = new WB_Quad(p1, p2, p3, p4);
			addQuad(quad);
		}

		public void addPentagon(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord p4,
				final WB_Coord p5) {
			pentagons.add(new WB_Pentagon(p1, p2, p3, p4, p5));
		}

		public void addHexagon(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord p4,
				final WB_Coord p5, final WB_Coord p6) {
			hexagons.add(new WB_Hexagon(p1, p2, p3, p4, p5, p6));
		}

		public void addOctagon(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord p4,
				final WB_Coord p5, final WB_Coord p6, final WB_Coord p7, final WB_Coord p8) {
			octagons.add(new WB_Octagon(p1, p2, p3, p4, p5, p6, p7, p8));
		}

		public List<WB_Triangle> getTriangles() {
			return triangles.asUnmodifiable();
		}

		public List<WB_Quad> getQuads() {
			return quads.asUnmodifiable();
		}

		public List<WB_Pentagon> getPentagons() {
			return pentagons.asUnmodifiable();
		}

		public List<WB_Hexagon> getHexagons() {
			return hexagons.asUnmodifiable();
		}

		public List<WB_Octagon> getOctagons() {
			return octagons.asUnmodifiable();
		}

	}

}
