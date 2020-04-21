package wblut.geom;

public class WB_BSPNode3D {
	protected WB_Plane partition;
	protected WB_List<WB_Polygon> polygons;
	protected WB_BSPNode3D pos = null;
	protected WB_BSPNode3D neg = null;

	public WB_BSPNode3D() {
		polygons = new WB_List<>();
	}
}