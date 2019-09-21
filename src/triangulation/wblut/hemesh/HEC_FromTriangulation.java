/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Triangulation2D;
import wblut.geom.WB_Triangulation2DWithPoints;

/**
 * Creates a new mesh from a triangulation.
 *
 * the generic type
 *
 * @author Frederik Vanhoutte (W:Blut)
 */
public class HEC_FromTriangulation extends HEC_Creator {
	/** Source triangles. */
	WB_Triangulation2D tri;
	private WB_CoordCollection points;

	/**
	 *
	 */
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

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {

		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(points).setFaces(tri.getTriangles())
				.setCheckDuplicateVertices(false);
		return ffl.createBase();

	}
}
