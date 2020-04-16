package wblut.geom;

public interface WB_Transformable4D {
	WB_Vector4D rotateXWSelf(double angle);

	WB_Vector4D rotateXYSelf(double angle);

	WB_Vector4D rotateXZSelf(double angle);

	WB_Vector4D rotateYWSelf(double angle);

	WB_Vector4D rotateYZSelf(double angle);

	WB_Vector4D rotateZWSelf(double angle);
}
