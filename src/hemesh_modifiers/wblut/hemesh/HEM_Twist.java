/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Line;
import wblut.geom.WB_Vector;

/**
 * Twist a mesh. Determined by a twist axis and an angle factor.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_Twist extends HEM_Modifier {
	/** Twist axis. */
	private WB_Line twistAxis;
	/** Angle factor. */
	private double angleFactor;

	/**
	 * Instantiates a new HEM_Twist.
	 */
	public HEM_Twist() {
		super();
	}

	/**
	 * Set twist axis.
	 *
	 * @param a
	 *            twist axis
	 * @return self
	 */
	public HEM_Twist setTwistAxis(final WB_Line a) {
		twistAxis = a;
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @param d
	 * @return
	 */
	public HEM_Twist setTwistAxis(final WB_Coord o, final WB_Coord d) {
		twistAxis = new WB_Line(o, d);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public HEM_Twist setTwistAxisFromPoints(final WB_Coord a, final WB_Coord b) {
		final WB_Vector axis = new WB_Vector(a, b);
		axis.normalizeSelf();
		twistAxis = new WB_Line(a, axis);
		return this;
	}

	/**
	 * Set angle factor, ratio of twist angle in degrees to distance to twist
	 * axis.
	 *
	 * @param f
	 *            direction
	 * @return self
	 */
	public HEM_Twist setAngleFactor(final double f) {
		angleFactor = f * (Math.PI / 180);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (twistAxis != null && angleFactor != 0) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, twistAxis);
				v.getPosition().rotateAboutAxisSelf(d * angleFactor, twistAxis.getOrigin(), twistAxis.getDirection());
			}
		}

		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (twistAxis != null && angleFactor != 0) {
			selection.collectVertices();
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, twistAxis);
				v.getPosition().rotateAboutAxisSelf(d * angleFactor, twistAxis.getOrigin(), twistAxis.getDirection());
			}
		}

		return selection.getParent();
	}
}
