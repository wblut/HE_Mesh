/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Point;
import wblut.math.WB_VectorParameter;

public class HEC_UVParametric extends HEC_FromFacelist { // Creator {
	/** Number of subdivisions along u-axis. */
	protected int usteps;
	/** Number of subdivisions along v-axis. */
	protected int vsteps;
	/** Scale factor. */
	protected double scale;
	/** The parametric evaluator. */
	protected WB_VectorParameter evaluator;
	protected double uRange, vRange, uMin, uMax, vMin, vMax;
	protected boolean fixDuplicatedVertices;

	/**
	 * Instantiates a new HEC_UVParametric.
	 */
	public HEC_UVParametric() {
		super();
		setOverride(true);
		usteps = 32;
		vsteps = 32;
		scale = 1.0;
		uMin = vMin = 0;
		uMax = vMax = 1.0;
		uRange = 1.0;
		vRange = 1.0;
		fixDuplicatedVertices = true;
	}

	/**
	 *
	 *
	 * @param eval
	 * @return
	 */
	public HEC_UVParametric setEvaluator(final WB_VectorParameter eval) {
		evaluator = eval;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_UVParametric setFixDuplicatedVertices(final boolean b) {
		fixDuplicatedVertices = b;
		return this;
	}

	/**
	 * Sets the number of subdivisions along u/v axes.
	 *
	 * @param usteps
	 *            the number of subdivisions along u-axis
	 * @param vsteps
	 *            the number of subdivisions along v-axis
	 * @return self
	 */
	public HEC_UVParametric setUVSteps(final int usteps, final int vsteps) {
		this.usteps = usteps > 1 ? usteps : 32;
		this.vsteps = usteps > 1 ? vsteps : 32;
		return this;
	}

	/**
	 * Sets the scale factor.
	 *
	 * @param r
	 *            the r
	 * @return self
	 */
	@Override
	public HEC_UVParametric setScale(final double r) {
		scale = r;
		return this;
	}

	/**
	 *
	 *
	 * @param umin
	 * @param umax
	 * @return
	 */
	public HEC_UVParametric setURange(final double umin, final double umax) {
		this.uMin = umin;
		this.uMax = umax;
		uRange = umax - umin;
		return this;
	}

	/**
	 *
	 *
	 * @param vmin
	 * @param vmax
	 * @return
	 */
	public HEC_UVParametric setVRange(final double vmin, final double vmax) {
		this.vMin = vmin;
		this.vMax = vmax;
		vRange = vmax - vmin;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (evaluator != null) {
			final int lusteps = usteps + 1;
			final int lvsteps = vsteps + 1;
			int N = lusteps * lvsteps;
			final WB_Point[] vertices = new WB_Point[N];
			final WB_Point[] uvws = new WB_Point[N];
			int index = 0;
			for (int iv = 0; iv < lvsteps; iv++) {
				final double v = iv == usteps ? vMax : vMin + iv / (double) vsteps * vRange;
				for (int iu = 0; iu < lusteps; iu++) {
					final double u = iu == usteps ? uMax : uMin + iu / (double) usteps * uRange;
					vertices[index] = new WB_Point(evaluator.evaluate(u, v));
					vertices[index].scaleSelf(scale);
					uvws[index] = new WB_Point(iu * 1.0 / lusteps, iv * 1.0 / lusteps, 0);
					index++;
				} // for iu
			} // for iv
			N = usteps * vsteps;
			final int[][] faces = new int[N][4];
			index = 0;
			for (int iv = 0; iv < vsteps; iv++) {
				for (int iu = 0; iu < usteps; iu++) {
					faces[index][0] = iv * lusteps + iu;
					faces[index][1] = iv * lusteps + iu + 1;
					faces[index][2] = (iv + 1) * lusteps + iu + 1;
					faces[index][3] = (iv + 1) * lusteps + iu;
					index++;
				} // for iu
			} // for iv
			this.setVertices(vertices).setFaces(faces).setVertexUVW(uvws).setCheckDuplicateVertices(fixDuplicatedVertices);
			return super.createBase();
		}
		return null;
	}
}
