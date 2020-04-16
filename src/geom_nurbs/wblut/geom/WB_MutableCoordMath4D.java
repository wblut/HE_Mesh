package wblut.geom;

public interface WB_MutableCoordMath4D extends WB_ImmutableCoordMath4D {
	WB_Coord addSelf(final double... x);

	WB_Coord addSelf(final WB_Coord p);

	WB_Coord subSelf(final double... x);

	WB_Coord subSelf(final WB_Coord v);

	WB_Coord mulSelf(final double f);

	WB_Coord divSelf(final double f);

	WB_Coord addMulSelf(final double f, final double... x);

	WB_Coord addMulSelf(final double f, final WB_Coord p);

	WB_Coord mulAddMulSelf(final double f, final double g, final double... x);

	WB_Coord mulAddMulSelf(final double f, final double g, final WB_Coord p);

	double normalizeSelf();

	WB_Coord trimSelf(final double d);

	WB_Coord add3DSelf(final WB_Coord p);

	WB_Coord add3DSelf(final double x, final double y, final double z);

	WB_Coord sub3DSelf(final WB_Coord v);

	WB_Coord sub3DSelf(final double x, final double y, final double z);

	WB_Coord mul3DSelf(final double f);

	WB_Coord div3DSelf(final double f);

	WB_Coord addMul3DSelf(final double f, final WB_Coord p);

	WB_Coord addMul3DSelf(final double f, final double x, final double y, final double z);

	WB_Coord mulAddMul3DSelf(final double f, final double g, final WB_Coord p);

	WB_Coord mulAddMul3DSelf(final double f, final double g, final double x, final double y, final double z);
}
