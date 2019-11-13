/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_KDTreeInteger3D;
import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

/**
 * Creates a new mesh from a list of vertices and faces. Vertices can be
 * duplicate.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_FromFacelist extends HEC_Creator {
	/** Vertices. */
	private WB_Coord[]	vertices;
	private WB_Coord[]	uvws;
	private WB_Coord[]	vertexuvws;
	private int[]		vertexColors;
	private boolean[]	vertexVisibility;
	private int[]		vertexLabels;
	private int[]		vertexInternalLabels;
	private int[]		faceColors;
	private int[]		faceTextureIds;
	private boolean[]	faceVisibility;
	private int[]		faceLabels;
	private int[]		faceInternalLabels;
	/** Face indices. */
	private int[][]		faces;
	private int[][]		faceuvws;


	/**
	 * Instantiates a new HEC_Facelist.
	 *
	 */
	public HEC_FromFacelist() {
		super();
		setOverride(true);
		setCheckDuplicateVertices(true);
		
		setRemoveUnconnectedElements(true);
	}
	
	protected boolean getCheckDuplicateVertices() {
		return parameters.get("duplicate", true);
	}
	
	protected boolean getUseFaceInfo() {
		return parameters.get("usefaceinfo", false);
	}
	protected boolean getUseVertexInfo() {
		return parameters.get("usevertexinfo", false);
	}

	/**
	 * Set vertex coordinates from an array of WB_point. No copies are made.
	 *
	 * @param vs
	 *            vertices
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final WB_Coord[] vs) {
		vertices = vs;
		return this;
	}

	public HEC_FromFacelist setVertices(final WB_CoordCollection vs) {
		vertices = vs.toArray();
		return this;
	}

	/**
	 * Set vertex coordinates from an arraylist of WB_point.
	 *
	 * @param vs
	 *            vertices
	 * @return self
	 */
	public HEC_FromFacelist setVertices(
			final Collection<? extends WB_Coord> vs) {
		final int n = vs.size();
		final Iterator<? extends WB_Coord> itr = vs.iterator();
		vertices = new WB_Coord[n];
		int i = 0;
		while (itr.hasNext()) {
			vertices[i] = itr.next();
			i++;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param vs
	 * @return
	 */
	public HEC_FromFacelist setVertexUVW(
			final Collection<? extends WB_Coord> vs) {
		final int n = vs.size();
		final Iterator<? extends WB_Coord> itr = vs.iterator();
		vertexuvws = new WB_Coord[n];
		int i = 0;
		while (itr.hasNext()) {
			vertexuvws[i] = itr.next();
			i++;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param vs
	 * @return
	 */
	public HEC_FromFacelist setVertexUVW(final WB_Coord[] vs) {
		final int n = vs.length;
		vertexuvws = new WB_Coord[n];
		int i = 0;
		for (final WB_Coord v : vs) {
			vertexuvws[i] = v;
			i++;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param vs
	 * @return
	 */
	public HEC_FromFacelist setVertexUVW(final double[][] vs) {
		final int n = vs.length;
		vertexuvws = new WB_Point[n];
		for (int i = 0; i < n; i++) {
			vertexuvws[i] = new WB_Point(vs[i][0], vs[i][1], vs[i][2]);
		}
		return this;
	}

	public HEC_FromFacelist setFaceVertexUVW(
			final Collection<? extends WB_Coord> vs) {
		final int n = vs.size();
		final Iterator<? extends WB_Coord> itr = vs.iterator();
		uvws = new WB_Coord[n];
		int i = 0;
		while (itr.hasNext()) {
			uvws[i] = itr.next();
			i++;
		}
		return this;
	}

	public HEC_FromFacelist setFaceVertexUVW(final WB_Coord[] vs) {
		final int n = vs.length;
		uvws = new WB_Coord[n];
		int i = 0;
		for (final WB_Coord v : vs) {
			uvws[i] = v;
			i++;
		}
		return this;
	}

	public HEC_FromFacelist setFaceVertexUVW(final double[][] vs) {
		final int n = vs.length;
		uvws = new WB_Point[n];
		for (int i = 0; i < n; i++) {
			uvws[i] = new WB_Point(vs[i][0], vs[i][1], vs[i][2]);
		}
		return this;
	}

	/**
	 * Set vertex coordinates from an array of WB_point.
	 *
	 * @param vs
	 *            vertices
	 * @param copy
	 *            copy points?
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final WB_Coord[] vs,
			final boolean copy) {
		if (copy) {
			final int n = vs.length;
			vertices = new WB_Coord[n];
			for (int i = 0; i < n; i++) {
				vertices[i] = new WB_Point(vs[i]);
			}
		} else {
			vertices = vs;
		}
		return this;
	}

	/**
	 * Set vertex coordinates from a 2D array of double: 1st index=point, 2nd
	 * index (0..2) coordinates
	 *
	 * @param vs
	 *            Nx3 2D array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final double[][] vs) {
		final int n = vs.length;
		vertices = new WB_Point[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new WB_Point(vs[i][0], vs[i][1], vs[i][2]);
		}
		return this;
	}

	/**
	 * Set vertex coordinates from array of double: x0, y0 ,z0 ,x1 ,y1 ,z1 ,...
	 *
	 * @param vs
	 *            array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final double[] vs) {
		final int n = vs.length;
		vertices = new WB_Point[n / 3];
		for (int i = 0; i < n; i += 3) {
			vertices[i] = new WB_Point(vs[i], vs[i + 1], vs[i + 2]);
		}
		return this;
	}

	/**
	 * Set vertex coordinates from a 2D array of float: 1st index=point, 2nd
	 * index (0..2) coordinates
	 *
	 * @param vs
	 *            Nx3 2D array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final float[][] vs) {
		final int n = vs.length;
		vertices = new WB_Point[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new WB_Point(vs[i][0], vs[i][1], vs[i][2]);
		}
		return this;
	}

	/**
	 * Set vertex coordinates from array of float: x0, y0 ,z0 ,x1 ,y1 ,z1 ,...
	 *
	 * @param vs
	 *            array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final float[] vs) {
		final int n = vs.length;
		vertices = new WB_Point[n / 3];
		for (int i = 0; i < n; i += 3) {
			vertices[i] = new WB_Point(vs[i], vs[i + 1], vs[i + 2]);
		}
		return this;
	}

	/**
	 * Set faces from 2D array of int: 1st index=face, 2nd=index of vertex.
	 *
	 * @param fs
	 *            2D array of vertex indices
	 * @return self
	 */
	public HEC_FromFacelist setFaces(final int[][] fs) {
		faces = fs;
		return this;
	}

	/**
	 * Set faces from 1D array of int: triangles are assumed.
	 *
	 * @param fs
	 *            1D array of vertex indices
	 * @return self
	 */
	public HEC_FromFacelist setFaces(final int[] fs) {
		faces = new int[fs.length / 3][3];
		for (int i = 0; i < fs.length; i += 3) {
			faces[i / 3] = new int[] { fs[i], fs[i + 1], fs[i + 2] };
		}
		return this;
	}

	public HEC_FromFacelist setFaces(final List<int[]> fs) {
		faces = new int[fs.size()][];
		int i = 0;
		for (final int[] indices : fs) {
			faces[i] = indices;
			i++;
		}
		return this;
	}

	public HEC_FromFacelist setFacesUVW(final int[][] fs) {
		faceuvws = fs;
		return this;
	}

	public HEC_FromFacelist setFacesUVW(final int[] fs) {
		faceuvws = new int[fs.length / 3][3];
		for (int i = 0; i < fs.length; i += 3) {
			faceuvws[i / 3] = new int[] { fs[i], fs[i + 1], fs[i + 2] };
		}
		return this;
	}

	public HEC_FromFacelist setFacesUVW(final List<int[]> fs) {
		faceuvws = new int[fs.size()][];
		int i = 0;
		for (final int[] indices : fs) {
			faceuvws[i] = indices;
			i++;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param fts
	 * @return
	 */
	public HEC_FromFacelist setFaceTextureIds(final int[] fts) {
		faceTextureIds = fts;
		return this;
	}

	/**
	 * Duplicate vertices in input?.
	 *
	 * @param b
	 *            true/false
	 * @return self
	 */
	public HEC_FromFacelist setCheckDuplicateVertices(final boolean b) {
		parameters.set("duplicate", b);
		return this;
	}


	public HEC_FromFacelist setUseFaceInformation(final boolean b) {
		parameters.set("usefaceinfo",b);
		return this;
	}

	public HEC_FromFacelist setUseVertexInformation(final boolean b) {
		parameters.set("usevertexinfo",b);
		return this;
	}

	public HEC_FromFacelist setFaceInformation(final int[] colors,
			final int[] textureIds, final boolean[] visibility,
			final int[] labels, final int[] intLabels) {
		this.faceColors = colors;
		this.faceTextureIds = textureIds;
		this.faceVisibility = visibility;
		this.faceLabels = labels;
		this.faceInternalLabels = intLabels;
		return this;
	}

	public HEC_FromFacelist setVertexInformation(final int[] colors,
			final boolean[] visibility, final int[] labels,
			final int[] intLabels) {
		this.vertexColors = colors;
		this.vertexVisibility = visibility;
		this.vertexLabels = labels;
		this.vertexInternalLabels = intLabels;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final HE_Mesh mesh = new HE_Mesh();
		if (faces != null && vertices != null) {
			if (faces.length == 0) {
				return mesh;
			}
			final boolean useVertexUVW = vertexuvws != null
					&& vertexuvws.length == vertices.length;
			final boolean useFaceUVW = uvws != null && faceuvws != null
					&& faceuvws.length == faces.length;
			final HE_Vertex[] uniqueVertices = new HE_Vertex[vertices.length];
			final boolean[] duplicated = new boolean[vertices.length];
			boolean useVertexInfo=getUseVertexInfo();
			boolean useFaceInfo=getUseFaceInfo();
			if (getCheckDuplicateVertices()) {
				final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
				WB_KDEntryInteger<WB_Coord>[] neighbors;
				HE_Vertex v = new HE_Vertex(vertices[0]);
				if (useVertexInfo) {
					v.setColor(vertexColors[0]);
					v.setVisible(vertexVisibility[0]);
					v.setUserLabel(vertexLabels[0]);
					v.setInternalLabel(vertexInternalLabels[0]);
				} else {
					v.setInternalLabel(0);
				}
				if (useVertexUVW) {
					v.setUVW(vertexuvws[0]);
				}
				kdtree.add(v, 0);
				uniqueVertices[0] = v;
				duplicated[0] = false;
				mesh.add(v);
				
				for (int i = 1; i < vertices.length; i++) {
					v = new HE_Vertex(vertices[i]);
					if (useVertexInfo) {
						v.setColor(vertexColors[i]);
						v.setVisible(vertexVisibility[i]);
						v.setUserLabel(vertexLabels[i]);
						v.setInternalLabel(vertexInternalLabels[i]);
					} else {
						v.setInternalLabel(i);
					}
					if (useVertexUVW) {
						v.setUVW(vertexuvws[i]);
					}
					neighbors = kdtree.getNearestNeighbors(v, 1);
					if (neighbors[0].d2 < WB_Epsilon.SQEPSILON) {
						uniqueVertices[i] = uniqueVertices[neighbors[0].value];
						duplicated[i] = true;
					} else {
						kdtree.add(v, i);
						uniqueVertices[i] = v;
						mesh.add(uniqueVertices[i]);
						duplicated[i] = false;
					}
				}
			} else {
				HE_Vertex v;
				for (int i = 0; i < vertices.length; i++) {
					v = new HE_Vertex(vertices[i]);
					if (useVertexInfo) {
						v.setColor(vertexColors[i]);
						v.setVisible(vertexVisibility[i]);
						v.setUserLabel(vertexLabels[i]);
						v.setInternalLabel(vertexInternalLabels[i]);
					} else {
						v.setInternalLabel(i);
					}
					if (useVertexUVW) {
						v.setUVW(vertexuvws[i]);
					}
					v.setInternalLabel(i);
					uniqueVertices[i] = v;
					duplicated[i] = false;
					mesh.add(uniqueVertices[i]);
				}
			}
			int id = 0;
			HE_Halfedge he;
			final List<Long> nmedges = new ArrayList<Long>();
			if (getCheckNormals()) {
				// Create adjacency table
				final UnifiedMap<Long, int[]> edges = new UnifiedMap<Long, int[]>();
				for (int i = 0; i < faces.length; i++) {
					final int[] face = faces[i];
					final int fl = face.length;
					for (int j = 0; j < fl; j++) {
						final long ohash = ohash(face[j], face[(j + 1) % fl]);
						final int[] faces = edges.get(ohash);
						if (faces == null) {
							edges.put(ohash, new int[] { i, -1 });
						} else {
							if (faces[1] > -1) {
								nmedges.add(ohash);
							}
							faces[1] = i;
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
						final Integer index = queue.poll();
						final int[] face = faces[index];
						final int fl = face.length;
						visited[index] = true;
						for (int j = 0; j < fl; j++) {
							final long ohash = ohash(face[j],
									face[(j + 1) % fl]);
							final int[] ns = edges.get(ohash);
							if (ns != null) {
								edges.remove(ohash);// no need to revisit
								// previous edges
								Integer neighbor;
								if (ns[0] == index) {
									neighbor = ns[1];
								} else {
									neighbor = ns[0];
								}
								if (neighbor > -1) {
									if (visited[neighbor] == false) {
										if (!queue.contains(neighbor)) {
											queue.add(neighbor);
										}
										if (consistentOrder(j, (j + 1) % fl,
												face, faces[neighbor]) == -1) {
											final int fln = faces[neighbor].length;
											for (int k = 0; k < fln / 2; k++) {
												temp = faces[neighbor][k];
												faces[neighbor][k] = faces[neighbor][fln
														- k - 1];
												faces[neighbor][fln - k
														- 1] = temp;
											}
										}
										visited[neighbor] = true;
									}
								}
							}
						}
					}
					facesleft = false;
					for (; starti < faces.length; starti++) {
						if (!visited[starti]) {
							facesleft = true;
							break;
						}
					}
				} while (facesleft);
			}
			final boolean useFaceTextures = faceTextureIds != null
					&& faceTextureIds.length == faces.length;
			int faceid = 0;
			for (final int[] face : faces) {
				int[] faceuvw = null;
				if (useFaceUVW) {
					faceuvw = faceuvws[faceid];
				}
				if (face != null) {
					final ArrayList<HE_Halfedge> faceEdges = new ArrayList<HE_Halfedge>();
					final HE_Face hef = new HE_Face();
					hef.setInternalLabel(id);
					if (useFaceInfo) {
						hef.setColor(faceColors[faceid]);
						hef.setTextureId(faceTextureIds[id]);
						hef.setVisible(faceVisibility[id]);
						hef.setUserLabel(faceLabels[id]);
						hef.setInternalLabel(faceInternalLabels[id]);
					}
					if (useFaceTextures) {
						hef.setTextureId(faceTextureIds[id]);
					}
					id++;
					final int fl = face.length;
					final int[] locface = new int[fl];
					final int[] locfaceuvw = new int[fl];
					int li = 0;
					locface[li] = face[0];
					if (useFaceUVW) {
						locfaceuvw[li] = faceuvw[0];
					}
					li++;
					for (int i = 1; i < fl - 1; i++) {
						if (uniqueVertices[face[i]] != uniqueVertices[face[i
								- 1]]) {
							locface[li] = face[i];
							if (useFaceUVW) {
								locfaceuvw[li] = faceuvw[i];
							}
							li++;
						}
					}
					if (uniqueVertices[face[fl - 1]] != uniqueVertices[face[fl
							- 2]]
							&& uniqueVertices[face[fl
									- 1]] != uniqueVertices[face[0]]) {
						locface[li] = face[fl - 1];
						if (useFaceUVW) {
							locfaceuvw[li] = faceuvw[fl - 1];
						}
						li++;
					}
					if (li > 2) {
						for (int i = 0; i < li; i++) {
							he = new HE_Halfedge();
							faceEdges.add(he);
							mesh.setFace(he, hef);
							if (hef.getHalfedge() == null) {
								mesh.setHalfedge(hef, he);
							}
							mesh.setVertex(he, uniqueVertices[locface[i]]);
							if (useFaceUVW) {
								he.setUVW(uvws[locfaceuvw[i]]);
							}
							if (useVertexUVW) {
								if (duplicated[locface[i]]) {
									final HE_TextureCoordinate uvw = uniqueVertices[locface[i]]
											.getVertexUVW();
									if (uvw.ud() != vertexuvws[locface[i]].xd()
											|| uvw.vd() != vertexuvws[locface[i]]
													.yd()
											|| uvw.wd() != vertexuvws[locface[i]]
													.zd()) {
										he.setUVW(vertexuvws[locface[i]]);
									}
								}
							}
							mesh.setHalfedge(he.getVertex(), he);
						}
						mesh.add(hef);
						HE_MeshOp.cycleHalfedges(mesh, faceEdges);
						mesh.addHalfedges(faceEdges);
					}
				}
				faceid++;
			}
			HE_MeshOp.pairHalfedges(mesh);
			HE_MeshOp.capHalfedges(mesh);
			
		}
		return mesh;
	}

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
}
