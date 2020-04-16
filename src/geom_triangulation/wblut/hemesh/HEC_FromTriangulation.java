package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Triangulation2D;
import wblut.geom.WB_Triangulation2DWithPoints;

public class HEC_FromTriangulation extends HEC_Creator {
	WB_Triangulation2D tri;
	private WB_CoordCollection points;

	public HEC_FromTriangulation() {
		super();
		setOverride(true);
	}

	public HEC_FromTriangulation setTriangulation(final WB_Triangulation2D tri) {
		this.tri = tri;
		return this;
	}

	public HEC_FromTriangulation setTriangulation(final WB_Triangulation2DWithPoints tri) {
		this.tri = tri;
		this.points = tri.getPoints();
		return this;
	}

	public HEC_FromTriangulation setPoints(final WB_CoordCollection points) {
		this.points = points;
		return this;
	}

	public HEC_FromTriangulation setPoints(final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	public HEC_FromTriangulation setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(points).setFaces(tri.getTriangles())
				.setCheckDuplicateVertices(false);
		return ffl.createBase();
	}
}
