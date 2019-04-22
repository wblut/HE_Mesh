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
public class WB_Voronoi3D {
	/**
	 *
	 */
	private FastList<WB_VoronoiCell3D> cells;

	public WB_Voronoi3D(final List<WB_VoronoiCell3D> cells) {
		this.cells = new FastList<WB_VoronoiCell3D>(cells);
	}

	public List<WB_VoronoiCell3D> getCells() {
		return cells.asUnmodifiable();
	}

	public WB_VoronoiCell3D getCell(final int i) {
		return cells.get(i);
	}

	public int size() {
		return cells.size();
	}

}
