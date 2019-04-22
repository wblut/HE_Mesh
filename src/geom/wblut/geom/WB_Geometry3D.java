package wblut.geom;

public interface WB_Geometry3D extends WB_Geometry2D {
	public WB_Geometry3D apply(final WB_Transform3D T);

	public WB_Geometry3D applySelf(final WB_Transform3D T);
}
