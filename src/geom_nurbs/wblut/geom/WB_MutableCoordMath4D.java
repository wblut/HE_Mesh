package wblut.geom;

/**
 *
 */
public interface WB_MutableCoordMath4D extends WB_ImmutableCoordMath4D {
	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	WB_Coord addSelf(final double... x);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord addSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	WB_Coord subSelf(final double... x);

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	WB_Coord subSelf(final WB_Coord v);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord mulSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord divSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @return
	 */
	WB_Coord addMulSelf(final double f, final double... x);

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	WB_Coord addMulSelf(final double f, final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return
	 */
	WB_Coord mulAddMulSelf(final double f, final double g, final double... x);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return
	 */
	WB_Coord mulAddMulSelf(final double f, final double g, final WB_Coord p);

	/**
	 *
	 *
	 * @return
	 */
	double normalizeSelf();

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	WB_Coord trimSelf(final double d);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord add3DSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord add3DSelf(final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	WB_Coord sub3DSelf(final WB_Coord v);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord sub3DSelf(final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord mul3DSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord div3DSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	WB_Coord addMul3DSelf(final double f, final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord addMul3DSelf(final double f, final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return
	 */
	WB_Coord mulAddMul3DSelf(final double f, final double g, final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord mulAddMul3DSelf(final double f, final double g, final double x, final double y, final double z);
}
