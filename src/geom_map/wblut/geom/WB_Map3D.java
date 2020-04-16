package wblut.geom;

public interface WB_Map3D {
	void mapPoint3D(WB_Coord p, WB_MutableCoord result);

	void mapPoint3D(double x, double y, double z, WB_MutableCoord result);

	void unmapPoint3D(WB_Coord p, WB_MutableCoord result);

	void unmapPoint3D(double u, double v, double w, WB_MutableCoord result);

	void mapVector3D(WB_Coord p, WB_MutableCoord result);

	void mapVector3D(double x, double y, double z, WB_MutableCoord result);

	void unmapVector3D(WB_Coord p, WB_MutableCoord result);

	void unmapVector3D(double u, double v, double w, WB_MutableCoord result);

	WB_Point mapPoint3D(WB_Coord p);

	WB_Point mapPoint3D(double x, double y, double z);

	WB_Point unmapPoint3D(WB_Coord p);

	WB_Point unmapPoint3D(double u, double v, double w);

	WB_Vector mapVector3D(WB_Coord p);

	WB_Vector mapVector3D(double x, double y, double z);

	WB_Vector unmapVector3D(WB_Coord p);

	WB_Vector unmapVector3D(double u, double v, double w);
}
