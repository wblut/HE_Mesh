/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.Collections;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_MTRandom;

public class WB_Danzer3D {

	public static enum Type {
		A, B, C, K
	};

	/**
	 *
	 */
	public static class WB_DanzerTile3D extends WB_Tetrahedron {

		private Type type;
		private int generation;
		private final static double a = Math.cos(1.0 / 10.0 * Math.PI);
		private final static double b = Math.cos(1.0 / 6.0 * Math.PI);
		private final static double tau = 0.5 * (1.0 + Math.sqrt(5.0));
		private final static double invtau = 1.0 / tau;

		/**
		 *
		 */
		protected WB_DanzerTile3D() {
		}

		private WB_DanzerTile3D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord p4,
				final Type type, final int generation) {
			this.p1 = geometryfactory.createPoint(p1);
			this.p2 = geometryfactory.createPoint(p2);
			this.p3 = geometryfactory.createPoint(p3);
			this.p4 = geometryfactory.createPoint(p4);
			this.type = type;
			this.generation = generation;
		}

		/**
		 *
		 */
		private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

		public WB_DanzerTile3D(final Type type, final double scale, final WB_Coord offset) {
			this(type, scale, offset, 0);
		}

		/**
		 *
		 * @param type
		 * @param scale
		 * @param offset
		 * @param generation
		 */
		WB_DanzerTile3D(final Type type, final double scale, final WB_Coord offset, final int generation) {
			p1 = new WB_Point();
			this.type = type;
			this.generation = generation;
			WB_Circle C13, C23;
			switch (type) {
			case A:
				p2 = new WB_Point(a, 0, 0);
				C13 = new WB_Circle(0, 0, a * tau);
				C23 = new WB_Circle(a, 0, b * tau);
				break;
			case B:
				p2 = new WB_Point(a, 0, 0);
				C13 = new WB_Circle(0, 0, b * tau);
				C23 = new WB_Circle(a, 0, a * tau);
				break;
			case C:
				p2 = new WB_Point(a * invtau, 0, 0);
				C13 = new WB_Circle(0, 0, tau);
				C23 = new WB_Circle(a * invtau, 0, b * tau);
				break;
			case K:
				p2 = new WB_Point(a, 0, 0);
				C13 = new WB_Circle(0, 0, a * invtau);
				C23 = new WB_Circle(a, 0, b);
				break;
			default:
				p2 = new WB_Point(a, 0, 0);
				C13 = new WB_Circle(0, 0, a * tau);
				C23 = new WB_Circle(a, 0, b * tau);
				break;

			}
			p3 = new WB_Point(WB_GeometryOp.getIntersection2D(C13, C23).get(0));

			WB_Coord[] points;
			switch (type) {
			case A:
				points = WB_GeometryOp.getFourthPoint(p1, 1.0, p2, a, p3, b);
				break;
			case B:
				points = WB_GeometryOp.getFourthPoint(p1, a * invtau, p2, b, p3, 1);
				break;
			case C:
				points = WB_GeometryOp.getFourthPoint(p1, a, p2, b, p3, a);
				break;
			case K:
				points = WB_GeometryOp.getFourthPoint(p1, 0.5, p2, 0.5 * tau, p3, 0.5 * invtau);
				break;
			default:
				points = WB_GeometryOp.getFourthPoint(p1, 1.0, p2, a, p3, b);
				break;

			}
			if (points[0].zd() >= 0) {
				p4 = new WB_Point(points[0]);
			} else {
				p4 = new WB_Point(points[1]);
			}
			p1.mulSelf(scale).addSelf(offset);
			p2.mulSelf(scale).addSelf(offset);
			p3.mulSelf(scale).addSelf(offset);
			p4.mulSelf(scale).addSelf(offset);

		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord p1() {
			return p1;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord p2() {
			return p2;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord p3() {
			return p3;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord p4() {
			return p4;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Simplex#getPoint(int)
		 */

		@Override
		public WB_Coord getPoint(final int i) {
			if (i == 0) {
				return p1;
			} else if (i == 1) {
				return p2;
			} else if (i == 2) {
				return p3;
			} else if (i == 3) {
				return p4;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Simplex#getCenter()
		 */

		@Override
		public WB_Point getCenter() {
			return geometryfactory.createMidpoint(p1, p2, p3, p3);
		}

		/**
		 * Get the volume of the tetrahedron.
		 *
		 * @return
		 */
		@Override
		public double getVolume() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			return Math.abs(a.dot(b.crossSelf(c))) / 6.0;
		}

		/**
		 * Calculate the radius of the circumsphere.
		 *
		 * @return
		 */
		@Override
		public double getCircumradius() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			final WB_Vector O = b.cross(c).mulSelf(a.dot(a));
			O.addSelf(c.cross(a).mulSelf(b.dot(b)));
			O.addSelf(a.cross(b).mulSelf(c.dot(c)));
			O.mulSelf(1.0 / (2 * a.dot(b.crossSelf(c))));
			return O.getLength();
		}

		/**
		 * Find the center of the circumscribing sphere.
		 *
		 * @return
		 */
		@Override
		public WB_Point getCircumcenter() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			final WB_Vector O = b.cross(c).mulSelf(a.dot(a));
			O.addSelf(c.cross(a).mulSelf(b.dot(b)));
			O.addSelf(a.cross(b).mulSelf(c.dot(c)));
			O.mulSelf(1.0 / (2 * a.dot(b.crossSelf(c))));
			return p4.add(O);
		}

		/**
		 * Find the circumscribing sphere.
		 *
		 * @return
		 */
		@Override
		public WB_Sphere getCircumsphere() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			final WB_Vector O = b.cross(c).mulSelf(a.dot(a));
			O.addSelf(c.cross(a).mulSelf(b.dot(b)));
			O.addSelf(a.cross(b).mulSelf(c.dot(c)));
			O.mulSelf(1.0 / (2 * a.dot(b.crossSelf(c))));
			return geometryfactory.createSphereWithRadius(p4.add(O), O.getLength());
		}

		/**
		 * Calculate the radius of the insphere.
		 *
		 * @return
		 */
		@Override
		public double getInradius() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			final WB_Vector bXc = b.cross(c);
			final double sixV = Math.abs(a.dot(bXc));
			c.crossSelf(a);
			a.crossSelf(b);
			final double denom = bXc.getLength() + c.getLength() + a.getLength() + bXc.addMulSelf(2, a).getLength();
			return sixV / denom;
		}

		/**
		 * Find the center of the inscribed sphere.
		 *
		 * @return
		 */
		@Override
		public WB_Point getIncenter() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			final WB_Vector bXc = b.cross(c);
			final WB_Vector cXa = c.cross(a);
			final WB_Vector aXb = a.cross(b);
			final double bXcLength = bXc.getLength();
			final double cXaLength = cXa.getLength();
			final double aXbLength = aXb.getLength();
			final double dLength = bXc.addSelf(cXa).addSelf(aXb).getLength();
			final WB_Vector O = a.mulSelf(bXcLength);
			O.addSelf(b.mulSelf(cXaLength));
			O.addSelf(c.mulSelf(aXbLength));
			O.divSelf(bXcLength + cXaLength + aXbLength + dLength);
			return p4.add(O);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Sphere getInsphere() {
			final WB_Vector a = geometryfactory.createVectorFromTo(p1, p4);
			final WB_Vector b = geometryfactory.createVectorFromTo(p2, p4);
			final WB_Vector c = geometryfactory.createVectorFromTo(p3, p4);
			final WB_Vector bXc = b.cross(c);
			final WB_Vector cXa = c.cross(a);
			final WB_Vector aXb = a.cross(b);
			final double bXcLength = bXc.getLength();
			final double cXaLength = cXa.getLength();
			final double aXbLength = aXb.getLength();
			final double dLength = bXc.addSelf(cXa).addSelf(aXb).getLength();
			final WB_Vector O = a.mulSelf(bXcLength);
			O.addSelf(b.mulSelf(cXaLength));
			O.addSelf(c.mulSelf(aXbLength));
			O.divSelf(bXcLength + cXaLength + aXbLength + dLength);
			return geometryfactory.createSphereWithRadius(p4.add(O), O.getLength());
		}

		public Type getType() {
			return type;
		}
		
		public int getTypeAsInt() {
			switch(type) {
			case A:
				return 0;
			case B:
				return 1;
			case C:
				return 2;
			case K:
				return 3;
			default:
				return 0;
			
			}

		}

		@Override
		public WB_DanzerTile3D apply(final WB_Transform3D T) {
			return new WB_DanzerTile3D(p1.applyAsPoint(T), p2.applyAsPoint(T), p3.applyAsPoint(T), p4.applyAsPoint(T),
					type, generation);
		}

		public List<WB_DanzerTile3D> inflate() {
			List<WB_DanzerTile3D> tiles = new FastList<WB_DanzerTile3D>();
			WB_Point p5, p6, p7, p8, p9, p10, p11;
			switch (type) {
			case A:
				p5 = interpolateNonNorm(p2, p3, b, b * tau);
				p6 = interpolateNonNorm(p1, p3, a * tau, a);
				p7 = interpolateNonNorm(p2, p4, a, a * invtau);
				p8 = interpolateNonNorm(p1, p4, 1, invtau);
				p9 = interpolateNonNorm(p8, p4, 1, 1);
				p10 = interpolateNonNorm(p1, p5, 1, invtau);
				p11 = interpolateNonNorm(p10, p5, 1, 1);
				tiles.add(new WB_DanzerTile3D(p7, p2, p1, p10, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p1, p10, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p1, p8, Type.B, generation + 1));

				tiles.add(new WB_DanzerTile3D(p7, p5, p3, p6, Type.C, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p4, p3, p6, Type.C, generation + 1));

				tiles.add(new WB_DanzerTile3D(p7, p6, p5, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p10, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p2, p5, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p2, p10, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p4, p9, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p8, p9, Type.K, generation + 1));

				break;
			case B:
				p5 = interpolateNonNorm(p2, p3, a * tau, a);
				p6 = interpolateNonNorm(p1, p3, b, b * tau);
				p7 = interpolateNonNorm(p2, p6, 1, invtau);
				p8 = interpolateNonNorm(p7, p6, 1, 1);
				tiles.add(new WB_DanzerTile3D(p4, p5, p2, p7, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p1, p2, p7, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p6, p3, p5, Type.C, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p5, p6, p8, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p5, p7, p8, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p1, p7, p8, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p1, p6, p8, Type.K, generation + 1));

				break;
			case C:
				p5 = interpolateNonNorm(p2, p3, b * tau, b);
				p6 = interpolateNonNorm(p1, p3, 1, tau);
				p7 = interpolateNonNorm(p6, p3, 1, 1);
				p8 = interpolateNonNorm(p3, p4, a, a * invtau);

				tiles.add(new WB_DanzerTile3D(p1, p2, p4, p6, Type.A, generation + 1));

				tiles.add(new WB_DanzerTile3D(p8, p5, p2, p6, Type.C, generation + 1));
				tiles.add(new WB_DanzerTile3D(p8, p4, p2, p6, Type.C, generation + 1));

				tiles.add(new WB_DanzerTile3D(p8, p6, p5, p7, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p8, p3, p5, p7, Type.K, generation + 1));

				break;
			case K:

				p5 = interpolateNonNorm(p2, p4, 1, 0.5 * invtau);
				tiles.add(new WB_DanzerTile3D(p3, p1, p2, p5, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p3, p1, p5, p4, Type.K, generation + 1));

				break;
			default:
				tiles.add(this);
				break;

			}
			return tiles;
		}

		List<WB_DanzerTile3D> inflate(final List<WB_Point> points) {
			List<WB_DanzerTile3D> tiles = new FastList<WB_DanzerTile3D>();
			WB_Point p5, p6, p7, p8, p9, p10, p11;
			switch (type) {
			case A:
				p5 = interpolateNonNorm(p2, p3, b, b * tau);
				p6 = interpolateNonNorm(p1, p3, a * tau, a);
				p7 = interpolateNonNorm(p2, p4, a, a * invtau);
				p8 = interpolateNonNorm(p1, p4, 1, invtau);
				p9 = interpolateNonNorm(p8, p4, 1, 1);
				p10 = interpolateNonNorm(p1, p5, 1, invtau);
				p11 = interpolateNonNorm(p10, p5, 1, 1);
				points.add(p5);
				points.add(p6);
				points.add(p7);
				points.add(p8);
				points.add(p9);
				points.add(p10);
				points.add(p11);

				tiles.add(new WB_DanzerTile3D(p7, p2, p1, p10, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p1, p10, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p1, p8, Type.B, generation + 1));

				tiles.add(new WB_DanzerTile3D(p7, p5, p3, p6, Type.C, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p4, p3, p6, Type.C, generation + 1));

				tiles.add(new WB_DanzerTile3D(p7, p6, p5, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p10, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p2, p5, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p2, p10, p11, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p4, p9, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p7, p6, p8, p9, Type.K, generation + 1));

				break;
			case B:
				p5 = interpolateNonNorm(p2, p3, a * tau, a);
				p6 = interpolateNonNorm(p1, p3, b, b * tau);
				p7 = interpolateNonNorm(p2, p6, 1, invtau);
				p8 = interpolateNonNorm(p7, p6, 1, 1);
				points.add(p5);
				points.add(p6);
				points.add(p7);
				points.add(p8);
				tiles.add(new WB_DanzerTile3D(p4, p5, p2, p7, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p1, p2, p7, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p6, p3, p5, Type.C, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p5, p6, p8, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p5, p7, p8, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p1, p7, p8, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p4, p1, p6, p8, Type.K, generation + 1));

				break;
			case C:
				p5 = interpolateNonNorm(p2, p3, b * tau, b);
				p6 = interpolateNonNorm(p1, p3, 1, tau);
				p7 = interpolateNonNorm(p6, p3, 1, 1);
				p8 = interpolateNonNorm(p3, p4, a, a * invtau);
				points.add(p5);
				points.add(p6);
				points.add(p7);
				points.add(p8);

				tiles.add(new WB_DanzerTile3D(p1, p2, p4, p6, Type.A, generation + 1));

				tiles.add(new WB_DanzerTile3D(p8, p5, p2, p6, Type.C, generation + 1));
				tiles.add(new WB_DanzerTile3D(p8, p4, p2, p6, Type.C, generation + 1));

				tiles.add(new WB_DanzerTile3D(p8, p6, p5, p7, Type.K, generation + 1));
				tiles.add(new WB_DanzerTile3D(p8, p3, p5, p7, Type.K, generation + 1));

				break;
			case K:

				p5 = interpolateNonNorm(p2, p4, 1, 0.5 * invtau);
				points.add(p5);
				tiles.add(new WB_DanzerTile3D(p3, p1, p2, p5, Type.B, generation + 1));
				tiles.add(new WB_DanzerTile3D(p3, p1, p5, p4, Type.K, generation + 1));

				break;
			default:
				tiles.add(this);
				break;

			}
			return tiles;
		}

		public static WB_Point interpolateNonNorm(final WB_Coord v, final WB_Coord w, final double a, final double b) {
			return new WB_Point(
					WB_CoordOp.interpolate(v.xd(), v.yd(), v.zd(), w.xd(), w.yd(), w.zd(), a / (a + b)));
		}

	}

	protected Type type;
	protected List<WB_Point> points;
	protected List<WB_DanzerTile3D> tiles;
	protected WB_MTRandom rnd;

	/**
	 *
	 * @param type
	 * @param scale
	 * @param offset
	 */
	public WB_Danzer3D(final Type type, final double scale, final WB_Coord offset) {
		rnd = new WB_MTRandom();
		points = new FastList<WB_Point>();
		tiles = new FastList<WB_DanzerTile3D>();
		this.type = type;
		final WB_DanzerTile3D T = new WB_DanzerTile3D(type, scale, offset, 0);
		points.add(T.p1);
		points.add(T.p2);
		points.add(T.p3);
		points.add(T.p4);
		tiles.add(T);
	}

	public void centerOnPoint(final int i) {
		if (points != null && i >= 0 && i < points.size()) {
			WB_Point center = new WB_Point(points.get(i));
			for (WB_Point p : points) {
				p.subSelf(center);
			}

		}

	}

	public void setSeed(final long seed) {
		rnd.setSeed(seed);

	}

	/**
	 *
	 */
	public void inflate() {
		final List<WB_DanzerTile3D> newTiles = new FastList<WB_DanzerTile3D>();
		for (int i = 0; i < tiles.size(); i++) {
			newTiles.addAll(tiles.get(i).inflate(points));
		}
		tiles = newTiles;
	}

	/**
	 *
	 *
	 * @param rep
	 */
	public void inflate(final int rep) {
		for (int r = 0; r < rep; r++) {
			inflate();
		}
	}

	/**
	 *
	 */
	public void inflate(final double probability) {
		final List<WB_DanzerTile3D> newTiles = new FastList<WB_DanzerTile3D>();
		for (int i = 0; i < tiles.size(); i++) {
			if (rnd.nextDouble() >= probability) {
				newTiles.add(tiles.get(i));
			} else {
				newTiles.addAll(tiles.get(i).inflate(points));
			}
		}
		tiles = newTiles;
	}

	/**
	 *
	 * @param probability
	 * @param rep
	 */
	public void inflate(final double probability, final int rep) {
		for (int r = 0; r < rep; r++) {
			inflate(probability);
		}
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_DanzerTile3D tile(final int i) {
		return tiles.get(i);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int oldest() {
		int result = Integer.MAX_VALUE;
		for (final WB_DanzerTile3D T : tiles) {
			result = Math.min(T.generation, result);
			if (result == 0) {
				return 0;
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int youngest() {
		int result = -1;
		for (final WB_DanzerTile3D T : tiles) {
			result = Math.max(T.generation, result);
		}
		return result;
	}

	/**
	 *
	 *
	 * @param i
	 */
	public void inflateTile(final int i) {
		tiles.addAll(tiles.get(i).inflate());
		tiles.remove(i);
	}

	/**
	 *
	 */
	public void inflateOldest() {
		inflateOldest(0);
	}

	/**
	 *
	 *
	 * @param r
	 */
	public void inflateOldest(final int r) {
		final int age = oldest();
		Collections.shuffle(tiles);
		for (final WB_DanzerTile3D T : tiles) {
			if (T.generation <= age + r) {
				tiles.addAll(T.inflate());
				tiles.remove(T);
				return;
			}
		}
	}

	/**
	 *
	 *
	 * @param i
	 */
	public void removeTile(final int i) {
		tiles.remove(i);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int size() {
		return tiles.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfPoints() {
		return points.size();
	}

	/**
	 *
	 *
	 * @return
	 */

	public WB_CoordCollection getPoints() {
		return WB_CoordCollection.getCollection(points);
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_DanzerTile3D> getTiles() {
		return tiles;
	}
	
	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfTiles() {
		return tiles.size();
	}

}
