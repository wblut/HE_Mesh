package wblut.geom;

/**
 *
 */
public interface WB_Coord extends Comparable<WB_Coord> {
	/**
	 *
	 *
	 * @return
	 */
	double xd();

	/**
	 *
	 *
	 * @return
	 */
	double yd();

	/**
	 *
	 *
	 * @return
	 */
	double zd();

	/**
	 *
	 *
	 * @return
	 */
	double wd();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	double getd(int i);

	void getd(double[] result);

	void getd(int i, double[] result);

	/**
	 *
	 *
	 * @return
	 */
	float xf();

	/**
	 *
	 *
	 * @return
	 */
	float yf();

	/**
	 *
	 *
	 * @return
	 */
	float zf();

	/**
	 *
	 *
	 * @return
	 */
	float wf();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	float getf(int i);

	void getf(float[] result);

	void getf(int i, float[] result);
}
