package wblut.hemesh;

import wblut.geom.WB_Point;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_Torus extends HEC_Creator {
	/**  */
	private WB_ScalarParameter Rix, Riz;
	/**  */
	private WB_ScalarParameter Ro;
	/**  */
	private int tubefacets;
	/**  */
	private int torusfacets;
	/**  */
	private int twist;
	/**  */
	private WB_ScalarParameter tubephase;
	/**  */
	private double torusphase;

	/**
	 *
	 */
	public HEC_Torus() {
		super();
		Rix = new WB_ConstantScalarParameter(50);
		Riz = new WB_ConstantScalarParameter(50);
		Ro = new WB_ConstantScalarParameter(100);
		tubefacets = 6;
		torusfacets = 6;
		tubephase = new WB_ConstantScalarParameter(0.0);
		torusphase = 0.0;
	}

	/**
	 *
	 *
	 * @param Ri
	 * @param Ro
	 * @param tubefacets
	 * @param torusfacets
	 */
	public HEC_Torus(final double Ri, final double Ro, final int tubefacets, final int torusfacets) {
		this();
		this.Rix = this.Riz = new WB_ConstantScalarParameter(Ri);
		this.Ro = new WB_ConstantScalarParameter(Ro);
		this.tubefacets = tubefacets;
		this.torusfacets = torusfacets;
	}

	/**
	 *
	 *
	 * @param Rix
	 * @param Riz
	 * @param Ro
	 * @param tubefacets
	 * @param torusfacets
	 */
	public HEC_Torus(final double Rix, final double Riz, final double Ro, final int tubefacets, final int torusfacets) {
		this();
		this.Rix = new WB_ConstantScalarParameter(Rix);
		this.Riz = new WB_ConstantScalarParameter(Riz);
		this.Ro = new WB_ConstantScalarParameter(Ro);
		this.tubefacets = tubefacets;
		this.torusfacets = torusfacets;
	}

	/**
	 *
	 *
	 * @param Ri
	 * @param Ro
	 * @return
	 */
	public HEC_Torus setRadius(final double Ri, final double Ro) {
		this.Rix = this.Riz = new WB_ConstantScalarParameter(Ri);
		this.Ro = new WB_ConstantScalarParameter(Ro);
		return this;
	}

	/**
	 *
	 *
	 * @param Rix
	 * @param Riz
	 * @param Ro
	 * @return
	 */
	public HEC_Torus setRadius(final double Rix, final double Riz, final double Ro) {
		this.Rix = new WB_ConstantScalarParameter(Rix);
		this.Riz = new WB_ConstantScalarParameter(Riz);
		this.Ro = new WB_ConstantScalarParameter(Ro);
		return this;
	}

	/**
	 *
	 *
	 * @param Ro
	 * @return
	 */
	public HEC_Torus setTorusRadius(final double Ro) {
		this.Ro = new WB_ConstantScalarParameter(Ro);
		return this;
	}

	/**
	 *
	 *
	 * @param Ri
	 * @return
	 */
	public HEC_Torus setTubeRadius(final double Ri) {
		this.Rix = this.Riz = new WB_ConstantScalarParameter(Ri);
		return this;
	}

	/**
	 *
	 *
	 * @param Rix
	 * @param Riz
	 * @return
	 */
	public HEC_Torus setTubeRadius(final double Rix, final double Riz) {
		this.Rix = new WB_ConstantScalarParameter(Rix);
		this.Riz = new WB_ConstantScalarParameter(Riz);
		return this;
	}

	/**
	 *
	 *
	 * @param Ro
	 * @return
	 */
	public HEC_Torus setTorusRadius(final WB_ScalarParameter Ro) {
		this.Ro = Ro;
		return this;
	}

	/**
	 *
	 *
	 * @param Ri
	 * @return
	 */
	public HEC_Torus setTubeRadius(final WB_ScalarParameter Ri) {
		this.Rix = Ri;
		this.Riz = Ri;
		return this;
	}

	/**
	 *
	 *
	 * @param Rix
	 * @param Riz
	 * @return
	 */
	public HEC_Torus setTubeRadius(final WB_ScalarParameter Rix, final WB_ScalarParameter Riz) {
		this.Rix = Rix;
		this.Riz = Riz;
		return this;
	}

	/**
	 *
	 *
	 * @param facets
	 * @return
	 */
	public HEC_Torus setTubeFacets(final int facets) {
		tubefacets = facets;
		return this;
	}

	/**
	 *
	 *
	 * @param facets
	 * @return
	 */
	public HEC_Torus setTorusFacets(final int facets) {
		torusfacets = facets;
		return this;
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public HEC_Torus setTwist(final int t) {
		twist = t;
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEC_Torus setTorusPhase(final double p) {
		torusphase = p;
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEC_Torus setTubePhase(final double p) {
		tubephase = new WB_ConstantScalarParameter(p);
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEC_Torus setTubePhase(final WB_ScalarParameter p) {
		tubephase = p;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		final WB_Point[] vertices = new WB_Point[(tubefacets + 1) * (torusfacets + 1)];
		final WB_Point[] uvws = new WB_Point[(tubefacets + 1) * (torusfacets + 1)];
		final double dtua = 2 * Math.PI / tubefacets;
		final double dtoa = 2 * Math.PI / torusfacets;
		final double dv = 1.0 / tubefacets;
		final double du = 1.0 / torusfacets;
		final double dtwa = twist * dtoa / tubefacets;
		int id = 0;
		WB_Point basevertex;
		for (int j = 0; j < torusfacets + 1; j++) {
			final int lj = j == torusfacets ? 0 : j;
			final double ca = Math.cos(lj * dtoa + torusphase);
			final double sa = Math.sin(lj * dtoa + torusphase);
			for (int i = 0; i < tubefacets + 1; i++) {
				final int li = i == tubefacets ? 0 : i;
				final double ro = Ro.evaluate(lj * dtoa + torusphase);
				final double rix = Rix.evaluate(lj * dtoa + torusphase);
				final double riz = Riz.evaluate(lj * dtoa + torusphase);
				final double x = rix * Math.cos(dtua * li + j * dtwa);
				final double z = riz * Math.sin(dtua * li + j * dtwa);
				final double a = tubephase.evaluate(lj * dtoa + torusphase);
				final double cta = Math.cos(a);
				final double sta = Math.sin(a);
				basevertex = new WB_Point(ro + cta * x - sta * z, 0, cta * z + sta * x);
				vertices[id] = new WB_Point(ca * basevertex.xd(), sa * basevertex.xd(), basevertex.zd());
				uvws[id] = new WB_Point(j * du, i * dv, 0);
				id++;
			}
		}
		final int nfaces = tubefacets * torusfacets;
		id = 0;
		final int[][] faces = new int[nfaces][];
		int j = 0;
		for (j = 0; j < torusfacets; j++) {
			for (int i = 0; i < tubefacets; i++) {
				faces[id] = new int[4];
				faces[id][0] = i + j * (tubefacets + 1);
				faces[id][1] = i + (j + 1) * (tubefacets + 1);
				faces[id][2] = i + 1 + (j + 1) * (tubefacets + 1);
				faces[id][3] = i + 1 + j * (tubefacets + 1);
				id++;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces).setUVW(uvws);
		return fl.createBase();
	}
}
