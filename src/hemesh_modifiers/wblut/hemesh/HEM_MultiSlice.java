package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;

/**
 *
 */
public class HEM_MultiSlice extends HEM_Modifier {
	/**  */
	private ArrayList<WB_Plane> planes;
	/**  */
	private int[] labels;
	/**  */
	private boolean reverse = false;
	/**  */
	private WB_Point center;
	/**  */
	private boolean capHoles = true;
	/**  */
	private boolean optimizeCap = true;
	/**  */
	private boolean triangulate = false;
	/**  */
	public HE_Selection origFaces;
	/**  */
	private double offset;

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_MultiSlice setOffset(final double d) {
		offset = d;
		return this;
	}

	/**
	 *
	 */
	public HEM_MultiSlice() {
		super();
	}

	/**
	 *
	 *
	 * @param planes
	 * @return
	 */
	public HEM_MultiSlice setPlanes(final Collection<WB_Plane> planes) {
		this.planes = new ArrayList<>();
		this.planes.addAll(planes);
		return this;
	}

	/**
	 *
	 *
	 * @param planes
	 * @return
	 */
	public HEM_MultiSlice setPlanes(final WB_Plane[] planes) {
		this.planes = new ArrayList<>();
		for (final WB_Plane plane : planes) {
			this.planes.add(plane);
		}
		return this;
	}

	/**
	 *
	 *
	 * @param labels
	 * @return
	 */
	public HEM_MultiSlice setLabels(final int[] labels) {
		this.labels = labels;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_MultiSlice setReverse(final Boolean b) {
		reverse = b;
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEM_MultiSlice setCenter(final WB_Point c) {
		center = c.copy();
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_MultiSlice setTriangulate(final boolean b) {
		triangulate = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_MultiSlice setCap(final Boolean b) {
		capHoles = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_MultiSlice setOptimizeCap(final boolean b) {
		optimizeCap = b;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		origFaces = HE_Selection.getSelection(mesh);
		final HE_Selection capFaces = HE_Selection.getSelection(mesh);
		if (planes == null) {
			return mesh;
		}
		if (labels == null) {
			labels = new int[planes.size()];
			for (int i = 0; i < planes.size(); i++) {
				labels[i] = i;
			}
		}
		HE_FaceIterator fItr = mesh.fItr();
		mesh.resetFaceInternalLabels();
		final HEM_Slice slice = new HEM_Slice();
		slice.setReverse(reverse).setCap(capHoles).setOffset(offset).setOptimizeCap(optimizeCap);
		if (center != null) {
			final double[] r = new double[planes.size()];
			for (int i = 0; i < planes.size(); i++) {
				final WB_Plane P = planes.get(i);
				r[i] = WB_GeometryOp.getSqDistance3D(P.getOrigin(), center);
			}
			for (int i = planes.size(); --i >= 0;) {
				for (int m = 0; m < i; m++) {
					if (r[m] > r[m + 1]) {
						Collections.swap(planes, m, m + 1);
						final double tmp = r[m];
						r[m] = r[m + 1];
						r[m + 1] = tmp;
						final int tmpid = labels[m];
						labels[m] = labels[m + 1];
						labels[m + 1] = tmpid;
					}
				}
			}
		}
		boolean unique = false;
		WB_Plane Pi;
		WB_Plane Pj;
		final int stop = planes.size();
		for (int i = 0; i < stop; i++) {
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
				slice.setPlane(Pi);
				slice.applySelf(mesh);
				if (mesh.getNumberOfVertices() == 0) {
					return mesh;
				}
				fItr = mesh.getSelection("caps").fItr();
				HE_Face f;
				while (fItr.hasNext()) {
					f = fItr.next();
					f.setInternalLabel(labels[i]);
				}
				if (triangulate) {
					HE_MeshOp.triangulateConcaveFaces(mesh);
				}
				mesh.removeSelection("caps");
			}
		}
		fItr = mesh.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() == -1) {
				origFaces.add(f);
			} else {
				capFaces.add(f);
			}
		}
		mesh.addSelection("caps", this, capFaces);
		return mesh;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
