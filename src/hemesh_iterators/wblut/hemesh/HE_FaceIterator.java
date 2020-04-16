package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

public class HE_FaceIterator implements Iterator<HE_Face> {
	Iterator<HE_Face> _itr;

	HE_FaceIterator(final Collection<HE_Face> faces) {
		_itr = faces.iterator();
	}

	HE_FaceIterator(final HE_HalfedgeStructure mesh) {
		_itr = mesh.fItr();
	}

	@Override
	public boolean hasNext() {
		return _itr.hasNext();
	}

	@Override
	public HE_Face next() {
		return _itr.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}