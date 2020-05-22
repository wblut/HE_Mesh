package wblut.geom;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import wblut.hemesh.HE_ObjectMap;

/**
 *
 */
public class WB_AlphaTriangulation2D implements WB_TriangleSource {
	/**  */
	private final int[] triangles;
	/**  */
	private int[] edges;
	/**  */
	private double[] alpha;
	/**  */
	private final WB_CoordList points;

	/**
	 *
	 *
	 * @param tris
	 * @param points
	 */
	public WB_AlphaTriangulation2D(final int[] tris, final Collection<? extends WB_Coord> points) {
		triangles = Arrays.copyOf(tris, tris.length);
		this.points = new WB_CoordList();
		this.points.addAll(points);
		setAlpha();
		if (triangles.length == 0) {
			edges = new int[0];
		} else {
			extractEdges(triangles);
		}
	}

	/**
	 *
	 *
	 * @param tris
	 * @param points
	 */
	public WB_AlphaTriangulation2D(final int[][] tris, final Collection<? extends WB_Coord> points) {
		triangles = new int[tris.length * 4];
		for (int i = 0; i < tris.length; i++) {
			triangles[4 * i] = tris[i][0];
			triangles[4 * i + 1] = tris[i][1];
			triangles[4 * i + 2] = tris[i][2];
			triangles[4 * i + 3] = tris[i][3];
		}
		this.points = new WB_CoordList();
		this.points.addAll(points);
		setAlpha();
		if (triangles.length == 0) {
			edges = new int[0];
		} else {
			extractEdges(triangles);
		}
	}

	/**
	 *
	 *
	 * @param tris
	 * @param points
	 */
	public WB_AlphaTriangulation2D(final int[] tris, final WB_Coord[] points) {
		triangles = Arrays.copyOf(tris, tris.length);
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		setAlpha();
		if (triangles.length == 0) {
			edges = new int[0];
		} else {
			extractEdges(triangles);
		}
	}

	/**
	 *
	 *
	 * @param tris
	 * @param points
	 */
	public WB_AlphaTriangulation2D(final int[][] tris, final WB_Coord[] points) {
		triangles = new int[tris.length * 3];
		for (int i = 0; i < tris.length; i++) {
			triangles[3 * i] = tris[i][0];
			triangles[3 * i + 1] = tris[i][1];
			triangles[3 * i + 2] = tris[i][2];
		}
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		setAlpha();
		if (triangles.length == 0) {
			edges = new int[0];
		} else {
			extractEdges(triangles);
		}
	}

	/**
	 *
	 */
	private void setAlpha() {
		alpha = new double[triangles.length / 3];
		int index = 0;
		for (int i = 0; i < triangles.length; i += 3) {
			alpha[index++] = WB_Predicates.circumradius2D(points.get(triangles[i]), points.get(triangles[i + 1]),
					points.get(triangles[i + 2]));
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int[] getTriangles() {
		return triangles;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int[] getEdges() {
		return edges;
	}

	/**
	 *
	 *
	 * @param tris
	 */
	private void extractEdges(final int[] tris) {
		final int f = tris.length;
		final HE_ObjectMap<int[]> map = new HE_ObjectMap<>();
		for (int i = 0; i < tris.length; i += 3) {
			final int v0 = tris[i];
			final int v1 = tris[i + 1];
			final int v2 = tris[i + 2];
			long index = getIndex(v0, v1, f);
			map.put(index, new int[] { v0, v1 });
			index = getIndex(v1, v2, f);
			map.put(index, new int[] { v1, v2 });
			index = getIndex(v2, v0, f);
			map.put(index, new int[] { v2, v0 });
		}
		edges = new int[2 * map.size()];
		final Collection<int[]> values = map.values();
		int i = 0;
		for (final int[] value : values) {
			edges[2 * i] = value[0];
			edges[2 * i + 1] = value[1];
			i++;
		}
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param f
	 * @return
	 */
	private long getIndex(final int i, final int j, final int f) {
		return i > j ? j + i * f : i + j * f;
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
	public double[] getAlpha() {
		return Arrays.copyOf(alpha, alpha.length);
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public int[] getAlphaTriangles(final double a) {
		final int[] alphaTriangles = new int[triangles.length];
		int index = 0;
		for (int i = 0; i < triangles.length; i += 3) {
			if (alpha[i / 3] <= a) {
				alphaTriangles[index++] = triangles[i + 0];
				alphaTriangles[index++] = triangles[i + 1];
				alphaTriangles[index++] = triangles[i + 2];
			}
		}
		return Arrays.copyOf(alphaTriangles, index);
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public int[] getAlphaEdges(final double a) {
		final UnifiedMap<Key, Tuple> edges = new UnifiedMap<>();
		for (int i = 0; i < triangles.length; i += 3) {
			if (alpha[i / 3] <= a) {
				Key key = new Key(triangles[i], triangles[i + 1]);
				Tuple doubleId = new Tuple(triangles[i], triangles[i + 1]);
				if (edges.get(key) == null) {
					edges.put(key, doubleId);
				} else {
					edges.remove(key);
				}
				key = new Key(triangles[i + 1], triangles[i + 2]);
				doubleId = new Tuple(triangles[i + 1], triangles[i + 2]);
				if (edges.get(key) == null) {
					edges.put(key, doubleId);
				} else {
					edges.remove(key);
				}
				key = new Key(triangles[i + 2], triangles[i]);
				doubleId = new Tuple(triangles[i + 2], triangles[i]);
				if (edges.get(key) == null) {
					edges.put(key, doubleId);
				} else {
					edges.remove(key);
				}
			}
		}
		final int[] alphaEdges = new int[2 * edges.size()];
		int index = 0;
		for (final Tuple T : edges.values()) {
			alphaEdges[index++] = T.i;
			alphaEdges[index++] = T.j;
		}
		return Arrays.copyOf(alphaEdges, index);
	}

	/**
	 *
	 */
	private class Tuple {
		/**  */
		int i, j;

		/**
		 *
		 *
		 * @param i
		 * @param j
		 */
		Tuple(final int i, final int j) {
			this.i = i;
			this.j = j;
		}
	}

	/**
	 *
	 */
	private class Key {
		/**  */
		int a, b;

		/**
		 *
		 *
		 * @param i
		 * @param j
		 */
		public Key(final int i, final int j) {
			a = Math.min(i, j);
			b = Math.max(i, j);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int hashCode() {
			return a << 5 ^ b;
		}

		/**
		 *
		 *
		 * @param o
		 * @return
		 */
		@Override
		public boolean equals(final Object o) {
			if (!(o instanceof Key)) {
				return false;
			}
			final Key k = (Key) o;
			return k.a == a && k.b == b;
		}
	}
}