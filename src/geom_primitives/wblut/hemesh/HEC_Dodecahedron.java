package wblut.hemesh;

public class HEC_Dodecahedron extends HEC_Creator {
	public HEC_Dodecahedron() {
		super();
		parameters.set("radius", 100.0);
	}

	public HEC_Dodecahedron(final double R) {
		super();
		parameters.set("radius", R);
	}

	public HEC_Dodecahedron setEdge(final double E) {
		parameters.set("radius", 1.40126 * E);
		return this;
	}

	public HEC_Dodecahedron setInnerRadius(final double R) {
		parameters.set("radius", R * 1.258406);
		return this;
	}

	public HEC_Dodecahedron setOuterRadius(final double R) {
		parameters.set("radius", R);
		return this;
	}

	public HEC_Dodecahedron setRadius(final double R) {
		parameters.set("radius", R);
		return this;
	}

	public HEC_Dodecahedron setMidRadius(final double R) {
		parameters.set("radius", R * 1.070465);
		return this;
	}

	double getRadius() {
		return parameters.get("radius", 0.0);
	}

	@Override
	public HE_Mesh createBase() {
		final HEC_Creator dode = new HEC_Plato().setType(2).setEdge(getRadius() / 1.40126);
		return dode.createBase();
	}
}
