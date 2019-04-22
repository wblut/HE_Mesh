/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 *
 */
public class WB_Voronoi2D {
	/**
	 *
	 */
	private FastList<WB_VoronoiCell2D> cells;

	public WB_Voronoi2D(final List<WB_VoronoiCell2D> cells) {
		this.cells = new FastList<WB_VoronoiCell2D>(cells);
	}

	public List<WB_VoronoiCell2D> getCells() {
		return cells.asUnmodifiable();
	}

	public WB_VoronoiCell2D getCell(final int i) {
		return cells.get(i);
	}

	public int size() {
		return cells.size();
	}

}
