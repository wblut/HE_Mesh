/*
 *
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
public class HEC_StepGrid extends HEC_Creator {
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
	private double valueScale;
	private double minValue, maxValue;

	/**
	 *
	 */
	public HEC_StepGrid() {
		super();
		uSize = 100;
		vSize = 100;
		U = 1;
		V = 1;
		valueScale = 1;
		maxValue = Double.NEGATIVE_INFINITY;
		minValue = Double.POSITIVE_INFINITY;
	}

	/**
	 *
	 *
	 * @param U
	 * @param V
	 * @param uSize
	 * @param vSize
	 */
	public HEC_StepGrid(final int U, final int V, final double uSize, final double vSize) {
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
	public HEC_StepGrid setU(final int U) {
		this.U = Math.max(1, U);
		return this;
	}

	/**
	 *
	 *
	 * @param V
	 * @return
	 */
	public HEC_StepGrid setV(final int V) {
		this.V = Math.max(1, V);
		return this;
	}

	/**
	 *
	 *
	 * @param uSize
	 * @return
	 */
	public HEC_StepGrid setUSize(final double uSize) {
		this.uSize = uSize;
		return this;
	}

	/**
	 *
	 *
	 * @param vSize
	 * @return
	 */
	public HEC_StepGrid setVSize(final double vSize) {
		this.vSize = vSize;
		return this;
	}

	/**
	 *
	 *
	 * @param values
	 * @return
	 */
	public HEC_StepGrid setValues(final double[][] values) {
		this.values = new double[U][V];
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < V; j++) {
				this.values[i][j] = values[i][j];
				maxValue = Math.max(maxValue, values[i][j]);
				minValue = Math.min(minValue, values[i][j]);
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
	public HEC_StepGrid setValues(final WB_ScalarParameter height, final double ui, final double vi, final double du,
			final double dv) {
		values = new double[U][V];
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < V; j++) {
				values[i][j] = height.evaluate(ui + (i + 0.5) * du, vi + (j + 0.5) * dv);
				maxValue = Math.max(maxValue, values[i][j]);
				minValue = Math.min(minValue, values[i][j]);
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
	public HEC_StepGrid setValues(final float[][] values) {
		this.values = new double[U][V];
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < V; j++) {
				this.values[i][j] = values[i][j];
				maxValue = Math.max(maxValue, values[i][j]);
				minValue = Math.min(minValue, values[i][j]);
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
	public HEC_StepGrid setValues(final float[] values) {
		int id = 0;
		this.values = new double[U][V];
		for (int j = 0; j < V; j++) {
			for (int i = 0; i < U; i++) {
				this.values[i][j] = values[id];
				maxValue = Math.max(maxValue, values[id]);
				minValue = Math.min(minValue, values[id]);
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
	public HEC_StepGrid setValues(final double[] values) {
		int id = 0;
		this.values = new double[U][V];
		for (int j = 0; j < V; j++) {
			for (int i = 0; i < U; i++) {
				this.values[i][j] = values[id];
				maxValue = Math.max(maxValue, values[id]);
				minValue = Math.min(minValue, values[id]);
				id++;
			}
		}
		return this;
	}

	/**
	 *
	 *
	 * @param scale
	 * @return
	 */
	public HEC_StepGrid setValueScale(final double scale) {
		valueScale = scale;
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
