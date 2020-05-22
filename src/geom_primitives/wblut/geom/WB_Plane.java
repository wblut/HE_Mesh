package wblut.geom;

import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 *
 */
public class WB_Plane implements WB_Transformable3D {
	/**
	 *
	 *
	 * @return
	 */
	public static final WB_Plane XY() {
		return new WB_Plane(0, 0, 0, 0, 0, 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static final WB_Plane ZX() {
		return new WB_Plane(0, 0, 0, 0, 1, 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static final WB_Plane YZ() {
		return new WB_Plane(0, 0, 0, 1, 0, 0);
	}

	/**  */
	private WB_Vector n;
	/**  */
	private WB_Point origin;
	/**  */
	private WB_Vector u, v;

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public WB_Plane(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		final WB_Vector v21 = new WB_Vector(p1, p2);
		final WB_Vector v31 = new WB_Vector(p1, p3);
		n = new WB_Vector(v21.cross(v31));
		n.normalizeSelf();
		origin = new WB_Point(p1);
		setAxes();
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @param nx
	 * @param ny
	 * @param nz
	 */
	public WB_Plane(final double ox, final double oy, final double oz, final double nx, final double ny,
			final double nz) {
		origin = new WB_Point(ox, oy, oz);
		n = new WB_Vector(nx, ny, nz);
		n.normalizeSelf();
		setAxes();
	}

	/**
	 *
	 *
	 * @param o
	 * @param n
	 */
	public WB_Plane(final WB_Coord o, final WB_Coord n) {
		origin = new WB_Point(o);
		this.n = new WB_Vector(n);
		this.n.normalizeSelf();
		setAxes();
	}

	/**
	 *
	 *
	 * @param o
	 * @param n
	 */
	protected void set(final WB_Coord o, final WB_Coord n) {
		origin = new WB_Point(o);
		this.n = new WB_Vector(n);
		this.n.normalizeSelf();
		setAxes();
	}

	/**
	 *
	 *
	 * @param n
	 * @param d
	 */
	public WB_Plane(final WB_Coord n, final double d) {
		this.n = new WB_Vector(n);
		this.n.normalizeSelf();
		if (WB_Math.fastAbs(n.xd()) > WB_Math.fastAbs(n.yd())) {
			if (WB_Math.fastAbs(n.xd()) > WB_Math.fastAbs(n.zd())) {
				origin = new WB_Point(d / n.xd(), 0, 0);
			} else {
				origin = new WB_Point(0, 0, d / n.zd());
			}
		} else {
			if (WB_Math.fastAbs(n.yd()) > WB_Math.fastAbs(n.zd())) {
				origin = new WB_Point(0, d / n.yd(), 0);
			} else {
				origin = new WB_Point(0, 0, d / n.zd());
			}
		}
		setAxes();
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @param d
	 */
	public WB_Plane(final double ox, final double oy, final double oz, final double nx, final double ny,
			final double nz, final double d) {
		origin = new WB_Point(ox, oy, oz);
		n = new WB_Vector(nx, ny, nz);
		n.normalizeSelf();
		origin.addMulSelf(d, n);
		setAxes();
	}

	/**
	 *
	 *
	 * @param o
	 * @param n
	 * @param d
	 */
	public WB_Plane(final WB_Coord o, final WB_Coord n, final double d) {
		origin = new WB_Point(o);
		this.n = new WB_Vector(n);
		this.n.normalizeSelf();
		origin.addMulSelf(d, this.n);
		setAxes();
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param d
	 */
	public WB_Plane(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final double d) {
		final WB_Vector v21 = new WB_Vector(p1, p2);
		final WB_Vector v31 = new WB_Vector(p1, p3);
		n = new WB_Vector(v21.cross(v31));
		n.normalizeSelf();
		origin = new WB_Point(p1);
		origin.addMulSelf(d, n);
		setAxes();
	}

	public void overrideUV(final WB_Coord u, final WB_Coord v) {
		this.u = new WB_Vector(u);
		this.v = new WB_Vector(v);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Plane get() {
		return new WB_Plane(origin, n);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector getNormal() {
		return n.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double a() {
		return n.xd();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double b() {
		return n.yd();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double c() {
		return n.zd();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double d() {
		return -n.dot(origin);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point getOrigin() {
		return origin.copy();
	}

	/**
	 *
	 */
	public void flipNormal() {
		n.mulSelf(-1);
		v.mulSelf(-1);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "Plane o: [" + origin + "] n: [" + n + "] d: [" + d() + "]";
	}

	/**
	 *
	 */
	private void setAxes() {
		u = WB_Vector.cross(WB_Vector.Z(), n);
		if (WB_Epsilon.isZeroSq(u.dot(u))) {
			u = WB_Vector.cross(WB_Vector.Y(), n);
		}
		u.normalizeSelf();
		v = n.cross(u);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	// Return coordinates relative to plane axes
	public WB_Point localPoint(final WB_Coord p) {
		return new WB_Point(
				u.xd() * (p.xd() - origin.xd()) + u.yd() * (p.yd() - origin.yd()) + u.zd() * (p.zd() - origin.zd()),
				v.xd() * (p.xd() - origin.xd()) + v.yd() * (p.yd() - origin.yd()) + v.zd() * (p.zd() - origin.zd()),
				n.xd() * (p.xd() - origin.xd()) + n.yd() * (p.yd() - origin.yd()) + n.zd() * (p.zd() - origin.zd()));
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	// Return 2D coordinates relative to plane axes
	public WB_Point localPoint2D(final WB_Coord p) {
		return new WB_Point(
				u.xd() * (p.xd() - origin.xd()) + u.yd() * (p.yd() - origin.yd()) + u.zd() * (p.zd() - origin.zd()),
				v.xd() * (p.xd() - origin.xd()) + v.yd() * (p.yd() - origin.yd()) + v.zd() * (p.zd() - origin.zd()));
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	// Return embedded point coordinates relative to world axes
	public WB_Point extractPoint(final WB_Coord p) {
		return new WB_Point(origin.xd() + p.xd() * u.xd() + p.yd() * v.xd(),
				origin.yd() + p.xd() * u.yd() + p.yd() * v.yd(), origin.zd() + p.xd() * u.zd() + p.yd() * v.zd());
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	// Return embedded point coordinates relative to world axes
	public WB_Point extractPoint(final double x, final double y) {
		return new WB_Point(origin.xd() + x * u.xd() + y * v.xd(), origin.yd() + x * u.yd() + y * v.yd(),
				origin.zd() + x * u.zd() + y * v.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	// Return coordinates relative to world axes
	public WB_Point extractPoint2D(final WB_Coord p) {
		return new WB_Point(origin.xd() + p.xd() * u.xd() + p.yd() * v.xd() + p.zd() * n.xd(),
				origin.yd() + p.xd() * u.yd() + p.yd() * v.yd() + p.zd() * n.yd(),
				origin.zd() + p.xd() * u.zd() + p.yd() * v.zd() + p.zd() * n.zd());
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	// Return coordinates relative to world axes
	public WB_Point extractPoint(final double x, final double y, final double z) {
		return new WB_Point(origin.xd() + x * u.xd() + y * v.xd() + z * n.xd(),
				origin.yd() + x * u.yd() + y * v.yd() + z * n.yd(), origin.zd() + x * u.zd() + y * v.zd() + z * n.zd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	// Return new point mirrored across plane
	public WB_Point mirrorPoint(final WB_Coord p) {
		if (WB_Epsilon.isZero(WB_GeometryOp.getDistance3D(p, this))) {
			return new WB_Point(p);
		}
		return extractPoint2D(localPoint(p).scaleSelf(1, 1, -1));
	}

	/**
	 *
	 *
	 * @return
	 */
	// Return copy of u coordinate axis in world coordinates
	public WB_Vector getU() {
		return u.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	// Return copy of v coordinate axis in world coordinates
	public WB_Vector getV() {
		return v.copy();
	}

	/**
	 *
	 *
	 * @return
	 */
	// Return copy of w coordinate axis in world coordinates
	public WB_Vector getW() {
		return getNormal();
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Plane apply2D(final WB_Transform2D T) {
		return new WB_Plane(origin.apply2D(T), n.apply2D(T));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Plane apply2DSelf(final WB_Transform2D T) {
		origin.apply2DSelf(T);
		n.apply2DSelf(T);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Plane apply(final WB_Transform3D T) {
		return new WB_Plane(origin.apply(T), n.apply(T));
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	@Override
	public WB_Plane applySelf(final WB_Transform3D T) {
		origin.applySelf(T);
		n.applySelf(T);
		return this;
	}
}
