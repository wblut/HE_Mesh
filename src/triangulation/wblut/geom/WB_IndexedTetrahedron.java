/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

/**
 *
 */
public class WB_IndexedTetrahedron extends WB_Tetrahedron {
	int i1, i2, i3, i4;
	
	protected WB_IndexedTetrahedron() {
	}

	/**
	 *
	 */
	private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 */
	public WB_IndexedTetrahedron(final int i1, final int i2, final int i3,int i4,
			final WB_CoordCollection points) {
		super(points.get(i1), points.get(i2), points.get(i3), points.get(i4));
		this.i1 = i1;
		this.i2 = i2;
		this.i3 = i3;
		this.i4 = i4;
	}

	public WB_IndexedTetrahedron(final int i, final int[] tetrahedra,
			final WB_CoordCollection points) {
		super(points.get(tetrahedra[i]), points.get(tetrahedra[i + 1]),
				points.get(tetrahedra[i + 2]),
				points.get(tetrahedra[i +3]));
		this.i1 = tetrahedra[i];
		this.i2 = tetrahedra[i + 1];
		this.i3 = tetrahedra[i + 2];
		this.i4= tetrahedra[i + 3];
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
	
	/**
	 *
	 *
	 * @return
	 */
	public int i4() {
		return i4;
	}
}