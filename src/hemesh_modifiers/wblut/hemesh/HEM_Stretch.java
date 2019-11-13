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

import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Line;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 * Stretch and compress a mesh. Determined by a ground plane, a stretch factor
 * and a compression factor. Most commonly, the ground plane normal is the
 * stretch direction.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_Stretch extends HEM_Modifier {
	/** Ground plane. */
	private WB_Plane groundPlane;
	/** Stretch direction. */
	private WB_Vector stretchDirection;
	/** Stretch factor. */
	private double stretchFactor;
	/** Compression factor. */
	private double compressionFactor;
	/** Modify only positive side of ground plane?. */
	private boolean posOnly;

	/**
	 * Instantiates a new HEM_Stretch.
	 */
	public HEM_Stretch() {
		super();
	}

	/**
	 * Set ground plane.
	 *
	 * @param P
	 *            ground plane
	 * @return self
	 */
	public HEM_Stretch setGroundPlane(final WB_Plane P) {
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
	 * @return the hE m_ stretch
	 */
	public HEM_Stretch setGroundPlane(final double ox, final double oy, final double oz, final double nx,
			final double ny, final double nz) {
		groundPlane = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	/**
	 * Set stretch factor along stretch direction.
	 *
	 * @param f
	 *            the f
	 * @return self
	 */
	public HEM_Stretch setStretchFactor(final double f) {
		stretchFactor = f;
		compressionFactor = Math.sqrt(f);
		return this;
	}

	/**
	 * Set compression factor perpendicular to stretch direction.
	 *
	 * @param f
	 *            the f
	 * @return self
	 */
	public HEM_Stretch setCompressionFactor(final double f) {
		if (f != 0) {
			compressionFactor = f;
		}
		return this;
	}

	/**
	 * Positive only? Only apply modifier to positive side of ground plane.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEM_Stretch setPosOnly(final boolean b) {
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
		if (groundPlane != null && stretchDirection == null) {
			stretchDirection = new WB_Vector(groundPlane.getNormal());
		}
		if (groundPlane != null && stretchFactor != 0 && compressionFactor != 0) {
			final WB_Line L = new WB_Line(groundPlane.getOrigin(), stretchDirection);
			WB_Point p;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			HE_Vertex v;
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > WB_Epsilon.EPSILON) {
					p = WB_GeometryOp.getClosestPoint3D(v, groundPlane);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(stretchFactor);
					v.getPosition().addSelf(p);
					p = WB_GeometryOp.getClosestPoint3D(v, L);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(1 / compressionFactor);
					v.getPosition().addSelf(p);
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
		if (groundPlane != null && stretchDirection == null) {
			stretchDirection = new WB_Vector(groundPlane.getNormal());
		}
		if (groundPlane != null && (stretchFactor != 0 || compressionFactor != 0)) {
			final WB_Line L = new WB_Line(groundPlane.getOrigin(), stretchDirection);
			WB_Point p;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			HE_Vertex v;
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > WB_Epsilon.EPSILON) {
					p = WB_GeometryOp.getClosestPoint3D(v, groundPlane);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(stretchFactor);
					v.getPosition().addSelf(p);
					p = WB_GeometryOp.getClosestPoint3D(v, L);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(1 / compressionFactor);
					v.getPosition().addSelf(p);
				}
			}
		}
		return selection.getParent();
	}
}
