package wblut.math;

import wblut.geom.WB_Coord;

public interface WB_VectorParameter {
	WB_Coord evaluate(double... x);
}
