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

import wblut.geom.WB_Coord;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Creates the Voronoi cell of one point in a collection of points, constrained
 * by a mesh.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_VoronoiCell extends HEC_Creator {
	/** Points. */
	private WB_Coord[] points;
	/** Number of points. */
	private int numberOfPoints;
	/** Use specific subselection of points. */
	private int[] pointsToUse;
	/** Cell index. */
	private int cellIndex;
	/** Container. */
	private HE_Mesh container;
	/** Treat container as surface?. */
	private boolean surface;

	/** Offset. */
	private WB_ScalarParameter offset;
	/** Faces fully interior to cell. */
	public HE_Selection inner;
	/** Faces part of container. */
	public HE_Selection outer;
	/** The limit points. */
	private boolean limitPoints;

	/**
	 * Instantiates a new HEC_VoronoiCell.
	 *
	 */
	public HEC_VoronoiCell() {
		super();
		setOverride(true);
		offset = WB_ScalarParameter.ZERO;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            array of vertex positions
	 * @return self
	 */
	public HEC_VoronoiCell setPoints(final WB_Coord[] points) {
		this.points = points;
		return this;
	}

	public HEC_VoronoiCell setPoints(final Collection<? extends WB_Coord> points) {
		this.points = new WB_Coord[points.size()];
		int i = 0;
		for (WB_Coord p : points) {
			this.points[i++] = p;
		}
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            2D array of double of vertex positions
	 * @return self
	 */
	public HEC_VoronoiCell setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new WB_Coord[n];
		for (int i = 0; i < n; i++) {
			this.points[i] = new WB_Point(points[i][0], points[i][1], points[i][2]);
		}
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            2D array of float of vertex positions
	 * @return self
	 */
	public HEC_VoronoiCell setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new WB_Point[n];
		for (int i = 0; i < n; i++) {
			this.points[i] = new WB_Point(points[i][0], points[i][1], points[i][2]);
		}
		return this;
	}

	/**
	 * Sets the points to use.
	 *
	 * @param pointsToUse
	 *            the points to use
	 * @return the hE c_ voronoi cell
	 */
	public HEC_VoronoiCell setPointsToUse(final int[] pointsToUse) {
		this.pointsToUse = pointsToUse;
		return this;
	}

	/**
	 * Sets the points to use.
	 *
	 * @param pointsToUse
	 *            the points to use
	 * @return the hE c_ voronoi cell
	 */
	public HEC_VoronoiCell setPointsToUse(final ArrayList<Integer> pointsToUse) {
		final int n = pointsToUse.size();
		this.pointsToUse = new int[n];
		for (int i = 0; i < n; i++) {
			this.pointsToUse[i] = pointsToUse.get(i);
		}
		return this;
	}

	/**
	 * Set index of cell to create.
	 *
	 * @param i
	 *            index
	 * @return self
	 */
	public HEC_VoronoiCell setCellIndex(final int i) {
		cellIndex = i;
		return this;
	}

	/**
	 * Set voronoi cell offset.
	 *
	 * @param o
	 *            offset
	 * @return self
	 */
	public HEC_VoronoiCell setOffset(final double o) {
		offset = new WB_ConstantScalarParameter(o);
		return this;
	}

	/**
	 * Set voronoi cell offset.
	 *
	 * @param o
	 *            offset
	 * @return self
	 */
	public HEC_VoronoiCell setOffset(final WB_ScalarParameter o) {
		offset = o;
		return this;
	}

	/**
	 * Set enclosing mesh limiting cells.
	 *
	 * @param container
	 *            enclosing mesh
	 * @return self
	 */
	public HEC_VoronoiCell setContainer(final HE_Mesh container) {
		this.container = container;
		return this;
	}

	/**
	 * Limit the points considered to those indices specified in the
	 * pointsToUseArray.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEC_VoronoiCell setLimitPoints(final boolean b) {
		limitPoints = b;
		return this;
	}

	/**
	 * Set optional surface mesh mode.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEC_VoronoiCell setSurface(final boolean b) {
		surface = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		if (container == null) {
			return new HE_Mesh();
		}
		if (points == null) {
			return container;
		}
		numberOfPoints = points.length;

		if (cellIndex < 0 || cellIndex >= numberOfPoints) {
			return new HE_Mesh();
		}
		final HE_Mesh result = container.copy();
		result.setInternalLabel(cellIndex);
		result.setUserLabel(cellIndex);
		result.resetFaceInternalLabels();
		final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
		int id = 0;
		final WB_Point O = new WB_Point();
		WB_Plane P;
		final int[] labels;
		if (limitPoints) {
			labels = new int[pointsToUse.length];
			for (final int element : pointsToUse) {
				if (cellIndex != element) {
					final WB_Vector N = new WB_Vector(points[cellIndex]);
					N.subSelf(points[element]);
					N.normalizeSelf();
					O.set(points[cellIndex]); // plane origin=point halfway
					// between point i and point j
					O.addSelf(points[element]);
					O.mulSelf(0.5);

					O.addSelf(N.mul(
							offset.evaluate(points[cellIndex].xd(), points[cellIndex].yd(), points[cellIndex].zd())));

					P = new WB_Plane(O, N);
					cutPlanes.add(P);
					labels[id] = element;
					id++;
				}
			}
		} else {
			labels = new int[numberOfPoints - 1];
			for (int j = 0; j < numberOfPoints; j++) {
				if (cellIndex != j) {
					final WB_Vector N = new WB_Vector(points[cellIndex]);
					N.subSelf(points[j]);
					N.normalizeSelf();
					O.set(points[cellIndex]); // plane origin=point halfway
					// between point i and point j
					O.addSelf(points[j]);
					O.mulSelf(0.5);
					O.addSelf(N.mul(
							offset.evaluate(points[cellIndex].xd(), points[cellIndex].yd(), points[cellIndex].zd())));
					P = new WB_Plane(O, N);
					cutPlanes.add(P);
					labels[id] = j;
					id++;
				}
			}
		}
		final HEM_MultiSlice msm = new HEM_MultiSlice();
		msm.setPlanes(cutPlanes).setCenter(new WB_Point(points[cellIndex])).setCap(!surface).setLabels(labels);

		try {
			msm.applySelf(result);

		} catch (Exception e) {
			return new HE_Mesh();
		}

		inner = HE_Selection.getSelection(result);

		for (int i = 0; i < labels.length; i++) {
			final HE_Selection sel = result.selectFacesWithInternalLabel(labels[i]);
			if (sel.getNumberOfFaces() > 0) {
				final HE_FaceIterator fitr = sel.fItr();
				while (fitr.hasNext()) {
					inner.add(fitr.next());
				}

			}
		}

		outer = msm.origFaces;
		return result;
	}
}
