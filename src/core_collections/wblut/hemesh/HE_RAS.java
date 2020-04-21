package wblut.hemesh;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import wblut.geom.WB_List;

//Random Access Set
public class HE_RAS<E extends HE_Element> extends AbstractSet<E> {
	WB_List<E> objects;
	HE_IntMap indices;

	public HE_RAS() {
		objects = new WB_List<>();
		indices = new HE_IntMap();
	}

	public HE_RAS(final Collection<E> items) {
		this();
		for (final E e : items) {
			add(e);
		}
	}

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

	public E getWithIndex(final int i) {
		return objects.get(i);
	}

	public E getWithKey(final long key) {
		final int i = indices.getIfAbsent(key, -1);
		if (i == -1) {
			return null;
		}
		return objects.get(i);
	}

	public int indexOf(final E object) {
		return indices.getIfAbsent(object.getKey(), -1);
	}

	public E pollRandom(final Random rnd) {
		if (objects.isEmpty()) {
			return null;
		}
		final int id = rnd.nextInt(objects.size());
		return removeAt(id);
	}

	@Override
	public int size() {
		return objects.size();
	}

	public boolean contains(final E object) {
		if (object == null) {
			return false;
		}
		return indices.containsKey(object.getKey());
	}

	public boolean containsKey(final Long key) {
		return indices.containsKey(key);
	}

	@Override
	public Iterator<E> iterator() {
		return objects.iterator();
	}

	public List<E> getObjects() {
		return objects.asUnmodifiable();
	}
}