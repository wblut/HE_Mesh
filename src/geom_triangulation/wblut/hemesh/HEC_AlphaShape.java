package wblut.hemesh;

import wblut.geom.WB_AlphaTriangulation3D;

/**
 *
 */
public class HEC_AlphaShape extends HEC_Creator {
	/**  */
	private WB_AlphaTriangulation3D alphaTri;
	/**  */
	private double alpha;

	/**
	 *
	 */
	public HEC_AlphaShape() {
		alphaTri = null;
		alpha = 10;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param alphaTri
	 * @return
	 */
	public HEC_AlphaShape setTriangulation(final WB_AlphaTriangulation3D alphaTri) {
		this.alphaTri = alphaTri;
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEC_AlphaShape setAlpha(final double a) {
		this.alpha = a;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		if (alpha <= 0.0 || alphaTri == null) {
			return new HE_Mesh();
		}
		final int[] tris = alphaTri.getAlphaTriangles(alpha);
		final HEC_FromFacelist ffl = new HEC_FromFacelist().setFaces(tris).setVertices(alphaTri.getPoints())
				.setCheckDuplicateVertices(false);
		ffl.setCheckManifold(true);
		return new HE_Mesh(ffl);
	}
}
