package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_Sphere;

public class HEM_Spherify extends HEM_Modifier {
	private final WB_Sphere sphere;
	private double factor;
	private final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();

	public HEM_Spherify() {
		super();
		sphere = new WB_Sphere();
		factor = 1.0;
	}

	public HEM_Spherify setRadius(final double r) {
		sphere.setRadius(r);
		return this;
	}

	public HEM_Spherify setCenter(final double x, final double y, final double z) {
		sphere.setCenter(new WB_Point(x, y, z));
		return this;
	}

	public HEM_Spherify setCenter(final WB_Point c) {
		sphere.setCenter(c);
		return this;
	}

	public HEM_Spherify setFactor(final double f) {
		factor = f;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(gf.createInterpolatedPoint(v, sphere.projectToSphere(v), factor));
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final Iterator<HE_Vertex> vItr = selection.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(gf.createInterpolatedPoint(v, sphere.projectToSphere(v), factor));
		}
		return selection.getParent();
	}
}
