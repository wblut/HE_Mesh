package wblut.hemesh;

import wblut.geom.WB_Circle;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

public class HEC_Disk extends HEC_Creator {
	public HEC_Disk() {
		super();
		parameters.set("circle", null);
		parameters.set("radius", 0.0);
		parameters.set("phase", 0.0);
		parameters.set("facets", 36);
		parameters.set("thickness", 0.0);
		parameters.set("offset", 0.0);
	}

	public HEC_Disk setRadius(final double r) {
		parameters.set("radius", Math.abs(r));
		return this;
	}

	public HEC_Disk setPhase(final double phase) {
		parameters.set("phase", phase);
		return this;
	}

	public HEC_Disk setFacets(final int N) {
		parameters.set("facets", N);
		return this;
	}

	public HEC_Disk setThickness(final double thickness) {
		parameters.set("thickness", thickness);
		return this;
	}

	public HEC_Disk setOffset(final double offset) {
		parameters.set("offset", offset);
		return this;
	}

	public HEC_Disk setCircle(final WB_Circle circle) {
		parameters.set("circle", circle);
		return this;
	}

	double getRadius() {
		return parameters.get("radius", 0.0);
	}

	double getPhase() {
		return parameters.get("phase", 0.0);
	}

	int getFacets() {
		return parameters.get("facets", 6);
	}

	WB_Vector getNormal() {
		return (WB_Vector) parameters.get("normal", new WB_Vector(0, 0, 1));
	}

	double getThickness() {
		return parameters.get("thickness", 0.0);
	}

	double getOffset() {
		return parameters.get("offset", 0.0);
	}

	WB_Circle getCircle() {
		return (WB_Circle) parameters.get("circle", null);
	}

	@Override
	protected HE_Mesh createBase() {
		WB_Circle circle = getCircle();
		final double radius = getRadius();
		final double phase = getPhase();
		final double thickness = getThickness();
		final double offset = getOffset();
		final WB_Coord normal = getAxis();
		final int N = getFacets();
		if (radius == 0 && circle == null) {
			return new HE_Mesh();
		}
		if (circle == null) {
			circle = new WB_Circle(getCenter(), normal, radius);
		}
		final WB_Point[] points = circle.getPoints(N, phase);
		final HEC_Polygon pc = new HEC_Polygon().setPolygon(new WB_Polygon(points)).setThickness(thickness)
				.setOffset(offset);
		return pc.createBase();
	}
}
