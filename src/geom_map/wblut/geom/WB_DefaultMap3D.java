package wblut.geom;

/**
 *
 */
public class WB_DefaultMap3D implements WB_Map {
	/**
	 *
	 */
	public WB_DefaultMap3D() {
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		result.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p);
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param result
	 */
	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		result.set(u, v, w);
	}

	/**
	 *
	 *
	 * @param v
	 * @param result
	 */
	@Override
	public void mapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		result.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param v
	 * @param result
	 */
	@Override
	public void unmapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v);
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param result
	 */
	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		result.set(u, v, w);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		final WB_Point result = new WB_Point();
		result.set(p);
		return result;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Point mapPoint3D(final double x, final double y, final double z) {
		final WB_Point result = new WB_Point();
		result.set(x, y, z);
		return result;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point unmapPoint3D(final WB_Coord p) {
		final WB_Point result = new WB_Point();
		result.set(p);
		return result;
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	@Override
	public WB_Point unmapPoint3D(final double u, final double v, final double w) {
		final WB_Point result = new WB_Point();
		result.set(u, v, w);
		return result;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Vector mapVector3D(final WB_Coord v) {
		final WB_Vector result = new WB_Vector();
		result.set(v);
		return result;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public WB_Vector mapVector3D(final double x, final double y, final double z) {
		final WB_Vector result = new WB_Vector();
		result.set(x, y, z);
		return result;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Vector unmapVector3D(final WB_Coord v) {
		final WB_Vector result = new WB_Vector();
		result.set(v);
		return result;
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return
	 */
	@Override
	public WB_Vector unmapVector3D(final double u, final double v, final double w) {
		final WB_Vector result = new WB_Vector();
		result.set(u, v, w);
		return result;
	}
}
