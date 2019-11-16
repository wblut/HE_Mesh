/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_CoordinateSystem {

	private WB_CoordinateSystem _parent;

	/**
	 *
	 *
	 * @return
	 */
	protected final static WB_CoordinateSystem WORLD() {
		return new WB_CoordinateSystem(true);
	}

	private WB_Point _origin;

	private WB_Vector _X;

	private WB_Vector _Y;

	private WB_Vector _Z;

	private boolean _isWorld;

	/**
	 *
	 *
	 * @param origin
	 * @param x
	 * @param y
	 * @param z
	 * @param parent
	 */
	public WB_CoordinateSystem(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z,
			final WB_CoordinateSystem parent) {
		_origin = new WB_Point(origin);
		_X = new WB_Vector(x);
		_Y = new WB_Vector(y);
		_Z = new WB_Vector(z);
		_parent = parent;
		_isWorld = _parent == null;
	}

	public WB_CoordinateSystem(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z) {
		_origin = new WB_Point(origin);
		_X = new WB_Vector(x);
		_Y = new WB_Vector(y);
		_Z = new WB_Vector(z);
		_parent = WORLD();
		_isWorld = _parent == null;
	}

	public WB_CoordinateSystem(final WB_Plane P) {
		this(P.getOrigin(), P.getU(), P.getV(), P.getW());

	}

	/**
	 *
	 *
	 * @param world
	 */
	protected WB_CoordinateSystem(final boolean world) {
		_origin = new WB_Point(WB_Point.ZERO());
		_X = new WB_Vector(WB_Vector.X());
		_Y = new WB_Vector(WB_Vector.Y());
		_Z = new WB_Vector(WB_Vector.Z());
		_isWorld = world;
		_parent = world ? null : WORLD();
	}

	/**
	 *
	 */
	public WB_CoordinateSystem() {
		this(false);
	}

	/**
	 *
	 *
	 * @param parent
	 */
	public WB_CoordinateSystem(final WB_CoordinateSystem parent) {
		_origin = new WB_Point(WB_Point.ZERO());
		_X = new WB_Vector(WB_Vector.X());
		_Y = new WB_Vector(WB_Vector.Y());
		_Z = new WB_Vector(WB_Vector.Z());
		_parent = parent;
		_isWorld = _parent == null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_CoordinateSystem get() {
		return new WB_CoordinateSystem(_origin, _X, _Y, _Z, _parent);
	}

	/**
	 *
	 *
	 * @param origin
	 * @param x
	 * @param y
	 * @param z
	 */
	protected void set(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z) {
		_origin = new WB_Point(origin);
		_X = new WB_Vector(x);
		_Y = new WB_Vector(y);
		_Z = new WB_Vector(z);
	}

	/**
	 *
	 *
	 * @param origin
	 * @param x
	 * @param y
	 * @param z
	 * @param CS
	 */
	protected void set(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z,
			final WB_CoordinateSystem CS) {
		_origin = new WB_Point(origin);
		_X = new WB_Vector(x);
		_Y = new WB_Vector(y);
		_Z = new WB_Vector(z);
		_parent = CS;
	}

	/**
	 *
	 *
	 * @param parent
	 * @return
	 */
	public WB_CoordinateSystem setParent(final WB_CoordinateSystem parent) {
		_parent = parent;
		_isWorld = _parent == null;
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public WB_CoordinateSystem setOrigin(final WB_Point o) {
		_origin.set(o);
		return this;
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @return
	 */
	public WB_CoordinateSystem setOrigin(final double ox, final double oy, final double oz) {
		_origin.set(ox, oy, oz);
		return this;
	}

	/**
	 *
	 *
	 * @param X
	 * @param Y
	 * @return
	 */
	public WB_CoordinateSystem setXY(final WB_Coord X, final WB_Coord Y) {
		_X.set(X);
		_X.normalizeSelf();
		_Y.set(Y);
		_Y.normalizeSelf();
		_Z.set(_X.cross(_Y));
		if (WB_Epsilon.isZeroSq(_Z.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalizeSelf();
		_Y.set(_Z.cross(_X));
		_Y.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param Y
	 * @param X
	 * @return
	 */
	public WB_CoordinateSystem setYX(final WB_Coord Y, final WB_Coord X) {
		_X.set(X);
		_X.normalizeSelf();
		_Y.set(Y);
		_Y.normalizeSelf();
		_Z.set(_X.cross(_Y));
		if (WB_Epsilon.isZeroSq(_Z.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalizeSelf();
		_X.set(_Y.cross(_Z));
		_X.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param X
	 * @param Z
	 * @return
	 */
	public WB_CoordinateSystem setXZ(final WB_Coord X, final WB_Coord Z) {
		_X.set(X);
		_X.normalizeSelf();
		_Z.set(Z);
		_Z.normalizeSelf();
		_Y.set(_Z.cross(_X));
		if (WB_Epsilon.isZeroSq(_Y.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalizeSelf();
		_Z.set(_X.cross(_Y));
		_Z.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param Z
	 * @param X
	 * @return
	 */
	public WB_CoordinateSystem setZX(final WB_Coord Z, final WB_Coord X) {
		_X.set(X);
		_X.normalizeSelf();
		_Z.set(Z);
		_Z.normalizeSelf();
		_Y.set(_Z.cross(_X));
		if (WB_Epsilon.isZeroSq(_Y.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalizeSelf();
		_X.set(_Y.cross(_Z));
		_X.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param Y
	 * @param Z
	 * @return
	 */
	public WB_CoordinateSystem setYZ(final WB_Coord Y, final WB_Coord Z) {
		_Y.set(Y);
		_Y.normalizeSelf();
		_Z.set(Z);
		_Z.normalizeSelf();
		_X.set(_Y.cross(_Z));
		if (WB_Epsilon.isZeroSq(_X.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalizeSelf();
		_Z.set(_X.cross(_Y));
		_Z.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param Z
	 * @param Y
	 * @return
	 */
	public WB_CoordinateSystem setZY(final WB_Coord Z, final WB_Coord Y) {
		_Y.set(Y);
		_Y.normalizeSelf();
		_Z.set(Z);
		_Z.normalizeSelf();
		_X.set(_Y.cross(_Z));
		if (WB_Epsilon.isZeroSq(_X.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalizeSelf();
		_Y.set(_Z.cross(_X));
		_Y.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector getX() {
		return _X.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector getY() {
		return _Y.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector getZ() {
		return _Z.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point getOrigin() {
		return _origin.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_CoordinateSystem getParent() {
		return _parent;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isWorld() {
		return _isWorld;
	}

	/**
	 *
	 *
	 * @param xx
	 * @param xy
	 * @param xz
	 * @param yx
	 * @param yy
	 * @param yz
	 * @return
	 */
	public WB_CoordinateSystem setXY(final double xx, final double xy, final double xz, final double yx,
			final double yy, final double yz) {
		_X.set(xx, xy, xz);
		_X.normalizeSelf();
		_Y.set(yx, yy, yz);
		_Y.normalizeSelf();
		_Z.set(_X.cross(_Y));
		if (WB_Epsilon.isZeroSq(_Z.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalizeSelf();
		_Y.set(_Z.cross(_X));
		_Y.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param yx
	 * @param yy
	 * @param yz
	 * @param xx
	 * @param xy
	 * @param xz
	 * @return
	 */
	public WB_CoordinateSystem setYX(final double yx, final double yy, final double yz, final double xx,
			final double xy, final double xz) {
		_X.set(xx, xy, xz);
		_X.normalizeSelf();
		_Y.set(yx, yy, yz);
		_Y.normalizeSelf();
		_Z.set(_X.cross(_Y));
		if (WB_Epsilon.isZeroSq(_Z.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalizeSelf();
		_X.set(_Y.cross(_Z));
		_X.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param xx
	 * @param xy
	 * @param xz
	 * @param zx
	 * @param zy
	 * @param zz
	 * @return
	 */
	public WB_CoordinateSystem setXZ(final double xx, final double xy, final double xz, final double zx,
			final double zy, final double zz) {
		_X.set(xx, xy, xz);
		_X.normalizeSelf();
		_Z.set(zx, zy, zz);
		_Z.normalizeSelf();
		_Y.set(_Z.cross(_X));
		if (WB_Epsilon.isZeroSq(_Y.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalizeSelf();
		_Z.set(_X.cross(_Y));
		_Z.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param zx
	 * @param zy
	 * @param zz
	 * @param xx
	 * @param xy
	 * @param xz
	 * @return
	 */
	public WB_CoordinateSystem setZX(final double zx, final double zy, final double zz, final double xx,
			final double xy, final double xz) {
		_X.set(xx, xy, xz);
		_X.normalizeSelf();
		_Z.set(zx, zy, zz);
		_Z.normalizeSelf();
		_Y.set(_Z.cross(_X));
		if (WB_Epsilon.isZeroSq(_Y.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalizeSelf();
		_X.set(_Y.cross(_Z));
		_X.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param yx
	 * @param yy
	 * @param yz
	 * @param zx
	 * @param zy
	 * @param zz
	 * @return
	 */
	public WB_CoordinateSystem setYZ(final double yx, final double yy, final double yz, final double zx,
			final double zy, final double zz) {
		_Y.set(yx, yy, yz);
		_Y.normalizeSelf();
		_Z.set(zx, zy, zz);
		_Z.normalizeSelf();
		_X.set(_Y.cross(_Z));
		if (WB_Epsilon.isZeroSq(_X.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalizeSelf();
		_Z.set(_X.cross(_Y));
		_Z.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param zx
	 * @param zy
	 * @param zz
	 * @param yx
	 * @param yy
	 * @param yz
	 * @return
	 */
	public WB_CoordinateSystem setZY(final double zx, final double zy, final double zz, final double yx,
			final double yy, final double yz) {
		_Y.set(yx, yy, yz);
		_Y.normalizeSelf();
		_Z.set(zx, zy, zz);
		_Z.normalizeSelf();
		_X.set(_Y.cross(_Z));
		if (WB_Epsilon.isZeroSq(_X.getSqLength())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalizeSelf();
		_Y.set(_Z.cross(_X));
		_Y.normalizeSelf();
		return this;
	}

	/**
	 *
	 *
	 * @param X
	 * @return
	 */
	public WB_CoordinateSystem setX(final WB_Coord X) {
		final WB_Vector lX = new WB_Vector(X);
		lX.normalizeSelf();
		final WB_Vector tmp = lX.cross(_X);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
			rotate(-Math.acos(WB_Math.clamp(_X.dot(lX), -1, 1)), tmp);
		} else if (_X.dot(lX) < -1 + WB_Epsilon.EPSILON) {
			flipX();
		}
		return this;
	}

	/**
	 *
	 *
	 * @param Y
	 * @return
	 */
	public WB_CoordinateSystem setY(final WB_Coord Y) {
		final WB_Vector lY = new WB_Vector(Y);
		lY.normalizeSelf();
		final WB_Vector tmp = lY.cross(_Y);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
			rotate(-Math.acos(WB_Math.clamp(_Y.dot(lY), -1, 1)), tmp);
		} else if (_Y.dot(lY) < -1 + WB_Epsilon.EPSILON) {
			flipY();
		}
		return this;
	}

	/**
	 *
	 *
	 * @param Z
	 * @return
	 */
	public WB_CoordinateSystem setZ(final WB_Coord Z) {
		final WB_Vector lZ = new WB_Vector(Z);
		lZ.normalizeSelf();
		final WB_Vector tmp = lZ.cross(_Z);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
			rotate(-Math.acos(WB_Math.clamp(_Z.dot(lZ), -1, 1)), tmp);
		} else if (_Z.dot(lZ) < -1 + WB_Epsilon.EPSILON) {
			flipZ();
		}
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public WB_CoordinateSystem rotateX(final double a) {
		_Y.rotateAboutAxisSelf(a, _origin, _X);
		_Z.rotateAboutAxisSelf(a, _origin, _X);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public WB_CoordinateSystem rotateY(final double a) {
		_X.rotateAboutAxisSelf(a, _origin, _Y);
		_Z.rotateAboutAxisSelf(a, _origin, _Y);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public WB_CoordinateSystem rotateZ(final double a) {
		_X.rotateAboutAxisSelf(a, _origin, _Z);
		_Y.rotateAboutAxisSelf(a, _origin, _Z);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @param v
	 * @return
	 */
	public WB_CoordinateSystem rotate(final double a, final WB_Vector v) {
		final WB_Vector lv = v.copy();
		lv.normalizeSelf();
		_X.rotateAboutAxisSelf(a, _origin, lv);
		_Y.rotateAboutAxisSelf(a, _origin, lv);
		_Z.rotateAboutAxisSelf(a, _origin, lv);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Transform3D getTransformFromParent() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromParentToCS(this);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Transform3D getTransformToParent() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToParent(this);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Transform3D getTransformFromWorld() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromWorldToCS(this);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Transform3D getTransformToWorld() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToWorld(this);
		return result;
	}

	/**
	 *
	 *
	 * @param CS
	 * @return
	 */
	public WB_Transform3D getTransformFrom(final WB_CoordinateSystem CS) {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToCS(CS, this);
		return result;
	}

	/**
	 *
	 *
	 * @param CS
	 * @return
	 */
	public WB_Transform3D getTransformTo(final WB_CoordinateSystem CS) {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToCS(this, CS);
		return result;
	}

	/**
	 *
	 *
	 * @param xx
	 * @param xy
	 * @param xz
	 * @return
	 */
	public WB_CoordinateSystem setX(final double xx, final double xy, final double xz) {
		final WB_Vector lX = new WB_Vector(xx, xy, xz);
		lX.normalizeSelf();
		final WB_Vector tmp = lX.cross(_X);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
			rotate(-Math.acos(WB_Math.clamp(_X.dot(lX), -1, 1)), tmp);
		} else if (_X.dot(lX) < -1 + WB_Epsilon.EPSILON) {
			flipX();
		}
		return this;
	}

	/**
	 *
	 *
	 * @param yx
	 * @param yy
	 * @param yz
	 * @return
	 */
	public WB_CoordinateSystem setY(final double yx, final double yy, final double yz) {
		final WB_Vector lY = new WB_Vector(yx, yy, yz);
		lY.normalizeSelf();
		final WB_Vector tmp = lY.cross(_Y);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
			rotate(-Math.acos(WB_Math.clamp(_Y.dot(lY), -1, 1)), tmp);
		} else if (_Y.dot(lY) < -1 + WB_Epsilon.EPSILON) {
			flipY();
		}
		return this;
	}

	/**
	 *
	 *
	 * @param zx
	 * @param zy
	 * @param zz
	 * @return
	 */
	public WB_CoordinateSystem setZ(final double zx, final double zy, final double zz) {
		final WB_Vector lZ = new WB_Vector(zx, zy, zz);
		lZ.normalizeSelf();
		final WB_Vector tmp = lZ.cross(_Z);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
			rotate(-Math.acos(WB_Math.clamp(_Z.dot(lZ), -1, 1)), tmp);
		} else if (_Z.dot(lZ) < -1 + WB_Epsilon.EPSILON) {
			flipZ();
		}
		return this;
	}

	/**
	 *
	 */
	public void flipX() {
		_X.mulSelf(-1);
		_Y.mulSelf(-1);
	}

	/**
	 *
	 */
	public void flipY() {
		_X.mulSelf(-1);
		_Y.mulSelf(-1);
	}

	/**
	 *
	 */
	public void flipZ() {
		_Z.mulSelf(-1);
		_Y.mulSelf(-1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WB_CoordinateSystem3d: origin: " + _origin + " [X=" + _X + ", Y=" + _Y + ", Z=" + _Z + "]";
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_CoordinateSystem apply(final WB_Transform3D T) {
		return new WB_CoordinateSystem(T.applyAsPoint(_origin), T.applyAsVector(_X), T.applyAsVector(_Y),
				T.applyAsVector(_Z), _parent == null ? WORLD() : _parent);
	}

	/**
	 *
	 *
	 * @param T
	 * @param parent
	 * @return
	 */
	WB_CoordinateSystem apply(final WB_Transform3D T, final WB_CoordinateSystem parent) {
		return new WB_CoordinateSystem(T.applyAsPoint(_origin), T.applyAsVector(_X), T.applyAsVector(_Y),
				T.applyAsVector(_Z), _parent);
	}
}
