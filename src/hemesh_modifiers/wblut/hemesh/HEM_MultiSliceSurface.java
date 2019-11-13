/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import wblut.geom.WB_Classification;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Plane;

/**
 * Multiple planar cuts of a mesh. No faces are removed.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_MultiSliceSurface extends HEM_Modifier {
	/** Cut planes. */
	private ArrayList<WB_Plane> planes;
	/** Store cut faces. */
	private HE_Selection cutFaces;
	/** The new edges. */
	private HE_Selection newEdges;
	/** The offset. */
	private double offset;

	/**
	 * Set offset.
	 *
	 * @param d
	 *            offset
	 * @return self
	 */
	public HEM_MultiSliceSurface setOffset(final double d) {
		offset = d;
		return this;
	}

	/**
	 * Instantiates a new HEM_MultiSlice surface.
	 */
	public HEM_MultiSliceSurface() {
		super();
	}

	/**
	 * Set cut planes from an arrayList of WB_Plane.
	 *
	 * @param planes
	 *            arrayList of WB_Plane
	 * @return self
	 */
	public HEM_MultiSliceSurface setPlanes(final Collection<WB_Plane> planes) {
		this.planes = new ArrayList<WB_Plane>();
		this.planes.addAll(planes);
		return this;
	}

	/**
	 * Set cut planes from an array of WB_Plane.
	 *
	 * @param planes
	 *            array of WB_Plane
	 * @return self
	 */
	public HEM_MultiSliceSurface setPlanes(final WB_Plane[] planes) {
		this.planes = new ArrayList<WB_Plane>();
		for (final WB_Plane plane : planes) {
			this.planes.add(plane);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		cutFaces = HE_Selection.getSelection(mesh);
		newEdges = HE_Selection.getSelection(mesh);
		mesh.resetFaceInternalLabels();
		mesh.resetEdgeInternalLabels();
		if (planes == null) {
			return mesh;
		}
		final HEM_SliceSurface slice = new HEM_SliceSurface();

		boolean unique = true;
		WB_Plane Pi, Pj;
		for (int i = 0; i < planes.size(); i++) {
			Pi = planes.get(i);
			unique = true;
			for (int j = 0; j < i; j++) {
				Pj = planes.get(j);
				if (WB_GeometryOp.isEqual(Pi, Pj)) {
					unique = false;
					break;
				}
			}
			if (unique) {

				slice.setPlane(Pi).setOffset(offset);
				slice.applySelf(mesh);
				cutFaces.add(mesh.getSelection("cuts"));

			}
		}
		mesh.resetEdgeInternalLabels();
		cutFaces.cleanSelection();
		cutFaces.collectEdgesByFace();
		final Iterator<HE_Halfedge> eItr = cutFaces.eItr();
		HE_Halfedge he;
		while (eItr.hasNext()) {
			he = eItr.next();
			for (int i = 0; i < planes.size(); i++) {
				if (WB_GeometryOp.classifySegmentToPlane3D(he.getVertex(), he.getEndVertex(),
						planes.get(i)) == WB_Classification.ON) {
					he.setInternalLabel(1);
					newEdges.addEdge(he);
					break;
				}
			}
		}
		mesh.addSelection("cuts", this, cutFaces);
		mesh.addSelection("edges", this, newEdges);
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		selection.getParent().resetFaceInternalLabels();
		selection.getParent().resetEdgeInternalLabels();
		cutFaces = HE_Selection.getSelection(selection.getParent());
		newEdges = HE_Selection.getSelection(selection.getParent());
		if (planes == null) {
			return selection.getParent();
		}
		final HEM_SliceSurface slice = new HEM_SliceSurface();
		boolean unique = true;
		WB_Plane Pi, Pj;
		for (int i = 0; i < planes.size(); i++) {
			Pi = planes.get(i);
			unique = true;
			for (int j = 0; j < i; j++) {
				Pj = planes.get(j);
				if (WB_GeometryOp.isEqual(Pi, Pj)) {
					unique = false;
					break;
				}
			}
			if (unique) {

				slice.setPlane(Pi).setOffset(offset);
				slice.apply(selection);
				if (selection.getParent().getSelection("cuts") != null) {
					cutFaces.add(selection.getParent().getSelection("cuts"));
				}
			}
		}
		selection.getParent().resetEdgeInternalLabels();
		cutFaces.cleanSelection();
		cutFaces.collectEdgesByFace();
		final Iterator<HE_Halfedge> eItr = cutFaces.eItr();
		HE_Halfedge he;
		while (eItr.hasNext()) {
			he = eItr.next();
			for (int i = 0; i < planes.size(); i++) {
				if (WB_GeometryOp.classifySegmentToPlane3D(he.getVertex(), he.getEndVertex(),
						planes.get(i)) == WB_Classification.ON) {
					he.setInternalLabel(1);
					newEdges.addEdge(he);
					break;
				}
			}
		}
		selection.getParent().addSelection("cuts", this, cutFaces);
		selection.getParent().addSelection("edges", this, newEdges);
		return selection.getParent();
	}
}
