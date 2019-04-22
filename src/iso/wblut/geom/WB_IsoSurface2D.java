/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
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
import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import processing.core.PApplet;
import processing.core.PImage;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class WB_IsoSurface2D {
	/**
	 *
	 */
	final static int						ONVERTEX		= 0;
	/**
	 *
	 */
	final static int						ONEDGE			= 1;
	/**
	 *
	 */
	final static int						NEGATIVE		= 0;
	/**
	 *
	 */
	final static int						EQUAL			= 1;
	/**
	 *
	 */
	final static int						POSITIVE		= 2;
	/**
	 *
	 */
	int[]									digits			= new int[8];
	/*
	 * VERTICES 000 ijk=0 100 Ijk=1 010 iJk=2 110 IJk=3 001 ijK=4 101 IjK=5 011
	 * iJK=6 111 IJK=7
	 */
	/**
	 *
	 */
	final static WB_Point[]					gridvertices	= new WB_Point[] {
			new WB_Point(0, 0), new WB_Point(1, 0), new WB_Point(0, 1),
			new WB_Point(1, 1) };
	// EDGES: 2 vertices per edge
	/**
	 *
	 */
	final static int[][]					edges			= { { 0, 1 },													// x
																															// ijk
			{ 0, 2 },																										// y
																															// ijk
			{ 1, 3 },																										// y
																															// Ijk
			{ 2, 3 },																										// x
																															// iJk
	};
	// ISOVERTICES: 8
	// type=ONVERTEX iso vertex on vertex, index in vertex list
	// type=ONEDGE iso vertex on edge, index in edge list
	/**
	 *
	 */
	final static int[][]					isovertices		= new int[][] {
			{ 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 1, 1 },
			{ 1, 2 }, { 1, 3 } };
	/**
	 *
	 */
	private int[][]							entries;
	/**
	 *
	 */
	private WB_IsoValues2D					values;
	/**
	 *
	 */
	private int								resx, resy;
	/**
	 *
	 */
	private double							cx, cy;
	/**
	 *
	 */
	private double							dx, dy;
	/**
	 *
	 */
	private double							isolevel;
	/**
	 *
	 */
	private double							boundary;
	/**
	 *
	 */
	private IntObjectHashMap<WB_Point>		xedges;
	/**
	 *
	 */
	private IntObjectHashMap<WB_Point>		yedges;
	/**
	 *
	 */
	private IntObjectHashMap<WB_Point>		vertices;
	/**
	 *
	 */
	private IntObjectHashMap<VertexRemap>	vertexremaps;
	/**
	 *
	 */
	private IntDoubleHashMap				valueremaps;
	/**
	 *
	 */
	private double							gamma;
	/**
	 *
	 */
	private boolean							invert;
	private FastList<WB_Segment>			segs;

	/**
	 *
	 */
	public WB_IsoSurface2D() {
		String line = "";
		final String cvsSplitBy = " ";
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		entries = new int[6561][];
		try {
			is = this.getClass().getClassLoader()
					.getResourceAsStream("resources/isonepcube2D.txt");
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
		gamma = 0;
		boundary = Double.NaN;
	}

	/**
	 *
	 *
	 * @param gamma
	 * @return
	 */
	public WB_IsoSurface2D setGamma(final double gamma) {
		this.gamma = gamma;
		return this;
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
	public WB_IsoSurface2D setResolution(final int resx, final int resy) {
		this.resx = resx;
		this.resy = resy;
		return this;
	}

	/**
	 * Size of cell.
	 *
	 * @param dx
	 * @param dy
	 * @return self
	 */
	public WB_IsoSurface2D setSize(final double dx, final double dy) {
		this.dx = dx;
		this.dy = dy;
		return this;
	}

	/**
	 * Values at grid points.
	 *
	 * @param values
	 *            double[resx+1][resy+1]
	 * @return self
	 */
	public WB_IsoSurface2D setValues(final double[][] values) {
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
	public WB_IsoSurface2D setValues(final float[][] values) {
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
	public WB_IsoSurface2D setValues(final WB_ScalarParameter function,
			final double xi, final double yi, final double dx, final double dy,
			final int width, final int height) {
		this.values = new WB_IsoValues2D.Function2D(function, xi, yi, dx, dy,
				width, height);
		resx = width - 1;
		resy = height - 1;
		return this;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public WB_IsoSurface2D setValues(final WB_HashGridDouble2D values) {
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
	public WB_IsoSurface2D setValues(final String path, final PApplet home,
			final int width, final int height) {
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
	public WB_IsoSurface2D setValues(final String path, final PApplet home,
			final int width, final int height, final WB_IsoValues2D.Mode mode) {
		this.values = new WB_IsoValues2D.ImageGrid2D(path, home, width, height,
				mode);
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
	public WB_IsoSurface2D setValues(final PImage image, final PApplet home,
			final int width, final int height) {
		this.values = new WB_IsoValues2D.ImageGrid2D(image, home, width,
				height);
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
	public WB_IsoSurface2D setValues(final PImage image, final PApplet home,
			final int width, final int height, final WB_IsoValues2D.Mode mode) {
		this.values = new WB_IsoValues2D.ImageGrid2D(image, home, width, height,
				mode);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	public WB_IsoSurface2D setValues(final WB_IsoValues3D values, int... s) {
		if (s.length < 1) {
			this.values = new WB_IsoValues2D.KSlice2D(values, 0);
		} else if (s.length == 1 || s[1] == 2) {
			this.values = new WB_IsoValues2D.KSlice2D(values, s[0]);
		} else if (s[1] == 0) {
			this.values = new WB_IsoValues2D.ISlice2D(values, s[0]);
		} else if (s[1] == 1) {
			this.values = new WB_IsoValues2D.JSlice2D(values, s[0]);
		}
		resx = this.values.getWidth() - 1;
		resy = this.values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public WB_IsoSurface2D setValues(final WB_IsoValues2D values) {
		this.values = values;
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 * Isolevel to render.
	 *
	 * @param v
	 *            isolevel
	 * @return self
	 */
	public WB_IsoSurface2D setIsolevel(final double v) {
		isolevel = v;
		return this;
	}

	/**
	 * Boundary level.
	 *
	 * @param v
	 *            boundary level
	 * @return self
	 */
	public WB_IsoSurface2D setBoundary(final double v) {
		boundary = v;
		return this;
	}

	/**
	 * Clear boundary level.
	 *
	 * @return self
	 */
	public WB_IsoSurface2D clearBoundary() {
		boundary = Double.NaN;
		return this;
	}

	/**
	 * Invert isosurface.
	 *
	 * @param invert
	 *            true/false
	 * @return self
	 */
	public WB_IsoSurface2D setInvert(final boolean invert) {
		this.invert = invert;
		return this;
	}

	public WB_IsoSurface2D setCenter(final WB_Coord c) {
		cx = c.xd();
		cy = c.yd();
		return this;
	}

	public List<WB_Segment> getSegments() {
		vertices = new IntObjectHashMap<WB_Point>();
		xedges = new IntObjectHashMap<WB_Point>();
		yedges = new IntObjectHashMap<WB_Point>();
		valueremaps = null;
		if (gamma > 0) {
			mapvertices();
			setvalues();
			polygonise();
			snapvertices();
		} else {
			polygonise();
		}
		return segs;
	}

	/**
	 *
	 */
	private void mapvertices() {
		vertexremaps = new IntObjectHashMap<VertexRemap>();
		valueremaps = new IntDoubleHashMap();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx,
				cy - 0.5 * resy * dy);
		if (Double.isNaN(boundary)) {
			for (int i = 0; i < resx; i++) {
				// System.out.println("HEC_IsoSurface: " + (i + 1) + " of " +
				// resx);
				for (int j = 0; j < resy; j++) {
					getPolygons(i, j, classifyCell(i, j), offset, true);
				}
			}
		} else {
			for (int i = -1; i < resx + 1; i++) {
				// System.out.println("HEC_IsoSurface: " + (i + 1) + " of " +
				// resx);
				for (int j = -1; j < resy + 1; j++) {
					getPolygons(i, j, classifyCell(i, j), offset, true);
				}
			}
		}
	}

	/**
	 *
	 */
	private void setvalues() {
		VertexRemap vr;
		for (final Object o : vertexremaps.values()) {
			vr = (VertexRemap) o;
			vr.snapvertex.set(vr.p);
			valueremaps.put(index(vr.i, vr.j), vr.originalvalue);
		}
	}

	/**
	 * Polygonise.
	 */
	private void polygonise() {
		segs = new FastList<WB_Segment>();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx,
				cy - 0.5 * resy * dy);
		if (Double.isNaN(boundary)) {
			for (int i = 0; i < resx; i++) {
				// System.out.println("HEC_IsoSurface: " + (i + 1) + " of " +
				// resx);
				for (int j = 0; j < resy; j++) {
					getPolygons(i, j, classifyCell(i, j), offset, false);
				}
			}
		} else {
			for (int i = -1; i < resx + 1; i++) {
				// System.out.println("HEC_IsoSurface: " + (i + 1) + " of " +
				// resx);
				for (int j = -1; j < resy + 1; j++) {
					getPolygons(i, j, classifyCell(i, j), offset, false);
				}
			}
		}
	}

	/**
	 *
	 */
	private void snapvertices() {
		VertexRemap vr;
		for (final Object o : vertexremaps.values()) {
			vr = (VertexRemap) o;
			vr.snapvertex.set(vr.p);
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
	 * @param dummyrun
	 * @return the polygons
	 */
	private List<WB_Segment> getPolygons(final int i, final int j,
			final int cubeindex, final WB_Point offset,
			final boolean dummyrun) {
		final int[] indices = entries[cubeindex];
		final int numsegs = indices[0];
		int currentindex = 1;
		for (int t = 0; t < numsegs; t++) {
			final WB_Point v2 = getIsoVertex(indices[currentindex++], i, j,
					offset, dummyrun);
			final WB_Point v1 = getIsoVertex(indices[currentindex++], i, j,
					offset, dummyrun);
			if (!dummyrun) {
				segs.add(new WB_Segment(v2, v1));
			}
		}
		return segs;
	}

	/**
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	private int classifyCell(final int i, final int j) {
		if (Double.isNaN(boundary)) {
			if (i < 0 || j < 0 || i >= resx || j >= resy) {
				return -1;
			}
		}
		digits = new int[8];
		int cubeindex = 0;
		int offset = 1;
		if (invert) {
			if (value(i, j) < isolevel) {
				cubeindex += 2 * offset;
				digits[0] = POSITIVE;
			} else if (value(i, j) == isolevel) {
				cubeindex += offset;
				digits[0] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j) < isolevel) {
				cubeindex += 2 * offset;
				digits[1] = POSITIVE;
			} else if (value(i + 1, j) == isolevel) {
				cubeindex += offset;
				digits[1] = EQUAL;
			}
			offset *= 3;
			if (value(i, j + 1) < isolevel) {
				cubeindex += 2 * offset;
				digits[2] = POSITIVE;
			} else if (value(i, j + 1) == isolevel) {
				cubeindex += offset;
				digits[2] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j + 1) < isolevel) {
				cubeindex += 2 * offset;
				digits[3] = POSITIVE;
			} else if (value(i + 1, j + 1) == isolevel) {
				cubeindex += offset;
				digits[3] = EQUAL;
			}
		} else {
			if (value(i, j) > isolevel) {
				cubeindex += 2 * offset;
				digits[0] = POSITIVE;
			} else if (value(i, j) == isolevel) {
				cubeindex += offset;
				digits[0] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j) > isolevel) {
				cubeindex += 2 * offset;
				digits[1] = POSITIVE;
			} else if (value(i + 1, j) == isolevel) {
				cubeindex += offset;
				digits[1] = EQUAL;
			}
			offset *= 3;
			if (value(i, j + 1) > isolevel) {
				cubeindex += 2 * offset;
				digits[2] = POSITIVE;
			} else if (value(i, j + 1) == isolevel) {
				cubeindex += offset;
				digits[2] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j + 1) > isolevel) {
				cubeindex += 2 * offset;
				digits[3] = POSITIVE;
			} else if (value(i + 1, j + 1) == isolevel) {
				cubeindex += offset;
				digits[3] = EQUAL;
			}
		}
		return cubeindex;
	}

	/**
	 *
	 * @param isopointindex
	 * @param i
	 * @param j
	 * @param offset
	 * @param dummyrun
	 * @return
	 */
	private WB_Point getIsoVertex(final int isopointindex, final int i,
			final int j, final WB_Point offset, final boolean dummyrun) {
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
			switch (isovertices[isopointindex][1]) {
				case 0:
					return xedge(i, j, offset, dummyrun);
				case 1:
					return yedge(i, j, offset, dummyrun);
				case 2:
					return yedge(i + 1, j, offset, dummyrun);
				case 3:
					return xedge(i, j + 1, offset, dummyrun);
				default:
					return null;
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param offset
	 * @return
	 */
	private WB_Point vertex(final int i, final int j, final WB_Point offset) {
		WB_Point vertex = vertices.get(index(i, j));
		if (vertex != null) {
			return vertex;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
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
	 * @param dummyrun
	 * @return edge vertex
	 */
	private WB_Point xedge(final int i, final int j, final WB_Point offset,
			final boolean dummyrun) {
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
		final WB_Point p1 = new WB_Point(i * dx + dx, j * dy);
		final double val0 = value(i, j);
		final double val1 = value(i + 1, j);
		double mu;
		if (dummyrun) {
			mu = (isolevel - val0) / (val1 - val0);
			if (i > 0 && j > 0 && i < resx && j < resy) {
				if (mu < gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = mu * dx;
						vr.i = i;
						vr.j = j;
						vr.originalvalue = value(i, j);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j, offset);
						vertexremaps.put(index(i, j), vr);
					} else {
						if (vr.closestd > mu * dx) {
							vr.closestd = mu * dx;
							vr.i = i;
							vr.j = j;
							vr.originalvalue = value(i, j);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j, offset);
						}
					}
				} else if (mu > 1 - gamma) {
					VertexRemap vr = vertexremaps.get(index(i + 1, j));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = (1 - mu) * dx;
						vr.i = i + 1;
						vr.j = j;
						vr.originalvalue = value(i + 1, j);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i + 1, j, offset);
						vertexremaps.put(index(i + 1, j), vr);
					} else {
						if (vr.closestd > (1 - mu) * dx) {
							vr.closestd = (1 - mu) * dx;
							vr.i = i + 1;
							vr.j = j;
							vr.originalvalue = value(i + 1, j);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i + 1, j, offset);
						}
					}
				}
			}
			return null;
		}
		WB_Point xedge = xedges.get(index(i, j));
		if (xedge != null) {
			return xedge;
		}
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
	 * @param offset
	 * @param dummyrun
	 * @return edge vertex
	 */
	private WB_Point yedge(final int i, final int j, final WB_Point offset,
			final boolean dummyrun) {
		WB_Point yedge = yedges.get(index(i, j));
		if (yedge != null) {
			return yedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
		final WB_Point p1 = new WB_Point(i * dx, j * dy + dy);
		final double val0 = value(i, j);
		final double val1 = value(i, j + 1);
		double mu;
		if (dummyrun) {
			mu = (isolevel - val0) / (val1 - val0);
			if (i > 0 && j > 0 && i < resx && j < resy) {
				if (mu < gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = mu * dy;
						vr.i = i;
						vr.j = j;
						vr.originalvalue = value(i, j);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j, offset);
						vertexremaps.put(index(i, j), vr);
					} else {
						if (vr.closestd > mu * dy) {
							vr.closestd = mu * dy;
							vr.i = i;
							vr.j = j;
							vr.originalvalue = value(i, j);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j, offset);
						}
					}
				} else if (mu > 1 - gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j + 1));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = (1 - mu) * dy;
						vr.i = i;
						vr.j = j + 1;
						vr.originalvalue = value(i, j + 1);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j + 1, offset);
						vertexremaps.put(index(i, j + 1), vr);
					} else {
						if (vr.closestd > (1 - mu) * dy) {
							vr.closestd = (1 - mu) * dy;
							vr.i = i;
							vr.j = j + 1;
							vr.originalvalue = value(i, j + 1);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j + 1, offset);
						}
					}
				}
			}
			return null;
		}
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
	private WB_Point interp(final double isolevel, final WB_Point p1,
			final WB_Point p2, final double valp1, final double valp2) {
		double mu;
		if (WB_Epsilon.isEqual(isolevel, valp1)) {
			return p1;
		}
		if (WB_Epsilon.isEqual(isolevel, valp2)) {
			return p2;
		}
		if (WB_Epsilon.isEqual(valp1, valp2)) {
			return p1;
		}
		mu = (isolevel - valp1) / (valp2 - valp1);
		return new WB_Point(p1.xd() + mu * (p2.xd() - p1.xd()),
				p1.yd() + mu * (p2.yd() - p1.yd()));
	}

	/**
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	private int index(final int i, final int j) {
		return i + 1 + (resx + 2) * (j + 1);
	}

	/**
	 * Value.
	 *
	 * @param i
	 *            i
	 * @param j
	 *            j
	 * @return double
	 */
	private double value(final int i, final int j) {
		if (valueremaps != null) {
			double val = valueremaps.getIfAbsent(index(i, j), Double.NaN);
			if (!Double.isNaN(val)) {
				return isolevel;
			}
		}
		if (Double.isNaN(boundary)) { // if no boundary is set i,j,k should
			// always be between o and resx,rey,resz
			return values.value(i, j);
		}
		if (i < 0 || j < 0 || i > resx || j > resy) {
			return invert ? -boundary : boundary;
		}
		return values.value(i, j);
	}

	/**
	 *
	 */
	class VertexRemap {
		/**
		 *
		 */
		int			i, j;
		/**
		 *
		 */
		double		closestd;
		/**
		 *
		 */
		WB_Point	p;
		/**
		 *
		 */
		double		originalvalue;
		/**
		 *
		 */
		WB_Point	snapvertex;
	}

	public static void main(final String[] args) {
		float[][] values = new float[81][51];
		for (int i = 0; i < 81; i++) {
			for (int j = 0; j < 51; j++) {
				values[i][j] = (float) (2.5 * Math.sin(12 * i * Math.PI / 180.0)
						* Math.cos(15 * j * Math.PI / 180.0));
			}
		}
		WB_IsoSurface2D creator = new WB_IsoSurface2D();
		creator.setResolution(80, 50);
		creator.setSize(12, 12);
		creator.setValues(values);
		creator.setIsolevel(0.2);
		creator.setBoundary(-200);
		creator.setGamma(0.0);
		creator.getSegments();
	}
}
