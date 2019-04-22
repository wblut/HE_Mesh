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
 * Interface for parameterized surfaces.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public interface WB_Surface {
	/**
	 * Retrieve the point at values (u,v).
	 *
	 * @param u
	 *
	 * @param v
	 *
	 * @return WB_Point
	 */
	public WB_Point surfacePoint(double u, double v);

	/**
	 * Get the lower end of the u parameter range.
	 *
	 * @return u
	 */
	public double getLowerU();

	/**
	 * Get the upper end of the u parameter range.
	 *
	 * @return u
	 */
	public double getUpperU();

	/**
	 * Get the lower end of the v parameter range.
	 *
	 * @return v
	 */
	public double getLowerV();

	/**
	 * Get the upper end of the v parameter range.
	 *
	 * @return v
	 */
	public double getUpperV();
}
