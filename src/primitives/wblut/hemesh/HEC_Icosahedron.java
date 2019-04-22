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
public class HEC_Icosahedron extends HEC_Creator {

	/**
	 *
	 */
	private double R;

	/**
	 *
	 */
	public HEC_Icosahedron() {
		super();
		R = 100;
	}

	/**
	 *
	 *
	 * @param E
	 * @return
	 */
	public HEC_Icosahedron setEdge(final double E) {
		R = 0.9510565 * E;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Icosahedron setInnerRadius(final double R) {
		this.R = R * 1.2584086;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Icosahedron setOuterRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Icosahedron setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Icosahedron setMidRadius(final double R) {
		this.R = R * 1.175570;
		return this;
	}

	/*
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		HEC_Creator ico = new HEC_Plato().setType(3).setEdge(R / 0.9510565);
		return ico.createBase();
	}
}
