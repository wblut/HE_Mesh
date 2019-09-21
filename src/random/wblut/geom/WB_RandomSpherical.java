/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.math.WB_MTRandom;

/**
 *
 * Random generator for vectors uniformly distributed on a unit sphere. The
 * coordinates returned give the angular parts of the spherical coordinates,
 * theta and phi theta: 0-PI phi: 0-2PI
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomSpherical extends WB_RandomGenerator {
	private final WB_MTRandom randomGen;

	/**
	 *
	 */
	public WB_RandomSpherical() {
		randomGen = new WB_MTRandom();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomSpherical(final long seed) {
		randomGen = new WB_MTRandom(seed);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomSpherical setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextPoint()
	 */
	@Override
	public WB_Point nextPoint() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		double phi = Math.acos(z);
		double theta = 2.0 * Math.PI * randomGen.nextDouble();
		return new WB_Point(phi, theta, 0.0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		double phi = Math.acos(z);
		double theta = 2.0 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(phi, theta, 0.0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#reset()
	 */
	@Override
	public void reset() {
		randomGen.reset();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(wblut.geom.WB_Coord)
	 */
	@Override
	public WB_RandomGenerator setOffset(final WB_Coord offset) {
		// TODO Auto-generated method stub
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomGenerator setOffset(final double x, final double y) {
		// TODO Auto-generated method stub
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomGenerator setOffset(final double x, final double y, final double z) {
		// TODO Auto-generated method stub
		return this;
	}

}
