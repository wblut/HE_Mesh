package wblut.hemesh;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

class HET_WriterBinaryHemesh {
	protected FileOutputStream hemeshStream;
	protected DataOutputStream hemeshWriter;

	public void beginSave(final FileOutputStream stream) {
		try {
			hemeshStream = stream;
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
			final File file = new File(fn, name + ".binhemesh");
			createDirectories(file);
			hemeshStream = new FileOutputStream(file);
			handleBeginSave();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

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

	protected void handleBeginSave() {
		hemeshWriter = new DataOutputStream(new DeflaterOutputStream(hemeshStream));
	}

	public void vertex(final HE_Vertex v, final int heid) {
		try {
			hemeshWriter.writeDouble(v.xd());
			hemeshWriter.writeDouble(v.yd());
			hemeshWriter.writeDouble(v.zd());
			hemeshWriter.writeInt(heid);
			hemeshWriter.writeInt(v.getColor());
			hemeshWriter.writeInt(v.getInternalLabel());
			hemeshWriter.writeInt(v.getLabel());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void halfedge(final HE_Halfedge he, final int vid, final int henextid, final int hepairid,
			final int faceid) {
		try {
			hemeshWriter.writeInt(vid);
			hemeshWriter.writeInt(henextid);
			hemeshWriter.writeInt(hepairid);
			hemeshWriter.writeInt(faceid);
			hemeshWriter.writeInt(he.getColor());
			hemeshWriter.writeInt(he.getInternalLabel());
			hemeshWriter.writeInt(he.getLabel());
			if (he.hasUVW()) {
				hemeshWriter.writeInt(1);
				hemeshWriter.writeDouble(he.getUVW().xd());
				hemeshWriter.writeDouble(he.getUVW().yd());
				hemeshWriter.writeDouble(he.getUVW().zd());
			} else {
				hemeshWriter.writeInt(0);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void face(final HE_Face f, final int heid) {
		try {
			hemeshWriter.writeInt(heid);
			hemeshWriter.writeInt(f.getColor());
			hemeshWriter.writeInt(f.getTextureId());
			hemeshWriter.writeInt(f.getInternalLabel());
			hemeshWriter.writeInt(f.getLabel());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

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
