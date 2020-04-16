package wblut.hemesh;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

public class HEC_Beethoven extends HEC_Creator {
	@Override
	protected HE_Mesh createBase() {
		final InputStream is = this.getClass().getClassLoader().getResourceAsStream("resources/beethoven.binhemesh");
		if (is == null) {
			return new HE_Mesh();
		}
		final HE_Mesh mesh = new HE_Mesh();
		try {
			final DataInputStream dis = new DataInputStream(new InflaterInputStream(is));
			final int numVertices = dis.readInt();
			final int numHalfedges = dis.readInt();
			final int numFaces = dis.readInt();
			final HE_VertexList vertices = new HE_VertexList();
			for (int i = 0; i < numVertices; i++) {
				vertices.add(new HE_Vertex());
			}
			final HE_HalfedgeList halfedges = new HE_HalfedgeList();
			for (int i = 0; i < numHalfedges; i++) {
				halfedges.add(new HE_Halfedge());
			}
			final HE_FaceList faces = new HE_FaceList();
			for (int i = 0; i < numFaces; i++) {
				faces.add(new HE_Face());
			}
			double x, y, z;
			int heid, vid, henextid, hepairid, fid, hasuvw;
			HE_Vertex v;
			for (int i = 0; i < numVertices; i++) {
				v = vertices.get(i);
				x = dis.readDouble();
				y = dis.readDouble();
				z = dis.readDouble();
				heid = dis.readInt();
				v.setColor(dis.readInt());
				v.setInternalLabel(dis.readInt());
				v.setLabel(dis.readInt());
				hasuvw = dis.readInt();
				v.set(x, y, z);
				if (heid > -1) {
					mesh.setHalfedge(v, halfedges.get(heid));
				}
			}
			HE_Halfedge he;
			for (int i = 0; i < numHalfedges; i++) {
				he = halfedges.get(i);
				vid = dis.readInt();
				henextid = dis.readInt();
				hepairid = dis.readInt();
				fid = dis.readInt();
				he.setColor(dis.readInt());
				he.setInternalLabel(dis.readInt());
				he.setLabel(dis.readInt());
				hasuvw = dis.readInt();
				if (vid > -1) {
					mesh.setVertex(he, vertices.get(vid));
				}
				if (henextid > -1) {
					mesh.setNext(he, halfedges.get(henextid));
				}
				if (hepairid > -1) {
					he.setPair(halfedges.get(hepairid));
					halfedges.get(hepairid).setPair(he);
				}
				if (fid > -1) {
					mesh.setFace(he, faces.get(fid));
				}
				if (hasuvw == 1) {
					he.setUVW(dis.readDouble(), dis.readDouble(), dis.readDouble());
				}
			}
			HE_Face f;
			for (int i = 0; i < numFaces; i++) {
				f = faces.get(i);
				heid = dis.readInt();
				if (heid > -1) {
					mesh.setHalfedge(f, halfedges.get(heid));
				}
				f.setColor(dis.readInt());
				f.setTextureId(dis.readInt());
				f.setInternalLabel(dis.readInt());
				f.setLabel(dis.readInt());
			}
			dis.close();
			mesh.addVertices(vertices);
			mesh.addHalfedges(halfedges);
			mesh.addFaces(faces);
			mesh.rotateAboutAxisSelf(2 * Math.PI / 3, 0, 0, 0, 0, 0, 1);
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		HE_MeshOp.improveTriangulation(mesh);
		return mesh;
	}
}
