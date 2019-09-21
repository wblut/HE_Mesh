/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;

/**
 * Axis Aligned Box.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Box extends HEC_Creator {
	/** width. */
	private double W;
	/** height. */
	private double H;
	/** depth. */
	private double D;
	/** width segments. */
	private int L;
	/** height segments. */
	private int M;
	/** depth segments. */
	private int N;

	/**
	 * Create a placeholder box.
	 *
	 */
	public HEC_Box() {
		super();
		W = H = D = 100;
		L = M = N = 1;
	}

	/**
	 * Create a box at (0,0,0).
	 *
	 * @param W
	 *            width (X)
	 * @param H
	 *            height (Y)
	 * @param D
	 *            depth (Z)
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_Box(final double W, final double H, final double D, final int L, final int M, final int N) {
		this();
		this.W = W;
		this.H = H;
		this.D = D;
		this.L = Math.max(1, L);
		this.M = Math.max(1, M);
		this.N = Math.max(1, N);
	}

	/**
	 * Create a box at (x,y,z).
	 *
	 *
	 * @param x
	 *            x-ordinate of center
	 * @param y
	 *            y-ordinate of center
	 * @param z
	 *            z-ordinate of center
	 * @param W
	 *            width (X)
	 * @param H
	 *            height (Y)
	 * @param D
	 *            depth (Z)
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_Box(final double x, final double y, final double z, final double W, final double H, final double D,
			final int L, final int M, final int N) {
		this();
		setCenter(x, y, z);
		this.W = W;
		this.H = H;
		this.D = D;
		this.L = Math.max(1, L);
		this.M = Math.max(1, M);
		this.N = Math.max(1, N);
	}

	/**
	 * Create a box at center.
	 *
	 *
	 * @param center
	 *            center
	 * @param W
	 *            width (X)
	 * @param H
	 *            height (Y)
	 * @param D
	 *            depth (Z)
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_Box(final WB_Coord center, final double W, final double H, final double D, final int L, final int M,
			final int N) {
		this();
		setCenter(center.xd(), center.yd(), center.zd());
		this.W = W;
		this.H = H;
		this.D = D;
		this.L = Math.max(1, L);
		this.M = Math.max(1, M);
		this.N = Math.max(1, N);
	}

	/**
	 * Set box from AABB.
	 *
	 * @param AABB
	 *            WB_AABB
	 * @param padding
	 *            the padding
	 * @return self
	 */
	public HEC_Box setFromAABB(final WB_AABB AABB, final double padding) {
		W = AABB.getWidth() + 2 * padding;
		H = AABB.getHeight() + 2 * padding;
		D = AABB.getDepth() + 2 * padding;
		setCenter(AABB.getCenter());
		return this;
	}

	public HEC_Box setFromAABB(final WB_AABB AABB) {
		W = AABB.getWidth();
		H = AABB.getHeight();
		D = AABB.getDepth();
		setCenter(AABB.getCenter());
		return this;
	}

	/**
	 *
	 * @param min
	 * @param max
	 * @return
	 */
	public HEC_Box setFromCorners(final WB_Coord min, final WB_Coord max) {
		W = max.xd() - min.xd();
		H = max.yd() - min.yd();
		D = max.zd() - min.zd();
		setCenter(0.5 * (max.xd() + min.xd()), 0.5 * (max.yd() + min.yd()), 0.5 * (max.zd() + min.zd()));
		return this;
	}

	/**
	 *
	 * @param minx
	 * @param miny
	 * @param minz
	 * @param maxx
	 * @param maxy
	 * @param maxz
	 * @return
	 */
	public HEC_Box setFromCorners(final double minx, final double miny, final double minz, final double maxx,
			final double maxy, final double maxz) {
		W = maxx - minx;
		H = maxy - miny;
		D = maxz - minz;
		setCenter(0.5 * (maxx + minx), 0.5 * (maxy + miny), 0.5 * (maxz + minz));
		return this;
	}

	/**
	 *
	 * @param L
	 * @param M
	 * @param N
	 * @return
	 */
	public HEC_Box setSegments(final int L, final int M, final int N) {
		this.L = L;
		this.M = M;
		this.N = N;
		return this;
	}

	/**
	 * Set box width.
	 *
	 * @param W
	 *            width of box (x-axis)
	 * @return self
	 */
	public HEC_Box setWidth(final double W) {
		this.W = W;
		return this;
	}

	/**
	 * Set box height.
	 *
	 * @param H
	 *            height of box (y-axis)
	 * @return self
	 */
	public HEC_Box setHeight(final double H) {
		this.H = H;
		return this;
	}

	/**
	 * Set box depth.
	 *
	 * @param D
	 *            depth of box (z-axis)
	 * @return self
	 */
	public HEC_Box setDepth(final double D) {
		this.D = D;
		return this;
	}

	public HEC_Box setSize(final double W, final double H, final double D) {
		this.W = W;
		this.H = H;
		this.D = D;
		return this;
	}

	/**
	 * Set box width segments.
	 *
	 * @param L
	 *            number of width segments (x-axis)
	 * @return self
	 */
	public HEC_Box setWidthSegments(final int L) {
		this.L = Math.max(1, L);
		return this;
	}

	/**
	 * Set box height segments.
	 *
	 * @param M
	 *            number of height segments (y-axis)
	 * @return self
	 */
	public HEC_Box setHeightSegments(final int M) {
		this.M = Math.max(1, M);
		return this;
	}

	/**
	 * Set box depth segments.
	 *
	 * @param N
	 *            number of depth segments (z-axis)
	 * @return self
	 */
	public HEC_Box setDepthSegments(final int N) {
		this.N = Math.max(1, N);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final double oW = -0.5 * W;// X
		final double oH = -0.5 * H;// Y
		final double oD = -0.5 * D;// Z
		final double dW = W * 1.0 / L;
		final double dH = H * 1.0 / M;
		final double dD = D * 1.0 / N;
		final double di = 1.0 / L;
		final double dj = 1.0 / M;
		final double dk = 1.0 / N;
		final double[][] vertices = new double[2 * (L + 1) * (M + 1) + 2 * (L + 1) * (N + 1)
				+ 2 * (M + 1) * (N + 1)][3];
		final double[][] uvws = new double[2 * (L + 1) * (M + 1) + 2 * (L + 1) * (N + 1) + 2 * (M + 1) * (N + 1)][3];
		final int[][] faces = new int[2 * M * L + 2 * L * N + 2 * M * N][4];// XY,XZ,YZ
		final int[] faceTextureIds = new int[2 * M * L + 2 * L * N + 2 * M * N];
		int idv = 0;
		int idf = 0;
		final int LMv = (L + 1) * (M + 1);
		final int LMf = L * M;
		final int LNv = (L + 1) * (N + 1);
		final int LNf = L * N;
		final int MNv = (M + 1) * (N + 1);
		final int MNf = M * N;
		for (int v = 0; v < M + 1; v++) {
			for (int u = 0; u < L + 1; u++) {
				vertices[idv][0] = vertices[idv + LMv][0] = oW + u * dW;
				vertices[idv][1] = vertices[idv + LMv][1] = oH + v * dH;
				vertices[idv][2] = oD;
				vertices[idv + LMv][2] = -oD;
				uvws[idv][0] = di * u;
				uvws[idv + LMv][1] = uvws[idv][1] = dj * v;
				uvws[idv + LMv][0] = 1 - uvws[idv][0];
				uvws[idv][2] = uvws[idv + LMv][2] = 0;
				idv++;
			}
		}
		for (int j = 0; j < M; j++) {
			for (int i = 0; i < L; i++) {
				faces[idf][3] = i + j * (L + 1);
				faces[idf][2] = i + 1 + j * (L + 1);
				faces[idf][1] = i + 1 + (j + 1) * (L + 1);
				faces[idf][0] = i + (j + 1) * (L + 1);
				faces[idf + LMf][0] = i + j * (L + 1) + LMv;
				faces[idf + LMf][1] = i + 1 + j * (L + 1) + LMv;
				faces[idf + LMf][2] = i + 1 + (j + 1) * (L + 1) + LMv;
				faces[idf + LMf][3] = i + (j + 1) * (L + 1) + LMv;
				faceTextureIds[idf] = 0;
				faceTextureIds[idf + LMf] = 1;
				idf++;
			}
		}
		int offset = 2 * LMv;
		idv = offset;
		for (int v = 0; v < N + 1; v++) {
			for (int u = 0; u < L + 1; u++) {
				vertices[idv][0] = vertices[idv + LNv][0] = oW + u * dW;
				vertices[idv][2] = vertices[idv + LNv][2] = oD + v * dD;
				vertices[idv][1] = oH;
				vertices[idv + LNv][1] = -oH;
				uvws[idv][0] = uvws[idv + LNv][0] = di * u;
				uvws[idv + LNv][1] = dk * v;
				uvws[idv][1] = 1 - uvws[idv + LNv][1];
				uvws[idv][2] = uvws[idv + LNv][2] = 0;
				idv++;
			}
		}
		idf = 2 * LMf;
		for (int j = 0; j < N; j++) {
			for (int i = 0; i < L; i++) {
				faces[idf][0] = i + j * (L + 1) + offset;
				faces[idf][1] = i + 1 + j * (L + 1) + offset;
				faces[idf][2] = i + 1 + (j + 1) * (L + 1) + offset;
				faces[idf][3] = i + (j + 1) * (L + 1) + offset;
				faces[idf + LNf][3] = i + j * (L + 1) + LNv + offset;
				faces[idf + LNf][2] = i + 1 + j * (L + 1) + LNv + offset;
				faces[idf + LNf][1] = i + 1 + (j + 1) * (L + 1) + LNv + offset;
				faces[idf + LNf][0] = i + (j + 1) * (L + 1) + LNv + offset;
				faceTextureIds[idf] = 2;
				faceTextureIds[idf + LNf] = 3;
				idf++;
			}
		}
		offset = 2 * LMv + 2 * LNv;
		idv = offset;
		for (int u = 0; u < N + 1; u++) {
			for (int v = 0; v < M + 1; v++) {
				vertices[idv][1] = vertices[idv + MNv][1] = oH + v * dH;
				vertices[idv][2] = vertices[idv + MNv][2] = oD + u * dD;
				vertices[idv][0] = oW;
				vertices[idv + MNv][0] = -oW;
				uvws[idv + MNv][0] = dk * u;
				uvws[idv][0] = 1 - uvws[idv + MNv][0];
				uvws[idv + MNv][1] = uvws[idv][1] = v * dj;
				uvws[idv + MNv][2] = uvws[idv][2] = 0;
				idv++;
			}
		}
		idf = 2 * LMf + 2 * LNf;
		for (int j = 0; j < N; j++) {
			for (int i = 0; i < M; i++) {
				faces[idf][3] = i + j * (M + 1) + offset;
				faces[idf][2] = i + 1 + j * (M + 1) + offset;
				faces[idf][1] = i + 1 + (j + 1) * (M + 1) + offset;
				faces[idf][0] = i + (j + 1) * (M + 1) + offset;
				faces[idf + MNf][0] = i + j * (M + 1) + MNv + offset;
				faces[idf + MNf][1] = i + 1 + j * (M + 1) + MNv + offset;
				faces[idf + MNf][2] = i + 1 + (j + 1) * (M + 1) + MNv + offset;
				faces[idf + MNf][3] = i + (j + 1) * (M + 1) + MNv + offset;
				faceTextureIds[idf] = 4;
				faceTextureIds[idf + MNf] = 5;
				idf++;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setCheckDuplicateVertices(true).setVertexUVW(uvws)
				.setFaceTextureIds(faceTextureIds);
		return fl.createBase();
	}
}
