package wblut.hemesh;

import java.util.List;

import wblut.core.WB_HashCode;
import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordinateSystem;
import wblut.geom.WB_MutableCoord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

public class HE_Vertex extends HE_MeshElement implements WB_MutableCoord {
	private final WB_Point pos;
	private HE_Halfedge _halfedge;

	public HE_Vertex() {
		super();
		pos = new WB_Point();
	}

	public HE_Vertex(final double x, final double y, final double z) {
		super();
		pos = new WB_Point(x, y, z);
	}

	public HE_Vertex(final WB_Coord v) {
		super();
		pos = new WB_Point(v);
	}

	public HE_Vertex get() {
		final HE_Vertex copy = new HE_Vertex(getPosition());
		copy.copyProperties(this);
		return copy;
	}

	public WB_Point getPosition() {
		return pos;
	}

	public HE_Halfedge getHalfedge() {
		return _halfedge;
	}

	protected void setHalfedge(final HE_Halfedge halfedge) {
		_halfedge = halfedge;
	}

	protected void clearHalfedge() {
		_halfedge = null;
	}

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

	public HE_VertexEdgeCirculator veCrc() {
		return new HE_VertexEdgeCirculator(this);
	}

	public HE_VertexFaceCirculator vfCrc() {
		return new HE_VertexFaceCirculator(this);
	}

	public HE_VertexVertexCirculator vvCrc() {
		return new HE_VertexVertexCirculator(this);
	}

	public HE_VertexHalfedgeInCirculator vheiCrc() {
		return new HE_VertexHalfedgeInCirculator(this);
	}

	public HE_VertexHalfedgeOutCirculator vheoCrc() {
		return new HE_VertexHalfedgeOutCirculator(this);
	}

	public HE_VertexEdgeRevCirculator veRevCrc() {
		return new HE_VertexEdgeRevCirculator(this);
	}

	public HE_VertexFaceRevCirculator vfRevCrc() {
		return new HE_VertexFaceRevCirculator(this);
	}

	public HE_VertexVertexRevCirculator vvRevCrc() {
		return new HE_VertexVertexRevCirculator(this);
	}

	public HE_VertexHalfedgeInRevCirculator vheiRevCrc() {
		return new HE_VertexHalfedgeInRevCirculator(this);
	}

	public HE_VertexHalfedgeOutRevCirculator vheoRevCrc() {
		return new HE_VertexHalfedgeOutRevCirculator(this);
	}

	public void set(final HE_Vertex v) {
		pos.set(v);
	}

	public List<HE_Halfedge> getHalfedgeStar() {
		final List<HE_Halfedge> vhe = new HE_HalfedgeList();
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

	public List<HE_Halfedge> getEdgeStar() {
		final List<HE_Halfedge> ve = new HE_HalfedgeList();
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

	public List<HE_Face> getFaceStar() {
		final List<HE_Face> vf = new HE_FaceList();
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

	public List<HE_Vertex> getVertexStar() {
		final List<HE_Vertex> vv = new HE_VertexList();
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

	public List<HE_Vertex> getNeighborVertices() {
		return getVertexStar();
	}

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

	public boolean isIsolated() {
		return _halfedge == null;
	}

	public WB_Vector getVertexNormal() {
		return HE_MeshOp.getVertexNormal(this);
	}

	public double getVertexArea() {
		return HE_MeshOp.getVertexArea(this);
	}

	@Override
	public double xd() {
		return getPosition().xd();
	}

	@Override
	public double yd() {
		return getPosition().yd();
	}

	@Override
	public double zd() {
		return getPosition().zd();
	}

	@Override
	public double wd() {
		return 1;
	}

	@Override
	public double getd(final int i) {
		return getPosition().getd(i);
	}

	@Override
	public float xf() {
		return getPosition().xf();
	}

	@Override
	public float yf() {
		return getPosition().yf();
	}

	@Override
	public float zf() {
		return getPosition().zf();
	}

	@Override
	public float wf() {
		return 1.0f;
	}

	@Override
	public float getf(final int i) {
		return getPosition().getf(i);
	}

	@Override
	public void setX(final double x) {
		pos.setX(x);
	}

	@Override
	public void setY(final double y) {
		pos.setY(y);
	}

	@Override
	public void setZ(final double z) {
		pos.setZ(z);
	}

	@Override
	public void setW(final double w) {
	}

	@Override
	public void setCoord(final int i, final double v) {
		pos.setCoord(i, v);
	}

	@Override
	public void set(final WB_Coord p) {
		pos.set(p);
	}

	@Override
	public void set(final double x, final double y) {
		pos.set(x, y);
	}

	@Override
	public void set(final double x, final double y, final double z) {
		pos.set(x, y, z);
	}

	@Override
	public void set(final double x, final double y, final double z, final double w) {
		pos.set(x, y, z, w);
	}

	public void copyProperties(final HE_Vertex el) {
		super.copyProperties(el);
	}

	@Override
	public void clear() {
		_halfedge = null;
	}
	// TEXTURE COORDINATES

	public void setUVW(final double u, final double v, final double w) {
		HE_Halfedge he = getHalfedge();
		if (he != null) {
			do {
				he.setUVW(u, v, w);
				he = he.getNextInVertex();
			} while (he != getHalfedge() && he != null);
		}
	}

	public void setUVW(final WB_Coord uvw) {
		HE_Halfedge he = getHalfedge();
		if (he != null) {
			do {
				he.setUVW(uvw);
				he = he.getNextInVertex();
			} while (he != getHalfedge() && he != null);
		}
	}

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

	@Override
	public int hashCode() {
		return WB_HashCode.calculateHashCode(xd(), yd(), zd());
	}

	@Override
	public String toString() {
		return "HE_Vertex key: " + getKey() + " [x=" + xd() + ", y=" + yd() + ", z=" + zd() + "]" + " (" + getLabel()
				+ "," + getInternalLabel() + ")";
	}

	@Override
	public void clearPrecomputed() {
	}

	public double getAngleDefect() {
		return HE_MeshOp.getAngleDefect(this);
	}

	public double getBarycentricDualVertexArea() {
		return HE_MeshOp.getBarycentricDualVertexArea(this);
	}

	public WB_CoordinateSystem getCurvatureDirections() {
		return HE_MeshOp.getCurvatureDirections(this);
	}

	public double getGaussianCurvature() {
		return HE_MeshOp.getGaussianCurvature(this);
	}

	public double getGaussianCurvature(final WB_Vector meanCurvatureVector) {
		return HE_MeshOp.getGaussianCurvature(this, meanCurvatureVector);
	}

	public double[] getPrincipalCurvatures() {
		return HE_MeshOp.getPrincipalCurvatures(this);
	}

	public double getScalarGaussianCurvature() {
		return getAngleDefect();
	}

	public double getScalarMeanCurvature() {
		return HE_MeshOp.getScalarMeanCurvature(this);
	}

	public double getUmbrellaAngle() {
		return HE_MeshOp.getUmbrellaAngle(this);
	}

	public WB_CoordinateSystem getVertexCS() {
		return HE_MeshOp.getVertexCS(this);
	}

	public WB_Point getNormalOffsetPosition(final double d) {
		return HE_MeshOp.getNormalOffsetPosition(this, d);
	}

	public WB_Vector getVertexNormalAngle() {
		return HE_MeshOp.getVertexNormal(this);
	}

	public WB_Vector getVertexNormalArea() {
		return HE_MeshOp.getVertexNormalArea(this);
	}

	public WB_Vector getVertexNormalAverage() {
		return HE_MeshOp.getVertexNormalAverage(this);
	}

	public WB_Vector getVertexNormalGaussianCurvature() {
		return HE_MeshOp.getVertexNormalGaussianCurvature(this);
	}

	public WB_Vector getVertexNormalMeanCurvature() {
		return HE_MeshOp.getVertexNormalMeanCurvature(this);
	}

	public WB_Vector getVertexNormalSphereInscribed() {
		return HE_MeshOp.getVertexNormalSphereInscribed(this);
	}

	public WB_Classification getVertexType() {
		return HE_MeshOp.getVertexType(this);
	}
}
