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
 *
 */
public class HEC_Octahedron extends HEC_Creator {

	/**
	 *
	 */
	private double R;

	/**
	 *
	 */
	public HEC_Octahedron() {
		super();
		R = 100;
	}

	/**
	 *
	 *
	 * @param E
	 * @return
	 */
	public HEC_Octahedron setEdge(final double E) {
		R = 0.70711 * E;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Octahedron setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Octahedron setInnerRadius(final double R) {
		this.R = R * 1.732051;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Octahedron setOuterRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Octahedron setMidRadius(final double R) {
		this.R = R * 1.41422;
		return this;
	}

	/*
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		HEC_Creator octa = new HEC_Plato().setType(4).setEdge(R / 0.70711);
		return octa.createBase();
	}
}
