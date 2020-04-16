package wblut.geom;

public interface WB_MutableCoordMath3D extends WB_ImmutableCoordMath3D {
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

	WB_Coord crossSelf(final WB_Coord p);

	double normalizeSelf();

	WB_Coord trimSelf(final double d);

	WB_Coord applyAsNormalSelf(final WB_Transform3D T);

	WB_Coord applyAsPointSelf(final WB_Transform3D T);

	WB_Coord applyAsVectorSelf(final WB_Transform3D T);

	WB_Coord rotateAboutAxis2PSelf(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z);

	WB_Coord rotateAboutAxis2PSelf(final double angle, final WB_Coord p1, final WB_Coord p2);

	WB_Coord rotateAboutAxisSelf(final double angle, final WB_Coord p, final WB_Coord a);

	WB_Coord rotateAboutAxisSelf(final double angle, final double px, final double py, final double pz, final double ax,
			final double ay, final double az);

	WB_Coord scaleSelf(final double f);

	WB_Coord scaleSelf(final double fx, final double fy, final double fz);

	WB_Coord translateSelf(final double px, final double py, double pz);

	WB_Coord translateSelf(final WB_Coord p);

	WB_Coord rotateAboutOriginSelf(final double angle, final double x, final double y, final double z);

	WB_Coord rotateAboutOriginSelf(final double angle, final WB_Coord v);

	@Override
	WB_Coord applyAsNormal2DSelf(final WB_Transform2D T);

	@Override
	WB_Coord applyAsPoint2DSelf(final WB_Transform2D T);

	@Override
	WB_Coord applyAsVector2DSelf(final WB_Transform2D T);

	@Override
	WB_Coord translate2DSelf(final double px, final double py);

	@Override
	WB_Coord translate2DSelf(final WB_Coord p);

	@Override
	WB_Coord rotateAboutPoint2DSelf(final double angle, final WB_Coord p);

	@Override
	WB_Coord rotateAboutPoint2DSelf(final double angle, final double px, final double py);

	@Override
	WB_Coord scale2DSelf(final double f);

	@Override
	WB_Coord scale2DSelf(final double fx, final double fy);

	@Override
	WB_Coord rotateAboutOrigin2DSelf(final double angle);
}
