package wblut.hemesh;

import lombok.ToString;

@ToString(includeFieldNames = true)
class HE_DoubleAttribute {
	String name;
	HE_DoubleMap attributeList;
	double defaultValue;
	boolean persistent;

	HE_DoubleAttribute(final String name, final double defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_DoubleMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	public void set(final HE_Element el, final double value) {
		attributeList.put(el, value);
	}

	public double get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	public void set(final long key, final double value) {
		attributeList.put(key, value);
	}

	public double get(final long key) {
		return attributeList.getIfAbsent(key, defaultValue);
	}

	public boolean isPersistent() {
		return persistent;
	}
}
