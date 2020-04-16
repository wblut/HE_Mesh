package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongFloatHashMap;

public class WB_IndexedFloatMap extends LongFloatHashMap {
	public WB_IndexedFloatMap() {
		super();
	}

	public void put(final int i, final float value) {
		put(i, value);
	}

	public float getIfAbsent(final int i, final float defaultValue) {
		return getIfAbsent(i, defaultValue);
	}

	public float get(final int i) {
		return get(i);
	}
}
