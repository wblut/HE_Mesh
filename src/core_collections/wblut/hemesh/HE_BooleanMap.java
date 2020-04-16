package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongBooleanHashMap;

public class HE_BooleanMap extends LongBooleanHashMap {
	public HE_BooleanMap() {
		super();
	}

	public void put(final HE_Element el, final boolean value) {
		put(el.getKey(), value);
	}

	public boolean getIfAbsent(final HE_Element el, final boolean defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	public boolean get(final HE_Element el) {
		return get(el.getKey());
	}
}
