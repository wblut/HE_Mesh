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
import wblut.math.WB_Epsilon;
import wblut.math.WB_LinearScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Cylinder.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Cylinder extends HEC_Creator {
	/** Base radius. */
	private double Ri;
	/** Top Radius. */
	private double Ro;
	/** Height. */
	private double H;
	/** Facets. */
	private int facets;
	/** Height steps. */
	private int steps;

	private boolean topcap;

	private boolean bottomcap;

	private WB_ScalarParameter profile;
	private WB_ScalarParameter taper;
	private WB_ScalarParameter heightTaper;
	private double phase;

	/**
	 * Instantiates a new cylinder.
	 *
	 */
	public HEC_Cylinder() {
		super();
		Ri = 100;
		Ro = 100;
		H = 100;
		facets = 6;
		steps = 1;
		setVerticalAxis(WB_Vector.Y());
		topcap = true;
		bottomcap = true;
		profile = new WB_ConstantScalarParameter(1.0);
		taper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
		heightTaper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
	}

	/**
	 * Instantiates a new cylinder.
	 *
	 * @param Ri
	 *            bottom radius
	 * @param Ro
	 *            top radius
	 * @param H
	 *            height
	 * @param facets
	 *            number of facets
	 * @param steps
	 *            number of height divisions
	 */
	public HEC_Cylinder(final double Ri, final double Ro, final double H, final int facets, final int steps) {
		this();
		this.Ri = Ri;
		this.Ro = Ro;
		this.H = H;
		this.facets = facets;
		this.steps = steps;
		profile = new WB_ConstantScalarParameter(1.0);
		taper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
		heightTaper = new WB_LinearScalarParameter(0.0, 1.0, 0.0, 1.0);
	}

	/**
	 * Set fixed radius.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Cylinder setRadius(final double R) {
		Ri = R;
		Ro = R;
		return this;
	}

	/**
	 * Set lower and upper radius.
	 *
	 * @param Ri
	 *            lower radius
	 * @param Ro
	 *            upper radius
	 * @return self
	 */
	public HEC_Cylinder setRadius(final double Ri, final double Ro) {
		this.Ri = Ri;
		this.Ro = Ro;
		return this;
	}

	/**
	 * set height.
	 *
	 * @param H
	 *            height
	 * @return self
	 */
	public HEC_Cylinder setHeight(final double H) {
		this.H = H;
		return this;
	}

	/**
	 * Set vertical divisions.
	 *
	 * @param steps
	 *            vertical divisions
	 * @return self
	 */
	public HEC_Cylinder setSteps(final int steps) {
		this.steps = steps;
		return this;
	}

	/**
	 * Set number of sides.
	 *
	 * @param facets
	 *            number of sides
	 * @return self
	 */
	public HEC_Cylinder setFacets(final int facets) {
		this.facets = facets;
		return this;
	}

	/**
	 * Set capping options.
	 *
	 * @param topcap
	 *            create top cap?
	 * @param bottomcap
	 *            create bottom cap?
	 * @return self
	 */
	public HEC_Cylinder setCap(final boolean topcap, final boolean bottomcap) {
		this.topcap = topcap;
		this.bottomcap = bottomcap;
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
	public HEC_Cylinder setProfile(final WB_ScalarParameter t) {
		profile = t;
		return this;
	}

	public HEC_Cylinder setTaper(final WB_ScalarParameter t) {
		taper = t;
		return this;
	}

	public HEC_Cylinder setTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		taper = new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type);
		return this;
	}

	public HEC_Cylinder setPhase(final double p) {
		phase = p;
		return this;
	}

	public HEC_Cylinder setHeightTaper(final WB_ScalarParameter t) {
		heightTaper = t;
		return this;
	}

	public HEC_Cylinder setHeightTaper(final WB_Ease E, final WB_Ease.EaseType type) {
		heightTaper = new WB_EaseScalarParameter(0, 1, 0, 1, true, E, type);
		return this;
	}

	/**
	 *
	 *
	 * @param direction
	 * @return
	 */
	public HEC_Cylinder align(final WB_Coord direction) {
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
	public HEC_Cylinder align(final WB_Coord origin, final WB_Coord endpoint) {
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
	public HEC_Cylinder align(final WB_Segment segment) {
		setHeight(segment.getLength());
		setCenter(segment.getCenter());
		setZAxis(segment.getDirection());
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (WB_Epsilon.isZero(Ro)) {
			final HEC_Cone cone = new HEC_Cone(Ri, H, facets, steps);
			cone.setCap(bottomcap).setProfile(profile).setTaper(taper).setHeightTaper(heightTaper);
			return cone.createBase();
		}
		if (WB_Epsilon.isZero(Ri)) {
			final HEC_Cone cone = new HEC_Cone(Ro, H, facets, steps);
			cone.setCap(topcap).setProfile(profile).setTaper(taper).setHeightTaper(heightTaper);
			cone.setReverse(true);
			return cone.createBase();
		}
		final double[][] vertices = new double[(steps + 1) * (facets + 1) + (bottomcap ? facets : 0)
				+ (topcap ? facets : 0)][3];
		final double[][] uvw = new double[(steps + 1) * (facets + 1) + (bottomcap ? facets : 0)
				+ (topcap ? facets : 0)][3];
		final double invs = 1.0 / steps;
		int id = 0;
		for (int i = 0; i < steps + 1; i++) {
			final double R = profile.evaluate(i * invs) * (Ri + taper.evaluate(i * invs) * (Ro - Ri));
			final double Hj = heightTaper.evaluate(i * invs) * H;
			for (int j = 0; j < facets + 1; j++) {
				vertices[id][0] = R * Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = R * Math.sin(2 * Math.PI / facets * j + phase);
				vertices[id][1] = Hj;
				uvw[id][0] = j * 1.0 / facets;
				uvw[id][1] = i * 1.0 / steps;
				uvw[id][2] = 0.0;
				id++;
			}
		}
		int bv = 0;
		int tv = 0;
		if (bottomcap) {
			bv = id;
			for (int j = 0; j < facets; j++) {
				vertices[id][0] = 0;
				vertices[id][2] = 0;
				vertices[id][1] = 0;
				uvw[id][0] = 0.5;// (j + 0.5) / facets;
				uvw[id][1] = 1;
				uvw[id][2] = 1.0;
				id++;
			}
		}
		if (topcap) {
			tv = id;
			for (int j = 0; j < facets; j++) {
				vertices[id][0] = 0;
				vertices[id][2] = 0;
				vertices[id][1] = H;
				uvw[id][0] = 0.5;// (j + 0.5) / facets;
				uvw[id][1] = 0;
				uvw[id][2] = 1.0;
				id++;
			}
		}
		int nfaces = steps * facets;
		int bc = 0;
		int tc = 0;
		if (bottomcap) {
			bc = nfaces;
			nfaces += facets;
		}
		if (topcap) {
			tc = nfaces;
			nfaces += facets;
		}
		final int[][] faces = new int[nfaces][];
		final int[] faceTextureIds = new int[nfaces];
		for (int j = 0; j < facets; j++) {
			for (int i = 0; i < steps; i++) {
				faces[j + i * facets] = new int[4];
				faces[j + i * facets][0] = j + i * (facets + 1);
				faces[j + i * facets][1] = j + i * (facets + 1) + facets + 1;
				faces[j + i * facets][2] = j + 1 + facets + 1 + i * (facets + 1);
				faces[j + i * facets][3] = j + 1 + i * (facets + 1);
				faceTextureIds[j + i * facets] = 0;
			}
		}
		if (bottomcap) {
			for (int i = 0; i < facets; i++) {
				faces[bc + i] = new int[3];
				faces[bc + i][0] = i;
				faces[bc + i][1] = i + 1;
				faces[bc + i][2] = bv + i;
				faceTextureIds[bc + i] = 1;
			}
		}
		if (topcap) {
			for (int i = 0; i < facets; i++) {
				faces[tc + i] = new int[3];
				faces[tc + i][1] = steps * (facets + 1) + i;
				faces[tc + i][0] = steps * (facets + 1) + i + 1;
				faces[tc + i][2] = tv + i;
				faceTextureIds[tc + i] = 2;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setVertexUVW(uvw).setFaceTextureIds(faceTextureIds);
		return fl.createBase();
	}
}
