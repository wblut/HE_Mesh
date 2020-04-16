package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongDoubleHashMap;

public class HE_DoubleMap extends LongDoubleHashMap {
	public HE_DoubleMap() {
		super();
	}

	public void put(final HE_Element el, final double value) {
		put(el.getKey(), value);
	}

	public double getIfAbsent(final HE_Element el, final double defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	public double get(final HE_Element el) {
		return get(el.getKey());
	}
}
