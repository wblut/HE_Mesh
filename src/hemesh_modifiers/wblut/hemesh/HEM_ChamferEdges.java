package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;

import wblut.geom.WB_Classification;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HEM_ChamferEdges extends HEM_Modifier {
	/**  */
	private double distance;

	/**
	 *
	 */
	public HEM_ChamferEdges() {
		super();
		distance = 0;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_ChamferEdges setDistance(final double d) {
		distance = d;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (distance == 0) {
			return mesh;
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<>();
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (HE_MeshOp.getVertexType(e.getVertex()) == WB_Classification.CONVEX
					|| HE_MeshOp.getVertexType(e.getEndVertex()) == WB_Classification.CONVEX) {
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

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (distance == 0) {
			return selection.getParent();
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<>();
		selection.collectEdgesByFace();
		final Iterator<HE_Halfedge> eItr = selection.getParent().eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (HE_MeshOp.getVertexType(e.getVertex()) == WB_Classification.CONVEX
					|| HE_MeshOp.getVertexType(e.getEndVertex()) == WB_Classification.CONVEX) {
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