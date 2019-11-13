/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_VoronoiFactory;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Creates the Voronoi cells of a collection of points, constrained by a mesh.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_VoronoiCells extends HEMC_MultiCreator {
	/** Points. */
	private List<WB_Coord>		points;
	/** Number of points. */
	private int					numberOfPoints;
	/** Container. */
	private HE_Mesh				container;
	/** Treat container as surface?. */
	private boolean				surface;
	/**
	 *
	 */
	private boolean				bruteForce;
	/** Offset. */
	private WB_ScalarParameter	offset;
	public HE_Selection[]		inner;
	public HE_Selection[]		outer;

	/**
	 * Instantiates a new HEMC_VoronoiCells.
	 *
	 */
	public HEMC_VoronoiCells() {
		super();
		offset = WB_ScalarParameter.ZERO;
	}

	/**
	 * Set mesh, defines both points and container.
	 *
	 * @param mesh
	 *            HE_Mesh
	 * @param addCenter
	 *            add mesh center as extra point?
	 * @return self
	 */
	public HEMC_VoronoiCells setMesh(final HE_Mesh mesh,
			final boolean addCenter) {
		if (addCenter) {
			points = new FastList<WB_Coord>();
			points.addAll(mesh.getVertices());
			points.add(HE_MeshOp.getCenter(mesh));
		} else {
			points = new FastList<WB_Coord>();
			points.addAll(mesh.getVertices());
		}
		container = mesh;
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            array of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiCells setPoints(final WB_Coord[] points) {
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
	public HEMC_VoronoiCells setPoints(
			final Collection<? extends WB_Coord> points) {
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
	public HEMC_VoronoiCells setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < n; i++) {
			this.points.add(
					new WB_Point(points[i][0], points[i][1], points[i][2]));
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
	public HEMC_VoronoiCells setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < n; i++) {
			this.points.add(
					new WB_Point(points[i][0], points[i][1], points[i][2]));
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
	public HEMC_VoronoiCells setOffset(final double o) {
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
	public HEMC_VoronoiCells setOffset(final WB_ScalarParameter o) {
		offset = o;
		return this;
	}

	/**
	 * Set enclosing mesh limiting cells.
	 *
	 * @param container
	 *            enclosing mesh
	 * @return self
	 */
	public HEMC_VoronoiCells setContainer(final HE_Mesh container) {
		this.container = container;
		return this;
	}

	/**
	 * Set optional surface mesh mode.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEMC_VoronoiCells setSurface(final boolean b) {
		surface = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEMC_VoronoiCells setBruteForce(final boolean b) {
		bruteForce = b;
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {
		tracker.setStartStatus(this, "Starting HEMC_VoronoiCells");
		if (container == null) {
			_numberOfMeshes = 0;
			return;
		}
		if (points == null) {
			result.add(container.copy());
			_numberOfMeshes = 1;
			return;
		}
		numberOfPoints = points.size();
		final ArrayList<HE_Selection> linnersel = new ArrayList<HE_Selection>();
		final ArrayList<HE_Selection> loutersel = new ArrayList<HE_Selection>();
		final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
		if (bruteForce || numberOfPoints < 10) {
			cvc.setPoints(points).setContainer(container).setSurface(surface)
					.setOffset(offset);
			for (int i = 0; i < numberOfPoints; i++) {
				tracker.setDuringStatus(this,
						"Creating cell " + (i + 1) + " of " + numberOfPoints
								+ " (" + numberOfPoints + " slices).");
				cvc.setCellIndex(i);
				final HE_Mesh mesh = cvc.createBase();
				linnersel.add(cvc.inner);
				loutersel.add(cvc.outer);
				result.add(mesh);
			}
		} else {
			final int[][] voronoiIndices = WB_VoronoiFactory
					.getVoronoi3DNeighbors(points);
			cvc.setPoints(points).setContainer(container).setSurface(surface)
					.setOffset(offset);
			for (int i = 0; i < numberOfPoints; i++) {
				tracker.setDuringStatus(this,
						"Creating cell " + (i + 1) + " of " + numberOfPoints
								+ " (" + voronoiIndices[i].length
								+ " slices).");
				System.out.println("Creating cell " + (i + 1) + " of "
						+ numberOfPoints + " (" + voronoiIndices[i].length
						+ " slices).");
				if (voronoiIndices[i].length == 0) {
					cvc.setCellIndex(i);
				} else {
					cvc.setLimitPoints(true);
					cvc.setCellIndex(i);
					cvc.setPointsToUse(voronoiIndices[i]);
				}
				final HE_Mesh mesh = cvc.createBase();
				linnersel.add(cvc.inner);
				loutersel.add(cvc.outer);
				result.add(mesh);
			}
		}
		inner = new HE_Selection[result.size()];
		outer = new HE_Selection[result.size()];
		for (int i = 0; i < _numberOfMeshes; i++) {
			inner[i] = linnersel.get(i);
			outer[i] = loutersel.get(i);
		}
		_numberOfMeshes = result.size();
		tracker.setStopStatus(this, "Exiting HEMC_VoronoiCells.");
	}
}
