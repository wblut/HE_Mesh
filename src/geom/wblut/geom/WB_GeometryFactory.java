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
public class WB_GeometryFactory extends WB_GeometryFactory3D {
	public WB_GeometryFactory() {
		super();

	}

	/**
	 * Legacy code, WB_GeometryFactory used to be a singleton but this limited
	 * its use in multithreaded code.
	 *
	 * @return
	 */

	public static WB_GeometryFactory instance() {
		return new WB_GeometryFactory();

	}
}
