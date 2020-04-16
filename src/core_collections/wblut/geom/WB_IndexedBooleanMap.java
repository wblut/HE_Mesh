package wblut.geom;

import org.eclipse.collections.impl.map.mutable.primitive.IntBooleanHashMap;

public class WB_IndexedBooleanMap extends IntBooleanHashMap {
	public WB_IndexedBooleanMap() {
		super();
	}

	@Override
	public void put(final int i, final boolean value) {
		put(i, value);
	}

	@Override
	public boolean getIfAbsent(final int i, final boolean defaultValue) {
		return getIfAbsent(i, defaultValue);
	}

	@Override
	public boolean get(final int i) {
		return get(i);
	}
}
