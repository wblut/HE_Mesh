/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_RandomOnSphere;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Expands or contracts all vertices along the vertex normals.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_Noise extends HEM_Modifier {
	/** Expansion distance. */
	private WB_ScalarParameter d;
	private final WB_RandomOnSphere rs;

	/**
	 * Instantiates a new hE m_ noise.
	 */
	public HEM_Noise() {
		super();
		setDistance(0);
		rs = new WB_RandomOnSphere();
	}

	/**
	 * Set distance to move vertices.
	 *
	 * @param d
	 *            distance
	 * @return this
	 */
	public HEM_Noise setDistance(final double d) {
		this.d = d == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 *
	 *
	 * @param seed
	 * @return
	 */
	public HEM_Noise setSeed(final long seed) {
		rs.setSeed(seed);
		return this;
	}

	/**
	 * Sets the distance.
	 *
	 * @param d
	 *            the d
	 * @return the hE m_ noise
	 */
	public HEM_Noise setDistance(final WB_ScalarParameter d) {
		this.d = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
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

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
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
