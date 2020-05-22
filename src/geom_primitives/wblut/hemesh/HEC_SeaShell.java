package wblut.hemesh;

/**
 *
 */
public class HEC_SeaShell extends HEC_Creator {
	/**  */
	private double D, A, alpha, beta, mu, omega, phi, a, b, L, P, W1, W2, N, sc;
	/**  */
	private double muf, omegaf, phif, Lf, Pf, W1f, W2f;
	/**  */
	private double smin, smax, thetamin, thetamax;
	/**  */
	private double sdiv, thetadiv;
	/**  */
	private int ssteps, thetasteps;

	/**
	 *
	 */
	public HEC_SeaShell() {
		super();
		setD(1);
		setA(2);
		setAlpha(81);
		setBeta(15);
		setMu(0);
		setOmega(0);
		setPhi(0);
		seta(2);
		setb(1.5);
		setL(0);
		setP(0);
		setW1(0);
		setW2(0);
		setN(0);
		setScale(5);
		setRange(-90, 90, -720, 720);
		setDivisions(10, 10);
	}

	/**
	 *
	 *
	 * @param sm
	 * @param sM
	 * @param thetam
	 * @param thetaM
	 * @return
	 */
	public HEC_SeaShell setRange(final double sm, final double sM, final double thetam, final double thetaM) {
		smin = Math.min(sm, sM) / 180.0 * Math.PI;
		smax = Math.max(sm, sM) / 180.0 * Math.PI;
		thetamin = Math.min(thetam, thetaM) / 180.0 * Math.PI;
		thetamax = Math.max(thetam, thetaM) / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param sdiv
	 * @param thetadiv
	 * @return
	 */
	public HEC_SeaShell setDivisions(final double sdiv, final double thetadiv) {
		this.sdiv = sdiv / 180.0 * Math.PI;
		this.thetadiv = thetadiv / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param D
	 * @return
	 */
	public HEC_SeaShell setD(final double D) {
		this.D = D;
		return this;
	}

	/**
	 *
	 *
	 * @param A
	 * @return
	 */
	public HEC_SeaShell setA(final double A) {
		this.A = A;
		return this;
	}

	/**
	 *
	 *
	 * @param alpha
	 * @return
	 */
	public HEC_SeaShell setAlpha(final double alpha) {
		this.alpha = alpha / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param beta
	 * @return
	 */
	public HEC_SeaShell setBeta(final double beta) {
		this.beta = beta / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param mu
	 * @return
	 */
	public HEC_SeaShell setMu(final double mu) {
		this.mu = mu / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param omega
	 * @return
	 */
	public HEC_SeaShell setOmega(final double omega) {
		this.omega = omega / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param phi
	 * @return
	 */
	public HEC_SeaShell setPhi(final double phi) {
		this.phi = phi / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param muf
	 * @return
	 */
	public HEC_SeaShell setMuFactor(final double muf) {
		this.muf = muf / 360.0;
		return this;
	}

	/**
	 *
	 *
	 * @param omegaf
	 * @return
	 */
	public HEC_SeaShell setOmegaFactor(final double omegaf) {
		this.omegaf = omegaf / 360.0;
		return this;
	}

	/**
	 *
	 *
	 * @param phif
	 * @return
	 */
	public HEC_SeaShell setPhiFactor(final double phif) {
		this.phif = phif / 360.0;
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEC_SeaShell seta(final double a) {
		this.a = a;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_SeaShell setb(final double b) {
		this.b = b;
		return this;
	}

	/**
	 *
	 *
	 * @param L
	 * @return
	 */
	public HEC_SeaShell setL(final double L) {
		this.L = L;
		return this;
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public HEC_SeaShell setP(final double P) {
		this.P = P / 180.0 * Math.PI;
		while (this.P > Math.PI) {
			this.P -= 2 * Math.PI;
		}
		while (this.P < -Math.PI) {
			this.P += 2 * Math.PI;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param W1
	 * @return
	 */
	public HEC_SeaShell setW1(final double W1) {
		this.W1 = W1 / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param W2
	 * @return
	 */
	public HEC_SeaShell setW2(final double W2) {
		this.W2 = W2 / 180.0 * Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param Lf
	 * @return
	 */
	public HEC_SeaShell setLFactor(final double Lf) {
		this.Lf = 0.5 * Lf / Math.PI;
		return this;
	}

	/**
	 *
	 *
	 * @param Pf
	 * @return
	 */
	public HEC_SeaShell setPFactor(final double Pf) {
		this.Pf = Pf / 360.0;
		return this;
	}

	/**
	 *
	 *
	 * @param W1f
	 * @return
	 */
	public HEC_SeaShell setW1Factor(final double W1f) {
		this.W1f = W1f / 360.0;
		return this;
	}

	/**
	 *
	 *
	 * @param W2f
	 * @return
	 */
	public HEC_SeaShell setW2Factor(final double W2f) {
		this.W2f = W2f / 360.0;
		return this;
	}

	/**
	 *
	 *
	 * @param N
	 * @return
	 */
	public HEC_SeaShell setN(final double N) {
		this.N = N;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		sc = getScale();
		assert sdiv != 0 && thetadiv != 0;
		ssteps = (int) Math.round((smax - smin) / sdiv);
		thetasteps = (int) Math.round((thetamax - thetamin) / thetadiv);
		final double[][] vertices = new double[(ssteps + 1) * (thetasteps + 1)][3];
		final double[][] uvw = new double[(ssteps + 1) * (thetasteps + 1)][3];
		double s, theta;
		double x, y, z, sb, sm, hst, etca, csp, ssp, cto, sto, ita, lt;
		sb = Math.sin(beta);
		ita = 1.0 / Math.tan(alpha);
		for (int j = 0; j < thetasteps + 1; j++) {
			theta = thetamin + j * thetadiv;
			sm = Math.sin(mu + muf * theta);
			etca = Math.exp(theta * ita);
			cto = Math.cos(theta + omega + omegaf * theta);
			sto = Math.sin(theta + omega + omegaf * theta);
			lt = l(theta);
			for (int i = 0; i < ssteps + 1; i++) {
				s = smin + i * sdiv;
				hst = H(s, theta, lt);
				csp = Math.cos(s + phi + phif * theta);
				ssp = Math.sin(s + phi + phif * theta);
				x = sc * D * (A * sb * Math.cos(theta) + hst * (csp * cto - sm * ssp * sto)) * etca;
				y = sc * (A * sb * Math.sin(theta) + hst * (csp * sto + sm * ssp * cto)) * etca;
				z = sc * (-A * Math.cos(beta) + hst * ssp * Math.cos(mu + muf * theta)) * etca;
				vertices[j + i * (thetasteps + 1)] = new double[] { x, y, z };
				uvw[j + i * (thetasteps + 1)] = new double[] { j * 1.0 / thetasteps, i * 1.0 / ssteps, 0.0 };
			}
		}
		final int nfaces = ssteps * thetasteps;
		final int[][] faces = new int[nfaces][];
		for (int j = 0; j < thetasteps; j++) {
			for (int i = 0; i < ssteps; i++) {
				faces[j + i * thetasteps] = new int[4];
				faces[j + i * thetasteps][0] = j + i * (thetasteps + 1);
				faces[j + i * thetasteps][1] = j + i * (thetasteps + 1) + thetasteps + 1;
				faces[j + i * thetasteps][2] = (j + 1) % (thetasteps + 1) + thetasteps + 1 + i * (thetasteps + 1);
				faces[j + i * thetasteps][3] = (j + 1) % (thetasteps + 1) + i * (thetasteps + 1);
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setUVW(uvw).setFaces(faces).setCheckDuplicateVertices(false).setCheckNormals(true);
		return fl.createBase();
	}

	/**
	 *
	 *
	 * @param theta
	 * @return
	 */
	private double l(final double theta) {
		final double tmp = N * theta / (2.0 * Math.PI);
		double result = 2 * Math.PI / N * (tmp - (int) tmp);
		if (result < -Math.PI / N) {
			result += 2 * Math.PI / N;
		}
		if (result > Math.PI / N) {
			result -= 2 * Math.PI / N;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param s
	 * @param theta
	 * @param lt
	 * @return
	 */
	private double k(final double s, final double theta, final double lt) {
		if (W1 + W1f * theta == 0 || W2 + W2f * theta == 0 || N == 0) {
			return 0;
		}
		double Pl = P + Pf * theta;
		while (Pl > Math.PI) {
			Pl -= 2 * Math.PI;
		}
		while (Pl < Math.PI) {
			Pl += 2 * Math.PI;
		}
		final double tmp1 = 2 * (s - Pf) / (W1 + W1f * theta);
		final double tmp2 = 2 * lt / (W2 + W2f * theta);
		return (L + Lf * theta) * Math.exp(-tmp1 * tmp1 - tmp2 * tmp2);
	}

	/**
	 *
	 *
	 * @param s
	 * @param theta
	 * @param lt
	 * @return
	 */
	private double H(final double s, final double theta, final double lt) {
		final double tmp1 = Math.cos(s) / a;
		final double tmp2 = Math.sin(s) / b;
		return 1.0 / Math.sqrt(tmp1 * tmp1 + tmp2 * tmp2) + k(s, theta, lt);
	}
}
