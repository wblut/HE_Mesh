package wblut.hemesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import wblut.geom.WB_Point;

public class HEC_Dual extends HEC_Creator {
	private HE_Mesh source;

	public HEC_Dual() {
		super();
		setOverride(true);
		setModelViewOverride(true);
		parameters.set("fixnonplanarfaces", true);
		parameters.set("resetcenter", false);
		parameters.set("preserveboundary", false);
	}

	public HEC_Dual(final HE_Mesh mesh) {
		this();
		source = mesh;
	}

	protected boolean getFixNonPlanarFaces() {
		return parameters.get("fixnonplanarfaces", true);
	}

	protected boolean getResetCenter() {
		return parameters.get("resetcenter", false);
	}

	protected boolean getPreserveBoundaries() {
		return parameters.get("preserveboundaries", false);
	}

	public HEC_Dual setSource(final HE_Mesh mesh) {
		source = mesh;
		return this;
	}

	public HEC_Dual setResetCenter(final boolean b) {
		parameters.set("resetcenter", b);
		return this;
	}

	public HEC_Dual setPreserveBoundaries(final boolean b) {
		parameters.set("preserveboundaries", b);
		return this;
	}

	public HEC_Dual setFixNonPlanarFaces(final boolean b) {
		parameters.set("fixnonplanarfaces", b);
		return this;
	}

	@Override
	public HE_Mesh createBase() {
		final HE_Mesh result = new HE_Mesh();
		if (source == null || source.getNumberOfFaces() < 3) {
			return result;
		}
		final HashMap<Long, Long> faceVertexCorrelation = new HashMap<>();
		final HE_FaceIterator fItr = source.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			final HE_Vertex cv = new HE_Vertex(HE_MeshOp.getFaceCenter(f));
			faceVertexCorrelation.put(f.getKey(), cv.getKey());
			result.add(cv);
		}
		HE_Halfedge he;
		if (getPreserveBoundaries()) {
			final Iterator<HE_Halfedge> heItr = source.heItr();
			while (heItr.hasNext()) {
				he = heItr.next();
				if (he.isOuterBoundary()) {
					HE_Vertex cv = new HE_Vertex(HE_MeshOp.getEdgeCenter(he));
					faceVertexCorrelation.put(he.getKey(), cv.getKey());
					result.add(cv);
					cv = new HE_Vertex(he.getVertex());
					faceVertexCorrelation.put(he.getVertex().getKey(), cv.getKey());
					result.add(cv);
				}
			}
		}
		final Iterator<HE_Vertex> vItr = source.vItr();
		HE_Vertex v;
		final List<WB_Point> centers = new ArrayList<>();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (!v.isBoundary()) {
				he = v.getHalfedge();
				final List<HE_Halfedge> faceHalfedges = new ArrayList<>();
				final HE_Face nf = new HE_Face();
				final WB_Point p = new WB_Point();
				int n = 0;
				do {
					final HE_Halfedge hen = new HE_Halfedge();
					faceHalfedges.add(hen);
					result.setFace(hen, nf);
					final Long key = faceVertexCorrelation.get(he.getFace().getKey());
					result.setVertex(hen, result.getVertexWithKey(key));
					p.addSelf(hen.getVertex());
					n++;
					if (hen.getVertex().getHalfedge() == null) {
						result.setHalfedge(hen.getVertex(), hen);
					}
					if (nf.getHalfedge() == null) {
						result.setHalfedge(nf, hen);
					}
					he = he.getNextInVertex();
				} while (he != v.getHalfedge());
				p.divSelf(n);
				centers.add(p);
				HE_MeshOp.cycleHalfedges(result, faceHalfedges);
				result.addHalfedges(faceHalfedges);
				result.add(nf);
			} else if (getPreserveBoundaries()) {
				he = v.getHalfedge();
				while (!he.isOuterBoundary()) {
					he = he.getNextInVertex();
				}
				final HE_Halfedge start = he;
				final List<HE_Halfedge> faceHalfedges = new ArrayList<>();
				final HE_Face nf = new HE_Face();
				final WB_Point p = new WB_Point();
				int n = 0;
				HE_Halfedge hen = new HE_Halfedge();
				faceHalfedges.add(hen);
				result.setFace(hen, nf);
				Long key = faceVertexCorrelation.get(v.getKey());
				result.setVertex(hen, result.getVertexWithKey(key));
				p.addSelf(hen.getVertex());
				n++;
				hen = new HE_Halfedge();
				faceHalfedges.add(hen);
				result.setFace(hen, nf);
				key = faceVertexCorrelation.get(he.getKey());
				result.setVertex(hen, result.getVertexWithKey(key));
				p.addSelf(hen.getVertex());
				n++;
				result.setHalfedge(hen.getVertex(), hen);
				result.setHalfedge(nf, hen);
				he = he.getNextInVertex();
				do {
					hen = new HE_Halfedge();
					faceHalfedges.add(hen);
					result.setFace(hen, nf);
					key = he.isOuterBoundary() ? faceVertexCorrelation.get(he.getKey())
							: faceVertexCorrelation.get(he.getFace().getKey());
					result.setVertex(hen, result.getVertexWithKey(key));
					p.addSelf(hen.getVertex());
					n++;
					he = he.getNextInVertex();
				} while (he != start);
				he = he.getPrevInVertex();
				hen = new HE_Halfedge();
				faceHalfedges.add(hen);
				result.setFace(hen, nf);
				key = faceVertexCorrelation.get(he.getPair().getKey());
				result.setVertex(hen, result.getVertexWithKey(key));
				p.addSelf(hen.getVertex());
				n++;
				p.divSelf(n);
				centers.add(p);
				HE_MeshOp.cycleHalfedges(result, faceHalfedges);
				result.addHalfedges(faceHalfedges);
				result.add(nf);
			}
		}
		HE_MeshOp.pairHalfedges(result);
		HE_MeshOp.capHalfedges(result);
		if (getResetCenter()) {
			result.moveToSelf(HE_MeshOp.getCenter(source));
		}
		HE_MeshOp.flipFaces(result);
		final List<HE_Face> faces = result.getFaces();
		final int fs = faces.size();
		final boolean fixNonPlanarFaces = getFixNonPlanarFaces();
		for (int i = 0; i < fs; i++) {
			if (!faces.get(i).isPlanar() && fixNonPlanarFaces) {
				HEM_TriSplit.splitFaceTri(result, faces.get(i), centers.get(i));
			}
		}
		return result;
	}
}
