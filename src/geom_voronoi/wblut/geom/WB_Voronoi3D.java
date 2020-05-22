package wblut.geom;

import java.util.List;

/**
 *
 */
public class WB_Voronoi3D {
	/**  */
	private final WB_List<WB_VoronoiCell3D> cells;

	/**
	 *
	 *
	 * @param cells
	 */
	public WB_Voronoi3D(final List<WB_VoronoiCell3D> cells) {
		this.cells = new WB_List<>(cells);
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_VoronoiCell3D> getCells() {
		return cells.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_VoronoiCell3D getCell(final int i) {
		return cells.get(i);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int size() {
		return cells.size();
	}
}
