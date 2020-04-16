package wblut.geom;

import java.util.Collection;
import java.util.List;

import wblut.math.WB_Epsilon;

public class WB_PolyLine implements WB_Transformable3D, WB_Curve {
	List<WB_Point> points;
	List<WB_Vector> directions;
	double[] incLengths;
	int numberOfPoints;
	int hashcode;
	private final WB_GeometryFactory3D geometryfactory = new WB_GeometryFactory3D();

	public WB_Point getPoint(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i + " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i);
	}

	public double getd(final int i, final int j) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i + " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i).getd(j);
	}

	public float getf(final int i, final int j) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i + " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i).getf(j);
	}

	public WB_Point getPointOnLine(final double t) {
		if (t < 0 || t > incLengths[numberOfPoints - 1]) {
			throw new IllegalArgumentException(
					"Parameter must between 0 and length of polyline" + incLengths[numberOfPoints - 1] + " .");
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

	public WB_Point getParametricPointOnLine(double t) {
		if (t < 0 || t > 1) {
			throw new IllegalArgumentException("Parameter must between 0 and " + 1 + ".");
		}
		if (t == 0) {
			return points.get(0);
		}
		if (t == 1) {
			return points.get(points.size() - 1);
		}
		t *= numberOfPoints - 1;
		final double ft = t - (int) t;
		if (ft == 0.0) {
			return new WB_Point(points.get((int) t));
		}
		return points.get((int) t).mulAddMul(1 - ft, ft, points.get(1 + (int) t));
	}

	public WB_Vector getDirection(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
		}
		return directions.get(i);
	}

	public WB_Vector getNormal(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
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

	public double a(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
		}
		return -directions.get(i).getd(1);
	}

	public double b(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
		}
		return directions.get(i).getd(0);
	}

	public double c(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
		}
		return points.get(i).getd(0) * directions.get(i).getd(1) - points.get(i).getd(1) * directions.get(i).getd(0);
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public WB_Segment getSegment(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
		}
		return geometryfactory.createSegment(getPoint(i), getPoint(i + 1));
	}

	public int getNumberSegments() {
		return points.size() - 1;
	}

	public double getLength(final int i) {
		if (i < 0 || i > numberOfPoints - 2) {
			throw new IllegalArgumentException("Parameter must between 0 and " + (numberOfPoints - 2) + ".");
		}
		return incLengths[i + 1] - incLengths[i];
	}

	protected WB_PolyLine() {
	}

	public WB_PolyLine(final Collection<? extends WB_Coord> points) {
		numberOfPoints = points.size();
		this.points = new WB_PointList();
		for (final WB_Coord p : points) {
			this.points.add(new WB_Point(p));
		}
		getDirections();
		hashcode = -1;
	}

	public WB_PolyLine(final WB_Coord... points) {
		numberOfPoints = points.length;
		this.points = new WB_PointList();
		for (final WB_Coord p : points) {
			this.points.add(new WB_Point(p));
		}
		getDirections();
		hashcode = -1;
	}

	public void addPoint(final WB_Coord p) {
		numberOfPoints++;
		points.add(new WB_Point(p));
		getDirections();
		hashcode = -1;
	}

	public void removePoint(final int i) {
		numberOfPoints--;
		points.remove(i);
		getDirections();
		hashcode = -1;
	}

	private void getDirections() {
		directions = new WB_VectorList();
		incLengths = new double[points.size() - 1];
		for (int i = 0; i < points.size() - 1; i++) {
			final WB_Vector v = new WB_Vector(points.get(i), points.get(i + 1));
			incLengths[i] = i == 0 ? v.getLength() : incLengths[i - 1] + v.getLength();
			v.normalizeSelf();
			directions.add(v);
		}
	}

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

	public WB_CoordCollection getPoints() {
		return WB_CoordCollection.getCollection(points);
	}

	@Override
	public WB_PolyLine apply(final WB_Transform3D T) {
		final List<WB_Point> tpoints = new WB_PointList();
		for (final WB_Point p : points) {
			tpoints.add(p.applyAsPoint(T));
		}
		return geometryfactory.createPolyLine(tpoints);
	}

	@Override
	public WB_PolyLine apply2D(final WB_Transform2D T) {
		final List<WB_Point> tpoints = new WB_PointList();
		for (final WB_Point p : points) {
			tpoints.add(p.applyAsPoint2D(T));
		}
		return geometryfactory.createPolyLine(tpoints);
	}

	@Override
	public WB_PolyLine apply2DSelf(final WB_Transform2D T) {
		for (final WB_Point p : points) {
			p.applyAsPoint2DSelf(T);
		}
		getDirections();
		hashcode = -1;
		return this;
	}

	@Override
	public WB_PolyLine applySelf(final WB_Transform3D T) {
		for (final WB_Point p : points) {
			p.applyAsPointSelf(T);
		}
		getDirections();
		hashcode = -1;
		return this;
	}

	@Override
	public WB_Point getPointOnCurve(final double u) {
		return getParametricPointOnLine(u);
	}

	@Override
	public WB_Vector getDirectionOnCurve(double u) {
		if (u < 0 || u > 1) {
			throw new IllegalArgumentException("Parameter must between 0 and " + 1 + ".");
		}
		if (u == 0) {
			return directions.get(0);
		}
		if (u == 1) {
			return directions.get(directions.size() - 1);
		}
		u *= numberOfPoints - 1;
		final double fu = u - (int) u;
		if (fu == 0.0 && u > 0 && u < 1.0) {
			return directions.get((int) u).mulAddMul(0.5, 0.5, directions.get((int) u + 1));
		}
		return directions.get((int) u);
	}

	@Override
	public WB_Vector getDerivative(final double u) {
		return null;
	}

	@Override
	public double getLowerU() {
		return 0;
	}

	@Override
	public double getUpperU() {
		return 1;
	}
}