/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

/**
 * Interface for random vector/point generators
 *
 * @author frederikvanhoutte
 *
 */
public interface WB_RandomGenerator extends WB_PointGenerator, WB_VectorGenerator {
	/**
	 * Set the seed for the RNG.
	 *
	 * @param seed
	 * @return this
	 */
	public WB_RandomGenerator setSeed(final long seed);

	/**
	 * Get the next random point.
	 *
	 * @return
	 */
	@Override
	public WB_Point nextPoint();

	/**
	 * Get the next random vector.
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVector();

	/**
	 * Reset the RNG.
	 */
	public void reset();

	/**
	 * Set point offset.
	 *
	 * @param offset
	 * @return
	 */
	public WB_RandomGenerator setOffset(WB_Coord offset);

	/**
	 * Set point offset.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public WB_RandomGenerator setOffset(double x, double y);

	/**
	 * Set point offset.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_RandomGenerator setOffset(double x, double y, double z);
}
