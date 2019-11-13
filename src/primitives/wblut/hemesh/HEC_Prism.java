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
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 * The Class HEC_Prism.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 *         Creates a rectangular prism. If a thickness is specified the result
 *         is a solid, otherwise it's a surface, a regular polygon.
 */
public class HEC_Prism extends HEC_Creator {

	/**
	 *
	 */
	private static WB_GeometryFactory gf = new WB_GeometryFactory();
	/** The facets. */
	private int facets;
	/** The thickness. */
	private double thickness;
	/** The radius. */
	private double radius;

	/**
	 * Instantiates a new hE c_ prism.
	 */
	public HEC_Prism() {
		super();
		facets = 6;
		thickness = 100;
		radius = 100;
	}

	/**
	 * Instantiates a new hE c_ prism.
	 *
	 * @param n
	 * @param r
	 * @param d
	 */
	public HEC_Prism(final int n, final double r, final double d) {
		super();
		facets = n;
		thickness = d;
		radius = r;
	}

	/**
	 * Sets the facets.
	 *
	 * @param n
	 *            the n
	 * @return the hE c_ prism
	 */
	public HEC_Prism setFacets(final int n) {
		facets = n;
		return this;
	}

	/**
	 * Sets the height.
	 *
	 * @param d
	 *            the d
	 * @return the hE c_ prism
	 */
	public HEC_Prism setHeight(final double d) {
		thickness = d;
		return this;
	}

	/**
	 * Sets the radius.
	 *
	 * @param r
	 *            the r
	 * @return the hE c_ prism
	 */
	public HEC_Prism setRadius(final double r) {
		radius = r;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (facets < 3 || WB_Epsilon.isZero(radius)) {
			return null;
		}
		final WB_Point[] ppoints = new WB_Point[facets];
		for (int i = 0; i < facets; i++) {
			final double x = radius * Math.cos(Math.PI * 2.0 / facets * i);
			final double y = radius * Math.sin(Math.PI * 2.0 / facets * i);
			ppoints[i] = new WB_Point(x, y, 0.0);
		}
		final WB_Polygon polygon = gf.createSimplePolygon(ppoints);
		final WB_Vector norm = polygon.getPlane().getNormal();
		final int n = polygon.getNumberOfPoints();
		final boolean surf = WB_Epsilon.isZero(thickness);
		final WB_Coord[] points = new WB_Coord[surf ? n : 2 * n];
		for (int i = 0; i < n; i++) {
			points[i] = polygon.getPoint(i);
		}
		if (!surf) {
			for (int i = 0; i < n; i++) {
				points[n + i] = new WB_Point(points[i]).addMulSelf(thickness, norm);
			}
		}
		int[][] faces;
		if (surf) {
			faces = new int[1][n];
			for (int i = 0; i < n; i++) {
				faces[0][i] = i;
			}
		} else {
			faces = new int[n + 2][];
			faces[n] = new int[n];
			faces[n + 1] = new int[n];
			for (int i = 0; i < n; i++) {
				faces[n][i] = i;
				faces[n + 1][i] = 2 * n - 1 - i;
				faces[i] = new int[4];
				faces[i][0] = i;
				faces[i][3] = (i + 1) % n;
				faces[i][2] = n + (i + 1) % n;
				faces[i][1] = n + i;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(points).setFaces(faces).setCheckDuplicateVertices(false);
		return HE_MeshOp.flipFaces(fl.createBase());
	}
}
