/**
 *
 */
package wblut.geom;

/**
 *
 */
public class WB_OBB {
	private final WB_Point origin;
	private final WB_Vector[] axes;
	private final double[] halfSizes;

	public WB_OBB(final WB_CoordCollection points) {
		final WB_PrincipalComponentAnalysis PCA = new WB_PrincipalComponentAnalysis(points);
		this.origin = PCA.getOrigin();
		this.axes = PCA.getPrincipalAxes();
		this.halfSizes = PCA.getHalfSizes();
	}

	public WB_OBB(final WB_Coord origin, final WB_Coord[] axes, final double[] halfSizes) {
		this.origin = new WB_Point(origin);
		this.axes = new WB_Vector[3];
		this.axes[0] = new WB_Vector(axes[0]);
		this.axes[1] = new WB_Vector(axes[1]);
		this.axes[2] = new WB_Vector(axes[2]);
		this.halfSizes = new double[3];
		this.halfSizes[0] = halfSizes[0];
		this.halfSizes[1] = halfSizes[1];
		this.halfSizes[2] = halfSizes[2];
	}

	public WB_Coord getOrigin() {
		return origin;
	}

	public WB_Coord getAxis(final int i) {
		return axes[i];
	}

	public double getHalfSize(final int i) {
		return halfSizes[i];
	}

	public int getDimension(final double cutoff) {
		int dim = 0;
		for (int i = 0; i < 3; i++) {
			if (halfSizes[i] > cutoff) {
				dim++;
			}
		}
		return dim;
	}
}
