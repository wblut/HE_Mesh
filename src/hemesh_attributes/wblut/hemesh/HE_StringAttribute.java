package wblut.hemesh;

import lombok.ToString;

@ToString(includeFieldNames = true)
class HE_StringAttribute {
	String name;
	HE_ObjectMap<String> attributeList;
	String defaultValue;
	boolean persistent;

	HE_StringAttribute(final String name, final String defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_ObjectMap<>();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	public void set(final HE_Element el, final String value) {
		attributeList.put(el, value);
	}

	public String get(final HE_Element el) {
		return attributeList.getIfAbsent(el.getKey(), () -> defaultValue);
	}

	public void set(final long key, final String value) {
		attributeList.put(key, value);
	}

	public String get(final long key) {
		return attributeList.getIfAbsent(key, () -> defaultValue);
	}

	public boolean isPersistent() {
		return persistent;
	}
}
