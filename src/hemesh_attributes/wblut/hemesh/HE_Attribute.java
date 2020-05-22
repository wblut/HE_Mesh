package wblut.hemesh;

import lombok.ToString;

/**
 *
 *
 * @param <T>
 */
/**
 *
 *
 * @return
 */
@ToString(includeFieldNames = true)
class HE_Attribute<T> {
	/**  */
	String name;
	/**  */
	HE_ObjectMap<T> attributeList;
	/**  */
	T defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_Attribute(final String name, final T defaultValue, final boolean persistent) {
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
	public void set(final HE_Element el, final T value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public T get(final HE_Element el) {
		return attributeList.getIfAbsent(el.getKey(), () -> defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final T value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public T get(final long key) {
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
