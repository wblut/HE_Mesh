package wblut.geom;

import wblut.math.WB_M33;

/**
 *
 */
public interface WB_ImmutableCoordMath {
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
	WB_Coord cross(final WB_Coord p);

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	WB_M33 tensor(final WB_Coord v);

	/**
	 *
	 *
	 * @param v
	 * @param w
	 * @return
	 */
	double scalarTriple(final WB_Coord v, final WB_Coord w);

	/**
	 *
	 *
	 * @return
	 */
	double getLength3D();

	/**
	 *
	 *
	 * @return
	 */
	double getSqLength3D();

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getDistance3D(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getSqDistance3D(final WB_Coord p);

	/**
	 *
	 *
	 * @return
	 */
	WB_Coord getOrthoNormal3D();

	/**
	 *
	 *
	 * @return
	 */
	double getLength();

	/**
	 *
	 *
	 * @return
	 */
	double getSqLength();

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getDistance(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getSqDistance(final WB_Coord p);

	/**
	 *
	 *
	 * @return
	 */
	WB_Coord getOrthoNormal();

	/**
	 *
	 *
	 * @return
	 */
	boolean isZero();

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	boolean isCollinear(WB_Coord p, WB_Coord q);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isParallel(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isParallel(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isParallelNorm(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isParallelNorm(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isOrthogonal(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isOrthogonal(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isOrthogonalNorm(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isOrthogonalNorm(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsPoint(final WB_Transform3D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsVector(final WB_Transform3D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsNormal(final WB_Transform3D T);

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @return
	 */
	WB_Coord translate(final double px, final double py, double pz);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord translate(final WB_Coord p);

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
	WB_Coord rotateAboutAxis2P(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z);

	/**
	 *
	 *
	 * @param angle
	 * @param p1
	 * @param p2
	 * @return
	 */
	WB_Coord rotateAboutAxis2P(final double angle, final WB_Coord p1, final WB_Coord p2);

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
	WB_Coord rotateAboutAxis(final double angle, final double px, final double py, final double pz, final double ax,
			final double ay, final double az);

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @param a
	 * @return
	 */
	WB_Coord rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord a);

	/**
	 *
	 *
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	WB_Coord rotateAboutOrigin(final double angle, final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param angle
	 * @param v
	 * @return
	 */
	WB_Coord rotateAboutOrigin(final double angle, final WB_Coord v);

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @param fz
	 * @return
	 */
	WB_Coord scale(final double fx, final double fy, final double fz);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord scale(final double f);

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
	double dot2D(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double absDot2D(final WB_Coord p);

	/**
	 *
	 *
	 * @return
	 */
	double getLength2D();

	/**
	 *
	 *
	 * @return
	 */
	double getSqLength2D();

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getDistance2D(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getSqDistance2D(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getAngle(final WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	double getAngleNorm(final WB_Coord p);

	/**
	 *
	 *
	 * @return
	 */
	double getHeading2D();

	/**
	 *
	 *
	 * @return
	 */
	WB_Coord getOrthoNormal2D();

	/**
	 *
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	boolean isCollinear2D(WB_Coord p, WB_Coord q);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isParallel2D(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isParallel2D(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isParallelNorm2D(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isParallelNorm2D(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isOrthogonal2D(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isOrthogonal2D(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	boolean isOrthogonalNorm2D(WB_Coord p);

	/**
	 *
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	boolean isOrthogonalNorm2D(WB_Coord p, double tol);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsPoint2D(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsVector2D(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsNormal2D(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @return
	 */
	WB_Coord translate2D(final double px, final double py);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord translate2D(final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return
	 */
	WB_Coord rotateAboutPoint2D(final double angle, final double px, final double py);

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	WB_Coord rotateAboutPoint2D(final double angle, final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Coord rotateAboutOrigin2D(final double angle);

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @return
	 */
	WB_Coord scale2D(final double fx, final double fy);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord scale2D(final double f);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsPoint2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsVector2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Coord applyAsNormal2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param angle
	 * @param p
	 * @return
	 */
	WB_Coord rotateAboutPoint2DSelf(final double angle, final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return
	 */
	WB_Coord rotateAboutPoint2DSelf(final double angle, final double px, final double py);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Coord rotateAboutOrigin2DSelf(final double angle);

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @return
	 */
	WB_Coord translate2DSelf(final double px, final double py);

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	WB_Coord translate2DSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Coord scale2DSelf(final double f);

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @return
	 */
	WB_Coord scale2DSelf(final double fx, final double fy);
}
