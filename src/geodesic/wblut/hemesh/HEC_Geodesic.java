/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_Geodesic;
import wblut.geom.WB_Geodesic.Type;
import wblut.geom.WB_Sphere;

/**
 *
 */
public class HEC_Geodesic extends HEC_Creator {
	/**
	 *
	 */
	private double	rx, ry, rz;
	/**
	 *
	 */
	private Type	type;
	/**
	 *
	 */
	private int		b;
	/**
	 *
	 */
	private int		c;

	/**
	 *
	 */
	public HEC_Geodesic() {
		super();
		rx = ry = rz = 100;
		type = Type.ICOSAHEDRON;
		b = c = 4;
	}

	/**
	 *
	 *
	 * @param R
	 */
	public HEC_Geodesic(final double R) {
		this();
		rx = ry = rz = R;
		b = c = 4;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Geodesic setRadius(final double R) {
		rx = ry = rz = R;
		return this;
	}

	public HEC_Geodesic setSphere(final WB_Sphere S) {
		rx = ry = rz = S.getRadius();
		setCenter(S.getCenter());
		return this;
	}

	/**
	 *
	 *
	 * @param rx
	 * @param ry
	 * @param rz
	 * @return
	 */
	public HEC_Geodesic setRadius(final double rx, final double ry,
			final double rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_Geodesic setB(final int b) {
		this.b = b;
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEC_Geodesic setC(final int c) {
		this.c = c;
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Geodesic setType(final Type t) {
		type = t;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final WB_Geodesic geo = new WB_Geodesic(1.0, b, c, type);
		final HE_Mesh mesh = new HE_Mesh(
				new HEC_FromWBMesh(geo).setNormalsCheck(false)
						.setUniformityCheck(false).setManifoldCheck(false));
		mesh.scaleSelf(rx, ry, rz);
		return mesh;
	}
}
