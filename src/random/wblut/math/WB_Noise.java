/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

public interface WB_Noise {

	/**
	 *
	 *
	 * @param seed
	 */
	public void setSeed(long seed);

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public double value1D(double x);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public double value2D(double x, double y);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double value3D(double x, double y, double z);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return
	 */
	public double value4D(double x, double y, double z, double w);

	/**
	 *
	 *
	 * @param sx
	 */
	public void setScale(double sx);

	/**
	 *
	 *
	 * @param sx
	 * @param sy
	 */
	public void setScale(double sx, double sy);

	/**
	 *
	 *
	 * @param sx
	 * @param sy
	 * @param sz
	 */
	public void setScale(double sx, double sy, double sz);

	/**
	 *
	 *
	 * @param sx
	 * @param sy
	 * @param sz
	 * @param sw
	 */
	public void setScale(double sx, double sy, double sz, double sw);
}
