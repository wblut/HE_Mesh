package wblut.geom;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_List<T> extends FastList<T> {
	public WB_List() {
		super();
	}

	public WB_List(final Collection<? extends T> collection) {
		super(collection);
	}
}
