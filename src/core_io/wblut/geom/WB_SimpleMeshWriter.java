package wblut.geom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import wblut.hemesh.HE_Mesh;

public class WB_SimpleMeshWriter {
	public static void saveAsSimpleMesh(final HE_Mesh mesh, final String path, final String name) {
		final Writer hem = new Writer();
		hem.beginSave(path, name);
		final List<WB_Coord> points = mesh.getVerticesAsCoord();
		hem.intValue(mesh.getNumberOfVertices());
		hem.vertices(points);
		final int[][] faces = mesh.getFacesAsInt();
		hem.intValue(mesh.getNumberOfFaces());
		hem.faces(faces);
		hem.endSave();
	}

	static class Writer {
		protected OutputStream simpleMeshStream;
		protected PrintWriter simpleMeshWriter;

		public void beginSave(final OutputStream stream) {
			try {
				simpleMeshStream = stream;
				handleBeginSave();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		static private void createDirectories(final File file) {
			try {
				final String parentName = file.getParent();
				if (parentName != null) {
					final File parent = new File(parentName);
					if (!parent.exists()) {
						parent.mkdirs();
					}
				}
			} catch (final SecurityException se) {
				System.err.println("No permissions to create " + file.getAbsolutePath());
			}
		}

		public void beginSave(final String fn, final String name) {
			try {
				final File file = new File(fn, name + ".txt");
				createDirectories(file);
				simpleMeshStream = new FileOutputStream(file);
				handleBeginSave();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		public void endSave() {
			try {
				simpleMeshWriter.flush();
				simpleMeshWriter.close();
				simpleMeshStream.flush();
				simpleMeshStream.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		public void faces(final int[][] f) {
			int i = 0;
			for (i = 0; i < f.length; i++) {
				face(f[i]);
			}
		}

		public void face(final int[] f) {
			int i = 0;
			final int fl = f.length;
			simpleMeshWriter.print(fl + " ");
			for (i = 0; i < fl - 1; i++) {
				simpleMeshWriter.print(f[i] + " ");
			}
			simpleMeshWriter.println(f[i]);
		}

		protected void handleBeginSave() {
			simpleMeshWriter = new PrintWriter(simpleMeshStream);
		}

		public void vertices(final WB_Coord[] v) {
			int i = 0;
			for (i = 0; i < v.length; i++) {
				simpleMeshWriter.println(v[i].xd() + " " + v[i].yd() + " " + v[i].zd());
			}
		}

		public void vertices(final List<? extends WB_Coord> v) {
			for (int i = 0; i < v.size(); i++) {
				final WB_Coord p = v.get(i);
				simpleMeshWriter.println(p.xd() + " " + p.yd() + " " + p.zd());
			}
		}

		public void intValue(final int v) {
			simpleMeshWriter.println(v);
		}
	}
}
