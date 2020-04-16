package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

public class WB_IndexedObjectMap<T> extends LongObjectHashMap<T> {
	public WB_IndexedObjectMap() {
		super();
	}

	public void put(final int i, final T value) {
		put(i, value);
	}

	public T getIfAbsent(final int i, final T defaultValue) {
		final T result = get(i);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	public T get(final int i) {
		return get(i);
	}
}
