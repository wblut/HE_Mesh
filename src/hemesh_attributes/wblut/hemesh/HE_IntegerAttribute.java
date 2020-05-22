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
class HE_IntegerAttribute {
	/**  */
	String name;
	/**  */
	HE_IntMap attributeList;
	/**  */
	int defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_IntegerAttribute(final String name, final int defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_IntMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void set(final HE_Element el, final int value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public int get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final int value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public int get(final long key) {
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
