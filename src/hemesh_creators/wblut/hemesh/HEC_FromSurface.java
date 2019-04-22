/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_Point;
import wblut.geom.WB_Surface;

/**
 *
 */
public class HEC_FromSurface extends HEC_Creator {
	/**
	 * 
	 */
	private int			U;
	/**
	 * 
	 */
	private int			V;
	/**
	 * 
	 */
	private WB_Surface	surf;
	/**
	 * 
	 */
	private boolean		uWrap;
	/**
	 * 
	 */
	private boolean		vWrap;
	private double		lowerU;
	private double		upperU;
	private double		lowerV;
	private double		upperV;

	/**
	 * 
	 */
	public HEC_FromSurface() {
		super();
		override = true;
	}

	/**
	 * 
	 *
	 * @param surf
	 * @param U
	 * @param V
	 * @param uWrap
	 * @param vWrap
	 */
	public HEC_FromSurface(final WB_Surface surf, final int U, final int V,
			final boolean uWrap, final boolean vWrap) {
		this();
		this.U = U;
		this.V = V;
		this.uWrap = uWrap;
		this.vWrap = vWrap;
		this.surf = surf;
		lowerU = surf.getLowerU();
		upperU = surf.getUpperU();
		lowerV = surf.getLowerV();
		upperV = surf.getUpperV();
	}

	/**
	 * 
	 *
	 * @param U
	 * @return
	 */
	public HEC_FromSurface setU(final int U) {
		this.U = U;
		return this;
	}

	public HEC_FromSurface setRange(final double lowerU, double upperU,
			double lowerV, double upperV) {
		this.lowerU = Math.max(lowerU, surf.getLowerU());
		this.upperU = Math.min(upperU, surf.getUpperU());
		this.lowerV = Math.max(lowerV, surf.getLowerV());
		this.upperV = Math.min(upperV, surf.getUpperV());
		return this;
	}

	/**
	 * 
	 *
	 * @param V
	 * @return
	 */
	public HEC_FromSurface setV(final int V) {
		this.V = V;
		return this;
	}

	/**
	 * 
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromSurface setUWrap(final boolean b) {
		uWrap = b;
		return this;
	}

	/**
	 * 
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromSurface setVWrap(final boolean b) {
		vWrap = b;
		return this;
	}

	/**
	 * 
	 *
	 * @param surf
	 * @return
	 */
	public HEC_FromSurface setSurface(final WB_Surface surf) {
		this.surf = surf;
		lowerU = surf.getLowerU();
		upperU = surf.getUpperU();
		lowerV = surf.getLowerV();
		upperV = surf.getUpperV();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.nurbs.WB_Surface#toHemesh(PApplet, double, double)
	 */
	@Override
	public HE_Mesh createBase() {
		final WB_Point[] points = new WB_Point[(U + 1) * (V + 1)];
		final double iU = 1.0 / U;
		final double iV = 1.0 / V;
		double u;
		double v;
		for (int i = 0; i <= U; i++) {
			u = lowerU + i * iU * (upperU - lowerU);
			for (int j = 0; j <= V; j++) {
				v = lowerV + j * iV * (upperV - lowerV);
				points[i + (U + 1) * j] = surf.surfacePoint(u, v);
			}
		}
		final int[][] faces = new int[U * V][4];
		int li, lj;
		for (int i = 0; i < U; i++) {
			li = uWrap && i == U - 1 ? 0 : i + 1;
			for (int j = 0; j < V; j++) {
				lj = vWrap && j == V - 1 ? 0 : j + 1;
				faces[i + U * j][0] = i + (U + 1) * j;
				faces[i + U * j][1] = li + (U + 1) * j;
				faces[i + U * j][2] = li + (U + 1) * lj;
				faces[i + U * j][3] = i + (U + 1) * lj;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setFaces(faces).setVertices(points).setDuplicate(false)
				.setCheckNormals(false);
		return new HE_Mesh(fl);
	}
}
