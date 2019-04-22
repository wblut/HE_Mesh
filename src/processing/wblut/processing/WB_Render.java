/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.processing;

import processing.core.PApplet;
import processing.opengl.PGraphics3D;

/**
 *
 */
public class WB_Render extends WB_Render3D {

	/**
	 * 
	 *
	 * @param home
	 */
	public WB_Render(final PApplet home) {
		super(home);
	}

	/**
	 * 
	 *
	 * @param home
	 */
	public WB_Render(final PGraphics3D home) {
		super(home);
	}
}
