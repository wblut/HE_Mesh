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
	public HEC_FromSurface() {
		super();
		setOverride(true);
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
	public HEC_FromSurface(final WB_Surface surf, final int U, final int V, final boolean uWrap, final boolean vWrap) {
		this();
		parameters.set("u", U);
		parameters.set("v", V);
		parameters.set("uwrap",uWrap);
		parameters.set("vwrap",vWrap);
		parameters.set("surf", surf);
		parameters.set("loweru", surf.getLowerU());
		parameters.set("upperu", surf.getUpperU());
		parameters.set("lowerv", surf.getLowerV());
		parameters.set("upperv", surf.getUpperV());
	}
	
	protected int getU() {
		return parameters.get("u", 1);
	}

	protected int getV() {
		return parameters.get("v", 1);
	}
	
	protected boolean getUWrap() {
		return parameters.get("uwrap", false);
	}

	protected boolean getVWrap() {
		return parameters.get("vwrap", false);
	}
	
	protected double getLowerU() {
		return parameters.get("loweru", 0.0);
	}
	
	protected double getUpperU() {
		return parameters.get("upperu", 1.0);
	}
	
	protected double getLowerV() {
		return parameters.get("lowerv", 0.0);
	}
	
	protected double getUpperV() {
		return parameters.get("upperv", 1.0);
	}
	
	protected WB_Surface getSurface() {
		return (WB_Surface)parameters.get("surf", null);
	}
	
	
	
	public HEC_FromSurface setU(final int U) {
		parameters.set("u", U);
		return this;
	}
	
	public HEC_FromSurface setV(final int V) {
		parameters.set("v", V);
		return this;
	}

	public HEC_FromSurface setRange(final double lowerU, double upperU, double lowerV, double upperV) {
		WB_Surface surf=getSurface();
		parameters.set("loweru", Math.max(lowerU, (surf==null)?Double.NEGATIVE_INFINITY:surf.getLowerU()));
		parameters.set("upperu", Math.min(upperU, (surf==null)?Double.POSITIVE_INFINITY:surf.getUpperU()));
		parameters.set("lowerv", Math.max(lowerV, (surf==null)?Double.NEGATIVE_INFINITY:surf.getLowerV()));
		parameters.set("upperv", Math.min(upperV, (surf==null)?Double.POSITIVE_INFINITY:surf.getUpperV()));
		return this;
	}

	

	/**
	 * 
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromSurface setUWrap(final boolean b) {
		parameters.set("uwrap",b);
		return this;
	}

	/**
	 * 
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromSurface setVWrap(final boolean b) {
		parameters.set("vwrap",b);
		return this;
	}

	/**
	 * 
	 *
	 * @param surf
	 * @return
	 */
	public HEC_FromSurface setSurface(final WB_Surface surf) {
		parameters.set("surf", surf);
		parameters.set("loweru", surf.getLowerU());
		parameters.set("upperu", surf.getUpperU());
		parameters.set("lowerv", surf.getLowerV());
		parameters.set("upperv", surf.getUpperV());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.nurbs.WB_Surface#toHemesh(PApplet, double, double)
	 */
	@Override
	public HE_Mesh createBase() {
		int U=getU();
		int V=getV();
		double lowerU=getLowerU();
		double upperU=getUpperU();
		double lowerV=getLowerV();
		double upperV=getUpperV();
		WB_Surface surf=getSurface();
		boolean uWrap=getUWrap();
		boolean vWrap=getVWrap();
		if(surf==null) return new HE_Mesh();
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
		fl.setFaces(faces).setVertices(points).setCheckDuplicateVertices(true).setCheckNormals(false);
		return new HE_Mesh(fl);
	}
}
