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

/**
*
*/
/**
 * Collection of mesh elements. Contains methods to manipulate the data
 * structures.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public interface HE_HalfedgeStructure {
	void add(HE_Element el);

	/**
	 * Add face.
	 *
	 * @param f
	 *            face to add
	 */
	void add(HE_Face f);

	/**
	 * Adds halfedge.
	 *
	 * @param he
	 *            halfedge to add
	 */
	void add(HE_Halfedge he);

	/**
	 * Add all mesh elements to this mesh. No copies are made.
	 *
	 * @param mesh
	 *            mesh to add
	 */
	void add(HE_Mesh mesh);

	/**
	 * Add vertex.
	 *
	 * @param v
	 *            vertex to add
	 */
	void add(HE_Vertex v);

	/**
	 * Adds faces.
	 *
	 * @param faces
	 *            faces to add as Collection<? extends HE_Face>
	 */
	void addFaces(Collection<? extends HE_Face> faces);

	/**
	 * Adds faces.
	 *
	 * @param faces
	 *            faces to add as HE_Face[]
	 */
	void addFaces(HE_Face[] faces);

	/**
	 *
	 *
	 * @param source
	 */
	void addFaces(HE_HalfedgeStructure source);

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as Collection<? extends HE_Halfedge>
	 */
	void addHalfedges(Collection<? extends HE_Halfedge> halfedges);

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as HE_Halfedge[]
	 */
	void addHalfedges(HE_Halfedge[] halfedges);

	/**
	 *
	 *
	 * @param source
	 */
	void addHalfedges(HE_HalfedgeStructure source);

	/**
	 * Adds vertices.
	 *
	 * @param vertices
	 *            vertices to add as Collection<? extends HE_Vertex>
	 */
	void addVertices(Collection<? extends HE_Vertex> vertices);

	/**
	 *
	 *
	 * @param source
	 */
	void addVertices(HE_HalfedgeStructure source);

	/**
	 * Adds vertices.
	 *
	 * @param vertices
	 *            vertices to add as HE_Vertex[]
	 */
	void addVertices(HE_Vertex[] vertices);

	/**
	 * Clear entire structure.
	 */
	void clear();

	/**
	 * Clear edges.
	 */
	void clearEdges();

	/**
	 * Clear faces.
	 */
	void clearFaces();

	/**
	 * Clear halfedges.
	 */
	void clearHalfedges();

	/**
	 * Clear vertices.
	 */
	void clearVertices();

	boolean contains(HE_Element el);

	/**
	 * Edge iterator.
	 *
	 * @return edge iterator
	 */
	Iterator<HE_Halfedge> eItr();

	/**
	 * Face iterator.
	 *
	 * @return face iterator
	 */
	Iterator<HE_Face> fItr();

	/**
	 *
	 *
	 * @return
	 */
	List<HE_Halfedge> getEdges();

	/**
	 * Edges as array.
	 *
	 * @return all edges as HE_Halfedge[]
	 */
	HE_Halfedge[] getEdgesAsArray();

	/**
	 * Get edge with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            edge index
	 * @return
	 */
	HE_Halfedge getEdgeWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	List<HE_Face> getFaces();

	/**
	 * Faces as array.
	 *
	 * @return all faces as HE_Face[]
	 */
	HE_Face[] getFacesAsArray();

	/**
	 * Get face with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            face index
	 * @return
	 */
	HE_Face getFaceWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	List<HE_Halfedge> getHalfedges();

	/**
	 * Halfedges as array.
	 *
	 * @return all halfedges as HE_Halfedge[]
	 */
	HE_Halfedge[] getHalfedgesAsArray();

	/**
	 * Get halfedge with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            halfedge index
	 * @return
	 */
	HE_Halfedge getHalfedgeWithIndex(int i);

	String getName();

	/**
	 * Number of edges.
	 *
	 * @return the number of edges
	 */
	int getNumberOfEdges();

	/**
	 * Number of faces.
	 *
	 * @return the number of faces
	 */
	int getNumberOfFaces();

	/**
	 * Number of halfedges.
	 *
	 * @return the number of halfedges
	 */
	int getNumberOfHalfedges();

	/**
	 * Number of vertices.
	 *
	 * @return the number of vertices
	 */
	int getNumberOfVertices();

	/**
	 * Get vertex with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            vertex index
	 * @return
	 */
	HE_Vertex getVertexWithIndex(int i);

	/**
	 *
	 *
	 * @return
	 */
	List<HE_Vertex> getVertices();

	/**
	 * Vertices as array.
	 *
	 * @return all vertices as HE_Vertex[]
	 */
	HE_Vertex[] getVerticesAsArray();

	/**
	 * Halfedge iterator.
	 *
	 * @return halfedge iterator
	 */
	HE_HalfedgeIterator heItr();

	/**
	 * Modify the mesh.
	 *
	 * @param modifier
	 *            HE_Modifier to apply
	 * @return self
	 */
	HE_Mesh modify(HEM_Modifier modifier);

	/**
	 * Removes face.
	 *
	 * @param f
	 *            face to remove
	 */
	void remove(HE_Face f);

	/**
	 * Removes halfedge.
	 *
	 * @param he
	 *            halfedge to remove
	 */
	void remove(HE_Halfedge he);

	/**
	 * Removes vertex.
	 *
	 * @param v
	 *            vertex to remove
	 */
	void remove(HE_Vertex v);

	/**
	 * Removes edges.
	 *
	 * @param edges
	 *            edges to remove as Collection<? extends HE_Halfedge>
	 */
	void removeEdges(Collection<? extends HE_Halfedge> edges);

	/**
	 * Removes edges.
	 *
	 * @param edges
	 *            edges to remove as HE_Halfedge[]
	 */
	void removeEdges(HE_Halfedge[] edges);

	/**
	 * Removes faces.
	 *
	 * @param faces
	 *            faces to remove as Collection<? extends HE_Face>
	 */
	void removeFaces(Collection<? extends HE_Face> faces);

	/**
	 * Removes faces.
	 *
	 * @param faces
	 *            faces to remove as HE_Face[]
	 */
	void removeFaces(HE_Face[] faces);

	/**
	 * Removes halfedges.
	 *
	 * @param halfedges
	 *            halfedges to remove as Collection<? extends HE_Halfedge>
	 */
	void removeHalfedges(Collection<? extends HE_Halfedge> halfedges);

	/**
	 * Removes halfedges.
	 *
	 * @param halfedges
	 *            halfedges to remove as HE_Halfedge[]
	 */
	void removeHalfedges(HE_Halfedge[] halfedges);

	/**
	 * Removes vertices.
	 *
	 * @param vertices
	 *            vertices to remove as Collection<? extends HE_Vertex>
	 */
	void removeVertices(Collection<? extends HE_Vertex> vertices);

	/**
	 * Removes vertices.
	 *
	 * @param vertices
	 *            vertices to remove as HE_Vertex[]
	 */
	void removeVertices(HE_Vertex[] vertices);

	void setName(String name);

	/**
	 * Simplify.
	 *
	 * @param simplifier
	 *            the simplifier
	 * @return the h e_ mesh
	 */
	HE_Mesh simplify(HES_Simplifier simplifier);

	/**
	 * Subdivide the mesh.
	 *
	 * @param subdividor
	 *            HE_Subdividor to apply
	 * @return self
	 */
	HE_Mesh subdivide(HES_Subdividor subdividor);

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
	HE_Mesh subdivide(HES_Subdividor subdividor, int rep);

	/**
	 * Vertex iterator.
	 *
	 * @return vertex iterator
	 */
	Iterator<HE_Vertex> vItr();

	/**
	 * @param key
	 * @return
	 */
	HE_Face getFaceWithKey(long key);

	/**
	 * @param key
	 * @return
	 */
	HE_Halfedge getHalfedgeWithKey(long key);

	/**
	 * @param key
	 * @return
	 */
	HE_Halfedge getEdgeWithKey(long key);

	/**
	 * @param key
	 * @return
	 */
	HE_Vertex getVertexWithKey(long key);

	/**
	 * @param f
	 * @return
	 */
	boolean contains(HE_Face f);

	/**
	 * @param he
	 * @return
	 */
	boolean contains(HE_Halfedge he);

	/**
	 * @param v
	 * @return
	 */
	boolean contains(HE_Vertex v);

	/**
	 * @param key
	 * @return
	 */
	boolean containsFace(long key);

	/**
	 * @param key
	 * @return
	 */
	boolean containsHalfedge(long key);

	/**
	 * @param key
	 * @return
	 */
	boolean containsEdge(long key);

	/**
	 * @param key
	 * @return
	 */
	boolean containsVertex(long key);

	/**
	 * @param f
	 * @return
	 */
	int getIndex(HE_Face f);

	/**
	 * @param edge
	 * @return
	 */
	int getIndex(HE_Halfedge edge);

	/**
	 * @param v
	 * @return
	 */
	int getIndex(HE_Vertex v);

	void stats();
}
