/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.math;

import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoordinate;

/**
 * The Class WB_ConstantParameter.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 *         A parameter which is constant, i.e. a single unchanging value.
 */
public class WB_ConstantVectorParameter implements WB_VectorParameter {
	/** The value. */
	WB_Coord value;

	/**
	 * 
	 *
	 * @param value
	 */
	public WB_ConstantVectorParameter(final WB_Coord value) {
		this.value = new WB_MutableCoordinate(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.math.WB_VectorParameter#evaluate(double[])
	 */
	@Override
	public WB_Coord evaluate(final double... x) {
		return value;
	}
}
