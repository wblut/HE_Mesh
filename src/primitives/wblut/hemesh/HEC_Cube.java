/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

/**
 * Axis Aligned Cube.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Cube extends HEC_Creator {
	private double W;
	private int L;
	private int M;
	private int N;

	/**
	 * Instantiates a new cube.
	 *
	 */
	public HEC_Cube() {
		super();
		W = 100;
		L = M = N = 1;
	}

	/**
	 * Create a cube.
	 *
	 * @param W
	 *            width
	 * @param L
	 *            number of width divisions
	 * @param M
	 *            number of height divisions
	 * @param N
	 *            number of depth divisions
	 */
	public HEC_Cube(final double W, final int L, final int M, final int N) {
		this();
		this.W = W;
		this.L = Math.max(1, L);
		this.M = Math.max(1, M);
		this.N = Math.max(1, N);
	}

	/**
	 * Set edge length.
	 *
	 * @param E
	 *            edge length
	 * @return self
	 */
	public HEC_Cube setEdge(final double E) {
		W = E;
		return this;
	}

	/**
	 * Set box width segments.
	 *
	 * @param L
	 *            number of width segments (x-axis)
	 * @return self
	 */
	public HEC_Cube setWidthSegments(final int L) {
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
	public HEC_Cube setHeightSegments(final int M) {
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
	public HEC_Cube setDepthSegments(final int N) {
		this.N = Math.max(1, N);
		return this;
	}

	/**
	 * Set radius of inscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Cube setRadius(final double R) {
		W = 2 * R;
		return this;
	}

	/**
	 * Set radius of inscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Cube setInnerRadius(final double R) {
		W = 2 * R;
		return this;
	}

	/**
	 * Set radius of circumscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Cube setOuterRadius(final double R) {
		W = 1.1547005 * R;
		return this;
	}

	/**
	 * Set radius of tangential sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Cube setMidRadius(final double R) {
		W = 1.4142136 * R;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final HEC_Box box = new HEC_Box(W, W, W, N, M, L);
		return box.createBase();
	}
}
