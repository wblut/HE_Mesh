package wblut.hemesh;

/**
 *
 */
public class HE_Path extends HE_MeshElement {
	/**  */
	protected HE_PathHalfedge _phalfedge;

	/**
	 *
	 *
	 * @param loop
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
	 *
	 *
	 * @param face
	 */
	public HE_Path(final HE_Face face) {
		this(face.getHalfedge());
	}

	/**
	 *
	 *
	 * @param v
	 */
	public HE_Path(final HE_Vertex v) {
		final HE_HalfedgeList halfedges = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @param v0
	 * @param v1
	 * @param mesh
	 * @return
	 */
	public static HE_Path getShortestPath(final HE_Vertex v0, final HE_Vertex v1, final HE_Mesh mesh) {
		if (!mesh.contains(v0) || !mesh.contains(v1) || v0 == v1) {
			return null;
		}
		final HET_MeshNetwork graph = new HET_MeshNetwork(mesh);
		final int[] shortestpath = graph.getShortestPathBetweenVertices(mesh.getIndex(v0), mesh.getIndex(v1));
		return HE_MeshOp.createPathFromIndices(mesh, shortestpath, false);
	}

	/**
	 *
	 *
	 * @param halfedges
	 * @param loop
	 */
	public HE_Path(final HE_HalfedgeList halfedges, final boolean loop) {
		super();
		createFromList(halfedges, loop);
	}

	/**
	 *
	 *
	 * @param halfedges
	 * @param loop
	 */
	private void createFromList(final HE_HalfedgeList halfedges, final boolean loop) {
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
	 *
	 * @return
	 */
	public long key() {
		return super.getKey();
	}

	/**
	 *
	 *
	 * @return
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
	 *
	 * @return
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
	 *
	 * @return
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
	 *
	 * @return
	 */
	public HE_HalfedgeList getHalfedges() {
		final HE_HalfedgeList fhe = new HE_HalfedgeList();
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
	 *
	 * @return
	 */
	public HE_VertexList getPathVertices() {
		final HE_VertexList fhe = new HE_VertexList();
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
	 *
	 * @return
	 */
	public HE_HalfedgeList getPathEdges() {
		final HE_HalfedgeList fe = new HE_HalfedgeList();
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
	 *
	 * @return
	 */
	public HE_PathHalfedge getPathHalfedge() {
		return _phalfedge;
	}

	/**
	 *
	 *
	 * @param phalfedge
	 */
	public void setPathHalfedge(final HE_PathHalfedge phalfedge) {
		_phalfedge = phalfedge;
	}

	/**
	 *
	 */
	public void clearPathHalfedge() {
		_phalfedge = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceList getPathInnerFaces() {
		final HE_FaceList ff = new HE_FaceList();
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
	 *
	 * @return
	 */
	public HE_FaceList getPathOuterFaces() {
		final HE_FaceList ff = new HE_FaceList();
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

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		String s = "HE_Path key: " + key() + ". Connects " + getPathOrder() + " vertices: ";
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

	/**
	 *
	 */
	@Override
	public void clear() {
		_phalfedge = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isLoop() {
		return _phalfedge.getPrevInPath() != null;
	}

	/**
	 *
	 *
	 * @return
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

	/**
	 *
	 */
	@Override
	public void clearPrecomputed() {
	}
}
