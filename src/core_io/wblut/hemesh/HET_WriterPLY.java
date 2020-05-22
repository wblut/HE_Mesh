package wblut.hemesh;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_VectorCollection;

/**
 *
 */
class HET_WriterPLY {
	/**  */
	private static final byte[] buf = new byte[4];

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	private static final byte[] le(final float f) {
		return le(Float.floatToRawIntBits(f));
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	private static final byte[] le(final int i) {
		buf[3] = (byte) (i >>> 24);
		buf[2] = (byte) (i >> 16 & 0xff);
		buf[1] = (byte) (i >> 8 & 0xff);
		buf[0] = (byte) (i & 0xff);
		return buf;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param stream
	 */
	public static void saveMesh(final HE_Mesh mesh, final OutputStream stream) {
		try {
			final BufferedOutputStream out = new BufferedOutputStream(stream, 0x20000);
			out.write("ply\n".getBytes());
			out.write("format binary_little_endian 1.0\n".getBytes());
			out.write(("element vertex " + mesh.getNumberOfVertices() + "\n").getBytes());
			out.write("property float x\n".getBytes());
			out.write("property float y\n".getBytes());
			out.write("property float z\n".getBytes());
			out.write("property float nx\n".getBytes());
			out.write("property float ny\n".getBytes());
			out.write("property float nz\n".getBytes());
			out.write(("element face " + mesh.getNumberOfFaces() + "\n").getBytes());
			out.write("property list uchar uint vertex_indices\n".getBytes());
			out.write("end_header\n".getBytes());
			final WB_CoordCollection verts = mesh.getPoints();
			final WB_VectorCollection normals = mesh.getVertexNormals();
			int i = 0;
			try {
				for (i = 0; i < verts.size(); i++) {
					final WB_Coord v = verts.get(i);
					out.write(le(v.xf()));
					out.write(le(v.yf()));
					out.write(le(v.zf()));
					out.write(le((float) normals.getX(i)));
					out.write(le((float) normals.getY(i)));
					out.write(le((float) normals.getZ(i)));
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			final int[][] faces = mesh.getFacesAsInt();
			for (final int[] f : faces) {
				out.write((byte) 3);
				out.write(le(f[0]));
				out.write(le(f[1]));
				out.write(le(f[2]));
			}
			out.flush();
			out.close();
		} catch (final IOException e) {
		}
	}

	/**
	 *
	 *
	 * @param what
	 * @return
	 */
	public static final int red(final int what) {
		return what >> 16 & 0xff;
	}

	/**
	 *
	 *
	 * @param what
	 * @return
	 */
	public static final int green(final int what) {
		return what >> 8 & 0xff;
	}

	/**
	 *
	 *
	 * @param what
	 * @return
	 */
	public static final int blue(final int what) {
		return what & 0xff;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveMesh(final HE_Mesh mesh, final String path, final String name) {
		try {
			saveMesh(mesh, createOutputStream(new File(path, name + ".ply")));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	static public OutputStream createOutputStream(final File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("file can't be null");
		}
		createDirectories(file);
		OutputStream stream = new FileOutputStream(file);
		if (file.getName().toLowerCase().endsWith(".gz")) {
			stream = new GZIPOutputStream(stream);
		}
		return stream;
	}

	/**
	 *
	 *
	 * @param file
	 */
	static public void createDirectories(final File file) {
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
	 * @param mesh
	 * @param stream
	 */
	public static void saveMeshWithVertexColor(final HE_Mesh mesh, final OutputStream stream) {
		try {
			final BufferedOutputStream out = new BufferedOutputStream(stream, 0x20000);
			out.write("ply\n".getBytes());
			out.write("format ascii 1.0\n".getBytes());
			out.write(("element vertex " + mesh.getNumberOfVertices() + "\n").getBytes());
			out.write("property float x\n".getBytes());
			out.write("property float y\n".getBytes());
			out.write("property float z\n".getBytes());
			out.write("property float nx\n".getBytes());
			out.write("property float ny\n".getBytes());
			out.write("property float nz\n".getBytes());
			out.write("property uchar red\n".getBytes());
			out.write("property uchar green\n".getBytes());
			out.write("property uchar blue\n".getBytes());
			out.write("property uchar alpha\n".getBytes());
			out.write(("element face " + mesh.getNumberOfFaces() + "\n").getBytes());
			out.write("property list uchar uint vertex_indices\n".getBytes());
			out.write("end_header\n".getBytes());
			final WB_CoordCollection verts = mesh.getPoints();
			final WB_VectorCollection normals = mesh.getVertexNormals();
			int i = 0;
			try {
				final HE_VertexIterator vitr = mesh.vItr();
				for (i = 0; i < verts.size(); i++) {
					final int c = vitr.next().getColor();
					final WB_Coord v = verts.get(i);
					out.write(("" + v.xf()).getBytes());
					out.write((" " + v.yf()).getBytes());
					out.write((" " + v.zf()).getBytes());
					out.write((" " + (float) normals.getX(i)).getBytes());
					out.write((" " + (float) normals.getY(i)).getBytes());
					out.write((" " + (float) normals.getZ(i)).getBytes());
					out.write((" " + red(c)).getBytes());
					out.write((" " + green(c)).getBytes());
					out.write((" " + blue(c)).getBytes());
					out.write((" " + 255 + "\n").getBytes());
				}
				final int[][] faces = mesh.getFacesAsInt();
				for (final int[] f : faces) {
					out.write(("3 " + f[0]).getBytes());
					out.write((" " + f[1]).getBytes());
					out.write((" " + f[2] + "\n").getBytes());
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			out.flush();
			out.close();
		} catch (final IOException e) {
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveMeshWithVertexColor(final HE_Mesh mesh, final String path, final String name) {
		try {
			saveMeshWithVertexColor(mesh, createOutputStream(new File(path, name + ".ply")));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param stream
	 */
	public static void saveMeshWithFaceColor(final HE_Mesh mesh, final OutputStream stream) {
		try {
			final BufferedOutputStream out = new BufferedOutputStream(stream, 0x20000);
			out.write("ply\n".getBytes());
			out.write("format ascii 1.0\n".getBytes());
			out.write(("element vertex " + mesh.getNumberOfVertices() + "\n").getBytes());
			out.write("property float x\n".getBytes());
			out.write("property float y\n".getBytes());
			out.write("property float z\n".getBytes());
			out.write("property float nx\n".getBytes());
			out.write("property float ny\n".getBytes());
			out.write("property float nz\n".getBytes());
			out.write(("element face " + mesh.getNumberOfFaces() + "\n").getBytes());
			out.write("property list uchar uint vertex_indices\n".getBytes());
			out.write("property uchar red\n".getBytes());
			out.write("property uchar green\n".getBytes());
			out.write("property uchar blue\n".getBytes());
			out.write("property uchar alpha\n".getBytes());
			out.write("end_header\n".getBytes());
			final WB_CoordCollection verts = mesh.getPoints();
			final WB_VectorCollection normals = mesh.getVertexNormals();
			int i = 0;
			try {
				for (i = 0; i < verts.size(); i++) {
					final WB_Coord v = verts.get(i);
					out.write(("" + v.xf()).getBytes());
					out.write((" " + v.yf()).getBytes());
					out.write((" " + v.zf()).getBytes());
					out.write((" " + (float) normals.getX(i)).getBytes());
					out.write((" " + (float) normals.getY(i)).getBytes());
					out.write((" " + (float) normals.getZ(i) + "\n").getBytes());
				}
				final int[][] faces = mesh.getFacesAsInt();
				final HE_FaceIterator fitr = mesh.fItr();
				for (final int[] f : faces) {
					final int c = fitr.next().getColor();
					out.write(("3 " + f[0]).getBytes());
					out.write((" " + f[1]).getBytes());
					out.write((" " + f[2]).getBytes());
					out.write((" " + red(c)).getBytes());
					out.write((" " + green(c)).getBytes());
					out.write((" " + blue(c)).getBytes());
					out.write((" " + 255 + "\n").getBytes());
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			out.flush();
			out.close();
		} catch (final IOException e) {
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param path
	 * @param name
	 */
	public static void saveMeshWithFaceColor(final HE_Mesh mesh, final String path, final String name) {
		try {
			saveMeshWithFaceColor(mesh, createOutputStream(new File(path, name + ".ply")));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
