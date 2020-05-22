package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

import wblut.external.QuickHull3D.WB_QuickHull3D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_PointFactory;

/**
 *
 */
public class HEC_ConvexHull extends HEC_Creator {
	/**  */
	private WB_CoordCollection points;
	/**  */
	public HE_IntMap vertexToPointIndex;

	/**
	 *
	 */
	public HEC_ConvexHull() {
		super();
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEC_ConvexHull setPoints(final WB_CoordCollection points) {
		this.points = points;
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEC_ConvexHull setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEC_ConvexHull setPoints(final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEC_ConvexHull setPoints(final double[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEC_ConvexHull setPoints(final float[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEC_ConvexHull setPoints(final int[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param generator
	 * @param numberOfPoints
	 * @return
	 */
	public HEC_ConvexHull setPoints(final WB_PointFactory generator, final int numberOfPoints) {
		this.points = WB_CoordCollection.getCollection(generator, numberOfPoints);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh createBase() {
		if (points == null) {
			return new HE_Mesh();
		}
		final WB_QuickHull3D hull = new WB_QuickHull3D(points.toArray());
		final int[][] faceIndices = hull.getFaces();
		final int[] originalindices = hull.getVertexPointIndices();
		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(hull.getVertices()).setFaces(faceIndices)
				.setCheckDuplicateVertices(false);
		ffl.setCheckNormals(false).setRemoveUnconnectedElements(false);
		final HE_Mesh result = ffl.createBase();
		vertexToPointIndex = new HE_IntMap();
		final Iterator<HE_Vertex> vItr = result.vItr();
		int i = 0;
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			v.setInternalLabel(originalindices[i]);
			vertexToPointIndex.put(v.getKey(), originalindices[i]);
			i++;
		}
		result.removeUnconnectedElements();
		HE_MeshOp.capHalfedges(result);
		return result;
	}
}
