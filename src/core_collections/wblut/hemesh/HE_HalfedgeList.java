package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_List;

/**
 *
 */
public class HE_HalfedgeList extends WB_List<HE_Halfedge> {
	/**
	 *
	 */
	public HE_HalfedgeList() {
		super();
	}

	/**
	 *
	 *
	 * @param collection
	 */
	public HE_HalfedgeList(final Collection<HE_Halfedge> collection) {
		super(collection);
	}
}
