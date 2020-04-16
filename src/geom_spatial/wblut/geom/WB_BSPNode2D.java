package wblut.geom;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_BSPNode2D {
	protected WB_Line partition;
	protected FastList<WB_Segment> segments;
	protected WB_BSPNode2D pos = null;
	protected WB_BSPNode2D neg = null;

	public WB_BSPNode2D() {
		segments = new FastList<>();
	}
}