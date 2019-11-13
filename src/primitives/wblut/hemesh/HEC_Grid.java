/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_Grid extends HEC_Creator {
	private double[][] values;
	private double minWValue, maxWValue;

	/**
	 *
	 */
	public HEC_Grid() {
		super();
		parameters.set("sizeu", 100.0);
		parameters.set("sizev", 100.0);
		parameters.set("u", 1);
		parameters.set("v", 1);
		parameters.set("valuescale", 1.0);
		maxWValue = Double.NEGATIVE_INFINITY;
		minWValue = Double.POSITIVE_INFINITY;
		parameters.set("stepped", false);
		parameters.set("base", false);
		parameters.set("baseValue", 0.0);
		parameters.set("override", true);
	}

	/**
	 *
	 *
	 * @param U
	 * @param V
	 * @param sizeU
	 * @param sizeV
	 */
	public HEC_Grid(final int U, final int V, final double sizeU, final double sizeV) {
		this();
		parameters.set("sizeu", (double) sizeU);
		parameters.set("sizev", (double) sizeV);
		parameters.set("u", U);
		parameters.set("v", V);
	}

	protected boolean getStepped() {
		return parameters.get("stepped", false);
	}

	protected boolean getBase() {
		return parameters.get("base", false);
	}

	protected double getBaseValue() {
		return parameters.get("baseValue", 0.0);
	}

	protected int getU() {
		return parameters.get("u", 1);
	}

	protected int getV() {
		return parameters.get("v", 1);
	}

	protected double getSizeU() {
		return parameters.get("sizeu", 100.0);
	}

	protected double getSizeV() {
		return parameters.get("sizev", 100.0);
	}

	protected double getValueScale() {
		return parameters.get("valuescale", 1.0);
	}

	/**
	 *
	 *
	 * @param U
	 * @return
	 */
	public HEC_Grid setU(final int U) {
		parameters.set("u", U);
		return this;
	}

	/**
	 *
	 *
	 * @param V
	 * @return
	 */
	public HEC_Grid setV(final int V) {
		parameters.set("v", V);
		return this;
	}

	/**
	 *
	 *
	 * @param sizeU
	 * @return
	 */
	public HEC_Grid setUSize(final double sizeU) {
		parameters.set("sizeu", (double) sizeU);
		return this;
	}

	public HEC_Grid setSizeU(final double sizeU) {
		parameters.set("sizeu", (double) sizeU);
		return this;
	}

	/**
	 *
	 *
	 * @param sizeV
	 * @return
	 */
	public HEC_Grid setVSize(final double sizeV) {
		parameters.set("sizev", (double) sizeV);
		return this;
	}

	public HEC_Grid setsizeV(final double sizeV) {
		parameters.set("sizev", (double) sizeV);
		return this;
	}

	/**
	 *
	 *
	 * @param values
	 * @return
	 */
	public HEC_Grid setValues(final double[][] values) {
		this.values = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
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
	public HEC_Grid setValues(final WB_ScalarParameter height, final double ui, final double vi, final double du,
			final double dv) {

		values = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
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
	public HEC_Grid setValues(final float[][] values) {
		this.values = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
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
	 * @param value
	 * @return
	 */
	public HEC_Grid setValueScale(final double value) {
		parameters.set("valuescale", (double) value);
		return this;
	}

	public HEC_Grid setStepped(final boolean b) {
		parameters.set("stepped", b);
		return this;
	}

	public HEC_Grid setBase(final boolean b) {
		parameters.set("base", b);
		return this;
	}

	public HEC_Grid setBaseValue(final double value) {
		parameters.set("baseValue", value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.creators.HEB_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (getStepped()) {
			return createStepGrid();
		} else {
			return createGrid();
		}
	}

	private HE_Mesh createGrid() {
		int U = getU();
		int V = getV();
		double sizeU = getSizeU();
		double sizeV = getSizeV();
		double valueScale = getValueScale();
		if (sizeU == 0 || sizeV == 0) {
			return new HE_Mesh();
		}
		final double dU = sizeU / U;
		final double dV = sizeV / V;
		final double ndU = 1.0 / U;
		final double ndV = 1.0 / V;
		double[][] points;
		double[][] uvw;
		int[][] faces;
		int index = 0;
		int UV = U * V;
		int UVpp = (U + 1) * (V + 1);
		if (values == null) {
			points = new double[UVpp][3];
			uvw = new double[UVpp][3];
			faces = new int[UV][4];
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
			points = getBase() ? new double[2 * UVpp][3] : new double[UVpp][3];
			uvw = getBase() ? new double[2 * UVpp][3] : new double[UVpp][3];
			faces = getBase() ? new int[2 * UV + 2 * U + 2 * V][4] : new int[UV][4];
			if (getBase()) {
				minWValue = Math.min(minWValue, getBaseValue());
				maxWValue = Math.max(maxWValue, getBaseValue());
			}
			for (int j = 0; j < V + 1; j++) {
				for (int i = 0; i < U + 1; i++) {
					points[index][0] = i * dU;
					points[index][1] = j * dV;
					points[index][2] = valueScale * values[i][j];
					uvw[index][0] = i * ndU;
					uvw[index][1] = j * ndV;
					uvw[index][2] = (values[i][j] - minWValue) / maxWValue;
					if (Double.isNaN(uvw[index][2])) {
						uvw[index][2] = 0.0;
					}
					if (getBase()) {
						points[UVpp + index][0] = i * dU;
						points[UVpp + index][1] = j * dV;
						points[UVpp + index][2] = valueScale * getBaseValue();
						uvw[UVpp + index][0] = i * ndU;
						uvw[UVpp + index][1] = j * ndV;
						uvw[UVpp + index][2] = (getBaseValue() - minWValue) / maxWValue;

						if (Double.isNaN(uvw[UVpp + index][2])) {
							uvw[UVpp + index][2] = 0.0;
						}
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
				if (getBase()) {
					faces[UV + index][3] = UVpp + i + (U + 1) * j;
					faces[UV + index][2] = UVpp + i + 1 + (U + 1) * j;
					faces[UV + index][1] = UVpp + i + 1 + (U + 1) * (j + 1);
					faces[UV + index][0] = UVpp + i + (U + 1) * (j + 1);
				}

				index++;
			}
		}
		if (getBase()) {
			index = 0;
			for (int i = 0; i < U; i++) {
				faces[2 * UV + index][3] = i;
				faces[2 * UV + index][2] = i + 1;
				faces[2 * UV + index][1] = UVpp + i + 1;
				faces[2 * UV + index][0] = UVpp + i;

				index++;
				faces[2 * UV + index][0] = i + (U + 1) * V;
				faces[2 * UV + index][1] = i + 1 + (U + 1) * V;
				faces[2 * UV + index][2] = UVpp + i + 1 + (U + 1) * V;
				faces[2 * UV + index][3] = UVpp + i + (U + 1) * V;

				index++;
			}

			for (int j = 0; j < V; j++) {
				faces[2 * UV + index][3] = (U + 1) * j;
				faces[2 * UV + index][2] = UVpp + (U + 1) * j;
				faces[2 * UV + index][1] = UVpp + (U + 1) * (j + 1);
				faces[2 * UV + index][0] = (U + 1) * (j + 1);

				index++;
				faces[2 * UV + index][0] = U + (U + 1) * j;
				faces[2 * UV + index][1] = UVpp + U + (U + 1) * j;
				faces[2 * UV + index][2] = UVpp + U + (U + 1) * (j + 1);
				faces[2 * UV + index][3] = U + (U + 1) * (j + 1);

				index++;
			}

		}

		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(points).setVertexUVW(uvw).setFaces(faces).setCheckDuplicateVertices(false)
				.setCheckNormals(false);
		HE_Mesh baseMesh = fl.createBase();
		baseMesh.selectFaces("grid", 0, UV);
		if (getBase()) {
			baseMesh.selectFaces("base", UV, 2 * UV);
			baseMesh.selectFaces("walls", 2 * UV, 2 * UV + 2 * U + 2 * V);
		}
		return baseMesh;
	}

	private HE_Mesh createStepGrid() {
		int U = getU();
		int V = getV();
		double sizeU = getSizeU();
		double sizeV = getSizeV();
		double valueScale = getValueScale();
		if (sizeU == 0 || sizeV == 0) {
			return new HE_Mesh();
		}
		final double dU = sizeU / U;
		final double dV = sizeV / V;

		final List<WB_Polygon> polygons = new FastList<WB_Polygon>();
		double outOfRange = getBaseValue();
		boolean base = getBase();
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < V; j++) {
				WB_Point p1 = new WB_Point(i * dU, j * dV, valueScale * getValue(i, j, outOfRange));
				WB_Point p2 = new WB_Point((i + 1) * dU, j * dV, valueScale * getValue(i, j, outOfRange));
				WB_Point p3 = new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * getValue(i, j, outOfRange));
				WB_Point p4 = new WB_Point(i * dU, (j + 1) * dV, valueScale * getValue(i, j, outOfRange));
				polygons.add(new WB_Polygon(p1, p2, p3, p4));
			}
		}
		if (base) {
			for (int i = 0; i < U; i++) {
				for (int j = 0; j < V; j++) {
					WB_Point p1 = new WB_Point(i * dU, j * dV, valueScale * getBaseValue());
					WB_Point p2 = new WB_Point((i + 1) * dU, j * dV, valueScale * getBaseValue());
					WB_Point p3 = new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * getBaseValue());
					WB_Point p4 = new WB_Point(i * dU, (j + 1) * dV, valueScale * getBaseValue());
					polygons.add(new WB_Polygon(p4, p3, p2, p1));
				}
			}

		}
		double vi, vipo, v1, v2;
		for (int i = (base ? -1 : 0); i < (base ? U : U - 1); i++) {
			for (int j = 0; j < V; j++) {
				List<WB_Coord> points = new FastList<WB_Coord>();
				vi = getValue(i, j, outOfRange);
				vipo = getValue(i + 1, j, outOfRange);
				if (vi != vipo) {
					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vi));
					points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * vi));
					
						v1 = getValue(i, j - 1, outOfRange);
						v2 = getValue(i + 1, j - 1, outOfRange);
						if (vi < vipo) {
							if (vi < v1 && v1 < vipo && vi < v2 && v2 < vipo) {
								if (v1 == v2) {
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
								} else if (v1 < v2) {
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v2));
								} else if (v2 < v1) {
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v2));
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
								}
							} else if (vi < v1 && v1 < vipo) {
								points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
							} else if (vi < v2 && v2 < vipo) {
								points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v2));
							}
						} else {
							if (vipo < v1 && v1 < vi && vipo < v2 && v2 < vi) {
								if (v1 == v2) {
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
								} else if (v2 < v1) {
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v2));
								} else if (v1 < v2) {
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v2));
									points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
								}
							} else if (vipo < v1 && v1 < vi) {
								points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v1));
							} else if (vipo < v2 && v2 < vi) {
								points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * v2));
							}
						}
					

					points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * vipo));
					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vipo));

					
						v1 = getValue(i, j + 1, outOfRange);
						v2 = getValue(i + 1, j + 1, outOfRange);
						
						if (vi < vipo) {
							if (vi < v1 && v1 < vipo && vi < v2 && v2 < vipo) {
								if (v1 == v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								} else if (v1 < v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								} else if (v2 < v1) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
								}
							} else if (vi < v1 && v1 < vipo) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
							} else if (vi < v2 && v2 < vipo) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
							}
						} else {
							if (vipo < v1 && v1 < vi && vipo < v2 && v2 < vi) {
								if (v1 == v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								} else if (v2 < v1) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								} else if (v1 < v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
								}
							} else if (vipo < v1 && v1 < vi) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
							} else if (vipo < v2 && v2 < vi) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
							}
						}
					

					polygons.add(new WB_Polygon(points));
				}
			}
		}
		double vj, vjpo;
		for (int i = 0; i < U; i++) {
			for (int j = (base ? -1 : 0); j < (base ? V : V - 1); j++) {
				List<WB_Coord> points = new FastList<WB_Coord>();
				vj = getValue(i, j, outOfRange);
				vjpo = getValue(i, j + 1, outOfRange);
				if (vj != vjpo) {
					points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * vj));
					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vj));

					
						v1 = getValue(i + 1, j, outOfRange);
						v2 = getValue(i + 1, j + 1, outOfRange);
						if (vj < vjpo) {
							if (vj < v1 && v1 < vjpo && vj < v2 && v2 < vjpo) {
								if (v1 == v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								} else if (v1 < v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
								} else if (v2 < v1) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								}
							} else if (vj < v1 && v1 < vjpo) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
							} else if (vj < v2 && v2 < vjpo) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
							}
						} else {
							if (vjpo < v1 && v1 < vj && vjpo < v2 && v2 < vj) {
								if (v1 == v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								} else if (v2 < v1) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
								} else if (v1 < v2) {
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
									points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
								}
							} else if (vjpo < v1 && v1 < vj) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v1));
							} else if (vjpo < v2 && v2 < vj) {
								points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * v2));
							}
						}
					

					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vjpo));
					points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * vjpo));

					
						v1 = getValue(i - 1, j + 1, outOfRange);
						v2 = getValue(i - 1, j, outOfRange);
						if (vj < vjpo) {
							if (vj < v1 && v1 < vjpo && vj < v2 && v2 < vjpo) {
								if (v1 == v2) {
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
								} else if (v1 < v2) {
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v2));
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
								} else if (v2 < v1) {
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v2));
								}
							} else if (vj < v1 && v1 < vjpo) {
								points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
							} else if (vj < v2 && v2 < vjpo) {
								points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v2));
							}
						} else {
							if (vjpo < v1 && v1 < vj && vjpo < v2 && v2 < vj) {
								if (v1 == v2) {
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
								} else if (v2 < v1) {
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v2));
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
								} else if (v1 < v2) {
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
									points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v2));
								}
							} else if (vjpo < v1 && v1 < vj) {
								points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v1));
							} else if (vjpo < v2 && v2 < vj) {
								points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * v2));
							}
						}
					

					polygons.add(new WB_Polygon(points));

				}
			}
		}
		HEC_FromPolygons fq = new HEC_FromPolygons();
		fq.setPolygons(polygons);

		HE_Mesh baseMesh = fq.createBase();
		baseMesh.selectFaces("grid", 0, U * V);
		if (getBase()) {
			baseMesh.selectFaces("base", U * V, 2 * U * V);
			baseMesh.selectFaces("walls", 2 * U * V, baseMesh.getNumberOfFaces());
		}
		return baseMesh;

	}

	double getValue(int u, int v, double outOfRange) {
		if (u >= 0 && u < values.length && v >= 0 && v < values[0].length) {
			return values[u][v];
		} else {
			return outOfRange;
		}

	}
}
