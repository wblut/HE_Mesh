/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

/**
 * Interface for implementing non-mutable transformation operations on 2D
 * coordinates.If the operations should change the calling object use
 * {@link wblut.geom.WB_MutableCoordTransform2D}.
 *
 * None of the operators change the calling object. Unlabelled operators, such
 * as "scale2D",create a new WB_Coord. Operators with the label "Into", such as
 * "scale2DInto" store the result into a WB_MutableCoord passed as additional
 * parameter.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordTransform2D {
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
