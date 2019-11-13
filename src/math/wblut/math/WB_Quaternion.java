// Adapted from com.badlogic.gdx
package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_MutableCoord;
import wblut.geom.WB_Vector;

public class WB_Quaternion {
	public double	x;
	public double	y;
	public double	z;
	public double	w;

	public WB_Quaternion(double x, double y, double z, double w) {
		this.set(x, y, z, w);
	}

	public WB_Quaternion() {
		identity();
	}

	public WB_Quaternion(WB_Quaternion quaternion) {
		this.set(quaternion);
	}

	public WB_Quaternion(WB_Coord axis, double angle) {
		this.set(axis, angle);
	}

	public WB_Quaternion set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public WB_Quaternion set(WB_Quaternion quaternion) {
		return this.set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
	}

	public WB_Quaternion set(WB_Coord axis, double angle) {
		return setFromAxis(axis.xd(), axis.yd(), axis.zd(), angle);
	}

	public WB_Quaternion setEulerAngles(double yaw, double pitch, double roll) {
		final double hr = roll * 0.5;
		final double shr = Math.sin(hr);
		final double chr = Math.cos(hr);
		final double hp = pitch * 0.5;
		final double shp = Math.sin(hp);
		final double chp = Math.cos(hp);
		final double hy = yaw * 0.5;
		final double shy = Math.sin(hy);
		final double chy = Math.cos(hy);
		final double chy_shp = chy * shp;
		final double shy_chp = shy * chp;
		final double chy_chp = chy * chp;
		final double shy_shp = shy * shp;
		x = (chy_shp * chr) + (shy_chp * shr);
		y = (shy_chp * chr) - (chy_shp * shr);
		z = (chy_chp * shr) - (shy_shp * chr);
		w = (chy_chp * chr) + (shy_shp * shr);
		return this;
	}

	public WB_Quaternion get() {
		return new WB_Quaternion(this);
	}

	public final static double getLength(final double x, final double y,
			final double z, final double w) {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public WB_Quaternion identity() {
		return this.set(0, 0, 0, 1);
	}

	public boolean isIdentity() {
		return WB_Epsilon.isZero(x) && WB_Epsilon.isZero(y)
				&& WB_Epsilon.isZero(z) && WB_Epsilon.isZero(w - 1.0);
	}

	@Override
	public String toString() {
		return "[" + x + "|" + y + "|" + z + "|" + w + "]";
	}

	public int getGimbalPole() {
		final double t = y * x + z * w;
		return t > 0.499999 ? 1 : (t < -0.499999 ? -1 : 0);
	}

	public double getRoll() {
		final int pole = getGimbalPole();
		return pole == 0
				? Math.atan2(2.0 * (w * z + y * x), 1.0 - 2.0 * (x * x + z * z))
				: pole * 2.0 * Math.atan2(y, w);
	}

	public double getPitch() {
		final int pole = getGimbalPole();
		return pole == 0 ? Math.asin(clamp(2.0 * (w * x - z * y), -1.0, 1.0))
				: pole * Math.PI * 0.5;
	}

	public double getYaw() {
		return getGimbalPole() == 0
				? Math.atan2(2.0 * (y * w + x * z), 1.0 - 2.0 * (y * y + x * x))
				: 0.0;
	}

	public double getAngle() {
		return 2.0 * Math.acos((this.w > 1) ? (this.w / getLength()) : this.w);
	}

	public double getAxisAngle(WB_MutableCoord axis) {
		if (this.w > 1)
			this.normalizeSelf();
		double angle = 2.0 * Math.acos(this.w);
		double s = Math.sqrt(1 - this.w * this.w);
		if (WB_Epsilon.isZero(s)) {
			axis.set(this.x, this.y, this.z);
		} else {
			axis.set(this.x / s, this.y / s, this.z / s);
		}
		return angle;
	}

	public double getAngleAround(final WB_Coord axis) {
		return getAngleAround(axis.xd(), axis.yd(), axis.zd());
	}

	public double getAngleAround(final double axisX, final double axisY,
			final double axisZ) {
		final double d = WB_CoordOp.dot(this.x, this.y, this.z, axisX, axisY,
				axisZ);
		final double l2 = getSqLength(axisX * d, axisY * d, axisZ * d, this.w);
		return WB_Epsilon.isZero(l2) ? 0.0
				: (2.0 * Math.acos(
						clamp(((d < 0 ? -this.w : this.w) / Math.sqrt(l2)),
								-1.0, 1.0)));
	}

	/**
	 * Get the swing rotation and twist rotation for the specified axis. The
	 * twist rotation represents the rotation around the
	 * specified axis. The swing rotation represents the rotation of the
	 * specified axis itself, which is the rotation around an
	 * axis perpendicular to the specified axis.
	 * </p>
	 * The swing and twist rotation can be used to reconstruct the original
	 * quaternion: this = swing * twist
	 * 
	 * @param axisX
	 *            the X component of the normalized axis for which to get the
	 *            swing and twist rotation
	 * @param axisY
	 *            the Y component of the normalized axis for which to get the
	 *            swing and twist rotation
	 * @param axisZ
	 *            the Z component of the normalized axis for which to get the
	 *            swing and twist rotation
	 * @param swing
	 *            will receive the swing rotation: the rotation around an axis
	 *            perpendicular to the specified axis
	 * @param twist
	 *            will receive the twist rotation: the rotation around the
	 *            specified axis
	 * @see <a href=
	 *      "http://www.euclideanspace.com/maths/geometry/rotations/for/decomposition">calculation</a>
	 */
	public void getSwingTwist(final double axisX, final double axisY,
			final double axisZ, final WB_Quaternion swing,
			final WB_Quaternion twist) {
		final double d = WB_CoordOp.dot(this.x, this.y, this.z, axisX, axisY,
				axisZ);
		twist.set(axisX * d, axisY * d, axisZ * d, this.w).normalizeSelf();
		if (d < 0)
			twist.mul(-1.0);
		swing.set(twist).conjugateSelf().mulLeftSelf(this);
	}

	/**
	 * Get the swing rotation and twist rotation for the specified axis. The
	 * twist rotation represents the rotation around the
	 * specified axis. The swing rotation represents the rotation of the
	 * specified axis itself, which is the rotation around an
	 * axis perpendicular to the specified axis.
	 * </p>
	 * The swing and twist rotation can be used to reconstruct the original
	 * quaternion: this = swing * twist
	 * 
	 * @param axis
	 *            the normalized axis for which to get the swing and twist
	 *            rotation
	 * @param swing
	 *            will receive the swing rotation: the rotation around an axis
	 *            perpendicular to the specified axis
	 * @param twist
	 *            will receive the twist rotation: the rotation around the
	 *            specified axis
	 * @see <a href=
	 *      "http://www.euclideanspace.com/maths/geometry/rotations/for/decomposition">calculation</a>
	 */
	public void getSwingTwist(final WB_Coord axis, final WB_Quaternion swing,
			final WB_Quaternion twist) {
		final double d = WB_CoordOp.dot(this.x, this.y, this.z, axis.xd(),
				axis.yd(), axis.zd());
		twist.set(axis.xd() * d, axis.yd() * d, axis.zd() * d, this.w)
				.normalizeSelf();
		if (d < 0)
			twist.mul(-1.0);
		swing.set(twist).conjugateSelf().mulLeftSelf(this);
	}

	private static double getSqLength(final double x, final double y,
			final double z, final double w) {
		return x * x + y * y + z * z + w * w;
	}

	public double getSqLength() {
		return x * x + y * y + z * z + w * w;
	}

	public WB_Quaternion normalizeSelf() {
		double len = getSqLength();
		if (len != 0. && !WB_Epsilon.isZero(len - 1.0)) {
			len = Math.sqrt(len);
			w /= len;
			x /= len;
			y /= len;
			z /= len;
		}
		return this;
	}

	public WB_Quaternion conjugateSelf() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public WB_Quaternion mul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		this.w *= scalar;
		return this;
	}

	public WB_Quaternion mulSelf(final WB_Quaternion other) {
		final double newX = this.w * other.x + this.x * other.w
				+ this.y * other.z - this.z * other.y;
		final double newY = this.w * other.y + this.y * other.w
				+ this.z * other.x - this.x * other.z;
		final double newZ = this.w * other.z + this.z * other.w
				+ this.x * other.y - this.y * other.x;
		final double newW = this.w * other.w - this.x * other.x
				- this.y * other.y - this.z * other.z;
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		this.w = newW;
		return this;
	}

	public WB_Quaternion mulSelf(final double x, final double y, final double z,
			final double w) {
		final double newX = this.w * x + this.x * w + this.y * z - this.z * y;
		final double newY = this.w * y + this.y * w + this.z * x - this.x * z;
		final double newZ = this.w * z + this.z * w + this.x * y - this.y * x;
		final double newW = this.w * w - this.x * x - this.y * y - this.z * z;
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		this.w = newW;
		return this;
	}

	@Deprecated
	public WB_Quaternion mulLeft(WB_Quaternion other) {
		return mulLeftSelf(other);
	}

	public WB_Quaternion mulLeftSelf(WB_Quaternion other) {
		final double newX = other.w * this.x + other.x * this.w
				+ other.y * this.z - other.z * this.y;
		final double newY = other.w * this.y + other.y * this.w
				+ other.z * this.x - other.x * this.z;
		final double newZ = other.w * this.z + other.z * this.w
				+ other.x * this.y - other.y * this.x;
		final double newW = other.w * this.w - other.x * this.x
				- other.y * this.y - other.z * this.z;
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		this.w = newW;
		return this;
	}

	public WB_Quaternion mulLeftSelf(final double x, final double y,
			final double z, final double w) {
		final double newX = w * this.x + x * this.w + y * this.z - z * this.y;
		final double newY = w * this.y + y * this.w + z * this.x - x * this.z;
		final double newZ = w * this.z + z * this.w + x * this.y - y * this.x;
		final double newW = w * this.w - x * this.x - y * this.y - z * this.z;
		this.x = newX;
		this.y = newY;
		this.z = newZ;
		this.w = newW;
		return this;
	}

	public WB_Quaternion addSelf(WB_Quaternion quaternion) {
		this.x += quaternion.x;
		this.y += quaternion.y;
		this.z += quaternion.z;
		this.w += quaternion.w;
		return this;
	}

	public WB_Quaternion addSelf(double qx, double qy, double qz, double qw) {
		this.x += qx;
		this.y += qy;
		this.z += qz;
		this.w += qw;
		return this;
	}

	public WB_M44 toMatrix() {
		final WB_M44 matrix = new WB_M44();
		final double xx = x * x;
		final double xy = x * y;
		final double xz = x * z;
		final double xw = x * w;
		final double yy = y * y;
		final double yz = y * z;
		final double yw = y * w;
		final double zz = z * z;
		final double zw = z * w;
		matrix.m11 = 1 - 2 * (yy + zz);
		matrix.m12 = 2 * (xy - zw);
		matrix.m13 = 2 * (xz + yw);
		matrix.m14 = 0;
		matrix.m21 = 2 * (xy + zw);
		matrix.m22 = 1 - 2 * (xx + zz);
		matrix.m23 = 2 * (yz - xw);
		matrix.m24 = 0;
		matrix.m31 = 2 * (xz - yw);
		matrix.m32 = 2 * (yz + xw);
		matrix.m33 = 1 - 2 * (xx + yy);
		matrix.m34 = 0;
		matrix.m41 = 0;
		matrix.m42 = 0;
		matrix.m43 = 0;
		matrix.m44 = 1;
		return matrix;
	}

	public WB_Quaternion setFromAxis(final WB_Coord axis,
			final double radians) {
		return setFromAxis(axis.xd(), axis.yd(), axis.zd(), radians);
	}

	public WB_Quaternion setFromAxis(final double x, final double y,
			final double z, final double radians) {
		double d = WB_CoordOp.getLength3D(x, y, z);
		if (d == 0.0)
			return identity();
		d = 1.0 / d;
		double l_ang = radians < 0 ? 2.0 * Math.PI - (-radians % 2.0 * Math.PI)
				: radians % 2.0 * Math.PI;
		double l_sin = Math.sin(l_ang / 2);
		double l_cos = Math.cos(l_ang / 2);
		return this.set(d * x * l_sin, d * y * l_sin, d * z * l_sin, l_cos)
				.normalizeSelf();
	}

	public WB_Quaternion setFromMatrix(boolean normalizeAxes, WB_M44 matrix) {
		return setFromAxes(normalizeAxes, matrix.m11, matrix.m12, matrix.m13,
				matrix.m21, matrix.m22, matrix.m23, matrix.m31, matrix.m32,
				matrix.m33);
	}

	public WB_Quaternion setFromMatrix(WB_M44 matrix) {
		return setFromMatrix(false, matrix);
	}

	public WB_Quaternion setFromAxes(double xx, double xy, double xz, double yx,
			double yy, double yz, double zx, double zy, double zz) {
		return setFromAxes(false, xx, xy, xz, yx, yy, yz, zx, zy, zz);
	}

	public WB_Quaternion setFromAxes(boolean normalizeAxes, double xx,
			double xy, double xz, double yx, double yy, double yz, double zx,
			double zy, double zz) {
		if (normalizeAxes) {
			final double lx = 1.0 / WB_CoordOp.getLength3D(xx, xy, xz);
			final double ly = 1.0 / WB_CoordOp.getLength3D(yx, yy, yz);
			final double lz = 1.0 / WB_CoordOp.getLength3D(zx, zy, zz);
			xx *= lx;
			xy *= lx;
			xz *= lx;
			yx *= ly;
			yy *= ly;
			yz *= ly;
			zx *= lz;
			zy *= lz;
			zz *= lz;
		}
		final double t = xx + yy + zz;
		if (t >= 0) {
			double s = Math.sqrt(t + 1);
			w = 0.5 * s;
			s = 0.5 / s;
			x = (zy - yz) * s;
			y = (xz - zx) * s;
			z = (yx - xy) * s;
		} else if ((xx > yy) && (xx > zz)) {
			double s = Math.sqrt(1.0 + xx - yy - zz);
			x = s * 0.5;
			s = 0.5 / s;
			y = (yx + xy) * s;
			z = (xz + zx) * s;
			w = (zy - yz) * s;
		} else if (yy > zz) {
			double s = Math.sqrt(1.0 + yy - xx - zz);
			y = s * 0.5;
			s = 0.5 / s;
			x = (yx + xy) * s;
			z = (zy + yz) * s;
			w = (xz - zx) * s;
		} else {
			double s = Math.sqrt(1.0 + zz - xx - yy);
			z = s * 0.5;
			s = 0.5 / s;
			x = (xz + zx) * s;
			y = (zy + yz) * s;
			w = (yx - xy) * s;
		}
		return this;
	}

	public WB_Quaternion setFromCross(final WB_Coord v1, final WB_Coord v2) {
		final double dot = clamp(WB_Vector.getDistance3D(v1, v2), -1.0, 1.0);
		final double angle = Math.acos(dot);
		return setFromAxis(v1.yd() * v2.zd() - v1.zd() * v2.yd(),
				v1.zd() * v2.xd() - v1.xd() * v2.zd(),
				v1.xd() * v2.yd() - v1.yd() * v2.xd(), angle);
	}

	public WB_Quaternion setFromCross(final double x1, final double y1,
			final double z1, final double x2, final double y2,
			final double z2) {
		final double dot = clamp(WB_CoordOp.dot(x1, y1, z1, x2, y2, z2), -1.0,
				1.0);
		final double angle = Math.acos(dot);
		return setFromAxis(y1 * z2 - z1 * y2, z1 * x2 - x1 * z2,
				x1 * y2 - y1 * x2, angle);
	}

	public WB_Quaternion slerp(WB_Quaternion end, double alpha) {
		final double d = this.x * end.x + this.y * end.y + this.z * end.z
				+ this.w * end.w;
		double absDot = d < 0.0 ? -d : d;
		double scale0 = 1.0 - alpha;
		double scale1 = alpha;
		if ((1 - absDot) > 0.01) {
			final double angle = Math.acos(absDot);
			final double invSinTheta = 1.0 / Math.sin(angle);
			scale0 = (Math.sin((1.0 - alpha) * angle) * invSinTheta);
			scale1 = (Math.sin((alpha * angle)) * invSinTheta);
		}
		if (d < 0.0)
			scale1 = -scale1;
		x = (scale0 * x) + (scale1 * end.x);
		y = (scale0 * y) + (scale1 * end.y);
		z = (scale0 * z) + (scale1 * end.z);
		w = (scale0 * w) + (scale1 * end.w);
		return this;
	}

	public WB_Quaternion slerp(WB_Quaternion[] q) {
		WB_Quaternion tmp1 = new WB_Quaternion(0, 0, 0, 0);
		final double w = 1.0 / q.length;
		set(q[0]).power(w);
		for (int i = 1; i < q.length; i++)
			mulSelf(tmp1.set(q[i]).power(w));
		normalizeSelf();
		return this;
	}

	public WB_Quaternion slerp(WB_Quaternion[] q, double[] w) {
		WB_Quaternion tmp1 = new WB_Quaternion(0, 0, 0, 0);
		set(q[0]).power(w[0]);
		for (int i = 1; i < q.length; i++)
			mulSelf(tmp1.set(q[i]).power(w[i]));
		normalizeSelf();
		return this;
	}

	public WB_Quaternion power(double alpha) {
		double norm = getLength();
		double normExp = Math.pow(norm, alpha);
		double theta = Math.acos(w / norm);
		double coeff = 0;
		if (Math.abs(theta) < 0.001)
			coeff = normExp * alpha / norm;
		else
			coeff = normExp * Math.sin(alpha * theta)
					/ (norm * Math.sin(theta));
		w = normExp * Math.cos(alpha * theta);
		x *= coeff;
		y *= coeff;
		z *= coeff;
		normalizeSelf();
		return this;
	}

	public double dot(final WB_Quaternion other) {
		return this.x * other.x + this.y * other.y + this.z * other.z
				+ this.w * other.w;
	}

	public double dot(final double x, final double y, final double z,
			final double w) {
		return this.x * x + this.y * y + this.z * z + this.w * w;
	}

	private static double clamp(double v, double l, double u) {
		return (v < l) ? l : (v > u) ? u : v;
	}

	public void rotate(WB_MutableCoord v) {
		WB_Quaternion tmp1 = new WB_Quaternion(0, 0, 0, 0);
		WB_Quaternion tmp2 = new WB_Quaternion(0, 0, 0, 0);
		tmp2.set(this);
		tmp2.conjugateSelf();
		tmp2.mulLeftSelf(tmp1.set(v.xd(), v.yd(), v.zd(), 0)).mulLeftSelf(this);
		v.set(tmp2.x, tmp2.y, tmp2.z);
	}
}