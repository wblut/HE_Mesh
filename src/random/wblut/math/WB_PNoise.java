/*
 *
 * Perlin noise, implementation adapted from Processing
 *
 * Original copyright header:
 *
 * Part of the Processing project - http://processing.org Copyright (c) 2013-15
 * The Processing Foundation Copyright (c) 2004-12 Ben Fry and Casey Reas
 * Copyright (c) 2001-04 Massachusetts Institute of Technology This library is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
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

	/**
	 *
	 */
	public WB_PNoise() {
		this(System.currentTimeMillis());
		sx = sy = sz = 1.0;
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_PNoise(final long seed) {
		setSeed(seed);
		sx = sy = sz = 1.0;
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#setSeed(long)
	 */
	@Override
	public void setSeed(final long seed) {
		perlinRandom = new WB_MTRandom(seed);
		perlin = null;
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#value1D(double)
	 */
	@Override
	public double value1D(final double x) {
		return value3D(x, 0, 0);
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#value2D(double, double)
	 */
	@Override
	public double value2D(final double x, final double y) {
		return value3D(x, y, 0);
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#value3D(double, double, double)
	 */
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

		for (int i = 0; i < perlin_octaves; i++)

		{
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

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	private double noise_fsc(final double i) {
		return 0.5 * (1.0 - cosLUT[(int) (i * perlin_PI) % perlin_TWOPI]);
	}

	/**
	 *
	 *
	 * @param lod
	 * @return
	 */
	public WB_PNoise setDetail(final int lod) {
		if (lod > 0) {
			perlin_octaves = lod;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param lod
	 * @param falloff
	 * @return
	 */
	public WB_PNoise setDetail(final int lod, final double falloff) {
		if (lod > 0) {
			perlin_octaves = lod;
		}
		if (falloff > 0) {
			perlin_amp_falloff = falloff;
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#value4D(double, double, double, double)
	 */
	@Override
	public double value4D(final double x, final double y, final double z, final double w) {
		throw new UnsupportedOperationException("4D Perlin noise not implemented.");
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#setScale(double)
	 */
	@Override
	public void setScale(final double sx) {
		this.sx = sx;
		this.sy = sx;
		this.sz = sx;

	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#setScale(double, double)
	 */
	@Override
	public void setScale(final double sx, final double sy) {
		this.sx = sx;
		this.sy = sy;
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#setScale(double, double, double)
	 */
	@Override
	public void setScale(final double sx, final double sy, final double sz) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}

	/* (non-Javadoc)
	 * @see wblut.math.WB_Noise#setScale(double, double, double, double)
	 */
	@Override
	public void setScale(final double sx, final double sy, final double sz, final double sw) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;

	}

}
