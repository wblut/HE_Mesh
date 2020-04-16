package wblut.geom;

public interface WB_Transformable3D extends WB_Transformable2D {
	WB_Transformable3D apply(final WB_Transform3D T);

	WB_Transformable3D applySelf(final WB_Transform3D T);
}
