package wblut.hemesh;

import lombok.ToString;

@ToString(includeFieldNames = true)
class HE_BooleanAttribute {
	String name;
	HE_BooleanMap attributeList;
	boolean defaultValue;
	boolean persistent;

	HE_BooleanAttribute(final String name, final boolean defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_BooleanMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	public void set(final HE_Element el, final boolean value) {
		attributeList.put(el, value);
	}

	public boolean get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	public void set(final long key, final boolean value) {
		attributeList.put(key, value);
	}

	public boolean get(final long key) {
		return attributeList.getIfAbsent(key, defaultValue);
	}

	public boolean isPersistent() {
		return persistent;
	}
}
