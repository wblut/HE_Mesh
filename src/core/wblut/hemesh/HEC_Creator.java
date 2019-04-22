/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;

import processing.core.PApplet;
import processing.opengl.PGraphics3D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 * Abstract base class for mesh creation. Implementation should return a valid
 * HE_Mesh.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public abstract class HEC_Creator extends HE_Machine {
	/** Calling applet. */
	public PApplet		home;
	/** Center. */
	protected WB_Point	center;
	/** Rotation angle about Z-axis. */
	protected double	zangle;
	/** Z-axis. */
	protected WB_Vector	zaxis;
	/** Override. */
	protected boolean	override;
	protected boolean	override2D;
	/** Use applet model coordinates. */
	protected boolean	toModelview;
	/** Base Z-axis. */
	protected WB_Vector	Z;
	protected boolean	manifoldCheck;
	protected double	scale;

	/**
	 * Constructor.
	 */
	public HEC_Creator() {
		super();
		center = new WB_Point();
		zaxis = new WB_Vector(WB_Vector.Z());
		Z = new WB_Vector(WB_Vector.Z());
		scale = 1.0;
		toModelview = false;
	}

	/**
	 * Set center of mesh.
	 *
	 * @param x
	 *            x-coordinate of center
	 * @param y
	 *            y-coordinate of center
	 * @param z
	 *            z-coordinate of center
	 * @return self
	 */
	public HEC_Creator setCenter(final double x, final double y,
			final double z) {
		center.set(x, y, z);
		return this;
	}

	/**
	 *
	 *
	 * @param s
	 * @return
	 */
	public HEC_Creator setScale(final double s) {
		scale = s;
		return this;
	}

	/**
	 * Set center of mesh.
	 *
	 * @param c
	 *            center
	 * @return self
	 */
	public HEC_Creator setCenter(final WB_Coord c) {
		center.set(c);
		return this;
	}

	/**
	 * Rotation of mesh about local Z-axis.
	 *
	 * @param a
	 *            angle
	 * @return self
	 */
	public HEC_Creator setZAngle(final double a) {
		zangle = a;
		return this;
	}

	/**
	 * Orientation of local Z-axis of mesh.
	 *
	 * @param x
	 *            x-coordinate of axis vector
	 * @param y
	 *            y-coordinate of axis vector
	 * @param z
	 *            z-coordinate of axis vector
	 * @return self
	 */
	public HEC_Creator setZAxis(final double x, final double y,
			final double z) {
		zaxis.set(x, y, z);
		zaxis.normalizeSelf();
		return this;
	}

	/**
	 * Local Z-axis of mesh.
	 *
	 * @param p0x
	 *            x-coordinate of first point on axis
	 * @param p0y
	 *            y-coordinate of first point on axis
	 * @param p0z
	 *            z-coordinate of first point on axis
	 * @param p1x
	 *            x-coordinate of second point on axis
	 * @param p1y
	 *            y-coordinate of second point on axis
	 * @param p1z
	 *            z-coordinate of second point on axis
	 * @return self
	 */
	public HEC_Creator setZAxis(final double p0x, final double p0y,
			final double p0z, final double p1x, final double p1y,
			final double p1z) {
		zaxis.set(p1x - p0x, p1y - p0y, p1z - p0z);
		zaxis.normalizeSelf();
		return this;
	}

	/**
	 * Orientation of local Z-axis of mesh.
	 *
	 * @param p
	 *            axis vector
	 * @return self
	 */
	public HEC_Creator setZAxis(final WB_Coord p) {
		zaxis.set(p);
		zaxis.normalizeSelf();
		return this;
	}

	/**
	 * Local Z-axis of mesh.
	 *
	 * @param p0
	 *            first point on axis
	 * @param p1
	 *            second point on axis
	 * @return self
	 */
	public HEC_Creator setZAxis(final WB_Coord p0, final WB_Coord p1) {
		zaxis.set(WB_Vector.sub(p0, p1));
		zaxis.normalizeSelf();
		return this;
	}

	/**
	 * Use the applet's modelview coordinates.
	 *
	 * @param home
	 *            calling applet, typically "this"
	 * @return self
	 */
	public HEC_Creator setToModelview(final PApplet home) {
		this.home = home;
		toModelview = true;
		return this;
	}

	/**
	 * Use absolute coordinates.
	 *
	 * @return self
	 */
	public HEC_Creator setToWorldview() {
		home = null;
		toModelview = false;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_Creator setManifoldCheck(final boolean b) {
		manifoldCheck = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_Creator setOverride(final boolean b) {
		override = b;
		return this;
	}

	/**
	 * Creates the base.
	 *
	 * @return HE_Mesh
	 */
	protected abstract HE_Mesh createBase();

	/**
	 * Generate a mesh, move to center and orient along axis.
	 *
	 * @return HE_Mesh
	 */
	public final HE_Mesh create() {
		tracker.setStartStatus(this, "Creating base mesh.");
		final HE_Mesh base = createBase();
		tracker.setStopStatus(this, "Base mesh created.");
		tracker.setStartStatus(this, "Transforming base mesh.");
		WB_Coord ctr = HE_MeshOp.getCenter(base);
		if (!override) {
			base.scaleSelf(scale);
			if (zangle != 0) {
				base.rotateAboutAxis2PSelf(zangle, ctr.xd(), ctr.yd(), ctr.zd(),
						ctr.xd(), ctr.yd(), ctr.zd() + 1);
			}
			final WB_Vector tmp = zaxis.cross(Z);
			if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
				base.rotateAboutAxis2PSelf(
						-Math.acos(WB_Math.clamp(zaxis.dot(Z), -1, 1)),
						ctr.xd(), ctr.yd(), ctr.zd(), ctr.xd() + tmp.xd(),
						ctr.yd() + tmp.yd(), ctr.zd() + tmp.zd());
			} else if (zaxis.dot(Z) < -1 + WB_Epsilon.EPSILON) {
				base.scaleSelf(1, 1, -1);
			}
			base.moveToSelf(center);
		}
		float cx, cy, cz;
		HE_Vertex v;
		if (toModelview) {
			if (home.g instanceof PGraphics3D) {
				final Iterator<HE_Vertex> vItr = base.vItr();
				while (vItr.hasNext()) {
					v = vItr.next();
					cx = v.xf();
					cy = v.yf();
					cz = v.zf();
					v.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
							home.modelZ(cx, cy, cz));
				}
			}
		}
		if (manifoldCheck) {
			tracker.setStartStatus(this, "Checking and fixing manifold.");
			HET_Fixer.fixNonManifoldVertices(base);
			tracker.setStopStatus(this, "Manifold checked.");
		}
		tracker.setStopStatus(this, "Base mesh transformed.");
		return base;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Machine#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		mesh.setNoCopy(create());
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Machine#apply(wblut.hemesh.HE_Selection)
	 */
	@Override
	public HE_Mesh apply(final HE_Selection sel) {
		return create();
	}
}
