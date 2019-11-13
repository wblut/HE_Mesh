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
import wblut.geom.WB_Point;
import wblut.geom.WB_Triangle;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HEM_TriangleInversion extends HEM_Modifier {

	/**
	 *
	 */
	private WB_Triangle triangle;

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
	public HEM_TriangleInversion() {
		super();
		icutoff = 0.0001;
		linear = false;

	}

	public HEM_TriangleInversion(final WB_Triangle triangle, final double r) {
		super();
		this.triangle = triangle;
		this.r = r;
		r2 = r * r;
		icutoff = 0.0001;
		linear = false;

	}

	/**
	 *
	 * @param triangle
	 * @return
	 */
	public HEM_TriangleInversion setTriangle(final WB_Triangle triangle) {
		this.triangle = triangle;
		return this;
	}

	public HEM_TriangleInversion setTriangle(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		triangle = new WB_Triangle(p1, p2, p3);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_TriangleInversion setRadius(final double r) {
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
	public HEM_TriangleInversion setCutoff(final double cutoff) {
		icutoff = 1.0 / cutoff;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_TriangleInversion setLinear(final boolean b) {
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
		if (triangle == null) {
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
			q = WB_GeometryOp.getClosestPoint3D(v, triangle);
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
		if (triangle == null) {
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
			q = WB_GeometryOp.getClosestPoint3D(v, triangle);
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
