package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordList;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_KDTreeInteger3D;
import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_RandomOnSphere;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

public class HEC_VoronoiSphere extends HEC_Creator {
	private List<WB_Coord> points;
	private int numberOfPoints;
	private int cellIndex;
	private int level;
	private double cutoff;
	private WB_Vector[] dir;
	private boolean approx;
	private int numTracers;
	private double traceStep;
	private final WB_RandomOnSphere randomGen;
	private double offset;

	public HEC_VoronoiSphere() {
		super();
		level = 1;
		traceStep = 10;
		numTracers = 100;
		setOverride(true);
		randomGen = new WB_RandomOnSphere();
	}

	public HEC_VoronoiSphere setCellIndex(final int i) {
		cellIndex = i;
		return this;
	}

	public HEC_VoronoiSphere setLevel(final int l) {
		level = l;
		return this;
	}

	public HEC_VoronoiSphere setNumTracers(final int n) {
		numTracers = n;
		return this;
	}

	public HEC_VoronoiSphere setTraceStep(final double d) {
		traceStep = d;
		return this;
	}

	public HEC_VoronoiSphere setCutoff(final double c) {
		cutoff = Math.abs(c);
		return this;
	}

	public HEC_VoronoiSphere setApprox(final boolean a) {
		approx = a;
		return this;
	}

	public HEC_VoronoiSphere setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	public HEC_VoronoiSphere setOffset(final double o) {
		offset = o;
		return this;
	}

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
			final ArrayList<WB_Plane> cutPlanes = new ArrayList<>();
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
			msm.setPlanes(cutPlanes).setCenter(new WB_Point(points.get(cellIndex)));
			result.modify(msm);
		}
		return result;
	}

	private void grow(final WB_Point[] tracers, final int index, final double offset) {
		final WB_KDTreeInteger3D<WB_Coord> kdtree = new WB_KDTreeInteger3D<>();
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
					p.addSelf(stepSize * r.xd(), stepSize * r.yd(), stepSize * r.zd());
					d2self = WB_GeometryOp3D.getSqDistance3D(p, c);
					final WB_KDEntryInteger<WB_Coord>[] closest = kdtree.getNearestNeighbors(p, 1);
					j = closest[0].value;
				}
				if (j != index) {
					p.subSelf(stepSize * r.xd(), stepSize * r.yd(), stepSize * r.zd());
					d2self = 0;
					stepSize /= 2;
				} else {
					p.set(c.xd() + cutoff * r.xd(), c.yd() + cutoff * r.yd(), c.zd() + cutoff * r.zd());
					stepSize = -1;
				}
			}
			p.addSelf(-offset * r.xd(), -offset * r.yd(), -offset * r.zd());
		}
	}

	public HEC_VoronoiSphere setPoints(final Collection<? extends WB_Coord> points) {
		this.points = new WB_CoordList();
		this.points.addAll(points);
		return this;
	}

	public HEC_VoronoiSphere setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEC_VoronoiSphere setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			this.points.add(new WB_Point(points[i][0], points[i][1], points[i][2]));
		}
		return this;
	}

	public HEC_VoronoiSphere setPoints(final WB_Coord[] points) {
		this.points = new WB_CoordList();
		for (final WB_Coord p : points) {
			this.points.add(p);
		}
		return this;
	}
}
