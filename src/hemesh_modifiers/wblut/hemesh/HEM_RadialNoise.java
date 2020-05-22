package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_MTRandom;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_RadialNoise extends HEM_Modifier {
	/**  */
	private WB_ScalarParameter d;
	/**  */
	private final WB_MTRandom rng;

	/**
	 *
	 */
	public HEM_RadialNoise() {
		super();
		setDistance(0);
		rng = new WB_MTRandom();
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_RadialNoise setDistance(final double d) {
		this.d = d == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 *
	 *
	 * @param seed
	 * @return
	 */
	public HEM_RadialNoise setSeed(final long seed) {
		rng.setSeed(seed);
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_RadialNoise setDistance(final WB_ScalarParameter d) {
		this.d = d;
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
		rng.reset();
		if (d == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		WB_Coord n;
		while (vItr.hasNext()) {
			v = vItr.next();
			n = mesh.getVertexNormal(v);
			v.getPosition().addMulSelf(rng.nextDouble() * d.evaluate(v.xd(), v.yd(), v.zd()), n);
		}
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
		rng.reset();
		selection.collectVertices();
		final Iterator<HE_Vertex> vItr = selection.vItr();
		HE_Vertex v;
		WB_Coord n;
		while (vItr.hasNext()) {
			v = vItr.next();
			n = selection.getParent().getVertexNormal(v);
			v.getPosition().addMulSelf(rng.nextDouble() * d.evaluate(v.xd(), v.yd(), v.zd()), n);
		}
		return selection.getParent();
	}
}
