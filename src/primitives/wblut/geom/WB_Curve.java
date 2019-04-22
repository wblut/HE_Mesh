/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

public interface WB_Curve {

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_Point getPointOnCurve(double u);

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_Vector getDirectionOnCurve(double u);

	/**
	 *
	 *
	 * @param u
	 * @return
	 */
	public WB_Vector getDerivative(double u);

	/**
	 *
	 *
	 * @return
	 */
	public double getLowerU();

	/**
	 *
	 *
	 * @return
	 */
	public double getUpperU();
}
