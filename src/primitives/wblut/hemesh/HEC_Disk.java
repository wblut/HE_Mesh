/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.geom.WB_Circle;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;

/**
 * @author FVH
 *
 */
public class HEC_Disk extends HEC_Creator {

	WB_Circle circle;
	double radius;
	double phase;
	double thickness;
	double offset;
	WB_Coord center;
	WB_Coord normal;
	int N;

	public HEC_Disk() {
		super();
		circle = null;
		radius = 0;
		center = new WB_Point(0, 0, 0);
		normal = new WB_Vector(0, 0, 1);
		phase = 0;
		N = 36;
		thickness = 0;
		offset = 0;

	}

	public HEC_Disk setRadius(final double r) {
		radius = Math.abs(r);
		return this;

	}

	public HEC_Disk setPhase(final double phase) {
		this.phase = phase;
		return this;

	}

	public HEC_Disk setN(final int N) {
		this.N = N;
		return this;

	}

	@Override
	public HEC_Disk setCenter(final WB_Coord center) {
		this.center = center;
		return this;

	}

	public HEC_Disk setNormal(final WB_Coord normal) {
		this.normal = normal;
		return this;

	}

	public HEC_Disk setThickness(final double thickness) {
		this.thickness = thickness;
		return this;

	}

	public HEC_Disk setOffset(final double offset) {
		this.offset = offset;
		return this;

	}

	public HEC_Disk setCircle(final WB_Circle circle) {
		this.circle = circle;
		return this;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (radius == 0 && circle == null) {
			return new HE_Mesh();
		}
		if (circle == null) {
			circle = new WB_Circle(center, normal, radius);
		}
		WB_Point[] points = circle.getPoints(N, phase);

		HEC_Polygon pc = new HEC_Polygon().setPolygon(new WB_Polygon(points)).setThickness(thickness).setOffset(offset);

		return pc.createBase();
	}

}
