/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import wblut.math.WB_ScalarParameter;

/**
 * WB_AlphaTriangulation3D stores the results of
 * WB_Triangulate.alphaTriangulate3D: a 3D Delaunay triangulation with the
 * corresponding circumsphere radii.
 *
 */
public class WB_AlphaTriangulation3D {
	/**
	 *
	 */
	private int[]				tetrahedra;
	private double[]			alpha;
	private FastList<WB_Coord>	points;

	/**
	 *
	 *
	 * @param tetra
	 * @param points
	 */
	public WB_AlphaTriangulation3D(final int[] tetra,
			final WB_CoordCollection points) {
		tetrahedra = Arrays.copyOf(tetra, tetra.length);
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < points.size(); i++) {
			this.points.add(points.get(i));
		}
		setAlpha();
	}

	/**
	 *
	 * @param tetra
	 * @param points
	 */
	public WB_AlphaTriangulation3D(final int[][] tetra,
			final WB_CoordCollection points) {
		tetrahedra = new int[tetra.length * 4];
		for (int i = 0; i < tetra.length; i++) {
			tetrahedra[4 * i] = tetra[i][0];
			tetrahedra[4 * i + 1] = tetra[i][1];
			tetrahedra[4 * i + 2] = tetra[i][2];
			tetrahedra[4 * i + 3] = tetra[i][3];
		}
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < points.size(); i++) {
			this.points.add(points.get(i));
		}
		setAlpha();
	}

	/**
	 *
	 *
	 * @param tetra
	 * @param points
	 */
	public WB_AlphaTriangulation3D(final int[] tetra,
			final Collection<? extends WB_Coord> points) {
		tetrahedra = Arrays.copyOf(tetra, tetra.length);
		this.points = new FastList<WB_Coord>();
		this.points.addAll(points);
		setAlpha();
	}

	/**
	 *
	 * @param tetra
	 * @param points
	 */
	public WB_AlphaTriangulation3D(final int[][] tetra,
			final Collection<? extends WB_Coord> points) {
		tetrahedra = new int[tetra.length * 4];
		for (int i = 0; i < tetra.length; i++) {
			tetrahedra[4 * i] = tetra[i][0];
			tetrahedra[4 * i + 1] = tetra[i][1];
			tetrahedra[4 * i + 2] = tetra[i][2];
			tetrahedra[4 * i + 3] = tetra[i][3];
		}
		this.points = new FastList<WB_Coord>();
		this.points.addAll(points);
		setAlpha();
	}

	/**
	 *
	 * @param tetra
	 * @param points
	 */
	public WB_AlphaTriangulation3D(final int[] tetra, final WB_Coord[] points) {
		tetrahedra = Arrays.copyOf(tetra, tetra.length);
		this.points = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			this.points.add(p);
		}
		setAlpha();
	}

	/**
	 *
	 * @param tetra
	 * @param points
	 */
	public WB_AlphaTriangulation3D(final int[][] tetra,
			final WB_Coord[] points) {
		tetrahedra = new int[tetra.length * 4];
		for (int i = 0; i < tetra.length; i++) {
			tetrahedra[4 * i] = tetra[i][0];
			tetrahedra[4 * i + 1] = tetra[i][1];
			tetrahedra[4 * i + 2] = tetra[i][2];
			tetrahedra[4 * i + 3] = tetra[i][3];
		}
		this.points = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			this.points.add(p);
		}
		setAlpha();
	}

	private void setAlpha() {
		alpha = new double[tetrahedra.length / 4];
		int index = 0;
		for (int i = 0; i < tetrahedra.length; i += 4) {
			alpha[index++] = WB_Predicates.circumradius3D(
					points.get(tetrahedra[i]), points.get(tetrahedra[i + 1]),
					points.get(tetrahedra[i + 2]),
					points.get(tetrahedra[i + 3]));
		}
	}

	/**
	 * Get the indices to the tetrahedra vertices as a single array of int. 4
	 * indices per tetrahedron.
	 *
	 * @return
	 */
	public int[] getTetrahedra() {
		return tetrahedra;
	}

	/**
	 * Get the vertices of the triangulation as an unmodifiable List<WB_Coord>.
	 *
	 * @return
	 */
	public List<WB_Coord> getPoints() {
		return points.asUnmodifiable();
	}

	/**
	 * Get the circumradii of the tetrahedra vertices as a single array of
	 * double. Original values are copied.
	 *
	 * @return
	 */
	public double[] getAlpha() {
		return Arrays.copyOf(alpha, alpha.length);
	}

	/**
	 * Get the indices to the alpha tetrahedra vertices as a single array of
	 * int. 4 indices per tetrahedron. Only tetrahedra with a circumradius
	 * smaller or equal to a are returned.
	 *
	 * @param a
	 *            alpha value
	 *
	 * @return
	 */
	public int[] getAlphaTetrahedra(final double a) {
		int[] alphaTetrahedra = new int[tetrahedra.length];
		int index = 0;
		for (int i = 0; i < tetrahedra.length; i += 4) {
			if (alpha[i / 4] <= a) {
				alphaTetrahedra[index++] = tetrahedra[i + 0];
				alphaTetrahedra[index++] = tetrahedra[i + 1];
				alphaTetrahedra[index++] = tetrahedra[i + 2];
				alphaTetrahedra[index++] = tetrahedra[i + 3];
			}
		}
		return Arrays.copyOf(alphaTetrahedra, index);
	}

	/**
	 * Get the indices to the alpha triangle vertices as a single array of int.
	 * 3 indices per triangle. Only unpaired triangles from the collection of
	 * tetrahedra with a circumradius smaller or equal to a are returned.
	 *
	 * @param a
	 *            alpha value
	 *
	 * @return
	 */
	public int[] getAlphaTriangles(final double a) {
		UnifiedMap<Key, Triple> tris = new UnifiedMap<Key, Triple>();
		for (int i = 0; i < tetrahedra.length; i += 4) {
			if (alpha[i / 4] <= a || Double.isNaN(alpha[i / 4])) {
				Key key = new Key(tetrahedra[i], tetrahedra[i + 1],
						tetrahedra[i + 2]);
				Triple triple = new Triple(tetrahedra[i], tetrahedra[i + 1],
						tetrahedra[i + 2]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 1], tetrahedra[i],
						tetrahedra[i + 3]);
				triple = new Triple(tetrahedra[i + 1], tetrahedra[i],
						tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 2], tetrahedra[i + 1],
						tetrahedra[i + 3]);
				triple = new Triple(tetrahedra[i + 2], tetrahedra[i + 1],
						tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i], tetrahedra[i + 2],
						tetrahedra[i + 3]);
				triple = new Triple(tetrahedra[i], tetrahedra[i + 2],
						tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
			}
		}
		int[] alphaTriangles = new int[3 * tris.size()];
		int index = 0;
		for (Triple T : tris.values()) {
			alphaTriangles[index++] = T.i;
			alphaTriangles[index++] = T.j;
			alphaTriangles[index++] = T.k;
		}
		return Arrays.copyOf(alphaTriangles, index);
	}

	WB_Coord centroid(int i, int[] tetrahedra) {
		WB_Point c = new WB_Point(points.get(tetrahedra[i]));
		c.addSelf(points.get(tetrahedra[i + 1]));
		c.addSelf(points.get(tetrahedra[i + 2]));
		c.addSelf(points.get(tetrahedra[i + 3]));
		c.mulSelf(0.25);
		return c;
	}

	public int[] getAlphaTriangles(final WB_ScalarParameter a) {
		UnifiedMap<Key, Triple> tris = new UnifiedMap<Key, Triple>();
		WB_Coord c;
		for (int i = 0; i < tetrahedra.length; i += 4) {
			c = centroid(i, tetrahedra);
			if (alpha[i / 4] <= a.evaluate(c.xd(), c.yd(), c.zd())
					|| Double.isNaN(alpha[i / 4])) {
				Key key = new Key(tetrahedra[i], tetrahedra[i + 1],
						tetrahedra[i + 2]);
				Triple triple = new Triple(tetrahedra[i], tetrahedra[i + 1],
						tetrahedra[i + 2]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 1], tetrahedra[i],
						tetrahedra[i + 3]);
				triple = new Triple(tetrahedra[i + 1], tetrahedra[i],
						tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 2], tetrahedra[i + 1],
						tetrahedra[i + 3]);
				triple = new Triple(tetrahedra[i + 2], tetrahedra[i + 1],
						tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i], tetrahedra[i + 2],
						tetrahedra[i + 3]);
				triple = new Triple(tetrahedra[i], tetrahedra[i + 2],
						tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
			}
		}
		int[] alphaTriangles = new int[3 * tris.size()];
		int index = 0;
		for (Triple T : tris.values()) {
			alphaTriangles[index++] = T.i;
			alphaTriangles[index++] = T.j;
			alphaTriangles[index++] = T.k;
		}
		return Arrays.copyOf(alphaTriangles, index);
	}

	private class Triple {
		int i, j, k;

		Triple(final int i, final int j, final int k) {
			this.i = i;
			this.j = j;
			this.k = k;
		}
	}

	private class Key {
		int a, b, c;

		public Key(final int i, final int j, final int k) {
			a = Math.min(Math.min(i, j), k);
			c = Math.max(Math.max(i, j), k);
			if (a == i && c == j) {
				b = k;
			} else if (a == j && c == i) {
				b = k;
			} else if (a == i && c == k) {
				b = j;
			} else if (a == k && c == i) {
				b = j;
			} else if (a == j && c == k) {
				b = i;
			} else if (a == k && c == j) {
				b = i;
			}
		}

		@Override
		public int hashCode() {
			return a << 10 ^ b << 5 ^ c;
		}

		@Override
		public boolean equals(final Object o) {
			if (!(o instanceof Key)) {
				return false;
			}
			Key k = (Key) o;
			return k.a == a && k.b == b && k.c == c;
		}
	}
}