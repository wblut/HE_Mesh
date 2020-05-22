package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_Ease;
import wblut.math.WB_EaseScalarParameter;
import wblut.math.WB_Epsilon;
import wblut.math.WB_LinearScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_Cylinder extends HEC_Creator {
	/**
	 *
	 */
	public HEC_Cylinder() {
		super();
		parameters.set("facets", 6);
		parameters.set("height", 100.0);
		parameters.set("bottomradius", 100.0);
		parameters.set("topradius", 100.0);
		parameters.set("steps", 1);
		parameters.set("topcap", true);
		parameters.set("bottomcap", true);
		parameters.set("reverse", false);
		parameters.set("profile", new WB_ConstantScalarParameter(1.0));
		parameters.set("taper", new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0));
		parameters.set("heightTaper", new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0));
		parameters.set("phase", 0.0);
		setCreationAxis(WB_Vector.Y());
	}

	/**
	 *
	 *
	 * @param Ri
	 * @param Ro
	 * @param H
	 * @param facets
	 * @param steps
	 */
	public HEC_Cylinder(final double Ri, final double Ro, final double H, final int facets, final int steps) {
		this();
		parameters.set("facets", facets);
		parameters.set("height", H);
		parameters.set("bottomradius", Ri);
		parameters.set("topradius", Ro);
		parameters.set("steps", steps);
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Cylinder setRadius(final double R) {
		parameters.set("bottomradius", R);
		parameters.set("topradius", R);
		return this;
	}

	/**
	 *
	 *
	 * @param Rb
	 * @param Rt
	 * @return
	 */
	public HEC_Cylinder setRadius(final double Rb, final double Rt) {
		parameters.set("bottomradius", Rb);
		parameters.set("topradius", Rt);
		return this;
	}

	/**
	 *
	 *
	 * @param Rb
	 * @return
	 */
	public HEC_Cylinder setBottomRadius(final double Rb) {
		parameters.set("bottomradius", Rb);
		return this;
	}

	/**
	 *
	 *
	 * @param Rt
	 * @return
	 */
	public HEC_Cylinder setTopRadius(final double Rt) {
		parameters.set("topradius", Rt);
		return this;
	}

	/**
	 *
	 *
	 * @param H
	 * @return
	 */
	public HEC_Cylinder setHeight(final double H) {
		parameters.set("height", H);
		return this;
	}

	/**
	 *
	 *
	 * @param steps
	 * @return
	 */
	public HEC_Cylinder setSteps(final int steps) {
		parameters.set("steps", steps);
		return this;
	}

	/**
	 *
	 *
	 * @param facets
	 * @return
	 */
	public HEC_Cylinder setFacets(final int facets) {
		parameters.set("facets", facets);
		return this;
	}

	/**
	 *
	 *
	 * @param topcap
	 * @param bottomcap
	 * @return
	 */
	public HEC_Cylinder setCap(final boolean topcap, final boolean bottomcap) {
		parameters.set("bottomcap", bottomcap);
		parameters.set("topcap", topcap);
		return this;
	}

	/**
	 *
	 *
	 * @param direction
	 * @return
	 */
	public HEC_Cylinder align(final WB_Coord direction) {
		setAxis(direction);
		return this;
	}

	/**
	 *
	 *
	 * @param origin
	 * @param endpoint
	 * @return
	 */
	public HEC_Cylinder align(final WB_Coord origin, final WB_Coord endpoint) {
		setHeight(WB_GeometryOp.getDistance3D(origin, endpoint));
		setCenter(origin);
		setAxis(new WB_Vector(origin, endpoint));
		return this;
	}

	/**
	 *
	 *
	 * @param segment
	 * @return
	 */
	public HEC_Cylinder align(final WB_Segment segment) {
		setHeight(segment.getLength());
		setCenter(segment.getOrigin());
		setAxis(segment.getDirection());
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	double getTopRadius() {
		return parameters.get("topradius", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	double getBottomRadius() {
		return parameters.get("bottomradius", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	double getHeight() {
		return parameters.get("height", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	int getSteps() {
		return parameters.get("steps", 1);
	}

	/**
	 *
	 *
	 * @return
	 */
	int getFacets() {
		return parameters.get("facets", 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	boolean getTopCap() {
		return parameters.get("topcap", true);
	}

	/**
	 *
	 *
	 * @return
	 */
	boolean getBottomCap() {
		return parameters.get("bottomcap", true);
	}

	/**
	 *
	 *
	 * @return
	 */
	boolean getReverse() {
		return parameters.get("reverse", true);
	}

	/**
	 *
	 *
	 * @return
	 */
	double getPhase() {
		return parameters.get("phase", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	WB_ScalarParameter getTaper() {
		return (WB_ScalarParameter) parameters.get("taper", new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0));
	}

	/**
	 *
	 *
	 * @return
	 */
	WB_ScalarParameter getHeightTaper() {
		return (WB_ScalarParameter) parameters.get("heighttaper", new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0));
	}

	/**
	 *
	 *
	 * @return
	 */
	WB_ScalarParameter getProfile() {
		return (WB_ScalarParameter) parameters.get("profile", new WB_ConstantScalarParameter(1.0));
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		final boolean topcap = getTopCap();
		final boolean bottomcap = getBottomCap();
		final int steps = getSteps();
		final int facets = getFacets();
		final double Rt = getTopRadius();
		final double Rb = getBottomRadius();
		final double H = getHeight();
		final double phase = getPhase();
		final WB_ScalarParameter profile = getProfile();
		final WB_ScalarParameter taper = getTaper();
		final WB_ScalarParameter heightTaper = getHeightTaper();
		if (WB_Epsilon.isZero(Rt)) {
			final HEC_Cone cone = new HEC_Cone(Rb, H, facets, steps);
			cone.setCap(bottomcap).setProfile(profile).setTaper(taper).setHeightTaper(heightTaper);
			return cone.createBase();
		}
		if (WB_Epsilon.isZero(Rb)) {
			final HEC_Cone cone = new HEC_Cone(Rt, H, facets, steps);
			cone.setCap(topcap).setProfile(profile).setTaper(taper).setHeightTaper(heightTaper);
			cone.setReverse(true);
			return cone.createBase();
		}
		final int mantleVertices = (facets + 1) * (steps + 1);
		final int topcapOffset = mantleVertices;
		final int topcapVertices = topcap ? 2 * facets + 1 : 0;
		final int bottomcapOffset = mantleVertices + topcapVertices;
		final int bottomVertices = bottomcap ? 2 * facets + 1 : 0;
		final double[][] vertices = new double[mantleVertices + topcapVertices + bottomVertices][3];
		final double[][] uvw = new double[vertices.length][3];
		final double invs = 1.0 / steps;
		int id = 0;
		double R = 0.0;
		double Hj = 0.0;
		for (int i = 0; i <= steps; i++) {
			R = profile.evaluate(i * invs) * (Rb + taper.evaluate(i * invs) * (Rt - Rb));
			Hj = heightTaper.evaluate(i * invs) * H;
			for (int j = 0; j <= facets; j++) {
				vertices[id][0] = R * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = R * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvw[id][0] = j * 1.0 / facets;
				uvw[id][1] = i * 1.0 / steps;
				uvw[id][2] = 0.0;
				id++;
			}
		}
		if (topcap) {
			R = profile.evaluate(1.0) * (Rb + taper.evaluate(1.0) * (Rt - Rb));
			Hj = heightTaper.evaluate(1.0) * H;
			for (int j = 0; j <= facets; j++) {
				vertices[id][0] = R * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = R * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvw[id][0] = 0.5;
				uvw[id][1] = 1.0;
				uvw[id][2] = 0;
				id++;
			}
			for (int j = 0; j < facets; j++) {
				vertices[id][0] = 0.0;
				vertices[id][2] = 0.0;
				vertices[id][1] = Hj;
				uvw[id][0] = 0.5;
				uvw[id][1] = 1.0;
				uvw[id][2] = 0;
				id++;
			}
		}
		if (bottomcap) {
			R = profile.evaluate(0.0) * (Rb + taper.evaluate(0.0) * (Rt - Rb));
			Hj = heightTaper.evaluate(0.0) * H;
			for (int j = 0; j <= facets; j++) {
				vertices[id][0] = R * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = R * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvw[id][0] = 0.5;
				uvw[id][1] = 0.0;
				uvw[id][2] = 0;
				id++;
			}
			for (int j = 0; j < facets; j++) {
				vertices[id][0] = 0.0;
				vertices[id][2] = 0.0;
				vertices[id][1] = Hj;
				uvw[id][0] = 0.5;
				uvw[id][1] = 0.0;
				uvw[id][2] = 0;
				id++;
			}
		}
		final int mantleFaces = facets * steps;
		final int topcapFaces = topcap ? facets : 0;
		final int bottomcapFaces = bottomcap ? facets : 0;
		final int[][] faces = new int[mantleFaces + topcapFaces + bottomcapFaces][];
		final int[] faceTextureIds = new int[faces.length];
		id = 0;
		for (int j = 0; j < facets; j++) {
			for (int i = 0; i < steps; i++) {
				faces[id] = new int[4];
				faces[id][0] = j + i * (facets + 1);
				faces[id][1] = j + i * (facets + 1) + facets + 1;
				faces[id][2] = j + 1 + facets + 1 + i * (facets + 1);
				faces[id][3] = j + 1 + i * (facets + 1);
				faceTextureIds[id] = 0;
				id++;
			}
		}
		if (topcap) {
			for (int j = 0; j < facets; j++) {
				faces[id] = new int[3];
				faceTextureIds[id] = 1;
				faces[id][0] = j + topcapOffset;
				faces[id][2] = j + 1 + topcapOffset;
				faces[id][1] = j + 1 + facets + topcapOffset;
				id++;
			}
		}
		if (bottomcap) {
			for (int j = 0; j < facets; j++) {
				faces[id] = new int[3];
				faceTextureIds[id] = 2;
				faces[id][0] = j + bottomcapOffset;
				faces[id][1] = j + 1 + bottomcapOffset;
				faces[id][2] = j + 1 + facets + bottomcapOffset;
				id++;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setUVW(uvw).setFaceTextureIds(faceTextureIds);
		return fl.createBase();
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Cylinder setProfile(final WB_ScalarParameter t) {
		parameters.set("profile", t);
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Cylinder setTaper(final WB_ScalarParameter t) {
		parameters.set("taper", t);
		return this;
	}

	/**
	 *
	 *
	 * @param E
	 * @param type
	 * @return
	 */
	public HEC_Cylinder setTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		parameters.set("taper", new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type));
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEC_Cylinder setPhase(final double p) {
		parameters.set("phase", p);
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Cylinder setHeightTaper(final WB_ScalarParameter t) {
		parameters.set("heighttaper", t);
		return this;
	}

	/**
	 *
	 *
	 * @param E
	 * @param type
	 * @return
	 */
	public HEC_Cylinder setHeigthTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		parameters.set("heighttaper", new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type));
		return this;
	}

	/**
	 *
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final HEC_Cylinder creator1 = new HEC_Cylinder();
		creator1.setRadius(150, 150);
		creator1.setHeight(400);
		creator1.setFacets(14).setSteps(3);
		creator1.setCap(true, true);
		final HE_Mesh mesh = new HE_Mesh(creator1);
		mesh.validate();
	}
}
