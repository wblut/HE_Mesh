/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

/**
 *
 */
public class HE_PathHalfedge extends HE_MeshElement {
	/**
	 *
	 */
	private HE_Halfedge _he;
	/**
	 *
	 */
	private HE_PathHalfedge _next;
	/**
	 *
	 */
	private HE_PathHalfedge _prev;

	/**
	 *
	 */
	public HE_PathHalfedge() {
		super();
	}

	/**
	 *
	 *
	 * @param he
	 */
	public HE_PathHalfedge(final HE_Halfedge he) {
		super();
		_he = he;
	}

	/**
	 *
	 */
	public void clearNext() {
		if (_next != null) {
			_next.clearPrev();
		}
		_next = null;
	}

	/**
	 *
	 */
	private void clearPrev() {
		_prev = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Halfedge getHalfedge() {
		return _he;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Vertex getVertex() {
		return _he.getVertex();
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Vertex getStartVertex() {
		return _he.getVertex();
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Vertex getEndVertex() {
		return _he.getEndVertex();
	}

	/**
	 *
	 *
	 * @param he
	 */
	public void setHalfedge(final HE_Halfedge he) {
		_he = he;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_PathHalfedge getNextInPath() {
		return _next;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_PathHalfedge getPrevInPath() {
		return _prev;
	}

	/**
	 *
	 *
	 * @return
	 */
	public Long key() {
		return super.getKey();
	}

	/**
	 *
	 *
	 * @param he
	 */
	public void setNext(final HE_PathHalfedge he) {
		_next = he;
	}

	/**
	 *
	 *
	 * @param he
	 */
	public void setPrev(final HE_PathHalfedge he) {
		_prev = he;
	}

	@Override
	public String toString() {
		return "HE_PathHalfedge key: " + key() + ".";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Element#clear()
	 */
	@Override
	public void clear() {
		_he = null;
		_next = null;
		_prev = null;
	}

	@Override
	public void clearPrecomputed() {

	}
}
