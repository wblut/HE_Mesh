package wblut.hemesh;

public class HEC_Cube extends HEC_Creator {
	public HEC_Cube() {
		super();
		parameters.set("edge", 100.0);
		parameters.set("widthSegments", 1);
		parameters.set("heightSegments", 1);
		parameters.set("depthSegments", 1);
	}

	public HEC_Cube(final double E, final int L, final int M, final int N) {
		this();
		parameters.set("edge", E);
		parameters.set("widthSegments", L);
		parameters.set("heightSegments", M);
		parameters.set("depthSegments", N);
	}

	public HEC_Cube setEdge(final double E) {
		parameters.set("edge", E);
		return this;
	}

	public HEC_Cube setWidthSegments(final int L) {
		parameters.set("widthSegments", L);
		return this;
	}

	public HEC_Cube setHeightSegments(final int M) {
		parameters.set("heightSegments", M);
		return this;
	}

	public HEC_Cube setDepthSegments(final int N) {
		parameters.set("depthSegments", N);
		return this;
	}

	public HEC_Cube setRadius(final double R) {
		parameters.set("edge", 2 * R);
		return this;
	}

	public HEC_Cube setInnerRadius(final double R) {
		parameters.set("edge", 2 * R);
		return this;
	}

	public HEC_Cube setOuterRadius(final double R) {
		parameters.set("edge", 1.1547005 * R);
		return this;
	}

	public HEC_Cube setMidRadius(final double R) {
		parameters.set("edge", 1.4142136 * R);
		return this;
	}

	double getEdge() {
		return parameters.get("edge", 0.0);
	}

	int getWidthSegments() {
		return parameters.get("widthSegments", 1);
	}

	int getHeightSegments() {
		return parameters.get("heightSegments", 1);
	}

	int getDepthSegments() {
		return parameters.get("depthSegments", 1);
	}

	@Override
	protected HE_Mesh createBase() {
		final double E = getEdge();
		final HEC_Box box = new HEC_Box(E, E, E, getWidthSegments(), getHeightSegments(), getDepthSegments());
		return box.createBase();
	}
}
