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
import java.util.List;

/**
 *
 */
public class WB_Triangulation3D {

	/**
	 *
	 */
	private int[] _tetrahedra;

	private int[] _triangles;

	private WB_CoordCollection _points;

	/**
	 *
	 */
	private int[] _edges;

	private int[][] neighbors;

	private int high;

	/**
	 *
	 */
	@SuppressWarnings("unused")
	private WB_Triangulation3D() {
	}

	/**
	 *
	 * @param tetra
	 * @param triangles
	 * @param edges
	 */
	public WB_Triangulation3D(final WB_CoordCollection points, final int[] tetra, final int[] triangles,
			final int[] edges) {
		_points = points;
		_tetrahedra = tetra;
		_triangles = triangles;
		_edges = edges;
		extractNeighbors(_edges);
	}

	public WB_CoordCollection getPoints() {
		return _points;
	}

	public int[] getTetrahedra() {
		return _tetrahedra;
	}

	public int[] getTriangles() {
		return _triangles;
	}

	public int[] getEdges() {
		return _edges;
	}

	public int[][] getNeighbors() {
		return neighbors;
	}

	@SuppressWarnings("unchecked")
	private void extractNeighbors(final int[] edges) {
		high = -1;
		for (int i = 0; i < edges.length; i += 2) {
			final int v0 = edges[i];
			high = Math.max(high, v0);
			final int v1 = edges[i + 1];
			high = Math.max(high, v1);
		}

		int i = 0;
		@SuppressWarnings("rawtypes")
		List[] nn = new ArrayList[high + 1];
		for (i = 0; i <= high; i++) {
			nn[i] = new ArrayList<Integer>();
		}
		i = 0;
		for (i = 0; i < edges.length; i += 2) {
			nn[edges[i]].add(edges[i + 1]);
			nn[edges[i + 1]].add(edges[i]);
		}
		neighbors = new int[high + 1][];
		for (i = 0; i <= high; i++) {
			neighbors[i] = new int[nn[i].size()];
			for (int j = 0; j < nn[i].size(); j++) {
				neighbors[i][j] = (Integer) nn[i].get(j);
			}
		}

	}

	public WB_Network getNetwork() {
		return new WB_Network(getPoints(), getEdges());

	}

}