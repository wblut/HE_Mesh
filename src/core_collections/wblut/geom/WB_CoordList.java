package wblut.geom;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_CoordList extends FastList<WB_Coord> {
	public WB_CoordList() {
		super();
	}

	public WB_CoordList(final Collection<WB_Coord> collection) {
		super(collection);
	}
}
