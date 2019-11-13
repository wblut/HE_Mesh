/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Plane;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEM_Mirror extends HEM_Modifier {
	private WB_Plane	P;
	private boolean		reverse		= false;
	private boolean		keepLargest	= false;
	private double		offset;
	// 1D array of vertex pairs, each vertex retained form original mesh is
	// followed by corresponding mirrored vertex. If the vertex lies on the
	// mirror plane, the original vertex is repeated.
	public HE_Vertex[]	pairs;
	// 1D array of original-mirrored vertex pairs on the boundary of the
	// resulting mesh.
	public HE_Vertex[]	boundaryPairs;

	/**
	 *
	 */
	public HEM_Mirror() {
		super();
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Mirror setOffset(final double d) {
		offset = d;
		return this;
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public HEM_Mirror setPlane(final WB_Plane P) {
		this.P = P;
		return this;
	}

	/**
	 * Set plane by origin and normal.
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return
	 */
	public HEM_Mirror setPlane(final double ox, final double oy,
			final double oz, final double nx, final double ny,
			final double nz) {
		P = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Mirror setReverse(final Boolean b) {
		reverse = b;
		return this;
	}

	public HEM_Mirror setKeepLargest(final Boolean b) {
		keepLargest = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (P == null) {
			pairs = new HE_Vertex[0];
			return mesh;
		}
		if (keepLargest) {
			HE_Selection selF = mesh.selectFrontVertices(P);
			HE_Selection selB = mesh.selectBackVertices(P);
			reverse = selF.getNumberOfVertices() < selB.getNumberOfVertices();
		}
		HEM_Slice slice = new HEM_Slice();
		slice.setPlane(P);
		slice.setOffset(offset);
		slice.setReverse(reverse);
		slice.setCap(false);
		mesh.modify(slice);
		mesh.selectAllFaces("mirror0");
		mesh.removeSelection("mirror1");
		HE_Mesh mirrormesh = mesh.get();
		mirrormesh.renameSelection("mirror0", "mirror1");
		mirrormesh.vItr();
		HE_Vertex v, origv;
		pairs = new HE_Vertex[2 * mirrormesh.getNumberOfVertices()];
		List<HE_Vertex> boundary = new FastList<HE_Vertex>();
		for (int i = 0; i < mirrormesh.getNumberOfVertices(); i++) {
			v = mirrormesh.getVertexWithIndex(i);
			origv = mesh.getVertexWithIndex(i);
			if (WB_Epsilon.isZero(WB_GeometryOp.getDistance3D(v, P))) {
				List<HE_Halfedge> star = v.getHalfedgeStar();
				for (HE_Halfedge he : star) {
					mirrormesh.setVertex(he, origv);
				}
				pairs[2 * i] = origv;
				pairs[2 * i + 1] = origv;
			} else {
				v.set(P.extractPoint2D(P.localPoint(v).scaleSelf(1, 1, -1)));
				pairs[2 * i] = origv;
				pairs[2 * i + 1] = v;
			}
		}
		HE_MeshOp.flipFaces(mirrormesh);
		mesh.add(mirrormesh);
		mesh.removeUnconnectedElements();
		mesh.uncapBoundaryHalfedges();
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		for (int i = 0; i < pairs.length; i += 2) {
			v = pairs[i];
			if (v.isBoundary()) {
				boundary.add(pairs[i]);
				boundary.add(pairs[i + 1]);
			}
		}
		boundaryPairs = new HE_Vertex[boundary.size()];
		boundaryPairs = boundary.toArray(boundaryPairs);
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}

	public static void main(final String[] args) {
		HEC_Cylinder creator = new HEC_Cylinder();
		creator.setFacets(32).setSteps(16).setRadius(50).setHeight(400);
		HE_Mesh mesh = new HE_Mesh(creator);
		HEM_Mirror modifier = new HEM_Mirror();
		WB_Plane P = new WB_Plane(0, 0, 0, 0, 1, 1);
		modifier.setPlane(P);
		modifier.setOffset(0);
		modifier.setReverse(false);
		modifier.setKeepLargest(true);
		mesh.modify(modifier);
	}
}
