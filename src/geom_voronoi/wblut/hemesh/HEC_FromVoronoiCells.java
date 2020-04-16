package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_Coord;

public class HEC_FromVoronoiCells extends HEC_Creator {
	private HE_MeshCollection cells;
	private boolean[] on;
	private boolean capBoundaries;
	private boolean membrane;

	public HEC_FromVoronoiCells() {
		super();
		setOverride(true);
		cells = null;
		on = null;
		capBoundaries = true;
		membrane = false;
	}

	public HEC_FromVoronoiCells setCells(final HE_MeshCollection cells) {
		this.cells = cells;
		return this;
	}

	public HEC_FromVoronoiCells setCells(final HE_Mesh[] cells) {
		this.cells = new HE_MeshCollection();
		for (final HE_Mesh cell : cells) {
			this.cells.add(cell);
		}
		return this;
	}

	public HEC_FromVoronoiCells setCells(final Collection<HE_Mesh> cells) {
		this.cells = new HE_MeshCollection();
		for (final HE_Mesh cell : cells) {
			this.cells.add(cell);
		}
		return this;
	}

	public HEC_FromVoronoiCells setActive(final boolean[] on) {
		this.on = on;
		return this;
	}

	@Deprecated
	public HEC_FromVoronoiCells setSurface(final boolean b) {
		this.capBoundaries = !b;
		return this;
	}

	public HEC_FromVoronoiCells setCapBoundaries(final boolean b) {
		this.capBoundaries = b;
		return this;
	}

	public HEC_FromVoronoiCells setMembrane(final boolean b) {
		this.membrane = b;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		if (cells == null) {
			return new HE_Mesh();
		}
		if (on == null) {
			return new HE_Mesh();
		}
		if (on.length > cells.size()) {
			return new HE_Mesh();
		}
		final int n = on.length;
		final HE_FaceList tmpfaces = new HE_FaceList();
		int nv = 0;
		for (int i = 0; i < n; i++) {
			final HE_Mesh m = cells.getMesh(i);
			if (on[i]) {
				final HE_FaceIterator fItr = m.fItr();
				while (fItr.hasNext()) {
					final HE_Face f = fItr.next();
					if (f.getInternalLabel() == -1) {
						tmpfaces.add(f);
						nv += f.getFaceDegree();
					} else if (!on[f.getInternalLabel()] || membrane) {
						tmpfaces.add(f);
						nv += f.getFaceDegree();
					}
				}
			}
		}
		final WB_Coord[] vertices = new WB_Coord[nv];
		final int[][] faces = new int[tmpfaces.size()][];
		final int[] labels = new int[tmpfaces.size()];
		final int[] intlabels = new int[tmpfaces.size()];
		final int[] colors = new int[tmpfaces.size()];
		int cid = 0;
		for (int i = 0; i < tmpfaces.size(); i++) {
			final HE_Face f = tmpfaces.get(i);
			faces[i] = new int[f.getFaceDegree()];
			labels[i] = f.getLabel();
			intlabels[i] = f.getInternalLabel();
			colors[i] = f.getColor();
			HE_Halfedge he = f.getHalfedge();
			for (int j = 0; j < f.getFaceDegree(); j++) {
				vertices[cid] = he.getVertex();
				faces[i][j] = cid;
				he = he.getNextInFace();
				cid++;
			}
		}
		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(vertices).setFaces(faces)
				.setCheckDuplicateVertices(true);
		final HE_Mesh result = ffl.createBase();
		final HE_FaceIterator fItr = result.fItr();
		int i = 0;
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			f.setLabel(labels[i]);
			f.setInternalLabel(intlabels[i]);
			f.setColor(colors[i]);
			i++;
		}
		HET_Fixer.fixNonManifoldVertices(result);
		if (!capBoundaries) {
			final HE_Selection sel = result.selectFacesWithInternalLabel(-1);
			final HE_FaceIterator fitr = sel.fItr();
			while (fitr.hasNext()) {
				result.deleteFace(fitr.next());
			}
			result.removeUnconnectedElements();
			HE_MeshOp.capHalfedges(result);
		} else {
			result.uncapBoundaryHalfedges();
			result.modify(new HEM_CapHoles());
			HE_MeshOp.capHalfedges(result);
		}
		return result;
	}
}
