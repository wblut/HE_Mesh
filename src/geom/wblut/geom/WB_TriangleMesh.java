/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.Collection;

/**
 *
 */
public class WB_TriangleMesh extends WB_Mesh {

	/**
	 *
	 */
	private final static int[] PREV = new int[] { 2, 0, 1 };

	/**
	 *
	 */
	private final static int[] NEXT = new int[] { 1, 2, 0 };

	/**
	 *
	 */
	WB_Vector[] pdir1 = null, pdir2 = null;

	/**
	 *
	 */
	double[] curv1 = null, curv2 = null;

	/**
	 *
	 */
	double k1min, k2min, Kmin, k1max, k2max, Kmax;

	/**
	 *
	 */
	double[][] dcurv = null;

	/**
	 *
	 */
	double[][] cornerareas = null;

	/**
	 *
	 */
	double[] pointareas = null;

	/**
	 *
	 */
	boolean areasUpdated, curvaturesUpdated, DCurvaturesUpdated;

	/**
	 *
	 */
	private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	/**
	 *
	 *
	 * @param points
	 * @param faces
	 */
	protected WB_TriangleMesh(final Collection<? extends WB_Coord> points, final int[][] faces) {
		super(points, faces);
		triangulate();
	}

	/**
	 *
	 *
	 * @param points
	 * @param faces
	 */
	protected WB_TriangleMesh(final WB_Coord[] points, final int[][] faces) {
		super(points, faces);
		triangulate();
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	protected WB_TriangleMesh(final WB_Mesh mesh) {
		vertices = mesh.getPoints().toList();
		faces = mesh.getFacesAsInt();
		aabb = new WB_AABB(vertices);
		triangulate();
	}

	/**
	 *
	 */
	private void updateFaceNormals() {
		final int nf = faces.length;
		if (fNormalsUpdated) {
			return;
		}
		faceNormals = new WB_Vector[nf];
		for (int i = 0; i < nf; i++) {
			final int[] face = faces[i];
			final WB_Coord p0 = vertices.get(face[0]);
			final WB_Coord p1 = vertices.get(face[1]);
			final WB_Coord p2 = vertices.get(face[2]);
			final WB_Vector a = geometryfactory.createNormalizedVectorFromTo(p0, p1);
			final WB_Vector b = geometryfactory.createNormalizedVectorFromTo(p2, p1);
			faceNormals[i] = a.cross(b);
			faceNormals[i].normalizeSelf();
		}
		fNormalsUpdated = true;
	}

	/**
	 *
	 */
	private void updateVertexNormals() {
		updateVertexNormalsAngle();
	}

	/**
	 * The normal of a vertex v is computed according to the formula described
	 * by Nelson Max in Max, N.,
	 * "Weights for Computing Vertex Normals from Facet Normals", Journal of
	 * Graphics Tools, 4(2) (1999) The weight for each wedge is the cross
	 * product of the two edge over the product of the square of the two edge
	 * lengths. According to the original paper it is perfect only for spherical
	 * surface, but it should perform well...
	 */
	@SuppressWarnings("unused")
	private void updateVertexNormalsSqLength() {
		final int nv = vertices.size();
		if (vNormalsUpdated) {
			return;
		}
		if (!fNormalsUpdated) {
			updateFaceNormals();
		}
		vertexNormals = new WB_Vector[nv];
		for (int i = 0; i < nv; i++) {
			vertexNormals[i] = geometryfactory.createVector();
		}
		for (final int[] face : faces) {
			final WB_Coord p0 = vertices.get(face[0]);
			final WB_Coord p1 = vertices.get(face[1]);
			final WB_Coord p2 = vertices.get(face[2]);
			final WB_Vector a = geometryfactory.createNormalizedVectorFromTo(p0, p1);
			final WB_Vector b = geometryfactory.createNormalizedVectorFromTo(p1, p2);
			final WB_Vector c = geometryfactory.createNormalizedVectorFromTo(p2, p0);
			final double l2a = a.getSqLength();
			final double l2b = b.getSqLength();
			final double l2c = c.getSqLength();
			final WB_Vector facenormal = a.cross(b);
			vertexNormals[face[0]].addMulSelf(1.0 / (l2a * l2c), facenormal);
			vertexNormals[face[1]].addMulSelf(1.0 / (l2b * l2a), facenormal);
			vertexNormals[face[2]].addMulSelf(1.0 / (l2c * l2b), facenormal);
		}
		for (final WB_Vector v : vertexNormals) {
			v.normalizeSelf();
		}
		vNormalsUpdated = true;
	}

	/**
	 *
	 */
	@SuppressWarnings("unused")
	private void updateVertexNormalsArea() {
		final int nv = vertices.size();
		if (vNormalsUpdated) {
			return;
		}
		if (!fNormalsUpdated) {
			updateFaceNormals();
		}
		vertexNormals = new WB_Vector[nv];
		for (int i = 0; i < nv; i++) {
			vertexNormals[i] = geometryfactory.createVector();
		}
		for (final int[] face : faces) {
			final WB_Coord p0 = vertices.get(face[0]);
			final WB_Coord p1 = vertices.get(face[1]);
			final WB_Coord p2 = vertices.get(face[2]);
			final WB_Vector a = geometryfactory.createNormalizedVectorFromTo(p0, p1);
			final WB_Vector b = geometryfactory.createNormalizedVectorFromTo(p1, p2);
			final WB_Vector facenormal = a.cross(b);
			vertexNormals[face[0]].addSelf(facenormal);
			vertexNormals[face[1]].addSelf(facenormal);
			vertexNormals[face[2]].addSelf(facenormal);
		}
		for (final WB_Vector v : vertexNormals) {
			v.normalizeSelf();
		}
		vNormalsUpdated = true;
	}

	/**
	 * The normal of a vertex v computed as a weighted sum f the incident face
	 * normals. The weight is simply the angle of the involved wedge. Described
	 * in:
	 *
	 * G. Thurmer, C. A. Wuthrich
	 * "Computing vertex normals from polygonal facets" Journal of Graphics
	 * Tools, 1998
	 */
	private void updateVertexNormalsAngle() {
		final int nv = vertices.size();
		if (vNormalsUpdated) {
			return;
		}
		if (!fNormalsUpdated) {
			updateFaceNormals();
		}
		vertexNormals = new WB_Vector[nv];
		for (int i = 0; i < nv; i++) {
			vertexNormals[i] = geometryfactory.createVector();
		}
		int i = 0;
		for (final int[] face : faces) {
			final WB_Coord p0 = vertices.get(face[0]);
			final WB_Coord p1 = vertices.get(face[1]);
			final WB_Coord p2 = vertices.get(face[2]);
			final WB_Vector P10 = geometryfactory.createNormalizedVectorFromTo(p0, p1);
			final WB_Vector P20 = geometryfactory.createNormalizedVectorFromTo(p0, p2);
			final WB_Vector P21 = geometryfactory.createNormalizedVectorFromTo(p1, p2);
			final double w0 = P10.getAngleNorm(P20);
			P10.mulSelf(-1);
			final double w1 = P10.getAngleNorm(P21);
			final WB_Vector fn = faceNormals[i];
			vertexNormals[face[0]].addMulSelf(w0, fn);
			vertexNormals[face[1]].addMulSelf(w1, fn);
			vertexNormals[face[2]].addMulSelf(Math.PI - w0 - w1, fn);
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
	@SuppressWarnings("unused")
	private void updateVertexNormalsNoWeight() {
		final int nv = vertices.size();
		if (vNormalsUpdated) {
			return;
		}
		if (!fNormalsUpdated) {
			updateFaceNormals();
		}
		vertexNormals = new WB_Vector[nv];
		for (int i = 0; i < nv; i++) {
			vertexNormals[i] = geometryfactory.createVector();
		}
		int i = 0;
		for (final int[] face : faces) {
			final WB_Vector fn = faceNormals[i];
			vertexNormals[face[0]].addSelf(fn);
			vertexNormals[face[1]].addSelf(fn);
			vertexNormals[face[2]].addSelf(fn);
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
	private void updatePointAreas() {
		final int nv = vertices.size();
		if (areasUpdated) {
			return;
		}
		final int nf = faces.length;
		pointareas = new double[nv];
		cornerareas = new double[nf][];
		for (int i = 0; i < nf; i++) {
			// Edges
			final WB_Vector e0 = geometryfactory.createVectorFromTo(vertices.get(faces[i][1]),
					vertices.get(faces[i][2]));
			final WB_Vector e1 = geometryfactory.createVectorFromTo(vertices.get(faces[i][2]),
					vertices.get(faces[i][0]));
			final WB_Vector e2 = geometryfactory.createVectorFromTo(vertices.get(faces[i][0]),
					vertices.get(faces[i][1]));
			// Compute corner weights
			final WB_Vector c = e0.cross(e1);
			double x, y, z;
			final double area = 0.5 * c.getLength();
			final double[] l2 = new double[] { e0.getSqLength(), e1.getSqLength(), e2.getSqLength() };
			final double[] ew = new double[] { l2[0] * (l2[1] + l2[2] - l2[0]), l2[1] * (l2[2] + l2[0] - l2[1]),
					l2[2] * (l2[0] + l2[1] - l2[2]) };
			if (ew[0] <= 0.0f) {
				y = -0.25 * l2[2] * area / e0.dot(e2);
				z = -0.25 * l2[1] * area / e0.dot(e1);
				x = area - y - z;
			} else if (ew[1] <= 0.0f) {
				z = -0.25 * l2[0] * area / e1.dot(e0);
				x = -0.25 * l2[2] * area / e1.dot(e2);
				y = area - z - x;
			} else if (ew[2] <= 0.0f) {
				x = -0.25 * l2[1] * area / e2.dot(e1);
				y = -0.25 * l2[0] * area / e2.dot(e0);
				z = area - x - y;
			} else {
				final double ewscale = 0.5f * area / (ew[0] + ew[1] + ew[2]);
				x = ewscale * (ew[1] + ew[2]);
				y = ewscale * (ew[2] + ew[0]);
				z = ewscale * (ew[0] + ew[1]);
			}
			cornerareas[i] = new double[] { x, y, z };
			final int[] face = faces[i];
			pointareas[face[0]] += x;
			pointareas[face[1]] += y;
			pointareas[face[2]] += z;
		}
		areasUpdated = true;
	}

	/**
	 *
	 */
	protected void updateCurvatures() {
		if (curvaturesUpdated) {
			return;
		}
		updateVertexNormals();
		updatePointAreas();
		final int nv = vertices.size();
		k1min = k2min = Kmin = Double.POSITIVE_INFINITY;
		k1max = k2max = Kmax = Double.NEGATIVE_INFINITY;
		curv1 = new double[nv];
		curv2 = new double[nv];
		pdir1 = new WB_Vector[nv];
		pdir2 = new WB_Vector[nv];
		final double[] curv12 = new double[nv];
		for (final int[] face : faces) {
			pdir1[face[0]] = geometryfactory.createVectorFromTo(vertices.get(face[0]), vertices.get(face[1]));
			pdir1[face[1]] = geometryfactory.createVectorFromTo(vertices.get(face[1]), vertices.get(face[2]));
			pdir1[face[2]] = geometryfactory.createVectorFromTo(vertices.get(face[2]), vertices.get(face[0]));
		}
		for (int i = 0; i < nv; i++) {
			pdir1[i].crossSelf(vertexNormals[i]);
			pdir1[i].normalizeSelf();
			pdir2[i] = vertexNormals[i].cross(pdir1[i]);
			pdir2[i].normalizeSelf();
		}
		int i = 0;
		for (final int[] face : faces) {
			final WB_Vector e2 = geometryfactory.createVectorFromTo(vertices.get(face[0]), vertices.get(face[1]));
			final WB_Vector e0 = geometryfactory.createVectorFromTo(vertices.get(face[1]), vertices.get(face[2]));
			final WB_Vector e1 = geometryfactory.createVectorFromTo(vertices.get(face[2]), vertices.get(face[0]));
			final WB_Vector t = geometryfactory.createNormalizedVector(e0);
			final WB_Vector n = e0.cross(e1);
			final WB_Vector b = n.cross(t);
			b.normalizeSelf();
			final double[] m = new double[] { 0, 0, 0 };
			final double[][] w = new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
			for (int j = 0; j < 3; j++) {
				final double u = (j == 0 ? e0 : j == 1 ? e1 : e2).dot(t);
				final double v = (j == 0 ? e0 : j == 1 ? e1 : e2).dot(b);
				w[0][0] += u * u;
				w[0][1] += u * v;
				w[2][2] += v * v;
				final WB_Vector dn = geometryfactory.createVectorFromTo(vertexNormals[face[NEXT[j]]],
						vertexNormals[face[PREV[j]]]);
				final double dnu = dn.dot(t);
				final double dnv = dn.dot(b);
				m[0] += dnu * u;
				m[1] += dnu * v + dnv * u;
				m[2] += dnv * v;
			}
			w[1][1] = w[0][0] + w[2][2];
			w[1][2] = w[0][1];
			final double[] diag = new double[3];
			if (!ldltdc(w, diag)) {
				continue;
			}
			ldltsl(w, diag, m, m);
			for (int j = 0; j < 3; j++) {
				final int vj = face[j];
				final WB_Vector cs = geometryfactory.createVector();
				projCurv(t, b, m[0], m[1], m[2], pdir1[vj], pdir2[vj], cs);
				final double wt = cornerareas[i][j] / pointareas[vj];
				curv1[vj] += wt * cs.xd();
				curv12[vj] += wt * cs.yd();
				curv2[vj] += wt * cs.zd();
			}
			i++;
		}
		for (i = 0; i < nv; i++) {
			final WB_Vector ks = geometryfactory.createVector();
			diagonalizeCurv(pdir1[i], pdir2[i], curv1[i], curv12[i], curv2[i], vertexNormals[i], pdir1[i], pdir2[i],
					ks);
			curv1[i] = ks.xd();
			curv2[i] = ks.yd();
			k1min = Math.min(k1min, curv1[i]);
			k2min = Math.min(k2min, curv2[i]);
			Kmin = Math.min(Kmin, curv1[i] * curv2[i]);
			k1max = Math.max(k1max, curv1[i]);
			k2max = Math.max(k2max, curv2[i]);
			Kmax = Math.max(Kmax, curv1[i] * curv2[i]);
		}
		curvaturesUpdated = true;
	}

	/**
	 *
	 */
	protected void updateDCurvatures() {
		if (DCurvaturesUpdated) {
			return;
		}
		updateCurvatures();
		final int nv = vertices.size();
		dcurv = new double[nv][4];
		int i = 0;
		for (final int[] face : faces) {
			final WB_Vector e2 = geometryfactory.createVectorFromTo(vertices.get(face[0]), vertices.get(face[1]));
			final WB_Vector e0 = geometryfactory.createVectorFromTo(vertices.get(face[1]), vertices.get(face[2]));
			final WB_Vector e1 = geometryfactory.createVectorFromTo(vertices.get(face[2]), vertices.get(face[0]));
			final WB_Vector t = geometryfactory.createNormalizedVector(e0);
			final WB_Vector n = e0.cross(e1);
			final WB_Vector b = n.cross(t);
			b.normalizeSelf();
			final WB_Vector[] fcurv = new WB_Vector[3];
			for (int j = 0; j < 3; j++) {
				fcurv[j] = geometryfactory.createVector();
				final int vj = faces[i][j];
				projCurv(pdir1[vj], pdir2[vj], curv1[vj], 0, curv2[vj], t, b, fcurv[j]);
			}
			final double[] m = new double[] { 0, 0, 0, 0 };
			final double[][] w = new double[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
			for (int j = 0; j < 3; j++) {
				final WB_Vector dfcurv = geometryfactory.createVectorFromTo(fcurv[NEXT[j]], fcurv[PREV[j]]);
				final double u = (j == 0 ? e0 : j == 1 ? e1 : e2).dot(t);
				final double v = (j == 0 ? e0 : j == 1 ? e1 : e2).dot(b);
				final double u2 = u * u;
				final double v2 = v * v;
				final double uv = u * v;
				w[0][0] += u2;
				w[0][1] += uv;
				w[3][3] += v2;
				m[0] += u * dfcurv.xd();
				m[1] += v * dfcurv.xd() + 2.0 * u * dfcurv.yd();
				m[2] += 2 * v * dfcurv.yd() + u * dfcurv.zd();
				m[3] += v * dfcurv.zd();
			}
			w[1][1] = 2.0 * w[0][0] + w[3][3];
			w[1][2] = 2.0 * w[0][1];
			w[2][2] = w[0][0] + 2.0 * w[3][3];
			w[2][3] = w[0][1];
			final double[] diag = new double[4];
			if (!ldltdc(w, diag)) {
				continue;
			}
			ldltsl(w, diag, m, m);
			final double[] faceDcurv = new double[4];
			for (int j = 0; j < 4; j++) {
				faceDcurv[j] = m[j];
			}
			for (int j = 0; j < 3; j++) {
				final int vj = face[j];
				final double[] thisVertDcurv = new double[4];
				projDcurv(t, b, faceDcurv, pdir1[vj], pdir2[vj], thisVertDcurv);
				final double wt = cornerareas[i][j] / pointareas[vj];
				for (int k = 0; k < 4; k++) {
					dcurv[vj][k] += wt * thisVertDcurv[k];
				}
			}
			i++;
		}
		DCurvaturesUpdated = true;
	}

	/**
	 *
	 */
	private void updatevvNeighbors() {
		if (vvNeighborsUpdated) {
			return;
		}
		vvNeighborsUpdated = true;
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
		for (final int[] tri : faces) {
			numadjacentfaces[tri[0]]++;
			numadjacentfaces[tri[1]]++;
			numadjacentfaces[tri[2]]++;
		}
		vfNeighbors = new int[nv][];
		for (int i = 0; i < nv; i++) {
			vfNeighbors[i] = new int[numadjacentfaces[i]];
			for (int j = 0; j < numadjacentfaces[i]; j++) {
				vfNeighbors[i][j] = -1;
			}
		}
		for (int i = 0; i < nf; i++) {
			final int[] tri = faces[i];
			for (int j = 0; j < 3; j++) {
				int counter = 0;
				while (vfNeighbors[tri[j]][counter] != -1) {
					counter++;
				}
				vfNeighbors[tri[j]][counter] = i;
			}
		}
		vfNeighborsUpdated = true;
		updateffNeighbors();
		for (int i = 0; i < nv; i++) {
			if (vfNeighbors[i].length == 0) {
				continue;
			}
			int f = vfNeighbors[i][0];
			int fPrev = prevFace(i, f);
			while (fPrev >= 0 && fPrev != vfNeighbors[i][0]) {
				f = fPrev;
				fPrev = prevFace(i, f);
			}
			int counter = 0;
			final int fStart = f;
			do {
				vfNeighbors[i][counter++] = f;
				f = nextFace(i, f);
			} while (f >= 0 && f != fStart);
		}
	}

	/**
	 *
	 */
	private void updateffNeighbors() {
		if (ffNeighborsUpdated) {
			return;
		}
		updatevfNeighbors();
		final int nf = faces.length;
		ffNeighbors = new int[nf][3];
		for (int i = 0; i < nf; i++) {
			ffNeighbors[i] = new int[] { -1, -1, -1 };
		}
		for (int i = 0; i < nf; i++) {
			final int[] face = faces[i];
			for (int j = 0; j < 3; j++) {
				if (ffNeighbors[i][j] != -1) {
					continue;
				}
				final int v1 = face[NEXT[j]];
				final int v2 = face[PREV[j]];
				final int[] a1 = vfNeighbors[v1];
				final int[] a2 = vfNeighbors[v2];
				for (final int element : a1) {
					final int other = element;
					if (other == i) {
						continue;
					}
					if (!contains(a2, other)) {
						continue;
					}
					final int ind = NEXT[indexOf(v1, faces[other])];
					if (faces[other][NEXT[ind]] != v2) {
						continue;
					}
					ffNeighbors[i][j] = other;
					ffNeighbors[other][ind] = i;
					break;
				}
			}
		}
		ffNeighborsUpdated = true;
	}

	/**
	 *
	 *
	 * @param i
	 * @param face
	 * @return
	 */
	int indexOf(final int i, final int[] face) {
		return face[0] == i ? 0 : face[1] == i ? 1 : face[2] == i ? 2 : -1;
	}

	/**
	 *
	 *
	 * @param a
	 * @param el
	 * @return
	 */
	boolean contains(final int[] a, final int el) {
		for (final int element : a) {
			if (element == el) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public boolean isBoundary(final int v) {
		if (vvNeighborsUpdated) {
			updatevvNeighbors();
		}
		if (vfNeighborsUpdated) {
			updatevfNeighbors();
		}
		return vvNeighbors[v].length != vfNeighbors[v].length;
	}

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param v2
	 * @return
	 */
	WB_Vector trinorm(final WB_Coord v0, final WB_Coord v1, final WB_Coord v2) {
		final WB_Vector a = geometryfactory.createVectorFromTo(v0, v1);
		return a.cross(geometryfactory.createVectorFromTo(v0, v2)).mulSelf(0.5);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	WB_Vector trinorm(final int[] f) {
		return trinorm(vertices.get(f[0]), vertices.get(f[1]), vertices.get(f[2]));
	}

	/**
	 *
	 *
	 * @param v_center
	 * @param f
	 * @return
	 */
	int nextFace(final int v_center, final int f) {
		final int[] face = faces[f];
		final int i_center = face[0] == v_center ? 0 : face[1] == v_center ? 1 : 2;
		final int i_next = NEXT[i_center];
		return ffNeighbors[f][i_next];
	}

	/**
	 *
	 *
	 * @param v_center
	 * @param f
	 * @return
	 */
	int prevFace(final int v_center, final int f) {
		final int[] face = faces[f];
		final int i_center = face[0] == v_center ? 0 : face[1] == v_center ? 1 : 2;
		final int i_prev = PREV[i_center];
		return ffNeighbors[f][i_prev];
	}

	/**
	 *
	 *
	 * @param A
	 * @param rdiag
	 * @return
	 */
	static boolean ldltdc(final double[][] A, final double[] rdiag) {
		final int N = rdiag.length;
		if (A.length != N) {
			return false;
		}
		final double[] v = new double[N - 1];
		for (int i = 0; i < N; i++) {
			for (int k = 0; k < i; k++) {
				v[k] = A[i][k] * rdiag[k];
			}
			for (int j = i; j < N; j++) {
				double sum = A[i][j];
				for (int k = 0; k < i; k++) {
					sum -= v[k] * A[j][k];
				}
				if (i == j) {
					if (sum <= 0.0) {
						return false;
					}
					rdiag[i] = 1.0 / sum;
				} else {
					A[j][i] = sum;
				}
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @param A
	 * @param rdiag
	 * @param B
	 * @param x
	 */
	static void ldltsl(final double[][] A, final double[] rdiag, final double[] B, final double[] x) {
		final int N = rdiag.length;
		int i;
		for (i = 0; i < N; i++) {
			double sum = B[i];
			for (int k = 0; k < i; k++) {
				sum -= A[i][k] * x[k];
			}
			x[i] = sum * rdiag[i];
		}
		for (i = N - 1; i >= 0; i--) {
			double sum = 0;
			for (int k = i + 1; k < N; k++) {
				sum += A[k][i] * x[k];
			}
			x[i] -= sum * rdiag[i];
		}
	}

	/**
	 *
	 *
	 * @param oldU
	 * @param oldV
	 * @param oldKu
	 * @param oldKuv
	 * @param oldKv
	 * @param newU
	 * @param newV
	 * @param newKs
	 */
	void projCurv(final WB_Vector oldU, final WB_Vector oldV, final double oldKu, final double oldKuv,
			final double oldKv, final WB_Vector newU, final WB_Vector newV, final WB_Vector newKs) {
		final WB_Vector rNewU = geometryfactory.createVector();
		final WB_Vector rNewV = geometryfactory.createVector();
		final WB_Vector newNorm = oldU.cross(oldV);
		rotCoordSys(newU, newV, newNorm, rNewU, rNewV);
		final double u1 = rNewU.dot(oldU);
		final double v1 = rNewU.dot(oldV);
		final double u2 = rNewV.dot(oldU);
		final double v2 = rNewV.dot(oldV);
		newKs.set(oldKu * u1 * u1 + oldKuv * (2 * u1 * v1) + oldKv * v1 * v1,
				oldKu * u1 * u2 + oldKuv * (u1 * v2 + u2 * v1) + oldKv * v1 * v2,
				oldKu * u2 * u2 + oldKuv * (2 * u2 * v2) + oldKv * v2 * v2);
	}

	/**
	 *
	 *
	 * @param oldU
	 * @param oldV
	 * @param oldDcurv
	 * @param newU
	 * @param newV
	 * @param newDcurv
	 */
	void projDcurv(final WB_Vector oldU, final WB_Vector oldV, final double[] oldDcurv, final WB_Vector newU,
			final WB_Vector newV, final double[] newDcurv) {
		final WB_Vector rNewU = geometryfactory.createVector();
		final WB_Vector rNewV = geometryfactory.createVector();
		final WB_Vector newNorm = oldU.cross(oldV);
		rotCoordSys(newU, newV, newNorm, rNewU, rNewV);
		final double u1 = rNewU.dot(oldU);
		final double v1 = rNewU.dot(oldV);
		final double u2 = rNewV.dot(oldU);
		final double v2 = rNewV.dot(oldV);
		newDcurv[0] = oldDcurv[0] * u1 * u1 * u1 + oldDcurv[1] * 3.0f * u1 * u1 * v1 + oldDcurv[2] * 3.0f * u1 * v1 * v1
				+ oldDcurv[3] * v1 * v1 * v1;
		newDcurv[1] = oldDcurv[0] * u1 * u1 * u2 + oldDcurv[1] * (u1 * u1 * v2 + 2.0f * u2 * u1 * v1)
				+ oldDcurv[2] * (u2 * v1 * v1 + 2.0f * u1 * v1 * v2) + oldDcurv[3] * v1 * v1 * v2;
		newDcurv[2] = oldDcurv[0] * u1 * u2 * u2 + oldDcurv[1] * (u2 * u2 * v1 + 2.0f * u1 * u2 * v2)
				+ oldDcurv[2] * (u1 * v2 * v2 + 2.0f * u2 * v2 * v1) + oldDcurv[3] * v1 * v2 * v2;
		newDcurv[3] = oldDcurv[0] * u2 * u2 * u2 + oldDcurv[1] * 3.0f * u2 * u2 * v2 + oldDcurv[2] * 3.0f * u2 * v2 * v2
				+ oldDcurv[3] * v2 * v2 * v2;
	}

	/**
	 *
	 *
	 * @param oldU
	 * @param oldV
	 * @param newNorm
	 * @param newU
	 * @param newV
	 */
	void rotCoordSys(final WB_Vector oldU, final WB_Vector oldV, final WB_Vector newNorm, final WB_Vector newU,
			final WB_Vector newV) {
		newU.set(oldU);
		newV.set(oldV);
		final WB_Vector oldNorm = oldU.cross(oldV);
		final double ndot = oldNorm.dot(newNorm);
		if (ndot <= -1.0) {
			newU.mulSelf(-1);
			newV.mulSelf(-1);
			return;
		}
		final WB_Vector perpOld = newNorm.addMul(-ndot, oldNorm);
		final WB_Vector dperp = newNorm.add(oldNorm);
		dperp.mulSelf(1.0 / (1 + ndot));
		newU.addMulSelf(-newU.dot(perpOld), dperp);
		newV.addMulSelf(-newV.dot(perpOld), dperp);
	}

	/**
	 *
	 *
	 * @param oldU
	 * @param oldV
	 * @param ku
	 * @param kuv
	 * @param kv
	 * @param newNorm
	 * @param pdir1
	 * @param pdir2
	 * @param ks
	 */
	void diagonalizeCurv(final WB_Vector oldU, final WB_Vector oldV, final double ku, final double kuv, final double kv,
			final WB_Vector newNorm, final WB_Vector pdir1, final WB_Vector pdir2, final WB_Vector ks) {
		final WB_Vector rOldU = geometryfactory.createVector();
		final WB_Vector rOldV = geometryfactory.createVector();
		rotCoordSys(oldU, oldV, newNorm, rOldU, rOldV);
		double c = 1, s = 0, tt = 0;
		if (kuv != 0.0f) {
			// Jacobi rotation to diagonalize
			final double h = 0.5 * (kv - ku) / kuv;
			tt = h < 0.0 ? 1.0 / (h - Math.sqrt(1.0 + h * h)) : 1.0 / (h + Math.sqrt(1.0 + h * h));
			c = 1.0 / Math.sqrt(1.0 + tt * tt);
			s = tt * c;
		}
		ks.set(ku - tt * kuv, kv + tt * kuv, 0);
		if (Math.abs(ks.xd()) >= Math.abs(ks.yd())) {
			pdir1.set(rOldU.mulAddMul(c, -s, rOldV));
		} else {
			ks.set(ks.yd(), ks.zd(), ks.xd());
			pdir1.set(rOldU.mulAddMul(s, c, rOldV));
		}
		pdir2.set(newNorm.cross(pdir1));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k1(int)
	 */
	@Override
	public double k1(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return curv1[i];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k2(int)
	 */
	@Override
	public double k2(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return curv2[i];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#K(int)
	 */
	@Override
	public double K(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return curv2[i] * curv1[i];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k1min()
	 */
	@Override
	public double k1min() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k1min;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k2min()
	 */
	@Override
	public double k2min() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k2min;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#Kmin()
	 */
	@Override
	public double Kmin() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return Kmin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k1max()
	 */
	@Override
	public double k1max() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k1max;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k2max()
	 */
	@Override
	public double k2max() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return k2max;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#Kmax()
	 */
	@Override
	public double Kmax() {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return Kmax;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k1dir(int)
	 */
	@Override
	public WB_Vector k1dir(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return pdir1[i];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#k2dir(int)
	 */
	@Override
	public WB_Vector k2dir(final int i) {
		if (!curvaturesUpdated) {
			updateCurvatures();
		}
		return pdir2[i];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#DCurv(int)
	 */
	@Override
	public double[] DCurv(final int i) {
		if (!DCurvaturesUpdated) {
			updateDCurvatures();
		}
		return dcurv[i];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#DCurvInvariant(int)
	 */
	@Override
	public double DCurvInvariant(final int i) {
		if (!DCurvaturesUpdated) {
			updateDCurvatures();
		}
		return dcurv[i][0] * dcurv[i][0] + dcurv[i][1] * dcurv[i][1] + dcurv[i][2] * dcurv[i][2]
				+ dcurv[i][3] * dcurv[i][3];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_FaceListMesh#vfNeighbors(int)
	 */
	@Override
	public int[] vfNeighbors(final int i) {
		if (!vfNeighborsUpdated) {
			updatevfNeighbors();
		}
		return vfNeighbors[i];
	}
}
