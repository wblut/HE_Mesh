package wblut.hemesh;

import java.util.List;

import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_JTS;
import wblut.geom.WB_List;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_TriangleSource;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HE_Face extends HE_MeshElement implements Comparable<HE_Face>, WB_TriangleSource {
	/**  */
	private HE_Halfedge _halfedge;
	/**  */
	private int textureId;
	/**  */
	private int[] triangles;

	/**
	 *
	 */
	public HE_Face() {
		super();
		triangles = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Halfedge getHalfedge() {
		return _halfedge;
	}

	/**
	 *
	 *
	 * @param halfedge
	 */
	protected void setHalfedge(final HE_Halfedge halfedge) {
		_halfedge = halfedge;
	}

	/**
	 *
	 */
	protected void clearHalfedge() {
		_halfedge = null;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public HE_Halfedge getHalfedge(final HE_Vertex v) {
		HE_Halfedge he = _halfedge;
		if (he == null) {
			return null;
		}
		do {
			if (he.getVertex() == v) {
				return he;
			}
			he = he.getNextInFace();
		} while (he != _halfedge);
		return null;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HE_Halfedge getHalfedge(final HE_Face f) {
		if (getHalfedge() == null || f == null) {
			return null;
		}
		HE_Halfedge he = getHalfedge();
		do {
			if (he.getPair() != null && he.getPair().getFace() != null && he.getPair().getFace() == f) {
				return he;
			}
			he = he.getNextInFace();
		} while (he != getHalfedge());
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getFaceArea() {
		return HE_MeshOp.getFaceArea(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getFaceDegree() {
		int result = 0;
		if (_halfedge == null) {
			return 0;
		}
		HE_Halfedge he = _halfedge;
		do {
			result++;
			he = he.getNextInFace();
		} while (he != _halfedge);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceEdgeCirculator feCrc() {
		return new HE_FaceEdgeCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceFaceCirculator ffCrc() {
		return new HE_FaceFaceCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceVertexCirculator fvCrc() {
		return new HE_FaceVertexCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceHalfedgeInnerCirculator fheiCrc() {
		return new HE_FaceHalfedgeInnerCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceHalfedgeOuterCirculator fheoCrc() {
		return new HE_FaceHalfedgeOuterCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceEdgeRevCirculator feRevCrc() {
		return new HE_FaceEdgeRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceFaceRevCirculator ffRevCrc() {
		return new HE_FaceFaceRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceHalfedgeInnerRevCirculator fheiRevCrc() {
		return new HE_FaceHalfedgeInnerRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceHalfedgeOuterRevCirculator fheoRevCrc() {
		return new HE_FaceHalfedgeOuterRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceVertexRevCirculator fvRevCrc() {
		return new HE_FaceVertexRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getFaceVertices() {
		final HE_VertexList fv = new HE_VertexList();
		if (_halfedge == null) {
			return fv;
		}
		HE_Halfedge he = _halfedge;
		do {
			fv.add(he.getVertex());
			he = he.getNextInFace();
		} while (he != _halfedge);
		return fv;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getUniqueFaceVertices() {
		final HE_VertexList fv = new HE_VertexList();
		if (_halfedge == null) {
			return fv;
		}
		HE_Halfedge he = _halfedge;
		do {
			if (!fv.contains(he.getVertex())) {
				fv.add(he.getVertex());
			}
			he = he.getNextInFace();
		} while (he != _halfedge);
		return fv;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getFaceHalfedges() {
		final HE_HalfedgeList fhe = new HE_HalfedgeList();
		if (_halfedge == null) {
			return fhe;
		}
		HE_Halfedge he = _halfedge;
		do {
			if (!fhe.contains(he)) {
				fhe.add(he);
			}
			he = he.getNextInFace();
		} while (he != _halfedge);
		return fhe;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getFaceHalfedgesTwoSided() {
		final HE_HalfedgeList fhe = new HE_HalfedgeList();
		if (_halfedge == null) {
			return fhe;
		}
		HE_Halfedge he = _halfedge;
		do {
			if (!fhe.contains(he)) {
				fhe.add(he);
				if (he.getPair() != null) {
					if (!fhe.contains(he.getPair())) {
						fhe.add(he.getPair());
					}
				}
			}
			he = he.getNextInFace();
		} while (he != _halfedge);
		return fhe;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getFaceEdges() {
		final HE_HalfedgeList fe = new HE_HalfedgeList();
		if (_halfedge == null) {
			return fe;
		}
		HE_Halfedge he = _halfedge;
		do {
			if (he.isEdge()) {
				if (!fe.contains(he)) {
					fe.add(he);
				}
			} else {
				if (!fe.contains(he.getPair())) {
					fe.add(he.getPair());
				}
			}
			he = he.getNextInFace();
		} while (he != _halfedge);
		return fe;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceList getNeighborFaces() {
		final HE_FaceList ff = new HE_FaceList();
		if (getHalfedge() == null) {
			return ff;
		}
		HE_Halfedge he = getHalfedge();
		do {
			final HE_Halfedge hep = he.getPair();
			if (hep != null && hep.getFace() != null) {
				if (hep.getFace() != this) {
					if (!ff.contains(hep.getFace())) {
						ff.add(hep.getFace());
					}
				}
			}
			he = he.getNextInFace();
		} while (he != getHalfedge());
		return ff;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<HE_TextureCoordinate> getFaceUVWs() {
		final WB_List<HE_TextureCoordinate> fv = new WB_List<>();
		if (_halfedge == null) {
			return fv;
		}
		HE_Halfedge he = _halfedge;
		do {
			fv.add(he.getUVW());
			he = he.getNextInFace();
		} while (he != _halfedge);
		return fv.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param c
	 */
	public void move(final WB_Coord c) {
		HE_Halfedge he = _halfedge;
		do {
			he.getVertex().getPosition().addSelf(c);
			he = he.getNextInFace();
		} while (he != _halfedge);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public int compareTo(final HE_Face f) {
		if (f.getHalfedge() == null) {
			if (getHalfedge() == null) {
				return 0;
			} else {
				return 1;
			}
		} else if (getHalfedge() == null) {
			return -1;
		}
		return getHalfedge().compareTo(f.getHalfedge());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int[] getTriangles() {
		return getTriangles(true);
	}

	/**
	 *
	 *
	 * @param optimize
	 * @return
	 */
	public int[] getTriangles(final boolean optimize) {
		if (triangles != null) {
			return triangles;
		}
		final int fo = getFaceDegree();
		if (fo < 3) {
			return triangles = new int[] { 0, 0, 0 };
		} else if (fo == 3) {
			return triangles = new int[] { 0, 1, 2 };
		} else if (isDegenerate()) {
			triangles = new int[3 * (fo - 2)];
			for (int i = 0; i < fo - 2; i++) {
				triangles[3 * i] = 0;
				triangles[3 * i + 1] = i + 1;
				triangles[3 * i + 2] = i + 2;
			}
			return triangles;
		} else if (fo == 4) {
			HE_Halfedge he=getHalfedge();
			boolean p0inside = WB_GeometryOp.pointInTriangleBary3D(he.getVertex(), he.getNextInFace().getVertex(), he.getNextInFace().getNextInFace().getVertex(), he.getPrevInFace().getVertex());
			if (p0inside) {
				return new int[] { 0, 1, 2, 0, 2, 3 };
			}
			boolean p2inside = WB_GeometryOp.pointInTriangleBary3D(he.getNextInFace().getNextInFace().getVertex(), he.getNextInFace().getVertex(), he.getVertex(), he.getPrevInFace().getVertex());
			if (p2inside) {
				return new int[] { 0, 1, 2, 0, 2, 3 };
			}
			return new int[] { 0, 1, 3, 1, 2, 3 };
			

		}
		return triangles = new WB_JTS.PolygonTriangulatorJTS()
				.triangulateHEFace(this, optimize);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_AABB getAABB() {
		return HE_MeshOp.getAABB(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		String s = "HE_Face key: " + getKey() + ". Connects " + getFaceDegree() + " vertices: ";
		HE_Halfedge he = getHalfedge();
		for (int i = 0; i < getFaceDegree() - 1; i++) {
			s += he.getVertex().getKey() + "-";
			he = he.getNextInFace();
		}
		s += he.getVertex().getKey() + "." + " (" + getLabel() + "," + getInternalLabel() + ")";
		return s;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isPlanar() {
		final WB_Plane P = HE_MeshOp.getPlane(this);
		HE_Halfedge he = getHalfedge();
		do {
			if (!WB_Epsilon.isZero(WB_GeometryOp.getDistance3D(he.getVertex(), P))) {
				return false;
			}
			he = he.getNextInFace();
		} while (he != getHalfedge());
		return true;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isBoundary() {
		HE_Halfedge he = _halfedge;
		do {
			if (he.getPair().getFace() == null) {
				return true;
			}
			he = he.getNextInFace();
		} while (he != _halfedge);
		return false;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isDegenerate() {
		return WB_Vector.getLength3D(HE_MeshOp.getFaceNormal(this)) < 0.5;
	}

	/**
	 *
	 *
	 * @param el
	 */
	public void copyProperties(final HE_Face el) {
		super.copyProperties(el);
		textureId = el.textureId;
	}

	/**
	 *
	 */
	@Override
	public void clear() {
		_halfedge = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getTextureId() {
		return textureId;
	}

	/**
	 *
	 *
	 * @param i
	 */
	public void setTextureId(final int i) {
		textureId = i;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public boolean isNeighbor(final HE_Face f) {
		if (getHalfedge() == null) {
			return false;
		}
		HE_Halfedge he = getHalfedge();
		do {
			if (he.getPair() != null && he.getPair().getFace() != null && he.getPair().getFace() == f) {
				return true;
			}
			he = he.getNextInFace();
		} while (he != getHalfedge());
		return false;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_CoordCollection getPoints() {
		return WB_CoordCollection.getCollection(getFaceVertices());
	}

	/**
	 *
	 */
	@Override
	public void clearPrecomputed() {
		triangles = null;
	}
}
