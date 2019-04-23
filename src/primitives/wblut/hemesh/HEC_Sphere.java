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
public class HEC_Sphere extends HEC_Creator {
	/** Radius. */
	private double rx, ry, rz;
	/** U facets. */
	private int uFacets;
	/** V facets. */
	private int vFacets;

	private double phase;

	/**
	 * Instantiates a new HEC_Sphere.
	 *
	 */
	public HEC_Sphere() {
		super();
		rx = ry = rz = 100;
		uFacets = 12;
		vFacets = 6;
		setVerticalAxis(WB_Vector.Y());
	}

	/**
	 * Set fixed radius.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Sphere setRadius(final double R) {
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
	public HEC_Sphere setRadius(final double rx, final double ry, final double rz) {
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
	public HEC_Sphere setUFacets(final int facets) {
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
	public HEC_Sphere setVFacets(final int facets) {
		vFacets = facets;
		return this;
	}

	public HEC_Sphere setPhase(final double p) {
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
		final double[][] vertices = new double[2 * uFacets + (uFacets + 1) * (vFacets - 1)][3];
		final double[][] uvws = new double[2 * uFacets + (uFacets + 1) * (vFacets - 1)][3];
		for (int u = 0; u < uFacets; u++) {
			vertices[2 * u][0] = 0;
			vertices[2 * u][1] = 1;
			vertices[2 * u][2] = 0;
			uvws[2 * u][0] = (u + 0.5) / uFacets;
			uvws[2 * u][1] = 1;
			uvws[2 * u][2] = 0;
			vertices[2 * u + 1][0] = 0;
			vertices[2 * u + 1][1] = -1;
			vertices[2 * u + 1][2] = 0;
			uvws[2 * u + 1][0] = (u + 0.5) / uFacets;
			uvws[2 * u + 1][1] = 0;
			uvws[2 * u + 1][2] = 0;
		}
		int id = 2 * uFacets;
		for (int v = 1; v < vFacets; v++) {
			final double Rs = Math.sin(v * Math.PI / vFacets);
			final double Rc = Math.cos(v * Math.PI / vFacets);
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
		final int[][] faces = new int[uFacets * vFacets][];
		for (int u = 0; u < uFacets; u++) {
			faces[u] = new int[3];
			faces[u][0] = 2 * u;
			faces[u][1] = index(u + 1, 1);
			faces[u][2] = index(u, 1);
		}
		for (int v = 1; v < vFacets - 1; v++) {
			for (int u = 0; u < uFacets; u++) {
				faces[u + uFacets * v] = new int[4];
				faces[u + uFacets * v][0] = index(u, v);
				faces[u + uFacets * v][1] = index(u + 1, v);
				faces[u + uFacets * v][2] = index(u + 1, v + 1);
				faces[u + uFacets * v][3] = index(u, v + 1);
			}
		}
		for (int u = 0; u < uFacets; u++) {
			faces[u + uFacets * (vFacets - 1)] = new int[3];
			faces[u + uFacets * (vFacets - 1)][0] = index(u, vFacets - 1);
			faces[u + uFacets * (vFacets - 1)][1] = index(u + 1, vFacets - 1);
			faces[u + uFacets * (vFacets - 1)][2] = 2 * u + 1;
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
			return 2 * u;
		}
		if (v == vFacets) {
			return 2 * u + 1;
		}
		return 2 * uFacets + u + (uFacets + 1) * (v - 1);
	}
}
