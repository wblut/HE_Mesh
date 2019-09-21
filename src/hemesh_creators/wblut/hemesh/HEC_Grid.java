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
		parameters.set("sizeu",100.0);
		parameters.set("sizev",100.0);
		parameters.set("u",1);
		parameters.set("v",1);
		parameters.set("valuescale",1.0);
		maxWValue = Double.NEGATIVE_INFINITY;
		minWValue = Double.POSITIVE_INFINITY;
		parameters.set("stepped", false);
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
		parameters.set("sizeu",(double)sizeU);
		parameters.set("sizev",(double)sizeV);
		parameters.set("u",U);
		parameters.set("v",V);
	}
	
	protected boolean getStepped() {
		return parameters.get("stepped",false);
	}
	
	protected int getU() {
		return parameters.get("u",1);
	}
	
	protected int getV() {
		return parameters.get("v",1);
	}
	
	protected double getSizeU() {
		return parameters.get("sizeu",100.0);
	}
	
	protected double getSizeV() {
		return parameters.get("sizev",100.0);
	}
	
	
	protected double getValueScale() {
		return parameters.get("valuescale",1.0);
	}

	/**
	 *
	 *
	 * @param U
	 * @return
	 */
	public HEC_Grid setU(final int U) {
		parameters.set("u",U);
		return this;
	}

	/**
	 *
	 *
	 * @param V
	 * @return
	 */
	public HEC_Grid setV(final int V) {
		parameters.set("v",V);
		return this;
	}

	/**
	 *
	 *
	 * @param sizeU
	 * @return
	 */
	public HEC_Grid setUSize(final double sizeU) {
		parameters.set("sizeu",(double)sizeU);
		return this;
	}
	public HEC_Grid setSizeU(final double sizeU) {
		parameters.set("sizeu",(double)sizeU);
		return this;
	}

	/**
	 *
	 *
	 * @param sizeV
	 * @return
	 */
	public HEC_Grid setVSize(final double sizeV) {
		parameters.set("sizev",(double)sizeV);
		return this;
	}
	public HEC_Grid setsizeV(final double sizeV) {
		parameters.set("sizev",(double)sizeV);
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
		parameters.set("valuescale",(double)value);
		return this;
	}
	
	public HEC_Grid setStepped(final boolean b) {
		parameters.set("stepped",b);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.creators.HEB_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if(getStepped()) {
			return createStepGrid();
		}else {
			return createGrid();
		}
	}
	
	private HE_Mesh createGrid() {
		int U=getU();
		int V=getV();
		double sizeU=getSizeU();
		double sizeV=getSizeV();
		double valueScale=getValueScale();
		if (sizeU == 0 || sizeV == 0) {
			return new HE_Mesh();
		}
		final double dU = sizeU / U;
		final double dV = sizeV / V;
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
					points[index][2] = valueScale * values[i][j];
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
		fl.setVertices(points).setVertexUVW(uvw).setFaces(faces).setCheckDuplicateVertices(false).setCheckNormals(false);
		return fl.createBase();
	}
	
	private HE_Mesh createStepGrid() {
		int U=getU();
		int V=getV();
		double sizeU=getSizeU();
		double sizeV=getSizeV();
		double valueScale=getValueScale();
		if (sizeU == 0 || sizeV == 0) {
			return new HE_Mesh();
		}
		final double dU = sizeU / U;
		final double dV = sizeV / V;

		final List<WB_Polygon> polygons = new FastList<WB_Polygon>();

		for (int i = 0; i < U; i++) {
			for (int j = 0; j < V; j++) {
				WB_Point p1 = new WB_Point(i * dU, j * dV, valueScale * values[i][j]);
				WB_Point p2 = new WB_Point((i + 1) * dU, j * dV, valueScale * values[i][j]);
				WB_Point p3 = new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * values[i][j]);
				WB_Point p4 = new WB_Point(i * dU, (j + 1) * dV, valueScale * values[i][j]);
				polygons.add(new WB_Polygon(p1, p2, p3, p4));
			}
		}

		double vi, vipo, v1, v2;
		for (int i = 0; i < U - 1; i++) {
			for (int j = 0; j < V; j++) {
				List<WB_Coord> points = new FastList<WB_Coord>();
				vi = values[i][j];
				vipo = values[i + 1][j];
				if (vi != vipo) {
					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vi));
					points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * vi));
					if (j - 1 >= 0) {
						v1 = values[i][j - 1];
						v2 = values[i + 1][j - 1];
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
					}

					points.add(new WB_Point((i + 1) * dU, j * dV, valueScale * vipo));
					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vipo));

					if (j + 1 < V) {
						v1 = values[i][j + 1];
						v2 = values[i + 1][j + 1];
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
					}

					polygons.add(new WB_Polygon(points));
				}
			}
		}
		double vj, vjpo;
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < V - 1; j++) {
				List<WB_Coord> points = new FastList<WB_Coord>();
				vj = values[i][j];
				vjpo = values[i][j + 1];
				if (vj != vjpo) {
					points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * vj));
					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vj));

					if (i + 1 < U) {
						v1 = values[i + 1][j];
						v2 = values[i + 1][j + 1];
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
					}

					points.add(new WB_Point((i + 1) * dU, (j + 1) * dV, valueScale * vjpo));
					points.add(new WB_Point(i * dU, (j + 1) * dV, valueScale * vjpo));

					if (i - 1 >= 0) {
						v1 = values[i - 1][j + 1];
						v2 = values[i - 1][j];
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
					}

					polygons.add(new WB_Polygon(points));

				}
			}
		}
		final HEC_FromPolygons fq = new HEC_FromPolygons();
		fq.setPolygons(polygons);
		return fq.createBase();
	}
}
