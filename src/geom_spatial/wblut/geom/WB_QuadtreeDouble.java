package wblut.geom;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_QuadtreeDouble {
	protected WB_AABB2D box;
	protected WB_Coord extent;
	protected WB_Coord min, max;
	protected double minNodeSize = 4.0;
	protected WB_QuadtreeDouble parent;
	protected WB_QuadtreeDouble[] nodes;
	protected int numNodes;
	protected List<WB_QuadtreeDoubleEntry> entries;
	protected double size, hsize;
	protected WB_Coord center;
	private int level = 0;
	private boolean autoPrune = false;

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

	public WB_QuadtreeDouble(final WB_Coord center, final double size) {
		box = new WB_AABB2D(new WB_Point(center.xd(), center.yd()).subSelf(0.5 * size, 0.5 * size),
				new WB_Point(center.xd(), center.yd()).addSelf(0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	public void addPoint(final WB_Coord p, final double value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new FastList<>();
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

	public void addAll(final Collection<? extends WB_Coord> points) {
		int i = 0;
		for (final WB_Coord point : points) {
			addPoint(point, i++);
		}
	}

	public void clear() {
		numNodes = 0;
		nodes = null;
		entries = null;
	}

	public int getLevel() {
		return level;
	}

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

	public double getMinNodeSize() {
		return minNodeSize;
	}

	public int getNumNodes() {
		return numNodes;
	}

	protected final int getQuadrant(final WB_Coord p) {
		return (p.xd() >= center.xd() ? 1 : 0) + (p.yd() >= center.yd() ? 2 : 0);
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_QuadtreeDouble getParent() {
		return parent;
	}

	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	public List<WB_QuadtreeDouble> getNodes() {
		final List<WB_QuadtreeDouble> result = new FastList<>();
		if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					result.add(nodes[i]);
				}
			}
		}
		return result;
	}

	public double getSize() {
		return size;
	}

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

	public void removeAll(final Collection<WB_Coord> points) {
		for (final WB_Coord p : points) {
			remove(p);
		}
	}

	public void setMinNodeSize(final double minNodeSize) {
		this.minNodeSize = minNodeSize * 0.5;
	}

	public void setAutoPrune(final boolean state) {
		autoPrune = state;
	}

	public WB_AABB2D getBox() {
		return box;
	}

	public List<WB_QuadtreeDoubleEntry> getEntries() {
		FastList<WB_QuadtreeDoubleEntry> result = null;
		if (entries != null) {
			result = new FastList<>();
			result.addAll(entries);
		} else if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					final List<WB_QuadtreeDoubleEntry> childPoints = nodes[i].getEntries();
					if (childPoints != null) {
						if (result == null) {
							result = new FastList<>();
						}
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_QuadtreeDoubleEntry> getEntriesInRange(final WB_AABB2D AABB) {
		FastList<WB_QuadtreeDoubleEntry> result = new FastList<>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new FastList<>();
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

	public List<WB_QuadtreeDoubleEntry> getEntriesInRange(final WB_Circle circle) {
		final FastList<WB_QuadtreeDoubleEntry> result = new FastList<>();
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

	public List<WB_QuadtreeDoubleEntry> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Circle(center, radius));
	}

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

	public List<WB_Coord> getPointsInRange(final WB_AABB2D AABB) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_QuadtreeDoubleEntry eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Circle circle) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_QuadtreeDoubleEntry eo : getEntriesInRange(circle)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Circle(center, radius));
	}

	public double[] getValues() {
		final List<WB_QuadtreeDoubleEntry> allEntries = getEntries();
		final double[] result = new double[allEntries.size()];
		int i = 0;
		for (final WB_QuadtreeDoubleEntry eo : allEntries) {
			result[i++] = eo.value;
		}
		return result;
	}

	public double[] getValuesInRange(final WB_AABB2D AABB) {
		final List<WB_QuadtreeDoubleEntry> entriesInRange = getEntriesInRange(AABB);
		final double[] result = new double[entriesInRange.size()];
		int i = 0;
		for (final WB_QuadtreeDoubleEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public double[] getValuesInRange(final WB_Circle circle) {
		final List<WB_QuadtreeDoubleEntry> entriesInRange = getEntriesInRange(circle);
		final double[] result = new double[entriesInRange.size()];
		int i = 0;
		for (final WB_QuadtreeDoubleEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public double[] getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Circle(center, radius));
	}

	public static class WB_QuadtreeDoubleEntry {
		public WB_Coord coord;
		public double value;

		public WB_QuadtreeDoubleEntry(final WB_Coord coord, final double value) {
			this.coord = coord;
			this.value = value;
		}
	}
}
