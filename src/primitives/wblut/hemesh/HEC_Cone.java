/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_Ease;
import wblut.math.WB_EaseScalarParameter;
import wblut.math.WB_LinearScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Cone.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Cone extends HEC_Creator {
	/** Base radius. */
	private double R;
	/** Height. */
	private double H;
	/** Height segments. */
	private int steps;
	/** Facets. */
	private int facets;
	/** The cap. */
	private boolean cap;
	/** The reverse. */
	private boolean reverse;
	/** The profile. */
	private WB_ScalarParameter profile;
	private WB_ScalarParameter taper;
	private WB_ScalarParameter heightTaper;
	private double phase;

	/**
	 * Instantiates a new cone.
	 *
	 */
	public HEC_Cone() {
		super();
		R = 100;
		H = 100;
		facets = 6;
		steps = 1;
		setVerticalAxis(WB_Vector.Y());
		cap = true;
		profile = new WB_ConstantScalarParameter(1.0);
		taper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
		heightTaper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
	}

	/**
	 * Instantiates a new cone.
	 *
	 * @param R
	 *            radius
	 * @param H
	 *            heights
	 * @param facets
	 *            number of facets
	 * @param steps
	 *            number of height divisions
	 */
	public HEC_Cone(final double R, final double H, final int facets, final int steps) {
		this();
		this.R = R;
		this.H = H;
		this.facets = facets;
		this.steps = steps;
		profile = new WB_ConstantScalarParameter(1.0);
		taper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
		heightTaper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
	}

	/**
	 * Set base radius.
	 *
	 * @param R
	 *            base radius
	 * @return self
	 */
	public HEC_Cone setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 * Set height.
	 *
	 * @param H
	 *            height
	 * @return self
	 */
	public HEC_Cone setHeight(final double H) {
		this.H = H;
		return this;
	}

	/**
	 * Set number of sides.
	 *
	 * @param facets
	 *            number of sides
	 * @return self
	 */
	public HEC_Cone setFacets(final int facets) {
		this.facets = facets;
		return this;
	}

	/**
	 * Set number of vertical divisions.
	 *
	 * @param steps
	 *            vertical divisions
	 * @return self
	 */
	public HEC_Cone setSteps(final int steps) {
		this.steps = steps;
		return this;
	}

	/**
	 * Set capping options.
	 *
	 * @param cap
	 *            create cap?
	 * @return self
	 */
	public HEC_Cone setCap(final boolean cap) {
		this.cap = cap;
		return this;
	}

	/**
	 * Reverse cone.
	 *
	 * @param rev
	 *            the rev
	 * @return self
	 */
	public HEC_Cone setReverse(final boolean rev) {
		reverse = rev;
		return this;
	}

	/**
	 *
	 *
	 * @param direction
	 * @return
	 */
	public HEC_Cone align(final WB_Coord direction) {
		setZAxis(direction);
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
		setHeight(WB_CoordOp.getDistance3D(origin, endpoint));
		setCenter(WB_Point.mulAddMul(0.5, origin, 0.5, endpoint));
		setZAxis(new WB_Vector(origin, endpoint));
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
		setCenter(segment.getCenter());
		setZAxis(segment.getDirection());
		return this;
	}

	/**
	 * Sets the profile. Parameter should be a WB_ScalarParameter on the domain
	 * [0,1]. The radius at fractional height x is multiplied by the profile
	 * value at x.
	 *
	 * @param t
	 *            the t
	 * @return self
	 */
	public HEC_Cone setProfile(final WB_ScalarParameter t) {
		profile = t;
		return this;
	}

	public HEC_Cone setTaper(final WB_ScalarParameter t) {
		taper = t;
		return this;
	}

	public HEC_Cone setTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		taper = new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type);
		return this;
	}

	public HEC_Cone setPhase(final double p) {
		phase = p;
		return this;
	}

	public HEC_Cone setHeightTaper(final WB_ScalarParameter t) {
		heightTaper = t;
		return this;
	}

	public HEC_Cone setHeigthTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		heightTaper = new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final double[][] vertices = new double[(facets + 1) * steps + facets + (cap ? facets : 0)][3];
		final double[][] uvws = new double[(facets + 1) * steps + facets + (cap ? facets : 0)][3];
		final int[][] faces = new int[cap ? facets * steps + facets : facets * steps][];
		final int[] faceTextureIds = new int[cap ? facets * steps + facets : facets * steps];
		double Ri;
		double Hj;
		final double invs = 1.0 / steps;
		int id = 0;
		for (int i = 0; i < steps; i++) {
			Ri = profile.evaluate(i * invs) * (R - taper.evaluate(i * invs) * R);
			Hj = reverse ? H - heightTaper.evaluate(i * invs) * H : heightTaper.evaluate(i * invs) * H;
			for (int j = 0; j < facets + 1; j++) {
				vertices[id][0] = Ri * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = Ri * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvws[id][0] = j * 1.0 / facets;
				uvws[id][1] = i * 1.0 / steps;
				uvws[id][2] = 0;
				id++;
			}
		}
		final int tipoffset = id;
		for (int j = 0; j < facets; j++) {
			vertices[id][0] = 0;
			vertices[id][2] = 0;
			vertices[id][1] = reverse ? 0 : H;
			uvws[id][0] = 0.5;
			uvws[id][1] = 1;
			uvws[id][2] = 0;
			id++;
		}
		final int capoffset = id;
		if (cap) {
			for (int j = 0; j < facets; j++) {
				vertices[id][0] = 0;
				vertices[id][2] = 0;
				vertices[id][1] = reverse ? H : 0;
				uvws[id][0] = 0.5;
				uvws[id][1] = 1;
				uvws[id][2] = 0;
				id++;
			}
		}
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
			faces[id] = new int[3];
			faceTextureIds[id] = 0;
			faces[id][0] = tipoffset + j;
			faces[id][2] = j + (steps - 1) * (facets + 1);
			faces[id][1] = j + 1 + (steps - 1) * (facets + 1);
			id++;
			if (cap) {
				faces[id] = new int[3];
				faceTextureIds[id] = 1;
				faces[id][0] = j;
				faces[id][2] = j + capoffset;
				faces[id][1] = j + 1;
				id++;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setVertexUVW(uvws).setFaces(faces).setFaceTextureIds(faceTextureIds);
		return fl.createBase();
	}

	public static void main(final String[] args) {
		HEC_Cone creator = new HEC_Cone();
		creator.setRadius(200).setHeight(400);
		creator.setFacets(7).setSteps(5);
		creator.setCap(true);
		creator.setReverse(false);
		creator.setZAxis(0, 0, 1);
		HE_Mesh mesh = new HE_Mesh(creator);
		mesh.validate();

	}
}
