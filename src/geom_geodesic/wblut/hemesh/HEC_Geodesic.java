package wblut.hemesh;

import wblut.geom.WB_Geodesic;
import wblut.geom.WB_Geodesic.Type;
import wblut.geom.WB_Sphere;

public class HEC_Geodesic extends HEC_Creator {
	public HEC_Geodesic() {
		super();
		parameters.set("rx", 100.0);
		parameters.set("ry", 100.0);
		parameters.set("rz", 100.0);
		parameters.set("type", Type.ICOSAHEDRON);
		parameters.set("b", 8);
		parameters.set("c", 0);
	}

	public HEC_Geodesic(final double R) {
		this();
		parameters.set("rx", R);
		parameters.set("ry", R);
		parameters.set("rz", R);
		parameters.set("type", Type.ICOSAHEDRON);
		parameters.set("b", 8);
		parameters.set("c", 0);
	}

	protected double getRx() {
		return parameters.get("rx", 100.0);
	}

	protected double getRy() {
		return parameters.get("ry", 100.0);
	}

	protected double getRz() {
		return parameters.get("rz", 100.0);
	}

	protected Type getType() {
		return (Type) parameters.get("type", Type.ICOSAHEDRON);
	}

	protected int getB() {
		return parameters.get("b", 8);
	}

	protected int getC() {
		return parameters.get("c", 0);
	}

	public HEC_Geodesic setRadius(final double R) {
		parameters.set("rx", R);
		parameters.set("ry", R);
		parameters.set("rz", R);
		return this;
	}

	public HEC_Geodesic setSphere(final WB_Sphere S) {
		final double R = S.getRadius();
		parameters.set("center", S.getCenter());
		parameters.set("rx", R);
		parameters.set("ry", R);
		parameters.set("rz", R);
		return this;
	}

	public HEC_Geodesic setRadius(final double rx, final double ry, final double rz) {
		parameters.set("rx", rx);
		parameters.set("ry", ry);
		parameters.set("rz", rz);
		return this;
	}

	public HEC_Geodesic setB(final int b) {
		parameters.set("b", b);
		return this;
	}

	public HEC_Geodesic setC(final int c) {
		parameters.set("c", c);
		return this;
	}

	public HEC_Geodesic setDivisions(final int div) {
		parameters.set("b", div);
		parameters.set("c", 0);
		return this;
	}

	public HEC_Geodesic setLevel(final int level) {
		parameters.set("b", (int) Math.pow(2, level));
		parameters.set("c", 0);
		return this;
	}

	public HEC_Geodesic setType(final Type t) {
		parameters.set("type", t);
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		final WB_Geodesic geo = new WB_Geodesic(1.0, getB(), getC(), getType());
		final HE_Mesh mesh = new HE_Mesh(new HEC_FromSimpleMesh(geo).setCheckUniformNormals(false)
				.setCheckManifold(false).setCheckNormals(false));
		mesh.scaleSelf(getRx(), getRy(), getRz());
		return mesh;
	}
}
