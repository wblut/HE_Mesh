/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEC_Polygon extends HEC_Creator {

	/**
	 *
	 */
	private List<WB_Polygon> polygon;

	/**
	 *
	 */
	private double thickness;
	private double offset;

	/**
	 *
	 */
	public HEC_Polygon() {
		super();
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 */
	public HEC_Polygon(final WB_Polygon poly, final double d) {
		this();
		setOverride(true);
		polygon = new FastList<WB_Polygon>();
		polygon.add(poly);
		thickness = d;
		offset = 0;
	}

	public HEC_Polygon(final Collection<? extends WB_Polygon> polygons, final double d) {
		this();
		setOverride(true);
		setPolygon(polygons);
		thickness = d;
		offset = 0;
	}

	public HEC_Polygon(final WB_Polygon[] polygons, final double d) {
		this();
		setOverride(true);
		setPolygon(polygons);
		thickness = d;
		offset = 0;
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	public HEC_Polygon setPolygon(final WB_Polygon poly) {
		polygon = new FastList<WB_Polygon>();
		polygon.add(poly);
		return this;
	}

	/**
	 *
	 * @param polygons
	 * @return
	 */
	public HEC_Polygon setPolygon(final Collection<? extends WB_Polygon> polygons) {
		polygon = new FastList<WB_Polygon>();
		polygon.addAll(polygons);
		return this;
	}

	/**
	 *
	 * @param polygons
	 * @return
	 */
	public HEC_Polygon setPolygon(final WB_Polygon[] polygons) {
		polygon = new FastList<WB_Polygon>();
		for (WB_Polygon poly : polygons) {
			polygon.add(poly);
		}
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEC_Polygon setThickness(final double d) {
		thickness = d;
		return this;
	}

	public HEC_Polygon setOffset(final double d) {
		offset = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (polygon == null || polygon.size() == 0) {
			return new HE_Mesh();
		}
		HE_Mesh result = new HE_Mesh(new HEC_FromPolygons().setPolygons(polygon));
		if (!WB_Epsilon.isZero(thickness)) {
			WB_Vector N = polygon.get(0).getPlane().getNormal();
			result.moveSelf(N.mul(-offset));
			result.modify(new HEM_Shell().setThickness(thickness));
			// HET_MeshOp.flipFaces(result);
		}
		return result;

	}
}
