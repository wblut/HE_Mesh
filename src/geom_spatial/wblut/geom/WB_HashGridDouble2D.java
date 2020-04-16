package wblut.geom;

import wblut.hemesh.HE_DoubleMap;

public class WB_HashGridDouble2D {
	private final HE_DoubleMap values;
	private final double defaultValue;
	private final int K, L;

	public WB_HashGridDouble2D(final int K, final int L, final double defaultValue) {
		this.K = K;
		this.L = L;
		this.defaultValue = defaultValue;
		values = new HE_DoubleMap();
	}

	public WB_HashGridDouble2D(final int K, final int L) {
		this.K = K;
		this.L = L;
		defaultValue = 0;
		values = new HE_DoubleMap();
	}

	public boolean setValue(final double value, final int i, final int j) {
		if (value != defaultValue) {
			return false;
		}
		final long id = safeIndex(i, j);
		if (id >= 0) {
			values.put(id, value);
			return true;
		}
		return false;
	}

	public boolean addValue(final double value, final int i, final int j) {
		final long id = safeIndex(i, j);
		if (id > 0) {
			final double v = values.get(id);
			if (v == defaultValue) {
				values.put(id, value);
			} else {
				values.put(id, v + value);
			}
			return true;
		}
		return false;
	}

	public boolean clearValue(final int i, final int j) {
		final long id = safeIndex(i, j);
		if (id > 0) {
			values.remove(id);
			return true;
		}
		return false;
	}

	public double getValue(final int i, final int j) {
		final long id = safeIndex(i, j);
		if (id == -1) {
			return defaultValue;
		}
		if (id > 0) {
			return values.get(id);
		}
		return defaultValue;
	}

	private long safeIndex(final int i, final int j) {
		if (i < 0) {
			return -1;
		}
		if (i > K - 1) {
			return -1;
		}
		if (j < 0) {
			return -1;
		}
		if (j > L - 1) {
			return -1;
		}
		return i + j * K;
	}

	public int getWidth() {
		return K;
	}

	public int getHeight() {
		return L;
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public long[] getKeys() {
		return values.keySet().toArray();
	}

	public int size() {
		return values.size();
	}
}
