package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Plane;

/**
 *
 */
public class HE_Selection extends HE_MeshElement implements HE_HalfedgeStructure {
	/**  */
	private HE_Mesh parent;
	/**  */
	String createdBy;
	/**  */
	protected WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	/**  */
	private HE_RAS<HE_Vertex> vertices;
	/**  */
	private HE_RAS<HE_Halfedge> halfedges;
	/**  */
	private HE_RAS<HE_Halfedge> edges;
	/**  */
	private HE_RAS<HE_Face> faces;
	/**  */
	String name;

	/**
	 *
	 */
	private HE_Selection() {
		super();
		vertices = new HE_RAS<>();
		halfedges = new HE_RAS<>();
		edges = new HE_RAS<>();
		faces = new HE_RAS<>();
	}

	/**
	 *
	 *
	 * @param parent
	 */
	public HE_Selection(final HE_Mesh parent) {
		this();
		this.parent = parent;
	}

	/**
	 *
	 *
	 * @param parent
	 * @return
	 */
	static HE_Selection getSelection(final HE_Mesh parent) {
		return new HE_Selection(parent);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 *
	 *
	 * @param name
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final int getNumberOfFaces() {
		return faces.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final int getNumberOfHalfedges() {
		return halfedges.size() + edges.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int getNumberOfEdges() {
		return edges.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final int getNumberOfVertices() {
		return vertices.size();
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final HE_Face getFaceWithKey(final long key) {
		return faces.getWithKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final HE_Halfedge getHalfedgeWithKey(final long key) {
		HE_Halfedge he = edges.getWithKey(key);
		if (he != null) {
			return he;
		}
		he = halfedges.getWithKey(key);
		return he;
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final HE_Halfedge getEdgeWithKey(final long key) {
		HE_Halfedge he = edges.getWithKey(key);
		if (he != null) {
			return he;
		}
		he = halfedges.getWithKey(key);
		return he;
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final HE_Vertex getVertexWithKey(final long key) {
		return vertices.getWithKey(key);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	@Override
	public final HE_Face getFaceWithIndex(final int i) {
		if (i < 0 || i >= faces.size()) {
			throw new IndexOutOfBoundsException("Requested face index " + i + "not in range.");
		}
		return faces.getWithIndex(i);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
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

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	@Override
	public final HE_Halfedge getEdgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size()) {
			throw new IndexOutOfBoundsException("Requested edge index " + i + "not in range.");
		}
		return edges.getWithIndex(i);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	@Override
	public final HE_Vertex getVertexWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException("Requested vertex index " + i + "not in range.");
		}
		return vertices.getWithIndex(i);
	}

	/**
	 *
	 *
	 * @param el
	 */
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

	/**
	 *
	 *
	 * @param f
	 */
	@Override
	public final void add(final HE_Face f) {
		faces.add(f);
	}

	/**
	 *
	 *
	 * @param he
	 */
	@Override
	public void add(final HE_Halfedge he) {
		if (he.isEdge()) {
			edges.add(he);
		} else {
			halfedges.add(he);
		}
	}

	/**
	 *
	 *
	 * @param v
	 */
	@Override
	public final void add(final HE_Vertex v) {
		vertices.add(v);
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	@Override
	public void add(final HE_Mesh mesh) {
		addVertices(mesh.getVertices());
		addFaces(mesh.getFaces());
		addHalfedges(mesh.getHalfedges());
		addHalfedges(mesh.getEdges());
	}

	/**
	 *
	 *
	 * @param faces
	 */
	@Override
	public final void addFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			add(face);
		}
	}

	/**
	 *
	 *
	 * @param faces
	 */
	@Override
	public final void addFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			add(f);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	@Override
	public final void addFaces(final HE_HalfedgeStructure source) {
		faces.addAll(source.getFaces());
	}

	/**
	 *
	 *
	 * @param halfedges
	 */
	@Override
	public final void addHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			add(halfedge);
		}
	}

	/**
	 *
	 *
	 * @param halfedges
	 */
	@Override
	public final void addHalfedges(final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			add(he);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	@Override
	public final void addHalfedges(final HE_HalfedgeStructure source) {
		for (final HE_Halfedge he : source.getHalfedges()) {
			add(he);
		}
	}

	/**
	 *
	 *
	 * @param edges
	 */
	public final void addEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			add(edge);
		}
	}

	/**
	 *
	 *
	 * @param edges
	 */
	public final void addEdges(final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			add(e);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	public final void addEdges(final HE_HalfedgeStructure source) {
		edges.addAll(source.getEdges());
	}

	/**
	 *
	 *
	 * @param vertices
	 */
	@Override
	public final void addVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertex : vertices) {
			add(vertex);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	@Override
	public final void addVertices(final HE_HalfedgeStructure source) {
		vertices.addAll(source.getVertices());
	}

	/**
	 *
	 *
	 * @param vertices
	 */
	@Override
	public final void addVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			add(v);
		}
	}

	/**
	 *
	 *
	 * @param f
	 */
	@Override
	public void remove(final HE_Face f) {
		faces.remove(f);
	}

	/**
	 *
	 *
	 * @param he
	 */
	@Override
	public void remove(final HE_Halfedge he) {
		edges.remove(he);
		halfedges.remove(he);
	}

	/**
	 *
	 *
	 * @param v
	 */
	@Override
	public void remove(final HE_Vertex v) {
		vertices.remove(v);
	}

	/**
	 *
	 *
	 * @param faces
	 */
	@Override
	public final void removeFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			remove(face);
		}
	}

	/**
	 *
	 *
	 * @param faces
	 */
	@Override
	public final void removeFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			remove(f);
		}
	}

	/**
	 *
	 *
	 * @param halfedges
	 */
	@Override
	public final void removeHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			remove(halfedge);
		}
	}

	/**
	 *
	 *
	 * @param halfedges
	 */
	@Override
	public final void removeHalfedges(final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			remove(he);
		}
	}

	/**
	 *
	 *
	 * @param edges
	 */
	@Override
	public final void removeEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			remove(edge);
		}
	}

	/**
	 *
	 *
	 * @param edges
	 */
	@Override
	public final void removeEdges(final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			remove(e);
		}
	}

	/**
	 *
	 *
	 * @param vertices
	 */
	@Override
	public final void removeVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertice : vertices) {
			remove(vertice);
		}
	}

	/**
	 *
	 *
	 * @param vertices
	 */
	@Override
	public final void removeVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			remove(v);
		}
	}

	/**
	 *
	 */
	@Override
	public void clear() {
		clearVertices();
		clearHalfedges();
		clearFaces();
	}

	/**
	 *
	 */
	@Override
	public void clearFaces() {
		faces = new HE_RAS<>();
	}

	/**
	 *
	 */
	@Override
	public void clearHalfedges() {
		halfedges = new HE_RAS<>();
		edges = new HE_RAS<>();
	}

	/**
	 *
	 */
	@Override
	public final void clearEdges() {
		edges = new HE_RAS<>();
	}

	/**
	 *
	 */
	@Override
	public void clearVertices() {
		vertices = new HE_RAS<>();
	}

	/**
	 *
	 */
	void clearFacesNoSelectionCheck() {
		faces = new HE_RAS<>();
	}

	/**
	 *
	 */
	void clearVerticesNoSelectionCheck() {
		vertices = new HE_RAS<>();
	}

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
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

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public final boolean contains(final HE_Face f) {
		return faces.contains(f);
	}

	/**
	 *
	 *
	 * @param he
	 * @return
	 */
	@Override
	public final boolean contains(final HE_Halfedge he) {
		return edges.contains(he) || halfedges.contains(he);
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public final boolean contains(final HE_Vertex v) {
		return vertices.contains(v);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final HE_VertexList getVertices() {
		return new HE_VertexList(vertices.getObjects());
	}

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final HE_HalfedgeList getHalfedges() {
		final HE_HalfedgeList halfedges = new HE_HalfedgeList();
		halfedges.addAll(this.halfedges);
		halfedges.addAll(this.edges);
		return halfedges;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final HE_Halfedge[] getHalfedgesAsArray() {
		final HE_HalfedgeList hes = getHalfedges();
		final HE_Halfedge[] halfedges = new HE_Halfedge[hes.size()];
		int i = 0;
		for (final HE_Halfedge he : hes) {
			halfedges[i] = he;
			i++;
		}
		return halfedges;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final HE_HalfedgeList getEdges() {
		return new HE_HalfedgeList(edges.getObjects());
	}

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public final HE_FaceList getFaces() {
		return new HE_FaceList(faces.getObjects());
	}

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final boolean containsFace(final long key) {
		return faces.containsKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final boolean containsHalfedge(final long key) {
		return halfedges.containsKey(key) || edges.containsKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final boolean containsEdge(final long key) {
		return edges.containsKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public final boolean containsVertex(final long key) {
		return vertices.containsKey(key);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public final int getIndex(final HE_Face f) {
		return faces.indexOf(f);
	}

	/**
	 *
	 *
	 * @param edge
	 * @return
	 */
	@Override
	public final int getIndex(final HE_Halfedge edge) {
		return edges.indexOf(edge);
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public final int getIndex(final HE_Vertex v) {
		return vertices.indexOf(v);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_VertexIterator vItr() {
		final HE_VertexList vs = new HE_VertexList(vertices);
		return new HE_VertexIterator(vs);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_EdgeIterator eItr() {
		final HE_HalfedgeList es = new HE_HalfedgeList(edges);
		return new HE_EdgeIterator(es);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_HalfedgeIterator heItr() {
		final HE_HalfedgeList hes = new HE_HalfedgeList(getHalfedges());
		return HE_HalfedgeIterator.getIterator(hes);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_FaceIterator fItr() {
		final HE_FaceList fs = new HE_FaceList(getFaces());
		return new HE_FaceIterator(fs);
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getAllBoundaryHalfedges() {
		final HE_HalfedgeList boundaryHalfedges = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @param modifier
	 * @return
	 */
	@Override
	public HE_Mesh modify(final HEM_Modifier modifier) {
		modifier.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	/**
	 *
	 *
	 * @param subdividor
	 * @return
	 */
	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor) {
		subdividor.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	/**
	 *
	 *
	 * @param subdividor
	 * @param rep
	 * @return
	 */
	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor, final int rep) {
		for (int i = 0; i < rep; i++) {
			subdividor.apply(this);
			parent.clearPrecomputed();
		}
		return this.parent;
	}

	/**
	 *
	 *
	 * @param simplifier
	 * @return
	 */
	@Override
	public HE_Mesh simplify(final HES_Simplifier simplifier) {
		simplifier.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getOuterEdges() {
		final HE_Selection sel = get();
		sel.collectEdgesByFace();
		final HE_HalfedgeList result = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getInnerEdges() {
		final HE_Selection sel = get();
		sel.collectEdgesByFace();
		final HE_HalfedgeList result = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getOuterVertices() {
		final HE_VertexList result = new HE_VertexList();
		final HE_HalfedgeList outerEdges = getOuterEdges();
		for (final HE_Halfedge e : outerEdges) {
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getInnerVertices() {
		final HE_Selection sel = get();
		sel.collectVertices();
		final HE_VertexList result = new HE_VertexList();
		final HE_VertexList outerVertices = getOuterVertices();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_VertexList getAllBoundaryVertices() {
		final HE_VertexList result = new HE_VertexList();
		final HE_HalfedgeList outerEdges = getOuterEdges();
		for (final HE_Halfedge e : outerEdges) {
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_FaceList getBoundaryFaces() {
		final HE_FaceList boundaryFaces = new HE_FaceList();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getOuterHalfedges() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final HE_HalfedgeList result = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getOuterHalfedgesInside() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final HE_HalfedgeList result = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_HalfedgeList getInnerHalfedges() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final HE_HalfedgeList result = new HE_HalfedgeList();
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

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_Mesh getAsMesh() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	/**
	 *
	 */
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

	/**
	 *
	 *
	 * @param sel
	 */
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

	/**
	 *
	 *
	 * @param sel
	 */
	public void union(final HE_Selection sel) {
		add(sel);
	}

	/**
	 *
	 *
	 * @param sel
	 */
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

	/**
	 *
	 *
	 * @param sel
	 */
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

	/**
	 *
	 */
	public void grow() {
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			addFaces(fItr.next().getNeighborFaces());
		}
	}

	/**
	 *
	 *
	 * @param n
	 */
	public void grow(final int n) {
		for (int i = 0; i < n; i++) {
			grow();
		}
	}

	/**
	 *
	 */
	public void shrink() {
		final HE_HalfedgeList outerEdges = getOuterEdges();
		for (final HE_Halfedge e : outerEdges) {
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

	/**
	 *
	 *
	 * @param n
	 */
	public void shrink(final int n) {
		for (int i = 0; i < n; i++) {
			shrink();
		}
	}

	/**
	 *
	 */
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

	/**
	 *
	 *
	 * @param n
	 */
	public void surround(final int n) {
		grow(n - 1);
		surround();
	}

	/**
	 *
	 *
	 * @param threshold
	 */
	public void smooth(final int threshold) {
		final HE_HalfedgeList currentHalfedges = new HE_HalfedgeList();
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			currentHalfedges.add(heItr.next());
		}
		for (final HE_Halfedge currentHalfedge : currentHalfedges) {
			final HE_Face f = currentHalfedge.getPair().getFace();
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

	/**
	 *
	 *
	 * @param threshold
	 */
	public void smooth(final double threshold) {
		final HE_HalfedgeList currentHalfedges = new HE_HalfedgeList();
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			currentHalfedges.add(heItr.next());
		}
		for (final HE_Halfedge currentHalfedge : currentHalfedges) {
			final HE_Face f = currentHalfedge.getPair().getFace();
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection invertSelection() {
		invertFaces();
		invertEdges();
		invertHalfedges();
		invertVertices();
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection boundary() {
		final HE_FaceList newFaces = this.getBoundaryFaces();
		clearFaces();
		addFaces(newFaces);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection collectVertices() {
		HE_VertexList tmpVertices = new HE_VertexList();
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

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
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

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection collectEdgesByVertex() {
		final HE_VertexIterator vitr = vItr();
		while (vitr.hasNext()) {
			addHalfedges(vitr.next().getEdgeStar());
		}
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Selection collectHalfedges() {
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			addHalfedges(f.getFaceHalfedgesTwoSided());
		}
		return this;
	}

	/**
	 *
	 *
	 * @param he
	 */
	public void addEdge(final HE_Halfedge he) {
		if (he.isEdge()) {
			edges.add(he);
			halfedges.add(he.getPair());
		} else {
			halfedges.add(he);
			edges.add(he.getPair());
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public String createdBy() {
		return createdBy == null ? "" : createdBy;
	}

	/**
	 *
	 */
	@Override
	protected void clearPrecomputed() {
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_Mesh getParent() {
		return parent;
	}

	/**
	 *
	 *
	 * @param args
	 */
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

	/**
	 *
	 */
	@Override
	public void stats() {
		System.out.println("HE_Selection: " + getKey() + " (parent: " + parent.getKey());
		System.out.println("Number of vertices: " + this.getNumberOfVertices());
		System.out.println("Number of faces: " + this.getNumberOfFaces());
		System.out.println("Number of halfedges: " + this.getNumberOfHalfedges());
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAll(final HE_Mesh mesh) {
		return mesh.selectAll();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAll(final HE_Mesh mesh, final String name) {
		return mesh.selectAll(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAllEdges(final HE_Mesh mesh) {
		return mesh.selectAllEdges();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAllEdges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllEdges(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAllFaces(final HE_Mesh mesh) {
		return mesh.selectAllFaces();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAllFaces(final HE_Mesh mesh, final String name) {
		return mesh.selectAllFaces(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAllHalfedges(final HE_Mesh mesh) {
		return mesh.selectAllHalfedges();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAllHalfedges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllHalfedges(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAllInnerBoundaryHalfedges(final HE_Mesh mesh) {
		return mesh.selectAllInnerBoundaryHalfedges();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAllInnerBoundaryHalfedges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllInnerBoundaryHalfedges(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAllOuterBoundaryHalfedges(final HE_Mesh mesh) {
		return mesh.selectAllOuterBoundaryHalfedges();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAllOuterBoundaryHalfedges(final HE_Mesh mesh, final String name) {
		return mesh.selectAllOuterBoundaryHalfedges(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectAllVertices(final HE_Mesh mesh) {
		return mesh.selectAllVertices();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectAllVertices(final HE_Mesh mesh, final String name) {
		return mesh.selectAllVertices(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectBackEdges(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectBackEdges(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectBackEdges(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectBackEdges(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectBackFaces(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectBackFaces(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectBackFaces(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectBackFaces(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectBackVertices(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectBackVertices(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectBackVertices(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectBackVertices(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectBoundaryEdges(final HE_Mesh mesh) {
		return mesh.selectBoundaryEdges();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectBoundaryEdges(final HE_Mesh mesh, final String name) {
		return mesh.selectBoundaryEdges(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectBoundaryFaces(final HE_Mesh mesh) {
		return mesh.selectBoundaryFaces();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectBoundaryFaces(final HE_Mesh mesh, final String name) {
		return mesh.selectBoundaryFaces(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection selectBoundaryVertices(final HE_Mesh mesh) {
		return mesh.selectBoundaryVertices();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @return
	 */
	public static HE_Selection selectBoundaryVertices(final HE_Mesh mesh, final String name) {
		return mesh.selectBoundaryVertices(name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectCrossingEdges(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectCrossingEdges(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectCrossingEdges(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectCrossingEdges(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectCrossingFaces(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectCrossingFaces(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectCrossingFaces(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectCrossingFaces(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectEdgesWithLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithOtherInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectEdgesWithOtherInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithOtherLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectEdgesWithOtherLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectEdgesWithInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectEdgesWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectEdgesWithInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectFacesWithInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectFacesWithLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param v
	 * @return
	 */
	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final String name, final WB_Coord v) {
		return mesh.selectFacesWithNormal(name, v);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param n
	 * @param ta
	 * @return
	 */
	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final String name, final WB_Coord n,
			final double ta) {
		return mesh.selectFacesWithNormal(name, n, ta);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param v
	 * @return
	 */
	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final WB_Coord v) {
		return mesh.selectFacesWithNormal(v);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param n
	 * @param ta
	 * @return
	 */
	public static HE_Selection selectFacesWithNormal(final HE_Mesh mesh, final WB_Coord n, final double ta) {
		return mesh.selectFacesWithNormal(n, ta);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithOtherInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectFacesWithOtherInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithOtherLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectFacesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectFacesWithOtherLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectFrontEdges(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontEdges(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectFrontEdges(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontEdges(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectFrontFaces(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontFaces(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectFrontFaces(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontFaces(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectFrontVertices(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontVertices(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectFrontVertices(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontVertices(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectHalfedgesWithLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithOtherInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectHalfedgesWithOtherInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithOtherLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectHalfedgesWithOtherLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgeWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgeWithInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectHalfedgeWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectHalfedgeWithInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param P
	 * @return
	 */
	public static HE_Selection selectOnVertices(final HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectOnVertices(name, P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static HE_Selection selectOnVertices(final HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectOnVertices(P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param r
	 * @return
	 */
	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final double r) {
		return mesh.selectRandomEdges(r);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param r
	 * @param seed
	 * @return
	 */
	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomEdges(r, seed);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param r
	 * @return
	 */
	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomEdges(name, r);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param r
	 * @param seed
	 * @return
	 */
	public static HE_Selection selectRandomEdges(final HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomEdges(name, r, seed);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param r
	 * @return
	 */
	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final double r) {
		return mesh.selectRandomFaces(r);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param r
	 * @param seed
	 * @return
	 */
	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomFaces(r, seed);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param r
	 * @return
	 */
	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomFaces(r);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param r
	 * @param seed
	 * @return
	 */
	public static HE_Selection selectRandomFaces(final HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomFaces(name, r, seed);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param r
	 * @return
	 */
	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final double r) {
		return mesh.selectRandomVertices(r);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param r
	 * @param seed
	 * @return
	 */
	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomVertices(r, seed);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param r
	 * @return
	 */
	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomVertices(name, r);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param r
	 * @param seed
	 * @return
	 */
	public static HE_Selection selectRandomVertices(final HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomVertices(name, r, seed);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithInternalLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectVerticesWithInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectVerticesWithLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithOtherInternalLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithOtherInternalLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithOtherInternalLabel(final HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectVerticesWithOtherInternalLabel(name, label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithOtherLabel(final HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithOtherLabel(label);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param name
	 * @param label
	 * @return
	 */
	public static HE_Selection selectVerticesWithOtherLabel(final HE_Mesh mesh, final String name, final int label) {
		return mesh.selectVerticesWithOtherLabel(name, label);
	}
}
