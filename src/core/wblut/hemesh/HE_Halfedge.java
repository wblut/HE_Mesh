/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.core.WB_HashCode;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 * Half-edge element of half-edge data structure.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HE_Halfedge extends HE_MeshElement
		implements Comparable<HE_Halfedge> {
	/** Start vertex of halfedge. */
	private HE_Vertex				_vertex;
	/** Halfedge pair. */
	private HE_Halfedge				_pair;
	/** Next halfedge in face. */
	private HE_Halfedge				_next;
	/** Previous halfedge in face. */
	private HE_Halfedge				_prev;
	/** Associated face. */
	private HE_Face					_face;
	/** Associated edge. */
	private HE_TextureCoordinate	uvw;

	/**
	 * Instantiates a new HE_Halfedge.
	 */
	public HE_Halfedge() {
		super();
		uvw = null;
		_vertex = null;
		_pair = null;
		_next = null;
		_prev = null;
		_face = null;
	}

	/**
	 * Get previous halfedge in face.
	 *
	 * @return previous halfedge
	 */
	public HE_Halfedge getPrevInFace() {
		return _prev;
	}

	/**
	 * Get n'th previous halfedge in face.
	 *
	 * @return
	 */
	public HE_Halfedge getPrevInFace(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getPrevInFace();
		}
		return he;
	}

	/**
	 * Get next halfedge in face.
	 *
	 * @return next halfedge
	 */
	public HE_Halfedge getNextInFace() {
		return _next;
	}

	/**
	 * Get n'th next halfedge in face.
	 *
	 * @return next halfedge
	 */
	public HE_Halfedge getNextInFace(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getNextInFace();
		}
		return he;
	}

	/**
	 * Get next halfedge in vertex.
	 *
	 * @return next halfedge
	 */
	public HE_Halfedge getNextInVertex() {
		if (_pair == null) {
			return null;
		}
		return _pair.getNextInFace();
	}

	/**
	 * Get n'th next halfedge in vertex.
	 *
	 * @return
	 */
	public HE_Halfedge getNextInVertex(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getNextInVertex();
		}
		return he;
	}

	/**
	 * Get previous halfedge in vertex.
	 *
	 * @return
	 */
	public HE_Halfedge getPrevInVertex() {
		if (_prev == null) {
			return null;
		}
		return getPrevInFace().getPair();
	}

	/**
	 * Get n'th previous halfedge in vertex.
	 *
	 * @return
	 */
	public HE_Halfedge getPrevInVertex(final int n) {
		HE_Halfedge he = this;
		for (int i = 0; i < n; i++) {
			he = he.getPrevInVertex();
		}
		return he;
	}

	/**
	 * Get paired halfedge.
	 *
	 * @return paired halfedge
	 */
	public HE_Halfedge getPair() {
		return _pair;
	}

	/**
	 * Set next halfedge in face.
	 *
	 * @param he
	 *            next halfedge
	 */
	protected void setNext(final HE_Halfedge he) {
		_next = he;
	}

	/**
	 * Sets previous halfedge in face, only to be called by setNext.
	 *
	 * @param he
	 *            next halfedge
	 */
	protected void setPrev(final HE_Halfedge he) {
		_prev = he;
	}

	/**
	 * Pair halfedges.
	 *
	 * @param he
	 *            halfedge to pair
	 */
	protected void setPair(final HE_Halfedge he) {
		_pair = he;
	}

	/**
	 * Get edge of halfedge.
	 *
	 * @return edge
	 */
	public HE_Halfedge getEdge() {
		if (isEdge()) {
			return this;
		}
		return _pair;
	}

	/**
	 * Get face of halfedge.
	 *
	 * @return face
	 */
	public HE_Face getFace() {
		return _face;
	}

	/**
	 * Sets the face.
	 *
	 * @param face
	 *            the new face
	 */
	protected void setFace(final HE_Face face) {
		_face = face;
	}

	/**
	 * Get vertex of halfedge.
	 *
	 * @return vertex
	 */
	public HE_Vertex getVertex() {
		return _vertex;
	}

	public WB_Point getPosition() {
		return _vertex.getPosition();
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Vertex getStartVertex() {
		return _vertex;
	}

	public WB_Point getStartPosition() {
		return _vertex.getPosition();
	}

	/**
	 * Sets vertex.
	 *
	 * @param vertex
	 *            vertex
	 */
	protected void setVertex(final HE_Vertex vertex) {
		_vertex = vertex;
	}

	/**
	 * Get end vertex of halfedge.
	 *
	 * @return vertex
	 */
	public HE_Vertex getEndVertex() {
		if (_pair != null) {
			return _pair._vertex;
		}
		if (_next != null) {
			return _next._vertex;
		}
		return null;
	}

	/**
	 * Get end position of halfedge.
	 *
	 * @return vertex
	 */
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

	/**
	 * Clear next.
	 */
	protected void clearNext() {
		_next = null;
	}

	/**
	 * Clear prev.
	 */
	protected void clearPrev() {
		_prev = null;
	}

	/**
	 * Clear pair.
	 */
	protected void clearPair() {
		_pair = null;
	}

	/**
	 * Clear face.
	 */
	protected void clearFace() {
		_face = null;
	}

	/**
	 * Clear vertex.
	 */
	protected void clearVertex() {
		_vertex = null;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.Point3D#toString()
	 */
	@Override
	public String toString() {
		return "HE_Halfedge key: " + getKey() + ", paired with halfedge "
				+ getPair().getKey() + ". Vertex: " + getVertex().getKey()
				+ ". Is this an edge: " + isEdge() + "." + " (" + getUserLabel()
				+ "," + getInternalLabel() + ")";
	}

	/**
	 * A halfedge is considered an edge if it has a paired halfedge and one of
	 * these conditions is met:
	 *
	 * a) both the halfedge and its pair have no face, and the halfedge key is
	 * lower b) the halfedge has a face and its pair has no face c) both the
	 * halfedge and its pair have a face, and the halfedge key is lower.
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	public boolean isInnerBoundary() {
		if (_face == null || _pair == null) {
			return false;
		}
		if (_pair._face == null) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isOuterBoundary() {
		if (_face == null) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param el
	 */
	public void copyProperties(final HE_Halfedge el) {
		super.copyProperties(el);
		if (el.getUVW() == null) {
			uvw = null;
		} else {
			uvw = new HE_TextureCoordinate(el.getUVW());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Element#clear()
	 */
	@Override
	protected void clear() {
		_face = null;
		_next = null;
		_pair = null;
		_vertex = null;
		uvw = null;
	}

	// TEXTURE COORDINATES
	/**
	 * Get texture coordinate belonging to the halfedge vertex in this face. If
	 * no halfedge UVW exists, returns the vertex UVW. If neither exist, zero
	 * coordinates are returned.
	 *
	 * @return
	 */
	public HE_TextureCoordinate getUVW() {
		if (uvw == null) {
			if (_vertex != null) {
				return _vertex.getVertexUVW();
			} else {
				return HE_TextureCoordinate.ZERO;
			}
		}
		return uvw;
	}

	/**
	 * Get texture coordinate belonging to this halfedge . If no halfedge UVW
	 * exists, zero coordinates are returned.
	 *
	 * @return
	 */
	public HE_TextureCoordinate getHalfedgeUVW() {
		if (uvw == null) {
			return HE_TextureCoordinate.ZERO;
		}
		return uvw;
	}

	/**
	 * Get texture coordinate belonging to the halfedge vertex. If no vertex UVW
	 * exists, zero coordinates are returned.
	 *
	 * @return
	 */
	public HE_TextureCoordinate getVertexUVW() {
		if (_vertex != null) {
			return _vertex.getVertexUVW();
		} else {
			return HE_TextureCoordinate.ZERO;
		}
	}

	/**
	 * Clear halfedge UVW.
	 */
	public void clearUVW() {
		uvw = null;
	}

	/**
	 * Set halfedge UVW.
	 *
	 * @param u
	 * @param v
	 * @param w
	 */
	public void setUVW(final double u, final double v, final double w) {
		uvw = new HE_TextureCoordinate(u, v, w);
	}

	/**
	 * Set halfedge UVW.
	 *
	 * @param uvw
	 *            WB_Coord
	 */
	public void setUVW(final WB_Coord uvw) {
		if (uvw == null) {
			return;
		}
		this.uvw = new HE_TextureCoordinate(uvw);
	}

	/**
	 * Set halfedge UVW.
	 *
	 * @param uvw
	 *            HE_TextureCoordinate
	 */
	public void setUVW(final HE_TextureCoordinate uvw) {
		if (uvw == null) {
			return;
		}
		this.uvw = new HE_TextureCoordinate(uvw);
	}

	/**
	 * Check if this halfedge has texture coordinates.
	 *
	 * @return
	 */
	public boolean hasHalfedgeUVW() {
		return uvw != null;
	}

	/**
	 * Check if the halfedge vertex has a UVW for this face, either a halfedge
	 * UVW or a vertex UVW.
	 *
	 *
	 * @return
	 */
	public boolean hasUVW() {
		if (uvw != null) {
			return true;
		}
		if (_vertex != null && _vertex.hasVertexUVW()) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the halfedge vertex has a vertex UVW.
	 *
	 * @return
	 */
	public boolean hasVertexUVW() {
		if (_vertex != null && _vertex.hasVertexUVW()) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param he
	 * @return
	 */
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

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public WB_Point getEdgeCenter(final double f) {
		
				return HE_MeshOp.getEdgeCenter(this,f);
	}

	public double getEdgeCosDihedralAngle() {
			return HE_MeshOp.getEdgeCosDihedralAngle(this);
	}

	/**
	 * Return angle between adjacent faces.
	 *
	 * @return angle
	 */
	public double getEdgeDihedralAngle() {
		return HE_MeshOp.getEdgeDihedralAngle(this);
	}

	/**
	 * Get offset center of halfedge.
	 *
	 * @param f
	 * @return center
	 */
	public WB_Point getHalfedgeCenter(final double f) {
		return HE_MeshOp.getHalfedgeCenter(this,f);
	}

	/**
	 * Get angle between adjacent faces.
	 *
	 * @return angle
	 */
	public double getHalfedgeDihedralAngle() {
		
		return HE_MeshOp.getHalfedgeDihedralAngle(this);
	}
}
