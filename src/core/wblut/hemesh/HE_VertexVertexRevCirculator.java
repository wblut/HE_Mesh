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

public class HE_VertexVertexRevCirculator implements Iterator<HE_Vertex> {

	/**
	 *
	 */
	private final HE_Halfedge _start;

	/**
	 *
	 */
	private HE_Halfedge _current;

	/**
	 *
	 *
	 * @param v
	 */
	HE_VertexVertexRevCirculator(final HE_Vertex v) {
		_start = v.getHalfedge();
		_current = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (_start == null) {
			return false;
		}
		return _current == null || _current.getNextInVertex() != _start;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#next()
	 */
	@Override
	public HE_Vertex next() {
		if (_current == null) {
			_current = _start;
		} else {
			_current = _current.getNextInVertex();
		}
		return _current.getEndVertex();
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