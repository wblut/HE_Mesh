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
 * Random generator for vectors uniformly distributed on a sphere with radius r.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomOnSphere extends WB_RandomGenerator {
	private final WB_MTRandom randomGen;
	private double radius;
	private WB_Vector offset;

	/**
	 *
	 */
	public WB_RandomOnSphere() {
		randomGen = new WB_MTRandom();
		radius = 1.0;
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomOnSphere(final long seed) {
		randomGen = new WB_MTRandom(seed);
		radius = 1.0;
		offset = new WB_Vector();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomOnSphere setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public WB_RandomOnSphere setRadius(final double r) {
		radius = r;
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
		final double r = radius * Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(r * Math.cos(t), r * Math.sin(t), radius * z).addSelf(offset);
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
		final double r = radius * Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(r * Math.cos(t), r * Math.sin(t), radius * z).addSelf(offset);
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
	public WB_RandomOnSphere setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomOnSphere setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomOnSphere setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}
