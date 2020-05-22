package wblut.geom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import wblut.hemesh.HE_Mesh;

/**
 *
 */
public class WB_SimpleMeshWriter {
	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveAsSimpleMesh(final HE_Mesh mesh, final String path, final String name) {
		final Writer hem = new Writer();
		hem.beginSave(path, name);
		final WB_CoordCollection points = mesh.getVerticesAsCoord();
		hem.intValue(mesh.getNumberOfVertices());
		hem.vertices(points);
		final int[][] faces = mesh.getFacesAsInt();
		hem.intValue(mesh.getNumberOfFaces());
		hem.faces(faces);
		hem.endSave();
	}

	/**
	 *
	 */
	static class Writer {
		/**  */
		protected OutputStream simpleMeshStream;
		/**  */
		protected PrintWriter simpleMeshWriter;

		/**
		 *
		 *
		 * @param stream
		 */
		public void beginSave(final OutputStream stream) {
			try {
				simpleMeshStream = stream;
				handleBeginSave();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 *
		 *
		 * @param file
		 */
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

		/**
		 *
		 *
		 * @param fn
		 * @param name
		 */
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

		/**
		 *
		 */
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

		/**
		 *
		 *
		 * @param f
		 */
		public void faces(final int[][] f) {
			int i = 0;
			for (i = 0; i < f.length; i++) {
				face(f[i]);
			}
		}

		/**
		 *
		 *
		 * @param f
		 */
		public void face(final int[] f) {
			int i = 0;
			final int fl = f.length;
			simpleMeshWriter.print(fl + " ");
			for (i = 0; i < fl - 1; i++) {
				simpleMeshWriter.print(f[i] + " ");
			}
			simpleMeshWriter.println(f[i]);
		}

		/**
		 *
		 */
		protected void handleBeginSave() {
			simpleMeshWriter = new PrintWriter(simpleMeshStream);
		}

		/**
		 *
		 *
		 * @param v
		 */
		public void vertices(final WB_Coord[] v) {
			int i = 0;
			for (i = 0; i < v.length; i++) {
				simpleMeshWriter.println(v[i].xd() + " " + v[i].yd() + " " + v[i].zd());
			}
		}

		/**
		 *
		 *
		 * @param v
		 */
		public void vertices(final WB_CoordCollection v) {
			for (int i = 0; i < v.size(); i++) {
				simpleMeshWriter.println(v.getX(i) + " " + v.getY(i) + " " + v.getZ(i));
			}
		}

		/**
		 *
		 *
		 * @param v
		 */
		public void intValue(final int v) {
			simpleMeshWriter.println(v);
		}
	}
}
