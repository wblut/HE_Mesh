package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Vector;

public class HEM_Shear extends HEM_Modifier {
	private WB_Plane groundPlane;
	private WB_Vector shearDirection;
	private double shearFactor;
	private boolean posOnly;

	public HEM_Shear() {
		super();
	}

	public HEM_Shear setGroundPlane(final WB_Plane P) {
		groundPlane = P;
		return this;
	}

	public HEM_Shear setGroundPlane(final double ox, final double oy, final double oz, final double nx, final double ny,
			final double nz) {
		groundPlane = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	public HEM_Shear setShearDirection(final WB_Coord p) {
		shearDirection = new WB_Vector(p);
		shearDirection.normalizeSelf();
		return this;
	}

	public HEM_Shear setShearDirection(final double vx, final double vy, final double vz) {
		shearDirection = new WB_Vector(vx, vy, vz);
		shearDirection.normalizeSelf();
		return this;
	}

	public HEM_Shear setShearFactor(final double f) {
		shearFactor = f;
		return this;
	}

	public HEM_Shear setPosOnly(final boolean b) {
		posOnly = b;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (groundPlane != null && shearDirection != null && shearFactor != 0) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp3D.getDistance3D(v, groundPlane);
				if (!posOnly || d > 0) {
					v.getPosition().addSelf(shearDirection.mul(d * shearFactor));
				}
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (groundPlane != null && shearDirection != null && shearFactor != 0) {
			selection.collectVertices();
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp3D.getDistance3D(v, groundPlane);
				if (!posOnly || d > 0) {
					v.getPosition().addSelf(shearDirection.mul(d * shearFactor));
				}
			}
		}
		return selection.getParent();
	}
}
