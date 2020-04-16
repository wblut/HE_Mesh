package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MeanValueCoordinates;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

public class HEM_NormalExpand extends HEM_Modifier {
	public HEM_NormalExpand() {
		super();
		setDistance(WB_ScalarParameter.ZERO);
	}

	protected WB_ScalarParameter getDistance() {
		return (WB_ScalarParameter) parameters.get("d", WB_ScalarParameter.ZERO);
	}

	protected int getSubSteps() {
		return parameters.get("substeps", 1);
	}

	public HEM_NormalExpand setDistance(final double d) {
		parameters.set("d", d == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(d));
		return this;
	}

	public HEM_NormalExpand setDistance(final WB_ScalarParameter d) {
		parameters.set("d", d);
		return this;
	}

	public HEM_NormalExpand setSubSteps(final int n) {
		parameters.set("substeps", n);
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final WB_ScalarParameter d = getDistance();
		if (d == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		final int[] triangles = mesh.getTriangles();
		final WB_Coord[] vertices = mesh.get().getVerticesAsArray();
		final WB_Coord[] values = mesh.getVertexNormals();
		HE_Vertex v;
		WB_Vector dv;
		final int subSteps = getSubSteps();
		double ld;
		for (int i = 0; i < subSteps; i++) {
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				dv = new WB_Vector(WB_MeanValueCoordinates.getValue(v, vertices, values, triangles));
				ld = dv.normalizeSelf();
				if (ld < 1.0) {
					dv.mulSelf(ld);
				}
				v.getPosition().addMulSelf(d.evaluate(v.xd(), v.yd(), v.zd()) / subSteps, dv);
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final WB_ScalarParameter d = getDistance();
		if (d == WB_ScalarParameter.ZERO) {
			return selection.getParent();
		}
		final int[] triangles = selection.getParent().getTriangles();
		final WB_Coord[] vertices = selection.getParent().get().getVerticesAsArray();
		final WB_Coord[] values = selection.getParent().getVertexNormals();
		HE_Vertex v;
		WB_Vector dv;
		final int subSteps = getSubSteps();
		double ld;
		for (int i = 0; i < subSteps; i++) {
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				dv = new WB_Vector(WB_MeanValueCoordinates.getValue(v, vertices, values, triangles));
				ld = dv.normalizeSelf();
				if (ld < 1.0) {
					dv.mulSelf(ld);
				}
				v.getPosition().addMulSelf(d.evaluate(v.xd(), v.yd(), v.zd()) / subSteps, dv);
			}
		}
		return selection.getParent();
	}
}
