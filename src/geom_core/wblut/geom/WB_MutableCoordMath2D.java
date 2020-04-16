package wblut.geom;

public interface WB_MutableCoordMath2D extends WB_ImmutableCoordMath2D {
	WB_Coord addSelf(final double... x);

	WB_Coord addSelf(final WB_Coord p);

	WB_Coord subSelf(final double... x);

	WB_Coord subSelf(final WB_Coord p);

	WB_Coord mulSelf(final double f);

	WB_Coord divSelf(final double f);

	WB_Coord addMulSelf(final double f, final double... x);

	WB_Coord addMulSelf(final double f, final WB_Coord p);

	WB_Coord mulAddMulSelf(final double f, final double g, final double... x);

	WB_Coord mulAddMulSelf(final double f, final double g, final WB_Coord p);

	double normalizeSelf();

	WB_Coord trimSelf(final double d);

	@Override
	WB_Coord applyAsPoint2DSelf(final WB_Transform2D T);

	@Override
	WB_Coord applyAsVector2DSelf(final WB_Transform2D T);

	@Override
	WB_Coord applyAsNormal2DSelf(final WB_Transform2D T);

	@Override
	WB_Coord rotateAboutPoint2DSelf(final double angle, final WB_Coord p);

	@Override
	WB_Coord rotateAboutPoint2DSelf(final double angle, final double px, final double py);

	@Override
	WB_Coord rotateAboutOrigin2DSelf(final double angle);

	@Override
	WB_Coord translate2DSelf(final double px, final double py);

	@Override
	WB_Coord translate2DSelf(final WB_Coord p);

	@Override
	WB_Coord scale2DSelf(final double f);

	@Override
	WB_Coord scale2DSelf(final double fx, final double fy);
}
