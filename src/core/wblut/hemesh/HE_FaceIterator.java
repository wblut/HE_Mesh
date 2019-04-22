/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 */
public class HE_FaceIterator implements Iterator<HE_Face> {

	/**
	 *
	 */
	Iterator<HE_Face> _itr;

	/**
	 *
	 *
	 * @param faces
	 */
	HE_FaceIterator(final Collection<HE_Face> faces) {
		_itr = faces.iterator();
	}

	HE_FaceIterator(final HE_HalfedgeStructure mesh) {
		_itr = mesh.fItr();
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
	public HE_Face next() {
		return _itr.next();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}