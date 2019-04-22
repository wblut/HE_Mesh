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
 * @author FVH
 *
 */
public abstract class WB_Swizzle {

	public static final WB_Swizzle X = new X();
	public static final WB_Swizzle Y = new Y();
	public static final WB_Swizzle Z = new Z();
	public static final WB_Swizzle XX = new XX();
	public static final WB_Swizzle XY = new XY();
	public static final WB_Swizzle XZ = new XZ();
	public static final WB_Swizzle YX = new YX();
	public static final WB_Swizzle YY = new YY();
	public static final WB_Swizzle YZ = new YZ();
	public static final WB_Swizzle ZX = new ZX();
	public static final WB_Swizzle ZY = new ZY();
	public static final WB_Swizzle ZZ = new ZZ();
	public static final WB_Swizzle XXX = new XXX();
	public static final WB_Swizzle XXY = new XXY();
	public static final WB_Swizzle XXZ = new XXZ();
	public static final WB_Swizzle XYX = new XYX();
	public static final WB_Swizzle XYY = new XYY();
	public static final WB_Swizzle XYZ = new XYZ();
	public static final WB_Swizzle XZX = new XZX();
	public static final WB_Swizzle XZY = new XZY();
	public static final WB_Swizzle XZZ = new XZZ();
	public static final WB_Swizzle YXX = new YXX();
	public static final WB_Swizzle YXY = new YXY();
	public static final WB_Swizzle YXZ = new YXZ();
	public static final WB_Swizzle YYX = new YYX();
	public static final WB_Swizzle YYY = new YYY();
	public static final WB_Swizzle YYZ = new YYZ();
	public static final WB_Swizzle YZX = new YZX();
	public static final WB_Swizzle YZY = new YZY();
	public static final WB_Swizzle YZZ = new YZZ();
	public static final WB_Swizzle ZXX = new ZXX();
	public static final WB_Swizzle ZXY = new ZXY();
	public static final WB_Swizzle ZXZ = new ZXZ();
	public static final WB_Swizzle ZYX = new ZYX();
	public static final WB_Swizzle ZYY = new ZYY();
	public static final WB_Swizzle ZYZ = new ZYZ();
	public static final WB_Swizzle ZZX = new ZZX();
	public static final WB_Swizzle ZZY = new ZZY();
	public static final WB_Swizzle ZZZ = new ZZZ();

	public abstract double xd(WB_Coord p);

	public abstract double yd(WB_Coord p);

	public abstract double zd(WB_Coord p);

	public abstract float xf(WB_Coord p);

	public abstract float yf(WB_Coord p);

	public abstract float zf(WB_Coord p);

	public abstract void swizzleSelf(WB_MutableCoord p);

	private WB_Swizzle() {
	}

	public static class Closest extends WB_Swizzle {

		WB_Swizzle internal = null;

		public Closest(final WB_Coord p) {
			if (Math.abs(p.xd()) > Math.abs(p.yd())) {
				internal = Math.abs(p.xd()) > Math.abs(p.zd()) ? YZ : XY;
			} else {
				internal = Math.abs(p.yd()) > Math.abs(p.zd()) ? XZ : XY;
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#xd(wblut.geom.WB_Coord)
		 */
		@Override
		public double xd(final WB_Coord p) {

			return internal.xd(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#yd(wblut.geom.WB_Coord)
		 */
		@Override
		public double yd(final WB_Coord p) {
			return internal.yd(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#zd(wblut.geom.WB_Coord)
		 */
		@Override
		public double zd(final WB_Coord p) {
			return internal.zd(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#xf(wblut.geom.WB_Coord)
		 */
		@Override
		public float xf(final WB_Coord p) {
			return internal.xf(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#yf(wblut.geom.WB_Coord)
		 */
		@Override
		public float yf(final WB_Coord p) {
			return internal.yf(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#zf(wblut.geom.WB_Coord)
		 */
		@Override
		public float zf(final WB_Coord p) {
			return internal.zf(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_Swizzle#swizzle(wblut.geom.WB_MutableCoord)
		 */
		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			internal.swizzleSelf(p);

		}

	}

	private static class XXX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.xd(), p.xd());
		}

	}

	private static class XXY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.xd(), p.yd());
		}

	}

	private static class XXZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.xd(), p.zd());
		}

	}

	private static class XYX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.yd(), p.xd());
		}

	}

	private static class XYY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.yd(), p.yd());
		}

	}

	private static class XYZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {

		}

	}

	private static class XZX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.zd(), p.xd());
		}

	}

	private static class XZY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.zd(), p.yd());

		}

	}

	private static class XZZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.zd(), p.zd());
		}

	}

	private static class YXX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.xd(), p.xd());
		}

	}

	private static class YXY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.xd(), p.yd());
		}

	}

	private static class YXZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.xd(), p.zd());
		}

	}

	private static class YYX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.yd(), p.xd());
		}

	}

	private static class YYY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.yd(), p.yd());
		}

	}

	private static class YYZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.yd(), p.zd());
		}

	}

	private static class YZX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.zd(), p.xd());
		}

	}

	private static class YZY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.zd(), p.yd());

		}

	}

	private static class YZZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.zd(), p.zd());
		}

	}

	private static class ZXX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.xd(), p.xd());
		}

	}

	private static class ZXY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.xd(), p.yd());
		}

	}

	private static class ZXZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.xd(), p.zd());
		}

	}

	private static class ZYX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.yd(), p.xd());
		}

	}

	private static class ZYY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.yd(), p.yd());
		}

	}

	private static class ZYZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.yd(), p.zd());
		}

	}

	private static class ZZX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.zd(), p.xd());
		}

	}

	private static class ZZY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.zd(), p.yd());

		}

	}

	private static class ZZZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.zd(), p.zd());
		}

	}

	private static class XX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.xd(), 0);
		}

	}

	private static class XY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.yd(), 0);
		}

	}

	private static class XZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), p.zd(), 0);
		}

	}

	private static class YX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.xd(), 0);
		}

	}

	private static class YY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.yd(), 0);
		}

	}

	private static class YZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), p.zd(), 0);
		}

	}

	private static class ZX extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.xd(), 0);
		}

	}

	private static class ZY extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.yd(), 0);
		}

	}

	private static class ZZ extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), p.zd(), 0);
		}

	}

	private static class X extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.xd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return 0;
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.xf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return 0;
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.xd(), 0, 0);
		}

	}

	private static class Y extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.yd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return 0;
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.yf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return 0;
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.yd(), 0, 0);
		}

	}

	private static class Z extends WB_Swizzle {

		@Override
		public double xd(final WB_Coord p) {
			return p.zd();
		}

		@Override
		public double yd(final WB_Coord p) {
			return 0;
		}

		@Override
		public double zd(final WB_Coord p) {
			return 0;
		}

		@Override
		public float xf(final WB_Coord p) {
			return p.zf();
		}

		@Override
		public float yf(final WB_Coord p) {
			return 0;
		}

		@Override
		public float zf(final WB_Coord p) {
			return 0.f;
		}

		@Override
		public void swizzleSelf(final WB_MutableCoord p) {
			p.set(p.zd(), 0, 0);
		}

	}

}
