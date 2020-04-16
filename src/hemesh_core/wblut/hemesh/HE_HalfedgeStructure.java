package wblut.hemesh;

import java.util.Collection;
import java.util.List;

public interface HE_HalfedgeStructure {
	void add(HE_Element el);

	void add(HE_Face f);

	void add(HE_Halfedge he);

	void add(HE_Mesh mesh);

	void add(HE_Vertex v);

	void addFaces(Collection<? extends HE_Face> faces);

	void addFaces(HE_Face[] faces);

	void addFaces(HE_HalfedgeStructure source);

	void addHalfedges(Collection<? extends HE_Halfedge> halfedges);

	void addHalfedges(HE_Halfedge[] halfedges);

	void addHalfedges(HE_HalfedgeStructure source);

	void addVertices(Collection<? extends HE_Vertex> vertices);

	void addVertices(HE_HalfedgeStructure source);

	void addVertices(HE_Vertex[] vertices);

	void clear();

	void clearEdges();

	void clearFaces();

	void clearHalfedges();

	void clearVertices();

	boolean contains(HE_Element el);

	HE_EdgeIterator eItr();

	HE_FaceIterator fItr();

	List<HE_Halfedge> getEdges();

	HE_Halfedge[] getEdgesAsArray();

	HE_Halfedge getEdgeWithIndex(int i);

	List<HE_Face> getFaces();

	HE_Face[] getFacesAsArray();

	HE_Face getFaceWithIndex(int i);

	List<HE_Halfedge> getHalfedges();

	HE_Halfedge[] getHalfedgesAsArray();

	HE_Halfedge getHalfedgeWithIndex(int i);

	String getName();

	int getNumberOfEdges();

	int getNumberOfFaces();

	int getNumberOfHalfedges();

	int getNumberOfVertices();

	HE_Vertex getVertexWithIndex(int i);

	List<HE_Vertex> getVertices();

	HE_Vertex[] getVerticesAsArray();

	HE_HalfedgeIterator heItr();

	HE_Mesh modify(HEM_Modifier modifier);

	void remove(HE_Face f);

	void remove(HE_Halfedge he);

	void remove(HE_Vertex v);

	void removeEdges(Collection<? extends HE_Halfedge> edges);

	void removeEdges(HE_Halfedge[] edges);

	void removeFaces(Collection<? extends HE_Face> faces);

	void removeFaces(HE_Face[] faces);

	void removeHalfedges(Collection<? extends HE_Halfedge> halfedges);

	void removeHalfedges(HE_Halfedge[] halfedges);

	void removeVertices(Collection<? extends HE_Vertex> vertices);

	void removeVertices(HE_Vertex[] vertices);

	void setName(String name);

	HE_Mesh simplify(HES_Simplifier simplifier);

	HE_Mesh subdivide(HES_Subdividor subdividor);

	HE_Mesh subdivide(HES_Subdividor subdividor, int rep);

	HE_VertexIterator vItr();

	HE_Face getFaceWithKey(long key);

	HE_Halfedge getHalfedgeWithKey(long key);

	HE_Halfedge getEdgeWithKey(long key);

	HE_Vertex getVertexWithKey(long key);

	boolean contains(HE_Face f);

	boolean contains(HE_Halfedge he);

	boolean contains(HE_Vertex v);

	boolean containsFace(long key);

	boolean containsHalfedge(long key);

	boolean containsEdge(long key);

	boolean containsVertex(long key);

	int getIndex(HE_Face f);

	int getIndex(HE_Halfedge edge);

	int getIndex(HE_Vertex v);

	void stats();
}
