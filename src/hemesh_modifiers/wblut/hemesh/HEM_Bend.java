package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Line;
import wblut.geom.WB_Plane;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_Bend extends HEM_Modifier {
	/**  */
	private WB_Plane groundPlane;
	/**  */
	private WB_Line bendAxis;
	/**  */
	private WB_ScalarParameter angleFactor;
	/**  */
	private boolean posOnly;

	/**
	 *
	 */
	public HEM_Bend() {
		super();
		angleFactor = WB_ScalarParameter.ZERO;
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public HEM_Bend setGroundPlane(final WB_Plane P) {
		groundPlane = P;
		return this;
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return
	 */
	public HEM_Bend setGroundPlane(final double ox, final double oy, final double oz, final double nx, final double ny,
			final double nz) {
		groundPlane = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_Bend setBendAxis(final WB_Line a) {
		bendAxis = a;
		return this;
	}

	/**
	 *
	 *
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @param p2x
	 * @param p2y
	 * @param p2z
	 * @return
	 */
	public HEM_Bend setBendAxis(final double p1x, final double p1y, final double p1z, final double p2x,
			final double p2y, final double p2z) {
		bendAxis = new WB_Line(p1x, p1y, p1z, p2x - p1x, p2y - p1y, p2z - p2y);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEM_Bend setAngleFactor(final double f) {
		angleFactor = new WB_ConstantScalarParameter(f * (Math.PI / 180));
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEM_Bend setAngleFactor(final WB_ScalarParameter f) {
		angleFactor = f;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Bend setPosOnly(final boolean b) {
		posOnly = b;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (groundPlane != null && bendAxis != null && angleFactor != WB_ScalarParameter.ZERO) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > 0) {
					v.getPosition().rotateAboutAxisSelf(d * angleFactor.evaluate(v.xd(), v.yd(), v.zd()),
							bendAxis.getOrigin(), bendAxis.getDirection());
				}
			}
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (groundPlane != null && bendAxis != null && angleFactor != WB_ScalarParameter.ZERO) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > 0) {
					v.getPosition().rotateAboutAxisSelf(d * angleFactor.evaluate(v.xd(), v.yd(), v.zd()),
							bendAxis.getOrigin(), bendAxis.getDirection());
				}
			}
		}
		return selection.getParent();
	}
}
