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
 * Random generator for vectors uniformly distributed on a halfopen linesegment
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomSegment extends WB_RandomGenerator {
	private final WB_MTRandom randomGen;
	WB_Point start;
	WB_Point end;
	private WB_Vector offset;

	/**
	 *
	 *
	 * @param start
	 * @param end
	 */
	public WB_RandomSegment(final WB_Coord start, final WB_Coord end) {
		this.start = new WB_Point(start);
		this.end = new WB_Point(end);
		randomGen = new WB_MTRandom();
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param start
	 * @param end
	 * @param seed
	 */
	public WB_RandomSegment(final WB_Coord start, final WB_Coord end, final long seed) {
		this.start = new WB_Point(start);
		this.end = new WB_Point(end);
		randomGen = new WB_MTRandom(seed);
		offset = new WB_Vector();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomSegment setSeed(final long seed) {
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
		final double d = randomGen.nextDouble();
		return new WB_Point(start.xd() + d * (end.xd() - start.xd()), start.yd() + d * (end.yd() - start.yd()),
				start.zd() + d * (end.zd() - start.zd()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		final double d = randomGen.nextDouble();
		return new WB_Vector(start.xd() + d * (end.xd() - start.xd()), start.yd() + d * (end.yd() - start.yd()),
				start.zd() + d * (end.zd() - start.zd()));
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
	public WB_RandomSegment setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomSegment setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomSegment setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}
