package wblut.hemesh;

import java.util.Iterator;

/**
 *
 */
public class HE_PathHalfedgeInnerIterator implements Iterator<HE_Halfedge> {
	/**  */
	private final HE_PathHalfedge _start;
	/**  */
	private HE_PathHalfedge _current;

	/**
	 *
	 *
	 * @param path
	 */
	public HE_PathHalfedgeInnerIterator(final HE_Path path) {
		_start = path.getPathHalfedge();
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
		return _current == null || _current.getNextInPath() != _start && _current.getNextInPath() != null;
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
			_current = _current.getNextInPath();
		}
		return _current.getHalfedge();
	}

	/**
	 *
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}