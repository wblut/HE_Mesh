package wblut.geom;

/**
 *
 */
public interface WB_Transformable4D {
	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Vector4D rotateXWSelf(double angle);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Vector4D rotateXYSelf(double angle);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Vector4D rotateXZSelf(double angle);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Vector4D rotateYWSelf(double angle);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Vector4D rotateYZSelf(double angle);

	/**
	 *
	 *
	 * @param angle
	 * @return
	 */
	WB_Vector4D rotateZWSelf(double angle);
}
