package wblut.geom;

import java.util.List;

/**
 *
 */
public class WB_Voronoi2D {
	/**  */
	private final WB_List<WB_VoronoiCell2D> cells;

	/**
	 *
	 *
	 * @param cells
	 */
	public WB_Voronoi2D(final List<WB_VoronoiCell2D> cells) {
		this.cells = new WB_List<>(cells);
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_VoronoiCell2D> getCells() {
		return cells.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_VoronoiCell2D getCell(final int i) {
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
