package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;

import wblut.geom.WB_Classification;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_ChamferCorners extends HEM_Modifier {
	/**  */
	private WB_ScalarParameter distance;

	/**
	 *
	 */
	public HEM_ChamferCorners() {
		super();
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_ChamferCorners setDistance(final double d) {
		distance = new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_ChamferCorners setDistance(final WB_ScalarParameter d) {
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
		if (distance == null) {
			return mesh;
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<>();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (HE_MeshOp.getVertexType(v) == WB_Classification.CONVEX) {
				final WB_Vector N = new WB_Vector(mesh.getVertexNormal(v));
				final WB_Point O = new WB_Point(N).mulSelf(-distance.evaluate(v.xd(), v.yd(), v.zd()));
				N.mulSelf(-1);
				O.addSelf(v);
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
		if (distance == null) {
			return selection.getParent();
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<>();
		selection.collectVertices();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = selection.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (HE_MeshOp.getVertexType(v) == WB_Classification.CONVEX) {
				final WB_Vector N = new WB_Vector(selection.getParent().getVertexNormal(v));
				final WB_Point O = new WB_Point(N).mulSelf(-distance.evaluate(v.xd(), v.yd(), v.zd()));
				N.mulSelf(-1);
				O.addSelf(v);
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
