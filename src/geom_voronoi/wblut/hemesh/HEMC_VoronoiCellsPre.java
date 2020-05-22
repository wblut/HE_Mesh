package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import wblut.geom.WB_AABB;
import wblut.geom.WB_AABBTree3D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_OBB;
import wblut.geom.WB_VoronoiFactory3D;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEMC_VoronoiCellsPre extends HEMC_MultiCreator {
	/**  */
	private WB_CoordCollection points;
	/**  */
	private HE_Mesh container;
	/**  */
	private boolean bruteForce;
	/**  */
	private WB_ScalarParameter offset;
	/**  */
	public HE_Selection[] inner;
	/**  */
	public HE_Selection[] outer;

	/**
	 *
	 */
	public HEMC_VoronoiCellsPre() {
		super();
		offset = WB_ScalarParameter.ZERO;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param addCenter
	 * @return
	 */
	public HEMC_VoronoiCellsPre setMesh(final HE_Mesh mesh, final boolean addCenter) {
		if (addCenter) {
			points = WB_CoordCollection.getCollection(mesh.getVertices());
			points.add(HE_MeshOp.getCenter(mesh));
		} else {
			points = WB_CoordCollection.getCollection(mesh.getVertices());
		}
		container = mesh;
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiCellsPre setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiCellsPre setPoints(final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiCellsPre setPoints(final double[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiCellsPre setPoints(final float[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEMC_VoronoiCellsPre setOffset(final double o) {
		offset = new WB_ConstantScalarParameter(o);
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEMC_VoronoiCellsPre setOffset(final WB_ScalarParameter o) {
		offset = o;
		return this;
	}

	/**
	 *
	 *
	 * @param container
	 * @return
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

	/**
	 *
	 */
	class VorResult {
		/**  */
		HE_Mesh mesh;
		/**  */
		HE_Selection inner;
		/**  */
		HE_Selection outer;

		/**
		 *
		 *
		 * @param mesh
		 * @param inner
		 * @param outer
		 */
		VorResult(final HE_Mesh mesh, final HE_Selection inner, final HE_Selection outer) {
			this.mesh = mesh;
			this.inner = inner;
			this.outer = outer;
		}
	}

	/**
	 *
	 */
	class CellRunner implements Callable<VorResult> {
		/**  */
		int index;
		/**  */
		int[] indices;

		/**
		 *
		 *
		 * @param index
		 * @param indices
		 */
		CellRunner(final int index, final int[] indices) {
			this.index = index;
			this.indices = indices;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public VorResult call() {
			final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
			cvc.setPoints(points.toList()).setContainer(container).setOffset(offset).setLimitPoints(true);
			cvc.setCellIndex(index);
			cvc.setPointsToUse(indices);
			return new VorResult(cvc.createBase(), cvc.inner, cvc.outer);
		}
	}

	/**
	 *
	 *
	 * @param result
	 */
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
		final WB_OBB OBB = new WB_OBB(points);
		final int dim = OBB.getDimension(WB_Epsilon.EPSILON);
		if (dim < 3) {
			bruteForce = true;
		}
		final HEMC_VoronoiBox multiCreator = new HEMC_VoronoiBox();
		multiCreator.setPoints(points);
		multiCreator.setContainer(HE_MeshOp.getAABB(container));
		multiCreator.setOffset(offset);
		multiCreator.setBruteForce(bruteForce);
		final HE_MeshCollection cells = multiCreator.create();
		final int[][] indices = WB_VoronoiFactory3D.getVoronoi3DNeighbors(points);
		final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
		cvc.setPoints(points.toList()).setContainer(container).setOffset(offset).setLimitPoints(true);
		final WB_AABBTree3D tree = new WB_AABBTree3D(container, 1);
		final ArrayList<HE_Selection> linnersel = new ArrayList<>();
		final ArrayList<HE_Selection> loutersel = new ArrayList<>();
		final HE_MeshIterator mItr = cells.mItr();
		HE_Mesh m;
		int i = 0;
		final List<Integer> surfaceCells = new ArrayList<>();
		while (mItr.hasNext()) {
			m = mItr.next();
			final HE_VertexIterator vItr = m.vItr();
			HE_Vertex v;
			boolean in = false;
			while (vItr.hasNext()) {
				v = vItr.next();
				in = HE_MeshOp.isInside(tree, v);
				if (in) {
					break;
				}
			}
			final WB_AABB maabb = HE_MeshOp.getAABB(m);
			final boolean intersects = WB_GeometryOp.checkIntersection3D(maabb, tree);
			if (!in && !intersects) {
				tracker.setDuringStatus(this, "Ignoring external cell " + (i + 1) + " of " + cells.size() + ".");
			} else if (!intersects) {
				tracker.setDuringStatus(this, "Creating internal cell " + (i + 1) + " of " + cells.size() + ".");
				result.add(m);
				linnersel.add(m.selectAllFaces());
				loutersel.add(HE_Selection.getSelection(m));
			} else {
				tracker.setDuringStatus(this, "Qeueing surface cell " + (i + 1) + " of " + cells.size() + ".");
				final int index = m.getInternalLabel();
				surfaceCells.add(index);
			}
			i++;
		}
		try {
			final int threadCount = Runtime.getRuntime().availableProcessors();
			final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			final List<Future<VorResult>> list = new ArrayList<>();
			i = 0;
			for (i = 0; i < surfaceCells.size(); i++) {
				final Callable<VorResult> runner = new CellRunner(surfaceCells.get(i), indices[surfaceCells.get(i)]);
				list.add(executor.submit(runner));
			}
			for (final Future<VorResult> future : list) {
				final VorResult vr = future.get();
				final HE_Mesh cell = vr.mesh;
				result.add(cell);
				linnersel.add(vr.inner);
				loutersel.add(vr.outer);
				tracker.setDuringStatus(this, "Retrieving surface cell " + cell.getInternalLabel() + ".");
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
