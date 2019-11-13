/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

/**
 * Capsule.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Capsule extends HEC_Creator {
	/** radius. */
	private double	R;
	/** Height. */
	private double	H;
	/** Facets. */
	private int		facets;
	private int		topcapsteps, bottomcapsteps;
	/** Height steps. */
	private int		steps;
	private boolean	topcap;
	private boolean	bottomcap;
	private double	phase;

	/**
	 * Instantiates a new Capsule.
	 *
	 */
	public HEC_Capsule() {
		super();
		R = 100;
		H = 100;
		facets = 6;
		steps = 1;
		setVerticalAxis(new WB_Vector(WB_Vector.Y()));
		topcap = true;
		bottomcap = true;
		topcapsteps = 3;
		bottomcapsteps = 3;
	}

	/**
	 *
	 * @param R
	 * @param H
	 * @param facets
	 * @param steps
	 * @param capfacets
	 */
	public HEC_Capsule(final double R, final double H, final int facets,
			final int steps, final int capfacets) {
		this();
		this.R = R;
		this.H = H;
		this.facets = facets;
		this.steps = steps;
		this.topcapsteps = capfacets;
		this.bottomcapsteps = capfacets;
	}

	/**
	 * Set radius.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Capsule setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 * set height.
	 *
	 * @param H
	 *            height
	 * @return self
	 */
	public HEC_Capsule setHeight(final double H) {
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
	public HEC_Capsule setSteps(final int steps) {
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
	public HEC_Capsule setFacets(final int facets) {
		this.facets = facets;
		return this;
	}

	public HEC_Capsule setCapSteps(final int steps) {
		this.topcapsteps = steps;
		this.bottomcapsteps = steps;
		return this;
	}

	public HEC_Capsule setCapSteps(final int topsteps, final int bottomsteps) {
		this.topcapsteps = topsteps;
		this.bottomcapsteps = bottomsteps;
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
	public HEC_Capsule setCap(final boolean topcap, final boolean bottomcap) {
		this.topcap = topcap;
		this.bottomcap = bottomcap;
		return this;
	}

	public HEC_Capsule setPhase(final double p) {
		phase = p;
		return this;
	}

	/**
	 *
	 *
	 * @param direction
	 * @return
	 */
	public HEC_Capsule align(final WB_Coord direction) {
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
	public HEC_Capsule align(final WB_Coord origin, final WB_Coord endpoint) {
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
	public HEC_Capsule align(final WB_Segment segment) {
		setHeight(segment.getLength());
		setCenter(segment.getCenter());
		setZAxis(segment.getDirection());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		int bcf = bottomcap ? Math.max(bottomcapsteps, 1) * facets : 0;
		int tcf = topcap ? Math.max(topcapsteps, 1) * facets : 0;
		final double[][] vertices = new double[(steps + 1) * (facets + 1)
				+ (bottomcap ? bcf * facets : 0)
				+ (topcap ? tcf * facets : 0)][3];
		final double[][] uvw = new double[(steps + 1) * (facets + 1)
				+ (bottomcap ? bcf * facets : 0)
				+ (topcap ? tcf * facets : 0)][3];
		final double invs = 1.0 / steps;
		int id = 0;
		for (int i = 0; i < steps + 1; i++) {
			final double Hj = i * H * invs;
			for (int j = 0; j < facets + 1; j++) {
				vertices[id][0] = R
						* Math.cos(2 * Math.PI / facets * j + phase);
				vertices[id][2] = R
						* Math.sin(2 * Math.PI / facets * j + phase);
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
			if (bottomcapsteps == 0) {
				for (int j = 0; j < facets; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = 0;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 1;
					uvw[id][2] = 1.0;
					id++;
				}
			} else if (bottomcapsteps == 1) {
				for (int j = 0; j < facets; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = -R;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 1;
					uvw[id][2] = 1.0;
					id++;
				}
			} else {
				for (int i = 0; i < bottomcapsteps; i++) {
					double offset = i == bottomcapsteps - 1 ? -R
							: -R * Math.sin(i * 0.5 * Math.PI / bottomcapsteps);
					double r = i == bottomcapsteps - 1 ? 0
							: R * Math.cos(i * 0.5 * Math.PI / bottomcapsteps);
					for (int j = 0; j < facets; j++) {
						vertices[id][0] = r
								* Math.cos(2 * Math.PI / facets * j + phase);
						vertices[id][2] = r
								* Math.sin(2 * Math.PI / facets * j + phase);
						vertices[id][1] = offset;
						uvw[id][0] = 0.5;// (j + 0.5) / facets;
						uvw[id][1] = 1;
						uvw[id][2] = 1.0;
						id++;
					}
				}
			}
		}
		if (topcap) {
			tv = id;
			if (topcapsteps == 0) {
				for (int j = 0; j < facets; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = H;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 0;
					uvw[id][2] = 1.0;
					id++;
				}
			} else if (topcapsteps == 1) {
				for (int j = 0; j < facets; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = H + R;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 0;
					uvw[id][2] = 1.0;
					id++;
				}
			} else {
				for (int i = 0; i < topcapsteps; i++) {
					double offset = i == topcapsteps - 1 ? R
							: R * Math.sin(i * 0.5 * Math.PI / topcapsteps);
					double r = i == topcapsteps - 1 ? 0
							: R * Math.cos(i * 0.5 * Math.PI / topcapsteps);
					for (int j = 0; j < facets; j++) {
						vertices[id][0] = r
								* Math.cos(2 * Math.PI / facets * j + phase);
						vertices[id][2] = r
								* Math.sin(2 * Math.PI / facets * j + phase);
						vertices[id][1] = H + offset;
						uvw[id][0] = 0.5;// (j + 0.5) / facets;
						uvw[id][1] = 0;
						uvw[id][2] = 1.0;
						id++;
					}
				}
			}
		}
		int nfaces = steps * facets;
		int bc = 0;
		int tc = 0;
		if (bottomcap) {
			bc = nfaces;
			nfaces += bcf * facets;
		}
		if (topcap) {
			tc = nfaces;
			nfaces += tcf * facets;
		}
		final int[][] faces = new int[nfaces][];
		final int[] faceTextureIds = new int[nfaces];
		for (int j = 0; j < facets; j++) {
			for (int i = 0; i < steps; i++) {
				faces[j + i * facets] = new int[4];
				faces[j + i * facets][0] = j + i * (facets + 1);
				faces[j + i * facets][1] = j + i * (facets + 1) + facets + 1;
				faces[j + i * facets][2] = j + 1 + facets + 1
						+ i * (facets + 1);
				faces[j + i * facets][3] = j + 1 + i * (facets + 1);
				faceTextureIds[j + i * facets] = 0;
			}
		}
		if (bottomcap) {
			if (bottomcapsteps <= 1) {
				for (int i = 0; i < facets; i++) {
					faces[bc + i] = new int[3];
					faces[bc + i][0] = i;
					faces[bc + i][1] = i + 1;
					faces[bc + i][2] = bv + i;
					faceTextureIds[bc + i] = 1;
				}
			} else {
			}
		}
		if (topcap) {
			if (topcapsteps <= 1) {
				for (int i = 0; i < facets; i++) {
					faces[tc + i] = new int[3];
					faces[tc + i][1] = steps * (facets + 1) + i;
					faces[tc + i][0] = steps * (facets + 1) + i + 1;
					faces[tc + i][2] = tv + i;
					faceTextureIds[tc + i] = 2;
				}
			} else {
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setVertexUVW(uvw)
				.setFaceTextureIds(faceTextureIds).setCheckDuplicateVertices(true).setRemoveUnconnectedElements(true);
		return fl.createBase();
	}
}
