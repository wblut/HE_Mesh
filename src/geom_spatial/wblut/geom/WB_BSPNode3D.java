package wblut.geom;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_BSPNode3D {
	protected WB_Plane partition;
	protected FastList<WB_Polygon> polygons;
	protected WB_BSPNode3D pos = null;
	protected WB_BSPNode3D neg = null;

	public WB_BSPNode3D() {
		polygons = new FastList<>();
	}
}