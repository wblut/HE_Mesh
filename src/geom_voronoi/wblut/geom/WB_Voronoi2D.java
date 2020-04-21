package wblut.geom;

import java.util.List;

public class WB_Voronoi2D {
	private final WB_List<WB_VoronoiCell2D> cells;

	public WB_Voronoi2D(final List<WB_VoronoiCell2D> cells) {
		this.cells = new WB_List<>(cells);
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
