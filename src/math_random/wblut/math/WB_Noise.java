package wblut.math;

/**
 *
 */
public interface WB_Noise {
	/**
	 *
	 *
	 * @param seed
	 */
	void setSeed(long seed);

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	double value1D(double x);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	double value2D(double x, double y);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	double value3D(double x, double y, double z);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return
	 */
	double value4D(double x, double y, double z, double w);

	/**
	 *
	 *
	 * @param sx
	 */
	void setScale(double sx);

	/**
	 *
	 *
	 * @param sx
	 * @param sy
	 */
	void setScale(double sx, double sy);

	/**
	 *
	 *
	 * @param sx
	 * @param sy
	 * @param sz
	 */
	void setScale(double sx, double sy, double sz);

	/**
	 *
	 *
	 * @param sx
	 * @param sy
	 * @param sz
	 * @param sw
	 */
	void setScale(double sx, double sy, double sz, double sw);
}
