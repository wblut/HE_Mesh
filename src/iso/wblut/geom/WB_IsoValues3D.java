/*
 *
 */
package wblut.geom;

import processing.core.PApplet;
import processing.core.PImage;
import wblut.math.WB_ScalarParameter;

/**
 * @author FVH
 *
 */
public abstract class WB_IsoValues3D {
	public static enum Mode {
		RED, GREEN, BLUE, HUE, SAT, BRI, ALPHA
	};

	public abstract double getValue(int i, int j, int k);

	public abstract int getSizeI();

	public abstract int getSizeJ();

	public abstract int getSizeK();

	public WB_BinaryGrid3D getBinaryGrid3D(final int sizeX, final int sizeY,
			final int sizeZ, final double threshold) {
		WB_BinaryGrid3D grid = WB_BinaryGrid3D.createGrid(new WB_Point(), sizeX,
				1.0, sizeY, 1.0, sizeZ, 1.0);
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

	public static class Grid3D extends WB_IsoValues3D {
		private double[][][]	values;
		private int				sizeI, sizeJ, sizeK;

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

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues2D#value(int, int, int)
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return values[i][j][k];
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeI()
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeJ()
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeK()
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	public static class GridRaw3D extends WB_IsoValues3D {
		private double[][][]	values;
		private int				sizeI, sizeJ, sizeK;

		public GridRaw3D(final double[][][] values) {
			this.values = values;
			sizeI = values.length;
			sizeJ = sizeI == 0 ? 0 : values[0].length;
			sizeK = sizeJ == 0 ? 0 : values[0][0].length;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#value(int, int, int)
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return values[i][j][k];
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeI()
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeJ()
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeK()
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	public static class Function3D extends WB_IsoValues3D {
		private double				fxi, fyi, fzi, dfx, dfy, dfz;
		private WB_ScalarParameter	function;
		private int					sizeI, sizeJ, sizeK;

		public Function3D(final WB_ScalarParameter function, final double xi,
				final double yi, final double zi, final double dx,
				final double dy, final double dz, final int sizeI,
				final int sizeJ, final int sizeK) {
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

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#value(int, int)
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return function.evaluate(fxi + i * dfx, fyi + j * dfy,
					fzi + k * dfz);
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeI()
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeJ()
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeK()
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	public static class HashGrid3D extends WB_IsoValues3D {
		private WB_HashGridDouble3D values;

		public HashGrid3D(final WB_HashGridDouble3D values) {
			this.values = values;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues2D#value(int, int, int)
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return values.getValue(i, j, k);
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeI()
		 */
		@Override
		public int getSizeI() {
			return values.getSizeI();
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeJ()
		 */
		@Override
		public int getSizeJ() {
			return values.getSizeJ();
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeK()
		 */
		@Override
		public int getSizeK() {
			return values.getSizeK();
		}
	}

	public static class ImageStack3D extends WB_IsoValues3D {
		private String[]	images;
		private double[][]	sliceK, sliceKpo;
		private int			currentK, currentKpo;
		private int			sizeI, sizeJ, sizeK;
		private Mode		mode;
		private PApplet		home;
		private PImage		slice;

		public ImageStack3D(final String[] images, final PApplet home,
				final int sizeI, final int sizeJ, final int sizeK,
				final Mode mode) {
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

		public ImageStack3D(final String[] images, final PApplet home,
				final int sizeI, final int sizeJ, final int sizeK) {
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

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues2D#value(int, int, int)
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

		private void initialize() {
			currentK = 0;
			currentKpo = 1;
			sliceK = new double[sizeI][sizeJ];
			sliceKpo = new double[sizeI][sizeJ];
			fillSlice(currentK, sliceK);
			fillSlice(currentKpo, sliceKpo);
		}

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
				double sliceThickness = images.length / (double) sizeK;
				double startK = k * sliceThickness;
				double endK = startK + sliceThickness;
				int startKIndex = (int) startK;
				int endKIndex = (int) endK;
				double startFactor = 1.0 - startK + startKIndex;
				double endFactor = endK - endKIndex;
				for (int i = 0; i < sizeI; i++) {
					for (int j = 0; j < sizeJ; j++) {
						values[i][j] = 0.0;
					}
				}
				double cumulf = 0.0;
				for (int img = startKIndex; img < endKIndex + 1; img++) {
					double f = img == startKIndex ? startFactor
							: img == endKIndex ? endFactor : 1.0;
					if (f > 0 && img >= 0 && img < images.length) {
						cumulf += f;
						slice = home.loadImage(images[img]);
						slice.resize(sizeI, sizeJ);
						for (int i = 0; i < sizeI; i++) {
							for (int j = 0; j < sizeJ; j++) {
								values[i][j] += f
										* getSingleSliceValue(slice, i, j);
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

		private void incrementSlice() {
			currentK = currentKpo;
			currentKpo = currentKpo + 1;
			double[][] swap = sliceK;
			sliceK = sliceKpo;
			sliceKpo = swap;
			fillSlice(currentKpo, sliceKpo);
		}

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

		private double getSingleSliceValue(final PImage img, final int i,
				final int j) {
			int color = img.get(i, j);
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

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeI()
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeJ()
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeK()
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}

	public static class SubValues3D extends WB_IsoValues3D {
		int				di, dj, dk;
		int				sizeI, sizeJ, sizeK;
		WB_IsoValues3D	parent;

		public SubValues3D(final WB_IsoValues3D parent, final int i,
				final int j, final int k, final int sizeI, final int sizeJ,
				final int sizeK) {
			this.parent = parent;
			int pi = parent.getSizeI();
			if (pi == -1) {
				throw new IllegalArgumentException(
						"SubGrid3D cannot have a FunctionGrid3D as a parent.");
			}
			int pj = parent.getSizeJ();
			int pk = parent.getSizeK();
			this.di = Math.min(Math.max(0, i), pi - 1);
			this.dj = Math.min(Math.max(0, j), pj - 1);
			this.dk = Math.min(Math.max(0, k), pk - 1);
			this.sizeI = Math.max(0, Math.min(sizeI, pi - di));
			this.sizeJ = Math.max(0, Math.min(sizeJ, pj - dj));
			this.sizeK = Math.max(0, Math.min(sizeK, pk - dk));
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#value(int, int, int)
		 */
		@Override
		public double getValue(final int i, final int j, final int k) {
			return parent.getValue(i + di, j + dj, k + dk);
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeI()
		 */
		@Override
		public int getSizeI() {
			return sizeI;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeJ()
		 */
		@Override
		public int getSizeJ() {
			return sizeJ;
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_IsoValues3D#getSizeK()
		 */
		@Override
		public int getSizeK() {
			return sizeK;
		}
	}
}
