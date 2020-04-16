package wblut.geom;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import wblut.math.WB_ScalarParameter;

public class WB_AlphaTriangulation3D {
	private final int[] tetrahedra;
	private double[] alpha;
	private final WB_CoordList points;

	public WB_AlphaTriangulation3D(final int[] tetra, final WB_CoordCollection points) {
		tetrahedra = Arrays.copyOf(tetra, tetra.length);
		this.points = new WB_CoordList();
		for (int i = 0; i < points.size(); i++) {
			this.points.add(points.get(i));
		}
		setAlpha();
	}

	public WB_AlphaTriangulation3D(final int[][] tetra, final WB_CoordCollection points) {
		tetrahedra = new int[tetra.length * 4];
		for (int i = 0; i < tetra.length; i++) {
			tetrahedra[4 * i] = tetra[i][0];
			tetrahedra[4 * i + 1] = tetra[i][1];
			tetrahedra[4 * i + 2] = tetra[i][2];
			tetrahedra[4 * i + 3] = tetra[i][3];
		}
		this.points = new WB_CoordList();
		for (int i = 0; i < points.size(); i++) {
			this.points.add(points.get(i));
		}
		setAlpha();
	}

	public WB_AlphaTriangulation3D(final int[] tetra, final Collection<? extends WB_Coord> points) {
		tetrahedra = Arrays.copyOf(tetra, tetra.length);
		this.points = new WB_CoordList();
		this.points.addAll(points);
		setAlpha();
	}

	public WB_AlphaTriangulation3D(final int[][] tetra, final Collection<? extends WB_Coord> points) {
		tetrahedra = new int[tetra.length * 4];
		for (int i = 0; i < tetra.length; i++) {
			tetrahedra[4 * i] = tetra[i][0];
			tetrahedra[4 * i + 1] = tetra[i][1];
			tetrahedra[4 * i + 2] = tetra[i][2];
			tetrahedra[4 * i + 3] = tetra[i][3];
		}
		this.points = new WB_CoordList();
		this.points.addAll(points);
		setAlpha();
	}

	public WB_AlphaTriangulation3D(final int[] tetra, final WB_Coord[] points) {
		tetrahedra = Arrays.copyOf(tetra, tetra.length);
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		setAlpha();
	}

	public WB_AlphaTriangulation3D(final int[][] tetra, final WB_Coord[] points) {
		tetrahedra = new int[tetra.length * 4];
		for (int i = 0; i < tetra.length; i++) {
			tetrahedra[4 * i] = tetra[i][0];
			tetrahedra[4 * i + 1] = tetra[i][1];
			tetrahedra[4 * i + 2] = tetra[i][2];
			tetrahedra[4 * i + 3] = tetra[i][3];
		}
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		setAlpha();
	}

	private void setAlpha() {
		alpha = new double[tetrahedra.length / 4];
		int index = 0;
		for (int i = 0; i < tetrahedra.length; i += 4) {
			alpha[index++] = WB_Predicates.circumradius3D(points.get(tetrahedra[i]), points.get(tetrahedra[i + 1]),
					points.get(tetrahedra[i + 2]), points.get(tetrahedra[i + 3]));
		}
	}

	public int[] getTetrahedra() {
		return tetrahedra;
	}

	public List<WB_Coord> getPoints() {
		return points.asUnmodifiable();
	}

	public double[] getAlpha() {
		return Arrays.copyOf(alpha, alpha.length);
	}

	public int[] getAlphaTetrahedra(final double a) {
		final int[] alphaTetrahedra = new int[tetrahedra.length];
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

	public int[] getAlphaTriangles(final double a) {
		final WB_IndexedObjectMap<Triple> tris = new WB_IndexedObjectMap<>();
		for (int i = 0; i < tetrahedra.length; i += 4) {
			if (alpha[i / 4] <= a || Double.isNaN(alpha[i / 4])) {
				int key = new Key(tetrahedra[i], tetrahedra[i + 1], tetrahedra[i + 2]).hashCode();
				Triple triple = new Triple(tetrahedra[i], tetrahedra[i + 1], tetrahedra[i + 2]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 1], tetrahedra[i], tetrahedra[i + 3]).hashCode();
				triple = new Triple(tetrahedra[i + 1], tetrahedra[i], tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 2], tetrahedra[i + 1], tetrahedra[i + 3]).hashCode();
				triple = new Triple(tetrahedra[i + 2], tetrahedra[i + 1], tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i], tetrahedra[i + 2], tetrahedra[i + 3]).hashCode();
				triple = new Triple(tetrahedra[i], tetrahedra[i + 2], tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
			}
		}
		final int[] alphaTriangles = new int[3 * tris.size()];
		int index = 0;
		for (final Triple T : tris.values()) {
			alphaTriangles[index++] = T.i;
			alphaTriangles[index++] = T.j;
			alphaTriangles[index++] = T.k;
		}
		return Arrays.copyOf(alphaTriangles, index);
	}

	WB_Coord centroid(final int i, final int[] tetrahedra) {
		final WB_Point c = new WB_Point(points.get(tetrahedra[i]));
		c.addSelf(points.get(tetrahedra[i + 1]));
		c.addSelf(points.get(tetrahedra[i + 2]));
		c.addSelf(points.get(tetrahedra[i + 3]));
		c.mulSelf(0.25);
		return c;
	}

	public int[] getAlphaTriangles(final WB_ScalarParameter a) {
		final WB_IndexedObjectMap<Triple> tris = new WB_IndexedObjectMap<>();
		WB_Coord c;
		for (int i = 0; i < tetrahedra.length; i += 4) {
			c = centroid(i, tetrahedra);
			if (alpha[i / 4] <= a.evaluate(c.xd(), c.yd(), c.zd()) || Double.isNaN(alpha[i / 4])) {
				int key = new Key(tetrahedra[i], tetrahedra[i + 1], tetrahedra[i + 2]).hashCode();
				Triple triple = new Triple(tetrahedra[i], tetrahedra[i + 1], tetrahedra[i + 2]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 1], tetrahedra[i], tetrahedra[i + 3]).hashCode();
				triple = new Triple(tetrahedra[i + 1], tetrahedra[i], tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i + 2], tetrahedra[i + 1], tetrahedra[i + 3]).hashCode();
				triple = new Triple(tetrahedra[i + 2], tetrahedra[i + 1], tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
				key = new Key(tetrahedra[i], tetrahedra[i + 2], tetrahedra[i + 3]).hashCode();
				triple = new Triple(tetrahedra[i], tetrahedra[i + 2], tetrahedra[i + 3]);
				if (tris.get(key) == null) {
					tris.put(key, triple);
				} else {
					tris.remove(key);
				}
			}
		}
		final int[] alphaTriangles = new int[3 * tris.size()];
		int index = 0;
		for (final Triple T : tris.values()) {
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
			final Key k = (Key) o;
			return k.a == a && k.b == b && k.c == c;
		}
	}
}