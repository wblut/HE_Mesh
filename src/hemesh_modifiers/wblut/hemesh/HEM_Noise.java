package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_RandomOnSphere;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

public class HEM_Noise extends HEM_Modifier {
	private WB_ScalarParameter d;
	private final WB_RandomOnSphere rs;

	public HEM_Noise() {
		super();
		setDistance(0);
		rs = new WB_RandomOnSphere();
	}

	public HEM_Noise setDistance(final double d) {
		this.d = d == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(d);
		return this;
	}

	public HEM_Noise setSeed(final long seed) {
		rs.setSeed(seed);
		return this;
	}

	public HEM_Noise setDistance(final WB_ScalarParameter d) {
		this.d = d;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		rs.reset();
		if (d == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		WB_Vector n;
		while (vItr.hasNext()) {
			v = vItr.next();
			n = rs.nextVector();
			v.getPosition().addSelf(n.mulSelf(d.evaluate(v.xd(), v.yd(), v.zd())));
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		rs.reset();
		selection.collectVertices();
		final Iterator<HE_Vertex> vItr = selection.vItr();
		HE_Vertex v;
		WB_Vector n;
		while (vItr.hasNext()) {
			v = vItr.next();
			n = rs.nextVector();
			v.getPosition().addSelf(n.mulSelf(d.evaluate(v.xd(), v.yd(), v.zd())));
		}
		return selection.getParent();
	}
}
