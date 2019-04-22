/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;

/**
 *
 */
public class WB_Triangulation2D {

	/**
	 *
	 */
	private int[] triangles;

	private int[] edges;

	private int[][] neighbors;

	private int high;

	/**
	 *
	 */
	public WB_Triangulation2D() {
	}

	/**
	 *
	 *
	 * @param T
	 * @param E
	 */
	public WB_Triangulation2D(final int[] T, final int[] E) {
		triangles = T;
		edges = E;
	}

	public WB_Triangulation2D(final int[] T) {
		triangles = T;
		if (triangles.length == 0) {
			edges = new int[0];
			neighbors = new int[0][0];
		} else {
			extractEdges(triangles);
		}
	}

	/**
	 *
	 *
	 * @return
	 */
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

	@SuppressWarnings("unchecked")
	private void extractEdges(final int[] tris) {
		high = -1;
		final int f = tris.length;
		final UnifiedMap<Long, int[]> map = new UnifiedMap<Long, int[]>();
		for (int i = 0; i < tris.length; i += 3) {
			final int v0 = tris[i];
			high = Math.max(high, v0);
			final int v1 = tris[i + 1];
			high = Math.max(high, v1);
			final int v2 = tris[i + 2];
			high = Math.max(high, v2);
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
		@SuppressWarnings("rawtypes")
		List[] nn = new ArrayList[high + 1];
		for (i = 0; i <= high; i++) {
			nn[i] = new ArrayList<Integer>();
		}
		i = 0;
		for (final int[] value : values) {
			edges[2 * i] = value[0];
			edges[2 * i + 1] = value[1];
			nn[value[0]].add(value[1]);
			nn[value[1]].add(value[0]);
			i++;
		}
		neighbors = new int[high + 1][];
		for (i = 0; i <= high; i++) {
			neighbors[i] = new int[nn[i].size()];
			for (int j = 0; j < nn[i].size(); j++) {
				neighbors[i][j] = (Integer) nn[i].get(j);
			}
		}

	}

	public int[][] getNeighbors() {
		return neighbors;
	}

	private long getIndex(final int i, final int j, final int f) {
		return i > j ? j + i * f : i + j * f;
	}
}