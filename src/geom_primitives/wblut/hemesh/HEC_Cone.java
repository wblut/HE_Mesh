package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_Ease;
import wblut.math.WB_EaseScalarParameter;
import wblut.math.WB_LinearScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_Cone extends HEC_Creator {
	/**
	 *
	 */
	public HEC_Cone() {
		super();
		parameters.set("facets", 6);
		parameters.set("height", 100.0);
		parameters.set("radius", 100.0);
		parameters.set("steps", 1);
		parameters.set("cap", true);
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
	 * @param R
	 * @param H
	 * @param facets
	 * @param steps
	 */
	public HEC_Cone(final double R, final double H, final int facets, final int steps) {
		this();
		parameters.set("facets", facets);
		parameters.set("height", H);
		parameters.set("radius", R);
		parameters.set("steps", steps);
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Cone setRadius(final double R) {
		parameters.set("radius", R);
		return this;
	}

	/**
	 *
	 *
	 * @param H
	 * @return
	 */
	public HEC_Cone setHeight(final double H) {
		parameters.set("height", H);
		return this;
	}

	/**
	 *
	 *
	 * @param facets
	 * @return
	 */
	public HEC_Cone setFacets(final int facets) {
		parameters.set("facets", facets);
		return this;
	}

	/**
	 *
	 *
	 * @param steps
	 * @return
	 */
	public HEC_Cone setSteps(final int steps) {
		parameters.set("steps", steps);
		return this;
	}

	/**
	 *
	 *
	 * @param cap
	 * @return
	 */
	public HEC_Cone setCap(final boolean cap) {
		parameters.set("cap", cap);
		return this;
	}

	/**
	 *
	 *
	 * @param rev
	 * @return
	 */
	public HEC_Cone setReverse(final boolean rev) {
		parameters.set("reverse", rev);
		return this;
	}

	/**
	 *
	 *
	 * @param direction
	 * @return
	 */
	public HEC_Cone align(final WB_Coord direction) {
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
	public HEC_Cone align(final WB_Coord origin, final WB_Coord endpoint) {
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
	public HEC_Cone align(final WB_Segment segment) {
		setHeight(segment.getLength());
		setCenter(segment.getOrigin());
		setAxis(segment.getDirection());
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Cone setProfile(final WB_ScalarParameter t) {
		parameters.set("profile", t);
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Cone setTaper(final WB_ScalarParameter t) {
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
	public HEC_Cone setTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		parameters.set("taper", new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type));
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEC_Cone setPhase(final double p) {
		parameters.set("phase", p);
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Cone setHeightTaper(final WB_ScalarParameter t) {
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
	public HEC_Cone setHeigthTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		parameters.set("heighttaper", new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type));
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	double getRadius() {
		return parameters.get("radius", 0.0);
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
	boolean getCap() {
		return parameters.get("cap", true);
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
		final boolean cap = getCap();
		final boolean reverse = getReverse();
		final int steps = getSteps();
		final int facets = getFacets();
		final double R = getRadius();
		final double H = getHeight();
		final double phase = getPhase();
		final WB_ScalarParameter profile = getProfile();
		final WB_ScalarParameter taper = getTaper();
		final WB_ScalarParameter heightTaper = getHeightTaper();
		final int mantleVertices = (facets + 1) * steps;
		final int tipOffset = mantleVertices;
		final int tipVertices = facets;
		final int capOffset = mantleVertices + tipVertices;
		final int capVertices = cap ? 2 * facets + 1 : 0;
		final double[][] vertices = new double[mantleVertices + tipVertices + capVertices][3];
		final double[][] uvws = new double[vertices.length][3];
		double Ri = 0;
		double Hj = 0;
		final double invs = 1.0 / steps;
		// mantleVertices
		int id = 0;
		for (int i = 0; i < steps; i++) {
			Ri = profile.evaluate(i * invs) * (R - taper.evaluate(i * invs) * R);
			Hj = reverse ? H - heightTaper.evaluate(i * invs) * H : heightTaper.evaluate(i * invs) * H;
			for (int j = 0; j <= facets; j++) {
				vertices[id][0] = Ri * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = Ri * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvws[id][0] = j * 1.0 / facets;
				uvws[id][1] = i * 1.0 / steps;
				if (reverse) {
					uvws[id][1] = 1.0 - uvws[id][1];
				}
				uvws[id][2] = 0;
				id++;
			}
		}
		// tipVertices
		for (int j = 0; j < facets; j++) {
			vertices[id][0] = 0.0;
			vertices[id][2] = 0.0;
			vertices[id][1] = reverse ? 0 : H;
			uvws[id][0] = (j + 0.5) * 1.0 / facets;
			uvws[id][1] = reverse ? 0 : 1;
			uvws[id][2] = 0;
			id++;
		}
		// capVertices
		if (cap) {
			Ri = profile.evaluate(0.0) * (R - taper.evaluate(0.0) * R);
			Hj = reverse ? H - heightTaper.evaluate(0.0) * H : heightTaper.evaluate(0.0) * H;
			for (int j = 0; j <= facets; j++) {
				vertices[id][0] = Ri * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = Ri * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvws[id][0] = 0.5;
				uvws[id][1] = reverse ? 1 : 0;
				uvws[id][2] = 0;
				id++;
			}
			for (int j = 0; j < facets; j++) {
				vertices[id][0] = 0.0;
				vertices[id][2] = 0.0;
				vertices[id][1] = reverse ? H : 0;
				uvws[id][0] = 0.5;
				uvws[id][1] = reverse ? 1 : 0;
				uvws[id][2] = 0;
				id++;
			}
		}
		final int mantleFaces = facets * steps;
		final int tipFaces = facets;
		final int capFaces = cap ? facets : 0;
		final int[][] faces = new int[mantleFaces + tipFaces + capFaces][];
		final int[] faceTextureIds = new int[faces.length];
		// mantleFaces
		id = 0;
		for (int j = 0; j < facets; j++) {
			for (int i = 0; i < steps - 1; i++) {
				faces[id] = new int[4];
				faceTextureIds[id] = 0;
				faces[id][0] = j + i * (facets + 1);
				faces[id][1] = j + (i + 1) * (facets + 1);
				faces[id][2] = j + 1 + (i + 1) * (facets + 1);
				faces[id][3] = j + 1 + i * (facets + 1);
				id++;
			}
		}
		// tipFaces
		for (int j = 0; j < facets; j++) {
			faces[id] = new int[3];
			faceTextureIds[id] = 0;
			faces[id][0] = j + tipOffset;
			faces[id][1] = j + tipOffset - facets;
			faces[id][2] = j + tipOffset - facets - 1;
			id++;
		}
		if (cap) {
			for (int j = 0; j < facets; j++) {
				faces[id] = new int[3];
				faceTextureIds[id] = 1;
				faces[id][0] = j + capOffset;
				faces[id][1] = j + 1 + capOffset;
				faces[id][2] = j + 1 + facets + capOffset;
				id++;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setUVW(uvws).setFaces(faces).setFaceTextureIds(faceTextureIds);
		return fl.createBase();
	}

	/**
	 *
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final HEC_Cone creator = new HEC_Cone();
		creator.setRadius(200).setHeight(400);
		creator.setFacets(7).setSteps(5);
		creator.setCap(true);
		creator.setReverse(false);
		creator.setCreationAxis(0, 0, 1);
		final HE_Mesh mesh = new HE_Mesh(creator);
		mesh.validate();
	}
}
