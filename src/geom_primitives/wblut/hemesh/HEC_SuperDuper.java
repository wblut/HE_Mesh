package wblut.hemesh;

import wblut.geom.WB_Point;

public class HEC_SuperDuper extends HEC_Creator {
	private int U;
	private int V;
	private boolean uWrap;
	private boolean vWrap;
	private double radius;
	private double[] param;

	public HEC_SuperDuper() {
		super();
		radius = 100.0;
		U = 24;
		V = 24;
		uWrap = false;
		vWrap = false;
	}

	public HEC_SuperDuper setU(final int U) {
		this.U = U;
		return this;
	}

	public HEC_SuperDuper setV(final int V) {
		this.V = V;
		return this;
	}

	public HEC_SuperDuper setUWrap(final boolean b) {
		uWrap = b;
		return this;
	}

	public HEC_SuperDuper setVWrap(final boolean b) {
		vWrap = b;
		return this;
	}

	public HEC_SuperDuper setRadius(final double r) {
		radius = r;
		return this;
	}

	public HEC_SuperDuper setGeneralParameters(final double m1, final double n11, final double n12, final double n13,
			final double m2, final double n21, final double n22, final double n23, final double t1, final double t2,
			final double d1, final double d2, final double c1, final double c2, final double c3) {
		param = new double[] { m1, n11, n12, n13, m2, n21, n22, n23, t1, t2, d1, d2, c1, c2, c3 };
		return this;
	}

	public HEC_SuperDuper setDonutParameters(final double m1, final double n11, final double n12, final double n13,
			final double m2, final double n21, final double n22, final double n23, final double t, final double c) {
		param = new double[] { m1, n11, n12, n13, m2, n21, n22, n23, t, 0, 0, 0, 1, 2, c };
		return this;
	}

	public HEC_SuperDuper setShellParameters(final double m1, final double n11, final double n12, final double n13,
			final double m2, final double n21, final double n22, final double n23, final double t, final double d1,
			final double d2, final double c) {
		param = new double[] { m1, n11, n12, n13, m2, n21, n22, n23, 0, t, d1, d2, c, 1, 0 };
		return this;
	}

	public HEC_SuperDuper setSuperShapeParameters(final double m1, final double n11, final double n12, final double n13,
			final double m2, final double n21, final double n22, final double n23) {
		param = new double[] { m1, n11, n12, n13, m2, n21, n22, n23, 0, 0, 0, 0, 1, 1, 0 };
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		final WB_Point[] points = getPoints();
		final WB_Point[] uvw = getUVW();
		final int[][] faces = new int[U * V][4];
		int li, lj;
		for (int i = 0; i < U; i++) {
			li = i + 1;
			for (int j = 0; j < V; j++) {
				lj = j + 1;
				faces[i + U * j][3] = i + (U + 1) * j;
				faces[i + U * j][2] = li + (U + 1) * j;
				faces[i + U * j][1] = li + (U + 1) * lj;
				faces[i + U * j][0] = i + (U + 1) * lj;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setFaces(faces).setVertices(points).setUVW(uvw);
		return new HE_Mesh(fl);
	}

	private WB_Point[] getPoints() {
		final WB_Point[] m = new WB_Point[(U + 1) * (V + 1)];
		final double iU = 1.0 / U;
		final double iV = 1.0 / V;
		for (int i = 0; i <= U; i++) {
			final int li = uWrap ? i == U ? 0 : i : i;
			for (int j = 0; j <= V; j++) {
				final int lj = vWrap ? j == V ? 0 : j : j;
				m[i + (U + 1) * j] = eval(li * iU, lj * iV);
			}
		}
		return m;
	}

	private WB_Point[] getUVW() {
		final WB_Point[] m = new WB_Point[(U + 1) * (V + 1)];
		final double iU = 1.0 / U;
		final double iV = 1.0 / V;
		for (int i = 0; i <= U; i++) {
			for (int j = 0; j <= V; j++) {
				m[i + (U + 1) * j] = new WB_Point(i * iU, j * iV, 0);
			}
		}
		return m;
	}

	private WB_Point eval(final double u, final double v) {
		return superduperformula(radius, u, v, param[12], param[13], param[14], param[0], param[1], param[2], param[3],
				param[4], param[5], param[6], param[7], param[8], param[9], param[10], param[11]);
	}

	private double superformula(final double phi, final double a, final double b, final double m, final double n1,
			final double n2, final double n3) {
		return Math.pow(
				Math.pow(Math.abs(Math.cos(m * phi / 4) / a), n2) + Math.pow(Math.abs(Math.sin(m * phi / 4) / b), n3),
				-1 / n1);
	}

	private WB_Point superduperformula(final double r0, double u, double v, final double c1, final double c2,
			final double c3, final double m1, final double n11, final double n12, final double n13, final double m2,
			final double n21, final double n22, final double n23, final double t1, double t2, double d1, double d2) {
		final double t2c = r0 * Math.pow(c2, d2) * t2 * c1 / 2;
		t2 = t2 * c1 * u;
		d1 = Math.pow(u * c1, d1);
		d2 = Math.pow(u * c2, d2);
		u = lerp(-Math.PI, Math.PI, u) * c1;
		v = lerp(-Math.PI / 2, Math.PI / 2, v) * c2;
		final double v2 = v + c3 * u;
		final double r1 = superformula(u, 1, 1, m1, n11, n12, n13);
		final double r2 = superformula(v, 1, 1, m2, n21, n22, n23);
		final double x = r0 * r1 * (t1 + d1 * r2 * Math.cos(v2)) * Math.sin(u);
		final double y = r0 * r1 * (t1 + d1 * r2 * Math.cos(v2)) * Math.cos(u);
		final double z = r0 * d2 * (r2 * Math.sin(v2) - t2) + t2c;
		return new WB_Point(x, y, z);
	}

	private double lerp(final double ll, final double ul, final double f) {
		return ll + f * (ul - ll);
	}
}
