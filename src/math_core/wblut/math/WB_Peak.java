package wblut.math;

/**
 *
 */
public abstract class WB_Peak {
	/**  */
	double power;

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Peak getPeakAbs(final double p) {
		return new PeakAbs(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Peak getPeakCos(final double p) {
		return new PeakCos(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Peak getPeakSin(final double p) {
		return new PeakSin(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Peak getPeakMin(final double p) {
		return new PeakMin(p);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public static WB_Peak getPeakMax(final double p) {
		return new PeakMax(p);
	}

	/**
	 *
	 */
	private WB_Peak() {
	}

	/**
	 *
	 *
	 * @param p
	 */
	private WB_Peak(final double p) {
		power = p;
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public abstract double getValue(double x);

	/**
	 *
	 *
	 * @param p
	 */
	public void setPower(final double p) {
		power = p;
	}

	/**
	 *
	 */
	static class PeakAbs extends WB_Peak {
		/**
		 *
		 *
		 * @param p
		 */
		PeakAbs(final double p) {
			super(p);
		}

		/**
		 *
		 *
		 * @param x
		 * @return
		 */
		@Override
		public double getValue(final double x) {
			return 1.0 - Math.pow(Math.abs(x), power);
		}
	}

	/**
	 *
	 */
	static class PeakCos extends WB_Peak {
		/**
		 *
		 *
		 * @param p
		 */
		PeakCos(final double p) {
			super(p);
		}

		/**
		 *
		 *
		 * @param x
		 * @return
		 */
		@Override
		public double getValue(final double x) {
			return Math.pow(Math.cos(0.5 * x * Math.PI), power);
		}
	}

	/**
	 *
	 */
	static class PeakSin extends WB_Peak {
		/**
		 *
		 *
		 * @param p
		 */
		PeakSin(final double p) {
			super(p);
		}

		/**
		 *
		 *
		 * @param x
		 * @return
		 */
		@Override
		public double getValue(final double x) {
			return 1.0 - Math.pow(Math.abs(Math.sin(0.5 * x * Math.PI)), power);
		}
	}

	/**
	 *
	 */
	static class PeakMin extends WB_Peak {
		/**
		 *
		 *
		 * @param p
		 */
		PeakMin(final double p) {
			super(p);
		}

		/**
		 *
		 *
		 * @param x
		 * @return
		 */
		@Override
		public double getValue(final double x) {
			return Math.pow(Math.min(Math.cos(0.5 * x * Math.PI), 1.0 - Math.abs(x)), power);
		}
	}

	/**
	 *
	 */
	static class PeakMax extends WB_Peak {
		/**
		 *
		 *
		 * @param p
		 */
		PeakMax(final double p) {
			super(p);
		}

		/**
		 *
		 *
		 * @param x
		 * @return
		 */
		@Override
		public double getValue(final double x) {
			return 1.0 - Math.pow(Math.max(0.0, 2.0 * Math.abs(x) - 1.0), power);
		}
	}
}
