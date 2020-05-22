package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

/**
 *
 *
 * @param <T>
 */
public class HE_ObjectMap<T> extends LongObjectHashMap<T> {
	/**
	 *
	 */
	public HE_ObjectMap() {
		super();
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void put(final HE_Element el, final T value) {
		put(el.getKey(), value);
	}

	/**
	 *
	 *
	 * @param el
	 * @param defaultValue
	 * @return
	 */
	public T getIfAbsent(final HE_Element el, final T defaultValue) {
		final T result = get(el.getKey());
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public T get(final HE_Element el) {
		return get(el.getKey());
	}
}
