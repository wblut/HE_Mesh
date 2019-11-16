/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import wblut.external.QuickHull3D.WB_QuickHull3D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_PointGenerator;

/**
 * Creates the convex hull of a collection of points.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_ConvexHull extends HEC_Creator {
	/** Points. */
	private WB_CoordCollection points;
	public Map<Long, Integer>	vertexToPointIndex;

	/**
	 * Instantiates a new HEC_ConvexHull.
	 *
	 */
	public HEC_ConvexHull() {
		super();
		setOverride(true);
		
	}

	/**
	 * Set points that define vertices.
	 *
	 * @param points
	 *            array of vertex positions
	 * @return self
	 */
	public HEC_ConvexHull setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 * Set points that define vertices.
	 *
	 * @param points
	 *            any Collection of vertex positions
	 * @return self
	 */
	public HEC_ConvexHull setPoints(
			final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	

	/**
	 * Set points that define vertices.
	 *
	 * @param points
	 *            2D array of double of vertex positions
	 * @return self
	 */
	public HEC_ConvexHull setPoints(final double[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 * Set points that define vertices.
	 *
	 * @param points
	 *            2D array of float of vertex positions
	 * @return self
	 */
	public HEC_ConvexHull setPoints(final float[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 * Set points that define vertices.
	 *
	 * @param points
	 *            2D array of float of vertex positions
	 * @return self
	 */
	public HEC_ConvexHull setPoints(final int[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}
	
	public HEC_ConvexHull setPoints(WB_PointGenerator generator, final int numberOfPoints) {
		this.points = WB_CoordCollection.getCollection(generator, numberOfPoints);
		return this;
	}


	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		if (points == null) {
			return new HE_Mesh();
		}

		final WB_QuickHull3D hull = new WB_QuickHull3D(points.toArray());
		final int[][] faceIndices = hull.getFaces();
		final int[] originalindices = hull.getVertexPointIndices();
		final HEC_FromFacelist ffl = new HEC_FromFacelist()
				.setVertices(hull.getVertices()).setFaces(faceIndices)
				.setCheckDuplicateVertices(false);
		ffl.setCheckNormals(false).setRemoveUnconnectedElements(false);
				
		final HE_Mesh result = ffl.createBase();
		vertexToPointIndex = new UnifiedMap<Long, Integer>();
		final Iterator<HE_Vertex> vItr = result.vItr();
		int i = 0;
		HE_Vertex v;
		while (vItr.hasNext()) {
			v=vItr.next();
			v.setInternalLabel(originalindices[i]);
			vertexToPointIndex.put(v.getKey(), originalindices[i]);
			i++;
		}
		result.removeUnconnectedElements();
		HE_MeshOp.capHalfedges(result);
		return result;
	}

	
}
