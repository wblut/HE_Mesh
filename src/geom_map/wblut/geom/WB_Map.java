package wblut.geom;

public interface WB_Map {
	void mapPoint3D(WB_Coord p, WB_MutableCoord result);

	void mapPoint3D(double x, double y, double z, WB_MutableCoord result);

	void unmapPoint3D(WB_Coord p, WB_MutableCoord result);

	void unmapPoint3D(double u, double v, double w, WB_MutableCoord result);

	void mapVector3D(WB_Coord p, WB_MutableCoord result);

	void mapVector3D(double x, double y, double z, WB_MutableCoord result);

	void unmapVector3D(WB_Coord p, WB_MutableCoord result);

	void unmapVector3D(double u, double v, double w, WB_MutableCoord result);

	WB_Coord mapPoint3D(WB_Coord p);

	WB_Coord mapPoint3D(double x, double y, double z);

	WB_Coord unmapPoint3D(WB_Coord p);

	WB_Coord unmapPoint3D(double u, double v, double w);

	WB_Coord mapVector3D(WB_Coord p);

	WB_Coord mapVector3D(double x, double y, double z);

	WB_Coord unmapVector3D(WB_Coord p);

	WB_Coord unmapVector3D(double u, double v, double w);
}
