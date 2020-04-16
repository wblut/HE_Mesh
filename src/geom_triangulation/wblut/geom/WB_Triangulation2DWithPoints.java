package wblut.geom;

import java.util.List;

public class WB_Triangulation2DWithPoints extends WB_Triangulation2D {
	private WB_CoordCollection _points;

	public WB_Triangulation2DWithPoints() {
	}

	public WB_Triangulation2DWithPoints(final int[] T, final int[] E, final WB_CoordCollection P) {
		super(T, E);
		_points = P;
	}

	public WB_Triangulation2DWithPoints(final int[] T, final int[] E, final List<? extends WB_Coord> P) {
		super(T, E);
		_points = WB_CoordCollection.getCollection(P);
	}

	public WB_Triangulation2DWithPoints(final int[] T, final int[] E, final WB_Coord[] P) {
		super(T, E);
		_points = WB_CoordCollection.getCollection(P);
	}

	public WB_Triangulation2DWithPoints(final int[] T, final WB_CoordCollection P) {
		super(T);
		_points = P;
	}

	public WB_Triangulation2DWithPoints(final int[] T, final List<? extends WB_Coord> P) {
		super(T);
		_points = WB_CoordCollection.getCollection(P);
	}

	public WB_Triangulation2DWithPoints(final int[] T, final WB_Coord[] P) {
		super(T);
		_points = WB_CoordCollection.getCollection(P);
	}

	protected WB_Triangulation2DWithPoints(final WB_Triangulation2D tri) {
		super(tri.getTriangles(), tri.getEdges());
		_points = null;
	}

	public WB_CoordCollection getPoints() {
		return _points;
	}
}