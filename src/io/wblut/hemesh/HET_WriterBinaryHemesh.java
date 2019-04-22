/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

/**
 * Helper class for HE_Export.saveToBinaryHemesh.
 *
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class HET_WriterBinaryHemesh {
	/** The hemesh stream. */
	protected FileOutputStream hemeshStream;
	/** The hemesh writer. */
	protected DataOutputStream hemeshWriter;

	/**
	 * Begin save.
	 *
	 * @param stream
	 *            the stream
	 */
	public void beginSave(final FileOutputStream stream) {
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
	 * Begin save.
	 *
	 * @param fn
	 *            the fn
	 * @param name
	 */
	public void beginSave(final String fn, final String name) {
		try {
			final File file = new File(fn, name + ".binhemesh");
			createDirectories(file);
			hemeshStream = new FileOutputStream(file);
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
			hemeshWriter.flush();
			hemeshWriter.close();
			hemeshStream.flush();
			hemeshStream.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle begin save.
	 */
	protected void handleBeginSave() {
		hemeshWriter = new DataOutputStream(new DeflaterOutputStream(hemeshStream));
	}

	/**
	 *
	 *
	 * @param v
	 * @param heid
	 */
	public void vertex(final HE_Vertex v, final int heid) {
		try {
			hemeshWriter.writeDouble(v.xd());
			hemeshWriter.writeDouble(v.yd());
			hemeshWriter.writeDouble(v.zd());
			hemeshWriter.writeInt(heid);
			hemeshWriter.writeInt(v.getColor());
			hemeshWriter.writeInt(v.getInternalLabel());
			hemeshWriter.writeInt(v.getUserLabel());
			if (v.hasVertexUVW()) {
				hemeshWriter.writeInt(1);
				hemeshWriter.writeDouble(v.getVertexUVW().xd());
				hemeshWriter.writeDouble(v.getVertexUVW().yd());
				hemeshWriter.writeDouble(v.getVertexUVW().zd());
			} else {
				hemeshWriter.writeInt(0);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
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

		try {
			hemeshWriter.writeInt(vid);
			hemeshWriter.writeInt(henextid);
			hemeshWriter.writeInt(hepairid);
			hemeshWriter.writeInt(faceid);
			hemeshWriter.writeInt(he.getColor());
			hemeshWriter.writeInt(he.getInternalLabel());
			hemeshWriter.writeInt(he.getUserLabel());
			if (he.hasHalfedgeUVW()) {
				hemeshWriter.writeInt(1);
				hemeshWriter.writeDouble(he.getHalfedgeUVW().xd());
				hemeshWriter.writeDouble(he.getHalfedgeUVW().yd());
				hemeshWriter.writeDouble(he.getHalfedgeUVW().zd());
			} else {
				hemeshWriter.writeInt(0);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 *
	 * @param f
	 * @param heid
	 */
	public void face(final HE_Face f, final int heid) {
		try {
			hemeshWriter.writeInt(heid);
			hemeshWriter.writeInt(f.getColor());
			hemeshWriter.writeInt(f.getTextureId());
			hemeshWriter.writeInt(f.getInternalLabel());
			hemeshWriter.writeInt(f.getUserLabel());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sizes.
	 *
	 * @param v1
	 *            the v1
	 * @param v2
	 *            the v2
	 * @param v3
	 *            the v3
	 */
	public void sizes(final int v1, final int v2, final int v3) {
		try {
			hemeshWriter.writeInt(v1);
			hemeshWriter.writeInt(v2);
			hemeshWriter.writeInt(v3);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
