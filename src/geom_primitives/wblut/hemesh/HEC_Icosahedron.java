package wblut.hemesh;

public class HEC_Icosahedron extends HEC_Creator {
	private double R;

	public HEC_Icosahedron() {
		super();
		R = 100;
	}

	public HEC_Icosahedron setEdge(final double E) {
		R = 0.9510565 * E;
		return this;
	}

	public HEC_Icosahedron setInnerRadius(final double R) {
		this.R = R * 1.2584086;
		return this;
	}

	public HEC_Icosahedron setOuterRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_Icosahedron setRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_Icosahedron setMidRadius(final double R) {
		this.R = R * 1.175570;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		final HEC_Creator ico = new HEC_Plato().setType(3).setEdge(R / 0.9510565);
		return ico.createBase();
	}
}