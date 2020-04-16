package wblut.math;

public interface WB_Noise {
	void setSeed(long seed);

	double value1D(double x);

	double value2D(double x, double y);

	double value3D(double x, double y, double z);

	double value4D(double x, double y, double z, double w);

	void setScale(double sx);

	void setScale(double sx, double sy);

	void setScale(double sx, double sy, double sz);

	void setScale(double sx, double sy, double sz, double sw);
}
