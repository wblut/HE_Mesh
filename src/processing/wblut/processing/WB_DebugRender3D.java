/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.processing;

import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_HalfedgeStructure;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_MeshOp;
import wblut.hemesh.HE_Vertex;

/**
 *
 */
public class WB_DebugRender3D {
	/**
	 *
	 */
	private final PGraphics		home;
	/**
	 *
	 */
	private WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();

	/**
	 *
	 *
	 * @param home
	 */
	public WB_DebugRender3D(final PApplet home) {
		if (home.g == null) {
			throw new IllegalArgumentException(
					"WB_DebugRender3D can only be used after size()");
		}
		if (!(home.g instanceof PGraphicsOpenGL)) {
			throw new IllegalArgumentException(
					"WB_DebugRender3D can only be used with P3D, OPENGL or derived ProcessingPGraphics object");
		}
		this.home = home.g;
	}

	/**
	 *
	 *
	 * @param home
	 */
	public WB_DebugRender3D(final PGraphics home) {
		this.home = home;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 */
	public void drawBadVertices(final HE_HalfedgeStructure mesh,
			final double d) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getHalfedge() == null || !mesh.contains(v.getHalfedge())) {
				home.pushMatrix();
				home.translate(v.xf(), v.yf(), v.zf());
				home.box((float) d);
				home.popMatrix();
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public void drawBoundaryEdges(final HE_HalfedgeStructure mesh) {
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(),
						he.getNextInFace().getVertex().xf(),
						he.getNextInFace().getVertex().yf(),
						he.getNextInFace().getVertex().zf());
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public void drawBoundaryHalfedges(final HE_HalfedgeStructure mesh) {
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		home.pushStyle();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getPair().getFace() == null) {
				home.stroke(255, 0, 0);
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(),
						he.getNextInFace().getVertex().xf(),
						he.getNextInFace().getVertex().yf(),
						he.getNextInFace().getVertex().zf());
			}
		}
		home.popStyle();
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public void drawUnpairedHalfedges(final HE_HalfedgeStructure mesh) {
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		home.pushStyle();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getPair() == null) {
				home.stroke(255, 0, 0);
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(),
						he.getNextInFace().getVertex().xf(),
						he.getNextInFace().getVertex().yf(),
						he.getNextInFace().getVertex().zf());
			}
		}
		home.popStyle();
	}

	/**
	 *
	 *
	 * @param f
	 * @param d
	 */
	public void drawFaceNormal(final HE_Face f, final double d) {
		final WB_Coord p1 = HE_MeshOp.getFaceCenter(f);
		final WB_Point p2 = WB_Point.mul(HE_MeshOp.getFaceNormal(f), d)
				.addSelf(p1);
		home.line(p1.xf(), p1.yf(), p1.zf(), p2.xf(), p2.yf(), p2.zf());
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 */
	public void drawFaceNormals(final HE_HalfedgeStructure mesh,
			final double d) {
		final Iterator<HE_Face> fItr = mesh.fItr();
		WB_Coord fc;
		WB_Coord fn;
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			fc = HE_MeshOp.getFaceCenter(f);
			fn = HE_MeshOp.getFaceNormal(f);
			home.line(fc.xf(), fc.yf(), fc.zf(), fc.xf() + (float) d * fn.xf(),
					fc.yf() + (float) d * fn.yf(),
					fc.zf() + (float) d * fn.zf());
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public void drawFaceTypes(final HE_HalfedgeStructure mesh) {
		final Iterator<HE_Face> fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (HE_MeshOp.getFaceType(f) == WB_Classification.CONVEX) {
				home.fill(0, 255, 0);
			} else if (HE_MeshOp.getFaceType(f) == WB_Classification.CONCAVE) {
				home.fill(255, 0, 0);
			} else {
				home.fill(0, 0, 255);
			}
			drawFace(f);
		}
	}

	/**
	 *
	 *
	 * @param he
	 * @param d
	 * @param s
	 */
	public void drawHalfedge(final HE_Halfedge he, final double d,
			final double s) {
		final WB_Point c = new WB_Point(HE_MeshOp.getHalfedgeCenter(he));
		c.addMulSelf(d, HE_MeshOp.getHalfedgeNormal(he));
		home.stroke(255, 0, 0);
		home.line(he.getVertex().xf(), he.getVertex().yf(), he.getVertex().zf(),
				c.xf(), c.yf(), c.zf());
		if (HE_MeshOp.getHalfedgeType(he) == WB_Classification.CONVEX) {
			home.stroke(0, 255, 0);
		} else if (HE_MeshOp.getHalfedgeType(he) == WB_Classification.CONCAVE) {
			home.stroke(255, 0, 0);
		} else {
			home.stroke(0, 0, 255);
		}
		home.pushMatrix();
		home.translate(c.xf(), c.yf(), c.zf());
		home.box((float) s);
		home.popMatrix();
	}

	/**
	 *
	 *
	 * @param he
	 * @param d
	 * @param s
	 * @param f
	 */
	public void drawHalfedge(final HE_Halfedge he, final double d,
			final double s, final double f) {
		final WB_Point c = geometryfactory
				.createInterpolatedPoint(he.getVertex(), he.getEndVertex(), f);
		c.addMulSelf(d, HE_MeshOp.getHalfedgeNormal(he));
		home.stroke(255, 0, 0);
		home.line(he.getVertex().xf(), he.getVertex().yf(), he.getVertex().zf(),
				c.xf(), c.yf(), c.zf());
		if (HE_MeshOp.getHalfedgeType(he) == WB_Classification.CONVEX) {
			home.stroke(0, 255, 0);
		} else if (HE_MeshOp.getHalfedgeType(he) == WB_Classification.CONCAVE) {
			home.stroke(255, 0, 0);
		} else {
			home.stroke(0, 0, 255);
		}
		home.pushMatrix();
		home.translate(c.xf(), c.yf(), c.zf());
		home.box((float) s);
		home.popMatrix();
	}

	/**
	 *
	 *
	 * @param key
	 * @param d
	 * @param s
	 * @param mesh
	 */
	public void drawHalfedge(final Long key, final double d, final double s,
			final HE_Mesh mesh) {
		final HE_Halfedge he = mesh.getHalfedgeWithKey(key);
		drawHalfedge(he, d, s);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 * @param f
	 */
	public void drawHalfedges(final HE_HalfedgeStructure mesh, final double d,
			final double f) {
		WB_Point c;
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		home.pushStyle();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() != null) {
				c = geometryfactory.createInterpolatedPoint(he.getVertex(),
						he.getEndVertex(), f);
				c.addMulSelf(d, HE_MeshOp.getHalfedgeNormal(he));
				home.stroke(255, 0, 0);
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(), c.xf(), c.yf(), c.zf());
				if (HE_MeshOp.getHalfedgeType(he) == WB_Classification.CONVEX) {
					home.stroke(0, 255, 0);
					home.fill(0, 255, 0);
				} else if (HE_MeshOp
						.getHalfedgeType(he) == WB_Classification.CONCAVE) {
					home.stroke(255, 0, 0);
					home.fill(255, 0, 0);
				} else {
					home.stroke(0, 0, 255);
					home.fill(0, 0, 255);
				}
				home.pushMatrix();
				home.translate(c.xf(), c.yf(), c.zf());
				home.box((float) d);
				home.popMatrix();
			} else {
				c = geometryfactory.createInterpolatedPoint(he.getVertex(),
						he.getEndVertex(), f);
				c.addMulSelf(-d, HE_MeshOp.getHalfedgeNormal(he.getPair()));
				home.stroke(255, 0, 0);
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(), c.xf(), c.yf(), c.zf());
				home.stroke(0, 255, 255);
				home.pushMatrix();
				home.translate(c.xf(), c.yf(), c.zf());
				home.box((float) d);
				home.popMatrix();
			}
		}
		home.popStyle();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 */
	public void drawHalfedges(final HE_HalfedgeStructure mesh, final double d) {
		WB_Point c;
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = mesh.heItr();
		home.pushStyle();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() != null) {
				c = new WB_Point(HE_MeshOp.getHalfedgeCenter(he));
				c.addMulSelf(d, HE_MeshOp.getHalfedgeNormal(he));
				home.stroke(255, 0, 0);
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(), c.xf(), c.yf(), c.zf());
				if (HE_MeshOp.getHalfedgeType(he) == WB_Classification.CONVEX) {
					home.stroke(0, 255, 0);
					home.fill(0, 255, 0);
				} else if (HE_MeshOp
						.getHalfedgeType(he) == WB_Classification.CONCAVE) {
					home.stroke(255, 0, 0);
					home.fill(255, 0, 0);
				} else {
					home.stroke(0, 0, 255);
					home.fill(0, 0, 255);
				}
				home.pushMatrix();
				home.translate(c.xf(), c.yf(), c.zf());
				home.box((float) d);
				home.popMatrix();
			} else {
				c = new WB_Point(HE_MeshOp.getHalfedgeCenter(he));
				c.addMulSelf(-d, HE_MeshOp.getHalfedgeNormal(he.getPair()));
				home.stroke(255, 0, 0);
				home.line(he.getVertex().xf(), he.getVertex().yf(),
						he.getVertex().zf(), c.xf(), c.yf(), c.zf());
				home.stroke(0, 255, 255);
				home.pushMatrix();
				home.translate(c.xf(), c.yf(), c.zf());
				home.box((float) d);
				home.popMatrix();
			}
		}
		home.popStyle();
	}

	/**
	 *
	 *
	 * @param he
	 * @param d
	 * @param s
	 */
	public void drawHalfedgeSimple(final HE_Halfedge he, final double d,
			final double s) {
		final WB_Point c = new WB_Point(HE_MeshOp.getHalfedgeCenter(he));
		c.addMulSelf(d, HE_MeshOp.getHalfedgeNormal(he));
		home.line(he.getVertex().xf(), he.getVertex().yf(), he.getVertex().zf(),
				c.xf(), c.yf(), c.zf());
		home.pushMatrix();
		home.translate(c.xf(), c.yf(), c.zf());
		home.box((float) s);
		home.popMatrix();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 */
	public void drawVertexNormals(final HE_HalfedgeStructure mesh,
			final double d) {
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		WB_Coord vn;
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			vn = HE_MeshOp.getVertexNormal(v);
			draw(v, vn, d);
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @param v
	 * @param d
	 */
	private void draw(final WB_Coord p, final WB_Coord v, final double d) {
		home.line(p.xf(), p.yf(), p.zf(), p.xf() + (float) d * v.xf(),
				p.yf() + (float) d * v.yf(), p.zf() + (float) d * v.zf());
	}

	/**
	 *
	 *
	 * @param f
	 */
	private void drawFace(final HE_Face f) {
		if (f.getFaceDegree() > 2) {
			final int[] tris = f.getTriangles();
			final List<HE_Vertex> vertices = f.getFaceVertices();
			WB_Coord v0, v1, v2;
			for (int i = 0; i < tris.length; i += 3) {
				home.beginShape(PConstants.TRIANGLES);
				v0 = vertices.get(tris[i]);
				v1 = vertices.get(tris[i + 1]);
				v2 = vertices.get(tris[i + 2]);
				home.vertex(v0.xf(), v0.yf(), v0.zf());
				home.vertex(v1.xf(), v1.yf(), v1.zf());
				home.vertex(v2.xf(), v2.yf(), v2.zf());
				home.endShape();
			}
		}
	}
}
