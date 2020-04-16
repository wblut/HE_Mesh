package wblut.hemesh;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import wblut.geom.WB_Point;
import wblut.geom.WB_Quad;
import wblut.geom.WB_Triangle;

public class HET_MeshBuffer extends PGraphicsOpenGL {
	protected PApplet home;
	protected HE_MeshCollection meshes;
	private ArrayList<WB_Triangle> triangles;
	private ArrayList<WB_Quad> quads;
	private WB_Point p1, p2, p3, p4;
	private int shape;
	private int shapectr;

	public HET_MeshBuffer() {
		meshes = new HE_MeshCollection();
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean displayable() {
		return false;
	}

	@Override
	public void beginDraw() {
	}

	@Override
	public void endDraw() {
	}

	public void nextMesh() {
		final HE_Mesh m = new HE_Mesh();
		if (triangles != null) {
			if (triangles.size() > 0) {
				final HEC_FromTriangles ft = new HEC_FromTriangles().setTriangles(triangles);
				m.add(new HE_Mesh(ft));
			}
		}
		if (quads != null) {
			if (quads.size() > 0) {
				final HEC_FromQuads fq = new HEC_FromQuads().setQuads(quads);
				m.add(new HE_Mesh(fq));
			}
		}
		if (m.getNumberOfVertices() > 0) {
			meshes.add(m);
		}
		triangles = new ArrayList<>();
		quads = new ArrayList<>();
		vertexCount = 0;
	}

	@Override
	public void beginShape(final int kind) {
		shape = kind;
		shapectr = 0;
		if (shape != TRIANGLES && shape != TRIANGLE_STRIP && shape != TRIANGLE_FAN && shape != QUADS
				&& shape != QUAD_STRIP) {
			final String err = "HET_Recorder can only be used with TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN, QUADS OR QUAD_STRIP.";
			throw new RuntimeException(err);
		}
		vertexCount = 0;
	}

	@Override
	public void vertex(final float x, final float y) {
		vertex(x, y, 0);
	}

	@Override
	public void vertex(final float x, final float y, final float z) {
		final float vertex[] = vertices[vertexCount];
		vertex[X] = x;
		vertex[Y] = y;
		vertex[Z] = z;
		final float tvertex[] = new float[3];
		tvertex[X] = modelX(vertex[X], vertex[Y], vertex[Z]);
		tvertex[Y] = modelY(vertex[X], vertex[Y], vertex[Z]);
		tvertex[Z] = modelZ(vertex[X], vertex[Y], vertex[Z]);
		vertex[X] = tvertex[X];
		vertex[Y] = tvertex[Y];
		vertex[Z] = tvertex[Z];
		vertexCount++;
		if (shape == TRIANGLES || shape == TRIANGLE_STRIP || shape == TRIANGLE_FAN) {
			if (vertexCount == 3) {
				addTriangle();
			}
		} else if (shape == QUADS || shape == QUAD_STRIP) {
			if (vertexCount == 4) {
				addQuad();
			}
		}
	}

	protected void addTriangle() {
		if (shape == TRIANGLES) {
			p1 = new WB_Point(vertices[0][X], vertices[0][Y], vertices[0][Z]);
			p2 = new WB_Point(vertices[1][X], vertices[1][Y], vertices[1][Z]);
			p3 = new WB_Point(vertices[2][X], vertices[2][Y], vertices[2][Z]);
			if (triangles == null) {
				triangles = new ArrayList<>();
			}
			triangles.add(new WB_Triangle(p1, p2, p3));
			vertexCount = 0;
		} else if (shape == TRIANGLE_STRIP) {
			p1 = new WB_Point(vertices[0][X], vertices[0][Y], vertices[0][Z]);
			p2 = new WB_Point(vertices[1][X], vertices[1][Y], vertices[1][Z]);
			p3 = new WB_Point(vertices[2][X], vertices[2][Y], vertices[2][Z]);
			if (triangles == null) {
				triangles = new ArrayList<>();
			}
			if (shapectr % 2 == 0) {
				triangles.add(new WB_Triangle(p1, p2, p3));
			} else {
				triangles.add(new WB_Triangle(p2, p1, p3));
			}
			vertices[0][X] = vertices[1][X];
			vertices[0][Y] = vertices[1][Y];
			vertices[0][Z] = vertices[1][Z];
			vertices[1][X] = vertices[2][X];
			vertices[1][Y] = vertices[2][Y];
			vertices[1][Z] = vertices[2][Z];
			vertexCount = 2;
			shapectr++;
		} else if (shape == TRIANGLE_FAN) {
			p1 = new WB_Point(vertices[0][X], vertices[0][Y], vertices[0][Z]);
			p2 = new WB_Point(vertices[1][X], vertices[1][Y], vertices[1][Z]);
			p3 = new WB_Point(vertices[2][X], vertices[2][Y], vertices[2][Z]);
			if (triangles == null) {
				triangles = new ArrayList<>();
			}
			triangles.add(new WB_Triangle(p1, p2, p3));
			vertices[1][X] = vertices[2][X];
			vertices[1][Y] = vertices[2][Y];
			vertices[1][Z] = vertices[2][Z];
			vertexCount = 2;
		}
	}

	protected void addQuad() {
		if (shape == QUADS) {
			p1 = new WB_Point(vertices[0][X], vertices[0][Y], vertices[0][Z]);
			p2 = new WB_Point(vertices[1][X], vertices[1][Y], vertices[1][Z]);
			p3 = new WB_Point(vertices[2][X], vertices[2][Y], vertices[2][Z]);
			p4 = new WB_Point(vertices[3][X], vertices[3][Y], vertices[3][Z]);
			if (quads == null) {
				quads = new ArrayList<>();
			}
			quads.add(new WB_Quad(p1, p2, p3, p4));
			vertexCount = 0;
		} else if (shape == QUAD_STRIP) {
			p1 = new WB_Point(vertices[0][X], vertices[0][Y], vertices[0][Z]);
			p2 = new WB_Point(vertices[1][X], vertices[1][Y], vertices[1][Z]);
			p3 = new WB_Point(vertices[2][X], vertices[2][Y], vertices[2][Z]);
			p4 = new WB_Point(vertices[3][X], vertices[3][Y], vertices[3][Z]);
			if (quads == null) {
				quads = new ArrayList<>();
			}
			quads.add(new WB_Quad(p1, p2, p4, p3));
			vertices[0][X] = vertices[2][X];
			vertices[0][Y] = vertices[2][Y];
			vertices[0][Z] = vertices[2][Z];
			vertices[1][X] = vertices[3][X];
			vertices[1][Y] = vertices[3][Y];
			vertices[1][Z] = vertices[3][Z];
			vertexCount = 2;
		}
	}
}
