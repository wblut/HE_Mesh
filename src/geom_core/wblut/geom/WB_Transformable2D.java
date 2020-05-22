package wblut.geom;

/**
 *
 */
public interface WB_Transformable2D {
	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Transformable2D apply2D(final WB_Transform2D T);

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	WB_Transformable2D apply2DSelf(final WB_Transform2D T);
}
