package wblut.hemesh;

import java.util.Collection;

/**
 *
 */
public interface HE_HalfedgeStructure {
	/**
	 *
	 *
	 * @param el
	 */
	void add(HE_Element el);

	/**
	 *
	 *
	 * @param f
	 */
	void add(HE_Face f);

	/**
	 *
	 *
	 * @param he
	 */
	void add(HE_Halfedge he);

	/**
	 *
	 *
	 * @param mesh
	 */
	void add(HE_Mesh mesh);

	/**
	 *
	 *
	 * @param v
	 */
	void add(HE_Vertex v);

	/**
	 *
	 *
	 * @param faces
	 */
	void addFaces(Collection<? extends HE_Face> faces);

	/**
	 *
	 *
	 * @param faces
	 */
	void addFaces(HE_Face[] faces);

	/**
	 *
	 *
	 * @param source
	 */
	void addFaces(HE_HalfedgeStructure source);

	/**
	 *
	 *
	 * @param halfedges
	 */
	void addHalfedges(Collection<? extends HE_Halfedge> halfedges);

	/**
	 *
	 *
	 * @param halfedges
	 */
	void addHalfedges(HE_Halfedge[] halfedges);

	/**
	 *
	 *
	 * @param source
	 */
	void addHalfedges(HE_HalfedgeStructure source);

	/**
	 *
	 *
	 * @param vertices
	 */
	void addVertices(Collection<? extends HE_Vertex> vertices);

	/**
	 *
	 *
	 * @param source
	 */
	void addVertices(HE_HalfedgeStructure source);

	/**
	 *
	 *
	 * @param vertices
	 */
	void addVertices(HE_Vertex[] vertices);

	/**
	 *
	 */
	void clear();

	/**
	 *
	 */
	void clearEdges();

	/**
	 *
	 */
	void clearFaces();

	/**
	 *
	 */
	void clearHalfedges();

	/**
	 *
	 */
	void clearVertices();

	/**
	 *
	 *
	 * @param el
	 * @return
	 */
	boolean contains(HE_Element el);

	/**
	 *
	 *
	 * @return
	 */
	HE_EdgeIterator eItr();

	/**
	 *
	 *
	 * @return
	 */
	HE_FaceIterator fItr();

	/**
	 *
	 *
	 * @return
	 */
	HE_HalfedgeList getEdges();

	/**
	 *
	 *
	 * @return
	 */
	HE_Halfedge[] getEdgesAsArray();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	HE_Halfedge getEdgeWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	HE_FaceList getFaces();

	/**
	 *
	 *
	 * @return
	 */
	HE_Face[] getFacesAsArray();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	HE_Face getFaceWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	HE_HalfedgeList getHalfedges();

	/**
	 *
	 *
	 * @return
	 */
	HE_Halfedge[] getHalfedgesAsArray();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	HE_Halfedge getHalfedgeWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	String getName();

	/**
	 *
	 *
	 * @return
	 */
	int getNumberOfEdges();

	/**
	 *
	 *
	 * @return
	 */
	int getNumberOfFaces();

	/**
	 *
	 *
	 * @return
	 */
	int getNumberOfHalfedges();

	/**
	 *
	 *
	 * @return
	 */
	int getNumberOfVertices();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	HE_Vertex getVertexWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	HE_VertexList getVertices();

	/**
	 *
	 *
	 * @return
	 */
	HE_Vertex[] getVerticesAsArray();

	/**
	 *
	 *
	 * @return
	 */
	HE_HalfedgeIterator heItr();

	/**
	 *
	 *
	 * @param modifier
	 * @return
	 */
	HE_Mesh modify(HEM_Modifier modifier);

	/**
	 *
	 *
	 * @param f
	 */
	void remove(HE_Face f);

	/**
	 *
	 *
	 * @param he
	 */
	void remove(HE_Halfedge he);

	/**
	 *
	 *
	 * @param v
	 */
	void remove(HE_Vertex v);

	/**
	 *
	 *
	 * @param edges
	 */
	void removeEdges(Collection<? extends HE_Halfedge> edges);

	/**
	 *
	 *
	 * @param edges
	 */
	void removeEdges(HE_Halfedge[] edges);

	/**
	 *
	 *
	 * @param faces
	 */
	void removeFaces(Collection<? extends HE_Face> faces);

	/**
	 *
	 *
	 * @param faces
	 */
	void removeFaces(HE_Face[] faces);

	/**
	 *
	 *
	 * @param halfedges
	 */
	void removeHalfedges(Collection<? extends HE_Halfedge> halfedges);

	/**
	 *
	 *
	 * @param halfedges
	 */
	void removeHalfedges(HE_Halfedge[] halfedges);

	/**
	 *
	 *
	 * @param vertices
	 */
	void removeVertices(Collection<? extends HE_Vertex> vertices);

	/**
	 *
	 *
	 * @param vertices
	 */
	void removeVertices(HE_Vertex[] vertices);

	/**
	 *
	 *
	 * @param name
	 */
	void setName(String name);

	/**
	 *
	 *
	 * @param simplifier
	 * @return
	 */
	HE_Mesh simplify(HES_Simplifier simplifier);

	/**
	 *
	 *
	 * @param subdividor
	 * @return
	 */
	HE_Mesh subdivide(HES_Subdividor subdividor);

	/**
	 *
	 *
	 * @param subdividor
	 * @param rep
	 * @return
	 */
	HE_Mesh subdivide(HES_Subdividor subdividor, int rep);

	/**
	 *
	 *
	 * @return
	 */
	HE_VertexIterator vItr();

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	HE_Face getFaceWithKey(long key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	HE_Halfedge getHalfedgeWithKey(long key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	HE_Halfedge getEdgeWithKey(long key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	HE_Vertex getVertexWithKey(long key);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	boolean contains(HE_Face f);

	/**
	 *
	 *
	 * @param he
	 * @return
	 */
	boolean contains(HE_Halfedge he);

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	boolean contains(HE_Vertex v);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	boolean containsFace(long key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	boolean containsHalfedge(long key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	boolean containsEdge(long key);

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	boolean containsVertex(long key);

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	int getIndex(HE_Face f);

	/**
	 *
	 *
	 * @param edge
	 * @return
	 */
	int getIndex(HE_Halfedge edge);

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	int getIndex(HE_Vertex v);

	/**
	 *
	 */
	void stats();
}
