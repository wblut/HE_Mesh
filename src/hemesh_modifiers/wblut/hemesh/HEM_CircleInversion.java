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

import wblut.geom.WB_Circle;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HEM_CircleInversion extends HEM_Modifier {

	/**
	 *
	 */
	private WB_Circle circle;

	/**
	 *
	 */
	private double r, r2;

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
	public HEM_CircleInversion() {
		super();
		icutoff = 0.0001;
		linear = false;

	}

	public HEM_CircleInversion(final WB_Circle circle, final double r) {
		super();
		this.circle = circle;
		this.r = r;
		r2 = r * r;
		icutoff = 0.0001;
		linear = false;

	}

	/**
	 *
	 * @param circle
	 * @return
	 */
	public HEM_CircleInversion setCircle(final WB_Circle circle) {
		this.circle = circle;
		return this;
	}

	public HEM_CircleInversion setCircle(final WB_Coord c, final double radius) {
		circle = new WB_Circle(c, radius);
		return this;
	}

	public HEM_CircleInversion setCircle(final WB_Coord c, final WB_Coord n, final double radius) {
		circle = new WB_Circle(c, n, radius);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_CircleInversion setRadius(final double r) {
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
	public HEM_CircleInversion setCutoff(final double cutoff) {
		icutoff = 1.0 / cutoff;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_CircleInversion setLinear(final boolean b) {
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
		if (circle == null) {
			return mesh;
		}
		if (r == 0) {
			return mesh;
		}

		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		WB_Vector d;
		WB_Point surf;
		WB_Point q;
		double ri, rf;
		while (vItr.hasNext()) {
			v = vItr.next();
			q = WB_GeometryOp.getClosestPoint3D(v, circle);
			if (linear) {

				d = WB_Vector.subToVector3D(v, q);
				d.normalizeSelf();
				surf = q.addMulSelf(r, d);
				d = surf.subToVector3D(v).mulSelf(2);
				v.getPosition().addSelf(d);
			} else {
				d = WB_Vector.subToVector3D(v, q);
				ri = d.normalizeSelf();
				rf = r2 * Math.max(icutoff, 1.0 / ri);
				v.set(q);
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
		if (circle == null) {
			return selection.getParent();
		}
		if (r == 0) {
			return selection.getParent();
		}

		final Iterator<HE_Vertex> vItr = selection.vItr();
		HE_Vertex v;
		WB_Vector d;
		WB_Point surf;
		WB_Point q;
		double ri, rf;
		while (vItr.hasNext()) {
			v = vItr.next();
			q = WB_GeometryOp.getClosestPoint3D(v, circle);
			if (linear) {

				d = WB_Vector.subToVector3D(v, q);
				d.normalizeSelf();
				surf = q.addMulSelf(r, d);
				d = surf.subToVector3D(v).mulSelf(2);
				v.getPosition().addSelf(d);
			} else {
				d = WB_Vector.subToVector3D(v, q);
				ri = d.normalizeSelf();
				rf = r2 * Math.max(icutoff, 1.0 / ri);
				v.set(q);
				v.getPosition().addMulSelf(rf, d);
			}
		}
		return selection.getParent();
	}
}
