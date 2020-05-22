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
class HE_BooleanAttribute {
	/**  */
	String name;
	/**  */
	HE_BooleanMap attributeList;
	/**  */
	boolean defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_BooleanAttribute(final String name, final boolean defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_BooleanMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void set(final HE_Element el, final boolean value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public boolean get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final boolean value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public boolean get(final long key) {
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
