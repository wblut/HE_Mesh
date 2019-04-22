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

}
