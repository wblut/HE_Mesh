package wblut.hemesh;

/**
 *
 *
 * @param <E>
 */
public interface HET_InfoEdge<E extends Object> {
	/**
	 *
	 *
	 * @param edge
	 * @return
	 */
	E retrieve(final HE_Halfedge edge);
}