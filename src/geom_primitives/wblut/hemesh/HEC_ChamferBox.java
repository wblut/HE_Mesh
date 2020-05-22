package wblut.hemesh;

import wblut.geom.WB_Coord;

/**
 *
 */
public class HEC_ChamferBox extends HEC_Creator {
	/**
	 *
	 */
	public HEC_ChamferBox() {
		super();
		parameters.set("width", 100);
		parameters.set("height", 100);
		parameters.set("depth", 100);
		parameters.set("chamferwidth", 10);
		parameters.set("chamferheight", 10);
		parameters.set("chamferdepth", 10);
		parameters.set("widthSegments", 1);
		parameters.set("heightSegments", 1);
		parameters.set("depthSegments", 1);
	}

	/**
	 *
	 *
	 * @param W
	 * @param H
	 * @param D
	 * @param CW
	 * @param CH
	 * @param CD
	 * @param L
	 * @param M
	 * @param N
	 */
	public HEC_ChamferBox(final double W, final double H, final double D, final double CW, final double CH,
			final double CD, final int L, final int M, final int N) {
		this();
		parameters.set("width", W);
		parameters.set("height", H);
		parameters.set("depth", D);
		parameters.set("chamferwidth", CW);
		parameters.set("chamferheight", CH);
		parameters.set("chamferdepth", CD);
		parameters.set("widthSegments", Math.max(1, L));
		parameters.set("heightSegments", Math.max(1, M));
		parameters.set("depthSegments", Math.max(1, N));
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param W
	 * @param H
	 * @param D
	 * @param CW
	 * @param CH
	 * @param CD
	 * @param L
	 * @param M
	 * @param N
	 */
	public HEC_ChamferBox(final double x, final double y, final double z, final double W, final double H,
			final double D, final double CW, final double CH, final double CD, final int L, final int M, final int N) {
		this();
		setCenter(x, y, z);
		parameters.set("width", W);
		parameters.set("height", H);
		parameters.set("depth", D);
		parameters.set("chamferwidth", CW);
		parameters.set("chamferheight", CH);
		parameters.set("chamferdepth", CD);
		parameters.set("widthSegments", Math.max(1, L));
		parameters.set("heightSegments", Math.max(1, M));
		parameters.set("depthSegments", Math.max(1, N));
	}

	/**
	 *
	 *
	 * @param center
	 * @param W
	 * @param H
	 * @param D
	 * @param CW
	 * @param CH
	 * @param CD
	 * @param L
	 * @param M
	 * @param N
	 */
	public HEC_ChamferBox(final WB_Coord center, final double W, final double H, final double D, final double CW,
			final double CH, final double CD, final int L, final int M, final int N) {
		this();
		setCenter(center.xd(), center.yd(), center.zd());
		parameters.set("width", W);
		parameters.set("height", H);
		parameters.set("depth", D);
		parameters.set("chamferwidth", CW);
		parameters.set("chamferheight", CH);
		parameters.set("chamferdepth", CD);
		parameters.set("widthSegments", Math.max(1, L));
		parameters.set("heightSegments", Math.max(1, M));
		parameters.set("depthSegments", Math.max(1, N));
	}

	/**
	 *
	 *
	 * @param L
	 * @param M
	 * @param N
	 * @return
	 */
	public HEC_ChamferBox setSegments(final int L, final int M, final int N) {
		parameters.set("widthSegments", Math.max(1, L));
		parameters.set("heightSegments", Math.max(1, M));
		parameters.set("depthSegments", Math.max(1, N));
		return this;
	}

	/**
	 *
	 *
	 * @param W
	 * @return
	 */
	public HEC_ChamferBox setWidth(final double W) {
		parameters.set("width", W);
		return this;
	}

	/**
	 *
	 *
	 * @param H
	 * @return
	 */
	public HEC_ChamferBox setHeight(final double H) {
		parameters.set("height", H);
		return this;
	}

	/**
	 *
	 *
	 * @param D
	 * @return
	 */
	public HEC_ChamferBox setDepth(final double D) {
		parameters.set("depth", D);
		return this;
	}

	/**
	 *
	 *
	 * @param L
	 * @return
	 */
	public HEC_ChamferBox setWidthSegments(final int L) {
		parameters.set("widthSegments", Math.max(1, L));
		return this;
	}

	/**
	 *
	 *
	 * @param M
	 * @return
	 */
	public HEC_ChamferBox setHeightSegments(final int M) {
		parameters.set("heightSegments", Math.max(1, M));
		return this;
	}

	/**
	 *
	 *
	 * @param N
	 * @return
	 */
	public HEC_ChamferBox setDepthSegments(final int N) {
		parameters.set("depthSegments", Math.max(1, N));
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEC_ChamferBox setChamfer(final double c) {
		parameters.set("chamferwidth", c);
		parameters.set("chamferheight", c);
		parameters.set("chamferdepth", c);
		return this;
	}

	/**
	 *
	 *
	 * @param CW
	 * @param CH
	 * @param CD
	 * @return
	 */
	public HEC_ChamferBox setChamfer(final double CW, final double CH, final double CD) {
		parameters.set("chamferwidth", CW);
		parameters.set("chamferheight", CH);
		parameters.set("chamferdepth", CD);
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEC_ChamferBox setChamferWidth(final double c) {
		parameters.set("chamferwidth", c);
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEC_ChamferBox setChamferHeight(final double c) {
		parameters.set("chamferheight", c);
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEC_ChamferBox setChamferDepth(final double c) {
		parameters.set("chamferdepth", c);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getWidth() {
		return parameters.get("width", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getHeight() {
		return parameters.get("height", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getDepth() {
		return parameters.get("depth", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getChamferWidth() {
		return parameters.get("chamferwidth", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getChamferHeight() {
		return parameters.get("chamferheight", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected double getChamferDepth() {
		return parameters.get("chamferdepth", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected int getWidthSegments() {
		return parameters.get("widthSegments", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected int getHeightSegments() {
		return parameters.get("heightSegments", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	protected int getDepthSegments() {
		return parameters.get("depthSegments", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		final double W = getWidth();
		final double H = getHeight();
		final double D = getDepth();
		final int L = getWidthSegments();
		final int M = getHeightSegments();
		final int N = getDepthSegments();
		final double CW = getChamferWidth();
		final double CH = getChamferHeight();
		final double CD = getChamferDepth();
		final double IW = W - 2 * CW;
		final double IH = H - 2 * CH;
		final double ID = D - 2 * CD;
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
				uvws[idv + LMv][0] = (1 - uvws[idv][0]);
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
				uvws[idv][1] = (1 - uvws[idv + LNv][1]);
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
				uvws[idv][0] = (1 - uvws[idv + MNv][0]);
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
		fl.setVertices(vertices).setFaces(faces).setCheckDuplicateVertices(true).setUVW(uvws)
				.setFaceTextureIds(faceTextureIds);
		final HE_Mesh mesh = fl.createBase();
		HE_Selection sel = mesh.getNewSelection("edges");
		for (idf = 2 * LMf + 2 * LNf + 2 * MNf; idf < mesh.getNumberOfFaces() - 8; idf++) {
			sel.add(mesh.getFaceWithIndex(idf));
		}
		sel = mesh.getNewSelection("corners");
		for (; idf < mesh.getNumberOfFaces(); idf++) {
			sel.add(mesh.getFaceWithIndex(idf));
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final HEC_ChamferBox creator = new HEC_ChamferBox();
		creator.setWidth(400).setHeight(60).setDepth(200);
		creator.setWidthSegments(14).setHeightSegments(3).setDepthSegments(20);
		new HE_Mesh(creator);
	}
}
