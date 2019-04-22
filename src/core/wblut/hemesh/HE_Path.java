/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * A HE_Path is a sequence of edges, or rather halfedges, in a mesh. It can be a
 * loop or open.
 *
 * A HE_Path consists of a double linked list of HE_PathHalfedge, a wrapper for
 * a HE_Halfedge that can have different connectivity than the HE_Halfedge
 * itself. The constructors do not check if the path is valid, i.e. a
 * non-interrupted loop or sequence of halfedges.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class HE_Path extends HE_MeshElement {
	/**
	 * The HE_PathHalfedge that is the start of this path. If getPrevInPath() is
	 * null, the path is assumed to be open. If getPrevInPath() is not null, the
	 * path should be a single loop
	 */
	protected HE_PathHalfedge _phalfedge;

	/**
	 * Create a looping path using the halfedge 'loop' as starting point. The
	 * path is created by calling getNextInFace() until 'loop' is reached.
	 *
	 * @param loop
	 *            starting halfegde;
	 */
	public HE_Path(final HE_Halfedge loop) {
		super();
		_phalfedge = new HE_PathHalfedge(loop);
		HE_Halfedge he = loop;
		final HE_PathHalfedge first = _phalfedge;
		HE_PathHalfedge current = first;
		HE_PathHalfedge next;
		while (he.getNextInFace() != loop) {
			next = new HE_PathHalfedge(he = he.getNextInFace());
			current.setNext(next);
			next.setPrev(current);
			current = next;
		}
		current.setNext(first);
		first.setPrev(current);
	}

	/**
	 * Create a looping path from a single face .
	 *
	 * @param face
	 *            single face
	 */
	public HE_Path(final HE_Face face) {
		this(face.getHalfedge());
	}

	/**
	 * Create a looping path around a single vertex.
	 *
	 * @param v
	 *            single vertex
	 */
	public HE_Path(final HE_Vertex v) {
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		HE_Halfedge hev = v.getHalfedge();
		HE_Halfedge circuit;
		do {
			circuit = hev.getNextInFace();
			while (circuit.getPair() != hev.getPrevInVertex()) {
				halfedges.add(circuit);
				circuit = circuit.getNextInFace();
			}
			hev = hev.getPrevInVertex();
		} while (hev != v.getHalfedge());
		createFromList(halfedges, true);
	}

	public static HE_Path getShortestPath(final HE_Vertex v0,
			final HE_Vertex v1, final HE_Mesh mesh) {
		if (!mesh.contains(v0) || !mesh.contains(v1) || v0 == v1) {
			return null;
		}
		HET_MeshGraph graph = new HET_MeshGraph(mesh);
		int[] shortestpath = graph.getShortestPathBetweenVertices(
				mesh.getIndex(v0), mesh.getIndex(v1));
		return HE_MeshOp.createPathFromIndices(mesh, shortestpath, false);
	}

	/**
	 * Create a path from a list of halfedges. The list is assumed to be a
	 * proper sequence or loop. No checking is performed.
	 *
	 * @param halfedges
	 *            List of HE_Halfedge
	 * @param loop
	 *            true/false, is the list supposed to be a loop?
	 */
	public HE_Path(final List<HE_Halfedge> halfedges, final boolean loop) {
		super();
		createFromList(halfedges, loop);
	}

	/**
	 * Internally creates a path from a list of halfedges. The list is
	 * assumed to be a proper sequence or loop. No checking is performed.
	 *
	 * @param halfedges
	 *            List of HE_Halfedge
	 * @param loop
	 *            true/false, is the list supposed to be a loop?
	 */
	private void createFromList(final List<HE_Halfedge> halfedges,
			final boolean loop) {
		_phalfedge = new HE_PathHalfedge(halfedges.get(0));
		HE_PathHalfedge current = _phalfedge;
		HE_PathHalfedge next;
		for (int i = 1; i < halfedges.size(); i++) {
			next = new HE_PathHalfedge(halfedges.get(i));
			current.setNext(next);
			next.setPrev(current);
			current = next;
		}
		if (loop) {
			current.setNext(_phalfedge);
			_phalfedge.setPrev(current);
		}
	}

	/**
	 *
	 * @return unique key of HE_Path element
	 */
	public long key() {
		return super.getKey();
	}

	/**
	 *
	 * @return number of halfedges in path. If the path is not a loop, care
	 *         should be taken to include the end vertex of the last halfedge in
	 *         the path.
	 */
	public int getPathOrder() {
		int result = 0;
		if (_phalfedge == null) {
			return 0;
		}
		HE_PathHalfedge he = _phalfedge;
		do {
			result++;
			he = he.getNextInPath();
		} while (he != _phalfedge && he != null);
		return result;
	}

	/**
	 *
	 * @return total length of path
	 */
	public double getPathLength() {
		double result = 0;
		if (_phalfedge == null) {
			return result;
		}
		HE_PathHalfedge he = _phalfedge;
		do {
			result += HE_MeshOp.getLength(he.getHalfedge());
			he = he.getNextInPath();
		} while (he != _phalfedge && he != null);
		return result;
	}

	/**
	 *
	 * @return array containing incremental lengths, first element is always 0
	 */
	public double[] getPathIncLengths() {
		final double[] result = new double[getPathOrder() + 1];
		if (_phalfedge == null) {
			return result;
		}
		HE_PathHalfedge he = _phalfedge;
		result[0] = 0;
		int i = 1;
		do {
			result[i] = result[i - 1] + HE_MeshOp.getLength(he.getHalfedge());
			he = he.getNextInPath();
			i++;
		} while (he != _phalfedge && he != null);
		return result;
	}

	/**
	 *
	 * @return halfedges of path as List
	 */
	public List<HE_Halfedge> getHalfedges() {
		final List<HE_Halfedge> fhe = new FastList<HE_Halfedge>();
		if (_phalfedge == null) {
			return fhe;
		}
		HE_PathHalfedge he = _phalfedge;
		do {
			if (!fhe.contains(he.getHalfedge())) {
				fhe.add(he.getHalfedge());
			}
			he = he.getNextInPath();
		} while (he != _phalfedge && he != null);
		return fhe;
	}

	/**
	 *
	 * @return vertices of path as List. Includes end vertex of an open path.
	 */
	public List<HE_Vertex> getPathVertices() {
		final List<HE_Vertex> fhe = new FastList<HE_Vertex>();
		if (_phalfedge == null) {
			return fhe;
		}
		HE_PathHalfedge he = _phalfedge;
		do {
			if (!fhe.contains(he.getVertex())) {
				fhe.add(he.getVertex());
			}
			if (he.getNextInPath() == null) {
				fhe.add(he.getEndVertex());
			}
			he = he.getNextInPath();
		} while (he != _phalfedge && he != null);
		return fhe;
	}

	/**
	 *
	 * @return the edges of the path
	 */
	public List<HE_Halfedge> getPathEdges() {
		final List<HE_Halfedge> fe = new FastList<HE_Halfedge>();
		if (_phalfedge == null) {
			return fe;
		}
		HE_PathHalfedge he = _phalfedge;
		do {
			if (he.getHalfedge().isEdge()) {
				fe.add(he.getHalfedge());
			} else {
				fe.add(he.getHalfedge().getPair());
			}
			he = he.getNextInPath();
		} while (he != _phalfedge && he != null);
		return fe;
	}

	/**
	 *
	 * @return get the starting HE_PathHalfedge
	 */
	public HE_PathHalfedge getPathHalfedge() {
		return _phalfedge;
	}

	/**
	 * Set the starting HE_PathHalfedge.
	 *
	 * @param phalfedge
	 */
	public void setPathHalfedge(final HE_PathHalfedge phalfedge) {
		_phalfedge = phalfedge;
	}

	/**
	 * Clear the HE_PathHalfedge.
	 */
	public void clearPathHalfedge() {
		_phalfedge = null;
	}

	/**
	 *
	 * @return get all faces belonging to the path halfedges
	 */
	public List<HE_Face> getPathInnerFaces() {
		final List<HE_Face> ff = new FastList<HE_Face>();
		if (getPathHalfedge() == null) {
			return ff;
		}
		HE_PathHalfedge lhe = _phalfedge;
		HE_Halfedge he;
		do {
			he = lhe.getHalfedge();
			if (he.getFace() != null) {
				if (!ff.contains(he.getFace())) {
					ff.add(he.getFace());
				}
			}
			lhe = lhe.getNextInPath();
		} while (lhe != _phalfedge && lhe != null);
		return ff;
	}

	/**
	 *
	 * @return get all faces belonging to the pairs of the path halfedges
	 */
	public List<HE_Face> getPathOuterFaces() {
		final List<HE_Face> ff = new FastList<HE_Face>();
		if (getPathHalfedge() == null) {
			return ff;
		}
		HE_PathHalfedge lhe = _phalfedge;
		HE_Halfedge hep;
		do {
			hep = lhe.getHalfedge().getPair();
			if (hep.getFace() != null) {
				if (!ff.contains(hep.getFace())) {
					ff.add(hep.getFace());
				}
			}
			lhe = lhe.getNextInPath();
		} while (lhe != _phalfedge && lhe != null);
		return ff;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.Point3D#toString()
	 */
	@Override
	public String toString() {
		String s = "HE_Path key: " + key() + ". Connects " + getPathOrder()
				+ " vertices: ";
		HE_PathHalfedge he = _phalfedge;
		if (he != null) {
			for (int i = 0; i < getPathOrder() - 1; i++) {
				s += he.getHalfedge().getVertex().getKey() + "-";
				he = he.getNextInPath();
			}
			s += he.getHalfedge().getEndVertex().getKey() + ".";
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Element#clear()
	 */
	@Override
	public void clear() {
		_phalfedge = null;
	}

	/**
	 * Is this path a loop? Only checks if first path halfedge has a valid
	 * getPrevInPath(). Assumes that the path is constructed correctly.
	 *
	 * @return true/false
	 */
	public boolean isLoop() {
		return _phalfedge.getPrevInPath() != null;
	}

	/**
	 * Does this path cut a mesh in two halves? Automatically true for a loop.
	 * An open path must have its start and end vertex on a boundary. Assumes
	 * that the path is constructed correctly. One of the "halves" can be
	 * degenerate, for example when looping around a single edge. NOTE: not sure
	 * if this is always correct for meshes with holes...
	 *
	 * @return true/false
	 */
	public boolean isCut() {
		if (isLoop()) {
			return true;
		}
		if (!_phalfedge.getVertex().isBoundary()) {
			return false;
		}
		HE_PathHalfedge last = _phalfedge;
		while (last.getNextInPath() != null) {
			last = last.getNextInPath();
		}
		if (!last.getEndVertex().isBoundary()) {
			return false;
		}
		return true;
	}

	@Override
	public void clearPrecomputed() {
	}
}
