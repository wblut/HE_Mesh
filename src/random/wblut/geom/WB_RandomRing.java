/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
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
public class WB_RandomRing extends WB_RandomGenerator {
	/**
	 *
	 */
	private final WB_MTRandom randomGen;
	private double innerRadius;
	private double outerRadius;
	private WB_Vector offset;

	/**
	 *
	 */
	public WB_RandomRing() {
		randomGen = new WB_MTRandom();
		innerRadius = 0.5;
		outerRadius = 1.0;
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomRing(final long seed) {
		randomGen = new WB_MTRandom(seed);
		innerRadius = 0.5;
		outerRadius = 1.0;
		offset = new WB_Vector();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomRing setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 *
	 * @param ir
	 * @param or
	 * @return
	 */
	public WB_RandomRing setRadius(final double ir, final double or) {
		innerRadius = Math.min(Math.abs(ir), Math.abs(or));
		outerRadius = Math.max(Math.abs(ir), Math.abs(or));
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextPoint()
	 */
	@Override
	public WB_Point nextPoint() {
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		}
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(r * Math.cos(t), r * Math.sin(t), 0).addSelf(offset);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		}
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(r * Math.cos(t), r * Math.sin(t), 0).addSelf(offset);
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
	public WB_RandomRing setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomRing setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomRing setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}