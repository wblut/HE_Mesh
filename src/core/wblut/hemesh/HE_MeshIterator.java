/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Iterator;

public class HE_MeshIterator implements Iterator<HE_Mesh> {
	/**
	 *
	 */
	Iterator<HE_Mesh> _itr;

	/**
	 *
	 *
	 * @param object
	 */
	public HE_MeshIterator(final HE_MeshCollection object) {
		_itr = object.meshes.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return _itr.hasNext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public HE_Mesh next() {
		return _itr.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		_itr.remove();
	}
}
