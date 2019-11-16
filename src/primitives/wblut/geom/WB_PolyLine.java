/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_PolyLine implements WB_Geometry {
	/**
	 *
	 */
	List<WB_Point>				points;
	/**
	 *
	 */
	List<WB_Vector>				directions;
	/**
	 *
	 */
	double[]					incLengths;
	/**
	 *
	 */
	int							numberOfPoints;
	/**
	 *
	 */
	int							hashcode;
	/**
	 *
	 */
	private WB_GeometryFactory	geometryfactory	= new WB_GeometryFactory();

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Point getPoint(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i
					+ " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i);
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public double getd(final int i, final int j) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i
					+ " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i).getd(j);
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public float getf(final int i, final int j) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i
					+ " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i).getf(j);
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public WB_Point getPointOnLine(final double t) {
		if (t < 0 || t > incLengths[numberOfPoints - 1]) {
			throw new IllegalArgumentException(
					"Parameter must between 0 and length of polyline"
							+ incLengths[numberOfPoints - 1] + " .");
		}
		if (t == 0) {
			return new WB_Point(points.get(0));
		}
		int index = 0;
		while (t > incLengths[index]) {
			index++;
		}
		final double x = t - incLengths[index];
		return points.get(index).addMul(x, directions.get(index));
	}

	/**
	 *
	 *
	 * @param t
	 * @return
	 */
	public WB_Point getParametricPointOnLine(double t) {
		if (t < 0 || t > 1) {
			throw new IllegalArgumentException(
					"Parameter must between 0 and " + 1 + ".");
		}
		t *= numberOfPoints - 1;
		final double ft = t - (int) t;
		if (ft == 0.0) {
			return new WB_Point(points.get((int) t));
		}
		return points.get((int) t).mulAddMul(1 - ft, ft,
				points.get(1 + (int) t));
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Vector getDirection(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		return directions.get(i);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Vector getNormal(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		WB_Vector normal = geometryfactory.createVector(0, 0, 1);
		normal = normal.cross(directions.get(i));
		final double d = normal.getLength();
		normal = normal.div(d);
		if (WB_Epsilon.isZero(d)) {
			normal = geometryfactory.createVector(1, 0, 0);
		}
		return normal;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double a(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		return -directions.get(i).getd(1);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double b(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		return directions.get(i).getd(0);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double c(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		return points.get(i).getd(0) * directions.get(i).getd(1)
				- points.get(i).getd(1) * directions.get(i).getd(0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Segment getSegment(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		return geometryfactory.createSegment(getPoint(i), getPoint(i + 1));
	}

	public int getNumberSegments() {
		return points.size() - 1;
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double getLength(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 2) + ".");
		}
		return incLengths[i + 1] - incLengths[i];
	}

	/**
	 *
	 */
	protected WB_PolyLine() {
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_PolyLine(final Collection<? extends WB_Coord> points) {
		numberOfPoints = points.size();
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : points) {
			this.points.add(new WB_Point(p));
		}
		getDirections();
		hashcode = -1;
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_PolyLine(final WB_Coord... points) {
		numberOfPoints = points.length;
		this.points = new FastList<WB_Point>();
		for (WB_Coord p : points) {
			this.points.add(new WB_Point(p));
		}
		getDirections();
		hashcode = -1;
	}

	public void addPoint(WB_Coord p) {
		numberOfPoints++;
		points.add(new WB_Point(p));
		getDirections();
		hashcode = -1;
	}

	public void removePoint(int i) {
		numberOfPoints--;
		points.remove(i);
		getDirections();
		hashcode = -1;
	}

	/**
	 *
	 */
	private void getDirections() {
		directions = new FastList<WB_Vector>();
		incLengths = new double[points.size() - 1];
		for (int i = 0; i < points.size() - 1; i++) {
			final WB_Vector v = new WB_Vector(points.get(i), points.get(i + 1));
			incLengths[i] = i == 0 ? v.getLength()
					: incLengths[i - 1] + v.getLength();
			v.normalizeSelf();
			directions.add(v);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_PolyLine)) {
			return false;
		}
		final WB_PolyLine L = (WB_PolyLine) o;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hashcode == -1) {
			hashcode = points.get(0).hashCode();
			for (int i = 1; i < points.size(); i++) {
				hashcode = 31 * hashcode + points.get(i).hashCode();
			}
		}
		return hashcode;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_CoordCollection getPoints() {
		return WB_CoordCollection.getCollection(points);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_Geometry#apply(wblut.geom.WB_Transform)
	 */
	@Override
	public WB_PolyLine apply(final WB_Transform3D T) {
		List<WB_Point> tpoints = new FastList<WB_Point>();
		for (WB_Point p : points) {
			tpoints.add(p.applyAsPoint(T));
		}
		return geometryfactory.createPolyLine(tpoints);
	}

	@Override
	public WB_PolyLine apply2D(WB_Transform2D T) {
		List<WB_Point> tpoints = new FastList<WB_Point>();
		for (WB_Point p : points) {
			tpoints.add(p.applyAsPoint2D(T));
		}
		return geometryfactory.createPolyLine(tpoints);
	}

	@Override
	public WB_PolyLine apply2DSelf(WB_Transform2D T) {
		for (WB_Point p : points) {
			p.applyAsPoint2DSelf(T);
		}
		getDirections();
		hashcode = -1;
		return this;
	}

	@Override
	public WB_PolyLine applySelf(WB_Transform3D T) {
		for (WB_Point p : points) {
			p.applyAsPointSelf(T);
		}
		getDirections();
		hashcode = -1;
		return this;
	}
}