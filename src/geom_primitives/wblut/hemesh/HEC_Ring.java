package wblut.hemesh;

import java.util.List;

import wblut.geom.WB_Circle;
import wblut.geom.WB_Coord;
import wblut.geom.WB_List;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

public class HEC_Ring extends HEC_Creator {
	double innerRadius, outerRadius;
	double phase;
	double thickness;
	double offset;
	WB_Coord normal;
	int N;

	public HEC_Ring() {
		super();
		innerRadius = outerRadius = 0;
		normal = new WB_Vector(0, 0, 1);
		phase = 0;
		N = 36;
		thickness = 0;
		offset = 0;
	}

	public HEC_Ring setRadius(final double ir, final double or) {
		innerRadius = Math.min(Math.abs(ir), Math.abs(or));
		outerRadius = Math.max(Math.abs(ir), Math.abs(or));
		return this;
	}

	public HEC_Ring setPhase(final double phase) {
		this.phase = phase;
		return this;
	}

	public HEC_Ring setN(final int N) {
		this.N = N;
		return this;
	}

	public HEC_Ring setNormal(final WB_Coord normal) {
		this.normal = normal;
		return this;
	}

	public HEC_Ring setThickness(final double thickness) {
		this.thickness = thickness;
		return this;
	}

	public HEC_Ring setOffset(final double offset) {
		this.offset = offset;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		if (innerRadius == 0 && outerRadius == 0) {
			return new HE_Mesh();
		}
		if (innerRadius == 0) {
			final HEC_Disk dc = new HEC_Disk().setRadius(outerRadius).setFacets(N).setOffset(offset).setPhase(phase)
					.setThickness(thickness);
			dc.setCenter(getCenter());
			dc.setAxis(getAxis());
			return dc.createBase();
		}
		final WB_Circle innerCircle = new WB_Circle(getCenter(), normal, innerRadius);
		final WB_Circle outerCircle = new WB_Circle(getCenter(), normal, outerRadius);
		final WB_Point[] innerPoints = innerCircle.getPoints(N, phase);
		final WB_Point[] outerPoints = outerCircle.getPoints(N, phase);
		final List<WB_Polygon> polygons = new WB_List<>();
		for (int i = 0; i < N; i++) {
			polygons.add(
					new WB_Polygon(innerPoints[i], innerPoints[(i + 1) % N], outerPoints[(i + 1) % N], outerPoints[i]));
		}
		final HEC_Polygon pc = new HEC_Polygon().setPolygon(polygons).setThickness(thickness).setOffset(offset);
		return pc.createBase();
	}
}
