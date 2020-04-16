package wblut.geom;

public interface WB_Surface {
	WB_Point surfacePoint(double u, double v);

	double getLowerU();

	double getUpperU();

	double getLowerV();

	double getUpperV();
}
