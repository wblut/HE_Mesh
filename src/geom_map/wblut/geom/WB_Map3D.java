package wblut.geom;

/**
 *
 */
public interface WB_Map3D {
	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	void mapPoint3D(WB_Coord p, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	void mapPoint3D(double x, double y, double z, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	void unmapPoint3D(WB_Coord p, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param result
	 */
	void unmapPoint3D(double u, double v, double w, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	void mapVector3D(WB_Coord p, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	void mapVector3D(double x, double y, double z, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	void unmapVector3D(WB_Coord p, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param result
	 */
	void unmapVector3D(double u, double v, double w, WB_MutableCoord result);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Point mapPoint3D(WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Point mapPoint3D(double x, double y, double z);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Point unmapPoint3D(WB_Coord p);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	WB_Point unmapPoint3D(double u, double v, double w);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Vector mapVector3D(WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Vector mapVector3D(double x, double y, double z);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Vector unmapVector3D(WB_Coord p);

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	WB_Vector unmapVector3D(double u, double v, double w);
}
