package wblut.geom;

import lombok.ToString;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

@ToString(includeFieldNames = true)
public class WB_CoordinateSystem {
	private WB_CoordinateSystem parent;

	protected final static WB_CoordinateSystem WORLD() {
		return new WB_CoordinateSystem(true);
	}

	private WB_Point origin;
	private WB_Vector xAxis;
	private WB_Vector yAxis;
	private WB_Vector zAxis;
	private boolean isWorld;

	public WB_CoordinateSystem(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z,
			final WB_CoordinateSystem parent) {
		this.origin = new WB_Point(origin);
		xAxis = new WB_Vector(x);
		yAxis = new WB_Vector(y);
		zAxis = new WB_Vector(z);
		this.parent = parent;
		isWorld = parent == null;
	}

	public WB_CoordinateSystem(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z) {
		this.origin = new WB_Point(origin);
		xAxis = new WB_Vector(x);
		yAxis = new WB_Vector(y);
		zAxis = new WB_Vector(z);
		parent = WORLD();
		isWorld = parent == null;
	}

	public WB_CoordinateSystem(final WB_Plane P) {
		this(P.getOrigin(), P.getU(), P.getV(), P.getW());
	}

	protected WB_CoordinateSystem(final boolean world) {
		this.origin = new WB_Point(WB_Point.ZERO());
		xAxis = new WB_Vector(WB_Vector.X());
		yAxis = new WB_Vector(WB_Vector.Y());
		zAxis = new WB_Vector(WB_Vector.Z());
		isWorld = world;
		parent = world ? null : WORLD();
	}

	public WB_CoordinateSystem() {
		this(false);
	}

	public WB_CoordinateSystem(final WB_CoordinateSystem parent) {
		this.origin = new WB_Point(WB_Point.ZERO());
		xAxis = new WB_Vector(WB_Vector.X());
		yAxis = new WB_Vector(WB_Vector.Y());
		zAxis = new WB_Vector(WB_Vector.Z());
		this.parent = parent;
		isWorld = parent == null;
	}

	public WB_CoordinateSystem get() {
		return new WB_CoordinateSystem(origin, xAxis, yAxis, zAxis, parent);
	}

	protected void set(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z) {
		this.origin = new WB_Point(origin);
		xAxis = new WB_Vector(x);
		yAxis = new WB_Vector(y);
		zAxis = new WB_Vector(z);
	}

	protected void set(final WB_Coord origin, final WB_Coord x, final WB_Coord y, final WB_Coord z,
			final WB_CoordinateSystem CS) {
		this.origin = new WB_Point(origin);
		xAxis = new WB_Vector(x);
		yAxis = new WB_Vector(y);
		zAxis = new WB_Vector(z);
		parent = CS;
	}

	public WB_CoordinateSystem setParent(final WB_CoordinateSystem parent) {
		this.parent = parent;
		isWorld = parent == null;
		return this;
	}

	public WB_CoordinateSystem setOrigin(final WB_Point o) {
		origin.set(o);
		return this;
	}

	public WB_CoordinateSystem setOrigin(final double ox, final double oy, final double oz) {
		origin.set(ox, oy, oz);
		return this;
	}

	public WB_CoordinateSystem setXY(final WB_Coord X, final WB_Coord Y) {
		xAxis.set(X);
		xAxis.normalizeSelf();
		yAxis.set(Y);
		yAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		if (WB_Epsilon.isZeroSq(zAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		zAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		yAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setYX(final WB_Coord Y, final WB_Coord X) {
		xAxis.set(X);
		xAxis.normalizeSelf();
		yAxis.set(Y);
		yAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		if (WB_Epsilon.isZeroSq(zAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		zAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		xAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setXZ(final WB_Coord X, final WB_Coord Z) {
		xAxis.set(X);
		xAxis.normalizeSelf();
		zAxis.set(Z);
		zAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		if (WB_Epsilon.isZeroSq(yAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		yAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		zAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setZX(final WB_Coord Z, final WB_Coord X) {
		xAxis.set(X);
		xAxis.normalizeSelf();
		zAxis.set(Z);
		zAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		if (WB_Epsilon.isZeroSq(yAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		yAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		xAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setYZ(final WB_Coord Y, final WB_Coord Z) {
		yAxis.set(Y);
		yAxis.normalizeSelf();
		zAxis.set(Z);
		zAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		if (WB_Epsilon.isZeroSq(xAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		xAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		zAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setZY(final WB_Coord Z, final WB_Coord Y) {
		yAxis.set(Y);
		yAxis.normalizeSelf();
		zAxis.set(Z);
		zAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		if (WB_Epsilon.isZeroSq(xAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		xAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		yAxis.normalizeSelf();
		return this;
	}

	public WB_Vector getX() {
		return xAxis.copy();
	}

	public WB_Vector getY() {
		return yAxis.copy();
	}

	public WB_Vector getZ() {
		return zAxis.copy();
	}

	public WB_Point getOrigin() {
		return origin.copy();
	}

	public WB_CoordinateSystem getParent() {
		return parent;
	}

	public boolean isWorld() {
		return isWorld;
	}

	public WB_CoordinateSystem setXY(final double xx, final double xy, final double xz, final double yx,
			final double yy, final double yz) {
		xAxis.set(xx, xy, xz);
		xAxis.normalizeSelf();
		yAxis.set(yx, yy, yz);
		yAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		if (WB_Epsilon.isZeroSq(zAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		zAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		yAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setYX(final double yx, final double yy, final double yz, final double xx,
			final double xy, final double xz) {
		xAxis.set(xx, xy, xz);
		xAxis.normalizeSelf();
		yAxis.set(yx, yy, yz);
		yAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		if (WB_Epsilon.isZeroSq(zAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		zAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		xAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setXZ(final double xx, final double xy, final double xz, final double zx,
			final double zy, final double zz) {
		xAxis.set(xx, xy, xz);
		xAxis.normalizeSelf();
		zAxis.set(zx, zy, zz);
		zAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		if (WB_Epsilon.isZeroSq(yAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		yAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		zAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setZX(final double zx, final double zy, final double zz, final double xx,
			final double xy, final double xz) {
		xAxis.set(xx, xy, xz);
		xAxis.normalizeSelf();
		zAxis.set(zx, zy, zz);
		zAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		if (WB_Epsilon.isZeroSq(yAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		yAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		xAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setYZ(final double yx, final double yy, final double yz, final double zx,
			final double zy, final double zz) {
		yAxis.set(yx, yy, yz);
		yAxis.normalizeSelf();
		zAxis.set(zx, zy, zz);
		zAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		if (WB_Epsilon.isZeroSq(xAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		xAxis.normalizeSelf();
		zAxis.set(xAxis.cross(yAxis));
		zAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setZY(final double zx, final double zy, final double zz, final double yx,
			final double yy, final double yz) {
		yAxis.set(yx, yy, yz);
		yAxis.normalizeSelf();
		zAxis.set(zx, zy, zz);
		zAxis.normalizeSelf();
		xAxis.set(yAxis.cross(zAxis));
		if (WB_Epsilon.isZeroSq(xAxis.getSqLength3D())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		xAxis.normalizeSelf();
		yAxis.set(zAxis.cross(xAxis));
		yAxis.normalizeSelf();
		return this;
	}

	public WB_CoordinateSystem setX(final WB_Coord X) {
		final WB_Vector lX = new WB_Vector(X);
		lX.normalizeSelf();
		final WB_Vector tmp = lX.cross(xAxis);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
			rotate(-Math.acos(WB_Math.clamp(xAxis.dot(lX), -1, 1)), tmp);
		} else if (xAxis.dot(lX) < -1 + WB_Epsilon.EPSILON) {
			flipX();
		}
		return this;
	}

	public WB_CoordinateSystem setY(final WB_Coord Y) {
		final WB_Vector lY = new WB_Vector(Y);
		lY.normalizeSelf();
		final WB_Vector tmp = lY.cross(yAxis);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
			rotate(-Math.acos(WB_Math.clamp(yAxis.dot(lY), -1, 1)), tmp);
		} else if (yAxis.dot(lY) < -1 + WB_Epsilon.EPSILON) {
			flipY();
		}
		return this;
	}

	public WB_CoordinateSystem setZ(final WB_Coord Z) {
		final WB_Vector lZ = new WB_Vector(Z);
		lZ.normalizeSelf();
		final WB_Vector tmp = lZ.cross(zAxis);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
			rotate(-Math.acos(WB_Math.clamp(zAxis.dot(lZ), -1, 1)), tmp);
		} else if (zAxis.dot(lZ) < -1 + WB_Epsilon.EPSILON) {
			flipZ();
		}
		return this;
	}

	public WB_CoordinateSystem rotateX(final double a) {
		yAxis.rotateAboutAxisSelf(a, origin, xAxis);
		zAxis.rotateAboutAxisSelf(a, origin, xAxis);
		return this;
	}

	public WB_CoordinateSystem rotateY(final double a) {
		xAxis.rotateAboutAxisSelf(a, origin, yAxis);
		zAxis.rotateAboutAxisSelf(a, origin, yAxis);
		return this;
	}

	public WB_CoordinateSystem rotateZ(final double a) {
		xAxis.rotateAboutAxisSelf(a, origin, zAxis);
		yAxis.rotateAboutAxisSelf(a, origin, zAxis);
		return this;
	}

	public WB_CoordinateSystem rotate(final double a, final WB_Vector v) {
		final WB_Vector lv = v.copy();
		lv.normalizeSelf();
		xAxis.rotateAboutAxisSelf(a, origin, lv);
		yAxis.rotateAboutAxisSelf(a, origin, lv);
		zAxis.rotateAboutAxisSelf(a, origin, lv);
		return this;
	}

	public WB_Transform3D getTransformFromParent() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromParentToCS(this);
		return result;
	}

	public WB_Transform3D getTransformToParent() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToParent(this);
		return result;
	}

	public WB_Transform3D getTransformFromWorld() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromWorldToCS(this);
		return result;
	}

	public WB_Transform3D getTransformToWorld() {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToWorld(this);
		return result;
	}

	public WB_Transform3D getTransformFrom(final WB_CoordinateSystem CS) {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToCS(CS, this);
		return result;
	}

	public WB_Transform3D getTransformTo(final WB_CoordinateSystem CS) {
		final WB_Transform3D result = new WB_Transform3D();
		result.addFromCSToCS(this, CS);
		return result;
	}

	public WB_CoordinateSystem setX(final double xx, final double xy, final double xz) {
		final WB_Vector lX = new WB_Vector(xx, xy, xz);
		lX.normalizeSelf();
		final WB_Vector tmp = lX.cross(xAxis);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
			rotate(-Math.acos(WB_Math.clamp(xAxis.dot(lX), -1, 1)), tmp);
		} else if (xAxis.dot(lX) < -1 + WB_Epsilon.EPSILON) {
			flipX();
		}
		return this;
	}

	public WB_CoordinateSystem setY(final double yx, final double yy, final double yz) {
		final WB_Vector lY = new WB_Vector(yx, yy, yz);
		lY.normalizeSelf();
		final WB_Vector tmp = lY.cross(yAxis);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
			rotate(-Math.acos(WB_Math.clamp(yAxis.dot(lY), -1, 1)), tmp);
		} else if (yAxis.dot(lY) < -1 + WB_Epsilon.EPSILON) {
			flipY();
		}
		return this;
	}

	public WB_CoordinateSystem setZ(final double zx, final double zy, final double zz) {
		final WB_Vector lZ = new WB_Vector(zx, zy, zz);
		lZ.normalizeSelf();
		final WB_Vector tmp = lZ.cross(zAxis);
		if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
			rotate(-Math.acos(WB_Math.clamp(zAxis.dot(lZ), -1, 1)), tmp);
		} else if (zAxis.dot(lZ) < -1 + WB_Epsilon.EPSILON) {
			flipZ();
		}
		return this;
	}

	public void flipX() {
		xAxis.mulSelf(-1);
		yAxis.mulSelf(-1);
	}

	public void flipY() {
		xAxis.mulSelf(-1);
		yAxis.mulSelf(-1);
	}

	public void flipZ() {
		zAxis.mulSelf(-1);
		yAxis.mulSelf(-1);
	}

	WB_CoordinateSystem apply(final WB_Transform3D T) {
		return new WB_CoordinateSystem(T.applyAsPoint(origin), T.applyAsVector(xAxis), T.applyAsVector(yAxis),
				T.applyAsVector(zAxis), parent == null ? WORLD() : parent);
	}

	WB_CoordinateSystem apply(final WB_Transform3D T, final WB_CoordinateSystem parent) {
		return new WB_CoordinateSystem(T.applyAsPoint(origin), T.applyAsVector(xAxis), T.applyAsVector(yAxis),
				T.applyAsVector(zAxis), parent);
	}
}
