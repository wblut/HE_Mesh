/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Vector;

/**
 * Sphere.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Hemisphere extends HEC_Creator {
	/** Radius. */
	private double rx, ry, rz;
	/** U facets. */
	private int uFacets;
	/** V facets. */
	private int vFacets;

	private boolean cap;

	private double phase;

	/**
	 * Instantiates a new HEC_Sphere.
	 *
	 */
	public HEC_Hemisphere() {
		super();
		rx = ry = rz = 100;
		uFacets = 12;
		vFacets = 6;
		cap = false;
		setVerticalAxis(WB_Vector.Y());
	}

	/**
	 * Set fixed radius.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Hemisphere setRadius(final double R) {
		rx = R;
		ry = R;
		rz = R;
		return this;
	}

	/**
	 *
	 *
	 * @param rx
	 * @param ry
	 * @param rz
	 * @return
	 */
	public HEC_Hemisphere setRadius(final double rx, final double ry, final double rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		return this;
	}

	/**
	 * Set number of faces along equator.
	 *
	 * @param facets
	 *            number of faces
	 * @return self
	 */
	public HEC_Hemisphere setUFacets(final int facets) {
		uFacets = facets;
		return this;
	}

	/**
	 * Set number of facets along meridian.
	 *
	 * @param facets
	 *            number of faces
	 * @return self
	 */
	public HEC_Hemisphere setVFacets(final int facets) {
		vFacets = facets;
		return this;
	}

	/**
	 *
	 *
	 * @param cap
	 * @return
	 */
	public HEC_Hemisphere setCap(final boolean cap) {
		this.cap = cap;
		return this;
	}

	public HEC_Hemisphere setPhase(final double p) {
		phase = p;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final double[][] vertices = new double[(uFacets + 1) * (vFacets + 1) - 1 + (cap ? uFacets : 0)][3];
		final double[][] uvws = new double[(uFacets + 1) * (vFacets + 1) - 1 + (cap ? uFacets : 0)][3];

		int id = 0;
		for (int u = 0; u < uFacets; u++) {
			vertices[id][0] = 0;
			vertices[id][1] = 1.0;
			vertices[id][2] = 0;
			uvws[id][0] = 0.5;
			uvws[id][1] = 1;
			uvws[id][2] = 0;
			id++;
		}

		for (int v = 1; v < vFacets + 1; v++) {
			final double Rs = Math.sin(v * 0.5 * Math.PI / vFacets);
			final double Rc = Math.cos(v * 0.5 * Math.PI / vFacets);
			for (int u = 0; u < uFacets + 1; u++) {
				vertices[id][0] = Rs * Math.cos(2 * u * Math.PI / uFacets + phase);
				vertices[id][1] = Rc;
				vertices[id][2] = Rs * Math.sin(2 * u * Math.PI / uFacets + phase);
				uvws[id][0] = u * 1.0 / uFacets;
				uvws[id][1] = 1 - v * 1.0 / vFacets;
				uvws[id][2] = 0;
				id++;
			}
		}

		if (cap) {
			for (int u = 0; u < uFacets; u++) {
				vertices[id][0] = 0;
				vertices[id][1] = 0;
				vertices[id][2] = 0;
				uvws[id][0] = 0.5;
				uvws[id][1] = 1;
				uvws[id][2] = 0;
				id++;
			}

		}

		final int[][] faces = new int[uFacets * vFacets + (cap ? uFacets : 0)][];
		id = 0;
		for (int u = 0; u < uFacets; u++) {

			faces[id] = new int[3];
			faces[id][0] = u;
			faces[id][1] = index(u + 1, 1);
			faces[id][2] = index(u, 1);
			id++;
		}
		for (int v = 1; v < vFacets; v++) {
			for (int u = 0; u < uFacets; u++) {

				faces[id] = new int[4];
				faces[id][0] = index(u, v);
				faces[id][1] = index(u + 1, v);
				faces[id][2] = index(u + 1, v + 1);
				faces[id][3] = index(u, v + 1);
				id++;
			}
		}

		if (cap) {
			for (int u = 0; u < uFacets; u++) {

				faces[id] = new int[3];
				faces[id][0] = uFacets + vFacets * (uFacets + 1) + u;
				faces[id][1] = index(u, vFacets);
				faces[id][2] = index(u + 1, vFacets);
				id++;
			}
		}

		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setVertexUVW(uvws);
		HE_Mesh mesh = fl.createBase();
		mesh.scaleSelf(rx, ry, rz);
		return mesh;
	}

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @return
	 */
	private int index(final int u, final int v) {
		if (v == 0) {
			return u;
		}

		return uFacets + u + (uFacets + 1) * (v - 1);
	}
}
