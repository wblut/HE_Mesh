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
 * Random generator for points on a halfopen curve. The distribution is only
 * uniform in the curve parameter.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomCurve extends WB_RandomGenerator {
	private final WB_MTRandom randomGen;
	private WB_Curve curve;
	private double start, end;
	private WB_Vector offset;

	/**
	 *
	 *
	 * @param curve
	 * @param start
	 * @param end
	 */
	public WB_RandomCurve(final WB_Curve curve, final double start, final double end) {
		this.start = start;
		this.end = end;
		this.curve = curve;
		randomGen = new WB_MTRandom();
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param curve
	 * @param start
	 * @param end
	 * @param seed
	 */
	public WB_RandomCurve(final WB_Curve curve, final double start, final double end, final long seed) {
		this.start = start;
		this.end = end;
		this.curve = curve;
		randomGen = new WB_MTRandom(seed);
		offset = new WB_Vector();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomCurve setSeed(final long seed) {
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
		final double d = start + (end - start) * randomGen.nextDouble();
		return curve.getPointOnCurve(d).addSelf(offset);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		final double d = start + (end - start) * randomGen.nextDouble();
		return curve.getPointOnCurve(d).addSelf(offset);
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
	public WB_RandomCurve setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomCurve setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomCurve setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}
