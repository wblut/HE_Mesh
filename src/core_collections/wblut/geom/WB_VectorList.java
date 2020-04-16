package wblut.geom;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_VectorList extends FastList<WB_Vector> {
	public WB_VectorList() {
		super();
	}

	public WB_VectorList(final Collection<WB_Vector> collection) {
		super(collection);
	}
}
