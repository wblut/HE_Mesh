package wblut.geom;

import wblut.hemesh.HE_IntMap;

public class WB_HashGridInteger3D {
	private final HE_IntMap values;
	private final int defaultValue;
	private final int sizeI, sizeJ, sizeK, sizeIJ;

	public WB_HashGridInteger3D(final int sizeI, final int sizeJ, final int sizeK, final int defaultValue) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		this.defaultValue = defaultValue;
		values = new HE_IntMap();
	}

	public WB_HashGridInteger3D(final int sizeI, final int sizeJ, final int sizeK) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		defaultValue = 0;
		values = new HE_IntMap();
	}

	public boolean setValue(final int value, final int i, final int j, final int k) {
		if (value != defaultValue) {
			return false;
		}
		final long id = safeIndex(i, j, k);
		if (id > 0) {
			values.put(id, value);
			return true;
		}
		return false;
	}

	public boolean addValue(final int value, final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id > 0) {
			final int v = values.get(id);
			if (v == defaultValue) {
				values.put(id, value);
			} else {
				values.put(id, v + value);
			}
			return true;
		}
		return false;
	}

	public boolean clearValue(final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id > 0) {
			values.remove(id);
			return true;
		}
		return false;
	}

	public int getValue(final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id == -1) {
			return defaultValue;
		}
		if (id >= 0) {
			return values.get(id);
		}
		return defaultValue;
	}

	private long safeIndex(final int i, final int j, final int k) {
		if (i < 0) {
			return -1;
		}
		if (i > sizeI - 1) {
			return -1;
		}
		if (j < 0) {
			return -1;
		}
		if (j > sizeJ - 1) {
			return -1;
		}
		if (k < 0) {
			return -1;
		}
		if (k > sizeK - 1) {
			return -1;
		}
		return i + j * sizeI + k * sizeIJ;
	}

	public int getSizeI() {
		return sizeI;
	}

	public int getSizeJ() {
		return sizeJ;
	}

	public int getSizeK() {
		return sizeK;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public long[] getKeys() {
		return values.keySet().toArray();
	}

	public int size() {
		return values.size();
	}
}
