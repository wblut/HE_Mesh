/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;

import wblut.geom.WB_Classification;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 * Chamfer all convex edges.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_ChamferEdges extends HEM_Modifier {
	/** Chamfer distance. */
	private double distance;

	/**
	 * Instantiates a new HEM_ChamferEdges.
	 */
	public HEM_ChamferEdges() {
		super();
		distance = 0;
	}

	/**
	 * Set chamfer distance along edge normals.
	 *
	 * @param d
	 *            distance
	 * @return self
	 */
	public HEM_ChamferEdges setDistance(final double d) {
		distance = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (distance == 0) {
			return mesh;
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (HE_MeshOp
					.getVertexType(e.getVertex()) == WB_Classification.CONVEX
					|| HE_MeshOp.getVertexType(
							e.getEndVertex()) == WB_Classification.CONVEX) {
				final WB_Vector N = new WB_Vector(HE_MeshOp.getEdgeNormal(e));
				final WB_Point O = new WB_Point(N).mulSelf(-distance);
				N.mulSelf(-1);
				O.addSelf(HE_MeshOp.getHalfedgeCenter(e));
				final WB_Plane P = new WB_Plane(O, N);
				cutPlanes.add(P);
			}
		}
		final HEM_MultiSlice msm = new HEM_MultiSlice();
		msm.setPlanes(cutPlanes);
		mesh.modify(msm);
		mesh.renameSelection("caps", "chamfer");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (distance == 0) {
			return selection.getParent();
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
		selection.collectEdgesByFace();
		final Iterator<HE_Halfedge> eItr = selection.getParent().eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (HE_MeshOp
					.getVertexType(e.getVertex()) == WB_Classification.CONVEX
					|| HE_MeshOp.getVertexType(
							e.getEndVertex()) == WB_Classification.CONVEX) {
				final WB_Vector N = new WB_Vector(HE_MeshOp.getEdgeNormal(e));
				final WB_Point O = new WB_Point(N).mulSelf(-distance);
				N.mulSelf(-1);
				O.addSelf(HE_MeshOp.getHalfedgeCenter(e));
				final WB_Plane P = new WB_Plane(O, N);
				cutPlanes.add(P);
			}
		}
		final HEM_MultiSlice msm = new HEM_MultiSlice();
		msm.setPlanes(cutPlanes);
		selection.getParent().modify(msm);
		selection.getParent().renameSelection("caps", "chamfer");
		return selection.getParent();
	}
}