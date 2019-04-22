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
 * Interface for implementing mutable transformation operations on 2D
 * coordinates.
 *
 * All of the operators defined in the interface change the calling object. All
 * operators use the label "Self", such as "scale2DSelf" to indicate this.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_MutableCoordTransform2D extends WB_CoordTransform2D {
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
