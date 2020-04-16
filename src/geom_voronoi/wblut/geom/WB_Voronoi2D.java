package wblut.geom;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_Voronoi2D {
	private final FastList<WB_VoronoiCell2D> cells;

	public WB_Voronoi2D(final List<WB_VoronoiCell2D> cells) {
		this.cells = new FastList<>(cells);
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
