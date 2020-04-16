package wblut.geom;

import wblut.hemesh.HE_ObjectMap;

public class WB_HashGridObject3D {
	@SuppressWarnings("rawtypes")
	private final HE_ObjectMap values;
	private final Object defaultValue;
	private final int sizeI, sizeJ, sizeK, sizeIJ;

	@SuppressWarnings("rawtypes")
	public WB_HashGridObject3D(final int sizeI, final int sizeJ, final int sizeK, final Object defaultValue) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		this.defaultValue = defaultValue;
		values = new HE_ObjectMap();
	}

	@SuppressWarnings("rawtypes")
	public WB_HashGridObject3D(final int sizeI, final int sizeJ, final int sizeK) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		defaultValue = null;
		values = new HE_ObjectMap();
	}

	@SuppressWarnings("unchecked")
	public boolean setValue(final Object value, final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id > 0) {
			values.put(id, value);
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

	public Object getValue(final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id == -1) {
			return defaultValue;
		}
		if (id >= 0) {
			final Object val = values.get(id);
			return val;
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

	public int getWidth() {
		return sizeI;
	}

	public int getHeight() {
		return sizeJ;
	}

	public int getDepth() {
		return sizeK;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public long[] getKeys() {
		return values.keySet().toArray();
	}

	public int size() {
		return values.size();
	}
}
