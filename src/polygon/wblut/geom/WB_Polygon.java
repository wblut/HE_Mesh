/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_Polygon extends WB_Ring implements WB_TriangleFactory {
	static boolean USE_JTS=true;
	static boolean OPTIMIZE_DEFAULT=true;
	
	/**
	 *
	 */
	int[]									triangles;
	/**
	 *
	 */
	int										numberOfContours;
	/**
	 *
	 */
	int[]									numberOfPointsPerContour;
	/**
	 *
	 */
	int										numberOfShellPoints;
	/**
	 *
	 */
	private static final WB_GeometryFactory	gf	= new WB_GeometryFactory();

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Polygon(final Collection<? extends WB_Coord> points) {
		numberOfPoints = points.size();
		numberOfShellPoints = points.size();
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : points) {
			this.points.add(new WB_Point(p));
		}
		calculateDirections();
		numberOfContours = 1;
		numberOfPointsPerContour = new int[] { numberOfPoints };
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Polygon(final WB_CoordCollection points) {
		numberOfPoints = points.size();
		numberOfShellPoints = points.size();
		this.points = new FastList<WB_Point>();
		for (int i = 0; i < points.size(); i++) {
			this.points.add(new WB_Point(points.get(i)));
		}
		calculateDirections();
		numberOfContours = 1;
		numberOfPointsPerContour = new int[] { numberOfPoints };
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Polygon() {
		numberOfPoints = 0;
		numberOfShellPoints = 0;
		this.points = new FastList<WB_Point>();
		numberOfContours = 0;
		numberOfPointsPerContour = new int[] {};
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Polygon(final WB_Coord... points) {
		numberOfPoints = points.length;
		numberOfShellPoints = points.length;
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : points) {
			this.points.add(new WB_Point(p));
		}
		calculateDirections();
		numberOfContours = 1;
		numberOfPointsPerContour = new int[] { numberOfPoints };
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 */
	public WB_Polygon(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> innerpoints) {
		numberOfShellPoints = points.size();
		numberOfPoints = points.size() + innerpoints.size();
		final ArrayList<WB_Coord> tmp = new ArrayList<WB_Coord>();
		tmp.addAll(points);
		tmp.addAll(innerpoints);
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : tmp) {
			this.points.add(new WB_Point(p));
		}
		calculateDirections();
		numberOfContours = 2;
		numberOfPointsPerContour = new int[] { numberOfShellPoints,
				innerpoints.size() };
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 */
	public WB_Polygon(final WB_Coord[] points, final WB_Coord[] innerpoints) {
		numberOfShellPoints = points.length;
		numberOfPoints = points.length + innerpoints.length;
		final ArrayList<WB_Coord> tmp = new ArrayList<WB_Coord>();
		for (final WB_Coord p : points) {
			tmp.add(p);
		}
		for (final WB_Coord p : innerpoints) {
			tmp.add(p);
		}
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : tmp) {
			this.points.add(new WB_Point(p));
		}
		calculateDirections();
		numberOfContours = 2;
		numberOfPointsPerContour = new int[] { numberOfShellPoints,
				innerpoints.length };
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 */
	public WB_Polygon(final Collection<? extends WB_Coord> points,
			final List<? extends WB_Coord>[] innerpoints) {
		numberOfShellPoints = points.size();
		numberOfPoints = points.size();
		final ArrayList<WB_Coord> tmp = new ArrayList<WB_Coord>();
		for (final WB_Coord p : points) {
			tmp.add(p);
		}
		numberOfContours = innerpoints.length + 1;
		numberOfPointsPerContour = new int[innerpoints.length + 1];
		numberOfPointsPerContour[0] = numberOfShellPoints;
		int i = 1;
		for (final List<? extends WB_Coord> hole : innerpoints) {
			for (final WB_Coord p : hole) {
				tmp.add(p);
			}
			numberOfPointsPerContour[i++] = hole.size();
			numberOfPoints += hole.size();
		}
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : tmp) {
			this.points.add(new WB_Point(p));
		}
		calculateDirections();
	}

	/**
	 *
	 *
	 * @param points
	 * @param innerpoints
	 */
	public WB_Polygon(final WB_Coord[] points, final WB_Coord[][] innerpoints) {
		numberOfShellPoints = points.length;
		numberOfPoints = points.length;
		final ArrayList<WB_Coord> tmp = new ArrayList<WB_Coord>();
		for (final WB_Coord p : points) {
			tmp.add(p);
		}
		numberOfContours = innerpoints.length + 1;
		numberOfPointsPerContour = new int[innerpoints.length + 1];
		numberOfPointsPerContour[0] = numberOfShellPoints;
		int i = 1;
		for (final WB_Coord[] hole : innerpoints) {
			for (final WB_Coord p : hole) {
				tmp.add(p);
			}
			numberOfPointsPerContour[i++] = hole.length;
			numberOfPoints += hole.length;
		}
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : tmp) {
			this.points.add(new WB_Point(p));
		}
		calculateDirections();
	}

	public WB_Polygon(WB_Polygon polygon) {
		points = new FastList<WB_Point>();
		for (WB_Point p : polygon.points) {
			points.add(new WB_Point(p));
		}
		numberOfContours = polygon.numberOfContours;
		numberOfPoints = polygon.numberOfPoints;
		numberOfShellPoints = polygon.numberOfShellPoints;
		numberOfPointsPerContour = new int[polygon.numberOfPointsPerContour.length];
		for (int i = 0; i < numberOfPointsPerContour.length; i++) {
			numberOfPointsPerContour[i] = polygon.numberOfPointsPerContour[i];
		}
		calculateDirections();
	}

	/**
	 *
	 */
	private void calculateDirections() {
		directions = new FastList<WB_Vector>();
		incLengths = new double[numberOfPoints];
		int offset = 0;
		for (int j = 0; j < numberOfContours; j++) {
			final int n = numberOfPointsPerContour[j];
			for (int i = 0; i < n; i++) {
				final int in = offset + (i + 1) % n;
				final WB_Vector v = new WB_Vector(points.get(offset + i),
						points.get(in));
				incLengths[offset + i] = i == 0 ? v.getLength()
						: incLengths[offset + i - 1] + v.getLength();
				v.normalizeSelf();
				directions.add(v);
			}
			offset += n;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Ring#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Polygon)) {
			return false;
		}
		final WB_Polygon L = (WB_Polygon) o;
		if (getNumberOfPoints() != L.getNumberOfPoints()) {
			return false;
		}
		for (int i = 0; i < numberOfPoints; i++) {
			if (!getPoint(i).equals(L.getPoint(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getNumberOfPoints() {
		return points.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfShellPoints() {
		return numberOfShellPoints;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfHoles() {
		return numberOfContours - 1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfContours() {
		return numberOfContours;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int[] getNumberOfPointsPerContour() {
		return numberOfPointsPerContour;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int[] getTriangles() {
		return getTriangles(OPTIMIZE_DEFAULT);
	}

	/**
	 *
	 *
	 * @param optimize
	 * @return
	 */
	public int[] getTriangles(final boolean optimize) {
		if (triangles == null) {
			if (numberOfShellPoints == 0) {
				return new int[] {};
			} else if (numberOfShellPoints < 3) {
				return new int[] { 0, 0, 0 };
			} else if (numberOfShellPoints == 3 && numberOfContours == 1) {
				return new int[] { 0, 1, 2 };
			} else if (numberOfShellPoints == 4 && numberOfContours == 1) {
				return WB_JTS.PolygonTriangulatorJTS.triangulateQuad(
						points.get(0), points.get(1), points.get(2),
						points.get(3));
			} else {
				if(USE_JTS) {
				final WB_Triangulation2D triangulation = new WB_JTS.PolygonTriangulatorJTS()
						.triangulatePolygon2D(this.toPolygon2DOrtho(),
								optimize);
				triangles = triangulation.getTriangles();
				}else {
				triangles=WB_Poly2Tri.triangulatePolygon(this);
				}
			}
		}
		return triangles;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public WB_Plane getPlane(final double d) {
		final WB_Vector normal = getNormal();
		if (normal.getSqLength() < 0.5) {
			return null;
		}
		return gf.createPlane(points.get(0).addMul(d, normal), normal);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Plane getPlane() {
		return getPlane(0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Vector getNormal() {
		final WB_Vector normal = gf.createVector();
		int ni;
		final int nsp = getNumberOfShellPoints();
		for (int i = 0; i < nsp; i++) {
			ni = (i + 1) % nsp;
			normal.addSelf(
					(points.get(i).yd() - points.get(ni).yd())
							* (points.get(i).zd() + points.get(ni).zd()),
					(points.get(i).zd() - points.get(ni).zd())
							* (points.get(i).xd() + points.get(ni).xd()),
					(points.get(i).xd() - points.get(ni).xd())
							* (points.get(i).yd() + points.get(ni).yd()));
		}
		normal.normalizeSelf();
		return normal;
	}

	public WB_Point getCenter() {
		final int nsp = getNumberOfShellPoints();
		WB_Point center = new WB_Point();
		for (int i = 0; i < nsp; i++) {
			center.addSelf(points.get(i));
		}
		center.divSelf(nsp);
		return center;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Ring#getPoint(int)
	 */
	@Override
	public WB_Point getPoint(final int i) {
		return points.get(i);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Ring#getd(int, int)
	 */
	@Override
	public double getd(final int i, final int j) {
		return points.get(i).getd(j);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Ring#getf(int, int)
	 */
	@Override
	public float getf(final int i, final int j) {
		return points.get(i).getf(j);
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isSimple() {
		return numberOfContours == 1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isCW2D() {
		// Find shell point with min x and if equal x, max y
		int index = 0;
		WB_Point extremum = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).xd() < extremum.xd()) {
				extremum = points.get(i);
				index = i;
			} else if (points.get(i).xd() == extremum.xd()
					&& points.get(i).yd() > extremum.yd()) {
				extremum = points.get(i);
				index = i;
			}
		}
		WB_Point previous;
		if (index == 0) {
			previous = points.get(numberOfShellPoints - 1);
		} else {
			previous = points.get(index - 1);
		}
		WB_Point next;
		if (index == numberOfShellPoints - 1) {
			next = points.get(0);
		} else {
			next = points.get(index + 1);
		}
		// get orientation
		return WB_Predicates.orient2D(previous, extremum, next) <= 0;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Polygon toPolygon2D() {
		final List<WB_Point> shellpoints = new FastList<WB_Point>();
		final WB_Plane P = getPlane(0);
		final WB_PlanarMap EP = new WB_PlanarMap(P);
		for (int i = 0; i < numberOfShellPoints; i++) {
			final WB_Point p2D = new WB_Point();
			EP.mapPoint3D(points.get(i), p2D);
			shellpoints.add(p2D);
		}
		if (isSimple()) {
			return new WB_Polygon(shellpoints);
		} else {
			@SuppressWarnings("unchecked")
			final List<WB_Point>[] holepoints = new FastList[numberOfContours
					- 1];
			int index = numberOfShellPoints;
			for (int i = 0; i < numberOfContours - 1; i++) {
				holepoints[i] = new FastList<WB_Point>();
				for (int j = 0; j < numberOfPointsPerContour[i + 1]; j++) {
					final WB_Point p2D = new WB_Point();
					EP.mapPoint3D(points.get(index++), p2D);
					holepoints[i].add(p2D);
				}
			}
			return new WB_Polygon(shellpoints, holepoints);
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Polygon toPolygon2DOrtho() {
		final List<WB_Point> shellpoints = new FastList<WB_Point>();
		final WB_Plane P = getPlane(0);
		final WB_OrthoProject OP = new WB_OrthoProject(P);
		for (int i = 0; i < numberOfShellPoints; i++) {
			final WB_Point p2D = new WB_Point();
			OP.mapPoint3D(points.get(i), p2D);
			shellpoints.add(p2D);
		}
		if (isSimple()) {
			return new WB_Polygon(shellpoints);
		} else {
			@SuppressWarnings("unchecked")
			final List<WB_Point>[] holepoints = new FastList[numberOfContours
					- 1];
			int index = numberOfShellPoints;
			for (int i = 0; i < numberOfContours - 1; i++) {
				holepoints[i] = new FastList<WB_Point>();
				for (int j = 0; j < numberOfPointsPerContour[i + 1]; j++) {
					final WB_Point p2D = new WB_Point();
					OP.mapPoint3D(points.get(index++), p2D);
					holepoints[i].add(p2D);
				}
			}
			return new WB_Polygon(shellpoints, holepoints);
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean is2D() {
		for (int i = 0; i < numberOfShellPoints; i++) {
			if (!WB_Epsilon.isZero(points.get(i).zd())) {
				return false;
			}
		}
		int index = numberOfShellPoints;
		for (int i = 0; i < numberOfContours - 1; i++) {
			for (int j = 0; j < numberOfPointsPerContour[i + 1]; j++) {
				if (!WB_Epsilon.isZero(points.get(index++).zd())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isPlanar() {
		final WB_Plane P = getPlane(0);
		for (int i = 0; i < numberOfShellPoints; i++) {
			if (!WB_Epsilon
					.isZero(WB_GeometryOp.getDistance3D(points.get(i), P))) {
				return false;
			}
		}
		int index = numberOfShellPoints;
		for (int i = 0; i < numberOfContours - 1; i++) {
			for (int j = 0; j < numberOfPointsPerContour[i + 1]; j++) {
				if (!WB_Epsilon.isZero(WB_GeometryOp
						.getDistance3D(points.get(index++), P))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @param i
	 * @param CW
	 * @return
	 */
	public boolean isConvex2D(final int i, final boolean CW) {
		final WB_Point extremum = points.get(i);
		WB_Point previous;
		if (i == 0) {
			previous = points.get(numberOfShellPoints - 1);
		} else {
			previous = points.get(i - 1);
		}
		WB_Point next;
		if (i == numberOfShellPoints - 1) {
			next = points.get(0);
		} else {
			next = points.get(i + 1);
		}
		final boolean vertexIsCW = WB_Predicates.orient2D(previous, extremum,
				next) <= 0;
		return vertexIsCW == CW;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isConvex2D() {
		double dx1, dy1, dx2, dy2, zcrossproduct;
		double sign = Double.NaN;
		for (int i = 0, im = numberOfShellPoints - 1, imm = numberOfShellPoints
				- 2; i < numberOfShellPoints; imm = im, im = i, i++) {
			dx1 = points.get(i).xd() - points.get(im).xd();
			dy1 = points.get(i).yd() - points.get(im).yd();
			dx2 = points.get(imm).xd() - points.get(im).xd();
			dy2 = points.get(imm).yd() - points.get(im).yd();
			zcrossproduct = dx1 * dy2 - dy1 * dx2;
			if (Double.isNaN(sign)) {
				if (zcrossproduct != 0.0) {
					sign = zcrossproduct < 0 ? -1 : 1;
				}
			} else {
				if (zcrossproduct < 0 && sign > 0) {
					return false;
				} else if (zcrossproduct > 0 && sign < 0) {
					return false;
				}
			}
		}
		if (Double.isNaN(sign)) {
			return false;
		}
		return true;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Polygon negate() {
		final List<WB_Point> shellpoints = new FastList<WB_Point>();
		for (int i = numberOfShellPoints - 1; i >= 0; i--) {
			shellpoints.add(new WB_Point(points.get(i)));
		}
		if (isSimple()) {
			return new WB_Polygon(shellpoints);
		} else {
			@SuppressWarnings("unchecked")
			final List<WB_Point>[] holepoints = new FastList[numberOfContours
					- 1];
			int index = numberOfShellPoints;
			for (int i = 0; i < numberOfContours - 1; i++) {
				holepoints[i] = new FastList<WB_Point>();
				for (int j = numberOfPointsPerContour[i + 1] - 1; j >= 0; j--) {
					holepoints[i].add(new WB_Point(points.get(index++)));
				}
			}
			return new WB_Polygon(shellpoints, holepoints);
		}
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public WB_Polygon[] splitPolygon(final WB_Plane P) {
		return WB_GeometryOp.splitPolygon(this, P);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public WB_Polygon trimConvexPolygon(final double d) {
		return WB_GeometryOp.trimConvexPolygon(this, d);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public WB_Polygon trimConvexPolygon(final double[] d) {
		return WB_GeometryOp.trimConvexPolygon(this, d);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public List<WB_Polygon> trimPolygon2D(final double d) {
		return gf.createBufferedPolygons2D(this, -d);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Coord closestPoint(final WB_Coord p) {
		double d = Double.POSITIVE_INFINITY;
		int id = -1;
		for (int i = 0; i < this.numberOfShellPoints; i++) {
			final double cd = WB_CoordOp.getSqDistance3D(p, getPoint(i));
			if (cd < d) {
				id = i;
				d = cd;
			}
		}
		return getPoint(id);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public int closestIndex(final WB_Coord p) {
		double d = Double.POSITIVE_INFINITY;
		int id = -1;
		for (int i = 0; i < this.numberOfShellPoints; i++) {
			final double cd = WB_CoordOp.getSqDistance3D(p, getPoint(i));
			if (cd < d) {
				id = i;
				d = cd;
			}
		}
		return id;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Segment> toSegments() {
		final List<WB_Segment> segments = new FastList<WB_Segment>();
		for (int i = 0, j = this.numberOfShellPoints
				- 1; i < this.numberOfShellPoints; j = i, i++) {
			segments.add(new WB_Segment(getPoint(j), getPoint(i)));
		}
		return segments;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_AABB getAABB() {
		WB_AABB AABB = new WB_AABB();
		for (int i = 0; i < numberOfShellPoints; i++) {
			AABB.expandToInclude(getPoint(i));
		}
		return AABB;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getSignedArea() {
		int n = this.getNumberOfShellPoints();
		if (n < 3) {
			return 0;
		}
		WB_Point p1 = getPoint(0);
		WB_Point p2 = getPoint(1);
		WB_Point p3;
		double area = 0;
		for (int i = 1; i < n - 1; i++) {
			p3 = getPoint(i + 1);
			area += WB_GeometryOp.getSignedArea(p1, p2, p3);
			p2 = p3;
		}
		int nh = getNumberOfHoles();
		int[] npc = getNumberOfPointsPerContour();
		int offset = 0;
		for (int i = 0; i < nh; i++) {
			offset += npc[i];
			if (npc[i + 1] < 3) {
				continue;
			}
			p1 = getPoint(offset);
			p2 = getPoint(offset + 1);
			for (int j = 1; j < npc[i + 1] - 1; j++) {
				p3 = getPoint(offset + j + 1);
				area += WB_GeometryOp.getSignedArea(p1, p2, p3);
				p2 = p3;
			}
		}
		return area;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double[][] toVertices2D() {
		final WB_Plane P = getPlane(0);
		final WB_PlanarMap EP = new WB_PlanarMap(P);
		double[][] vertices = new double[this.numberOfPoints][2];
		final WB_Point p2D = new WB_Point();
		for (int i = 0; i < this.numberOfPoints; i++) {
			EP.mapPoint3D(points.get(i), p2D);
			vertices[i][0] = p2D.xf();
			vertices[i][1] = p2D.yf();
		}
		return vertices;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Polygon getSimplePolygon() {
		return new WB_JTS.PolygonTriangulatorJTS().makeSimplePolygon(this);
	}

	@Override
	public WB_Polygon apply(final WB_Transform3D T) {
		WB_Polygon poly = new WB_Polygon(this);
		poly.applySelf(T);
		return poly;
	}

	@Override
	public WB_Ring apply2D(WB_Transform2D T) {
		WB_Polygon poly = new WB_Polygon(this);
		poly.apply2DSelf(T);
		return poly;
	}

	@Override
	public WB_Polygon apply2DSelf(WB_Transform2D T) {
		for (WB_Point p : points) {
			p.applyAsPoint2DSelf(T);
		}
		calculateDirections();
		return this;
	}

	@Override
	public WB_Polygon applySelf(WB_Transform3D T) {
		for (WB_Point p : points) {
			p.applyAsPointSelf(T);
		}
		calculateDirections();
		return this;
	}
}