/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Point;

/**
 *
 */
public class HE_FaceIntersection {

	/**
	 * 
	 */
	public HE_Face face;

	/**
	 * 
	 */
	public WB_Point point;

	/**
	 * 
	 *
	 * @param f
	 * @param p
	 */
	public HE_FaceIntersection(final HE_Face f, final WB_Point p) {
		face = f;
		point = p;
	}
}
