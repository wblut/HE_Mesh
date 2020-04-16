package wblut.hemesh;

import org.eclipse.collections.impl.map.mutable.primitive.LongFloatHashMap;

public class HE_FloatMap extends LongFloatHashMap {
	public HE_FloatMap() {
		super();
	}

	public void put(final HE_Element el, final float value) {
		put(el.getKey(), value);
	}

	public float getIfAbsent(final HE_Element el, final float defaultValue) {
		return getIfAbsent(el.getKey(), defaultValue);
	}

	public float get(final HE_Element el) {
		return get(el.getKey());
	}
}
