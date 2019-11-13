/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 * This work is published from Belgium.
 * (http://creativecommons.org/publicdomain/zero/1.0/)
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_SimpleMesh implements WB_TriangleFactory {
	protected int[][]			faces;
	protected List<WB_Coord>	vertices;
	List<int[]>					tris;
	private WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();

	/**
	 *
	 */
	protected WB_SimpleMesh() {
		vertices = new FastList<WB_Coord>();
		this.faces = new int[0][];
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	private List<WB_Coord> createVertices(
			final Collection<? extends WB_Coord> points) {
		vertices = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			vertices.add(new WB_Point(p));
		}
		return vertices;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	private List<WB_Coord> createVertices(final WB_Coord[] points) {
		vertices = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			vertices.add(new WB_Point(p));
		}
		return vertices;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	protected WB_SimpleMesh(final WB_SimpleMesh mesh) {
		vertices = createVertices(mesh.vertices);
		this.faces = new int[mesh.faces.length][];
		int i = 0;
		for (final int[] face : mesh.faces) {
			this.faces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				this.faces[i][j] = face[j];
			}
			i++;
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param faces
	 */
	protected WB_SimpleMesh(final Collection<? extends WB_Coord> points,
			final int[][] faces) {
		vertices = createVertices(points);
		this.faces = new int[faces.length][];
		int i = 0;
		for (final int[] face : faces) {
			this.faces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				this.faces[i][j] = face[j];
			}
			i++;
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param faces
	 */
	protected WB_SimpleMesh(final WB_Coord[] points, final int[][] faces) {
		vertices = createVertices(points);
		this.faces = new int[faces.length][];
		int i = 0;
		for (final int[] face : faces) {
			this.faces[i] = new int[face.length];
			for (int j = 0; j < face.length; j++) {
				this.faces[i][j] = face[j];
			}
			i++;
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_SimpleMesh get() {
		return new WB_SimpleMesh(this);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Mesh#getFacesAsInt()
	 */
	public int[][] getFacesAsInt() {
		return faces;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Mesh#getEdgesAsInt()
	 */
	public int[][] getEdgesAsInt() {
		if (faces == null) {
			return null;
		}
		int noe = 0;
		for (final int[] f : faces) {
			noe += f.length;
		}
		noe /= 2;
		final int[][] edges = new int[noe][2];
		int id = 0;
		for (final int[] f : faces) {
			final int fl = f.length;
			for (int j = 0; j < fl; j++) {
				if (f[j] < f[(j + 1) % fl]) {
					edges[id][0] = f[j];
					edges[id++][1] = f[(j + 1) % fl];
				}
			}
		}
		return edges;
	}

	/**
	 *
	 *
	 * @param id
	 * @param d
	 * @return
	 */
	public WB_Plane getPlane(final int id, final double d) {
		final int[] face = getFace(id);
		final WB_Vector normal = geometryfactory.createVector();
		final WB_Point center = geometryfactory.createPoint();
		WB_Vector tmp;
		WB_Coord p0;
		WB_Coord p1;
		for (int i = 0, j = face.length - 1; i < face.length; j = i, i++) {
			p0 = vertices.get(face[j]);
			p1 = vertices.get(face[i]);
			center.addSelf(p1);
			tmp = geometryfactory.createVector(
					(p0.yd() - p1.yd()) * (p0.zd() + p1.zd()),
					(p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
					(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
			normal.addSelf(tmp);
		}
		normal.normalizeSelf();
		center.divSelf(face.length);
		return geometryfactory.createPlane(center.addMul(d, normal), normal);
	}

	/**
	 *
	 *
	 * @param id
	 * @return
	 */
	public WB_Plane getPlane(final int id) {
		return getPlane(id, 0);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public List<WB_Plane> getPlanes(final double d) {
		final List<WB_Plane> planes = new FastList<WB_Plane>();
		for (int i = 0; i < faces.length; i++) {
			planes.add(getPlane(i, d));
		}
		return planes;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Plane> getPlanes() {
		return getPlanes(0);
	}

	public WB_Point getCenter() {
		double cx = 0;
		double cy = 0;
		double cz = 0;
		for (WB_Coord p : vertices) {
			cx += p.xd();
			cy += p.yd();
			cz += p.zd();
		}
		cx /= vertices.size();
		cy /= vertices.size();
		cz /= vertices.size();
		return geometryfactory.createPoint(cx, cy, cz);
	}

	public WB_AABB getAABB() {
		return new WB_AABB(vertices);
	}

	public int getNumberOfFaces() {
		return faces.length;
	}

	public int getNumberOfVertices() {
		return vertices.size();
	}

	public WB_Coord getVertex(final int i) {
		return vertices.get(i);
	}

	@Override
	public WB_CoordCollection getPoints() {
		return WB_CoordCollection.getCollection(vertices);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public int[] getFace(final int i) {
		return faces[i];
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_TriangleGenerator#getTriangles()
	 */
	@Override
	public int[] getTriangles() {
		return triangulateMTIndices();
	}

	private int[] triangulateMTIndices() {
		int[] triangles = new int[0];
		try {
			int threadCount = Runtime.getRuntime().availableProcessors();
			int dfaces = faces.length / threadCount;
			if (dfaces < 1024) {
				dfaces = 1024;
				threadCount = (int) Math.ceil(faces.length / 1024.0);
			}
			final ExecutorService executor = Executors
					.newFixedThreadPool(threadCount);
			final Set<Future<ArrayList<int[]>>> set = new HashSet<Future<ArrayList<int[]>>>();
			int i = 0;
			for (i = 0; i < threadCount - 1; i++) {
				final Callable<ArrayList<int[]>> runner = new TriangulateRunner(
						dfaces * i, dfaces * (i + 1) - 1);
				set.add(executor.submit(runner));
			}
			final Callable<ArrayList<int[]>> runner = new TriangulateRunner(
					dfaces * i, faces.length - 1);
			set.add(executor.submit(runner));
			ArrayList<int[]> tris = new ArrayList<int[]>();
			for (Future<ArrayList<int[]>> future : set) {
				tris.addAll(future.get());
			}
			triangles = new int[3 * tris.size()];
			i = 0;
			for (final int[] tri : tris) {
				triangles[i++] = tri[0];
				triangles[i++] = tri[1];
				triangles[i++] = tri[2];
			}
			executor.shutdown();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}
		return triangles;
	}

	private class TriangulateRunner implements Callable<ArrayList<int[]>> {
		int		start;
		int		end;
		int[]	triangles;
	
		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 */
		TriangulateRunner(final int s, final int e) {
			start = s;
			end = e;
		}
	
		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public ArrayList<int[]> call() {
			int[] face;
			ArrayList<int[]> tris = new ArrayList<int[]>();
			for (int i = start; i <= end; i++) {
				face = faces[i];
				if (face.length == 3) {
					tris.add(face);
				} else {
					triangles = new WB_JTS.PolygonTriangulatorJTS()
							.triangulatePolygon2D(face, vertices, true,
									geometryfactory
											.createEmbeddedPlane(getPlane(i)))
							.getTriangles();
					for (int j = 0; j < triangles.length; j += 3) {
						tris.add(new int[] { triangles[j], triangles[j + 1],
								triangles[j + 2] });
					}
				}
			}
			return tris;
		}
	}
}
