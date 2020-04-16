package wblut.hemesh;

import java.util.Iterator;

public class HE_FaceHalfedgeInnerRevCirculator implements Iterator<HE_Halfedge> {
	private final HE_Halfedge _start;
	private HE_Halfedge _current;

	HE_FaceHalfedgeInnerRevCirculator(final HE_Face f) {
		_start = f.getHalfedge();
		_current = null;
	}

	@Override
	public boolean hasNext() {
		if (_start == null) {
			return false;
		}
		return (_current == null || _current.getPrevInFace() != _start) && _start != null;
	}

	@Override
	public HE_Halfedge next() {
		if (_current == null) {
			_current = _start;
		} else {
			_current = _current.getPrevInFace();
		}
		return _current;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}