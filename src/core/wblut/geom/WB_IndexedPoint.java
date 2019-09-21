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
 * @author FVH
 *
 */
public class WB_IndexedPoint extends WB_Point {
	int index;

	/**
	 *
	 */
	public WB_IndexedPoint() {
		super();
		setIndex(-1);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param i
	 */
	public WB_IndexedPoint(final double x, final double y, final int i) {
		super(x, y);
		setIndex(i);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param i
	 */
	public WB_IndexedPoint(final double x, final double y, final double z, final int i) {
		super(x, y, z);
		setIndex(i);
	}

	/**
	 *
	 * @param v
	 * @param i
	 */
	public WB_IndexedPoint(final WB_Coord v, final int i) {
		super(v);
		setIndex(i);
	}

	/**
	 *
	 * @param i
	 */
	void setIndex(final int i) {
		index = i;
	}

	/**
	 *
	 * @return
	 */
	int getIndex() {
		return index;

	}

}
