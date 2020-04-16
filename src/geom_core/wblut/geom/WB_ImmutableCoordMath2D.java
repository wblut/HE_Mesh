package wblut.geom;

public interface WB_ImmutableCoordMath2D {
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

	double dot2D(final WB_Coord p);

	double absDot2D(final WB_Coord p);

	double getLength2D();

	double getSqLength2D();

	double getDistance2D(final WB_Coord p);

	double getSqDistance2D(final WB_Coord p);

	double getAngle(final WB_Coord p);

	double getAngleNorm(final WB_Coord p);

	double getHeading2D();

	WB_Coord getOrthoNormal2D();

	boolean isZero();

	boolean isCollinear2D(WB_Coord p, WB_Coord q);

	boolean isParallel2D(WB_Coord p);

	boolean isParallel2D(WB_Coord p, double tol);

	boolean isParallelNorm2D(WB_Coord p);

	boolean isParallelNorm2D(WB_Coord p, double tol);

	boolean isOrthogonal2D(WB_Coord p);

	boolean isOrthogonal2D(WB_Coord p, double tol);

	boolean isOrthogonalNorm2D(WB_Coord p);

	boolean isOrthogonalNorm2D(WB_Coord p, double tol);

	WB_Coord applyAsPoint2D(final WB_Transform2D T);

	WB_Coord applyAsVector2D(final WB_Transform2D T);

	WB_Coord applyAsNormal2D(final WB_Transform2D T);

	WB_Coord translate2D(final double px, final double py);

	WB_Coord translate2D(final WB_Coord p);

	WB_Coord rotateAboutPoint2D(final double angle, final double px, final double py);

	WB_Coord rotateAboutPoint2D(final double angle, final WB_Coord p);

	WB_Coord rotateAboutOrigin2D(final double angle);

	WB_Coord scale2D(final double fx, final double fy);

	WB_Coord scale2D(final double f);

	WB_Coord applyAsPoint2DSelf(final WB_Transform2D T);

	WB_Coord applyAsVector2DSelf(final WB_Transform2D T);

	WB_Coord applyAsNormal2DSelf(final WB_Transform2D T);

	WB_Coord rotateAboutPoint2DSelf(final double angle, final WB_Coord p);

	WB_Coord rotateAboutPoint2DSelf(final double angle, final double px, final double py);

	WB_Coord rotateAboutOrigin2DSelf(final double angle);

	WB_Coord translate2DSelf(final double px, final double py);

	WB_Coord translate2DSelf(final WB_Coord p);

	WB_Coord scale2DSelf(final double f);

	WB_Coord scale2DSelf(final double fx, final double fy);
}
