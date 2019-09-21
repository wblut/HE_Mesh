/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

/**
 * Interface for implementing non-mutable mathematical operations on 2D
 * coordinates.If the operations should change the calling object use
 * {@link wblut.geom.WB_MutableCoordMath2D}.
 *
 * None of the operators change the calling object. Unlabelled operators, such
 * as "add",create a new WB_Coord. Operators with the label "Into", such as
 * "addInto" store the result into a WB_MutableCoord passed as additional
 * parameter.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordMath2D {

	/**
	 * Add coordinate values.
	 *
	 * @param x
	 * @return new WB_Coord
	 */
	public WB_Coord add(final double... x);

	/**
	 * Add coordinate values.
	 *
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord add(final WB_Coord p);

	/**
	 * Subtract coordinate values.
	 *
	 * @param x
	 * @return new WB_Coord
	 */
	public WB_Coord sub(final double... x);

	/**
	 * Subtract coordinate values.
	 *
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord sub(final WB_Coord p);

	/**
	 * Multiply by factor.
	 *
	 * @param f
	 * @return new WB_Coord
	 */
	public WB_Coord mul(final double f);

	/**
	 * Divide by factor.
	 *
	 * @param f
	 * @return new WB_Coord
	 */
	public WB_Coord div(final double f);

	/**
	 * Add multiple of coordinate values.
	 *
	 * @param f
	 *            multiplier
	 * @param x
	 * @return new WB_Coord
	 */
	public WB_Coord addMul(final double f, final double... x);

	/**
	 * Add multiple of coordinate values.
	 *
	 * @param f
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord addMul(final double f, final WB_Coord p);

	/**
	 * Multiply this coordinate by factor f and add other coordinate values
	 * multiplied by g.
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return new WB_Coord
	 */
	public WB_Coord mulAddMul(final double f, final double g, final double... x);

	/**
	 * Multiply this coordinate by factor f and add other coordinate values
	 * multiplied by g.
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord mulAddMul(final double f, final double g, final WB_Coord p);

	/**
	 * 2D dot product.
	 *
	 * @param p
	 * @return 2D dot product
	 */
	public double dot2D(final WB_Coord p);

	/**
	 * Absolute value of 2D dot product.
	 *
	 * @param p
	 * @return absolute value of 2D dot product
	 */
	public double absDot2D(final WB_Coord p);

	/**
	 * Length of 2D coordinate.
	 *
	 * @return
	 */
	public double getLength2D();

	/**
	 * Square length of 2D coordinate.
	 *
	 * @return
	 */
	public double getSqLength2D();

	/**
	 * 2D distance to coordinate
	 *
	 * @param p
	 * @return
	 */
	public double getDistance2D(final WB_Coord p);

	/**
	 * Square 2D distance to coordinate.
	 *
	 * @param p
	 * @return
	 */
	public double getSqDistance2D(final WB_Coord p);

	/**
	 * Angle between two vectors.
	 *
	 * @param p
	 * @return
	 */
	public double getAngle(final WB_Coord p);

	/**
	 * Angle between two normalized vectors.
	 *
	 * @param p
	 * @return
	 */
	public double getAngleNorm(final WB_Coord p);

	/**
	 * 2D heading of vector.
	 *
	 * @return
	 */
	public double getHeading2D();

	/**
	 * Get 2D vector perpendicular and CCW to this one.
	 *
	 * @return
	 */
	public WB_Coord getOrthoNormal2D();

	/**
	 * Is this a degenerate vector?
	 *
	 * @return
	 */
	public boolean isZero();

	/**
	 * Is this point collinear with two other points?
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public boolean isCollinear2D(WB_Coord p, WB_Coord q);

	/**
	 * Is this vector parallel with other vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isParallel2D(WB_Coord p);

	/**
	 * Is this vector, within a given tolerance, parallel with other vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isParallel2D(WB_Coord p, double tol);

	/**
	 * Is this normalized vector parallel with other normalized vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isParallelNorm2D(WB_Coord p);

	/**
	 * Is this normalized vector, within a given tolerance, parallel with other
	 * normalized vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isParallelNorm2D(WB_Coord p, double tol);

	/**
	 * Is this vector perpendicular to other vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isOrthogonal2D(WB_Coord p);

	/**
	 * Is this vector, within a given tolerance, perpendicular to other vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isOrthogonal2D(WB_Coord p, double tol);

	/**
	 * Is this normalized vector perpendicular to other normalized vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isOrthogonalNorm2D(WB_Coord p);

	/**
	 * Is this normalized vector, within a given tolerance, perpendicular to
	 * other normalized vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isOrthogonalNorm2D(WB_Coord p, double tol);

	/**
	 * Apply WB_Transform2D as point.
	 *
	 * @param T
	 * @return new WB_Coord
	 */
	public WB_Coord applyAsPoint2D(final WB_Transform2D T);

	/**
	 * Apply WB_Transform2D as vector.
	 *
	 * @param T
	 * @return new WB_Coord
	 */
	public WB_Coord applyAsVector2D(final WB_Transform2D T);

	/**
	 * Apply WB_Transform2D as normal.
	 *
	 * @param T
	 * @return new WB_Coord
	 */
	public WB_Coord applyAsNormal2D(final WB_Transform2D T);

	/**
	 * 2D translate.
	 *
	 * @param px
	 * @param py
	 * @return new WB_Coord
	 */
	public WB_Coord translate2D(final double px, final double py);

	/**
	 * 2D translate.
	 *
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord translate2D(final WB_Coord p);

	/**
	 * Rotate around point.
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutPoint2D(final double angle, final double px,
			final double py);

	/**
	 * Rotate around point.
	 *
	 * @param angle
	 * @param p
	 *
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutPoint2D(final double angle, final WB_Coord p);

	/**
	 * Rotate around origin.
	 *
	 * @param angle
	 *
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutOrigin2D(final double angle);

	/**
	 * Non-uniform scale.
	 *
	 * @param fx
	 * @param fy
	 * @return new WB_Coord
	 */
	public WB_Coord scale2D(final double fx, final double fy);

	/**
	 * Uniform scale.
	 *
	 * @param f
	 * @return new WB_Coord
	 */
	public WB_Coord scale2D(final double f);

	/**
	 *
	 *
	 * @param T
	 * @return this
	 */
	public WB_Coord applyAsPoint2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return this
	 */
	public WB_Coord applyAsVector2DSelf(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return this
	 */
	public WB_Coord applyAsNormal2DSelf(final WB_Transform2D T);

	/**
	 * 
	 * @param angle
	 * @param p
	 * @return
	 */
	public WB_Coord rotateAboutPoint2DSelf(final double angle,
			final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return this
	 */
	public WB_Coord rotateAboutPoint2DSelf(final double angle, final double px,
			final double py);

	/**
	 *
	 *
	 * @param angle
	 *
	 * @return this
	 */
	public WB_Coord rotateAboutOrigin2DSelf(final double angle);

	/**
	 * 2D translate.
	 *
	 * @param px
	 * @param py
	 * @return this
	 */
	public WB_Coord translate2DSelf(final double px, final double py);

	/**
	 * 2D translate.
	 *
	 * @param p
	 * @return this
	 */
	public WB_Coord translate2DSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord scale2DSelf(final double f);

	/**
	 *
	 *
	 * @param fx
	 * @param fy
	 * @return this
	 */
	public WB_Coord scale2DSelf(final double fx, final double fy);

}
