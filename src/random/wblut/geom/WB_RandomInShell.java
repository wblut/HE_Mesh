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
 * Random generator for vectors uniformly distributed inside a sphere with
 * radius r.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomInShell extends WB_RandomGenerator {
	private final WB_MTRandom randomGen;
	private double innerRadius;
	private double outerRadius;
	private WB_Vector offset;

	/**
	 *
	 */
	public WB_RandomInShell() {
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
	public WB_RandomInShell(final long seed) {
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
	public WB_RandomInShell setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 * 
	 * @param ir
	 * @param or
	 * @return
	 */
	public WB_RandomInShell setRadius(final double ir, final double or) {
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
		final double elevation = Math.asin(2.0 * randomGen.nextDouble() - 1);
		final double azimuth = 2 * Math.PI * randomGen.nextDouble();
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		}
		return new WB_Point(r * Math.cos(elevation) * Math.cos(azimuth), r * Math.cos(elevation) * Math.sin(azimuth),
				r * Math.sin(elevation)).addSelf(offset);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		final double elevation = Math.asin(2.0 * randomGen.nextDouble() - 1);
		final double azimuth = 2 * Math.PI * randomGen.nextDouble();
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		}
		return new WB_Vector(r * Math.cos(elevation) * Math.cos(azimuth), r * Math.cos(elevation) * Math.sin(azimuth),
				r * Math.sin(elevation)).addSelf(offset);
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
	public WB_RandomInShell setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomInShell setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomInShell setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}
