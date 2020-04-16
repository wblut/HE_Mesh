package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongLongHashMap;

public class WB_IndexedLongMap extends LongLongHashMap {
	public WB_IndexedLongMap() {
		super();
	}

	public void put(final int i, final long value) {
		put(i, value);
	}

	public long getIfAbsent(final int i, final long defaultValue) {
		return getIfAbsent(i, defaultValue);
	}

	public long get(final int i) {
		return get(i);
	}
}
