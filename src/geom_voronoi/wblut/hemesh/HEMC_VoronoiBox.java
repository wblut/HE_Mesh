package wblut.hemesh;

import java.util.Collection;
import java.util.List;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordList;
import wblut.geom.WB_Point;
import wblut.geom.WB_VoronoiCell3D;
import wblut.geom.WB_VoronoiFactory3D;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

public class HEMC_VoronoiBox extends HEMC_MultiCreator {
	private List<WB_Coord> points;
	private int numberOfPoints;
	private WB_AABB aabb;
	private boolean bruteForce;
	private WB_ScalarParameter offset;

	public HEMC_VoronoiBox() {
		super();
		offset = WB_ScalarParameter.ZERO;
	}

	public HEMC_VoronoiBox setPoints(final WB_Coord[] points) {
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		return this;
	}

	public HEMC_VoronoiBox setPoints(final Collection<? extends WB_Coord> points) {
		this.points = new WB_CoordList();
		this.points.addAll(points);
		return this;
	}

	public HEMC_VoronoiBox setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEMC_VoronoiBox setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEMC_VoronoiBox setOffset(final double o) {
		offset = new WB_ConstantScalarParameter(o);
		return this;
	}

	public HEMC_VoronoiBox setOffset(final WB_ScalarParameter o) {
		offset = o;
		return this;
	}

	public HEMC_VoronoiBox setContainer(final WB_AABB container) {
		this.aabb = container;
		return this;
	}

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
		final List<WB_VoronoiCell3D> voronoi = bruteForce
				? WB_VoronoiFactory3D.getVoronoi3DBruteForce(points, numberOfPoints, aabb, offset).getCells()
				: WB_VoronoiFactory3D.getVoronoi3D(points, numberOfPoints, aabb, offset).getCells();
		final WB_ProgressCounter counter = new WB_ProgressCounter(voronoi.size(), 10);
		tracker.setCounterStatus(this, "Creating cell mesh.", counter);
		for (final WB_VoronoiCell3D vor : voronoi) {
			final HE_Mesh m = new HE_Mesh(vor.getMesh());
			m.setInternalLabel(vor.getIndex());
			m.setLabel(vor.getIndex());
			result.add(m);
			counter.increment();
		}
		_numberOfMeshes = result.size();
		tracker.setStopStatus(this, "Exiting HEMC_VoronoiBox.");
	}
}
