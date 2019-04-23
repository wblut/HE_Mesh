/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import wblut.geom.WB_Point;
import wblut.geom.WB_Triangle;

/**
 *
 */
public class HEC_FromBinarySTLFile extends HEC_Creator {

	/**
	 *
	 */
	private final byte[] buf = new byte[12];

	/**
	 *
	 */
	private String path;

	/**
	 *
	 */
	private double scale;

	/**
	 *
	 */
	public HEC_FromBinarySTLFile() {
		super();
		scale = 1;
		path = null;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param path
	 */
	public HEC_FromBinarySTLFile(final String path) {
		super();
		this.path = path;
		scale = 1;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public HEC_FromBinarySTLFile setPath(final String path) {
		this.path = path;
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public HEC_FromBinarySTLFile setScale(final double f) {
		scale = f;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	private final double bufferToDouble() {
		return Float.intBitsToFloat(bufferToInt());
	}

	/**
	 *
	 *
	 * @return
	 */
	private final int bufferToInt() {
		return byteToInt(buf[0]) | byteToInt(buf[1]) << 8 | byteToInt(buf[2]) << 16 | byteToInt(buf[3]) << 24;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	private final int byteToInt(final byte b) {
		return b < 0 ? 256 + b : b;
	}

	/**
	 *
	 *
	 * @param file
	 * @return
	 */
	private InputStream createInputStream(final File file) {
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

	/**
	 *
	 *
	 * @param ds
	 * @param result
	 * @return
	 * @throws IOException
	 */
	private WB_Point readVector(final DataInputStream ds, final WB_Point result) throws IOException {
		ds.read(buf, 0, 4);
		result.setX(scale * bufferToDouble());
		ds.read(buf, 0, 4);
		result.setY(scale * bufferToDouble());
		ds.read(buf, 0, 4);
		result.setZ(scale * bufferToDouble());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		final File file = new File(path);
		final InputStream stream = createInputStream(file);
		final ArrayList<WB_Triangle> triangles = new ArrayList<WB_Triangle>();
		try {
			final DataInputStream ds = new DataInputStream(new BufferedInputStream(stream, 0x8000));
			// read header, ignore color model
			for (int i = 0; i < 80; i++) {
				ds.read();
			}
			// read num faces
			ds.read(buf, 0, 4);
			final int numFaces = bufferToInt();

			for (int i = 0; i < numFaces; i++) {
				final WB_Point a = new WB_Point();
				final WB_Point b = new WB_Point();
				final WB_Point c = new WB_Point();
				// ignore face normal
				ds.read(buf, 0, 12);
				// face vertices
				readVector(ds, a);
				readVector(ds, b);
				readVector(ds, c);
				triangles.add(new WB_Triangle(a, b, c));
				// ignore colour
				ds.read(buf, 0, 2);
			}
			final HEC_FromTriangles ft = new HEC_FromTriangles();
			ft.setTriangles(triangles);
			return new HE_Mesh(ft);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
