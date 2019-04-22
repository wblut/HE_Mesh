/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

import wblut.geom.WB_Coord;

/**
 *
 * Collection of export functions.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class HET_Export {
	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJ(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToOBJ(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsOBJ(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJ(final HE_Mesh mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJNoNormals(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJNN(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsOBJNoNormals(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJNoNormals(final HE_Mesh mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJWithFaceColor(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJWithFaceColor(final HE_Mesh mesh,
			final String path, final String name) {
		saveAsOBJWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJWithFaceColor(final HE_Mesh mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJWithVertexColor(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJWithVertexColor(final HE_Mesh mesh,
			final String path, final String name) {
		saveAsOBJWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJWithVertexColor(final HE_Mesh mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJ(Collection<? extends
	 *             HE_Mesh>,String,String)} instead
	 */
	@Deprecated
	public static void saveToOBJ(final Collection<? extends HE_Mesh> mesh,
			final String path, final String name) {
		saveAsOBJ(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJ(final Collection<? extends HE_Mesh> mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	/**
	 * @deprecated Use {@link #saveAsOBJ(HE_MeshCollection,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJ(final HE_MeshCollection mesh,
			final String path, final String name) {
		saveAsOBJ(mesh, path, name);
	}

	public static void saveAsOBJ(final HE_MeshCollection mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJNoNormals(Collection<? extends
	 *             HE_Mesh>,String,String)} instead
	 */
	@Deprecated
	public static void saveToOBJNN(final Collection<? extends HE_Mesh> mesh,
			final String path, final String name) {
		saveAsOBJNoNormals(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJNoNormals(
			final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	/**
	 * @deprecated Use
	 *             {@link #saveAsOBJNoNormals(HE_MeshCollection,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJNN(final HE_MeshCollection mesh,
			final String path, final String name) {
		saveAsOBJNoNormals(mesh, path, name);
	}

	public static void saveAsOBJNoNormals(final HE_MeshCollection mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJWithFaceColor(Collection<? extends
	 *             HE_Mesh>,String,String)} instead
	 */
	@Deprecated
	public static void saveToOBJWithFaceColor(
			final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		saveAsOBJWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJWithFaceColor(
			final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	/**
	 * @deprecated Use
	 *             {@link #saveAsOBJWithFaceColor(HE_MeshCollection,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJWithFaceColor(final HE_MeshCollection mesh,
			final String path, final String name) {
		saveAsOBJWithFaceColor(mesh, path, name);
	}

	public static void saveAsOBJWithFaceColor(final HE_MeshCollection mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJWithVertexColor(Collection<? extends
	 *             HE_Mesh>,String,String)} instead
	 */
	@Deprecated
	public static void saveToOBJWithVertexColor(
			final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		saveAsOBJWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJWithVertexColor(
			final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	/**
	 * @deprecated Use
	 *             {@link #saveAsOBJWithVertexColor(HE_MeshCollection,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJWithVertexColor(final HE_MeshCollection mesh,
			final String path, final String name) {
		saveAsOBJWithVertexColor(mesh, path, name);
	}

	public static void saveAsOBJWithVertexColor(final HE_MeshCollection mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJ(HE_Mesh[],String,String)} instead
	 */
	@Deprecated
	public static void saveToOBJ(final HE_Mesh[] mesh, final String path,
			final String name) {
		saveAsOBJ(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJ(final HE_Mesh[] mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJNoNormals(HE_Mesh[],String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJNN(final HE_Mesh[] mesh, final String path,
			final String name) {
		saveAsOBJNoNormals(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJNoNormals(final HE_Mesh[] mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsOBJWithFaceColor(HE_Mesh[],String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJWithFaceColor(final HE_Mesh[] mesh,
			final String path, final String name) {
		saveAsOBJWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJWithFaceColor(final HE_Mesh[] mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use
	 *             {@link #saveAsOBJWithVertexColor(HE_Mesh[],String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToOBJWithVertexColor(final HE_Mesh[] mesh,
			final String path, final String name) {
		saveAsOBJWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsOBJWithVertexColor(final HE_Mesh[] mesh,
			final String path, final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsSTL(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToSTL(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsSTL(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsSTL(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsSTLWithFaceColor(mesh, path, name, NONE);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @param colormodel
	 * @deprecated Use
	 *             {@link #saveAsSTLWithFaceColor(HE_Mesh,String,String,int)}
	 *             instead
	 */
	@Deprecated
	public static void saveToSTLWithFaceColor(final HE_Mesh mesh,
			final String path, final String name, final int colormodel) {
		saveAsSTLWithFaceColor(mesh, path, name, colormodel);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @param colormodel
	 */
	public static void saveAsSTLWithFaceColor(final HE_Mesh mesh,
			final String path, final String name, final int colormodel) {
		final HET_WriterSTL stl = new HET_WriterSTL(
				colormodel == 1 ? HET_WriterSTL.MATERIALISE
						: colormodel == 0 ? HET_WriterSTL.DEFAULT
								: HET_WriterSTL.NONE,
				HET_WriterSTL.DEFAULT_BUFFER);
		stl.beginSave(path, name, mesh.getNumberOfFaces());
		saveAsSTLWithFaceColor(mesh, stl);
		stl.endSave();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param stl
	 * @deprecated Use {@link #saveAsSTLWithFaceColor(HE_Mesh,HET_WriterSTL)}
	 *             instead
	 */
	@Deprecated
	public static void saveToSTLWithFaceColor(final HE_Mesh mesh,
			final HET_WriterSTL stl) {
		saveAsSTLWithFaceColor(mesh, stl);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param stl
	 */
	public static void saveAsSTLWithFaceColor(final HE_Mesh mesh,
			final HET_WriterSTL stl) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			stl.face(f.getHalfedge().getVertex(),
					f.getHalfedge().getNextInFace().getVertex(),
					f.getHalfedge().getPrevInFace().getVertex(),
					HE_MeshOp.getFaceNormal(f), f.getColor());
		}
	}

	/**
	 * Saves the mesh as simpleMesh format to the given file path. Existing
	 * files will be overwritten. The file gives the vertex coordinates and an
	 * indexed facelist.
	 *
	 * @param mesh
	 *            the mesh
	 * @param path
	 *            the path
	 * @param name
	 * @deprecated Use {@link #saveAsSimpleMesh(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToSimpleMesh(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsSimpleMesh(mesh, path, name);
	}

	/**
	 * Saves the mesh as simpleMesh format to the given file path. Existing
	 * files will be overwritten. The file gives the vertex coordinates and an
	 * indexed facelist.
	 *
	 * @param mesh
	 *            the mesh
	 * @param path
	 *            the path
	 * @param name
	 */
	public static void saveAsSimpleMesh(final HE_Mesh mesh, final String path,
			final String name) {
		final HET_WriterSimpleMesh hem = new HET_WriterSimpleMesh();
		hem.beginSave(path, name);
		final List<WB_Coord> points = mesh.getVerticesAsCoord();
		hem.intValue(mesh.getNumberOfVertices());
		hem.vertices(points);
		final int[][] faces = mesh.getFacesAsInt();
		hem.intValue(mesh.getNumberOfFaces());
		hem.faces(faces);
		hem.endSave();
	}

	/**
	 * Saves the mesh as hemesh format to the given file path. Existing files
	 * will be overwritten. The file contains the vertex coordinates and all
	 * half-edge interconnection information. Larger than a simpleMesh but much
	 * quicker to rebuild.
	 *
	 * @param mesh
	 *            the mesh
	 * @param path
	 *            the path
	 * @param name
	 * @deprecated Use {@link #saveAsHemesh(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToHemesh(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsHemesh(mesh, path, name);
	}

	/**
	 * Saves the mesh as hemesh format to the given file path. Existing files
	 * will be overwritten. The file contains the vertex coordinates and all
	 * half-edge interconnection information. Larger than a simpleMesh but much
	 * quicker to rebuild.
	 *
	 * @param mesh
	 *            the mesh
	 * @param path
	 *            the path
	 * @param name
	 */
	public static void saveAsHemesh(final HE_Mesh mesh, final String path,
			final String name) {
		final HET_WriterHemesh hem = new HET_WriterHemesh();
		hem.beginSave(path, name);
		final LongIntHashMap vertexKeys = new LongIntHashMap();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		int i = 0;
		while (vItr.hasNext()) {
			vertexKeys.put(vItr.next().getKey(), i);
			i++;
		}
		final LongIntHashMap halfedgeKeys = new LongIntHashMap();
		Iterator<HE_Halfedge> heItr = mesh.heItr();
		i = 0;
		while (heItr.hasNext()) {
			halfedgeKeys.put(heItr.next().getKey(), i);
			i++;
		}
		final LongIntHashMap faceKeys = new LongIntHashMap();
		Iterator<HE_Face> fItr = mesh.fItr();
		i = 0;
		while (fItr.hasNext()) {
			faceKeys.put(fItr.next().getKey(), i);
			i++;
		}
		hem.sizes(mesh.getNumberOfVertices(), mesh.getNumberOfHalfedges(),
				mesh.getNumberOfFaces());
		vItr = mesh.vItr();
		HE_Vertex v;
		int heid;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getHalfedge() == null) {
				heid = -1;
			} else {
				heid = halfedgeKeys.get(v.getHalfedge().getKey());
			}
			hem.vertex(v, heid);
		}
		heItr = mesh.heItr();
		HE_Halfedge he;
		int vid, henextid, hepairid;
		int fid;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getVertex() == null) {
				vid = -1;
			} else {
				vid = vertexKeys.get(he.getVertex().getKey());
			}
			if (he.getNextInFace() == null) {
				henextid = -1;
			} else {
				henextid = halfedgeKeys.get(he.getNextInFace().getKey());
			}
			if (he.getPair() == null) {
				hepairid = -1;
			} else {
				hepairid = halfedgeKeys.get(he.getPair().getKey());
			}
			if (he.getFace() == null) {
				fid = -1;
			} else {
				fid = faceKeys.get(he.getFace().getKey());
			}
			hem.halfedge(he, vid, henextid, hepairid, fid);
		}
		fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getHalfedge() == null) {
				heid = -1;
			} else {
				heid = halfedgeKeys.get(f.getHalfedge().getKey());
			}
			hem.face(f, heid);
		}
		hem.endSave();
	}

	/**
	 * Saves the mesh as binary hemesh format to the given file path. Existing
	 * files will be overwritten. The file contains the vertex coordinates and
	 * all half-edge interconnection information. About the same size of a
	 * simpleMesh but a lot quicker to rebuild. Due to compression about half as
	 * fast as an ordinary hemesh file but only a third in size.
	 *
	 * @param mesh
	 *            the mesh
	 * @param path
	 *            the path
	 * @param name
	 * @deprecated Use {@link #saveAsBinaryHemesh(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToBinaryHemesh(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsBinaryHemesh(mesh, path, name);
	}

	/**
	 * Saves the mesh as binary hemesh format to the given file path. Existing
	 * files will be overwritten. The file contains the vertex coordinates and
	 * all half-edge interconnection information. About the same size of a
	 * simpleMesh but a lot quicker to rebuild. Due to compression about half as
	 * fast as an ordinary hemesh file but only a third in size.
	 *
	 * @param mesh
	 *            the mesh
	 * @param path
	 *            the path
	 * @param name
	 */
	public static void saveAsBinaryHemesh(final HE_Mesh mesh, final String path,
			final String name) {
		final HET_WriterBinaryHemesh hem = new HET_WriterBinaryHemesh();
		hem.beginSave(path, name);
		final LongIntHashMap vertexKeys = new LongIntHashMap();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		int i = 0;
		while (vItr.hasNext()) {
			vertexKeys.put(vItr.next().getKey(), i);
			i++;
		}
		final LongIntHashMap halfedgeKeys = new LongIntHashMap();
		Iterator<HE_Halfedge> heItr = mesh.heItr();
		i = 0;
		while (heItr.hasNext()) {
			halfedgeKeys.put(heItr.next().getKey(), i);
			i++;
		}
		final LongIntHashMap faceKeys = new LongIntHashMap();
		Iterator<HE_Face> fItr = mesh.fItr();
		i = 0;
		while (fItr.hasNext()) {
			faceKeys.put(fItr.next().getKey(), i);
			i++;
		}
		hem.sizes(mesh.getNumberOfVertices(), mesh.getNumberOfHalfedges(),
				mesh.getNumberOfFaces());
		vItr = mesh.vItr();
		HE_Vertex v;
		Integer heid;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getHalfedge() == null) {
				heid = -1;
			} else {
				heid = halfedgeKeys.get(v.getHalfedge().getKey());
			}
			hem.vertex(v, heid);
		}
		heItr = mesh.heItr();
		HE_Halfedge he;
		Integer vid, henextid, hepairid, fid;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getVertex() == null) {
				vid = -1;
			} else {
				vid = vertexKeys.get(he.getVertex().getKey());
			}
			if (he.getNextInFace() == null) {
				henextid = -1;
			} else {
				henextid = halfedgeKeys.get(he.getNextInFace().getKey());
			}
			if (he.getPair() == null) {
				hepairid = -1;
			} else {
				hepairid = halfedgeKeys.get(he.getPair().getKey());
			}
			if (he.getFace() == null) {
				fid = -1;
			} else {
				fid = faceKeys.get(he.getFace().getKey());
			}
			hem.halfedge(he, vid, henextid, hepairid, fid);
		}
		fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getHalfedge() == null) {
				heid = -1;
			} else {
				heid = halfedgeKeys.get(f.getHalfedge().getKey());
			}
			hem.face(f, heid);
		}
		hem.endSave();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsPOV(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToPOV(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsPOV(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsPOV(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsPOV(mesh, path, name, true);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @param saveNormals
	 * @deprecated Use {@link #saveAsPOV(HE_Mesh,String,String,boolean)} instead
	 */
	@Deprecated
	public static void saveToPOV(final HE_Mesh mesh, final String path,
			final String name, final boolean saveNormals) {
		saveAsPOV(mesh, path, name, saveNormals);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @param saveNormals
	 */
	public static void saveAsPOV(final HE_Mesh mesh, final String path,
			final String name, final boolean saveNormals) {
		final HET_WriterPOV obj = new HET_WriterPOV();
		obj.beginSave(path, name);
		saveAsPOV(mesh, obj, saveNormals);
		obj.endSave();
	}

	/**
	 * Saves the mesh as PovRAY mesh2 format by appending it to the given mesh.
	 *
	 * @param mesh
	 *            the mesh
	 * @param pov
	 *            instance of HET_POVWriter
	 * @param normals
	 *            smooth faces {@link HET_WriterPOV} instance.
	 * @deprecated Use {@link #saveAsPOV(HE_Mesh,HET_WriterPOV,boolean)} instead
	 */
	@Deprecated
	public static void saveToPOV(final HE_Mesh mesh, final HET_WriterPOV pov,
			final boolean normals) {
		saveAsPOV(mesh, pov, normals);
	}

	/**
	 * Saves the mesh as PovRAY mesh2 format by appending it to the given mesh.
	 *
	 * @param mesh
	 *            the mesh
	 * @param pov
	 *            instance of HET_POVWriter
	 * @param normals
	 *            smooth faces {@link HET_WriterPOV} instance.
	 */
	public static void saveAsPOV(final HE_Mesh mesh, final HET_WriterPOV pov,
			final boolean normals) {
		final int vOffset = pov.getCurrVertexOffset();
		pov.beginMesh2(String.format("obj%d", mesh.getKey()));
		final LongIntHashMap keyToIndex = new LongIntHashMap();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		final int vcount = mesh.getNumberOfVertices();
		pov.total(vcount);
		HE_Vertex v;
		int fcount = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			keyToIndex.put(v.getKey(), fcount);
			pov.vertex(v);
			fcount++;
		}
		pov.endSection();
		if (normals) {
			pov.beginNormals(vcount);
			vItr = mesh.vItr();
			while (vItr.hasNext()) {
				pov.vertex(HE_MeshOp.getVertexNormal(vItr.next()));
			}
			pov.endSection();
		}
		final Iterator<HE_Face> fItr = mesh.fItr();
		pov.beginIndices(mesh.getNumberOfFaces());
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			pov.face(
					keyToIndex.get(f.getHalfedge().getVertex().getKey())
							+ vOffset,
					keyToIndex.get(f.getHalfedge().getNextInFace().getVertex()
							.getKey()) + vOffset,
					keyToIndex.get(f.getHalfedge().getPrevInFace().getVertex()
							.getKey()) + vOffset);
		}
		pov.endSection();
	}

	/**
	 * Saves the mesh as PovRAY format to the given PrintWriter.
	 *
	 * @param mesh
	 *            HE_Mesh
	 * @param pw
	 *            PrintWriter
	 * @deprecated Use {@link #saveAsPOV(HE_Mesh,PrintWriter)} instead
	 */
	@Deprecated
	public static void saveToPOV(final HE_Mesh mesh, final PrintWriter pw) {
		saveAsPOV(mesh, pw);
	}

	/**
	 * Saves the mesh as PovRAY format to the given PrintWriter.
	 *
	 * @param mesh
	 *            HE_Mesh
	 * @param pw
	 *            PrintWriter
	 */
	public static void saveAsPOV(final HE_Mesh mesh, final PrintWriter pw) {
		saveAsPOV(mesh, pw, true);
	}

	/**
	 * Saves the mesh as PovRAY format to the given PrintWriter.
	 *
	 * @param mesh
	 *            HE_Mesh
	 * @param pw
	 *            PrintWriter
	 * @param saveNormals
	 *            boolean (Smooth face or otherwise)
	 * @deprecated Use {@link #saveAsPOV(HE_Mesh,PrintWriter,boolean)} instead
	 */
	@Deprecated
	public static void saveToPOV(final HE_Mesh mesh, final PrintWriter pw,
			final boolean saveNormals) {
		saveAsPOV(mesh, pw, saveNormals);
	}

	/**
	 * Saves the mesh as PovRAY format to the given PrintWriter.
	 *
	 * @param mesh
	 *            HE_Mesh
	 * @param pw
	 *            PrintWriter
	 * @param saveNormals
	 *            boolean (Smooth face or otherwise)
	 */
	public static void saveAsPOV(final HE_Mesh mesh, final PrintWriter pw,
			final boolean saveNormals) {
		final HET_WriterPOV obj = new HET_WriterPOV();
		obj.beginSave(pw);
		saveAsPOV(mesh, obj, saveNormals);
		obj.endSave();
	}

	/**
	 *
	 */
	public static int	NONE		= -1;
	/**
	 *
	 */
	public static int	DEFAULT		= 0;
	/**
	 *
	 */
	public static int	MATERIALISE	= 1;

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsWRLWithFaceColor(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToWRLWithFaceColor(final HE_Mesh mesh,
			final String path, final String name) {
		saveAsWRLWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsWRLWithFaceColor(final HE_Mesh mesh,
			final String path, final String name) {
		HET_WriterWRL.saveMeshWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsWRLWithVertexColor(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToWRLWithVertexColor(final HE_Mesh mesh,
			final String path, final String name) {
		saveAsWRLWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsWRLWithVertexColor(final HE_Mesh mesh,
			final String path, final String name) {
		HET_WriterWRL.saveMeshWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsWRL(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToWRL(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsWRL(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsWRL(final HE_Mesh mesh, final String path,
			final String name) {
		HET_WriterWRL.saveMesh(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsPLY(HE_Mesh,String,String)} instead
	 */
	@Deprecated
	public static void saveToPLY(final HE_Mesh mesh, final String path,
			final String name) {
		saveAsPLY(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsPLY(final HE_Mesh mesh, final String path,
			final String name) {
		HET_WriterPLY.saveMesh(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsPLYWithVertexColor(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToPLYWithVertexColor(final HE_Mesh mesh,
			final String path, final String name) {
		saveAsPLYWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsPLYWithVertexColor(final HE_Mesh mesh,
			final String path, final String name) {
		HET_WriterPLY.saveMeshWithVertexColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 * @deprecated Use {@link #saveAsPLYWithFaceColor(HE_Mesh,String,String)}
	 *             instead
	 */
	@Deprecated
	public static void saveToPLYWithFaceColor(final HE_Mesh mesh,
			final String path, final String name) {
		saveAsPLYWithFaceColor(mesh, path, name);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsPLYWithFaceColor(final HE_Mesh mesh,
			final String path, final String name) {
		HET_WriterPLY.saveMeshWithFaceColor(mesh, path, name);
	}
}
