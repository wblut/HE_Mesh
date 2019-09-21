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
 * WB_OrthoProject projects coordinates from world space to the X, Y or Z-plane.
 * Since a projection is not reversible, the 2D-to-3D functions always return a
 * point on the X-,Y- or Z-plane, unless the w-coordinate is explicitly given.
 *
 */
public class WB_OrthoProject implements WB_Map2D {
	/**
	 *
	 */
	int id;
	/**
	 *
	 */
	private int mode;
	/**
	 *
	 */
	public static final int YZ = 0;
	/**
	 *
	 */
	public static final int XZ = 1;
	/**
	 *
	 */
	public static final int XY = 2;
	/**
	 *
	 */
	public static final int YZrev = 3;
	/**
	 *
	 */
	public static final int XZrev = 4;
	/**
	 *
	 */
	public static final int XYrev = 5;

	/**
	 *
	 */
	public WB_OrthoProject() {
		this(XY);
	}

	/**
	 *
	 *
	 * @param mode
	 */
	public WB_OrthoProject(final int mode) {
		super();
		if (mode < 0 || mode > 2) {
			throw new IndexOutOfBoundsException();
		}
		this.mode = mode;
	}

	/**
	 *
	 *
	 * @param v
	 */
	public WB_OrthoProject(final WB_Coord v) {
		set(v);
	}

	/**
	 *
	 *
	 * @param P
	 */
	public WB_OrthoProject(final WB_Plane P) {
		set(P.getNormal());
	}

	/**
	 *
	 *
	 * @param c
	 */
	public void set(final WB_Coord c) {
		if (Math.abs(c.xd()) > Math.abs(c.yd())) {
			mode = Math.abs(c.xd()) > Math.abs(c.zd()) ? YZ : XY;
		} else {
			mode = Math.abs(c.yd()) > Math.abs(c.zd()) ? XZ : XY;
		}
		if (mode == XY && c.zd() < 0) {
			mode = XYrev;
		}
		if (mode == YZ && c.xd() < 0) {
			mode = YZrev;
		}
		if (mode == XZ && c.yd() < 0) {
			mode = XZrev;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo2D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(p.xd(), p.yd(), 0);
			break;
		case YZ:
			result.set(p.yd(), p.zd(), 0);
			break;
		case XZ:
			result.set(p.zd(), p.xd(), 0);
			break;
		case XYrev:
			result.set(p.yd(), p.xd(), 0);
			break;
		case YZrev:
			result.set(p.zd(), p.yd(), 0);
			break;
		case XZrev:
			result.set(p.xd(), p.zd(), 0);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo2D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(x, y, 0);
			break;
		case YZ:
			result.set(y, z, 0);
			break;
		case XZ:
			result.set(z, x, 0);
			break;
		case XYrev:
			result.set(y, x, 0);
			break;
		case YZrev:
			result.set(z, y, 0);
			break;
		case XZrev:
			result.set(x, z, 0);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo3D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(p.xd(), p.yd(), 0);
			break;
		case YZ:
			result.set(0, p.xd(), p.yd());
			break;
		case XZ:
			result.set(p.yd(), 0, p.xd());
			break;
		case XYrev:
			result.set(p.yd(), p.xd(), 0);
			break;
		case YZrev:
			result.set(0, p.yd(), p.xd());
			break;
		case XZrev:
			result.set(p.xd(), 0, p.yd());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo3D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(u, v, w);
			break;
		case YZ:
			result.set(w, u, v);
			break;
		case XZ:
			result.set(v, w, u);
			break;
		case XYrev:
			result.set(v, u, -w);
			break;
		case YZrev:
			result.set(-w, v, u);
			break;
		case XZrev:
			result.set(u, -w, v);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#pointTo3D(double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapPoint2D(final double u, final double v, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(u, v, 0);
			break;
		case YZ:
			result.set(0, u, v);
			break;
		case XZ:
			result.set(v, 0, u);
			break;
		case XYrev:
			result.set(v, u, 0);
			break;
		case YZrev:
			result.set(0, v, u);
			break;
		case XZrev:
			result.set(u, 0, v);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapPoint2D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapPoint2D(final WB_Coord p, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(p.xf(), p.yf(), 0);
			break;
		case YZ:
			result.set(0, p.xf(), p.yf());
			break;
		case XZ:
			result.set(p.yf(), 0, p.xf());
			break;
		case XYrev:
			result.set(p.yf(), p.xf(), 0);
			break;
		case YZrev:
			result.set(0, p.yf(), p.xf());
			break;
		case XZrev:
			result.set(p.xf(), 0, p.yf());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo2D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(v.xd(), v.yd(), 0);
			break;
		case YZ:
			result.set(v.yd(), v.zd(), 0);
			break;
		case XZ:
			result.set(v.zd(), v.xd(), 0);
			break;
		case XYrev:
			result.set(v.yd(), v.xd(), 0);
			break;
		case YZrev:
			result.set(v.zd(), v.yd(), 0);
			break;
		case XZrev:
			result.set(v.xd(), v.zd(), 0);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo2D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(x, y, 0);
			break;
		case YZ:
			result.set(y, z, 0);
			break;
		case XZ:
			result.set(z, x, 0);
			break;
		case XYrev:
			result.set(y, x, 0);
			break;
		case YZrev:
			result.set(z, y, 0);
			break;
		case XZrev:
			result.set(x, z, 0);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo3D(wblut.geom.WB_Coordinate,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(v.xd(), v.yd(), 0);
			break;
		case YZ:
			result.set(0, v.xd(), v.yd());
			break;
		case XZ:
			result.set(v.yd(), 0, v.xd());
			break;
		case XYrev:
			result.set(v.yd(), v.xd(), 0);
			break;
		case YZrev:
			result.set(0, v.yd(), v.xd());
			break;
		case XZrev:
			result.set(v.xd(), 0, v.yd());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo3D(double, double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(u, v, w);
			break;
		case YZ:
			result.set(w, u, v);
			break;
		case XZ:
			result.set(v, w, u);
			break;
		case XYrev:
			result.set(v, u, -w);
			break;
		case YZrev:
			result.set(-w, v, u);
			break;
		case XZrev:
			result.set(u, -w, v);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Context2D#vectorTo3D(double, double,
	 * wblut.geom.WB_MutableCoordinate)
	 */
	@Override
	public void unmapVector2D(final double u, final double v, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(u, v, 0);
			break;
		case YZ:
			result.set(0, u, v);
			break;
		case XZ:
			result.set(v, 0, u);
			break;
		case XYrev:
			result.set(v, u, 0);
			break;
		case YZrev:
			result.set(0, v, u);
			break;
		case XZrev:
			result.set(u, 0, v);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map2D#unmapVector2D(wblut.geom.WB_Coord,
	 * wblut.geom.WB_MutableCoord)
	 */
	@Override
	public void unmapVector2D(final WB_Coord v, final WB_MutableCoord result) {
		switch (mode) {
		case XY:
			result.set(v.xf(), v.yf(), 0);
			break;
		case YZ:
			result.set(0, v.xf(), v.yf());
			break;
		case XZ:
			result.set(v.yf(), 0, v.xf());
			break;
		case XYrev:
			result.set(v.yf(), v.xf(), 0);
			break;
		case YZrev:
			result.set(0, v.yf(), v.xf());
			break;
		case XZrev:
			result.set(v.xf(), 0, v.yf());
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Map#mapPoint3D(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		WB_Point result = new WB_Point();
		switch (mode) {
		case XY:
			result.set(p.xd(), p.yd(), 0);
			break;
		case YZ:
			result.set(p.yd(), p.zd(), 0);
			break;
		case XZ:
			result.set(p.zd(), p.xd(), 0);
			break;
		case XYrev:
			result.set(p.yd(), p.xd(), 0);
			break;
		case YZrev:
			result.set(p.zd(), p.yd(), 0);
			break;
		case XZrev:
			result.set(p.xd(), p.zd(), 0);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(x, y, 0);
			break;
		case YZ:
			result.set(y, z, 0);
			break;
		case XZ:
			result.set(z, x, 0);
			break;
		case XYrev:
			result.set(y, x, 0);
			break;
		case YZrev:
			result.set(z, y, 0);
			break;
		case XZrev:
			result.set(x, z, 0);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(p.xd(), p.yd(), 0);
			break;
		case YZ:
			result.set(0, p.xd(), p.yd());
			break;
		case XZ:
			result.set(p.yd(), 0, p.xd());
			break;
		case XYrev:
			result.set(p.yd(), p.xd(), 0);
			break;
		case YZrev:
			result.set(0, p.yd(), p.xd());
			break;
		case XZrev:
			result.set(p.xd(), 0, p.yd());
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(u, v, w);
			break;
		case YZ:
			result.set(w, u, v);
			break;
		case XZ:
			result.set(v, w, u);
			break;
		case XYrev:
			result.set(v, u, -w);
			break;
		case YZrev:
			result.set(-w, v, u);
			break;
		case XZrev:
			result.set(u, -w, v);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(u, v, 0);
			break;
		case YZ:
			result.set(0, u, v);
			break;
		case XZ:
			result.set(v, 0, u);
			break;
		case XYrev:
			result.set(v, u, 0);
			break;
		case YZrev:
			result.set(0, v, u);
			break;
		case XZrev:
			result.set(u, 0, v);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(p.xf(), p.yf(), 0);
			break;
		case YZ:
			result.set(0, p.xf(), p.yf());
			break;
		case XZ:
			result.set(p.yf(), 0, p.xf());
			break;
		case XYrev:
			result.set(p.yf(), p.xf(), 0);
			break;
		case YZrev:
			result.set(0, p.yf(), p.xf());
			break;
		case XZrev:
			result.set(p.xf(), 0, p.yf());
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(v.xd(), v.yd(), 0);
			break;
		case YZ:
			result.set(v.yd(), v.zd(), 0);
			break;
		case XZ:
			result.set(v.zd(), v.xd(), 0);
			break;
		case XYrev:
			result.set(v.yd(), v.xd(), 0);
			break;
		case YZrev:
			result.set(v.zd(), v.yd(), 0);
			break;
		case XZrev:
			result.set(v.xd(), v.zd(), 0);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(x, y, 0);
			break;
		case YZ:
			result.set(y, z, 0);
			break;
		case XZ:
			result.set(z, x, 0);
			break;
		case XYrev:
			result.set(y, x, 0);
			break;
		case YZrev:
			result.set(z, y, 0);
			break;
		case XZrev:
			result.set(x, z, 0);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(v.xd(), v.yd(), 0);
			break;
		case YZ:
			result.set(0, v.xd(), v.yd());
			break;
		case XZ:
			result.set(v.yd(), 0, v.xd());
			break;
		case XYrev:
			result.set(v.yd(), v.xd(), 0);
			break;
		case YZrev:
			result.set(0, v.yd(), v.xd());
			break;
		case XZrev:
			result.set(v.xd(), 0, v.yd());
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(u, v, w);
			break;
		case YZ:
			result.set(w, u, v);
			break;
		case XZ:
			result.set(v, w, u);
			break;
		case XYrev:
			result.set(v, u, -w);
			break;
		case YZrev:
			result.set(-w, v, u);
			break;
		case XZrev:
			result.set(u, -w, v);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(u, v, 0);
			break;
		case YZ:
			result.set(0, u, v);
			break;
		case XZ:
			result.set(v, 0, u);
			break;
		case XYrev:
			result.set(v, u, 0);
			break;
		case YZrev:
			result.set(0, v, u);
			break;
		case XZrev:
			result.set(u, 0, v);
			break;
		}
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
		switch (mode) {
		case XY:
			result.set(v.xf(), v.yf(), 0);
			break;
		case YZ:
			result.set(0, v.xf(), v.yf());
			break;
		case XZ:
			result.set(v.yf(), 0, v.xf());
			break;
		case XYrev:
			result.set(v.yf(), v.xf(), 0);
			break;
		case YZrev:
			result.set(0, v.yf(), v.xf());
			break;
		case XZrev:
			result.set(v.xf(), 0, v.yf());
			break;
		}
		return result;
	}
}
