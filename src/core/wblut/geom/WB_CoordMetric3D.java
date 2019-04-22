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
 * Interface for implementing metric operations on 3D coordinates.
 *
 * None of the operators change the calling object.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordMetric3D extends WB_CoordMetric2D {

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

}
