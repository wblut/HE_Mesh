/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

public class WB_HashGridObject3D {

	@SuppressWarnings("rawtypes")
	private final LongObjectHashMap values;

	private final Object defaultValue;

	private final int sizeI, sizeJ, sizeK, sizeIJ;

	/**
	 *
	 *
	 * @param sizeI
	 * @param sizeJ
	 * @param sizeK
	 * @param defaultValue
	 */
	@SuppressWarnings("rawtypes")
	public WB_HashGridObject3D(final int sizeI, final int sizeJ, final int sizeK, final Object defaultValue) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		this.defaultValue = defaultValue;
		values = new LongObjectHashMap();
	}

	/**
	 *
	 *
	 * @param sizeI
	 * @param sizeJ
	 * @param sizeK
	 */
	@SuppressWarnings("rawtypes")
	public WB_HashGridObject3D(final int sizeI, final int sizeJ, final int sizeK) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		defaultValue = null;
		values = new LongObjectHashMap();
	}

	/**
	 *
	 *
	 * @param value
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean setValue(final Object value, final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id > 0) {
			values.put(id, value);
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public boolean clearValue(final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id > 0) {
			values.remove(id);
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
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

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	public int getWidth() {
		return sizeI;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getHeight() {
		return sizeJ;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getDepth() {
		return sizeK;
	}

	/**
	 *
	 *
	 * @return
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 *
	 *
	 * @return
	 */
	public long[] getKeys() {
		return values.keySet().toArray();
	}

	/**
	 *
	 *
	 * @return
	 */
	public int size() {
		return values.size();
	}
}
