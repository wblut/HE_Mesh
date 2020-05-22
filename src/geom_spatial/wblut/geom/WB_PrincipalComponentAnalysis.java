package wblut.geom;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.dense.row.SingularOps_DDRM;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;

public class WB_PrincipalComponentAnalysis {
	private DMatrixRMaj PCSubSpace;
	private final DMatrixRMaj data;
	private final static int DIM = 3;
	private final double[] mean;
	private double[] sizes;
	private double[] origin;

	public WB_PrincipalComponentAnalysis(final WB_CoordCollection points) {
		mean = new double[DIM];
		data = new DMatrixRMaj(points.size(), DIM);
		WB_Coord point;
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			data.set(i, 0, point.xd());
			data.set(i, 1, point.yd());
			data.set(i, 2, point.zd());
		}
		computeBasis();
		calculateSizes(points);
	}

	public WB_Vector[] getPrincipalAxes() {
		final double[] v0 = getBasisVector(0);
		final double[] v1 = getBasisVector(1);
		final double[] v2 = getBasisVector(2);
		return new WB_Vector[] { new WB_Vector(v0), new WB_Vector(v1), new WB_Vector(v2) };
	}

	public double[] getHalfSizes() {
		return sizes;
	}

	private void calculateSizes(final WB_CoordCollection points) {
		sizes = new double[3];
		origin = new double[3];
		double minx = Double.POSITIVE_INFINITY;
		double miny = Double.POSITIVE_INFINITY;
		double minz = Double.POSITIVE_INFINITY;
		double maxx = Double.NEGATIVE_INFINITY;
		double maxy = Double.NEGATIVE_INFINITY;
		double maxz = Double.NEGATIVE_INFINITY;
		WB_Coord p;
		for (int i = 0; i < points.size(); i++) {
			p = sampleToEigenSpace(points.get(i));
			minx = Math.min(minx, p.xd());
			miny = Math.min(miny, p.yd());
			minz = Math.min(minz, p.zd());
			maxx = Math.max(maxx, p.xd());
			maxy = Math.max(maxy, p.yd());
			maxz = Math.max(maxz, p.zd());
		}
		sizes[0] = 0.5 * (maxx - minx);
		sizes[1] = 0.5 * (maxy - miny);
		sizes[2] = 0.5 * (maxz - minz);
		origin[0] = 0.5 * (maxx + minx);
		origin[1] = 0.5 * (maxy + miny);
		origin[2] = 0.5 * (maxz + minz);
	}

	public WB_Point getOrigin() {
		return eigenToSampleSpace(new WB_Point(origin));
	}

	public WB_CoordinateSystem getCS() {
		return new WB_CoordinateSystem(getOrigin(), getPrincipalAxes()[0], getPrincipalAxes()[1],
				getPrincipalAxes()[2]);
	}

	public WB_Plane getPlane() {
		final WB_Vector[] axes = getPrincipalAxes();
		final WB_Plane P = new WB_Plane(getOrigin(), axes[2]);
		P.overrideUV(axes[0], axes[1]);
		return P;
	}

	public WB_Line getLine() {
		final WB_Vector[] axes = getPrincipalAxes();
		final WB_Line L = new WB_Line(getOrigin(), axes[0]);
		L.overrideUV(axes[1], axes[2]);
		return L;
	}

	private void computeBasis() {
		for (int i = 0; i < data.getNumRows(); i++) {
			for (int j = 0; j < mean.length; j++) {
				mean[j] += data.get(i, j);
			}
		}
		for (int j = 0; j < mean.length; j++) {
			mean[j] /= data.getNumRows();
		}
		for (int i = 0; i < data.getNumRows(); i++) {
			for (int j = 0; j < mean.length; j++) {
				data.set(i, j, data.get(i, j) - mean[j]);
			}
		}
		final SingularValueDecomposition<DMatrixRMaj> svd = DecompositionFactory_DDRM.svd(data.numRows, data.numCols,
				false, true, false);
		if (!svd.decompose(data)) {
			throw new RuntimeException("Principal component analysis failed");
		}
		PCSubSpace = svd.getV(null, true);
		final DMatrixRMaj W = svd.getW(null);
		// Singular values are in an arbitrary order initially
		SingularOps_DDRM.descendingOrder(null, false, W, PCSubSpace, true);
		// strip off unneeded components and find the basis
		PCSubSpace.reshape(3, mean.length, true);
	}

	private double[] getBasisVector(final int which) {
		if (which < 0 || which >= DIM) {
			throw new IllegalArgumentException("Invalid component");
		}
		final DMatrixRMaj v = new DMatrixRMaj(1, data.numCols);
		CommonOps_DDRM.extract(PCSubSpace, which, which + 1, 0, data.numCols, v, 0, 0);
		return v.data;
	}

	/**
	 * Converts a point from sample space into eigen space.
	 *
	 * @param sampleData Sample space data.
	 * @return Eigen space projection.
	 */
	public WB_Point sampleToEigenSpace(final WB_Coord sample) {
		final DMatrixRMaj mean = DMatrixRMaj.wrap(data.getNumCols(), 1, this.mean);
		final DMatrixRMaj s = new DMatrixRMaj(data.getNumCols(), 1, true,
				new double[] { sample.xd(), sample.yd(), sample.zd() });
		final DMatrixRMaj r = new DMatrixRMaj(DIM, 1);
		CommonOps_DDRM.subtract(s, mean, s);
		CommonOps_DDRM.mult(PCSubSpace, s, r);
		return new WB_Point(r.data);
	}

	/**
	 * Converts a point from eigen space into sample space.
	 *
	 * @param eigenData Eigen space data.
	 * @return Sample space projection.
	 */
	public WB_Point eigenToSampleSpace(final WB_Coord eigen) {
		final DMatrixRMaj s = new DMatrixRMaj(data.getNumCols(), 1);
		final DMatrixRMaj r = DMatrixRMaj.wrap(DIM, 1, new double[] { eigen.xd(), eigen.yd(), eigen.zd() });
		CommonOps_DDRM.multTransA(PCSubSpace, r, s);
		final DMatrixRMaj mean = DMatrixRMaj.wrap(data.getNumCols(), 1, this.mean);
		CommonOps_DDRM.add(s, mean, s);
		return new WB_Point(s.data);
	}

	/**
	 * <p>
	 * The membership error for a sample. If the error is less than a threshold then
	 * it can be considered a member. The threshold's value depends on the data set.
	 * </p>
	 * <p>
	 * The error is computed by projecting the sample into eigenspace then
	 * projecting it back into sample space and
	 * </p>
	 *
	 * @param sample The sample whose membership status is being considered.
	 * @return Its membership error.
	 */
	public double errorMembership(final WB_Coord sample) {
		final WB_Point eig = sampleToEigenSpace(sample);
		final WB_Point reproj = eigenToSampleSpace(eig);
		return reproj.getSqDistance3D(sample);
	}

	/**
	 * Computes the dot product of each basis vector against the sample. Can be used
	 * as a measure for membership in the training sample set. High values
	 * correspond to a better fit.
	 *
	 * @param sample Sample of original data.
	 * @return Higher value indicates it is more likely to be a member of input
	 *         dataset.
	 */
	public double response(final WB_Coord sample) {
		final DMatrixRMaj dots = new DMatrixRMaj(DIM, 1);
		final DMatrixRMaj s = DMatrixRMaj.wrap(data.numCols, 1, new double[] { sample.xd(), sample.yd(), sample.zd() });
		CommonOps_DDRM.mult(PCSubSpace, s, dots);
		return NormOps_DDRM.normF(dots);
	}
}
