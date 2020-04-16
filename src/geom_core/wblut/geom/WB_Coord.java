package wblut.geom;

public interface WB_Coord extends Comparable<WB_Coord> {
	double xd();

	double yd();

	double zd();

	double wd();

	double getd(int i);

	float xf();

	float yf();

	float zf();

	float wf();

	float getf(int i);
}
