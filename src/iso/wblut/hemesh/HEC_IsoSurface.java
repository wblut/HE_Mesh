/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import processing.core.PApplet;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_HashGridDouble3D;
import wblut.geom.WB_IsoValues3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_RandomCircle;
import wblut.geom.WB_RandomGenerator;
import wblut.geom.WB_Segment;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_IsoSurface extends HEC_Creator {
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
			new WB_Point(0, 0, 0), new WB_Point(1, 0, 0), new WB_Point(0, 1, 0),
			new WB_Point(1, 1, 0), new WB_Point(0, 0, 1), new WB_Point(1, 0, 1),
			new WB_Point(0, 1, 1), new WB_Point(1, 1, 1), };
	// EDGES: 2 vertices per edge
	/**
	 *
	 */
	final static int[][]					edges			= { { 0, 1 },																					// x
																																							// ijk
			{ 0, 2 },																																		// y
																																							// ijk
			{ 1, 3 },																																		// y
																																							// Ijk
			{ 2, 3 },																																		// x
																																							// iJk
			{ 0, 4 },																																		// z
																																							// ijk
			{ 1, 5 },																																		// z
																																							// Ijk
			{ 2, 6 },																																		// z
																																							// iJk
			{ 3, 7 },																																		// z
																																							// IJk
			{ 4, 5 },																																		// x
																																							// ijK
			{ 4, 6 },																																		// y
																																							// ijK
			{ 5, 7 },																																		// y
																																							// IjK
			{ 6, 7 }																																		// x
																																							// iJK
	};
	// ISOVERTICES: 20
	// type=ONVERTEX iso vertex on vertex, index in vertex list
	// type=ONEDGE iso vertex on edge, index in edge list
	/**
	 *
	 */
	final static int[][]					isovertices		= new int[][] {
			{ 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 },
			{ 0, 6 }, { 0, 7 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 },
			{ 1, 4 }, { 1, 5 }, { 1, 6 }, { 1, 7 }, { 1, 8 }, { 1, 9 },
			{ 1, 10 }, { 1, 11 } };
	/**
	 *
	 */
	int[][]									entries;
	/**
	 *
	 */
	private WB_IsoValues3D					values;
	/**
	 *
	 */
	private int								resx, resy, resz;
	/**
	 *
	 */
	private double							cx, cy, cz;
	/**
	 *
	 */
	private double							dx, dy, dz;
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
	private IntObjectHashMap<HE_Vertex>		xedges;
	/**
	 *
	 */
	private IntObjectHashMap<HE_Vertex>		yedges;
	/**
	 *
	 */
	private IntObjectHashMap<HE_Vertex>		zedges;
	/**
	 *
	 */
	private IntObjectHashMap<HE_Vertex>		vertices;
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
	HE_Mesh									mesh;
	/**
	 *
	 */
	private boolean							invert;
	WB_ScalarParameter						valueFactor;
	WB_ScalarParameter						valueShift;

	/**
	 *
	 */
	public HEC_IsoSurface() {
		super();
		String line = "";
		final String cvsSplitBy = " ";
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		entries = new int[6561][];
		try {
			is = this.getClass().getClassLoader()
					.getResourceAsStream("resources/isonepcube3D.txt");
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
		gamma = 0.3;
		setOverride(true);
		boundary = Double.NaN;
		valueFactor = WB_ScalarParameter.ONE;
		valueShift = WB_ScalarParameter.ZERO;
		setCheckManifold(true);
	}

	/**
	 *
	 *
	 * @param gamma
	 * @return
	 */
	public HEC_IsoSurface setGamma(final double gamma) {
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
	 * @param resz
	 *            the resz
	 * @return self
	 */
	public HEC_IsoSurface setResolution(final int resx, final int resy,
			final int resz) {
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
	public HEC_IsoSurface setSize(final double dx, final double dy,
			final double dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		return this;
	}

	/**
	 * Isolevel to render.
	 *
	 * @param v
	 *            isolevel
	 * @return self
	 */
	public HEC_IsoSurface setIsolevel(final double v) {
		isolevel = v;
		return this;
	}

	public HEC_IsoSurface setValueFactor(final WB_ScalarParameter v) {
		valueFactor = v;
		return this;
	}

	public HEC_IsoSurface setValueShift(final WB_ScalarParameter v) {
		valueShift = v;
		return this;
	}

	/**
	 * Values at grid points.
	 *
	 * @param values
	 *            double[resx+1][resy+1][resz+1]
	 * @return self
	 */
	public HEC_IsoSurface setValues(final double[][][] values) {
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
	public HEC_IsoSurface setValues(final float[][][] values) {
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
	public HEC_IsoSurface setValues(final WB_ScalarParameter function,
			final double xi, final double yi, final double zi, final double dx,
			final double dy, final double dz, final int sizeI, final int sizeJ,
			final int sizeK) {
		this.values = new WB_IsoValues3D.Function3D(function, xi, yi, zi, dx,
				dy, dz, sizeI, sizeJ, sizeK);
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
	public HEC_IsoSurface setValues(final WB_HashGridDouble3D values) {
		this.values = new WB_IsoValues3D.HashGrid3D(values);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	/**
	 *
	 * @param images
	 * @param home
	 * @param sizeI
	 * @param sizeJ
	 * @param sizeK
	 * @return
	 */
	public HEC_IsoSurface setValues(final String[] images, final PApplet home,
			final int sizeI, final int sizeJ, final int sizeK) {
		this.values = new WB_IsoValues3D.ImageStack3D(images, home, sizeI,
				sizeJ, sizeK);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	/**
	 *
	 * @param images
	 * @param home
	 * @param sizeI
	 * @param sizeJ
	 * @param sizeK
	 * @param mode
	 * @return
	 */
	public HEC_IsoSurface setValues(final String[] images, final PApplet home,
			final int sizeI, final int sizeJ, final int sizeK,
			final WB_IsoValues3D.Mode mode) {
		this.values = new WB_IsoValues3D.ImageStack3D(images, home, sizeI,
				sizeJ, sizeK, mode);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	public HEC_IsoSurface setValues(final WB_IsoValues3D values) {
		this.values = values;
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	/**
	 * Boundary level.
	 *
	 * @param v
	 *            boundary level
	 * @return self
	 */
	public HEC_IsoSurface setBoundary(final double v) {
		boundary = v;
		return this;
	}

	/**
	 * Clear boundary level.
	 *
	 * @return self
	 */
	public HEC_IsoSurface clearBoundary() {
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
	public HEC_IsoSurface setInvert(final boolean invert) {
		this.invert = invert;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEC_Creator#setCenter(wblut.geom.WB_Point3d)
	 */
	@Override
	public HEC_IsoSurface setCenter(final WB_Coord c) {
		cx = c.xd();
		cy = c.yd();
		cz = c.zd();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.creators.HEB_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		mesh = new HE_Mesh();
		vertices = new IntObjectHashMap<HE_Vertex>();
		xedges = new IntObjectHashMap<HE_Vertex>();
		yedges = new IntObjectHashMap<HE_Vertex>();
		zedges = new IntObjectHashMap<HE_Vertex>();
		valueremaps = null;
		if (gamma > 0) {
			mapvertices();
			setvalues();
			polygonise();
			snapvertices();
		} else {
			polygonise();
		}
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		HE_MeshOp.removeUnconnectedElements(mesh);
		return mesh;
	}

	/**
	 *
	 */
	private void mapvertices() {
		vertexremaps = new IntObjectHashMap<VertexRemap>();
		valueremaps = new IntDoubleHashMap();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx,
				cy - 0.5 * resy * dy, cz - 0.5 * resz * dz);
		if (Double.isNaN(boundary)) {
			for (int k = 0; k < resz; k++) {
				for (int i = 0; i < resx; i++) {
					// System.out.println("HEC_IsoSurface: " + (i + 1) + " of "
					// +
					// resx);
					for (int j = 0; j < resy; j++) {
						getPolygons(i, j, k, classifyCell(i, j, k), offset,
								true);
					}
				}
			}
		} else {
			for (int k = -1; k < resz + 1; k++) {
				for (int i = -1; i < resx + 1; i++) {
					// System.out.println("HEC_IsoSurface: " + (i + 1) + " of "
					// +
					// resx);
					for (int j = -1; j < resy + 1; j++) {
						getPolygons(i, j, k, classifyCell(i, j, k), offset,
								true);
					}
				}
			}
		}
	}

	/**
	 *
	 */
	void setvalues() {
		VertexRemap vr;
		for (final Object o : vertexremaps.values()) {
			vr = (VertexRemap) o;
			vr.snapvertex.set(vr.p);
			valueremaps.put(index(vr.i, vr.j, vr.k), vr.originalvalue);
		}
	}

	/**
	 * Polygonise.
	 */
	private void polygonise() {
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx,
				cy - 0.5 * resy * dy, cz - 0.5 * resz * dz);
		if (Double.isNaN(boundary)) {
			for (int k = 0; k < resz; k++) {
				for (int i = 0; i < resx; i++) {
					// System.out.println("HEC_IsoSurface: " + (i + 1) + " of "
					// +
					// resx);
					for (int j = 0; j < resy; j++) {
						getPolygons(i, j, k, classifyCell(i, j, k), offset,
								false);
					}
				}
			}
		} else {
			for (int k = -1; k < resz + 1; k++) {
				for (int i = -1; i < resx + 1; i++) {
					// System.out.println("HEC_IsoSurface: " + (i + 1) + " of "
					// +
					// resx);
					for (int j = -1; j < resy + 1; j++) {
						getPolygons(i, j, k, classifyCell(i, j, k), offset,
								false);
					}
				}
			}
		}
	}

	/**
	 *
	 */
	void snapvertices() {
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
	 * @param k
	 *            the k
	 * @param cubeindex
	 *            the cubeindex
	 * @param offset
	 * @param dummyrun
	 * @return the polygons
	 */
	private void getPolygons(final int i, final int j, final int k,
			final int cubeindex, final WB_Point offset,
			final boolean dummyrun) {
		final int[] indices = entries[cubeindex];
		final int numtris = indices[0];
		int currentindex = 1;
		for (int t = 0; t < numtris; t++) {
			final HE_Face f = new HE_Face();
			final HE_Vertex v2 = getIsoVertex(indices[currentindex++], i, j, k,
					offset, dummyrun);
			final HE_Vertex v1 = getIsoVertex(indices[currentindex++], i, j, k,
					offset, dummyrun);
			final HE_Vertex v3 = getIsoVertex(indices[currentindex++], i, j, k,
					offset, dummyrun);
			if (!dummyrun) {
				final HE_Halfedge he1 = new HE_Halfedge();
				final HE_Halfedge he2 = new HE_Halfedge();
				final HE_Halfedge he3 = new HE_Halfedge();
				mesh.setNext(he1, he2);
				mesh.setNext(he2, he3);
				mesh.setNext(he3, he1);
				mesh.setFace(he1, f);
				mesh.setFace(he2, f);
				mesh.setFace(he3, f);
				mesh.setVertex(he1, v1);
				mesh.setHalfedge(v1, he1);
				mesh.setVertex(he2, v2);
				mesh.setHalfedge(v2, he2);
				mesh.setVertex(he3, v3);
				mesh.setHalfedge(v3, he3);
				mesh.setHalfedge(f, he1);
				mesh.add(f);
				mesh.add(he1);
				mesh.add(he2);
				mesh.add(he3);
			}
		}
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
		if (Double.isNaN(boundary)) {
			if (i < 0 || j < 0 || k < 0 || i >= resx || j >= resy
					|| k >= resz) {
				return -1;
			}
		}
		digits = new int[8];
		int cubeindex = 0;
		int offset = 1;
		if (invert) {
			if (value(i, j, k) < isolevel) {
				cubeindex += 2 * offset;
				digits[0] = POSITIVE;
			} else if (value(i, j, k) == isolevel) {
				cubeindex += offset;
				digits[0] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j, k) < isolevel) {
				cubeindex += 2 * offset;
				digits[1] = POSITIVE;
			} else if (value(i + 1, j, k) == isolevel) {
				cubeindex += offset;
				digits[1] = EQUAL;
			}
			offset *= 3;
			if (value(i, j + 1, k) < isolevel) {
				cubeindex += 2 * offset;
				digits[2] = POSITIVE;
			} else if (value(i, j + 1, k) == isolevel) {
				cubeindex += offset;
				digits[2] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k) < isolevel) {
				cubeindex += 2 * offset;
				digits[3] = POSITIVE;
			} else if (value(i + 1, j + 1, k) == isolevel) {
				cubeindex += offset;
				digits[3] = EQUAL;
			}
			offset *= 3;
			if (value(i, j, k + 1) < isolevel) {
				cubeindex += 2 * offset;
				digits[4] = POSITIVE;
			} else if (value(i, j, k + 1) == isolevel) {
				cubeindex += offset;
				digits[4] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j, k + 1) < isolevel) {
				cubeindex += 2 * offset;
				digits[5] = POSITIVE;
			} else if (value(i + 1, j, k + 1) == isolevel) {
				cubeindex += offset;
				digits[5] = EQUAL;
			}
			offset *= 3;
			if (value(i, j + 1, k + 1) < isolevel) {
				cubeindex += 2 * offset;
				digits[6] = POSITIVE;
			} else if (value(i, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
				digits[6] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k + 1) < isolevel) {
				cubeindex += 2 * offset;
				digits[7] = POSITIVE;
			} else if (value(i + 1, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
				digits[7] = EQUAL;
			}
		} else {
			if (value(i, j, k) > isolevel) {
				cubeindex += 2 * offset;
				digits[0] = POSITIVE;
			} else if (value(i, j, k) == isolevel) {
				cubeindex += offset;
				digits[0] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j, k) > isolevel) {
				cubeindex += 2 * offset;
				digits[1] = POSITIVE;
			} else if (value(i + 1, j, k) == isolevel) {
				cubeindex += offset;
				digits[1] = EQUAL;
			}
			offset *= 3;
			if (value(i, j + 1, k) > isolevel) {
				cubeindex += 2 * offset;
				digits[2] = POSITIVE;
			} else if (value(i, j + 1, k) == isolevel) {
				cubeindex += offset;
				digits[2] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k) > isolevel) {
				cubeindex += 2 * offset;
				digits[3] = POSITIVE;
			} else if (value(i + 1, j + 1, k) == isolevel) {
				cubeindex += offset;
				digits[3] = EQUAL;
			}
			offset *= 3;
			if (value(i, j, k + 1) > isolevel) {
				cubeindex += 2 * offset;
				digits[4] = POSITIVE;
			} else if (value(i, j, k + 1) == isolevel) {
				cubeindex += offset;
				digits[4] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j, k + 1) > isolevel) {
				cubeindex += 2 * offset;
				digits[5] = POSITIVE;
			} else if (value(i + 1, j, k + 1) == isolevel) {
				cubeindex += offset;
				digits[5] = EQUAL;
			}
			offset *= 3;
			if (value(i, j + 1, k + 1) > isolevel) {
				cubeindex += 2 * offset;
				digits[6] = POSITIVE;
			} else if (value(i, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
				digits[6] = EQUAL;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k + 1) > isolevel) {
				cubeindex += 2 * offset;
				digits[7] = POSITIVE;
			} else if (value(i + 1, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
				digits[7] = EQUAL;
			}
		}
		return cubeindex;
	}

	/**
	 *
	 *
	 * @param isopointindex
	 * @param i
	 * @param j
	 * @param k
	 * @param offset
	 * @param dummyrun
	 * @return
	 */
	HE_Vertex getIsoVertex(final int isopointindex, final int i, final int j,
			final int k, final WB_Point offset, final boolean dummyrun) {
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
			switch (isovertices[isopointindex][1]) {
				case 0:
					return xedge(i, j, k, offset, dummyrun);
				case 1:
					return yedge(i, j, k, offset, dummyrun);
				case 2:
					return yedge(i + 1, j, k, offset, dummyrun);
				case 3:
					return xedge(i, j + 1, k, offset, dummyrun);
				case 4:
					return zedge(i, j, k, offset, dummyrun);
				case 5:
					return zedge(i + 1, j, k, offset, dummyrun);
				case 6:
					return zedge(i, j + 1, k, offset, dummyrun);
				case 7:
					return zedge(i + 1, j + 1, k, offset, dummyrun);
				case 8:
					return xedge(i, j, k + 1, offset, dummyrun);
				case 9:
					return yedge(i, j, k + 1, offset, dummyrun);
				case 10:
					return yedge(i + 1, j, k + 1, offset, dummyrun);
				case 11:
					return xedge(i, j + 1, k + 1, offset, dummyrun);
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
	 * @param k
	 * @param offset
	 * @return
	 */
	private HE_Vertex vertex(final int i, final int j, final int k,
			final WB_Point offset) {
		HE_Vertex vertex = vertices.get(index(i, j, k));
		if (vertex != null) {
			return vertex;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		vertex = new HE_Vertex(p0.addSelf(offset));
		mesh.add(vertex);
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
	 * @param dummyrun
	 * @return edge vertex
	 */
	private HE_Vertex xedge(final int i, final int j, final int k,
			final WB_Point offset, final boolean dummyrun) {
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx + dx, j * dy, k * dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i + 1, j, k);
		double mu;
		if (dummyrun) {
			mu = (isolevel - val0) / (val1 - val0);
			if (i > 0 && j > 0 && k > 0 && i < resx && j < resy && k < resz) {
				if (mu < gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j, k));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = mu * dx;
						vr.i = i;
						vr.j = j;
						vr.k = k;
						vr.originalvalue = value(i, j, k);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j, k, offset);
						vertexremaps.put(index(i, j, k), vr);
					} else {
						if (vr.closestd > mu * dx) {
							vr.closestd = mu * dx;
							vr.i = i;
							vr.j = j;
							vr.k = k;
							vr.originalvalue = value(i, j, k);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j, k, offset);
						}
					}
				} else if (mu > 1 - gamma) {
					VertexRemap vr = vertexremaps.get(index(i + 1, j, k));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = (1 - mu) * dx;
						vr.i = i + 1;
						vr.j = j;
						vr.k = k;
						vr.originalvalue = value(i + 1, j, k);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i + 1, j, k, offset);
						vertexremaps.put(index(i + 1, j, k), vr);
					} else {
						if (vr.closestd > (1 - mu) * dx) {
							vr.closestd = (1 - mu) * dx;
							vr.i = i + 1;
							vr.j = j;
							vr.k = k;
							vr.originalvalue = value(i + 1, j, k);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i + 1, j, k, offset);
						}
					}
				}
			}
			return null;
		}
		HE_Vertex xedge = xedges.get(index(i, j, k));
		if (xedge != null) {
			return xedge;
		}
		xedge = new HE_Vertex(interp(isolevel, p0, p1, val0, val1));
		xedge.getPosition().addSelf(offset);
		if (!dummyrun) {
			mesh.add(xedge);
		}
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
	 * @param dummyrun
	 * @return edge vertex
	 */
	private HE_Vertex yedge(final int i, final int j, final int k,
			final WB_Point offset, final boolean dummyrun) {
		HE_Vertex yedge = yedges.get(index(i, j, k));
		if (yedge != null) {
			return yedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx, j * dy + dy, k * dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i, j + 1, k);
		double mu;
		if (dummyrun) {
			mu = (isolevel - val0) / (val1 - val0);
			if (i > 0 && j > 0 && k > 0 && i < resx && j < resy && k < resz) {
				if (mu < gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j, k));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = mu * dy;
						vr.i = i;
						vr.j = j;
						vr.k = k;
						vr.originalvalue = value(i, j, k);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j, k, offset);
						vertexremaps.put(index(i, j, k), vr);
					} else {
						if (vr.closestd > mu * dy) {
							vr.closestd = mu * dy;
							vr.i = i;
							vr.j = j;
							vr.k = k;
							vr.originalvalue = value(i, j, k);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j, k, offset);
						}
					}
				} else if (mu > 1 - gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j + 1, k));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = (1 - mu) * dy;
						vr.i = i;
						vr.j = j + 1;
						vr.k = k;
						vr.originalvalue = value(i, j + 1, k);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j + 1, k, offset);
						vertexremaps.put(index(i, j + 1, k), vr);
					} else {
						if (vr.closestd > (1 - mu) * dy) {
							vr.closestd = (1 - mu) * dy;
							vr.i = i;
							vr.j = j + 1;
							vr.k = k;
							vr.originalvalue = value(i, j + 1, k);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j + 1, k, offset);
						}
					}
				}
			}
			return null;
		}
		yedge = new HE_Vertex(interp(isolevel, p0, p1, val0, val1));
		yedge.getPosition().addSelf(offset);
		if (!dummyrun) {
			mesh.add(yedge);
		}
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
	 * @param dummyrun
	 * @return edge vertex
	 */
	private HE_Vertex zedge(final int i, final int j, final int k,
			final WB_Point offset, final boolean dummyrun) {
		HE_Vertex zedge = zedges.get(index(i, j, k));
		if (zedge != null) {
			return zedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx, j * dy, k * dz + dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i, j, k + 1);
		double mu;
		if (dummyrun) {
			mu = (isolevel - val0) / (val1 - val0);
			if (i > 0 && j > 0 && k > 0 && i < resx && j < resy && k < resz) {
				if (mu < gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j, k));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = mu * dz;
						vr.i = i;
						vr.j = j;
						vr.k = k;
						vr.originalvalue = value(i, j, k);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j, k, offset);
						vertexremaps.put(index(i, j, k), vr);
					} else {
						if (vr.closestd > mu * dz) {
							vr.closestd = mu * dz;
							vr.i = i;
							vr.j = j;
							vr.k = k;
							vr.originalvalue = value(i, j, k);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j, k, offset);
						}
					}
				} else if (mu > 1 - gamma) {
					VertexRemap vr = vertexremaps.get(index(i, j, k + 1));
					if (vr == null) {
						vr = new VertexRemap();
						vr.closestd = (1 - mu) * dz;
						vr.i = i;
						vr.j = j;
						vr.k = k + 1;
						vr.originalvalue = value(i, j, k + 1);
						vr.p = interp(isolevel, p0, p1, val0, val1)
								.addSelf(offset);
						vr.snapvertex = vertex(i, j, k + 1, offset);
						vertexremaps.put(index(i, j, k + 1), vr);
					} else {
						if (vr.closestd > (1 - mu) * dz) {
							vr.closestd = (1 - mu) * dz;
							vr.i = i;
							vr.j = j;
							vr.k = k + 1;
							vr.originalvalue = value(i, j, k + 1);
							vr.p = interp(isolevel, p0, p1, val0, val1)
									.addSelf(offset);
							vr.snapvertex = vertex(i, j, k + 1, offset);
						}
					}
				}
			}
			return null;
		}
		zedge = new HE_Vertex(interp(isolevel, p0, p1, val0, val1));
		zedge.getPosition().addSelf(offset);
		if (!dummyrun) {
			mesh.add(zedge);
		}
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
				p1.yd() + mu * (p2.yd() - p1.yd()),
				p1.zd() + mu * (p2.zd() - p1.zd()));
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
		if (valueremaps != null) {
			double val = valueremaps.getIfAbsent(index(i, j, k), Double.NaN);
			if (!Double.isNaN(val)) {
				return isolevel;
			}
		}
		if (Double.isNaN(boundary)) { // if no boundary is set i,j,k should
			// always be between o and resx,rey,resz
			return values.getValue(i, j, k) * valueFactor.evaluate(i, j, k)
					+ valueShift.evaluate(i, j, k);
		}
		if (i < 0 || j < 0 || k < 0 || i > resx || j > resy || k > resz) {
			return invert ? -boundary : boundary;
		}
		return values.getValue(i, j, k) * valueFactor.evaluate(i, j, k)
				+ valueShift.evaluate(i, j, k);
	}

	/**
	 *
	 */
	class VertexRemap {
		/**
		 *
		 */
		int			i, j, k;
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
		HE_Vertex	snapvertex;
	}

	public static void main(final String[] args) {
		float[][][] values = new float[121][121][201];
		for (int i = 0; i < 121; i++) {
			for (int j = 0; j < 121; j++) {
				for (int k = 0; k < 201; k++) {
					values[i][j][k] = 1000000;
				}
			}
		}
		WB_RandomGenerator rpo = new WB_RandomCircle().setRadius(56).setOffset(60,
				60, 0);
		WB_RandomGenerator rpi = new WB_RandomCircle().setRadius(56).setOffset(60,
				60, 200);
		for (int s = 0; s < 80; s++) {
			WB_Segment seg = new WB_Segment(rpi.nextPoint(), rpo.nextPoint());
			for (int i = 0; i < 121; i++) {
				for (int j = 0; j < 121; j++) {
					for (int k = 0; k < 201; k++) {
						float r = 1.0f;
						float d = (float) WB_GeometryOp
								.getDistance3D(new WB_Point(i, j, k), seg);
						float e = PApplet
								.max(r - PApplet.abs(values[i][j][k] - d), 0);
						values[i][j][k] = PApplet.min(values[i][j][k], d)
								- e * e * 0.25f / r;
					}
				}
			}
		}
		HEC_IsoSurface creator = new HEC_IsoSurface();
		creator.setResolution(120, 120, 200);
		creator.setSize(4, 4, 4);
		creator.setValues(values);
		creator.setIsolevel(2);
		creator.setBoundary(1000);
		creator.setGamma(0.3);
		HE_Mesh mesh = new HE_Mesh(creator);
		mesh.stats();
	}
}
