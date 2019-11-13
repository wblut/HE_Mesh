package wblut.geom;

public interface WB_Geometry extends WB_Geometry2D {
	public WB_Geometry apply(final WB_Transform T);

	public WB_Geometry applySelf(final WB_Transform T);
}
