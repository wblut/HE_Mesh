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
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;

/**
 * Creates the Voronoi cell of a collection of points, constrained by a maximum
 * radius.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_VoronoiSpheres extends HEMC_MultiCreator {
	/** Points. */
	private List<WB_Coord> points;
	/** Number of points. */
	private int numberOfPoints;
	/** Level of geodesic sphere in exact mode. */
	private int level;
	/** Number of tracer points in approximate mode. */
	private int numTracers;
	/** Starting trace step in approximate mode?. */
	private double traceStep;
	/** Maximum radius. */
	private double cutoff;
	/** Approximate mode?. */
	private boolean approx;
	private double offset;

	/**
	 * Instantiates a new HEMC_VoronoiSpheres.
	 *
	 */
	public HEMC_VoronoiSpheres() {
		super();
		level = 1;
		traceStep = 10;
		numTracers = 100;
	}

	/**
	 * Set level of geodesic sphere in each cell.
	 *
	 * @param l
	 *            recursive level
	 * @return self
	 */
	public HEMC_VoronoiSpheres setLevel(final int l) {
		level = l;
		return this;
	}

	/**
	 * Set number of tracer points to use in approximate model.
	 *
	 * @param n
	 *            number of tracer points
	 * @return self
	 */
	public HEMC_VoronoiSpheres setNumTracers(final int n) {
		numTracers = n;
		return this;
	}

	/**
	 * Set initial trace step size.
	 *
	 * @param d
	 *            trace step
	 * @return self
	 */
	public HEMC_VoronoiSpheres setTraceStep(final double d) {
		traceStep = d;
		return this;
	}

	/**
	 * Set maximum radius of cell.
	 *
	 * @param c
	 *            cutoff radius
	 * @return self
	 */
	public HEMC_VoronoiSpheres setCutoff(final double c) {
		cutoff = Math.abs(c);
		return this;
	}

	/**
	 * Set approximate mode.
	 *
	 * @param a
	 *            true, false
	 * @return self
	 */
	public HEMC_VoronoiSpheres setApprox(final boolean a) {
		approx = a;
		return this;
	}

	public HEMC_VoronoiSpheres setOffset(final double o) {
		offset = o;
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            collection of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiSpheres setPoints(final Collection<? extends WB_Coord> points) {
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
	public HEMC_VoronoiSpheres setPoints(final double[][] points) {
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
	public HEMC_VoronoiSpheres setPoints(final float[][] points) {
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
	 *            array of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiSpheres setPoints(final WB_Coord[] points) {
		this.points = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			this.points.add(p);
		}
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {

		if (points == null) {
			_numberOfMeshes = 0;
			return;
		}

		numberOfPoints = points.size();

		final HEC_VoronoiSphere cvc = new HEC_VoronoiSphere();
		cvc.setPoints(points).setLevel(level).setCutoff(cutoff).setApprox(approx).setNumTracers(numTracers)
				.setTraceStep(traceStep).setOffset(offset);
		for (int i = 0; i < numberOfPoints; i++) {
			System.out.println("HEMC_VoronoiSpheres: creating cell " + (i + 1) + " of " + numberOfPoints + ".");
			cvc.setCellIndex(i);
			final HE_Mesh mesh = cvc.createBase();
			if (mesh.getNumberOfVertices() > 0) {
				result.add(mesh);
			}
		}
		_numberOfMeshes = result.size();

	}
}
