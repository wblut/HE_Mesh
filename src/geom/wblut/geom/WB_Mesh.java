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
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_Mesh implements WB_TriangleGenerator {
	protected int[][]			faces;
	protected List<WB_Coord>	vertices;
	protected WB_AABB			aabb;
	WB_Vector[]					vertexNormals	= null;
	WB_Vector[]					faceNormals		= null;
	int[][]						vvNeighbors		= null;
	int[][]						vfNeighbors		= null;
	int[][]						ffNeighbors		= null;
	boolean						vNormalsUpdated, fNormalsUpdated,
			vvNeighborsUpdated, vfNeighborsUpdated, ffNeighborsUpdated;
	List<int[]>					tris;
	WB_Vector[]					pdir1			= null;
	WB_Vector[]					pdir2			= null;
	double[]					curv1			= null;
	double[]					curv2			= null;
	double						k1min;
	double						k2min;
	double						Kmin;
	double						k1max;
	double						k2max;
	double						Kmax;
	double[][]					dcurv			= null;
	double[][]					cornerareas		= null;
	double[]					pointareas		= null;
	boolean						areasUpdated;
	boolean						curvaturesUpdated;
	boolean						DCurvaturesUpdated;
	private WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();

	/**
	 *
	 */
	protected WB_Mesh() {
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
	protected WB_Mesh(final WB_Mesh mesh) {
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
	protected WB_Mesh(final Collection<? extends WB_Coord> points,
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
	protected WB_Mesh(final WB_Coord[] points, final int[][] faces) {
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
	public WB_Mesh get() {
		return new WB_Mesh(this);
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

	/**
	 *
	 *
	 * @param id
	 * @return
	 */
	public WB_Polygon getPolygon(final int id) {
		WB_Coord[] points = new WB_Coord[faces[id].length];
		for (int i = 0; i < faces[id].length; i++) {
			points[i] = getVertex(faces[id][i]);
		}
		return geometryfactory.createSimplePolygon(points);
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Polygon> getPolygons() {
		final List<WB_Polygon> polygons = new FastList<WB_Polygon>();
		for (int i = 0; i < faces.length; i++) {
			polygons.add(getPolygon(i));
		}
		return polygons;
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

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public WB_Mesh isoFitInAABB(final WB_AABB AABB) {
		final WB_AABB self = getAABB();
		final double scx = self.getCenterX();
		final double acx = AABB.getCenterX();
		final double scy = self.getCenterY();
		final double acy = AABB.getCenterY();
		final double scz = self.getCenterZ();
		final double acz = AABB.getCenterZ();
		double f = Math.min(AABB.getWidth() / self.getWidth(),
				AABB.getHeight() / self.getHeight());
		f = Math.min(f, AABB.getDepth() / self.getDepth());
		final List<WB_Point> rescaled = new FastList<WB_Point>();
		for (int i = 0; i < vertices.size(); i++) {
			final WB_Point p = new WB_Point(-scx, -scy, -scz)
					.add(vertices.get(i));
			p.mulSelf(f);
			p.addSelf(acx, acy, acz);
			rescaled.add(p);
		}
		return geometryfactory.createMesh(rescaled, faces);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Mesh triangulate() {
		return triangulateMT();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Mesh triangulateForceST() {
		return triangulateST();
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Mesh triangulateForceMT() {
		return triangulateMT();
	}

	/**
	 *
	 *
	 * @return
	 */
	private WB_Mesh triangulateST() {
		tris = new FastList<int[]>();
		int[] face;
		int[] triangles;
		int id = 0;
		for (final int[] face2 : faces) {
			face = face2;
			if (face.length == 3) {
				addTriangle(face);
			} else {
				triangles = new WB_JTS.PolygonTriangulatorJTS()
						.triangulatePolygon2D(face, vertices, true,
								geometryfactory
										.createEmbeddedPlane(getPlane(id)))
						.getTriangles();
				for (int i = 0; i < triangles.length; i += 3) {
					addTriangle(new int[] { triangles[i], triangles[i + 1],
							triangles[i + 2] });
				}
			}
			id++;
		}
		faces = new int[tris.size()][3];
		int i = 0;
		for (final int[] tri : tris) {
			faces[i++] = tri;
		}
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	private WB_Mesh triangulateMT() {
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
						dfaces * i, dfaces * (i + 1) - 1, i);
				set.add(executor.submit(runner));
			}
			final Callable<ArrayList<int[]>> runner = new TriangulateRunner(
					dfaces * i, faces.length - 1, i);
			set.add(executor.submit(runner));
			ArrayList<int[]> tris = new ArrayList<int[]>();
			for (Future<ArrayList<int[]>> future : set) {
				tris.addAll(future.get());
			}
			faces = new int[tris.size()][3];
			i = 0;
			for (final int[] tri : tris) {
				faces[i++] = tri;
			}
			executor.shutdown();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}
		return this;
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
						dfaces * i, dfaces * (i + 1) - 1, i);
				set.add(executor.submit(runner));
			}
			final Callable<ArrayList<int[]>> runner = new TriangulateRunner(
					dfaces * i, faces.length - 1, i);
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

	class TriangulateRunner implements Callable<ArrayList<int[]>> {
		int		start;
		int		end;
		int		id;
		int[]	triangles;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 */
		TriangulateRunner(final int s, final int e, final int id) {
			start = s;
			end = e;
			this.id = id;
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

	/**
	 *
	 *
	 * @param tri
	 */
	synchronized void addTriangle(final int[] tri) {
		// tris.add(tri);
	}

	public WB_Vector getFaceNormal(final int id) {
		if (!fNormalsUpdated) {
			updateFaceNormalsST();
		}
		return faceNormals[id];
	}

	public WB_Point getFaceCenter(final int id) {
		final WB_Point c = geometryfactory.createPoint();
		for (int i = 0; i < faces[id].length; i++) {
			c.addSelf(getVertex(faces[id][i]));
		}
		c.divSelf(faces[id].length);
		return c;
	}

	public WB_Vector getVertexNormal(final int i) {
		if (!vNormalsUpdated) {
			updateVertexNormals();
		}
		return vertexNormals[i];
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
	 */
	private void updatevfNeighbors() {
		if (vfNeighborsUpdated) {
			return;
		}
		final int nv = vertices.size();
		final int nf = faces.length;
		final int[] numadjacentfaces = new int[nv];
		for (final int[] face : faces) {
			for (int i = 0; i < face.length; i++) {
				numadjacentfaces[face[i]]++;
			}
		}
		vfNeighbors = new int[nv][];
		for (int i = 0; i < nv; i++) {
			vfNeighbors[i] = new int[numadjacentfaces[i]];
			for (int j = 0; j < numadjacentfaces[i]; j++) {
				vfNeighbors[i][j] = -1;
			}
		}
		for (int i = 0; i < nf; i++) {
			final int[] face = faces[i];
			for (int j = 0; j < face.length; j++) {
				int counter = 0;
				while (vfNeighbors[face[j]][counter] != -1) {
					counter++;
				}
				vfNeighbors[face[j]][counter] = i;
			}
		}
		vfNeighborsUpdated = true;
	}

	/**
	 *
	 */
	private void updateVertexNormals() {
		updateVertexNormalsAngle();
	}

	/**
	 *
	 */
	private void updateVertexNormalsAngle() {
		final int nv = vertices.size();
		if (vNormalsUpdated) {
			return;
		}
		if (!fNormalsUpdated) {
			updateFaceNormalsMT();
		}
		vertexNormals = new WB_Vector[nv];
		for (int i = 0; i < nv; i++) {
			vertexNormals[i] = geometryfactory.createVector();
		}
		int i = 0;
		WB_Coord p0, p1, p2;
		for (final int[] face : faces) {
			for (int j = 0; j < face.length; j++) {
				p0 = vertices.get(face[j]);
				p1 = vertices.get(face[(j + 1) % face.length]);
				p2 = vertices.get(face[(j - 1 + face.length) % face.length]);
				final WB_Vector P10 = geometryfactory
						.createNormalizedVectorFromTo(p0, p1);
				final WB_Vector P20 = geometryfactory
						.createNormalizedVectorFromTo(p0, p2);
				final double w = P10.getAngleNorm(P20);
				vertexNormals[face[j]].addMulSelf(w, faceNormals[i]);
			}
			i++;
		}
		for (final WB_Vector v : vertexNormals) {
			v.normalizeSelf();
		}
		vNormalsUpdated = true;
	}

	/**
	 *
	 */
	private void updateFaceNormalsST() {
		final int nf = faces.length;
		if (fNormalsUpdated) {
			return;
		}
		faceNormals = new WB_Vector[nf];
		WB_Coord p0, p1;
		for (int i = 0; i < nf; i++) {
			final int[] face = faces[i];
			final WB_Vector tmp = geometryfactory.createVector();
			for (int j = 0, k = face.length - 1; j < face.length; k = j++) {
				p1 = getVertex(face[j]);
				p0 = getVertex(face[k]);
				final WB_Vector tmp2 = geometryfactory.createVector(
						(p0.yd() - p1.yd()) * (p0.zd() + p1.zd()),
						(p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
						(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
				tmp.addSelf(tmp2);
			}
			faceNormals[i] = tmp;
			faceNormals[i].normalizeSelf();
		}
		fNormalsUpdated = true;
	}

	/**
	 *
	 */
	private void updateFaceNormalsMT() {
		final int nf = faces.length;
		if (fNormalsUpdated) {
			return;
		}
		faceNormals = new WB_Vector[nf];
		final int threadCount = Runtime.getRuntime().availableProcessors();
		final int dfaces = nf / threadCount;
		final ExecutorService executor = Executors
				.newFixedThreadPool(threadCount);
		int i = 0;
		for (i = 0; i < threadCount - 1; i++) {
			final Runnable runner = new FaceNormalRunner(dfaces * i,
					dfaces * (i + 1) - 1);
			executor.submit(runner);
		}
		final Runnable runner = new FaceNormalRunner(dfaces * i,
				faces.length - 1);
		executor.submit(runner);
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		fNormalsUpdated = true;
	}

	class FaceNormalRunner implements Runnable {
		int	start;
		int	end;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 */
		FaceNormalRunner(final int s, final int e) {
			start = s;
			end = e;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			WB_Coord p0, p1;
			for (int i = start; i < end; i++) {
				final int[] face = faces[i];
				final WB_Vector tmp = geometryfactory.createVector();
				for (int j = 0, k = face.length - 1; j < face.length; k = j++) {
					p1 = getVertex(face[j]);
					p0 = getVertex(face[k]);
					final WB_Vector tmp2 = geometryfactory.createVector(
							(p0.yd() - p1.yd()) * (p0.zd() + p1.zd()),
							(p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
							(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
					tmp.addSelf(tmp2);
				}
				faceNormals[i] = tmp;
				faceNormals[i].normalizeSelf();
			}
		}
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public int[] vfNeighbors(final int i) {
		if (!vfNeighborsUpdated) {
			updatevfNeighbors();
		}
		return vfNeighbors[i];
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

	public WB_Mesh apply(final WB_Transform3D WB_Point) {
		final FastList<WB_Point> newvertices = new FastList<WB_Point>();
		WB_Point point;
		for (int i = 0; i < vertices.size(); i++) {
			point = geometryfactory.createPoint();
			WB_Point.applyAsPointInto(vertices.get(i), point);
			newvertices.add(point);
		}
		return geometryfactory.createMesh(newvertices, faces);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double k1(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return curv1[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double k2(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return curv2[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double K(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return curv2[i] * curv1[i];
	}

	/**
	 *
	 *
	 * @return
	 */
	public double k1min() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k1min;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double k2min() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k2min;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double Kmin() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return Kmin;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double k1max() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k1max;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double k2max() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k2max;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double Kmax() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return Kmax;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Vector k1dir(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return pdir1[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Vector k2dir(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return pdir2[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double[] DCurv(final int i) {
		if (!DCurvaturesUpdated) {
			updateDCurvatures();
		}
		return dcurv[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double DCurvInvariant(final int i) {
		if (!DCurvaturesUpdated) {
			updateDCurvatures();
		}
		return dcurv[i][0] * dcurv[i][0] + dcurv[i][1] * dcurv[i][1]
				+ dcurv[i][2] * dcurv[i][2] + dcurv[i][3] * dcurv[i][3];
	}

	/**
	 *
	 */
	private void updateCurvatures() {
		final WB_TriangleMesh tri = (WB_TriangleMesh) geometryfactory
				.createTriMesh(this);
		tri.updateCurvatures();
		k1min = tri.k1min;
		k2min = tri.k2min;
		Kmin = tri.Kmin;
		k1max = tri.k1max;
		k2max = tri.k2max;
		Kmax = tri.Kmax;
		curv1 = tri.curv1;
		curv2 = tri.curv2;
		pdir1 = tri.pdir1;
		pdir2 = tri.pdir2;
		curvaturesUpdated = true;
	}

	/**
	 *
	 */
	private void updateDCurvatures() {
		final WB_TriangleMesh tri = (WB_TriangleMesh) geometryfactory
				.createTriMesh(this);
		tri.updateDCurvatures();
		k1min = tri.k1min;
		k2min = tri.k2min;
		Kmin = tri.Kmin;
		k1max = tri.k1max;
		k2max = tri.k2max;
		Kmax = tri.Kmax;
		curv1 = tri.curv1;
		curv2 = tri.curv2;
		pdir1 = tri.pdir1;
		pdir2 = tri.pdir2;
		dcurv = tri.dcurv;
		curvaturesUpdated = true;
		DCurvaturesUpdated = true;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_TriangleGenerator#getTriangles()
	 */
	@Override
	public int[] getTriangles() {
		return triangulateMTIndices();
	}
}
