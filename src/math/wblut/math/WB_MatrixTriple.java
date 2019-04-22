/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

public class WB_MatrixTriple {
	public int i; // row
	public int j; // col
	public double value;

	public WB_MatrixTriple(final int i, final int j, final double value) {
		this.i = i;
		this.j = j;
		this.value = value;
	}
}
