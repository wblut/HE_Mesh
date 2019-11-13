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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_AABB;
import wblut.geom.WB_AABBTree3D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_VoronoiFactory;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Creates the Voronoi cells of a collection of points, constrained by a mesh.
 * This creator tries to optimize by first creating the Voronoi cells of the
 * enclosing box. If the cell crosses the container boundary, it is regenerated
 * using the full container.
 *
 * Limitations:
 *
 * - this creator does not generate the necessary information for
 * HEMC_FromVoronoiCells. - intersection tests are only vertex based. Large
 * cells, or thin container geometry can lead to wrongly classifying a cell as
 * non-crossing. All vertices can be outside the mesh but still describe an
 * intersecting volume.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_VoronoiCellsPre extends HEMC_MultiCreator {
	/** Points. */
	private List<WB_Coord>		points;
	/** Container. */
	private HE_Mesh				container;
	/**
	 *
	 */
	private boolean				bruteForce;
	/** Offset. */
	private WB_ScalarParameter	offset;
	public HE_Selection[]		inner;
	public HE_Selection[]		outer;

	/**
	 *
	 */
	public HEMC_VoronoiCellsPre() {
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
	public HEMC_VoronoiCellsPre setMesh(final HE_Mesh mesh,
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
	public HEMC_VoronoiCellsPre setPoints(final WB_Coord[] points) {
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
	public HEMC_VoronoiCellsPre setPoints(
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
	public HEMC_VoronoiCellsPre setPoints(final double[][] points) {
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
	public HEMC_VoronoiCellsPre setPoints(final float[][] points) {
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
	public HEMC_VoronoiCellsPre setOffset(final double o) {
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
	public HEMC_VoronoiCellsPre setOffset(final WB_ScalarParameter o) {
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
	public HEMC_VoronoiCellsPre setContainer(final HE_Mesh container) {
		this.container = container;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEMC_VoronoiCellsPre setBruteForce(final boolean b) {
		bruteForce = b;
		return this;
	}

	class VorResult {
		HE_Mesh			mesh;
		HE_Selection	inner;
		HE_Selection	outer;

		VorResult(final HE_Mesh mesh, final HE_Selection inner,
				final HE_Selection outer) {
			this.mesh = mesh;
			this.inner = inner;
			this.outer = outer;
		}
	}

	/**
	 *
	 */
	class CellRunner implements Callable<VorResult> {
		int		index;
		int[]	indices;

		CellRunner(final int index, final int[] indices) {
			this.index = index;
			this.indices = indices;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public VorResult call() {
			final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
			cvc.setPoints(points).setContainer(container).setOffset(offset)
					.setLimitPoints(true);
			cvc.setCellIndex(index);
			cvc.setPointsToUse(indices);
			return new VorResult(cvc.createBase(), cvc.inner, cvc.outer);
		}
	}

	@Override
	void create(final HE_MeshCollection result) {
		tracker.setStartStatus(this, "Starting HEMC_VoronoiCellsPre");
		if (container == null) {
			_numberOfMeshes = 0;
			return;
		}
		if (points == null) {
			result.add(container.copy());
			_numberOfMeshes = 1;
			return;
		}
		HEMC_VoronoiBox multiCreator = new HEMC_VoronoiBox();
		multiCreator.setPoints(points);
		multiCreator.setContainer(HE_MeshOp.getAABB(container));
		multiCreator.setOffset(offset);
		multiCreator.setBruteForce(bruteForce);
		HE_MeshCollection cells = multiCreator.create();
		int[][] indices = WB_VoronoiFactory.getVoronoi3DNeighbors(points);
		final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
		cvc.setPoints(points).setContainer(container).setOffset(offset)
				.setLimitPoints(true);
		WB_AABBTree3D tree = new WB_AABBTree3D(container, 1);
		final ArrayList<HE_Selection> linnersel = new ArrayList<HE_Selection>();
		final ArrayList<HE_Selection> loutersel = new ArrayList<HE_Selection>();
		HE_MeshIterator mItr = cells.mItr();
		HE_Mesh m;
		int i = 0;
		List<Integer> surfaceCells = new ArrayList<Integer>();
		while (mItr.hasNext()) {
			m = mItr.next();
			HE_VertexIterator vItr = m.vItr();
			HE_Vertex v;
			boolean in = false;
			while (vItr.hasNext()) {
				v = vItr.next();
				in = HE_MeshOp.isInside(tree, v);
				if (in) {
					break;
				}
			}
			WB_AABB maabb = HE_MeshOp.getAABB(m);
			boolean intersects = WB_GeometryOp.checkIntersection3D(maabb, tree);
			if (!in && !intersects) {
				tracker.setDuringStatus(this, "Ignoring external cell "
						+ (i + 1) + " of " + cells.size() + ".");
			} else if (!intersects) {
				tracker.setDuringStatus(this, "Creating internal cell "
						+ (i + 1) + " of " + cells.size() + ".");
				result.add(m);
				linnersel.add(m.selectAllFaces());
				loutersel.add(HE_Selection.getSelection(m));
			} else {
				tracker.setDuringStatus(this, "Qeueing surface cell " + (i + 1)
						+ " of " + cells.size() + ".");
				int index = m.getInternalLabel();
				surfaceCells.add(index);
			}
			i++;
		}
		try {
			int threadCount = Runtime.getRuntime().availableProcessors();
			final ExecutorService executor = Executors
					.newFixedThreadPool(threadCount);
			final List<Future<VorResult>> list = new ArrayList<Future<VorResult>>();
			i = 0;
			for (i = 0; i < surfaceCells.size(); i++) {
				final Callable<VorResult> runner = new CellRunner(
						surfaceCells.get(i), indices[surfaceCells.get(i)]);
				list.add(executor.submit(runner));
			}
			for (Future<VorResult> future : list) {
				VorResult vr = future.get();
				HE_Mesh cell = vr.mesh;
				result.add(cell);
				linnersel.add(vr.inner);
				loutersel.add(vr.outer);
				tracker.setDuringStatus(this, "Retrieving surface cell "
						+ cell.getInternalLabel() + ".");
			}
			executor.shutdown();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}
		_numberOfMeshes = result.size();
		inner = new HE_Selection[_numberOfMeshes];
		outer = new HE_Selection[_numberOfMeshes];
		for (i = 0; i < _numberOfMeshes; i++) {
			inner[i] = linnersel.get(i);
			outer[i] = loutersel.get(i);
		}
		tracker.setStopStatus(this, "Exiting HEMC_VoronoiCellsPre.");
	}
}
