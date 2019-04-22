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
 * Interface for implementing metric operations on 4D coordinates.
 *
 * None of the operators change the calling object.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordMetric4D {

	/**
	 * Length of 4D coordinate.
	 *
	 * @return
	 */
	public double getLength4D();

	/**
	 * Square length of 4D coordinate.
	 *
	 * @return
	 */
	public double getSqLength4D();

	/**
	 * 4D distance to coordinate
	 *
	 * @param p
	 * @return
	 */
	public double getDistance4D(final WB_Coord p);

	/**
	 * Square 4D distance to coordinate.
	 *
	 * @param p
	 * @return
	 */
	public double getSqDistance4D(final WB_Coord p);

	/**
	 * Is this a degenerate vector?
	 *
	 * @return
	 */
	public boolean isZero();

}
