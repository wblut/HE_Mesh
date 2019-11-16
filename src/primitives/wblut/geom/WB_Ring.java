/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_Epsilon;

/**
 *
 */
public class WB_Ring extends WB_PolyLine {
	/**
	 *
	 */
	private WB_GeometryFactory geometryfactory = new WB_GeometryFactory();

	/**
	 * Get direction.
	 *
	 * @param i
	 * @return direction
	 */
	@Override
	public WB_Vector getDirection(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		return directions.get(i);
	}

	/**
	 * Get a normal to the line.
	 *
	 * @param i
	 * @return a normal
	 */
	@Override
	public WB_Vector getNormal(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		WB_Vector n = geometryfactory.createVector(0, 0, 1);
		n = n.cross(directions.get(i));
		final double d = n.getLength();
		n = n.div(d);
		if (WB_Epsilon.isZero(d)) {
			n = geometryfactory.createVector(1, 0, 0);
		}
		return n;
	}

	/**
	 * a.x+b.y+c=0
	 *
	 * @param i
	 * @return a for a 2D line
	 */
	@Override
	public double a(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		return -directions.get(i).yd();
	}

	/**
	 * a.x+b.y+c=0
	 *
	 * @param i
	 * @return b for a 2D line
	 */
	@Override
	public double b(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		return directions.get(i).xd();
	}

	/**
	 * a.x+b.y+c=0
	 *
	 * @param i
	 * @return c for a 2D line
	 */
	@Override
	public double c(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		return points.get(i).xd() * directions.get(i).yd()
				- points.get(i).yd() * directions.get(i).xd();
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getLength(int)
	 */
	@Override
	public double getLength(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		return incLengths[i] - (i == 0 ? 0 : incLengths[i - 1]);
	}

	/**
	 *
	 */
	protected WB_Ring() {
	};

	/**
	 *
	 *
	 * @param points
	 */
	protected WB_Ring(final List<? extends WB_Coord> points) {
		super(points);
		getDirections();
	}

	/**
	 *
	 *
	 * @param points
	 */
	protected WB_Ring(final WB_Coord[] points) {
		super(points);
		getDirections();
	}

	/**
	 *
	 */
	private void getDirections() {
		directions = new FastList<WB_Vector>();
		incLengths = new double[numberOfPoints];
		for (int i = 0; i < numberOfPoints; i++) {
			final int in = (i + 1) % numberOfPoints;
			final WB_Vector v = new WB_Vector(points.get(i), points.get(in));
			incLengths[i] = i == 0 ? v.getLength()
					: incLengths[i - 1] + v.getLength();
			v.normalizeSelf();
			directions.add(v);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WB_Ring)) {
			return false;
		}
		final WB_Ring L = (WB_Ring) o;
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

	/**
	 *
	 *
	 * @return
	 */
	public boolean isCCW() {
		final int nPts = getNumberOfPoints();
		// sanity check
		if (nPts < 3) {
			throw new IllegalArgumentException(
					"Ring has fewer than 3 points, so orientation cannot be determined");
		}
		// find highest point
		WB_Point hiPt = getPoint(0);
		int hiIndex = 0;
		for (int i = 1; i <= nPts; i++) {
			final WB_Point p = getPoint(i);
			if (p.yd() > hiPt.yd()) {
				hiPt = p;
				hiIndex = i;
			}
		}
		// find distinct point before highest point
		int iPrev = hiIndex;
		do {
			iPrev = iPrev - 1;
			if (iPrev < 0) {
				iPrev = nPts;
			}
		} while (getPoint(iPrev).equals(hiPt) && iPrev != hiIndex);
		// find distinct point after highest point
		int iNext = hiIndex;
		do {
			iNext = (iNext + 1) % nPts;
		} while (getPoint(iNext).equals(hiPt) && iNext != hiIndex);
		final WB_Point prev = getPoint(iPrev);
		final WB_Point next = getPoint(iNext);
		/**
		 * This check catches cases where the ring contains an A-B-A
		 * configuration of points. This can happen if the ring does not contain
		 * 3 distinct points (including the case where the input array has fewer
		 * than 4 elements), or it contains coincident line segments.
		 */
		if (prev.equals(hiPt) || next.equals(hiPt) || prev.equals(next)) {
			return false;
		}
		// System.out.println(prev + " " + hiPt + " " + next);
		final int disc = (int) Math
				.signum(WB_Predicates.orient2D(prev, hiPt, next));
		/**
		 * If disc is exactly 0, lines are collinear. There are two possible
		 * cases: (1) the lines lie along the x axis in opposite directions (2)
		 * the lines lie on top of one another
		 *
		 * (1) is handled by checking if next is left of prev ==> CCW (2) will
		 * never happen if the ring is valid, so don't check for it (Might want
		 * to assert this)
		 */
		boolean isCCW = false;
		if (disc == 0) {
			// poly is CCW if prev x is right of next x
			isCCW = prev.xd() > next.xd();
		} else {
			// if area is positive, points are ordered CCW
			isCCW = disc > 0;
		}
		return isCCW;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getPoint(int)
	 */
	@Override
	public WB_Point getPoint(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i
					+ " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getd(int, int)
	 */
	@Override
	public double getd(final int i, final int j) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i
					+ " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i).getd(j);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getf(int, int)
	 */
	@Override
	public float getf(final int i, final int j) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter " + i
					+ " must between 0 and " + (numberOfPoints - 1) + ".");
		}
		return points.get(i).getf(j);
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getPointOnLine(double)
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getParametricPointOnLine(double)
	 */
	@Override
	public WB_Point getParametricPointOnLine(final double t) {
		if (t < 0 || t > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		final double ft = t - (int) t;
		if (ft == 0.0) {
			return new WB_Point(points.get((int) t));
		}
		return points.get((int) t).mulAddMul(1 - ft, ft,
				points.get(1 + (int) t));
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getNumberOfPoints()
	 */
	@Override
	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.WB_PolyLine#getSegment(int)
	 */
	@Override
	public WB_Segment getSegment(final int i) {
		if (i < 0 || i > numberOfPoints - 1) {
			throw new IllegalArgumentException("Parameter must between 0 and "
					+ (numberOfPoints - 1) + ".");
		}
		return geometryfactory.createSegment(getPoint(i),
				getPoint((i + 1) % numberOfPoints));
	}

	@Override
	public WB_Ring apply(final WB_Transform3D T) {
		List<WB_Point> tpoints = new FastList<WB_Point>();
		for (WB_Point p : points) {
			tpoints.add(p.applyAsPoint(T));
		}
		return geometryfactory.createRing(tpoints);
	}

	@Override
	public WB_Ring apply2D(WB_Transform2D T) {
		List<WB_Point> tpoints = new FastList<WB_Point>();
		for (WB_Point p : points) {
			tpoints.add(p.applyAsPoint2D(T));
		}
		return geometryfactory.createRing(tpoints);
	}

	@Override
	public WB_Ring apply2DSelf(WB_Transform2D T) {
		for (WB_Point p : points) {
			p.applyAsPoint2DSelf(T);
		}
		getDirections();
		hashcode = -1;
		return this;
	}

	@Override
	public WB_Ring applySelf(WB_Transform3D T) {
		for (WB_Point p : points) {
			p.applyAsPointSelf(T);
		}
		getDirections();
		hashcode = -1;
		return this;
	}
}