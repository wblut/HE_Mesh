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
 *
 *
 */
public class WB_DefaultMap2D implements WB_Map2D {

	/**
	 *
	 */
	public WB_DefaultMap2D() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo2D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {

		result.set(p.xd(), p.yd(), 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo2D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {

		result.set(x, y, 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo3D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {

		result.set(p.xd(), p.yd(), 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo3D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {

		result.set(u, v, w);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo3D(double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapPoint2D(final double u, final double v, final WB_MutableCoord result) {

		result.set(u, v, 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapPoint2D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapPoint2D(final WB_Coord p, final WB_MutableCoord result) {

		result.set(p.xf(), p.yf(), 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo2D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapVector3D(final WB_Coord v, final WB_MutableCoord result) {

		result.set(v.xd(), v.yd(), 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo2D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {

		result.set(x, y, 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo3D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapVector3D(final WB_Coord v, final WB_MutableCoord result) {

		result.set(v.xd(), v.yd(), 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo3D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {

		result.set(u, v, w);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo3D(double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapVector2D(final double u, final double v, final WB_MutableCoord result) {

		result.set(u, v, 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapVector2D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapVector2D(final WB_Coord v, final WB_MutableCoord result) {

		result.set(v.xf(), v.yf(), 0);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		WB_Point result = new WB_Point();

		result.set(p.xd(), p.yd(), 0);

		return result;
	}

	/*
	 * @Override(non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(double, double, double)
	 */
	@Override
	public WB_Point mapPoint3D(final double x, final double y, final double z) {
		WB_Point result = new WB_Point();

		result.set(x, y, 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapPoint3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point unmapPoint3D(final WB_Coord p) {
		WB_Point result = new WB_Point();

		result.set(p.xd(), p.yd(), 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapPoint3D(double, double, double)
	 */
	@Override
	public WB_Point unmapPoint3D(final double u, final double v, final double w) {
		WB_Point result = new WB_Point();

		result.set(u, v, w);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapPoint2D(double, double)
	 */
	@Override
	public WB_Point unmapPoint2D(final double u, final double v) {
		WB_Point result = new WB_Point();

		result.set(u, v, 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapPoint2D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point unmapPoint2D(final WB_Coord p) {
		WB_Point result = new WB_Point();

		result.set(p.xf(), p.yf(), 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapVector3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Vector mapVector3D(final WB_Coord v) {
		WB_Vector result = new WB_Vector();

		result.set(v.xd(), v.yd(), 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapVector3D(double, double, double)
	 */
	@Override
	public WB_Vector mapVector3D(final double x, final double y, final double z) {
		WB_Vector result = new WB_Vector();

		result.set(x, y, 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapVector3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Vector unmapVector3D(final WB_Coord v) {
		WB_Vector result = new WB_Vector();

		result.set(v.xd(), v.yd(), 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapVector3D(double, double, double)
	 */
	@Override
	public WB_Vector unmapVector3D(final double u, final double v, final double w) {
		WB_Vector result = new WB_Vector();

		result.set(u, v, w);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapVector2D(double, double)
	 */
	@Override
	public WB_Vector unmapVector2D(final double u, final double v) {
		WB_Vector result = new WB_Vector();

		result.set(u, v, 0);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapVector2D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Vector unmapVector2D(final WB_Coord v) {
		WB_Vector result = new WB_Vector();

		result.set(v.xf(), v.yf(), 0);

		return result;
	}
}
