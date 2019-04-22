/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

import wblut.geom.WB_Coord;

/**
 *
 *
 *
 */
public interface WB_VectorParameter {
	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public WB_Coord evaluate(double... x);
}
