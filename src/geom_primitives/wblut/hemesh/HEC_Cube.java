package wblut.hemesh;

/**
 *
 */
public class HEC_Cube extends HEC_Creator {
	/**
	 *
	 */
	public HEC_Cube() {
		super();
		parameters.set("edge", 100.0);
		parameters.set("widthSegments", 1);
		parameters.set("heightSegments", 1);
		parameters.set("depthSegments", 1);
	}

	/**
	 *
	 *
	 * @param E
	 * @param L
	 * @param M
	 * @param N
	 */
	public HEC_Cube(final double E, final int L, final int M, final int N) {
		this();
		parameters.set("edge", E);
		parameters.set("widthSegments", L);
		parameters.set("heightSegments", M);
		parameters.set("depthSegments", N);
	}

	/**
	 *
	 *
	 * @param E
	 * @return
	 */
	public HEC_Cube setEdge(final double E) {
		parameters.set("edge", E);
		return this;
	}

	/**
	 *
	 *
	 * @param L
	 * @return
	 */
	public HEC_Cube setWidthSegments(final int L) {
		parameters.set("widthSegments", L);
		return this;
	}

	/**
	 *
	 *
	 * @param M
	 * @return
	 */
	public HEC_Cube setHeightSegments(final int M) {
		parameters.set("heightSegments", M);
		return this;
	}

	/**
	 *
	 *
	 * @param N
	 * @return
	 */
	public HEC_Cube setDepthSegments(final int N) {
		parameters.set("depthSegments", N);
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Cube setRadius(final double R) {
		parameters.set("edge", 2 * R);
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Cube setInnerRadius(final double R) {
		parameters.set("edge", 2 * R);
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Cube setOuterRadius(final double R) {
		parameters.set("edge", 1.1547005 * R);
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Cube setMidRadius(final double R) {
		parameters.set("edge", 1.4142136 * R);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	double getEdge() {
		return parameters.get("edge", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	int getWidthSegments() {
		return parameters.get("widthSegments", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	int getHeightSegments() {
		return parameters.get("heightSegments", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	int getDepthSegments() {
		return parameters.get("depthSegments", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		final double E = getEdge();
		final HEC_Box box = new HEC_Box(E, E, E, getWidthSegments(), getHeightSegments(), getDepthSegments());
		return box.createBase();
	}
}
