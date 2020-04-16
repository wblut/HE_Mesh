package wblut.hemesh;

import java.util.Collection;

import org.eclipse.collections.impl.list.mutable.FastList;

public class HE_VertexList extends FastList<HE_Vertex> {
	public HE_VertexList() {
		super();
	}

	public HE_VertexList(final Collection<HE_Vertex> collection) {
		super(collection);
	}
}
