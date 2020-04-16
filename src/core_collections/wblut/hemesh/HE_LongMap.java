package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongLongHashMap;

public class HE_LongMap extends LongLongHashMap {
	public HE_LongMap() {
		super();
	}

	public void put(final HE_Element el, final long value) {
		put(el.getKey(), value);
	}

	public long getIfAbsent(final HE_Element el, final long defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	public long get(final HE_Element el) {
		return get(el.getKey());
	}
}
