/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_KDTreeInteger3D;
import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_RandomOnSphere;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 * Creates the Voronoi cell of one point in a collection of points, constrained
 * by a maximum radius.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_VoronoiSphere extends HEC_Creator {
	/** points. */
	private List<WB_Coord>			points;
	/** Number of points. */
	private int						numberOfPoints;
	/** Cell index. */
	private int						cellIndex;
	/** Level of geodesic sphere in exact mode. */
	private int						level;
	/** Maximum radius. */
	private double					cutoff;
	/** Point directions. */
	private WB_Vector[]				dir;
	/** Approximate mode?. */
	private boolean					approx;
	/** Number of tracer points to use in approximate mode?. */
	private int						numTracers;
	/** Starting trace step in approximate mode?. */
	private double					traceStep;
	/** The random gen. */
	private final WB_RandomOnSphere	randomGen;
	private double					offset;

	/**
	 * Instantiates a new HEC_VoronoiSphere.
	 *
	 */
	public HEC_VoronoiSphere() {
		super();
		level = 1;
		traceStep = 10;
		numTracers = 100;
		setOverride(true);
		randomGen = new WB_RandomOnSphere();
	}

	/**
	 * Set index of cell to create.
	 *
	 * @param i
	 *            index
	 * @return self
	 */
	public HEC_VoronoiSphere setCellIndex(final int i) {
		cellIndex = i;
		return this;
	}

	/**
	 * Set level of geodesic sphere in each cell.
	 *
	 * @param l
	 *            recursive level
	 * @return self
	 */
	public HEC_VoronoiSphere setLevel(final int l) {
		level = l;
		return this;
	}

	/**
	 * Set number of tracer points to use in approximate model.
	 *
	 * @param n
	 *            number of tracer points
	 * @return self
	 */
	public HEC_VoronoiSphere setNumTracers(final int n) {
		numTracers = n;
		return this;
	}

	/**
	 * Set initial trace step size.
	 *
	 * @param d
	 *            trace step
	 * @return self
	 */
	public HEC_VoronoiSphere setTraceStep(final double d) {
		traceStep = d;
		return this;
	}

	/**
	 * Set maximum radius of cell.
	 *
	 * @param c
	 *            cutoff radius
	 * @return self
	 */
	public HEC_VoronoiSphere setCutoff(final double c) {
		cutoff = Math.abs(c);
		return this;
	}

	/**
	 * Set approximate mode.
	 *
	 * @param a
	 *            true, false
	 * @return self
	 */
	public HEC_VoronoiSphere setApprox(final boolean a) {
		approx = a;
		return this;
	}

	/**
	 * Set seed of random generator.
	 *
	 * @param seed
	 *            seed
	 * @return self
	 */
	public HEC_VoronoiSphere setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	public HEC_VoronoiSphere setOffset(final double o) {
		offset = o;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		randomGen.reset();
		if (cutoff == 0) {
			return new HE_Mesh();
		}
		if (points == null) {
			return new HE_Mesh();
		}
		numberOfPoints = points.size();
		if (cellIndex < 0 || cellIndex >= numberOfPoints) {
			return new HE_Mesh();
		}
		HE_Mesh result;
		if (approx) {
			final WB_Point[] tracers = new WB_Point[numTracers];
			for (int i = 0; i < numTracers; i++) {
				tracers[i] = randomGen.nextPoint();
			}
			dir = new WB_Vector[numTracers];
			for (int i = 0; i < numTracers; i++) {
				dir[i] = new WB_Vector(tracers[i]);
				dir[i].normalizeSelf();
				tracers[i].addSelf(points.get(cellIndex));
			}
			grow(tracers, cellIndex, offset);
			final HEC_ConvexHull ch = new HEC_ConvexHull().setPoints(tracers);
			result = new HE_Mesh(ch);
		} else {
			final HEC_Geodesic gc = new HEC_Geodesic().setB(level).setC(0);
			gc.setCenter(points.get(cellIndex));
			gc.setRadius(cutoff);
			result = new HE_Mesh(gc);
			final ArrayList<WB_Plane> cutPlanes = new ArrayList<WB_Plane>();
			for (int j = 0; j < numberOfPoints; j++) {
				if (cellIndex != j) {
					final WB_Vector N = new WB_Vector(points.get(cellIndex));
					N.subSelf(points.get(j));
					N.normalizeSelf();
					final WB_Point O = new WB_Point(points.get(cellIndex)); // plane
					// origin=point
					// halfway
					// between point i and point j
					O.addSelf(points.get(j));
					O.mulSelf(0.5);
					if (offset != 0) {
						O.addSelf(N.mul(offset));
					}
					final WB_Plane P = new WB_Plane(O, N);
					cutPlanes.add(P);
				}
			}
			final HEM_MultiSlice msm = new HEM_MultiSlice();
			msm.setPlanes(cutPlanes)
					.setCenter(new WB_Point(points.get(cellIndex)));
			result.modify(msm);
		}
		return result;
	}

	/**
	 *
	 * @param tracers
	 * @param index
	 * @param offset
	 */
	private void grow(final WB_Point[] tracers, final int index,
			final double offset) {
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<WB_Coord>();
		for (int i = 0; i < numberOfPoints; i++) {
			kdtree.add(points.get(i), i);
		}
		final WB_Point c = new WB_Point(points.get(index));
		WB_Point p;
		WB_Vector r;
		double d2self = 0;
		for (int i = 0; i < numTracers; i++) {
			p = tracers[i];
			r = dir[i];
			d2self = 0;
			double stepSize = traceStep;
			int j = index;
			while (stepSize > WB_Epsilon.EPSILON) {
				while (j == index && d2self < cutoff * cutoff) {
					p.addSelf(stepSize * r.xd(), stepSize * r.yd(),
							stepSize * r.zd());
					d2self = WB_CoordOp.getSqDistance3D(p, c);
					final WB_KDEntryInteger<WB_Coord>[] closest = kdtree
							.getNearestNeighbors(p, 1);
					j = closest[0].value;
				}
				if (j != index) {
					p.subSelf(stepSize * r.xd(), stepSize * r.yd(),
							stepSize * r.zd());
					d2self = 0;
					stepSize /= 2;
				} else {
					p.set(c.xd() + cutoff * r.xd(), c.yd() + cutoff * r.yd(),
							c.zd() + cutoff * r.zd());
					stepSize = -1;
				}
			}
			p.addSelf(-offset * r.xd(), -offset * r.yd(), -offset * r.zd());
		}
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            collection of vertex positions
	 * @return self
	 */
	public HEC_VoronoiSphere setPoints(
			final Collection<? extends WB_Coord> points) {
		this.points = new FastList<WB_Coord>();
		this.points.addAll(points);
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            2D array of double of vertex positions
	 * @return self
	 */
	public HEC_VoronoiSphere setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < n; i++) {
			this.points.add(
					new WB_Point(points[i][0], points[i][1], points[i][2]));
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
	public HEC_VoronoiSphere setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new FastList<WB_Coord>();
		for (int i = 0; i < n; i++) {
			this.points.add(
					new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	/**
	 * Set points that define cell centers.
	 *
	 * @param points
	 *            array of vertex positions
	 * @return self
	 */
	public HEC_VoronoiSphere setPoints(final WB_Coord[] points) {
		this.points = new FastList<WB_Coord>();
		for (WB_Coord p : points) {
			this.points.add(p);
		}
		return this;
	}
}
