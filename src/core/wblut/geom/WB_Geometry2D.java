package wblut.geom;

public interface WB_Geometry2D {
	public WB_Geometry2D apply2D(final WB_Transform2D T);

	public WB_Geometry2D apply2DSelf(final WB_Transform2D T);
}
