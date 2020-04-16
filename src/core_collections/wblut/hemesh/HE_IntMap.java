package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

public class HE_IntMap extends LongIntHashMap {
	public HE_IntMap() {
		super();
	}

	public void put(final HE_Element el, final int value) {
		put(el.getKey(), value);
	}

	public int getIfAbsent(final HE_Element el, final int defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	public int get(final HE_Element el) {
		return get(el.getKey());
	}
}
