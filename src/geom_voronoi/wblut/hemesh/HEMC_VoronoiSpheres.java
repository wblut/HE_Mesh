package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;

/**
 *
 */
public class HEMC_VoronoiSpheres extends HEMC_MultiCreator {
	/**  */
	private WB_CoordCollection points;
	/**  */
	private int numberOfPoints;
	/**  */
	private int level;
	/**  */
	private int numTracers;
	/**  */
	private double traceStep;
	/**  */
	private double cutoff;
	/**  */
	private boolean approx;
	/**  */
	private double offset;

	/**
	 *
	 */
	public HEMC_VoronoiSpheres() {
		super();
		level = 1;
		traceStep = 10;
		numTracers = 100;
	}

	/**
	 *
	 *
	 * @param l
	 * @return
	 */
	public HEMC_VoronoiSpheres setLevel(final int l) {
		level = l;
		return this;
	}

	/**
	 *
	 *
	 * @param n
	 * @return
	 */
	public HEMC_VoronoiSpheres setNumTracers(final int n) {
		numTracers = n;
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEMC_VoronoiSpheres setTraceStep(final double d) {
		traceStep = d;
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEMC_VoronoiSpheres setCutoff(final double c) {
		cutoff = Math.abs(c);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEMC_VoronoiSpheres setApprox(final boolean a) {
		approx = a;
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEMC_VoronoiSpheres setOffset(final double o) {
		offset = o;
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiSpheres setPoints(final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiSpheres setPoints(final double[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiSpheres setPoints(final float[][] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public HEMC_VoronoiSpheres setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param result
	 */
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
