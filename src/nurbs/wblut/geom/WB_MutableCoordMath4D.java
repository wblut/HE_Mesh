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
 * Interface for implementing mutable mathematical operations on 4D coordinates.
 *
 * All of the operators defined in the interface change the calling object. All
 * operators use the label "Self", such as "addSelf" to indicate this.
 *
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_MutableCoordMath4D extends WB_CoordMath4D {
	/**
	 *
	 *
	 * @param x
	 *
	 * @return this
	 */
	public WB_Coord addSelf(final double... x);

	/**
	 *
	 *
	 * @param p
	 * @return this
	 */
	public WB_Coord addSelf(final WB_Coord p);

	/**
	 *
	 *
	 * @param x
	 *
	 * @return this
	 */
	public WB_Coord subSelf(final double... x);

	/**
	 *
	 *
	 * @param v
	 * @return this
	 */
	public WB_Coord subSelf(final WB_Coord v);

	/**
	 *
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord mulSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord divSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @param x
	 *
	 * @return this
	 */
	public WB_Coord addMulSelf(final double f, final double... x);

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return this
	 */
	public WB_Coord addMulSelf(final double f, final WB_Coord p);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param x
	 * @return this
	 */
	public WB_Coord mulAddMulSelf(final double f, final double g, final double... x);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return this
	 */
	public WB_Coord mulAddMulSelf(final double f, final double g, final WB_Coord p);

	/**
	 *
	 *
	 * @return this
	 */
	public double normalizeSelf();

	/**
	 *
	 *
	 * @param d
	 * @return this
	 */
	public WB_Coord trimSelf(final double d);

	/**
	 *
	 *
	 * @param p
	 * @return this
	 */
	public WB_Coord add3DSelf(final WB_Coord p);

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Coord add3DSelf(final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param v
	 * @return this
	 */
	public WB_Coord sub3DSelf(final WB_Coord v);

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Coord sub3DSelf(final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord mul3DSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @return this
	 */
	public WB_Coord div3DSelf(final double f);

	/**
	 *
	 *
	 * @param f
	 * @param p
	 * @return this
	 */
	public WB_Coord addMul3DSelf(final double f, final WB_Coord p);

	/**
	 * 
	 * @param f
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Coord addMul3DSelf(final double f, final double x, final double y, final double z);

	/**
	 *
	 *
	 * @param f
	 * @param g
	 * @param p
	 * @return this
	 */
	public WB_Coord mulAddMul3DSelf(final double f, final double g, final WB_Coord p);

	/**
	 * 
	 * @param f
	 * @param g
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Coord mulAddMul3DSelf(final double f, final double g, final double x, final double y, final double z);
}
