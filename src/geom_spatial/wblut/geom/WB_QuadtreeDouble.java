package wblut.geom;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class WB_QuadtreeDouble {
	/**  */
	protected WB_AABB2D box;
	/**  */
	protected WB_Coord extent;
	/**  */
	protected WB_Coord min, max;
	/**  */
	protected double minNodeSize = 4.0;
	/**  */
	protected WB_QuadtreeDouble parent;
	/**  */
	protected WB_QuadtreeDouble[] nodes;
	/**  */
	protected int numNodes;
	/**  */
	protected List<WB_QuadtreeDoubleEntry> entries;
	/**  */
	protected double size, hsize;
	/**  */
	protected WB_Coord center;
	/**  */
	private int level = 0;
	/**  */
	private boolean autoPrune = false;

	/**
	 *
	 *
	 * @param p
	 * @param center
	 * @param size
	 */
	private WB_QuadtreeDouble(final WB_QuadtreeDouble p, final WB_Coord center, final double size) {
		box = new WB_AABB2D(new WB_Point(center.xd(), center.yd()).subSelf(0.5 * size, 0.5 * size),
				new WB_Point(center.xd(), center.yd()).addSelf(0.5 * size, 0.5 * size));
		this.parent = p;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
		if (parent != null) {
			level = parent.level + 1;
			minNodeSize = parent.minNodeSize;
		}
	}

	/**
	 *
	 *
	 * @param center
	 * @param size
	 */
	public WB_QuadtreeDouble(final WB_Coord center, final double size) {
		box = new WB_AABB2D(new WB_Point(center.xd(), center.yd()).subSelf(0.5 * size, 0.5 * size),
				new WB_Point(center.xd(), center.yd()).addSelf(0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	/**
	 *
	 *
	 * @param p
	 * @param value
	 */
	public void addPoint(final WB_Coord p, final double value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new WB_List<>();
				}
				entries.add(new WB_QuadtreeDoubleEntry(p, value));
				return;
			} else {
				if (nodes == null) {
					nodes = new WB_QuadtreeDouble[4];
				}
				final int quadrant = getQuadrant(p);
				if (nodes[quadrant] == null) {
					final WB_Coord newCenter = WB_Point.add(center,
							new WB_Point((quadrant & 1) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(quadrant & 2) != 0 ? 0.5 * hsize : -0.5 * hsize));
					nodes[quadrant] = new WB_QuadtreeDouble(this, newCenter, hsize);
					numNodes++;
				}
				nodes[quadrant].addPoint(p, value);
				return;
			}
		}
		return;
	}

	/**
	 *
	 *
	 * @param points
	 */
	public void addAll(final Collection<? extends WB_Coord> points) {
		int i = 0;
		for (final WB_Coord point : points) {
			addPoint(point, i++);
		}
	}

	/**
	 *
	 */
	public void clear() {
		numNodes = 0;
		nodes = null;
		entries = null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_QuadtreeDouble getNode(final WB_Coord p) {
		if (box.contains(p)) {
			if (numNodes > 0) {
				final int quadrant = getQuadrant(p);
				if (nodes[quadrant] != null) {
					return nodes[quadrant].getNode(p);
				}
			} else if (entries != null) {
				return this;
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getMinNodeSize() {
		return minNodeSize;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	protected final int getQuadrant(final WB_Coord p) {
		return (p.xd() >= center.xd() ? 1 : 0) + (p.yd() >= center.yd() ? 2 : 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getCenter() {
		return center;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_QuadtreeDouble getParent() {
		return parent;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_QuadtreeDouble> getNodes() {
		final List<WB_QuadtreeDouble> result = new WB_List<>();
		if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					result.add(nodes[i]);
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getSize() {
		return size;
	}

	/**
	 *
	 */
	private void prune() {
		if (entries != null && entries.size() == 0) {
			entries = null;
		}
		if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null && nodes[i].entries == null) {
					nodes[i] = null;
				}
			}
		}
		if (parent != null) {
			parent.prune();
		}
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public boolean remove(final WB_Coord p) {
		boolean found = false;
		final WB_QuadtreeDouble node = getNode(p);
		if (node != null) {
			for (final WB_QuadtreeDoubleEntry eo : node.entries) {
				if (eo.coord.equals(p)) {
					if (node.entries.remove(eo)) {
						found = true;
						if (autoPrune && node.entries.size() == 0) {
							node.prune();
						}
					}
				}
			}
		}
		return found;
	}

	/**
	 *
	 *
	 * @param points
	 */
	public void removeAll(final Collection<WB_Coord> points) {
		for (final WB_Coord p : points) {
			remove(p);
		}
	}

	/**
	 *
	 *
	 * @param minNodeSize
	 */
	public void setMinNodeSize(final double minNodeSize) {
		this.minNodeSize = minNodeSize * 0.5;
	}

	/**
	 *
	 *
	 * @param state
	 */
	public void setAutoPrune(final boolean state) {
		autoPrune = state;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_AABB2D getBox() {
		return box;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_QuadtreeDoubleEntry> getEntries() {
		WB_List<WB_QuadtreeDoubleEntry> result = null;
		if (entries != null) {
			result = new WB_List<>();
			result.addAll(entries);
		} else if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					final List<WB_QuadtreeDoubleEntry> childPoints = nodes[i].getEntries();
					if (childPoints != null) {
						if (result == null) {
							result = new WB_List<>();
						}
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public List<WB_QuadtreeDoubleEntry> getEntriesInRange(final WB_AABB2D AABB) {
		WB_List<WB_QuadtreeDoubleEntry> result = new WB_List<>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new WB_List<>();
				for (final WB_QuadtreeDoubleEntry oe : entries) {
					if (AABB.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 4; i++) {
					if (nodes[i] != null) {
						final List<WB_QuadtreeDoubleEntry> points = nodes[i].getEntriesInRange(AABB);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param circle
	 * @return
	 */
	public List<WB_QuadtreeDoubleEntry> getEntriesInRange(final WB_Circle circle) {
		final WB_List<WB_QuadtreeDoubleEntry> result = new WB_List<>();
		if (box.intersects(circle)) {
			if (entries != null) {
				for (final WB_QuadtreeDoubleEntry oe : entries) {
					if (circle.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 4; i++) {
					if (nodes[i] != null) {
						final List<WB_QuadtreeDoubleEntry> points = nodes[i].getEntriesInRange(circle);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public List<WB_QuadtreeDoubleEntry> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Circle(center, radius));
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Coord> getPoints() {
		final WB_CoordList result = new WB_CoordList();
		if (entries != null) {
			for (final WB_QuadtreeDoubleEntry oe : entries) {
				result.add(oe.coord);
			}
		} else if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					final List<WB_Coord> childPoints = nodes[i].getPoints();
					if (childPoints != null) {
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public List<WB_Coord> getPointsInRange(final WB_AABB2D AABB) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_QuadtreeDoubleEntry eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param circle
	 * @return
	 */
	public List<WB_Coord> getPointsInRange(final WB_Circle circle) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_QuadtreeDoubleEntry eo : getEntriesInRange(circle)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Circle(center, radius));
	}

	/**
	 *
	 *
	 * @return
	 */
	public double[] getValues() {
		final List<WB_QuadtreeDoubleEntry> allEntries = getEntries();
		final double[] result = new double[allEntries.size()];
		int i = 0;
		for (final WB_QuadtreeDoubleEntry eo : allEntries) {
			result[i++] = eo.value;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public double[] getValuesInRange(final WB_AABB2D AABB) {
		final List<WB_QuadtreeDoubleEntry> entriesInRange = getEntriesInRange(AABB);
		final double[] result = new double[entriesInRange.size()];
		int i = 0;
		for (final WB_QuadtreeDoubleEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param circle
	 * @return
	 */
	public double[] getValuesInRange(final WB_Circle circle) {
		final List<WB_QuadtreeDoubleEntry> entriesInRange = getEntriesInRange(circle);
		final double[] result = new double[entriesInRange.size()];
		int i = 0;
		for (final WB_QuadtreeDoubleEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public double[] getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Circle(center, radius));
	}

	/**
	 *
	 */
	public static class WB_QuadtreeDoubleEntry {
		/**  */
		public WB_Coord coord;
		/**  */
		public double value;

		/**
		 *
		 *
		 * @param coord
		 * @param value
		 */
		public WB_QuadtreeDoubleEntry(final WB_Coord coord, final double value) {
			this.coord = coord;
			this.value = value;
		}
	}
}
