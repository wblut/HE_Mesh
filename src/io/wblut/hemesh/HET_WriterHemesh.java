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

/**
 *
 */
public class HET_WriterHemesh {

	/**
	 *
	 */
	protected OutputStream hemeshStream;

	/**
	 *
	 */
	protected PrintWriter hemeshWriter;

	/**
	 *
	 *
	 * @param stream
	 */
	public void beginSave(final OutputStream stream) {
		try {
			hemeshStream = stream;
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
			final File file = new File(fn, name + ".hemesh");
			createDirectories(file);
			hemeshStream = new FileOutputStream(file);
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
			hemeshWriter.flush();
			hemeshWriter.close();
			hemeshStream.flush();
			hemeshStream.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	protected void handleBeginSave() {
		hemeshWriter = new PrintWriter(hemeshStream);
	}

	/**
	 *
	 *
	 * @param v
	 * @param heid
	 */
	public void vertex(final HE_Vertex v, final int heid) {
		String s = v.xd() + " " + v.yd() + " " + v.zd() + " " + heid + " " + v.getColor() + " " + v.getInternalLabel()
				+ " " + v.getUserLabel();
		if (v.hasVertexUVW()) {
			s += " 1 " + v.getVertexUVW().xd() + " " + v.getVertexUVW().yd() + " " + v.getVertexUVW().zd();

		} else {

			s += " 0";
		}

		hemeshWriter.println(s);
	}

	/**
	 *
	 *
	 * @param he
	 * @param vid
	 * @param henextid
	 * @param hepairid
	 * @param faceid
	 */
	public void halfedge(final HE_Halfedge he, final int vid, final int henextid, final int hepairid,
			final int faceid) {
		String s = vid + " " + henextid + " " + hepairid + " " + faceid + " " + he.getColor() + " "
				+ he.getInternalLabel() + " " + he.getUserLabel();
		if (he.hasHalfedgeUVW()) {
			s += " 1 " + he.getHalfedgeUVW().xd() + " " + he.getHalfedgeUVW().yd() + " " + he.getHalfedgeUVW().zd();

		} else {

			s += " 0";
		}
		hemeshWriter.println(s);
	}

	/**
	 *
	 *
	 * @param f
	 * @param heid
	 */
	public void face(final HE_Face f, final int heid) {
		hemeshWriter.println(heid + " " + f.getColor() + " " + f.getTextureId() + " " + f.getInternalLabel() + " "
				+ f.getUserLabel());
	}

	/**
	 *
	 *
	 * @param v1
	 * @param v2
	 * @param v3
	 */
	public void sizes(final int v1, final int v2, final int v3) {
		hemeshWriter.println(v1 + " " + v2 + " " + v3);
	}
}
