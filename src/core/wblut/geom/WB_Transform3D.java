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
import wblut.math.WB_M33;
import wblut.math.WB_M44;

/**
 *
 */
public class WB_Transform3D {
	/**
	 *
	 */
	private double _xt, _yt, _zt;
	/**
	 *
	 */
	private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	/** Transform matrix. */
	private WB_M44 T;
	/** Inverse transform matrix. */
	private WB_M44 invT;

	/**
	 * Instantiates a new WB_Transfrom.
	 */
	public WB_Transform3D() {
		T = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		invT = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	}

	/**
	 *
	 *
	 * @param Trans
	 */
	public WB_Transform3D(final WB_Transform3D Trans) {
		T = Trans.T.get();
		invT = Trans.invT.get();
	}

	/**
	 *
	 *
	 * @param sourceOrigin
	 * @param sourceDirection
	 * @param targetOrigin
	 * @param targetDirection
	 */
	public WB_Transform3D(final WB_Coord sourceOrigin, final WB_Coord sourceDirection, final WB_Coord targetOrigin,
			final WB_Coord targetDirection) {
		T = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		invT = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		addTranslate(-1, sourceOrigin);
		final WB_Vector v1 = geometryfactory.createNormalizedVector(sourceDirection);
		final WB_Vector v2 = geometryfactory.createNormalizedVector(targetDirection);
		WB_Vector axis = v1.cross(v2);
		final double l = axis.getLength();
		if (WB_Epsilon.isZero(l)) {
			if (v1.dot(v2) < 0.0) {
				axis = geometryfactory.createNormalizedPerpendicularVector(sourceDirection);
				addRotateAboutOrigin(Math.PI, axis);
			}
		} else {
			final double angle = Math.atan2(l, v1.dot(v2));
			axis.normalizeSelf();
			addRotateAboutOrigin(angle, axis);
		}
		addTranslate(targetOrigin);
	}

	/**
	 *
	 *
	 * @param sourceDirection
	 * @param targetDirection
	 */
	public WB_Transform3D(final WB_Coord sourceDirection, final WB_Coord targetDirection) {
		T = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		invT = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		final WB_Vector v1 = geometryfactory.createNormalizedVector(sourceDirection);
		final WB_Vector v2 = geometryfactory.createNormalizedVector(targetDirection);
		WB_Vector axis = v1.cross(v2);
		final double l = axis.getLength();
		if (WB_Epsilon.isZero(l)) {
			if (v1.dot(v2) < 0.0) {
				axis = geometryfactory.createNormalizedPerpendicularVector(sourceDirection);
				addRotateAboutOrigin(Math.PI, axis);
			}
		} else {
			final double angle = Math.atan2(l, v1.dot(v2));
			axis.normalizeSelf();
			addRotateAboutOrigin(angle, axis);
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Transform3D get() {
		return new WB_Transform3D(this);
	}
	
	public WB_Transform3D addTransform(final WB_Transform3D transform) {
		T = transform.T.mult(T);
		invT = invT.mult(transform.T);
		return this;
	}

	/**
	 * Add translation to transform.
	 *
	 * @param v
	 *            vector
	 * @return self
	 */
	public WB_Transform3D addTranslate(final WB_Coord v) {
		T = new WB_M44(1, 0, 0, v.xd(), 0, 1, 0, v.yd(), 0, 0, 1, v.zd(), 0, 0, 0, 1).mult(T);
		invT = invT.mult(new WB_M44(1, 0, 0, -v.xd(), 0, 1, 0, -v.yd(), 0, 0, 1, -v.zd(), 0, 0, 0, 1));
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @param v
	 * @return
	 */
	public WB_Transform3D addTranslate(final double f, final WB_Coord v) {
		T = new WB_M44(1, 0, 0, f * v.xd(), 0, 1, 0, f * v.yd(), 0, 0, 1, f * v.zd(), 0, 0, 0, 1).mult(T);
		invT = invT.mult(new WB_M44(1, 0, 0, -f * v.xd(), 0, 1, 0, -f * v.yd(), 0, 0, 1, -f * v.zd(), 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add non-uniform scale to transform.
	 *
	 * @param s
	 *            scaling vector
	 * @return self
	 */
	public WB_Transform3D addScale(final WB_Coord s) {
		T = new WB_M44(s.xd(), 0, 0, 0, 0, s.yd(), 0, 0, 0, 0, s.zd(), 0, 0, 0, 0, 1).mult(T);
		invT = invT.mult(new WB_M44(1.0 / s.xd(), 0, 0, 0, 0, 1.0 / s.yd(), 0, 0, 0, 0, 1.0 / s.zd(), 0, 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add non-uniform scale to transform.
	 *
	 * @param sx
	 *            scaling vector
	 * @param sy
	 *            scaling vector
	 * @param sz
	 *            scaling vector
	 * @return self
	 */
	public WB_Transform3D addScale(final double sx, final double sy, final double sz) {
		T = new WB_M44(sx, 0, 0, 0, 0, sy, 0, 0, 0, 0, sz, 0, 0, 0, 0, 1).mult(T);
		invT = invT.mult(new WB_M44(1.0 / sx, 0, 0, 0, 0, 1.0 / sy, 0, 0, 0, 0, 1.0 / sz, 0, 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add uniform scale to transform.
	 *
	 * @param s
	 *            scaling point
	 * @return self
	 */
	public WB_Transform3D addScale(final double s) {
		T = new WB_M44(s, 0, 0, 0, 0, s, 0, 0, 0, 0, s, 0, 0, 0, 0, 1).mult(T);
		invT = invT.mult(new WB_M44(1 / s, 0, 0, 0, 0, 1 / s, 0, 0, 0, 0, 1 / s, 0, 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add rotation about X-axis.
	 *
	 * @param angle
	 *            angle in radians
	 * @return self
	 */
	public WB_Transform3D addRotateX(final double angle) {
		final double s = Math.sin(angle);
		final double c = Math.cos(angle);
		final WB_M44 tmp = new WB_M44(1, 0, 0, 0, 0, c, -s, 0, 0, s, c, 0, 0, 0, 0, 1);
		T = tmp.mult(T);
		invT = invT.mult(tmp.getTranspose());
		return this;
	}

	/**
	 * Add rotation about Y-axis.
	 *
	 * @param angle
	 *            angle in radians
	 * @return self
	 */
	public WB_Transform3D addRotateY(final double angle) {
		final double s = Math.sin(angle);
		final double c = Math.cos(angle);
		final WB_M44 tmp = new WB_M44(c, 0, s, 0, 0, 1, 0, 0, -s, 0, c, 0, 0, 0, 0, 1);
		T = tmp.mult(T);
		invT = invT.mult(tmp.getTranspose());
		return this;
	}

	/**
	 * Add rotation about Z-axis.
	 *
	 * @param angle
	 *            angle in radians
	 * @return self
	 */
	public WB_Transform3D addRotateZ(final double angle) {
		final double s = Math.sin(angle);
		final double c = Math.cos(angle);
		final WB_M44 tmp = new WB_M44(c, -s, 0, 0, s, c, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		T = tmp.mult(T);
		invT = invT.mult(tmp.getTranspose());
		return this;
	}

	/**
	 * Add rotation about arbitrary axis in origin.
	 *
	 * @param angle
	 *            angle in radians
	 * @param axis
	 *            WB_Vector
	 * @return self
	 */
	public WB_Transform3D addRotateAboutOrigin(final double angle, final WB_Coord axis) {
		final WB_Vector a = new WB_Vector(axis);
		a.normalizeSelf();
		final double s = Math.sin(angle);
		final double c = Math.cos(angle);
		final WB_M44 tmp = new WB_M44(a.xd() * a.xd() + (1.f - a.xd() * a.xd()) * c,
				a.xd() * a.yd() * (1.f - c) - a.zd() * s, a.xd() * a.zd() * (1.f - c) + a.yd() * s, 0,
				a.xd() * a.yd() * (1.f - c) + a.zd() * s, a.yd() * a.yd() + (1.f - a.yd() * a.yd()) * c,
				a.yd() * a.zd() * (1.f - c) - a.xd() * s, 0, a.xd() * a.zd() * (1.f - c) - a.yd() * s,
				a.yd() * a.zd() * (1.f - c) + a.xd() * s, a.zd() * a.zd() + (1.f - a.zd() * a.zd()) * c, 0, 0, 0, 0, 1);
		T = tmp.mult(T);
		invT = invT.mult(tmp.getTranspose());
		return this;
	}

	/**
	 * Add rotation about arbitrary axis defined by point and direction.
	 *
	 * @param angle
	 *            angle in radians
	 * @param p
	 *            point
	 * @param axis
	 *            direction
	 * @return self
	 */
	public WB_Transform3D addRotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord axis) {
		addTranslate(-1, p);
		addRotateAboutOrigin(angle, axis);
		addTranslate(p);
		return this;
	}

	/**
	 * Add rotation about arbitrary axis defiend by two points .
	 *
	 * @param angle
	 *            angle in radians
	 * @param p
	 *            first point
	 * @param q
	 *            second point
	 * @return self
	 */
	public WB_Transform3D addRotateAboutAxis2P(final double angle, final WB_Coord p, final WB_Coord q) {
		addTranslate(-1, p);
		addRotateAboutOrigin(angle, WB_Vector.sub(q, p));
		addTranslate(p);
		return this;
	}

	/**
	 * Add a object-to-world transform.
	 *
	 * @param origin
	 *            object origin in world coordinates
	 * @param up
	 *            object up direction in world coordinates
	 * @param front
	 *            object front direction in world coordinates
	 * @return self
	 */
	public WB_Transform3D addObjectToWorld(final WB_Coord origin, final WB_Coord up, final WB_Coord front) {
		final WB_Vector dir = new WB_Vector(origin, front);
		dir.normalizeSelf();
		final WB_Vector tup = new WB_Vector(origin, up);
		tup.normalizeSelf();
		final WB_Vector right = dir.cross(tup);
		final WB_Vector newUp = right.cross(dir);
		final WB_M44 tmp = new WB_M44(right.xd(), dir.xd(), newUp.xd(), origin.xd(), right.yd(), dir.yd(), newUp.yd(),
				origin.yd(), right.zd(), dir.zd(), newUp.zd(), origin.zd(), 0, 0, 0, 1);
		T = tmp.mult(T);
		invT = invT.mult(tmp.inverse());
		return this;
	}

	/**
	 * Adds the reflect x.
	 *
	 * @return
	 */
	public WB_Transform3D addReflectX() {
		addScale(-1, 1, 1);
		return this;
	}

	/**
	 * Adds the reflect y.
	 *
	 * @return
	 */
	public WB_Transform3D addReflectY() {
		addScale(1, -1, 1);
		return this;
	}

	/**
	 * Adds the reflect z.
	 *
	 * @return
	 */
	public WB_Transform3D addReflectZ() {
		addScale(1, 1, -1);
		return this;
	}

	/**
	 * Adds the invert.
	 *
	 * @return
	 */
	public WB_Transform3D addInvert() {
		addScale(-1, -1, -1);
		return this;
	}

	/**
	 * Adds the reflect x.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform3D addReflectX(final WB_Coord p) {
		addTranslate(-1, p);
		addScale(-1, 1, 1);
		addTranslate(p);
		return this;
	}

	/**
	 * Adds the reflect y.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform3D addReflectY(final WB_Coord p) {
		addTranslate(-1, p);
		addScale(1, -1, 1);
		addTranslate(p);
		return this;
	}

	/**
	 * Adds the reflect z.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform3D addReflectZ(final WB_Coord p) {
		addTranslate(-1, p);
		addScale(1, 1, -1);
		addTranslate(p);
		return this;
	}

	/**
	 * Adds the invert.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform3D addInvert(final WB_Coord p) {
		addTranslate(-1, p);
		addScale(-1, -1, -1);
		addTranslate(p);
		return this;
	}

	/**
	 * Adds the reflect.
	 *
	 * @param P
	 *            the p
	 * @return
	 */
	public WB_Transform3D addReflect(final WB_Plane P) {
		final WB_M33 tmp = P.getNormal().tensor(P.getNormal());
		final double Qn = P.getOrigin().dot(P.getNormal());
		final WB_M44 Tr = new WB_M44(1 - 2 * tmp.m11, -2 * tmp.m12, -2 * tmp.m13, 0, -2 * tmp.m21, 1 - 2 * tmp.m22,
				-2 * tmp.m23, 0, -2 * tmp.m31, -2 * tmp.m32, 1 - 2 * tmp.m33, 0, 2 * Qn * P.getNormal().xd(),
				2 * Qn * P.getNormal().yd(), 2 * Qn * P.getNormal().zd(), 1);
		T = Tr.mult(T);
		invT = invT.mult(Tr);
		return this;
	}

	/**
	 * Adds the shear.
	 *
	 * @param P
	 *            the p
	 * @param v
	 *            the v
	 * @param angle
	 *            the angle
	 * @return
	 */
	public WB_Transform3D addShear(final WB_Plane P, final WB_Coord v, final double angle) {
		final WB_Vector lv = new WB_Vector(v);
		lv.normalizeSelf();
		double tana = Math.tan(angle);
		final WB_M33 tmp = P.getNormal().tensor(lv);
		final double Qn = P.getOrigin().dot(P.getNormal());
		WB_M44 Tr = new WB_M44(1 + tana * tmp.m11, tana * tmp.m12, tana * tmp.m13, 0, tana * tmp.m21,
				1 + tana * tmp.m22, tana * tmp.m23, 0, tana * tmp.m31, tana * tmp.m32, 1 + tana * tmp.m33, 0,
				-Qn * lv.xd(), -Qn * lv.yd(), -Qn * lv.zd(), 1);
		T = Tr.mult(T);
		tana *= -1;
		Tr = new WB_M44(1 + tana * tmp.m11, tana * tmp.m12, tana * tmp.m13, 0, tana * tmp.m21, 1 + tana * tmp.m22,
				tana * tmp.m23, 0, tana * tmp.m31, tana * tmp.m32, 1 + tana * tmp.m33, 0, -Qn * lv.xd(), -Qn * lv.yd(),
				-Qn * lv.zd(), 1);
		invT = invT.mult(Tr);
		return this;
	}

	/**
	 *
	 * @param CS1
	 *
	 * @param CS2
	 *
	 * @return
	 */
	public WB_Transform3D addFromCSToCS(final WB_CoordinateSystem CS1, final WB_CoordinateSystem CS2) {
		addFromCSToWorld(CS1);
		addFromWorldToCS(CS2);
		return this;
	}

	/**
	 *
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform3D addFromCSToWorld(final WB_CoordinateSystem CS) {
		WB_CoordinateSystem current = CS;
		while (!current.isWorld()) {
			addFromCSToParent(current);
			current = current.getParent();
		}
		return this;
	}

	/**
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform3D addFromWorldToCS(final WB_CoordinateSystem CS) {
		final WB_Transform3D tmp = new WB_Transform3D();
		tmp.addFromCSToWorld(CS);
		T = tmp.invT.mult(T);
		invT = invT.mult(tmp.T);
		return this;
	}

	/**
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform3D addFromCSToParent(final WB_CoordinateSystem CS) {
		final WB_CoordinateSystem WCS = WB_CoordinateSystem.WORLD();
		if (CS.isWorld()) {
			return this;
		}
		final WB_Vector ex1 = CS.getX(), ey1 = CS.getY(), ez1 = CS.getZ();
		final WB_Point o1 = CS.getOrigin();
		final WB_Vector ex2 = WCS.getX(), ey2 = WCS.getY(), ez2 = WCS.getZ();
		final WB_Point o2 = WCS.getOrigin();
		final double xx = ex2.dot(ex1);
		final double xy = ex2.dot(ey1);
		final double xz = ex2.dot(ez1);
		final double yx = ey2.dot(ex1);
		final double yy = ey2.dot(ey1);
		final double yz = ey2.dot(ez1);
		final double zx = ez2.dot(ex1);
		final double zy = ez2.dot(ey1);
		final double zz = ez2.dot(ez1);
		final WB_M44 tmp = new WB_M44(xx, xy, xz, 0, yx, yy, yz, 0, zx, zy, zz, 0, 0, 0, 0, 1);
		final WB_M44 invtmp = new WB_M44(xx, yx, zx, 0, xy, yy, zy, 0, xz, yz, zz, 0, 0, 0, 0, 1);
		T = tmp.mult(T);
		invT = invT.mult(invtmp);
		addTranslate(o1.subSelf(o2));
		return this;
	}

	/**
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform3D addFromParentToCS(final WB_CoordinateSystem CS) {
		if (CS.isWorld()) {
			return this;
		}
		final WB_Transform3D tmp = new WB_Transform3D();
		tmp.addFromCSToParent(CS);
		T = tmp.invT.mult(T);
		invT = invT.mult(tmp.T);
		return this;
	}

	/**
	 * Invert transform.
	 */
	public void inverse() {
		WB_M44 tmp;
		tmp = T;
		T = invT;
		invT = tmp;
	}

	/**
	 * Clear transform.
	 */
	public void clear() {
		T = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		invT = new WB_M44(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	}

	/**
	 * Apply transform to point.
	 *
	 * @param p
	 *            point
	 * @return new WB_XYZ
	 */
	public WB_Point applyAsPoint(final WB_Coord p) {
		final double xp = T.m11 * p.xd() + T.m12 * p.yd() + T.m13 * p.zd() + T.m14;
		final double yp = T.m21 * p.xd() + T.m22 * p.yd() + T.m23 * p.zd() + T.m24;
		final double zp = T.m31 * p.xd() + T.m32 * p.yd() + T.m33 * p.zd() + T.m34;
		double wp = T.m41 * p.xd() + T.m42 * p.yd() + T.m43 * p.zd() + T.m44;
		if (WB_Epsilon.isZero(wp)) {
			return new WB_Point(xp, yp, zp);
		}
		wp = 1.0 / wp;
		return new WB_Point(xp * wp, yp * wp, zp * wp);
	}

	/**
	 * Apply transform to point.
	 *
	 * @param p
	 *            point
	 */
	public void applyAsPointSelf(final WB_MutableCoord p) {
		final double x = T.m11 * p.xd() + T.m12 * p.yd() + T.m13 * p.zd() + T.m14;
		final double y = T.m21 * p.xd() + T.m22 * p.yd() + T.m23 * p.zd() + T.m24;
		final double z = T.m31 * p.xd() + T.m32 * p.yd() + T.m33 * p.zd() + T.m34;
		double wp = T.m41 * p.xd() + T.m42 * p.yd() + T.m43 * p.zd() + T.m44;
		wp = 1.0 / wp;
		p.set(x * wp, y * wp, z * wp);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyAsPointInto(final WB_Coord p, final WB_MutableCoord result) {
		_xt = T.m11 * p.xd() + T.m12 * p.yd() + T.m13 * p.zd() + T.m14;
		_yt = T.m21 * p.xd() + T.m22 * p.yd() + T.m23 * p.zd() + T.m24;
		_zt = T.m31 * p.xd() + T.m32 * p.yd() + T.m33 * p.zd() + T.m34;
		double wp = T.m41 * p.xd() + T.m42 * p.yd() + T.m43 * p.zd() + T.m44;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 * Apply as point.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return
	 */
	public WB_Point applyAsPoint(final double x, final double y, final double z) {
		final double xp = T.m11 * x + T.m12 * y + T.m13 * z + T.m14;
		final double yp = T.m21 * x + T.m22 * y + T.m23 * z + T.m24;
		final double zp = T.m31 * x + T.m32 * y + T.m33 * z + T.m34;
		double wp = T.m41 * x + T.m42 * y + T.m43 * z + T.m44;
		if (WB_Epsilon.isZero(wp)) {
			return new WB_Point(xp, yp, zp);
		}
		wp = 1.0 / wp;
		return new WB_Point(xp * wp, yp * wp, zp * wp);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	public void applyAsPointInto(final double x, final double y, final double z, final WB_MutableCoord result) {
		_xt = T.m11 * x + T.m12 * y + T.m13 * z + T.m14;
		_yt = T.m21 * x + T.m22 * y + T.m23 * z + T.m24;
		_zt = T.m31 * x + T.m32 * y + T.m33 * z + T.m34;
		double wp = T.m41 * x + T.m42 * y + T.m43 * z + T.m44;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public WB_Point applyInvAsPoint(final WB_Coord p) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13 * p.zd() + invT.m14;
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23 * p.zd() + invT.m24;
		_zt = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33 * p.zd() + invT.m34;
		double wp = invT.m41 * p.xd() + invT.m42 * p.yd() + invT.m43 * p.zd() + invT.m44;
		wp = 1.0 / wp;
		return new WB_Point(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 *
	 *
	 * @param p
	 */
	public void applyInvAsPointSelf(final WB_MutableCoord p) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13 * p.zd() + invT.m14;
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23 * p.zd() + invT.m24;
		_zt = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33 * p.zd() + invT.m34;
		double wp = invT.m41 * p.xd() + invT.m42 * p.yd() + invT.m43 * p.zd() + invT.m44;
		wp = 1.0 / wp;
		p.set(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyInvAsPointInto(final WB_Coord p, final WB_MutableCoord result) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13 * p.zd() + invT.m14;
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23 * p.zd() + invT.m24;
		_zt = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33 * p.zd() + invT.m34;
		double wp = invT.m41 * p.xd() + invT.m42 * p.yd() + invT.m43 * p.zd() + invT.m44;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Point applyInvAsPoint(final double x, final double y, final double z) {
		_xt = invT.m11 * x + invT.m12 * y + invT.m13 * z + invT.m14;
		_yt = invT.m21 * x + invT.m22 * y + invT.m23 * z + invT.m24;
		_zt = invT.m31 * x + invT.m32 * y + invT.m33 * z + invT.m34;
		double wp = invT.m41 * x + invT.m42 * y + invT.m43 * z + invT.m44;
		wp = 1.0 / wp;
		return new WB_Point(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	public void applyInvAsPointInto(final double x, final double y, final double z, final WB_MutableCoord result) {
		_xt = invT.m11 * x + invT.m12 * y + invT.m13 * z + invT.m14;
		_yt = invT.m21 * x + invT.m22 * y + invT.m23 * z + invT.m24;
		_zt = invT.m31 * x + invT.m32 * y + invT.m33 * z + invT.m34;
		double wp = invT.m41 * x + invT.m42 * y + invT.m43 * z + invT.m44;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp, _zt * wp);
	}

	/**
	 * Apply transform to vector.
	 *
	 * @param p
	 *            vector
	 * @return new WB_Vector
	 */
	public WB_Vector applyAsVector(final WB_Coord p) {
		final double xp = T.m11 * p.xd() + T.m12 * p.yd() + T.m13 * p.zd();
		final double yp = T.m21 * p.xd() + T.m22 * p.yd() + T.m23 * p.zd();
		final double zp = T.m31 * p.xd() + T.m32 * p.yd() + T.m33 * p.zd();
		return new WB_Vector(xp, yp, zp);
	}

	/**
	 * Apply transform to vector.
	 *
	 * @param p
	 *            vector
	 */
	public void applyAsVectorSelf(final WB_MutableCoord p) {
		final double x = T.m11 * p.xd() + T.m12 * p.yd() + T.m13 * p.zd();
		final double y = T.m21 * p.xd() + T.m22 * p.yd() + T.m23 * p.zd();
		final double z = T.m31 * p.xd() + T.m32 * p.yd() + T.m33 * p.zd();
		p.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyAsVectorInto(final WB_Coord p, final WB_MutableCoord result) {
		_xt = T.m11 * p.xd() + T.m12 * p.yd() + T.m13 * p.zd();
		_yt = T.m21 * p.xd() + T.m22 * p.yd() + T.m23 * p.zd();
		_zt = T.m31 * p.xd() + T.m32 * p.yd() + T.m33 * p.zd();
		result.set(_xt, _yt, _zt);
	}

	/**
	 * Apply as vector.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return
	 */
	public WB_Vector applyAsVector(final double x, final double y, final double z) {
		final double xp = T.m11 * x + T.m12 * y + T.m13 * z;
		final double yp = T.m21 * x + T.m22 * y + T.m23 * z;
		final double zp = T.m31 * x + T.m32 * y + T.m33 * z;
		return new WB_Vector(xp, yp, zp);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	public void applyAsVectorInto(final double x, final double y, final double z, final WB_MutableCoord result) {
		_xt = T.m11 * x + T.m12 * y + T.m13 * z;
		_yt = T.m21 * x + T.m22 * y + T.m23 * z;
		_zt = T.m31 * x + T.m32 * y + T.m33 * z;
		result.set(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param p
	 */
	public WB_Vector applyInvAsVector(final WB_Coord p) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13 * p.zd();
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23 * p.zd();
		_zt = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33 * p.zd();
		return new WB_Vector(_xt, _yt, _zt);
	}

	/**
	 *
	 * @param v
	 */
	public void applyInvAsVectorSelf(final WB_MutableCoord v) {
		_xt = invT.m11 * v.xd() + invT.m12 * v.yd() + invT.m13 * v.zd();
		_yt = invT.m21 * v.xd() + invT.m22 * v.yd() + invT.m23 * v.zd();
		_zt = invT.m31 * v.xd() + invT.m32 * v.yd() + invT.m33 * v.zd();
		v.set(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyInvAsVectorInto(final WB_Coord p, final WB_MutableCoord result) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13 * p.zd();
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23 * p.zd();
		_zt = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33 * p.zd();
		result.set(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public WB_Vector applyInvAsVector(final double x, final double y, final double z) {
		_xt = invT.m11 * x + invT.m12 * y + invT.m13 * z;
		_yt = invT.m21 * x + invT.m22 * y + invT.m23 * z;
		_zt = invT.m31 * x + invT.m32 * y + invT.m33 * z;
		return new WB_Vector(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	public void applyInvAsVectorInto(final double x, final double y, final double z, final WB_MutableCoord result) {
		_xt = invT.m11 * x + invT.m12 * y + invT.m13 * z;
		_yt = invT.m21 * x + invT.m22 * y + invT.m23 * z;
		_zt = invT.m31 * x + invT.m32 * y + invT.m33 * z;
		result.set(_xt, _yt, _zt);
	}

	/**
	 * Apply as normal.
	 *
	 * @param p
	 *
	 * @return
	 */
	public WB_Vector applyAsNormal(final WB_Coord p) {
		final double nx = invT.m11 * p.xd() + invT.m21 * p.yd() + invT.m31 * p.zd();
		final double ny = invT.m12 * p.xd() + invT.m22 * p.yd() + invT.m32 * p.zd();
		final double nz = invT.m13 * p.xd() + invT.m23 * p.yd() + invT.m33 * p.zd();
		return new WB_Vector(nx, ny, nz);
	}

	/**
	 * Apply transform to normal.
	 *
	 * @param n
	 *            normal
	 */
	public void applyAsNormalSelf(final WB_MutableCoord n) {
		final double x = invT.m11 * n.xd() + invT.m21 * n.yd() + invT.m31 * n.zd();
		final double y = invT.m12 * n.xd() + invT.m22 * n.yd() + invT.m32 * n.zd();
		final double z = invT.m13 * n.xd() + invT.m23 * n.yd() + invT.m33 * n.zd();
		n.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param n
	 * @param result
	 */
	public void applyAsNormalInto(final WB_Coord n, final WB_MutableCoord result) {
		_xt = invT.m11 * n.xd() + invT.m21 * n.yd() + invT.m31 * n.zd();
		_yt = invT.m12 * n.xd() + invT.m22 * n.yd() + invT.m32 * n.zd();
		_zt = invT.m13 * n.xd() + invT.m23 * n.yd() + invT.m33 * n.zd();
		result.set(_xt, _yt, _zt);
	}

	/**
	 * Apply as normal.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return
	 */
	public WB_Vector applyAsNormal(final double x, final double y, final double z) {
		final double nx = invT.m11 * x + invT.m21 * y + invT.m31 * z;
		final double ny = invT.m12 * x + invT.m22 * y + invT.m32 * z;
		final double nz = invT.m13 * x + invT.m23 * y + invT.m33 * z;
		return new WB_Vector(nx, ny, nz);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	public void applyAsNormalInto(final double x, final double y, final double z, final WB_MutableCoord result) {
		_xt = invT.m11 * x + invT.m21 * y + invT.m31 * z;
		_yt = invT.m12 * x + invT.m22 * y + invT.m32 * z;
		_zt = invT.m13 * x + invT.m23 * y + invT.m33 * z;
		result.set(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param n
	 */
	public WB_Vector applyInvAsNormal(final WB_Coord n) {
		_xt = T.m11 * n.xd() + T.m21 * n.yd() + T.m31 * n.zd();
		_yt = T.m12 * n.xd() + T.m22 * n.yd() + T.m32 * n.zd();
		_zt = T.m13 * n.xd() + T.m23 * n.yd() + T.m33 * n.zd();
		return new WB_Vector(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param n
	 */
	public void applyInvAsNormalSelf(final WB_MutableCoord n) {
		_xt = T.m11 * n.xd() + T.m21 * n.yd() + T.m31 * n.zd();
		_yt = T.m12 * n.xd() + T.m22 * n.yd() + T.m32 * n.zd();
		_zt = T.m13 * n.xd() + T.m23 * n.yd() + T.m33 * n.zd();
		n.set(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param n
	 * @param result
	 */
	public void applyInvAsNormalInto(final WB_Coord n, final WB_MutableCoord result) {
		_xt = T.m11 * n.xd() + T.m21 * n.yd() + T.m31 * n.zd();
		_yt = T.m12 * n.xd() + T.m22 * n.yd() + T.m32 * n.zd();
		_zt = T.m13 * n.xd() + T.m23 * n.yd() + T.m33 * n.zd();
		result.set(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public WB_Vector applyInvAsNormal(final double x, final double y, final double z) {
		_xt = T.m11 * x + T.m21 * y + T.m31 * z;
		_yt = T.m12 * x + T.m22 * y + T.m32 * z;
		_zt = T.m13 * x + T.m23 * y + T.m33 * z;
		return new WB_Vector(_xt, _yt, _zt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param result
	 */
	public void applyInvAsNormalInto(final double x, final double y, final double z, final WB_MutableCoord result) {
		_xt = T.m11 * x + T.m21 * y + T.m31 * z;
		_yt = T.m12 * x + T.m22 * y + T.m32 * z;
		_zt = T.m13 * x + T.m23 * y + T.m33 * z;
		result.set(_xt, _yt, _zt);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String s = "WB_Transform T:" + "\n" + "[" + T.m11 + ", " + T.m12 + ", " + T.m13 + ", " + T.m14 + "]"
				+ "\n" + "[" + T.m21 + ", " + T.m22 + ", " + T.m23 + ", " + T.m24 + "]" + "\n" + "[" + T.m31 + ", "
				+ T.m32 + ", " + T.m33 + ", " + T.m34 + "]" + "\n" + "[" + T.m41 + ", " + T.m42 + ", " + T.m43 + ", "
				+ T.m44 + "]";
		return s;
	}

	/**
	 * Get the Euler angles corresponding to the rotational part of the
	 * transformation. Only works if the transformation is rotation and
	 * translation, nothing else!
	 *
	 * @return
	 */
	public WB_Vector getEulerAnglesXYZ() {

		double theta, phi, psi;
		if (WB_Epsilon.isEqualAbs(Math.abs(T.m31), 1.0)) {
			phi = 0.0;
			if (T.m31 < 0) {
				theta = Math.PI * 0.5;
				psi = Math.atan2(T.m12, T.m13);
			} else {
				theta = -Math.PI * 0.5;
				psi = Math.atan2(-T.m12, -T.m13);
			}
		} else {
			theta = -Math.asin(T.m31);
			final double ic = 1.0 / Math.cos(theta);
			psi = Math.atan2(T.m32 * ic, T.m33 * ic);
			phi = Math.atan2(T.m21 * ic, T.m11 * ic);
		}
		return geometryfactory.createVector(psi, theta, phi);
	}
}
