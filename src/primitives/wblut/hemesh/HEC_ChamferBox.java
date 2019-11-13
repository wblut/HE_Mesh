/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Coord;

/**
 * Axis Aligned Box.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_ChamferBox extends HEC_Creator {
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

	/** chamfer width. */
	private double CW;
	/** chamfer height. */
	private double CH;
	/** chamfer depth. */
	private double CD;

	/** inner width. */
	private double IW;
	/** inner height. */
	private double IH;
	/** inner depth. */
	private double ID;

	/**
	 * Create a placeholder box.
	 *
	 */
	public HEC_ChamferBox() {
		super();
		W = H = D = 100;
		L = M = N = 1;
		CW = CH = CD = 10;
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
	 * @param CW
	 *            chamfer width (X)
	 * @param CH
	 *            chamfer height (Y)
	 * @param CD
	 *            chamfer depth (Z)
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_ChamferBox(final double W, final double H, final double D, final double CW, final double CH,
			final double CD, final int L, final int M, final int N) {
		this();
		this.W = W;
		this.H = H;
		this.D = D;
		this.CW = CW;
		this.CH = CH;
		this.CD = CD;
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
	 * @param CW
	 *            chamfer width (X)
	 * @param CH
	 *            chamfer height (Y)
	 * @param CD
	 *            chamfer depth (Z)
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_ChamferBox(final double x, final double y, final double z, final double W, final double H,
			final double D, final double CW, final double CH, final double CD, final int L, final int M, final int N) {
		this();
		setCenter(x, y, z);
		this.W = W;
		this.H = H;
		this.D = D;
		this.CW = CW;
		this.CH = CH;
		this.CD = CD;
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
	 * @param CW
	 *            chamfer width (X)
	 * @param CH
	 *            chamfer height (Y)
	 * @param CD
	 *            chamfer depth (Z)
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_ChamferBox(final WB_Coord center, final double W, final double H, final double D, final double CW,
			final double CH, final double CD, final int L, final int M, final int N) {
		this();
		setCenter(center.xd(), center.yd(), center.zd());
		this.W = W;
		this.H = H;
		this.D = D;
		this.CW = CW;
		this.CH = CH;
		this.CD = CD;
		this.L = Math.max(1, L);
		this.M = Math.max(1, M);
		this.N = Math.max(1, N);
	}

	/**
	 *
	 * @param L
	 * @param M
	 * @param N
	 * @return
	 */
	public HEC_ChamferBox setSegments(final int L, final int M, final int N) {
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
	public HEC_ChamferBox setWidth(final double W) {
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
	public HEC_ChamferBox setHeight(final double H) {
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
	public HEC_ChamferBox setDepth(final double D) {
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
	public HEC_ChamferBox setWidthSegments(final int L) {
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
	public HEC_ChamferBox setHeightSegments(final int M) {
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
	public HEC_ChamferBox setDepthSegments(final int N) {
		this.N = Math.max(1, N);
		return this;
	}

	public HEC_ChamferBox setChamfer(final double c) {
		CW = CH = CD = c;
		return this;
	}

	public HEC_ChamferBox setChamfer(final double CW, final double CH, final double CD) {
		this.CW = CW;
		this.CH = CH;
		this.CD = CD;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		IW = W - 2 * CW;
		IH = H - 2 * CH;
		ID = D - 2 * CD;

		final double oW = -0.5 * W;// X
		final double oH = -0.5 * H;// Y
		final double oD = -0.5 * D;// Z
		final double oiW = -0.5 * IW;// X
		final double oiH = -0.5 * IH;// Y
		final double oiD = -0.5 * ID;// Z
		final double dW = IW * 1.0 / L;
		final double dH = IH * 1.0 / M;
		final double dD = ID * 1.0 / N;
		final double di = 1.0 / L;
		final double dj = 1.0 / M;
		final double dk = 1.0 / N;
		final double[][] vertices = new double[2 * (L + 1) * (M + 1) + 2 * (L + 1) * (N + 1)
				+ 2 * (M + 1) * (N + 1)][3];
		final double[][] uvws = new double[2 * (L + 1) * (M + 1) + 2 * (L + 1) * (N + 1) + 2 * (M + 1) * (N + 1)][3];
		final int[][] faces = new int[2 * M * L + 2 * L * N + 2 * M * N + 4 * L + 4 * M + 4 * N + 8][];// 6
																										// faces
																										// +
																										// 12
																										// edges+
																										// 8
																										// corners,
		final int[] faceTextureIds = new int[2 * M * L + 2 * L * N + 2 * M * N + 4 * L + 4 * M + 4 * N + 8];
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
				vertices[idv][0] = vertices[idv + LMv][0] = oiW + u * dW;
				vertices[idv][1] = vertices[idv + LMv][1] = oiH + v * dH;
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
				faces[idf] = new int[4];
				faces[idf][3] = i + j * (L + 1);
				faces[idf][2] = i + 1 + j * (L + 1);
				faces[idf][1] = i + 1 + (j + 1) * (L + 1);
				faces[idf][0] = i + (j + 1) * (L + 1);
				faces[idf + LMf] = new int[4];
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
				vertices[idv][0] = vertices[idv + LNv][0] = oiW + u * dW;
				vertices[idv][2] = vertices[idv + LNv][2] = oiD + v * dD;
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
				faces[idf] = new int[4];
				faces[idf][0] = i + j * (L + 1) + offset;
				faces[idf][1] = i + 1 + j * (L + 1) + offset;
				faces[idf][2] = i + 1 + (j + 1) * (L + 1) + offset;
				faces[idf][3] = i + (j + 1) * (L + 1) + offset;
				faces[idf + LNf] = new int[4];
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

				vertices[idv][1] = vertices[idv + MNv][1] = oiH + v * dH;
				vertices[idv][2] = vertices[idv + MNv][2] = oiD + u * dD;
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
				faces[idf] = new int[4];
				faces[idf][3] = i + j * (M + 1) + offset;
				faces[idf][2] = i + 1 + j * (M + 1) + offset;
				faces[idf][1] = i + 1 + (j + 1) * (M + 1) + offset;
				faces[idf][0] = i + (j + 1) * (M + 1) + offset;
				faces[idf + MNf] = new int[4];
				faces[idf + MNf][0] = i + j * (M + 1) + MNv + offset;
				faces[idf + MNf][1] = i + 1 + j * (M + 1) + MNv + offset;
				faces[idf + MNf][2] = i + 1 + (j + 1) * (M + 1) + MNv + offset;
				faces[idf + MNf][3] = i + (j + 1) * (M + 1) + MNv + offset;
				faceTextureIds[idf] = 4;
				faceTextureIds[idf + MNf] = 5;
				idf++;
			}
		}
		idf = 2 * LMf + 2 * LNf + 2 * MNf;
		// 4 X-edges
		for (int i = 0; i < L; i++) {
			faces[idf] = new int[4];
			faces[idf][0] = i;
			faces[idf][1] = i + 1;
			faces[idf][2] = 2 * LMv + i + 1;
			faces[idf][3] = 2 * LMv + i;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][3] = i + M * (L + 1);
			faces[idf][2] = i + M * (L + 1) + 1;
			faces[idf][1] = 2 * LMv + LNv + i + 1;
			faces[idf][0] = 2 * LMv + LNv + i;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][3] = i + LMv;
			faces[idf][2] = i + LMv + 1;
			faces[idf][1] = 2 * LMv + N * (L + 1) + i + 1;
			faces[idf][0] = 2 * LMv + N * (L + 1) + i;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][0] = i + M * (L + 1) + LMv;
			faces[idf][1] = i + M * (L + 1) + LMv + 1;
			faces[idf][2] = 2 * LMv + N * (L + 1) + LNv + i + 1;
			faces[idf][3] = 2 * LMv + N * (L + 1) + LNv + i;
			faceTextureIds[idf] = 7;
			idf++;
		}
		// 4 Y-edges
		for (int i = 0; i < M; i++) {
			faces[idf] = new int[4];
			faces[idf][3] = i * (L + 1);
			faces[idf][2] = (i + 1) * (L + 1);
			faces[idf][1] = 2 * LMv + 2 * LNv + i + 1;
			faces[idf][0] = 2 * LMv + 2 * LNv + i;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][0] = i * (L + 1) + L;
			faces[idf][1] = (i + 1) * (L + 1) + L;
			faces[idf][2] = 2 * LMv + 2 * LNv + i + 1 + MNv;
			faces[idf][3] = 2 * LMv + 2 * LNv + i + MNv;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][0] = i * (L + 1) + LMv;
			faces[idf][1] = (i + 1) * (L + 1) + LMv;
			faces[idf][2] = 2 * LMv + 2 * LNv + N * (M + 1) + i + 1;
			faces[idf][3] = 2 * LMv + 2 * LNv + N * (M + 1) + i;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][3] = i * (L + 1) + L + LMv;
			faces[idf][2] = (i + 1) * (L + 1) + L + LMv;
			faces[idf][1] = 2 * LMv + 2 * LNv + N * (M + 1) + i + 1 + MNv;
			faces[idf][0] = 2 * LMv + 2 * LNv + N * (M + 1) + i + MNv;
			faceTextureIds[idf] = 7;
			idf++;
		}
		// 4 Z-edges
		for (int i = 0; i < N; i++) {
			faces[idf] = new int[4];
			faces[idf][0] = 2 * LMv + i * (L + 1);
			faces[idf][1] = 2 * LMv + (i + 1) * (L + 1);
			faces[idf][2] = 2 * LMv + 2 * LNv + (i + 1) * (M + 1);
			faces[idf][3] = 2 * LMv + 2 * LNv + i * (M + 1);
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][3] = 2 * LMv + i * (L + 1) + L;
			faces[idf][2] = 2 * LMv + (i + 1) * (L + 1) + L;
			faces[idf][1] = 2 * LMv + 2 * LNv + (i + 1) * (M + 1) + MNv;
			faces[idf][0] = 2 * LMv + 2 * LNv + i * (M + 1) + MNv;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][3] = 2 * LMv + i * (L + 1) + LNv;
			faces[idf][2] = 2 * LMv + (i + 1) * (L + 1) + LNv;
			faces[idf][1] = 2 * LMv + 2 * LNv + (i + 1) * (M + 1) + M;
			faces[idf][0] = 2 * LMv + 2 * LNv + i * (M + 1) + M;
			faceTextureIds[idf] = 7;
			idf++;
			faces[idf] = new int[4];
			faces[idf][0] = 2 * LMv + i * (L + 1) + L + LNv;
			faces[idf][1] = 2 * LMv + (i + 1) * (L + 1) + L + LNv;
			faces[idf][2] = 2 * LMv + 2 * LNv + (i + 1) * (M + 1) + M + MNv;
			faces[idf][3] = 2 * LMv + 2 * LNv + i * (M + 1) + M + MNv;
			faceTextureIds[idf] = 7;
			idf++;

		}

		// 8 corners

		faces[idf] = new int[3];
		faces[idf][0] = 0;
		faces[idf][1] = 2 * LMv;
		faces[idf][2] = 2 * LMv + 2 * LNv;
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][2] = L;
		faces[idf][1] = 2 * LMv + L;
		faces[idf][0] = 2 * LMv + 2 * LNv + MNv;
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][2] = M * (L + 1);
		faces[idf][1] = 2 * LMv + LNv;
		faces[idf][0] = 2 * LMv + 2 * LNv + M;
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][0] = M * (L + 1) + L;
		faces[idf][1] = 2 * LMv + L + LNv;
		faces[idf][2] = 2 * LMv + 2 * LNv + M + MNv;
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][2] = LMv;
		faces[idf][1] = 2 * LMv + N * (L + 1);
		faces[idf][0] = 2 * LMv + 2 * LNv + N * (M + 1);
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][0] = L + LMv;
		faces[idf][1] = 2 * LMv + N * (L + 1) + L;
		faces[idf][2] = 2 * LMv + 2 * LNv + N * (M + 1) + MNv;
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][0] = M * (L + 1) + LMv;
		faces[idf][1] = 2 * LMv + LNv + N * (L + 1);
		faces[idf][2] = 2 * LMv + 2 * LNv + M + N * (M + 1);
		faceTextureIds[idf] = 8;
		idf++;

		faces[idf] = new int[3];
		faces[idf][2] = 2 * LMv - 1;
		faces[idf][1] = 2 * LMv + 2 * LNv - 1;
		faces[idf][0] = 2 * LMv + 2 * LNv + 2 * MNv - 1;
		faceTextureIds[idf] = 8;
		idf++;

		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setCheckDuplicateVertices(true).setVertexUVW(uvws)
				.setFaceTextureIds(faceTextureIds);
		HE_Mesh mesh=fl.createBase();
		HE_Selection sel=mesh.getNewSelection("edges");
		for(idf= 2 * LMf + 2 * LNf + 2 * MNf;idf<mesh.getNumberOfFaces()-8;idf++) {
			sel.add(mesh.getFaceWithIndex(idf));
		}
		sel=mesh.getNewSelection("corners");
		for(;idf<mesh.getNumberOfFaces();idf++) {
			sel.add(mesh.getFaceWithIndex(idf));
		}
		return mesh;
	}

	public static void main(final String[] args) {
		HEC_ChamferBox creator = new HEC_ChamferBox();
		creator.setWidth(400).setHeight(60).setDepth(200);
		creator.setWidthSegments(14).setHeightSegments(3).setDepthSegments(20);
		new HE_Mesh(creator);
	}
}
