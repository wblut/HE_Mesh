package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoordinate3D;

public class WB_ConstantVectorParameter implements WB_VectorParameter {
	WB_Coord value;

	public WB_ConstantVectorParameter(final WB_Coord value) {
		this.value = new WB_MutableCoordinate3D(value);
	}

	@Override
	public WB_Coord evaluate(final double... x) {
		return value;
	}
}
