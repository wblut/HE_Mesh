package wblut.geom;

/**
 *
 */
public interface WB_MutableCoordMath extends WB_ImmutableCoordMath {
	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord crossSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsNormalSelf(final WB_Transform3D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsPointSelf(final WB_Transform3D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsVectorSelf(final WB_Transform3D T);

	/**
	 *
	 *
	 * @param angle
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @param p2x
	 * @param p2y
	 * @param p2z
	 * @return
	 */
	WB_Coord rotateAboutAxis2PSelf(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z);

	/**
	 *
	 *
	 * @param angle
	 * @param p1
	 * @param p2
	 * @return
	 */
	WB_Coord rotateAboutAxis2PSelf(final double angle, final WB_Coord p1, final WB_Coord p2);

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @param a
	 * @return
	 */
	WB_Coord rotateAboutAxisSelf(final double angle, final WB_Coord p, final WB_Coord a);

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @param pz
	 * @param ax
	 * @param ay
	 * @param az
	 * @return
	 */
	WB_Coord rotateAboutAxisSelf(final double angle, final double px, final double py, final double pz, final double ax,
			final double ay, final double az);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord scaleSelf(final double f);

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @param fz
	 * @return
	 */
	WB_Coord scaleSelf(final double fx, final double fy, final double fz);

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @return
	 */
	WB_Coord translateSelf(final double px, final double py, double pz);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord translateSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord rotateAboutOriginSelf(final double angle, final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param angle
	 * @param v
	 * @return
	 */
	WB_Coord rotateAboutOriginSelf(final double angle, final WB_Coord v);

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
	 * @param p
	 * @return
	 */
	WB_Coord subSelf(final WB_Coord p);

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
	 * @param T
	 * @return
	 */
	@Override
	WB_Coord applyAsPoint2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	WB_Coord applyAsVector2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	WB_Coord applyAsNormal2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	@Override
	WB_Coord rotateAboutPoint2DSelf(final double angle, final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	WB_Coord rotateAboutPoint2DSelf(final double angle, final double px, final double py);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	@Override
	WB_Coord rotateAboutOrigin2DSelf(final double angle);

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @return
	 */
	@Override
	WB_Coord translate2DSelf(final double px, final double py);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	WB_Coord translate2DSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	WB_Coord scale2DSelf(final double f);

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @return
	 */
	@Override
	WB_Coord scale2DSelf(final double fx, final double fy);
}
