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

import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import processing.core.PApplet;
import processing.core.PImage;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_HashGridDouble2D;
import wblut.geom.WB_IsoValues2D;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_IsoSurfaceVOL2D extends HEC_Creator {
	/**
	 *
	 */
	final static int					ONVERTEX		= 0;
	/**
	 *
	 */
	final static int					ONEDGE			= 1;
	/**
	 *
	 */
	final static int					NEGATIVE		= 0;
	/**
	 *
	 */
	final static int					EQUAL			= 1;
	/**
	 *
	 */
	final static int					POSITIVE		= 2;
	/**
	 *
	 */
	private int[]						digits			= new int[4];
	/*
	 * VERTICES 00 ij=0 10 Ij=1 01 iJ=2 11 IJ=3
	 */
	/**
	 *
	 */
	final static WB_Point[]				gridvertices	= new WB_Point[] {
			new WB_Point(0, 0), new WB_Point(1, 0), new WB_Point(0, 1),
			new WB_Point(1, 1) };
	// EDGES: 2 vertices per edge
	/**
	 *
	 */
	final static int[][]				edges			= { { 0, 1 },					// x
																						// ij
			{ 0, 2 },																	// y
																						// ij
			{ 1, 3 }																	// y
																						// Ij
	};
	/**
	 *
	 */
	private int[][]						entries;
	/**
	 *
	 */
	private WB_IsoValues2D				values;
	// type=ONVERTEX iso vertex on vertex, index in vertex list
	// type=ONEDGE iso vertex on edge, index in edge list, 0=lower
	// threshold,1=higher threshold
	/**
	 *
	 */
	final static int[][]				isovertices		= new int[][] {
			{ 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 }, { 1, 2, 0 },
			{ 1, 2, 1 }, { 1, 3, 0 }, { 1, 3, 1 }, { 0, 0 }, { 0, 1 }, { 0, 2 },
			{ 0, 3 } };
	/**
	 *
	 */
	private int							resx, resy;
	/**
	 *
	 */
	private double						cx, cy;
	/**
	 *
	 */
	private double						dx, dy;
	/**
	 *
	 */
	private double						isolevelmin, isolevelmax;
	/**
	 *
	 */
	private IntObjectHashMap<HE_Vertex>	xedges;
	/**
	 *
	 */
	private IntObjectHashMap<HE_Vertex>	yedges;
	/**
	 *
	 */
	private IntObjectHashMap<HE_Vertex>	vertices;
	/**
	 *
	 */
	private HE_Mesh						mesh;
	/**
	 *
	 */
	private double						zFactor;

	/**
	 *
	 */
	public HEC_IsoSurfaceVOL2D() {
		super();
		String line = "";
		final String cvsSplitBy = " ";
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		entries = new int[6561][];
		try {
			is = this.getClass().getClassLoader()
					.getResourceAsStream("resources/ivolcube2D.txt");
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
		setOverride(true);
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
	public HEC_IsoSurfaceVOL2D setResolution(final int resx, final int resy) {
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
	public HEC_IsoSurfaceVOL2D setSize(final double dx, final double dy) {
		this.dx = dx;
		this.dy = dy;
		return this;
	}

	public HEC_IsoSurfaceVOL2D setZFactor(final double zf) {
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
	public HEC_IsoSurfaceVOL2D setValues(final double[][] values) {
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
	public HEC_IsoSurfaceVOL2D setValues(final float[][] values) {
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
	public HEC_IsoSurfaceVOL2D setValues(final WB_ScalarParameter function,
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
	public HEC_IsoSurfaceVOL2D setValues(final WB_HashGridDouble2D values) {
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
	public HEC_IsoSurfaceVOL2D setValues(final String path, final PApplet home,
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
	public HEC_IsoSurfaceVOL2D setValues(final String path, final PApplet home,
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
	public HEC_IsoSurfaceVOL2D setValues(final PImage image, final PApplet home,
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
	public HEC_IsoSurfaceVOL2D setValues(final PImage image, final PApplet home,
			final int width, final int height, final WB_IsoValues2D.Mode mode) {
		this.values = new WB_IsoValues2D.ImageGrid2D(image, home, width, height,
				mode);
		resx = values.getWidth() - 1;
		resy = values.getHeight() - 1;
		return this;
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public HEC_IsoSurfaceVOL2D setValues(final WB_IsoValues2D values) {
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
	public HEC_IsoSurfaceVOL2D setIsolevel(final double isolevelmin,
			final double isolevelmax) {
		this.isolevelmin = isolevelmin;
		this.isolevelmax = isolevelmax;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEC_Creator#setCenter(wblut.geom.WB_Point3d)
	 */
	@Override
	public HEC_IsoSurfaceVOL2D setCenter(final WB_Coord c) {
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
	private HE_Vertex vertex(final int i, final int j, final WB_Point offset) {
		HE_Vertex vertex = vertices.get(index(i, j));
		if (vertex != null) {
			return vertex;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy, zFactor * value(i, j));
		vertex = new HE_Vertex(p0.addSelf(offset));
		mesh.add(vertex);
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
	private HE_Vertex xedge(final int i, final int j, final WB_Point offset,
			final double isolevel) {
		HE_Vertex xedge = xedges.get(index(i, j));
		if (xedge != null) {
			return xedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
		final WB_Point p1 = new WB_Point(i * dx + dx, j * dy);
		final double val0 = value(i, j);
		final double val1 = value(i + 1, j);
		xedge = new HE_Vertex(interp(isolevel, p0, p1, val0, val1));
		xedge.getPosition().addSelf(offset);
		mesh.add(xedge);
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
	private HE_Vertex yedge(final int i, final int j, final WB_Point offset,
			final double isolevel) {
		HE_Vertex yedge = yedges.get(index(i, j));
		if (yedge != null) {
			return yedge;
		}
		final WB_Point p0 = new WB_Point(i * dx, j * dy);
		final WB_Point p1 = new WB_Point(i * dx, j * dy + dy);
		final double val0 = value(i, j);
		final double val1 = value(i, j + 1);
		yedge = new HE_Vertex(interp(isolevel, p0, p1, val0, val1));
		yedge.getPosition().addSelf(offset);
		mesh.add(yedge);
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
	private HE_Vertex interp(final double isolevel, final WB_Point p1,
			final WB_Point p2, final double valp1, final double valp2) {
		double mu;
		if (WB_Epsilon.isEqual(isolevel, valp1)) {
			return new HE_Vertex(p1.xd(), p1.yd(), zFactor * isolevel);
		}
		if (WB_Epsilon.isEqual(isolevel, valp2)) {
			return new HE_Vertex(p2.xd(), p2.yd(), zFactor * isolevel);
		}
		if (WB_Epsilon.isEqual(valp1, valp2)) {
			return new HE_Vertex(p1.xd(), p1.yd(), zFactor * isolevel);
		}
		mu = (isolevel - valp1) / (valp2 - valp1);
		return new HE_Vertex(p1.xd() + mu * (p2.xd() - p1.xd()),
				p1.yd() + mu * (p2.yd() - p1.yd()), zFactor * isolevel);
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
		xedges = new IntObjectHashMap<HE_Vertex>();
		yedges = new IntObjectHashMap<HE_Vertex>();
		vertices = new IntObjectHashMap<HE_Vertex>();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx,
				cy - 0.5 * resy * dy);
		for (int i = 0; i < resx; i++) {
			// System.out.println("HEC_IsoSurface: " + (i + 1) + " of " +
			// resx);
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
	private void getPolygons(final int i, final int j, final int cubeindex,
			final WB_Point offset) {
		final int[] indices = entries[cubeindex];
		final int numtris = indices[0];
		int currentindex = 1;
		for (int t = 0; t < numtris; t++) {
			final HE_Face f = new HE_Face();
			final HE_Vertex v2 = getIsoVertex(indices[currentindex++], i, j,
					offset);
			final HE_Vertex v1 = getIsoVertex(indices[currentindex++], i, j,
					offset);
			final HE_Vertex v3 = getIsoVertex(indices[currentindex++], i, j,
					offset);
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

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.creators.HEB_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		mesh = new HE_Mesh();
		polygonise();
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		return mesh;
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
	private HE_Vertex getIsoVertex(final int isopointindex, final int i,
			final int j, final WB_Point offset) {
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

	public static void main(final String[] args) {
		int num = 50;
		int resx = 100;
		int resy = 100;
		float dx = 6;
		float dy = 6;
		WB_Point center = new WB_Point();
		float[][] values = new float[resx + 1][resy + 1];
		WB_Segment[] segs = new WB_Segment[num];
		for (int i = 0; i < num; i++) {
			WB_Point p1 = new WB_Point(Math.random() * resx,
					Math.random() * resy);
			WB_Point p2 = new WB_Point(Math.random() * resx,
					Math.random() * resy);
			segs[i] = new WB_Segment(p1, p2);
		}
		double radius = 2.0;
		double r;
		for (int i = 0; i < resx + 1; i++) {
			for (int j = 1; j < resy; j++) {
				values[i][j] = 0;// Float.POSITIVE_INFINITY;
			}
		}
		for (int i = 0; i < resx + 1; i++) {
			for (int j = 0; j < resy + 1; j++) {
				for (int s = 0; s < num; s++) {
					r = WB_GeometryOp.getSqDistance3D(new WB_Point(i, j),
							segs[s]) / (radius * radius);
					values[i][j] = Math.max(1f / (float) r, values[i][j]);//
					// values[i][j][k]+=(r>=1.0)?0: 1-r * r * r * (r * (r * 6 -
					// 15) + 10) ;
				}
			}
		}
		HEC_IsoSurfaceVOL2D iso = new HEC_IsoSurfaceVOL2D()
				.setResolution(resx, resy).setValues(values).setSize(dx, dy)
				.setCenter(center).setIsolevel(0.5, 1.5);
		new HE_Mesh(iso);
	}
}
