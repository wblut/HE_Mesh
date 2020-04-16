package wblut.hemesh;

import java.util.Iterator;

public class HE_VertexVertexRevCirculator implements Iterator<HE_Vertex> {
	private final HE_Halfedge _start;
	private HE_Halfedge _current;

	HE_VertexVertexRevCirculator(final HE_Vertex v) {
		_start = v.getHalfedge();
		_current = null;
	}

	@Override
	public boolean hasNext() {
		if (_start == null) {
			return false;
		}
		return _current == null || _current.getNextInVertex() != _start;
	}

	@Override
	public HE_Vertex next() {
		if (_current == null) {
			_current = _start;
		} else {
			_current = _current.getNextInVertex();
		}
		return _current.getEndVertex();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}