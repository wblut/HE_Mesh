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
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HEM_SphericalInversion extends HEM_Modifier {

	/**
	 *
	 */
	private WB_Point center;

	/**
	 *
	 */
	private double r;

	/**
	 *
	 */
	private double r2;

	/**
	 *
	 */
	private double icutoff;

	/**
	 *
	 */
	private boolean linear;

	/**
	 *
	 */
	public HEM_SphericalInversion() {
		super();
		center = new WB_Point(0, 0, 0);
		icutoff = 0.0001;
		linear = false;
	}

	public HEM_SphericalInversion(final double x, final double y, final double z, final double r) {
		super();
		center = new WB_Point(x, y, z);
		this.r = r;
		r2 = r * r;
		icutoff = 0.0001;
		linear = false;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEM_SphericalInversion setCenter(final WB_Coord c) {
		center = new WB_Point(c);
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public HEM_SphericalInversion setCenter(final double x, final double y, final double z) {
		center = new WB_Point(x, y, z);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_SphericalInversion setRadius(final double r) {
		this.r = r;
		r2 = r * r;
		return this;
	}

	/**
	 *
	 *
	 * @param cutoff
	 * @return
	 */
	public HEM_SphericalInversion setCutoff(final double cutoff) {
		icutoff = 1.0 / cutoff;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_SphericalInversion setLinear(final boolean b) {
		linear = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEM_Modifier#modify(wblut.hemesh.core.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (center == null) {
			return mesh;
		}
		if (r == 0) {
			return mesh;
		}
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		WB_Vector d;
		WB_Point surf;
		double ri, rf;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (linear) {
				d = WB_Vector.subToVector3D(v, center);
				d.normalizeSelf();
				surf = new WB_Point(center).addMulSelf(r, d);
				d = surf.subToVector3D(v).mulSelf(2);
				v.getPosition().addSelf(d);
			} else {
				d = WB_Vector.subToVector3D(v, center);
				ri = d.normalizeSelf();
				rf = r2 * Math.max(icutoff, 1.0 / ri);
				v.set(center);
				v.getPosition().addMulSelf(rf, d);
			}
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEM_Modifier#modifySelected(wblut.hemesh.core.
	 * HE_Mesh, wblut.hemesh.core.HE_Selection)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (center == null) {
			return selection.getParent();
		}
		if (r == 0) {
			return selection.getParent();
		}
		final Iterator<HE_Vertex> vItr = selection.vItr();
		HE_Vertex v;
		WB_Vector d;
		WB_Point surf;
		double ri, rf;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (linear) {
				d = WB_Vector.subToVector3D(v, center);
				d.normalizeSelf();
				surf = new WB_Point(center).addMulSelf(r, d);
				d = WB_Vector.subToVector3D(v, surf);
				v.getPosition().addSelf(d);
			} else {
				d = WB_Vector.subToVector3D(v, center);
				ri = d.getLength();
				d.normalizeSelf();
				rf = r2 * Math.max(icutoff, 1.0 / ri);
				v.set(center);
				v.getPosition().addMulSelf(rf, d);
			}
		}
		return selection.getParent();
	}
}
