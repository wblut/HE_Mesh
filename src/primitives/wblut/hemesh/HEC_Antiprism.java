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
 * @author Frederik Vanhoutte, W:Blut
 *
 *         Creates an antiprism. If a thickness is specified the result is a
 *         solid, otherwise it's a surface, a regular polygon.
 */
public class HEC_Antiprism extends HEC_Creator {
	/**
	 *
	 */
	private static WB_GeometryFactory gf = new WB_GeometryFactory();
	private int facets;
	private double thickness;
	private double radius;

	public HEC_Antiprism() {
		super();
		facets = 5;
		thickness = 100;
		radius = 100;
	}

	/**
	 *
	 * @param n
	 * @param r
	 * @param d
	 */
	public HEC_Antiprism(final int n, final double r, final double d) {
		this();
		facets = n;
		thickness = d;
		radius = r;
	}

	/**
	 * Sets the facets.
	 *
	 * @param n
	 *
	 * @return
	 */
	public HEC_Antiprism setFacets(final int n) {
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
	public HEC_Antiprism setHeight(final double d) {
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
	public HEC_Antiprism setRadius(final double r) {
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
				points[n + i] = new WB_Point(points[i]).addMulSelf(thickness, norm)
						.rotateAboutAxis2PSelf(Math.PI / facets, 0, 0, 0, 0, 0, 1);
			}
		}
		int[][] faces;
		if (surf) {
			faces = new int[1][n];
			for (int i = 0; i < n; i++) {
				faces[0][i] = i;
			}
		} else {
			faces = new int[2 * n + 2][];
			faces[2 * n] = new int[n];
			faces[2 * n + 1] = new int[n];
			for (int i = 0; i < n; i++) {
				faces[2 * n][i] = i;
				faces[2 * n + 1][i] = 2 * n - 1 - i;
				faces[2 * i] = new int[3];
				faces[2 * i][0] = i;
				faces[2 * i][1] = n + i;
				faces[2 * i][2] = (i + 1) % n;
				faces[2 * i + 1] = new int[3];
				faces[2 * i + 1][0] = i + n;
				faces[2 * i + 1][2] = (i + 1) % n;
				faces[2 * i + 1][1] = n + (i + 1) % n;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(points).setFaces(faces).setCheckDuplicateVertices(false);
		return HE_MeshOp.flipFaces(fl.createBase());
	}
}
