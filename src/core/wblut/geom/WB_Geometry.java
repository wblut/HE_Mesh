package wblut.geom;

public interface WB_Geometry extends WB_Geometry2D {
	public WB_Geometry apply(final WB_Transform3D T);

	public WB_Geometry applySelf(final WB_Transform3D T);
}
