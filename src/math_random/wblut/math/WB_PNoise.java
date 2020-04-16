package wblut.math;

public class WB_PNoise implements WB_Noise {
	static final int PERLIN_YWRAPB = 4;
	static final int PERLIN_YWRAP = 1 << PERLIN_YWRAPB;
	static final int PERLIN_ZWRAPB = 8;
	static final int PERLIN_ZWRAP = 1 << PERLIN_ZWRAPB;
	static final int PERLIN_SIZE = 4095;
	static final protected double cosLUT[];
	static final protected double SINCOS_PRECISION = 0.05;
	static final protected int SINCOS_LENGTH = (int) (360.0 / SINCOS_PRECISION);
	static {
		cosLUT = new double[SINCOS_LENGTH];
		for (int i = 0; i < SINCOS_LENGTH; i++) {
			cosLUT[i] = Math.cos(((i * Math.PI) / 180) * SINCOS_PRECISION);
		}
	}
	int perlin_octaves = 8;
	double perlin_amp_falloff = 0.5;
	int perlin_TWOPI, perlin_PI;
	double[] perlin;
	WB_MTRandom perlinRandom;
	private double sx, sy, sz;

	public WB_PNoise() {
		this(System.currentTimeMillis());
		sx = sy = sz = 1.0;
	}

	public WB_PNoise(final long seed) {
		setSeed(seed);
		sx = sy = sz = 1.0;
	}

	@Override
	public void setSeed(final long seed) {
		perlinRandom = new WB_MTRandom(seed);
		perlin = null;
	}

	@Override
	public double value1D(final double x) {
		return value3D(x, 0, 0);
	}

	@Override
	public double value2D(final double x, final double y) {
		return value3D(x, y, 0);
	}

	@Override
	public double value3D(double x, double y, double z) {
		if (perlin == null) {
			perlin = new double[PERLIN_SIZE + 1];
			for (int i = 0; i < (PERLIN_SIZE + 1); i++) {
				perlin[i] = perlinRandom.nextDouble();
			}
			perlin_TWOPI = perlin_PI = SINCOS_LENGTH;
			perlin_PI >>= 1;
		}
		if (x < 0) {
			x = -x;
		}
		if (y < 0) {
			y = -y;
		}
		if (z < 0) {
			z = -z;
		}
		int xi = (int) (sx * x), yi = (int) (sy * y), zi = (int) (sz * z);
		double xf = (sx * x) - xi;
		double yf = (sy * y) - yi;
		double zf = (sz * z) - zi;
		double rxf, ryf;
		double r = 0;
		double ampl = 0.5f;
		double n1, n2, n3;
		for (int i = 0; i < perlin_octaves; i++) {
			int of = xi + (yi << PERLIN_YWRAPB) + (zi << PERLIN_ZWRAPB);
			rxf = noise_fsc(xf);
			ryf = noise_fsc(yf);
			n1 = perlin[of & PERLIN_SIZE];
			n1 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n1);
			n2 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
			n2 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n2);
			n1 += ryf * (n2 - n1);
			of += PERLIN_ZWRAP;
			n2 = perlin[of & PERLIN_SIZE];
			n2 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n2);
			n3 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
			n3 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n3);
			n2 += ryf * (n3 - n2);
			n1 += noise_fsc(zf) * (n2 - n1);
			r += n1 * ampl;
			ampl *= perlin_amp_falloff;
			xi <<= 1;
			xf *= 2;
			yi <<= 1;
			yf *= 2;
			zi <<= 1;
			zf *= 2;
			if (xf >= 1.0f) {
				xi++;
				xf--;
			}
			if (yf >= 1.0f) {
				yi++;
				yf--;
			}
			if (zf >= 1.0f) {
				zi++;
				zf--;
			}
		}
		return r;
	}

	private double noise_fsc(final double i) {
		return 0.5 * (1.0 - cosLUT[(int) (i * perlin_PI) % perlin_TWOPI]);
	}

	public WB_PNoise setDetail(final int lod) {
		if (lod > 0) {
			perlin_octaves = lod;
		}
		return this;
	}

	public WB_PNoise setDetail(final int lod, final double falloff) {
		if (lod > 0) {
			perlin_octaves = lod;
		}
		if (falloff > 0) {
			perlin_amp_falloff = falloff;
		}
		return this;
	}

	@Override
	public double value4D(final double x, final double y, final double z, final double w) {
		throw new UnsupportedOperationException("4D Perlin noise not implemented.");
	}

	@Override
	public void setScale(final double sx) {
		this.sx = sx;
		this.sy = sx;
		this.sz = sx;
	}

	@Override
	public void setScale(final double sx, final double sy) {
		this.sx = sx;
		this.sy = sy;
	}

	@Override
	public void setScale(final double sx, final double sy, final double sz) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}

	@Override
	public void setScale(final double sx, final double sy, final double sz, final double sw) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}
}
