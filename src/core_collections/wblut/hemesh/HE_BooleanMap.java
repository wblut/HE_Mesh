package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongBooleanHashMap;

/**
 *
 */
public class HE_BooleanMap extends LongBooleanHashMap {
	/**
	 *
	 */
	public HE_BooleanMap() {
		super();
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void put(final HE_Element el, final boolean value) {
		put(el.getKey(), value);
	}

	/**
	 *
	 *
	 * @param el
	 * @param defaultValue
	 * @return
	 */
	public boolean getIfAbsent(final HE_Element el, final boolean defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public boolean get(final HE_Element el) {
		return get(el.getKey());
	}
}
