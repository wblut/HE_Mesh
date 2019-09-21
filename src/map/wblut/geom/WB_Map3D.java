/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/

 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
 */
package wblut.geom;

/**
 *
 * WB_Map is an interface for classes that transform between 3D coordinates
 * through some form of mapping.
 *
 *
 */
public interface WB_Map3D {

	/**
	 * Map 3D point: p->map(p)
	 *
	 * @param p
	 *            3D point
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void mapPoint3D(WB_Coord p, WB_MutableCoord result);

	/**
	 * Map 3D point: p->map(p)
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void mapPoint3D(double x, double y, double z, WB_MutableCoord result);

	/**
	 * Unmap 3D point: map(p)->p
	 *
	 * @param p
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void unmapPoint3D(WB_Coord p, WB_MutableCoord result);

	/**
	 * Unmap 3D point: map(p)->p
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void unmapPoint3D(double u, double v, double w, WB_MutableCoord result);

	/**
	 * Map 3D vector: p->map(p)
	 *
	 * @param p
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void mapVector3D(WB_Coord p, WB_MutableCoord result);

	/**
	 * Map 3D vector: p->map(p)
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void mapVector3D(double x, double y, double z, WB_MutableCoord result);

	/**
	 * Unmap 3D vector: map(p)->p
	 *
	 * @param p
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void unmapVector3D(WB_Coord p, WB_MutableCoord result);

	/**
	 * Unmap 3D vector: map(p)->p
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @param result
	 *            object implementing the WB_MutableCoordinate interface to
	 *            receive the result;
	 */
	public void unmapVector3D(double u, double v, double w, WB_MutableCoord result);

	/**
	 * Map 3D point: p->map(p)
	 *
	 * @param p
	 *            3D point
	 * @return new WB_ WB_Point
	 */
	public WB_Point mapPoint3D(WB_Coord p);

	/**
	 * Map 3D point: p->map(p)
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return new WB_ WB_Point
	 */

	public WB_Point mapPoint3D(double x, double y, double z);

	/**
	 * Unmap 3D point: map(p)->p
	 *
	 * @param p
	 * @return new WB_ WB_Point
	 */
	public WB_Point unmapPoint3D(WB_Coord p);

	/**
	 * Unmap 3D point: map(p)->p
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return new WB_Point
	 */
	public WB_Point unmapPoint3D(double u, double v, double w);

	/**
	 * Map 3D vector: p->map(p)
	 *
	 * @param p
	 * @return new WB_Vector
	 */
	public WB_Vector mapVector3D(WB_Coord p);

	/**
	 * Map 3D vector: p->map(p)
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return new WB_Vector
	 */
	public WB_Vector mapVector3D(double x, double y, double z);

	/**
	 * Unmap 3D vector: map(p)->p
	 *
	 * @param p
	 * @return new WB_Vector
	 */
	public WB_Vector unmapVector3D(WB_Coord p);

	/**
	 * Unmap 3D vector: map(p)->p
	 *
	 * @param u
	 * @param v
	 * @param w
	 * @return new WB_Vector
	 */
	public WB_Vector unmapVector3D(double u, double v, double w);

}
