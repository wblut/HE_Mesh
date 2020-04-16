package wblut.hemesh;

import java.util.Set;

import org.eclipse.collections.impl.set.mutable.UnifiedSet;

import wblut.math.WB_Epsilon;

public class HEM_Contours extends HEM_Modifier {
	boolean dblAtt;
	boolean fltAtt;
	boolean intAtt;

	public HEM_Contours setAttribute(final String name) {
		parameters.set("attribute", name);
		return this;
	}

	String getAttribute() {
		return (String) parameters.get("attribute", null);
	}

	public HEM_Contours setLevel(final double level) {
		parameters.set("level", level);
		return this;
	}

	double getLevel() {
		return parameters.get("level", 0.0);
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final String attribute = getAttribute();
		dblAtt = mesh.getDoubleAttribute(attribute) != null;
		fltAtt = mesh.getFloatAttribute(attribute) != null;
		intAtt = mesh.getIntegerAttribute(attribute) != null;
		HE_MeshOp.triangulate(mesh);
		mesh.resetVertexInternalLabels();
		final double level = getLevel();
		if (attribute == null) {
			return mesh;
		}
		final HE_DoubleMap values = getValues(mesh, attribute);
		if (values == null) {
			return mesh;
		}
		HE_Vertex p, q;
		double intersection;
		double sidep, sideq, signp, signq;
		final HE_EdgeIterator eItr = mesh.eItr();
		HE_Halfedge edge;
		final HE_HalfedgeList edgesToSplit = new HE_HalfedgeList();
		final Set<HE_Face> facesToSplit = new UnifiedSet<>();
		final HE_DoubleMap intersections = new HE_DoubleMap();
		while (eItr.hasNext()) {
			edge = eItr.next();
			p = edge.getStartVertex();
			q = edge.getEndVertex();
			sidep = values.get(p) - level;
			sideq = values.get(q) - level;
			signp = Math.signum(sidep);
			signq = Math.signum(sideq);
			if (signp != signq) {
				intersection = -sidep / (sideq - sidep);// i=p+intersection*(q-p)
				edgesToSplit.add(edge);
				intersections.put(edge, intersection);
				if (edge.getFace() != null) {
					facesToSplit.add(edge.getFace());
				}
				if (edge.getPair().getFace() != null) {
					facesToSplit.add(edge.getPair().getFace());
				}
			}
		}
		HE_Selection sel;
		HE_Vertex newv;
		for (final HE_Halfedge e : edgesToSplit) {
			intersection = intersections.get(e);
			if (WB_Epsilon.isZero(intersection)) {
				e.getStartVertex().setInternalLabel(1);
			} else if (WB_Epsilon.isEqual(intersection, 1.0)) {
				e.getEndVertex().setInternalLabel(1);
			} else {
				sel = HE_MeshOp.splitEdge(mesh, e, intersection);
				newv = sel.getVertexWithIndex(0);
				if (dblAtt) {
					mesh.setDoubleAttribute(newv, attribute, level);
				} else if (fltAtt) {
					mesh.setFloatAttribute(newv, attribute, (float) level);
				} else if (intAtt) {
					mesh.setIntegerAttribute(newv, attribute, (int) level);
				}
				values.put(newv, level);
			}
		}
		mesh.selectVerticesWithInternalLabel("splitVertices", 1);
		HE_FaceVertexCirculator fvCrc;
		HE_Vertex v;
		HE_Vertex v0 = null;
		HE_Vertex v1 = null;
		final HE_Selection edges = mesh.getNewSelection("splitEdges");
		final HE_Selection faces = mesh.getNewSelection("splitFaces");
		for (final HE_Face f : facesToSplit) {
			v0 = null;
			v1 = null;
			fvCrc = f.fvCrc();
			while (fvCrc.hasNext()) {
				v = fvCrc.next();
				if (v.getInternalLabel() == 1) {
					if (v0 == null) {
						v0 = v;
					} else if (v1 == null) {
						v1 = v;
					} else {
						// This shouldn't happen...
					}
				}
			}
			if (v0 != null && v1 != null) {
				sel = HE_MeshOp.splitFace(mesh, f, v0, v1);
				if (sel != null) {
					edges.addEdges(sel.getEdges());
					faces.addFaces(sel.getFaces());
					faces.add(f);
				} else {
					HE_Halfedge he = f.getHalfedge(v0);
					if (he.getNextInFace().getVertex() == v1) {
						edges.add(he.getEdge());
						faces.add(f);
						if (he.getPair().getFace() != null) {
							faces.add(he.getPair().getFace());
						}
					} else if (he.getPrevInFace().getVertex() == v1) {
						he = he.getPrevInFace();
						edges.add(he.getEdge());
						faces.add(f);
						if (he.getPair().getFace() != null) {
							faces.add(he.getPair().getFace());
						}
					}
				}
			}
		}
		final HE_Selection insideFaces = mesh.getNewSelection("inside");
		final HE_Selection outsideFaces = mesh.getNewSelection("outside");
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			fvCrc = f.fvCrc();
			boolean outside = false;
			while (fvCrc.hasNext()) {
				v = fvCrc.next();
				if (v.getInternalLabel() == 1) {
					continue;
				} else if (values.get(v) < level) {
					outside = true;
					break;
				}
			}
			if (outside) {
				outsideFaces.add(f);
			} else {
				insideFaces.add(f);
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HE_Mesh mesh = selection.getParent();
		selection.collectEdgesByFace();
		selection.collectVertices();
		final String attribute = getAttribute();
		dblAtt = mesh.getDoubleAttribute(attribute) != null;
		fltAtt = mesh.getFloatAttribute(attribute) != null;
		intAtt = mesh.getIntegerAttribute(attribute) != null;
		HE_MeshOp.triangulate(mesh);
		mesh.resetVertexInternalLabels();
		final double level = getLevel();
		if (attribute == null) {
			return mesh;
		}
		final HE_DoubleMap values = getValues(selection, attribute);
		if (values == null) {
			return mesh;
		}
		HE_Vertex p, q;
		double intersection;
		double sidep, sideq, signp, signq;
		final HE_EdgeIterator eItr = selection.eItr();
		HE_Halfedge edge;
		final HE_HalfedgeList edgesToSplit = new HE_HalfedgeList();
		final Set<HE_Face> facesToSplit = new UnifiedSet<>();
		final HE_DoubleMap intersections = new HE_DoubleMap();
		while (eItr.hasNext()) {
			edge = eItr.next();
			p = edge.getStartVertex();
			q = edge.getEndVertex();
			sidep = values.get(p) - level;
			sideq = values.get(q) - level;
			signp = Math.signum(sidep);
			signq = Math.signum(sideq);
			if (signp != signq) {
				intersection = -sidep / (sideq - sidep);// i=p+intersection*(q-p)
				edgesToSplit.add(edge);
				intersections.put(edge, intersection);
				if (edge.getFace() != null) {
					facesToSplit.add(edge.getFace());
				}
				if (edge.getPair().getFace() != null) {
					facesToSplit.add(edge.getPair().getFace());
				}
			}
		}
		HE_Selection sel;
		HE_Vertex newv;
		for (final HE_Halfedge e : edgesToSplit) {
			intersection = intersections.get(e);
			if (WB_Epsilon.isZero(intersection)) {
				e.getStartVertex().setInternalLabel(1);
			} else if (WB_Epsilon.isEqual(intersection, 1.0)) {
				e.getEndVertex().setInternalLabel(1);
			} else {
				sel = HE_MeshOp.splitEdge(mesh, e, intersection);
				newv = sel.getVertexWithIndex(0);
				if (dblAtt) {
					mesh.setDoubleAttribute(newv, attribute, level);
				} else if (fltAtt) {
					mesh.setFloatAttribute(newv, attribute, (float) level);
				} else if (intAtt) {
					mesh.setIntegerAttribute(newv, attribute, (int) level);
				}
				values.put(newv, level);
			}
		}
		mesh.selectVerticesWithInternalLabel("splitVertices", 1);
		HE_FaceVertexCirculator fvCrc;
		HE_Vertex v;
		HE_Vertex v0 = null;
		HE_Vertex v1 = null;
		final HE_Selection edges = mesh.getNewSelection("splitEdges");
		final HE_Selection faces = mesh.getNewSelection("splitFaces");
		for (final HE_Face f : facesToSplit) {
			v0 = null;
			v1 = null;
			fvCrc = f.fvCrc();
			while (fvCrc.hasNext()) {
				v = fvCrc.next();
				if (v.getInternalLabel() == 1) {
					if (v0 == null) {
						v0 = v;
					} else if (v1 == null) {
						v1 = v;
					} else {
						// This shouldn't happen...
					}
				}
			}
			if (v0 != null && v1 != null) {
				sel = HE_MeshOp.splitFace(mesh, f, v0, v1);
				if (sel != null) {
					edges.addEdges(sel.getEdges());
					faces.addFaces(sel.getFaces());
					faces.add(f);
				} else {
					HE_Halfedge he = f.getHalfedge(v0);
					if (he.getNextInFace().getVertex() == v1) {
						edges.add(he.getEdge());
						faces.add(f);
						if (he.getPair().getFace() != null) {
							faces.add(he.getPair().getFace());
						}
					} else if (he.getPrevInFace().getVertex() == v1) {
						he = he.getPrevInFace();
						edges.add(he.getEdge());
						faces.add(f);
						if (he.getPair().getFace() != null) {
							faces.add(he.getPair().getFace());
						}
					}
				}
			}
		}
		final HE_Selection insideFaces = mesh.getNewSelection("inside");
		final HE_Selection outsideFaces = mesh.getNewSelection("outside");
		final HE_FaceIterator fItr = selection.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			fvCrc = f.fvCrc();
			boolean outside = false;
			while (fvCrc.hasNext()) {
				v = fvCrc.next();
				if (v.getInternalLabel() == 1) {
					continue;
				} else if (values.get(v) < level) {
					outside = true;
					break;
				}
			}
			if (outside) {
				outsideFaces.add(f);
			} else {
				insideFaces.add(f);
			}
		}
		return mesh;
	}

	private HE_DoubleMap getValues(final HE_HalfedgeStructure structure, final String attribute) {
		final HE_DoubleMap values = new HE_DoubleMap();
		HE_Mesh mesh = null;
		if (structure instanceof HE_Mesh) {
			mesh = (HE_Mesh) structure;
		} else if (structure instanceof HE_Selection) {
			mesh = ((HE_Selection) structure).getParent();
		}
		if (!dblAtt && !fltAtt && !intAtt) {
			return null;
		}
		HE_Vertex v;
		double value;
		final HE_VertexIterator vItr = structure.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (dblAtt) {
				value = mesh.getDoubleAttributeForElement(v, attribute);
			} else if (fltAtt) {
				value = mesh.getFloatAttributeForElement(v, attribute);
			} else {
				value = mesh.getIntegerAttributeForElement(v, attribute);
			}
			values.put(v, value);
		}
		return values;
	}
}
