package wblut.geom;

import processing.core.PApplet;
import processing.core.PImage;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public abstract class WB_IsoValues3D {
	/**
	 *
	 */
	public enum Mode {
		/**  */
		RED,
		/**  */
		GREEN,
		/**  */
		BLUE,
		/**  */
		HUE,
		/**  */
		SAT,
		/**  */
		BRI,
		/**  */
		ALPHA
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public abstract double getValue(int i, int j, int k);

	/**
	 *
	 *
	 * @return
	 */
	public abstract int getSizeI();

	/**
	 *
	 *
	 * @return
	 */
	public abstract int getSizeJ();

	/**
	 *
	 *
	 * @return
	 */
	public abstract int getSizeK();

	/**
	 *
	 *
	 * @param sizeX
	 * @param sizeY
	 * @param sizeZ
	 * @param threshold
	 * @return
	 */
	public WB_BinaryGrid3D getBinaryGrid3D(final int sizeX, final int sizeY, final int sizeZ, final double threshold) {
		final WB_BinaryGrid3D grid = WB_BinaryGrid3D.createGrid(new WB_Point(), sizeX, 1.0, sizeY, 1.0, sizeZ, 1.0);
		for (int k = 0; k < sizeZ; k++) {
			for (int i = 0; i < sizeX; i++) {
				for (int j = 0; j < sizeY; j++) {
					if (getValue(i, j, k) >= threshold) {
						grid.set(i, j, k);
					}
				}
			}
		}
		return grid;
	}

	/**
	 *
	 */
	public static class Grid3D extends WB_IsoValues3D {
		/**  */
		private final double[][][] values;
		/**  */
		private final int sizeI, sizeJ, sizeK;

		/**
		 *
		 *
		 * @param values
		 */
		public Grid3D(final double[][][] values) {
			sizeI = values.length;
			sizeJ = sizeI == 0 ? 0 : values[0].length;
			sizeK = sizeJ == 0 ? 0 : values[0][0].length;
			this.values = new double[sizeI][sizeJ][sizeK];
			for (int i = 0; i < sizeI; i++) {
				for (int j = 0; j < sizeJ; j++) {
					for (int k = 0; k < sizeK; k++) {
						this.values[i][j][k] = values[i][j][k];
					}
				}
			}
		}

		/**
		 *
		 *
		 * @param values
		 */
		public Grid3D(final float[][][] values) {
			sizeI = values.length;
			sizeJ = sizeI == 0 ? 0 : values[0].length;
			sizeK = sizeJ == 0 ? 0 : values[0][0].length;
			this.values = new double[sizeI][sizeJ][sizeK];
			for (int i = 0; i < sizeI; i++) {
				for (int j = 0; j < sizeJ; j++) {
					for (int k = 0; k < sizeK; k++) {
						this.values[i][j][k] = values[i][j][k];
					}
				}
			}
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return values[i][j][k];
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	/**
	 *
	 */
	public static class GridRaw3D extends WB_IsoValues3D {
		/**  */
		private final double[][][] values;
		/**  */
		private final int sizeI, sizeJ, sizeK;

		/**
		 *
		 *
		 * @param values
		 */
		public GridRaw3D(final double[][][] values) {
			this.values = values;
			sizeI = values.length;
			sizeJ = sizeI == 0 ? 0 : values[0].length;
			sizeK = sizeJ == 0 ? 0 : values[0][0].length;
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return values[i][j][k];
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	/**
	 *
	 */
	public static class Function3D extends WB_IsoValues3D {
		/**  */
		private final double fxi, fyi, fzi, dfx, dfy, dfz;
		/**  */
		private final WB_ScalarParameter function;
		/**  */
		private final int sizeI, sizeJ, sizeK;

		/**
		 *
		 *
		 * @param function
		 * @param xi
		 * @param yi
		 * @param zi
		 * @param dx
		 * @param dy
		 * @param dz
		 * @param sizeI
		 * @param sizeJ
		 * @param sizeK
		 */
		public Function3D(final WB_ScalarParameter function, final double xi, final double yi, final double zi,
				final double dx, final double dy, final double dz, final int sizeI, final int sizeJ, final int sizeK) {
			this.function = function;
			fxi = xi;
			fyi = yi;
			fzi = zi;
			dfx = dx;
			dfy = dy;
			dfz = dz;
			this.sizeI = sizeI;
			this.sizeJ = sizeJ;
			this.sizeK = sizeK;
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return function.evaluate(fxi + i * dfx, fyi + j * dfy, fzi + k * dfz);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	/**
	 *
	 */
	public static class HashGrid3D extends WB_IsoValues3D {
		/**  */
		private final WB_HashGridDouble3D values;

		/**
		 *
		 *
		 * @param values
		 */
		public HashGrid3D(final WB_HashGridDouble3D values) {
			this.values = values;
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return values.getValue(i, j, k);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeI() {
			return values.getSizeI();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeJ() {
			return values.getSizeJ();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeK() {
			return values.getSizeK();
		}
	}

	/**
	 *
	 */
	public static class ImageStack3D extends WB_IsoValues3D {
		/**  */
		private final String[] images;
		/**  */
		private double[][] sliceK, sliceKpo;
		/**  */
		private int currentK, currentKpo;
		/**  */
		private final int sizeI, sizeJ, sizeK;
		/**  */
		private final Mode mode;
		/**  */
		private final PApplet home;
		/**  */
		private PImage slice;

		/**
		 *
		 *
		 * @param images
		 * @param home
		 * @param sizeI
		 * @param sizeJ
		 * @param sizeK
		 * @param mode
		 */
		public ImageStack3D(final String[] images, final PApplet home, final int sizeI, final int sizeJ,
				final int sizeK, final Mode mode) {
			this.images = images;
			this.mode = mode;
			this.home = home;
			this.sizeI = sizeI;
			this.sizeJ = sizeJ;
			this.sizeK = sizeK;
			sliceK = null;
			sliceKpo = null;
			currentK = -1;
			currentKpo = -1;
			initialize();
		}

		/**
		 *
		 *
		 * @param images
		 * @param home
		 * @param sizeI
		 * @param sizeJ
		 * @param sizeK
		 */
		public ImageStack3D(final String[] images, final PApplet home, final int sizeI, final int sizeJ,
				final int sizeK) {
			this.images = images;
			this.mode = WB_IsoValues3D.Mode.BRI;
			this.home = home;
			this.sizeI = sizeI;
			this.sizeJ = sizeJ;
			this.sizeK = sizeK;
			sliceK = null;
			sliceKpo = null;
			currentK = -1;
			currentKpo = -1;
			initialize();
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			if (k == currentK) {
				return sliceK[i][j];
			} else if (k == currentKpo) {
				return sliceKpo[i][j];
			} else if (k == currentKpo + 1) {
				incrementSlice();
				return sliceKpo[i][j];
			} else {
				return updateSlice(k)[i][j];
			}
		}

		/**
		 *
		 */
		private void initialize() {
			currentK = 0;
			currentKpo = 1;
			sliceK = new double[sizeI][sizeJ];
			sliceKpo = new double[sizeI][sizeJ];
			fillSlice(currentK, sliceK);
			fillSlice(currentKpo, sliceKpo);
		}

		/**
		 *
		 *
		 * @param k
		 * @param values
		 */
		private void fillSlice(final int k, final double[][] values) {
			if (sizeK == images.length && k >= 0 && k < images.length) {
				slice = home.loadImage(images[k]);
				slice.resize(sizeI, sizeJ);
				for (int i = 0; i < sizeI; i++) {
					for (int j = 0; j < sizeJ; j++) {
						values[i][j] = getSingleSliceValue(slice, i, j);
					}
				}
			} else {
				final double sliceThickness = images.length / (double) sizeK;
				final double startK = k * sliceThickness;
				final double endK = startK + sliceThickness;
				final int startKIndex = (int) startK;
				final int endKIndex = (int) endK;
				final double startFactor = 1.0 - startK + startKIndex;
				final double endFactor = endK - endKIndex;
				for (int i = 0; i < sizeI; i++) {
					for (int j = 0; j < sizeJ; j++) {
						values[i][j] = 0.0;
					}
				}
				double cumulf = 0.0;
				for (int img = startKIndex; img < endKIndex + 1; img++) {
					final double f = img == startKIndex ? startFactor : img == endKIndex ? endFactor : 1.0;
					if (f > 0 && img >= 0 && img < images.length) {
						cumulf += f;
						slice = home.loadImage(images[img]);
						slice.resize(sizeI, sizeJ);
						for (int i = 0; i < sizeI; i++) {
							for (int j = 0; j < sizeJ; j++) {
								values[i][j] += f * getSingleSliceValue(slice, i, j);
							}
						}
					}
				}
				for (int i = 0; i < sizeI; i++) {
					for (int j = 0; j < sizeJ; j++) {
						values[i][j] /= cumulf;
					}
				}
			}
		}

		/**
		 *
		 */
		private void incrementSlice() {
			currentK = currentKpo;
			currentKpo = currentKpo + 1;
			final double[][] swap = sliceK;
			sliceK = sliceKpo;
			sliceKpo = swap;
			fillSlice(currentKpo, sliceKpo);
		}

		/**
		 *
		 *
		 * @param k
		 * @return
		 */
		private double[][] updateSlice(final int k) {
			if (k == currentK + 1) {
				currentKpo = k;
				fillSlice(currentKpo, sliceKpo);
				return sliceKpo;
			} else if (k == currentKpo - 1) {
				currentK = k;
				fillSlice(currentK, sliceK);
				return sliceK;
			} else {
				currentK = k;
				fillSlice(currentK, sliceK);
				currentKpo = currentKpo + 1;
				fillSlice(currentKpo, sliceKpo);
				return sliceK;
			}
		}

		/**
		 *
		 *
		 * @param img
		 * @param i
		 * @param j
		 * @return
		 */
		private double getSingleSliceValue(final PImage img, final int i, final int j) {
			final int color = img.get(i, j);
			switch (mode) {
			case BRI:
				return home.brightness(color);
			case HUE:
				return home.hue(color);
			case SAT:
				return home.saturation(color);
			case RED:
				return home.red(color);
			case GREEN:
				return home.green(color);
			case BLUE:
				return home.blue(color);
			case ALPHA:
				return home.alpha(color);
			default:
				return home.brightness(color);
			}
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	/**
	 *
	 */
	public static class SubValues3D extends WB_IsoValues3D {
		/**  */
		int di, dj, dk;
		/**  */
		int sizeI, sizeJ, sizeK;
		/**  */
		WB_IsoValues3D parent;

		/**
		 *
		 *
		 * @param parent
		 * @param i
		 * @param j
		 * @param k
		 * @param sizeI
		 * @param sizeJ
		 * @param sizeK
		 */
		public SubValues3D(final WB_IsoValues3D parent, final int i, final int j, final int k, final int sizeI,
				final int sizeJ, final int sizeK) {
			this.parent = parent;
			final int pi = parent.getSizeI();
			if (pi == -1) {
				throw new IllegalArgumentException("SubGrid3D cannot have a FunctionGrid3D as a parent.");
			}
			final int pj = parent.getSizeJ();
			final int pk = parent.getSizeK();
			this.di = Math.min(Math.max(0, i), pi - 1);
			this.dj = Math.min(Math.max(0, j), pj - 1);
			this.dk = Math.min(Math.max(0, k), pk - 1);
			this.sizeI = Math.max(0, Math.min(sizeI, pi - di));
			this.sizeJ = Math.max(0, Math.min(sizeJ, pj - dj));
			this.sizeK = Math.max(0, Math.min(sizeK, pk - dk));
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return parent.getValue(i + di, j + dj, k + dk);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}
}
