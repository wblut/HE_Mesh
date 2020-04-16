package wblut.hemesh;

import java.util.Collection;
import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordList;
import wblut.geom.WB_Point;

public class HEMC_VoronoiSpheres extends HEMC_MultiCreator {
	private List<WB_Coord> points;
	private int numberOfPoints;
	private int level;
	private int numTracers;
	private double traceStep;
	private double cutoff;
	private boolean approx;
	private double offset;

	public HEMC_VoronoiSpheres() {
		super();
		level = 1;
		traceStep = 10;
		numTracers = 100;
	}

	public HEMC_VoronoiSpheres setLevel(final int l) {
		level = l;
		return this;
	}

	public HEMC_VoronoiSpheres setNumTracers(final int n) {
		numTracers = n;
		return this;
	}

	public HEMC_VoronoiSpheres setTraceStep(final double d) {
		traceStep = d;
		return this;
	}

	public HEMC_VoronoiSpheres setCutoff(final double c) {
		cutoff = Math.abs(c);
		return this;
	}

	public HEMC_VoronoiSpheres setApprox(final boolean a) {
		approx = a;
		return this;
	}

	public HEMC_VoronoiSpheres setOffset(final double o) {
		offset = o;
		return this;
	}

	public HEMC_VoronoiSpheres setPoints(final Collection<? extends WB_Coord> points) {
		this.points = new WB_CoordList();
		this.points.addAll(points);
		return this;
	}

	public HEMC_VoronoiSpheres setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEMC_VoronoiSpheres setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEMC_VoronoiSpheres setPoints(final WB_Coord[] points) {
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
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
