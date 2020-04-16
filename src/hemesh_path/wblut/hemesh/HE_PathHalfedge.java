package wblut.hemesh;

public class HE_PathHalfedge extends HE_MeshElement {
	private HE_Halfedge _he;
	private HE_PathHalfedge _next;
	private HE_PathHalfedge _prev;

	public HE_PathHalfedge() {
		super();
	}

	public HE_PathHalfedge(final HE_Halfedge he) {
		super();
		_he = he;
	}

	public void clearNext() {
		if (_next != null) {
			_next.clearPrev();
		}
		_next = null;
	}

	private void clearPrev() {
		_prev = null;
	}

	public HE_Halfedge getHalfedge() {
		return _he;
	}

	public HE_Vertex getVertex() {
		return _he.getVertex();
	}

	public HE_Vertex getStartVertex() {
		return _he.getVertex();
	}

	public HE_Vertex getEndVertex() {
		return _he.getEndVertex();
	}

	public void setHalfedge(final HE_Halfedge he) {
		_he = he;
	}

	public HE_PathHalfedge getNextInPath() {
		return _next;
	}

	public HE_PathHalfedge getPrevInPath() {
		return _prev;
	}

	public Long key() {
		return super.getKey();
	}

	public void setNext(final HE_PathHalfedge he) {
		_next = he;
	}

	public void setPrev(final HE_PathHalfedge he) {
		_prev = he;
	}

	@Override
	public String toString() {
		return "HE_PathHalfedge key: " + key() + ".";
	}

	@Override
	public void clear() {
		_he = null;
		_next = null;
		_prev = null;
	}

	@Override
	public void clearPrecomputed() {
	}
}
