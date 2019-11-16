/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(includeFieldNames=true)
public class WB_Quad implements WB_Geometry {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();
	/** First point. */
	@Setter(AccessLevel.NONE) private WB_Point				p1;
	/** Second point. */
	@Setter(AccessLevel.NONE) private WB_Point				p2;
	/** Third point. */
	@Setter(AccessLevel.NONE) private  WB_Point				p3;
	/** Fourth point. */
	@Setter(AccessLevel.NONE) private WB_Point				p4;

	/**
	 * Instantiates a new WB_Quad. No copies are made.
	 *
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @param p3
	 *            third point
	 * @param p4
	 *            fourth point
	 */
	public WB_Quad(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3,
			final WB_Coord p4) {
		this.p1 = geometryfactory.createPoint(p1);
		this.p2 = geometryfactory.createPoint(p2);
		this.p3 = geometryfactory.createPoint(p3);
		this.p4 = geometryfactory.createPoint(p4);
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static int[] triangulateQuad(final WB_Coord p0, final WB_Coord p1,
			final WB_Coord p2, final WB_Coord p3) {
		final boolean p0inside = WB_GeometryOp.pointInTriangleBary3D(p0, p1,
				p2, p3);
		final boolean p2inside = WB_GeometryOp.pointInTriangleBary3D(p2, p0,
				p1, p3);
		if (p0inside || p2inside) {
			return new int[] { 0, 1, 2, 0, 2, 3 };
		} else {
			return new int[] { 0, 1, 3, 1, 2, 3 };
		}
	}

	public boolean isConvex() {
		// return WB_GeometryOp.getIntersection3D(new WB_Segment(p1, p3), new
		// WB_Segment(p2, p4)).intersection;
		if (WB_GeometryOp.pointInTriangleBary3D(p1, p2, p3, p4)) {
			return false;
		}
		if (WB_GeometryOp.pointInTriangleBary3D(p2, p1, p3, p4)) {
			return false;
		}
		if (WB_GeometryOp.pointInTriangleBary3D(p3, p1, p2, p4)) {
			return false;
		}
		if (WB_GeometryOp.pointInTriangleBary3D(p4, p1, p2, p3)) {
			return false;
		}
		return true;
	}

	public void cycle() {
		WB_Point tmp = p1;
		p1 = p2;
		p2 = p3;
		p3 = p4;
		p4 = tmp;
	}

	public void cycle(int n) {
		while (n >= 4) {
			n -= 4;
		}
		while (n < 0) {
			n += 4;
		}
		for (int i = 0; i < n; i++) {
			cycle();
		}
	}

	@Override
	public WB_Quad apply2D(WB_Transform2D T) {
		return new WB_Quad(p1.apply2D(T), p2.apply2D(T), p3.apply2D(T),
				p4.apply2D(T));
	}

	@Override
	public WB_Quad apply2DSelf(WB_Transform2D T) {
		p1.apply2DSelf(T);
		p2.apply2DSelf(T);
		p3.apply2DSelf(T);
		p4.apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Quad apply(WB_Transform3D T) {
		return new WB_Quad(p1.apply(T), p2.apply(T), p3.apply(T), p4.apply(T));
	}

	@Override
	public WB_Quad applySelf(WB_Transform3D T) {
		p1.applySelf(T);
		p2.applySelf(T);
		p3.applySelf(T);
		p4.applySelf(T);
		return this;
	}
}