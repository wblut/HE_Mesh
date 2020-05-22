package wblut.hemesh;

import java.util.Iterator;

/**
 *
 */
public class HE_VertexFaceRevCirculator implements Iterator<HE_Face> {
	/**  */
	private final HE_Halfedge _start;
	/**  */
	private HE_Halfedge _current;

	/**
	 *
	 *
	 * @param v
	 */
	HE_VertexFaceRevCirculator(final HE_Vertex v) {
		_start = v.getHalfedge();
		_current = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public boolean hasNext() {
		if (_start == null) {
			return false;
		}
		return (_current == null || _current.getPrevInVertex() != _start) && _start != null;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Face next() {
		if (_current == null) {
			_current = _start;
		} else {
			_current = _current.getPrevInVertex();
		}
		return _current.getFace();
	}

	/**
	 *
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}