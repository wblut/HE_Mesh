package wblut.hemesh;

import wblut.core.WB_HashCode;
import wblut.geom.WB_Coord;
import wblut.geom.WB_MutableCoord;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HE_Vertex extends HE_MeshElement implements WB_MutableCoord {
	/**  */
	private final WB_Point pos;
	/**  */
	private HE_Halfedge _halfedge;

	/**
	 *
	 */
	public HE_Vertex() {
		super();
		pos = new WB_Point();
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public HE_Vertex(final double x, final double y, final double z) {
		super();
		pos = new WB_Point(x, y, z);
	}

	/**
	 *
	 *
	 * @param v
	 */
	public HE_Vertex(final WB_Coord v) {
		super();
		pos = new WB_Point(v);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Vertex get() {
		final HE_Vertex copy = new HE_Vertex(getPosition());
		copy.copyProperties(this);
		return copy;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point getPosition() {
		return pos;
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
	 * @param f
	 * @return
	 */
	public HE_Halfedge getHalfedge(final HE_Face f) {
		HE_Halfedge he = _halfedge;
		if (he == null) {
			return null;
		}
		if (f == null) {
			do {
				if (he.getFace() == null) {
					return he;
				}
				he = he.getNextInVertex();
			} while (he != _halfedge);
		} else {
			do {
				if (he.getFace() == f) {
					return he;
				}
				he = he.getNextInVertex();
			} while (he != _halfedge);
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexEdgeCirculator veCrc() {
		return new HE_VertexEdgeCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexFaceCirculator vfCrc() {
		return new HE_VertexFaceCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexVertexCirculator vvCrc() {
		return new HE_VertexVertexCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexHalfedgeInCirculator vheiCrc() {
		return new HE_VertexHalfedgeInCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexHalfedgeOutCirculator vheoCrc() {
		return new HE_VertexHalfedgeOutCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexEdgeRevCirculator veRevCrc() {
		return new HE_VertexEdgeRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexFaceRevCirculator vfRevCrc() {
		return new HE_VertexFaceRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexVertexRevCirculator vvRevCrc() {
		return new HE_VertexVertexRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexHalfedgeInRevCirculator vheiRevCrc() {
		return new HE_VertexHalfedgeInRevCirculator(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexHalfedgeOutRevCirculator vheoRevCrc() {
		return new HE_VertexHalfedgeOutRevCirculator(this);
	}

	/**
	 *
	 *
	 * @param v
	 */
	public void set(final HE_Vertex v) {
		pos.set(v);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getHalfedgeStar() {
		final HE_HalfedgeList vhe = new HE_HalfedgeList();
		if (getHalfedge() == null) {
			return vhe;
		}
		HE_Halfedge he = getHalfedge();
		do {
			if (!vhe.contains(he)) {
				vhe.add(he);
			}
			he = he.getNextInVertex();
		} while (he != getHalfedge());
		return vhe;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getEdgeStar() {
		final HE_HalfedgeList ve = new HE_HalfedgeList();
		if (getHalfedge() == null) {
			return ve;
		}
		HE_Halfedge he = getHalfedge();
		do {
			if (he.isEdge()) {
				if (!ve.contains(he)) {
					ve.add(he);
				}
			} else {
				if (!ve.contains(he.getPair())) {
					ve.add(he.getPair());
				}
			}
			he = he.getNextInVertex();
		} while (he != getHalfedge());
		return ve;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceList getFaceStar() {
		final HE_FaceList vf = new HE_FaceList();
		if (getHalfedge() == null) {
			return vf;
		}
		HE_Halfedge he = getHalfedge();
		do {
			if (he.getFace() != null) {
				if (!vf.contains(he.getFace())) {
					vf.add(he.getFace());
				}
			}
			he = he.getNextInVertex();
		} while (he != getHalfedge());
		return vf;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getVertexStar() {
		final HE_VertexList vv = new HE_VertexList();
		if (getHalfedge() == null) {
			return vv;
		}
		HE_Halfedge he = getHalfedge();
		do {
			final HE_Halfedge hen = he.getNextInFace();
			if (hen.getVertex() != this && !vv.contains(hen.getVertex())) {
				vv.add(hen.getVertex());
			}
			he = he.getNextInVertex();
		} while (he != getHalfedge());
		return vv;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getNeighborVertices() {
		return getVertexStar();
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getVertexDegree() {
		int result = 0;
		if (getHalfedge() == null) {
			return 0;
		}
		HE_Halfedge he = getHalfedge();
		do {
			result++;
			he = he.getNextInVertex();
		} while (he != getHalfedge());
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isBoundary() {
		HE_Halfedge he = _halfedge;
		do {
			if (he.getFace() == null) {
				return true;
			}
			he = he.getNextInVertex();
		} while (he != _halfedge);
		return false;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isIsolated() {
		return _halfedge == null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getVertexArea() {
		return HE_MeshOp.getVertexArea(this);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double xd() {
		return getPosition().xd();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double yd() {
		return getPosition().yd();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double zd() {
		return getPosition().zd();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public double wd() {
		return 1;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	@Override
	public double getd(final int i) {
		return getPosition().getd(i);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float xf() {
		return getPosition().xf();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float yf() {
		return getPosition().yf();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float zf() {
		return getPosition().zf();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public float wf() {
		return 1.0f;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	@Override
	public float getf(final int i) {
		return getPosition().getf(i);
	}

	/**
	 *
	 *
	 * @param x
	 */
	@Override
	public void setX(final double x) {
		pos.setX(x);
	}

	/**
	 *
	 *
	 * @param y
	 */
	@Override
	public void setY(final double y) {
		pos.setY(y);
	}

	/**
	 *
	 *
	 * @param z
	 */
	@Override
	public void setZ(final double z) {
		pos.setZ(z);
	}

	/**
	 *
	 *
	 * @param w
	 */
	@Override
	public void setW(final double w) {
	}

	/**
	 *
	 *
	 * @param i
	 * @param v
	 */
	@Override
	public void setCoord(final int i, final double v) {
		pos.setCoord(i, v);
	}

	/**
	 *
	 *
	 * @param p
	 */
	@Override
	public void set(final WB_Coord p) {
		pos.set(p);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	@Override
	public void set(final double x, final double y) {
		pos.set(x, y);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void set(final double x, final double y, final double z) {
		pos.set(x, y, z);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	@Override
	public void set(final double x, final double y, final double z, final double w) {
		pos.set(x, y, z, w);
	}

	/**
	 *
	 *
	 * @param el
	 */
	public void copyProperties(final HE_Vertex el) {
		super.copyProperties(el);
	}

	/**
	 *
	 */
	@Override
	public void clear() {
		_halfedge = null;
	}
	// TEXTURE COORDINATES

	/**
	 *
	 *
	 * @param u
	 * @param v
	 * @param w
	 */
	public void setUVW(final double u, final double v, final double w) {
		HE_Halfedge he = getHalfedge();
		if (he != null) {
			do {
				he.setUVW(u, v, w);
				he = he.getNextInVertex();
			} while (he != getHalfedge() && he != null);
		}
	}

	/**
	 *
	 *
	 * @param uvw
	 */
	public void setUVW(final WB_Coord uvw) {
		HE_Halfedge he = getHalfedge();
		if (he != null) {
			do {
				he.setUVW(uvw);
				he = he.getNextInVertex();
			} while (he != getHalfedge() && he != null);
		}
	}

	/**
	 *
	 *
	 * @param uvw
	 */
	public void setUVW(final HE_TextureCoordinate uvw) {
		HE_Halfedge he = getHalfedge();
		if (he != null) {
			do {
				he.setUVW(uvw);
				he = he.getNextInVertex();
			} while (he != getHalfedge() && he != null);
		}
	}

//
//
//	public void setUVW(final double u, final double v, final double w,
//			final HE_Face face) {
//		HE_Halfedge he = getHalfedge(face);
//		if (he != null) {
//			he.setUVW(u, v, w);
//		}
//	}
//
//
//	public void setUVW(final WB_Coord uvw, final HE_Face face) {
//		HE_Halfedge he = getHalfedge(face);
//		if (he != null) {
//			he.setUVW(uvw);
//		}
//	}
//
//
//	public void setUVW(final HE_TextureCoordinate uvw, final HE_Face face) {
//		HE_Halfedge he = getHalfedge(face);
//		if (he != null) {
//			he.setUVW(uvw);
//		}
//	}
//
//	public boolean hasUVW(final HE_Face f) {
//		final HE_Halfedge he = getHalfedge(f);
//		if (he != null && he.hasUVW()) {
//			return true;
//		}
//		return false;
//	}
//
//
//	public HE_TextureCoordinate getUVW(final HE_Face f) {
//		final HE_Halfedge he = getHalfedge(f);
//		if (he != null && he.hasUVW()) {
//			return he.getUVW();
//		} else {
//			return HE_TextureCoordinate.ZERO;
//		}
	/**
	 *
	 *
	 * @param v
	 * @return
	 */
//	}
	public boolean isNeighbor(final HE_Vertex v) {
		if (getHalfedge() == null) {
			return false;
		}
		HE_Halfedge he = getHalfedge();
		do {
			if (he.getEndVertex() == v) {
				return true;
			}
			he = he.getNextInVertex();
		} while (he != getHalfedge());
		return false;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	@Override
	public int compareTo(final WB_Coord p) {
		int cmp = Double.compare(xd(), p.xd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(yd(), p.yd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(zd(), p.zd());
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(wd(), p.wd());
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public int compareToY1st(final WB_Coord p) {
		int cmp = Double.compare(yd(), p.yd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(xd(), p.xd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(zd(), p.zd());
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(wd(), p.wd());
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (!(o instanceof HE_Vertex)) {
			return false;
		}
		final HE_Vertex p = (HE_Vertex) o;
		if (!WB_Epsilon.isEqual(xd(), p.xd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(yd(), p.yd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(zd(), p.zd())) {
			return false;
		}
		if (!WB_Epsilon.isEqual(wd(), p.wd())) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		return WB_HashCode.calculateHashCode(xd(), yd(), zd());
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "HE_Vertex key: " + getKey() + " [x=" + xd() + ", y=" + yd() + ", z=" + zd() + "]" + " (" + getLabel()
				+ "," + getInternalLabel() + ")";
	}

	/**
	 *
	 */
	@Override
	public void clearPrecomputed() {
	}

	@Override
	public void getd(final double[] result) {
		result[0] = xd();
		result[1] = yd();
		result[2] = zd();
	}

	@Override
	public void getd(final int i, final double[] result) {
		result[i] = xd();
		result[i + 1] = yd();
		result[i + 2] = zd();
	}

	@Override
	public void getf(final float[] result) {
		result[0] = xf();
		result[1] = yf();
		result[2] = zf();
	}

	@Override
	public void getf(final int i, final float[] result) {
		result[i] = xf();
		result[i + 1] = yf();
		result[i + 2] = zf();
	}
}
