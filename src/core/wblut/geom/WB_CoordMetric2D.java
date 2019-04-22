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
 * Interface for implementing metric operations on 2D coordinates.
 *
 * None of the operators change the calling object.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordMetric2D {

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
}
