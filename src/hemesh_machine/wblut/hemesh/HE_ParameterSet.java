package wblut.hemesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HE_ParameterSet {
	/**  */
	Map<String, Object> values;
	/**  */
	List<String> names;
	/**  */
	String name;

	/**
	 *
	 *
	 * @param name
	 */
	public HE_ParameterSet(final String name) {
		this.name = name;
		values = new HashMap<>();
		names = new ArrayList<>();
	}

	/**
	 *
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public HE_ParameterSet set(final String name, final Object value) {
		if (values.put(name.toLowerCase(), value) == null) {
			names.add(name.toLowerCase());
		}
		return this;
	}

	/**
	 *
	 *
	 * @param name
	 * @param ifEmpty
	 * @return
	 */
	public Object get(final String name, final Object ifEmpty) {
		final Object value = values.get(name.toLowerCase());
		if (value == null) {
			return ifEmpty;
		}
		return value;
	}

	/**
	 *
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public HE_ParameterSet set(final String name, final int value) {
		if (values.put(name.toLowerCase(), value) == null) {
			names.add(name.toLowerCase());
		}
		return this;
	}

	/**
	 *
	 *
	 * @param name
	 * @param ifEmpty
	 * @return
	 */
	public int get(final String name, final int ifEmpty) {
		final Object value = values.get(name.toLowerCase());
		if (value == null) {
			return ifEmpty;
		}
		return ((Integer) value).intValue();
	}

	/**
	 *
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public HE_ParameterSet set(final String name, final double value) {
		if (values.put(name.toLowerCase(), value) == null) {
			names.add(name.toLowerCase());
		}
		return this;
	}

	/**
	 *
	 *
	 * @param name
	 * @param ifEmpty
	 * @return
	 */
	public double get(final String name, final double ifEmpty) {
		final Object value = values.get(name.toLowerCase());
		if (value == null) {
			return ifEmpty;
		}
		return ((Double) value).doubleValue();
	}

	/**
	 *
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public HE_ParameterSet set(final String name, final boolean value) {
		if (values.put(name.toLowerCase(), value) == null) {
			names.add(name.toLowerCase());
		}
		return this;
	}

	/**
	 *
	 *
	 * @param name
	 * @param ifEmpty
	 * @return
	 */
	public boolean get(final String name, final boolean ifEmpty) {
		final Object value = values.get(name.toLowerCase());
		if (value == null) {
			return ifEmpty;
		}
		return ((Boolean) value).booleanValue();
	}

	/**
	 *
	 *
	 * @return
	 */
	public String[] getParameterNames() {
		final String[] result = new String[names.size()];
		int n = 0;
		for (final String name : names) {
			result[n++] = name;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param name
	 * @return
	 */
	public HE_ParameterSet remove(final String name) {
		values.remove(name.toLowerCase());
		final Iterator<String> nItr = names.iterator();
		String n;
		while (nItr.hasNext()) {
			n = nItr.next();
			if (n.contentEquals(name)) {
				nItr.remove();
			}
		}
		return this;
	}
}
