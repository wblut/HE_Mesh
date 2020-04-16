package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.LongDoubleHashMap;

public class WB_IndexedDoubleMap extends LongDoubleHashMap {
	public WB_IndexedDoubleMap() {
		super();
	}

	public void put(final int i, final double value) {
		put(i, value);
	}

	public double getIfAbsent(final int i, final double defaultValue) {
		return getIfAbsent(i, defaultValue);
	}

	public double get(final int i) {
		return get(i);
	}
}
