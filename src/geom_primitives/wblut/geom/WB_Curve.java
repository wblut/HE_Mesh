package wblut.geom;

public interface WB_Curve {
	WB_Point getPointOnCurve(double u);

	WB_Vector getDirectionOnCurve(double u);

	WB_Vector getDerivative(double u);

	double getLowerU();

	double getUpperU();
}
