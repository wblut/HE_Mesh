package wblut.hemesh;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

public class HEC_Polygon extends HEC_Creator {
	private List<WB_Polygon> polygon;
	private double thickness;
	private double offset;

	public HEC_Polygon() {
		super();
		setOverride(true);
	}

	public HEC_Polygon(final WB_Polygon poly, final double d) {
		this();
		setOverride(true);
		polygon = new FastList<>();
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

	public HEC_Polygon setPolygon(final WB_Polygon poly) {
		polygon = new FastList<>();
		polygon.add(poly);
		return this;
	}

	public HEC_Polygon setPolygon(final Collection<? extends WB_Polygon> polygons) {
		polygon = new FastList<>();
		polygon.addAll(polygons);
		return this;
	}

	public HEC_Polygon setPolygon(final WB_Polygon[] polygons) {
		polygon = new FastList<>();
		for (final WB_Polygon poly : polygons) {
			polygon.add(poly);
		}
		return this;
	}

	public HEC_Polygon setThickness(final double d) {
		thickness = d;
		return this;
	}

	public HEC_Polygon setOffset(final double d) {
		offset = d;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		if (polygon == null || polygon.size() == 0) {
			return new HE_Mesh();
		}
		final HE_Mesh result = new HE_Mesh(new HEC_FromPolygons().setPolygons(polygon));
		if (!WB_Epsilon.isZero(thickness)) {
			final WB_Vector N = polygon.get(0).getPlane().getNormal();
			result.moveSelf(N.mul(-offset));
			result.modify(new HEM_Shell().setThickness(thickness));
			// HET_MeshOp.flipFaces(result);
		}
		return result;
	}
}