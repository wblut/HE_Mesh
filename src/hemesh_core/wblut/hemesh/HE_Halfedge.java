package wblut.hemesh;

import wblut.core.WB_HashCode;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

public class HE_Halfedge extends HE_MeshElement implements Comparable<HE_Halfedge> {
	private HE_Vertex _vertex;
	private HE_Halfedge _pair;
	private HE_Halfedge _next;
	private HE_Halfedge _prev;
	private HE_Face _face;
	private HE_TextureCoordinate uvw;

	public HE_Halfedge() {
		super();
		uvw = null;
		_vertex = null;
		_pair = null;
		_next = null;
		_prev = null;
		_face = null;
	}

	public HE_Halfedge getPrevInFace() {
		return _prev;
	}

	public HE_Halfedge getPrevInFace(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getPrevInFace();
		}
		return he;
	}

	public HE_Halfedge getNextInFace() {
		return _next;
	}

	public HE_Halfedge getNextInFace(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getNextInFace();
		}
		return he;
	}

	public HE_Halfedge getNextInVertex() {
		if (_pair == null) {
			return null;
		}
		return _pair.getNextInFace();
	}

	public HE_Halfedge getNextInVertex(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getNextInVertex();
		}
		return he;
	}

	public HE_Halfedge getPrevInVertex() {
		if (_prev == null) {
			return null;
		}
		return getPrevInFace().getPair();
	}

	public HE_Halfedge getPrevInVertex(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getPrevInVertex();
		}
		return he;
	}

	public HE_Halfedge getPair() {
		return _pair;
	}

	protected void setNext(final HE_Halfedge he) {
		_next = he;
	}

	protected void setPrev(final HE_Halfedge he) {
		_prev = he;
	}

	protected void setPair(final HE_Halfedge he) {
		_pair = he;
	}

	public HE_Halfedge getEdge() {
		if (isEdge()) {
			return this;
		}
		return _pair;
	}

	public HE_Face getFace() {
		return _face;
	}

	protected void setFace(final HE_Face face) {
		_face = face;
	}

	public HE_Vertex getVertex() {
		return _vertex;
	}

	public WB_Point getPosition() {
		return _vertex.getPosition();
	}

	public HE_Vertex getStartVertex() {
		return _vertex;
	}

	public WB_Point getStartPosition() {
		return _vertex.getPosition();
	}

	protected void setVertex(final HE_Vertex vertex) {
		_vertex = vertex;
	}

	public HE_Vertex getEndVertex() {
		if (_pair != null) {
			return _pair._vertex;
		}
		if (_next != null) {
			return _next._vertex;
		}
		return null;
	}

	public WB_Point getEndPosition() {
		if (_pair != null) {
			return _pair._vertex.getPosition();
		}
		if (_next != null) {
			return _next._vertex.getPosition();
		}
		return null;
	}

	public WB_Point getCenter() {
		return HE_MeshOp.getEdgeCenter(this);
	}

	public WB_Vector getEdgeNormal() {
		return HE_MeshOp.getEdgeNormal(this);
	}

	public WB_Vector getEdgeDirection() {
		return HE_MeshOp.getEdgeTangent(this);
	}

	public WB_Vector getHalfedgeNormal() {
		return HE_MeshOp.getHalfedgeNormal(this);
	}

	public WB_Vector getHalfedgeDirection() {
		return HE_MeshOp.getHalfedgeTangent(this);
	}

	public WB_AABB getAABB() {
		return HE_MeshOp.getAABB(this);
	}

	public double getEdgeArea() {
		return HE_MeshOp.getEdgeArea(this);
	}

	public double getHalfedgeArea() {
		return HE_MeshOp.getHalfedgeArea(this);
	}

	public double getSqLength() {
		return HE_MeshOp.getSqLength(this);
	}

	public double getLength() {
		return HE_MeshOp.getLength(this);
	}

	protected void clearNext() {
		_next = null;
	}

	protected void clearPrev() {
		_prev = null;
	}

	protected void clearPair() {
		_pair = null;
	}

	protected void clearFace() {
		_face = null;
	}

	protected void clearVertex() {
		_vertex = null;
	}

	@Override
	public String toString() {
		return "HE_Halfedge key: " + getKey() + ", paired with halfedge " + getPair().getKey() + ". Vertex: "
				+ getVertex().getKey() + ". Is this an edge: " + isEdge() + "." + " (" + getLabel() + ","
				+ getInternalLabel() + ")";
	}

	public boolean isEdge() {
		if (_pair == null) {
			return false;
		}
		if (_face == null) {
			if (_pair._face == null) {// both halfedges are faceless
				return getKey() < _pair.getKey();
			} else {
				return false;
			}
		} else if (_pair._face == null) {
			return true;
		}
		return getKey() < _pair.getKey();
	}

	public boolean isInnerBoundary() {
		if (_face == null || _pair == null) {
			return false;
		}
		if (_pair._face == null) {
			return true;
		}
		return false;
	}

	public boolean isOuterBoundary() {
		if (_face == null) {
			return true;
		}
		return false;
	}

	public void copyProperties(final HE_Halfedge el) {
		super.copyProperties(el);
		if (el.getUVW() == null) {
			uvw = null;
		} else {
			uvw = new HE_TextureCoordinate(el.getUVW());
		}
	}

	@Override
	protected void clear() {
		_face = null;
		_next = null;
		_pair = null;
		_vertex = null;
		uvw = null;
	}

	// TEXTURE COORDINATES
	public HE_TextureCoordinate getUVW() {
		if (uvw == null) {
			return HE_TextureCoordinate.ZERO;
		}
		return uvw;
	}

	public void setUVW(final double u, final double v, final double w) {
		uvw = new HE_TextureCoordinate(u, v, w);
	}

	public void setUVW(final WB_Coord uvw) {
		if (uvw == null) {
			return;
		}
		this.uvw = new HE_TextureCoordinate(uvw);
	}

	public void setUVW(final HE_TextureCoordinate uvw) {
		if (uvw == null) {
			return;
		}
		this.uvw = new HE_TextureCoordinate(uvw);
	}

	public boolean hasUVW() {
		return uvw != null;
	}

	@Override
	public int compareTo(final HE_Halfedge he) {
		if (he.getVertex() == null) {
			if (getVertex() == null) {
				return Long.compare(getKey(), he.getKey());
			} else {
				return 1;
			}
		} else if (getVertex() == null) {
			return -1;
		}
		return getVertex().compareTo(he.getVertex());
		// return cmp == 0 ? Long.compare(key(), he.key()) : 0;
	}

	@Override
	public int hashCode() {
		if (getVertex() == null) {
			return WB_HashCode.calculateHashCode(0, 0, 0);
		}
		return getVertex().hashCode();
	}

	@Override
	public void clearPrecomputed() {
	}

	public double getCotan() {
		return HE_MeshOp.getCotan(this);
	}

	public WB_Point getEdgeCenter(final double f) {
		return HE_MeshOp.getEdgeCenter(this, f);
	}

	public double getEdgeCosDihedralAngle() {
		return HE_MeshOp.getEdgeCosDihedralAngle(this);
	}

	public double getEdgeDihedralAngle() {
		return HE_MeshOp.getEdgeDihedralAngle(this);
	}

	public WB_Point getHalfedgeCenter(final double f) {
		return HE_MeshOp.getHalfedgeCenter(this, f);
	}

	public double getHalfedgeDihedralAngle() {
		return HE_MeshOp.getHalfedgeDihedralAngle(this);
	}
}
