/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import wblut.math.WB_M33;

/**
 * Interface for implementing non-mutable mathematical operations on 3D
 * coordinates.If the operations should change the calling object use
 * {@link wblut.geom.WB_MutableCoordMath}.
 *
 * None of the operators change the calling object. Unlabelled operators, such
 * as "add",create a new WB_Coord. Operators with the label "Into", such as
 * "addInto" store the result into a WB_MutableCoord passed as additional
 * parameter.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordMath extends WB_CoordMath2D {
	/**
	 * dot product.
	 *
	 * @param p
	 * @return dot product
	 */
	public double dot(final WB_Coord p);

	/**
	 * Absolute value of dot product.
	 *
	 * @param p
	 * @return absolute value of dot product
	 */
	public double absDot(final WB_Coord p);

	/**
	 * Cross product of this coordinate with other coordinate.
	 *
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord cross(final WB_Coord p);

	/**
	 * Tensor product.
	 *
	 * @param v
	 * @return tensor product
	 */
	public WB_M33 tensor(final WB_Coord v);

	/**
	 * Scalar triple: this.(v x w)
	 *
	 * @param v
	 * @param w
	 * @return scalar triple
	 */
	public double scalarTriple(final WB_Coord v, final WB_Coord w);

	/**
	 *
	 *
	 * @return
	 */
	public double getLength3D();

	/**
	 *
	 *
	 * @return
	 */
	public double getSqLength3D();

	/**
	 *
	 * @param p
	 * @return
	 */
	public double getDistance3D(final WB_Coord p);

	/**
	 *
	 * @param p
	 * @return
	 */
	public double getSqDistance3D(final WB_Coord p);

	/**
	 *
	 *
	 * @return
	 */
	
	public WB_Coord getOrthoNormal3D();

	/**
	 * Length of 3D coordinate.
	 *
	 * @return
	 */
	public double getLength();

	/**
	 * Square length of 3D coordinate.
	 *
	 * @return
	 */
	public double getSqLength();

	/**
	 * 3D distance to coordinate
	 *
	 * @param p
	 * @return
	 */
	public double getDistance(final WB_Coord p);

	/**
	 * Square 3D distance to coordinate.
	 *
	 * @param p
	 * @return
	 */
	public double getSqDistance(final WB_Coord p);

	/**
	 * Get vector perpendicular and CCW to this one.
	 *
	 * @return
	 */
	public WB_Coord getOrthoNormal();

	/**
	 * Is this a degenerate vector?
	 *
	 * @return
	 */
	@Override
	public boolean isZero();

	/**
	 * Is this point collinear with two other points?
	 *
	 * @param p
	 * @param q
	 * @return
	 */
	public boolean isCollinear(WB_Coord p, WB_Coord q);

	/**
	 * Is this vector parallel with other vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isParallel(WB_Coord p);

	/**
	 * Is this vector, within a given tolerance, parallel with other vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isParallel(WB_Coord p, double tol);

	/**
	 * Is this normalized vector parallel with other normalized vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isParallelNorm(WB_Coord p);

	/**
	 * Is this normalized vector, within a given tolerance, parallel with other
	 * normalized vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isParallelNorm(WB_Coord p, double tol);

	/**
	 * Is this vector perpendicular to other vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isOrthogonal(WB_Coord p);

	/**
	 * Is this vector, within a given tolerance, perpendicular to other vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isOrthogonal(WB_Coord p, double tol);

	/**
	 * Is this normalized vector perpendicular to other normalized vector?
	 *
	 * @param p
	 * @return
	 */
	public boolean isOrthogonalNorm(WB_Coord p);

	/**
	 * Is this normalized vector, within a given tolerance, perpendicular to
	 * other normalized vector?
	 *
	 * @param p
	 * @param tol
	 * @return
	 */
	public boolean isOrthogonalNorm(WB_Coord p, double tol);

	/**
	 * Apply WB_Transform as point.
	 *
	 * @param T
	 * @return new WB_Coord
	 */
	public WB_Coord applyAsPoint(final WB_Transform3D T);

	/**
	 * Apply WB_Transform as vector.
	 *
	 * @param T
	 * @return new WB_Coord
	 */
	public WB_Coord applyAsVector(final WB_Transform3D T);

	/**
	 * Apply WB_Transform as normal.
	 *
	 * @param T
	 * @return new WB_Coord
	 */
	public WB_Coord applyAsNormal(final WB_Transform3D T);

	/**
	 * 3D translate.
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @return new WB_Coord
	 */
	public WB_Coord translate(final double px, final double py, double pz);

	/**
	 * 3D translate.
	 *
	 * @param p
	 * @return new WB_Coord
	 */
	public WB_Coord translate(final WB_Coord p);

	/**
	 * Rotate around axis defined by two points.
	 *
	 * @param angle
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @param p2x
	 * @param p2y
	 * @param p2z
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutAxis2P(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z);

	/**
	 * Rotate around axis defined by two points.
	 *
	 * @param angle
	 * @param p1
	 * @param p2
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutAxis2P(final double angle, final WB_Coord p1, final WB_Coord p2);

	/**
	 * Rotate around axis defined by point and direction.
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @param pz
	 * @param ax
	 * @param ay
	 * @param az
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutAxis(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az);

	/**
	 * Rotate around axis defined by point and direction.
	 *
	 * @param angle
	 * @param p
	 * @param a
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord a);

	/**
	 * Rotate around axis defined by origin and direction.
	 *
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 *
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutOrigin(final double angle, final double x, final double y, final double z);

	/**
	 * Rotate around axis defined by origin and direction.
	 *
	 *
	 * @param angle
	 * @param v
	 * @return new WB_Coord
	 */
	public WB_Coord rotateAboutOrigin(final double angle, final WB_Coord v);

	/**
	 * Non-uniform scale.
	 *
	 * @param fx
	 * @param fy
	 * @param fz
	 * @return
	 */
	public WB_Coord scale(final double fx, final double fy, final double fz);

	/**
	 * Uniform scale.
	 *
	 * @param f
	 * @return
	 */
	public WB_Coord scale(final double f);
}
