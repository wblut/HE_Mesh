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
 * {@link wblut.geom.WB_MutableCoordMath3D}.
 *
 * None of the operators change the calling object. Unlabelled operators, such
 * as "add",create a new WB_Coord. Operators with the label "Into", such as
 * "addInto" store the result into a WB_MutableCoord passed as additional
 * parameter.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordMath3D extends WB_CoordMath2D {
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
}
