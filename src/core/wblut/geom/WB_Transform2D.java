/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_M33;

/**
 *
 */
public class WB_Transform2D {
	/**
	 *
	 */
	private double	_xt, _yt;
	/** Transform matrix. */
	private WB_M33	T;
	/** Inverse transform matrix. */
	private WB_M33	invT;

	/**
	 * Instantiates a new WB_Transfrom.
	 */
	public WB_Transform2D() {
		T = new WB_M33(1, 0, 0, 0, 1, 0, 0, 0, 1);
		invT = new WB_M33(1, 0, 0, 0, 1, 0, 0, 0, 1);
	}

	/**
	 *
	 *
	 * @param Trans
	 */
	public WB_Transform2D(final WB_Transform2D Trans) {
		T = Trans.T.get();
		invT = Trans.invT.get();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Transform2D get() {
		return new WB_Transform2D(this);
	}

	/**
	 * Add translation to transform.
	 *
	 * @param v
	 *            vector
	 * @return self
	 */
	public WB_Transform2D addTranslate2D(final WB_Coord v) {
		T = new WB_M33(1, 0, v.xd(), 0, 1, v.yd(), 0, 0, 1).mul(T);
		invT = invT.mul(new WB_M33(1, 0, -v.xd(), 0, 1, -v.yd(), 0, 0, 1));
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @param v
	 * @return
	 */
	public WB_Transform2D addTranslate2D(final double f, final WB_Coord v) {
		T = new WB_M33(1, 0, f * v.xd(), 0, 1, f * v.yd(), 0, 0, 1).mul(T);
		invT = invT
				.mul(new WB_M33(1, 0, -f * v.xd(), 0, 1, -f * v.yd(), 0, 0, 1));
		return this;
	}

	/**
	 * Add uniform scale to transform.
	 *
	 * @param s
	 *            scaling vector
	 * @return self
	 */
	public WB_Transform2D addScale2D(final WB_Coord s) {
		T = new WB_M33(s.xd(), 0, 0, 0, s.yd(), 0, 0, 0, 1).mul(T);
		invT = invT.mul(
				new WB_M33(1.0 / s.xd(), 0, 0, 0, 1.0 / s.yd(), 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add non-uniform scale to transform.
	 *
	 * @param sx
	 *            scaling vector
	 * @param sy
	 *            scaling vector
	 * @return self
	 */
	public WB_Transform2D addScale2D(final double sx, final double sy) {
		T = new WB_M33(sx, 0, 0, 0, sy, 0, 0, 0, 1).mul(T);
		invT = invT.mul(new WB_M33(1.0 / sx, 0, 0, 0, 1.0 / sy, 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add uniform scale to transform.
	 *
	 * @param s
	 *            scaling point
	 * @return self
	 */
	public WB_Transform2D addScale2D(final double s) {
		T = new WB_M33(s, 0, 0, 0, s, 0, 0, 0, 1).mul(T);
		invT = invT.mul(new WB_M33(1 / s, 0, 0, 0, 1 / s, 0, 0, 0, 1));
		return this;
	}

	/**
	 * Add rotation around origin.
	 *
	 * @param angle
	 *            angle in radians
	 * @return self
	 */
	public WB_Transform2D addRotateAboutOrigin(final double angle) {
		final double s = Math.sin(angle);
		final double c = Math.cos(angle);
		final WB_M33 tmp = new WB_M33(c, -s, 0, s, c, 0, 0, 0, 1);
		T = tmp.mul(T);
		invT = invT.mul(tmp.getTranspose());
		return this;
	}

	/**
	 * Add rotation around point.
	 *
	 * @param angle
	 *            angle in radians
	 * @param p
	 *            point
	 * @return self
	 */
	public WB_Transform2D addRotateAboutPoint(final double angle,
			final WB_Coord p) {
		addTranslate2D(-1, p);
		addRotateAboutOrigin(angle);
		addTranslate2D(p);
		return this;
	}

	/**
	 * Adds the reflect x.
	 *
	 * @return
	 */
	public WB_Transform2D addReflectX() {
		addScale2D(-1, 1);
		return this;
	}

	/**
	 * Adds the reflect y.
	 *
	 * @return
	 */
	public WB_Transform2D addReflectY() {
		addScale2D(1, -1);
		return this;
	}

	/**
	 * Adds the invert.
	 *
	 * @return
	 */
	public WB_Transform2D addInvert2D() {
		addScale2D(-1);
		return this;
	}

	/**
	 * Adds the reflect x.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform2D addReflectX(final WB_Coord p) {
		addTranslate2D(-1, p);
		addScale2D(-1, 1);
		addTranslate2D(p);
		return this;
	}

	/**
	 * Adds the reflect y.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform2D addReflectY(final WB_Coord p) {
		addTranslate2D(-1, p);
		addScale2D(1, -1);
		addTranslate2D(p);
		return this;
	}

	/**
	 * Adds the invert.
	 *
	 * @param p
	 *            the p
	 * @return
	 */
	public WB_Transform2D addInvert2D(final WB_Coord p) {
		addTranslate2D(-1, p);
		addScale2D(-1, -1);
		addTranslate2D(p);
		return this;
	}

	/**
	 *
	 * @param p1
	 * @param p2
	 * @return
	 */
	public WB_Transform2D addReflect2D(final WB_Coord p1, final WB_Coord p2) {
		WB_Vector u = WB_Vector.sub(p2, p1);
		WB_Vector v = new WB_Vector(-u.yd(), u.xd());
		final WB_M33 Tr = new WB_M33(1, 0, p1.xd(), 0, 1, p1.yd(), 0, 0, 1);
		Tr.mul(new WB_M33(u.x, v.x, 0, u.y, v.y, 0, 0, 0, 1));
		Tr.mul(new WB_M33(1, 0, 0, 0, -1, 0, 0, 0, 1));
		Tr.mul(new WB_M33(u.x, u.y, 0, v.x, v.y, 0, 0, 0, 1));
		Tr.mul(new WB_M33(1, 0, -p1.xd(), 0, 1, -p1.yd(), 0, 0, 1));
		T = Tr.mul(T);
		invT = invT.mul(Tr);
		return this;
	}

	/**
	 *
	 * @return
	 */
	public WB_Transform2D addShear2D(final double shx, final double shy) {
		WB_M33 Tr = new WB_M33(1, shx, 0, shy, 1, 0, 0, 0, 1);
		T = Tr.mul(T);
		invT = invT.mul(Tr.inverse());
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
	public WB_Transform2D addFromCSToCS2D(final WB_CoordinateSystem CS1,
			final WB_CoordinateSystem CS2) {
		addFromCSToWorld2D(CS1);
		addFromWorldToCS2D(CS2);
		return this;
	}

	/**
	 *
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform2D addFromCSToWorld2D(final WB_CoordinateSystem CS) {
		WB_CoordinateSystem current = CS;
		while (!current.isWorld()) {
			addFromCSToParent2D(current);
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
	public WB_Transform2D addFromWorldToCS2D(final WB_CoordinateSystem CS) {
		final WB_Transform2D tmp = new WB_Transform2D();
		tmp.addFromCSToWorld2D(CS);
		T = tmp.invT.mul(T);
		invT = invT.mul(tmp.T);
		return this;
	}

	/**
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform2D addFromCSToParent2D(final WB_CoordinateSystem CS) {
		final WB_CoordinateSystem WCS = WB_CoordinateSystem.WORLD();
		if (CS.isWorld()) {
			return this;
		}
		final WB_Vector ex1 = CS.getX(), ey1 = CS.getY();
		final WB_Point o1 = CS.getOrigin();
		final WB_Vector ex2 = WCS.getX(), ey2 = WCS.getY();
		final WB_Point o2 = WCS.getOrigin();
		final double xx = ex2.dot(ex1);
		final double xy = ex2.dot(ey1);
		final double yx = ey2.dot(ex1);
		final double yy = ey2.dot(ey1);
		final WB_M33 tmp = new WB_M33(xx, xy, 0, yx, yy, 0, 0, 0, 1);
		final WB_M33 invtmp = new WB_M33(xx, yx, 0, xy, yy, 0, 0, 0, 1);
		T = tmp.mul(T);
		invT = invT.mul(invtmp);
		addTranslate2D(o1.subSelf(o2));
		return this;
	}

	/**
	 *
	 * @param CS
	 *
	 * @return
	 */
	public WB_Transform2D addFromParentToCS2D(final WB_CoordinateSystem CS) {
		if (CS.isWorld()) {
			return this;
		}
		final WB_Transform2D tmp = new WB_Transform2D();
		tmp.addFromCSToParent2D(CS);
		T = tmp.invT.mul(T);
		invT = invT.mul(tmp.T);
		return this;
	}

	/**
	 * Invert transform.
	 */
	public void inverse() {
		WB_M33 tmp;
		tmp = T;
		T = invT;
		invT = tmp;
	}

	/**
	 * Clear transform.
	 */
	public void clear() {
		T = new WB_M33(1, 0, 0, 0, 1, 0, 0, 0, 1);
		invT = new WB_M33(1, 0, 0, 0, 1, 0, 0, 0, 1);
	}

	/**
	 * Apply transform to point.
	 *
	 * @param p
	 *            point
	 * @return new WB_XYZ
	 */
	public WB_Point applyAsPoint2D(final WB_Coord p) {
		final double xp = T.m11 * p.xd() + T.m12 * p.yd() + T.m13;
		final double yp = T.m21 * p.xd() + T.m22 * p.yd() + +T.m23;
		double wp = T.m31 * p.xd() + T.m32 * p.yd() + +T.m33;
		if (WB_Epsilon.isZero(wp)) {
			return new WB_Point(xp, yp);
		}
		wp = 1.0 / wp;
		return new WB_Point(xp * wp, yp * wp);
	}

	/**
	 * Apply transform to point.
	 *
	 * @param p
	 *            point
	 */
	public void applyAsPoint2DSelf(final WB_MutableCoord p) {
		final double x = T.m11 * p.xd() + T.m12 * p.yd() + T.m13;
		final double y = T.m21 * p.xd() + T.m22 * p.yd() + T.m23;
		double wp = T.m31 * p.xd() + T.m32 * p.yd() + T.m33;
		wp = 1.0 / wp;
		p.set(x * wp, y * wp);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyAsPoint2DInto(final WB_Coord p,
			final WB_MutableCoord result) {
		_xt = T.m11 * p.xd() + T.m12 * p.yd() + T.m13;
		_yt = T.m21 * p.xd() + T.m22 * p.yd() + T.m23;
		double wp = T.m31 * p.xd() + T.m32 * p.yd() + T.m33;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp);
	}

	/**
	 * Apply as point.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @return
	 */
	public WB_Point applyAsPoint2D(final double x, final double y) {
		final double xp = T.m11 * x + T.m12 * y + T.m13;
		final double yp = T.m21 * x + T.m22 * y + T.m23;
		double wp = T.m31 * x + T.m32 * y + T.m33;
		if (WB_Epsilon.isZero(wp)) {
			return new WB_Point(xp, yp);
		}
		wp = 1.0 / wp;
		return new WB_Point(xp * wp, yp * wp);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @param result
	 */
	public void applyAsPoint2DInto(final double x, final double y,
			final WB_MutableCoord result) {
		_xt = T.m11 * x + T.m12 * y + T.m13;
		_yt = T.m21 * x + T.m22 * y + T.m23;
		double wp = T.m31 * x + T.m32 * y + T.m33;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp);
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public WB_Point applyInvAsPoint2D(final WB_Coord p) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13;
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23;
		double wp = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33;
		wp = 1.0 / wp;
		return new WB_Point(_xt * wp, _yt * wp);
	}

	/**
	 *
	 *
	 * @param p
	 */
	public void applyInvAsPoint2DSelf(final WB_MutableCoord p) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13;
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23;
		double wp = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33;
		wp = 1.0 / wp;
		p.set(_xt * wp, _yt * wp);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyInvAsPoint2DInto(final WB_Coord p,
			final WB_MutableCoord result) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd() + invT.m13;
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd() + invT.m23;
		double wp = invT.m31 * p.xd() + invT.m32 * p.yd() + invT.m33;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp);
	}

	/**
	 *
	 * @param x
	 * @param y
	 *
	 * @return
	 */
	public WB_Point applyInvAsPoint2D(final double x, final double y) {
		_xt = invT.m11 * x + invT.m12 * y + invT.m13;
		_yt = invT.m21 * x + invT.m22 * y + invT.m23;
		double wp = invT.m31 * x + invT.m32 * y + invT.m33;
		wp = 1.0 / wp;
		return new WB_Point(_xt * wp, _yt * wp);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @param result
	 */
	public void applyInvAsPoint2DInto(final double x, final double y,
			final WB_MutableCoord result) {
		_xt = invT.m11 * x + invT.m12 * y + invT.m13;
		_yt = invT.m21 * x + invT.m22 * y + invT.m23;
		double wp = invT.m31 * x + invT.m32 * y + invT.m33;
		wp = 1.0 / wp;
		result.set(_xt * wp, _yt * wp);
	}

	/**
	 * Apply transform to vector.
	 *
	 * @param p
	 *            vector
	 * @return new WB_Vector
	 */
	public WB_Vector applyAsVector2D(final WB_Coord p) {
		final double xp = T.m11 * p.xd() + T.m12 * p.yd();
		final double yp = T.m21 * p.xd() + T.m22 * p.yd();
		return new WB_Vector(xp, yp);
	}

	/**
	 * Apply transform to vector.
	 *
	 * @param p
	 *            vector
	 */
	public void applyAsVector2DSelf(final WB_MutableCoord p) {
		final double x = T.m11 * p.xd() + T.m12 * p.yd();
		final double y = T.m21 * p.xd() + T.m22 * p.yd();
		p.set(x, y);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyAsVector2DInto(final WB_Coord p,
			final WB_MutableCoord result) {
		_xt = T.m11 * p.xd() + T.m12 * p.yd();
		_yt = T.m21 * p.xd() + T.m22 * p.yd();
		result.set(_xt, _yt);
	}

	/**
	 * Apply as vector.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @return
	 */
	public WB_Vector applyAsVector2D(final double x, final double y) {
		final double xp = T.m11 * x + T.m12 * y;
		final double yp = T.m21 * x + T.m22 * y;
		return new WB_Vector(xp, yp);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @param result
	 */
	public void applyAsVector2DInto(final double x, final double y,
			final WB_MutableCoord result) {
		_xt = T.m11 * x + T.m12 * y;
		_yt = T.m21 * x + T.m22 * y;
		result.set(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param p
	 */
	public WB_Vector applyInvAsVector2D(final WB_Coord p) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd();
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd();
		return new WB_Vector(_xt, _yt);
	}

	/**
	 *
	 * @param v
	 */
	public void applyInvAsVector2DSelf(final WB_MutableCoord v) {
		_xt = invT.m11 * v.xd() + invT.m12 * v.yd();
		_yt = invT.m21 * v.xd() + invT.m22 * v.yd();
		v.set(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param p
	 * @param result
	 */
	public void applyInvAsVector2DInto(final WB_Coord p,
			final WB_MutableCoord result) {
		_xt = invT.m11 * p.xd() + invT.m12 * p.yd();
		_yt = invT.m21 * p.xd() + invT.m22 * p.yd();
		result.set(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 */
	public WB_Vector applyInvAsVector2D(final double x, final double y) {
		_xt = invT.m11 * x + invT.m12 * y;
		_yt = invT.m21 * x + invT.m22 * y;
		return new WB_Vector(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @param result
	 */
	public void applyInvAsVector2DInto(final double x, final double y,
			final WB_MutableCoord result) {
		_xt = invT.m11 * x + invT.m12 * y;
		_yt = invT.m21 * x + invT.m22 * y;
		result.set(_xt, _yt);
	}

	/**
	 * Apply as normal.
	 *
	 * @param p
	 *
	 * @return
	 */
	public WB_Vector applyAsNormal2D(final WB_Coord p) {
		final double nx = invT.m11 * p.xd() + invT.m21 * p.yd();
		final double ny = invT.m12 * p.xd() + invT.m22 * p.yd();
		return new WB_Vector(nx, ny);
	}

	/**
	 * Apply transform to normal.
	 *
	 * @param n
	 *            normal
	 */
	public void applyAsNormal2DSelf(final WB_MutableCoord n) {
		final double x = invT.m11 * n.xd() + invT.m21 * n.yd();
		final double y = invT.m12 * n.xd() + invT.m22 * n.yd();
		n.set(x, y);
	}

	/**
	 *
	 *
	 * @param n
	 * @param result
	 */
	public void applyAsNormal2DInto(final WB_Coord n,
			final WB_MutableCoord result) {
		_xt = invT.m11 * n.xd() + invT.m21 * n.yd();
		_yt = invT.m12 * n.xd() + invT.m22 * n.yd();
		result.set(_xt, _yt);
	}

	/**
	 * Apply as normal.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @return
	 */
	public WB_Vector applyAsNormal2D(final double x, final double y) {
		final double nx = invT.m11 * x + invT.m21 * y;
		final double ny = invT.m12 * x + invT.m22 * y;
		return new WB_Vector(nx, ny);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @param result
	 */
	public void applyAsNormal2DInto(final double x, final double y,
			final WB_MutableCoord result) {
		_xt = invT.m11 * x + invT.m21 * y;
		_yt = invT.m12 * x + invT.m22 * y;
		result.set(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param n
	 */
	public WB_Vector applyInvAsNormal2D(final WB_Coord n) {
		_xt = T.m11 * n.xd() + T.m21 * n.yd();
		_yt = T.m12 * n.xd() + T.m22 * n.yd();
		return new WB_Vector(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param n
	 */
	public void applyInvAsNormal2DSelf(final WB_MutableCoord n) {
		_xt = T.m11 * n.xd() + T.m21 * n.yd();
		_yt = T.m12 * n.xd() + T.m22 * n.yd();
		n.set(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param n
	 * @param result
	 */
	public void applyInvAsNormal2DInto(final WB_Coord n,
			final WB_MutableCoord result) {
		_xt = T.m11 * n.xd() + T.m21 * n.yd();
		_yt = T.m12 * n.xd() + T.m22 * n.yd();
		result.set(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 */
	public WB_Vector applyInvAsNormal2D(final double x, final double y) {
		_xt = T.m11 * x + T.m21 * y;
		_yt = T.m12 * x + T.m22 * y;
		return new WB_Vector(_xt, _yt);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @param result
	 */
	public void applyInvAsNormal2DInto(final double x, final double y,
			final WB_MutableCoord result) {
		_xt = T.m11 * x + T.m21 * y;
		_yt = T.m12 * x + T.m22 * y;
		result.set(_xt, _yt);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String s = "WB_Transform2D T:" + "\n" + "[" + T.m11 + ", " + T.m12
				+ ", " + T.m13 + "]" + "\n" + "[" + T.m21 + ", " + T.m22 + ", "
				+ T.m23 + "]" + "\n" + "[" + T.m31 + ", " + T.m32 + ", " + T.m33
				+ "]";
		return s;
	}
}
