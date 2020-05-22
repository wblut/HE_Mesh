package wblut.hemesh;

import lombok.ToString;

/**
 *
 */
/**
 *
 *
 * @return
 */
@ToString(includeFieldNames = true)
class HE_DoubleAttribute {
	/**  */
	String name;
	/**  */
	HE_DoubleMap attributeList;
	/**  */
	double defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_DoubleAttribute(final String name, final double defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_DoubleMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void set(final HE_Element el, final double value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public double get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final double value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public double get(final long key) {
		return attributeList.getIfAbsent(key, defaultValue);
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isPersistent() {
		return persistent;
	}
}
