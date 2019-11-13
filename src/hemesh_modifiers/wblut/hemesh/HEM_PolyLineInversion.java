/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_AABBTree3D;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.hemesh.HE_MeshOp.HE_FaceLineIntersection;

/**
 *
 */
public class HEM_PolyLineInversion extends HEM_Modifier {
	/**
	 *
	 */
	private WB_PolyLine	polyLine;
	/**
	 *
	 */
	private double		r, r2;
	/**
	 *
	 */
	private double		icutoff;
	/**
	 *
	 */
	private boolean		linear;

	/**
	 *
	 */
	public HEM_PolyLineInversion() {
		super();
		icutoff = 0.0001;
		linear = false;
	}

	public HEM_PolyLineInversion(final WB_PolyLine poly, final double r) {
		super();
		polyLine = poly;
		this.r = r;
		r2 = r * r;
		icutoff = 0.0001;
		linear = false;
	}

	/**
	 *
	 * @param poly
	 * @return
	 */
	public HEM_PolyLineInversion setPolyLine(final WB_PolyLine poly) {
		polyLine = poly;
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_PolyLineInversion setRadius(final double r) {
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
	public HEM_PolyLineInversion setCutoff(final double cutoff) {
		icutoff = 1.0 / cutoff;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_PolyLineInversion setLinear(final boolean b) {
		linear = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEM_Modifier#modify(wblut.hemesh.core.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (polyLine == null) {
			return mesh;
		}
		if (r == 0) {
			return mesh;
		}
		WB_AABBTree3D tree = new WB_AABBTree3D(mesh, 10);
		List<HE_FaceLineIntersection> intersections = new FastList<HE_FaceLineIntersection>();
		for (int i = 0; i < polyLine.getNumberSegments(); i++) {
			WB_Segment S = polyLine.getSegment(i);
			intersections.addAll(HE_MeshOp.getIntersection(tree, S));
		}
		for (HE_FaceLineIntersection fi : intersections) {
			if (mesh.contains(fi.face)) {
				mesh.deleteFace(fi.face);
			}
		}
		mesh.removeUnconnectedElements();
		HE_MeshOp.capHalfedges(mesh);
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		WB_Vector d;
		WB_Point surf;
		WB_Point q;
		double ri, rf;
		while (vItr.hasNext()) {
			v = vItr.next();
			q = WB_GeometryOp.getClosestPoint3D(v, polyLine);
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
	 * @see
	 * wblut.hemesh.modifiers.HEM_Modifier#modifySelected(wblut.hemesh.core.
	 * HE_Mesh, wblut.hemesh.core.HE_Selection)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (polyLine == null) {
			return selection.getParent();
		}
		if (r == 0) {
			return selection.getParent();
		}
		WB_AABBTree3D tree = new WB_AABBTree3D(selection.getParent(), 10);
		List<HE_FaceLineIntersection> intersections = new FastList<HE_FaceLineIntersection>();
		for (int i = 0; i < polyLine.getNumberSegments(); i++) {
			WB_Segment S = polyLine.getSegment(i);
			intersections.addAll(HE_MeshOp.getIntersection(tree, S));
		}
		for (HE_FaceLineIntersection fi : intersections) {
			if (selection.contains(fi.face)) {
				if (selection.getParent().contains(fi.face)) {
					selection.getParent().deleteFace(fi.face);
				}
			}
		}
		selection.getParent().removeUnconnectedElements();
		HE_MeshOp.capHalfedges(selection.getParent());
		selection.cleanSelection();
		final Iterator<HE_Vertex> vItr = selection.vItr();
		HE_Vertex v;
		WB_Vector d;
		WB_Point surf;
		WB_Point q;
		double ri, rf;
		while (vItr.hasNext()) {
			v = vItr.next();
			q = WB_GeometryOp.getClosestPoint3D(v, polyLine);
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
