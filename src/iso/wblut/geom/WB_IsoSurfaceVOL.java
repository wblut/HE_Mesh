/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class WB_IsoSurfaceVOL {
	/**
	 *
	 */
	final static int ONVERTEX = 0;
	/**
	 *
	 */
	final static int ONEDGE = 1;
	/**
	 *
	 */
	final static int NEGATIVE = 0;
	/**
	 *
	 */
	final static int EQUAL = 1;
	/**
	 *
	 */
	final static int POSITIVE = 2;
	/**
	 *
	 */
	int[] digits = new int[8];
	/*
	 * VERTICES 000 ijk=0 100 Ijk=1 010 iJk=2 110 IJk=3 001 ijK=4 101 IjK=5 011
	 * iJK=6 111 IJK=7
	 */
	/**
	 *
	 */
	final static WB_Point[] gridvertices = new WB_Point[] { new WB_Point(0, 0, 0), new WB_Point(1, 0, 0),
			new WB_Point(0, 1, 0), new WB_Point(1, 1, 0), new WB_Point(0, 0, 1), new WB_Point(1, 0, 1),
			new WB_Point(0, 1, 1), new WB_Point(1, 1, 1), };
	// EDGES: 2 vertices per edge
	/**
	 *
	 */
	final static int[][] edges = { { 0, 1 }, // x ijk
			{ 0, 2 }, // y ijk
			{ 1, 3 }, // y Ijk
			{ 2, 3 }, // x iJk
			{ 0, 4 }, // z ijk
			{ 1, 5 }, // z Ijk
			{ 2, 6 }, // z iJk
			{ 3, 7 }, // z IJk
			{ 4, 5 }, // x ijK
			{ 4, 6 }, // y ijK
			{ 5, 7 }, // y IjK
			{ 6, 7 } // x iJK
	};
	/**
	 *
	 */
	int[][] entries;

	/**
	 *
	 */
	private WB_IsoValues3D values;

	// ISOVERTICES: 20
	// type=ONVERTEX iso vertex on vertex, index in vertex list
	// type=ONEDGE iso vertex on edge, index in edge list, 0=lower
	// threshold,1=higher threshold
	/**
	 *
	 */
	final static int[][] isovertices = new int[][] { { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 }, { 1, 2, 0 },
			{ 1, 2, 1 }, { 1, 3, 0 }, { 1, 3, 1 }, { 1, 4, 0 }, { 1, 4, 1 }, { 1, 5, 0 }, { 1, 5, 1 }, { 1, 6, 0 },
			{ 1, 6, 1 }, { 1, 7, 0 }, { 1, 7, 1 }, { 1, 8, 0 }, { 1, 8, 1 }, { 1, 9, 0 }, { 1, 9, 1 }, { 1, 10, 0 },
			{ 1, 10, 1 }, { 1, 11, 0 }, { 1, 11, 1 }, { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 },
			{ 0, 6 }, { 0, 7 } };
	/**
	 *
	 */
	private int resx, resy, resz;
	/**
	 *
	 */
	private double cx, cy, cz;
	/**
	 *
	 */
	private double dx, dy, dz;
	/**
	 *
	 */
	private double isolevelmin, isolevelmax;

	/**
	 *
	 */
	private IntObjectHashMap<WB_Point> xedges;
	/**
	 *
	 */
	private IntObjectHashMap<WB_Point> yedges;
	/**
	 *
	 */
	private IntObjectHashMap<WB_Point> zedges;
	/**
	 *
	 */
	private IntObjectHashMap<WB_Point> vertices;

	private List<WB_Tetrahedron> tetra;

	/**
	 *
	 */
	public WB_IsoSurfaceVOL() {
		super();
		String line = "";
		final String cvsSplitBy = " ";
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		entries = new int[6561][];
		try {
			is = this.getClass().getClassLoader().getResourceAsStream("resources/ivolcube3D.txt");
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			int i = 0;
			while ((line = br.readLine()) != null) {
				final String[] cell = line.split(cvsSplitBy);
				final int[] indices = new int[cell.length];
				for (int j = 0; j < cell.length; j++) {
					indices[j] = Integer.parseInt(cell[j]);
				}
				entries[i] = indices;
				i++;
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					isr.close();
					is.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Number of cells.
	 *
	 * @param resx
	 *            the resx
	 * @param resy
	 *            the resy
	 * @param resz
	 *            the resz
	 * @return self
	 */
	public WB_IsoSurfaceVOL setResolution(final int resx, final int resy, final int resz) {
		this.resx = resx;
		this.resy = resy;
		this.resz = resz;
		return this;
	}

	/**
	 * Size of cell.
	 *
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return self
	 */
	public WB_IsoSurfaceVOL setSize(final double dx, final double dy, final double dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		return this;
	}

	/**
	 * Values at grid points.
	 *
	 * @param values
	 *            double[resx+1][resy+1][resz+1]
	 * @return self
	 */
	public WB_IsoSurfaceVOL setValues(final double[][][] values) {
		this.values = new WB_IsoValues3D.GridRaw3D(values);
		resx = values.length - 1;
		resy = resx > 0 ? values[0].length - 1 : 0;
		resz = resy > 0 ? values[0][0].length - 1 : 0;
		return this;
	}

	/**
	 * Sets the values.
	 *
	 * @param values
	 *            float[resx+1][resy+1][resz+1]
	 * @return self
	 */
	public WB_IsoSurfaceVOL setValues(final float[][][] values) {
		this.values = new WB_IsoValues3D.Grid3D(values);
		resx = values.length - 1;
		resy = resx > 0 ? values[0].length - 1 : 0;
		resz = resy > 0 ? values[0][0].length - 1 : 0;
		return this;
	}

	/**
	 *
	 *
	 * @param function
	 * @param xi
	 * @param yi
	 * @param zi
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public WB_IsoSurfaceVOL setValues(final WB_ScalarParameter function, final double xi, final double yi,
			final double zi, final double dx, final double dy, final double dz, final int sizeI, final int sizeJ,
			final int sizeK) {
		this.values = new WB_IsoValues3D.Function3D(function, xi, yi, zi, dx, dy, dz, sizeI, sizeJ, sizeK);
		resx = sizeI - 1;
		resy = sizeJ - 1;
		resz = sizeK - 1;
		return this;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public WB_IsoSurfaceVOL setValues(final WB_HashGridDouble3D values) {
		this.values = new WB_IsoValues3D.HashGrid3D(values);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	/**
	 *
	 *
	 * @param isolevelmin
	 * @param isolevelmax
	 * @return
	 */
	public WB_IsoSurfaceVOL setIsolevel(final double isolevelmin, final double isolevelmax) {
		this.isolevelmin = isolevelmin;
		this.isolevelmax = isolevelmax;
		return this;
	}

	public WB_IsoSurfaceVOL setCenter(final WB_Coord c) {
		cx = c.xd();
		cy = c.yd();
		cz = c.zd();
		return this;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	private int index(final int i, final int j, final int k) {
		return i + 1 + (resx + 2) * (j + 1) + (resx + 2) * (resy + 2) * (k + 1);
	}

	/**
	 * Value.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param k
	 *            the k
	 * @return the double
	 */
	private double value(final int i, final int j, final int k) {

		return values.getValue(i, j, k);

	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param k
	 * @param offset
	 * @return
	 */
	private WB_Point vertex(final int i, final int j, final int k, final WB_Point offset) {
		WB_Point vertex = vertices.get(index(i, j, k));
		if (vertex != null) {
			return vertex;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		vertex = new WB_Point(p0.addSelf(offset));

		vertices.put(index(i, j, k), vertex);
		return vertex;
	}

	/**
	 * Xedge.
	 *
	 * @param i
	 *            i: -1 .. resx+1
	 * @param j
	 *            j: -1 .. resy+1
	 * @param k
	 *            k: -1 .. resz+1
	 * @param offset
	 * @param isolevel
	 * @return edge vertex
	 */
	private WB_Point xedge(final int i, final int j, final int k, final WB_Point offset, final double isolevel) {
		WB_Point xedge = xedges.get(index(i, j, k));
		if (xedge != null) {
			return xedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx + dx, j * dy, k * dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i + 1, j, k);
		xedge = new WB_Point(interp(isolevel, p0, p1, val0, val1));
		xedge.addSelf(offset);
		xedges.put(index(i, j, k), xedge);
		return xedge;
	}

	/**
	 * Yedge.
	 *
	 * @param i
	 *            i: -1 .. resx+1
	 * @param j
	 *            j: -1 .. resy+1
	 * @param k
	 *            k: -1 .. resz+1
	 * @param offset
	 * @param isolevel
	 * @return edge vertex
	 */
	private WB_Point yedge(final int i, final int j, final int k, final WB_Point offset, final double isolevel) {
		WB_Point yedge = yedges.get(index(i, j, k));
		if (yedge != null) {
			return yedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx, j * dy + dy, k * dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i, j + 1, k);
		yedge = new WB_Point(interp(isolevel, p0, p1, val0, val1));
		yedge.addSelf(offset);
		yedges.put(index(i, j, k), yedge);
		return yedge;
	}

	/**
	 * Zedge.
	 *
	 * @param i
	 *            i: -1 .. resx+1
	 * @param j
	 *            j: -1 .. resy+1
	 * @param k
	 *            k: -1 .. resz+1
	 * @param offset
	 * @param isolevel
	 * @return edge vertex
	 */
	private WB_Point zedge(final int i, final int j, final int k, final WB_Point offset, final double isolevel) {
		WB_Point zedge = zedges.get(index(i, j, k));
		if (zedge != null) {
			return zedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx, j * dy, k * dz + dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i, j, k + 1);
		zedge = new WB_Point(interp(isolevel, p0, p1, val0, val1));
		zedge.addSelf(offset);
		zedges.put(index(i, j, k), zedge);
		return zedge;
	}

	/*
	 * Linearly interpolate the position where an isosurface cuts an edge
	 * between two vertices, each with their own scalar value
	 */
	/**
	 * Interp.
	 *
	 * @param isolevel
	 *            the isolevel
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @param valp1
	 *            the valp1
	 * @param valp2
	 *            the valp2
	 * @return the h e_ vertex
	 */
	private WB_Point interp(final double isolevel, final WB_Point p1, final WB_Point p2, final double valp1,
			final double valp2) {
		double mu;
		if (WB_Epsilon.isEqual(isolevel, valp1)) {
			return new WB_Point(p1);
		}
		if (WB_Epsilon.isEqual(isolevel, valp2)) {
			return new WB_Point(p2);
		}
		if (WB_Epsilon.isEqual(valp1, valp2)) {
			return new WB_Point(p1);
		}
		mu = (isolevel - valp1) / (valp2 - valp1);
		return new WB_Point(p1.xd() + mu * (p2.xd() - p1.xd()), p1.yd() + mu * (p2.yd() - p1.yd()),
				p1.zd() + mu * (p2.zd() - p1.zd()));
	}

	/**
	 * Classify cell.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param k
	 *            the k
	 * @return the int
	 */
	private int classifyCell(final int i, final int j, final int k) {

		if (i < 0 || j < 0 || k < 0 || i >= resx || j >= resy || k >= resz) {
			return -1;
		}

		digits = new int[8];
		int cubeindex = 0;
		int offset = 1;

		if (value(i, j, k) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[0] = POSITIVE;
		} else if (value(i, j, k) >= isolevelmin) {
			cubeindex += offset;
			digits[0] = EQUAL;
		}
		offset *= 3;
		if (value(i + 1, j, k) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[1] = POSITIVE;
		} else if (value(i + 1, j, k) >= isolevelmin) {
			cubeindex += offset;
			digits[1] = EQUAL;
		}
		offset *= 3;
		if (value(i, j + 1, k) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[2] = POSITIVE;
		} else if (value(i, j + 1, k) >= isolevelmin) {
			cubeindex += offset;
			digits[2] = EQUAL;
		}
		offset *= 3;
		if (value(i + 1, j + 1, k) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[3] = POSITIVE;
		} else if (value(i + 1, j + 1, k) >= isolevelmin) {
			cubeindex += offset;
			digits[3] = EQUAL;
		}
		offset *= 3;
		if (value(i, j, k + 1) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[4] = POSITIVE;
		} else if (value(i, j, k + 1) >= isolevelmin) {
			cubeindex += offset;
			digits[4] = EQUAL;
		}
		offset *= 3;
		if (value(i + 1, j, k + 1) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[5] = POSITIVE;
		} else if (value(i + 1, j, k + 1) >= isolevelmin) {
			cubeindex += offset;
			digits[5] = EQUAL;
		}
		offset *= 3;
		if (value(i, j + 1, k + 1) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[6] = POSITIVE;
		} else if (value(i, j + 1, k + 1) >= isolevelmin) {
			cubeindex += offset;
			digits[6] = EQUAL;
		}
		offset *= 3;
		if (value(i + 1, j + 1, k + 1) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[7] = POSITIVE;
		} else if (value(i + 1, j + 1, k + 1) >= isolevelmin) {
			cubeindex += offset;
			digits[7] = EQUAL;
		}

		return cubeindex;
	}

	/**
	 * Polygonise.
	 */
	private void polygonise() {
		xedges = new IntObjectHashMap<WB_Point>();
		yedges = new IntObjectHashMap<WB_Point>();
		zedges = new IntObjectHashMap<WB_Point>();
		vertices = new IntObjectHashMap<WB_Point>();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx, cy - 0.5 * resy * dy, cz - 0.5 * resz * dz);
		tetra = new FastList<WB_Tetrahedron>();

		for (int i = 0; i < resx; i++) {
			for (int j = 0; j < resy; j++) {
				for (int k = 0; k < resz; k++) {
					getPolygons(i, j, k, classifyCell(i, j, k), offset);
				}
			}
		}

	}

	/**
	 * Gets the polygons.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param k
	 *            the k
	 * @param cubeindex
	 *            the cubeindex
	 * @param offset
	 * @return the polygons
	 */
	private void getPolygons(final int i, final int j, final int k, final int cubeindex, final WB_Point offset) {
		final int[] indices = entries[cubeindex];
		final int numtetras = indices[0];
		int currentindex = 1;
		for (int t = 0; t < numtetras; t++) {
			final WB_Point v1 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			final WB_Point v2 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			final WB_Point v3 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			final WB_Point v4 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			tetra.add(new WB_Tetrahedron(v1, v2, v3, v4));
		}
	}

	public List<WB_Tetrahedron> getTetrahedra() {
		polygonise();
		return tetra;
	}

	/**
	 *
	 *
	 * @param isopointindex
	 * @param i
	 * @param j
	 * @param k
	 * @param offset
	 * @return
	 */
	WB_Point getIsoVertex(final int isopointindex, final int i, final int j, final int k, final WB_Point offset) {
		if (isovertices[isopointindex][0] == ONVERTEX) {
			switch (isovertices[isopointindex][1]) {
			case 0:
				return vertex(i, j, k, offset);
			case 1:
				return vertex(i + 1, j, k, offset);
			case 2:
				return vertex(i, j + 1, k, offset);
			case 3:
				return vertex(i + 1, j + 1, k, offset);
			case 4:
				return vertex(i, j, k + 1, offset);
			case 5:
				return vertex(i + 1, j, k + 1, offset);
			case 6:
				return vertex(i, j + 1, k + 1, offset);
			case 7:
				return vertex(i + 1, j + 1, k + 1, offset);
			default:
				return null;
			}
		} else if (isovertices[isopointindex][0] == ONEDGE) {
			if (isovertices[isopointindex][2] == 0) {
				switch (isovertices[isopointindex][1]) {
				case 0:
					return xedge(i, j, k, offset, isolevelmin);
				case 1:
					return yedge(i, j, k, offset, isolevelmin);
				case 2:
					return yedge(i + 1, j, k, offset, isolevelmin);
				case 3:
					return xedge(i, j + 1, k, offset, isolevelmin);
				case 4:
					return zedge(i, j, k, offset, isolevelmin);
				case 5:
					return zedge(i + 1, j, k, offset, isolevelmin);
				case 6:
					return zedge(i, j + 1, k, offset, isolevelmin);
				case 7:
					return zedge(i + 1, j + 1, k, offset, isolevelmin);
				case 8:
					return xedge(i, j, k + 1, offset, isolevelmin);
				case 9:
					return yedge(i, j, k + 1, offset, isolevelmin);
				case 10:
					return yedge(i + 1, j, k + 1, offset, isolevelmin);
				case 11:
					return xedge(i, j + 1, k + 1, offset, isolevelmin);
				default:
					return null;
				}
			} else {
				switch (isovertices[isopointindex][1]) {
				case 0:
					return xedge(i, j, k, offset, isolevelmax);
				case 1:
					return yedge(i, j, k, offset, isolevelmax);
				case 2:
					return yedge(i + 1, j, k, offset, isolevelmax);
				case 3:
					return xedge(i, j + 1, k, offset, isolevelmax);
				case 4:
					return zedge(i, j, k, offset, isolevelmax);
				case 5:
					return zedge(i + 1, j, k, offset, isolevelmax);
				case 6:
					return zedge(i, j + 1, k, offset, isolevelmax);
				case 7:
					return zedge(i + 1, j + 1, k, offset, isolevelmax);
				case 8:
					return xedge(i, j, k + 1, offset, isolevelmax);
				case 9:
					return yedge(i, j, k + 1, offset, isolevelmax);
				case 10:
					return yedge(i + 1, j, k + 1, offset, isolevelmax);
				case 11:
					return xedge(i, j + 1, k + 1, offset, isolevelmax);
				default:
					return null;
				}
			}
		}
		return null;
	}
}
