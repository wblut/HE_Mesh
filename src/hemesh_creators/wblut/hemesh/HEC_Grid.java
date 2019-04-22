/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_Grid extends HEC_Creator {
	/**
	 *
	 */
	private int U;
	/**
	 *
	 */
	private int V;
	/**
	 *
	 */
	private double uSize;
	/**
	 *
	 */
	private double vSize;
	/**
	 *
	 */
	private double[][] values;
	/**
	 *
	 */
	private double WScale;
	private double minWValue, maxWValue;

	/**
	 *
	 */
	public HEC_Grid() {
		super();
		uSize = 100;
		vSize = 100;
		U = 1;
		V = 1;
		WScale = 1;
		maxWValue = Double.NEGATIVE_INFINITY;
		minWValue = Double.POSITIVE_INFINITY;
	}

	/**
	 *
	 *
	 * @param U
	 * @param V
	 * @param uSize
	 * @param vSize
	 */
	public HEC_Grid(final int U, final int V, final double uSize, final double vSize) {
		this();
		this.uSize = uSize;
		this.vSize = vSize;
		this.U = U;
		this.V = V;
	}

	/**
	 *
	 *
	 * @param U
	 * @return
	 */
	public HEC_Grid setU(final int U) {
		this.U = Math.max(1, U);
		return this;
	}

	/**
	 *
	 *
	 * @param V
	 * @return
	 */
	public HEC_Grid setV(final int V) {
		this.V = Math.max(1, V);
		return this;
	}

	/**
	 *
	 *
	 * @param uSize
	 * @return
	 */
	public HEC_Grid setUSize(final double uSize) {
		this.uSize = uSize;
		return this;
	}

	/**
	 *
	 *
	 * @param vSize
	 * @return
	 */
	public HEC_Grid setVSize(final double vSize) {
		this.vSize = vSize;
		return this;
	}

	/**
	 *
	 *
	 * @param values
	 * @return
	 */
	public HEC_Grid setWValues(final double[][] values) {
		this.values = new double[U + 1][V + 1];
		for (int i = 0; i < U + 1; i++) {
			for (int j = 0; j < V + 1; j++) {
				this.values[i][j] = values[i][j];
				maxWValue = Math.max(maxWValue, values[i][j]);
				minWValue = Math.min(minWValue, values[i][j]);
			}
		}
		return this;
	}

	/**
	 *
	 *
	 * @param height
	 * @param ui
	 * @param vi
	 * @param du
	 * @param dv
	 * @return
	 */
	public HEC_Grid setWValues(final WB_ScalarParameter height, final double ui, final double vi, final double du,
			final double dv) {
		values = new double[U + 1][V + 1];
		for (int i = 0; i < U + 1; i++) {
			for (int j = 0; j < V + 1; j++) {
				values[i][j] = height.evaluate(ui + i * du, vi + j * dv);
				maxWValue = Math.max(maxWValue, values[i][j]);
				minWValue = Math.min(minWValue, values[i][j]);
			}
		}
		return this;
	}

	/**
	 *
	 *
	 * @param values
	 * @return
	 */
	public HEC_Grid setWValues(final float[][] values) {
		this.values = new double[U + 1][V + 1];
		for (int i = 0; i < U + 1; i++) {
			for (int j = 0; j < V + 1; j++) {
				this.values[i][j] = values[i][j];
				maxWValue = Math.max(maxWValue, values[i][j]);
				minWValue = Math.min(minWValue, values[i][j]);
			}
		}
		return this;
	}

	/**
	 *
	 *
	 * @param values
	 * @return
	 */
	public HEC_Grid setWValues(final float[] values) {
		int id = 0;
		this.values = new double[U + 1][V + 1];
		for (int j = 0; j < V + 1; j++) {
			for (int i = 0; i < U + 1; i++) {
				this.values[i][j] = values[id];
				maxWValue = Math.max(maxWValue, values[id]);
				minWValue = Math.min(minWValue, values[id]);
				id++;
			}
		}
		return this;
	}

	/**
	 *
	 *
	 * @param values
	 * @return
	 */
	public HEC_Grid setWValues(final double[] values) {
		int id = 0;
		this.values = new double[U + 1][V + 1];
		for (int j = 0; j < V + 1; j++) {
			for (int i = 0; i < U + 1; i++) {
				this.values[i][j] = values[id];
				maxWValue = Math.max(maxWValue, values[id]);
				minWValue = Math.min(minWValue, values[id]);
				id++;
			}
		}
		return this;
	}

	/**
	 *
	 *
	 * @param value
	 * @return
	 */
	public HEC_Grid setWScale(final double value) {
		WScale = value;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.creators.HEB_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (uSize == 0 || vSize == 0) {
			return new HE_Mesh();
		}
		final double dU = uSize / U;
		final double dV = vSize / V;
		final double ndU = 1.0 / U;
		final double ndV = 1.0 / V;
		final double[][] points = new double[(U + 1) * (V + 1)][3];
		final double[][] uvw = new double[(U + 1) * (V + 1)][3];
		final int[][] faces = new int[U * V][4];
		int index = 0;
		if (values == null) {
			for (int j = 0; j < V + 1; j++) {
				for (int i = 0; i < U + 1; i++) {
					points[index][0] = i * dU;
					points[index][1] = j * dV;
					points[index][2] = 0;
					uvw[index][0] = i * ndU;
					uvw[index][1] = j * ndV;
					uvw[index][2] = 0;
					index++;
				}
			}
		} else {
			for (int j = 0; j < V + 1; j++) {
				for (int i = 0; i < U + 1; i++) {
					points[index][0] = i * dU;
					points[index][1] = j * dV;
					points[index][2] = WScale * values[i][j];
					uvw[index][0] = i * ndU;
					uvw[index][1] = j * ndV;
					uvw[index][2] = (values[i][j] - minWValue) / maxWValue;
					if (Double.isNaN(uvw[index][2])) {
						uvw[index][2] = 0.0;
					}
					index++;
				}
			}
		}
		index = 0;
		for (int j = 0; j < V; j++) {
			for (int i = 0; i < U; i++) {
				faces[index][0] = i + (U + 1) * j;
				faces[index][1] = i + 1 + (U + 1) * j;
				faces[index][2] = i + 1 + (U + 1) * (j + 1);
				faces[index][3] = i + (U + 1) * (j + 1);
				index++;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(points).setVertexUVW(uvw).setFaces(faces).setDuplicate(false).setCheckNormals(false);
		return fl.createBase();
	}
}
