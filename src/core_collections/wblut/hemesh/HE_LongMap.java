package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongLongHashMap;

/**
 *
 */
public class HE_LongMap extends LongLongHashMap {
	/**
	 *
	 */
	public HE_LongMap() {
		super();
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void put(final HE_Element el, final long value) {
		put(el.getKey(), value);
	}

	/**
	 *
	 *
	 * @param el
	 * @param defaultValue
	 * @return
	 */
	public long getIfAbsent(final HE_Element el, final long defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public long get(final HE_Element el) {
		return get(el.getKey());
	}
}
