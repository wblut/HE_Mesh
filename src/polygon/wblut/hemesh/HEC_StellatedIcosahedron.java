/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_GeometryFactory;

public class HEC_StellatedIcosahedron extends HEC_Creator {
	private double	R;
	/** Type. */
	private int		type;

	public HEC_StellatedIcosahedron() {
		super();
		R = 100;
		type = 1;
	}

	public HEC_StellatedIcosahedron(final int type, final double R) {
		super();
		this.R = R;
		this.type = type;
		if (type < 1 || type > 59) {
			throw new IllegalArgumentException(
					"Type of stellated icosahedron should be between 1 and 59.");
		}
	}

	public HEC_StellatedIcosahedron setRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_StellatedIcosahedron setType(final int type) {
		if (type < 1 || type > 59) {
			throw new IllegalArgumentException(
					"Type of stellated icosahedron should be between 1 and 59.");
		}
		this.type = type;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		HE_Mesh hmesh = new HE_Mesh(new WB_GeometryFactory()
				.createStellatedIcosahedron(type - 1, R));
		return hmesh;
	}
}
