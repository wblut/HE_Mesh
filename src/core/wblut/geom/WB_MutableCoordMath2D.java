/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/

 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
 */
package wblut.geom;

/**
 * Interface for implementing mutable mathematical operations on 2D coordinates.
 *
 * All of the operators defined in the interface change the calling object. All
 * operators use the label "Self", such as "addSelf" to indicate this.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_MutableCoordMath2D extends WB_CoordMath2D {
	/**
	 * Add coordinate values.
	 *
	 * @param x
	 * @return this
	 */
	public WB_Coord addSelf(final double... x);

	/**
	 * Add coordinate values.
	 *
	 * @param p
	 * @return this
	 */
	public WB_Coord addSelf(final WB_Coord p);

	/**
	 * Subtract coordinate values.
	 *
	 * @param x
	 * @return this
	 */
	public WB_Coord subSelf(final double... x);

	/**
	 * Subtract coordinate values.
	 *
	 * @param p
	 * @return this
	 */
	public WB_Coord subSelf(final WB_Coord p);

	/**
	 * Multiply by factor.
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord mulSelf(final double f);

	/**
	 * Divide by factor.
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord divSelf(final double f);

	/**
	 * Add multiple of coordinate values.
	 *
	 * @param f
	 *            multiplier
	 * @param x
	 * @return this
	 */
	public WB_Coord addMulSelf(final double f, final double... x);

	/**
	 * Add multiple of coordinate values.
	 *
	 * @param f
	 * @param p
	 * @return this
	 */
	public WB_Coord addMulSelf(final double f, final WB_Coord p);

	/**
	 * Multiply this coordinate by factor f and add other coordinate values
	 * multiplied by g.
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return this
	 */
	public WB_Coord mulAddMulSelf(final double f, final double g, final double... x);

	/**
	 * Multiply this coordinate by factor f and add other coordinate values
	 * multiplied by g.
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return this
	 */
	public WB_Coord mulAddMulSelf(final double f, final double g, final WB_Coord p);

	/**
	 * Normalize this vector. Return the length before normalization. If this
	 * vector is degenerate 0 is returned and the vector remains the zero
	 * vector.
	 *
	 * @return this
	 */
	public double normalizeSelf();

	/**
	 * If vector is larger than given value, trim vector.
	 *
	 * @param d
	 * @return this
	 */
	public WB_Coord trimSelf(final double d);

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
	public WB_Coord rotateAboutPoint2DSelf(final double angle, final WB_Coord p);

	/**
	 *
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @return this
	 */
	public WB_Coord rotateAboutPoint2DSelf(final double angle, final double px, final double py);

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
