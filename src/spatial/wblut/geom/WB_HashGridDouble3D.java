/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongDoubleHashMap;

public class WB_HashGridDouble3D {

	private final LongDoubleHashMap values;

	private final double defaultValue;

	private final int sizeI, sizeJ, sizeK, sizeIJ;

	/**
	 *
	 *
	 * @param sizeI
	 * @param sizeJ
	 * @param sizeK
	 * @param defaultValue
	 */
	public WB_HashGridDouble3D(final int sizeI, final int sizeJ, final int sizeK, final double defaultValue) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		this.defaultValue = defaultValue;
		values = new LongDoubleHashMap();
	}

	/**
	 *
	 *
	 * @param sizeI
	 * @param sizeJ
	 * @param sizeK
	 */
	public WB_HashGridDouble3D(final int sizeI, final int sizeJ, final int sizeK) {
		this.sizeI = sizeI;
		this.sizeJ = sizeJ;
		this.sizeK = sizeK;
		sizeIJ = sizeI * sizeJ;
		defaultValue = 0;
		values = new LongDoubleHashMap();
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
	public boolean setValue(final double value, final int i, final int j, final int k) {
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

	/**
	 *
	 *
	 * @param value
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public boolean addValue(final double value, final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
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
	public double getValue(final int i, final int j, final int k) {
		final long id = safeIndex(i, j, k);
		if (id == -1) {
			return defaultValue;
		}
		if (id >= 0) {
			return values.get(id);
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
	public int getSizeI() {
		return sizeI;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getSizeJ() {
		return sizeJ;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getSizeK() {
		return sizeK;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getDefaultValue() {
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
