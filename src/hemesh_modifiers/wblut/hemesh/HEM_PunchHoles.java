/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_FactorScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_PunchHoles extends HEM_Modifier {

	/**
	 *
	 */
	private WB_ScalarParameter sew;

	/**
	 *
	 */
	private WB_ScalarParameter hew;

	/**
	 *
	 */
	private double thresholdAngle;

	/**
	 *
	 */
	private boolean fuse;

	/**
	 *
	 */
	private double fuseAngle;

	private boolean relative;

	/**
	 *
	 */
	public HEM_PunchHoles() {
		super();
		sew = WB_ScalarParameter.ZERO;
		thresholdAngle = -1;
		fuseAngle = Math.PI / 36;
		fuse = false;
		relative = false;
	}

	/**
	 *
	 *
	 * @param w
	 * @return
	 */
	public HEM_PunchHoles setWidth(final double w) {
		sew = w == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(0.5 * w);
		hew = w == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(w);
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @param hew
	 * @return
	 */
	public HEM_PunchHoles setWidth(final double w, final double hew) {
		sew = w == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(0.5 * w);
		this.hew = hew == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(hew);
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @return
	 */
	public HEM_PunchHoles setWidth(final WB_ScalarParameter w) {
		sew = new WB_FactorScalarParameter(0.5, w);
		hew = w;
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @param hew
	 * @return
	 */
	public HEM_PunchHoles setWidth(final WB_ScalarParameter w, final WB_ScalarParameter hew) {
		sew = new WB_FactorScalarParameter(0.5, w);
		this.hew = hew;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_PunchHoles setFuse(final boolean b) {
		fuse = b;
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_PunchHoles setThresholdAngle(final double a) {
		thresholdAngle = a;
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_PunchHoles setFuseAngle(final double a) {
		fuseAngle = a;
		return this;
	}

	/**
	 *
	 * @param relative
	 * @return
	 */
	public HEM_PunchHoles setRelative(final boolean relative) {
		this.relative = relative;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (sew == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		final HEM_Extrude extm = new HEM_Extrude().setDistance(0).setRelative(relative).setChamfer(sew).setFuse(fuse)
				.setHardEdgeChamfer(hew).setFuseAngle(fuseAngle).setThresholdAngle(thresholdAngle);
		mesh.modify(extm);
		mesh.deleteFaces(mesh.getSelection("extruded"));
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (sew == WB_ScalarParameter.ZERO) {
			return selection.getParent();
		}
		final HEM_Extrude extm = new HEM_Extrude().setDistance(0).setRelative(relative).setChamfer(sew).setFuse(fuse)
				.setHardEdgeChamfer(hew).setFuseAngle(fuseAngle).setThresholdAngle(thresholdAngle);
		selection.modify(extm);
		selection.getParent().deleteFaces(selection.getParent().getSelection("extruded"));
		return selection.getParent();
	}
}
