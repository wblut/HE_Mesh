package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Line;
import wblut.geom.WB_Vector;

public class HEM_Twist extends HEM_Modifier {
	private WB_Line twistAxis;
	private double angleFactor;

	public HEM_Twist() {
		super();
	}

	public HEM_Twist setTwistAxis(final WB_Line a) {
		twistAxis = a;
		return this;
	}

	public HEM_Twist setTwistAxis(final WB_Coord o, final WB_Coord d) {
		twistAxis = new WB_Line(o, d);
		return this;
	}

	public HEM_Twist setTwistAxisFromPoints(final WB_Coord a, final WB_Coord b) {
		final WB_Vector axis = new WB_Vector(a, b);
		axis.normalizeSelf();
		twistAxis = new WB_Line(a, axis);
		return this;
	}

	public HEM_Twist setAngleFactor(final double f) {
		angleFactor = f * (Math.PI / 180);
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (twistAxis != null && angleFactor != 0) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp3D.getDistance3D(v, twistAxis);
				v.getPosition().rotateAboutAxisSelf(d * angleFactor, twistAxis.getOrigin(), twistAxis.getDirection());
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (twistAxis != null && angleFactor != 0) {
			selection.collectVertices();
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp3D.getDistance3D(v, twistAxis);
				v.getPosition().rotateAboutAxisSelf(d * angleFactor, twistAxis.getOrigin(), twistAxis.getDirection());
			}
		}
		return selection.getParent();
	}
}
