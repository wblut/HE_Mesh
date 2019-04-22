/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_VertexExpand extends HEM_Modifier {
	/**
	 *
	 */
	private WB_ScalarParameter d;

	/**
	 *
	 */
	public HEM_VertexExpand() {
		super();
		d = WB_ScalarParameter.ZERO;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_VertexExpand setDistance(final double d) {
		this.d = d == 0 ? WB_ScalarParameter.ZERO
				: new WB_ConstantScalarParameter(d);
		return this;
	}

	public HEM_VertexExpand setDistance(final WB_ScalarParameter d) {
		this.d = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (d == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = mesh.vItr();
		final FastList<WB_Coord> normals = new FastList<WB_Coord>();
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

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (d == WB_ScalarParameter.ZERO) {
			return selection.getParent();
		}
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = selection.vItr();
		final FastList<WB_Coord> normals = new FastList<WB_Coord>();
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
