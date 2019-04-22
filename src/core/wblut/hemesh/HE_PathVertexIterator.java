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

/**
 *
 */
public class HE_PathVertexIterator implements Iterator<HE_Vertex> {

	/**
	 * 
	 */
	private final HE_PathHalfedge _start;

	/**
	 * 
	 */
	private HE_PathHalfedge _current;

	/**
	 * 
	 */
	private boolean endreached;

	/**
	 * 
	 *
	 * @param path
	 */
	public HE_PathVertexIterator(final HE_Path path) {
		_start = path.getPathHalfedge();
		_current = null;
		if (_start == null) {
			endreached = true;
		} else {
			endreached = _start.getNextInPath() == null || _start.getNextInPath() == _start;
		}
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
		return _current == null || _current.getNextInPath() != _start && !endreached;
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
			if (_current.getNextInPath() == null || _current.getNextInPath() == _start) {
				endreached = true;
			} else {
				_current = _current.getNextInPath();
			}
		}
		return endreached ? _current.getHalfedge().getEndVertex() : _current.getHalfedge().getVertex();
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
