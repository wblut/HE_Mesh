package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongDoubleHashMap;

/**
 *
 */
public class HE_DoubleMap extends LongDoubleHashMap {
	/**
	 *
	 */
	public HE_DoubleMap() {
		super();
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void put(final HE_Element el, final double value) {
		put(el.getKey(), value);
	}

	/**
	 *
	 *
	 * @param el
	 * @param defaultValue
	 * @return
	 */
	public double getIfAbsent(final HE_Element el, final double defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public double get(final HE_Element el) {
		return get(el.getKey());
	}
}
