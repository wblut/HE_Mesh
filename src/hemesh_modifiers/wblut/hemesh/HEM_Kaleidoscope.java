package wblut.hemesh;

import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Line;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

public class HEM_Kaleidoscope extends HEM_Modifier {
	private WB_Point origin;
	private WB_Vector axis;
	private int n;
	private double angle;

	public HEM_Kaleidoscope() {
		super();
	}

	public HEM_Kaleidoscope setOrigin(final WB_Coord o) {
		origin = new WB_Point(o);
		return this;
	}

	public HEM_Kaleidoscope setAxis(final WB_Coord v) {
		axis = new WB_Vector(v);
		return this;
	}

	public HEM_Kaleidoscope setSymmetry(final int n) {
		this.n = n;
		return this;
	}

	public HEM_Kaleidoscope setAngle(final double a) {
		angle = a;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (n < 2) {
			return mesh;
		}
		if (origin == null) {
			return mesh;
		}
		if (axis == null) {
			return mesh;
		}
		final WB_Vector perp = axis.getOrthoNormal();
		perp.rotateAboutOriginSelf(angle, axis);
		final WB_Vector normal = axis.cross(perp);
		final WB_Plane P1 = new WB_Plane(origin, normal);
		mesh.modify(new HEM_Slice().setPlane(P1).setCap(false));
		normal.rotateAboutAxisSelf(Math.PI / n, origin, axis);
		normal.mulSelf(-1);
		final WB_Plane P2 = new WB_Plane(origin, normal);
		mesh.modify(new HEM_Slice().setPlane(P2).setCap(false));
		final HEM_Mirror mm = new HEM_Mirror().setPlane(P2);
		mesh.modify(mm);
		final HE_Vertex[] boundaryPairs = mm.boundaryPairs;
		if (mesh.getNumberOfVertices() == 0) {
			return new HE_Mesh();
		}
		final WB_Point[][] origboundary = new WB_Point[boundaryPairs.length / 2][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < boundaryPairs.length; j += 2) {
				origboundary[j / 2][i] = i == 0 ? new WB_Point(boundaryPairs[j])
						: new WB_Point(boundaryPairs[j]).rotateAboutAxisSelf(i * 2.0 * Math.PI / n, origin, axis);
			}
		}
		final boolean[] spindlepoint = new boolean[boundaryPairs.length / 2];
		for (int j = 0; j < boundaryPairs.length; j += 2) {
			boundaryPairs[j + 1].set(origboundary[j / 2][1]);
			spindlepoint[j / 2] = WB_Epsilon
					.isZeroSq(WB_GeometryOp3D.getSqDistanceToLine3D(boundaryPairs[j], new WB_Line(origin, axis)));
		}
		final HE_Mesh[] copies = new HE_Mesh[n];
		for (int i = 0; i < n; i++) {
			if (i == 0) {
				copies[i] = mesh;
				mesh.selectAllFaces("part0");
			} else {
				copies[i] = mesh.get();
				copies[i].renameSelection("part0", "part" + i);
				copies[i].rotateAboutAxisSelf(i * 2.0 * Math.PI / n, origin, axis);
			}
			for (int j = 0; j < origboundary.length; j++) {
				final int indexmirror = mesh.getIndex(boundaryPairs[2 * j + 1]);
				copies[i].getVertexWithIndex(indexmirror).set(origboundary[j][(i + 1) % n]);
			}
			copies[i].setFaceInternalLabels(i);
		}
		HE_Vertex origv, newv;
		for (int j = 0; j < origboundary.length; j++) {
			final int indexorig = mesh.getIndex(boundaryPairs[2 * j]);
			final int indexmirror = mesh.getIndex(boundaryPairs[2 * j + 1]);
			for (int i = 0; i < n; i++) {
				origv = copies[i].getVertexWithIndex(indexmirror);
				newv = spindlepoint[j] ? mesh.getVertexWithIndex(indexorig)
						: copies[(i + 1) % n].getVertexWithIndex(indexorig);
				replaceVertex(origv, newv, copies[i]);
			}
		}
		for (int i = 0; i < n - 1; i++) {
			mesh.add(copies[i + 1]);
		}
		mesh.removeUnconnectedElements();
		mesh.uncapBoundaryHalfedges();
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		return mesh;
	}

	void replaceVertex(final HE_Vertex origv, final HE_Vertex newv, final HE_Mesh origmesh) {
		final List<HE_Halfedge> star = origv.getHalfedgeStar();
		for (final HE_Halfedge he : star) {
			origmesh.setVertex(he, newv);
		}
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
