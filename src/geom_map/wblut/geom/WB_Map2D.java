package wblut.geom;

/**
 *
 */
public interface WB_Map2D extends WB_Map {
	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	void unmapPoint2D(WB_Coord p, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param result
	 */
	void unmapPoint2D(double u, double v, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param v
	 * @param result
	 */
	void unmapVector2D(WB_Coord v, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param result
	 */
	void unmapVector2D(double u, double v, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Point unmapPoint2D(WB_Coord p);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	WB_Coord unmapPoint2D(double u, double v);

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	WB_Coord unmapVector2D(WB_Coord v);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	WB_Coord unmapVector2D(double u, double v);
}
