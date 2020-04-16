package wblut.geom;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class WB_FromBinarySTLFile {
	private WB_FromBinarySTLFile() {
	}

	private static final double bufferToDouble(final byte[] buf) {
		return Float.intBitsToFloat(bufferToInt(buf));
	}

	private static final int bufferToInt(final byte[] buf) {
		return byteToInt(buf[0]) | byteToInt(buf[1]) << 8 | byteToInt(buf[2]) << 16 | byteToInt(buf[3]) << 24;
	}

	private static final int byteToInt(final byte b) {
		return b < 0 ? 256 + b : b;
	}

	private static InputStream createInputStream(final File file) {
		if (file == null) {
			throw new IllegalArgumentException("file can't be null");
		}
		try {
			InputStream stream = new FileInputStream(file);
			if (file.getName().toLowerCase().endsWith(".gz")) {
				stream = new GZIPInputStream(stream);
			}
			return stream;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static WB_Point readVector(final DataInputStream ds, final WB_Point result, final double scale,
			final byte[] buf) throws IOException {
		ds.read(buf, 0, 4);
		result.setX(scale * bufferToDouble(buf));
		ds.read(buf, 0, 4);
		result.setY(scale * bufferToDouble(buf));
		ds.read(buf, 0, 4);
		result.setZ(scale * bufferToDouble(buf));
		return result;
	}

	public static WB_SimpleMesh createMesh(final String path, final double scale) {
		final byte[] buf = new byte[12];
		final File file = new File(path);
		final InputStream stream = createInputStream(file);
		final ArrayList<WB_Triangle> triangles = new ArrayList<>();
		try {
			final DataInputStream ds = new DataInputStream(new BufferedInputStream(stream, 0x8000));
			// read header, ignore color model
			for (int i = 0; i < 80; i++) {
				ds.read();
			}
			// read num faces
			ds.read(buf, 0, 4);
			final int numFaces = bufferToInt(buf);
			for (int i = 0; i < numFaces; i++) {
				final WB_Point a = new WB_Point();
				final WB_Point b = new WB_Point();
				final WB_Point c = new WB_Point();
				// ignore face normal
				ds.read(buf, 0, 12);
				// face vertices
				readVector(ds, a, scale, buf);
				readVector(ds, b, scale, buf);
				readVector(ds, c, scale, buf);
				triangles.add(new WB_Triangle(a, b, c));
				// ignore colour
				ds.read(buf, 0, 2);
			}
			final WB_Coord[] vertices = new WB_Point[triangles.size() * 3];
			final int[][] faces = new int[triangles.size()][3];
			for (int i = 0; i < triangles.size(); i++) {
				vertices[3 * i] = triangles.get(i).p1();
				vertices[3 * i + 1] = triangles.get(i).p2();
				vertices[3 * i + 2] = triangles.get(i).p3();
				faces[i][0] = 3 * i;
				faces[i][1] = 3 * i + 1;
				faces[i][2] = 3 * i + 2;
			}
			return new WB_SimpleMesh(vertices, faces);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
