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
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_VoronoiFactory;
import wblut.geom.WB_VoronoiCell3D;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Creates the Voronoi cells of a collection of points, constrained by a box.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_VoronoiBox extends HEMC_MultiCreator {
	/** Points. */
	private List<WB_Coord> points;
	/** Number of points. */
	private int numberOfPoints;
	/** Container. */
	private WB_AABB aabb;

	private boolean bruteForce;
	/** Offset. */
	private WB_ScalarParameter offset;

	/**
	 *
	 */
	public HEMC_VoronoiBox() {
		super();
		offset = WB_ScalarParameter.ZERO;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            array of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiBox setPoints(final WB_Coord[] points) {
		this.points = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			this.points.add(p);
		}
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            collection of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiBox setPoints(final Collection<? extends WB_Coord> points) {
		this.points = new FastList<WB_Coord>();
		this.points.addAll(points);
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            2D array of double of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiBox setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            2D array of float of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiBox setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	/**
	 * Set voronoi cell offset.
	 *
	 * @param o
	 *            offset
	 * @return self
	 */
	public HEMC_VoronoiBox setOffset(final double o) {
		offset = new WB_ConstantScalarParameter(o);
		return this;
	}

	/**
	 * Set voronoi cell offset.
	 *
	 * @param o
	 *            offset
	 * @return self
	 */
	public HEMC_VoronoiBox setOffset(final WB_ScalarParameter o) {
		offset = o;
		return this;
	}

	/**
	 * Set enclosing box limiting cells.
	 *
	 * @param container
	 *            enclosing WB_AABB
	 * @return self
	 */
	public HEMC_VoronoiBox setContainer(final WB_AABB container) {
		this.aabb = container;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEMC_VoronoiBox setBruteForce(final boolean b) {
		bruteForce = b;
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {
		tracker.setStartStatus(this, "Starting HEMC_VoronoiBox");

		if (aabb == null) {
			_numberOfMeshes = 0;
			return;
		}
		if (points == null) {
			result.add(new HE_Mesh(new HEC_Box().setFromAABB(aabb)));
			_numberOfMeshes = 1;
			return;
		}

		numberOfPoints = points.size();
		tracker.setDuringStatus(this, "Calculating Voronoi cells.");
		List<WB_VoronoiCell3D> voronoi = bruteForce
				? WB_VoronoiFactory.getVoronoi3DBruteForce(points, numberOfPoints, aabb, offset).getCells()
				: WB_VoronoiFactory.getVoronoi3D(points, numberOfPoints, aabb, offset).getCells();

		WB_ProgressCounter counter = new WB_ProgressCounter(voronoi.size(), 10);
		tracker.setCounterStatus(this, "Creating cell mesh.", counter);
		for (WB_VoronoiCell3D vor : voronoi) {
			HE_Mesh m = new HE_Mesh(vor.getMesh());
			m.setInternalLabel(vor.getIndex());
			m.setUserLabel(vor.getIndex());
			result.add(m);
			counter.increment();

		}

		_numberOfMeshes = result.size();
		tracker.setStopStatus(this, "Exiting HEMC_VoronoiBox.");
	}

}
