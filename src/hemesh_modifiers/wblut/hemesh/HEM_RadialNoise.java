/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_MTRandom;
import wblut.math.WB_ScalarParameter;

/**
 * Expands or contracts all vertices along the vertex normals.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_RadialNoise extends HEM_Modifier {
	/** Expansion distance. */
	private WB_ScalarParameter	d;
	private final WB_MTRandom	rng;

	/**
	 * Instantiates a new hE m_ noise.
	 */
	public HEM_RadialNoise() {
		super();
		setDistance(0);
		rng = new WB_MTRandom();
	}

	/**
	 * Set distance to move vertices.
	 *
	 * @param d
	 *            distance
	 * @return this
	 */
	public HEM_RadialNoise setDistance(final double d) {
		this.d = d == 0 ? WB_ScalarParameter.ZERO
				: new WB_ConstantScalarParameter(d);
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
	 * Sets the distance.
	 *
	 * @param d
	 *            the d
	 * @return the hE m_ noise
	 */
	public HEM_RadialNoise setDistance(final WB_ScalarParameter d) {
		this.d = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
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
			n = HE_MeshOp.getVertexNormal(v);
			v.getPosition().addMulSelf(
					rng.nextDouble() * d.evaluate(v.xd(), v.yd(), v.zd()), n);
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
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
			n = HE_MeshOp.getVertexNormal(v);
			v.getPosition().addMulSelf(
					rng.nextDouble() * d.evaluate(v.xd(), v.yd(), v.zd()), n);
		}
		return selection.getParent();
	}
}
