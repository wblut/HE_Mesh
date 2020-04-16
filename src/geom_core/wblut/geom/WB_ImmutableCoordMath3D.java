package wblut.geom;

import wblut.math.WB_M33;

public interface WB_ImmutableCoordMath3D extends WB_ImmutableCoordMath2D {
	double dot(final WB_Coord p);

	double absDot(final WB_Coord p);

	WB_Coord cross(final WB_Coord p);

	WB_M33 tensor(final WB_Coord v);

	double scalarTriple(final WB_Coord v, final WB_Coord w);

	double getLength3D();

	double getSqLength3D();

	double getDistance3D(final WB_Coord p);

	double getSqDistance3D(final WB_Coord p);

	WB_Coord getOrthoNormal3D();

	double getLength();

	double getSqLength();

	double getDistance(final WB_Coord p);

	double getSqDistance(final WB_Coord p);

	WB_Coord getOrthoNormal();

	@Override
	boolean isZero();

	boolean isCollinear(WB_Coord p, WB_Coord q);

	boolean isParallel(WB_Coord p);

	boolean isParallel(WB_Coord p, double tol);

	boolean isParallelNorm(WB_Coord p);

	boolean isParallelNorm(WB_Coord p, double tol);

	boolean isOrthogonal(WB_Coord p);

	boolean isOrthogonal(WB_Coord p, double tol);

	boolean isOrthogonalNorm(WB_Coord p);

	boolean isOrthogonalNorm(WB_Coord p, double tol);

	WB_Coord applyAsPoint(final WB_Transform3D T);

	WB_Coord applyAsVector(final WB_Transform3D T);

	WB_Coord applyAsNormal(final WB_Transform3D T);

	WB_Coord translate(final double px, final double py, double pz);

	WB_Coord translate(final WB_Coord p);

	WB_Coord rotateAboutAxis2P(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z);

	WB_Coord rotateAboutAxis2P(final double angle, final WB_Coord p1, final WB_Coord p2);

	WB_Coord rotateAboutAxis(final double angle, final double px, final double py, final double pz, final double ax,
			final double ay, final double az);

	WB_Coord rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord a);

	WB_Coord rotateAboutOrigin(final double angle, final double x, final double y, final double z);

	WB_Coord rotateAboutOrigin(final double angle, final WB_Coord v);

	WB_Coord scale(final double fx, final double fy, final double fz);

	WB_Coord scale(final double f);
}
