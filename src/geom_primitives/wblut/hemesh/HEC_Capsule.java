package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;

public class HEC_Capsule extends HEC_Creator {
	public HEC_Capsule() {
		super();
		setCreationAxis(new WB_Vector(WB_Vector.Y()));
		parameters.set("facets", 6);
		parameters.set("height", 100.0);
		parameters.set("radius", 100.0);
		parameters.set("steps", 1);
		parameters.set("topcap", true);
		parameters.set("bottomcap", true);
		parameters.set("topcapsteps", 3);
		parameters.set("bottomcapsteps", 3);
		parameters.set("phase", 0.0);
	}

	public HEC_Capsule(final double R, final double H, final int facets, final int steps, final int capsteps) {
		this();
		parameters.set("facets", facets);
		parameters.set("height", H);
		parameters.set("radius", R);
		parameters.set("steps", steps);
		parameters.set("topcapsteps", capsteps);
		parameters.set("bottomcapsteps", capsteps);
	}

	public HEC_Capsule setRadius(final double R) {
		parameters.set("radius", R);
		return this;
	}

	public HEC_Capsule setHeight(final double H) {
		parameters.set("height", H);
		return this;
	}

	public HEC_Capsule setSteps(final int steps) {
		parameters.set("steps", steps);
		return this;
	}

	public HEC_Capsule setFacets(final int facets) {
		parameters.set("facets", facets);
		return this;
	}

	public HEC_Capsule setCapSteps(final int steps) {
		parameters.set("topcapsteps", steps);
		parameters.set("bottomcapsteps", steps);
		return this;
	}

	public HEC_Capsule setCapSteps(final int topsteps, final int bottomsteps) {
		parameters.set("topcapsteps", topsteps);
		parameters.set("bottomcapsteps", bottomsteps);
		return this;
	}

	public HEC_Capsule setCap(final boolean topcap, final boolean bottomcap) {
		parameters.set("topcap", topcap);
		parameters.set("bottomcap", bottomcap);
		return this;
	}

	public HEC_Capsule setPhase(final double p) {
		parameters.set("phase", p);
		return this;
	}

	public HEC_Capsule align(final WB_Coord direction) {
		setAxis(direction);
		return this;
	}

	public HEC_Capsule align(final WB_Coord origin, final WB_Coord endpoint) {
		setHeight(WB_GeometryOp3D.getDistance3D(origin, endpoint));
		setCenter(WB_Point.mulAddMul(0.5, origin, 0.5, endpoint));
		setAxis(new WB_Vector(origin, endpoint));
		return this;
	}

	public HEC_Capsule align(final WB_Segment segment) {
		setHeight(segment.getLength());
		setCenter(segment.getCenter());
		setAxis(segment.getDirection());
		return this;
	}

	double getRadius() {
		return parameters.get("radius", 0.0);
	}

	double getHeight() {
		return parameters.get("height", 0.0);
	}

	int getSteps() {
		return parameters.get("steps", 1);
	}

	int getFacets() {
		return parameters.get("facets", 0);
	}

	int getBottomCapSteps() {
		return parameters.get("bottomcapsteps", 3);
	}

	int getTopCapSteps() {
		return parameters.get("topcapsteps", 3);
	}

	boolean getBottomCap() {
		return parameters.get("bottomcap", true);
	}

	boolean getTopCap() {
		return parameters.get("topcap", true);
	}

	double getPhase() {
		return parameters.get("phase", 0.0);
	}

	@Override
	protected HE_Mesh createBase() {
		final boolean bottomcap = getBottomCap();
		final boolean topcap = getTopCap();
		final int bottomcapsteps = getBottomCapSteps();
		final int topcapsteps = getTopCapSteps();
		final int facets = getFacets();
		final int steps = getSteps();
		final double R = getRadius();
		final double H = getHeight();
		final double phase = getPhase();
		setCreationOrigin(new WB_Point(0, H / 2, 0));
		final int bcv = bottomcap ? (bottomcapsteps <= 1) ? facets + 1 : (bottomcapsteps + 1) * (facets + 1) : 0;
		final int tcv = topcap ? (topcapsteps <= 1) ? facets + 1 : (topcapsteps + 1) * (facets + 1) : 0;
		final double[][] vertices = new double[(steps + 1) * (facets + 1) + bcv + tcv][3];
		final double[][] uvw = new double[vertices.length][3];
		final double invs = 1.0 / steps;
		int id = 0;
		for (int i = 0; i < steps + 1; i++) {
			final double Hj = i * H * invs;
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
			if (bottomcapsteps == 0) {
				for (int j = 0; j < facets + 1; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = 0;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 1;
					uvw[id][2] = 1.0;
					id++;
				}
			} else if (bottomcapsteps == 1) {
				for (int j = 0; j < facets + 1; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = -R;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 1;
					uvw[id][2] = 1.0;
					id++;
				}
			} else {
				for (int i = 0; i < bottomcapsteps + 1; i++) {
					final double offset = i == bottomcapsteps ? -R : -R * Math.sin(i * 0.5 * Math.PI / bottomcapsteps);
					final double r = i == bottomcapsteps ? 0 : R * Math.cos(i * 0.5 * Math.PI / bottomcapsteps);
					for (int j = 0; j < facets + 1; j++) {
						vertices[id][0] = r * Math.cos(2 * Math.PI / facets * j + phase);
						vertices[id][2] = r * Math.sin(2 * Math.PI / facets * j + phase);
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
				for (int j = 0; j < facets + 1; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = H;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 0;
					uvw[id][2] = 1.0;
					id++;
				}
			} else if (topcapsteps == 1) {
				for (int j = 0; j < facets + 1; j++) {
					vertices[id][0] = 0;
					vertices[id][2] = 0;
					vertices[id][1] = H + R;
					uvw[id][0] = 0.5;// (j + 0.5) / facets;
					uvw[id][1] = 0;
					uvw[id][2] = 1.0;
					id++;
				}
			} else {
				for (int i = 0; i < topcapsteps + 1; i++) {
					final double offset = i == topcapsteps ? R : R * Math.sin(i * 0.5 * Math.PI / topcapsteps);
					final double r = i == topcapsteps ? 0 : R * Math.cos(i * 0.5 * Math.PI / topcapsteps);
					for (int j = 0; j < facets + 1; j++) {
						vertices[id][0] = r * Math.cos(2 * Math.PI / facets * j + phase);
						vertices[id][2] = r * Math.sin(2 * Math.PI / facets * j + phase);
						vertices[id][1] = H + offset;
						uvw[id][0] = 0.5;// (j + 0.5) / facets;
						uvw[id][1] = 0;
						uvw[id][2] = 1.0;
						id++;
					}
				}
			}
		}
		final int bcf = bottomcap ? Math.max(1, bottomcapsteps) * facets : 0;
		final int tcf = topcap ? Math.max(1, topcapsteps) * facets : 0;
		final int nfaces = steps * facets + bcf + tcf;
		final int[][] faces = new int[nfaces][];
		final int[] faceTextureIds = new int[nfaces];
		int fid = 0;
		for (int i = 0; i < steps; i++) {
			for (int j = 0; j < facets; j++) {
				faces[fid] = new int[4];
				faces[fid][0] = j + i * (facets + 1);
				faces[fid][1] = j + i * (facets + 1) + facets + 1;
				faces[fid][2] = j + 1 + facets + 1 + i * (facets + 1);
				faces[fid][3] = j + 1 + i * (facets + 1);
				faceTextureIds[fid] = 0;
				fid++;
			}
		}
		if (bottomcap) {
			if (bottomcapsteps <= 1) {
				for (int i = 0; i < facets; i++) {
					faces[fid] = new int[3];
					faces[fid][0] = i;
					faces[fid][1] = i + 1;
					faces[fid][2] = bv + i;
					faceTextureIds[fid] = 1;
					fid++;
				}
			} else {
				for (int i = 0; i < bottomcapsteps - 1; i++) {
					for (int j = 0; j < facets; j++) {
						faces[fid] = new int[4];
						faces[fid][0] = bv + j + i * (facets + 1);
						faces[fid][3] = bv + j + i * (facets + 1) + facets + 1;
						faces[fid][2] = bv + j + 1 + facets + 1 + i * (facets + 1);
						faces[fid][1] = bv + j + 1 + i * (facets + 1);
						faceTextureIds[fid] = 1;
						fid++;
					}
				}
				for (int j = 0; j < facets; j++) {
					faces[fid] = new int[3];
					faces[fid][0] = bv + j + (bottomcapsteps - 1) * (facets + 1);
					faces[fid][2] = bv + j + bottomcapsteps * (facets + 1);
					faces[fid][1] = bv + j + 1 + (bottomcapsteps - 1) * (facets + 1);
					faceTextureIds[fid] = 1;
					fid++;
				}
			}
		}
		if (topcap) {
			if (topcapsteps <= 1) {
				for (int i = 0; i < facets; i++) {
					faces[fid] = new int[3];
					faces[fid][1] = steps * (facets + 1) + i;
					faces[fid][0] = steps * (facets + 1) + i + 1;
					faces[fid][2] = tv + i;
					faceTextureIds[fid] = 2;
					fid++;
				}
			} else {
				for (int i = 0; i < topcapsteps - 1; i++) {
					for (int j = 0; j < facets; j++) {
						faces[fid] = new int[4];
						faces[fid][0] = tv + j + i * (facets + 1);
						faces[fid][1] = tv + j + i * (facets + 1) + facets + 1;
						faces[fid][2] = tv + j + 1 + facets + 1 + i * (facets + 1);
						faces[fid][3] = tv + j + 1 + i * (facets + 1);
						faceTextureIds[fid] = 2;
						fid++;
					}
				}
				for (int j = 0; j < facets; j++) {
					faces[fid] = new int[3];
					faces[fid][0] = tv + j + (topcapsteps - 1) * (facets + 1);
					faces[fid][1] = tv + j + topcapsteps * (facets + 1);
					faces[fid][2] = tv + j + 1 + (topcapsteps - 1) * (facets + 1);
					faceTextureIds[fid] = 2;
					fid++;
				}
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setUVW(uvw).setFaceTextureIds(faceTextureIds)
				.setCheckDuplicateVertices(true).setRemoveUnconnectedElements(true);
		return fl.createBase();
	}
}
