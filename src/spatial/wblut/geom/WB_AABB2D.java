/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wblut.math.WB_Epsilon;

public class WB_AABB2D {

	protected double[] _min;

	protected double[] _max;

	int _id;

	/**
	 *
	 *
	 * @param p
	 */
	public WB_AABB2D(final WB_Coord p) {
		init();
		setToNull();
		expandToInclude(p);
	}

	/**
	 *
	 */
	public WB_AABB2D() {
		init();
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_AABB2D(final WB_Coord[] points) {
		if (points == null) {
			throw new NullPointerException("Array not initialized.");
		}
		if (points.length == 0) {
			throw new IllegalArgumentException("Array has zero size.");
		}
		WB_Coord point = points[0];
		if (point == null) {
			throw new NullPointerException("Array point not initialized.");
		}
		init();
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < 2; j++) {
				point = points[i];
				if (point == null) {
					throw new NullPointerException("Array point not initialized.");
				}
				if (_min[j] > point.getd(j)) {
					_min[j] = point.getd(j);
				}
				if (_max[j] < point.getd(j)) {
					_max[j] = point.getd(j);
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_AABB2D(final Collection<? extends WB_Coord> points) {
		if (points == null) {
			throw new IllegalArgumentException("Collection not initialized.");
		}
		if (points.size() == 0) {
			throw new IllegalArgumentException("Collection has zero size.");
		}
		final WB_Coord fpoint = points.iterator().next();
		if (fpoint == null) {
			throw new NullPointerException("Collection point not initialized.");
		}
		init();
		for (final WB_Coord point : points) {
			if (point == null) {
				throw new NullPointerException("Collection point not initialized.");
			}
			for (int j = 0; j < 2; j++) {
				if (_min[j] > point.getd(j)) {
					_min[j] = point.getd(j);
				}
				if (_max[j] < point.getd(j)) {
					_max[j] = point.getd(j);
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param min
	 * @param max
	 */
	public WB_AABB2D(final double[] min, final double[] max) {
		init();
		if (min.length == 2 && max.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (min[i] < max[i]) {
					_min[i] = min[i];
					_max[i] = max[i];
				} else {
					_min[i] = max[i];
					_max[i] = min[i];
				}
			}

		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 *
	 *
	 * @param min
	 * @param max
	 */
	public WB_AABB2D(final float[] min, final float[] max) {
		init();
		if (min.length == 2 && max.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (min[i] < max[i]) {
					_min[i] = min[i];
					_max[i] = max[i];
				} else {
					_min[i] = max[i];
					_max[i] = min[i];
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 *
	 *
	 * @param min
	 * @param max
	 */
	public WB_AABB2D(final int[] min, final int[] max) {
		init();
		if (min.length == 2 && max.length == 2) {
			for (int i = 0; i < 2; i++) {
				if (min[i] < max[i]) {
					_min[i] = min[i];
					_max[i] = max[i];
				} else {
					_min[i] = max[i];
					_max[i] = min[i];
				}
			}

		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 *
	 *
	 * @param min
	 * @param max
	 */
	public WB_AABB2D(final WB_Coord min, final WB_Coord max) {
		init();
		for (int i = 0; i < 2; i++) {
			if (min.getd(i) < max.getd(i)) {
				_min[i] = min.getd(i);
				_max[i] = max.getd(i);
			} else {
				_min[i] = max.getd(i);
				_max[i] = min.getd(i);
			}
		}
	}

	/**
	 *
	 *
	 * @param minx
	 * @param miny
	 * @param maxx
	 * @param maxy
	 */
	public WB_AABB2D(final double minx, final double miny, final double maxx, final double maxy) {
		this();
		expandToInclude(minx, miny);
		expandToInclude(maxx, maxy);
	}

	/**
	 *
	 *
	 * @param values
	 */
	public WB_AABB2D(final double[] values) {
		init();
		if (values.length == 0) {
		} else if (values.length == 4) {
			for (int i = 0; i < 2; i++) {
				_min[i] = values[i];
				_max[i] = values[i + 2];
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 *
	 *
	 * @param values
	 */
	public WB_AABB2D(final int[] values) {
		init();
		if (values.length == 0) {
		} else if (values.length == 4) {
			for (int i = 0; i < 2; i++) {
				_min[i] = values[i];
				_max[i] = values[i + 2];
			}

		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 *
	 *
	 * @param values
	 */
	public WB_AABB2D(final float[] values) {
		init();
		if (values.length == 0) {
		} else if (values.length == 4) {
			for (int i = 0; i < 2; i++) {
				_min[i] = values[i];
				_max[i] = values[i + 2];
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double getSize(final int i) {
		if (isNull()) {
			return 0;
		}
		return _max[i] - _min[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double getMin(final int i) {
		return _min[i];
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public double getMax(final int i) {
		return _max[i];
	}

	/**
	 *
	 *
	 * @return
	 */
	public int minOrdinate() {
		if (isNull()) {
			return 0;
		}
		double res = Double.POSITIVE_INFINITY;
		int ord = 0;
		for (int i = 0; i < 2; i++) {
			final double w = getSize(i);
			if (res > w) {
				res = w;
				ord = i;
			}
		}
		return ord;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int maxOrdinate() {
		if (isNull()) {
			return 0;
		}
		double res = Double.NEGATIVE_INFINITY;
		int ord = 0;
		for (int i = 0; i < 2; i++) {
			final double w = getSize(i);
			if (res < w) {
				res = w;
				ord = i;
			}
		}
		return ord;
	}

	/**
	 *
	 *
	 * @param p
	 */
	public void expandToInclude(final WB_Coord p) {
		expandToInclude(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param p
	 */
	public void add(final WB_Coord p) {
		expandToInclude(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param distance
	 */
	public void expandBy(final double distance) {
		if (isNull()) {
			return;
		}
		for (int i = 0; i < 2; i++) {
			_min[i] -= distance;
			_max[i] += distance;
			if (_min[i] > _max[i]) {
				setToNull();
				return;
			}
		}
	}

	/**
	 *
	 *
	 * @param delta
	 */
	public void expandBy(final double[] delta) {
		if (isNull()) {
			return;
		}
		for (int i = 0; i < 2; i++) {
			_min[i] -= delta[i];
			_max[i] += delta[i];
			if (_min[i] > _max[i]) {
				setToNull();
				return;
			}
		}
	}

	/**
	 *
	 *
	 * @param dx
	 * @param dy
	 *
	 */
	public void expandBy(final double dx, final double dy) {
		if (isNull()) {
			return;
		}
		_min[0] -= dx;
		_max[0] += dx;
		if (_min[0] > _max[0]) {
			setToNull();
			return;
		}
		_min[1] -= dy;
		_max[1] += dy;
		if (_min[1] > _max[1]) {
			setToNull();
			return;
		}

	}

	/**
	 *
	 *
	 * @param p
	 */
	public void expandToInclude(final double[] p) {
		if (isNull()) {
			for (int i = 0; i < 2; i++) {
				_min[i] = p[i];
				_max[i] = p[i];
			}
		} else {
			for (int i = 0; i < 2; i++) {
				if (p[i] < _min[i]) {
					_min[i] = p[i];
				}
				if (p[i] > _max[i]) {
					_max[i] = p[i];
				}
			}
		}
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public void expandToInclude(final double x, final double y) {
		if (isNull()) {
			_min[0] = x;
			_max[0] = x;
			_min[1] = y;
			_max[1] = y;
		} else {
			if (x < _min[0]) {
				_min[0] = x;
			}
			if (x > _max[0]) {
				_max[0] = x;
			}
			if (y < _min[1]) {
				_min[1] = y;
			}
			if (y > _max[1]) {
				_max[1] = y;
			}
		}
	}

	/**
	 *
	 *
	 * @param other
	 */
	public void expandToInclude(final WB_AABB2D other) {
		expandToInclude(other._min);
		expandToInclude(other._max);
	}

	/**
	 *
	 *
	 * @param other
	 */
	public void add(final WB_AABB2D other) {
		expandToInclude(other);
	}

	/**
	 *
	 *
	 * @param d
	 */
	public void translate(final double[] d) {
		if (isNull()) {
			return;
		}
		for (int i = 0; i < 2; i++) {
			_min[i] += d[i];
			_max[i] += d[i];
		}
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public boolean intersects(final WB_AABB2D other) {
		if (isNull() || other.isNull()) {
			return false;
		}
		for (int i = 0; i < 2; i++) {
			if (other._min[i] > _max[i]) {
				return false;
			}
			if (other._max[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public boolean intersects(final WB_Coord p) {
		return intersects(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public boolean intersects(final double[] x) {
		if (isNull()) {
			return false;
		}
		for (int i = 0; i < 2; i++) {
			if (x[i] > _max[i]) {
				return false;
			}
			if (x[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean intersects(final double x, final double y) {
		if (isNull()) {
			return false;
		}
		if (x > _max[0]) {
			return false;
		}
		if (x < _min[0]) {
			return false;
		}
		if (y > _max[1]) {
			return false;
		}
		if (y < _min[1]) {
			return false;
		}

		return true;
	}

	public boolean intersects(final WB_Circle circle) {
		WB_Coord c = circle.getCenter();
		double r = circle.getRadius();
		double s, d = 0;
		// find the square of the distance
		// from the sphere to the box
		if (c.xd() < _min[0]) {
			s = c.xd() - _min[0];
			d = s * s;
		} else if (c.xd() > _max[0]) {
			s = c.xd() - _max[0];
			d += s * s;
		}

		if (c.yd() < _min[1]) {
			s = c.yd() - _min[1];
			d += s * s;
		} else if (c.yd() > _max[1]) {
			s = c.yd() - _max[1];
			d += s * s;
		}

		return d <= r * r;
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public boolean contains(final WB_AABB2D other) {
		return covers(other);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public boolean contains(final WB_Coord p) {
		return covers(p);
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public boolean contains(final double[] x) {
		return covers(x);
	}

	/**
	 *
	 *
	 * @param x
	 * @return
	 */
	public boolean covers(final double[] x) {
		if (isNull()) {
			return false;
		}
		for (int i = 0; i < 2; i++) {
			if (x[i] > _max[i]) {
				return false;
			}
			if (x[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @return
	 */
	public boolean covers(final double x, final double y) {
		if (isNull()) {
			return false;
		}
		if (x > _max[0]) {
			return false;
		}
		if (x < _min[0]) {
			return false;
		}
		if (y > _max[1]) {
			return false;
		}
		if (y < _min[1]) {
			return false;
		}

		return true;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public boolean covers(final WB_Coord p) {
		return covers(p.xd(), p.yd());
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public boolean covers(final WB_AABB2D other) {
		if (isNull() || other.isNull()) {
			return false;
		}
		for (int i = 0; i < 2; i++) {
			if (other._max[i] > _max[i]) {
				return false;
			}
			if (other._min[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public double getDistance(final WB_AABB2D other) {
		if (intersects(other)) {
			return 0;
		}
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 2; i++) {
			if (_max[i] < other._min[i]) {
				dx = other._min[i] - _max[i];
			} else if (_min[i] > other._max[i]) {
				dx = _min[i] - other._max[i];
			}
			sqr += dx * dx;
		}
		return Math.sqrt(sqr);
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public double getDistanceSquare(final WB_AABB2D other) {
		if (intersects(other)) {
			return 0;
		}
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 2; i++) {
			if (_max[i] < other._min[i]) {
				dx = other._min[i] - _max[i];
			} else if (_min[i] > other._max[i]) {
				dx = _min[i] - other._max[i];
			}
			sqr += dx * dx;
		}
		return sqr;
	}

	/**
	 *
	 *
	 * @param tuple
	 * @return
	 */
	public double getDistance(final WB_Coord tuple) {
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 2; i++) {
			if (_max[i] < tuple.getd(i)) {
				sqr += (dx = tuple.getd(i) - _max[i]) * dx;
			} else if (_min[i] > tuple.getd(i)) {
				sqr += (dx = _min[i] - tuple.getd(i)) * dx;
			}
		}
		return Math.sqrt(sqr);
	}

	/**
	 *
	 *
	 * @param tuple
	 * @return
	 */
	public double getDistanceSquare(final WB_Coord tuple) {
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 2; i++) {
			if (_max[i] < tuple.getd(i)) {
				sqr += (dx = tuple.getd(i) - _max[i]) * dx;
			} else if (_min[i] > tuple.getd(i)) {
				sqr += (dx = _min[i] - tuple.getd(i)) * dx;
			}
		}
		return sqr;
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public boolean equals(final WB_AABB2D other) {
		if (isNull()) {
			return other.isNull();
		}
		for (int i = 0; i < 2; i++) {
			if (other._max[i] != _max[i]) {
				return false;
			}
			if (other._min[i] != _min[i]) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "WB_AABB2D [";
		int i = 0;
		for (i = 0; i < 2 - 1; i++) {
			string += _min[i] + ":" + _max[i] + ", ";
		}
		string += _min[i] + ":" + _max[i] + "]";
		return string;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int numberOfPoints() {
		if (isNull()) {
			return 0;
		}
		return 4;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int numberOfSegments() {
		if (isNull()) {
			return 0;
		}
		return 4;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int numberOfTriangles() {
		if (isNull()) {
			return 0;
		}
		return 2;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int numberOfFaces() {
		if (isNull()) {
			return 0;
		}
		return 1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point[] getCorners() {
		if (isNull()) {
			return null;
		}
		final int n = numberOfPoints();
		final WB_Point[] points = new WB_Point[n];
		double[] values;
		for (int i = 0; i < n; i++) {
			values = new double[2];
			int disc = 1;
			for (int j = 0; j < 2; j++) {
				if (i / disc % 2 == 0) {
					values[j] = _min[j];
				} else {
					values[j] = _max[j];
				}
				disc *= 2;
			}
			points[i] = new WB_Point(values);
		}
		return points;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<int[]> getSegments() {
		final List<int[]> segments = new ArrayList<int[]>(numberOfSegments());
		segments.add(new int[] { 0, 1 });
		segments.add(new int[] { 1, 2 });
		segments.add(new int[] { 2, 3 });
		segments.add(new int[] { 3, 0 });
		return segments;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getId() {
		return _id;
	}

	/**
	 *
	 *
	 * @param id
	 */
	public void setId(final int id) {
		_id = id;
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isDegenerate() {
		return getTrueDim() < 2 && getTrueDim() > -1;
	}

	/**
	 *
	 *
	 * @param src
	 */
	public void set(final WB_AABB2D src) {
		System.arraycopy(src._min, 0, _min, 0, 2);
		System.arraycopy(src._max, 0, _max, 0, 2);
	}

	/**
	 *
	 */
	private void init() {
		_min = new double[2];
		_max = new double[2];
		for (int i = 0; i < 2; i++) {
			_min[i] = Double.POSITIVE_INFINITY;
			_max[i] = Double.NEGATIVE_INFINITY;
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_AABB2D get() {
		return new WB_AABB2D(_min, _max);
	}

	/**
	 *
	 *
	 * @param aabb
	 * @return
	 */
	public WB_AABB2D getUnion(final WB_AABB2D aabb) {
		final double[] newmin = new double[2];
		final double[] newmax = new double[2];
		for (int i = 0; i < 2; i++) {
			newmin[i] = Math.min(_min[i], aabb._min[i]);
			newmax[i] = Math.max(_max[i], aabb._max[i]);
		}
		return new WB_AABB2D(newmin, newmax);
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	public WB_AABB2D getIntersection(final WB_AABB2D other) {
		if (isNull() || other.isNull() || !intersects(other)) {
			return null;
		}
		final double[] newmin = new double[2];
		final double[] newmax = new double[2];
		for (int i = 0; i < 2; i++) {
			newmin[i] = Math.max(_min[i], other._min[i]);
			newmax[i] = Math.min(_max[i], other._max[i]);
		}
		return new WB_AABB2D(newmin, newmax);
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param q
	 * @return
	 */
	public static boolean intersects(final WB_Coord p1, final WB_Coord p2, final WB_Coord q) {
		if (q.xd() >= (p1.xd() < p2.xd() ? p1.xd() : p2.xd()) && q.xd() <= (p1.xd() > p2.xd() ? p1.xd() : p2.xd())
				&& q.yd() >= (p1.yd() < p2.yd() ? p1.yd() : p2.yd())
				&& q.yd() <= (p1.yd() > p2.yd() ? p1.yd() : p2.yd())) {
			return true;
		}
		return false;
	}

	/**
	 *
	 *
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static boolean intersects(final WB_Coord p1, final WB_Coord p2, final WB_Coord q1, final WB_Coord q2) {
		double minq = Math.min(q1.xd(), q2.xd());
		double maxq = Math.max(q1.xd(), q2.xd());
		double minp = Math.min(p1.xd(), p2.xd());
		double maxp = Math.max(p1.xd(), p2.xd());
		if (minp > maxq) {
			return false;
		}
		if (maxp < minq) {
			return false;
		}
		minq = Math.min(q1.yd(), q2.yd());
		maxq = Math.max(q1.yd(), q2.yd());
		minp = Math.min(p1.yd(), p2.yd());
		maxp = Math.max(p1.yd(), p2.yd());
		if (minp > maxq) {
			return false;
		}
		if (maxp < minq) {
			return false;
		}

		return true;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point getCenter() {
		final double[] center = new double[2];
		for (int i = 0; i < 2; i++) {
			center[i] = 0.5 * (_min[i] + _max[i]);
		}
		return new WB_Point(center);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getWidth() {
		return getSize(0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getHeight() {
		return getSize(1);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getMinX() {
		return _min[0];
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getCenterX() {
		return 0.5 * (_min[0] + _max[0]);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getCenterY() {
		return 0.5 * (_min[1] + _max[1]);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getMaxX() {
		return _max[0];
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getMinY() {
		return _min[1];
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getMaxY() {
		return _max[1];
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getArea() {
		return getWidth() * getHeight();
	}

	/**
	 *
	 *
	 * @return
	 */
	public double minExtent() {
		if (isNull()) {
			return 0.0;
		}
		final double w = getWidth();
		final double h = getHeight();

		return w < h ? w : h;

	}

	/**
	 *
	 *
	 * @return
	 */
	public double maxExtent() {
		if (isNull()) {
			return 0.0;
		}
		final double w = getWidth();
		final double h = getHeight();

		return w > h ? w : h;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public void translate(final double x, final double y) {
		if (isNull()) {
			return;
		}
		_min[0] += x;
		_max[0] += x;
		_min[1] += y;
		_max[1] += y;

	}

	/**
	 *
	 *
	 * @return
	 */
	public List<int[]> getTriangles() {
		final List<int[]> tris = new ArrayList<int[]>();
		final int[] tri01 = { 0, 1, 2 };
		final int[] tri02 = { 0, 2, 3 };
		tris.add(tri01);
		tris.add(tri02);
		return tris;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int[][] getFaces() {
		final int[][] faces = new int[1][];
		faces[0] = new int[] { 0, 1, 2, 3 };
		return faces;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point getMin() {
		return new WB_Point(_min);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point getMax() {
		return new WB_Point(_max);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getDim() {
		return 2;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getTrueDim() {
		if (!isValid()) {
			return -1;
		}
		int dim = 0;
		for (int i = 0; i < 2; i++) {
			if (_max[i] - _min[i] >= WB_Epsilon.EPSILON) {
				dim++;
			}
		}
		return dim;
	}

	/**
	 *
	 *
	 * @param factor
	 */
	public void pad(final double factor) {
		final WB_Point c = getCenter();
		for (int i = 0; i < 2; i++) {
			_min[i] = c.getd(i) + (factor + 1.0) * (_min[i] - c.getd(i));
			_max[i] = c.getd(i) + (factor + 1.0) * (_max[i] - c.getd(i));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < 2; i++) {
			result = 37 * result + hashCode(_min[i]);
			result = 37 * result + hashCode(_max[i]);
		}
		return result;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	private int hashCode(final double v) {
		final long tmp = Double.doubleToLongBits(v);
		return (int) (tmp ^ tmp >>> 32);
	}

	/**
	 *
	 */
	public void setToNull() {
		for (int i = 0; i < 2; i++) {
			_min[i] = Double.POSITIVE_INFINITY;
			_max[i] = Double.NEGATIVE_INFINITY;
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isNull() {
		return _max[0] < _min[0];
	}

	/**
	 *
	 *
	 * @return
	 */
	public boolean isValid() {
		return !isNull();
	}
}
