package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoordinate;

/**
 *
 */
public class WB_ConstantVectorParameter implements WB_VectorParameter {
	/**  */
	WB_Coord value;

	/**
	 *
	 *
	 * @param value
	 */
	public WB_ConstantVectorParameter(final WB_Coord value) {
		this.value = new WB_MutableCoordinate(value);
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	@Override
	public WB_Coord evaluate(final double... x) {
		return value;
	}
}
