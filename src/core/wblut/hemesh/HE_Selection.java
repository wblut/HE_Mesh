/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Plane;

/**
 * Collection of mesh elements. Contains methods to manipulate selections
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HE_Selection extends HE_MeshElement
		implements HE_HalfedgeStructure {
	/**
	 *
	 */
	private HE_Mesh					parent;
	String							createdBy;
	protected WB_GeometryFactory	gf	= new WB_GeometryFactory();
	private HE_RAS<HE_Vertex>		vertices;
	private HE_RAS<HE_Halfedge>		halfedges;
	private HE_RAS<HE_Halfedge>		edges;
	private HE_RAS<HE_Face>			faces;
	String							name;

	private HE_Selection() {
		super();
		vertices = new HE_RAS<HE_Vertex>();
		halfedges = new HE_RAS<HE_Halfedge>();
		edges = new HE_RAS<HE_Halfedge>();
		faces = new HE_RAS<HE_Face>();
	}

	/**
	 * Instantiates a new HE_Selection.
	 *
	 * @param parent
	 */
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

	/**
	 * Number of faces.
	 *
	 * @return the number of faces
	 */
	@Override
	public final int getNumberOfFaces() {
		return faces.size();
	}

	/**
	 * Number of halfedges.
	 *
	 * @return the number of halfedges
	 */
	@Override
	public final int getNumberOfHalfedges() {
		return halfedges.size() + edges.size();
	}

	/**
	 * Number of edges.
	 *
	 * @return the number of edges
	 */
	@Override
	public int getNumberOfEdges() {
		return edges.size();
	}

	/**
	 * Number of vertices.
	 *
	 * @return the number of vertices
	 */
	@Override
	public final int getNumberOfVertices() {
		return vertices.size();
	}

	/**
	 * Get face with key. The key of a mesh element is unique and never changes.
	 *
	 * @param key
	 *            face key
	 * @return face
	 */
	@Override
	public final HE_Face getFaceWithKey(final long key) {
		return faces.getWithKey(key);
	}

	/**
	 * Get halfedge with key. The key of a mesh element is unique and never
	 * changes.
	 *
	 * @param key
	 *            halfedge key
	 * @return halfedge
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
	 * Get edge with key. The key of a mesh element is unique and never changes.
	 *
	 * @param key
	 *            halfedge key
	 * @return halfedge
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
	 * Get vertex with key. The key of a mesh element is unique and never
	 * changes.
	 *
	 * @param key
	 *            vertex key
	 * @return vertex
	 */
	@Override
	public final HE_Vertex getVertexWithKey(final long key) {
		return vertices.getWithKey(key);
	}

	/**
	 * Get face with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            face index
	 * @return
	 */
	@Override
	public final HE_Face getFaceWithIndex(final int i) {
		if (i < 0 || i >= faces.size()) {
			throw new IndexOutOfBoundsException(
					"Requested face index " + i + "not in range.");
		}
		return faces.getWithIndex(i);
	}

	/**
	 * Get halfedge with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            halfedge index
	 * @return
	 */
	@Override
	public final HE_Halfedge getHalfedgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size() + halfedges.size()) {
			throw new IndexOutOfBoundsException(
					"Requested halfedge index " + i + "not in range.");
		}
		if (i >= edges.size()) {
			return halfedges.getWithIndex(i - edges.size());
		}
		return edges.getWithIndex(i);
	}

	/**
	 * Get edge with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            edge index
	 * @return
	 */
	@Override
	public final HE_Halfedge getEdgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size()) {
			throw new IndexOutOfBoundsException(
					"Requested edge index " + i + "not in range.");
		}
		return edges.getWithIndex(i);
	}

	/**
	 * Get vertex with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            vertex index
	 * @return
	 */
	@Override
	public final HE_Vertex getVertexWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException(
					"Requested vertex index " + i + "not in range.");
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

	/**
	 * Add face.
	 *
	 * @param f
	 *            face to add
	 */
	@Override
	public final void add(final HE_Face f) {
		faces.add(f);
	}

	/**
	 * Adds halfedge.
	 *
	 * @param he
	 *            halfedge to add
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
	 * Add vertex.
	 *
	 * @param v
	 *            vertex to add
	 */
	@Override
	public final void add(final HE_Vertex v) {
		vertices.add(v);
	}

	/**
	 * Add all mesh elements to this mesh. No copies are made.
	 *
	 * @param mesh
	 *            mesh to add
	 */
	@Override
	public void add(final HE_Mesh mesh) {
		addVertices(mesh.getVertices());
		addFaces(mesh.getFaces());
		addHalfedges(mesh.getHalfedges());
		addHalfedges(mesh.getEdges());
	}

	/**
	 * Adds faces.
	 *
	 * @param faces
	 *            faces to add as HE_Face[]
	 */
	@Override
	public final void addFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			add(face);
		}
	}

	/**
	 * Adds faces.
	 *
	 * @param faces
	 *            faces to add as Collection<? extends HE_Face>
	 */
	@Override
	public final void addFaces(final Collection<? extends HE_Face> faces) {
		for (HE_Face f : faces) {
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
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as HE_Halfedge[]
	 */
	@Override
	public final void addHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			add(halfedge);
		}
	}

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as Collection<? extends HE_Halfedge>
	 */
	@Override
	public final void addHalfedges(
			final Collection<? extends HE_Halfedge> halfedges) {
		for (HE_Halfedge he : halfedges) {
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
		for (HE_Halfedge he : source.getHalfedges()) {
			add(he);
		}
	}

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as HE_Halfedge[]
	 */
	public final void addEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			add(edge);
		}
	}

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as Collection<? extends HE_Halfedge>
	 */
	public final void addEdges(final Collection<? extends HE_Halfedge> edges) {
		for (HE_Halfedge e : edges) {
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
	 * Adds vertices.
	 *
	 * @param vertices
	 *            vertices to add as HE_Vertex[]
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
	 * Adds vertices.
	 *
	 * @param vertices
	 *            vertices to add as Collection<? extends HE_Vertex>
	 */
	@Override
	public final void addVertices(
			final Collection<? extends HE_Vertex> vertices) {
		for (HE_Vertex v : vertices) {
			add(v);
		}
	}

	/**
	 * Removes face.
	 *
	 * @param f
	 *            face to remove
	 */
	@Override
	public void remove(final HE_Face f) {
		faces.remove(f);
	}

	/**
	 * Removes halfedge.
	 *
	 * @param he
	 *            halfedge to remove
	 */
	@Override
	public void remove(final HE_Halfedge he) {
		edges.remove(he);
		halfedges.remove(he);
	}

	/**
	 * Removes vertex.
	 *
	 * @param v
	 *            vertex to remove
	 */
	@Override
	public void remove(final HE_Vertex v) {
		vertices.remove(v);
	}

	/**
	 * Removes faces.
	 *
	 * @param faces
	 *            faces to remove as HE_Face[]
	 */
	@Override
	public final void removeFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			remove(face);
		}
	}

	/**
	 * Removes faces.
	 *
	 * @param faces
	 *            faces to remove as Collection<? extends HE_Face>
	 */
	@Override
	public final void removeFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			remove(f);
		}
	}

	/**
	 * Removes halfedges.
	 *
	 * @param halfedges
	 *            halfedges to remove as HE_Halfedge[]
	 */
	@Override
	public final void removeHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			remove(halfedge);
		}
	}

	/**
	 * Removes halfedges.
	 *
	 * @param halfedges
	 *            halfedges to remove as Collection<? extends HE_Halfedge>
	 */
	@Override
	public final void removeHalfedges(
			final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			remove(he);
		}
	}

	/**
	 * Removes edges.
	 *
	 * @param edges
	 *            edges to remove as HE_Halfedge[]
	 */
	@Override
	public final void removeEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			remove(edge);
		}
	}

	/**
	 * Removes edges.
	 *
	 * @param edges
	 *            edges to remove as Collection<? extends HE_Halfedge>
	 */
	@Override
	public final void removeEdges(
			final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			remove(e);
		}
	}

	/**
	 * Removes vertices.
	 *
	 * @param vertices
	 *            vertices to remove as HE_Vertex[]
	 */
	@Override
	public final void removeVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertice : vertices) {
			remove(vertice);
		}
	}

	/**
	 * Removes vertices.
	 *
	 * @param vertices
	 *            vertices to remove as Collection<? extends HE_Vertex>
	 */
	@Override
	public final void removeVertices(
			final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			remove(v);
		}
	}

	/**
	 * Clear entire structure.
	 */
	@Override
	public void clear() {
		clearVertices();
		clearHalfedges();
		clearFaces();
	}

	/**
	 * Clear faces.
	 */
	@Override
	public void clearFaces() {
		faces = new HE_RAS<HE_Face>();
	}

	/**
	 * Clear halfedges.
	 */
	@Override
	public void clearHalfedges() {
		halfedges = new HE_RAS<HE_Halfedge>();
		edges = new HE_RAS<HE_Halfedge>();
	}

	/**
	 * Clear edges.
	 */
	@Override
	public final void clearEdges() {
		edges = new HE_RAS<HE_Halfedge>();
	}

	/**
	 * Clear vertices.
	 */
	@Override
	public void clearVertices() {
		vertices = new HE_RAS<HE_Vertex>();
	}

	/**
	 * Clear faces.
	 */
	void clearFacesNoSelectionCheck() {
		faces = new HE_RAS<HE_Face>();
	}

	/**
	 * Clear vertices.
	 */
	void clearVerticesNoSelectionCheck() {
		vertices = new HE_RAS<HE_Vertex>();
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

	/**
	 * Check if structure contains face.
	 *
	 * @param f
	 *            face
	 * @return true, if successful
	 */
	@Override
	public final boolean contains(final HE_Face f) {
		return faces.contains(f);
	}

	/**
	 * Check if structure contains halfedge.
	 *
	 * @param he
	 *            halfedge
	 * @return true, if successful
	 */
	@Override
	public final boolean contains(final HE_Halfedge he) {
		return edges.contains(he) || halfedges.contains(he);
	}

	/**
	 * Check if structure contains vertex.
	 *
	 * @param v
	 *            vertex
	 * @return true, if successful
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
	public final List<HE_Vertex> getVertices() {
		return new FastList<HE_Vertex>(vertices.getObjects());
	}

	/**
	 * Vertices as array.
	 *
	 * @return all vertices as HE_Vertex[]
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
	public final List<HE_Halfedge> getHalfedges() {
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		halfedges.addAll(this.halfedges);
		halfedges.addAll(this.edges);
		return halfedges;
	}

	/**
	 * Halfedges as array.
	 *
	 * @return all halfedges as HE_Halfedge[]
	 */
	@Override
	public final HE_Halfedge[] getHalfedgesAsArray() {
		List<HE_Halfedge> hes = getHalfedges();
		final HE_Halfedge[] halfedges = new HE_Halfedge[hes.size()];
		int i = 0;
		for (HE_Halfedge he : hes) {
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
	public final List<HE_Halfedge> getEdges() {
		return new FastList<HE_Halfedge>(edges.getObjects());
	}

	/**
	 * Edges as array.
	 *
	 * @return all edges as HE_Halfedge[]
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
	public final List<HE_Face> getFaces() {
		return new FastList<HE_Face>(faces.getObjects());
	}

	/**
	 * Faces as array.
	 *
	 * @return all faces as HE_Face[]
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
	 * Vertex iterator.
	 *
	 * @return vertex iterator
	 */
	@Override
	public HE_VertexIterator vItr() {
		List<HE_Vertex> vs = new FastList<HE_Vertex>(vertices);
		return new HE_VertexIterator(vs);
	}

	/**
	 * Edge iterator.
	 *
	 * @return edge iterator
	 */
	@Override
	public HE_EdgeIterator eItr() {
		List<HE_Halfedge> es = new FastList<HE_Halfedge>(edges);
		return new HE_EdgeIterator(es);
	}

	/**
	 * Halfedge iterator.
	 *
	 * @return halfedge iterator
	 */
	@Override
	public HE_HalfedgeIterator heItr() {
		List<HE_Halfedge> hes = new FastList<HE_Halfedge>(getHalfedges());
		return HE_HalfedgeIterator.getIterator(hes);
	}

	/**
	 * Face iterator.
	 *
	 * @return face iterator
	 */
	@Override
	public HE_FaceIterator fItr() {
		List<HE_Face> fs = new FastList<HE_Face>(getFaces());
		return new HE_FaceIterator(fs);
	}

	/**
	 * Collect all boundary halfedges.
	 *
	 * @return boundary halfedges
	 */
	public List<HE_Halfedge> getAllBoundaryHalfedges() {
		final List<HE_Halfedge> boundaryHalfedges = new FastList<HE_Halfedge>();
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
	 * Modify the mesh.
	 *
	 * @param modifier
	 *            HE_Modifier to apply
	 * @return self
	 */
	@Override
	public HE_Mesh modify(final HEM_Modifier modifier) {
		modifier.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	/**
	 * Subdivide the mesh.
	 *
	 * @param subdividor
	 *            HE_Subdividor to apply
	 * @return self
	 */
	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor) {
		subdividor.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	/**
	 * Subdivide the mesh a number of times.
	 *
	 * @param subdividor
	 *            HE_Subdividor to apply
	 * @param rep
	 *            subdivision iterations. WARNING: higher values will lead to
	 *            unmanageable number of faces.
	 * @return self
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
	 * Simplify.
	 *
	 * @param simplifier
	 *            the simplifier
	 * @return the h e_ mesh
	 */
	@Override
	public HE_Mesh simplify(final HES_Simplifier simplifier) {
		simplifier.apply(this);
		parent.clearPrecomputed();
		return this.parent;
	}

	/**
	 * Get outer edges.
	 *
	 * @return outer edges as FastList<HE_Edge>
	 */
	public List<HE_Halfedge> getOuterEdges() {
		final HE_Selection sel = get();
		sel.collectEdgesByFace();
		final List<HE_Halfedge> result = new FastList<HE_Halfedge>();
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.isEdge()) {
				final HE_Face f1 = he.getFace();
				final HE_Face f2 = he.getPair().getFace();
				if (f1 == null || f2 == null || !contains(f1)
						|| !contains(f2)) {
					result.add(he);
				}
			}
		}
		return result;
	}

	/**
	 * Get inner edges.
	 *
	 * @return inner edges as FastList<HE_Edge>
	 */
	public List<HE_Halfedge> getInnerEdges() {
		final HE_Selection sel = get();
		sel.collectEdgesByFace();
		final List<HE_Halfedge> result = new FastList<HE_Halfedge>();
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.isEdge()) {
				final HE_Face f1 = he.getFace();
				final HE_Face f2 = he.getPair().getFace();
				if (!(f1 == null || f2 == null || !contains(f1)
						|| !contains(f2))) {
					result.add(he);
				}
			}
		}
		return result;
	}

	/**
	 * Get outer vertices.
	 *
	 * @return outer vertices as FastList<HE_Vertex>
	 */
	public List<HE_Vertex> getOuterVertices() {
		final List<HE_Vertex> result = new FastList<HE_Vertex>();
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

	/**
	 * Get inner vertices.
	 *
	 * @return inner vertices as FastList<HE_Vertex>
	 */
	public List<HE_Vertex> getInnerVertices() {
		final HE_Selection sel = get();
		sel.collectVertices();
		final List<HE_Vertex> result = new FastList<HE_Vertex>();
		final List<HE_Vertex> outerVertices = getOuterVertices();
		HE_VertexIterator vItr = sel.vItr();
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
	 * Get vertices in selection on mesh boundary.
	 *
	 * @return boundary vertices in selection as FastList<HE_Vertex>
	 */
	public List<HE_Vertex> getAllBoundaryVertices() {
		final List<HE_Vertex> result = new FastList<HE_Vertex>();
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
		List<HE_Face> boundaryFaces = new FastList<HE_Face>();
		HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			HE_FaceFaceCirculator ffCrc = f.ffCrc();
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
	 * Get outer halfedges.
	 *
	 * @return outside halfedges of outer edges as FastList<HE_halfedge>
	 */
	public List<HE_Halfedge> getOuterHalfedges() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final List<HE_Halfedge> result = new FastList<HE_Halfedge>();
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
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
	 * Get outer halfedges.
	 *
	 * @return inside halfedges of outer edges as FastList<HE_halfedge>
	 */
	public List<HE_Halfedge> getOuterHalfedgesInside() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final List<HE_Halfedge> result = new FastList<HE_Halfedge>();
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
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
	 * Get innerhalfedges.
	 *
	 * @return inner halfedges as FastList<HE_halfedge>
	 */
	public List<HE_Halfedge> getInnerHalfedges() {
		final HE_Selection sel = get();
		sel.collectHalfedges();
		final List<HE_Halfedge> result = new FastList<HE_Halfedge>();
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (contains(he.getPair().getFace()) && contains(he.getFace())) {
				result.add(he);
			}
		}
		return result;
	}

	/**
	 * Copy selection.
	 *
	 * @return copy of selection
	 */
	public HE_Selection get() {
		final HE_Selection copy = new HE_Selection(parent);
		HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			copy.add(f);
		}
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			copy.add(he);
		}
		HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			copy.add(v);
		}
		copy.createdBy = createdBy == null ? null : createdBy;
		return copy;
	}

	/**
	 * Creates a submesh from the faces in the selection. The original mesh is
	 * not modified. It is not necessary to use {@link #completeFromFaces()
	 * completeFromFaces} before using this operation.
	 *
	 * @return
	 */
	public HE_Mesh getAsMesh() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	/**
	 * Add all halfedges and vertices belonging to the faces of the selection,
	 * except the outer boundary halfedges that belong to other faces. This
	 * clears all vertices and halfedges that might have been part of the
	 * selection. It also makes sure that vertices only refer to halfedges
	 * inside the selection. After this operation is done, the selection is in
	 * essence an almost self-consistent submesh. Boundary halfedges might refer
	 * to faces not present in the submesh.
	 */
	void completeFromFaces() {
		this.clearHalfedges();
		this.clearVertices();
		HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		HE_Halfedge he;
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceVertexCirculator fvcrc = new HE_FaceVertexCirculator(
					f);
			while (fvcrc.hasNext()) {
				add(fvcrc.next());
			}
			final HE_FaceHalfedgeInnerCirculator fheicrc = new HE_FaceHalfedgeInnerCirculator(
					f);
			while (fheicrc.hasNext()) {
				he = fheicrc.next();
				add(he);
			}
		}
		fitr = this.fItr();
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceHalfedgeInnerCirculator fheicrc = new HE_FaceHalfedgeInnerCirculator(
					f);
			while (fheicrc.hasNext()) {
				he = fheicrc.next();
				if (!contains(he.getVertex().getHalfedge())) {
					parent.setHalfedge(he.getVertex(), he);
				}
			}
		}
	}

	/**
	 * Add selection.
	 *
	 * @param sel
	 *            selection to add
	 */
	public void add(final HE_Selection sel) {
		HE_FaceIterator fItr = sel.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			add(f);
		}
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			add(he);
		}
		HE_VertexIterator vItr = sel.vItr();
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
	 * Remove selection.
	 *
	 * @param sel
	 *            selection to remove
	 */
	public void subtract(final HE_Selection sel) {
		HE_FaceIterator fItr = sel.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			remove(f);
		}
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			remove(he);
		}
		HE_VertexIterator vItr = sel.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			remove(v);
		}
	}

	/**
	 * Remove elements outside selection.
	 *
	 * @param sel
	 *            selection to check
	 */
	public void intersect(final HE_Selection sel) {
		final HE_RAS<HE_Face> newFaces = new HE_RAS<HE_Face>();
		HE_FaceIterator fItr = sel.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (contains(f)) {
				newFaces.add(f);
			}
		}
		clearFaces();
		addFaces(newFaces);
		final HE_RAS<HE_Halfedge> newHalfedges = new HE_RAS<HE_Halfedge>();
		HE_Halfedge he;
		Iterator<HE_Halfedge> heItr = sel.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (contains(he)) {
				newHalfedges.add(he);
			}
		}
		clearHalfedges();
		addHalfedges(newHalfedges);
		final HE_RAS<HE_Vertex> newVertices = new HE_RAS<HE_Vertex>();
		HE_VertexIterator vItr = sel.vItr();
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
	 * Grow face selection outwards by one face.
	 */
	public void grow() {
		HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			addFaces(fItr.next().getNeighborFaces());
		}
	}

	/**
	 * Grow face selection outwards.
	 *
	 * @param n
	 *            number of faces to grow
	 */
	public void grow(final int n) {
		for (int i = 0; i < n; i++) {
			grow();
		}
	}

	/**
	 * Grow face selection inwards by one face.
	 */
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

	/**
	 * Shrink face selection inwards.
	 *
	 * @param n
	 *            number of faces to shrink
	 */
	public void shrink(final int n) {
		for (int i = 0; i < n; i++) {
			shrink();
		}
	}

	/**
	 * Select faces surrounding current face selection.
	 */
	public void surround() {
		final FastList<HE_Face> currentFaces = new FastList<HE_Face>();
		HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			currentFaces.add(f);
			addFaces(f.getNeighborFaces());
		}
		removeFaces(currentFaces);
	}

	/**
	 * Select faces surrounding current face selection at a distance of n-1
	 * faces.
	 *
	 * @param n
	 *            distance to current selection
	 */
	public void surround(final int n) {
		grow(n - 1);
		surround();
	}

	/**
	 * Add faces with certain number of edges in selection to selection.
	 *
	 * @param threshold
	 *            number of edges that have to belong to the selection before a
	 *            face is added
	 */
	public void smooth(final int threshold) {
		final FastList<HE_Halfedge> currentHalfedges = new FastList<HE_Halfedge>();
		Iterator<HE_Halfedge> heItr = heItr();
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

	/**
	 * Add faces with certain proportion of edges in selection to selection.
	 *
	 * @param threshold
	 *            number of edges that have to belong to the selection before a
	 *            face is added
	 */
	public void smooth(final double threshold) {
		final FastList<HE_Halfedge> currentHalfedges = new FastList<HE_Halfedge>();
		Iterator<HE_Halfedge> heItr = heItr();
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

	/**
	 * Invert current selection.
	 *
	 * @return inverted selection
	 */
	public HE_Selection invertSelection() {
		invertFaces();
		invertEdges();
		invertHalfedges();
		invertVertices();
		return this;
	}

	/**
	 * Invert current face selection.
	 *
	 * @return inverted face selection
	 */
	public HE_Selection invertFaces() {
		final HE_RAS<HE_Face> newFaces = new HE_RAS<HE_Face>();
		HE_FaceIterator fItr = parent.fItr();
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
		List<HE_Face> newFaces = this.getBoundaryFaces();
		clearFaces();
		addFaces(newFaces);
		return this;
	}

	/**
	 * Invert current edge election.
	 *
	 * @return inverted edge selection
	 */
	public HE_Selection invertEdges() {
		final HE_RAS<HE_Halfedge> newEdges = new HE_RAS<HE_Halfedge>();
		HE_EdgeIterator eItr = parent.eItr();
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
	 * Invert current vertex selection.
	 *
	 * @return inverted vertex selection
	 */
	public HE_Selection invertVertices() {
		final HE_RAS<HE_Vertex> newVertices = new HE_RAS<HE_Vertex>();
		HE_VertexIterator vItr = parent.vItr();
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
	 * Invert current halfedge selection.
	 *
	 * @return inverted halfedge selection
	 */
	public HE_Selection invertHalfedges() {
		final HE_RAS<HE_Halfedge> newHalfedges = new HE_RAS<HE_Halfedge>();
		Iterator<HE_Halfedge> heItr = parent.heItr();
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
	 * Clean current selection, removes all elements no longer part of mesh.
	 *
	 * @return current selection
	 */
	public HE_Selection cleanSelection() {
		final HE_RAS<HE_Face> newFaces = new HE_RAS<HE_Face>();
		HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (parent.contains(f)) {
				newFaces.add(f);
			}
		}
		clearFaces();
		addFaces(newFaces);
		final HE_RAS<HE_Halfedge> newHalfedges = new HE_RAS<HE_Halfedge>();
		Iterator<HE_Halfedge> heItr = heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (parent.contains(he)) {
				newHalfedges.add(he);
			}
		}
		clearHalfedges();
		addHalfedges(newHalfedges);
		final HE_RAS<HE_Vertex> newVertices = new HE_RAS<HE_Vertex>();
		HE_VertexIterator vItr = vItr();
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
	 * Collect vertices belonging to selection elements.
	 */
	public HE_Selection collectVertices() {
		List<HE_Vertex> tmpVertices = new FastList<HE_Vertex>();
		HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			tmpVertices = f.getUniqueFaceVertices();
			addVertices(tmpVertices);
		}
		Iterator<HE_Halfedge> heItr = heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			add(he.getVertex());
			add(he.getEndVertex());
		}
		return this;
	}

	/**
	 * Collect faces belonging to selection elements.
	 */
	public HE_Selection collectFaces() {
		HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			addFaces(v.getFaceStar());
		}
		Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			add(heItr.next().getFace());
		}
		return this;
	}

	/**
	 * Collect edges belonging to face selection.
	 */
	public HE_Selection collectEdgesByFace() {
		final HE_FaceIterator fitr = fItr();
		while (fitr.hasNext()) {
			HE_FaceEdgeCirculator feCrc = fitr.next().feCrc();
			while (feCrc.hasNext()) {
				add(feCrc.next());
			}
		}
		return this;
	}

	/**
	 *
	 */
	public HE_Selection collectEdgesByVertex() {
		final HE_VertexIterator vitr = vItr();
		while (vitr.hasNext()) {
			addHalfedges(vitr.next().getEdgeStar());
		}
		return this;
	}

	/**
	 * Collect halfedges belonging to face selection.
	 */
	public HE_Selection collectHalfedges() {
		HE_FaceIterator fItr = fItr();
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

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Element#clearPrecomputed()
	 */
	@Override
	protected void clearPrecomputed() {
		// TODO Auto-generated method stub
	}

	public HE_Mesh getParent() {
		return parent;
	}

	public static void main(String[] args) {
		HEC_Grid creator = new HEC_Grid();
		creator.setU(10);// number of cells in U direction
		creator.setV(10);// number of cells in V direction
		creator.setUSize(300);// size of grid in U direction
		creator.setVSize(500);// size of grid in V direction
		HE_Mesh mesh = new HE_Mesh(creator);
		mesh.stats();
		HE_Selection sel = mesh.selectRandomFaces(0.4);
		mesh = sel.getAsMesh();
		mesh.stats();
	}

	@Override
	public void stats() {
		System.out.println(
				"HE_Selection: " + getKey() + " (parent: " + parent.getKey());
		System.out.println("Number of vertices: " + this.getNumberOfVertices());
		System.out.println("Number of faces: " + this.getNumberOfFaces());
		System.out
				.println("Number of halfedges: " + this.getNumberOfHalfedges());
	}

	/**
	 * Select all mesh elements. Unnamed selections are not stored in the mesh
	 * and are not updated.
	 *
	 * @return current selection
	 */
	public static HE_Selection selectAll(HE_Mesh mesh) {
		return mesh.selectAll();
	}

	/**
	 * Select all mesh elements.
	 *
	 * @return current selection
	 */
	public static HE_Selection selectAll(HE_Mesh mesh,final String name) {
		return mesh.selectAll(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllEdges(HE_Mesh mesh) {
		return mesh.selectAllEdges();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllEdges(HE_Mesh mesh,final String name) {
		return mesh.selectAllEdges(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllFaces(HE_Mesh mesh) {
		return mesh.selectAllFaces();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllFaces(HE_Mesh mesh,final String name) {
		return mesh.selectAllFaces(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllHalfedges(HE_Mesh mesh) {
		return mesh.selectAllHalfedges();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllHalfedges(HE_Mesh mesh,final String name) {
		return mesh.selectAllHalfedges(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllInnerBoundaryHalfedges(HE_Mesh mesh) {
		return mesh.selectAllInnerBoundaryHalfedges();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllInnerBoundaryHalfedges(HE_Mesh mesh,final String name) {
		return mesh.selectAllInnerBoundaryHalfedges(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllOuterBoundaryHalfedges(HE_Mesh mesh) {
		return mesh.selectAllOuterBoundaryHalfedges();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllOuterBoundaryHalfedges(HE_Mesh mesh,final String name) {
		return mesh.selectAllOuterBoundaryHalfedges(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllVertices(HE_Mesh mesh) {
		return mesh.selectAllVertices();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectAllVertices(HE_Mesh mesh,final String name) {
		return mesh.selectAllVertices(name);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectBackEdges(HE_Mesh mesh,final String name, final WB_Plane P) {
		return mesh.selectBackEdges(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectBackEdges(HE_Mesh mesh,final WB_Plane P) {
		return mesh.selectBackEdges(P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectBackFaces(HE_Mesh mesh,final String name, final WB_Plane P) {
		return mesh.selectBackFaces(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectBackFaces(HE_Mesh mesh,final WB_Plane P) {
		return mesh.selectBackFaces(P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectBackVertices(HE_Mesh mesh,final String name,
			final WB_Plane P) {
		return mesh.selectBackVertices(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectBackVertices(HE_Mesh mesh,final WB_Plane P) {
		return mesh.selectBackVertices(P);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectBoundaryEdges(HE_Mesh mesh) {
		return mesh.selectBoundaryEdges();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectBoundaryEdges(HE_Mesh mesh,final String name) {
		return mesh.selectBoundaryEdges(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectBoundaryFaces(HE_Mesh mesh) {
		return mesh.selectBoundaryFaces();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectBoundaryFaces(HE_Mesh mesh,final String name) {
		return mesh.selectBoundaryFaces(name);
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectBoundaryVertices(HE_Mesh mesh) {
		return mesh.selectBoundaryVertices();
	}

	/**
	 *
	 * @param name
	 */
	public static HE_Selection selectBoundaryVertices(HE_Mesh mesh,final String name) {
		return mesh.selectBoundaryVertices(name);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectCrossingEdges(HE_Mesh mesh,final String name,
			final WB_Plane P) {
		return mesh.selectCrossingEdges(name, P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectCrossingEdges(HE_Mesh mesh,final WB_Plane P) {
		return mesh.selectCrossingEdges(P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectCrossingFaces(HE_Mesh mesh,final String name,
			final WB_Plane P) {
		return mesh.selectCrossingFaces(name, P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectCrossingFaces(HE_Mesh mesh,final WB_Plane P) {
		return mesh.selectCrossingFaces(P);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithLabel(HE_Mesh mesh,final int label) {
		return mesh.selectEdgesWithLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithLabel(HE_Mesh mesh,final String name,
			final int label) {
		return mesh.selectEdgesWithLabel(name, label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithOtherInternalLabel(HE_Mesh mesh,final int label) {
		return mesh.selectEdgesWithOtherInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithOtherInternalLabel(HE_Mesh mesh,final String name,
			final int label) {
		return mesh.selectEdgesWithOtherInternalLabel(name, label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithOtherLabel(HE_Mesh mesh,final int label) {
		return mesh.selectEdgesWithOtherLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithOtherLabel(HE_Mesh mesh,final String name,
			final int label) {
		return mesh.selectEdgesWithOtherLabel(name, label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithInternalLabel(HE_Mesh mesh,final int label) {
		return mesh.selectEdgesWithInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectEdgesWithInternalLabel(HE_Mesh mesh,final String name,
			final int label) {
		return mesh.selectEdgesWithInternalLabel(name, label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithInternalLabel(HE_Mesh mesh,final int label) {
		return mesh.selectFacesWithInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithInternalLabel(HE_Mesh mesh,final String name,
			final int label) {
		return mesh.selectFacesWithInternalLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithLabel(HE_Mesh mesh,final int label) {
		return mesh.selectFacesWithLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectFacesWithLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param v
	 */
	public static HE_Selection selectFacesWithNormal(HE_Mesh mesh, final String name,
			final WB_Coord v) {
		return mesh.selectFacesWithNormal(name,v);
	}

	/**
	 *
	 * @param name
	 * @param n
	 * @param ta
	 */
	public static HE_Selection selectFacesWithNormal(HE_Mesh mesh, final String name,
			final WB_Coord n, final double ta) {
		return mesh.selectFacesWithNormal(name, n, ta);
	}

	/**
	 *
	 * @param name
	 * @param v
	 */
	public static HE_Selection selectFacesWithNormal(HE_Mesh mesh, final WB_Coord v) {
		return mesh.selectFacesWithNormal(v);
	}

	/**
	 *
	 * @param name
	 * @param n
	 * @param ta
	 */
	public static HE_Selection selectFacesWithNormal(HE_Mesh mesh, final WB_Coord n,
			final double ta) {
		return mesh.selectFacesWithNormal(n,ta);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithOtherInternalLabel(HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithOtherInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithOtherInternalLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectFacesWithOtherInternalLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithOtherLabel(HE_Mesh mesh, final int label) {
		return mesh.selectFacesWithOtherLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectFacesWithOtherLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectFacesWithOtherLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectFrontEdges(HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontEdges(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectFrontEdges(HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontEdges(P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectFrontFaces(HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectFrontFaces(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectFrontFaces(HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontFaces(P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectFrontVertices(HE_Mesh mesh, final String name,
			final WB_Plane P) {
		return mesh.selectFrontVertices(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectFrontVertices(HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectFrontVertices(P);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgesWithLabel(HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgesWithLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectHalfedgesWithLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgesWithOtherInternalLabel(HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithOtherInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgesWithOtherInternalLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectHalfedgesWithOtherInternalLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgesWithOtherLabel(HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgesWithOtherLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgesWithOtherLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectHalfedgesWithOtherLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgeWithInternalLabel(HE_Mesh mesh, final int label) {
		return mesh.selectHalfedgeWithInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectHalfedgeWithInternalLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectHalfedgeWithInternalLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectOnVertices(HE_Mesh mesh, final String name, final WB_Plane P) {
		return mesh.selectOnVertices(name,P);
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public static HE_Selection selectOnVertices(HE_Mesh mesh, final WB_Plane P) {
		return mesh.selectOnVertices(P);
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public static HE_Selection selectRandomEdges(HE_Mesh mesh, final double r) {
		return mesh.selectRandomEdges(r);
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public static HE_Selection selectRandomEdges(HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomEdges(r,seed);
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public static HE_Selection selectRandomEdges(HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomEdges(name,r);
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public static HE_Selection selectRandomEdges(HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomEdges(name,r,seed);
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public static HE_Selection selectRandomFaces(HE_Mesh mesh, final double r) {
		return mesh.selectRandomFaces(r);
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public static HE_Selection selectRandomFaces(HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomFaces(r,seed);
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public static HE_Selection selectRandomFaces(HE_Mesh mesh, final String name, final double r) {
		return mesh.selectRandomFaces(r);
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public static HE_Selection selectRandomFaces(HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomFaces(name,r,seed);
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public static HE_Selection selectRandomVertices(HE_Mesh mesh, final double r) {
		return mesh.selectRandomVertices(r);
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public static HE_Selection selectRandomVertices(HE_Mesh mesh, final double r, final long seed) {
		return mesh.selectRandomVertices(r,seed);
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public static HE_Selection selectRandomVertices(HE_Mesh mesh, final String name,
			final double r) {
		return mesh.selectRandomVertices(name,r);
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public static HE_Selection selectRandomVertices(HE_Mesh mesh, final String name, final double r,
			final long seed) {
		return mesh.selectRandomVertices(name,r,seed);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithInternalLabel(HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithInternalLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectVerticesWithInternalLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithLabel(HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectVerticesWithLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithOtherInternalLabel(HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithOtherInternalLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithOtherInternalLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectVerticesWithOtherInternalLabel(name,label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithOtherLabel(HE_Mesh mesh, final int label) {
		return mesh.selectVerticesWithOtherLabel(label);
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public static HE_Selection selectVerticesWithOtherLabel(HE_Mesh mesh, final String name,
			final int label) {
		return mesh.selectVerticesWithOtherLabel(name,label);
	}
}
