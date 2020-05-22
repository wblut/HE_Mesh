package wblut.hemesh;

import java.util.Iterator;

/**
 *
 */
public class HE_MeshIterator implements Iterator<HE_Mesh> {
	/**  */
	Iterator<HE_Mesh> _itr;

	/**
	 *
	 *
	 * @param object
	 */
	public HE_MeshIterator(final HE_MeshCollection object) {
		_itr = object.meshes.iterator();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public boolean hasNext() {
		return _itr.hasNext();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh next() {
		return _itr.next();
	}

	/**
	 *
	 */
	@Override
	public void remove() {
		_itr.remove();
	}
}
