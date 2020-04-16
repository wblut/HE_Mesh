package wblut.hemesh;

import wblut.geom.WB_Vector;

public class HEC_Ellipsoid extends HEC_Creator {
	private double rx, ry, rz;
	private int uFacets;
	private int vFacets;

	public HEC_Ellipsoid() {
		super();
		rx = ry = rz = 100;
		uFacets = 12;
		vFacets = 6;
		setCreationAxis(WB_Vector.Y());
	}

	public HEC_Ellipsoid setRadius(final double rx, final double ry, final double rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		return this;
	}

	public HEC_Ellipsoid setUFacets(final int facets) {
		uFacets = facets;
		return this;
	}

	public HEC_Ellipsoid setVFacets(final int facets) {
		vFacets = facets;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		final double[][] vertices = new double[2 + uFacets * (vFacets - 1)][3];
		vertices[0][0] = 0;
		vertices[0][1] = ry;
		vertices[0][2] = 0;
		vertices[1][0] = 0;
		vertices[1][1] = -ry;
		vertices[1][2] = 0;
		int id = 2;
		for (int v = 1; v < vFacets; v++) {
			final double Rs = Math.sin(v * Math.PI / vFacets);
			final double Rc = Math.cos(v * Math.PI / vFacets);
			for (int u = 0; u < uFacets; u++) {
				vertices[id][0] = rx * Rs * Math.cos(2 * u * Math.PI / uFacets);
				vertices[id][1] = ry * Rc;
				vertices[id][2] = rz * Rs * Math.sin(2 * u * Math.PI / uFacets);
				id++;
			}
		}
		final int[][] faces = new int[uFacets * vFacets][];
		for (int u = 0; u < uFacets; u++) {
			faces[u] = new int[3];
			faces[u][0] = index(u, 0);
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
			faces[u + uFacets * (vFacets - 1)][2] = index(u + 1, vFacets);
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setCheckDuplicateVertices(false).setCheckNormals(false);
		return fl.createBase();
	}

	private int index(final int u, final int v) {
		if (v == 0) {
			return 0;
		}
		if (v == vFacets) {
			return 1;
		}
		if (u == uFacets) {
			return index(0, v);
		}
		return 2 + u + uFacets * (v - 1);
	}
}
