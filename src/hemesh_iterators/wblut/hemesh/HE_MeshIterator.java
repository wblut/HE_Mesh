package wblut.hemesh;

import java.util.Iterator;

public class HE_MeshIterator implements Iterator<HE_Mesh> {
	Iterator<HE_Mesh> _itr;

	public HE_MeshIterator(final HE_MeshCollection object) {
		_itr = object.meshes.iterator();
	}

	@Override
	public boolean hasNext() {
		return _itr.hasNext();
	}

	@Override
	public HE_Mesh next() {
		return _itr.next();
	}

	@Override
	public void remove() {
		_itr.remove();
	}
}
