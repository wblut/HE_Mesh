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
public class HE_VertexIterator implements Iterator<HE_Vertex> {

	/**
	 *
	 */
	Iterator<HE_Vertex> _itr;

	/**
	 *
	 *
	 * @param vertices
	 */
	HE_VertexIterator(final Collection<HE_Vertex> vertices) {
		_itr = vertices.iterator();
	}

	HE_VertexIterator(final HE_HalfedgeStructure mesh) {
		_itr = mesh.vItr();
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
	public HE_Vertex next() {
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
