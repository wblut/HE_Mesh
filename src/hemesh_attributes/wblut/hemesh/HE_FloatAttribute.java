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
class HE_FloatAttribute {
	/**  */
	String name;
	/**  */
	HE_FloatMap attributeList;
	/**  */
	float defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_FloatAttribute(final String name, final float defaultValue, final boolean persistent) {
		this.name = name;
		attributeList = new HE_FloatMap();
		this.defaultValue = defaultValue;
		this.persistent = persistent;
	}

	/**
	 *
	 *
	 * @param el
	 * @param value
	 */
	public void set(final HE_Element el, final float value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public float get(final HE_Element el) {
		return attributeList.getIfAbsent(el, defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final float value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public float get(final long key) {
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
