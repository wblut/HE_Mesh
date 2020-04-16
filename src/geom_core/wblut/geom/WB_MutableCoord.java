package wblut.geom;

public interface WB_MutableCoord extends WB_Coord {
	void setX(double x);

	void setY(double y);

	void setZ(double z);

	void setW(double w);

	void setCoord(int i, double v);

	void set(WB_Coord p);

	void set(double x, double y);

	void set(double x, double y, double z);

	void set(double x, double y, double z, double w);
}
