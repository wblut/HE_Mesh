/*
 * Colormap from matplotlib
 * https://github.com/matplotlib/
 */

package wblut.processing;

/**
 * @author FVH
 *
 */

public interface WB_ColorMap {
	public int getColor(final double f);

	static abstract class AbstractColorMap implements WB_ColorMap {
		int color(int v1, int v2, int v3) {
			if (v1 > 255) {
				v1 = 255;
			} else if (v1 < 0) {
				v1 = 0;
			}
			if (v2 > 255) {
				v2 = 255;
			} else if (v2 < 0) {
				v2 = 0;
			}
			if (v3 > 255) {
				v3 = 255;
			} else if (v3 < 0) {
				v3 = 0;
			}
			return 0xff000000 | v1 << 16 | v2 << 8 | v3;
		}

		int normColor(final double v1, final double v2, final double v3) {
			return color((int) (256 * v1), (int) (256 * v2), (int) (256 * v3));
		}

		double lookup(final double x, final double[][] values) {
			int i = 1;
			while (values[i][0] < x) {
				i = i + 1;
			}
			i = i - 1;
			double c1 = values[i][1];
			double c2 = values[i + 1][1];
			double scaling = (x - values[i][0]) / Math.abs(values[i][0] - values[i + 1][0]);
			return c1 + (c2 - c1) * scaling;
		}

		// Gnuplot palette functions

		double gfunc0(final double x) {
			return 0;
		}

		double gfunc1(final double x) {
			return 0;
		}

		double gfunc2(final double x) {
			return 1;
		}

		double gfunc3(final double x) {
			return x;
		}

		double gfunc4(final double x) {
			return x * x;
		}

		double gfunc5(final double x) {
			return x * x * x;
		}

		double gfunc6(final double x) {
			return x * x * x * x;
		}

		double gfunc7(final double x) {
			return Math.sqrt(x);
		}

		double gfunc8(final double x) {
			return Math.sqrt(Math.sqrt(x));
		}

		double gfunc9(final double x) {
			return Math.sin(x * Math.PI * 0.5);
		}

		double gfunc10(final double x) {
			return Math.cos(x * Math.PI * 0.5);
		}

		double gfunc11(final double x) {
			return Math.abs(x - 0.5);
		}

		double gfunc12(final double x) {
			return (2.0 * x - 1.0) * (2.0 * x - 1.0);
		}

		double gfunc13(final double x) {
			return Math.sin(x * Math.PI);
		}

		double gfunc14(final double x) {
			return Math.abs(Math.cos(x * Math.PI));
		}

		double gfunc15(final double x) {
			return Math.sin(2.0 * x * Math.PI);
		}

		double gfunc16(final double x) {
			return Math.cos(2.0 * x * Math.PI);
		}

		double gfunc17(final double x) {
			return Math.abs(Math.sin(2.0 * x * Math.PI));
		}

		double gfunc18(final double x) {
			return Math.abs(Math.cos(2.0 * x * Math.PI));
		}

		double gfunc19(final double x) {
			return Math.abs(Math.sin(4.0 * x * Math.PI));
		}

		double gfunc20(final double x) {
			return Math.abs(Math.cos(4.0 * x * Math.PI));
		}

		double gfunc21(final double x) {
			return 3.0 * x;
		}

		double gfunc22(final double x) {
			return 3.0 * x - 1.0;
		}

		double gfunc23(final double x) {
			return 3.0 * x - 2.0;
		}

		double gfunc24(final double x) {
			return Math.abs(3.0 * x - 1.0);
		}

		double gfunc25(final double x) {
			return Math.abs(3.0 * x - 2.0);
		}

		double gfunc26(final double x) {
			return (3.0 * x - 1.0) * 0.5;
		}

		double gfunc27(final double x) {
			return (3.0 * x - 2.0) * 0.5;
		}

		double gfunc28(final double x) {
			return Math.abs(3.0 * x - 1.0) * 0.5;
		}

		double gfunc29(final double x) {
			return Math.abs(3.0 * x - 2.0) * 0.5;
		}

		double gfunc30(final double x) {
			return x / 0.32 - 0.78125;
		}

		double gfunc31(final double x) {
			return 2.0 * x - 0.84;
		}

		double gfunc32(final double x) {
			if (x < 0.25) {
				return 4 * x;
			}
			if (x >= 0.92) {
				return x / 0.08 - 11.5;
			}
			return -2.0 * x + 1.84;
		}

		double gfunc33(final double x) {
			return Math.abs(2.0 * x - 0.5);
		}

		double gfunc34(final double x) {
			return 2.0 * x;
		}

		double gfunc35(final double x) {
			return 2.0 * x - 0.5;
		}

		double gfunc36(final double x) {
			return 2.0 * x - 1.0;
		}
	}

	public static class Binary extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 1. }, { 1., 0. } };
		double[][] green = new double[][] { { 0., 1. }, { 1., 0. } };
		double[][] blue = new double[][] { { 0., 1. }, { 1., 0. } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Autumn extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 1. }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 0. }, { 1., 1. } };
		double[][] blue = new double[][] { { 0., 0. }, { 1., 0. } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Winter extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 0. }, { 1., 0. } };
		double[][] green = new double[][] { { 0., 0. }, { 1., 1. } };
		double[][] blue = new double[][] { { 0., 1. }, { 1., 0.5 } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Spring extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 1. }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 0. }, { 1., 1. } };
		double[][] blue = new double[][] { { 0., 1. }, { 1., 0. } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Summer extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 0. }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 0.5 }, { 1., 1. } };
		double[][] blue = new double[][] { { 0., 0.4 }, { 1., 0.4 } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Bone extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 0. }, { 0.746032, 0.652778 }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 0. }, { 0.365079, 0.319444 }, { 0.746032, 0.777778 }, { 1.0, 1.0 } };
		double[][] blue = new double[][] { { 0., 0. }, { 0.365079, 0.444444 }, { 1., 0. } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Cool extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 0. }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 1. }, { 1.0, 0.0 } };
		double[][] blue = new double[][] { { 0., 1. }, { 1., 1. } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Copper extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 0. }, { 0.809524, 1.0 }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 0. }, { 1.0, 0.7812 } };
		double[][] blue = new double[][] { { 0., 0. }, { 1.0, 0.4975 } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

	public static class Flag extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(0.75 * Math.sin((f * 31.5 + 0.25) * Math.PI) + 0.5, Math.sin(f * 31.5 * Math.PI),
					0.75 * Math.sin((f * 31.5 - 0.25) * Math.PI) + 0.5);
		}
	}

	public static class Prism extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(0.75 * Math.sin((f * 20.9 + 0.25) * Math.PI) + 0.67,
					0.75 * Math.sin((f * 20.9 - 0.25) * Math.PI) + 0.33, -1.1 * Math.sin(f * 20.9 * Math.PI));
		}
	}

	public static class GnuPlot extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(gfunc7(f), gfunc5(f), gfunc15(f));
		}
	}

	public static class GnuPlot2 extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(gfunc30(f), gfunc31(f), gfunc32(f));
		}
	}

	public static class Ocean extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(gfunc23(f), gfunc28(f), gfunc3(f));
		}
	}

	public static class Afmhot extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(gfunc34(f), gfunc35(f), gfunc36(f));
		}
	}

	public static class Rainbow extends AbstractColorMap {
		@Override
		public int getColor(final double f) {
			return normColor(gfunc33(f), gfunc13(f), gfunc10(f));
		}
	}

	public static class Seismic extends AbstractColorMap {
		double[][] red = new double[][] { { 0., 0. }, { 0.809524, 1.0 }, { 1., 1. } };
		double[][] green = new double[][] { { 0., 0. }, { 1.0, 0.7812 } };
		double[][] blue = new double[][] { { 0., 0. }, { 1.0, 0.4975 } };

		@Override
		public int getColor(final double f) {
			return normColor(lookup(f, red), lookup(f, green), lookup(f, blue));
		}
	}

}
