/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

public class WB_TransformMap implements WB_Map {

	private WB_Transform3D T;
	private WB_Transform3D invT;

	/**
	 *
	 *
	 * @param transform
	 */
	public WB_TransformMap(final WB_Transform3D transform) {
		T = transform.get();
		invT = transform.get();
		invT.inverse();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		T.applyAsPointInto(p, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(double, double, double,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		T.applyAsPointInto(x, y, z, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapPoint3D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		invT.applyAsPointInto(p, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapPoint3D(double, double, double,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		invT.applyAsPointInto(u, v, w, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapVector3D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void mapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		T.applyAsVectorInto(p, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapVector3D(double, double, double,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		T.applyAsVectorInto(x, y, z, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapVector3D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		invT.applyAsVectorInto(p, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapVector3D(double, double, double,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		invT.applyAsVectorInto(u, v, w, result);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		return T.applyAsPoint(p);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(double, double, double)
	 */
	@Override
	public WB_Point mapPoint3D(final double x, final double y, final double z) {
		return T.applyAsPoint(x, y, z);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapPoint3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point unmapPoint3D(final WB_Coord p) {
		return invT.applyAsPoint(p);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapPoint3D(double, double, double)
	 */
	@Override
	public WB_Point unmapPoint3D(final double u, final double v, final double w) {
		return invT.applyAsPoint(u, v, w);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapVector3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Vector mapVector3D(final WB_Coord p) {
		return T.applyAsVector(p);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapVector3D(double, double, double)
	 */
	@Override
	public WB_Vector mapVector3D(final double x, final double y, final double z) {
		return T.applyAsVector(x, y, z);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapVector3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Vector unmapVector3D(final WB_Coord p) {
		return invT.applyAsVector(p);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#unmapVector3D(double, double, double)
	 */
	@Override
	public WB_Vector unmapVector3D(final double u, final double v, final double w) {
		return invT.applyAsVector(u, v, w);

	}

}
