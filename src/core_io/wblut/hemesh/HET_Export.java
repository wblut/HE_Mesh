package wblut.hemesh;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

public class HET_Export {
	public static void saveAsOBJ(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	public static void saveAsOBJNoNormals(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	public static void saveAsOBJWithFaceColor(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	public static void saveAsOBJWithVertexColor(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	public static void saveAsOBJ(final Collection<? extends HE_Mesh> mesh, final String path, final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	public static void saveAsOBJ(final HE_MeshCollection mesh, final String path, final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	public static void saveAsOBJNoNormals(final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	public static void saveAsOBJNoNormals(final HE_MeshCollection mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	public static void saveAsOBJWithFaceColor(final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	public static void saveAsOBJWithFaceColor(final HE_MeshCollection mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	public static void saveAsOBJWithVertexColor(final Collection<? extends HE_Mesh> mesh, final String path,
			final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	public static void saveAsOBJWithVertexColor(final HE_MeshCollection mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	public static void saveAsOBJ(final HE_Mesh[] mesh, final String path, final String name) {
		HET_WriterOBJ.saveMesh(mesh, path, name);
	}

	public static void saveAsOBJNoNormals(final HE_Mesh[] mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshNN(mesh, path, name);
	}

	public static void saveAsOBJWithFaceColor(final HE_Mesh[] mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshWithFaceColor(mesh, path, name);
	}

	public static void saveAsOBJWithVertexColor(final HE_Mesh[] mesh, final String path, final String name) {
		HET_WriterOBJ.saveMeshWithVertexColor(mesh, path, name);
	}

	public static void saveAsSTL(final HE_Mesh mesh, final String path, final String name) {
		saveAsSTLWithFaceColor(mesh, path, name, NONE);
	}

	public static void saveAsSTLWithFaceColor(final HE_Mesh mesh, final String path, final String name,
			final int colormodel) {
		final HET_WriterSTL stl = new HET_WriterSTL(colormodel == 1 ? HET_WriterSTL.MATERIALISE
				: colormodel == 0 ? HET_WriterSTL.DEFAULT : HET_WriterSTL.NONE, HET_WriterSTL.DEFAULT_BUFFER);
		stl.beginSave(path, name, mesh.getNumberOfFaces());
		saveAsSTLWithFaceColor(mesh, stl);
		stl.endSave();
	}

	public static void saveAsSTLWithFaceColor(final HE_Mesh mesh, final HET_WriterSTL stl) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			stl.face(f.getHalfedge().getVertex(), f.getHalfedge().getNextInFace().getVertex(),
					f.getHalfedge().getPrevInFace().getVertex(), HE_MeshOp.getFaceNormal(f), f.getColor());
		}
	}

	public static void saveAsHemesh(final HE_Mesh mesh, final String path, final String name) {
		final HET_WriterHemesh hem = new HET_WriterHemesh();
		hem.beginSave(path, name);
		final HE_IntMap vertexKeys = new HE_IntMap();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		int i = 0;
		while (vItr.hasNext()) {
			vertexKeys.put(vItr.next().getKey(), i);
			i++;
		}
		final HE_IntMap halfedgeKeys = new HE_IntMap();
		Iterator<HE_Halfedge> heItr = mesh.heItr();
		i = 0;
		while (heItr.hasNext()) {
			halfedgeKeys.put(heItr.next().getKey(), i);
			i++;
		}
		final HE_IntMap faceKeys = new HE_IntMap();
		HE_FaceIterator fItr = mesh.fItr();
		i = 0;
		while (fItr.hasNext()) {
			faceKeys.put(fItr.next().getKey(), i);
			i++;
		}
		hem.sizes(mesh.getNumberOfVertices(), mesh.getNumberOfHalfedges(), mesh.getNumberOfFaces());
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

	public static void saveAsBinaryHemesh(final HE_Mesh mesh, final String path, final String name) {
		final HET_WriterBinaryHemesh hem = new HET_WriterBinaryHemesh();
		hem.beginSave(path, name);
		final HE_IntMap vertexKeys = new HE_IntMap();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		int i = 0;
		while (vItr.hasNext()) {
			vertexKeys.put(vItr.next().getKey(), i);
			i++;
		}
		final HE_IntMap halfedgeKeys = new HE_IntMap();
		Iterator<HE_Halfedge> heItr = mesh.heItr();
		i = 0;
		while (heItr.hasNext()) {
			halfedgeKeys.put(heItr.next().getKey(), i);
			i++;
		}
		final HE_IntMap faceKeys = new HE_IntMap();
		HE_FaceIterator fItr = mesh.fItr();
		i = 0;
		while (fItr.hasNext()) {
			faceKeys.put(fItr.next().getKey(), i);
			i++;
		}
		hem.sizes(mesh.getNumberOfVertices(), mesh.getNumberOfHalfedges(), mesh.getNumberOfFaces());
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

	public static void saveAsPOV(final HE_Mesh mesh, final String path, final String name) {
		saveAsPOV(mesh, path, name, true);
	}

	public static void saveAsPOV(final HE_Mesh mesh, final String path, final String name, final boolean saveNormals) {
		final HET_WriterPOV obj = new HET_WriterPOV();
		obj.beginSave(path, name);
		saveAsPOV(mesh, obj, saveNormals);
		obj.endSave();
	}

	public static void saveAsPOV(final HE_Mesh mesh, final HET_WriterPOV pov, final boolean normals) {
		final int vOffset = pov.getCurrVertexOffset();
		pov.beginMesh2(String.format("obj%d", mesh.getKey()));
		final HE_IntMap keyToIndex = new HE_IntMap();
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
		final HE_FaceIterator fItr = mesh.fItr();
		pov.beginIndices(mesh.getNumberOfFaces());
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			pov.face(keyToIndex.get(f.getHalfedge().getVertex().getKey()) + vOffset,
					keyToIndex.get(f.getHalfedge().getNextInFace().getVertex().getKey()) + vOffset,
					keyToIndex.get(f.getHalfedge().getPrevInFace().getVertex().getKey()) + vOffset);
		}
		pov.endSection();
	}

	public static void saveAsPOV(final HE_Mesh mesh, final PrintWriter pw) {
		saveAsPOV(mesh, pw, true);
	}

	public static void saveAsPOV(final HE_Mesh mesh, final PrintWriter pw, final boolean saveNormals) {
		final HET_WriterPOV obj = new HET_WriterPOV();
		obj.beginSave(pw);
		saveAsPOV(mesh, obj, saveNormals);
		obj.endSave();
	}

	public static int NONE = -1;
	public static int DEFAULT = 0;
	public static int MATERIALISE = 1;

	public static void saveAsWRLWithFaceColor(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterWRL.saveMeshWithFaceColor(mesh, path, name);
	}

	public static void saveAsWRLWithVertexColor(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterWRL.saveMeshWithVertexColor(mesh, path, name);
	}

	public static void saveAsWRL(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterWRL.saveMesh(mesh, path, name);
	}

	public static void saveAsPLY(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterPLY.saveMesh(mesh, path, name);
	}

	public static void saveAsPLYWithVertexColor(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterPLY.saveMeshWithVertexColor(mesh, path, name);
	}

	public static void saveAsPLYWithFaceColor(final HE_Mesh mesh, final String path, final String name) {
		HET_WriterPLY.saveMeshWithFaceColor(mesh, path, name);
	}
}
