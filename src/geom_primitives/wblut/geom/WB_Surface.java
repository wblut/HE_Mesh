package wblut.geom;

/**
 *
 */
public interface WB_Surface {
	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	WB_Point surfacePoint(double u, double v);

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

	/**
	 *
	 *
	 * @return
	 */
	double getLowerV();

	/**
	 *
	 *
	 * @return
	 */
	double getUpperV();
}
