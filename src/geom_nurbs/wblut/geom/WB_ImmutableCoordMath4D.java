package wblut.geom;

public interface WB_ImmutableCoordMath4D {
	WB_Coord add(final double... x);

	WB_Coord add(final WB_Coord p);

	WB_Coord sub(final double... x);

	WB_Coord sub(final WB_Coord p);

	WB_Coord mul(final double f);

	WB_Coord div(final double f);

	WB_Coord addMul(final double f, final double... x);

	WB_Coord addMul(final double f, final WB_Coord p);

	WB_Coord mulAddMul(final double f, final double g, final double... x);

	WB_Coord mulAddMul(final double f, final double g, final WB_Coord p);

	double dot(final WB_Coord p);

	double absDot(final WB_Coord p);

	WB_Coord add3D(final WB_Coord p);

	WB_Coord add3D(final double x, final double y, final double z);

	WB_Coord sub3D(final WB_Coord p);

	WB_Coord sub3D(final double x, final double y, final double z);

	WB_Coord mul3D(final double f);

	WB_Coord div3D(final double f);

	WB_Coord addMul3D(final double f, final WB_Coord p);

	WB_Coord addMul3D(final double f, final double x, final double y, final double z);

	WB_Coord mulAddMul3D(final double f, final double g, final WB_Coord p);

	WB_Coord mulAddMul3D(final double f, final double g, final double x, final double y, final double z);
}
