/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.core;

public class WB_Disclaimer {
	private WB_Disclaimer() {
	};

	public static final WB_Disclaimer CURRENT_DISCLAIMER = new WB_Disclaimer();

	@Override
	public String toString() {
		final String dis = "License: https://github.com/wblut/HE_Mesh#license";
		return dis;
	}

	public static String disclaimer() {
		return CURRENT_DISCLAIMER.toString();
	}
}