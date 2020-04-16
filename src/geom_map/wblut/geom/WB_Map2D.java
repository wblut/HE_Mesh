package wblut.geom;

public interface WB_Map2D extends WB_Map {
	void unmapPoint2D(WB_Coord p, WB_MutableCoord result);

	void unmapPoint2D(double u, double v, WB_MutableCoord result);

	void unmapVector2D(WB_Coord v, WB_MutableCoord result);

	void unmapVector2D(double u, double v, WB_MutableCoord result);

	WB_Point unmapPoint2D(WB_Coord p);

	WB_Coord unmapPoint2D(double u, double v);

	WB_Coord unmapVector2D(WB_Coord v);

	WB_Coord unmapVector2D(double u, double v);
}
