package wblut.geom;

public class WB_BSPNode2D {
	protected WB_Line partition;
	protected WB_List<WB_Segment> segments;
	protected WB_BSPNode2D pos = null;
	protected WB_BSPNode2D neg = null;

	public WB_BSPNode2D() {
		segments = new WB_List<>();
	}
}