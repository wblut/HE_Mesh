package wblut.geom;

/**
 *
 */
public class WB_DefaultMap2D implements WB_Map2D {
	/**
	 *
	 */
	public WB_DefaultMap2D() {
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p.xd(), p.yd(), 0);
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
		result.set(x, y, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p.xd(), p.yd(), 0);
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
	 * @param u
	 * @param v
	 * @param result
	 */
	@Override
	public void unmapPoint2D(final double u, final double v, final WB_MutableCoord result) {
		result.set(u, v, 0);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void unmapPoint2D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p.xf(), p.yf(), 0);
	}

	/**
	 *
	 *
	 * @param v
	 * @param result
	 */
	@Override
	public void mapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v.xd(), v.yd(), 0);
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
		result.set(x, y, 0);
	}

	/**
	 *
	 *
	 * @param v
	 * @param result
	 */
	@Override
	public void unmapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v.xd(), v.yd(), 0);
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
	 * @param u
	 * @param v
	 * @param result
	 */
	@Override
	public void unmapVector2D(final double u, final double v, final WB_MutableCoord result) {
		result.set(u, v, 0);
	}

	/**
	 *
	 *
	 * @param v
	 * @param result
	 */
	@Override
	public void unmapVector2D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v.xf(), v.yf(), 0);
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
		result.set(p.xd(), p.yd(), 0);
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
		result.set(x, y, 0);
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
		result.set(p.xd(), p.yd(), 0);
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
	 * @param u
	 * @param v
	 * @return
	 */
	@Override
	public WB_Point unmapPoint2D(final double u, final double v) {
		final WB_Point result = new WB_Point();
		result.set(u, v, 0);
		return result;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point unmapPoint2D(final WB_Coord p) {
		final WB_Point result = new WB_Point();
		result.set(p.xf(), p.yf(), 0);
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
		result.set(v.xd(), v.yd(), 0);
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
		result.set(x, y, 0);
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
		result.set(v.xd(), v.yd(), 0);
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

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	@Override
	public WB_Vector unmapVector2D(final double u, final double v) {
		final WB_Vector result = new WB_Vector();
		result.set(u, v, 0);
		return result;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public WB_Vector unmapVector2D(final WB_Coord v) {
		final WB_Vector result = new WB_Vector();
		result.set(v.xf(), v.yf(), 0);
		return result;
	}
}
