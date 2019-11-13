/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import wblut.geom.WB_Coord;
import wblut.geom.WB_KDTreeInteger3D;
import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.geom.WB_SimpleMesh;
import wblut.geom.WB_SimpleMeshCreator;
import wblut.math.WB_Epsilon;

/**
 * Creates a new mesh from a list of vertices and faces. Vertices can be
 * duplicate.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_FromSimpleMesh extends HEC_Creator {
	/**
	 * Facelist source mesh.
	 */
	private final WB_SimpleMesh	source;


	/**
	 * 
	 *
	 * @param source
	 */
	public HEC_FromSimpleMesh(final WB_SimpleMesh source) {
		super();
		this.source = source;
		setCheckDuplicateVertices(true);
		setCheckNormals(true);
		setOverride(true);
		setCheckUniformNormals(true);
	}

	/**
	 *
	 *
	 * @param source
	 */
	public HEC_FromSimpleMesh(final WB_SimpleMeshCreator source) {
		super();
		this.source = source.create();
		setCheckDuplicateVertices(true);
		setCheckNormals(true);
		setOverride(true);
		setCheckUniformNormals(true);
	}
	
	protected boolean getCheckDuplicateVertices() {
		return parameters.get("duplicate",  true);	
	}
	
	protected boolean getCheckUniformNormals() {
		return parameters.get("uniform",  true);	
	}

	public HEC_FromSimpleMesh setCheckDuplicateVertices(final boolean b) {
		parameters.set("duplicate", b);
		return this;
	}

	
	public HEC_FromSimpleMesh setCheckUniformNormals(final boolean b) {
		parameters.set("uniform", b);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final HE_Mesh mesh = new HE_Mesh();
		if (source == null) {
			return mesh;
		}
		if (source.getNumberOfVertices() == 0) {
			return mesh;
		}
		final int[][] faces = source.getFacesAsInt();
		final List<HE_Vertex> uniqueVertices = getUniqueVertices(mesh);
		if (getCheckUniformNormals()) {
			unifyNormals(faces);
		}
		int id = 0;
		HE_Halfedge he;
		for (final int[] face : faces) {
			final ArrayList<HE_Halfedge> faceEdges = new ArrayList<HE_Halfedge>();
			final HE_Face hef = new HE_Face();
			hef.setInternalLabel(id);
			id++;
			final int fl = face.length;
			final int[] locface = new int[fl];
			int li = 0;
			locface[li++] = face[0];
			for (int i = 1; i < fl - 1; i++) {
				if (uniqueVertices.get(face[i]) != uniqueVertices
						.get(face[i - 1])) {
					locface[li++] = face[i];
				}
			}
			if (uniqueVertices.get(face[fl - 1]) != uniqueVertices
					.get(face[fl - 2])
					&& uniqueVertices.get(face[fl - 1]) != uniqueVertices
							.get(face[0])) {
				locface[li++] = face[fl - 1];
			}
			if (li > 2) {
				for (int i = 0; i < li; i++) {
					he = new HE_Halfedge();
					faceEdges.add(he);
					mesh.setFace(he, hef);
					if (hef.getHalfedge() == null) {
						mesh.setHalfedge(hef, he);
					}
					mesh.setVertex(he, uniqueVertices.get(locface[i]));
					mesh.setHalfedge(he.getVertex(), he);
				}
				mesh.add(hef);
				HE_MeshOp.cycleHalfedges(mesh, faceEdges);
				mesh.addHalfedges(faceEdges);
			}
		}
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		if (getCheckUniformNormals()) {
			HET_Fixer.unifyNormals(mesh);
		}
		return mesh;
	}

	/**
	 * Ohash.
	 *
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @return the long
	 */
	private Long ohash(final int u, final int v) {
		int lu = u;
		int lv = v;
		if (u > v) {
			lu = v;
			lv = u;
		}
		final long A = lu >= 0 ? 2 * lu : -2 * lu - 1;
		final long B = lv >= 0 ? 2 * lv : -2 * lv - 1;
		return A >= B ? A * A + A + B : A + B * B;
	}

	/**
	 * Consistent order.
	 *
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param face
	 *            the face
	 * @param neighbor
	 *            the neighbor
	 * @return the int
	 */
	private int consistentOrder(final int i, final int j, final int[] face,
			final int[] neighbor) {
		for (int k = 0; k < neighbor.length; k++) {
			if (neighbor[k] == face[i]
					&& neighbor[(k + 1) % neighbor.length] == face[j]) {
				return -1;
			}
			if (neighbor[k] == face[j]
					&& neighbor[(k + 1) % neighbor.length] == face[i]) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	private List<HE_Vertex> getUniqueVertices(final HE_Mesh mesh) {
		final List<HE_Vertex> uniqueVertices = new FastList<HE_Vertex>();
		if (getCheckDuplicateVertices()) {
			final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
			WB_KDEntryInteger<WB_Coord> neighbor;
			HE_Vertex v = new HE_Vertex(source.getVertex(0));
			kdtree.add(source.getVertex(0), 0);
			uniqueVertices.add(v);
			mesh.add(v);
			for (int i = 1; i < source.getNumberOfVertices(); i++) {
				v = new HE_Vertex(source.getVertex(i));
				v.setInternalLabel(i);
				neighbor = kdtree.getNearestNeighbor(v);
				if (neighbor.d2 < WB_Epsilon.SQEPSILON) {
					uniqueVertices.add(uniqueVertices.get(neighbor.value));
				} else {
					kdtree.add(source.getVertex(i), i);
					uniqueVertices.add(v);
					mesh.add(uniqueVertices.get(i));
				}
			}
		} else {
			HE_Vertex v;
			for (int i = 0; i < source.getNumberOfVertices(); i++) {
				v = new HE_Vertex(source.getVertex(i));
				v.setInternalLabel(i);
				uniqueVertices.add(v);
				mesh.add(v);
			}
		}
		return uniqueVertices;
	}

	/**
	 *
	 *
	 * @param faces
	 */
	private void unifyNormals(final int[][] faces) {
		final LongObjectHashMap<int[]> edges = new LongObjectHashMap<int[]>();
		for (int i = 0; i < faces.length; i++) {
			final int[] face = faces[i];
			final int fl = face.length;
			for (int j = 0; j < fl; j++) {
				final long ohash = ohash(face[j], face[(j + 1) % fl]);
				final int[] efaces = edges.get(ohash);
				if (efaces == null) {
					edges.put(ohash, new int[] { i, -1 });
				} else {
					efaces[1] = i;
				}
			}
		}
		final boolean[] visited = new boolean[faces.length];
		final LinkedList<Integer> queue = new LinkedList<Integer>();
		boolean facesleft = false;
		int starti = 0;
		do {
			queue.add(starti);
			int temp;
			while (!queue.isEmpty()) {
				facesleft = false;
				final Integer index = queue.poll();
				final int[] face = faces[index];
				final int fl = face.length;
				visited[index] = true;
				for (int j = 0; j < fl; j++) {
					final long ohash = ohash(face[j], face[(j + 1) % fl]);
					final int[] ns = edges.get(ohash);
					int neighbor;
					if (ns[0] == index) {
						neighbor = ns[1];
					} else {
						neighbor = ns[0];
					}
					if (neighbor > -1) {
						if (visited[neighbor] == false) {
							queue.add(neighbor);
							if (consistentOrder(j, (j + 1) % fl, face,
									faces[neighbor]) == -1) {
								final int fln = faces[neighbor].length;
								for (int k = 0; k < fln / 2; k++) {
									temp = faces[neighbor][k];
									faces[neighbor][k] = faces[neighbor][fln - k
											- 1];
									faces[neighbor][fln - k - 1] = temp;
								}
							}
						}
					}
				}
			}
			for (; starti < faces.length; starti++) {
				if (!visited[starti]) {
					facesleft = true;
					break;
				}
			}
		} while (facesleft);
	}
}
