/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import wblut.geom.WB_Coord;

/**
 * Helper class for HE_Export.saveToSimpleMesh.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class HET_WriterSimpleMesh {
	/** The simple mesh stream. */
	protected OutputStream simpleMeshStream;
	/** The simple mesh writer. */
	protected PrintWriter simpleMeshWriter;

	/**
	 * Begin save.
	 *
	 * @param stream
	 *            the stream
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
	 * Begin save.
	 *
	 * @param fn
	 *            the fn
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
	 * End save.
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
	 * Faces.
	 *
	 * @param f
	 *            the f
	 */
	public void faces(final int[][] f) {
		int i = 0;
		for (i = 0; i < f.length; i++) {
			face(f[i]);
		}
	}

	/**
	 * Face.
	 *
	 * @param f
	 *            the f
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
	 * Handle begin save.
	 */
	protected void handleBeginSave() {
		simpleMeshWriter = new PrintWriter(simpleMeshStream);
	}

	/**
	 * Vertices.
	 *
	 * @param v
	 *            the v
	 */
	public void vertices(final WB_Coord[] v) {
		int i = 0;
		for (i = 0; i < v.length; i++) {
			simpleMeshWriter.println(v[i].xd() + " " + v[i].yd() + " " + v[i].zd());
		}
	}

	public void vertices(final List<? extends WB_Coord> v) {
		for (int i = 0; i < v.size(); i++) {
			WB_Coord p = v.get(i);
			simpleMeshWriter.println(p.xd() + " " + p.yd() + " " + p.zd());
		}
	}

	/**
	 * Int value.
	 *
	 * @param v
	 *            the v
	 */
	public void intValue(final int v) {
		simpleMeshWriter.println(v);
	}
}
