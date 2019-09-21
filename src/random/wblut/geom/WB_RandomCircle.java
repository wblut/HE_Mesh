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
 * Random generator for vectors or points uniformly distributed on a circle with
 * radius r.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomCircle extends WB_RandomGenerator {
	/**
	 *
	 */
	private final WB_MTRandom randomGen;
	private double radius;
	private WB_Vector offset;

	/**
	 *
	 */
	public WB_RandomCircle() {
		randomGen = new WB_MTRandom();
		radius = 1.0;
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomCircle(final long seed) {
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
	public WB_RandomCircle setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public WB_RandomCircle setRadius(final double r) {
		this.radius = r;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextPoint()
	 */
	@Override
	public WB_Point nextPoint() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(radius * Math.cos(t), radius * Math.sin(t), 0).addSelf(offset);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(radius * Math.cos(t), radius * Math.sin(t), 0).addSelf(offset);
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
	public WB_RandomCircle setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomCircle setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomCircle setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}