/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

public interface HET_InfoVertex<E extends Object> {

	/**
	 *
	 *
	 * @param vertex
	 * @return
	 */
	public E retrieve(final HE_Vertex vertex);
}