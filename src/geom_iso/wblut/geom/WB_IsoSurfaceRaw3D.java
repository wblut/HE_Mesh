package wblut.geom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import processing.core.PApplet;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

public class WB_IsoSurfaceRaw3D {
	final static int ONVERTEX = 1;
	final static int ONEDGE = 2;
	final static int NEGATIVE = 4;
	final static int EQUAL = 8;
	final static int POSITIVE = 16;
	final static WB_Point[] gridvertices = new WB_Point[] { new WB_Point(0, 0, 0), new WB_Point(1, 0, 0),
			new WB_Point(0, 1, 0), new WB_Point(1, 1, 0), new WB_Point(0, 0, 1), new WB_Point(1, 0, 1),
			new WB_Point(0, 1, 1), new WB_Point(1, 1, 1), };
	// EDGES: 2 vertices per edge
	final static int[][] edges = { { 0, 1 }, { 1, 3 }, { 2, 3 }, { 0, 4 }, { 1, 5 }, { 2, 6 }, { 3, 7 }, { 4, 5 },
			{ 4, 6 }, { 5, 7 }, { 6, 7 } };
	// ISOVERTICES: 20
	// type=ONVERTEX iso vertex on vertex, index in vertex list
	// type=ONEDGE iso vertex on edge, index in edge list
	final static int[][] isovertices = new int[][] { { ONVERTEX, 0 }, { ONVERTEX, 1 }, { ONVERTEX, 2 }, { ONVERTEX, 3 },
			{ ONVERTEX, 4 }, { ONVERTEX, 5 }, { ONVERTEX, 6 }, { ONVERTEX, 7 }, { ONEDGE, 0 }, { ONEDGE, 1 },
			{ ONEDGE, 2 }, { ONEDGE, 3 }, { ONEDGE, 4 }, { ONEDGE, 5 }, { ONEDGE, 6 }, { ONEDGE, 7 }, { ONEDGE, 8 },
			{ ONEDGE, 9 }, { ONEDGE, 10 }, { ONEDGE, 11 } };
	private final int[][] entries;
	private WB_IsoValues3D values;
	private int resx, resy, resz;
	private double cx, cy, cz;
	private double dx, dy, dz;
	private double isolevel;
	private double boundary;
	private WB_IndexedObjectMap<WB_Point> xedges;
	private WB_IndexedObjectMap<WB_Point> yedges;
	private WB_IndexedObjectMap<WB_Point> zedges;
	private WB_IndexedObjectMap<WB_Point> vertices;
	private boolean invert;
	private WB_CoordList triangles;

	public WB_IsoSurfaceRaw3D() {
		String line = "";
		final String cvsSplitBy = " ";
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		entries = new int[6561][];
		try {
			is = this.getClass().getClassLoader().getResourceAsStream("resources/isonepcube3D.txt");
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
		boundary = Double.NaN;
	}

	public WB_IsoSurfaceRaw3D setResolution(final int resx, final int resy, final int resz) {
		this.resx = resx;
		this.resy = resy;
		this.resz = resz;
		return this;
	}

	public WB_IsoSurfaceRaw3D setSize(final double dx, final double dy, final double dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final double[][][] values) {
		this.values = new WB_IsoValues3D.GridRaw3D(values);
		resx = values.length - 1;
		resy = resx > 0 ? values[0].length - 1 : 0;
		resz = resy > 0 ? values[0][0].length - 1 : 0;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final float[][][] values) {
		this.values = new WB_IsoValues3D.Grid3D(values);
		resx = values.length - 1;
		resy = resx > 0 ? values[0].length - 1 : 0;
		resz = resy > 0 ? values[0][0].length - 1 : 0;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final WB_ScalarParameter function, final double xi, final double yi,
			final double zi, final double dx, final double dy, final double dz, final int sizeI, final int sizeJ,
			final int sizeK) {
		this.values = new WB_IsoValues3D.Function3D(function, xi, yi, zi, dx, dy, dz, sizeI, sizeJ, sizeK);
		resx = sizeI - 1;
		resy = sizeJ - 1;
		resz = sizeK - 1;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final WB_HashGridDouble3D values) {
		this.values = new WB_IsoValues3D.HashGrid3D(values);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final String[] images, final PApplet home, final int sizeI, final int sizeJ,
			final int sizeK) {
		this.values = new WB_IsoValues3D.ImageStack3D(images, home, sizeI, sizeJ, sizeK);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final String[] images, final PApplet home, final int sizeI, final int sizeJ,
			final int sizeK, final WB_IsoValues3D.Mode mode) {
		this.values = new WB_IsoValues3D.ImageStack3D(images, home, sizeI, sizeJ, sizeK, mode);
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	public WB_IsoSurfaceRaw3D setValues(final WB_IsoValues3D values) {
		this.values = values;
		resx = values.getSizeI() - 1;
		resy = values.getSizeJ() - 1;
		resz = values.getSizeK() - 1;
		return this;
	}

	public WB_IsoSurfaceRaw3D setIsolevel(final double v) {
		isolevel = v;
		return this;
	}

	public WB_IsoSurfaceRaw3D setBoundary(final double v) {
		boundary = v;
		return this;
	}

	public WB_IsoSurfaceRaw3D clearBoundary() {
		boundary = Double.NaN;
		return this;
	}

	public WB_IsoSurfaceRaw3D setInvert(final boolean invert) {
		this.invert = invert;
		return this;
	}

	public WB_IsoSurfaceRaw3D setCenter(final WB_Coord c) {
		cx = c.xd();
		cy = c.yd();
		cz = c.zd();
		return this;
	}

	public float[] getTriangles() {
		vertices = new WB_IndexedObjectMap<>();
		xedges = new WB_IndexedObjectMap<>();
		yedges = new WB_IndexedObjectMap<>();
		zedges = new WB_IndexedObjectMap<>();
		polygoniseRaw();
		final int size = triangles.size();
		final float[] output = new float[3 * size];
		int c = 0;
		for (final WB_Coord point : triangles) {
			output[c++] = point.xf();
			output[c++] = point.yf();
			output[c++] = point.zf();
		}
		return output;
	}

	static void createDirectories(final File file) {
		try {
			final String parentName = file.getParent();
			if (parentName != null) {
				final File parent = new File(parentName);
				if (!parent.exists()) {
					parent.mkdirs();
				}
			}
		} catch (final SecurityException se) {
			System.err.println("No permissions to create " + file.getAbsolutePath());
		}
	}

	static OutputStream createOutputStream(final File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("file can't be null");
		}
		createDirectories(file);
		OutputStream stream = new FileOutputStream(file);
		if (file.getName().toLowerCase().endsWith(".gz")) {
			stream = new GZIPOutputStream(stream);
		}
		return stream;
	}

	private void polygoniseRaw() {
		triangles = new WB_CoordList();
		final WB_Point offset = new WB_Point(cx - 0.5 * resx * dx, cy - 0.5 * resy * dy, cz - 0.5 * resz * dz);
		if (Double.isNaN(boundary)) {
			for (int k = 0; k < resz; k++) {
				for (int i = 0; i < resx; i++) {
					for (int j = 0; j < resy; j++) {
						getPolygonsRaw(i, j, k, classifyCell(i, j, k), offset);
					}
				}
			}
		} else {
			for (int k = -1; k < resz + 1; k++) {
				for (int i = -1; i < resx + 1; i++) {
					for (int j = -1; j < resy + 1; j++) {
						getPolygonsRaw(i, j, k, classifyCell(i, j, k), offset);
					}
				}
			}
		}
	}

	private List<WB_Coord> getPolygonsRaw(final int i, final int j, final int k, final int cubeindex,
			final WB_Point offset) {
		final int[] indices = entries[cubeindex];
		final int numtris = indices[0];
		int currentindex = 1;
		for (int t = 0; t < numtris; t++) {
			final WB_Point v2 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			final WB_Point v1 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			final WB_Point v3 = getIsoVertex(indices[currentindex++], i, j, k, offset);
			triangles.add(v2);
			triangles.add(v1);
			triangles.add(v3);
		}
		return triangles;
	}

	private int classifyCell(final int i, final int j, final int k) {
		if (Double.isNaN(boundary)) {
			if (i < 0 || j < 0 || k < 0 || i >= resx || j >= resy || k >= resz) {
				return -1;
			}
		}
		int cubeindex = 0;
		int offset = 1;
		if (invert) {
			if (value(i, j, k) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j, k) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i, j + 1, k) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j + 1, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j + 1, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i, j, k + 1) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j, k + 1) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j, k + 1) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j, k + 1) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i, j + 1, k + 1) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k + 1) < isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
			}
		} else {
			if (value(i, j, k) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j, k) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i, j + 1, k) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j + 1, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j + 1, k) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i, j, k + 1) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j, k + 1) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j, k + 1) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j, k + 1) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i, j + 1, k + 1) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
			}
			offset *= 3;
			if (value(i + 1, j + 1, k + 1) > isolevel) {
				cubeindex += 2 * offset;
			} else if (value(i + 1, j + 1, k + 1) == isolevel) {
				cubeindex += offset;
			}
		}
		return cubeindex;
	}

	private WB_Point getIsoVertex(final int isopointindex, final int i, final int j, final int k,
			final WB_Point offset) {
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
				return xedge(i, j, k, offset);
			case 1:
				return yedge(i, j, k, offset);
			case 2:
				return yedge(i + 1, j, k, offset);
			case 3:
				return xedge(i, j + 1, k, offset);
			case 4:
				return zedge(i, j, k, offset);
			case 5:
				return zedge(i + 1, j, k, offset);
			case 6:
				return zedge(i, j + 1, k, offset);
			case 7:
				return zedge(i + 1, j + 1, k, offset);
			case 8:
				return xedge(i, j, k + 1, offset);
			case 9:
				return yedge(i, j, k + 1, offset);
			case 10:
				return yedge(i + 1, j, k + 1, offset);
			case 11:
				return xedge(i, j + 1, k + 1, offset);
			default:
				return null;
			}
		}
		return null;
	}

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

	private WB_Point xedge(final int i, final int j, final int k, final WB_Point offset) {
		final WB_Point p0 = new WB_Point(i * dx, j * dy, k * dz);
		final WB_Point p1 = new WB_Point(i * dx + dx, j * dy, k * dz);
		final double val0 = value(i, j, k);
		final double val1 = value(i + 1, j, k);
		WB_Point xedge = xedges.get(index(i, j, k));
		if (xedge != null) {
			return xedge;
		}
		xedge = new WB_Point(interp(isolevel, p0, p1, val0, val1));
		xedge.addSelf(offset);
		xedges.put(index(i, j, k), xedge);
		return xedge;
	}

	private WB_Point yedge(final int i, final int j, final int k, final WB_Point offset) {
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

	private WB_Point zedge(final int i, final int j, final int k, final WB_Point offset) {
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

	private WB_Point interp(final double isolevel, final WB_Point p1, final WB_Point p2, final double valp1,
			final double valp2) {
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
		return new WB_Point(p1.xd() + mu * (p2.xd() - p1.xd()), p1.yd() + mu * (p2.yd() - p1.yd()),
				p1.zd() + mu * (p2.zd() - p1.zd()));
	}

	private int index(final int i, final int j, final int k) {
		return i + 1 + (resx + 2) * (j + 1) + (resx + 2) * (resy + 2) * (k + 1);
	}

	private double value(final int i, final int j, final int k) {
		if (Double.isNaN(boundary)) { // if no boundary is set i,j,k should
			// always be between o and resx,rey,resz
			return values.getValue(i, j, k);
		}
		if (i < 0 || j < 0 || k < 0 || i > resx || j > resy || k > resz) {
			return invert ? -boundary : boundary;
		}
		return values.getValue(i, j, k);
	}
}
