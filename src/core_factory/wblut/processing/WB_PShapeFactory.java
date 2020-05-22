package wblut.processing;

import java.util.Collection;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import wblut.geom.WB_BinaryGrid3D;
import wblut.geom.WB_Coord;
import wblut.hemesh.HEC_IsoSkin;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_FaceIterator;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_HalfedgeList;
import wblut.hemesh.HE_HalfedgeStructure;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_Selection;
import wblut.hemesh.HE_Vertex;

/**
 *
 */
public class WB_PShapeFactory {
	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Mesh mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param meshes
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final Collection<? extends HE_Mesh> meshes, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		for (final HE_Mesh mesh : meshes) {
			final HE_FaceIterator fItr = mesh.fItr();
			HE_Face f;
			while (fItr.hasNext()) {
				f = fItr.next();
				halfedges = f.getFaceHalfedges();
				final int[] tris = f.getTriangles();
				HE_Vertex v0, v1, v2;
				HE_Halfedge he0, he1, he2;
				for (int i = 0; i < tris.length; i += 3) {
					he0 = halfedges.get(tris[i]);
					v0 = he0.getVertex();
					he1 = halfedges.get(tris[i + 1]);
					v1 = he1.getVertex();
					he2 = halfedges.get(tris[i + 2]);
					v2 = he2.getVertex();
					retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
					retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
					retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
				}
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Mesh mesh, final PImage img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		retained.texture(img);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Mesh mesh, final PImage[] img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.texture(img[f.getTextureId()]);
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param offset
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_HalfedgeStructure mesh, final double offset, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		final HE_Mesh parent = mesh instanceof HE_Mesh ? (HE_Mesh) mesh : ((HE_Selection) mesh).getParent();
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		HE_HalfedgeList halfedges;
		HE_Vertex v;
		HE_Halfedge he;
		WB_Coord fn;
		final float df = (float) offset;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			if (halfedges.size() > 2) {
				final int[] tris = f.getTriangles();
				for (int i = 0; i < tris.length; i += 3) {
					he = halfedges.get(tris[i]);
					v = he.getVertex();
					fn = parent.getVertexNormal(v);
					retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(), v.zf() + df * fn.zf(),
							he.getUVW().xf(), he.getUVW().yf());
					he = halfedges.get(tris[i + 1]);
					v = he.getVertex();
					fn = parent.getVertexNormal(v);
					retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(), v.zf() + df * fn.zf(),
							he.getUVW().xf(), he.getUVW().yf());
					he = halfedges.get(tris[i + 2]);
					v = he.getVertex();
					fn = parent.getVertexNormal(v);
					retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(), v.zf() + df * fn.zf(),
							he.getUVW().xf(), he.getUVW().yf());
				}
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShapeWithFaceColor(final HE_Mesh mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.fill(f.getColor());
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShapeWithVertexColor(final HE_Mesh mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.fill(v0.getColor());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.fill(v1.getColor());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.fill(v2.getColor());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_Mesh mesh, final PApplet home) {
		return createFacetedPShape(mesh, home);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_Mesh mesh, final PImage img, final PApplet home) {
		return createFacetedPShape(mesh, img, home);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_Mesh mesh, final PImage[] img, final PApplet home) {
		return createFacetedPShape(mesh, img, home);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param offset
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShape(final HE_HalfedgeStructure mesh, final double offset,
			final PApplet home) {
		return createFacetedPShape(mesh, offset, home);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShapeWithFaceColor(final HE_Mesh mesh, final PApplet home) {
		return createFacetedPShapeWithFaceColor(mesh, home);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacettedPShapeWithVertexColor(final HE_Mesh mesh, final PApplet home) {
		return createFacetedPShapeWithVertexColor(mesh, home);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Mesh mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_Mesh parent = mesh;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			WB_Coord n0, n1, n2;
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				n0 = parent.getVertexNormal(v0);
				n1 = parent.getVertexNormal(v1);
				n2 = parent.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Selection mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_Mesh parent = mesh.getParent();
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			WB_Coord n0, n1, n2;
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				n0 = parent.getVertexNormal(v0);
				n1 = parent.getVertexNormal(v1);
				n2 = parent.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Mesh mesh, final PImage img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		retained.texture(img);
		HE_HalfedgeList halfedges;
		final HE_Mesh parent = mesh;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			WB_Coord n0, n1, n2;
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				n0 = parent.getVertexNormal(v0);
				n1 = parent.getVertexNormal(v1);
				n2 = parent.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param img
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShape(final HE_Mesh mesh, final PImage[] img, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_Mesh parent = mesh;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.texture(img[f.getTextureId()]);
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			WB_Coord n0, n1, n2;
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				n0 = parent.getVertexNormal(v0);
				n1 = parent.getVertexNormal(v1);
				n2 = parent.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShapeWithFaceColor(final HE_Mesh mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		final HE_Mesh parent = mesh;
		while (fItr.hasNext()) {
			f = fItr.next();
			retained.fill(f.getColor());
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			WB_Coord n0, n1, n2;
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				n0 = parent.getVertexNormal(v0);
				n1 = parent.getVertexNormal(v1);
				n2 = parent.getVertexNormal(v2);
				retained.normal(n0.xf(), n0.yf(), n0.zf());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.normal(n1.xf(), n1.yf(), n1.zf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.normal(n2.xf(), n2.yf(), n2.zf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createSmoothPShapeWithVertexColor(final HE_Mesh mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.fill(v0.getColor());
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.fill(v1.getColor());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.fill(v2.getColor());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createWireframePShape(final HE_HalfedgeStructure mesh, final PApplet home) {
		final PShape retained = home.createShape();
		if (mesh instanceof HE_Selection) {
			((HE_Selection) mesh).collectEdgesByFace();
		}
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
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

	/**
	 *
	 *
	 * @param meshes
	 * @param home
	 * @return
	 */
	public static PShape createWireframePShape(final Collection<? extends HE_HalfedgeStructure> meshes,
			final PApplet home) {
		final PShape retained = home.createShape();
		for (final HE_HalfedgeStructure mesh : meshes) {
			if (mesh instanceof HE_Selection) {
				((HE_Selection) mesh).collectEdgesByFace();
			}
			final Iterator<HE_Halfedge> eItr = mesh.eItr();
			HE_Halfedge e;
			HE_Vertex v;
			retained.beginShape(PConstants.LINES);
			while (eItr.hasNext()) {
				e = eItr.next();
				v = e.getVertex();
				retained.vertex(v.xf(), v.yf(), v.zf());
				v = e.getEndVertex();
				retained.vertex(v.xf(), v.yf(), v.zf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param meshes
	 * @param offset
	 * @param home
	 * @return
	 */
	public static PShape createWireframePShape(final Collection<? extends HE_HalfedgeStructure> meshes,
			final double offset, final PApplet home) {
		final PShape retained = home.createShape();
		HE_Mesh parent;
		for (final HE_HalfedgeStructure mesh : meshes) {
			if (mesh instanceof HE_Selection) {
				((HE_Selection) mesh).collectEdgesByFace();
				parent = ((HE_Selection) mesh).getParent();
			} else {
				parent = (HE_Mesh) mesh;
			}
			final Iterator<HE_Halfedge> eItr = mesh.eItr();
			HE_Halfedge e;
			HE_Vertex v;
			WB_Coord fn;
			final float df = (float) offset;
			retained.beginShape(PConstants.LINES);
			while (eItr.hasNext()) {
				e = eItr.next();
				v = e.getVertex();
				fn = parent.getVertexNormal(v);
				retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(), v.zf() + df * fn.zf());
				v = e.getEndVertex();
				fn = parent.getVertexNormal(v);
				retained.vertex(v.xf() + df * fn.xf(), v.yf() + df * fn.yf(), v.zf() + df * fn.zf());
			}
		}
		retained.endShape();
		return retained;
	}

	/**
	 *
	 *
	 * @param skin
	 * @param home
	 * @return
	 */
	public static PShape createSubstratePShape(final HEC_IsoSkin skin, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.LINES);
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
	 *
	 * @param grid
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final WB_BinaryGrid3D grid, final PApplet home) {
		home.pushMatrix();
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.QUADS);
		final WB_Coord c = grid.getMin();
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
	 *
	 * @param grid
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final WB_BinaryGrid3D grid, final int cx, final int cy, final int cz,
			final PApplet home) {
		home.pushMatrix();
		home.pushStyle();
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.QUADS);
		final WB_Coord c = grid.getMin();
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

	/**
	 *
	 *
	 * @param grid
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param home
	 * @return
	 */
	public static PShape[] createFacetedPShapes(final WB_BinaryGrid3D grid, final int cx, final int cy, final int cz,
			final PApplet home) {
		final PShape[] result = new PShape[3];
		home.pushMatrix();
		home.pushStyle();
		result[0] = home.createShape();
		result[0].beginShape(PConstants.QUADS);
		final WB_Coord c = grid.getMin();
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

	/**
	 *
	 *
	 * @param grid
	 * @param home
	 * @return
	 */
	public static PShape createWireframePShape(final WB_BinaryGrid3D grid, final PApplet home) {
		home.pushMatrix();
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.LINES);
		final WB_Coord c = grid.getMin();
		retained.translate(c.xf(), c.yf(), c.zf());
		drawXEdges(grid, retained);
		drawYEdges(grid, retained);
		drawZEdges(grid, retained);
		retained.endShape();
		home.popMatrix();
		return retained;
	}

	/**
	 *
	 *
	 * @param grid
	 * @param retained
	 */
	static void drawXEdges(final WB_BinaryGrid3D grid, final PShape retained) {
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

	/**
	 *
	 *
	 * @param grid
	 * @param retained
	 */
	static void drawXFaces(final WB_BinaryGrid3D grid, final PShape retained) {
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
						retained.vertex((float) x, (float) (y + grid.getDY()), (float) z);
						retained.vertex((float) x, (float) (y + grid.getDY()), (float) (z + grid.getDZ()));
						retained.vertex((float) x, (float) y, (float) (z + grid.getDZ()));
					}
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param grid
	 * @param retained
	 */
	static void drawYEdges(final WB_BinaryGrid3D grid, final PShape retained) {
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

	/**
	 *
	 *
	 * @param grid
	 * @param retained
	 */
	static void drawYFaces(final WB_BinaryGrid3D grid, final PShape retained) {
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
						retained.vertex((float) (x + grid.getDX()), (float) y, (float) z);
						retained.vertex((float) (x + grid.getDX()), (float) y, (float) (z + grid.getDZ()));
						retained.vertex((float) x, (float) y, (float) (z + grid.getDZ()));
					}
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param grid
	 * @param retained
	 */
	static void drawZEdges(final WB_BinaryGrid3D grid, final PShape retained) {
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

	/**
	 *
	 *
	 * @param grid
	 * @param retained
	 */
	static void drawZFaces(final WB_BinaryGrid3D grid, final PShape retained) {
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
						retained.vertex((float) (x + grid.getDX()), (float) y, (float) z);
						retained.vertex((float) (x + grid.getDX()), (float) (y + grid.getDY()), (float) z);
						retained.vertex((float) x, (float) (y + grid.getDY()), (float) z);
					}
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param retained
	 */
	static void line(final double x1, final double y1, final double z1, final double x2, final double y2,
			final double z2, final PShape retained) {
		retained.vertex((float) x1, (float) y1, (float) z1);
		retained.vertex((float) x2, (float) y2, (float) z2);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param home
	 * @return
	 */
	public static PShape createFacetedPShape(final HE_Selection mesh, final PApplet home) {
		final PShape retained = home.createShape();
		retained.beginShape(PConstants.TRIANGLES);
		HE_HalfedgeList halfedges;
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			halfedges = f.getFaceHalfedges();
			final int[] tris = f.getTriangles();
			HE_Vertex v0, v1, v2;
			HE_Halfedge he0, he1, he2;
			for (int i = 0; i < tris.length; i += 3) {
				he0 = halfedges.get(tris[i]);
				v0 = he0.getVertex();
				he1 = halfedges.get(tris[i + 1]);
				v1 = he1.getVertex();
				he2 = halfedges.get(tris[i + 2]);
				v2 = he2.getVertex();
				retained.vertex(v0.xf(), v0.yf(), v0.zf(), he0.getUVW().uf(), he0.getUVW().vf());
				retained.vertex(v1.xf(), v1.yf(), v1.zf(), he1.getUVW().uf(), he1.getUVW().vf());
				retained.vertex(v2.xf(), v2.yf(), v2.zf(), he2.getUVW().uf(), he2.getUVW().vf());
			}
		}
		retained.endShape();
		return retained;
	}
}
