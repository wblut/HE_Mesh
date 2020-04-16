package wblut.math;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class WB_MTRandom implements Serializable, Cloneable {
	private static final long serialVersionUID = 3636987267914792302L;
	private static final int N = 624;
	private static final int M = 397;
	private static final int MATRIX_A = 0x9908b0df;
	private static final int UPPER_MASK = 0x80000000;
	private static final int LOWER_MASK = 0x7fffffff;
	private static final int TEMPERING_MASK_B = 0x9d2c5680;
	private static final int TEMPERING_MASK_C = 0xefc60000;
	private int mt[];
	private int mti;
	private int mag01[];
	// private static final long GOOD_SEED = 4357;
	private double __nextNextGaussian;
	private boolean __haveNextNextGaussian;
	private long seed;

	@Override
	public Object clone() throws CloneNotSupportedException {
		final WB_MTRandom f = (WB_MTRandom) super.clone();
		f.mt = mt.clone();
		f.mag01 = mag01.clone();
		return f;
	}

	public boolean stateEquals(final Object o) {
		if (o == this) {
			return true;
		}
		if (o == null || !(o instanceof WB_MTRandom)) {
			return false;
		}
		final WB_MTRandom other = (WB_MTRandom) o;
		if (mti != other.mti) {
			return false;
		}
		for (int x = 0; x < mag01.length; x++) {
			if (mag01[x] != other.mag01[x]) {
				return false;
			}
		}
		for (int x = 0; x < mt.length; x++) {
			if (mt[x] != other.mt[x]) {
				return false;
			}
		}
		return true;
	}

	public void readState(final DataInputStream stream) throws IOException {
		int len = mt.length;
		for (int x = 0; x < len; x++) {
			mt[x] = stream.readInt();
		}
		len = mag01.length;
		for (int x = 0; x < len; x++) {
			mag01[x] = stream.readInt();
		}
		mti = stream.readInt();
		__nextNextGaussian = stream.readDouble();
		__haveNextNextGaussian = stream.readBoolean();
	}

	public void writeState(final DataOutputStream stream) throws IOException {
		int len = mt.length;
		for (int x = 0; x < len; x++) {
			stream.writeInt(mt[x]);
		}
		len = mag01.length;
		for (int x = 0; x < len; x++) {
			stream.writeInt(mag01[x]);
		}
		stream.writeInt(mti);
		stream.writeDouble(__nextNextGaussian);
		stream.writeBoolean(__haveNextNextGaussian);
	}

	public WB_MTRandom() {
		this(System.currentTimeMillis());
	}

	public WB_MTRandom(final long seed) {
		setSeed(seed);
	}

	synchronized public void setSeed(final long seed) {
		// Due to a bug in java.util.Random clear up to 1.2, we're
		// doing our own Gaussian variable.
		this.seed = seed;
		__haveNextNextGaussian = false;
		mt = new int[N];
		mag01 = new int[2];
		mag01[0] = 0x0;
		mag01[1] = MATRIX_A;
		mt[0] = (int) (seed & 0xffffffff);
		for (mti = 1; mti < N; mti++) {
			mt[mti] = 1812433253 * (mt[mti - 1] ^ mt[mti - 1] >>> 30) + mti;
			mt[mti] &= 0xffffffff;
		}
	}

	synchronized public void reset() {
		setSeed(seed);
	}

	public final int nextInt() {
		int y;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return y;
	}

	public final short nextShort() {
		int y;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return (short) (y >>> 16);
	}

	public final char nextChar() {
		int y;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return (char) (y >>> 16);
	}

	public final boolean nextBoolean() {
		int y;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return y >>> 31 != 0;
	}

	public final boolean nextBoolean(final float probability) {
		int y;
		if (probability < 0.0f || probability > 1.0f) {
			throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
		}
		if (probability == 0.0f) {
			return false; // fix half-open issues
		} else if (probability == 1.0f) {
			return true; // fix half-open issues
		}
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return (y >>> 8) / (float) (1 << 24) < probability;
	}

	public final boolean nextBoolean(final double probability) {
		int y;
		int z;
		if (probability < 0.0 || probability > 1.0) {
			throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
		}
		if (probability == 0.0) {
			return false; // fix half-open issues
		} else if (probability == 1.0) {
			return true; // fix half-open issues
		}
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ z >>> 1 ^ mag01[z & 0x1];
			}
			for (; kk < N - 1; kk++) {
				z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ z >>> 1 ^ mag01[z & 0x1];
			}
			z = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ z >>> 1 ^ mag01[z & 0x1];
			mti = 0;
		}
		z = mt[mti++];
		z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
		z ^= z << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
		z ^= z << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
		z ^= z >>> 18; // TEMPERING_SHIFT_L(z)
		return (((long) (y >>> 6) << 27) + (z >>> 5)) / (double) (1L << 53) < probability;
	}

	public final byte nextByte() {
		int y;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return (byte) (y >>> 24);
	}

	public final void nextBytes(final byte[] bytes) {
		int y;
		for (int x = 0; x < bytes.length; x++) {
			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster
				for (kk = 0; kk < N - M; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				for (; kk < N - 1; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
				mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
				mti = 0;
			}
			y = mt[mti++];
			y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
			y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
			y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
			y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
			bytes[x] = (byte) (y >>> 24);
		}
	}

	public final long nextLong() {
		int y;
		int z;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ z >>> 1 ^ mag01[z & 0x1];
			}
			for (; kk < N - 1; kk++) {
				z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ z >>> 1 ^ mag01[z & 0x1];
			}
			z = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ z >>> 1 ^ mag01[z & 0x1];
			mti = 0;
		}
		z = mt[mti++];
		z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
		z ^= z << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
		z ^= z << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
		z ^= z >>> 18; // TEMPERING_SHIFT_L(z)
		return ((long) y << 32) + z;
	}

	public final long nextLong(final long n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n must be > 0");
		}
		long bits, val;
		do {
			int y;
			int z;
			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster
				for (kk = 0; kk < N - M; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				for (; kk < N - 1; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
				mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
				mti = 0;
			}
			y = mt[mti++];
			y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
			y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
			y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
			y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster
				for (kk = 0; kk < N - M; kk++) {
					z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M] ^ z >>> 1 ^ mag01[z & 0x1];
				}
				for (; kk < N - 1; kk++) {
					z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M - N] ^ z >>> 1 ^ mag01[z & 0x1];
				}
				z = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
				mt[N - 1] = mt[M - 1] ^ z >>> 1 ^ mag01[z & 0x1];
				mti = 0;
			}
			z = mt[mti++];
			z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
			z ^= z << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
			z ^= z << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
			z ^= z >>> 18; // TEMPERING_SHIFT_L(z)
			bits = ((long) y << 32) + z >>> 1;
			val = bits % n;
		} while (bits - val + n - 1 < 0);
		return val;
	}

	public final double nextDouble() {
		int y;
		int z;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ z >>> 1 ^ mag01[z & 0x1];
			}
			for (; kk < N - 1; kk++) {
				z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ z >>> 1 ^ mag01[z & 0x1];
			}
			z = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ z >>> 1 ^ mag01[z & 0x1];
			mti = 0;
		}
		z = mt[mti++];
		z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
		z ^= z << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
		z ^= z << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
		z ^= z >>> 18; // TEMPERING_SHIFT_L(z)
		return (((long) (y >>> 6) << 27) + (z >>> 5)) / (double) (1L << 53);
	}

	public final double nextCenteredDouble() {
		return nextDouble() - 0.5;
	}

	public final double nextGaussian() {
		if (__haveNextNextGaussian) {
			__haveNextNextGaussian = false;
			return __nextNextGaussian;
		} else {
			double v1, v2, s;
			do {
				int y;
				int z;
				int a;
				int b;
				if (mti >= N) // generate N words at one time
				{
					int kk;
					final int[] mt = this.mt; // locals are slightly faster
					final int[] mag01 = this.mag01; // locals are slightly
					// faster
					for (kk = 0; kk < N - M; kk++) {
						y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
					}
					for (; kk < N - 1; kk++) {
						y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
					}
					y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
					mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
					mti = 0;
				}
				y = mt[mti++];
				y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
				y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
				y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
				y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
				if (mti >= N) // generate N words at one time
				{
					int kk;
					final int[] mt = this.mt; // locals are slightly faster
					final int[] mag01 = this.mag01; // locals are slightly
					// faster
					for (kk = 0; kk < N - M; kk++) {
						z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M] ^ z >>> 1 ^ mag01[z & 0x1];
					}
					for (; kk < N - 1; kk++) {
						z = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M - N] ^ z >>> 1 ^ mag01[z & 0x1];
					}
					z = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
					mt[N - 1] = mt[M - 1] ^ z >>> 1 ^ mag01[z & 0x1];
					mti = 0;
				}
				z = mt[mti++];
				z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
				z ^= z << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
				z ^= z << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
				z ^= z >>> 18; // TEMPERING_SHIFT_L(z)
				if (mti >= N) // generate N words at one time
				{
					int kk;
					final int[] mt = this.mt; // locals are slightly faster
					final int[] mag01 = this.mag01; // locals are slightly
					// faster
					for (kk = 0; kk < N - M; kk++) {
						a = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M] ^ a >>> 1 ^ mag01[a & 0x1];
					}
					for (; kk < N - 1; kk++) {
						a = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M - N] ^ a >>> 1 ^ mag01[a & 0x1];
					}
					a = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
					mt[N - 1] = mt[M - 1] ^ a >>> 1 ^ mag01[a & 0x1];
					mti = 0;
				}
				a = mt[mti++];
				a ^= a >>> 11; // TEMPERING_SHIFT_U(a)
				a ^= a << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(a)
				a ^= a << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(a)
				a ^= a >>> 18; // TEMPERING_SHIFT_L(a)
				if (mti >= N) // generate N words at one time
				{
					int kk;
					final int[] mt = this.mt; // locals are slightly faster
					final int[] mag01 = this.mag01; // locals are slightly
					// faster
					for (kk = 0; kk < N - M; kk++) {
						b = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M] ^ b >>> 1 ^ mag01[b & 0x1];
					}
					for (; kk < N - 1; kk++) {
						b = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
						mt[kk] = mt[kk + M - N] ^ b >>> 1 ^ mag01[b & 0x1];
					}
					b = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
					mt[N - 1] = mt[M - 1] ^ b >>> 1 ^ mag01[b & 0x1];
					mti = 0;
				}
				b = mt[mti++];
				b ^= b >>> 11; // TEMPERING_SHIFT_U(b)
				b ^= b << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(b)
				b ^= b << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(b)
				b ^= b >>> 18; // TEMPERING_SHIFT_L(b)
				v1 = 2 * ((((long) (y >>> 6) << 27) + (z >>> 5)) / (double) (1L << 53)) - 1;
				v2 = 2 * ((((long) (a >>> 6) << 27) + (b >>> 5)) / (double) (1L << 53)) - 1;
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			final double multiplier = Math.sqrt(-2 * Math.log(s) / s);
			__nextNextGaussian = v2 * multiplier;
			__haveNextNextGaussian = true;
			return v1 * multiplier;
		}
	}

	public final float nextFloat() {
		int y;
		if (mti >= N) // generate N words at one time
		{
			int kk;
			final int[] mt = this.mt; // locals are slightly faster
			final int[] mag01 = this.mag01; // locals are slightly faster
			for (kk = 0; kk < N - M; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
				mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
			}
			y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
			mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
			mti = 0;
		}
		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
		return (y >>> 8) / (float) (1 << 24);
	}

	public final float nextCenteredFloat() {
		return nextFloat() - 0.5f;
	}

	public final int nextInt(final int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n must be > 0");
		}
		if ((n & -n) == n) // i.e., n is a power of 2
		{
			int y;
			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster
				for (kk = 0; kk < N - M; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				for (; kk < N - 1; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
				mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
				mti = 0;
			}
			y = mt[mti++];
			y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
			y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
			y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
			y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
			return (int) (n * (long) (y >>> 1) >> 31);
		}
		int bits, val;
		do {
			int y;
			if (mti >= N) // generate N words at one time
			{
				int kk;
				final int[] mt = this.mt; // locals are slightly faster
				final int[] mag01 = this.mag01; // locals are slightly faster
				for (kk = 0; kk < N - M; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				for (; kk < N - 1; kk++) {
					y = mt[kk] & UPPER_MASK | mt[kk + 1] & LOWER_MASK;
					mt[kk] = mt[kk + M - N] ^ y >>> 1 ^ mag01[y & 0x1];
				}
				y = mt[N - 1] & UPPER_MASK | mt[0] & LOWER_MASK;
				mt[N - 1] = mt[M - 1] ^ y >>> 1 ^ mag01[y & 0x1];
				mti = 0;
			}
			y = mt[mti++];
			y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
			y ^= y << 7 & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
			y ^= y << 15 & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
			y ^= y >>> 18; // TEMPERING_SHIFT_L(y)
			bits = y >>> 1;
			val = bits % n;
		} while (bits - val + n - 1 < 0);
		return val;
	}
}
