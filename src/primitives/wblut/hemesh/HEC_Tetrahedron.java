/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Tetrahedron;

/**
 * Tetrahedron.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Tetrahedron extends HEC_Creator {
	/** Outer radius. */
	private double R;
	private WB_Coord[] points;

	/**
	 * Instantiates a new HEC_Tetrahedron.
	 *
	 */
	public HEC_Tetrahedron() {
		super();
		R = 100;
	}

	/**
	 * Set edge length.
	 *
	 * @param E
	 *            edge length
	 * @return self
	 */
	public HEC_Tetrahedron setEdge(final double E) {
		R = 0.612372 * E;
		points = null;
		return this;
	}

	/**
	 * Set radius of inscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Tetrahedron setInnerRadius(final double R) {
		this.R = R * 3;
		points = null;
		return this;
	}

	/**
	 * Set radius of circumscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Tetrahedron setOuterRadius(final double R) {
		this.R = R;
		points = null;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Tetrahedron setRadius(final double R) {
		this.R = R;
		points = null;
		return this;
	}

	/**
	 * Set radius of tangential sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Tetrahedron setMidRadius(final double R) {
		this.R = R * 1.732060;
		points = null;
		return this;
	}

	public HEC_Tetrahedron setPoints(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		points = new WB_Coord[4];
		points[0] = new WB_Point(p0);
		points[1] = new WB_Point(p1);
		points[2] = new WB_Point(p2);
		points[3] = new WB_Point(p3);
		return this;
	}
	
	public HEC_Tetrahedron setPoints(WB_Tetrahedron tetra) {
		points = new WB_Coord[4];
		points[0] = new WB_Point(tetra.p1());
		points[1] = new WB_Point(tetra.p2());
		points[2] = new WB_Point(tetra.p3());
		points[3] = new WB_Point(tetra.p4());
		return this;
	}

	/**
	 *
	 * @see wblut.hemesh.HEC_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (points == null) {
			HEC_Creator tetra = new HEC_Plato().setType(5).setEdge(R / 0.612372);
			return tetra.createBase();
		} else {
			final int[][] faces = { { 0, 1, 2 }, { 0, 2, 3 }, { 0, 3, 1 }, { 1, 3, 2 } };
			final HEC_FromFacelist fl = new HEC_FromFacelist();
			fl.setVertices(points).setFaces(faces);
			return fl.createBase();

		}

	}
}
