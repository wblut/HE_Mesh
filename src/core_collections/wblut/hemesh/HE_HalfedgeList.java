package wblut.hemesh;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

public class HE_HalfedgeList extends FastList<HE_Halfedge> {
	public HE_HalfedgeList() {
		super();
	}

	public HE_HalfedgeList(final Collection<HE_Halfedge> collection) {
		super(collection);
	}
}
