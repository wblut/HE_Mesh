/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.security.InvalidParameterException;

import wblut.math.WB_Epsilon;

public class WB_Geodesic implements WB_SimpleMeshCreator {
	private static WB_GeometryFactory gf = new WB_GeometryFactory();

	public static enum Type {
		TETRAHEDRON(0), OCTAHEDRON(1), CUBE(2), DODECAHEDRON(3), ICOSAHEDRON(4);
		Type(final int index) {
			this.index = index;
		}

		private final int index;

		public int getIndex() {
			return index;
		}
	};

	private WB_SimpleMesh			mesh;
	private final double	radius;
	private final Type		type;
	private final int		b;
	private final int		c;

	/**
	 *
	 *
	 * @param radius
	 * @param b
	 * @param c
	 */
	public WB_Geodesic(final double radius, final int b, final int c) {
		this(radius, b, c, Type.ICOSAHEDRON);
	}

	/**
	 *
	 *
	 * @param radius
	 * @param b
	 * @param c
	 * @param type
	 */
	public WB_Geodesic(final double radius, final int b, final int c,
			final Type type) {
		if (b + c == 0 || b < 0 || c < 0) {
			throw new InvalidParameterException("Invalid values for b and c.");
		}
		this.b = b;
		this.c = c;
		this.type = type;
		this.radius = radius;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_MeshCreator#getMesh()
	 */
	@Override
	public WB_SimpleMesh create() {
		createMesh();
		return mesh;
	}

	/**
	 *
	 */
	private void createMesh() {
		if (b == c) {
			final WB_GeodesicII geo = new WB_GeodesicII(radius, b + c, type);
			mesh = geo.getMesh();
		} else if (b == 0 || c == 0) {
			if (type == Type.CUBE || type == Type.DODECAHEDRON) {
				throw new InvalidParameterException(
						"Invalid type for this class of geodesic.");
			}
			final WB_GeodesicI geo = new WB_GeodesicI(radius, b + c, type, 1);
			mesh = geo.getMesh();
		} else {
			if (type == Type.CUBE || type == Type.DODECAHEDRON) {
				throw new InvalidParameterException(
						"Invalid type for this class of geodesic.");
			}
			final WB_GeodesicIII geo = new WB_GeodesicIII(radius, b, c, type);
			mesh = geo.getMesh();
		}
	}

	/**
	 *
	 *
	 * @param start1
	 * @param end1
	 * @param start2
	 * @param end2
	 * @return
	 */
	public static WB_GreatCircleIntersection getGreatCircleIntersection(
			final WB_Coord start1, final WB_Coord end1, final WB_Coord start2,
			final WB_Coord end2) {
		final WB_Point origin = gf.createPoint(0, 0, 0);
		final WB_Vector r1 = getNormalToGreatCircle(origin, start1, end1);
		final WB_Vector r2 = getNormalToGreatCircle(origin, start2, end2);
		final WB_Vector r3 = getNormalToGreatCircle(origin, r1, r2);
		if (WB_Epsilon.isZeroSq(r3.getSqLength())) {
			return null;
		}
		r3.normalizeSelf();
		final WB_Point p0 = gf.createPoint(r3);
		final WB_Point p1 = p0.mul(-1);
		final double dihedral = Math
				.acos(Math.abs(r1.dot(r2)) / (r1.getLength() * r2.getLength()));
		p0.addSelf(origin);
		p1.addSelf(origin);
		return new WB_GreatCircleIntersection(p0.coords(), p1.coords(),
				dihedral);
	}

	/**
	 *
	 *
	 * @param start
	 * @param end
	 * @param f
	 * @return
	 */
	public static double[] getPointOnGreatCircleArc(final WB_Coord start,
			final WB_Coord end, final double f) {
		final WB_Point origin = gf.createPoint(0, 0, 0);
		final double angle = Math
				.acos(getCosAngleOfGreatCircleArc(origin, start, end));
		final double isa = 1.0 / Math.sin(angle);
		final double alpha = Math.sin((1.0 - f) * angle) * isa;
		final double beta = Math.sin(f * angle) * isa;
		final WB_Point r0 = new WB_Point(start).mul(alpha);
		final WB_Point r1 = new WB_Point(end).mul(beta);
		return r0.add(r1).coords();
	}

	/**
	 *
	 *
	 * @param origin
	 * @param start
	 * @param end
	 * @return
	 */
	private static WB_Vector getNormalToGreatCircle(final WB_Coord origin,
			final WB_Coord start, final WB_Coord end) {
		final WB_Vector r0 = new WB_Vector(origin, start);
		final WB_Vector r1 = new WB_Vector(origin, end);
		return r1.cross(r0);
	}

	/**
	 *
	 *
	 * @param origin
	 * @param start
	 * @param end
	 * @return
	 */
	private static double getCosAngleOfGreatCircleArc(final WB_Coord origin,
			final WB_Coord start, final WB_Coord end) {
		final WB_Vector r0 = new WB_Vector(origin, start);
		final WB_Vector r1 = new WB_Vector(origin, end);
		return r0.dot(r1) / (r0.getLength() * r1.getLength());
	}

	public static class WB_GreatCircleIntersection {
		public double[]	p0;
		public double[]	p1;
		public double	dihedral;

		/**
		 *
		 *
		 * @param p0
		 * @param p1
		 * @param dihedral
		 */
		public WB_GreatCircleIntersection(final double[] p0, final double[] p1,
				final double dihedral) {
			this.p0 = p0;
			this.p1 = p1;
			this.dihedral = dihedral;
		}
	}
}
