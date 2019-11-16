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

public class WB_Danzer2D implements WB_TriangleFactory {

	private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	public static enum Type {
		A, B, C
	}

	static class WB_DanzerTile2D {
		public int p1, p2, p3;
		public Type type;
		public int generation;

		/**
		 *
		 *
		 * @param t
		 * @param g
		 */
		public WB_DanzerTile2D(final Type t, final int g) {
			type = t;
			p1 = p2 = p3 = -1;
			generation = g;

		}
	}

	final static double theta = Math.PI / 7.0;
	final static double psi = Math.PI / 3.5;
	final static double beta = 3.0 * Math.PI / 7.0;
	final static double phi = Math.PI / 1.75;
	final static double sintheta = Math.sin(theta);
	final static double sinhtheta = Math.sin(0.5 * theta);
	final static double sinpsi = Math.sin(psi);
	final static double sinbeta = Math.sin(beta);
	final static double sinhbeta = Math.sin(0.5 * beta);
	final static double sinphi = Math.sin(phi);
	final static double costheta = Math.cos(theta);
	final static double coshtheta = Math.cos(0.5 * theta);
	final static double cospsi = Math.cos(psi);
	final static double cosbeta = Math.cos(beta);
	final static double coshbeta = Math.cos(0.5 * beta);
	final static double cosphi = Math.cos(phi);

	final double gamma = sintheta / (sintheta + sinpsi);
	protected double a, b, c, r1, r2, r3;
	protected Type type;
	protected List<WB_Point> points;
	protected List<WB_DanzerTile2D> tiles;
	protected WB_MTRandom rnd;

	/**
	 *
	 *
	 * @param sc
	 * @param t
	 */
	public WB_Danzer2D(final double sc, final Type t) {
		this(sc, t, 0.0, new WB_Point(), new WB_PlanarMap());
	}

	/**
	 *
	 *
	 * @param sc
	 * @param t
	 * @param offset
	 */
	public WB_Danzer2D(final double sc, final Type t, final WB_Coord offset) {
		this(sc, t, 0.0, offset, new WB_PlanarMap());
	}

	/**
	 *
	 *
	 * @param sc
	 * @param t
	 * @param angle
	 */
	public WB_Danzer2D(final double sc, final Type t, final double angle) {
		this(sc, t, angle, new WB_Point(), new WB_PlanarMap());
	}

	/**
	 *
	 *
	 * @param sc
	 * @param t
	 * @param angle
	 * @param offset
	 */
	public WB_Danzer2D(final double sc, final Type t, final double angle, final WB_Coord offset) {
		this(sc, t, angle, offset, new WB_PlanarMap());
	}

	/**
	 *
	 *
	 * @param sc
	 * @param t
	 * @param context
	 */
	public WB_Danzer2D(final double sc, final Type t, final WB_Map2D context) {
		this(sc, t, 0.0, new WB_Point(), context);
	}

	/**
	 *
	 *
	 * @param sc
	 * @param t
	 * @param angle
	 * @param offset
	 * @param context
	 */
	public WB_Danzer2D(final double sc, final Type t, final double angle, final WB_Coord offset, final WB_Map2D context) {
		rnd = new WB_MTRandom();
		c = sc;
		b = c / sinbeta * sintheta;
		a = c / sinbeta * sinpsi;
		r1 = c / (a + 2 * c);
		r2 = c / (a + b + c);
		r3 = b / (a + b + c);
		points = new FastList<WB_Point>();
		tiles = new FastList<WB_DanzerTile2D>();
		type = t;
		final WB_DanzerTile2D T = new WB_DanzerTile2D(type, 0);
		WB_Point q;
		WB_Transform3D ROT = new WB_Transform3D().addRotateZ(angle);
		switch (type) {
		case A:
			WB_Point p = geometryfactory.createPoint();
			q = new WB_Point(0, 0.5 * sinbeta * c);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);

			p = geometryfactory.createPoint();
			q = new WB_Point(-0.5 * b, -0.5 * sinbeta * c);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);

			p = geometryfactory.createPoint();
			q = new WB_Point(0.5 * b, -0.5 * sinbeta * c);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);
			break;
		case C:
			p = geometryfactory.createPoint();
			q = new WB_Point(-0.5 * a * coshbeta, 0);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), +offset.yd(), p);
			points.add(p);
			p = geometryfactory.createPoint();
			q = new WB_Point(0.5 * a * coshbeta, -a * cospsi);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);
			p = geometryfactory.createPoint();
			q = new WB_Point(0.5 * a * coshbeta, a * cospsi);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);
			break;
		case B:
			p = geometryfactory.createPoint();
			q = new WB_Point(0, 0.5 * sinbeta * c);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);
			p = geometryfactory.createPoint();
			q = new WB_Point(-a * sinhtheta, -a * coshtheta + 0.5 * sinbeta * c);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);
			p = geometryfactory.createPoint();
			q = new WB_Point(0.5 * b, -0.5 * sinbeta * c);
			q.applySelf(ROT);
			context.unmapPoint2D(q.xd() + offset.xd(), q.yd() + offset.yd(), p);
			points.add(p);
			break;
		default:
		}
		T.p1 = 0;
		T.p2 = 1;
		T.p3 = 2;
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
		final List<WB_DanzerTile2D> newTiles = new FastList<WB_DanzerTile2D>();
		for (int i = 0; i < tiles.size(); i++) {
			newTiles.addAll(inflateTileInt(tiles.get(i), 2.0));
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
		final List<WB_DanzerTile2D> newTiles = new FastList<WB_DanzerTile2D>();
		for (int i = 0; i < tiles.size(); i++) {
			newTiles.addAll(inflateTileInt(tiles.get(i), probability));
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
	 * @param T
	 * @param probability
	 * @return
	 */
	protected List<WB_DanzerTile2D> inflateTileInt(final WB_DanzerTile2D T, final double probability) {
		final List<WB_DanzerTile2D> newTiles = new FastList<WB_DanzerTile2D>();

		if (rnd.nextDouble() >= probability) {
			newTiles.add(T);
		} else {

			final WB_Point p1 = points.get(T.p1);
			final WB_Point p2 = points.get(T.p2);
			final WB_Point p3 = points.get(T.p3);
			final int cnp = points.size();
			final Type type = T.type;
			switch (type) {
			case A:
				WB_Point q1 = geometryfactory.createInterpolatedPoint(p1, p2, r1);
				points.add(q1);
				points.add(geometryfactory.createInterpolatedPoint(p1, p3, r1));
				points.add(geometryfactory.createInterpolatedPoint(p2, p1, r1));
				points.add(geometryfactory.createInterpolatedPoint(p3, p1, r1));
				final WB_Point q2 = geometryfactory.createInterpolatedPoint(p2, p3, a / (a + b));
				points.add(q2);
				points.add(geometryfactory.createInterpolatedPoint(q1, q2, c / (a + c)));
				WB_DanzerTile2D nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p1;
				nT.p2 = cnp;
				nT.p3 = cnp + 1;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = cnp + 5;
				nT.p2 = cnp;
				nT.p3 = cnp + 1;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p2;
				nT.p2 = cnp + 2;
				nT.p3 = cnp + 5;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p3;
				nT.p2 = cnp + 3;
				nT.p3 = cnp + 5;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp;
				nT.p2 = cnp + 2;
				nT.p3 = cnp + 5;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 1;
				nT.p2 = cnp + 3;
				nT.p3 = cnp + 5;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 5;
				nT.p2 = cnp + 4;
				nT.p3 = T.p3;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.C, T.generation + 1);
				nT.p1 = cnp + 4;
				nT.p2 = T.p2;
				nT.p3 = cnp + 5;
				newTiles.add(nT);
				break;
			case B:
				points.add(geometryfactory.createInterpolatedPoint(p1, p2, r2));
				points.add(geometryfactory.createInterpolatedPoint(p1, p3, r1));
				points.add(geometryfactory.createInterpolatedPoint(p2, p1, r3));
				points.add(geometryfactory.createInterpolatedPoint(p3, p1, r1));
				points.add(geometryfactory.createInterpolatedPoint(p2, p3, a / (a + b)));
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p1;
				nT.p2 = cnp + 1;
				nT.p3 = cnp;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 3;
				nT.p2 = cnp + 1;
				nT.p3 = cnp;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.C, T.generation + 1);
				nT.p1 = cnp + 2;
				nT.p2 = cnp;
				nT.p3 = cnp + 3;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 3;
				nT.p2 = cnp + 2;
				nT.p3 = T.p2;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.C, T.generation + 1);
				nT.p1 = cnp + 4;
				nT.p3 = cnp + 3;
				nT.p2 = T.p2;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 3;
				nT.p2 = cnp + 4;
				nT.p3 = T.p3;
				newTiles.add(nT);
				break;
			case C:
				q1 = geometryfactory.createInterpolatedPoint(p1, p2, r2);
				points.add(q1);
				points.add(geometryfactory.createInterpolatedPoint(p1, p3, r3));
				points.add(geometryfactory.createInterpolatedPoint(p2, p1, r3));
				points.add(geometryfactory.createInterpolatedPoint(p3, p1, r2));
				points.add(geometryfactory.createInterpolatedPoint(p2, p3, r1));
				points.add(geometryfactory.createInterpolatedPoint(p3, p2, r1));
				points.add(geometryfactory.createInterpolatedPoint(q1, p3, r3));
				points.add(geometryfactory.createInterpolatedPoint(p3, q1, r2));
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p3;
				nT.p3 = cnp + 3;
				nT.p2 = cnp + 7;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 6;
				nT.p2 = cnp + 7;
				nT.p3 = cnp + 3;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.C, T.generation + 1);
				nT.p1 = cnp + 1;
				nT.p3 = cnp + 6;
				nT.p2 = cnp + 3;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 6;
				nT.p2 = cnp + 1;
				nT.p3 = T.p1;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p1;
				nT.p3 = cnp;
				nT.p2 = cnp + 6;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.A, T.generation + 1);
				nT.p1 = T.p3;
				nT.p2 = cnp + 5;
				nT.p3 = cnp + 7;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 4;
				nT.p2 = cnp + 5;
				nT.p3 = cnp + 7;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.C, T.generation + 1);
				nT.p1 = cnp + 6;
				nT.p2 = cnp + 7;
				nT.p3 = cnp + 4;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 4;
				nT.p2 = cnp + 6;
				nT.p3 = cnp;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.C, T.generation + 1);
				nT.p1 = cnp + 2;
				nT.p3 = cnp + 4;
				nT.p2 = cnp;
				newTiles.add(nT);
				nT = new WB_DanzerTile2D(Type.B, T.generation + 1);
				nT.p1 = cnp + 4;
				nT.p2 = cnp + 2;
				nT.p3 = T.p2;
				newTiles.add(nT);
			default:
			}
		}
		return newTiles;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_DanzerTile2D tile(final int i) {
		return tiles.get(i);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int oldest() {
		int result = Integer.MAX_VALUE;
		for (final WB_DanzerTile2D T : tiles) {
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
		for (final WB_DanzerTile2D T : tiles) {
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
		tiles.addAll(inflateTileInt(tiles.get(i), 2.0));
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
		for (final WB_DanzerTile2D T : tiles) {
			if (T.generation <= age + r) {
				tiles.addAll(inflateTileInt(T, 2.0));
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
	public List<WB_Point> points() {
		return points;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_CoordCollection getPoints() {
		return WB_CoordCollection.getCollection(points);
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Polygon> getTiles() {
		final List<WB_Polygon> faces = new FastList<WB_Polygon>();
		clean();
		for (final WB_DanzerTile2D T : tiles) {
			faces.add(geometryfactory.createSimplePolygon(points.get(T.p1), points.get(T.p2), points.get(T.p3)));
		}
		return faces;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int[] getTriangles() {

		clean();
		int[] triangles = new int[3 * tiles.size()];
		int id = 0;
		for (final WB_DanzerTile2D T : tiles) {
			triangles[id++] = T.p1;
			triangles[id++] = T.p2;
			triangles[id++] = T.p3;
		}
		return triangles;
	}

	public WB_Triangulation2DWithPoints getTriangulation() {
		return new WB_Triangulation2DWithPoints(getTilesAsIndices(), getPoints());
	}

	/**
	 *
	 *
	 * @return
	 */
	public int[] getTilesAsIndices() {
		clean();
		final int[] indices = new int[tiles.size() * 3];
		int i = 0;
		for (final WB_DanzerTile2D T : tiles) {
			indices[i++] = T.p1;
			indices[i++] = T.p2;
			indices[i++] = T.p3;
		}
		return indices;
	}

	/**
	 *
	 */
	private void clean() {
		final boolean[] used = new boolean[points.size()];
		final int[] newindices = new int[points.size()];
		for (final WB_DanzerTile2D T : tiles) {
			used[T.p1] = true;
			used[T.p2] = true;
			used[T.p3] = true;
		}
		int ni = 0;
		final List<WB_Point> newpoints = new FastList<WB_Point>();
		for (int i = 0; i < points.size(); i++) {
			if (used[i]) {
				newindices[i] = ni++;
				newpoints.add(points.get(i));
			}
		}
		for (final WB_DanzerTile2D T : tiles) {
			T.p1 = newindices[T.p1];
			T.p2 = newindices[T.p2];
			T.p3 = newindices[T.p3];
		}
		points = newpoints;
	}
}
