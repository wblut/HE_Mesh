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
import processing.core.PGraphics;

/**
 *
 */
public class WB_Processing {
	/**
	 *
	 */
	protected PGraphics home;

	protected WB_Processing() {

	}

	/**
	 *
	 *
	 * @param home
	 */
	public WB_Processing(final PApplet home) {
		if (home.g == null) {
			throw new IllegalArgumentException("WB_Render3D can only be used after size()");
		}
		this.home = home.g;
	}

	/**
	 *
	 *
	 * @param home
	 */
	public WB_Processing(final PGraphics home) {
		this.home = home;
	}

	/**
	 *
	 *
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int color(final int r, final int g, final int b) {
		return 255 << 24 | r << 16 | g << 8 | b;
	}
}
