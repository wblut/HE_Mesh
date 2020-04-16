package wblut.geom;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_Voronoi3D {
	private final FastList<WB_VoronoiCell3D> cells;

	public WB_Voronoi3D(final List<WB_VoronoiCell3D> cells) {
		this.cells = new FastList<>(cells);
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
