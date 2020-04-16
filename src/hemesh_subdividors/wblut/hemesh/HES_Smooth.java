package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;

public class HES_Smooth extends HES_Subdividor {
	private boolean keepEdges = true;
	private boolean keepBoundary = false;
	private double origWeight;
	private double neigWeight;

	public HES_Smooth() {
		super();
		origWeight = 1.0;
		neigWeight = 1.0;
	}

	public HES_Smooth setKeepEdges(final boolean b) {
		keepEdges = b;
		return this;
	}

	public HES_Smooth setKeepBoundary(final boolean b) {
		keepBoundary = b;
		return this;
	}

	public HES_Smooth setWeight(final double origWeight, final double neigWeight) {
		this.origWeight = origWeight;
		this.neigWeight = neigWeight;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_MeshOp.splitFacesQuad(mesh);
		final WB_Coord[] newPositions = new WB_Coord[mesh.getNumberOfVertices()];
		final HE_Selection all = mesh.selectAllFaces();
		final List<HE_Vertex> boundary = all.getOuterVertices();
		final List<HE_Vertex> inner = all.getInnerVertices();
		HE_Vertex v;
		HE_Vertex n;
		List<HE_Vertex> neighbors;
		int id = 0;
		Iterator<HE_Vertex> vItr = inner.iterator();
		while (vItr.hasNext()) {
			v = vItr.next();
			final WB_Point p = new WB_Point(v);
			neighbors = v.getNeighborVertices();
			p.mulSelf(origWeight);
			double c = origWeight;
			for (int i = 0; i < neighbors.size(); i++) {
				n = neighbors.get(i);
				p.addSelf(neigWeight * n.xd(), neigWeight * n.yd(), neigWeight * n.zd());
				c += neigWeight;
			}
			newPositions[id] = p.scaleSelf(1.0 / c);
			id++;
		}
		vItr = boundary.iterator();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (keepBoundary) {
				newPositions[id] = v;
			} else {
				final WB_Point p = new WB_Point(v);
				neighbors = v.getNeighborVertices();
				p.mulSelf(origWeight);
				double c = origWeight;
				int nc = 0;
				for (int i = 0; i < neighbors.size(); i++) {
					n = neighbors.get(i);
					if (boundary.contains(n)) {
						p.addSelf(neigWeight * n.xd(), neigWeight * n.yd(), neigWeight * n.zd());
						c += neigWeight;
						nc++;
					}
				}
				newPositions[id] = nc > 1 ? p.scaleSelf(1.0 / c) : v;
			}
			id++;
		}
		vItr = inner.iterator();
		id = 0;
		while (vItr.hasNext()) {
			vItr.next().set(newPositions[id]);
			id++;
		}
		vItr = boundary.iterator();
		while (vItr.hasNext()) {
			vItr.next().set(newPositions[id]);
			id++;
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		HE_MeshOp.splitFacesQuad(selection);
		final List<WB_Coord> newPositions = new ArrayList<>();
		final List<HE_Vertex> boundary = selection.getAllBoundaryVertices();
		final List<HE_Vertex> inner = selection.getInnerVertices();
		final List<HE_Vertex> outer = selection.getOuterVertices();
		List<HE_Face> sharedFaces;
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = outer.iterator();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (boundary.contains(v)) {
				vItr.remove();
			}
		}
		HE_Vertex n;
		List<HE_Vertex> neighbors;
		int id = 0;
		vItr = inner.iterator();
		while (vItr.hasNext()) {
			v = vItr.next();
			final WB_Point p = new WB_Point(v);
			neighbors = v.getNeighborVertices();
			p.mulSelf(origWeight);
			double c = origWeight;
			for (int i = 0; i < neighbors.size(); i++) {
				n = neighbors.get(i);
				p.addSelf(neigWeight * n.xd(), neigWeight * n.yd(), neigWeight * n.zd());
				c += neigWeight;
			}
			newPositions.add(p.scaleSelf(1.0 / c));
			id++;
		}
		vItr = boundary.iterator();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (keepBoundary) {
				newPositions.add(v);
			} else {
				final WB_Point p = new WB_Point(v);
				neighbors = v.getNeighborVertices();
				p.mulSelf(origWeight);
				double c = origWeight;
				int nc = 0;
				for (int i = 0; i < neighbors.size(); i++) {
					n = neighbors.get(i);
					if (boundary.contains(n)) {
						p.addSelf(neigWeight * n.xd(), neigWeight * n.yd(), neigWeight * n.zd());
						c += neigWeight;
						nc++;
					}
				}
				newPositions.add(nc > 1 ? p.scaleSelf(1.0 / c) : v);
			}
			id++;
		}
		vItr = outer.iterator();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (keepEdges || HE_MeshOp.getVertexType(v) != WB_Classification.FLAT) {
				newPositions.add(v);
			} else {
				final WB_Point p = new WB_Point(v);
				neighbors = v.getNeighborVertices();
				p.mulSelf(origWeight);
				double c = origWeight;
				int nc = 0;
				for (int i = 0; i < neighbors.size(); i++) {
					n = neighbors.get(i);
					if (outer.contains(n)) {
						sharedFaces = selection.getParent().getSharedFaces(v, n);
						boolean singleFaceGap = true;
						for (int j = 0; j < sharedFaces.size(); j++) {
							if (selection.contains(sharedFaces.get(j))) {
								singleFaceGap = false;
								break;
							}
						}
						if (!singleFaceGap) {
							p.addSelf(neigWeight * n.xd(), neigWeight * n.yd(), neigWeight * n.zd());
							c += neigWeight;
							nc++;
						}
					}
				}
				newPositions.add(nc > 1 ? p.scaleSelf(1.0 / c) : v);
			}
			id++;
		}
		vItr = inner.iterator();
		id = 0;
		while (vItr.hasNext()) {
			vItr.next().set(newPositions.get(id));
			id++;
		}
		vItr = boundary.iterator();
		while (vItr.hasNext()) {
			vItr.next().set(newPositions.get(id));
			id++;
		}
		vItr = outer.iterator();
		while (vItr.hasNext()) {
			vItr.next().set(newPositions.get(id));
			id++;
		}
		return selection.getParent();
	}
}
