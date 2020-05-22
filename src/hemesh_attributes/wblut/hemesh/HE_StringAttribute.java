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
class HE_StringAttribute {
	/**  */
	String name;
	/**  */
	HE_ObjectMap<String> attributeList;
	/**  */
	String defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_StringAttribute(final String name, final String defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_ObjectMap<>();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void set(final HE_Element el, final String value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public String get(final HE_Element el) {
		return attributeList.getIfAbsent(el.getKey(), () -> defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final String value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public String get(final long key) {
		return attributeList.getIfAbsent(key, () -> defaultValue);
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
