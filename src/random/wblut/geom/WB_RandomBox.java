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
 * Random generator for vectors uniformly distributed in the halfopen box
 * [-X/2,-Y/2,-Z/2]-(X/2,Y/2,Z/2).
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomBox extends WB_RandomGenerator {
	private final WB_MTRandom randomGen;
	private double X, Y, Z;
	private WB_Vector offset;

	/**
	 *
	 */
	public WB_RandomBox() {
		randomGen = new WB_MTRandom();
		X = Y = Z = 1.0;
		offset = new WB_Vector();
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomBox(final long seed) {
		randomGen = new WB_MTRandom(seed);
		X = Y = Z = 1.0;
		offset = new WB_Vector();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setSeed(long)
	 */
	@Override
	public WB_RandomBox setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 *
	 *
	 * @param X
	 * @param Y
	 * @param Z
	 * @return
	 */
	public WB_RandomBox setSize(final double X, final double Y, final double Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		return this;
	}

	public WB_RandomBox set(final WB_AABB AABB) {
		this.X = AABB.getWidth();
		this.Y = AABB.getHeight();
		this.Z = AABB.getDepth();
		this.offset.set(AABB.getCenterX(), AABB.getCenterY(), AABB.getCenterZ());
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextPoint()
	 */
	@Override
	public WB_Point nextPoint() {
		return new WB_Point(X * randomGen.nextCenteredDouble(), Y * randomGen.nextCenteredDouble(),
				Z * randomGen.nextCenteredDouble()).addSelf(offset);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#nextVector()
	 */
	@Override
	public WB_Vector nextVector() {
		return new WB_Vector(X * randomGen.nextCenteredDouble(), Y * randomGen.nextCenteredDouble(),
				Z * randomGen.nextCenteredDouble()).addSelf(offset);
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
	public WB_RandomBox setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double)
	 */
	@Override
	public WB_RandomBox setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_RandomPoint#setOffset(double, double, double)
	 */
	@Override
	public WB_RandomBox setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}
}
