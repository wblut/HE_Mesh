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
import wblut.geom.WB_CoordList;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_VoronoiFactory3D;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

public class HEMC_VoronoiCellsPre extends HEMC_MultiCreator {
	private List<WB_Coord> points;
	private HE_Mesh container;
	private boolean bruteForce;
	private WB_ScalarParameter offset;
	public HE_Selection[] inner;
	public HE_Selection[] outer;

	public HEMC_VoronoiCellsPre() {
		super();
		offset = WB_ScalarParameter.ZERO;
	}

	public HEMC_VoronoiCellsPre setMesh(final HE_Mesh mesh, final boolean addCenter) {
		if (addCenter) {
			points = new WB_CoordList();
			points.addAll(mesh.getVertices());
			points.add(HE_MeshOp.getCenter(mesh));
		} else {
			points = new WB_CoordList();
			points.addAll(mesh.getVertices());
		}
		container = mesh;
		return this;
	}

	public HEMC_VoronoiCellsPre setPoints(final WB_Coord[] points) {
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		return this;
	}

	public HEMC_VoronoiCellsPre setPoints(final Collection<? extends WB_Coord> points) {
		this.points = new WB_CoordList();
		this.points.addAll(points);
		return this;
	}

	public HEMC_VoronoiCellsPre setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEMC_VoronoiCellsPre setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEMC_VoronoiCellsPre setOffset(final double o) {
		offset = new WB_ConstantScalarParameter(o);
		return this;
	}

	public HEMC_VoronoiCellsPre setOffset(final WB_ScalarParameter o) {
		offset = o;
		return this;
	}

	public HEMC_VoronoiCellsPre setContainer(final HE_Mesh container) {
		this.container = container;
		return this;
	}

	public HEMC_VoronoiCellsPre setBruteForce(final boolean b) {
		bruteForce = b;
		return this;
	}

	class VorResult {
		HE_Mesh mesh;
		HE_Selection inner;
		HE_Selection outer;

		VorResult(final HE_Mesh mesh, final HE_Selection inner, final HE_Selection outer) {
			this.mesh = mesh;
			this.inner = inner;
			this.outer = outer;
		}
	}

	class CellRunner implements Callable<VorResult> {
		int index;
		int[] indices;

		CellRunner(final int index, final int[] indices) {
			this.index = index;
			this.indices = indices;
		}

		@Override
		public VorResult call() {
			final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
			cvc.setPoints(points).setContainer(container).setOffset(offset).setLimitPoints(true);
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
		final HEMC_VoronoiBox multiCreator = new HEMC_VoronoiBox();
		multiCreator.setPoints(points);
		multiCreator.setContainer(HE_MeshOp.getAABB(container));
		multiCreator.setOffset(offset);
		multiCreator.setBruteForce(bruteForce);
		final HE_MeshCollection cells = multiCreator.create();
		final int[][] indices = WB_VoronoiFactory3D.getVoronoi3DNeighbors(points);
		final HEC_VoronoiCell cvc = new HEC_VoronoiCell();
		cvc.setPoints(points).setContainer(container).setOffset(offset).setLimitPoints(true);
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
			final boolean intersects = WB_GeometryOp3D.checkIntersection3D(maabb, tree);
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
