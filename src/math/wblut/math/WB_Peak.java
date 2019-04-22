/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

public abstract class WB_Peak {
	double power;

	public static WB_Peak getPeakAbs(final double p) {
		return new PeakAbs(p);
	}

	public static WB_Peak getPeakCos(final double p) {
		return new PeakCos(p);
	}

	public static WB_Peak getPeakSin(final double p) {
		return new PeakSin(p);
	}

	public static WB_Peak getPeakMin(final double p) {
		return new PeakMin(p);
	}

	public static WB_Peak getPeakMax(final double p) {
		return new PeakMax(p);
	}

	private WB_Peak() {

	}

	private WB_Peak(final double p) {
		power = p;
	}

	public abstract double getValue(double x);

	public void setPower(final double p) {
		power = p;
	}

	static class PeakAbs extends WB_Peak {

		PeakAbs(final double p) {
			super(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.math.WB_Peak#getValue(double)
		 */
		@Override
		public double getValue(final double x) {
			return 1.0 - Math.pow(Math.abs(x), power);
		}
	}

	static class PeakCos extends WB_Peak {

		PeakCos(final double p) {
			super(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.math.WB_Peak#getValue(double)
		 */
		@Override
		public double getValue(final double x) {
			return Math.pow(Math.cos(0.5 * x * Math.PI), power);
		}
	}

	static class PeakSin extends WB_Peak {

		PeakSin(final double p) {
			super(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.math.WB_Peak#getValue(double)
		 */
		@Override
		public double getValue(final double x) {
			return 1.0 - Math.pow(Math.abs(Math.sin(0.5 * x * Math.PI)), power);
		}
	}

	static class PeakMin extends WB_Peak {

		PeakMin(final double p) {
			super(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.math.WB_Peak#getValue(double)
		 */
		@Override
		public double getValue(final double x) {
			return Math.pow(Math.min(Math.cos(0.5 * x * Math.PI), 1.0 - Math.abs(x)), power);
		}
	}

	static class PeakMax extends WB_Peak {

		PeakMax(final double p) {
			super(p);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.math.WB_Peak#getValue(double)
		 */
		@Override
		public double getValue(final double x) {
			return 1.0 - Math.pow(Math.max(0.0, 2.0 * Math.abs(x) - 1.0), power);
		}
	}
}
