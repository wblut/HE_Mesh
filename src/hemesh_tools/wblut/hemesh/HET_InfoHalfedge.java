package wblut.hemesh;

/**
 *
 *
 * @param <E>
 */
public interface HET_InfoHalfedge<E extends Object> {
	/**
	 *
	 *
	 * @param halfedge
	 * @return
	 */
	E retrieve(final HE_Halfedge halfedge);
}