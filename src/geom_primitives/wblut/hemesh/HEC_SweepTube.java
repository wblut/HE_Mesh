package wblut.hemesh;

import wblut.geom.WB_Curve;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class HEC_SweepTube extends HEC_Creator {
	private double R;
	private int facets;
	private int steps;
	private WB_Curve curve;
	private boolean topcap;
	private boolean bottomcap;
	private double umin, umax, s;

	public HEC_SweepTube() {
		super();
		R = 100;
		facets = 6;
		steps = 1;
		topcap = true;
		bottomcap = true;
		setOverride(true);
		umin = 0;
		umax = 1;
	}

	public HEC_SweepTube(final double R, final int facets, final int steps, final WB_Curve curve) {
		this();
		this.R = R;
		this.facets = facets;
		this.steps = steps;
		this.curve = curve;
		setOverride(true);
		umin = 0;
		umax = 1;
	}

	public HEC_SweepTube setRange(final double umin, final double umax) {
		this.umin = umin;
		this.umax = umax;
		return this;
	}

	public HEC_SweepTube setRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_SweepTube setSteps(final int steps) {
		this.steps = steps;
		return this;
	}

	public HEC_SweepTube setFacets(final int facets) {
		this.facets = facets;
		return this;
	}

	public HEC_SweepTube setCap(final boolean topcap, final boolean bottomcap) {
		this.topcap = topcap;
		this.bottomcap = bottomcap;
		return this;
	}

	public HEC_SweepTube setCurve(final WB_Curve curve) {
		this.curve = curve;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		final WB_Point[] vertices = new WB_Point[(steps + 1) * facets];
		final WB_Point[] basevertices = new WB_Point[facets];
		final double da = 2 * Math.PI / facets;
		for (int i = 0; i < facets; i++) {
			// normal(0,0,1);
			basevertices[i] = new WB_Point(R * Math.cos(da * i), R * Math.sin(da * i), 0);
		}
		final double ds = (umax - umin) / steps;
		WB_Point onCurve;
		WB_Vector deriv;
		WB_Vector oldderiv = new WB_Vector(0, 0, 1);
		final WB_Point origin = new WB_Point(0, 0, 0);
		for (int i = 0; i < steps + 1; i++) {
			onCurve = curve.getPointOnCurve(umin + i * ds);
			deriv = curve.getDirectionOnCurve(umin + i * ds);
			final WB_Vector axis = oldderiv.cross(deriv);
			final double angle = Math.acos(WB_Math.clamp(oldderiv.dot(deriv), -1, 1));
			for (int j = 0; j < facets; j++) {
				if (!WB_Epsilon.isZeroSq(axis.getSqLength3D())) {
					basevertices[j].rotateAboutAxisSelf(angle, origin, axis);
				}
				vertices[j + i * facets] = new WB_Point(basevertices[j]);
				vertices[j + i * facets].addSelf(onCurve);
			}
			oldderiv = deriv;
		}
		int nfaces = steps * facets;
		int bc = 0;
		int tc = 0;
		if (bottomcap) {
			bc = nfaces;
			nfaces++;
		}
		if (topcap) {
			tc = nfaces;
			nfaces++;
		}
		final int[][] faces = new int[nfaces][];
		if (bottomcap) {
			faces[bc] = new int[facets];
		}
		if (topcap) {
			faces[tc] = new int[facets];
		}
		for (int j = 0; j < facets; j++) {
			if (bottomcap) {
				faces[bc][facets - 1 - j] = j;
			}
			if (topcap) {
				faces[tc][j] = steps * facets + j;
			}
			for (int i = 0; i < steps; i++) {
				faces[j + i * facets] = new int[4];
				faces[j + i * facets][0] = j + i * facets;
				faces[j + i * facets][3] = j + i * facets + facets;
				faces[j + i * facets][2] = (j + 1) % facets + facets + i * facets;
				faces[j + i * facets][1] = (j + 1) % facets + i * facets;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces);
		final HE_Mesh base = fl.createBase();
		s = getScale();
		base.scaleSelf(s);
		return base;
	}
}
