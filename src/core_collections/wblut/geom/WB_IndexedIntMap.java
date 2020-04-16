package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

public class WB_IndexedIntMap extends LongIntHashMap {
	public WB_IndexedIntMap() {
		super();
	}

	public void put(final int i, final int value) {
		put(i, value);
	}

	public int getIfAbsent(final int i, final int defaultValue) {
		return getIfAbsent(i, defaultValue);
	}

	public int get(final int i) {
		return get(i);
	}
}
