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
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Chamfer all convex corners.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_ChamferCorners extends HEM_Modifier {
	/** Chamfer distance. */
	private WB_ScalarParameter distance;

	/**
	 * Instantiates a new HEM_ChamferCorners.
	 */
	public HEM_ChamferCorners() {
		super();
	}

	/**
	 * Set chamfer distance along vertex normals.
	 *
	 * @param d
	 *            distance
	 * @return self
	 */
	public HEM_ChamferCorners setDistance(final double d) {
		distance = new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 * Set chamfer distance along vertex normals.
	 *
	 * @param d
	 *            WB_Parameter
	 * @return self
	 */
	public HEM_ChamferCorners setDistance(final WB_ScalarParameter d) {
		distance = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (distance == null) {
			return mesh;
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (HE_MeshOp.getVertexType(v) == WB_Classification.CONVEX) {
				final WB_Vector N = new WB_Vector(HE_MeshOp.getVertexNormal(v));
				final WB_Point O = new WB_Point(N)
						.mulSelf(-distance.evaluate(v.xd(), v.yd(), v.zd()));
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

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (distance == null) {
			return selection.getParent();
		}
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
		selection.collectVertices();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = selection.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (HE_MeshOp.getVertexType(v) == WB_Classification.CONVEX) {
				final WB_Vector N = new WB_Vector(HE_MeshOp.getVertexNormal(v));
				final WB_Point O = new WB_Point(N)
						.mulSelf(-distance.evaluate(v.xd(), v.yd(), v.zd()));
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
