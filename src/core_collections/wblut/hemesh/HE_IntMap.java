package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

/**
 *
 */
public class HE_IntMap extends LongIntHashMap {
	/**
	 *
	 */
	public HE_IntMap() {
		super();
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void put(final HE_Element el, final int value) {
		put(el.getKey(), value);
	}

	/**
	 *
	 *
	 * @param el
	 * @param defaultValue
	 * @return
	 */
	public int getIfAbsent(final HE_Element el, final int defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public int get(final HE_Element el) {
		return get(el.getKey());
	}
}
