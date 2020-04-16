package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

public class HE_EdgeIterator implements Iterator<HE_Halfedge> {
	Iterator<HE_Halfedge> _itr;

	HE_EdgeIterator(final Collection<HE_Halfedge> edges) {
		_itr = edges.iterator();
	}

	@Override
	public boolean hasNext() {
		return _itr.hasNext();
	}

	@Override
	public HE_Halfedge next() {
		return _itr.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
