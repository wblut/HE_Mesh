package wblut.geom;

public interface WB_Transformable2D {
	WB_Transformable2D apply2D(final WB_Transform2D T);

	WB_Transformable2D apply2DSelf(final WB_Transform2D T);
}
