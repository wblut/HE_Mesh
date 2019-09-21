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
 * Random generator for vectors or points uniformly distributed on the mantle of
 * a cylinder with radius r and height h.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomOnCylinder extends WB_RandomGenerator {
	/**
	 *
	 */
	private final WB_MTRandom randomGen;

	private double radius, height;
	private WB_Vector offset;

	/**
	 *
	 */
	public WB_RandomOnCylinder() {
		randomGen = new WB_MTRandom();
		radius = 1.0;
		height = 1.0;
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomOnCylinder(final long seed) {
		randomGen = new WB_MTRandom(seed);
		radius = 1.0;
		height = 1.0;
		offset = new WB_Vector();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomOnCylinder setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public WB_RandomOnCylinder setRadius(final double r) {
		radius = r;
		return this;
	}

	/**
	 *
	 *
	 * @param h
	 * @return
	 */
	public WB_RandomOnCylinder setHeight(final double h) {
		height = h;
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
		return new WB_Point(radius * Math.cos(t), radius * Math.sin(t), height * randomGen.nextCenteredDouble())
				.addSelf(offset);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(radius * Math.cos(t), radius * Math.sin(t), height * randomGen.nextCenteredDouble())
				.addSelf(offset);
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
	public WB_RandomOnCylinder setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomOnCylinder setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomOnCylinder setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}