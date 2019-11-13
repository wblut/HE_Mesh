/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.processing;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import wblut.geom.WB_BinaryGrid3D;
import wblut.geom.WB_Coord;
import wblut.hemesh.HEC_IsoSkin;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_HalfedgeStructure;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_MeshOp;
import wblut.hemesh.HE_Selection;
import wblut.hemesh.HE_Vertex;

/**
 * @author FVH
 *
 */
public class WB_PShapeFactory {


	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Mesh mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}
	
	public static PShape createFacetedPShape(final Collection<? extends HE_Mesh> meshes,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		for(HE_Mesh mesh:meshes) {
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		}
		retained.endShape();
		return retained;
	}
	
	

	/**
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Mesh mesh,
			final PImage img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		retained.texture(img);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Mesh mesh,
			final PImage[] img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.texture(img[f.getTextureId()]);
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param offset
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_HalfedgeStructure mesh,
			final double offset, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		List<HE_Vertex> vertices;
		HE_Vertex v;
		WB_Coord fn;
		final float df = (float) offset;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			if (vertices.size() > 2) {
				final int[] tris = f.getTriangles();
				for (int i = 0; i < tris.length; i += 3) {
					v = vertices.get(tris[i]);
					fn = HE_MeshOp.getVertexNormal(v);
					retained.vertex(v.xf() + df * fn.xf(),
							v.yf() + df * fn.yf(), v.zf() + df * fn.zf(),
							v.getUVW(f).xf(), v.getUVW(f).yf());
					v = vertices.get(tris[i + 1]);
					fn = HE_MeshOp.getVertexNormal(v);
					retained.vertex(v.xf() + df * fn.xf(),
							v.yf() + df * fn.yf(), v.zf() + df * fn.zf(),
							v.getUVW(f).xf(), v.getUVW(f).yf());
					v = vertices.get(tris[i + 2]);
					fn = HE_MeshOp.getVertexNormal(v);
					retained.vertex(v.xf() + df * fn.xf(),
							v.yf() + df * fn.yf(), v.zf() + df * fn.zf(),
							v.getUVW(f).xf(), v.getUVW(f).yf());
				}
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShapeWithFaceColor(final HE_Mesh mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.fill(f.getColor());
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShapeWithVertexColor(final HE_Mesh mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.fill(v0.getColor());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.fill(v1.getColor());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.fill(v2.getColor());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_Mesh mesh,
			final PApplet home) {
		return createFacetedPShape(mesh, home);
	}

	/**
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_Mesh mesh,
			final PImage img, final PApplet home) {
		return createFacetedPShape(mesh, img, home);
	}

	/**
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_Mesh mesh,
			final PImage[] img, final PApplet home) {
		return createFacetedPShape(mesh, img, home);
	}

	/**
	 *
	 * @param mesh
	 * @param offset
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_HalfedgeStructure mesh,
			final double offset, final PApplet home) {
		return createFacetedPShape(mesh, offset, home);
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShapeWithFaceColor(final HE_Mesh mesh,
			final PApplet home) {
		return createFacetedPShapeWithFaceColor(mesh, home);
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShapeWithVertexColor(final HE_Mesh mesh,
			final PApplet home) {
		return createFacetedPShapeWithVertexColor(mesh, home);
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Mesh mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			WB_Coord n0, n1, n2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				n0 = HE_MeshOp.getVertexNormal(v0);
				v1 = vertices.get(tris[i + 1]);
				n1 = HE_MeshOp.getVertexNormal(v1);
				v2 = vertices.get(tris[i + 2]);
				n2 = HE_MeshOp.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	public static PShape createSmoothPShape(final HE_Selection mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			WB_Coord n0, n1, n2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				n0 = HE_MeshOp.getVertexNormal(v0);
				v1 = vertices.get(tris[i + 1]);
				n1 = HE_MeshOp.getVertexNormal(v1);
				v2 = vertices.get(tris[i + 2]);
				n2 = HE_MeshOp.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Mesh mesh,
			final PImage img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		retained.texture(img);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			WB_Coord n0, n1, n2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				n0 = HE_MeshOp.getVertexNormal(v0);
				v1 = vertices.get(tris[i + 1]);
				n1 = HE_MeshOp.getVertexNormal(v1);
				v2 = vertices.get(tris[i + 2]);
				n2 = HE_MeshOp.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Mesh mesh,
			final PImage[] img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.texture(img[f.getTextureId()]);
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			WB_Coord n0, n1, n2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				n0 = HE_MeshOp.getVertexNormal(v0);
				v1 = vertices.get(tris[i + 1]);
				n1 = HE_MeshOp.getVertexNormal(v1);
				v2 = vertices.get(tris[i + 2]);
				n2 = HE_MeshOp.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShapeWithFaceColor(final HE_Mesh mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.fill(f.getColor());
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			WB_Coord n0, n1, n2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				n0 = HE_MeshOp.getVertexNormal(v0);
				v1 = vertices.get(tris[i + 1]);
				n1 = HE_MeshOp.getVertexNormal(v1);
				v2 = vertices.get(tris[i + 2]);
				n2 = HE_MeshOp.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShapeWithVertexColor(final HE_Mesh mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.fill(v0.getColor());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.fill(v1.getColor());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.fill(v2.getColor());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createWireframePShape(final HE_HalfedgeStructure mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		if (mesh instanceof HE_Selection) {
			((HE_Selection) mesh).collectEdgesByFace();
		}
		Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		HE_Vertex v;
		retained.beginShape(PConstants.LINES);
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.isVisible()) {
				v = e.getVertex();
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = e.getEndVertex();
				retained.vertex(v.xf(), v.yf(), v.zf());
			}
		}
		retained.endShape();
		return retained;
	}
	
	public static PShape createWireframePShape(
			final Collection<? extends HE_HalfedgeStructure> meshes,
					final PApplet home) {

		final PShape retained = home.createShape();
		for (HE_HalfedgeStructure mesh : meshes) {

			if (mesh instanceof HE_Selection)
				((HE_Selection) mesh).collectEdgesByFace();

			Iterator<HE_Halfedge> eItr = mesh.eItr();
			HE_Halfedge e;
			HE_Vertex v;

			
			retained.beginShape(PApplet.LINES);
			while (eItr.hasNext()) {
				e = eItr.next();
				v = e.getVertex();

				retained.vertex(v.xf() , v.yf(),
						v.zf() );

				v = e.getEndVertex();

				retained.vertex(v.xf() , v.yf() ,
						v.zf() );

				
			}
		}
		retained.endShape();
	
		return retained;
	}
	
	public static  PShape createWireframePShape(
			final Collection<? extends HE_HalfedgeStructure> meshes, double offset,
					final PApplet home) {

		final PShape retained = home.createShape();
		for (HE_HalfedgeStructure mesh : meshes) {

			if (mesh instanceof HE_Selection)
				((HE_Selection) mesh).collectEdgesByFace();

			Iterator<HE_Halfedge> eItr = mesh.eItr();
			HE_Halfedge e;
			HE_Vertex v;
			WB_Coord fn;
			float df = (float) offset;
			retained.beginShape(PApplet.LINES);
			while (eItr.hasNext()) {
				e = eItr.next();
				v = e.getVertex();
				fn = v.getVertexNormal();
				retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(),
						v.zf() + df * fn.zf());

				v = e.getEndVertex();
				fn = v.getVertexNormal();
				retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(),
						v.zf() + df * fn.zf());

				
			}
		}
		retained.endShape();
	
		return retained;
	}

	public static PShape createSubstratePShape(final HEC_IsoSkin skin,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PApplet.LINES);
		WB_Coord v;
		wblut.hemesh.HEC_IsoSkin.Cell cell;
		for (int i = 0; i < skin.getNumberOfLayers(); i++) {
			for (int j = 0; j < skin.getCells()[0].length; j++) {
				cell = skin.getCells()[i][j];
				if (i == 0) {
					v = skin.getGridpositions()[i][cell.getCornerIndices()[0]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[1]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[0]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[2]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[1]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[3]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[2]];
					retained.vertex(v.xf(), v.yf(), v.zf());
					v = skin.getGridpositions()[i][cell.getCornerIndices()[3]];
					retained.vertex(v.xf(), v.yf(), v.zf());
				}
				v = skin.getGridpositions()[i][cell.getCornerIndices()[0]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[0]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i][cell.getCornerIndices()[1]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[1]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i][cell.getCornerIndices()[2]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[2]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i][cell.getCornerIndices()[3]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[3]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[0]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[1]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[0]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[2]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[1]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[3]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[2]];
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = skin.getGridpositions()[i + 1][cell.getCornerIndices()[3]];
				retained.vertex(v.xf(), v.yf(), v.zf());
			}
		}
		retained.endShape();
		retained.disableStyle();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(WB_BinaryGrid3D grid,
			final PApplet home) {
		home.pushMatrix();
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.QUADS);
		WB_Coord c = grid.getMin();
		home.translate(c.xf(), c.yf(), c.zf());
		drawXFaces(grid, retained);
		drawYFaces(grid, retained);
		drawZFaces(grid, retained);
		retained.endShape();
		home.popMatrix();
		return retained;
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(WB_BinaryGrid3D grid, int cx,
			int cy, int cz, final PApplet home) {
		home.pushMatrix();
		home.pushStyle();
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.QUADS);
		WB_Coord c = grid.getMin();
		retained.translate(c.xf(), c.yf(), c.zf());
		home.noStroke();
		home.fill(cx);
		drawXFaces(grid, retained);
		home.fill(cy);
		drawYFaces(grid, retained);
		home.fill(cz);
		drawZFaces(grid, retained);
		retained.endShape();
		home.popStyle();
		home.popMatrix();
		return retained;
	}

	public static PShape[] createFacetedPShapes(WB_BinaryGrid3D grid, int cx,
			int cy, int cz, final PApplet home) {
		PShape[] result = new PShape[3];
		home.pushMatrix();
		home.pushStyle();
		result[0] = home.createShape();
		result[0].beginShape(PConstants.QUADS);
		WB_Coord c = grid.getMin();
		result[0].translate(c.xf(), c.yf(), c.zf());
		home.noStroke();
		home.fill(cx);
		drawXFaces(grid, result[0]);
		result[0].endShape();
		result[1] = home.createShape();
		result[1].beginShape(PConstants.QUADS);
		result[1].translate(c.xf(), c.yf(), c.zf());
		home.noStroke();
		home.fill(cy);
		drawYFaces(grid, result[1]);
		result[1].endShape();
		result[2] = home.createShape();
		result[2].beginShape(PConstants.QUADS);
		result[2].translate(c.xf(), c.yf(), c.zf());
		home.noStroke();
		home.fill(cx);
		drawZFaces(grid, result[2]);
		result[2].endShape();
		home.popStyle();
		home.popMatrix();
		return result;
	}

	public static PShape createWireframePShape(final WB_BinaryGrid3D grid,
			final PApplet home) {
		home.pushMatrix();
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.LINES);
		WB_Coord c = grid.getMin();
		retained.translate(c.xf(), c.yf(), c.zf());
		drawXEdges(grid, retained);
		drawYEdges(grid, retained);
		drawZEdges(grid, retained);
		retained.endShape();
		home.popMatrix();
		return retained;
	}

	static void drawXEdges(final WB_BinaryGrid3D grid, PShape retained) {
		int val00, valm0, valmm, val0m, sum;
		double x, y, z;
		for (int i = grid.lx(); i < grid.ux(); i++) {
			x = i * grid.getDX();
			for (int j = grid.ly(); j <= grid.uy(); j++) {
				y = j * grid.getDY();
				for (int k = grid.lz(); k <= grid.uz(); k++) {
					z = k * grid.getDZ();
					val00 = grid.get(i, j, k) ? 1 : 0;
					valm0 = grid.get(i, j - 1, k) ? 1 : 0;
					valmm = grid.get(i, j - 1, k - 1) ? 1 : 0;
					val0m = grid.get(i, j, k - 1) ? 1 : 0;
					sum = val00 + valm0 + valmm + val0m;
					if (sum == 1 || sum == 3) {
						line(x, y, z, x + grid.getDX(), y, z, retained);
					}
					if (sum == 2) {
						if (val00 + valmm == 2 || val0m + valm0 == 2) {
							line(x, y, z, x + grid.getDX(), y, z, retained);
						}
					}
				}
			}
		}
	}

	static void drawXFaces(final WB_BinaryGrid3D grid, PShape retained) {
		int val0, valm, sum;
		double x, y, z;
		for (int i = grid.lx(); i <= grid.ux(); i++) {
			x = i * grid.getDX();
			for (int j = grid.ly(); j < grid.uy(); j++) {
				y = j * grid.getDY();
				for (int k = grid.lz(); k < grid.uz(); k++) {
					z = k * grid.getDZ();
					val0 = grid.get(i, j, k) ? 1 : 0;
					valm = grid.get(i - 1, j, k) ? 1 : 0;
					sum = val0 + valm;
					if (sum == 1) {
						retained.vertex((float) x, (float) y, (float) z);
						retained.vertex((float) x, (float) (y + grid.getDY()),
								(float) z);
						retained.vertex((float) x, (float) (y + grid.getDY()),
								(float) (z + grid.getDZ()));
						retained.vertex((float) x, (float) y,
								(float) (z + grid.getDZ()));
					}
				}
			}
		}
	}

	static void drawYEdges(final WB_BinaryGrid3D grid, PShape retained) {
		int val00, valm0, valmm, val0m, sum;
		double x, y, z;
		for (int j = grid.ly(); j < grid.uy(); j++) {
			y = j * grid.getDY();
			for (int i = grid.lx(); i <= grid.ux(); i++) {
				x = i * grid.getDX();
				for (int k = grid.lz(); k <= grid.uz(); k++) {
					z = k * grid.getDZ();
					val00 = grid.get(i, j, k) ? 1 : 0;
					valm0 = grid.get(i - 1, j, k) ? 1 : 0;
					valmm = grid.get(i - 1, j, k - 1) ? 1 : 0;
					val0m = grid.get(i, j, k - 1) ? 1 : 0;
					sum = val00 + valm0 + valmm + val0m;
					if (sum == 1 || sum == 3) {
						line(x, y, z, x, y + grid.getDY(), z, retained);
					}
					if (sum == 2) {
						if (val00 + valmm == 2 || val0m + valm0 == 2) {
							line(x, y, z, x, y + grid.getDY(), z, retained);
						}
					}
				}
			}
		}
	}

	static void drawYFaces(final WB_BinaryGrid3D grid, PShape retained) {
		int val0, valm, sum;
		double x, y, z;
		for (int i = grid.lx(); i < grid.ux(); i++) {
			x = i * grid.getDX();
			for (int j = grid.ly(); j <= grid.uy(); j++) {
				y = j * grid.getDY();
				for (int k = grid.lz(); k < grid.uz(); k++) {
					z = k * grid.getDZ();
					val0 = grid.get(i, j, k) ? 1 : 0;
					valm = grid.get(i, j - 1, k) ? 1 : 0;
					sum = val0 + valm;
					if (sum == 1) {
						retained.vertex((float) x, (float) y, (float) z);
						retained.vertex((float) (x + grid.getDX()), (float) y,
								(float) z);
						retained.vertex((float) (x + grid.getDX()), (float) y,
								(float) (z + grid.getDZ()));
						retained.vertex((float) x, (float) y,
								(float) (z + grid.getDZ()));
					}
				}
			}
		}
	}

	static void drawZEdges(final WB_BinaryGrid3D grid, PShape retained) {
		int val00, valm0, valmm, val0m, sum;
		double x, y, z;
		for (int k = grid.lz(); k < grid.uz(); k++) {
			z = k * grid.getDZ();
			for (int j = grid.ly(); j <= grid.uy(); j++) {
				y = j * grid.getDY();
				for (int i = grid.lx(); i <= grid.ux(); i++) {
					x = i * grid.getDX();
					val00 = grid.get(i, j, k) ? 1 : 0;
					valm0 = grid.get(i - 1, j, k) ? 1 : 0;
					valmm = grid.get(i - 1, j - 1, k) ? 1 : 0;
					val0m = grid.get(i, j - 1, k) ? 1 : 0;
					sum = val00 + valm0 + valmm + val0m;
					if (sum == 1 || sum == 3) {
						line(x, y, z, x, y, z + grid.getDZ(), retained);
					}
					if (sum == 2) {
						if (val00 + valmm == 2 || val0m + valm0 == 2) {
							line(x, y, z, x, y, z + grid.getDZ(), retained);
						}
					}
				}
			}
		}
	}

	static void drawZFaces(final WB_BinaryGrid3D grid, PShape retained) {
		int val0, valm, sum;
		double x, y, z;
		for (int i = grid.lx(); i < grid.ux(); i++) {
			x = i * grid.getDX();
			for (int j = grid.ly(); j < grid.uy(); j++) {
				y = j * grid.getDY();
				for (int k = grid.lz(); k <= grid.uz(); k++) {
					z = k * grid.getDZ();
					val0 = grid.get(i, j, k) ? 1 : 0;
					valm = grid.get(i, j, k - 1) ? 1 : 0;
					sum = val0 + valm;
					if (sum == 1) {
						retained.vertex((float) x, (float) y, (float) z);
						retained.vertex((float) (x + grid.getDX()), (float) y,
								(float) z);
						retained.vertex((float) (x + grid.getDX()),
								(float) (y + grid.getDY()), (float) z);
						retained.vertex((float) x, (float) (y + grid.getDY()),
								(float) z);
					}
				}
			}
		}
	}

	static void line(final double x1, final double y1, final double z1,
			final double x2, final double y2, final double z2,
			PShape retained) {
		retained.vertex((float) x1, (float) y1, (float) z1);
		retained.vertex((float) x2, (float) y2, (float) z2);
	}

	/**
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Selection mesh,
			final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		List<HE_Vertex> vertices;
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			vertices = f.getFaceVertices();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), v0.getUVW(f).uf(),
						v0.getUVW(f).vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), v1.getUVW(f).uf(),
						v1.getUVW(f).vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), v2.getUVW(f).uf(),
						v2.getUVW(f).vf());
			}
		}
		retained.endShape();
		return retained;
	}
}
