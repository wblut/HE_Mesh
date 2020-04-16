package wblut.geom;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_PointList extends FastList<WB_Point> {
	public WB_PointList() {
		super();
	}

	public WB_PointList(final Collection<WB_Point> collection) {
		super(collection);
	}
}
