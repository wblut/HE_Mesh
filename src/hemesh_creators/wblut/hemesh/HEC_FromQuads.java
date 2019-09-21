/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_Point;
import wblut.geom.WB_Quad;

/**
 * Creates a new mesh from a list of quads. Duplicate vertices are fused.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_FromQuads extends HEC_Creator {
	/** Quads. */
	private WB_Quad[] quads;

	/**
	 * Instantiates a new HEC_FromQuads.
	 *
	 */
	public HEC_FromQuads() {
		super();
		setOverride(true);
	}

	/**
	 * Instantiates a new HEC_FromQuads.
	 *
	 * @param qs
	 *            the qs
	 */
	public HEC_FromQuads(final WB_Quad[] qs) {
		this();
		quads = qs;
	}

	/**
	 * Instantiates a new HEC_FromQuads.
	 *
	 * @param qs
	 * 
	 */
	public HEC_FromQuads(final Collection<WB_Quad> qs) {
		this();
		setQuads(qs);
	}

	/**
	 * Sets the source quads.
	 *
	 * @param qs
	 *            source quads
	 * @return self
	 */
	public HEC_FromQuads setQuads(final WB_Quad[] qs) {
		quads = qs;
		return this;
	}

	/**
	 * Sets the source quads.
	 *
	 * @param qs
	 *            source quads
	 * @return self
	 */
	public HEC_FromQuads setQuads(final Collection<WB_Quad> qs) {
		final int n = qs.size();
		quads = new WB_Quad[n];
		int i = 0;
		for (final WB_Quad quad : qs) {
			quads[i] = quad;
			i++;
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (quads != null) {
			final int nq = quads.length;
			final WB_Point[] vertices = new WB_Point[nq * 4];
			final int[][] faces = new int[nq][4];
			for (int i = 0; i < nq; i++) {
				vertices[4 * i] = quads[i].getP1();
				vertices[4 * i + 1] = quads[i].getP2();
				vertices[4 * i + 2] = quads[i].getP3();
				vertices[4 * i + 3] = quads[i].getP4();
				faces[i][0] = 4 * i;
				faces[i][1] = 4 * i + 1;
				faces[i][2] = 4 * i + 2;
				faces[i][3] = 4 * i + 3;
			}
			final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(vertices).setFaces(faces)
					.setCheckDuplicateVertices(true);
			return ffl.createBase();
		}
		return null;
	}
}
