/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

public class WB_IndexedSegment extends WB_Segment {

	private int i1;

	private int i2;

	// private final WB_Coordinate[] points;

	/**
	 *
	 *
	 * @param i1
	 * @param i2
	 * @param points
	 */
	public WB_IndexedSegment(final int i1, final int i2, final WB_Coord[] points) {
		super(points[i1], points[i2]);
		this.i1 = i1;
		this.i2 = i2;

	}

	/**
	 *
	 *
	 * @param i1
	 * @param i2
	 * @param p1
	 * @param p2
	 */
	protected WB_IndexedSegment(final int i1, final int i2, final WB_Coord p1, final WB_Coord p2) {
		super(p1, p2);
		this.i1 = i1;
		this.i2 = i2;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int i1() {
		return i1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int i2() {
		return i2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Segment#negate()
	 */
	@Override
	public WB_IndexedSegment negate() {
		return new WB_IndexedSegment(i2, i1, endpoint, origin);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.geom.WB_Segment#reverse()
	 */
	@Override
	public void reverse() {
		direction = new WB_Vector(direction).mulSelf(-1);
		WB_Point tmpp = origin;
		origin = endpoint;
		endpoint = tmpp;
		final int tmp = i2;
		i2 = i1;
		i1 = tmp;
	}

}
