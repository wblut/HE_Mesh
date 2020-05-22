package wblut.hemesh;

import lombok.ToString;
import wblut.geom.WB_Coord;

/**
 *
 */
/**
 *
 *
 * @return
 */
@ToString(includeFieldNames = true)
class HE_VectorAttribute {
	/**  */
	String name;
	/**  */
	HE_ObjectMap<WB_Coord> attributeList;
	/**  */
	WB_Coord defaultValue;
	/**  */
	boolean persistent;

	/**
	 *
	 *
	 * @param name
	 * @param defaultValue
	 * @param persistent
	 */
	HE_VectorAttribute(final String name, final WB_Coord defaultValue, final boolean persistent) {
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
	public void set(final HE_Element el, final WB_Coord value) {
		attributeList.put(el, value);
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	public WB_Coord get(final HE_Element el) {
		return attributeList.getIfAbsent(el.getKey(), () -> defaultValue);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 */
	public void set(final long key, final WB_Coord value) {
		attributeList.put(key, value);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public WB_Coord get(final long key) {
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
