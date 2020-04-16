package wblut.hemesh;

import lombok.ToString;

@ToString(includeFieldNames = true)
class HE_IntegerAttribute {
	String name;
	HE_IntMap attributeList;
	int defaultValue;
	boolean persistent;

	HE_IntegerAttribute(final String name, final int defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_IntMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	public void set(final HE_Element el, final int value) {
		attributeList.put(el, value);
	}

	public int get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	public void set(final long key, final int value) {
		attributeList.put(key, value);
	}

	public int get(final long key) {
		return attributeList.getIfAbsent(key, defaultValue);
	}

	public boolean isPersistent() {
		return persistent;
	}
}
