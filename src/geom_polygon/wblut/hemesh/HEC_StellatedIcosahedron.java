package wblut.hemesh;

import wblut.geom.WB_GeometryFactory3D;

/**
 *
 */
public class HEC_StellatedIcosahedron extends HEC_Creator {
	/**  */
	private double R;
	/**  */
	private int type;

	/**
	 *
	 */
	public HEC_StellatedIcosahedron() {
		super();
		R = 100;
		type = 1;
	}

	/**
	 *
	 *
	 * @param type
	 * @param R
	 */
	public HEC_StellatedIcosahedron(final int type, final double R) {
		super();
		this.R = R;
		this.type = type;
		if (type < 1 || type > 59) {
			throw new IllegalArgumentException("Type of stellated icosahedron should be between 1 and 59.");
		}
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_StellatedIcosahedron setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param type
	 * @return
	 */
	public HEC_StellatedIcosahedron setType(final int type) {
		if (type < 1 || type > 59) {
			throw new IllegalArgumentException("Type of stellated icosahedron should be between 1 and 59.");
		}
		this.type = type;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh createBase() {
		final HE_Mesh hmesh = new HEC_FromSimpleMesh(new WB_GeometryFactory3D().createStellatedIcosahedron(type, R))
				.create();
		return hmesh;
	}
}
