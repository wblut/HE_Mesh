package wblut.hemesh;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import wblut.geom.WB_List;

/**
 *
 *
 * @param <E>
 */
//Random Access Set
public class HE_RAS<E extends HE_Element> extends AbstractSet<E> {
	/**  */
	WB_List<E> objects;
	/**  */
	HE_IntMap indices;

	/**
	 *
	 */
	public HE_RAS() {
		objects = new WB_List<>();
		indices = new HE_IntMap();
	}

	/**
	 *
	 *
	 * @param items
	 */
	public HE_RAS(final Collection<E> items) {
		this();
		for (final E e : items) {
			add(e);
		}
	}

	/**
	 *
	 *
	 * @param item
	 * @return
	 */
	@Override
	public boolean add(final E item) {
		if (item == null) {
			return false;
		}
		if (!indices.containsKey(item.getKey())) {
			indices.put(item.getKey(), objects.size());
			objects.add(item);
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param id
	 * @return
	 */
	public E removeAt(final int id) {
		if (id >= objects.size()) {
			return null;
		}
		final E res = objects.get(id);
		indices.remove(res.getKey());
		final E last = objects.remove(objects.size() - 1);
		// skip filling the hole if last is removed
		if (id < objects.size()) {
			indices.put(last.getKey(), id);
			objects.set(id, last);
		}
		return res;
	}

	/**
	 *
	 *
	 * @param item
	 * @return
	 */
	public boolean remove(final E item) {
		if (item == null) {
			return false;
		}
		// @SuppressWarnings(value = "element-type-mismatch
		final int id = indices.getIfAbsent(item.getKey(), -1);
		if (id == -1) {
			return false;
		}
		removeAt(id);
		return true;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public E getWithIndex(final int i) {
		return objects.get(i);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public E getWithKey(final long key) {
		final int i = indices.getIfAbsent(key, -1);
		if (i == -1) {
			return null;
		}
		return objects.get(i);
	}

	/**
	 *
	 *
	 * @param object
	 * @return
	 */
	public int indexOf(final E object) {
		return indices.getIfAbsent(object.getKey(), -1);
	}

	/**
	 *
	 *
	 * @param rnd
	 * @return
	 */
	public E pollRandom(final Random rnd) {
		if (objects.isEmpty()) {
			return null;
		}
		final int id = rnd.nextInt(objects.size());
		return removeAt(id);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int size() {
		return objects.size();
	}

	/**
	 *
	 *
	 * @param object
	 * @return
	 */
	public boolean contains(final E object) {
		if (object == null) {
			return false;
		}
		return indices.containsKey(object.getKey());
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	public boolean containsKey(final Long key) {
		return indices.containsKey(key);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public Iterator<E> iterator() {
		return objects.iterator();
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<E> getObjects() {
		return objects.asUnmodifiable();
	}
}