/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/

 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wblut.math.WB_Epsilon;

/**
 * Class WB_KDTree.
 *
 * @param <T>
 *            generic type
 *
 */
public class WB_KDTreeInteger2D<T extends WB_Coord> {
	/** dim. */
	private final int _dim;
	/** maximum bin size. */
	private final int _maximumBinSize;
	/** root. */
	private final WB_KDNodeInteger<T> root;

	/**
	 *
	 */
	public WB_KDTreeInteger2D() {
		this._dim = 2;
		this._maximumBinSize = 32;
		this.root = new WB_KDNodeInteger<T>();
	}

	/**
	 *
	 *
	 * @param binsize
	 */
	public WB_KDTreeInteger2D(final int binsize) {
		this._dim = 2;
		this._maximumBinSize = binsize;
		this.root = new WB_KDNodeInteger<T>();
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_AABB2D> getLeafBounds() {
		final List<WB_AABB2D> leafs = new ArrayList<WB_AABB2D>();
		root.addLeafBounds(leafs);
		return leafs;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_AABB2D> getAllBounds() {
		final List<WB_AABB2D> all = new ArrayList<WB_AABB2D>();
		root.addBox(all, 0);
		return all;
	}

	/**
	 * Get the leaf regions.
	 *
	 * @return leaf regions
	 */
	public List<WB_AABB2D> getLeafRegions() {
		final List<WB_AABB2D> leafs = new ArrayList<WB_AABB2D>();
		root.addLeafRegion(leafs);
		return leafs;
	}

	/**
	 * Get all regions.
	 *
	 * @return all regions
	 */
	public List<WB_AABB2D> getAllRegions() {
		final List<WB_AABB2D> all = new ArrayList<WB_AABB2D>();
		root.addRegion(all, 0);
		return all;
	}

	/**
	 *
	 *
	 * @param coord
	 * @param val
	 * @return
	 */
	public int add(final T coord, final int val) {
		return root.add(new WB_KDEntryInteger<T>(coord, val, -1));
	}

	/**
	 *
	 *
	 * @param aabb
	 * @return
	 */
	public WB_KDEntryInteger<T>[] getRange(final WB_AABB2D aabb) {
		return root.range(aabb);
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public WB_KDEntryInteger<T>[] getRange(final WB_Coord center, final double radius) {
		final double r2 = radius * radius;
		return root.range(center, r2);
	}

	/**
	 *
	 *
	 * @param center
	 * @param lower
	 * @param upper
	 * @return
	 */
	public WB_KDEntryInteger<T>[] getRange(final WB_Coord center, final double lower, final double upper) {
		final double lower2 = lower * lower;
		final double upper2 = upper * upper;
		return root.range(center, lower2, upper2);
	}

	/**
	 *
	 *
	 * @param coord
	 * @param num
	 * @return
	 */
	public WB_KDEntryInteger<T>[] getNearestNeighbors(final WB_Coord coord, final int num) {
		final QueryResultInteger<T> heap = new QueryResultInteger<T>(num);
		root.findNearest(heap, coord);
		return heap.entries;
	}

	/**
	 *
	 *
	 * @param coord
	 * @return
	 */
	public WB_KDEntryInteger<T> getNearestNeighbor(final WB_Coord coord) {
		final QueryResultInteger<T> heap = new QueryResultInteger<T>(1);
		root.findNearest(heap, coord);
		return heap.entries[0];
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public WB_KDEntryInteger<T> getNearestNeighbor(final double x, final double y) {
		final QueryResultInteger<T> heap = new QueryResultInteger<T>(1);
		root.findNearest(heap, new WB_Point(x, y));
		return heap.entries[0];
	}

	/**
	 *
	 *
	 * @param <T>
	 * @param <V>
	 */
	public static class WB_KDEntryInteger<T extends WB_Coord> {

		/**
		 *
		 */
		public T coord;

		/**
		 *
		 */
		public int value;

		/**
		 *
		 */
		public double d2;

		/**
		 *
		 *
		 * @param coord
		 * @param value
		 * @param d2
		 */
		public WB_KDEntryInteger(final T coord, final int value, final double d2) {
			this.coord = coord;
			this.value = value;
			this.d2 = d2;
		}
	}

	/**
	 *
	 *
	 * @param <T>
	 */
	@SuppressWarnings("hiding")
	private class WB_KDNodeInteger<T extends WB_Coord> {

		/**
		 *
		 */
		private WB_AABB2D _limits;

		/**
		 *
		 */
		private WB_KDNodeInteger<T> _negative, _positive;

		/**
		 *
		 */
		private final WB_AABB2D _region;

		/**
		 *
		 */
		private WB_KDEntryInteger<T>[] _bin;

		/**
		 *
		 */
		private boolean _isLeaf;

		/**
		 *
		 */
		private int _binSize;

		/**
		 *
		 */
		private int _discriminator;

		/**
		 *
		 */
		private double _sliceValue;

		/**
		 *
		 */
		private int _id;

		/**
		 *
		 */
		@SuppressWarnings("unchecked")
		private WB_KDNodeInteger() {
			_bin = new WB_KDEntryInteger[_maximumBinSize];
			_negative = _positive = null;
			_limits = null;
			_isLeaf = true;
			_binSize = 0;
			final double[] min = new double[_dim];
			Arrays.fill(min, Double.NEGATIVE_INFINITY);
			final double[] max = new double[_dim];
			Arrays.fill(max, Double.POSITIVE_INFINITY);
			_region = new WB_AABB2D(min, max);
			_id = 0;
		}

		/**
		 *
		 *
		 * @param leafs
		 */
		private void addLeafBounds(final List<WB_AABB2D> leafs) {
			if (_isLeaf) {
				final WB_AABB2D box = _limits.get();
				box.setId(_id);
				leafs.add(box);
			} else {
				_positive.addLeafBounds(leafs);
				_negative.addLeafBounds(leafs);
			}
		}

		/**
		 *
		 *
		 * @param leafs
		 */
		private void addLeafRegion(final List<WB_AABB2D> leafs) {
			if (_isLeaf) {
				final WB_AABB2D box = _region.get();
				if (box.getMinX() == Double.NEGATIVE_INFINITY) {
					box._min[0] = _limits._min[0];
				}
				if (box.getMinY() == Double.NEGATIVE_INFINITY) {
					box._min[1] = _limits._min[1];
				}

				if (box.getMaxX() == Double.POSITIVE_INFINITY) {
					box._max[0] = _limits._max[0];
				}
				if (box.getMaxY() == Double.POSITIVE_INFINITY) {
					box._max[1] = _limits._max[1];
				}
				box.setId(_id);
				leafs.add(box);
			} else {
				_positive.addLeafRegion(leafs);
				_negative.addLeafRegion(leafs);
			}
		}

		/**
		 *
		 *
		 * @param leafs
		 * @param level
		 */
		private void addBox(final List<WB_AABB2D> leafs, final int level) {
			final WB_AABB2D box = _limits.get();
			box.setId(_id);
			leafs.add(box);
			if (!_isLeaf) {
				_positive.addBox(leafs, level + 1);
				_negative.addBox(leafs, level + 1);
			}
		}

		/**
		 *
		 *
		 * @param leafs
		 * @param level
		 */
		private void addRegion(final List<WB_AABB2D> leafs, final int level) {
			final WB_AABB2D box = _region.get();
			if (box.getMinX() == Double.NEGATIVE_INFINITY) {
				box._min[0] = _limits._min[0];
			}
			if (box.getMinY() == Double.NEGATIVE_INFINITY) {
				box._min[1] = _limits._min[1];
			}

			if (box.getMaxX() == Double.POSITIVE_INFINITY) {
				box._max[0] = _limits._max[0];
			}
			if (box.getMaxY() == Double.POSITIVE_INFINITY) {
				box._max[1] = _limits._max[1];
			}
			box.setId(_id);
			leafs.add(box);
			if (!_isLeaf) {
				_positive.addRegion(leafs, level + 1);
				_negative.addRegion(leafs, level + 1);
			}
		}

		/**
		 *
		 *
		 * @param entry
		 * @return
		 */
		private int add(final WB_KDEntryInteger<T> entry) {
			if (_isLeaf) {
				return addInLeaf(entry);
			} else {
				extendBounds(entry.coord);
				if (entry.coord.getd(_discriminator) > _sliceValue) {
					return _positive.add(entry);
				} else {
					return _negative.add(entry);
				}
			}
		}

		/**
		 *
		 *
		 * @param entry
		 * @return
		 */
		private int addInLeaf(final WB_KDEntryInteger<T> entry) {
			final int lookup = lookup(entry.coord);
			if (lookup == -1) {
				extendBounds(entry.coord);
				if (_binSize + 1 > _maximumBinSize) {
					addLevel();
					add(entry);
					return -1;
				}
				_bin[_binSize] = entry;
				_binSize++;
				return -1;
			}
			return lookup;
		}

		/**
		 *
		 *
		 * @param point
		 * @return
		 */
		private int lookup(final WB_Coord point) {
			for (int i = 0; i < _binSize; i++) {
				if (WB_Epsilon.isZeroSq(WB_CoordOp2D.getSqDistance2D(point, _bin[i].coord))) {
					return _bin[i].value;
				}
			}
			return -1;
		}

		/**
		 *
		 *
		 * @param heap
		 * @param data
		 */
		private void findNearest(final QueryResultInteger<T> heap, final WB_Coord data) {
			if (_binSize == 0) {
				return;
			}
			if (_isLeaf) {
				for (int i = 0; i < _binSize; i++) {
					final double dist = WB_CoordOp2D.getSqDistance2D(_bin[i].coord, data);
					heap.tryToAdd(dist, _bin[i]);
				}
			} else {
				if (data.getd(_discriminator) > _sliceValue) {
					_positive.findNearest(heap, data);
					if (_negative._binSize == 0) {
						return;
					}
					if (heap.size < heap.capacity
							|| _negative._limits.getDistanceSquare(data) < heap.getDistanceSquare(heap.size - 1)) {
						_negative.findNearest(heap, data);
					}
				} else {
					_negative.findNearest(heap, data);
					if (_positive._binSize == 0) {
						return;
					}
					if (heap.size < heap.capacity
							|| _positive._limits.getDistanceSquare(data) < heap.getDistanceSquare(heap.size - 1)) {
						_positive.findNearest(heap, data);
					}
				}
			}
		}

		/**
		 *
		 *
		 * @param range
		 * @return
		 */
		private WB_KDEntryInteger<T>[] range(final WB_AABB2D range) {
			if (_bin == null) {
				@SuppressWarnings("unchecked")
				WB_KDEntryInteger<T>[] tmp = new WB_KDEntryInteger[0];
				if (_negative._limits.intersects(range)) {
					final WB_KDEntryInteger<T>[] tmpl = _negative.range(range);
					if (0 == tmp.length) {
						tmp = tmpl;
					}
				}
				if (_positive._limits.intersects(range)) {
					final WB_KDEntryInteger<T>[] tmpr = _positive.range(range);
					if (0 == tmp.length) {
						tmp = tmpr;
					} else if (0 < tmpr.length) {
						@SuppressWarnings("unchecked")
						final WB_KDEntryInteger<T>[] tmp2 = new WB_KDEntryInteger[tmp.length + tmpr.length];
						System.arraycopy(tmp, 0, tmp2, 0, tmp.length);
						System.arraycopy(tmpr, 0, tmp2, tmp.length, tmpr.length);
						tmp = tmp2;
					}
				}
				return tmp;
			}
			@SuppressWarnings("unchecked")
			final WB_KDEntryInteger<T>[] tmp = new WB_KDEntryInteger[_binSize];
			int n = 0;
			for (int i = 0; i < _binSize; i++) {
				if (range.contains(_bin[i].coord)) {
					tmp[n++] = _bin[i];
				}
			}
			@SuppressWarnings("unchecked")
			final WB_KDEntryInteger<T>[] tmp2 = new WB_KDEntryInteger[n];
			System.arraycopy(tmp, 0, tmp2, 0, n);
			return tmp2;
		}

		/**
		 *
		 *
		 * @param center
		 * @param r2
		 * @return
		 */
		private WB_KDEntryInteger<T>[] range(final WB_Coord center, final double r2) {
			if (_bin == null) {
				@SuppressWarnings("unchecked")
				WB_KDEntryInteger<T>[] tmp = new WB_KDEntryInteger[0];
				if (_negative._limits.getDistanceSquare(center) <= r2) {
					final WB_KDEntryInteger<T>[] tmpl = _negative.range(center, r2);
					if (tmp.length == 0) {
						tmp = tmpl;
					}
				}
				if (_positive._limits.getDistanceSquare(center) <= r2) {
					final WB_KDEntryInteger<T>[] tmpr = _positive.range(center, r2);
					if (tmp.length == 0) {
						tmp = tmpr;
					} else if (0 < tmpr.length) {
						@SuppressWarnings("unchecked")
						final WB_KDEntryInteger<T>[] tmp2 = new WB_KDEntryInteger[tmp.length + tmpr.length];
						System.arraycopy(tmp, 0, tmp2, 0, tmp.length);
						System.arraycopy(tmpr, 0, tmp2, tmp.length, tmpr.length);
						tmp = tmp2;
					}
				}
				return tmp;
			}
			@SuppressWarnings("unchecked")
			final WB_KDEntryInteger<T>[] tmp = new WB_KDEntryInteger[_binSize];
			int n = 0;
			for (int i = 0; i < _binSize; i++) {
				final double d2 = WB_CoordOp2D.getSqDistance2D(center, _bin[i].coord);
				if (d2 <= r2) {
					_bin[i].d2 = d2;
					tmp[n++] = _bin[i];
				}
			}
			@SuppressWarnings("unchecked")
			final WB_KDEntryInteger<T>[] tmp2 = new WB_KDEntryInteger[n];
			System.arraycopy(tmp, 0, tmp2, 0, n);
			return tmp2;
		}

		/**
		 *
		 *
		 * @param center
		 * @param lower2
		 * @param upper2
		 * @return
		 */
		private WB_KDEntryInteger<T>[] range(final WB_Coord center, final double lower2, final double upper2) {
			if (_bin == null) {
				@SuppressWarnings("unchecked")
				WB_KDEntryInteger<T>[] tmp = new WB_KDEntryInteger[0];
				if (_negative._limits.getDistanceSquare(center) <= upper2) {
					final WB_KDEntryInteger<T>[] tmpl = _negative.range(center, lower2, upper2);
					if (tmp.length == 0) {
						tmp = tmpl;
					}
				}
				if (_positive._limits.getDistanceSquare(center) <= upper2) {
					final WB_KDEntryInteger<T>[] tmpr = _positive.range(center, lower2, upper2);
					if (tmp.length == 0) {
						tmp = tmpr;
					} else if (0 < tmpr.length) {
						@SuppressWarnings("unchecked")
						final WB_KDEntryInteger<T>[] tmp2 = new WB_KDEntryInteger[tmp.length + tmpr.length];
						System.arraycopy(tmp, 0, tmp2, 0, tmp.length);
						System.arraycopy(tmpr, 0, tmp2, tmp.length, tmpr.length);
						tmp = tmp2;
					}
				}
				return tmp;
			}
			@SuppressWarnings("unchecked")
			final WB_KDEntryInteger<T>[] tmp = new WB_KDEntryInteger[_binSize];
			int n = 0;
			for (int i = 0; i < _binSize; i++) {
				final double d2 = WB_CoordOp2D.getSqDistance2D(center, _bin[i].coord);
				if (d2 <= upper2 && d2 >= lower2) {
					_bin[i].d2 = d2;
					tmp[n++] = _bin[i];
				}
			}
			@SuppressWarnings("unchecked")
			final WB_KDEntryInteger<T>[] tmp2 = new WB_KDEntryInteger[n];
			System.arraycopy(tmp, 0, tmp2, 0, n);
			return tmp2;
		}

		/**
		 * Adds the level.
		 */
		private void addLevel() {
			_discriminator = _limits.maxOrdinate();
			_negative = new WB_KDNodeInteger<T>();
			_positive = new WB_KDNodeInteger<T>();
			_negative._id = 2 * _id;
			_positive._id = 2 * _id + 1;
			_sliceValue = (_limits.getMax(_discriminator) + _limits.getMin(_discriminator)) * 0.5;
			for (int i = 0; i < _dim; i++) {
				_negative._region._min[i] = _region._min[i];
				_positive._region._max[i] = _region._max[i];
				if (i == _discriminator) {
					_negative._region._max[i] = _sliceValue;
					_positive._region._min[i] = _sliceValue;
				} else {
					_negative._region._max[i] = _region._max[i];
					_positive._region._min[i] = _region._min[i];
				}
			}
			for (int i = 0; i < _binSize; ++i) {
				if (_bin[i].coord.getd(_discriminator) > _sliceValue) {
					_positive.addInLeaf(_bin[i]);
				} else {
					_negative.addInLeaf(_bin[i]);
				}
			}
			_bin = null;
			_isLeaf = false;
		}

		/**
		 *
		 *
		 * @param coord
		 */
		private void extendBounds(final WB_Coord coord) {
			if (_limits == null) {
				_limits = new WB_AABB2D(coord);
			} else {
				_limits.expandToInclude(coord);
			}
		}
	}

	/**
	 *
	 *
	 * @param <T>
	 * @param <V>
	 */
	@SuppressWarnings("hiding")
	public class QueryResultInteger<T extends WB_Coord> {
		/** The entries. */
		private final WB_KDEntryInteger<T>[] entries;
		/** The dist sqs. */
		private final double[] distSqs;
		/** The capacity. */
		private final int capacity;
		/** The size. */
		private int size;

		/**
		 *
		 *
		 * @param capacity
		 */
		@SuppressWarnings("unchecked")
		protected QueryResultInteger(final int capacity) {
			this.entries = new WB_KDEntryInteger[capacity];
			this.distSqs = new double[capacity];
			this.capacity = capacity;
			this.size = 0;
		}

		/**
		 *
		 *
		 * @param dist
		 * @param entry
		 */
		protected void tryToAdd(final double dist, final WB_KDEntryInteger<T> entry) {
			int i = size;
			for (; i > 0 && distSqs[i - 1] > dist; --i) {
				;
			}
			if (i >= capacity) {
				return;
			}
			if (size < capacity) {
				size++;
			}
			final int j = i + 1;
			System.arraycopy(distSqs, i, distSqs, j, size - j);
			distSqs[i] = dist;
			System.arraycopy(entries, i, entries, j, size - j);
			entry.d2 = dist;
			entries[i] = entry;
		}

		/**
		 * Gets the entry.
		 *
		 * @param i
		 *            the i
		 * @return the entry
		 */
		public WB_KDEntryInteger<T> getEntry(final int i) {
			if (size == 0) {
				return null;
			}
			return entries[i];
		}

		/**
		 * Gets the distance square.
		 *
		 * @param i
		 *            the i
		 * @return the distance square
		 */
		public double getDistanceSquare(final int i) {
			if (size == 0) {
				return Double.NaN;
			}
			return distSqs[i];
		}

		/**
		 * Size.
		 *
		 * @return the int
		 */
		public int size() {
			return size;
		}
	}
}