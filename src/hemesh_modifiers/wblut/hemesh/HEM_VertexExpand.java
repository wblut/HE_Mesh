package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordList;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

public class HEM_VertexExpand extends HEM_Modifier {
	private WB_ScalarParameter d;

	public HEM_VertexExpand() {
		super();
		d = WB_ScalarParameter.ZERO;
	}

	public HEM_VertexExpand setDistance(final double d) {
		this.d = d == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(d);
		return this;
	}

	public HEM_VertexExpand setDistance(final WB_ScalarParameter d) {
		this.d = d;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (d == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = mesh.vItr();
		final WB_CoordList normals = new WB_CoordList();
		while (vItr.hasNext()) {
			v = vItr.next();
			normals.add(HE_MeshOp.getVertexNormal(v));
		}
		final Iterator<WB_Coord> vnItr = normals.iterator();
		vItr = mesh.vItr();
		WB_Coord n;
		while (vItr.hasNext()) {
			v = vItr.next();
			n = vnItr.next();
			v.getPosition().addMulSelf(d.evaluate(v.xd(), v.yd(), v.zd()), n);
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (d == WB_ScalarParameter.ZERO) {
			return selection.getParent();
		}
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = selection.vItr();
		final WB_CoordList normals = new WB_CoordList();
		while (vItr.hasNext()) {
			v = vItr.next();
			normals.add(HE_MeshOp.getVertexNormal(v));
		}
		final Iterator<WB_Coord> vnItr = normals.iterator();
		vItr = selection.vItr();
		WB_Coord n;
		while (vItr.hasNext()) {
			v = vItr.next();
			n = vnItr.next();
			v.getPosition().addMulSelf(d.evaluate(v.xd(), v.yd(), v.zd()), n);
		}
		return selection.getParent();
	}
}
