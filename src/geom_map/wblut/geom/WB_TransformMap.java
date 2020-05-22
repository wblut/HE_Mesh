package wblut.geom;

/**
 *
 */
public class WB_TransformMap implements WB_Map {
	/**  */
	private final WB_Transform3D T;
	/**  */
	private final WB_Transform3D invT;

	/**
	 *
	 *
	 * @param transform
	 */
	public WB_TransformMap(final WB_Transform3D transform) {
		T = transform.get();
		invT = transform.get();
		invT.inverse();
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		T.applyAsPointInto(p, result);
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
		T.applyAsPointInto(x, y, z, result);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		invT.applyAsPointInto(p, result);
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
		invT.applyAsPointInto(u, v, w, result);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void mapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		T.applyAsVectorInto(p, result);
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
		T.applyAsVectorInto(x, y, z, result);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	@Override
	public void unmapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		invT.applyAsVectorInto(p, result);
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
		invT.applyAsVectorInto(u, v, w, result);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		return T.applyAsPoint(p);
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
		return T.applyAsPoint(x, y, z);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Point unmapPoint3D(final WB_Coord p) {
		return invT.applyAsPoint(p);
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
		return invT.applyAsPoint(u, v, w);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector mapVector3D(final WB_Coord p) {
		return T.applyAsVector(p);
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
		return T.applyAsVector(x, y, z);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public WB_Vector unmapVector3D(final WB_Coord p) {
		return invT.applyAsVector(p);
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
		return invT.applyAsVector(u, v, w);
	}
}
