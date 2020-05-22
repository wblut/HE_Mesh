package wblut.geom;

/**
 *
 */
public interface WB_ImmutableCoordMath4D {
	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	WB_Coord add(final double... x);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord add(final WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	WB_Coord sub(final double... x);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord sub(final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord mul(final double f);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord div(final double f);

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @return
	 */
	WB_Coord addMul(final double f, final double... x);

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	WB_Coord addMul(final double f, final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return
	 */
	WB_Coord mulAddMul(final double f, final double g, final double... x);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return
	 */
	WB_Coord mulAddMul(final double f, final double g, final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double dot(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double absDot(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord add3D(final WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord add3D(final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord sub3D(final WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord sub3D(final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord mul3D(final double f);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord div3D(final double f);

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return
	 */
	WB_Coord addMul3D(final double f, final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord addMul3D(final double f, final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return
	 */
	WB_Coord mulAddMul3D(final double f, final double g, final WB_Coord p);

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
	WB_Coord mulAddMul3D(final double f, final double g, final double x, final double y, final double z);
}
