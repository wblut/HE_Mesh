package wblut.hemesh;

import java.util.Iterator;

/**
 *
 */
public class HE_FaceHalfedgeOuterRevCirculator implements Iterator<HE_Halfedge> {
	/**  */
	private final HE_Halfedge _start;
	/**  */
	private HE_Halfedge _current;

	/**
	 *
	 *
	 * @param f
	 */
	HE_FaceHalfedgeOuterRevCirculator(final HE_Face f) {
		_start = f.getHalfedge();
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
		return (_current == null || _current.getNextInFace() != _start) && _start != null;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Halfedge next() {
		if (_current == null) {
			_current = _start;
		} else {
			_current = _current.getNextInFace();
		}
		return _current.getPair();
	}

	/**
	 *
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}