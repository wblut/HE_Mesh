package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_OctreeInteger {
	protected WB_AABB box;
	protected WB_Coord extent;
	protected WB_Coord min, max;
	protected double minNodeSize = 4.0;
	protected WB_OctreeInteger parent;
	protected WB_OctreeInteger[] nodes;
	protected int numNodes;
	protected List<WB_OctreeIntegerEntry> entries;
	protected double size, hsize;
	protected WB_Coord center;
	private int level = 0;
	private boolean autoPrune = false;

	private WB_OctreeInteger(final WB_OctreeInteger p, final WB_Coord center, final double size) {
		box = new WB_AABB(new WB_Point(center).subSelf(0.5 * size, 0.5 * size, 0.5 * size),
				new WB_Point(center).addSelf(0.5 * size, 0.5 * size, 0.5 * size));
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

	public WB_OctreeInteger(final WB_Coord center, final double size) {
		box = new WB_AABB(new WB_Point(center).subSelf(0.5 * size, 0.5 * size, 0.5 * size),
				new WB_Point(center).addSelf(0.5 * size, 0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	public void addPoint(final WB_Coord p, final int value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new FastList<>();
				}
				entries.add(new WB_OctreeIntegerEntry(p, value));
				return;
			} else {
				if (nodes == null) {
					nodes = new WB_OctreeInteger[8];
				}
				final int octant = getOctant(p);
				if (nodes[octant] == null) {
					final WB_Coord newCenter = WB_Point.add(center,
							new WB_Point((octant & 1) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(octant & 2) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(octant & 4) != 0 ? 0.5 * hsize : -0.5 * hsize));
					nodes[octant] = new WB_OctreeInteger(this, newCenter, hsize);
					numNodes++;
				}
				nodes[octant].addPoint(p, value);
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

	public WB_OctreeInteger getNode(final WB_Coord p) {
		if (box.contains(p)) {
			if (numNodes > 0) {
				final int octant = getOctant(p);
				if (nodes[octant] != null) {
					return nodes[octant].getNode(p);
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

	protected final int getOctant(final WB_Coord p) {
		return (p.xd() >= center.xd() ? 1 : 0) + (p.yd() >= center.yd() ? 2 : 0) + (p.zd() >= center.zd() ? 4 : 0);
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_OctreeInteger getParent() {
		return parent;
	}

	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	public List<WB_OctreeInteger> getNodes() {
		final List<WB_OctreeInteger> result = new FastList<>();
		if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
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
			for (int i = 0; i < 8; i++) {
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
		final WB_OctreeInteger node = getNode(p);
		if (node != null) {
			for (final WB_OctreeIntegerEntry eo : node.entries) {
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
		this.minNodeSize = minNodeSize * 0.5f;
	}

	public void setAutoPrune(final boolean state) {
		autoPrune = state;
	}

	public WB_AABB getBox() {
		return box;
	}

	public List<WB_OctreeIntegerEntry> getEntries() {
		FastList<WB_OctreeIntegerEntry> result = null;
		if (entries != null) {
			result = new FastList<>();
			result.addAll(entries);
		} else if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
				if (nodes[i] != null) {
					final List<WB_OctreeIntegerEntry> childPoints = nodes[i].getEntries();
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

	public List<WB_OctreeIntegerEntry> getEntriesInRange(final WB_AABB AABB) {
		FastList<WB_OctreeIntegerEntry> result = new FastList<>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new FastList<>();
				for (final WB_OctreeIntegerEntry oe : entries) {
					if (AABB.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 8; i++) {
					if (nodes[i] != null) {
						final List<WB_OctreeIntegerEntry> points = nodes[i].getEntriesInRange(AABB);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_OctreeIntegerEntry> getEntriesInRange(final WB_Sphere sphere) {
		final FastList<WB_OctreeIntegerEntry> result = new FastList<>();
		if (box.intersects(sphere)) {
			if (entries != null) {
				for (final WB_OctreeIntegerEntry oe : entries) {
					if (sphere.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 8; i++) {
					if (nodes[i] != null) {
						final List<WB_OctreeIntegerEntry> points = nodes[i].getEntriesInRange(sphere);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result;
	}

	public List<WB_OctreeIntegerEntry> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Sphere(center, radius));
	}

	public List<WB_Coord> getPoints() {
		final WB_CoordList result = new WB_CoordList();
		if (entries != null) {
			for (final WB_OctreeIntegerEntry oe : entries) {
				result.add(oe.coord);
			}
		} else if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
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

	public List<WB_Coord> getPointsInRange(final WB_AABB AABB) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_OctreeIntegerEntry eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Sphere sphere) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_OctreeIntegerEntry eo : getEntriesInRange(sphere)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Sphere(center, radius));
	}

	public int[] getValues() {
		final List<WB_OctreeIntegerEntry> allEntries = getEntries();
		final int[] result = new int[allEntries.size()];
		int i = 0;
		for (final WB_OctreeIntegerEntry eo : allEntries) {
			result[i++] = eo.value;
		}
		return result;
	}

	public int[] getValuesInRange(final WB_AABB AABB) {
		final List<WB_OctreeIntegerEntry> entriesInRange = getEntriesInRange(AABB);
		final int[] result = new int[entriesInRange.size()];
		int i = 0;
		for (final WB_OctreeIntegerEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public int[] getValuesInRange(final WB_Sphere sphere) {
		final List<WB_OctreeIntegerEntry> entriesInRange = getEntriesInRange(sphere);
		final int[] result = new int[entriesInRange.size()];
		int i = 0;
		for (final WB_OctreeIntegerEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public int[] getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Sphere(center, radius));
	}

	public static class WB_OctreeIntegerEntry {
		public WB_Coord coord;
		public int value;

		public WB_OctreeIntegerEntry(final WB_Coord coord, final int value) {
			this.coord = coord;
			this.value = value;
		}
	}

	public static void main(final String[] args) {
		WB_OctreeInteger tree;
		final WB_AABB AABB = new WB_AABB(0, 0, 0, 100, 100, 100);
		final ArrayList<WB_Point> points = new ArrayList<>();
		final WB_RandomFactory rp = new WB_RandomBox().setSize(800, 800, 800).setOffset(400, 400, 400);
		for (int i = 0; i < 1000; i++) {
			points.add(rp.nextPoint());
		}
		tree = new WB_OctreeInteger(new WB_Point(400, 400, 400), 800);
		tree.addAll(points);
		final List<WB_Coord> inRange = tree.getPointsInRange(AABB);
		System.out.println(inRange.size());
	}
}
