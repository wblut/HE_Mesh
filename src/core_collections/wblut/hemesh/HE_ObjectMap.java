package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

public class HE_ObjectMap<T> extends LongObjectHashMap<T> {
	public HE_ObjectMap() {
		super();
	}

	public void put(final HE_Element el, final T value) {
		put(el.getKey(), value);
	}

	public T getIfAbsent(final HE_Element el, final T defaultValue) {
		final T result = get(el.getKey());
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	public T get(final HE_Element el) {
		return get(el.getKey());
	}
}
