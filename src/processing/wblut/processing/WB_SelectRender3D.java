/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.processing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.opengl.PGraphics3D;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_HalfedgeStructure;
import wblut.hemesh.HE_Vertex;

/**
 *
 */
public class WB_SelectRender3D {

	/**
	 *
	 */
	private final PApplet home;

	/**
	 *
	 */
	private final PGraphics3D selector;

	/**
	 *
	 */
	private final int[] samples;

	/**
	 *
	 */

	/** The current_color. */
	protected int currentColor;
	/** The color to object. */
	protected HashMap<Integer, Long> colorToObject;

	/**
	 *
	 */
	private final double scale;

	/**
	 *
	 *
	 * @param home
	 */
	public WB_SelectRender3D(final PApplet home) {
		scale = 1;// doesn't work yet
		selector = (PGraphics3D) home.createGraphics((int) (home.width * scale), (int) (home.height * scale),
				PConstants.P3D);
		selector.beginDraw();
		selector.background(0);
		selector.noLights();
		selector.endDraw();
		this.home = home;
		colorToObject = new HashMap<Integer, Long>();
		currentColor = -16777216;
		samples = new int[5];
	}

	/**
	 * Draw one face.
	 *
	 * @param f
	 *            face
	 */
	private void drawFace(final HE_Face f) {
		if (f.getFaceDegree() > 2) {
			final int[] tris = f.getTriangles();
			final List<HE_Vertex> vertices = f.getFaceVertices();
			HE_Vertex v0, v1, v2;

			for (int i = 0; i < tris.length; i += 3) {

				selector.beginShape(PConstants.TRIANGLES);
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				selector.vertex(v0.xf(), v0.yf(), v0.zf());
				selector.vertex(v1.xf(), v1.yf(), v1.zf());
				selector.vertex(v2.xf(), v2.yf(), v2.zf());
				selector.endShape();
			}
		}
	}

	/**
	 * Draw mesh faces. Typically used with noStroke();
	 *
	 * @param mesh
	 *            the mesh
	 * @return key of face at mouse position
	 */
	public void drawFaces(final HE_HalfedgeStructure mesh) {
		selector.beginDraw();
		selector.setMatrix(home.getMatrix());
		selector.scale((float) scale);
		selector.clear();
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		selector.strokeWeight(1.0f);
		while (fItr.hasNext()) {
			f = fItr.next();
			setKey(f.getKey());
			drawFace(f);
		}
		selector.endDraw();
	}

	/**
	 * Draw mesh edges.
	 *
	 * @param mesh
	 *            the mesh
	 * @param d
	 * @return key of edge at mouse position
	 */
	public void drawEdges(final HE_HalfedgeStructure mesh, final double d) {
		selector.beginDraw();
		selector.setMatrix(home.getMatrix());
		selector.scale((float) scale);
		selector.clear();
		selector.strokeWeight((float) d);
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			setKey(e.getKey());
			selector.line(e.getVertex().xf(), e.getVertex().yf(), e.getVertex().zf(), e.getEndVertex().xf(),
					e.getEndVertex().yf(), e.getEndVertex().zf());
		}
		selector.endDraw();
	}

	/**
	 * Draw mesh vertices as box.
	 *
	 * @param mesh
	 *            the mesh
	 * @param d
	 *            size of box
	 * @return key of vertex at mouse position
	 */
	public void drawVertices(final HE_HalfedgeStructure mesh, final double d) {
		selector.beginDraw();
		selector.setMatrix(home.getMatrix());
		selector.scale((float) scale);
		selector.clear();
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		selector.strokeWeight(1.0f);
		while (vItr.hasNext()) {
			v = vItr.next();
			setKey(v.getKey());
			selector.pushMatrix();
			selector.translate(v.xf(), v.yf(), v.zf());
			selector.box((float) d);
			selector.popMatrix();
		}
		selector.endDraw();
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public long getKeyAA(final int x, final int y) {
		final int locx = (int) (x * scale);
		final int locy = (int) (y * scale);
		selector.loadPixels();
		// COLOR -16777216 (black) to -1 => ID -1 (no object) to 16777214
		samples[0] = selector.pixels[locy * selector.width + locx];
		final int lx = locx <= 0 ? 0 : locx - 1;
		final int ly = locy <= 0 ? 0 : locy - 1;
		final int ux = locx >= selector.width - 1 ? locx : locx + 1;
		final int uy = locy >= selector.height - 1 ? locy : locy + 1;
		samples[1] = selector.pixels[ly * selector.width + lx];
		if (samples[0] != samples[1]) {
			return -1;
		}
		samples[2] = selector.pixels[ly * selector.width + ux];
		if (samples[0] != samples[2]) {
			return -1;
		}
		samples[3] = selector.pixels[uy * selector.width + lx];
		if (samples[0] != samples[3]) {
			return -1;
		}
		samples[4] = selector.pixels[uy * selector.width + ux];
		if (samples[0] != samples[4]) {
			return -1;
		}
		final Long selection = colorToObject.get(samples[0]);
		return selection == null ? -1 : selection;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public long getKey(final int x, final int y) {
		final int locx = (int) (x * scale);
		final int locy = (int) (y * scale);
		selector.loadPixels();
		// COLOR -16777216 (black) to -1 => ID -1 (no object) to 16777214
		samples[0] = selector.pixels[locy * selector.width + locx];
		final Long selection = colorToObject.get(samples[0]);
		return selection == null ? -1 : selection;
	}

	/**
	 *
	 *
	 * @return
	 */
	public long getKey() {
		return getKey(home.mouseX, home.mouseY);
	}

	/**
	 *
	 *
	 * @return
	 */
	public long getKeyAA() {
		return getKeyAA(home.mouseX, home.mouseY);
	}

	/**
	 * Set the key.
	 *
	 * @param i
	 *            new key
	 */
	private void setKey(final Long i) {
		if (i < 0 || i > 16777214) {
			PApplet.println("[HE_Selector error] setKey(): ID out of range");
			return;
		}
		// ID 0 to 16777214 => COLOR -16777215 to -1 (white)
		// -16777216 is black
		currentColor++;
		colorToObject.put(currentColor, i);
		selector.fill(currentColor);
		selector.stroke(currentColor);
	}

	/**
	 *
	 */
	public void image() {
		home.image(selector, 0, 0);
	}
}
