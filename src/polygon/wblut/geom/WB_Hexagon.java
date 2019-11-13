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
public class WB_Hexagon implements WB_Geometry {
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();
	@Setter(AccessLevel.NONE) private WB_Point				p1;
	@Setter(AccessLevel.NONE) private WB_Point				p2;
	@Setter(AccessLevel.NONE) private WB_Point				p3;
	@Setter(AccessLevel.NONE) private WB_Point				p4;
	@Setter(AccessLevel.NONE) private WB_Point				p5;
	@Setter(AccessLevel.NONE) private WB_Point				p6;

	
	public WB_Hexagon(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3,
			final WB_Coord p4, final WB_Coord p5, final WB_Coord p6) {
		this.p1 = geometryfactory.createPoint(p1);
		this.p2 = geometryfactory.createPoint(p2);
		this.p3 = geometryfactory.createPoint(p3);
		this.p4 = geometryfactory.createPoint(p4);
		this.p5 = geometryfactory.createPoint(p5);
		this.p6 = geometryfactory.createPoint(p6);
	}

	public void cycle() {
		WB_Point tmp = p1;
		p1 = p2;
		p2 = p3;
		p3 = p4;
		p4 = p5;
		p5 = p6;
		p6 = tmp;
	}

	public void cycle(int n) {
		while (n >= 6) {
			n -= 6;
		}
		while (n < 0) {
			n += 6;
		}
		for (int i = 0; i < n; i++) {
			cycle();
		}
	}

	@Override
	public WB_Hexagon apply2D(WB_Transform2D T) {
		return new WB_Hexagon(p1.apply2D(T), p2.apply2D(T), p3.apply2D(T),
				p4.apply2D(T), p5.apply2D(T), p6.apply2D(T));
	}

	@Override
	public WB_Hexagon apply2DSelf(WB_Transform2D T) {
		p1.apply2DSelf(T);
		p2.apply2DSelf(T);
		p3.apply2DSelf(T);
		p4.apply2DSelf(T);
		p5.apply2DSelf(T);
		p6.apply2DSelf(T);
		return this;
	}

	@Override
	public WB_Hexagon apply(WB_Transform T) {
		return new WB_Hexagon(p1.apply(T), p2.apply(T), p3.apply(T),
				p4.apply(T), p5.apply(T), p6.apply(T));
	}

	@Override
	public WB_Hexagon applySelf(WB_Transform T) {
		p1.applySelf(T);
		p2.applySelf(T);
		p3.applySelf(T);
		p4.applySelf(T);
		p5.applySelf(T);
		p6.applySelf(T);
		return this;
	}
}