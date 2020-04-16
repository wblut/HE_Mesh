package wblut.geom;

import processing.core.PApplet;
import processing.core.PImage;
import wblut.math.WB_ScalarParameter;

public abstract class WB_IsoValues2D {
	public enum Mode {
		RED, GREEN, BLUE, HUE, SAT, BRI, ALPHA
	}

	public abstract double value(int i, int j);

	public abstract int getWidth();

	public abstract int getHeight();

	public WB_BinaryGrid2D getBinaryGrid2D(final double threshold) {
		final WB_BinaryGrid2D grid = WB_BinaryGrid2D.createGrid(new WB_Point(), getWidth(), 1.0, getHeight(), 1.0);
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				if (value(i, j) >= threshold) {
					grid.set(i, j);
				}
			}
		}
		return grid;
	}

	public static class KSlice2D extends WB_IsoValues2D {
		private final WB_IsoValues3D values;
		private final int width, height;
		private final int k;

		public KSlice2D(final WB_IsoValues3D values, final int k) {
			this.values = values;
			this.k = k;
			width = values.getSizeI();
			height = values.getSizeJ();
		}

		@Override
		public double value(final int i, final int j) {
			return values.getValue(i, j, k);
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class ISlice2D extends WB_IsoValues2D {
		private final WB_IsoValues3D values;
		private final int width, height;
		private final int i;

		public ISlice2D(final WB_IsoValues3D values, final int i) {
			this.values = values;
			this.i = i;
			width = values.getSizeJ();
			height = values.getSizeK();
		}

		@Override
		public double value(final int j, final int k) {
			return values.getValue(i, j, k);
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class JSlice2D extends WB_IsoValues2D {
		private final WB_IsoValues3D values;
		private final int width, height;
		private final int j;

		public JSlice2D(final WB_IsoValues3D values, final int j) {
			this.values = values;
			this.j = j;
			width = values.getSizeK();
			height = values.getSizeI();
		}

		@Override
		public double value(final int k, final int i) {
			return values.getValue(i, j, k);
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class Grid2D extends WB_IsoValues2D {
		private final double[][] values;
		private final int width, height;

		public Grid2D(final double[][] values) {
			width = values.length;
			height = values.length > 0 ? values[0].length : 0;
			this.values = new double[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					this.values[i][j] = values[i][j];
				}
			}
		}

		public Grid2D(final float[][] values) {
			width = values.length;
			height = values.length > 0 ? values[0].length : 0;
			this.values = new double[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					this.values[i][j] = values[i][j];
				}
			}
		}

		@Override
		public double value(final int i, final int j) {
			return values[i][j];
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class GridRaw2D extends WB_IsoValues2D {
		private final double[][] values;
		private final int width, height;

		public GridRaw2D(final double[][] values) {
			this.values = values;
			width = values.length;
			height = values.length > 0 ? values[0].length : 0;
		}

		@Override
		public double value(final int i, final int j) {
			return values[i][j];
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class Function2D extends WB_IsoValues2D {
		private final double fxi, fyi, dfx, dfy;
		private final int width, height;
		private WB_ScalarParameter function;

		public Function2D(final WB_ScalarParameter function, final double xi, final double yi, final double dx,
				final double dy, final int width, final int height) {
			this.function = function;
			fxi = xi;
			fyi = yi;
			dfx = dx;
			dfy = dy;
			this.function = function;
			this.width = width;
			this.height = height;
		}

		@Override
		public double value(final int i, final int j) {
			return function.evaluate(fxi + i * dfx, fyi + j * dfy);
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class HashGrid2D extends WB_IsoValues2D {
		private final WB_HashGridDouble2D values;
		private final int width, height;

		public HashGrid2D(final WB_HashGridDouble2D values) {
			this.values = values;
			width = values.getWidth();
			height = values.getHeight();
		}

		@Override
		public double value(final int i, final int j) {
			return values.getValue(i, j);
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}

	public static class ImageGrid2D extends WB_IsoValues2D {
		private final PImage image;
		private final Mode mode;
		private final PApplet home;

		public ImageGrid2D(final String path, final PApplet home, final int width, final int height) {
			this.image = home.loadImage(path);
			image.resize(width, height);
			mode = Mode.BRI;
			this.home = home;
		}

		public ImageGrid2D(final String path, final PApplet home) {
			image = home.loadImage(path);
			mode = Mode.BRI;
			this.home = home;
		}

		public ImageGrid2D(final String path, final PApplet home, final int width, final int height, final Mode mode) {
			this.image = home.loadImage(path);
			image.resize(width, height);
			this.mode = mode;
			this.home = home;
		}

		public ImageGrid2D(final String path, final PApplet home, final Mode mode) {
			image = home.loadImage(path);
			this.mode = mode;
			this.home = home;
		}

		public ImageGrid2D(final PImage image, final PApplet home, final int width, final int height) {
			// PGraphics is a PImage but does not support resize(). Making a
			// copy with get() solves this potential problem.
			this.image = image.get();
			this.image.resize(width, height);
			mode = Mode.BRI;
			this.home = home;
		}

		public ImageGrid2D(final PImage image, final PApplet home) {
			this.image = image;
			mode = Mode.BRI;
			this.home = home;
		}

		public ImageGrid2D(final PImage image, final PApplet home, final int width, final int height, final Mode mode) {
			// PGraphics is a PImage but does not support resize(). Making a
			// copy with get() solves this potential problem.
			this.image = image.get();
			this.image.resize(width, height);
			this.mode = mode;
			this.home = home;
		}

		public ImageGrid2D(final PImage image, final PApplet home, final Mode mode) {
			this.image = image;
			this.mode = mode;
			this.home = home;
		}

		@Override
		public double value(final int i, final int j) {
			final int color = image.get(i, j);
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

		@Override
		public int getWidth() {
			return image.width;
		}

		@Override
		public int getHeight() {
			return image.height;
		}
	}
}
