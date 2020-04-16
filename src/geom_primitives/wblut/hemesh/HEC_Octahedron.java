package wblut.hemesh;

public class HEC_Octahedron extends HEC_Creator {
	private double R;

	public HEC_Octahedron() {
		super();
		R = 100;
	}

	public HEC_Octahedron setEdge(final double E) {
		R = 0.70711 * E;
		return this;
	}

	public HEC_Octahedron setRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_Octahedron setInnerRadius(final double R) {
		this.R = R * 1.732051;
		return this;
	}

	public HEC_Octahedron setOuterRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_Octahedron setMidRadius(final double R) {
		this.R = R * 1.41422;
		return this;
	}

	@Override
	public HE_Mesh createBase() {
		final HEC_Creator octa = new HEC_Plato().setType(4).setEdge(R / 0.70711);
		return octa.createBase();
	}
}
