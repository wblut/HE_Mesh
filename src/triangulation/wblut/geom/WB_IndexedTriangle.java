/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

/**
 * @author FVH
 *
 */
public class WB_IndexedTriangle extends WB_Triangle {
	int i1, i2, i3;

	public WB_IndexedTriangle(final int i1, final int i2, final int i3,
			final WB_CoordCollection points) {
		super(points.get(i1), points.get(i2), points.get(i3));
		this.i1 = i1;
		this.i2 = i2;
		this.i3 = i3;
	}

	public WB_IndexedTriangle(final int i, final int[] tris,
			final WB_CoordCollection points) {
		super(points.get(tris[i]), points.get(tris[i + 1]),
				points.get(tris[i + 2]));
		this.i1 = tris[i];
		this.i2 = tris[i + 1];
		this.i3 = tris[i + 2];
	}

	@Override
	public void cycle() {
		WB_Coord tmp = p1;
		p1 = p2;
		p2 = p3;
		p3 = tmp;
		int tmpi = i1;
		i1 = i2;
		i2 = i3;
		i3 = tmpi;
		update();
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

	/**
	 *
	 *
	 * @return
	 */
	public int i3() {
		return i3;
	}
}
