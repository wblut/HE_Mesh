package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_List;

public class HE_FaceList extends WB_List<HE_Face> {
	public HE_FaceList() {
		super();
	}

	public HE_FaceList(final Collection<HE_Face> collection) {
		super(collection);
	}
}
