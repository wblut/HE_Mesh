package wblut.geom;

/**
 *
 */
public interface WB_Curve {
	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	WB_Point getPointOnCurve(double u);

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	WB_Vector getDirectionOnCurve(double u);

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	WB_Vector getDerivative(double u);

	/**
	 *
	 *
	 * @return
	 */
	double getLowerU();

	/**
	 *
	 *
	 * @return
	 */
	double getUpperU();
}
