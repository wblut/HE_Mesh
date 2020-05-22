package wblut.geom;

/**
 *
 */
public interface WB_MutableCoord extends WB_Coord {
	/**
	 *
	 *
	 * @param x
	 */
	void setX(double x);

	/**
	 *
	 *
	 * @param y
	 */
	void setY(double y);

	/**
	 *
	 *
	 * @param z
	 */
	void setZ(double z);

	/**
	 *
	 *
	 * @param w
	 */
	void setW(double w);

	/**
	 *
	 *
	 * @param i
	 * @param v
	 */
	void setCoord(int i, double v);

	/**
	 *
	 *
	 * @param p
	 */
	void set(WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	void set(double x, double y);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	void set(double x, double y, double z);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	void set(double x, double y, double z, double w);
}
