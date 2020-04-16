package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Plane;

public class HE_Selection extends HE_MeshElement implements HE_HalfedgeStructure {
	private HE_Mesh parent;
	String createdBy;
	protected WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	private HE_RAS<HE_Vertex> vertices;
	private HE_RAS<HE_Halfedge> halfedges;
	private HE_RAS<HE_Halfedge> edges;
	private HE_RAS<HE_Face> faces;
	String name;

	private HE_Selection() {
		super();
		vertices = new HE_RAS<>();
		halfedges = new HE_RAS<>();
		edges = new HE_RAS<>();
		faces = new HE_RAS<>();
	}

	public HE_Selection(final HE_Mesh parent) {
		this();
		this.parent = parent;
	}

	static HE_Selection getSelection(final HE_Mesh parent) {
		return new HE_Selection(parent);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public final int getNumberOfFaces() {
		return faces.size();
	}

	@Override
	public final int getNumberOfHalfedges() {
		return halfedges.size() + edges.size();
	}

	@Override
	public int getNumberOfEdges() {
		return edges.size();
	}

	@Override
	public final int getNumberOfVertices() {
		return vertices.size();
	}

	@Override
	public final HE_Face getFaceWithKey(final long key) {
		return faces.getWithKey(key);
	}

	@Override
	public final HE_Halfedge getHalfedgeWithKey(final long key) {
		HE_Halfedge he = edges.getWithKey(key);
		if (he != null) {
			return he;
		}
		he = halfedges.getWithKey(key);
		return he;
	}

	@Override
	public final HE_Halfedge getEdgeWithKey(final long key) {
		HE_Halfedge he = edges.getWithKey(key);
		if (he != null) {
			return he;
		}
		he = halfedges.getWithKey(key);
		return he;
	}

	@Override
	public final HE_Vertex getVertexWithKey(final long key) {
		return vertices.getWithKey(key);
	}

	@Override
	public final HE_Face getFaceWithIndex(final int i) {
		if (i < 0 || i >= faces.size()) {
			throw new IndexOutOfBoundsException("Requested face index " + i + "not in range.");
		}
		return faces.getWithIndex(i);
	}

	@Override
	public final HE_Halfedge getHalfedgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size() + halfedges.size()) {
			throw new IndexOutOfBoundsException("Requested halfedge index " + i + "not in range.");
		}
		if (i >= edges.size()) {
			return halfedges.getWithIndex(i - edges.size());
		}
		return edges.getWithIndex(i);
	}

	@Override
	public final HE_Halfedge getEdgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size()) {
			throw new IndexOutOfBoundsException("Requested edge index " + i + "not in range.");
		}
		return edges.getWithIndex(i);
	}

	@Override
	public final HE_Vertex getVertexWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException("Requested vertex index " + i + "not in range.");
		}
		return vertices.getWithIndex(i);
	}

	@Override
	public final void add(final HE_Element el) {
		if (el instanceof HE_Face) {
			add((HE_Face) el);
		} else if (el instanceof HE_Vertex) {
			add((HE_Vertex) el);
		} else if (el instanceof HE_Halfedge) {
			add((HE_Halfedge) el);
		}
	}

	@Override
	public final void add(final HE_Face f) {
		faces.add(f);
	}

	@Override
	public void add(final HE_Halfedge he) {
		if (he.isEdge()) {
			edges.add(he);
		} else {
			halfedges.add(he);
		}
	}

	@Override
	public final void add(final HE_Vertex v) {
		vertices.add(v);
	}

	@Override
	public void add(final HE_Mesh mesh) {
		addVertices(mesh.getVertices());
		addFaces(mesh.getFaces());
		addHalfedges(mesh.getHalfedges());
		addHalfedges(mesh.getEdges());
	}

	@Override
	public final void addFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			add(face);
		}
	}

	@Override
	public final void addFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			add(f);
		}
	}

	@Override
	public final void addFaces(final HE_HalfedgeStructure source) {
		faces.addAll(source.getFaces());
	}

	@Override
	public final void addHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			add(halfedge);
		}
	}

	@Override
	public final void addHalfedges(final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			add(he);
		}
	}

	@Override
	public final void addHalfedges(final HE_HalfedgeStructure source) {
		for (final HE_Halfedge he : source.getHalfedges()) {
			add(he);
		}
	}

	public final void addEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			add(edge);
		}
	}

	public final void addEdges(final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			add(e);
		}
	}

	public final void addEdges(final HE_HalfedgeStructure source) {
		edges.addAll(source.getEdges());
	}

	@Override
	public final void addVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertex : vertices) {
			add(vertex);
		}
	}

	@Override
	public final void addVertices(final HE_HalfedgeStructure source) {
		vertices.addAll(source.getVertices());
	}

	@Override
	public final void addVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			add(v);
		}
	}

	@Override
	public void remove(final HE_Face f) {
		faces.remove(f);
	}

	@Override
	public void remove(final HE_Halfedge he) {
		edges.remove(he);
		halfedges.remove(he);
	}

	@Override
	public void remove(final HE_Vertex v) {
		vertices.remove(v);
	}

	@Override
	public final void removeFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			remove(face);
		}
	}

	@Override
	public final void removeFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			remove(f);
		}
	}

	@Override
	public final void removeHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			remove(halfedge);
		}
	}

	@Override
	public final void removeHalfedges(final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			remove(he);
		}
	}

	@Override
	public final void removeEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			remove(edge);
		}
	}

	@Override
	public final void removeEdges(final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			remove(e);
		}
	}

	@Override
	public final void removeVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertice : vertices) {
			remove(vertice);
		}
	}

	@Override
	public final void removeVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			remove(v);
		}
	}

	@Override
	public void clear() {
		clearVertices();
		clearHalfedges();
		clearFaces();
	}

	@Override
	public void clearFaces() {
		faces = new HE_RAS<>();
	}

	@Override
	public void clearHalfedges() {
		halfedges = new HE_RAS<>();
		edges = new HE_RAS<>();
	}

	@Override
	public final void clearEdges() {
		edges = new HE_RAS<>();
	}

	@Override
	public void clearVertices() {
		vertices = new HE_RAS<>();
	}

	void clearFacesNoSelectionCheck() {
		faces = new HE_RAS<>();
	}

	void clearVerticesNoSelectionCheck() {
		vertices = new HE_RAS<>();
	}

	@Override
	public final boolean contains(final HE_Element el) {
		if (el instanceof HE_Face) {
			return contains((HE_Face) el);
		} else if (el instanceof HE_Vertex) {
			return contains((HE_Vertex) el);
		} else if (el instanceof HE_Halfedge) {
			return contains((HE_Halfedge) el);
		}
		return false;
	}

	@Override
	public final boolean contains(final HE_Face f) {
		return faces.contains(f);
	}

	@Override
	public final boolean contains(final HE_Halfedge he) {
		return edges.contains(he) || halfedges.contains(he);
	}

	@Override
	public final boolean contains(final HE_Vertex v) {
		return vertices.contains(v);
	}

	@Override
	public final List<HE_Vertex> getVertices() {
		return new HE_VertexList(vertices.getObjects());
	}

	@Override
	public final HE_Vertex[] getVerticesAsArray() {
		final HE_Vertex[] vertices = new HE_Vertex[getNumberOfVertices()];
		final Collection<HE_Vertex> _vertices = this.vertices;
		final Iterator<HE_Vertex> vitr = _vertices.iterator();
		int i = 0;
		while (vitr.hasNext()) {
			vertices[i] = vitr.next();
			i++;
		}
		return vertices;
	}

	@Override
	public final List<HE_Halfedge> getHalfedges() {
		final List<HE_Halfedge> halfedges = new HE_HalfedgeList();
		halfedges.addAll(this.halfedges);
		halfedges.addAll(this.edges);
		return halfedges;
	}

	@Override
	public final HE_Halfedge[] getHalfedgesAsArray() {
		final List<HE_Halfedge> hes = getHalfedges();
		final HE_Halfedge[] halfedges = new HE_Halfedge[hes.size()];
		int i = 0;
		for (final HE_Halfedge he : hes) {
			halfedges[i] = he;
			i++;
		}
		return halfedges;
	}

	@Override
	public final List<HE_Halfedge> getEdges() {
		return new HE_HalfedgeList(edges.getObjects());
	}

	@Override
	public final HE_Halfedge[] getEdgesAsArray() {
		final HE_Halfedge[] edges = new HE_Halfedge[getNumberOfEdges()];
		final Iterator<HE_Halfedge> eItr = eItr();
		int i = 0;
		while (eItr.hasNext()) {
			edges[i] = eItr.next();
			i++;
		}
		return edges;
	}

	@Override
	public final List<HE_Face> getFaces() {
		return new HE_FaceList(faces.getObjects());
	}

	@Override
	public final HE_Face[] getFacesAsArray() {
		final HE_Face[] faces = new HE_Face[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = this.faces.iterator();
		int i = 0;
		while (fItr.hasNext()) {
			faces[i] = fItr.next();
			i++;
		}
		return faces;
	}

	@Override
	public final boolean containsFace(final long key) {
		return faces.containsKey(key);
	}

	@Override
	public final boolean containsHalfedge(final long key) {
		return halfedges.containsKey(key) || edges.containsKey(key);
	}

	@Override
	public final boolean containsEdge(final long key) {
		return edges.containsKey(key);
	}

	@Override
	public final boolean containsVertex(final long key) {
		return vertices.containsKey(key);
	}

	@Override
	public final int getIndex(final HE_Face f) {
		return faces.indexOf(f);
	}

	@Override
	public final int getIndex(final HE_Halfedge edge) {
		return edges.indexOf(edge);
	}

	@Override
	public final int getIndex(final HE_Vertex v) {
		return vertices.indexOf(v);
	}

	@Override
	public HE_VertexIterator vItr() {
		final List<HE_Vertex> vs = new HE_VertexList(vertices);
		return new HE_VertexIterator(vs);
	}

	@Override
	public HE_EdgeIterator eItr() {
		final List<HE_Halfedge> es = new HE_HalfedgeList(edges);
		return new HE_EdgeIterator(es);
	}

	@Override
	public HE_HalfedgeIterator heItr() {
		final List<HE_Halfedge> hes = new HE_HalfedgeList(getHalfedges());
		return HE_HalfedgeIterator.getIterator(hes);
	}

	@Override
	public HE_FaceIterator fItr() {
		final List<HE_Face> fs = new HE_FaceList(getFaces());
		return new HE_FaceIterator(fs);
	}

	public List<HE_Halfedge> getAllBoundaryHalfedges() {
		final List<HE_Halfedge> boundaryHalfedges = new HE_HalfedgeList();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				boundaryHalfedges.add(he);
			}
		}
		return boundaryHalfedges;
	}

	@Override
	public HE_Mesh modify(final HEM_Modifier modifier) {
		modifier.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor) {
		subdividor.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor, final int rep) {
		for (int i = 0; i < rep; i++) {
			subdividor.apply(this);
			parent.clearPrecomputed();
		}
		return this.parent;
	}

	@Override
	public HE_Mesh simplify(final HES_Simplifier simplifier) {
		simplifier.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	public List<HE_Halfedge> getOuterEdges() {
		final HE_Selection sel = get();
		sel.collectEdgesByFace();
		final List<HE_Halfedge> result = new HE_HalfedgeList();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.isEdge()) {
				final HE_Face f1 = he.getFace();
				final HE_Face f2 = he.getPair().getFace();
				if (f1 == null || f2 == null || !contains(f1) || !contains(f2)) {
					result.add(he);
				}
			}
		}
		return result;
	}

	public List<HE_Halfedge> getInnerEdges() {
		final HE_Selection sel = get();
		sel.collectEdgesByFace();
		final List<HE_Halfedge> result = new HE_HalfedgeList();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.isEdge()) {
				final HE_Face f1 = he.getFace();
				final HE_Face f2 = he.getPair().getFace();
				if (!(f1 == null || f2 == null || !contains(f1) || !contains(f2))) {
					result.add(he);
				}
			}
		}
		return result;
	}

	public List<HE_Vertex> getOuterVertices() {
		final List<HE_Vertex> result = new HE_VertexList();
		final List<HE_Halfedge> outerEdges = getOuterEdges();
		for (int i = 0; i < outerEdges.size(); i++) {
			final HE_Halfedge e = outerEdges.get(i);
			final HE_Vertex v1 = e.getVertex();
			final HE_Vertex v2 = e.getEndVertex();
			if (!result.contains(v1)) {
				result.add(v1);
			}
			if (!result.contains(v2)) {
				result.add(v2);
			}
		}
		return result;
	}

	public List<HE_Vertex> getInnerVertices() {
		final HE_Selection sel = get();
		sel.collectVertices();
		final List<HE_Vertex> result = new HE_VertexList();
		final List<HE_Vertex> outerVertices = getOuterVertices();
		final HE_VertexIterator vItr = sel.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (!outerVertices.contains(v)) {
				result.add(v);
			}
		}
		return result;
	}

	public List<HE_Vertex> getAllBoundaryVertices() {
		final List<HE_Vertex> result = new HE_VertexList();
		final List<HE_Halfedge> outerEdges = getOuterEdges();
		for (int i = 0; i < outerEdges.size(); i++) {
			final HE_Halfedge e = outerEdges.get(i);
			if (e.getFace() == null || e.getPair().getFace() == null) {
				final HE_Vertex v1 = e.getVertex();
				final HE_Vertex v2 = e.getEndVertex();
				if (!result.contains(v1)) {
					result.add(v1);
				}
				if (!result.contains(v2)) {
					result.add(v2);
				}
			}
		}
		return result;
	}

	public List<HE_Face> getBoundaryFaces() {
		final List<HE_Face> boundaryFaces = new HE_FaceList();
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			final HE_FaceFaceCirculator ffCrc = f.ffCrc();
			while (ffCrc.hasNext()) {
				if (!contains(ffCrc.next())) {
					boundaryFaces.add(f);
					break;
				}
			}
		}
		return boundaryFaces;
	}

	public List<HE_Halfedge> getOuterHalfedges() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final List<HE_Halfedge> result = new HE_HalfedgeList();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			final HE_Face f1 = he.getFace();
			if (f1 == null || !contains(f1)) {
				result.add(he);
			}
		}
		return result;
	}

	public List<HE_Halfedge> getOuterHalfedgesInside() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final List<HE_Halfedge> result = new HE_HalfedgeList();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			final HE_Face f1 = he.getPair().getFace();
			if (f1 == null || !contains(f1)) {
				result.add(he);
			}
		}
		return result;
	}

	public List<HE_Halfedge> getInnerHalfedges() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final List<HE_Halfedge> result = new HE_HalfedgeList();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (contains(he.getPair().getFace()) && contains(he.getFace())) {
				result.add(he);
			}
		}
		return result;
	}

	public HE_Selection get() {
		final HE_Selection copy = new HE_Selection(parent);
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			copy.add(f);
		}
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			copy.add(he);
		}
		final HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			copy.add(v);
		}
		copy.createdBy = createdBy == null ? null : createdBy;
		return copy;
	}

	public HE_Mesh getAsMesh() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	void completeFromFaces() {
		this.clearHalfedges();
		this.clearVertices();
		HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		HE_Halfedge he;
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceVertexCirculator fvcrc = new HE_FaceVertexCirculator(f);
			while (fvcrc.hasNext()) {
				add(fvcrc.next());
			}
			final HE_FaceHalfedgeInnerCirculator fheicrc = new HE_FaceHalfedgeInnerCirculator(f);
			while (fheicrc.hasNext()) {
				he = fheicrc.next();
				add(he);
			}
		}
		fitr = this.fItr();
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceHalfedgeInnerCirculator fheicrc = new HE_FaceHalfedgeInnerCirculator(f);
			while (fheicrc.hasNext()) {
				he = fheicrc.next();
				if (!contains(he.getVertex().getHalfedge())) {
					parent.setHalfedge(he.getVertex(), he);
				}
			}
		}
	}

	public void add(final HE_Selection sel) {
		final HE_FaceIterator fItr = sel.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			add(f);
		}
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			add(he);
		}
		final HE_VertexIterator vItr = sel.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			add(v);
		}
	}

	public void union(final HE_Selection sel) {
		add(sel);
	}

	public void subtract(final HE_Selection sel) {
		final HE_FaceIterator fItr = sel.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			remove(f);
		}
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			remove(he);
		}
		final HE_VertexIterator vItr = sel.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			remove(v);
		}
	}

	public void intersect(final HE_Selection sel) {
		final HE_RAS<HE_Face> newFaces = new HE_RAS<>();
		final HE_FaceIterator fItr = sel.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (contains(f)) {
				newFaces.add(f);
			}
		}
		clearFaces();
		addFaces(newFaces);
		final HE_RAS<HE_Halfedge> newHalfedges = new HE_RAS<>();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (contains(he)) {
				newHalfedges.add(he);
			}
		}
		clearHalfedges();
		addHalfedges(newHalfedges);
		final HE_RAS<HE_Vertex> newVertices = new HE_RAS<>();
		final HE_VertexIterator vItr = sel.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (contains(v)) {
				newVertices.add(v);
			}
		}
		clearVertices();
		addVertices(newVertices);
	}

	public void grow() {
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			addFaces(fItr.next().getNeighborFaces());
		}
	}

	public void grow(final int n) {
		for (int i = 0; i < n; i++) {
			grow();
		}
	}

	public void shrink() {
		final List<HE_Halfedge> outerEdges = getOuterEdges();
		for (int i = 0; i < outerEdges.size(); i++) {
			final HE_Halfedge e = outerEdges.get(i);
			final HE_Face f1 = e.getFace();
			final HE_Face f2 = e.getPair().getFace();
			if (f1 == null || !contains(f1)) {
				remove(f2);
			}
			if (f2 == null || !contains(f2)) {
				remove(f1);
			}
		}
	}

	public void shrink(final int n) {
		for (int i = 0; i < n; i++) {
			shrink();
		}
	}

	public void surround() {
		final HE_FaceList currentFaces = new HE_FaceList();
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			currentFaces.add(f);
			addFaces(f.getNeighborFaces());
		}
		removeFaces(currentFaces);
	}

	public void surround(final int n) {
		grow(n - 1);
		surround();
	}

	public void smooth(final int threshold) {
		final HE_HalfedgeList currentHalfedges = new HE_HalfedgeList();
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			currentHalfedges.add(heItr.next());
		}
		for (int i = 0; i < currentHalfedges.size(); i++) {
			final HE_Face f = currentHalfedges.get(i).getPair().getFace();
			if (f != null && !contains(f)) {
				int ns = 0;
				HE_Halfedge he = f.getHalfedge();
				do {
					if (contains(he.getPair().getFace())) {
						ns++;
					}
					he = he.getNextInFace();
				} while (he != f.getHalfedge());
				if (ns >= threshold) {
					add(f);
				}
			}
		}
	}

	public void smooth(final double threshold) {
		final HE_HalfedgeList currentHalfedges = new HE_HalfedgeList();
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			currentHalfedges.add(heItr.next());
		}
		for (int i = 0; i < currentHalfedges.size(); i++) {
			final HE_Face f = currentHalfedges.get(i).getPair().getFace();
			if (f != null && !contains(f)) {
				int ns = 0;
				HE_Halfedge he = f.getHalfedge();
				do {
					if (contains(he.getPair().getFace())) {
						ns++;
					}
					he = he.getNextInFace();
				} while (he != f.getHalfedge());
				if (ns >= threshold * f.getFaceDegree()) {
					add(f);
				}
			}
		}
	}

	public HE_Selection invertSelection() {
		invertFaces();
		invertEdges();
		invertHalfedges();
		invertVertices();
		return this;
	}

	public HE_Selection invertFaces() {
		final HE_RAS<HE_Face> newFaces = new HE_RAS<>();
		final HE_FaceIterator fItr = parent.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (!contains(f)) {
				newFaces.add(f);
			}
		}
		clearFaces();
		addFaces(newFaces);
		return this;
	}

	public HE_Selection boundary() {
		final List<HE_Face> newFaces = this.getBoundaryFaces();
		clearFaces();
		addFaces(newFaces);
		return this;
	}

	public HE_Selection invertEdges() {
		final HE_RAS<HE_Halfedge> newEdges = new HE_RAS<>();
		final HE_EdgeIterator eItr = parent.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (!contains(e)) {
				newEdges.add(e);
			}
		}
		clearEdges();
		addHalfedges(newEdges);
		return this;
	}

	public HE_Selection invertVertices() {
		final HE_RAS<HE_Vertex> newVertices = new HE_RAS<>();
		final HE_VertexIterator vItr = parent.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (!contains(v)) {
				newVertices.add(v);
			}
		}
		clearVertices();
		addVertices(newVertices);
		return this;
	}

	public HE_Selection invertHalfedges() {
		final HE_RAS<HE_Halfedge> newHalfedges = new HE_RAS<>();
		final Iterator<HE_Halfedge> heItr = parent.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (!contains(he)) {
				newHalfedges.add(he);
			}
		}
		clearHalfedges();
		addHalfedges(newHalfedges);
		return this;
	}

	public HE_Selection cleanSelection() {
		final HE_RAS<HE_Face> newFaces = new HE_RAS<>();
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (parent.contains(f)) {
				newFaces.add(f);
			}
		}
		clearFaces();
		addFaces(newFaces);
		final HE_RAS<HE_Halfedge> newHalfedges = new HE_RAS<>();
		final Iterator<HE_Halfedge> heItr = heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (parent.contains(he)) {
				newHalfedges.add(he);
			}
		}
		clearHalfedges();
		addHalfedges(newHalfedges);
		final HE_RAS<HE_Vertex> newVertices = new HE_RAS<>();
		final HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (parent.contains(v)) {
				newVertices.add(v);
			}
		}
		clearVertices();
		addVertices(newVertices);
		return this;
	}

	public HE_Selection collectVertices() {
		List<HE_Vertex> tmpVertices = new HE_VertexList();
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			tmpVertices = f.getUniqueFaceVertices();
			addVertices(tmpVertices);
		}
		final Iterator<HE_Halfedge> heItr = heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			add(he.getVertex());
			add(he.getEndVertex());
		}
		return this;
	}

	public HE_Selection collectFaces() {
		final HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			addFaces(v.getFaceStar());
		}
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			add(heItr.next().getFace());
		}
		return this;
	}

	public HE_Selection collectEdgesByFace() {
		final HE_FaceIterator fitr = fItr();
		while (fitr.hasNext()) {
			final HE_FaceEdgeCirculator feCrc = fitr.next().feCrc();
			while (feCrc.hasNext()) {
				add(feCrc.next());
			}
		}
		return this;
	}

	public HE_Selection collectEdgesByVertex() {
		final HE_VertexIterator vitr = vItr();
		while (vitr.hasNext()) {
			addHalfedges(vitr.next().getEdgeStar());
		}
		return this;
	}

	public HE_Selection collectHalfedges() {
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			addHalfedges(f.getFaceHalfedgesTwoSided());
		}
		return this;
	}

	public void addEdge(final HE_Halfedge he) {
		if (he.isEdge()) {
			edges.add(he);
			halfedges.add(he.getPair());
		} else {
			halfedges.add(he);
			edges.add(he.getPair());
		}
	}

	public String createdBy() {
		return createdBy == null ? "" : createdBy;
	}

	@Override
	protected void clearPrecomputed() {
	}

	public HE_Mesh getParent() {
		return parent;
	}

	public static void main(final String[] args) {
		final HEC_Grid creator = new HEC_Grid();
		creator.setU(10);// number of cells in U direction
		creator.setV(10);// number of cells in V direction
		creator.setUSize(300);// size of grid in U direction
		creator.setVSize(500);// size of grid in V direction
		HE_Mesh mesh = new HE_Mesh(creator);
		mesh.stats();
		final HE_Selection sel = mesh.selectRandomFaces(0.4);
		mesh = sel.getAsMesh();
		mesh.stats();
	}

	@Override
	public void stats() {
		System.out.println("HE_Selection: " + getKey() + " (parent: " + parent.getKey());
		System.out.println("Number of vertices: " + this.getNumberOfVertices());
		System.out.println("Number of faces: " + this.getNumberOfFaces());
		System.out.println("Number of halfedges: " + this.getNumberOfHalfedges());
	}

	public static HE_Selection selectAll(final HE_Mesh mesh) {
		return mesh.selectAll();
	}

	public static HE_Selection selectAll(final HE_Mesh mesh, final String name) {
		return mesh.selectAll(name);
	}

	public static HE_Selection selectAllEdges(final HE_Mesh mesh) {
		return mesh.selectAllEdges();
	}

	public static HE_Selection selectAllEdges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllEdges(name);
	}

	public static HE_Selection selectAllFaces(final HE_Mesh mesh) {
		return mesh.selectAllFaces();
	}

	public static HE_Selection selectAllFaces(final HE_Mesh mesh, final String name) {
		return mesh.selectAllFaces(name);
	}

	public static HE_Selection selectAllHalfedges(final HE_Mesh mesh) {
		return mesh.selectAllHalfedges();
	}

	public static HE_Selection selectAllHalfedges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllHalfedges(name);
	}

	public static HE_Selection selectAllInnerBoundaryHalfedges(final HE_Mesh mesh) {
		return mesh.selectAllInnerBoundaryHalfedges();
	}

	public static HE_Selection selectAllInnerBoundaryHalfedges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllInnerBoundaryHalfedges(name);
	}

	public static HE_Selection selectAllOuterBoundaryHalfedges(final HE_Mesh mesh) {
		return mesh.selectAllOuterBoundaryHalfedges();
	}

	public static HE_Selection selectAllOuterBoundaryHalfedges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllOuterBoundaryHalfedges(name);
	}

	public static HE_Selection selectAllVertices(final HE_Mesh mesh) {
		return mesh.selectAllVertices();
	}

	public static HE_Selection selectAllVertices(final HE_Mesh mesh, final String name) {
		return mesh.selectAllVertices(name);
	}

	public static HE_Selection selectBackEdges(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectBackEdges(name, P);
	}

	public static HE_Selection selectBackEdges(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectBackEdges(P);
	}

	public static HE_Selection selectBackFaces(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectBackFaces(name, P);
	}

	public static HE_Selection selectBackFaces(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectBackFaces(P);
	}

	public static HE_Selection selectBackVertices(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectBackVertices(name, P);
	}

	public static HE_Selection selectBackVertices(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectBackVertices(P);
	}

	public static HE_Selection selectBoundaryEdges(final HE_Mesh mesh) {
		return mesh.selectBoundaryEdges();
	}

	public static HE_Selection selectBoundaryEdges(final HE_Mesh mesh, final String name) {
		return mesh.selectBoundaryEdges(name);
	}

	public static HE_Selection selectBoundaryFaces(final HE_Mesh mesh) {
		return mesh.selectBoundaryFaces();
	}

	public static HE_Selection selectBoundaryFaces(final HE_Mesh mesh, final String name) {
		return mesh.selectBoundaryFaces(name);
	}

	public static HE_Selection selectBoundaryVertices(final HE_Mesh mesh) {
		return mesh.selectBoundaryVertices();
	}

	public static HE_Selection selectBoundaryVertices(final HE_Mesh mesh, final String name) {
		return mesh.selectBoundaryVertices(name);
	}

	public static HE_Selection selectCrossingEdges(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectCrossingEdges(name, P);
	}

	public static HE_Selection selectCrossingEdges(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectCrossingEdges(P);
	}

	public static HE_Selection selectCrossingFaces(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectCrossingFaces(name, P);
	}

	public static HE_Selection selectCrossingFaces(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectCrossingFaces(P);
	}

	public static HE_Selection selectEdgesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithLabel(label);
	}

	public static HE_Selection selectEdgesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectEdgesWithLabel(name, label);
	}

	public static HE_Selection selectEdgesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithOtherInternalLabel(label);
	}

	public static HE_Selection selectEdgesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectEdgesWithOtherInternalLabel(name, label);
	}

	public static HE_Selection selectEdgesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithOtherLabel(label);
	}

	public static HE_Selection selectEdgesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectEdgesWithOtherLabel(name, label);
	}

	public static HE_Selection selectEdgesWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithInternalLabel(label);
	}

	public static HE_Selection selectEdgesWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectEdgesWithInternalLabel(name, label);
	}

	public static HE_Selection selectFacesWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithInternalLabel(label);
	}

	public static HE_Selection selectFacesWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectFacesWithInternalLabel(name, label);
	}

	public static HE_Selection selectFacesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithLabel(label);
	}

	public static HE_Selection selectFacesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectFacesWithLabel(name, label);
	}

	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final String name, final WB_Coord v) {
		return mesh.selectFacesWithNormal(name, v);
	}

	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final String name, final WB_Coord n,
			final double ta) {
		return mesh.selectFacesWithNormal(name, n, ta);
	}

	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final WB_Coord v) {
		return mesh.selectFacesWithNormal(v);
	}

	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final WB_Coord n, final double ta) {
		return mesh.selectFacesWithNormal(n, ta);
	}

	public static HE_Selection selectFacesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithOtherInternalLabel(label);
	}

	public static HE_Selection selectFacesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectFacesWithOtherInternalLabel(name, label);
	}

	public static HE_Selection selectFacesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithOtherLabel(label);
	}

	public static HE_Selection selectFacesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectFacesWithOtherLabel(name, label);
	}

	public static HE_Selection selectFrontEdges(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontEdges(name, P);
	}

	public static HE_Selection selectFrontEdges(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontEdges(P);
	}

	public static HE_Selection selectFrontFaces(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontFaces(name, P);
	}

	public static HE_Selection selectFrontFaces(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontFaces(P);
	}

	public static HE_Selection selectFrontVertices(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontVertices(name, P);
	}

	public static HE_Selection selectFrontVertices(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontVertices(P);
	}

	public static HE_Selection selectHalfedgesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithLabel(label);
	}

	public static HE_Selection selectHalfedgesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectHalfedgesWithLabel(name, label);
	}

	public static HE_Selection selectHalfedgesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithOtherInternalLabel(label);
	}

	public static HE_Selection selectHalfedgesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectHalfedgesWithOtherInternalLabel(name, label);
	}

	public static HE_Selection selectHalfedgesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithOtherLabel(label);
	}

	public static HE_Selection selectHalfedgesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectHalfedgesWithOtherLabel(name, label);
	}

	public static HE_Selection selectHalfedgeWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgeWithInternalLabel(label);
	}

	public static HE_Selection selectHalfedgeWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectHalfedgeWithInternalLabel(name, label);
	}

	public static HE_Selection selectOnVertices(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectOnVertices(name, P);
	}

	public static HE_Selection selectOnVertices(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectOnVertices(P);
	}

	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final double r) {
		return mesh.selectRandomEdges(r);
	}

	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomEdges(r, seed);
	}

	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomEdges(name, r);
	}

	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomEdges(name, r, seed);
	}

	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final double r) {
		return mesh.selectRandomFaces(r);
	}

	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomFaces(r, seed);
	}

	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomFaces(r);
	}

	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomFaces(name, r, seed);
	}

	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final double r) {
		return mesh.selectRandomVertices(r);
	}

	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomVertices(r, seed);
	}

	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomVertices(name, r);
	}

	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomVertices(name, r, seed);
	}

	public static HE_Selection selectVerticesWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithInternalLabel(label);
	}

	public static HE_Selection selectVerticesWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectVerticesWithInternalLabel(name, label);
	}

	public static HE_Selection selectVerticesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithLabel(label);
	}

	public static HE_Selection selectVerticesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectVerticesWithLabel(name, label);
	}

	public static HE_Selection selectVerticesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithOtherInternalLabel(label);
	}

	public static HE_Selection selectVerticesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectVerticesWithOtherInternalLabel(name, label);
	}

	public static HE_Selection selectVerticesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithOtherLabel(label);
	}

	public static HE_Selection selectVerticesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectVerticesWithOtherLabel(name, label);
	}
}
