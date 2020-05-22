package wblut.geom;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 *
 *
 * @param <T>
 */
public class WB_List<T> extends FastList<T> {
	/**
	 *
	 */
	public WB_List() {
		super();
	}

	/**
	 *
	 *
	 * @param collection
	 */
	public WB_List(final Collection<? extends T> collection) {
		super(collection);
	}
}
