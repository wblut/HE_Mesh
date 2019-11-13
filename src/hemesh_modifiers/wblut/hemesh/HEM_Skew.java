/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Vector;

/**
 * Skew a mesh. Determined by a ground plane, a skew direction and a skew
 * factor.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_Skew extends HEM_Modifier {
	/** Ground plane. */
	private WB_Plane groundPlane;
	/** Skew direction. */
	private WB_Vector skewDirection;
	/** Skew factor. */
	private double skewFactor;
	/** Only modify positive side of ground plane. */
	private boolean posOnly;

	/**
	 * Instantiates a new HEM_Skew.
	 */
	public HEM_Skew() {
		super();
	}

	/**
	 * Set ground plane.
	 *
	 * @param P
	 *            ground plane
	 * @return self
	 */
	public HEM_Skew setGroundPlane(final WB_Plane P) {
		groundPlane = P;
		return this;
	}

	/**
	 * Sets the ground plane.
	 *
	 * @param ox
	 *            the ox
	 * @param oy
	 *            the oy
	 * @param oz
	 *            the oz
	 * @param nx
	 *            the nx
	 * @param ny
	 *            the ny
	 * @param nz
	 *            the nz
	 * @return the hE m_ skew
	 */
	public HEM_Skew setGroundPlane(final double ox, final double oy, final double oz, final double nx, final double ny,
			final double nz) {
		groundPlane = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	/**
	 * Set skew direction.
	 *
	 * @param p
	 *            direction
	 * @return self
	 */
	public HEM_Skew setSkewDirection(final WB_Coord p) {
		skewDirection = new WB_Vector(p);
		skewDirection.normalizeSelf();
		return this;
	}

	/**
	 * Sets the skew direction.
	 *
	 * @param vx
	 *            the vx
	 * @param vy
	 *            the vy
	 * @param vz
	 *            the vz
	 * @return the hE m_ skew
	 */
	public HEM_Skew setSkewDirection(final double vx, final double vy, final double vz) {
		skewDirection = new WB_Vector(vx, vy, vz);
		skewDirection.normalizeSelf();
		return this;
	}

	/**
	 * Set skew factor, ratio of skew distance to distance to ground plane.
	 *
	 * @param f
	 *            direction
	 * @return self
	 */
	public HEM_Skew setSkewFactor(final double f) {
		skewFactor = f;
		return this;
	}

	/**
	 * Positive only? Only apply modifier to positive side of ground plane.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEM_Skew setPosOnly(final boolean b) {
		posOnly = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (groundPlane != null && skewDirection != null && skewFactor != 0) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > 0) {
					v.getPosition().addSelf(skewDirection.mul(d * skewFactor));
				}
			}
		}

		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (groundPlane != null && skewDirection != null && skewFactor != 0) {
			selection.collectVertices();
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > 0) {
					v.getPosition().addSelf(skewDirection.mul(d * skewFactor));
				}
			}
		}

		return selection.getParent();
	}
}
