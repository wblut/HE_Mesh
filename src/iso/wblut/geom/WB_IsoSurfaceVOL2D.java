/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/

 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
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

import processing.core.PApplet;
import processing.core.PImage;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class WB_IsoSurfaceVOL2D {
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
	private int[] digits = new int[4];
	/*
	 * VERTICES 00 ij=0 10 Ij=1 01 iJ=2 11 IJ=3
	 */
	/**
	 *
	 */
	final static WB_Point[] gridvertices = new WB_Point[] { new WB_Point(0, 0), new WB_Point(1, 0), new WB_Point(0, 1),
			new WB_Point(1, 1) };
	// EDGES: 2 vertices per edge
	/**
	 *
	 */
	final static int[][] edges = { { 0, 1 }, // x ij
			{ 0, 2 }, // y ij
			{ 1, 3 } // y Ij
	};
	/**
	 *
	 */
	private int[][] entries;
	/**
	 *
	 */
	private WB_IsoValues2D values;

	// type=ONVERTEX iso vertex on vertex, index in vertex list
	// type=ONEDGE iso vertex on edge, index in edge list, 0=lower
	// threshold,1=higher threshold
	/**
	 *
	 */
	final static int[][] isovertices = new int[][] { { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 }, { 1, 2, 0 },
			{ 1, 2, 1 }, { 1, 3, 0 }, { 1, 3, 1 }, { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } };
	/**
	 *
	 */
	private int resx, resy;
	/**
	 *
	 */
	private double cx, cy;
	/**
	 *
	 */
	private double dx, dy;
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
	private IntObjectHashMap<WB_Point> vertices;

	/**
	 *
	 */
	private double zFactor;

	private List<WB_Triangle> triangles;

	/**
	 *
	 */
	public WB_IsoSurfaceVOL2D() {
		super();
		String line = "";
		final String cvsSplitBy = " ";
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		entries = new int[6561][];
		try {
			is = this.getClass().getClassLoader().getResourceAsStream("resources/ivolcube2D.txt");
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
		zFactor = 0.0;

	}

	/**
	 * Number of cells.
	 *
	 * @param resx
	 *            the resx
	 * @param resy
	 *            the resy
	 *
	 * @return self
	 */
	public WB_IsoSurfaceVOL2D setResolution(final int resx, final int resy) {
		this.resx = resx;
		this.resy = resy;

		return this;
	}

	/**
	 *
	 * @param dx
	 * @param dy
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setSize(final double dx, final double dy) {
		this.dx = dx;
		this.dy = dy;

		return this;
	}

	public WB_IsoSurfaceVOL2D setZFactor(final double zf) {
		this.zFactor = zf;

		return this;
	}

	/**
	 * Values at grid points.
	 *
	 * @param values
	 *            double[resx+1][resy+1]
	 * @return self
	 */
	public WB_IsoSurfaceVOL2D setValues(final double[][] values) {
		this.values = new WB_IsoValues2D.GridRaw2D(values);
		resx = values.length - 1;
		resy = resx > 0 ? values[0].length - 1 : 0;

		return this;
	}

	/**
	 * Values at grid points.
	 *
	 * @param values
	 *            float[resx+1][resy+1]
	 * @return self
	 */
	public WB_IsoSurfaceVOL2D setValues(final float[][] values) {
		this.values = new WB_IsoValues2D.Grid2D(values);
		resx = values.length - 1;
		resy = resx > 0 ? values[0].length - 1 : 0;

		return this;
	}

	/**
	 *
	 *
	 * @param function
	 * @param xi
	 * @param yi
	 * @param dx
	 * @param dy
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final WB_ScalarParameter function, final double xi, final double yi,
			final double dx, final double dy, final int width, final int height) {
		this.values = new WB_IsoValues2D.Function2D(function, xi, yi, dx, dy, width, height);
		resx = width - 1;
		resy = height - 1;

		return this;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final WB_HashGridDouble2D values) {
		this.values = new WB_IsoValues2D.HashGrid2D(values);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param path
	 * @param home
	 * @param width
	 * @param height
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final String path, final PApplet home, final int width, final int height) {
		this.values = new WB_IsoValues2D.ImageGrid2D(path, home, width, height);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param path
	 * @param home
	 * @param width
	 * @param height
	 * @param mode
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final String path, final PApplet home, final int width, final int height,
			final WB_IsoValues2D.Mode mode) {
		this.values = new WB_IsoValues2D.ImageGrid2D(path, home, width, height, mode);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param image
	 * @param home
	 * @param width
	 * @param height
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final PImage image, final PApplet home, final int width, final int height) {
		this.values = new WB_IsoValues2D.ImageGrid2D(image, home, width, height);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param image
	 * @param home
	 * @param width
	 * @param height
	 * @param mode
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final PImage image, final PApplet home, final int width, final int height,
			final WB_IsoValues2D.Mode mode) {
		this.values = new WB_IsoValues2D.ImageGrid2D(image, home, width, height, mode);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setValues(final WB_IsoValues2D values) {
		this.values = values;
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 *
	 * @param isolevelmin
	 * @param isolevelmax
	 * @return
	 */
	public WB_IsoSurfaceVOL2D setIsolevel(final double isolevelmin, final double isolevelmax) {
		this.isolevelmin = isolevelmin;
		this.isolevelmax = isolevelmax;
		return this;
	}

	public WB_IsoSurfaceVOL2D setCenter(final WB_Coord c) {
		cx = c.xd();
		cy = c.yd();
		return this;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 *
	 * @return
	 */
	private int index(final int i, final int j) {
		return i + 1 + (resx + 2) * (j + 1);
	}

	/**
	 * Value.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 *
	 * @return the double
	 */
	private double value(final int i, final int j) {

		return values.value(i, j);

	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 *
	 * @param offset
	 * @return
	 */
	private WB_Point vertex(final int i, final int j, final WB_Point offset) {
		WB_Point vertex = vertices.get(index(i, j));
		if (vertex != null) {
			return vertex;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, zFactor * value(i, j));
		vertex = new WB_Point(p0.addSelf(offset));
		vertices.put(index(i, j), vertex);
		return vertex;
	}

	/**
	 * Xedge.
	 *
	 * @param i
	 *            i: -1 .. resx+1
	 * @param j
	 *            j: -1 .. resy+1
	 *
	 * @param offset
	 * @param isolevel
	 * @return edge vertex
	 */
	private WB_Point xedge(final int i, final int j, final WB_Point offset, final double isolevel) {
		WB_Point xedge = xedges.get(index(i, j));
		if (xedge != null) {
			return xedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
		final WB_Point p1 = new WB_Point(i * dx + dx, j * dy);
		final double val0 = value(i, j);
		final double val1 = value(i + 1, j);
		xedge = new WB_Point(interp(isolevel, p0, p1, val0, val1));
		xedge.addSelf(offset);
		xedges.put(index(i, j), xedge);
		return xedge;
	}

	/**
	 * Yedge.
	 *
	 * @param i
	 *            i: -1 .. resx+1
	 * @param j
	 *            j: -1 .. resy+1
	 *
	 * @param offset
	 * @param isolevel
	 * @return edge vertex
	 */
	private WB_Point yedge(final int i, final int j, final WB_Point offset, final double isolevel) {
		WB_Point yedge = yedges.get(index(i, j));
		if (yedge != null) {
			return yedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
		final WB_Point p1 = new WB_Point(i * dx, j * dy + dy);
		final double val0 = value(i, j);
		final double val1 = value(i, j + 1);
		yedge = new WB_Point(interp(isolevel, p0, p1, val0, val1));
		yedge.addSelf(offset);

		yedges.put(index(i, j), yedge);
		return yedge;
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
			return new WB_Point(p1.xd(), p1.yd(), zFactor * isolevel);
		}
		if (WB_Epsilon.isEqual(isolevel, valp2)) {
			return new WB_Point(p2.xd(), p2.yd(), zFactor * isolevel);
		}
		if (WB_Epsilon.isEqual(valp1, valp2)) {
			return new WB_Point(p1.xd(), p1.yd(), zFactor * isolevel);
		}
		mu = (isolevel - valp1) / (valp2 - valp1);
		return new WB_Point(p1.xd() + mu * (p2.xd() - p1.xd()), p1.yd() + mu * (p2.yd() - p1.yd()), zFactor * isolevel);
	}

	/**
	 * Classify cell.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 *
	 * @return the int
	 */
	private int classifyCell(final int i, final int j) {

		if (i < 0 || j < 0 || i >= resx || j >= resy) {
			return -1;
		}

		digits = new int[8];
		int cubeindex = 0;
		int offset = 1;

		if (value(i, j) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[0] = POSITIVE;
		} else if (value(i, j) >= isolevelmin) {
			cubeindex += offset;
			digits[0] = EQUAL;
		}
		offset *= 3;
		if (value(i + 1, j) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[1] = POSITIVE;
		} else if (value(i + 1, j) >= isolevelmin) {
			cubeindex += offset;
			digits[1] = EQUAL;
		}
		offset *= 3;
		if (value(i, j + 1) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[2] = POSITIVE;
		} else if (value(i, j + 1) >= isolevelmin) {
			cubeindex += offset;
			digits[2] = EQUAL;
		}
		offset *= 3;
		if (value(i + 1, j + 1) > isolevelmax) {
			cubeindex += 2 * offset;
			digits[3] = POSITIVE;
		} else if (value(i + 1, j + 1) >= isolevelmin) {
			cubeindex += offset;
			digits[3] = EQUAL;
		}

		return cubeindex;
	}

	/**
	 * Polygonise.
	 */
	private void polygonise() {
		xedges = new IntObjectHashMap<WB_Point>();
		yedges = new IntObjectHashMap<WB_Point>();
		vertices = new IntObjectHashMap<WB_Point>();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx, cy - 0.5 * resy * dy);
		triangles = new FastList<WB_Triangle>();
		for (int i = 0; i < resx; i++) {

			for (int j = 0; j < resy; j++) {

				getPolygons(i, j, classifyCell(i, j), offset);

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
	 *
	 * @param cubeindex
	 *            the cubeindex
	 * @param offset
	 * @return the polygons
	 */
	private void getPolygons(final int i, final int j, final int cubeindex, final WB_Point offset) {
		final int[] indices = entries[cubeindex];
		final int numtris = indices[0];
		int currentindex = 1;
		for (int t = 0; t < numtris; t++) {
			final WB_Point v2 = getIsoVertex(indices[currentindex++], i, j, offset);
			final WB_Point v1 = getIsoVertex(indices[currentindex++], i, j, offset);
			final WB_Point v3 = getIsoVertex(indices[currentindex++], i, j, offset);
			triangles.add(new WB_Triangle(v1, v2, v3));

		}
	}

	public List<WB_Triangle> getTriangles() {

		polygonise();

		return triangles;
	}

	/**
	 *
	 *
	 * @param isopointindex
	 * @param i
	 * @param j
	 * @param offset
	 * @return
	 */
	private WB_Point getIsoVertex(final int isopointindex, final int i, final int j, final WB_Point offset) {
		if (isovertices[isopointindex][0] == ONVERTEX) {
			switch (isovertices[isopointindex][1]) {
			case 0:
				return vertex(i, j, offset);
			case 1:
				return vertex(i + 1, j, offset);
			case 2:
				return vertex(i, j + 1, offset);
			case 3:
				return vertex(i + 1, j + 1, offset);
			default:
				return null;
			}
		} else if (isovertices[isopointindex][0] == ONEDGE) {
			if (isovertices[isopointindex][2] == 0) {
				switch (isovertices[isopointindex][1]) {
				case 0:
					return xedge(i, j, offset, isolevelmin);
				case 1:
					return yedge(i, j, offset, isolevelmin);
				case 2:
					return yedge(i + 1, j, offset, isolevelmin);
				case 3:
					return xedge(i, j + 1, offset, isolevelmin);
				default:
					return null;
				}
			} else {
				switch (isovertices[isopointindex][1]) {
				case 0:
					return xedge(i, j, offset, isolevelmax);
				case 1:
					return yedge(i, j, offset, isolevelmax);
				case 2:
					return yedge(i + 1, j, offset, isolevelmax);
				case 3:
					return xedge(i, j + 1, offset, isolevelmax);

				default:
					return null;
				}
			}
		}
		return null;
	}
}
